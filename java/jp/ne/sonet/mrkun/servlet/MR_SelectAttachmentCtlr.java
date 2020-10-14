
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

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

/**
 * This class is the boundary class for the MR5.7 use case. It forms the
 * controller part of an MVC pattern with the mr5_7.jsp page and the
 * MessageManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_SelectAttachmentCtlr.java,v 1.1.2.23 2001/12/20 08:59:09 rick Exp $
 */
public class MR_SelectAttachmentCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  CHOOSE_TEMPLATE       = HttpConstant.Mr05_7_View;
//  final String  SUBMITTED_TEMPLATE    = HttpConstant.Mr05_1_Ctlr;
  final String  SUBMITTED_TEMPLATE    = HttpConstant.Mr05_7a_View;
  final String  DELETE_TEMPLATE       = HttpConstant.Mr05_7a_View;
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

  // Parameters passed in by the browser
  final String  FILE_PARAMETER_PREFIX   = "file_";
  final String  DELETE_ATTACH_PARAMETER = "deleteAttachment";
  final int     MAX_FILE_COUNT          = 5;
  final int     MAX_FILE_SIZE           = 5242880;

  final String  REQUEST_FROM_5_7        = "from5.7";
  final String  REQUEST_PAGE_MR         = "pageMR";
  final String  REQUEST_ATTACHMENT_LIST = "attachmentList";
  final String  REQUEST_ERROR_MESSAGE   = "errorMessage";

  /**
   * This method handles any kind of request that comes to the
   * servlet. When a standard-encoded request comes in, a submission
   * page is presented. If a multipart encoded request is detected,
   * the contents of the request are saved as attachments to the
   * messages in the session object. Any error handling is done in
   * the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {

    MR sessionMR = null;

    // Check for an MR in session
    sessionMR = (MR) checkSession(request);
    request.setAttribute(REQUEST_PAGE_MR, sessionMR);
    request.setAttribute(REQUEST_ERROR_MESSAGE, "");

    if ((request.getContentType() == null) ||
        !request.getContentType().startsWith("multipart/form-data"))
    {
      // check for delete url case
      if (request.getParameter(DELETE_ATTACH_PARAMETER) != null)
      {
        // Get the message details from the session - If none found, make one
        Map pendingMessageDetails = getMessageInSession(request);

        // Retrieve all the message details
        EDetail message = (EDetail) pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);

        // Remove the parameter specified
        String deleteItem = request.getParameter(DELETE_ATTACH_PARAMETER);
        deleteAttachment(message, deleteItem);
        request.setAttribute(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
        super.forwardToTemplate(DELETE_TEMPLATE, request, response);
      }
      else
        super.forwardToTemplate(CHOOSE_TEMPLATE, request, response);
    }
    else
    {
      // Get the message details from the session - If none found, make one
      Map pendingMessageDetails = getMessageInSession(request);

      // Retrieve all the message details
      EDetail message = (EDetail) pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection tempAttachmentList = new ArrayList();

      // Get the MultipartRequest object
      MultipartHandler mph = new MultipartHandler(request);

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

      // Get each file from the request and build an attachment
      for (int nLoop = 0; nLoop < MAX_FILE_COUNT; nLoop++)
      {
        if (mph.getParameter(FILE_PARAMETER_PREFIX + nLoop) != null)
        {
          byte contentBytes[] = mph.getRawParameter(FILE_PARAMETER_PREFIX + nLoop);

          // Build the attachment
          if ((contentBytes != null) && (contentBytes.length > 0))
          {
            // Check for oversize
            if (contentBytes.length > MAX_FILE_SIZE)
            {
              // Note - this is very inefficient, but I'll change it later
              String errorMsg = SystemMessages.get("mr_fileTooLarge");
              String replacedMsg1 =
                 StringUtils.globalReplace(errorMsg, "[#FILE_NAME]",
                      extractCanonicalName(mph.getFileName(FILE_PARAMETER_PREFIX + nLoop)));
              String replacedMsg2 =
                 StringUtils.globalReplace(replacedMsg1, "[#FILE_MAX_SIZE]", MAX_FILE_SIZE + "");
              throw new ValidationException(replacedMsg2);
                //throw new ValidationException(
                //  "The file " + extractCanonicalName(mph.getFileName(FILE_PARAMETER_PREFIX + nLoop)) +
                //  "was larger than the maximum allowed file size (" + MAX_FILE_SIZE +
                //  " bytes).");
            }
    			  else
            {
              AttachmentLink att = new AttachmentLink();
              String fileName = mph.getFileName(FILE_PARAMETER_PREFIX + nLoop);
              att.setName(new String(extractCanonicalName(fileName).getBytes("8859_1"), "JISAutoDetect"));
              att.setContent(contentBytes);
              att.setAttachmentLinkId("" + (message.getAttachmentList().size() + tempAttachmentList.size()));
              tempAttachmentList.add(att);
            }
          }
        }
      }

      if (tempAttachmentList.size() + message.getAttachmentList().size() > MAX_FILE_COUNT)
      {
        String errorMsg = StringUtils.globalReplace(SystemMessages.get("mr_tooManyFiles"),
                                    "[#MAX_FILE_COUNT]", "" + MAX_FILE_COUNT);
        request.setAttribute(REQUEST_ERROR_MESSAGE, errorMsg);
        super.forwardToTemplate(CHOOSE_TEMPLATE, request, response);
      }
      else
      {
        message.getAttachmentList().addAll(tempAttachmentList);

        // Forward to the Mr5.7a page, that updates the main window
        request.setAttribute(REQUEST_ATTACHMENT_LIST, message.getAttachmentList());
        super.forwardToTemplate(SUBMITTED_TEMPLATE, request, response);
      }
    }
  }

  /**
   * Retrieves the file name only from a full file path. This method is more
   * sophisticated than the default java.io.File method, because it has to
   * cater to the client browser submission of a file name (ie the slashes can
   * be supplied to point either direction).
   */
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
}
