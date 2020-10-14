
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
import java.io.*;
import java.util.*;
import java.rmi.*;
import java.text.*;

/**
 * This class is the boundary class for the MR5.1 use case. It forms the
 * controller part of an MVC pattern with the mr5_1.jsp page and the
 * MessageManager EJB.
 * Note: This servlet is extremely complex and should be better documented.
 * Please read the doAction method code to get a better idea.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_WriteDetailCtlr.java,v 1.1.2.26 2001/12/17 04:42:40 rick Exp $
 */
public class MR_WriteDetailCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE              = HttpConstant.Mr09_View;
  final String  EDIT_TEMPLATE               = HttpConstant.Mr05_1_View;
  final String  CONFIRM_TEMPLATE            = HttpConstant.Mr05_6_View;
  final String  CONFIRM_CTLR                = HttpConstant.Mr05_6_Ctlr;

  // Parameters passed in by the browser
  final String  REDIRECT_PARAMETER          = "redirect";
  final String  DELETE_LINK_PARAMETER       = "deleteLink";
  final String  DELETE_ATTACHMENT_PARAMETER = "deleteAttachment";
  final String  RECIPIENT_PREFIX            = "addressee_";
  final String  IMAGE_PARAMETER             = "image";
  final String  HTML_BODY_PARAMETER         = "htmlBody";
  final String  PLAIN_BODY_PARAMETER        = "plainBody";
  final String  TITLE_PARAMETER             = "title";
  final String  CATEGORY_PARAMETER          = "category";
  final String  EXPIRY_YEAR_PARAMETER       = "expiryYear";
  final String  EXPIRY_MONTH_PARAMETER      = "expiryMonth";
  final String  EXPIRY_DAY_PARAMETER        = "expiryDay";
  final String  REPLY_ID_PARAMETER          = "replyId";
  final String  FORWARD_MESSAGE_PARAMETER   = "forwardMessage";
  
  // Parameters to pass back to the JSP
  final String  REQUEST_ERROR_MESSAGE       = "errorMessage";
  final String  REQUEST_WEBIMAGE_LIST       = "photoList";
  final String  REQUEST_CATEGORIES          = "categoryList";
  final String  REQUEST_RECIPIENT_NAMES     = "recipientNames";
  final String  REQUEST_MESSAGE_OBJECT      = "message";
  final String  REQUEST_ATTACHMENT_LIST     = "attachmentList";
  final String  REQUEST_LINK_LIST           = "linkList";
  final String  REQUEST_FROM_5_7            = "from5.7";
  final String  REQUEST_PAGE_MR             = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  
  /**
   * This method handles any kind of request that comes to the
   * servlet. This servlet acts as a router for a number of other use cases,
   * so this method handles requests from all of the referrers. Any error
   * handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    MR sessionMR = null;
    String sessionId = this.getWLCookie(request);

    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
    
      request.setAttribute(REQUEST_PAGE_MR, sessionMR);
      request.setAttribute(REQUEST_REPLY_TEXT, "");
      request.setAttribute(REQUEST_ERROR_MESSAGE, "");

      // Get the message details from the session - If none found, make one
      Map pendingMessageDetails = getMessageInSession(request);

      // Retrieve all the message details
      EDetail    message            = (EDetail)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

      // Check for whether this message is to be previewed for sending or just
      // continue editing
      if (request.getParameter(REDIRECT_PARAMETER) != null)
      {
        String redirectTo = request.getParameter(REDIRECT_PARAMETER);
        try
        {
          // Validate parameters supplied
          setupComboLists(request, sessionMR, recipientList, message, sessionId);
          validateSuppliedParameters(request, pendingMessageDetails);

          // Send on to the jsp view
          super.forwardToTemplate(redirectTo, request, response);
        }
        catch (ValidationException errValid)
        {
          // Display the same page, but with a suggestion about the error field
          if (redirectTo.equals(CONFIRM_CTLR))
          {
            request.setAttribute(REQUEST_ERROR_MESSAGE, errValid.getMessage());
            super.forwardToTemplate(EDIT_TEMPLATE, request, response);
            //throw new ApplicationError("Validation error", errValid);
          }
          else
            //throw new ApplicationError("Validation error", errValid);
            super.forwardToTemplate(redirectTo, request, response);
        }
      }
      else if (request.getParameter(DELETE_LINK_PARAMETER) != null)
      {
        // Remove the link and send back to the same page
        deleteLink(message, request.getParameter(DELETE_LINK_PARAMETER));
        setupComboLists(request, sessionMR, recipientList, message, sessionId);
        super.forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
      else if (request.getParameter(DELETE_ATTACHMENT_PARAMETER) != null)
      {
        // Remove the attachment and send back to the same page
        deleteAttachment(message, request.getParameter(DELETE_ATTACHMENT_PARAMETER));
        setupComboLists(request, sessionMR, recipientList, message, sessionId);
        super.forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
      else if (request.getParameter(REPLY_ID_PARAMETER) != null)
      {
        // Remove the attachment and send back to the same page
        String replyText = getReplyText(pendingMessageDetails,
                                        request.getParameter(REPLY_ID_PARAMETER),
                                        sessionMR,
                                        true,
                                        sessionId);
        request.setAttribute(REQUEST_REPLY_TEXT, replyText);
        setupComboLists(request, sessionMR, recipientList, message, sessionId);
        super.forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
      else if (request.getParameter(FORWARD_MESSAGE_PARAMETER) != null)
      {
        // Remove the attachment and send back to the same page
        String forwardText = StringUtils.getParameter(request, FORWARD_MESSAGE_PARAMETER);
        request.setAttribute(REQUEST_REPLY_TEXT, forwardText);
        updateRecipientList(request, recipientList);
        setupComboLists(request, sessionMR, recipientList, message, sessionId);
        super.forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
      else
      {
        // Handle recipient lists supplied if there is one
        updateRecipientList(request, recipientList);
        setupComboLists(request, sessionMR, recipientList, message, sessionId);
        super.forwardToTemplate(EDIT_TEMPLATE, request, response);
      } 
  }

  /**
   * Get any recipients from the request object. If there aren't any, leave
   * it in tact.
   * @param request The HTTP request object
   * @param recipientList The list of DrIds to send the message to
   */
  protected void updateRecipientList(HttpServletRequest request, Collection recipients)
  {
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
    EDetail     message       = (EDetail)    pendingMessage.get(HttpConstant.MESSAGE_IN_SESSION);
    Collection  recipientList = (Collection) pendingMessage.get(HttpConstant.RECIPIENTS_IN_SESSION);

    // Get all the parameters from the request
    String testHTMLBody   = StringUtils.getParameter(request, HTML_BODY_PARAMETER);
    String testPlainBody  = StringUtils.getParameter(request, PLAIN_BODY_PARAMETER);
    String testTitle      = StringUtils.getParameter(request, TITLE_PARAMETER);
    String testCategory   = StringUtils.getParameter(request, CATEGORY_PARAMETER);
    String testImageId    = StringUtils.getParameter(request, IMAGE_PARAMETER);
    String testExpYear    = StringUtils.getParameter(request, EXPIRY_YEAR_PARAMETER);
    String testExpMonth   = StringUtils.getParameter(request, EXPIRY_MONTH_PARAMETER);
    String testExpDay     = StringUtils.getParameter(request, EXPIRY_DAY_PARAMETER);

    String finalErrorMessage = "";

    // Check the html body
    if ((testHTMLBody == null) ||
        testHTMLBody.equals(""))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoBodyProvided");//"No message body supplied";
    }
    else if (testPlainBody.equals(""))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoPlainBodyProvided");//"No plain text message body supplied";
    }
    else if ((testHTMLBody.length() >= 4000) &&
             (testPlainBody.length() >= 4000))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetMessageBodyTooLong");//"Please make sure the message is no longer than 4000 characters";
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
        finalErrorMessage = SystemMessages.get("mr_newEdetNoTitleProvided");//"No message title supplied";
    }
    else
    {
      message.setTitle(testTitle);
    }

    // Check the category
    if ((testCategory == null) ||
        (testCategory.equals("")))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoMessageCategoryProvided");//"No message category supplied";
    }
    else
    {
      message.setEDetailCategory(testCategory);
    }

    // Check the image id
    if ((testImageId == null) ||
             (testImageId.equals("")))
    {
      //if (finalErrorMessage.equals(""))
      //  finalErrorMessage = SystemMessages.get("mr_newEdetNoImageProvided");//"No MR image supplied";
      message.setImage(null);
    }
    else
    {
      message.setImage(getWebImage(testImageId, getWLCookie(request)));
    }

    // Check the expiry date
/*
    if ((testExpYear == null) ||
             (testExpYear.equals("-")))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = "No expiry year supplied";
    }
    else if ((testExpMonth == null) ||
             (testExpMonth.equals("-")))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = "No expiry month supplied";
    }
    else if ((testExpDay == null) ||
             (testExpDay.equals("-")))
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = "No expiry day supplied";
    }
    else
    {
      // Try and build a date out of the expiry parts
      Date expiryDate = null;
      try
      {
        DateFormat sdfTest = new SimpleDateFormat("d/M/yyyy");
        expiryDate = sdfTest.parse(testExpDay + "/" + testExpMonth + "/" + testExpYear);

        if (!expiryDate.after(new Date()))
        {
          if (finalErrorMessage.equals(""))
            finalErrorMessage = "The expiry date must be in the future.";
        }
        else
        {
          message.setExpireDate(expiryDate);
        }
      }
      catch (ParseException errDate)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = "Invalid date specified for expiry date.";
      }
    }
*/

    if (testExpYear == null)
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoExpYearProvided");//"No expiry year supplied";
    }
    else if (testExpMonth == null)
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoExpMonthProvided");//"No expiry month supplied";
    }
    else if (testExpDay == null)
    {
      if (finalErrorMessage.equals(""))
        finalErrorMessage = SystemMessages.get("mr_newEdetNoExpDayProvided");//"No expiry day supplied";
    }
    else if (testExpYear.equals("-") || testExpMonth.equals("-") || testExpDay.equals("-"))
    {
      message.setExpireDate(null);
    }
    else
    {
      // Try and build a date out of the expiry parts
      Date expiryDate = null;
      try
      {
        DateFormat sdfTest = new SimpleDateFormat("d/M/yyyy");
        expiryDate = sdfTest.parse(testExpDay + "/" + testExpMonth + "/" + testExpYear);

        if (!expiryDate.after(new Date()))
        {
          if (finalErrorMessage.equals(""))
            finalErrorMessage = SystemMessages.get("mr_newEdetOldExpiryProvided");//"The expiry date must be in the future.";
        }
        else
        {
          message.setExpireDate(expiryDate);
        }
      }
      catch (ParseException errDate)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_newEdetInvalidExpiry");//"Invalid date specified for expiry date.";
      }
    }

    if (recipientList.size() == 0)
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_newEdetNoRecipients");//"No recipients supplied.";

    if (!finalErrorMessage.equals(""))
    {
System.out.println("Throwing validation error message: " + finalErrorMessage);
      throw new ValidationException(finalErrorMessage);
    }
  }

  /**
   * Builds the combo list items for the page
   */
  protected void setupComboLists(HttpServletRequest request,
                                 MR                 userMR,
                                 Collection         recipientList,
                                 EDetail            message,
                                 String             sessionId)
  {
    try
    {
      // Get a list of webImages for this MR
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                          new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      Collection webImages = mrm.getWebImagesByMrId(userMR.getMrId(), sessionId);
      request.setAttribute(REQUEST_WEBIMAGE_LIST, webImages);

      request.setAttribute(REQUEST_RECIPIENT_NAMES, getRecipientNames(recipientList, userMR, sessionId));

      // Get the edetail categories
      AssetManagerRemoteIntf arm = (AssetManagerRemoteIntf)
                          new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
    request.setAttribute(REQUEST_CATEGORIES, arm.getEDetailCategories(userMR.getCompany().getCompanyId(), sessionId));
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error accessing the MRManager EJB", errRemote);
    }


    request.setAttribute(REQUEST_MESSAGE_OBJECT, message);
    request.setAttribute(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
    request.setAttribute(REQUEST_LINK_LIST, message.getResourceList());
  }

}

