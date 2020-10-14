
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
import java.net.URLDecoder;
import java.security.*;

// Note we use the weblogic specific Https URL Connection class here,
// but only because BEA were too anal to let us use anyone elses (permission problems).
import weblogic.net.http.HttpsURLConnection;
//import com.sun.net.ssl.HttpsURLConnection;

/**
 * This servlet checks whether or not the login and password supplied
 * are valid Medipro logins. This is done by simulating a login to
 * MyMedipro invisibly and checking the responses to determine whether
 * authentication was successful.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: DesktopMRkunLoginCtlr.java,v 1.1.2.5 2001/12/24 10:31:52 damon Exp $
 */
public class DesktopMRkunLoginCtlr extends BaseServlet
{
  static String AUTHENTICATION_SUCCESS        = "0";
  static String AUTHENTICATION_FAILED_M3      = "1";
  static String AUTHENTICATION_FAILED_MEDIPRO = "2";
  
  final String LOGIN_FIELD    		= "loginId";
  final String PASSWORD_FIELD 		= "password";
  final String ACTION		  		= "action";
  final String DR_ID		  		= "drId";		

  /**
   * The gateway method - this handles all incoming requests.
   */
  public void doAction (HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    String action = request.getParameter(ACTION);
    
    if (action.equals(DesktopMRkunConstant.VIEW_DR_2)   ||
        action.equals(DesktopMRkunConstant.VIEW_DR_8)   ||
        action.equals(DesktopMRkunConstant.VIEW_DR_8_1) ||
        action.equals(DesktopMRkunConstant.VIEW_DR_11))
    { 
      doAuthenticate(request, response, action);   
    }   
    else if (action.equals(DesktopMRkunConstant.MEDIPRO_LOGIN_CHECK))
    {
      // Get request params
      String loginId  = request.getParameter(LOGIN_FIELD);
      String password = request.getParameter(PASSWORD_FIELD);
    
      try
      {
            
        if ((loginId == null) || (password == null))
          writeMediproFailure(response);
        else
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
          // System.out.println("Response:");
          // System.out.println(respOne);

          // Check response for error page
          if (respOne.indexOf("Now loading MyMedipro") == -1)
          {
            writeMediproFailure(response);
            return;
          }      

          // Attempt phase two - the MRKun login
          DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil()
                    .getManager(HttpConstant.DRMANAGER_HOME);
          DR dr = drm.getDRById(loginId, "DesktopMRKunLoginCheck");
          if (dr != null)
            writeSuccess(response);
          else
            writeM3Failure(response);
        }
      }
      catch (MalformedURLException errMal)
      {
        // Write out failure code
        writeMediproFailure(response);
        errMal.printStackTrace();
        return;
      }   
      catch (RemoteException errRemote)
      {
        // Write out failure code
        errRemote.printStackTrace();
        writeM3Failure(response);
        return;
      } 
      catch (ApplicationError errApp)
      {
        // Write out failure code
        writeMediproFailure(response);
        errApp.printStackTrace();
        return;
      }       
    }
    else if(action.equals("") || action == null)
    {
      throw new ApplicationError("MediproLoginCtlr.service: Unknown HTTP parameter.");
    }
    else
    {
      throw new ApplicationError("MediproLoginCtlr.service: Unknown HTTP parameter.");
    }   
  }
  
  /**
   * Handle dr ID authentication, session creation and JSP forwarding
   */  
  private void doAuthenticate(HttpServletRequest request, 
                              HttpServletResponse response, 
                              String action) throws IOException
  
  {
  	Date timeStampId = new Date();
  	String drId = request.getParameter(DR_ID);
    //drId = URLDecoder.decode(drId);
    
    System.out.println("In doAuthenticate");
    
    //if (drId.equals(hashPassword(drId+DesktopMRkunConstant.HSP, response)))
    //{    
      System.out.println("In doAuthenticate: Passed hashPassword check");        
      
      M3SecurityManager security = new M3SecurityManager();	
      SessionManager session = new SessionManager(request, new Boolean(true));
      drId = security.changeDrId(drId);
      DR dr = security.loginDR(drId, timeStampId.toString());
      if(dr == null)
        throw new ApplicationError("MediproLoginCtlr.service: M3security.loginDR return null dr, drId login session failed.");          
      session.setUserItem(dr);
      Map message = (Map) session.getSessionItem(HttpConstant.PENDING_IN_SESSION);
      if (message!= null) message.clear();
      
      System.out.println("In doAuthenticate: Login ok and session is created."); 
         
      // Set the cookie for the load balancer
      Properties p = System.getProperties();
      String hostname = p.getProperty("mrkun.hostname", "localhost");
      Cookie hostCookie = new Cookie("mrkun.hostname", hostname);
      hostCookie.setMaxAge(-1);
      response.addCookie(hostCookie);
		
      // Go to the successful login page
      if (action.equals(DesktopMRkunConstant.VIEW_DR_2))
      {
      	System.out.println("In doAuthenticate: Forward to DR2 servlet."); 
        super.forwardToTemplate(HttpConstant.Dr02_Ctlr, request, response);
      }
      else if (action.equals(DesktopMRkunConstant.VIEW_DR_8))
      {
      	System.out.println("In doAuthenticate: Forward to DR 8 servlet.");
        super.forwardToTemplate(HttpConstant.Dr08_Ctlr, request, response);       
      }
      else if (action.equals(DesktopMRkunConstant.VIEW_DR_8_1))
      {
      	System.out.println("In doAuthenticate: Forward to DR 8 1 servlet.");
        super.forwardToTemplate(HttpConstant.Dr08_1_Ctlr, request, response);  
      }
      else if (action.equals(DesktopMRkunConstant.VIEW_DR_11))
      {
      	System.out.println("In doAuthenticate: Forward to DR 11 help page.");
        super.forwardToTemplate(HttpConstant.Dr11_Ctlr, request, response);  
      }
  /*  } 
    else
    {
      // Write out failure code
      writeM3Failure(response);
    } 
  */     
  }

  /**
   * Writes out the response for a successful login
   */
  private void writeSuccess(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_SUCCESS);
    out.close();
  }

  /**
   * Writes out the response for a failed login
   */
  private void writeMediproFailure(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_FAILED_MEDIPRO);
    out.close();
  }

  /**
   * Writes out the response for a failed login
   */
  private void writeM3Failure(HttpServletResponse response) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.println(AUTHENTICATION_FAILED_M3);
    out.close();
  }

  /**
   * Create secret hashed value
   */
  private String hashPassword(String loginId, HttpServletResponse response) throws IOException
  {
  	try
    {
      String hashSecret = "DR" + ":" + loginId + ":" + new Date();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(hashSecret.getBytes());

      StringBuffer hashedKey = new StringBuffer(64);
      byte hashedArray[] = md5.digest();
      for (int n = 0; n < hashedArray.length; n++)
        hashedKey.append(hashedArray[n] + "-");  
      return hashedKey.toString();       
    }
    catch (NoSuchAlgorithmException errNSA)
    {
      // Write out failure code
      writeM3Failure(response);
      errNSA.printStackTrace();
      return "";
    }     
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

