// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.rmi.RemoteException;
import jp.ne.sonet.medipro.mr.*;
import java.sql.*;
import java.util.*;

/**
 * <P>This servlet that handles any Throwable type (Error/Exception) that thrown by
 * the application.
 *
 * @author Damon Lok
 * @version $Id: MRKUN_ErrorHandlerCtlr.java,v 1.1.2.20 2001/10/18 07:23:05 damon Exp $
 * @since   JDK1.3 
 */
public class MRKUN_ErrorHandlerCtlr extends HttpServlet
{
  private final String SESSION_ERROR_MESSAGE   = "sessionErrorMessage";
  private final String DR_LOGIN_FAILED_MESSAGE = "drLoginFailedMessage";
  private final String ERROR_DESCRIPTION       = "ErrorDescription";
  private final String ERROR_INSTANCE          = "errorInstance"; 
  private final String PAGE_DR                 = "pageDR";
  private final String PAGE_MR                 = "pageMR"; 
  private final String REDIRECT_URL            = "redirectURL";
  private final String SUB_SERVLET_NAME        = "subServletName";
  private final String DR_SITE                 = "DRSite";
  private final String MR_SITE                 = "MRSite";
  
  /**
   * Initialize servlet data from servlet engine configuration
   * @param pConfig Servlet engine configuration information
   * @exception ServletException
   */
  public void init (ServletConfig pConfig) throws ServletException
  {
    super.init (pConfig);
  }
  
  /**
   * Calls doAction() in the subclass.
   * Catches any exceptions not caught in the subclass.
   *
   * @param pReq              HttpServletRequest
   * @param pRes              HttpServletResponse
   * @exception               java.io.IOException
   *                          if there is an IO error
   */
  public void service(HttpServletRequest pReq, HttpServletResponse pRes)
  {
    this.doAction(pReq, pRes);
  }
  
  /**
   * <P>This method handles any kind of request that comes to the servlet.
   * It verifies that a valid templateId has been passed in and then
   * forwards on to the JSP page that shows a preview of that template.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest pReq, HttpServletResponse pRes)
  { 
    // Get the current session's user type      
  	Object userType = null;
    
    // Get the throwable error/exception instance and pass it on to handle it
    Throwable throwableError = (Throwable) pReq.getAttribute(ERROR_INSTANCE);
    
    // Unknown user type if session has already invalid
    if (throwableError instanceof SessionFailedException)
      userType = null;
    else
      userType = checkUserType(pReq, pRes); 
           
    this.doHandleError(pReq, pRes, throwableError, userType);
  } 
  
 /**
  * This method will differentiate type of exception or error and handle
  * them respectively.
  * @param Throwable The throwable type exception or error.
  */
  private void doHandleError(HttpServletRequest pReq, HttpServletResponse pRes, 
                             Throwable throwableError, Object userType) 
  {    
    System.out.println("Execution is coming into doHandleError.");
    System.out.println("UserType: " + userType);
    
    if (throwableError == null)
    {
      ApplicationError appError = new ApplicationError("Unknown error/exception has been caught in MRKUN_ErrorHandlerCtlr.");
      if ((userType instanceof MR) || (userType == null))
      {
        pReq.setAttribute(PAGE_MR, userType);
        this.outputErrorMessage(pReq, appError, userType);
        this.forwardToErrorPage(HttpConstant.Mr01_View, pReq, pRes, 
          						appError, userType);
      }
      else
      {
        pReq.setAttribute(PAGE_DR, userType);
        this.outputErrorMessage(pReq, appError, userType);
        this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, 
          						appError, userType);    
      }
    }
    else    
    {
      // For error that when doctor login process failed with 
      // unknown fatal error
      if (throwableError instanceof DoctorLoginProcessFailedException)
      {
        System.out.println("Execution is in doctorLoginProcessFailed exception.");
      
        // Pass on the error description to the respective error jsp
        pReq.setAttribute(DR_LOGIN_FAILED_MESSAGE, SystemMessages.get("dr_login_not_id"));
        pReq.setAttribute(PAGE_DR, null);
        this.outputErrorMessage(pReq, throwableError, userType);
        this.forwardToErrorPage(HttpConstant.Dr12_1_View, pReq, pRes, throwableError, userType);    
      }    
      // For error that user will see a more specific and sensible error message
      // on the error page
      else if (throwableError instanceof UserDetailException)
      {
        System.out.println("Execution is in userDetail exception.");
      
        // Pass on the error description to the respective error jsp
        pReq.setAttribute(ERROR_DESCRIPTION, throwableError.getMessage());
        if ((userType instanceof MR) || (userType == null))
        {
          pReq.setAttribute(PAGE_MR, userType);
          this.outputErrorMessage(pReq, throwableError, userType);
          this.forwardToErrorPage(HttpConstant.Mr01_View, pReq, pRes, throwableError, userType);
        }
        else
        {
          pReq.setAttribute(PAGE_DR, userType);
          this.outputErrorMessage(pReq, throwableError, userType);
          this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, throwableError, userType);    
        }
      }
      // For session timeout, invalid or any session related error, user will
      // be forward to the login page and display with a session error message 						  
      else if (throwableError instanceof SessionFailedException)
      {
        try
        {
      	  // Determine which part of the site the redirect should go to
      	  String siteArea = MR_SITE;
      	  String subServletName = (String) pReq.getAttribute(SUB_SERVLET_NAME);
          if ((subServletName != null) && (!subServletName.equals("")))
          {
            if(subServletName.equals("PointInfoCtlr"))
              siteArea = DR_SITE;
            else if(subServletName.startsWith("DR_"))
              siteArea = DR_SITE;
            else
              siteArea = MR_SITE;  
          }
        
      	  System.out.println("Execution is in sessionFailed exception.");
        
          // Pass on the error description to the respective error jsp         
          pReq.setAttribute(SESSION_ERROR_MESSAGE, SystemMessages.get("system_sessionTimeout"));
          System.out.println("subServletName :" + subServletName);
          if (siteArea.equals(MR_SITE))
          {
          	this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Mr01_View, pReq, pRes, throwableError, userType);          
          }
          else
          {
            pReq.setAttribute(PAGE_DR, null);
            this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, throwableError, userType);
          }
        }
        catch (ApplicationError errApp)
        {
      	  System.out.println("Execution is in system.message.");
          pReq.setAttribute(ERROR_DESCRIPTION, HttpConstant.SYSTEM_NOT_AVAILABLE);
          if ((userType instanceof MR) || (userType == null))
          {
          	this.outputErrorMessage(pReq, errApp, userType);
            this.forwardToErrorPage(HttpConstant.Mr01_View, pReq, pRes, errApp, userType); 
          }
          else
          {
            this.outputErrorMessage(pReq, errApp, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, errApp, userType);    
          }
        }        
      }
      // For ApplicationError, user will be forward to the error page with the
      // URL link to choose to go back. 						  
      else if (throwableError instanceof ApplicationError)
      { 	    
        try
        {
          ApplicationError appError = (ApplicationError) throwableError;
      	  System.out.println("Execution is in Application error.");
          pReq.setAttribute(ERROR_DESCRIPTION, SystemMessages.get("system_notAvailable"));
 
          if ((userType instanceof MR) || (userType == null))
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Mr09_View, pReq, pRes, throwableError, userType); 
          }
          else
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, throwableError, userType);
          }
        }
        catch (ApplicationError errApp)
        {
      	  System.out.println("Execution is in system.messages error.");
          pReq.setAttribute(ERROR_DESCRIPTION, HttpConstant.SYSTEM_NOT_AVAILABLE);
          if ((userType instanceof MR) || (userType == null))
          {  
            pReq.setAttribute(PAGE_MR, userType); 
            this.outputErrorMessage(pReq, errApp, userType);
            this.forwardToErrorPage(HttpConstant.Mr09_View, pReq, pRes, errApp, userType); 
          }
          else
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, errApp, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, errApp, userType);    
          }
        }        
      }
      // For runtime application error/exception that happens in the master servlet,
      // the state object is being retrieved and handled 
      else if (throwableError instanceof StateAvailableError)
      {
        StateAvailableError appStateError = (StateAvailableError) throwableError; 
        String errorMessage = appStateError.getMessage();
      
        // Get a reference of the state instance being set inside the exception
        State state = appStateError.getState();      
      
        // Harry, please fill in the rest of the related logic to 
        // handle the exception.   Damon 9/5/2001
      
      
        /** Uncomment this if they are useful to you, otherwise delete them please
        //
        //pReq.setAttribute(ERROR_DESCRIPTION, throwableError.getMessage()); 
        //if((userType instanceof MR) || (userType == null))
        //{
        //  pReq.setAttribute(PAGE_MR, userType);
        //  this.forwardToErrorPage(HttpConstant.Mr09_View, pReq, pRes, throwableError, userType); 
        //}
        //else
        //{
        //  pReq.setAttribute(PAGE_DR, userType); 
        //  this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, throwableError, uesrType);  
        //}
        */
      }
      // For all those unhandlable runtime error or exception, 
      // user will be forwarded to the respective error page and
      // display with a general system not available message
      else if ((throwableError instanceof Error) ||
               (throwableError instanceof Exception))
      {    	  
        // Pass on the error description to the respective error jsp  	    
        try
        {
        	System.out.println("Execution is in general exception group.");
          pReq.setAttribute(ERROR_DESCRIPTION, SystemMessages.get("system_notAvailable"));
          if ((userType instanceof MR) || (userType == null))
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Mr09_View, pReq, pRes, throwableError, userType); 
          }
          else
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, throwableError, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, throwableError, userType);
          }
        }
        catch (ApplicationError errApp)
        {
      	  System.out.println("Execution is in system.messages error.");
          pReq.setAttribute(ERROR_DESCRIPTION, HttpConstant.SYSTEM_NOT_AVAILABLE);
          if ((userType instanceof MR) || (userType == null))
          {  
            pReq.setAttribute(PAGE_MR, userType);
            this.outputErrorMessage(pReq, errApp, userType); 
            this.forwardToErrorPage(HttpConstant.Mr09_View, pReq, pRes, errApp, userType); 
          }
          else
          {
            pReq.setAttribute(PAGE_DR, userType);
            this.outputErrorMessage(pReq, errApp, userType);
            this.forwardToErrorPage(HttpConstant.Dr12_View, pReq, pRes, errApp, userType);    
          }
        }    
      }
    }  
  }
  
  
  /**
   * Forward request/response objects to specified JSP (template)
   * @param pTemplate         URL for JSP template
   * @param pReq              HttpServletRequest
   * @param pRes              HttpServletResponse
   */
  public void forwardToErrorPage(String pTemplate, HttpServletRequest pReq, 
                                 HttpServletResponse pRes, Throwable error, 
                                 Object userType)
  { 
    try
    { 
      ServletContext context = getServletConfig().getServletContext();     
      context.log("MRKUN - Forwarding to " + pTemplate);
	  context.getRequestDispatcher(pTemplate).forward(pReq, pRes);
	}
    catch(ServletException excSE)
    {           
      System.out.println("!!Unhandlable error ocurrs.  ServletException error has been caught in MRKUN_ErrorHandlerCtlr.forwardToErrorPage.");
    } 
    catch(IOException excIO)
    {
      System.out.println("!!Unhandlable error ocurrs.  IOException error has been caught in MRKUN_ErrorHandlerCtlr.forwardToErrorPage.");
    }
  }
  
 /**
  * This will display and the error/exception to the output stream as 
  * well as invoke method to email the error message well as.
  * @param error The Throwable error/exception object.
  * @param the userType object
  */
  private void outputErrorMessage(HttpServletRequest pReq, Throwable error, 
                                  Object userType)  
  {
    // Display once the error to the standard out     
    error.printStackTrace();
      
    // Wrap around the error and send to a email  
    StringWriter s = new StringWriter();
    PrintWriter p = new PrintWriter(s);
    error.printStackTrace(p);      
    String userBrowser = pReq.getHeader("User-Agent");
    String host = pReq.getHeader("Host");
    // String referer = pReq.getHeader("Referer");
    this.emailExceptionReport(s.toString(), userType, userBrowser, host);  
  }
  
 /**
  * Get the session object from the SessionManager and check current 
  * session's user type.
  * @param request The request with the current session in it.
  */
  protected Object checkUserType(HttpServletRequest pReq, HttpServletResponse pRes) 
  { 
    Object sessionUser = null;
    
    // Determine who is currently in the session
    SessionManager mgr = new SessionManager(pReq, new Boolean(false));
   
    // Retrieve the User type instance
    if (mgr == null)
    {  
      SessionFailedException excSF = new SessionFailedException();
      this.doHandleError(pReq, pRes, excSF, null);    
    }
    else
    {
      try
      {
      	// Differentiate whether is DR or MR in session
        Object sessionObject = mgr.getUserItem();    
        if (sessionObject instanceof MR)
          sessionUser = (MR) sessionObject;  
        if (sessionObject instanceof DR)
          sessionUser = (DR) sessionObject;
      }
      catch(SessionFailedException excSF)
      {
        this.doHandleError(pReq, pRes, excSF, sessionUser);
      }
    }
    
    return sessionUser;
  }
  
 /**
  * This will send the exception as a email to debug@so-netm3.co.jp. 
  * @param exceptionReport The exception.
  */  
  private void emailExceptionReport(String exceptionReport, Object userType, 
                                    String userBrowser, String host)
  {
    String userId = "";
  	Connection conn = null;
    ConstantMasterTableManager constantmastertablemanager = null;
    ConstantMasterTable constantmaster = null;
    
    if (userType instanceof MR)
    {
      MR mr = (MR) userType;
      userId = mr.getMrId();
    }
    else if (userType instanceof DR)
    {
      DR dr = (DR) userType;
      userId = dr.getDrId();    
    }
    else
      userId = "Unknown";
      
    if((userBrowser == null)||(userBrowser.equals("")))
      userBrowser = "Unknown";  
    
    if((host == null)||(host.equals("")))
      host = "Unknown";

    //if((referer == null)||(referer.equals("")))
    //  referer = "Unknown";
    
    try
    {
      Driver myDriver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
      Properties connProps = new Properties();
      connProps.setProperty("user", dbUtilConstant.JDBC_USER);
      connProps.setProperty("password", dbUtilConstant.JDBC_PASS);
      conn = myDriver.connect(dbUtilConstant.JDBC_LOOKUP, connProps);
      constantmastertablemanager = new ConstantMasterTableManager(conn);
      constantmaster = constantmastertablemanager.getConstantMasterTable("SMTPSRV");
   	}
    catch(ClassNotFoundException cnfExc)
    {
      throw new ApplicationError("In emailExceptionReport: ClassNotFoundException", cnfExc);
    }
    catch(InstantiationException iExc)
    {
      throw new ApplicationError("In emailExceptionReport: InstantiationException", iExc);
    }
    catch(IllegalAccessException iaExc)
    {
      throw new ApplicationError("In emailExceptionReport: IllegalAccessException", iaExc);
    }
    catch(SQLException sqlExc)
    {
      throw new ApplicationError("In emailExceptionReport: SQLException", sqlExc);
    }
    finally
    {
      try
      {							   
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
 
    MailUtil mailUtil = new MailUtil(constantmaster.getNaiyo1());

    mailUtil.setFrom(SystemMessages.get("system_m3DebugEmailAddess"),"Weblogic server in production");
    mailUtil.setSubject("Exception Report from Production Server");

    mailUtil.setTo(SystemMessages.get("system_m3DebugEmailAddess"),"Dear M3 Development Team");      
    mailUtil.setText("User ID: " + userId +
    				 "\nHost: " + host + 
                    // "\nReferer: " + referer +
                     "\nBrowser: " + userBrowser +
                     "\nException content: \n" + exceptionReport);
    mailUtil.send();
  }
} 