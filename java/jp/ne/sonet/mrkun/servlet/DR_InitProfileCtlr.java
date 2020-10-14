
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.rmi.*;
import javax.naming.*;
import javax.ejb.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;

/**
 * The request handler for the init Show/Edit/Cancel profile operation.
 * This servlet handles GET and POST requests. GET requests are handled
 * by building a view interface on the DR object in the session scope.
 * POST requests are handled as edits, and the servlet acts as a
 * controller, modifying the underlying data, and returning the updated
 * view.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @author M.Mizuki
 * @version $Id: DR_InitProfileCtlr.java,v 1.1.2.5 2001/11/13 07:56:55 rick Exp $
 */
public class DR_InitProfileCtlr extends BaseServlet 
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr11_View;
  final String  VIEW_TEMPLATE         = HttpConstant.Dr00_2_View;
  final String  EDIT_TEMPLATE         = HttpConstant.Dr00_2_View;

  // Parameters in the HTTP form within the JSP page
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  SESSION_MANAGER_NAME  = "SessionManager";
  final String  ACTION_PARAMETER      = "action";
  final String  SAVE_PARAMETER        = "DrProfile_submit";

  final String  CERTFICATE_PARAMETER  = "certificate";
  final String  SPECIALTY_PARAMETER   = "specialty";

  final String  ERROR_MESSAGE         = "errorMessage";
  final String  FINISH_MESSAGE        = "finishMessage";

  // Lists for populating combo boxes in the JSP page
  final String  REQUEST_DR            = "pageDR";
  final String  QUALIFICATIONLIST     = "qualificationList";
  final String  SPECIALTYLIST         = "specialtyList";

  final String  DOCTOR_CODE           = "0001";

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
    // Check for a DR in session
    DR sessionDR = null;
    String sessionId = getWLCookie(request);
    
    // Check for an MR in session
    try
    {
      sessionDR = (DR) checkSession(request);
      request.setAttribute(ERROR_MESSAGE, "");
      request.setAttribute(FINISH_MESSAGE, "");
      request.setAttribute(REQUEST_DR, sessionDR);

      // get Medical Qualification List and Specialty List
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      Collection qualificationList = (Collection)drm.getMedicalQualificationList(sessionId);
      Collection specialtyList = (Collection)drm.getSpecialtyList(sessionId);

      request.setAttribute(QUALIFICATIONLIST, qualificationList);
      request.setAttribute(SPECIALTYLIST, specialtyList);

      // Check for the save indicator (ie the calling page has
      // saveable data
      if (request.getParameter(ACTION_PARAMETER) != null &&
          request.getParameter(ACTION_PARAMETER).equals(ACTION_PARAMETER))
      {
        // Validate and save the submitted parameters
        try
        {
          validate(sessionDR, request);
        }
        catch(ValidationException errValid)
        {
          request.setAttribute(ERROR_MESSAGE, "");
        }
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
      else if (request.getParameter(SAVE_PARAMETER) != null)
      {
        // Validate and save the submitted parameters
        validate(sessionDR, request);
        dataSave(sessionDR, sessionId);
        request.setAttribute(FINISH_MESSAGE, "Profile Finish");
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
      else
      {
      // Check for the Profile input finish ?
        if(!(sessionDR.getMedCertification() == null ||
          sessionDR.getMedCertification().equals("")))
        {
          request.setAttribute(FINISH_MESSAGE, "Profile Finish");
        }
        forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
    }
    catch(ValidationException errValid)
    {
      request.setAttribute(ERROR_MESSAGE, errValid.getMessage());
      forwardToTemplate(EDIT_TEMPLATE, request, response);
    }
    catch(RemoteException errRemote)
    {
      throw new ApplicationError("get Medical Qualification List and Specialty List in Error" , errRemote);
    }
  }

  /**
   * Validate the parameters passed in and save them to the DR object if
   * they pass all the validation checks.
   * @param userDR The DR object to be saved to.
   * @param request The HTTP request object containing the new DR values.
   * @exception ValidationException thrown if a parameter is invalid
   */
  private void validate(DR userDR, HttpServletRequest request)
    throws ValidationException
  {
    String finalErrorMessage = "";

    try
    {

      // Retrieve all the values
      String testCertification  = StringUtils.getParameter(request, CERTFICATE_PARAMETER);
      String testSpecialty      = StringUtils.getParameter(request, SPECIALTY_PARAMETER);

      // Check that all string fields are not empty

      // Medical certification
      if ((testCertification == null) ||
          (testCertification.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_initProfileCertificationEmpty");//"The certification field was left empty.";
      }
      else
        userDR.setMedCertification(testCertification);

      if( testCertification.equals(DOCTOR_CODE) )
      {
        // Specialty
        if ((testSpecialty == null) ||
            (testSpecialty.equals("")))
        {
          if (finalErrorMessage.equals(""))
            finalErrorMessage = SystemMessages.get("dr_initProfileSpecialtyEmpty");//"The Specialty field was left empty.";
        }
        else
          userDR.setSpecialty(testSpecialty);
      }
    }
    catch (Exception errAny)
    {
      throw new ApplicationError("Validation error during DR save" , errAny);
    }
    if (!finalErrorMessage.equals(""))
      throw new ValidationException(finalErrorMessage);
  }

  private void dataSave(DR userDR, String sessionId)
    throws ApplicationError
  {
    try
    {
      // Create a DRManager and save the DR
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drm.updateDR(userDR, sessionId);

    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens	
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on updating DR in doAction and RemoteException is thrown.");
    }

  }
}
