
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.dao.*;
import java.util.*;
import java.sql.Date;
import java.text.*;

/**
 * The implementation class for the ReportManager bean.
 * This bean is stateless in nature, and uses DAO support classes to
 * write persistent information to the database.
 *
 * @author Damon Lok
 * @version $Id: ReportManager.java,v 1.1.2.12 2001/11/13 07:56:56 rick Exp $
 */
public class ReportManager extends BaseSessionEJB
{
  /**
   * Get the Ranking object by MR ID from persistent storage
   */
  public Ranking getUserRanking(String mrId, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade reportDAO = new DAOFacade(Report.class);
    try
    {
      Map searchCriteria = new Hashtable();
      Calendar cdr = Calendar.getInstance();
      java.util.Date currentMonth = new java.util.Date();
      java.util.Date previousMonth = cdr.getTime();
      java.sql.Date sqlCurrentMonth = new Date(currentMonth.getTime());
      java.sql.Date sqlPreviousMonth = new Date(previousMonth.getTime()); 
      searchCriteria.put("mrId", mrId);
      searchCriteria.put("cur_month", sqlCurrentMonth);
      searchCriteria.put("pre_month", sqlPreviousMonth);
      return (Ranking) reportDAO.searchRecord(searchCriteria, "ranking", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Get the usage statistics object by MR ID from persistent storage
   */
  public Map getUsagePoint(String mrId, String sessionId)
  {
  	// Get the objects that need some checking first 
  	Map searchCriteria = new Hashtable();
    Map criteria = new Hashtable();
    Map mrUsagePoint = new Hashtable();
    Map selfUsagePoint = new Hashtable();
    Map companyAverage = new Hashtable();
    Map drMessagePoint = new Hashtable();
    Map ranking = new Hashtable();
    java.util.Date currentMonth = new java.util.Date();
    Calendar cdr = Calendar.getInstance();
    cdr.add(Calendar.MONTH, -1);
    java.util.Date previousMonth = cdr.getTime();
    java.sql.Date sqlCurrentMonth = new Date(currentMonth.getTime());
    java.sql.Date sqlPreviousMonth = new Date(previousMonth.getTime()); 

    searchCriteria.put("mrId", mrId);
    searchCriteria.put("c_month", sqlCurrentMonth);
    searchCriteria.put("p_month", sqlPreviousMonth);
        
    // Get a DAO and perform the save query
    DAOFacade reportDAO = new DAOFacade(Report.class);
    try
    {
      selfUsagePoint = (Map) reportDAO.searchMultiple(searchCriteria, sessionEJBConstant.MR_USAGE_SELF_COUNT, sessionId);
      companyAverage = (Map) reportDAO.searchMultiple(searchCriteria, sessionEJBConstant.MR_USAGE_COMPANY_COUNT, sessionId);
      ranking = (Map) reportDAO.searchMultiple(searchCriteria, sessionEJBConstant.MR_USAGE_RANKING_COUNT, sessionId);
      drMessagePoint = (Map) reportDAO.searchMultiple(searchCriteria, sessionEJBConstant.MR_USAGE_DR_PROFILE_COUNT, sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
    
    mrUsagePoint.put("self_data", selfUsagePoint);
    mrUsagePoint.put("company_avg_data", companyAverage);
    mrUsagePoint.put("ranking", ranking);
    mrUsagePoint.put("dr_point", drMessagePoint);
    
    return mrUsagePoint;
  }

  /**
   * Get the single usage statistics object by MR ID and DR ID from persistent storage
   */
  public Map getSingleUsagePoint(String mrId, String drId, String sessionId)
  {
    if ((mrId == null ) || (drId == null))
	{
	  // Parameter (mrId/drId) passed in null
      throw new ApplicationError(this.getClass().getName()
        + ": Parameter (mrId/drId) passed in null. Mrid: " + mrId + "DrId: " +drId);
	}
   
  	// Create date and essential data container 
  	Map searchCriteria = new Hashtable();
    Map singleUsagePoint = new Hashtable();

    java.util.Date currentMonth = new java.util.Date();
    Calendar cdr = Calendar.getInstance();
    cdr.add(Calendar.MONTH, -1);
    java.util.Date previousMonth = cdr.getTime();
    java.sql.Date sqlCurrentMonth = new Date(currentMonth.getTime());
    java.sql.Date sqlPreviousMonth = new Date(previousMonth.getTime()); 
    searchCriteria.put("mrId", mrId);
    searchCriteria.put("drId", drId);
    searchCriteria.put("c_month", sqlCurrentMonth);
    searchCriteria.put("p_month", sqlPreviousMonth);
        
    // Get a DAO and perform the save query
    DAOFacade reportDAO = new DAOFacade(Report.class);
    try
    {
      singleUsagePoint = (Map) reportDAO.searchMultiple(searchCriteria, sessionEJBConstant.MR_SINGLE_USAGE_COUNT, sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
        
    return singleUsagePoint;
  }
  
}

