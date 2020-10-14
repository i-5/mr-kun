
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.rmi.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.ejb.*;

import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;

/**
 * The servlet that handles incoming HTTP requests from the MR4.0 and 4.1 pages.
 * This page allows Show / Edit for a DRInformation object.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_ShowDoctorProfileCtlr.java,v 1.1.2.20 2001/11/13 07:56:55 rick Exp $
 */
public class MR_ShowDoctorProfileCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE            = HttpConstant.Mr09_View;
  final String  VIEW_TEMPLATE             = HttpConstant.Mr04_View;
  final String  EDIT_TEMPLATE             = HttpConstant.Mr04_1_View;

  // Parameters in the HTTP form within the JSP page
  final String  EDIT_PARAMETER            = "edit";
  final String  SAVE_PARAMETER            = "save";

  final String  DRID_PARAMETER            = "drId";
  final String  NAME_PARAMETER            = "name";
  final String  HOSPITAL_PARAMETER        = "hospital";
  final String  CLIENTID_PARAMETER        = "customerId";
  final String  HOSPITALID_PARAMETER      = "hospitalId";
  final String  IMPORTANCE_PARAMETER      = "importance";
  final String  FIELD_PARAMETER           = "field";
  final String  SPECIALTY_PARAMETER       = "specialty";
  final String  POSITION_PARAMETER        = "position";
  final String  UNIVERSITY_PARAMETER      = "university";
  final String  GRADUATION_YEAR_PARAMETER = "gradyear";
  final String  HOBBIES_PARAMETER         = "hobbies";
  final String  MEMO_PARAMETER            = "memo";

  // Objects for populating the JSP page
  final String  IMPORTANCE_LABELS_LIST    = "importanceLabelsList";
//  final String  IMPORTANCE_VALUES_LIST    = "importanceValuesList";
  final String  REQUEST_DRINFO            = "drInfo";
  final String  REQUEST_DOCTOR            = "fromDoctor";
  final String  REQUEST_PAGE_MR           = "pageMR";

  final String  ERROR_MESSAGE             = "errorMessage";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

  private String errorField = "";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays the current MR state.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
  {
    MR sessionMR = null;
    String sessionId = getWLCookie(request);
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
    
      if (request.getParameter(DRID_PARAMETER) == null)
        throw new ApplicationError("No DrID passed");
      else
      {
        request.setAttribute(REQUEST_PAGE_MR, sessionMR);
        request.setAttribute(ERROR_MESSAGE, "");
        setupComboLists(request, sessionMR.getCompany());

        // Load the DRInformation object
        String drId = request.getParameter(DRID_PARAMETER);
        DRInformation drInfo = getDrInfo(sessionMR.getMrId(), drId, sessionId);
        DR doctor = getDrInfoFromDoctor(drId, sessionId);

        if (drInfo == null || doctor == null)
          this.forwardToTemplate(ERROR_TEMPLATE, request, response);
        else
        {
          // Validate and save the submitted parameters if requested
          if (request.getParameter(SAVE_PARAMETER) != null){
            try
            {
              validateAndSave(drInfo, sessionMR, request);
            }
            catch( ValidationException ex )
            {
              request.setAttribute(REQUEST_DRINFO, drInfo);
              request.setAttribute(REQUEST_DOCTOR, doctor);
              request.setAttribute(ERROR_MESSAGE, errorField);
              forwardToTemplate(EDIT_TEMPLATE, request, response);
              return;
            }
          }
          request.setAttribute(REQUEST_DRINFO, drInfo);
          request.setAttribute(REQUEST_DOCTOR, doctor);
          if (request.getParameter(EDIT_PARAMETER) != null)
            forwardToTemplate(EDIT_TEMPLATE, request, response);
          else
            forwardToTemplate(VIEW_TEMPLATE, request, response);
        }
      } 
  }

  /**
   * Validate the parameters passed in and save them to the MR object if
   * they pass all the validation checks.
   * @param userDRInfo The DRInformation object to be saved to.
   * @param userMR Used to save the DRInformation object. See comments in
   *               the save section of the code.
   * @param request The HTTP request object containing the new MR values.
   * @exception ValidationException thrown if a parameter is invalid
   */
  private void validateAndSave(DRInformation userDRInfo,
                               MR userMR,
                               HttpServletRequest request)
    throws ValidationException
  {
    try
    {
      // Retrieve all the values
      String testName             = StringUtils.getParameter(request, NAME_PARAMETER);
      String testHospital         = StringUtils.getParameter(request, HOSPITAL_PARAMETER);
      String testClientId         = StringUtils.getParameter(request, CLIENTID_PARAMETER);
      String testHospitalId       = StringUtils.getParameter(request, HOSPITALID_PARAMETER);
      String testImportance       = StringUtils.getParameter(request, IMPORTANCE_PARAMETER);
      String testField            = StringUtils.getParameter(request, FIELD_PARAMETER);
      String testSpecialty        = StringUtils.getParameter(request, SPECIALTY_PARAMETER);
      String testPosition         = StringUtils.getParameter(request, POSITION_PARAMETER);
      String testUniversity       = StringUtils.getParameter(request, UNIVERSITY_PARAMETER);
      String testYearOfGraduation = StringUtils.getParameter(request, GRADUATION_YEAR_PARAMETER);
      String testHobby            = StringUtils.getParameter(request, HOBBIES_PARAMETER);
      String testMemo             = StringUtils.getParameter(request, MEMO_PARAMETER);
	
      System.out.println("Hobby's length = " + testHobby.length());
      
      // Check that all string fields are not empty
      if ((testName == null) ||
          (testName.equals("")))
      {
        errorField = SystemMessages.get("mr_drprofNameEmpty");//"The name field was left empty.";
        throw new ValidationException(errorField);
      }
      if ((testHospital == null) ||
          (testHospital.equals("")))
      {
        errorField = SystemMessages.get("mr_drprofHospitalEmpty");//"The hospital field was left empty.";
        throw new ValidationException(errorField);
      }
      if (testClientId == null)
      {
        errorField = SystemMessages.get("mr_drprofCustomerIdEmpty");//"The customerId field was left empty.";
        throw new ValidationException(errorField);
      }
      if (testHospitalId == null)
      {
        errorField = SystemMessages.get("mr_drprofHospitalIdEmpty");//"The hospitalId field was left empty.";
        throw new ValidationException(errorField);
      }
      if ((testImportance == null) ||
          (testImportance.equals("")))
      {
        errorField = SystemMessages.get("mr_drprofImportanceEmpty");//"The importance field was left empty.";
        throw new ValidationException(errorField);
      }
      if (testField == null)
      {
        errorField = SystemMessages.get("mr_drprofDivisionEmpty");//"The field/division field was left empty.";
        throw new ValidationException(errorField);
      }
      if (testSpecialty == null)
      {
        errorField = SystemMessages.get("mr_drprofSpecialtyEmpty");//"The specialty field was not supplied.";
        throw new ValidationException(errorField);
      }
      if (testPosition == null)
      {
        errorField = SystemMessages.get("mr_drprofPositionEmpty");//"The position field was not supplied.";
        throw new ValidationException(errorField);
      }
      if (testUniversity == null)
      {
        errorField = SystemMessages.get("mr_drprofUniversityEmpty");//"The university field was not supplied.";
        throw new ValidationException(errorField);
      }
      if (testYearOfGraduation == null)
      {
        errorField = SystemMessages.get("mr_drprofYearGradEmpty");//"The year of graduation field was not supplied.";
        throw new ValidationException(errorField);
      }
      if (testHobby == null)
      {
        errorField = SystemMessages.get("mr_drprofHobbyEmpty");//"The hobby field was left empty.";
        throw new ValidationException(errorField);
      }
      /* This will validate if the field user entered has exceed 64 full Japanese charaters - Damon 011106 */ 
      if (testHobby.length() > 64)
      {
        errorField = SystemMessages.get("mr_hobbyExceedCharater");//"The hobby field was left empty.";
        throw new ValidationException(errorField);
      }
      if (testMemo == null)
      {
        errorField = SystemMessages.get("mr_drprofMemoEmpty");//"The memo field was left empty.";
        throw new ValidationException(errorField);
      }
      /* This will validate if the field user entered has exceed 64 full Japanese charaters - Damon 011106 */
      if (testMemo.length() > 64)
      {
        errorField = SystemMessages.get("mr_drMemoExceedCharater");//"The memo field was left empty.";
        throw new ValidationException(errorField);
      }
      
      // Verify the combo fields are within the allowed lists
//      if (!((Collection) request.getAttribute(IMPORTANCE_VALUES_LIST)).contains(testImportance))
//        throw new ValidationException("The importance value is not within the allowed options.");
      List labellist = (List)request.getAttribute(IMPORTANCE_LABELS_LIST);
      boolean checkflag=false;
      String labelName = null;
      for( int ii=0; ii<labellist.size(); ii++ ){
        Importance imp = (Importance)labellist.get(ii);
        if(imp.getImportanceId().equalsIgnoreCase(testImportance)){
          labelName = imp.getName();
          checkflag = true;
          break;
        }
      }
      if(!checkflag)
	      throw new ValidationException(SystemMessages.get("mr_drprofIllegalImportanceValue"));//"The importance value is not within the allowed options.");

      // Validation successful - Modify the DRInfo object
      userDRInfo.setName(testName);
      userDRInfo.setHospital(testHospital);
      userDRInfo.setClientId(testClientId);
      userDRInfo.setHospitalId(testHospitalId);
      userDRInfo.setOccupation(testField);
      userDRInfo.setDivision(testSpecialty);
      userDRInfo.setPosition(testPosition);
      userDRInfo.setGraduatedUniversity(testUniversity);
      userDRInfo.setYearOfGraduation(testYearOfGraduation);
      userDRInfo.setHobby(testHobby);
      userDRInfo.setMemo(testMemo);

      Importance importance = userDRInfo.getImportance();
      importance.setImportanceId(testImportance);
      importance.setName(labelName);

    }
    catch (ValidationException errValid)
    {
      throw errValid;
    }
    catch (Exception errAny)
    {
      throw new ValidationException(SystemMessages.get("mr_drprofOtherError") + errAny.toString());//"Validation error during MR save - " + errAny);
    }

    // Create a DRManager and save the DR
    MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
    try
    {
      mrm.updateDRInfo(userDRInfo, getWLCookie(request));
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error saving the DRInformation object", errRemote);
    }
  }

  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  private void setupComboLists(HttpServletRequest request, Company company)
  {

    try
    {
      // Load the manager bean
      MRManagerRemoteIntf mrMgr =
            (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      List importanceList = (List)mrMgr.getImportanceList(company, getWLCookie(request));

      request.setAttribute(IMPORTANCE_LABELS_LIST, importanceList);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with MRManager EJB", errRemote);
    }


    // Build a list of options for the importance field
/**
    List importanceOptionLabels = new ArrayList();
    List importanceOptionValues = new ArrayList();

    importanceOptionLabels.add("low");
    importanceOptionValues.add("low");

    importanceOptionLabels.add("medium");
    importanceOptionValues.add("medium");

    importanceOptionLabels.add("Åd—v");
    importanceOptionValues.add("high");

    request.setAttribute(IMPORTANCE_LABELS_LIST, importanceOptionLabels);
    request.setAttribute(IMPORTANCE_VALUES_LIST, importanceOptionValues);
**/
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

  /**
   * Get a DR object for a particular DR
   */
  private DR getDrInfoFromDoctor(String drId, String sessionId)
  {
    try
    {
      // Load the manager bean
      DRManagerRemoteIntf drMgr =
            (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      return drMgr.getDRById(drId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error communicating with DRManager EJB", errRemote);
    }
  }

}
