package jp.ne.sonet.mrkun.servlet;

import java.io.*;
import java.rmi.*;
import java.text.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jp.ne.sonet.mrkun.framework.servlet.BaseServlet;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * The servlet that handles incoming HTTP requests from the MR2 page.
 * This page gives an overview for the MR of all the DRInformation objects
 * available.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_DoctorInfoCtlr.java,v 1.1.1.1.2.27 2001/11/13 07:56:55 rick Exp $
 */
public class MR_DoctorInfoCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr02_View;

  // Parameters in the HTTP form within the JSP page
  final String  SORT_BY_PARAMETER     = "sortBy";
  final String  DEFAULT_SORT_ORDER    = "action";

  // Lists for populating combo boxes in the JSP page
  final String  SORT_BY_COLLECTION    = "sortOptions";
  final String  MESSAGE_ID 			      = "messageId";
  final String  DRINFO_LIST           = "drInfoList";
  final String  SORT_BY_OPTION        = "sortBy";
  final String  RANKING               = "ranking";
  final String  REQUEST_PAGE_MR       = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  
  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays the current DRInfo state.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
  {
    MR sessionMR = null;
    
    try
    {
SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss:SSSSSS");
System.out.println("Starting mr2.0 execution: " + sdf.format(new Date()));
      // Check for an MR in session
      sessionMR = (MR) checkSession(request);

      request.setAttribute(REQUEST_PAGE_MR, sessionMR);
      setupComboLists(request);

      // Check for the sort field to determine the sorting order
      String sortOrder = DEFAULT_SORT_ORDER;
      String requestedOrder = request.getParameter(SORT_BY_PARAMETER);
      Map mrMessageData = new Hashtable();

      // Get MRManager for the loadup of DRInformation object
	    MRManagerRemoteIntf mrManager = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      MessageHelperManager mhm = new MessageHelperManager();

  	  if ((requestedOrder != null) &&
        ((Collection) request.getAttribute(SORT_BY_COLLECTION)).contains(requestedOrder))
      sortOrder = requestedOrder;

      request.setAttribute(SORT_BY_OPTION, sortOrder);

      // Build the drInfoList in the correct order
System.out.println("mr2.0 db load: " + sdf.format(new Date()));
      //Collection drInfoList    = mrManager.getDRInformationList(sessionMR.getMrId());
      //Map        messageCounts = mhm.getMRMessageCount(sessionMR, "");
      Map          messageCounts = new Hashtable();
      Collection   drInfoList    = mrManager.getDRInformationList(sessionMR.getMrId(), messageCounts, getWLCookie(request));
System.out.println("mr2.0 finished db load: " + sdf.format(new Date()));

      // Update the collection with the message counts and action values
/*
      for (Iterator i = drInfoList.iterator(); i.hasNext(); )
      {
        DRInformation drInfo = (DRInformation) i.next();
        DRMessageCount mc = (DRMessageCount) messageCounts.get(drInfo.getDrId());
        if (mc != null)
        {
          drInfo.setActionValue(new Integer(mc.getActionValue()));
          drInfo.setReceivedMessage(new Integer(mc.getNewContacts()));
          drInfo.setUnreadSentMessage(new Integer(mc.getNewEDetails()));
        }
        else
        {
          drInfo.setActionValue(new Integer(1));
          drInfo.setReceivedMessage(new Integer(0));
          drInfo.setUnreadSentMessage(new Integer(0));
        }
      }
*/
      if (!drInfoList.isEmpty())
      {
          Object sortedDRInfo[] = drInfoList.toArray();
          DRInfoComparator drc = new DRInfoComparator(sortOrder,
                                                      !(sortOrder.equals("importance") ||
                                                        sortOrder.equals("unread")));
        Arrays.sort(sortedDRInfo, drc);
        drInfoList = Arrays.asList(sortedDRInfo);
      }
      request.setAttribute(DRINFO_LIST, drInfoList);

      // Build the drInfoList in the correct order
      request.setAttribute(MESSAGE_ID, messageCounts);

      // Get the Ranking for this MR
      request.setAttribute(RANKING, getMRRanking(sessionMR, getWLCookie(request)));

      // Send to template
System.out.println("finished mr2.0 execution: " + sdf.format(new Date()));
      super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with the EJB server", errRemote);
    }   
  }
  
  /**
   * getMRRanking will return the ranking object that associated with the MR
   *
   * @param mrId The ranking that belongs to this MR.
   * @return Ranking object
   */ 
  private Ranking getMRRanking(MR userMR, String sessionId)
  {	  			
	  Ranking ranking = new Ranking();
    
  	try
    {
  	  // Get MRManager for the loadup of DRInformation object
	    ReportManagerRemoteIntf reportManager = (ReportManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.REPORTMANAGER_HOME);

  	  // Build a list of all drInformation object
      ranking = reportManager.getUserRanking(userMR.getMrId(), sessionId);
	    if ( ranking == null )
	    {
	      // Throw ApplicationError if null is returned
        throw new ApplicationError(this.getClass().getName()
             			+ ": Return null Ranking object in getMRRanking.");
	    }
    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on building the Ranking object in getMRRanking.");
    }
    return ranking;
  }

  /**
   * Based on the sort order supplied, get the DRInfo objects requested and
   * return the sorted list within the request object.
   *
   * Note: this is probably a slow way to do this, but I can't think of a
   * quicker way right now.
   *
   * @param userMR The MR object to be saved to.
   * @param sortOrder The order in which to return the DRInfo objects
   */
/*
  private Collection buildDRInfo(MR userMR, String sortOrder, MRManagerRemoteIntf mrManager)
  {
    Collection drInformationList = new ArrayList();
  	try
    {
  	  // Build a list of all drInformation object
      drInformationList = mrManager.getDRListWithMessageCount(userMR, mrMessageData);

  	  if ( drInformationList == null )
  	  {
  	    // Throw ApplicationError if null is returned
        throw new ApplicationError(this.getClass().getName()
             			+ ": Return null DRInformation object list in buildDRInfo.");
  	  }
	    else if ( mrMessageData == null )
	    {
	      // Throw ApplicationError if null is returned
        throw new ApplicationError(this.getClass().getName()
             			+ ": Return null mrMessageData object list in buildDRInfo.");
	    }

      if (drInformationList != null)
      {
        Object sortedDRInfo[] = drInformationList.toArray();
        DRInfoComparator drc = new DRInfoComparator(sortOrder);
        Arrays.sort(sortedDRInfo, drc);
        drInformationList = Arrays.asList(sortedDRInfo);
      }
  	  // Build a list of all drInformation object
      drInformationList = ;

    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on building the DRInformation object list in buildDRInfo and RemoteException is thrown.");
    }
    return drInformationList;
  }
*/

  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  private void setupComboLists(HttpServletRequest request)
  {
    Collection colSortBy = new ArrayList();
    colSortBy.add("name");
    colSortBy.add("hospital");
    colSortBy.add("specialty");
    colSortBy.add("importance");
    colSortBy.add("action");
    colSortBy.add("unread");
    colSortBy.add("unrecieved");
    request.setAttribute(SORT_BY_COLLECTION, colSortBy);
  }

}
