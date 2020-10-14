
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.RemoteException;

// Note we use the weblogic specific Https URL Connection class here,
// but only because BEA were too anal to let us use anyone elses (permission problems).
//import weblogic.net.http.HttpsURLConnection;
//import com.sun.net.ssl.HttpsURLConnection;

/**
 * This servlet checks whether or not the login and password supplied
 * are valid Medipro logins. This is done by simulating a login to
 * MyMedipro invisibly and checking the responses to determine whether
 * authentication was successful.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MediproLoginCtlr.java,v 1.1.2.2 2001/12/20 06:05:44 rick Exp $
 */
public class MediproLoginCtlr extends HttpServlet
{
  public static String AUTHENTICATION_SUCCESS        = "0";
  public static String AUTHENTICATION_FAILED_M3      = "1";
  public static String AUTHENTICATION_FAILED_MEDIPRO = "2";

  final String LOGIN_FIELD    = "loginId";
  final String PASSWORD_FIELD = "password";

  /**
   * The gateway method - this handles all incoming requests.
   */
  public void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // Get request params
    String loginId  = request.getParameter(LOGIN_FIELD);
    String password = request.getParameter(PASSWORD_FIELD);

    if ((loginId == null) || (password == null))
      writeMediproFailure(response);
    else
    {
      try
      {
        // Attempt login phase one - the initial request
        //URL urlOne = new URL("https://oto.so-net.ne.jp/cgi-bin/bvcgi");
        URL urlOne = new URL("http://www.so-net.ne.jp/medipro/cookie/" +
            "mm/mmlogin_withmrcookie.cgi?loginhtml=autostart_redirect.html&" +
            "submit%25form=&username=" + loginId + "&password=" + password);
        //URL urlOne = new URL("https://mr.m3.so-net.ne.jp/");
        HttpURLConnection reqOne = (HttpURLConnection) urlOne.openConnection();
        reqOne.setFollowRedirects(true);
        reqOne.setRequestMethod("GET");
        reqOne.connect();

        // Process the response
        List cookies = new Vector();
        String respOne = getResponse(reqOne, cookies);
        //System.out.println("Response:");
        //System.out.println(respOne);

        // Check response for error page
        if (respOne.indexOf("Now loading MyMedipro") == -1)
        {
          writeMediproFailure(response);
          return;
        }
      }
      catch (ApplicationError errApp)
      {
        // Write out failure code
        writeMediproFailure(response);
        errApp.printStackTrace();
        return;
      }

      // Attempt phase two - the MRKun login
      try
      {
        DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil()
                      .getManager(HttpConstant.DRMANAGER_HOME);
        DR dr = drm.getDRById(loginId, "DesktopMRKunLoginCheck");
        if (dr != null)
          writeSuccess(response);
        else
          writeM3Failure(response);
      }
      catch (RemoteException errRemote)
      {
        // Write out failure code
        errRemote.printStackTrace();
        writeM3Failure(response);
        return;
      }
    }
  }

  /**
   * Writes out the response for a successful login
   */
  private void writeSuccess(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_SUCCESS);
    //out.println("Logged in successfully");
    out.close();
  }

  /**
   * Writes out the response for a failed login
   */
  private void writeMediproFailure(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_FAILED_MEDIPRO);
    //out.println("Login failed - Medipro");
    out.close();
  }

  /**
   * Writes out the response for a failed login
   */
  private void writeM3Failure(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_FAILED_M3);
    //out.println("Login failed - M3");
    out.close();
  }

  /**
   * Takes the request and retrieves the response as a string an the
   * cookies in the header.
   */
  private String getResponse(HttpURLConnection req, List cookies)
  {
    try
    {
      //System.out.println("Status code = " + req.getResponseCode());
      // Check for cookies
      int headerIndex = 1;
      String headerKey = req.getHeaderFieldKey(headerIndex);
      while (headerKey != null)
      {
        //System.out.println("Header: " + headerKey + "=" + req.getHeaderField(headerIndex));
        if (headerKey.toLowerCase().indexOf("cookie") != -1)
          cookies.add(req.getHeaderField(headerIndex));
        headerKey = req.getHeaderFieldKey(++headerIndex);
      }

      // Read the response using a reader
      InputStream inResp = (InputStream) req.getInputStream();
      //System.out.println("Available chars = " + inResp.available());
      Reader inReader = null;
      if (req.getContentEncoding() != null)
        inReader = new InputStreamReader(inResp, req.getContentEncoding());
      else
        inReader = new InputStreamReader(inResp);
      StringBuffer response = new StringBuffer();
      int result = inReader.read();
      while (result > 0)
      {
        response.append((char) result);
        result = inReader.read();
      }
      inReader.close();
      inResp.close();
      return response.toString();
    }
    catch (UnsupportedEncodingException errUSE)
    {
      throw new ApplicationError("Error processing the response", errUSE);
    }
    catch (IOException errIO)
    {
      throw new ApplicationError("Error processing the response", errIO);
    }
  }
}

