
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

/**
 * This class is the boundary class for the MR3.1 use case. It forms the
 * controller part of an MVC pattern with the mr03_1.jsp page and the
 * MessageManager EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DetailMessageCtlr.java,v 1.1.2.22 2001/11/13 07:56:55 rick Exp $
 */
public class DetailMessageCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  RETRACTED_TEMPLATE    = HttpConstant.Mr03_Ctlr;
  final String  SENT_TEMPLATE         = HttpConstant.Mr03_1_View;
  final String  RECEIVED_TEMPLATE     = HttpConstant.Mr03_2_View;
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

  // Request parameters needed to build the JSP
  final String  REQUEST_DRINFO          = "drInfo";
  final String  REQUEST_MESSAGE         = "message";
  final String  REQUEST_ATTACH_LIST     = "attachmentList";
  final String  REQUEST_LINK_LIST       = "linkList";
  final String  REQUEST_UNREAD_COUNT    = "unreadMessages";
  final String  REQUEST_UNREAD_MESSAGE  = "unreadMessageId";
  final String  REQUEST_PAGE_MR         = "pageMR";

  // Parameters passed in by the browser
  final String  MESSAGE_ID_PARAMETER  = "msgId";
  final String  DRID_PARAMETER        = "drId";
  final String  RETRACT_PARAMETER     = "retract";
  
  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays a specific message.
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
    
      if (request.getParameter(MESSAGE_ID_PARAMETER) == null)
        throw new ApplicationError("No message id supplied");
      else
      {
        request.setAttribute(REQUEST_PAGE_MR, sessionMR);
        request.setAttribute(REQUEST_UNREAD_MESSAGE, "");

        // Get the message ID requested
        String messageId = request.getParameter(MESSAGE_ID_PARAMETER);

        // If the message is not loadable, throw an error
        if ((messageId == null) || !loadMessage(request, sessionMR, messageId, sessionId))
          super.forwardToTemplate(ERROR_TEMPLATE, request, response);
        else
        {
          // Otherwise, load it and send to appropriate JSP view
          Message msg = (Message) request.getAttribute(REQUEST_MESSAGE);

          if (request.getParameter(RETRACT_PARAMETER) != null)
          {
            retractMessage(msg, sessionId);
            forwardToTemplate(RETRACTED_TEMPLATE, request, response);
          }
          else if (msg.getSender().equals(sessionMR.getMrId()))
          {
            // Setup the drInfo object
            request.setAttribute(REQUEST_DRINFO, getDrInfo(sessionMR.getMrId(), msg.getRecipient(), getWLCookie(request)));
            forwardToTemplate(SENT_TEMPLATE, request, response);
          }
          else if (msg.getRecipient().equals(sessionMR.getMrId()))
          {
            request.setAttribute(REQUEST_DRINFO, getDrInfo(sessionMR.getMrId(), msg.getSender(), getWLCookie(request)));
            forwardToTemplate(RECEIVED_TEMPLATE, request, response);
          }
      }
    }
  }

  /**
   * Requests a specific message by Id, then deposits the
   * details required by the rendering page into the request object.
   * @param request The HTTP request object.
   * @param userMR The MR requesting the message.
   * @param messageId The Id of the message to be retrieved.
   * @returns True if the message was loaded successfully
   */
  private boolean loadMessage(HttpServletRequest request,
                              MR                 userMR,
                              String             messageId,
                              String             sessionId)
  {
    try
    {
      // Get a reference to the MessageManager EJB
      //MessageManagerRemoteIntf messmgr =
      //      (MessageManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messmgr = new MessageHelperManager();
      Message msg = messmgr.getMessageById(messageId, sessionId);

      if (msg != null)
      {
        // Confirm the set date
        if ((msg instanceof Contact) && msg.getReadDate() == null)
          messmgr.setMessageAsRead(msg, sessionId);

        // Set up all the passback variables
        request.setAttribute(REQUEST_MESSAGE, msg);
        request.setAttribute(REQUEST_ATTACH_LIST, msg.getAttachmentList());
        if (msg.getSender().equals(userMR.getMrId()))
          request.setAttribute(REQUEST_LINK_LIST, ((EDetail) msg).getResourceList());
        else
        {
		      Integer availableMessages =
                messmgr.getAvailableMessages(userMR.getMrId(),
                                             msg.getSender(),
                                             true,
                                             MessageHelperManager.CONTACTS_ONLY,
                                             sessionId);
          request.setAttribute(REQUEST_UNREAD_COUNT, availableMessages);

          if (availableMessages.intValue() == 1)
          {
            // Get the message id
            Collection availableMessageList = messmgr.getMessages
                (userMR.getMrId(),msg.getSender(), "F1", new Integer(0), new Integer(1), true, MessageHelperManager.CONTACTS_ONLY, sessionId);
            if (availableMessageList.size() == 1)
            {
              Iterator i = availableMessageList.iterator();
              request.setAttribute(REQUEST_UNREAD_MESSAGE,
                                   ((Message) i.next()).getMessageId());
            }
            else
              throw new ApplicationError("More than one unread message available");
          }
        }
      }
      return (msg != null);
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with MessageManager EJB", errRemote);
    }
  }

  /**
   * Get a DRInformation object for a particular MR and DR
   */
  private DRInformation getDrInfo(String mrId, String drId, String sessionId)
  {
    try
    {
      // Load the manager bean
      MRManagerRemoteIntf mrMgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      return mrMgr.getDRInformation(mrId, drId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
  }

  /**
   * Get a DRInformation object for a particular MR and DR
   */
  private void retractMessage(Message retractMessage, String sessionId)
  {
    try
    {
      // Load the manager bean
      MessageHelperManager messmgr = new MessageHelperManager();
      messmgr.retractMessage(retractMessage, sessionId);
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
  }
}

