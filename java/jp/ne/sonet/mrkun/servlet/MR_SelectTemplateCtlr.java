
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
 * This class is the boundary class for the MR5.4 use case. It forms the
 * controller part of an MVC pattern with the mr5_4.jsp page and the
 * TemplateManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_SelectTemplateCtlr.java,v 1.1.2.14 2001/11/13 07:56:55 rick Exp $
 */
public class MR_SelectTemplateCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr05_4_View;
  final String  SUBMITTED_TEMPLATE    = HttpConstant.Mr05_4a_View;

  // Parameters passed in by the browser
  final String  TEMPLATE_ID_PARAMETER       = "templateId";
  final String  SUBMIT_PARAMETER            = "submitForm";

  final String  REQUEST_CURRENT_TEMPLATE    = "templateId";
  final String  REQUEST_TEMPLATE_CATEGORIES = "categoryList";
  final String  REQUEST_REPLY_MESSAGE       = "replyMessage";
  final String  REQUEST_TEMPLATE            = "template";
  final String  REQUEST_PAGE_MR             = "pageMR";
  final String  SESSION_ERROR_MESSAGE       = "sessionErrorMessage";

  /**
   * This method handles any kind of request that comes to the servlet.
   * It verifies that a valid templateId has been passed in and then
   * forwards on to the JSP page that shows a preview of that template.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    MR sessionMR = null;
    String sessionId = getWLCookie(request);
    
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
    
      request.setAttribute(REQUEST_PAGE_MR, sessionMR);

      // Get the message details from the session - If none found, make one
      Map pendingMessageDetails = getMessageInSession(request);

      if (request.getParameter(TEMPLATE_ID_PARAMETER) != null)
      {
        // Retrieve all the template and message details
        EDetail message        = (EDetail) pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);
        String  origMessageId  = (String) pendingMessageDetails.get(HttpConstant.ORIGINAL_MESSAGE_ID_IN_SESSION);
        String  templateId     = request.getParameter(TEMPLATE_ID_PARAMETER);

        // Load the template object collection
        if (!origMessageId.equals(""))
          request.setAttribute(REQUEST_REPLY_MESSAGE, getMessageById(origMessageId, sessionMR, sessionId).getBody());
        pendingMessageDetails.put(HttpConstant.TEMPLATE_ID_IN_SESSION, templateId);
        Template tmpl = loadTemplate(templateId, getWLCookie(request));
        message.setBody(tmpl.getBody());
        request.setAttribute(REQUEST_TEMPLATE, tmpl);
        super.forwardToTemplate(SUBMITTED_TEMPLATE, request, response);
      }
      else
      {
        // Retrieve all the message details
        String templateId = (String) pendingMessageDetails.get(HttpConstant.TEMPLATE_ID_IN_SESSION);
        request.setAttribute(REQUEST_CURRENT_TEMPLATE, templateId);

        // Load the template object collection
        request.setAttribute(REQUEST_TEMPLATE_CATEGORIES, loadTemplateCategories(sessionMR, getWLCookie(request)));
        super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
      } 
  }

  /**
   * Load all templates from persistent storage
   */
  protected Collection loadTemplateCategories(MR sessionMR, String sessionId)
  {
    try
    {
      AssetManagerRemoteIntf tmplMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      return tmplMgr.getTemplateList(sessionMR.getCompany(), sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the AssetManager EJB", errRemote);
    }
  }
}

