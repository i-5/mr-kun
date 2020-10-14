
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
 * The request handler for the DR10 Show/Edit/Cancel profile operation.
 * This servlet handles GET and POST requests. GET requests are handled
 * by building a view interface on the DR object in the session scope.
 * POST requests are handled as edits, and the servlet acts as a
 * controller, modifying the underlying data, and returning the updated
 * view.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DR_ProfileCtlr.java,v 1.1.1.1.2.30 2001/11/13 07:56:55 rick Exp $
 */
public class DR_ProfileCtlr extends BaseServlet 
{
  final String  VIEW_TEMPLATE         = HttpConstant.Dr10_View;
  final String  EDIT_TEMPLATE         = HttpConstant.Dr10_View;
  final String  DISPLAY_MR_CTLR       = HttpConstant.Dr02_Ctlr;
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  USAGE_POINT_TEMPLATE  = HttpConstant.Dr09_View;
  
  // Parameters in the HTTP form within the JSP page
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  SESSION_MANAGER_NAME  = "SessionManager";
  final String  CANCEL_PARAMETER      = "cancel";
  final String  SAVE_PARAMETER        = "save";
  final String  NAME_PARAMETER        = "name_chinese";
  final String  KANA_PARAMETER        = "name_hiragana";
  final String  HOSPITAL_PARAMETER    = "company";
  final String  CERTFICATE_PARAMETER  = "certificate";
  final String  OFFICIAL_PARAMETER    = "is_civil_servant";
  final String  WORK_AREA_PARAMETER   = "prefecture";
  final String  USAGE_AREA_PARAMETER  = "use_location";
  final String  NETWORK_ENV_PARAMETER = "connection_type";
  final String  EMAIL_PARAMETER       = "email";
  final String  MRKUN_MAIL_PARAMETER  = "receive_mrkun";
  final String  EMAIL_INFO_PARAMETER  = "receive_email";
  final String  ERROR_MESSAGE         = "errorMsg";

  // Lists for populating combo boxes in the JSP page
  final String  CERTIFICATE_LIST      = "CertificateList";
  final String  WORK_AREA_LIST        = "WorkAreaList";
  final String  USAGE_AREA_LIST       = "UsageAreaList";
  final String  CONNECTION_TYPE_LIST  = "ConnectionTypeList";
  final String  REQUEST_DR            = "pageDR";
  
  // HTTP Parameter for DR 7 mr's banner position 
  final String  MR_BANNER_POSITION       = "mrBannerPosition";
  final String  MR_BANNER_POSITION_SAVED = "mrBannerPositionSaved";
  final String  MR_PROFILE_LIST          = "mrProfileList";

  // HTTP Parameter for DR 9 dr usage point
  final String  DR_USAGE_POINT           = "drPoint";

  final String  MEDICAL_CERTIFICATION_LIST = "medicalCertificationList";
  
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
    
    try
    {
      // Check for an MR in session
      sessionDR = (DR) checkSession(request);
      request.setAttribute(REQUEST_DR, sessionDR);
      request.setAttribute(ERROR_MESSAGE, "");

      // Load the certificationList object
      AssetManagerRemoteIntf amr = (AssetManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      request.setAttribute(MEDICAL_CERTIFICATION_LIST, amr.getCertificationList(sessionId));

      // Check for the save indicator (ie the calling page has
      // saveable data
      if (request.getParameter(SAVE_PARAMETER) != null)
      {
        // Validate and save the submitted parameters
        validateAndSave(sessionDR, request);
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
      else if (request.getParameter(MR_BANNER_POSITION) != null)
      {
      	// Get user input and store the new position setting to the backend
        request.setAttribute(MR_BANNER_POSITION_SAVED, "true");
      	setMRBannerPosition(sessionDR.getDrId(), request);
        forwardToTemplate(DISPLAY_MR_CTLR, request, response);
      }
      else if (request.getParameter(DR_USAGE_POINT) != null)
      {
      	// Send the response to DR 9 dr usage point page
        forwardToTemplate(USAGE_POINT_TEMPLATE, request, response);
      }
      else
      {
        forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
    }
    catch(ValidationException errValid)
    {
      request.setAttribute(ERROR_MESSAGE, errValid.getMessage());
      forwardToTemplate(EDIT_TEMPLATE, request, response);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with the AssetManager EJB", errRemote);
    }
  }

  /**
   * This will obtain the user input of mr's banner position and
   * store the new position setting to the backend.
   * @param userDR The DR object to be saved to.
   * @param request The HTTP request object containing the new DR values.
   */
  private void setMRBannerPosition(String drId, HttpServletRequest request)
  {
    String firstPosition = request.getParameter("mr_left");
    String secondPosition = request.getParameter("mr_right");
    if((firstPosition == null) || (secondPosition == null))
      throw new ApplicationError(this.getClass().getName() + ": User input passed from DR 7 are null");
    
    try
    {
      SessionManager sessionMgr = new SessionManager(request, new Boolean(false));
      Collection colMRList = (Collection) sessionMgr.getSessionItem(MR_PROFILE_LIST);      
      
      // Get a reference to the DRManager EJB
      DRManagerRemoteIntf drmgr =
            (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drmgr.updateMRBannerPosition(colMRList, drId, firstPosition, secondPosition, getWLCookie(request));
    }
    catch (Exception errRemote)
    {
      throw new ApplicationError("Error communicating with DRManager EJB", errRemote);
    }
  }
  
  /**
   * Validate the parameters passed in and save them to the DR object if
   * they pass all the validation checks.
   * @param userDR The DR object to be saved to.
   * @param request The HTTP request object containing the new DR values.
   * @exception ValidationException thrown if a parameter is invalid
   */
  private void validateAndSave(DR userDR, HttpServletRequest request)
    throws ValidationException
  {
    String finalErrorMessage = "";

    try
    {

      // Retrieve all the values
/*
      String testKanjiName      = request.getParameter(NAME_PARAMETER);
      String testKanaName       = request.getParameter(KANA_PARAMETER);
      String testHospital       = request.getParameter(HOSPITAL_PARAMETER);
      String testCertification  = request.getParameter(CERTFICATE_PARAMETER);
      String testPublicOfficial = request.getParameter(OFFICIAL_PARAMETER);
      String testWorkArea       = request.getParameter(WORK_AREA_PARAMETER);
      String testUsageArea      = request.getParameter(USAGE_AREA_PARAMETER);
      String testNetworkEnv     = request.getParameter(NETWORK_ENV_PARAMETER);
      String testEmail          = request.getParameter(EMAIL_PARAMETER);
      String testMRKunMail      = request.getParameter(MRKUN_MAIL_PARAMETER);
      String testEmailInfo      = request.getParameter(EMAIL_INFO_PARAMETER);
*/
      String testKanjiName      = StringUtils.getParameter(request, NAME_PARAMETER);
      String testKanaName       = StringUtils.getParameter(request, KANA_PARAMETER);
      String testHospital       = StringUtils.getParameter(request, HOSPITAL_PARAMETER);
      String testCertification  = StringUtils.getParameter(request, CERTFICATE_PARAMETER);
      String testPublicOfficial = StringUtils.getParameter(request, OFFICIAL_PARAMETER);
      String testWorkArea       = StringUtils.getParameter(request, WORK_AREA_PARAMETER);
      String testUsageArea      = StringUtils.getParameter(request, USAGE_AREA_PARAMETER);
      String testNetworkEnv     = StringUtils.getParameter(request, NETWORK_ENV_PARAMETER);
      String testEmail          = StringUtils.getParameter(request, EMAIL_PARAMETER);
      String testMRKunMail      = StringUtils.getParameter(request, MRKUN_MAIL_PARAMETER);
      String testEmailInfo      = StringUtils.getParameter(request, EMAIL_INFO_PARAMETER);
      // Check that all string fields are not empty

      // Kanji name
      if ((testKanjiName == null) ||
          (testKanjiName.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileKanjiNameEmpty");//"The kanji name field was left empty.";
      }
      else
        userDR.setKanjiName(testKanjiName);

      // Kana name
      if ((testKanaName == null) ||
          (testKanaName.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileKanaNameEmpty");//"The kana name field was left empty.";
      }
      else
        userDR.setKanaName(testKanaName);

      // Hospital
      if ((testHospital == null) ||
          (testHospital.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileHospitalEmpty");//"The hospital field was left empty.";
      }
      else
        userDR.setHospital(testHospital);

      // Medical certification
      if ((testCertification == null) ||
          (testCertification.startsWith("-")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileCertificationEmpty");//"The certification field was left empty.";
      }
      else
        userDR.setMedCertification(testCertification);

      // Work area
      if (testWorkArea == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileWorkAreaEmpty");//"The work area field was left empty.";
      }
      else if (testWorkArea.startsWith("-"))
        userDR.setWorkArea(null);
      else
        userDR.setWorkArea(testWorkArea);

      // Usage area
      if (testUsageArea == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileUsageAreaEmpty");//"The usage area field was left empty.";
      }
      else if (testUsageArea.startsWith("-"))
        userDR.setUsageArea(null);
      else
        userDR.setUsageArea(testUsageArea);

      // Network Environment
      if (testNetworkEnv == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileNetworkEnvEmpty");//"The network environment field was left empty.";
      }
      else if (testNetworkEnv.startsWith("-"))
        userDR.setNetworkEnv(null);
      else
        userDR.setNetworkEnv(testNetworkEnv);

      // Email
      if ((testEmail == null) ||
          (testEmail.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileEmailEmpty");//"The email field was left empty.";
      }
      // Check email field is valid syntax
      else if ((testEmail.indexOf('@') == -1) ||
               (testEmail.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("dr_profileEmailInvalid");//"The email supplied was invalid.";
      }
      else
        userDR.setEmail(testEmail);

      // Boolean fields
      userDR.setPublicOfficial(new Boolean(testPublicOfficial != null));
      userDR.setMRKunMail(new Boolean(testMRKunMail != null));
      userDR.setEmailInfo(new Boolean(testEmailInfo != null));
    }
    catch (Exception errAny)
    {
      throw new ApplicationError("Validation error during DR save" , errAny);
    }

    try
    {
      // Create a DRManager and save the DR
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drm.updateDR(userDR, getWLCookie(request));

    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens	
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on updating DR in doAction and RemoteException is thrown.");
    }

    if (!finalErrorMessage.equals(""))
      throw new ValidationException(finalErrorMessage);
  }
}

