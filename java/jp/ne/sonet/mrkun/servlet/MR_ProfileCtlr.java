
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.text.*;
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
 * The servlet that handles incoming HTTP requests from the MR7 page.
 * This page allows Show / Edit / Cancel for the MR object.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_ProfileCtlr.java,v 1.1.2.26 2001/11/13 07:56:55 rick Exp $
 */
public class MR_ProfileCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  VIEW_TEMPLATE         = HttpConstant.Mr07_View;
  final String  EDIT_TEMPLATE         = HttpConstant.Mr07_View;

  // Parameters in the HTTP form within the JSP page
  final String  CANCEL_PARAMETER      = "cancel";
  final String  SAVE_PARAMETER        = "save";

  final String  COMPANY_PHONE_PARAMETER   = "phone_work";
  final String  CELLULAR_PHONE_PARAMETER  = "phone_mobile";
  final String  FAX_PHONE_PARAMETER       = "phone_fax";
  final String  EMAIL_PARAMETER           = "mr_email";
  final String  AREA_CODE_PARAMETER       = "zipcode";
  final String  LOCATION_PARAMETER        = "streetaddress";
  final String  OPEN_HOURS_PARAMETER      = "hours";
  final String  OPEN_TIME_PARAMETER       = "hour_open";
  final String  CLOSE_TIME_PARAMETER      = "hour_close";
  final String  FORWARD_1_PARAMETER       = "forward_sent_email_1";
  final String  FORWARD_2_PARAMETER       = "forward_sent_email_2";
  final String  FORWARD_3_PARAMETER       = "forward_sent_email_3";
  final String  FORWARD_4_PARAMETER       = "forward_sent_email_4";
  final String  CC_EMAIL_PARAMETER        = "forward_recieved_email";
  final String  PASSWORD_1_PARAMETER      = "password_1";
  final String  PASSWORD_2_PARAMETER      = "password_2";
  final String  UID_PARAMETER             = "uid";
  final String  SESSION_ERROR_MESSAGE     = "sessionErrorMessage";
  final String  ERROR_MESSAGE             = "errorMessage";

  // Lists for populating combo boxes in the JSP page
  final String  OPEN_TIMES_LIST             = "OpenTimesList";
  final String  CLOSE_TIMES_LIST            = "CloseTimesList";
  final String  MEDICAL_CERTIFICATION_LIST  = "MedicalCertificationList";
  final String  REQUEST_MR                  = "pageMR";

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
    
    // Check for an MR in session
    sessionMR = (MR) checkSession(request);
    request.setAttribute(REQUEST_MR, sessionMR);
    request.setAttribute(ERROR_MESSAGE, "");
    setupComboLists(request);

    // Check for the save indicator (ie the calling page has
   // saveable data
    if (request.getParameter(SAVE_PARAMETER) != null)
    {
      // Validate and save the submitted parameters
      try
      {
        validateAndSave(sessionMR, request);
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }
      catch (ValidationException errValid)
      {
        request.setAttribute(ERROR_MESSAGE, errValid.getMessage());
        forwardToTemplate(EDIT_TEMPLATE, request, response);
      }
    }
    else
      forwardToTemplate(EDIT_TEMPLATE, request, response);
  }

  /**
   * Validate the parameters passed in and save them to the MR object if
   * they pass all the validation checks.
   * @param userMR The MR object to be saved to.
   * @param request The HTTP request object containing the new MR values.
   * @exception ValidationException thrown if a parameter is invalid
   */
  private void validateAndSave(MR userMR, HttpServletRequest request)
    throws ValidationException
  {
    String finalErrorMessage = "";
    try
    {
      // Retrieve all the values
      String testCompanyPhone     = StringUtils.getParameter(request, COMPANY_PHONE_PARAMETER);
      String testCellularPhone    = StringUtils.getParameter(request, CELLULAR_PHONE_PARAMETER);
      String testFaxPhone         = StringUtils.getParameter(request, FAX_PHONE_PARAMETER);
      String testEmail            = StringUtils.getParameter(request, EMAIL_PARAMETER);
      String testAddressAreaCode  = StringUtils.getParameter(request, AREA_CODE_PARAMETER);
      String testAddressLocation  = StringUtils.getParameter(request, LOCATION_PARAMETER);
      String testOpenHours        = StringUtils.getParameter(request, OPEN_HOURS_PARAMETER);
      String testOpenTime         = StringUtils.getParameter(request, OPEN_TIME_PARAMETER);
      String testCloseTime        = StringUtils.getParameter(request, CLOSE_TIME_PARAMETER);
      String testForwardEmail1    = StringUtils.getParameter(request, FORWARD_1_PARAMETER);
      String testForwardEmail2    = StringUtils.getParameter(request, FORWARD_2_PARAMETER);
      String testForwardEmail3    = StringUtils.getParameter(request, FORWARD_3_PARAMETER);
      String testForwardEmail4    = StringUtils.getParameter(request, FORWARD_4_PARAMETER);
      String testCCEmail          = StringUtils.getParameter(request, CC_EMAIL_PARAMETER);
      String testPassword1        = StringUtils.getParameter(request, PASSWORD_1_PARAMETER);
      String testPassword2        = StringUtils.getParameter(request, PASSWORD_2_PARAMETER);

      // Build the company phone contact
      if (testCompanyPhone == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileCompanyPhoneNotSupplied");//"The company phone field was left empty.");
      }
      else if (testCompanyPhone.equals(""))
        userMR.setCompanyPhone(null);
      else
      {
        if (userMR.getCompanyPhone() == null)
          userMR.setCompanyPhone(new PhoneContact());
        PhoneContact mrCompanyPhone = userMR.getCompanyPhone();
        mrCompanyPhone.setPhoneNumber(testCompanyPhone);
      }

      // Build the cellular phone contact
      if (testCellularPhone == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileCellularPhoneNotSupplied");////"The cellular phone field was left empty.");
      }
      else if (testCellularPhone.equals(""))
        userMR.setCellularPhone(null);
      else
      {
        if (userMR.getCellularPhone() == null)
          userMR.setCellularPhone(new PhoneContact());
        PhoneContact mrCellularPhone = userMR.getCellularPhone();
        mrCellularPhone.setPhoneNumber(testCellularPhone);
      }

      // Build the fax phone contact
      if (testFaxPhone == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileFaxPhoneNotSupplied");//"The fax phone field was left empty.");
      }
      else if (testFaxPhone.equals(""))
        userMR.setFaxPhone(null);
      else
      {
        if (userMR.getFaxPhone() == null)
          userMR.setFaxPhone(new PhoneContact());
        PhoneContact mrFaxPhone = userMR.getFaxPhone();
        mrFaxPhone.setPhoneNumber(testFaxPhone);
      }

      // Build the email contact
      if (testEmail == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileEmailFieldNotSupplied");//"The email field was left empty.");
      }
      else if (testEmail.equals(""))
        userMR.setEmail(null);
      else if ((testEmail.indexOf('@') == -1) || (testEmail.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileEmailFieldNotSupplied");//"The email field was left empty.");
      }
      else
        userMR.setEmail(testEmail);

      // Build the ccEmail contact
      if (testCCEmail == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileCcEmailNotSupplied");//"The CC email field was left empty.");
      }
      else if (testCCEmail.equals(""))
        userMR.setCCEmail(null);
      else if ((testCCEmail.indexOf('@') == -1) || (testCCEmail.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileCcEmailInvalid");//"The CC email supplied was invalid.");
      }
      else
        userMR.setCCEmail(testCCEmail);

      // Flush all forward emails
      List forwardEmail = userMR.getForwardEmailList();
      forwardEmail.clear();

      // Validate the first
      if (testForwardEmail1 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail1NotSupplied");//"The forward email 1 field was not supplied.");
      }
      else if (testForwardEmail1.equals(""))
      {/* Do nothing */}
      else if ((testForwardEmail1.indexOf('@') == -1) || (testForwardEmail1.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail1Invalid");//"The forward email 1 supplied was invalid.");
      }
      else
        userMR.addForwardEmail(testForwardEmail1);

      // Validate the 2nd
      if (testForwardEmail2 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail2NotSupplied");//"The forward email 2 field was not supplied.");
      }
      else if (testForwardEmail2.equals(""))
      {/* Do nothing */}
      else if ((testForwardEmail2.indexOf('@') == -1) || (testForwardEmail2.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail2Invalid");//"The forward email 2 supplied was invalid.");
      }
      else
        userMR.addForwardEmail(testForwardEmail2);

      // Validate the 3rd
      if (testForwardEmail3 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail3NotSupplied");//"The forward email 3 field was not supplied.");
      }
      else if (testForwardEmail3.equals(""))
      {/* Do nothing */}
      else if ((testForwardEmail3.indexOf('@') == -1) || (testForwardEmail3.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail3Invalid");//"The forward email 3 supplied was invalid.");
      }
      else
        userMR.addForwardEmail(testForwardEmail3);

      // Validate the 4th
      if (testForwardEmail4 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail4NotSupplied");//"The forward email 4 field was not supplied.");
      }
      else if (testForwardEmail4.equals(""))
      {/* Do nothing */}
      else if ((testForwardEmail4.indexOf('@') == -1) || (testForwardEmail4.indexOf('.') == -1))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileForwardEmail4Invalid");//"The forward email 4 supplied was invalid.");
      }
      else
        userMR.addForwardEmail(testForwardEmail4);


      // Validate address object
      if (testAddressAreaCode == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileAreaCodeNotSupplied");//"The area code field was left empty.");
      }
      else if (testAddressLocation == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileLocationNotSupplied");//"The location field was left empty.");
      }
      else if (testAddressAreaCode.equals("") && testAddressLocation.equals(""))
      {
        userMR.setAddress(null);
      }
      else
      {
        String stripped = StringUtils.globalReplace(testAddressAreaCode, "-", "");
        try
        {
          // Check for a valid number (without the slashes)
          if (Integer.parseInt(stripped) > 0)
          {
            if (userMR.getAddress() == null)
              userMR.setAddress(new Address());
            Address mrAddress = userMR.getAddress();
            mrAddress.setAreaCode(testAddressAreaCode);
            mrAddress.setLocation(testAddressLocation);
          }
        }
        catch (NumberFormatException errNumFormat)
        {
          if (finalErrorMessage.equals(""))
            finalErrorMessage = SystemMessages.get("mr_profileAreaCodeInvalid");//"The location field was left empty.");
        }
      }

      // Check passwords
      if (testPassword1 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profilePassword1NotSupplied");//"The password 1 field was left empty.");
      }
      else if (testPassword2 == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profilePassword2NotSupplied");//"The password 2 field was left empty.");
      }
      else if (!testPassword1.equals(testPassword2))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profilePasswordsDifferent");//"The password fields do not match.");
      }
      else
        userMR.setPassword(testPassword1);

      // Verify the combo fields are within the allowed lists
      if ((testOpenHours == null) || (testOpenHours.equals("")))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileOpenHoursNotSupplied");//"The open hours field was left empty.");
      }
      else
        userMR.setOpenDays(new Integer(testOpenHours));

      if (testOpenTime == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileOpenTimeNotSupplied");//"The open time field was left empty.");
      }
      else if (testCloseTime == null)
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileCloseTimeNotSupplied");//"The close time field was left empty.");
      }
      else if (testOpenTime.equals("-") || testCloseTime.equals("-"))
      {
        userMR.setOpenHoursSetting(new Boolean(false));
        userMR.setOpenTime(null);
        userMR.setCloseTime(null);
      }
      else if (!((Collection) request.getAttribute(OPEN_TIMES_LIST)).contains(testOpenTime))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileInvalidOpenTime");//"The open time value is not within the allowed options.");
      }
      else if (!((Collection) request.getAttribute(CLOSE_TIMES_LIST)).contains(testCloseTime))
      {
        if (finalErrorMessage.equals(""))
          finalErrorMessage = SystemMessages.get("mr_profileInvalidCloseTime");//"The close time value is not within the allowed options.");
      }
      else
      {
        // Check the open time is before the close time
        DateFormat sdfTimes = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try
        {
          Date openTime   = sdfTimes.parse("01/01/2001 " + testOpenTime);
          Date closeTime  = sdfTimes.parse("01/01/2001 " + testCloseTime);
          if (!openTime.before(closeTime))
          {
            if (finalErrorMessage.equals(""))
              finalErrorMessage = SystemMessages.get("mr_profileCloseBeforeOpen");//"The closing time was before the opening time");
          }
          else
          {
            userMR.setOpenHoursSetting(new Boolean(true));
            userMR.setOpenTime(testOpenTime);
            userMR.setCloseTime(testCloseTime);
          }
        }
        catch (ParseException errParse)
        {
          if (finalErrorMessage.equals(""))
            finalErrorMessage = SystemMessages.get("mr_profileInvalidOpenCloseTimes");//"The date supplied was not in a valid time format");
        }
      }

    }
    catch (Exception errAny)
    {
      StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errAny.printStackTrace(p);
      System.out.println("Error validating ....\n" + s.toString());
      throw new ValidationException(SystemMessages.get("mr_validationError") + //"Validation error during MR save - "
                                    errAny.getMessage());
    }

    try
    {
      // Create an MRManager and save the MR
	    MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      mrm.updateMR(userMR, getWLCookie(request));
    }
    catch (RemoteException errRemote)
    {
	  // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on updating MR in doAction and RemoteException is thrown.");
    }
    
    if (!finalErrorMessage.equals(""))
      throw new ValidationException(finalErrorMessage);

  }

  /**
   * Set up the combos for the page.
   * Note: These values should be replaced with lookups from a database.
   * @param request The request variable to add the collections to.
   */
  private void setupComboLists(HttpServletRequest request)
  {
    // Build a list of times on the half hour and isert into collection
    Collection openTimes = new ArrayList();
    Collection closeTimes = new ArrayList();
    DecimalFormat dfm = new DecimalFormat("00");
    for (int nLoop = 0; nLoop < 24; nLoop++)
    {
      openTimes.add(dfm.format(nLoop) + ":00");
      openTimes.add(dfm.format(nLoop) + ":30");
      closeTimes.add(dfm.format(nLoop) + ":00");
      closeTimes.add(dfm.format(nLoop) + ":30");
    }
    request.setAttribute(OPEN_TIMES_LIST, openTimes);
    request.setAttribute(CLOSE_TIMES_LIST, closeTimes);

  }

}