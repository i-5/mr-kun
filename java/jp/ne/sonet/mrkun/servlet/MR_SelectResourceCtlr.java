
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
 * This class is the boundary class for the MR5.2 use case. It forms the
 * controller part of an MVC pattern with the mr05-2.jsp page and the
 * AssetManager EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MR_SelectResourceCtlr.java,v 1.1.2.12 2001/11/13 07:56:55 rick Exp $
 */
public class MR_SelectResourceCtlr extends EDetailServlet
{
  final String  ERROR_TEMPLATE            = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE          = HttpConstant.Mr05_2_View;
  final String  VALIDATION_ERROR_TEMPLATE = HttpConstant.Mr05_2_View;
  final String  SUBMITTED_TEMPLATE        = HttpConstant.Mr05_2a_View;
  final String  DELETED_TEMPLATE          = HttpConstant.Mr05_2a_View;
  
  // Parameters passed in by the browser
  final String  LINK_PREFIX                 = "link_";
  final String  SUBMIT_PARAMETER            = "submitForm";
  final String  DELETE_LINK_PARAMETER       = "deleteLink";

  final String  REQUEST_CURRENT_LINKS       = "selectedLinkIds";
  final String  REQUEST_LINK_CATEGORIES     = "categoryList";
  final String  REQUEST_LINK_LIST           = "linkList";
  final String  REQUEST_MR                  = "pageMR";
  final String  REQUEST_ERROR_MESSAGE       = "errorMessage";
  final String  SESSION_ERROR_MESSAGE       = "sessionErrorMessage";

  final int     MAX_LINKS_ALLOWED           = 5;
  
  /**
   * This method handles any kind of request that comes to the servlet.
   * It shows a list of links available from the link library for this company.
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
      request.setAttribute(REQUEST_ERROR_MESSAGE, "");

      request.setAttribute(REQUEST_MR, sessionMR);

      // Get the message details from the session - If none found, make one
      Map pendingMessageDetails = getMessageInSession(request);
      EDetail message        = (EDetail) pendingMessageDetails.get(HttpConstant.MESSAGE_IN_SESSION);

      // Check for parameters
      List requestedLinks = buildListOfRequestedLinks(request);

      if (request.getParameter(DELETE_LINK_PARAMETER) != null)
      {
        // Get the link id
        String deleteLinkId = request.getParameter(DELETE_LINK_PARAMETER);
        deleteLink(message, deleteLinkId);

        // Retrieve all the template and message details
        request.setAttribute(REQUEST_LINK_LIST, message.getResourceList());
        super.forwardToTemplate(DELETED_TEMPLATE, request, response);
      }
      else if (request.getParameter(SUBMIT_PARAMETER) != null)
      {
        try
        {
          // Check that we haven't added more than 5 links
          if (requestedLinks.size() > MAX_LINKS_ALLOWED)
            throw new ValidationException(
              StringUtils.globalReplace(SystemMessages.get("mr_tooManyLinks"),
                  "[#MAX_LINK_COUNT]", "" + MAX_LINKS_ALLOWED));
          //throw new ValidationException(
          //  "You supplied more than the maximum allowable links (" +
          //  MAX_LINKS_ALLOWED + "). Please select fewer links.");
          else
          {
            // Load the link and add to the collection
            message.getResourceList().clear();
            message.getResourceList().addAll(loadLinks(requestedLinks, getWLCookie(request)));

            // Retrieve all the template and message details
            request.setAttribute(REQUEST_LINK_LIST, message.getResourceList());
            super.forwardToTemplate(SUBMITTED_TEMPLATE, request, response);
          }
        }
        catch (ValidationException errValid)
        {
          request.setAttribute(REQUEST_ERROR_MESSAGE, errValid.getMessage());
          request.setAttribute(REQUEST_CURRENT_LINKS, requestedLinks);

          // Load the template object collection
          request.setAttribute(REQUEST_LINK_CATEGORIES, loadLinkCategories(sessionMR, getWLCookie(request)));
          super.forwardToTemplate(VALIDATION_ERROR_TEMPLATE, request, response);
        }
      }
      else
      {
        // Pass in the list of link ids
        Collection selectedLinks = message.getResourceList();
        Collection selectedLinkIds = new ArrayList();
        for (Iterator i = selectedLinks.iterator(); i.hasNext(); )
          selectedLinkIds.add(((ResourceLink) i.next()).getResourceLinkId());

        request.setAttribute(REQUEST_CURRENT_LINKS, selectedLinkIds);

        // Load the template object collection
        request.setAttribute(REQUEST_LINK_CATEGORIES, loadLinkCategories(sessionMR, getWLCookie(request)));
        super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
      }
  }

  /**
   * Load all templates from persistent storage
   */
  protected Collection loadLinkCategories(MR sessionMR, String sessionId)
  {
    try
    {
      AssetManagerRemoteIntf linkMgr = (AssetManagerRemoteIntf)
                            new EJBUtil().getManager(HttpConstant.ASSETMANAGER_HOME);
      return linkMgr.getResourceList(sessionMR.getCompany(), sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error using the AssetManager EJB", errRemote);
    }
  }

  /**
   * Iterates through the request object and extracts the link ids requested
   */
  protected List buildListOfRequestedLinks(HttpServletRequest request)
  {
    List returnList = new ArrayList();
    Enumeration paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements())
    {
      String paramName = (String) paramNames.nextElement();
      if (paramName.startsWith(LINK_PREFIX))
        returnList.add(paramName.substring(LINK_PREFIX.length()));
    }
    return returnList;
  }
}

