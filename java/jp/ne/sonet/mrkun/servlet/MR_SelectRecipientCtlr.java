
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
 * This class is the boundary class for the MR5.0 use case. It forms the
 * controller part of an MVC pattern with the mr05.jsp page and the
 * MessageManager EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_SelectRecipientCtlr.java,v 1.1.2.24 2001/11/13 07:56:55 rick Exp $
 */
public class MR_SelectRecipientCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  LIST_TEMPLATE         = HttpConstant.Mr05_View;

  // Parameters passed in by the browser
  final String  REQUEST_DRINFO_LIST   = "drInfoList";
  final String  REQUEST_SORT_OPTIONS  = "sortOptions";
  final String  REQUEST_SORT_BY       = "sortBy";
  final String  REQUEST_MESSAGE_CLUMP = "messageId";
  final String  REQUEST_DR_SELECTED   = "selectedDRs";
  final String  REQUEST_FORWARD_TEXT  = "forwardMessage";
  
  final String  SORTBY_PARAMETER              = "sortBy";
  final String  FORWARD_MESSAGE_ID_PARAMETER  = "forwardMessageId";

// hb010821: changed this per spec  final String  DEFAULT_SORT_ORDER            = "name";
  final String  DEFAULT_SORT_ORDER            = "unrecieved";
  final String  REQUEST_PAGE_MR                    = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  
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
    MR sessionMR = null;
    String sessionId = getWLCookie(request);
    
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);

      request.setAttribute(REQUEST_PAGE_MR, sessionMR);

      SessionManager session = new SessionManager(request, new Boolean(false));

      // Get the message details from the session - If none found, make one
      Map pendingMessageDetails = getMessageInSession(request);

      // Check for forwarding
      if (request.getParameter(FORWARD_MESSAGE_ID_PARAMETER) != null)
      {
        // Remove the attachment and send back to the same page
        String forwardText = getReplyText(pendingMessageDetails,
                                          request.getParameter(FORWARD_MESSAGE_ID_PARAMETER),
                                          sessionMR,
                                          false,
                                          sessionId);
        request.setAttribute(REQUEST_FORWARD_TEXT, (forwardText==null?"":forwardText));
      }
      else
        request.setAttribute(REQUEST_FORWARD_TEXT, "");

      // Retrieve all the message details
      EDetail    message            = (EDetail)    pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
      Collection recipientList      = (Collection) pendingMessageDetails.get(HttpConstant.RECIPIENTS_IN_SESSION);
      String     originalMessageId  = (String)     pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);

      // Set the collection of sort options etc
      setupComboLists(request);
      
      // Process sort order
      String requestedOrder = request.getParameter(SORTBY_PARAMETER);
      String sortOrder      = DEFAULT_SORT_ORDER;

	  if ((requestedOrder != null) &&
        ((Collection) request.getAttribute(REQUEST_SORT_OPTIONS)).contains(requestedOrder))
      sortOrder = requestedOrder;

      // Build a list of DRInfo objects and the message support clump (also
      // known as the 5 object array used to pass back message details for a DR)
      Map messageCounts = new Hashtable();
      Collection drInfoList = getSortedDrList(sessionMR, sortOrder, messageCounts, getWLCookie(request));

      // Send on to the jsp view
      request.setAttribute(REQUEST_DRINFO_LIST, drInfoList);
      request.setAttribute(REQUEST_MESSAGE_CLUMP, messageCounts);
      request.setAttribute(REQUEST_DR_SELECTED, recipientList);
      request.setAttribute(REQUEST_SORT_BY, sortOrder);
      super.forwardToTemplate(LIST_TEMPLATE, request, response);        
  }

  /**
   * Produces a list containing the DRInformation objects sorted in the
   * requested order. It also modifies a map which contains the
   * unread message detail for each DR.
   * @param userMR The MR to filter for.
   * @param sortOrder The order in which to return the DRInformation list.
   */
  protected Collection getSortedDrList(MR userMR, String sortOrder, Map messageCounts, String sessionId)
    throws RemoteException
  {
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                        new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      MessageHelperManager mhm = new MessageHelperManager();
      //Collection unsortedDRInfoList = mrm.getDRInformationList(userMR.getMrId());
      //messageCounts.putAll(mhm.getMRMessageCount(userMR, ""));
      Collection unsortedDRInfoList = mrm.getDRInformationList(userMR.getMrId(), messageCounts, sessionId);

      // Handle the messageClump
/*
      for (Iterator drList = unsortedDRInfoList.iterator(); drList.hasNext();)
      {
        DRInformation dr = (DRInformation) drList.next();
        DRMessageCount drm = (DRMessageCount) messageCounts.get(dr.getDrId());
        if (drm != null)
        {
          dr.setActionValue(new Integer(drm.getActionValue()));
          dr.setReceivedMessage(new Integer(drm.getNewContacts()));
          dr.setUnreadSentMessage(new Integer(drm.getNewEDetails()));
        }
        else
        {
          dr.setActionValue(new Integer(1));
          dr.setReceivedMessage(new Integer(0));
          dr.setUnreadSentMessage(new Integer(0));
        }
      }
*/
      // Sort the list
      Object drArray[] = unsortedDRInfoList.toArray();
      DRInfoComparator drc = new DRInfoComparator(sortOrder, !sortOrder.equals("unread"));
      Arrays.sort(drArray, drc);

      // Return the sorted list
      return Arrays.asList(drArray);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Remote error using the MRManager bean", errRemote);
    }
  }

  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  protected void setupComboLists(HttpServletRequest request)
  {
    Collection colSortBy = new ArrayList();
    colSortBy.add("name");
    colSortBy.add("hospital");
    colSortBy.add("specialty");
    colSortBy.add("importance");
    colSortBy.add("unrecieved");
    request.setAttribute(REQUEST_SORT_OPTIONS, colSortBy);
  }
}

