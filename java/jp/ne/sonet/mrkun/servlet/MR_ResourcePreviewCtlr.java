
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
 * This class is the boundary class for the MR5.3 use case. It forms the
 * controller part of an MVC pattern with the mr05-3.jsp page and the
 * AssetManagerEJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_ResourcePreviewCtlr.java,v 1.1.2.8 2001/11/13 07:56:55 rick Exp $
 */
public class MR_ResourcePreviewCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  FRAMESET_TEMPLATE     = HttpConstant.Mr05_3_Fr_View;
  final String  NAVBAR_TEMPLATE       = HttpConstant.Mr05_3_View;

  // Parameters passed in by the browser
  final String  LINK_ID_PARAMETER = "linkId";
  final String  NAVBAR_PARAMETER  = "navbar";

  final String  REQUEST_LINK      = "link";
  final String  REQUEST_PAGE_MR        = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";
  /**
   * This method handles any kind of request that comes to the servlet.
   * It displays a preview of the link, using a frameset to put a navbar
   * above the URL we're after.
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
    
      if (request.getParameter(LINK_ID_PARAMETER) == null)
        throw new ApplicationError("No link id supplied");
      else
      {
        request.setAttribute(REQUEST_PAGE_MR, sessionMR);
        String linkId = request.getParameter(LINK_ID_PARAMETER);

        // Load the link object
        ResourceLink link = loadLink(linkId, getWLCookie(request));
        request.setAttribute(REQUEST_LINK, link);

        // Display the navbar
        if (request.getParameter(NAVBAR_PARAMETER) == null)
          super.forwardToTemplate(FRAMESET_TEMPLATE, request, response);
        else
          super.forwardToTemplate(NAVBAR_TEMPLATE, request, response);
      }
  }

  /**
   * Load the link from persistent storage matching the supplied linkID.
   * @param templateId The id of the link to be loaded.
   */
  private ResourceLink loadLink(String linkId, String sessionId)
  {
    try
    {
      AssetManagerRemoteIntf linkMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      return linkMgr.getByLinkId(linkId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the LinkManager EJB", errRemote);
    }
  }

}

