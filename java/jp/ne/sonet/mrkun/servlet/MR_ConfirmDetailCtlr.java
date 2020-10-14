
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
 * This class is the boundary class for the MR5.6 use case. It forms the
 * controller part of an MVC pattern with the mr5_6.jsp page and the
 * MessageManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_ConfirmDetailCtlr.java,v 1.1.2.27 2001/12/20 08:59:09 rick Exp $
 */
public class MR_ConfirmDetailCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE          = HttpConstant.Mr09_View;
  final String  PREVIEW_TEMPLATE        = HttpConstant.Mr05_6_View;
  final String  SENT_TEMPLATE           = HttpConstant.Mr02_Ctlr;

  // Request parameters needed to build the JSP
  final String  REQUEST_MESSAGE         = "message";
  final String  REQUEST_ATTACHMENT_LIST = "attachmentList";
  final String  REQUEST_LINK_LIST       = "linkList";
  final String  REQUEST_RECIPIENT_LIST  = "recipientList";
  final String  REQUEST_PAGE_MR         = "pageMR";
  final String  REQUEST_CATEGORY_TEXT   = "categoryText";
  
  final String  SESSION_ERROR_MESSAGE   = "sessionErrorMessage";

  // Parameters passed in by the browser
  final String  SEND_PARAMETER          = "send";
  final String  SEND_VALUE              = "true";
  final String  PICTURE_PARAMETER       = "picture";
  final String  ATTACHMENT_PARAMETER    = "attachmentId";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It first displays the details of the message and
   * allows confirmation, then on confirmation it duplicates the
   * message for each recipient and saves to the database.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    MR sessionMR = null;
    String sessionId = getWLCookie(request);
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
   
      request.setAttribute(REQUEST_PAGE_MR, sessionMR);
      Map messageData = getMessageInSession(request);

      EDetail    pendingMessage = (EDetail) messageData.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList  = (Collection) messageData.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     plainText      = (String) messageData.get(HttpConstant.PLAINTEXT_IN_SESSION);

      // Check for the attachment parameter
      if (request.getParameter(ATTACHMENT_PARAMETER) != null)
      {
        returnAttachment(response,
                         pendingMessage.getAttachmentList(),
                         request.getParameter(ATTACHMENT_PARAMETER));
      }
      // Check for send parameter
      else if (request.getParameter(SEND_PARAMETER) != null)
      {
        // Dump files to file system
        dumpAttachments(pendingMessage,
                        sessionMR.getCompany(), sessionId);

        // Send message
        sendMessage(sessionMR, request, sessionId);

        // Forward back to MR2.0 screen
        super.forwardToTemplate(SENT_TEMPLATE, request, response);
      }
      else
      {
        // Get the eDetail category
        request.setAttribute(REQUEST_CATEGORY_TEXT, getCategoryName(pendingMessage.getEDetailCategory(), sessionMR.getCompany().getCompanyId(), sessionId));

        request.setAttribute(REQUEST_MESSAGE, pendingMessage);
        request.setAttribute(REQUEST_ATTACHMENT_LIST, pendingMessage.getAttachmentList());
        request.setAttribute(REQUEST_LINK_LIST, pendingMessage.getResourceList());
        request.setAttribute(REQUEST_RECIPIENT_LIST, getRecipientNames(recipientList, sessionMR, getWLCookie(request)));
        super.forwardToTemplate(PREVIEW_TEMPLATE, request, response);
      }
  }

  /**
   * Send the message within the session scope to each of the recipients
   * within the recipient list (also in the session).
   * @param request The HTTP request object
   */
  private void sendMessage(MR userMR, HttpServletRequest request, String sessionId)
  {
    try
    {
      // Get the recipient list and message to be sent
      Map messageData = getMessageInSession(request);

      EDetail    pendingMessage = (EDetail) messageData.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList  = (Collection) messageData.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     plainText      = (String) messageData.get(HttpConstant.PLAINTEXT_IN_SESSION);

      // If the user has inadvertedly clicked twice, ignore the second time
      if (recipientList.isEmpty()) return;

      // Get the MessageManager EJB
      //MessageManagerRemoteIntf messmgr = (MessageManagerRemoteIntf)
      //                new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager mmh = new MessageHelperManager();

      System.out.println("Sending message:");
      System.out.println("message body:" + pendingMessage.getBody());
      System.out.println("plain text:" + plainText);
      System.out.println("recipient count:" + recipientList.size());

      mmh.addMessage(userMR, recipientList, pendingMessage, plainText, sessionId);

      // Send a notify mail
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      if (userMR.getCCEmail() != null)
        for (Iterator i = recipientList.iterator(); i.hasNext(); )
          mrm.notifyBigBrotherEDetail(userMR, (String) i.next(), pendingMessage, plainText, getWLCookie(request));

      // Remove the message from the session now that it has been sent
      messageData.clear();
    }
    catch (Exception errRemote)
    {
      StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errRemote.printStackTrace(p);
      throw new ApplicationError("Error using the MessageManager EJB - " + s.toString(), errRemote);
    }
  }

  /**
   * This method writes all the attachment objects to the file system.
   */
  protected void dumpAttachments(EDetail pendingMessage, Company company, String sessionId)
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
    File companyRoot  = new File(allRoot,     company.getCompanyPrefix().toLowerCase());
    File randomFolder = new File(companyRoot, "" + new Random().nextInt(9));

    try
    {
      for (Iterator i = pendingMessage.getAttachmentList().iterator(); i.hasNext(); )
      {
        AttachmentLink att = (AttachmentLink) i.next();

        // Skip this element if the attachment is a forwarded one
        if (att.getFileName() == null)
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

  protected String getCategoryName(String categoryId, String companyId, String sessionId)
  {
    // Get an AssetManager
    try
    {
      AssetManagerRemoteIntf assetMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      return assetMgr.getEDetailCategoryById(categoryId, companyId, sessionId).getName();
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the AssetManager EJB", errRemote);
    }
  }

  protected void returnAttachment(HttpServletResponse response,
                                  Collection          attachmentList,
                                  String              attachmentId)
  {
    // Iterate through attachments until you find one that matches the id
    AttachmentLink current = null;
    for (Iterator i = attachmentList.iterator(); i.hasNext(); )
    {
      current = (AttachmentLink) i.next();
      if (current.getAttachmentLinkId().equals(attachmentId))
        break;
    }

    // If not found then throw exception
    if (!current.getAttachmentLinkId().equals(attachmentId))
      throw new ApplicationError("The attachment requested was not found.");
    else
    {
      response.setHeader("Content-Disposition", "attachment; filename=\"" + current.getName() + "\"");
      response.setContentType("application/octet-stream");
      response.setContentLength(current.getContent().length);
      try
      {
        OutputStream out = response.getOutputStream();
        out.write(current.getContent());
        out.close();
      }
      catch (IOException errIO)
      {
        throw new ApplicationError("Error writing the requested attachment " +
          "back for verification", errIO);
      }
    }
  }
}

