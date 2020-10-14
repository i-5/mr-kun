
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

import org.jdom.*;
import org.jdom.output.*;

/**
 * The boundary class for the MR1.0 use case. This servlet acts as the
 * controller in the MVC pattern with the mr1.jsp page and the
 * M3SecurityManager class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: LoginCtlr.java,v 1.1.2.15 2001/11/13 07:56:55 rick Exp $
 */
public class LoginCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  FAILED_LOGIN_TEMPLATE = HttpConstant.Mr01_View;
  final String  UNKNOWN_USER_TEMPLATE = HttpConstant.Mr01_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr02_Ctlr;

  final String  LOGIN_PARAMETER       = "userid";
  final String  PASSWORD_PARAMETER    = "password";
  final String  UID_PARAMETER         = "uid";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then handles postLogin
   * processing.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // Accept the submitted fields (login and password)
    String login    = request.getParameter(LOGIN_PARAMETER);
    String password = request.getParameter(PASSWORD_PARAMETER);
    
    if ((login == null) || (password == null))
      super.forwardToTemplate(ERROR_TEMPLATE, request, response);
    else try
    {
      // Get the M3SecurityManager
      M3SecurityManager security = new M3SecurityManager();
      //SessionManager session = SessionManager.getSessionManager(request, false);
      SessionManager session = new SessionManager(request, new Boolean(true));

      // Authenticate
      HttpSession hs = request.getSession(true);
//      String sessionKey = hs.getId();
//      MR userMR = security.authenticateMR(login, password, sessionKey);
      String agent = request.getHeader("User-Agent");
      MR userMR = security.authenticateMR(login, password, agent, getWLCookie(request));
      if (userMR == null)
        throw new UserNotFoundException("User " + login + " not found.");
      else
      {
        //session.setMR(userMR);
        EventLogUser eventLogUser = new EventLogUser(request, userMR);
        eventLogUser.submitEvent();
        session.postLogin(login);
        session.setUserItem(userMR);
        Map message = (Map) session.getSessionItem(HttpConstant.PENDING_IN_SESSION);
        if (message!= null) message.clear();

        // Quick dump out of the MR object to system stream
        // Note: Testing only - Remove before production.
        //XMLOutputter outWriter = new XMLOutputter("  ", true);
        //PrintWriter outPW = new PrintWriter(System.out);
        //outWriter.output(userMR.toXML("mr"), outPW);
        //outPW.flush();

        // Set the cookie for the load balancer
        Properties p = System.getProperties();
        String hostname = p.getProperty("mrkun.hostname", "localhost");
        Cookie hostCookie = new Cookie("mrkun.hostname", hostname);
        hostCookie.setMaxAge(-1);
        response.addCookie(hostCookie);

        // Go to the successful login page
        super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
      }
    }
    catch (UserNotFoundException errUNF)
    {
      // Go to the unknown user page
      super.forwardToTemplate(UNKNOWN_USER_TEMPLATE, request, response);
    }
    catch (LoginFailedException errLF)
    {
      // Go to the failed login page
      super.forwardToTemplate(FAILED_LOGIN_TEMPLATE, request, response);
    }
  }
}

 