
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.rmi.*;


/**
 * This class is the base to all of the MR 5.x servlets - it contains useful
 * methods they all share.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: EDetailServlet.java,v 1.1.2.13 2001/11/13 07:56:55 rick Exp $
 */
public abstract class EDetailServlet extends BaseServlet
{
  final String  REQUEST_REPLY_TEXT          = "replyMessage";

  /**
   * Retrieve the collection of info about the pending message from the
   * session context. If none is found, the details are created.
   */
  protected Map getMessageInSession(HttpServletRequest request)
  {
    SessionManager session = new SessionManager(request, new Boolean(false));
    Map pendingMessageDetails = (Map) session.getSessionItem(HttpConstant.PENDING_IN_SESSION);
    //UserStateContainer state = UserStateFactory.getState(request);
    //Map pendingMessageDetails = (Map) state.getSessionItem(HttpConstant.PENDING_IN_SESSION);

    if (pendingMessageDetails == null)
    {
      pendingMessageDetails = new Hashtable();
      session.setSessionItem(HttpConstant.PENDING_IN_SESSION, pendingMessageDetails);
      //state.setSessionItem(HttpConstant.PENDING_IN_SESSION, pendingMessageDetails);
    }

    // Make sure there is an edetail object
    if (pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.MESSAGE_IN_SESSION, new EDetail());

    // Make sure there is a recipients collection
    if (pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.RECIPIENTS_IN_SESSION, new ArrayList());

    // Make sure there is an original message Id
    if (pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION, "");

    // Make sure there is a plain text version of the message
    if (pendingMessageDetails.get(HttpConstant.PLAINTEXT_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.PLAINTEXT_IN_SESSION, "");

    // Make sure there is a template
    if (pendingMessageDetails.get(HttpConstant.TEMPLATE_ID_IN_SESSION) == null)
      pendingMessageDetails.put(HttpConstant.TEMPLATE_ID_IN_SESSION, "");

    return pendingMessageDetails;
  }

  /**
   * Deletes links from the session message object if required.
   */
  protected void deleteLink(EDetail message, String deleteLinkId)
  {
    // Iterate through and find the linkId matching the deleteLink
    Collection links = message.getResourceList();
    ResourceLink removal = null;
    for (Iterator i = links.iterator(); i.hasNext();)
    {
      ResourceLink link = (ResourceLink) i.next();
      if (link.getResourceLinkId().equals(deleteLinkId))
        removal = link;
    }
    if (removal != null) links.remove(removal);
  }

  /**
   * Deletes attachments from the session message object if required.
   */
  protected void deleteAttachment(EDetail message, String deleteAttachmentId)
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
   * Retrieves the webImage object from the EJB
   */
  protected WebImage getWebImage(String webImageId, String sessionId)
  {
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                          new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      return mrm.getWebImageById(webImageId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error accessing the MRManager EJB", errRemote);
    }
  }

  /**
   * Retrieves the webImage object from the EJB
   */
  protected Message getMessageById(String messageId, MR userMR, String sessionId)
  {
    try
    {
      //MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
      //                    new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      MessageHelperManager mrm = new MessageHelperManager();
      return mrm.getMessageById(messageId, sessionId);
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error accessing the MRManager EJB", errRemote);
    }
  }

  /**
   * Get a collection of the recipient names that match the ids within this collection
   */
  protected Collection getRecipientNames(Collection recipientList, MR userMR, String sessionId)
  {
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                          new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);

      // Get the recipient names
      Collection recipientNames = new ArrayList();
      for (Iterator i = recipientList.iterator(); i.hasNext(); )
      {
        String drId = (String)i.next();
        DRInformation recipientDRInfo = mrm.getDRInformation(userMR.getMrId(), drId, sessionId);
        if (recipientDRInfo != null)
          recipientNames.add(recipientDRInfo.getName());
        else System.out.println("DRInformation not found MR: " + userMR.getMrId() + " DR: " + drId);
      }
      return recipientNames;
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error accessing the MRManager EJB", errRemote);
    }
  }

  /**
   * Load the template from persistent storage matching the supplied templateID.
   * @param templateId The id of the template to be loaded.
   */
  protected Template loadTemplate(String templateId, String sessionId)
  {
    try
    {
      AssetManagerRemoteIntf tmplMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      return tmplMgr.getByTemplateId(templateId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the AssetManager EJB", errRemote);
    }
  }

  /**
   * Load the links from persistent storage matching the supplied linkIDs.
   * @param linkId The id of the link to be loaded.
   */
  protected Collection loadLinks(Collection linkIds, String sessionId)
  {
    try
    {
      AssetManagerRemoteIntf linkMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      Collection resultLinks = new ArrayList();
      for (Iterator i = linkIds.iterator(); i.hasNext(); )
        resultLinks.add(linkMgr.getByLinkId((String) i.next(), sessionId));
      return resultLinks;
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the AssetManager EJB", errRemote);
    }
  }

  /**
   * Build a reply to a message, setting the follow up text and the recipient id
   */
  protected String getReplyText(Map params, String replyMessageId, MR userMR, boolean reply, String sessionId)
  {
    // Load up the message we're replying to
    try
    {
      //MessageManagerRemoteIntf messMgr = (MessageManagerRemoteIntf)
      //                new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messMgr = new MessageHelperManager();
      Message replyToMsg = messMgr.getMessageById(replyMessageId, sessionId);

      // Set the recipient
      Collection  recipientList = (Collection) params.get(HttpConstant.RECIPIENTS_IN_SESSION);
      EDetail     message       = (EDetail) params.get(HttpConstant.MESSAGE_IN_SESSION);
      recipientList.clear();
      if (reply)
      {
        recipientList.add(replyToMsg.getSender());
        message.setTitle("Re: " + replyToMsg.getTitle());
      }
      else
      {
        message.setTitle(replyToMsg.getTitle());
      }

      // Copy the attachment list across (MRs only receive Contacts, so no links)
      message.getAttachmentList().addAll(replyToMsg.getAttachmentList());
      
      // Set the original message id in the map
      params.put(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION, replyMessageId);

      // return the follow up message
      return (replyToMsg.getBody() == null ? "" : replyToMsg.getBody());
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with the MessageManager EJB", errRemote);
    }

  }

}

