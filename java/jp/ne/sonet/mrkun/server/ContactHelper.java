
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.log.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import javax.servlet.*;
import javax.naming.*;
import javax.ejb.*;
import javax.rmi.*;
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
 * @version $Id: ContactHelper.java,v 1.1.2.23 2001/12/11 05:27:46 rick Exp $
 */
public class ContactHelper implements Serializable
{

  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  MESSAGE_LIST_TEMPLATE = HttpConstant.Dr08_View;
  final String  EDIT_TEMPLATE		  = HttpConstant.Dr08_1_View;
  final String  ATTACHMENT_CHOOSE_TEMPLATE	  = HttpConstant.Dr08_2_View;
  final String  ATTACHMENT_SUBMITTED_TEMPLATE    = HttpConstant.Dr08_2a_View;
  final String  ATTACHMENT_DELETE_TEMPLATE       = HttpConstant.Dr08_2a_View;
  
  final String  MR_LIST_TEMPLATE	  = HttpConstant.Dr02_Ctlr;


// hb010821: changed this per spec  final String  DEFAULT_SORT_ORDER            = "name";
  final String  DEFAULT_SORT_ORDER            = "company";
  final String  REQUEST_PAGE_DR               = "pageDR";
  
  // Parameters passed in by the browser
  final String  FILE_PARAMETER_PREFIX   = "file_";
  final String  DELETE_ATTACH_PARAMETER = "deleteAttachment";
  final int     MAX_FILE_COUNT          = 5;
  final int     MAX_FILE_SIZE           = 5242880;
  final String  REQUEST_ATTACHMENT_LIST = "attachmentList";
  
  
  // Parameters passed in by the browser
  final String  REDIRECT_PARAMETER          = "redirect";
  final String  DELETE_ATTACHMENT_PARAMETER = "deleteAttachment";
  final String  RECIPIENT_PREFIX            = "addressee_";
  final String  HTML_BODY_PARAMETER         = "htmlBody";
  final String  PLAIN_BODY_PARAMETER        = "plainBody";
  final String  TITLE_PARAMETER             = "title";

  final String  REPLY_ID_PARAMETER          = "replyId";
  
  // Parameters to pass back to the JSP
  final String  REQUEST_RECIPIENT_NAMES     = "recipientNames";
  final String  REQUEST_MESSAGE_OBJECT      = "message";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  REQUEST_MRINFO_LIST   = "mrInfoList";
  final String  REQUEST_SORT_OPTIONS  = "sortOptions";
  final String  REQUEST_SORT_BY       = "sortBy";
  final String  REQUEST_MR_SELECTED   = "selectedMRs";
  final String	REQUEST_REFERER_PAGE  = "page";
  final String	REQUEST_GOTO_PAGE     = "goto";
  
  
  
  
  
  
  private Map sortDirection;


 public ContactHelper()
 {
	 sortDirection = new Hashtable();
	 sortDirection.put("name",new Boolean(true));
	 sortDirection.put("company",new Boolean(true));
 }
 
 /**
  * Get the session object from the SessionManager.
  * @param request The request with the current session in it.
  */
  protected Object checkSession(HttpServletRequest request) 
  {
    Object sessionObject = new Object();
    
    try
    {      
      //SessionManager mgr = SessionManager.getSessionManager(request, false);
      SessionManager mgr = new SessionManager(request, new Boolean(false));
   
      // Retrieve the MR object
      if (mgr == null)   
         throw new SessionFailedException("SessionManager mgr == null");
      else
        sessionObject = mgr.getUserItem();          
    }
    catch(SessionFailedException err)
    {
      throw new SessionFailedException("SessionManager is returned null."); 
    }
    catch(IllegalStateException err)
    {
      throw new SessionFailedException("Session invalid ocurrs with illegalStateException is thrown."); 
    } 
         
    return sessionObject;
  }
 
  
  public String createRecipientList(Map args)
  {
  	System.out.println("ContactHelper.createRecipientList.1");
    HttpServletRequest request = (HttpServletRequest) args.get("request");
    HttpServletResponse response = (HttpServletResponse) args.get("response");
    System.out.println("ContactHelper.createRecipientList.2");

	// this should never happen
    if ( request == null || response == null )
    {
    	throw new ApplicationError("DR_SendContactCtlr.createRecipientList: request or response parameter not initialized");
    }
    
  
    Map pendingMessageDetails = getMessageInSession(request);
    DR sessionDR = (DR) checkSession(request);

    System.out.println("ContactHelper.createRecipientList.3 DR="+sessionDR.getDrId());
	      
      // Retrieve all the message details
      Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

      
      
      String requestedOrder = request.getParameter("sortBy");
      String sortOrder      = DEFAULT_SORT_ORDER;

        if ((requestedOrder != null) &&
          ((Collection) request.getAttribute(REQUEST_SORT_OPTIONS)).contains(requestedOrder))
        sortOrder = requestedOrder;

      // Build a list of MRProfile objects and the message support clump (also
      // known as the 5 object array used to pass back message details for a DR)
      //Map messageCounts = new Hashtable();
      Collection mrProfileList = getSortedMrList(sessionDR, sortOrder, BaseServlet.getWLCookie(request)); //, messageCounts);

      System.out.println("ContactHelper.createRecipientList.4");
      
      args.put(REQUEST_MR_SELECTED,recipientList); // this needs to be expanded
      		// to add class/type information
      args.put(REQUEST_MRINFO_LIST, mrProfileList);
      args.put(REQUEST_SORT_BY, sortOrder);
      System.out.println("ContactHelper.createRecipientList.5");
      return("OK");    
  }
  
  
  public String updateRecipientList(Map args)
  {
    HttpServletRequest request = (HttpServletRequest) args.get("request");
    HttpServletResponse response = (HttpServletResponse) args.get("response");

  // this should never happen    
    if ( request == null || response == null )
    {
    	throw new ApplicationError("DR_SendContactCtlr.createRecipientList: request or response parameter not initialized");
    }
    
  
    Map pendingMessageDetails = getMessageInSession(request);
    DR sessionDR = (DR) checkSession(request);

    updateParameters(request, pendingMessageDetails);
    // validateSuppliedParameters(request, pendingMessageDetails);
    
      // Retrieve all the message details
      Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);


      updateRecipientList(request,recipientList);
	 
      // Set the collection of sort options etc
    //  setupComboLists(request);
      
      // Process sort order
      String requestedOrder = request.getParameter("sortBy");
      String sortOrder      = DEFAULT_SORT_ORDER;

      if ((requestedOrder != null) &&
       sortDirection.containsKey(requestedOrder))
       {
        sortOrder = requestedOrder;
       }

      System.out.println("ContactHelper.updateRecipientList: sortOrder="+sortOrder);
      // Build a list of MRProfile objects and the message support clump (also
      // known as the 5 object array used to pass back message details for a DR)
      //Map messageCounts = new Hashtable();
      Collection mrProfileList = getSortedMrList(sessionDR, sortOrder, BaseServlet.getWLCookie(request));//, messageCounts);

      /* Send on to the jsp view
      request.setAttribute(REQUEST_MRINFO_LIST, mrProfileList);
      request.setAttribute(REQUEST_MR_SELECTED, recipientList);
      request.setAttribute(REQUEST_SORT_BY, sortOrder);
      super.forwardToTemplate(MESSAGE_LIST_TEMPLATE, request, response);
	  */
      
      args.put(REQUEST_MR_SELECTED,recipientList); // this needs to be expanded
      		// to add class/type information
      args.put(REQUEST_MRINFO_LIST, mrProfileList);
      args.put(REQUEST_SORT_BY, sortOrder);
      // super.forwardToTemplate(MESSAGE_LIST_TEMPLATE, request, response);
      return("OK");    
   	
  } // updateRecipientList
  
  protected Map getMessageInSession(HttpServletRequest request)
  {
    SessionManager session = new SessionManager(request, new Boolean(false));
    Map pendingMessageDetails = (Map) session.getSessionItem(HttpConstant.PENDING_IN_SESSION);

    int i =0;
    if (pendingMessageDetails == null)
    {
      pendingMessageDetails = new Hashtable();
      session.setSessionItem(HttpConstant.PENDING_IN_SESSION, pendingMessageDetails);
    }

    // Make sure there is a contact object
    if (pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.MESSAGE_IN_SESSION, new Contact());

    // Make sure there is a recipients collection
    if (pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.RECIPIENTS_IN_SESSION, new ArrayList());

    // Make sure there is an original message Id
    if (pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION, "");

    // Make sure there is a plain text version of the message
    if (pendingMessageDetails.get(HttpConstant.PLAINTEXT_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.PLAINTEXT_IN_SESSION, "");

    return pendingMessageDetails;
  } // getMessagInSession()
  
  
  /**
   * Get a collection of the recipient names that match the ids within this collection
   */
  protected Collection getRecipientNames(Collection recipientList, DR userDR, String sessionId)
  {
    try
    {
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf)
                          new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);

      // Get the recipient names
      Collection recipientNames = new ArrayList();
      for (Iterator i = recipientList.iterator(); i.hasNext(); )
      {
        String mrId = (String)i.next();
        MrProfile recipientMrProfile = drm.getMrProfile(mrId, sessionId);
        if (recipientMrProfile != null)
          recipientNames.add(recipientMrProfile.getName());
        else System.out.println("DR_SendContactCtlr.getRecipientNames: MrProfile not found for DR: " + userDR.getDrId() + " MR: " + mrId);
      }
      return recipientNames;
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error accessing the DRManager EJB", errRemote);
    }
  } // getRecipientNames()

  /**
   * Build a reply to a message, setting the follow up text and the recipient id
   */
  protected String getFollowupText(Map params, String replyMessageId, DR userDR, boolean isReply, String sessionId)
  {
    Contact msg = (Contact) params.get(HttpConstant.MESSAGE_IN_SESSION);

    // Load up the message we're replying to
    try
    {
      //MessageManagerRemoteIntf messMgr = (MessageManagerRemoteIntf)
      //                new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messMgr = new MessageHelperManager();
      Message replyToMsg = messMgr.getMessageById(replyMessageId, sessionId);

        Collection recipientList = (Collection) params.get(HttpConstant.RECIPIENTS_IN_SESSION);
        recipientList.clear();
        if ( isReply)
        {
	        recipientList.add(replyToMsg.getSender());
        }
        else
        {
        	recipientList.add(replyToMsg.getRecipient());
        }
      msg.getAttachmentList().addAll(replyToMsg.getAttachmentList());
      // Set the original message id in the map
      params.put(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION, replyMessageId);

      // return the follow up message
      return replyToMsg.getBody();
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with the MessageManager EJB", errRemote);
    }

  } // getFollowupText()
  

  protected String getFollowupTitle(Map params, String replyMessageId, DR userDR, boolean isReply, String sessionId)
  {
    // Load up the message we're replying to
    try
    {
      //MessageManagerRemoteIntf messMgr = (MessageManagerRemoteIntf)
      //                new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messMgr = new MessageHelperManager();
      Message replyToMsg = messMgr.getMessageById(replyMessageId, sessionId);
      System.out.println("ContactHelper.getFollowupTitle: title:"+replyToMsg.getTitle());
      return replyToMsg.getTitle();
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with the MessageManager EJB", errRemote);
    }

  } // getFollowupTitle()
  
  /**
   * Produces a list containing the MRProfile objects sorted in the
   * requested order. It also modifies a map which contains the
   * unread message detail for each DR.
   * @param userDR The DR to filter for.
   * @param sortOrder The order in which to return the MRProfile list.
   * @param messageCounts hashtable with information about number of messages per MR
   */
  protected Collection getSortedMrList(DR userDR, String sortOrder, String sessionId) //, Map messageCounts)
  {
    try
    {

      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                        new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);



      MessageHelperManager mhm = new MessageHelperManager();
      Collection mrIdList = mrm.getMRProfileByDRId(userDR.getDrId(), sessionId).values();



      // Sort the list
      Object mrArray[] = mrIdList.toArray();
      boolean direction = ((Boolean)sortDirection.get(sortOrder)).booleanValue();
      MrProfileComparator mrc = new MrProfileComparator(sortOrder, direction);
      Arrays.sort(mrArray, mrc);

      // Return the sorted list
      return Arrays.asList(mrArray);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Remote error using the MRManager bean", errRemote);
    }
  }
  
   /**
   * Deletes attachments from the session message object if required.
   */
  protected void deleteAttachment(Contact message, String deleteAttachmentId)
  {
    // Iterate through and find the linkId matching the deleteLink
    Collection attachments = message.getAttachmentList();
    AttachmentLink removal = null;
    for (Iterator i = attachments.iterator(); i.hasNext();)
    {
      AttachmentLink attachment = (AttachmentLink) i.next();
      if (attachment.getAttachmentLinkId().equals(deleteAttachmentId))
        removal = attachment;
    }
    if (removal != null) attachments.remove(removal);
  }

 

  /**
   * This method validates the parameters passed in with the request, and if
   * they pass, the session message is updated with the new values. Otherwise
   * a ValidationException is thrown.
   * @param request The HTTP request object
   * @param message The list of DrIds to send the message to
   */
  protected void validateSuppliedParameters(HttpServletRequest request, Map pendingMessage)
    throws ValidationException
  {
    Contact message = (Contact) pendingMessage.get(HttpConstant.MESSAGE_IN_SESSION);
    
    // Get all the parameters from the request
    String testHTMLBody   = StringUtils.getParameter(request,HTML_BODY_PARAMETER);
    String testPlainBody  = StringUtils.getParameter(request,PLAIN_BODY_PARAMETER);
    String testTitle      = StringUtils.getParameter(request,TITLE_PARAMETER);

    String finalErrorMessage = "";

    // Check the html body
    if ((testHTMLBody == null) ||
        testHTMLBody.equals(""))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("dr_noMessageBodySupplied");//"No message body supplied";
    }
    else if (testPlainBody.equals(""))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("dr_noPlainTextBodySupplied");//"No plain text message body supplied";
    }
    else
    {
      message.setBody(testHTMLBody);
      pendingMessage.put(HttpConstant.PLAINTEXT_IN_SESSION, testPlainBody);
    }

    // Check the title
    if (testTitle == null)
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("dr_noMessageTitleSupplied");//"No message title supplied";
    }
    else
    {
      message.setTitle(testTitle);
    }


    if (!finalErrorMessage.equals(""))
    {
      throw new ValidationException(finalErrorMessage);
    }
  }



  /**
   * This method validates the parameters passed in with the request, and if
   * they pass, the session message is updated with the new values. Otherwise
   * a ValidationException is thrown.
   * @param request The HTTP request object
   * @param message The list of DrIds to send the message to
   */
  protected void updateParameters(HttpServletRequest request, Map pendingMessage)
  {
    Contact message = (Contact) pendingMessage.get(HttpConstant.MESSAGE_IN_SESSION);
    
    // Get all the parameters from the request
    String testHTMLBody   = StringUtils.getParameter(request,HTML_BODY_PARAMETER);
    String testPlainBody  = StringUtils.getParameter(request,PLAIN_BODY_PARAMETER);
    String testTitle      = StringUtils.getParameter(request,TITLE_PARAMETER);

    String finalErrorMessage = "";

    // Check the html body
    if ((testHTMLBody == null) ||
        testHTMLBody.equals(""))
    {
	    testHTMLBody = new String("");
    }

    message.setBody(testHTMLBody);
    if(testPlainBody != null)
    {
    	pendingMessage.put(HttpConstant.PLAINTEXT_IN_SESSION, testPlainBody);
    }

    // Check the title
    if (testTitle == null)
    {
  		testTitle = new String("");
    }
    message.setTitle(testTitle);
    System.out.println("ContactHelper.updateParameters: subject is "+testTitle);
    System.out.println("ContactHelper.updateParameters: HTML body is "+testHTMLBody);
    System.out.println("ContactHelper.updateParameters: plain body is "+testPlainBody);

  } //updateParameters

  public String createMailEditor(Map args)
  {
	  HttpServletRequest request = (HttpServletRequest) args.get("request");
	  HttpServletResponse response = (HttpServletResponse) args.get("response");

	  // this should never happen
	  if ( request == null || response == null )
	  {
	  	throw new ApplicationError("ContactHelper.createMailEditor: request or response parameter not initialized");
	  }


	  Map pendingMessageDetails = getMessageInSession(request);
	  DR sessionDR = (DR) checkSession(request);
	  Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
	  Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
	  String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

	  try
	  {
	    // Retrieve all the message details
	    // Check for whether this message is to be previewed for sending or just
	    // continue editing
	    if (request.getParameter(DELETE_ATTACHMENT_PARAMETER) != null)
	    {
	      // Remove the attachment and send back to the same page
	      deleteAttachment(message, request.getParameter(DELETE_ATTACHMENT_PARAMETER));
	    }
	    else
	    {
	      // Handle recipient lists supplied if there is one
	      updateRecipientList(request, recipientList);
	    }
	  }
	  catch(SessionFailedException errSession)
	  {
	    request.setAttribute(SESSION_ERROR_MESSAGE, HttpConstant.SESSION_ERROR_MESSAGE);
        throw new ApplicationError("TODO",errSession);
	  }  
	  String outOfTime = null;

      outOfTime = getOutOfTime(sessionDR,recipientList);

      SessionManager sm = new SessionManager(request,new Boolean(false));
      
	  args.put("outOfTime",outOfTime);
      String followuptxt = (String) sm.getSessionItem("followupmsg");
      System.out.println("ContactHelper.createMailEditor: followupmsg="+
       followuptxt);
      if ( followuptxt != null )
      {
	   	args.put("followupmsg",followuptxt);
      }
      else
      {
      	   args.put("followupmsg", "");
	  }
      args.put(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, sessionDR, BaseServlet.getWLCookie(request)));
	  args.put(REQUEST_MESSAGE_OBJECT, message);
	  args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
	  return("OK");    
  }
  
  
  /**
   * Get any recipients from the request object. If there aren't any, leave
   * it in tact.
   * @param request The HTTP request object
   * @param recipientList The list of DrIds to send the message to
   */
  protected void updateRecipientList(HttpServletRequest request, Collection recipients)
  {
  
  	Map pendingMessageDetails = getMessageInSession(request);
  	DR sessionDR = (DR) checkSession(request);
  
    // Iterate through the parameters and check for ones that match the
    // recipient prefix
    Collection newRecipients = new ArrayList();
    Enumeration paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements())
    {
      String paramName = (String) paramNames.nextElement();
      if (paramName.startsWith(RECIPIENT_PREFIX))
        newRecipients.add(paramName.substring(RECIPIENT_PREFIX.length()));
    }

    // If there were any found, wipe out the old list and replace with the new list
    if (!newRecipients.isEmpty())
    {
      recipients.clear();
      recipients.addAll(newRecipients);
    }
  }
  
  
  public String sendContact(Map args)
  {
	  HttpServletRequest request = (HttpServletRequest) args.get("request");
	  HttpServletResponse response = (HttpServletResponse) args.get("response");
    String sessionId = BaseServlet.getWLCookie(request);

	  // this should never happen    
	  if ( request == null || response == null )
	  {
	  	throw new ApplicationError("ContactHelper.sendContact: request or response parameter not initialized");
	  }
	  DR sessionDR = (DR) checkSession(request);

	  Map pendingMessageDetails = getMessageInSession(request);
	  updateParameters(request,pendingMessageDetails);
	  	
      
	  // Retrieve all the message details
	  Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
	  Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
	  String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

	  // Set the collection of sort options etc
	  //setupComboLists(request);
	  
	  // Process sort order
	  String requestedOrder = request.getParameter("sortBy");
	  String sortOrder      = DEFAULT_SORT_ORDER;

	    if ((requestedOrder != null) &&
	      ((Collection) request.getAttribute(REQUEST_SORT_OPTIONS)).contains(requestedOrder))
	    sortOrder = requestedOrder;

	  // Build a list of MRProfile objects and the message support clump (also
	  // known as the 5 object array used to pass back message details for a DR)
	  //Map messageCounts = new Hashtable();
	  Collection mrProfileList = getSortedMrList(sessionDR, sortOrder, sessionId); //, messageCounts);

      dumpAttachments(message, sessionId);
        
      String ret = null;  
	  try
	  {
	    // Retrieve all the message details
		  String plainText = (String) pendingMessageDetails.get(HttpConstant.PLAINTEXT_IN_SESSION);
      validateSuppliedParameters(request, pendingMessageDetails);
	    MessageHelperManager mgr = new MessageHelperManager();
      mgr.addMessage((User) sessionDR, recipientList, (Message) message, plainText, sessionId);

      sendFollupEmail(sessionDR, recipientList, message, plainText, sessionId);
      ret = "OK";
  	  pendingMessageDetails.clear();
  	}
    catch(ValidationException e)
    {
      String errorMessage = e.getMessage();
      args.put("errorMessage", errorMessage);
		  ret = "VALIDATION_ERROR";
      // super.forwardToTemplate(EDIT_TEMPLATE, request, response);
    }
    // Send on to the jsp view

    String outOfTime = getOutOfTime(sessionDR, recipientList);
    args.put("followupmsg","");
    args.put(REQUEST_MESSAGE_OBJECT, message);
    args.put(REQUEST_MR_SELECTED,recipientList); // this needs to be expanded
 		// to add class/type information
    args.put(REQUEST_MRINFO_LIST, mrProfileList);
    args.put(REQUEST_SORT_BY, sortOrder);

    args.put("outOfTime",outOfTime);
    args.put(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, sessionDR, sessionId));
    args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());

    return(ret);

  } // sendContact


  void sendFollupEmail(DR sessionDR, Collection recipientList, Contact message, String plainText, String sessionId)
  {
	  Map mrProfileMap = sessionDR.getMRProfileMap();
      Iterator iter = recipientList.iterator();
	  String doctorName = sessionDR.getKanjiName();
      String kinmusaki = sessionDR.getHospital();
      System.out.println("ContactHelper.sendFollupEmail: 1");
      String mrkun = SystemMessages.get("dr_sendContactFollowup_MrKun");
      MessageHelperManager mailer = new MessageHelperManager();

      while(iter.hasNext())
      {
        Collection toEmail = new Vector();
      	String mrId = (String) iter.next();
        System.out.println("ContactHelper.sendFollupEmail: processing mrId="+mrId);
        MrProfile mrp = (MrProfile) mrProfileMap.get(mrId);
	    MR mr = (MR) mrp.getMR();
        String wmMailAddress = mr.getCompany().getWmEmailAddress();
        String mrName = mrp.getName();
        if ( (wmMailAddress == null) || wmMailAddress.equals("") )
        {
        	wmMailAddress = SystemMessages.get("dr_sendContactFollowup_m3center");
        }
        System.out.println("ContactHelper.sendFollupEmail: sending mail from:"+wmMailAddress);
        Collection toEmails = mr.getForwardEmailList();
        Iterator iter2 = toEmails.iterator();
        while (iter2.hasNext())
        {
        	EmailContact ec = new EmailContact();
            String email = (String) iter2.next();
            System.out.println("ContactHelper.sendFollupEmail: processing mail address="+email);
            ec.setEmailAddress(email);
            ec.setName(mrName);
            toEmail.add(ec);
        }
        
        
        String messageBody = doctorName + "\n"+kinmusaki+"\n"+plainText;
        if ( toEmail.size() > 0)
        {
        	mailer.sendEmailMessage(wmMailAddress,mrkun,toEmail,
             message.getTitle(),messageBody, sessionId);
           System.out.println("ContactHelper.sendFollupEmail: sent email");
        }
      } // while iter.next()

  } // sendFollowUpEmail
  
  public String forwardContact(Map args)
  {
  
  	Contact message = null;
    Collection recipientList = null;
    
  	HttpServletRequest request = (HttpServletRequest) args.get("request");
  	HttpServletResponse response = (HttpServletResponse) args.get("response");
    String sessionId = BaseServlet.getWLCookie(request);

    Map pendingMessageDetails = getMessageInSession(request);
    DR sessionDR = (DR) checkSession(request);
      // Retrieve all the message details
     message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
     recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
     String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

      
      
     String sortOrder      = DEFAULT_SORT_ORDER;
    String forwardText = null;
    String subject = null;

    
    
    //Map messageCounts = new Hashtable();
    Collection mrProfileList = getSortedMrList(sessionDR, sortOrder, sessionId); //, messageCounts);

    
    String msgId = request.getParameter("forwardMessageId");

    if ( msgId != null)
    {
    
	    // return the follow up message
	    pendingMessageDetails = getMessageInSession(request);
	    updateParameters(request,pendingMessageDetails);
	    	
	    
	    // Retrieve all the message details
	    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
	    recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);

	    forwardText = getFollowupText(pendingMessageDetails,
	        request.getParameter("forwardMessageId"), sessionDR,false, sessionId);

        System.out.println("ContactHelper.forwardContact: forwardText="+forwardText);

	    
	  	if ( forwardText == null)
	  	{
	          forwardText = new String("");
	  	}
	      subject = getFollowupTitle(pendingMessageDetails,     
	      request.getParameter("forwardMessageId"), sessionDR,true, sessionId);
    }
    
    
    
    String outOfTime = getOutOfTime(sessionDR,recipientList);

    args.put("outOfTime",outOfTime);
    args.put("followupmsg", forwardText);
    
    /*
    * <hb010919
    * this is a hack
    * work on this to make a 'parameter pool
    * instead of a parameter pipe
    */
    SessionManager sm = new SessionManager(request,new Boolean(false));
    
    sm.setSessionItem("followupmsg", forwardText);
    sm.setSessionItem("followupTitle",subject);
    
    
    /*
    * this is for page /dr08.jsp
    */
    args.put("sortBy",DEFAULT_SORT_ORDER);
    args.put(REQUEST_MR_SELECTED,new Vector()); // this needs to be expanded
    		// to add class/type information
    args.put(REQUEST_MRINFO_LIST, mrProfileList);
    
    /*
    * this is for page /dr08-1.jsp
    */
    
    args.put(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, sessionDR, sessionId));
    args.put(REQUEST_MESSAGE_OBJECT, message);
    args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
    
    return("OK");    
    
  } // forwardContact
  
  public String replyToEDetail(Map args)
  {
  	HttpServletRequest request = (HttpServletRequest) args.get("request");
  	HttpServletResponse response = (HttpServletResponse) args.get("response");
  	String sessionId = BaseServlet.getWLCookie(request);
  	DR sessionDR = (DR) checkSession(request);
    
  	Map pendingMessageDetails = getMessageInSession(request);
  	updateParameters(request,pendingMessageDetails);
  		
  	
  	// Retrieve all the message details
  	Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
  	Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
  	String replyText = null;
    String replyTitle = "Re.: ";

    if (request.getParameter(REPLY_ID_PARAMETER) != null)
  	{
  	  // Remove the attachment and send back to the same page
  	  replyText = getFollowupText(pendingMessageDetails,
         request.getParameter(REPLY_ID_PARAMETER),
         sessionDR,true, sessionId);
      String subject = getFollowupTitle(pendingMessageDetails,     
	      request.getParameter(REPLY_ID_PARAMETER),
	      sessionDR,true, sessionId);
      replyTitle += subject;
                                      
  	}
  	System.out.println("ContactHelper.replyToEDetail: replyText="+replyText);
	
	if ( replyText == null)
	{
		replyText = new String("");
	}
    
        message.setTitle(replyTitle);
	String outOfTime = getOutOfTime(sessionDR,recipientList);

	args.put("outOfTime",outOfTime);
  	args.put("followupmsg", replyText);
  	args.put(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, sessionDR, BaseServlet.getWLCookie(request)));
  	args.put(REQUEST_MESSAGE_OBJECT, message);
  	args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
    
  	return("OK");    

    
  	
  } // replyToEDetail
  
  public String handleAttachment(Map args) throws IOException
  {
  	
	  HttpServletRequest request = (HttpServletRequest) args.get("request");
	  HttpServletResponse response = (HttpServletResponse) args.get("response");
	  String ret = null;
      String errorMsg = "";

	    DR sessionDR = (DR) checkSession(request);
	    Map pendingMessageDetails = getMessageInSession(request);

	    // Retrieve all the message details
	    Contact message = (Contact) pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);

	    if ((request.getContentType() == null) ||
	        !request.getContentType().startsWith("multipart/form-data"))
	    {
	      // check for delete url case
	      if (request.getParameter(DELETE_ATTACH_PARAMETER) != null)
	      {
	        // Get the message details from the session - If none found, make one

	        // Remove the parameter specified
	        String deleteItem = request.getParameter(DELETE_ATTACH_PARAMETER);
	        deleteAttachment(message, deleteItem);
			args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
			ret = new String("DELETE"); //,"DR_SendContactCtlr.handleAttachment",args);
	      }
	      else
	      {
	        ret = new String("CHOOSE"); //,"DR_SendContactCtlr.handleAttachment",args);
	      }
	    }
	    else
	    {


	      // Get the MultipartRequest object
	      MultipartHandler mph = (MultipartHandler) args.get("mph");

        // RK Changes 13/11/2001 - Time logging. This is a specific case
        // necessary here because of the MultipartHandler.
        // Obtain the global servletContext and the current executing
        // sub-servlet instance name
        String subServletName = "DR_MasterCtlr";
        String wlCookie = BaseServlet.getWLCookie(request);

        // Time stamp entries
        String onLoadTime = (String) mph.getParameter("onLoadTimeStamp");
        String onSubmitTime = (String) mph.getParameter("onSubmitTimeStamp");
        String pageName = (String) mph.getParameter("pageName");

        try
        {
          if ((onLoadTime != null) && (onSubmitTime != null) && (pageName != null))
          {
            long endTime   = Long.parseLong(onLoadTime);
            long startTime = Long.parseLong(onSubmitTime);
            ThreadLogger.log("\"" + wlCookie + "\",\"" + pageName + "\",\"AccessTimeEnd\",\"" + endTime + "\"");
            ThreadLogger.log("\"" + wlCookie + "\",\"" + subServletName + "\",\"AccessTimeStart\",\"" + startTime + "\"");
          }
        }
        catch (NumberFormatException errNF)
        {
          ThreadLogger.log("\"" + wlCookie + "\",\"none\",\"AccessTimeEnd\",\"-\"");
          ThreadLogger.log("\"" + wlCookie + "\",\"" + subServletName + "\",\"AccessTimeStart\",\"-\"");
        }

          // new MultipartHandler(request);

          Collection tempAttachmentList = new ArrayList();
          
	      // Get each file from the request and build an attachment
	      for (int nLoop = 0; nLoop < MAX_FILE_COUNT; nLoop++)
	        if (mph.getParameter(FILE_PARAMETER_PREFIX + nLoop) != null)
	        {
	          byte contentBytes[] = mph.getRawParameter(FILE_PARAMETER_PREFIX + nLoop);

/*
	          // Handle file type
	          if (fileSubmitted instanceof InputStream)
	          {
	            // Read the content
	            InputStream inContent = (InputStream) fileSubmitted;
	            if (inContent.available() != 0)
	            {
	              contentBytes = new byte[inContent.available()];
	              inContent.read(contentBytes);
	            }
	          }
	          else if (fileSubmitted instanceof String)
            {
              if (!((String) fileSubmitted).equals(""))
                contentBytes = ((String) fileSubmitted).getBytes();
            }
	          else
	            throw new ApplicationError("Unknown attachment class - " + fileSubmitted.getClass());
*/
	          // Build the attachment
	          if ((contentBytes != null) && (contentBytes.length > 0))
	          {
	            System.out.println("ContactMaster.handleAttachment: got "+contentBytes.length+" Bytes");
	            // Check for oversize
	            if (contentBytes.length > MAX_FILE_SIZE)
              {
                errorMsg = SystemMessages.get("dr_fileTooLarge");
                String replacedMsg1 =
                   StringUtils.globalReplace(errorMsg, "[#FILE_NAME]",
                        extractCanonicalName(mph.getFileName(FILE_PARAMETER_PREFIX + nLoop)));
                String replacedMsg2 =
                   StringUtils.globalReplace(replacedMsg1, "[#FILE_MAX_SIZE]", MAX_FILE_SIZE + "");
 	              throw new ValidationException(replacedMsg2);

 	              //throw new ValidationException(
	                //"The file " + extractCanonicalName(mph.getFileName(FILE_PARAMETER_PREFIX + nLoop)) +
	                //"was larger than the maximum allowed file size (" + MAX_FILE_SIZE +
	                //" bytes).");
	            }
	            else
  		        {
	  		        AttachmentLink att = new AttachmentLink();
		  	        String fileName = mph.getFileName(FILE_PARAMETER_PREFIX + nLoop);
			          att.setName(new String(extractCanonicalName(fileName).getBytes("8859_1"), "JISAutoDetect"));
			          att.setContent(contentBytes);
			          att.setAttachmentLinkId("" + message.getAttachmentList().size());
			          tempAttachmentList.add(att);
		          }
	          } // if contentBytes != null
	        } // for (int nloop....

	        if (tempAttachmentList.size() + message.getAttachmentList().size() > MAX_FILE_COUNT)
	        {
	          errorMsg = StringUtils.globalReplace(SystemMessages.get("mr_tooManyFiles"),
	                                      "[#MAX_FILE_COUNT]", "" + MAX_FILE_COUNT);
	          ret = "ERROR"; //super.forwardToTemplate(CHOOSE_TEMPLATE, request, response);
	        }
	        else
	        {
	          message.getAttachmentList().addAll(tempAttachmentList);
		      ret = "SUBMIT"; 
	        }
            
	      // Forward to the Mr5.1 page
	    }

    args.put("errorMessage", errorMsg);
    System.out.println("ContactMaster.handleAttachment: errorMessage="+args.get("errorMessage"));
    args.put(REQUEST_MESSAGE_OBJECT,message);
    args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
    return(ret);

  }
  
  /**
    * Deletes attachments from the session message object if required.
    */
   protected void deleteAttachment(Message message, String deleteAttachmentId)
   {
     // Iterate through and find the linkId matching the deleteLink
     Collection attachments = message.getAttachmentList();
     AttachmentLink removal = null;
     for (Iterator i = attachments.iterator(); i.hasNext();)
     {
       AttachmentLink attachment = (AttachmentLink) i.next();
       if (attachment.getAttachmentLinkId().equals(deleteAttachmentId))
         removal = attachment;
     }
     if (removal != null) attachments.remove(removal);
   }
  
  
   private String extractCanonicalName(String fullPath)
   {
     StringBuffer  result = new StringBuffer(fullPath);
     int           nLength = fullPath.length();
 
     // Check for back slashes (ie windows paths)
     if (fullPath.lastIndexOf('\\') != -1)
       if (nLength > fullPath.lastIndexOf('\\'))
         result.delete(0, fullPath.lastIndexOf('\\') + 1);
       else
         result.delete(0, nLength);
 
     nLength = result.length();
     String searchString = result.toString();
     
     // Check for forward slashes (ie other paths)
     if (searchString.lastIndexOf('/') != -1)
       if (nLength > searchString.lastIndexOf('/'))
         result.delete(0, searchString.lastIndexOf('/') + 1);
       else
         result.delete(0, nLength);
 
     return result.toString();
   }
  
  
  
   protected void dumpAttachments(Contact pendingMessage, String sessionId)
   {
     // Get location from database
     DatabaseConstant location = null;
     try
     {
       AssetManagerRemoteIntf arm = (AssetManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
       location = arm.getConstantById(HttpConstant.ATTACHMENT_REPOSITORY, sessionId);
       if (location == null)
         throw new ApplicationError("Attachment home directory not found");
     }
     catch (RemoteException errRemote)
     {
       throw new ApplicationError("Error retrieving attachment directory from EJB", errRemote);
     }
 
     File publicHtml   = new File(location.getNaiyo3());
     File allRoot      = new File(publicHtml,  location.getNaiyo1());
     File randomFolder = new File(allRoot, "" + new Random().nextInt(9));
 
     try
     {
       for (Iterator i = pendingMessage.getAttachmentList().iterator(); i.hasNext(); )
       {
         AttachmentLink att = (AttachmentLink) i.next();

         if ( att.getFileName() == null )
         {
          // Try to build a file name
          if (!randomFolder.exists())
           randomFolder.mkdirs();
          SimpleDateFormat sdfCustomName = new SimpleDateFormat("0yyyyMMddhhmmssSSS");
          File dumpLocation = File.createTempFile(sdfCustomName.format(new Date()), getExtension(att.getName()).toLowerCase(), randomFolder);
          String dbFileName = dumpLocation.getAbsolutePath().substring(publicHtml.getAbsolutePath().length());
          att.setFileName(changeToForwardSlashes(dbFileName));

         // Write the file out
          FileOutputStream outAttach = new FileOutputStream(dumpLocation);
          outAttach.write(att.getContent());
          outAttach.close();
         } 
       }
     }
     catch (IOException errIO)
     {
       throw new ApplicationError("Error writing attachments to file system", errIO);
     }
   }
  
   private String getExtension(String fileName)
   {
     if (fileName.lastIndexOf('.') != -1)
       return fileName.substring(fileName.lastIndexOf('.'));
     else
       return ".att";
   }
  
   private String changeToForwardSlashes(String input)
   {
     if (input.indexOf(File.separatorChar) != -1)
     {
       int nPos = input.indexOf(File.separatorChar);
       return changeToForwardSlashes(input.substring(0, nPos)) + "/" +
              changeToForwardSlashes(input.substring(nPos + 1));
     }
     else return input;
   }
 
 
   String getOutOfTime(DR sessionDR, Collection recipientList)
   {
   	String outOfTime = null;
   	
	   Map mrProfileMap = sessionDR.getMRProfileMap();
	   Collection outOfTimeMRs = checkRecipientHours(recipientList,mrProfileMap);
	   if ( outOfTimeMRs.size() > 0)
	   {
	   	StringBuffer err = new StringBuffer("");
	   	Iterator iter = outOfTimeMRs.iterator();
	     MR mr = null;
	     while(iter.hasNext())
	     {
	     	mr = (MR) iter.next();
	     	err.append(mr.getMrId()).append(".");
	     }
	     String errstring = err.toString();
	     int length = errstring.length();
	   	outOfTime = errstring.substring(0,length-1);
	   }
	   else
	   {
	   	outOfTime = new String("NO");	
	   }
       return(outOfTime);
   
   }
  
   Collection checkRecipientHours(Collection recipientList, Map mrProfileMap)
   {
   		Collection ret = new Vector();
   		Date now = new Date();
   		Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int dayOfWeek = cal.get(cal.DAY_OF_WEEK);
        int hrOfDay = cal.get(cal.HOUR_OF_DAY);
        int minute = cal.get(cal.MINUTE);
        
        int nowTime = 60*hrOfDay + minute;
        
        Iterator iter = recipientList.iterator();
        
        while(iter.hasNext())
        {
        	boolean flag = false;
            
        	MR mr = (MR) ( (MrProfile) mrProfileMap.get(iter.next())).getMR();
        	String from = (String) mr.getOpenTime();
        	String to =   (String) mr.getCloseTime();
            if ( (from == null) || (to == null) )
            {
            	return ret;	
            }
            System.out.println("ContactHelper.checkRecipientHours: MR="+mr.getMrId()
             +" starts:"+from+" ends "+to);
            StringTokenizer st = new StringTokenizer(from,":");
            String el = st.nextToken();
            int hr = Integer.parseInt(el);
            el = st.nextToken();
            int min = Integer.parseInt(el);
            int fromTime = 60*hr + min;
			
            
            st = new StringTokenizer(to,":");
            el = st.nextToken();
            hr = Integer.parseInt(el);
            el = st.nextToken();
            min = Integer.parseInt(el);
            int toTime = 60*hr + min;

            int workDays = mr.getOpenDays().intValue();
            System.out.println("ContactHelper.checkRecipientHours: nowTime="+nowTime
             +" fromTime:"+fromTime+" toTime "+toTime+" workDays="+workDays
             +" dayOfWeek="+dayOfWeek+" SUNDAY="+cal.SUNDAY);
                        
            if( workDays == 1)
            {
            	if ( dayOfWeek == cal.SATURDAY || dayOfWeek == cal.SUNDAY )
            	{
            		flag = true;
            	}
            }
            else if ( workDays == 2 )
            {
            	if ( dayOfWeek == cal.SUNDAY )
            	{
            		flag = true;
            	}
            }
            if ( ! flag )
            {
            	if ( (nowTime < fromTime) || (nowTime > toTime) )
            	{
            		flag = true;
            	}
            }
            
            
            if ( flag )
            {
            	ret.add(mr);
            }
        } // while (iter.hasNext()
	    return(ret);
    }

  public String refreshAttachments(Map args)
  {
	  HttpServletRequest request = (HttpServletRequest) args.get("request");
	  HttpServletResponse response = (HttpServletResponse) args.get("response");

	  // this should never happen    
	  if ( request == null || response == null )
	  {
	  	throw new ApplicationError("ContactHelper.sendContact: request or response parameter not initialized");
	  }
	  DR sessionDR = (DR) checkSession(request);

	  Map pendingMessageDetails = getMessageInSession(request);
	  updateParameters(request,pendingMessageDetails);

	  // Retrieve all the message details
	  Contact    message            = (Contact)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
	  Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
	  String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

    try
    {
      validateSuppliedParameters(request, pendingMessageDetails);
  	}
    catch (ValidationException errValid)
    {
      request.setAttribute("errorMessage", errValid.getMessage());
    }
    String outOfTime = getOutOfTime(sessionDR,recipientList);
	  args.put("outOfTime",outOfTime);
	  args.put("followupmsg", "");
  	args.put(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, sessionDR, BaseServlet.getWLCookie(request)));
  	args.put(REQUEST_MESSAGE_OBJECT, message);
  	args.put(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());

    return "OK";
  }

} // DR_SendContactCtlr




