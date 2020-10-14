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
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * The servlet that handles incoming HTTP requests from the DR5.0 and 5.1 pages.
 * This page allows a DR to add new MRs to their list of approved MRs.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DR_AddNewMRCtlr.java,v 1.1.2.8 2001/11/13 07:56:55 rick Exp $
 */
public class DR_AddNewMRCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  ADD_NEW_TEMPLATE      = HttpConstant.Dr05_View;
  final String  CONFIRM_TEMPLATE      = HttpConstant.Dr05_1_View;
  final String  EXIT_TEMPLATE         = HttpConstant.Dr02_Ctlr;

  // Parameters in the HTTP form within the JSP page
  final String  ADD_ANOTHER_PARAMETER = "addAnother";
  final String  MRID_PARAMETER        = "mrId";

  final String  ADD_ANOTHER_YES       = "yes";
  final String  ADD_ANOTHER_NO        = "no";

  // Return parameters
  final String  REQUEST_MR_CONFIRM    = "mrConfirm";
  final String  REQUEST_PAGE_DR       = "pageDR";
  final String  REQUEST_ERROR_MESSAGE = "errorMessage";
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
    DR sessionDR = null;
    String sessionId = getWLCookie(request);
    
    // Check for an MR in session
    try
    {
      sessionDR = (DR) checkSession(request);

      request.setAttribute(REQUEST_PAGE_DR, sessionDR);
      request.setAttribute(REQUEST_ERROR_MESSAGE, "");

      // Get the request parameters
      String mrId       = request.getParameter(MRID_PARAMETER);
      String addAnother = request.getParameter(ADD_ANOTHER_PARAMETER);

      // If no mrId, we are new to the page
      if (mrId == null)
        super.forwardToTemplate(ADD_NEW_TEMPLATE, request, response);
      else if (addAnother == null)
      {
        // Load the MrProfile and redirect to the confirm page
        MR mr = loadMR(mrId, sessionId);
        if (mr == null)
          throw new ValidationException(SystemMessages.get("dr_noMatchingMRFound"));//"No MR was found with that Id");
        else if (sessionDR.getMRIdList().contains(mrId))
          throw new ValidationException(SystemMessages.get("dr_alreadyHaveThisMR"));//"You already have this MR in your list");
        else
        {
          request.setAttribute(REQUEST_MR_CONFIRM, mr);
          super.forwardToTemplate(CONFIRM_TEMPLATE, request, response);
        }
      }
      else
      {
        // Build a new profile, then write to persistent storage
        addMRProfile(sessionDR, mrId, sessionId);

        // Check if we should add another
        if (addAnother.equals(ADD_ANOTHER_YES))
          super.forwardToTemplate(ADD_NEW_TEMPLATE, request, response);
        else
          super.forwardToTemplate(EXIT_TEMPLATE, request, response);
      }
    }
    catch (ValidationException errValid)
    {
      request.setAttribute(REQUEST_ERROR_MESSAGE, errValid.getMessage());
      forwardToTemplate(ADD_NEW_TEMPLATE, request, response);
    }
  }

  /**
   * Load up the MR object corresponding to this mrId, so that we can confirm
   * it's the one we want.
   * @param mrId The mr in question.
   */
  protected MR loadMR(String mrId, String sessionId)
  {
    try
    {
      // Load the manager bean
      MRManagerRemoteIntf mrMgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      return mrMgr.getMRById(mrId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
  }

  /**
   * Add the MrProfile object we just created to persistent storage
   * @param mrProfile The new DR-MR association.
   */
  protected void addMRProfile(DR userDR, String mrId, String sessionId)
  {
    try
    {
      // Load the manager bean
      DRManagerRemoteIntf drMgr =
            (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drMgr.addDRMemo(mrId, userDR, "", sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with DRManager EJB", errRemote);
    }
  }
}
