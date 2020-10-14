
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
 * @version $Id: DR_EnqueteVol1Ctlr.java,v 1.1.2.7 2001/12/07 06:43:19 mizuki Exp $
 */
public class DR_EnqueteVol1Ctlr extends BaseServlet 
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  VIEW_TEMPLATE         = HttpConstant.DrEnqueteVol1View;
  final String  NEXT_TEMPLATE         = HttpConstant.Dr00_4_Ctlr;

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

  final String  FILEDNAME             = "company";
  final String  ENQUETE_ID            = "0001";

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
      request.setAttribute(QUESTION_LIST, questionList);

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
        if( enqinfo.getMessageHeaderId() == null ||
            enqinfo.getMessageHeaderId().equals("") )
          next_page = NEXT_TEMPLATE;
        else
        {
          request.setAttribute(FINISH_MESSAGE, "Enquete Finish");
          next_page = VIEW_TEMPLATE;
        }
//System.out.println( "Finish: " + next_page );
      }
      // Check for the save indicator (ie the calling page has
      // saveable data
      else if (request.getParameter(SAVE_PARAMETER) != null)
      {
        // Validate and save the submitted parameters
        validateAndSave(enqinfo,request);
        if( enqinfo.getMessageHeaderId() == null ||
            enqinfo.getMessageHeaderId().equals("") )
          next_page = NEXT_TEMPLATE;
        else
        {
          request.setAttribute(FINISH_MESSAGE, "Enquete Finish");
          next_page = VIEW_TEMPLATE;
        }
//System.out.println( "Input: " + next_page );

      }
      else
      {
//System.out.println( "SAVE_PARAMETER: \"" + request.getParameter(SAVE_PARAMETER) + "\"");
        questionList = (Collection)drm.getEnqueteQuestion( enqinfo, getWLCookie(request) );
        request.setAttribute(QUESTION_LIST, questionList);

        // Check for the Profile input finish ?
//        if(!(enqinfo.getMessageHeaderId() == null ||
//          enqinfo.getMessageHeaderId().equals("")))
//        {
//          request.setAttribute(FINISH_MESSAGE, "Enquete Finish");
//        }
        next_page = VIEW_TEMPLATE;
      }
      forwardToTemplate(next_page, request, response);
    }
    catch(ValidationException errValid)
    {
      try{
        EnqueteInfo enqinfo = (EnqueteInfo)request.getAttribute(ENQUETE_INFO);
        DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
        questionList = (Collection)drm.getEnqueteQuestion( enqinfo, getWLCookie(request) );
        request.setAttribute(QUESTION_LIST, questionList);
      }
      catch(RemoteException errRemote)
      {
        throw new ApplicationError("get Enquete Question List in Error" , errRemote);
      }

      request.setAttribute(ERROR_MESSAGE, errValid.getMessage());
      forwardToTemplate(VIEW_TEMPLATE, request, response);
    }
    catch(RemoteException errRemote)
    {
      throw new ApplicationError("get EnqueteInfo in Error" , errRemote);
    }
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
      String[] Answer = request.getParameterValues(FILEDNAME);
      for( ii=0; ii<Answer.length; ii++ ){
        if( Answer[ii].equals( "" ) )	continue;
        EnqueteAnswer ans = new EnqueteAnswer();
        ans.setMinorId(enqinfo.getGroupId());
        ans.setFiled(FILEDNAME);
        ans.setAnswer(new String(Answer[ii].getBytes("8859_1"), "SJIS"));
        enqinfo.addEnqueteAnswer( ans );
      }

      if( enqinfo.getEnqueteAnswer() == null )
        finalErrorMessage = SystemMessages.get("dr_emptyEnqueteAnswer");//"Empty Answer";
    }
    catch (Exception errAny)
    {
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
