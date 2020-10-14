
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
 * This class is the boundary class for the MR3.3 use case. It forms the
 * controller part of an MVC pattern with the mr03_3.jsp page and the
 * MessageManager EJB.<br/>
 * It's main purpose it to allow the deletion of messages.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: SelectMessageCtlr.java,v 1.1.2.14 2001/11/13 07:56:55 rick Exp $
 */
public class SelectMessageCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  REVIEW_TEMPLATE       = HttpConstant.Mr03_3_View;
  final String  COMPLETED_TEMPLATE    = HttpConstant.Mr03_Ctlr;

  // Request parameters needed to build the JSP
  final String  REQUEST_DRINFO        = "drInfo";
  final String  REQUEST_PAGE_MR       = "pageMR";
  final String  REQUEST_MESSAGE_LIST  = "messageList";

  // Parameters passed in by the browser
  final String  DRID_PARAMETER        = "drId";
  final String  MESSAGE_ID_PREFIX     = "delete_";
  final String  MESSAGE_ID_VALUE      = "true";
  final String  CONFIRM_DEL_PARAMETER = "confirmDelete";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which lists all the messages
   * you selected for deletion, asking for an ok. Any error handling
   * is done in the base class service method.
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
    
      if (request.getParameter(DRID_PARAMETER) == null)
        throw new ApplicationError("No DRInformation Id supplied");
      else
      {
        // Set up the DRInfo - this is needed for all destination pages
        String drId = request.getParameter(DRID_PARAMETER);
        DRInformation drInfo = getDrInfo(sessionMR.getMrId(), drId, sessionId);
        request.setAttribute(REQUEST_DRINFO, drInfo);
        request.setAttribute(REQUEST_PAGE_MR, sessionMR);

        // Get the list of items requested for deletion
        Collection deleteList = buildMessageIdList(request);
        if (deleteList == null)
          super.forwardToTemplate(COMPLETED_TEMPLATE, request, response);
        // Check whether we are just reviewing or actually deleting
        else if (request.getParameter(CONFIRM_DEL_PARAMETER) == null)
        {
          // Set up for review of deletable items
          Collection messageList = loadMessages(sessionMR, deleteList, sessionId);
          request.setAttribute(REQUEST_MESSAGE_LIST, messageList);
          super.forwardToTemplate(REVIEW_TEMPLATE, request, response);
        }
        else
        {
          // Perform the actual deletion
          removeMessages(sessionMR, deleteList, sessionId);
          super.forwardToTemplate(COMPLETED_TEMPLATE, request, response);
        }
      }     
  }

  /**
   * Builds a list of the messageIds supplied in the request object
   * and returns them as a collection.
   */
  private Collection buildMessageIdList(HttpServletRequest request)
  {
    Collection messageIdList = new ArrayList();
    Enumeration params = request.getParameterNames();
    while (params.hasMoreElements())
    {
      String paramName = (String) params.nextElement();
      if (paramName.startsWith(MESSAGE_ID_PREFIX) &&
          request.getParameter(paramName).equals(MESSAGE_ID_VALUE))
        messageIdList.add(paramName.substring(MESSAGE_ID_PREFIX.length()));
    }
    return (messageIdList.isEmpty()?null:messageIdList);
  }

  /**
   * Requests the messages specified by the ids within a collection,
   * then puts the messages into the request object.
   * @param messageIdList The list of messageIds to be retrieved.
   * @retruns The list of messages.
   */
  private Collection loadMessages(MR userMR, Collection messageIdList, String sessionId)
  {
    try
    {
      // Get a reference to the MessageManager EJB
      //MessageManagerRemoteIntf messmgr =
      //      (MessageManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messmgr = new MessageHelperManager();
      return messmgr.getMessages(messageIdList, sessionId);
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with MessageManager EJB", errRemote);
    }
  }

  /**
   * Removes the messages specified in the collection.
   * @param messageIdList A list of messageIds that re to be removed.
   */
  private void removeMessages(MR userMR, Collection messageIdList, String sessionId)
  {
    try
    {
      // Get a reference to the MessageManager EJB
      //MessageManagerRemoteIntf messmgr =
      //      (MessageManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);
      MessageHelperManager messmgr = new MessageHelperManager();
      messmgr.removeMessages(messageIdList, sessionId);
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
}


