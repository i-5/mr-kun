
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
 * This class is the servlet controller for dr06.jsp page <br/>
 * It's main purpose is to handle the deletion request of registered mr from 
 * the doctor. 
 *
 * @author <a>Damon Lok</a>
 * @version $Id: DR_MRDeleteCtlr.java,v 1.1.2.12 2001/12/20 08:59:09 rick Exp $
 */
public class DR_MRDeleteCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  POST_DELETE_TEMPLATE  = HttpConstant.Dr02_Ctlr;
  final String  PRE_DELETE_TEMPLATE   = HttpConstant.Dr06_1_View;
  final String  DISPLAY_MR_LIST       = HttpConstant.Dr02_Ctlr;

  // Request parameters needed to build the JSP
  final String  PAGE_DR       		  = "pageDR";
  final String  SORT_BY_OPTION        = "sortBy";
  final String  DELETE_MR_LIST        = "deleteMRList";
  
  // Constant used in this servlet
  final String  REQUEST_SORT_OPTIONS  = "sortOptions";
  final String  SORT_BY_PARAMETER     = "sortBy";
  final String  SORT_BY_COLLECTION    = "sortOptions";
  final String  DELETE_CONFIRMING	  = "deleteConfirming";
  final String  DELETE_CONFIRMED	  = "deleteConfirmed";
  final String  SORT_BY_NAME          = "name";

  // Parameters used by other JSP or servlet 
  final String  CONFIRM_DEL_PARAMETER = "confirmDelete";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  DELETE_PARAMETER	  = "delete";

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
    DR sessionDR = null;
    String sortOrder = SORT_BY_NAME;
      
    // Check for an MR in session
      sessionDR = (DR) checkSession(request);
      
      // Check for the sort field to determine the sorting order
      String requestedOrder = request.getParameter(SORT_BY_PARAMETER);
      
      // Retreive the selected mr for deletion                                  
      String[] deleteMRList = request.getParameterValues(DELETE_MR_LIST);
      
      if (requestedOrder != null)  
        sortOrder = requestedOrder;
      
      request.setAttribute(PAGE_DR, sessionDR);
      request.setAttribute(SORT_BY_OPTION, sortOrder);
      
      // Check whether we are just reviewing or actually deleting
      if (request.getParameter(DELETE_CONFIRMING) != null)
      {
        // User did not choose any mr to delete, thus it will be back to DR 6
        if (deleteMRList == null)
        {
          request.setAttribute(DELETE_PARAMETER, "true");
          request.setAttribute(HttpConstant.ERROR_MESSAGE, SystemMessages.get("dr_NoDeleteMR"));      	 
          super.forwardToTemplate(DISPLAY_MR_LIST, request, response);           
        }
        else
        { 
          // Retrieve the mr that chosen to be delete and display them
          Collection displayDeleteMRList = loadMRProfile(deleteMRList, sessionDR.getDrId(), getWLCookie(request));
          SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
          sessionMgr.setSessionItem(DELETE_MR_LIST, displayDeleteMRList);
          request.setAttribute(DELETE_MR_LIST, displayDeleteMRList);  
          super.forwardToTemplate(PRE_DELETE_TEMPLATE, request, response);
        }
      }
      else if(request.getParameter(DELETE_CONFIRMED) != null)
      {
        // Perform the actual deletion
        SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
        Collection colDeleteMRList = (Collection) sessionMgr.getSessionItem(DELETE_MR_LIST);
        removeMR(colDeleteMRList, sessionDR.getDrId(), getWLCookie(request));
        notifyMRByEmail(colDeleteMRList, sessionDR, getWLCookie(request));
        super.forwardToTemplate(POST_DELETE_TEMPLATE, request, response);
      }
      else
      {
        throw new ApplicationError(this.getClass().getName() + ": Undefined request type or unknown parameter.");
      }         
  }

  /**
   * Obtain the list of MRProfile object that are chosen to be deleted later
   * @param String array of mrIdList The list of mr to be deleted.
   * @param String DR ID.
   * @retruns The list of MRProfile object.
   */
  private Collection loadMRProfile(String[] mrIdList, String drId, String sessionId)
  {
    try
    {
      // Get a reference to the MRManager EJB
      MRManagerRemoteIntf mrmgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      Map deleteMRList = mrmgr.getMRProfileByDRId(drId, sessionId);
      List resultList = new ArrayList();
      for (int nLoop = 0; nLoop < mrIdList.length; nLoop++)
        resultList.add(((MrProfile)deleteMRList.get(mrIdList[nLoop])).getMR());
      return resultList;
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }
  }

  /**
   * Send the email to the mr notifying that he/she has just been deleted by the DR.
   * @param colEmailMRIdList A list of mr that to be sent the email.
   * @param DR object The session DR 
   */
  private void notifyMRByEmail(Collection colEmailMRList, DR dr, String sessionId)
  {
    try
    {
      // Get a reference to the DRManager EJB
      DRManagerRemoteIntf drMgr =
           (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drMgr.sendDeletedMREmail(colEmailMRList, dr, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with DRManager EJB", errRemote);
    }
  }

  /**
   * Removes the mr specified in the collection.
   * @param deletingMRIdList A list of mrIds that are to be removed.
   * @param String The DR Id
   */
  private void removeMR(Collection colDeleteMRList, String drId, String sessionId)
  {    
    try
    {
      // Get a reference to the MRManager EJB
      MRManagerRemoteIntf mrmgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      mrmgr.removeMR(colDeleteMRList, drId, sessionId);
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with MessageManager EJB", errRemote);
    }
  }
}


