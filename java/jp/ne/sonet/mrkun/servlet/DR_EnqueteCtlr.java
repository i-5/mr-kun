
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
 * @version $Id: DR_EnqueteCtlr.java,v 1.1.2.3 2001/11/13 07:56:55 rick Exp $
 */
public class DR_EnqueteCtlr extends BaseServlet 
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr11_View;
  final String  VIEW_TEMPLATE         = HttpConstant.DrEnqueteVol1Ctlr;
  final String  NEXT_TEMPLATE         = HttpConstant.Dr00_4_Ctlr;

  // Parameters in the HTTP form within the JSP page
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  final String  ENQUETE_PARAMETER     = "enqueteInfo";


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
    
    // Check for an DR in session
    try
    {
      sessionDR = (DR) checkSession(request);
      // get Enquete Question List
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      EnqueteInfo enqinfo = (EnqueteInfo)drm.getEnqueteInfo(sessionDR.getDrId(), getWLCookie(request));
      if( enqinfo == null )
        forwardToTemplate(NEXT_TEMPLATE, request, response);

      request.setAttribute(ENQUETE_PARAMETER, enqinfo);

      // Goto Enquete Page
//      forwardToTemplate(enqinfo.getUrl, request, response);
      forwardToTemplate(VIEW_TEMPLATE, request, response);

    }
    catch(RemoteException errRemote)
    {
      throw new ApplicationError("get Medical Qualification List and Specialty List in Error" , errRemote);
    }
  }
}
