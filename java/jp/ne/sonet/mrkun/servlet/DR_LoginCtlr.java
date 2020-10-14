
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.io.*;
import java.net.URLDecoder;

import org.jdom.*;
import org.jdom.output.*;

/**
 * The boundary class for the DR1.0 use case. This servlet acts as the
 * controller in the MVC pattern with the dr01.jsp page and the
 * M3SecurityManager class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author M.Mizuki
 * @version $Id: DR_LoginCtlr.java,v 1.1.2.20 2001/11/13 07:56:55 rick Exp $
 */
public class DR_LoginCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  FAILED_LOGIN_TEMPLATE = HttpConstant.Dr12_View;
  final String  UNKNOWN_USER_TEMPLATE = HttpConstant.Dr00_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Dr01_View;

  final String  LOGIN_PARAMETER       = "userid";
  final String  PASSWORD_PARAMETER    = "password";
  final String  UID_PARAMETER         = "uid";

  final String  REQUEST_PAGE_DR       = "pageDR";
  final String  MR_PROFILE_LIST       = "mrProfileList";
  final String  MR_DETAIL_PERSONS     = "mrDetail";
  final String  ERRORMESSAGE          = "drLoginFailedMessage";

  final String  FRONT_LEFT            = "1";
  final String  FRONT_RIGHT           = "2";

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
    String login  = null;
    String drHash = "";
    String sortOrder = "";

    // cookie get
    Cookie[] cookies = request.getCookies();
    Cookie thisCookie = null;

    String loginDrIdName = System.getProperty("mrkun.dridname");
    if( loginDrIdName == null ){
        loginDrIdName = HttpConstant.testDrIdCookieName;
    }

    boolean flagHash = false;
    String modeHash = System.getProperty("mrkun.hash");
    if( modeHash != null ){
        try
        {
            flagHash = new Boolean(modeHash).booleanValue();
        }
        catch (Exception e)
        {
        }
    }

    for(int i=0; i < cookies.length; i++) {
        thisCookie = cookies[i];
        System.out.println("Cookies: " + thisCookie.getName() + "  Value: " + thisCookie.getValue());
        if (login == null && thisCookie.getName().equals(loginDrIdName)) {
            login = thisCookie.getValue();
        }else if (drHash.equals("") && thisCookie.getName().equals("drcd")) {
            drHash = thisCookie.getValue();
	}
    }

    if (login == null)
      throw new DoctorLoginProcessFailedException("No login parameters supplied");
    else if (drHash == null && flagHash)
      throw new LoginFailedException("No login parameters supplied");
    else try
    {
      // Id and Hash Data Decode
      login = URLDecoder.decode( login );
//      drHash = URLDecoder.decode( drHash );

      // Get the M3SecurityManager
      M3SecurityManager security = new M3SecurityManager();	  
      //SessionManager session = SessionManager.getSessionManager(request, false);
      SessionManager session = new SessionManager(request, new Boolean(true));

      String nextPage = SUCCESS_TEMPLATE;

      // Authenticate
      String agent = request.getHeader("User-Agent");
      String userId = security.authenticateDR(login, drHash, agent, getWLCookie(request));
//    System.out.println("DR_LoginCtlr: stored login info for dr=:"+userId+agent); 
      if (userId == null)
        throw new LoginFailedException("User " + login + " not found.");
      else
      {
        DR userDR = security.loginDR(userId, getWLCookie(request));
        if(userDR == null){ // Go Entry Page
          userDR = new DR();
          userDR.setDrId(userId);
          nextPage = UNKNOWN_USER_TEMPLATE;
        }
        else  // Go Front Page
        {
          nextPage = SUCCESS_TEMPLATE;
        }
        session.postLogin(userId);
        session.setUserItem(userDR);
        Map message = (Map) session.getSessionItem(HttpConstant.PENDING_IN_SESSION);
        if (message!= null) message.clear();

        // Quick dump out of the MR object to system stream
        // Note: Testing only - Remove before production.
        //XMLOutputter outWriter = new XMLOutputter("  ", true);
        //PrintWriter outPW = new PrintWriter(System.out);
        //outWriter.output(userDR.toXML("dr"), outPW);
        //outPW.flush();

        // Set the cookie for the load balancer
        Properties p = System.getProperties();
        String hostname = p.getProperty("mrkun.hostname", "localhost");
        Cookie hostCookie = new Cookie("mrkun.hostname", hostname);
        hostCookie.setMaxAge(-1);
        response.addCookie(hostCookie);

        // JSP send data
        request.setAttribute(REQUEST_PAGE_DR, userDR);

        // Get MRManager for the loadup of DRInformation object
	      //MRManagerRemoteIntf mrManager = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
        // Build the mr list in the correct order
        //Map mrList = mrManager.getMRProfileByDRId(userDR.getDrId());
        Map mrList = userDR.getMRProfileMap();
        
        List mrListTop = new ArrayList();

        int detail_num = 0;
        for(Iterator i = mrList.values().iterator(); i.hasNext(); )
        {
          MrProfile mrProfile = (MrProfile) i.next();
          if( mrProfile.getUnreadMessageId() != null ) detail_num++;
          if( mrProfile.getMrBannerPosition().equals( FRONT_LEFT ) )
            mrListTop.add(0,mrProfile);
          else if( mrProfile.getMrBannerPosition().equals( FRONT_RIGHT ) )
            mrListTop.add(mrProfile);
        }
        request.setAttribute(MR_DETAIL_PERSONS, new Integer(detail_num));

        request.setAttribute(MR_PROFILE_LIST, mrListTop);

        // Go to the successful login page
        super.forwardToTemplate(nextPage, request, response);
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
      request.setAttribute(ERRORMESSAGE, errLF.toString());
      super.forwardToTemplate(FAILED_LOGIN_TEMPLATE, request, response);
    }
    catch (ApplicationError errLF)
    {
      // Go to the failed login page
      request.setAttribute(ERRORMESSAGE, errLF.toString());
      super.forwardToTemplate(FAILED_LOGIN_TEMPLATE, request, response);
    }
  }
}


