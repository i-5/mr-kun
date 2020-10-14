package jp.ne.sonet.mrkun.servlet;

import java.io.*;
import java.lang.*;
import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jp.ne.sonet.mrkun.framework.servlet.BaseServlet;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * The servlet that handles incoming HTTP requests from the MR6 page.
 * This will populate MR's usage statistic data to the front-end.
 *
 * @author Damon Lok
 * @version $Id: MR_StatisticsCtlr.java,v 1.1.2.12 2001/11/13 07:56:55 rick Exp $
 */
public class MR_StatisticsCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Mr09_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Mr06_View;

  final String  REQUEST_PAGE_MR       = "pageMR";
  final String  SESSION_ERROR_MESSAGE = "sessionErrorMessage";

  /**
   * This method handles any kind of request that comes to the
   * servlet. It determines whether the request has modifications,
   * and if so validates and applies them. It then returns the
   * updated values to a JSP page which displays the MR's usage statistic data.
   * Any error handling is done in the base class service method.
   *
   * @param request The parameters in the HTTP request.
   * @param response The response to be sent back to the browser.
   */
  public void doAction(HttpServletRequest request, HttpServletResponse response)
  {
    MR sessionMR = null;
    
    // Check for an MR in session
      sessionMR = (MR) checkSession(request);
    
      request.setAttribute(REQUEST_PAGE_MR, sessionMR);

      // Obtain MR's usage statistic data
      request.setAttribute("mrUsagePoint", getMRUsageData(sessionMR.getMrId(), getWLCookie(request)));

      // Send to template
      super.forwardToTemplate(SUCCESS_TEMPLATE, request, response);
  }
  
  /**
   * getMRUsageData will return the hashtable that holds MR's usage statistic as
   * well as company average and ranking.
   * @param mrId The ranking that belongs to this MR.
   * @return Map The hashtable that holds all the data mentioned above.
   */ 
  private Map getMRUsageData(String mrId, String sessionId)
  {	  			
	Map mrUsageData = new Hashtable();
    
  	try
    {
  	  // Get MRManager for the loadup of DRInformation object
	    ReportManagerRemoteIntf reportManager = (ReportManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.REPORTMANAGER_HOME);

  	  // Build a list of all drInformation object
      mrUsageData = reportManager.getUsagePoint(mrId, sessionId);
      
      if ( mrUsageData == null )
	  {
	    // Throw ApplicationError if null is returned
        throw new ApplicationError(this.getClass().getName()
           			+ ": The hashtable (MR usage statistic data) is returned nul in getMRUsageData.");
	  }
    }
    catch (RemoteException e)
    {
	    // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on building the usage data in getMRUsageData.");
    }
    return mrUsageData;
  }

}
