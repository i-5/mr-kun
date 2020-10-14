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
 * The servlet that handles incoming HTTP requests from the DR2.0 page.
 * This page gives an overview for the DR of all the MRInformation objects
 * available.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: DR_MRInfoCtlr.java,v 1.1.2.18 2001/11/13 07:56:55 rick Exp $
 */
public class DR_MRInfoCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE         = HttpConstant.Dr12_View;
  final String  SUCCESS_TEMPLATE       = HttpConstant.Dr02_View;
  final String  DELETE_MR_PAGE         = HttpConstant.Dr06_View;
  final String  BANNER_POSITION_PAGE   = HttpConstant.Dr07_View;

  // Parameters in the HTTP form within the JSP page
  final String  SORT_BY_PARAMETER      = "sortBy";
  final String  DEFAULT_SORT_ORDER     = "date";
  final String  SORT_BY_NAME           = "name";
  final String  CHECKED_DELETE_MR_LIST = "checkedDeleteMR";
  
  // Lists for populating combo boxes in the JSP page
  final String  SORT_BY_COLLECTION     = "sortOptions";
  final String  MESSAGE_ID             = "messageId";
  final String  MR_PROFILE_LIST        = "mrProfileList";
  final String  SORT_BY_OPTION         = "sortBy";
  final String  REQUEST_PAGE_DR        = "pageDR";
  final String  DELETE_MR_LIST         = "deleteMRList";  
  final String  SESSION_ERROR_MESSAGE  = "sessionErrorMessage";
  
  // Constant to determine whether the request is delete mr, display mr's banner 
  // position or show mr list
  final String  SHOW_MR_LIST             = "showMRList";
  final String  CHECKED_DELETE_MR        = "checkedDeleteMR";
  final String  DELETE_PARAMETER         = "delete";
  final String  MR_BANNER_POSITION       = "mrBannerPosition";
  final String  MR_BANNER_POSITION_SAVED = "mrBannerPositionSaved";  
  final String  DELETE_MR                = "deleteMR";  
  
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
    DR sessionDR = null;
    String sortOrder = "";
    String userRequest = "";
    
    // Check for an DR in session
    try
    {
      sessionDR = (DR) checkSession(request);

      request.setAttribute(REQUEST_PAGE_DR, sessionDR);
      
      // This request is initiated from DR 6 when user didn't check any mr for deletion
      if (request.getAttribute(DELETE_PARAMETER) != null)
      {
      	userRequest = DELETE_MR;
        setupComboLists(request, DELETE_MR);    
        sortOrder = SORT_BY_NAME;
        request.setAttribute(HttpConstant.ERROR_MESSAGE, (String) request.getAttribute(HttpConstant.ERROR_MESSAGE));      
      }
      // This request is initiated from non DR 6 or DR 7 pages which will response
      // with mr profile list display
      else if ((request.getParameter(DELETE_PARAMETER) == null) &&
               (request.getParameter(MR_BANNER_POSITION) == null) ||
               (request.getAttribute(MR_BANNER_POSITION_SAVED) != null))  
      {
      	userRequest = SHOW_MR_LIST;
        setupComboLists(request, SHOW_MR_LIST);
        sortOrder = DEFAULT_SORT_ORDER;
      }
      // This request is initiated from DR 7 for view and edit mr's banner position
      else if ((request.getParameter(MR_BANNER_POSITION) != null) && 
               (request.getAttribute(MR_BANNER_POSITION_SAVED) == null))
      {
        userRequest = MR_BANNER_POSITION;    
        sortOrder = SORT_BY_NAME;
      } 
      // This request is initiated from DR 6 for display the mr profile list for deletion
      else
      { 
      	userRequest = DELETE_MR;
        setupComboLists(request, DELETE_MR);    
        sortOrder = SORT_BY_NAME;
        
        // This request is initiated from DR 6 when user click for delete another mr
      	if (request.getParameter(CHECKED_DELETE_MR) != null)
        {
          // Retrieve the checked mr profile list from the session  
          SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
          Collection checkedDeleteMR = (Collection) sessionMgr.getSessionItem(DELETE_MR_LIST);
          request.setAttribute(CHECKED_DELETE_MR_LIST, checkedDeleteMR);       
        }
      }
       
      // Check for the sort field to determine the sorting order
      String requestedOrder = request.getParameter(SORT_BY_PARAMETER);

      // Get MRManager for the loadup of DRInformation object
      MRManagerRemoteIntf mrManager = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);

      if ((requestedOrder != null) &&
        ((Collection) request.getAttribute(SORT_BY_COLLECTION)).contains(requestedOrder))
        sortOrder = requestedOrder;

      // Build the mr list in the correct order
      Map mrList = mrManager.getMRProfileByDRId(sessionDR.getDrId(), getWLCookie(request));
      if (mrList != null)
      {
        Object sortedMRInfo[] = mrList.values().toArray();
        MrProfileComparator mrc = new MrProfileComparator(sortOrder,
                                                    !sortOrder.equals("date"));
        Arrays.sort(sortedMRInfo, mrc);
        if (userRequest.equals(DELETE_MR))
        {
          filterEmzoDisplay(request, Arrays.asList(sortedMRInfo));    
        }
        else        
          request.setAttribute(MR_PROFILE_LIST, Arrays.asList(sortedMRInfo));
      }
      sessionDR.setMrProfileMap(mrList);

      // Build the drInfoList in the correct order
      //request.setAttribute(MESSAGE_ID, messageCounts);

      // Send to template
      if (userRequest.equals(SHOW_MR_LIST))
      {
      	request.setAttribute(SORT_BY_OPTION, sortOrder);
        super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
      }
      else if (userRequest.equals(DELETE_MR))
      {
        request.setAttribute(SORT_BY_OPTION, sortOrder);
        super.forwardToTemplate(DELETE_MR_PAGE, request, response); 
      }
      else
      {
      	// Store the mr profile list for later DR 7 banner position 
      	SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
        sessionMgr.setSessionItem(MR_PROFILE_LIST, mrList.values());
        super.forwardToTemplate(BANNER_POSITION_PAGE, request, response); 
      }
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with the EJB server", errRemote);
    }
  }
  
  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  private void setupComboLists(HttpServletRequest request, String userRequest)
  {
    Collection colSortBy = new ArrayList();
    colSortBy.add("name");
    colSortBy.add("company");
    if(userRequest.equals(SHOW_MR_LIST))
      colSortBy.add("date");
    request.setAttribute(SORT_BY_COLLECTION, colSortBy);
  }

  /**
   * This will scan the mr list for emzo and filter out the emzo from displaying 
   * to the screen.
   * @param request The request variable to add the collections to.
   * @param MrList Collection that holds all the mr associates with the dr.
   */  
  private void filterEmzoDisplay(HttpServletRequest request, Collection MrProfileList)
  {
  	String emzoId = "";
    Collection colDeleteMRList = new ArrayList();
    
    // Get DRManager to obtain the emzo ID
    DRManagerRemoteIntf drManager = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
    try
    {
      emzoId = drManager.getEmzoId(getWLCookie(request));
      if(emzoId == null)
        throw new ApplicationError("Emzo Id is return nulled in DR_MRInfoCtlr.filterEmzoDisplay");   
      else
      {
        for (Iterator itr = MrProfileList.iterator(); itr.hasNext(); )
        {
          MrProfile mrProfile = (MrProfile) itr.next();
          if(!mrProfile.getMrId().equals(emzoId))
            colDeleteMRList.add(mrProfile);         
        }
        request.setAttribute(MR_PROFILE_LIST, colDeleteMRList);
      }   
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with the DR manager", errRemote);
    }
  }
}
