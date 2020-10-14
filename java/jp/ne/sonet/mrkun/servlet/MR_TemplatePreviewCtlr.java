
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
 * This class is the boundary class for the MR5.5 use case. It forms the
 * controller part of an MVC pattern with the mr5_5.jsp page and the
 * TemplateManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_TemplatePreviewCtlr.java,v 1.1.2.8 2001/11/13 07:56:55 rick Exp $
 */
public class MR_TemplatePreviewCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr05_5_View;

  // Parameters passed in by the browser
  final String  TEMPLATE_ID_PARAMETER = "templateId";
  final String  REQUEST_TEMPLATE      = "template";
  final String  REQUEST_PAGE_MR       = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

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
    
    // Check for an MR in session
    sessionMR = (MR) checkSession(request);

    if (request.getParameter(TEMPLATE_ID_PARAMETER) == null)
      throw new ApplicationError("No template id supplied");
    else
    {
      // Load the template object
      String templateId = request.getParameter(TEMPLATE_ID_PARAMETER);
      Template tmpl = loadTemplate(templateId, getWLCookie(request));
      request.setAttribute(REQUEST_TEMPLATE, tmpl);
      request.setAttribute(REQUEST_PAGE_MR, sessionMR);
      super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
    }
  }


}

