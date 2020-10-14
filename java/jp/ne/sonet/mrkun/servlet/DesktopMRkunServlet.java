
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.rmi.RemoteException;
import jp.ne.sonet.medipro.mr.*;
import java.sql.*;
import java.util.*;

/**
 * Class name:  DesktopMRkunServlet
 *  
 * This class defines the 2-tier architecture server component for the 
 * Desktop MRkun client component.  It will extract the action parameter 
 * and query the database accordingly.  Then have the query result response 
 * to the client.
 *
 * @author Damon Lok
 * @version $Id: DesktopMRkunServlet.java,v 1.1.2.4 2001/12/24 06:25:22 damon Exp $ 
 */  
public class DesktopMRkunServlet extends HttpServlet
{
  final String USER_MODE      = "userMode";
  final String MESSAGE_CHECK  = "messageCheck";
  final String ACTION		  = "action";
  final String VERSION_CHECK  = "versionCheck";
  final String HTML_HEADER    = "<html><head><meta http-equiv='Content-Type' " + 
                                "content='text/html; charset=Shift_JIS'></head></html>";
  
  /**
   * Initialize servlet data from servlet engine configuration
   * @param pConfig Servlet engine configuration information
   * @exception ServletException
   */
  public void init (ServletConfig pConfig) throws ServletException
  {
  	System.out.println("DMServer: Client request is received.\n");
    super.init (pConfig);
  }
  
  /**
   * This will call doAction().
   * Catches any exceptions not caught in the subclass.
   *
   * @param pReq              HttpServletRequest
   * @param pRes              HttpServletResponse
   * @exception               java.io.IOException
   *                          if there is an IO error
   */
  public void service(HttpServletRequest pReq, HttpServletResponse pRes)
  {
    this.doAction(pReq, pRes);
  }
  
  /**
   * <P>This method handles the request that comes to the servlet
   * and will response to the response accordingly.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest pReq, HttpServletResponse pRes)
  {
  	String drId = "";
    
    String request = pReq.getParameter(ACTION);
    System.out.println("DMServer: Client request is " + request + ".\n");
    
    if (request.equals(MESSAGE_CHECK))
    {          				         
      // Determine user mode
      String userMode = pReq.getParameter(USER_MODE);         
      System.out.println("DMServer: Desktop MRkun client user mode: " + userMode); 
          
      if (userMode.equals(DesktopMRkunConstant.Single))
      {
        drId = handleSingleUserMode(pReq, pRes);    
      }  
      else if (userMode.equals(DesktopMRkunConstant.Multiple))
      {
      	handleMultiUserMode(pReq, pRes);
        drId = "Multiple Doctors"; 
      }
      else
      {
        System.out.println(DesktopMRkunConstant.Communication_Error);
        throw new ApplicationError("In DesktopMRkunServlet.doAction: Invalid UserMode parameter."); 	     
      } 
           
      System.out.println("DMServer: Client session: " + drId + " is closed.\n");   				
    }
    else if (request.equals(VERSION_CHECK))
    {
      versionCheck(pRes);
    } 
    else
    {
      System.out.println("DMServer: Invalid action parameter. Reject client service request.\n");
      throw new ApplicationError("In DesktopMRkunServlet.doAction: Invalid action parameter. Reject client service request.");
    }  
  } 

  /**
   * <P>This method will interface with MessageChecker for version check. 
   */  
  private void versionCheck(HttpServletResponse pRes)
  { 
    System.out.println("DMServer: In versionCheck method.\n");        
    MessageChecker msgChecker = new MessageChecker();
    String version = msgChecker.queryVersion();    
    System.out.println("DMServer: Verion is " + version + ".\n");
    
    try
    {
      PrintWriter output = pRes.getWriter();
      output.println(version);
      output.close();
      System.out.println("DMServer: Version check session is closed.\n");
    } 
    catch (IOException e) 
    {
      System.err.println("IOException while using socket streams: " + e);
      throw new ApplicationError("In DesktopMRkunServlet.handleSingleUserMode: Network stream output error."); 	       
    }
  }

  /**
   * <P>This method handles the request from a multiple doctors 
   * Desktop MRkun client.  It interfaces with MessageChecker object for
   * message check service request.  Upon receiving results, it prints 
   * them out to the network stream as the response to the client.    
   */  
  private void handleMultiUserMode(HttpServletRequest pReq, HttpServletResponse pRes)
  {
    
    // Doctor count
    int count = 0;
    
    // Client's doctor ID
    String drId = "";
        
    // Doctor count request parameter 
    String countParameter = "";
 
    // MR count map
    ArrayList mrCountList = null;
 
    // The print writer for the response string
    PrintWriter output = null;
 
    // The doctors who share the Desktop MRkun
    ArrayList drList = new ArrayList();    
    
    // Message Checker class
    MessageChecker msgChecker = new MessageChecker();
    msgChecker.setUserMode(DesktopMRkunConstant.Multiple);
    
    try
    {
      // Get the doctor count from the Desktop MRkun client
      countParameter = pReq.getParameter("count");
      count = Integer.valueOf(countParameter).intValue(); 
      System.out.println("DMServer: Doctor count is " + count + ".\n");
      
      // For each doctor get the his/her id from the request header
      for (int index = 0; index < count; index++)
      {
      	drId = pReq.getParameter("ID" + index);
        drList.add(drId);
        System.out.println("DMServer: ID" + index + " is " + drId + ".\n");            
      }
      
      // Get the mr count for each doctor
      mrCountList = (ArrayList) msgChecker.checkMessage(drList);
      output = pRes.getWriter();
   
      // For each doctor write the result to the network stream
      for (int index = 0; index < count; index++)
      {
      	Integer mrCount = (Integer) mrCountList.get(index);
        output.println("ID" + index);
        output.println(mrCount.intValue()); 
        System.out.println("DMServer: ID" + index + "'s mr count is " + mrCount + ".\n");            
      }
      output.close();
    }
    catch(NumberFormatException nfErr)
    {
      System.out.println(DesktopMRkunConstant.Communication_Error);
      throw new ApplicationError("In DesktopMRkunServlet.handleMultiUserMode: Invalid format of Count parameter."); 	      
    }  
    catch (IOException e) 
    {
      System.err.println("IOException while using socket streams: " + e); 
      throw new ApplicationError("In DesktopMRkunServlet.handleMultiUserMode: Network stream output error."); 	      
    }  
  }

  /**
   * <P>This method handles the request from a single doctors 
   * Desktop MRkun client.  It interfaces with MessageChecker object for
   * message check service request.  Upon receiving results, it prints 
   * them out to the network stream as the response to the client.    
   */  
  private String handleSingleUserMode(HttpServletRequest pReq, HttpServletResponse pRes)
  {
  
    // Client's doctor ID
    String drId = "";
  
    // Doctor's MR count
    int mrCount = 0;    
 
    // The MR who have new eDetail sent to the doctor
    Collection mrList = null;
    
    // The print writer for the response string
    PrintWriter output = null;  

    // This will carry the drId
    ArrayList drList = new ArrayList(); 

    // Message Checker class
    MessageChecker msgChecker = new MessageChecker();         
    msgChecker.setUserMode(DesktopMRkunConstant.Single);
               
    // Get drId from the client
    drId = pReq.getParameter("drId");
    drList.add(drId);           
    System.out.println("DMServer: Desktop MRkun client drId: " + drId);                   
    mrList = msgChecker.checkMessage(drList);
    mrCount = mrList.size();
          
    try
    { 
      // Send mr count to the client
      output = pRes.getWriter();
      // output.println(HTML_HEADER);
      output.println(mrCount);
      System.out.println("DMServer: Sent mrCount to Desktop MRkun client: " + mrCount);
      
      if (mrCount == 1)
      {
        Iterator itr = mrList.iterator();
        Map mrMap = (Map) itr.next();
        String mrName = (String) mrMap.get("mrName");
        output.println(mrName);
        System.out.println("DMServer: Sent mr name = " + mrName);
        String mrCompany = (String) mrMap.get("mrCompany");               
        output.println(mrCompany);
        System.out.println("DMServer: Sent mr company = " + mrCompany);
      }            
    } 
    catch (IOException e) 
    {
      System.err.println("IOException while using socket streams: " + e);
      throw new ApplicationError("In DesktopMRkunServlet.handleSingleUserMode: Network stream output error."); 	       
    }
    output.close();
    return drId;
  }    
}