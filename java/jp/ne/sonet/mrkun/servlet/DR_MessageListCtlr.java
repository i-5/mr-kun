
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
 * This class is the boundary class for the DR3.0 use case. It forms the
 * controller part of an MVC pattern with the dr03.jsp page and the
 * MessageManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DR_MessageListCtlr.java,v 1.1.2.5 2001/11/13 07:56:55 rick Exp $
 */
public class DR_MessageListCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Dr03_View;

  // Request parameters needed to build the JSP
  final String  REQUEST_MRPROFILE     = "mrProfile";
  final String  REQUEST_MESSAGE_LIST  = "messageList";
  final String  REQUEST_SORT_OPTIONS  = "sortOptions";
  final String  REQUEST_SELECTED_PAGE = "selectedPage";
  final String  REQUEST_MAX_PAGES     = "maxPages";
  final String  REQUEST_SORT_FIELD    = "sortByField";
  final String  REQUEST_PAGE_DR       = "pageDR";

  // Parameters passed in by the browser
  final String  PAGE_PARAMETER        = "pageNo";
  final String  SORT_BY_PARAMETER     = "sortBy";
  final String  MRID_PARAMETER        = "mrId";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final Integer DEFAULT_SELECTED_PAGE = new Integer(0);
  final String  DEFAULT_SORT_ORDER    = "date";
  final int     MESSAGES_PER_PAGE     = 15;

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays a list of Message details.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  { 
    String mrId = "";
    DR sessionDR = null;
    String sessionId = getWLCookie(request);
    // Check for an MR in session
      sessionDR = (DR) checkSession(request);
    
      if ((request.getParameter(MRID_PARAMETER) == null) &&
             (request.getAttribute(REQUEST_MRPROFILE) == null))
        throw new ApplicationError("No MRProfile object supplied");
      else
      {
        // Get the mrProfile object
        MrProfile mrProfile = null;
        if (request.getParameter(MRID_PARAMETER) == null)
        {
          // This is provided for the case of forwarding from another servlet
          mrProfile = (MrProfile) request.getAttribute(REQUEST_MRPROFILE);
        }
        else
        {
          // Get the mrProfile from the request parameters
          mrId = request.getParameter(MRID_PARAMETER);
          mrProfile = getMRProfile(sessionDR.getDrId(), mrId, getWLCookie(request));
        }

        // Check it's a valid DRInformation
        if (mrProfile == null)
          throw new ApplicationError("No MrProfile available");
        else
        {
          setupComboLists(request);

          // Get the sorting field if available
          String sortByField = request.getParameter(SORT_BY_PARAMETER);
          if (sortByField == null)
            sortByField = DEFAULT_SORT_ORDER;

          // Work out the current page number
          Integer pageSelected = DEFAULT_SELECTED_PAGE;
          String  requestedPage = request.getParameter(PAGE_PARAMETER);
          if (requestedPage != null)
            pageSelected = new Integer(requestedPage);

          // Get the list of messages (sorted)
          loadMessages(request, sessionDR.getDrId(), mrProfile.getMrId(), sortByField, pageSelected, sessionId);

          // Assign variables to request object and send to JSP view
          request.setAttribute(REQUEST_PAGE_DR, sessionDR);
          request.setAttribute(REQUEST_MRPROFILE, mrProfile);
          forwardToTemplate(SUCCESS_TEMPLATE, request, response);
        }
      }
  }
  
  /**
   * Requests the available messages for a specific MR/DR pair in the
   * order requested, then deposits them into the request object.
   * @param request The HTTP request object.
   * @param mrId The MR's Id to filter messages for.
   * @param drId The DR's Id to filter messages for.
   * @param sortByField The sort order in which to return the messages.
   * @param pageSelected The page to filter for.
   */
  private void loadMessages(HttpServletRequest request,
                            String             drId,
                            String             mrId,
                            String             sortByField,
                            Integer            pageSelected,
                            String             sessionId)
  {
    try
    {
      // Get a reference to the MessageManager EJB
      //MessageManagerRemoteIntf  messmgr = (MessageManagerRemoteIntf)
      //        new EJBUtil().getManager(HttpConstant.MESSAGEMANAGER_HOME);

      MessageHelperManager messmgr = new MessageHelperManager();

      // Determine the max number of pages
      Integer messageCount = messmgr.getAvailableMessages(mrId, drId, false, MessageHelperManager.ALL_DR_MESSAGES, true, sessionId);
      System.out.println("DR 3.0 - messageCount displayed=" + messageCount);
      int pageCount = (int) Math.floor((messageCount.intValue() - 1) / MESSAGES_PER_PAGE) + 1;
      System.out.println("DR 3.0 - PageCount displayed=" + pageCount);

      Integer maxPages = new Integer(pageCount);
      if (maxPages == null)
        maxPages = new Integer(0);

      // Trim selected page to the last page if required
      if (pageSelected.compareTo(maxPages) > 0)
        pageSelected = maxPages;

      // Determine sort orders (we need a number matching the position in the list)
      Collection sortOrders = (Collection) request.getAttribute(REQUEST_SORT_OPTIONS);
      int nSortClause = 1;
      for (Iterator i = sortOrders.iterator(); i.hasNext();)
        if (sortByField.equals((String)i.next()))
          break;
        else
          nSortClause++;

      // Get a list of messages
      Integer startId = new Integer((pageSelected.intValue() * MESSAGES_PER_PAGE));
      Integer endId   = new Integer(((pageSelected.intValue() + 1) * MESSAGES_PER_PAGE) - 1);
      System.out.println("DR 3.0 - StartId displayed=" + startId);
      System.out.println("DR 3.0 - EndId displayed=" + endId);
      if (endId.compareTo(messageCount) > 0)
        endId = messageCount;
      Collection messageList = messmgr.getMessages(mrId,
                                                   drId,
                                                   "F" + nSortClause,
                                                   startId,
                                                   endId,
                                                   false,
                                                   MessageHelperManager.ALL_DR_MESSAGES,
                                                   true,
                                                   sessionId);

      request.setAttribute(REQUEST_MESSAGE_LIST, messageList);
      request.setAttribute(REQUEST_SELECTED_PAGE, pageSelected);
      request.setAttribute(REQUEST_MAX_PAGES, maxPages);
      request.setAttribute(REQUEST_SORT_FIELD, sortByField);
    }
    catch (Exception errRemote)
    {
      StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errRemote.printStackTrace(p);
      throw new ApplicationError("Error communicating with MessageManager - " + s.toString());
    }
  }

  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  private void setupComboLists(HttpServletRequest request)
  {
    Collection sortOptions = new ArrayList();
    sortOptions.add("sent");
    sortOptions.add("recieved");
    sortOptions.add("date");
    sortOptions.add("subject");
    request.setAttribute(REQUEST_SORT_OPTIONS, sortOptions);
  }

  /**
   * Get a DRInformation object for a particular MR and DR
   */
  private MrProfile getMRProfile(String drId, String mrId, String sessionId)
  {
    try
    {
      // Load the manager bean
      MRManagerRemoteIntf mrMgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      return mrMgr.getMRProfileById(mrId, drId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
  }

}

