
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import javax.servlet.*;
import javax.naming.*;
import javax.ejb.*;
import javax.rmi.*;
import java.lang.reflect.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.rmi.*;
import java.text.*;

/**
 * This class is the boundary class for the DR8.0 use case. It forms the
 * controller part of an MVC pattern with the dr08.jsp page and the
 * MessageHelperManager.
 *
 * @author <a href="mailto:Harry@Behrens.com">Harry Behrens</a>
 * @version $Id: DR_MasterCtlr.java,v 1.1.2.10 2001/10/15 05:28:37 damon Exp $
 */
public class DR_MasterCtlr extends BaseServlet 
{

 static final String ACTION_SQL = 
  "SELECT distinct Class, Method FROM "
  + "ServletFlow where FromPage = ? and ActionName = ? And ActionValue = ?";
 static final String NEXT_PAGE_SQL = 
  "SELECT distinct ToPage FROM "
  + "ServletFlow where FromPage = ? and Class = ? And Method = ? "
  + "AND StatusCode = ?";
  
 String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
 String  MESSAGE_LIST_TEMPLATE = HttpConstant.Dr08_View;
 String  EDIT_TEMPLATE		  = HttpConstant.Dr08_1_View;
 String  ATTACHMENT_CHOOSE_TEMPLATE	  = HttpConstant.Dr08_2_View;
 String  ATTACHMENT_SUBMITTED_TEMPLATE    = HttpConstant.Dr08_2a_View;
 String  ATTACHMENT_DELETE_TEMPLATE       = HttpConstant.Dr08_2a_View;
 String  MR_LIST_TEMPLATE	  = HttpConstant.Dr02_Ctlr;


 
 
 //-----------NONE OF THIS BELONHS HERE ------------------------
 // Parameters to pass back to the JSP
 final String  REQUEST_ERROR_MESSAGE       = "errorMessage";
 final String  REQUEST_RECIPIENT_NAMES     = "recipientNames";
 final String  REQUEST_MESSAGE_OBJECT      = "message";
 final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
 final String  REQUEST_MRINFO_LIST   = "mrInfoList";
 final String  REQUEST_SORT_OPTIONS  = "sortOptions";
 final String  REQUEST_MR_SELECTED   = "selectedMRs";
 final String	REQUEST_REFERER_PAGE  = "page";
 final String	REQUEST_GOTO_PAGE     = "gehzu";
 
 final boolean useReflection = false;
 
  
 
 
// hb010821: changed this per spec  final String  DEFAULT_SORT_ORDER            = "name";
  final String  DEFAULT_SORT_ORDER            = "company";
  final String  REQUEST_PAGE_DR               = "pageDR";
  
  // Parameters passed in by the browser
  final String  FILE_PARAMETER_PREFIX   = "file_";
  final String  DELETE_ATTACH_PARAMETER = "deleteAttachment";
  final int     MAX_FILE_COUNT          = 5;
  final int     MAX_FILE_SIZE           = 5242880;
  final String  REQUEST_ATTACHMENT_LIST = "attachmentList";
  final String  REDIRECT_PARAMETER          = "redirect";
  final String  DELETE_ATTACHMENT_PARAMETER = "deleteAttachment";
  final String  RECIPIENT_PREFIX            = "addressee_";
  final String  HTML_BODY_PARAMETER         = "htmlBody";
  final String  PLAIN_BODY_PARAMETER        = "plainBody";
  final String  TITLE_PARAMETER             = "title";
  final String  SORTBY_PARAMETER            = "sortBy";

  final String  FORWARD_ID_PARAMETER  		= "forwardMessageId";
  final String  REPLY_ID_PARAMETER          = "replyId";
 //--------------------CUT-------------------------------

  protected State state;
  protected Map beans;
    
 public void init(ServletConfig c) throws ServletException
 {
  super.init(c);
  	
  this.state = new State("MasterCtlr");    
  this.beans = new Hashtable();
  	
  this.state = new State("DR_MasterCtlr"); 
  System.out.println("hi damon");   
 
 }
 
 
 
  /**
   * This method handles any kind of request that comes to the
   * servlet. This servlet produces a list of Doctors to add as
   * recipients to a new message. Any error handling is done in
   * the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */

   
   public void doAction(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
		handlePageFlow(request, response);   
   } // doAction()
  
   

  void handlePageFlow(HttpServletRequest request, HttpServletResponse response)
  {
      try
      {
      	
		  Map args = new Hashtable();   
		  args.put("request",request);
		  args.put("response",response);
		  String page = getPageName(args);
		  Map action = selectAction(args);
		  
		  State s = callMethod(page,action,args);
		  String nextPage = getNextPageName(page, action, s);
		  setParameters(nextPage, s);
	  
		  gotoNextPage(nextPage,action,s);
      }
      catch(IOException e)
      {
      	throw new ApplicationError("MasterCtlr.handlePageFlow: IOException",e);
      }
  	
  }
  
  String getPageName(Map args) throws IOException
  {
  	String page = null;
    HttpServletRequest request = (HttpServletRequest) args.get("request");
    
   if ( (request.getContentType() != null) && (request.getContentType().startsWith("multipart/form-data")) )
   {
     MultipartHandler mph = new MultipartHandler(request); 	
     args.put("mph",mph);
     page = (String) mph.getParameter("page");
   }
   else
   {
   	 page = (String) request.getParameter("page");
   }
   return((page == null) ? new String("default") : page);	
  } //getPageName


  void gotoNextPage(String page, Map action, State s)
  {
	  	Map args = (Map) ((Map) s.peek(s.getSP())).get("args");
	  	HttpServletRequest request = (HttpServletRequest)
	  		args.get("request");
	  	HttpServletResponse response = (HttpServletResponse)
	  		args.get("response");
	  	s.pop();
	  	System.out.println("DR_MasterCtlr.gotoNextPage: going to "+page);
	  	super.forwardToTemplate(page, request, response);
  }
  
  
  String getNextPageName(String from, Map action, State s)
  {
  	Connection conn = null;
  	Map stackEntry = s.peek(s.getSP());
  	String returnCode = (String) s.getReturnCode();
    String executor = (String) ((Map) stackEntry.get("executor")).get("class");
	String message = (String) ((Map) stackEntry.get("executor")).get("method");
    
    System.out.println("DR_MasterCtlr.getNextPageName: returnCode="+returnCode+" for executor="+executor+"."+message);
	try
	{
		conn = getConnection();
		
		PreparedStatement searchQuery = conn.prepareStatement(NEXT_PAGE_SQL);
		searchQuery.setString(1, from);
		searchQuery.setString(2, executor);
		searchQuery.setString(3, message);
		searchQuery.setString(4, returnCode);
		System.out.println("MasterCtlr.getNextPageName: getting next page for"
		 +"executor="+ executor+" method="+message+" return="+returnCode);
		 
		ResultSet results = searchQuery.executeQuery();

		if ( results.next())
		{
			return(results.getString("ToPage"));
		}
		else
		{
			throw new ApplicationError("MasterCtlr.getNextPageName: no ToPage"
		    +"for from="+from+" executor="+ executor
		    +" method="+message+" return="+returnCode);
		}
		
	}
    catch(SQLException e)
    {
    	throw new ApplicationError("MasterCtlr.getNextPageName: SQLException",e);
    }
    finally
    {
    	try
    	{
    		conn.close();
    	}
      catch(SQLException e) 
      {
      }
      
    }
    
  } // getNextPageName()

  State callMethod(String page, Map action, Map args)
  {
	  Connection conn = null;
      String actionName = (String) action.get("name");
	  String actionValue = (String) action.get("value");
	  String returnCode = null;
  
  	  System.out.println("MasterCtlr.callMethod: page="+page
      +" actionName="+actionName
      +" actionValue="+actionValue);
	  try
	  {
	  	conn = getConnection();
	  	
	  	PreparedStatement searchQuery = conn.prepareStatement(ACTION_SQL);
	  	searchQuery.setString(1, page);
	  	searchQuery.setString(2, actionName);
	  	searchQuery.setString(3, actionValue);
	  	System.out.println("MasterCtlr.callMethod: getting class/method for"
	  	 +"page="+ page+" actionName="+actionName+" actionValue="+actionValue);
	  	 
	  	ResultSet results = searchQuery.executeQuery();

	  	if ( results.next())
	  	{
        	String className = results.getString("Class");
            String method = results.getString("method");
            state.push(className, method, args);
            System.out.println("MasterCtlr.callMethod: found class="+className
             +" method="+method);
			returnCode = executeMethod(className, method, args, useReflection);
            state.setReturnCode(returnCode);					  		
	  	}
	  }
	  catch(SQLException e)
	  {
	  	throw new ApplicationError("MasterCtlr.callMethod: SQLException",e);
	  }
	  finally
	  {
	  	try
	  	{
	  		conn.close();
	  	}
        catch(SQLException e) 
        {
        }
        
      }
      return(state);
 } // callMethod
  
 
  String executeMethod(String className, String method, Map args, boolean useReflection) 
  {
  	if (useReflection)
  	{
  		return(executeMethod(className,method,args));
  	}
    else // try how expensive reflection really is
    {
     try
     {
     	
    	ContactHelper helper = new ContactHelper();
        System.out.println("DR_MasterCtlr.executeMethod: non-ref exec of "
         +className+"."+method+"()");
    	if ( method.equals("createMailEditor"))
    	{
    		return(helper.createMailEditor(args));
    	}
        else if ( method.equals("createRecipientList"))
        {
        	return(helper.createRecipientList(args));
        }
	    else if ( method.equals("forwardContact"))
	    {
	    	return(helper.forwardContact(args));
	    }
	    else if ( method.equals("handleAttachment"))
	    {
	    	return(helper.handleAttachment(args));
	    }
	    else if ( method.equals("refreshAttachments"))
	    {
	    	return(helper.refreshAttachments(args));
	    }
	    else if ( method.equals("replyToEDetail"))
	    {
	    	return(helper.replyToEDetail(args));
	    }
	    else if ( method.equals("sendContact"))
	    {
	    	return(helper.sendContact(args));
	    }
	    else if ( method.equals("updateRecipientList"))
	    {
	    	return(helper.updateRecipientList(args));
	    }
     } // try
     catch(IOException e)
     {
     	throw new ApplicationError("DR_MasterCtlr.executeMethod: IOException",e);	
     }
    } // if (useReflection)
    return("APPLICATION_ERROR");
  }
  	
  String executeMethod(String className, String method, Map args)
  {
  		Class c = null;
	   Object o = null;;
	   Method m = null;
       String ret = null;
       	
        if ( beans.get(className) != null)
        {
	        o = ((Map) beans.get(className)).get("object");
	        System.out.println("MasterCtlr.executeMethod: found instance "
	         +" for class="+className);
            m = (Method) ((Map) beans.get(className)).get(method);
        }


        try
        {
        	c = Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
        	throw new ApplicationError("MasterCtlr.executeMethod: ClassNotFoundException when"
             +"instantiating bean for "+className);  
        }
        if ( o == null)
        {
        	try
        	{
                o = c.newInstance();
        		System.out.println("MasterCtlr.executeMethod: " 
                 +"instantiated object for "+className);
        	}
        	catch(InstantiationException e)
            {
            	throw new ApplicationError("MasterCtlr.executeMethod: InstantiationException when"
                 +"instantiating bean for "+className);  
            }
            catch(IllegalAccessException e)
            {
            	throw new ApplicationError("MasterCtlr.executeMethod: IllegalAccessException when"
                 +"instantiating bean for "+className);  
            }
            
            Map mp = new Hashtable();
            mp.put("object",o);
        	beans.put(className,mp);
        } // if ( o == null )



	    if ( m != null )
	    {
            System.out.println("MasterCtlr.executeMethod: found method="
             +method+" for class="+className);
	    }
	    else
	    {
        	try
        	{
        	   Class[] p = new Class[] { Map.class};	/* argument types */
		       m = c.getMethod(method,p);
		       System.out.println("MasterCtlr.executeMethod: storing method "+method
		         +" for class "+className);
		        
		        ((Map) beans.get(className)).put(method,m);
	        }
	        catch(NoSuchMethodException ex)
	        {
	            throw new ApplicationError("MasterCtlr.executeMethod: NoSuchMethodException for class="
	            +c.getName()+" method="+method,ex);
	        	
	        }
            
	    }
        
	    
	    
		try 
		{
		    Object[] a = new Object[] { args };		/* set property value */
		   System.out.println("MasterCtlr.executeMethod: executing "+c.getName()+"."+method);
           
	  	 ret = (String) m.invoke(o,a);
	  	 System.out.println("MasterCtlr.executeMethod: invoked "+className+"."+method+"()");
         
	    }
	    catch (InvocationTargetException e)
	    {
        PrintWriter p = new PrintWriter(System.out);
        e.printStackTrace(p);
        e.getTargetException().printStackTrace(p);
        p.flush();
		  	throw new ApplicationError("MasterCtlr.executeMethod: InvocationTargetException",e.getTargetException());
	  	}
        catch(IllegalAccessException ei)
        {
	
	        throw new ApplicationError("MasterCtlr.executeMethod: IllegalAccessException",ei);
        }
	
	return(ret);
  }
  
 protected Map selectAction(Map args) 
 {
 	String name = new String("default");
   String gotoParameter;
   Map actionDescriptor = null;
   HttpServletRequest request = (HttpServletRequest) args.get("request");
   
   // use MIME handling if we have file uploads
   
   if ( (request.getContentType() != null) && (request.getContentType().startsWith("multipart/form-data")) )
   {
   	MultipartHandler mph = (MultipartHandler) args.get("mph");

       
   // Get each file from the request and build an attachment
     gotoParameter = (String) mph.getParameter(REQUEST_GOTO_PAGE);
   		
   }
   else
   {
   	gotoParameter = request.getParameter(REQUEST_GOTO_PAGE);
   }
   
   if(gotoParameter != null) 
   {
   	actionDescriptor = new Hashtable();
    actionDescriptor.put("name","gehzu");
    actionDescriptor.put("value",gotoParameter);
   }
   else
   {
   	actionDescriptor = new Hashtable();
   	actionDescriptor.put("name","default");
   	actionDescriptor.put("value","default");
    	
   }
   return(actionDescriptor);
 }
 
  
  /*
  * This method handles parameter passing between servlet, view
  * and helper classe
  * Based on Rick's XML descriptor or view and servlet parameter dictionary
  * this method should be expanded to dynamically check for
  * existence and consistency of necessary parameters
  * @param to The page we will be dispatchin to  
  * @s the current state
  */
  void setParameters(String to, State s)
  {


   HttpServletRequest request = (HttpServletRequest) 
    ((Map) s.peek(s.getSP()).get("args")).get("request");
    DR sessionDR = (DR) checkSession(request);

  request.setAttribute(REQUEST_PAGE_DR, sessionDR);
  Map args = ((Map) s.peek(s.getSP()).get("args"));
  Collection recipientList      = (Collection)
   args.get(REQUEST_MR_SELECTED);
  Collection mrProfileList      = (Collection)
   args.get(REQUEST_MRINFO_LIST);
  String sortOrder = (String)
   args.get("sortBy");
  Collection colSortBy = new ArrayList();
  colSortBy.add("name");
  colSortBy.add("hospital");
  request.setAttribute(REQUEST_SORT_OPTIONS, colSortBy);
  
  	if ( to.equals(MR_LIST_TEMPLATE) )
  	{

        if ( request == null || mrProfileList == null || sortOrder == null)
        {
        	throw new ApplicationError("DR_MasterCtlr.setParameters: parameters not sufficient for page"+to);
        }
  		request.setAttribute(REQUEST_MRINFO_LIST, mrProfileList);
  		request.setAttribute(REQUEST_MR_SELECTED, recipientList);
    	request.setAttribute("sortBy", sortOrder);
  		
  	}
    else if ( to.equals(EDIT_TEMPLATE) )
    {

	    SessionManager sm = new SessionManager(request,new Boolean(false));
    
	String subject =  (String) sm.getSessionItem("followupTitle");

    	recipientList = (Collection) args.get(REQUEST_RECIPIENT_NAMES);
        Message message = (Message) args.get(REQUEST_MESSAGE_OBJECT);

	if ( subject != null )
	{
	 message.setTitle(subject);
	}

        Collection attchList = (Collection) args.get(REQUEST_ATTACHMENT_LIST);
        String outOfTime = (String) args.get("outOfTime");
        String errorMessage = (String) args.get("errorMessage");
        if (  errorMessage == null )
        {
        	request.setAttribute("errorMessage","");
        }
        else
        {
        	request.setAttribute("errorMessage",errorMessage);
        }
        request.setAttribute("outOfTime",outOfTime);
        request.setAttribute("followupmsg", args.get("followupmsg"));
    	request.setAttribute(REQUEST_RECIPIENT_NAMES, recipientList);
    	request.setAttribute(REQUEST_MESSAGE_OBJECT, message);
    	request.setAttribute(REQUEST_ATTACHMENT_LIST, attchList);
    	
    }
    else if ( to.equals(MESSAGE_LIST_TEMPLATE) )
    {
    	System.out.println("DR_MasterCtlr.setParameters: setting parameters for MESSAGE_LIST_TEMPLATE");
        String errorMessage = (String) args.get("errorMessage");
        if (  errorMessage == null )
        {
        	request.setAttribute("errorMessage","");
        }
        else
        {
        	request.setAttribute("errorMessage",errorMessage);
        }
        request.setAttribute(REQUEST_MRINFO_LIST, mrProfileList);
        request.setAttribute(REQUEST_MR_SELECTED, recipientList);
        request.setAttribute("sortBy", sortOrder);
    	
    }
    else if ( to.equals(ATTACHMENT_DELETE_TEMPLATE) )
    {
    	Collection al = (Collection) args.get(REQUEST_ATTACHMENT_LIST);
    	request.setAttribute(REQUEST_ATTACHMENT_LIST, al);
    	
    }
    else if ( to.equals(ATTACHMENT_SUBMITTED_TEMPLATE) )
    {
    	Collection al = (Collection) args.get(REQUEST_ATTACHMENT_LIST);
    	request.setAttribute(REQUEST_ATTACHMENT_LIST, al);
    	
    }
    // to.equals(ATTACHMENT_CHOOSE_TEMPLATE) nothing needed
    else if ( to.equals(ATTACHMENT_CHOOSE_TEMPLATE) )
    {
    	System.out.println("DR_MasterCtlr.setParameters: for "+ATTACHMENT_CHOOSE_TEMPLATE+"errorMessage="+(String) args.get("errorMessage"));
    	Collection al = (Collection) args.get(REQUEST_ATTACHMENT_LIST);
    	request.setAttribute(REQUEST_ATTACHMENT_LIST, al);
        request.setAttribute("errorMessage",args.get("errorMessage"));
    	
    }
    else // being lazy
    {
      request.setAttribute(REQUEST_MRINFO_LIST, mrProfileList);
      request.setAttribute(REQUEST_MR_SELECTED, recipientList);
      request.setAttribute("sortBy", sortOrder);
    
  	    	
    }
  }
  
  // This method will return the JDBC connection instance - Added by Damon 011015
  private Connection getConnection() throws SQLException
  {
  	Connection conn = null;
    
    try
    {
      Driver driver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
      Properties properties = new Properties();
      properties.setProperty("user", dbUtilConstant.JDBC_USER);
      properties.setProperty("password", dbUtilConstant.JDBC_PASS);
      conn = driver.connect(dbUtilConstant.JDBC_LOOKUP, properties);
      return conn;
   	}
    catch(ClassNotFoundException cnfExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: ClassNotFoundException", cnfExc);
    }
    catch(InstantiationException iExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: InstantiationException", iExc);
    }
    catch(IllegalAccessException iaExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: IllegalAccessException", iaExc);
    }
  } 
  
} // MasterCtlr




