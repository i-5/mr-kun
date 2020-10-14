
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;

/**
 * This servlet handles GET and POST requests for the DR04  
 * Show Registered MR profile page operation including view, edit
 * and save input form data from the user. 
 *
 * @author Damon Lok
 * @version $Id: DR_MRProfileCtlr.java,v 1.1.2.8 2001/11/13 07:56:55 rick Exp $
 */
public class DR_MRProfileCtlr extends BaseServlet 
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  VIEW_TEMPLATE         = HttpConstant.Dr04_View;
  final String  EDIT_TEMPLATE         = HttpConstant.Dr04_View;

  // Parameters in the HTTP form within the JSP page
  final String  MR_ID                 = "mrId";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  SESSION_MANAGER_NAME  = "SessionManager";
  final String  MEMO                  = "memo";
  final String  SAVE_PARAMETER        = "save";
  final String  CANCEL_PARAMETER      = "cancel";
  final String  PAGE_DR               = "pageDR";
  final String  MR_PROFILE            = "mrProfile";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays the current DR state.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction (HttpServletRequest request, HttpServletResponse response)
  {
    DR sessionDR = null;
    MrProfile mrProfile = null;
    String memo = "";
    String mrId = StringUtils.getParameter(request, MR_ID);

    String sessionId = getWLCookie(request);
    
    // Get the MR Id parameter from the requesting URL
    if((mrId == null) || (mrId.equals("")))
      throw new ApplicationError("No MR id supplied.");

    // Check for an DR in session
      sessionDR = (DR) checkSession(request);
                          
      // Store this MR data in session for the next save request
      SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
      
      // This is a read request from the user 
      if ((request.getParameter(SAVE_PARAMETER) == null) && (request.getParameter(CANCEL_PARAMETER) == null))
      {
        // User come to the page for the first time 
        // Retreive the MR who is chosen to be populated
        mrProfile = getMRProfile(mrId, sessionDR.getDrId(), sessionId);
        memo = mrProfile.getDrMemo(); 
        
        // Store this data in the session for the next save request
        sessionMgr.setSessionItem(MR_PROFILE, mrProfile);
        
        // Forward the data to the jsp and display them
        request.setAttribute(MR_PROFILE, mrProfile);
        request.setAttribute(PAGE_DR, sessionDR);
        request.setAttribute(MEMO, memo);
        forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
      else if (request.getParameter(CANCEL_PARAMETER)!= null)
      {        
        // Retreive the MrProfile object that stored in the session previously
        mrProfile = (MrProfile) sessionMgr.getSessionItem(MR_PROFILE);
        memo = mrProfile.getDrMemo();
        
        // Return to the page
        request.setAttribute(MR_PROFILE, mrProfile);
        request.setAttribute(PAGE_DR, sessionDR);
        request.setAttribute(MEMO, memo);
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
      else
      {
        // User click the save link        
        memo = StringUtils.getParameter(request, MEMO);
        saveMemo(mrId, sessionDR.getDrId(), memo, sessionId);
        
        // Retreive the MrProfile object that stored in the session previously
        mrProfile = (MrProfile) sessionMgr.getSessionItem(MR_PROFILE);
        mrProfile.setDrMemo(memo);
        
        // Return to the page
        request.setAttribute(MR_PROFILE, mrProfile);
        request.setAttribute(PAGE_DR, sessionDR);
        request.setAttribute(MEMO, memo);
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
  }

  /**
   * This method will save the form data entered by user to the database.
   * @param memo The memo that doctor enters to let the MR see.
   */
  private void saveMemo(String mrId, String drId, String memo, String sessionId)
  {    
  	try
    {
  	  // Get MRManager for storing the memo field
	  DRManagerRemoteIntf drManager = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drManager.updateDRMemo(mrId, drId, memo, sessionId);
    }
    catch (RemoteException e)
    {
	  // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
        + ": Problem on setting the dr's memo field in SaveMemo.");
    }
  }                     
    
  /**
   * Validate the parameters passed in and save them to the DR object if
   * they pass all the validation checks.
   * @param mrId The MR ID.
   * @param drId The DR ID.
   * @return MrProfile The MR's profile that associated with the DR. 
   */
  private MrProfile getMRProfile(String mrId, String drId, String sessionId)
  { 
    MrProfile mrProfile = null;
     
  	try
    {
  	  // Get MRManager for the loadup of DRInformation object
	  MRManagerRemoteIntf mrManager = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);

  	  // Retreive the MrProfile object
      mrProfile = mrManager.getMRProfileById(mrId, drId, sessionId);
	  if ( mrProfile == null )
	  {
	    // Throw ApplicationError if null is returned
        throw new ApplicationError(this.getClass().getName()
             			+ ": Return null MrProfile object in getMRProfile.");
	  }
    }
    catch (RemoteException e)
    {
	  // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on building the MrProfile object in getMRProfile.");
    }
    
    return mrProfile;
  }
}

 