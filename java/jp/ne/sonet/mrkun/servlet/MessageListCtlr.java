
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
 * This class is the boundary class for the MR3.0 use case. It forms the
 * controller part of an MVC pattern with the mr3.jsp page and the
 * MessageManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MessageListCtlr.java,v 1.1.2.31 2001/11/13 07:56:55 rick Exp $
 */
public class MessageListCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr03_View;

  // Request parameters needed to build the JSP
  final String  REQUEST_DRINFO        = "drInfo";
  final String  REQUEST_PRE_STATS     = "preMonthUsageData";
  final String  REQUEST_CUR_STATS     = "curMonthUsageData";
  final String  REQUEST_MESSAGE_LIST  = "messageList";
  final String  REQUEST_SORT_OPTIONS  = "sortOptions";
  final String  REQUEST_SELECTED_PAGE = "selectedPage";
  final String  REQUEST_MAX_PAGES     = "maxPages";
  final String  REQUEST_SORT_FIELD    = "sortByField";
  final String  REQUEST_PAGE_MR       = "pageMR";

  // Parameters passed in by the browser
  final String  PAGE_PARAMETER        = "pageNo";
  final String  SORT_BY_PARAMETER     = "sortBy";
  final String  DRID_PARAMETER        = "drId";
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
    String drId = ""; 
    MR sessionMR = null;
    String sessionId = this.getWLCookie(request);
    
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
    
      if ((request.getParameter(DRID_PARAMETER) == null) &&
             (request.getAttribute(REQUEST_DRINFO) == null))
      throw new ApplicationError("No DRInformation object supplied");
    else
    {
      // Get the DRInformation object
      DRInformation drInfo = null;
      if (request.getParameter(DRID_PARAMETER) == null)
      {
        // This is provided for the case of forwarding from another servlet
        drInfo = (DRInformation) request.getAttribute(REQUEST_DRINFO);
      }
      else
      {
        // Get the drInfo from the request parameters
        drId = StringUtils.getParameter(request, DRID_PARAMETER);
        drInfo = getDrInfo(sessionMR.getMrId(), drId, sessionId);
      }

      // Check it's a valid DRInformation
      if (drInfo == null)
        throw new ApplicationError("No DRInformation available");
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
        loadMessages(request, sessionMR.getMrId(), drInfo.getDrId(), sortByField, pageSelected, sessionId);

          // Generating the MR's usage statistics
          UsageStatistics currentMonthData = new UsageStatistics();
          UsageStatistics previousMonthData = new UsageStatistics();
               
          Map mrUsageStatistic = loadMrUsageData(sessionMR.getMrId(), drId, sessionId);  
          currentMonthData = (UsageStatistics)mrUsageStatistic.get(HttpConstant.SINGLE_USAGE_CUR_MONTH); 
          previousMonthData = (UsageStatistics)mrUsageStatistic.get(HttpConstant.SINGLE_USAGE_PRE_MONTH);
        
          // Assign variables to request object and send to JSP view
          request.setAttribute(REQUEST_PRE_STATS, previousMonthData);
          request.setAttribute(REQUEST_CUR_STATS, currentMonthData);
          request.setAttribute(REQUEST_PAGE_MR, sessionMR);
          request.setAttribute(REQUEST_DRINFO, drInfo);
          forwardToTemplate(SUCCESS_TEMPLATE, request, response);        
        }
      }
  }
  
  /**
   * Requests the available messages for a specific MR/DR pair in the
   * order requested, then deposits them into the request object.
   * @param drId The DR who associated with MR.
   * @param mrId The MR's Id to generate the usage point for.
   * @return Map The container that contains current and previous month data.
   */
  private Map loadMrUsageData(String mrId, String drId, String sessionId)
  {
  	Map mrUsagePoint = null;
    
    try
    {
      
      // Load the manager bean
      ReportManagerRemoteIntf reportMgr =
            (ReportManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.REPORTMANAGER_HOME); 
      mrUsagePoint = reportMgr.getSingleUsagePoint(mrId, drId, sessionId);
      
      if( mrUsagePoint == null)
        throw new ApplicationError("Report manager returns null usage point data in " + this.getClass().getName());
      	 
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
    
    return mrUsagePoint;
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
                            String             mrId,
                            String             drId,
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
      System.out.println("XXXXXXXXXXXXXXXXXXXXX");
      Integer messageCount = messmgr.getAvailableMessages(mrId, drId, false, MessageHelperManager.ANY_MESSAGE, sessionId);
      System.out.println("MR 3.0 - messageCount displayed=" + messageCount);
      int pageCount = (int) Math.floor((messageCount.intValue() - 1) / MESSAGES_PER_PAGE) + 1;
      System.out.println("MR 3.0 - PageCount displayed=" + pageCount);

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
      System.out.println("MR 3.0 - StartId displayed=" + startId);
      System.out.println("MR 3.0 - EndId displayed=" + endId);
      if (endId.compareTo(messageCount) > 0)
        endId = messageCount;
      Collection messageList = messmgr.getMessages(mrId,
                                                   drId,
                                                   "F" + nSortClause,
                                                   startId,
                                                   endId,
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
    sortOptions.add("recieved");
    sortOptions.add("sent");
    sortOptions.add("date");
    sortOptions.add("subject");
    request.setAttribute(REQUEST_SORT_OPTIONS, sortOptions);
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

