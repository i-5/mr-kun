
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
 * @version $Id: DR_EnqueteVol2Ctlr.java
 */
public class DR_EnqueteVol2Ctlr extends BaseServlet 
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  VIEW_TEMPLATE         = HttpConstant.DrEnqueteVol2View;
//  final String  NEXT_TEMPLATE         = HttpConstant.Dr00_4_Ctlr;

  // Parameters in the HTTP form within the JSP page
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  SESSION_MANAGER_NAME  = "SessionManager";
  final String  SAVE_PARAMETER        = "agreement";

  // Lists for populating combo boxes in the JSP page
  final String  REQUEST_DR            = "pageDR";
  final String  ERROR_MESSAGE         = "errorMessage";
  final String  FINISH_MESSAGE        = "finishMessage";
  final String  ENQUETE_INFO          = "enqueteInfo";
  final String  QUESTION_LIST         = "questionList";

  final String  FILEDNAME1             = "answer1";
  final String  FILEDNAME2             = "answer2";
  final String  FILEDNAME3             = "answer3";
  final String  FILEDNAME4             = "answer4";
  final String  ENQUETE_ID            = "0002";

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
    String next_page = VIEW_TEMPLATE;
    Collection questionList = new ArrayList();
    
    try
    {
      sessionDR = (DR) checkSession(request);

      request.setAttribute(ERROR_MESSAGE, "");
      request.setAttribute(FINISH_MESSAGE, "");
      request.setAttribute(REQUEST_DR, sessionDR);
//      request.setAttribute(QUESTION_LIST, questionList);

      // get Enquete Information
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      EnqueteInfo enqinfo = (EnqueteInfo)request.getAttribute(ENQUETE_INFO);
      if( enqinfo == null )
        enqinfo = new EnqueteInfo( sessionDR.getDrId(), ENQUETE_ID );
      enqinfo = (EnqueteInfo)drm.getEnqueteInfoByEnqId(enqinfo, getWLCookie(request));
      request.setAttribute(ENQUETE_INFO,enqinfo);

      // Inaccurate data Check
      if( enqinfo == null ){
        request.setAttribute(FINISH_MESSAGE, "Enquete Finish");
        forwardToTemplate(VIEW_TEMPLATE, request, response);
      }

      // if Enquete Finish ?
//System.out.println( "ReplyStatus:" + enqinfo.getReplyStatus().toString() );
      if ( enqinfo.getReplyStatus().booleanValue() )
      {
//System.out.println( "Finish: " + next_page );
        request.setAttribute(FINISH_MESSAGE, "Enquete Finish");
      }
      // Check for the save indicator (ie the calling page has
      // saveable data
      else if (request.getParameter(SAVE_PARAMETER) != null)
      {
        // Validate and save the submitted parameters
//System.out.println( "Input: " + next_page );
        validateAndSave(enqinfo,request);
        request.setAttribute(FINISH_MESSAGE, "Enquete Finish");

      }
//      else
//      {
//System.out.println( "SAVE_PARAMETER: \"" + request.getParameter(SAVE_PARAMETER) + "\"");
//      }
      forwardToTemplate(next_page, request, response);
    }
    catch(ValidationException errValid)
    {
//System.out.println( "ValidationException: " + errValid.toString() );
      request.setAttribute(ERROR_MESSAGE, errValid.getMessage());
      forwardToTemplate(VIEW_TEMPLATE, request, response);
    }
    catch(RemoteException errRemote)
    {
//System.out.println( "RemoteException: " + errRemote.toString() );
      throw new ApplicationError("get EnqueteInfo in Error" , errRemote);
    }
//    catch(Exception ex)
//    {
//System.out.println("Exception: ");
//ex.printStackTrace();
//    }
  }

  /**
   * Validate the parameters passed in and save them to the DR object if
   * they pass all the validation checks.
   * @param userDR The DR object to be saved to.
   * @param request The HTTP request object containing the new DR values.
   * @exception ValidationException thrown if a parameter is invalid
   */
  private void validateAndSave(EnqueteInfo enqinfo, HttpServletRequest request)
    throws ValidationException
  {
    String finalErrorMessage = "";

    try
    {
      int ii;

      String[] Answer3 = request.getParameterValues(FILEDNAME3);
      for( ii=0; ii<Answer3.length; ii++ ){
        if( Answer3[ii].equals( "" ) )	continue;
        EnqueteAnswer ans3 = new EnqueteAnswer();
        ans3.setMinorId("03");
        ans3.setFiled(FILEDNAME3);
        ans3.setAnswer(new String(Answer3[ii].getBytes("8859_1"), "SJIS"));
        enqinfo.addEnqueteAnswer( ans3 );
      }

      if( enqinfo.getEnqueteAnswer() == null )
        finalErrorMessage = SystemMessages.get("dr_emptyEnqueteAnswer");//"Empty Answer";

      String[] Answer4 = request.getParameterValues(FILEDNAME4);
      for( ii=0; ii<Answer4.length; ii++ ){
        if( Answer4[ii].equals( "" ) )	continue;
        EnqueteAnswer ans4 = new EnqueteAnswer();
        ans4.setMinorId("04");
        ans4.setFiled(FILEDNAME4);
        ans4.setAnswer(new String(Answer4[ii].getBytes("8859_1"), "SJIS"));
        enqinfo.addEnqueteAnswer( ans4 );
      }

      String Answer1 = request.getParameter(FILEDNAME1);
      if( Answer1 == null || Answer1.equals( "" ) )
        finalErrorMessage = SystemMessages.get("dr_emptyEnqueteAnswer");//"Empty Answer";
      else
      {
        EnqueteAnswer ans1 = new EnqueteAnswer();
        ans1.setMinorId("01");
        ans1.setFiled(FILEDNAME1);
        ans1.setAnswer(new String(Answer1.getBytes("8859_1"), "SJIS"));
        enqinfo.addEnqueteAnswer( ans1 );
      }

      String Answer2 = request.getParameter(FILEDNAME2);
      if( Answer2 == null || Answer2.equals( "" ) )
        finalErrorMessage = SystemMessages.get("dr_emptyEnqueteAnswer");//"Empty Answer";
      else
      {
        EnqueteAnswer ans2 = new EnqueteAnswer();
        ans2.setMinorId("02");
        ans2.setFiled(FILEDNAME2);
        ans2.setAnswer(new String(Answer2.getBytes("8859_1"), "SJIS"));
        enqinfo.addEnqueteAnswer( ans2 );
      }

    }
    catch (Exception errAny)
    {
System.out.println( "Exception: " + errAny.toString() );
      throw new ApplicationError("Validation error during DR save" , errAny);
    }
    if (!finalErrorMessage.equals(""))
      throw new ValidationException(finalErrorMessage);

    try
    {
      // Create a DRManager and save the DR
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drm.addEnqueteAnswer(enqinfo, getWLCookie(request));

    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens	
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on updating DR in doAction and RemoteException is thrown.");
    }

  }
}
