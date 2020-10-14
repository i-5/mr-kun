package jp.ne.sonet.mrkun.framework.servlet;

import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.ejb.*;
import java.net.ConnectException;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.log.Logger;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * BaseServlet is the superclass for all servlets.
 *
 * @author Damon Lok, Sonet M3
 * @version 	1
 */
public abstract class BaseServlet extends HttpServlet
{
    private final String ERROR_MESSAGE          = "errorMessage"; 
    private final String SESSION_ERROR_MESSAGE  = "sessionErrorMessage";
   
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
	    throws ServletException, IOException
	{
	  ServletContext context = getServletConfig().getServletContext();
	  try
      {
	    this.doAction(pReq, pRes);
	  }
	  // Catch any runtime exceptions
      catch (SessionFailedException excSF)
      {
      	context.log("MRKUN - Session Error ", excSF);
	    try
        {
		  this.handleRuntimeException(pReq, pRes, excSF);
        }
        catch (IOException e)
        {
		  //FIXME: handle exception by throwing our application exception
	    }
	  }
      catch (RuntimeException re)
      {
        context.log("MRKUN - Error ", re);
	    try
        {
		  this.handleRuntimeException(pReq, pRes, re);
	    }
        catch (IOException e)
        {
		  //FIXME: handle exception by throwing our application exception
	    }
	  }
      catch (ApplicationError error)
      {
        context.log("MRKUN - Error ", error);
	    try
        {
		  this.handleError(pReq, pRes, error);
	    }
        catch (IOException e)
        {
		  //FIXME: handle exception by throwing our application exception
	    }
      }
      catch (Error error)
      {
        context.log("MRKUN - Error ", error);
	    try
        {
		  this.handleError(pReq, pRes, error);
	    }
        catch (IOException e)
        {
          //FIXME: handle exception by throwing our application exception
	    }
	  }
	}


	/**
	* This is the main method which is called when a request is received.
	* This method is called for both GET and POST requests.
	* Every subclass must implement this method.
	*
	* @param pReq              HttpServletRequest
	* @param pRes              HttpServletResponse
	* @exception               java.io.IOException
	*                          if there is an IO error
	*/
	public abstract void doAction(HttpServletRequest pReq, HttpServletResponse pRes)
	    throws ServletException, IOException;


	/**
	 * Forward request/response objects to specified JSP (template)
	 * @param pTemplate         URL for JSP template
	 * @param pReq              HttpServletRequest
	 * @param pRes              HttpServletResponse
	 */
	public void forwardToTemplate(String pTemplate, HttpServletRequest pReq, HttpServletResponse pRes)
	{
		ServletContext context = getServletConfig().getServletContext();

	  try{
      context.log("MRKUN - Forwarding to " + pTemplate);
      Logger.log("MRKUN - Forwarding to " + pTemplate, Logger.INFO_LEVEL);
			context.getRequestDispatcher(pTemplate).forward(pReq, pRes);
      context.log("MRKUN - Finished Forwarding to " + pTemplate);
		} catch(ServletException e){
      Logger.log(e);
      context.log("MRKUN - Error forwarding", e);
     	//FIXME: handle exception by throwing our application exception
    } catch(IOException e){
      Logger.log(e);
      context.log("MRKUN - Error forwarding", e);
   		//FIXME: handle exception by throwing our application exception
    }
	}

 /**
  * Get the session object from the SessionManager.
  * @param request The request with the current session in it.
  */
  protected Object checkSession(HttpServletRequest request) 
    throws SessionFailedException
  {
  	ServletContext context = getServletConfig().getServletContext();
    Object sessionObject = new Object();
    
    try
    {      
      //SessionManager mgr = SessionManager.getSessionManager(request, false);
      SessionManager mgr = new SessionManager(request, new Boolean(false));
   
      // Retrieve the MR object
      if (mgr == null)   
         throw new SessionFailedException("Session timeout or invalid. Please login again.");
      else
        sessionObject = mgr.getUserItem();          
    }
    catch(SessionFailedException err)
    {
      context.log("MRKUN - Error ", err);
      throw new SessionFailedException("Session timeout or invalid. Please login again."); 
    }
    catch(IllegalStateException err)
    {
      context.log("MRKUN - Error ", err);
      throw new SessionFailedException("Session timeout or invalid. Please login again."); 
    } 
         
    return sessionObject;
  }
	   
  /**
   * Directs the container to disassociate the session bean from ejbObject.
   *
   * @param      pRemoteIntfHandle          the bean to be removed
   */
  protected void removeBean(BaseSessionEJBRemoteIntf pRemoteIntfHandle)
    throws ApplicationError
  {
    try {
      pRemoteIntfHandle.remove();
    } catch (RemoveException e) {
     	throw new ApplicationError("RemoveException when calling remove() on " + pRemoteIntfHandle.getClass().getName());
    }	catch (RemoteException e) {
     	throw new ApplicationError("RemoteException when calling remove() on the InvestorManagerRemoteIntf" + pRemoteIntfHandle.getClass().getName());
    }
  }
 
  /**
   * Gets a session EJB remote interface given a constant defining the
   * object name. This method will never return null.
   *
   * @param pManagerName constant defining the object type
   *
   * @return a remote interface to a session EJB
   */
/*
  protected BaseSessionEJBRemoteIntf getManager(String pManagerName)
  {
	BaseSessionEJBRemoteIntf remoteInterface = null;

	if (pManagerName == null)
	{
	  // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName()
         			    + ": " + pManagerName + " is null.");
	}

    try
	{
	  //Get the real stuff: remote interface of the bean
	  InitialContext initial = new InitialContext();
      Object objHome = initial.lookup(pManagerName);

	  if (pManagerName.equals(HttpConstant.MESSAGEMANAGER_HOME))
	  {
	    MessageManagerHomeIntf homeInterface = (MessageManagerHomeIntf)
          PortableRemoteObject.narrow(objHome, MessageManagerHomeIntf.class);
	    remoteInterface = homeInterface.create();
	  }

	  if (pManagerName.equals(HttpConstant.MRMANAGER_HOME))
	  {
	    MRManagerHomeIntf homeInterface = (MRManagerHomeIntf)
          PortableRemoteObject.narrow(objHome, MRManagerHomeIntf.class);
	    remoteInterface = homeInterface.create();
	  }
	  if (pManagerName.equals(HttpConstant.DRMANAGER_HOME))
	  {
	    DRManagerHomeIntf homeInterface = (DRManagerHomeIntf)
          PortableRemoteObject.narrow(objHome, DRManagerHomeIntf.class);
	   remoteInterface = homeInterface.create();
	  }
	  if (pManagerName.equals(HttpConstant.REPORTMANAGER_HOME))
	  {
	    ReportManagerHomeIntf homeInterface = (ReportManagerHomeIntf)
          PortableRemoteObject.narrow(objHome, ReportManagerHomeIntf.class);
	   remoteInterface = homeInterface.create();
	  }

	}
    catch (NamingException e)
	{
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Could not resolve the JNDI name "
                    + pManagerName, e);
	}
    catch (CreateException e)
	{
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Could not create "
                    + pManagerName + "HomeIntf.", e);
	}
	catch (RemoteException e)
	{
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Remote network problem occurs (RemoteException.)"
                    , e);
	}

    if (remoteInterface == null)
	{
	  // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName()
       			    + ": Returned " + remoteInterface + " is null.");
    }

	return remoteInterface;
  }
*/

  /**
   * Returns the value of a parameter of a request.
   * The value is a String and might be null
   *
   * @param pParameter Name of the parameter
   * @param pReq Request that should contain the parameter
   *
   * @return value of the parameter(String)
   */
  protected String getNullableStringFromReq(String pParameter, HttpServletRequest pReq)
  {
    String aString;

    if (pReq == null)
      throw new ApplicationError("the Request is null");
    else
      aString = pReq.getParameter(pParameter);

    Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);
    return aString;
  }

  /**
   * Returns the value of a parameter of a request.
   * The value is a String and should not be null
   * If the parameter is not defined in the request, an ApplicationError
   * will be thrown
   *
   * @param pParameter Name of the parameter
   * @param pReq Request that should contain the parameter
   *
   * @return value of the parameter(String)
   */
  protected String getNotNullStringFromReq(String pParameter, HttpServletRequest pReq)
  {
    String aString;

    if (pReq == null)
      throw new ApplicationError("the Request is null");
    else {
      aString = pReq.getParameter(pParameter);
      Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);
      if (aString == null) {
        throw new ApplicationError("Request parameter <"
                                 + pParameter + "> is missing" );
      }
    }
    return aString;
  }

    /**
     * Returns the value of a parameter of a request.
     * The value is an Integer and might be null
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(Integer)
     */
    protected Integer getNullableIntegerFromReq(String pParameter, HttpServletRequest pReq) {
        Integer anInteger = null;
        String aString = null;

        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
                if (aString.length() != 0) {
                    try {
            	        anInteger = new Integer(aString);
                    } catch (NumberFormatException e) {

                        String errorMessage = new String("Request parameter <" + pParameter
                                                                + "> is invalid: value = " + aString);
                        throw new ApplicationError(errorMessage);
                    }
                }
            }
        }

        return anInteger;
    }

    /**
     * Returns the value of a parameter of a request.
     * The value is an Integer and should not be null
     * If the parameter is not defined in the request, an ApplicationError
     * will be thrown
     * If the parameter is not properly defined in the request, an ApplicationError
     * will be thrown
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(String)
     */
    protected Integer getNotNullIntegerFromReq(String pParameter, HttpServletRequest pReq) {
        Integer anInteger = null;
        String aString = null;

        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
                if (aString.length() != 0) {
                    try {
        	            anInteger = new Integer(aString);
                    } catch (NumberFormatException e) {

                        String errorMessage = new String("Request parameter <" + pParameter
                                                                + "> is invalid: value = " + aString);
                        throw new ApplicationError(errorMessage);
                    }
                } else {
                    throw new ApplicationError("Request parameter <"
                                        + pParameter + "> is missing (string is empty)" );
                }
            } else {
                throw new ApplicationError("Request parameter <"
                                    + pParameter + "> is missing" );
            }
        }

        return anInteger;
    }

    /**
     * Returns the value of a parameter of a request.
     * The value is a Double and might be null
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(Double)
     */
    protected Double getNullableDoubleFromReq(String pParameter, HttpServletRequest pReq) {
        Double aDouble = null;
        String aString = null;
        
        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
                if (aString.length() != 0) {
                    try {
            	        aDouble = new Double(aString);
                    } catch (NumberFormatException e) {

                        String errorMessage = new String("Request parameter <" + pParameter 
                                                                + "> is invalid: value = " + aString);
                        throw new ApplicationError(errorMessage);
                    }
                }
            }
        }

        return aDouble;
    }

    /**
     * Returns the value of a parameter of a request.
     * The value is a Double and should not be null
     * If the parameter is not defined in the request, an ApplicationError
     * will be thrown
     * If the parameter is not properly defined in the request, an ApplicationError
     * will be thrown
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(Double)
     */
    protected Double getNotNullDoubleFromReq(String pParameter, HttpServletRequest pReq) {
        Double aDouble = null;
        String aString = null;
    
        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
                if (aString.length() != 0) {
                    try {
        	            aDouble = new Double(aString);
                    } catch (NumberFormatException e) {
            
                        String errorMessage = new String("Request parameter <" + pParameter 
                                                                + "> is invalid: value = " + aString);
                        throw new ApplicationError(errorMessage);
                    }
                } else {
                    throw new ApplicationError("Request parameter <" 
                                        + pParameter + "> is missing (string is empty)" );
                }
            } else {
                throw new ApplicationError("Request parameter <" 
                                    + pParameter + "> is missing" );
            }
        }

        return aDouble;
    }

    /**
     * Returns the value of a parameter of a request.
     * The value is a Boolean and might be null
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(Boolean)
     */
    protected Boolean getNullableBooleanFromReq(String pParameter, HttpServletRequest pReq) {
        Boolean aBoolean = null;
        String aString = null;
        
        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
          	    aBoolean = new Boolean(aString);
            }
        }

        return aBoolean;
    }

    /**
     * Returns the value of a parameter of a request.
     * The value is a Boolean and should not be null
     * If the parameter is not defined in the request, an ApplicationError
     * will be thrown
     * If the parameter is not properly defined in the request, an ApplicationError
     * will be thrown
     *
     * @param pParameter Name of the parameter
     * @param pReq Request that should contain the parameter
     *
     * @return value of the parameter(Boolean)
     */
    protected Boolean getNotNullBooleanFromReq(String pParameter, HttpServletRequest pReq) {
        Boolean aBoolean = null;
        String aString = null;
    
        if (pReq == null)
            throw new ApplicationError("the Request is null");
        else {
            aString = pReq.getParameter(pParameter);
            Logger.log("Parameter <" + pParameter + "> = <" + aString +">", Logger.DEBUG_LEVEL);

            if (aString != null) {
       	        aBoolean = new Boolean(aString);
            } else {
                throw new ApplicationError("Request parameter <"
                                    + pParameter + "> is missing" );
            }
        }

        return aBoolean;
    }

	protected void handleRuntimeException(HttpServletRequest pReq, HttpServletResponse pRes, RuntimeException pRuntimeException) throws IOException
	{
    loadErrorPage(pReq,pRes,pRuntimeException);
	}

	protected void handleError(HttpServletRequest pReq, HttpServletResponse pRes, Error pError) throws IOException
  {
    loadErrorPage(pReq,pRes,pError);
	}

  protected void loadErrorPage(HttpServletRequest pReq, HttpServletResponse pRes, Throwable pThrowable)
  	throws IOException
  {
    //FIXME: to be filled in later.
    StringWriter s = new StringWriter();
    PrintWriter p = new PrintWriter(s);
    pThrowable.printStackTrace(p);
    pReq.setAttribute("ErrorDescription", s.toString());
    
    if (pThrowable instanceof SessionFailedException)
    { 
      pReq.setAttribute(SESSION_ERROR_MESSAGE, HttpConstant.SESSION_ERROR_MESSAGE);
      this.forwardToTemplate(HttpConstant.Mr01_View, pReq, pRes);
    }
    else
    {       
      Object sessionObject = this.checkSession(pReq);
      if (sessionObject instanceof MR)
        this.forwardToTemplate(HttpConstant.Mr09_View, pReq, pRes);
      if (sessionObject instanceof DR)
        this.forwardToTemplate(HttpConstant.Dr12_View, pReq, pRes);
    }
  }
}
