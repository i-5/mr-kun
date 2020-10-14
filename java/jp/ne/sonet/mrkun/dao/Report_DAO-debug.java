
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.Map;
import java.util.Hashtable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for all Report data.
 *
 * @author Damon Lok
 * @version $Id: Report_DAO.java,v 1.1.2.19 2001/08/20 10:00:17 damon Exp $
 */
public class Report_DAO implements DAO_SQL
{ 
  //If needed to use ADD, SAVE, and REMOVE, uncomment the respective one:  
  //final String  ADD_QUERY             		         = "java:comp/env/sql/ReportInsertSQL";
    //final String  SAVE_QUERY                           = "java:comp/env/sql/ReportUpdateSQL";
  //final String  REMOVE_QUERY                         = "java:comp/env/sql/ReportDeleteSQL";
  final String  RANKING_READ_EDETAIL_COUNT_QUERY          = "java:comp/env/sql/ReportRankingReadEDetailSP";
  final String  RANKING_TOTAL_RANKING_COUNT_QUERY         = "java:comp/env/sql/ReportRankingTotalCountSP";
  final String  USAGE_POINT_SEARCH_SINGLE_USAGE_PRE_MONTH = "java:comp/env/sql/ReportSingleUsagePointPreviousMonth";
  final String  USAGE_POINT_SEARCH_BY_MRID_QUERY          = "java:comp/env/sql/ReportUsagePointSearchIDSQL";
  final String  USAGE_POINT_SEARCH_PER_DR_USAGE_PRE_MONTH = "java:comp/env/sql/ReportUsagePointPerDRPreMonth";
  final String  USAGE_POINT_SEARCH_PER_DR_USAGE_CUR_MONTH = "java:comp/env/sql/ReportUsagePointPerDRCurMonth";
  final String  USAGE_POINT_SEARCH_SINGLE_DR_USAGE        = "java:comp/env/sql/ReportSingleUsagePoint";
  final String  USAGE_POINT_SP_SELF_EDETAIL_COUNT         = "java:comp/env/sql/ReportSelfEdetailCountSP";
  final String  USAGE_POINT_SP_SELF_CONTACT_COUNT         = "java:comp/env/sql/ReportSelfContactCountSP";
  final String  USAGE_POINT_SP_SELF_ACTIVE_COUNT          = "java:comp/env/sql/ReportSelfActiveCountSP";  
  final String  USAGE_POINT_SP_COMPANY_EDETAIL_COUNT      = "java:comp/env/sql/ReportCompEdetailCountSP";
  final String  USAGE_POINT_SP_COMPANY_CONTACT_COUNT      = "java:comp/env/sql/ReportCompContactCountSP";
  final String  USAGE_POINT_SP_COMPANY_ACTIVE_COUNT       = "java:comp/env/sql/ReportCompActiveCountSP";
  final String  USAGE_POINT_SP_READ_EDETAIL_RANKING       = "java:comp/env/sql/ReportRankingReadEdetailSP";
  final String  USAGE_POINT_SP_CONTACT_RANKING            = "java:comp/env/sql/ReportRankingContactSP";
  final String  USAGE_POINT_SP_ACTIVE_RANKING             = "java:comp/env/sql/ReportRankingActiveSP";
  private final int SELF_EDETAIL_COUNT                    = 101;
  private final int COMP_EDETAIL_COUNT                    = 102;
  private final int SELF_CONTACT_COUNT                    = 103;
  private final int COMP_CONTACT_COUNT                    = 104;
  private final int SELF_ACTIVE_COUNT                     = 105;
  private final int COMP_ACTIVE_COUNT                     = 106;
  private final int READ_EDETAIL_RANKING                  = 107;
  private final int CONTACT_RANKING                       = 108;
  private final int ACTIVE_RANKING                        = 109;
  
  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param searchQuery The CallableStatement with the retrieved object in it.
   * @param usagePoint The Map that will contain the return data - passed by reference.
   * @param queryFor The flag indicator
   */
  private void populateUsageStatisticsObject(CallableStatement searchQuery, Map usagePoint, int queryFor)
    throws SQLException
  {
    // Check the state of the resultset
    if (searchQuery == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (usagePoint == null)
      throw new RuntimeException("Need to pass in a hash map that contains current and previous UsageStatistic object.");
    
    UsageStatistics curMonthCount = (UsageStatistics) usagePoint.get("cur_month");
    UsageStatistics preMonthCount = (UsageStatistics) usagePoint.get("pre_month");
        
    System.out.println("Display report data: \n");
    
    switch (queryFor)
    {
      case SELF_EDETAIL_COUNT:
        Double preSentEDetail = new Double(Math.floor(searchQuery.getFloat(5))+0.5);
        Double curSentEDetail = new Double(Math.floor(searchQuery.getFloat(6))+0.5);
        Double preReadEDetail = new Double(Math.floor(searchQuery.getFloat(7))+0.5);
        Double curReadEDetail = new Double(Math.floor(searchQuery.getFloat(8))+0.5);
        Double prePercentage = new Double(Math.floor(searchQuery.getFloat(9))+0.5);
        Double curPercentage = new Double(Math.floor(searchQuery.getFloat(10))+0.5);
        preMonthCount.setSentEDetail(new Integer(preSentEDetail.intValue())); 
        curMonthCount.setSentEDetail(new Integer(curSentEDetail.intValue()));
        preMonthCount.setReadEDetail(new Integer(preReadEDetail.intValue()));
        curMonthCount.setReadEDetail(new Integer(curReadEDetail.intValue()));
        preMonthCount.setPercentage(new Integer(prePercentage.intValue())); 
        curMonthCount.setPercentage(new Integer(curPercentage.intValue()));
        
        System.out.println("Self eDetail count (previous month): " + preMonthCount.getSentEDetail());
    	System.out.println("Self eDetail count (current month): " + curMonthCount.getSentEDetail());
    	System.out.println("Self read eDetail count (previous month): " + preMonthCount.getReadEDetail());
    	System.out.println("Self read eDetail count (current month): " + curMonthCount.getReadEDetail());
        System.out.println("Self eDetail percentage (previous month): " + preMonthCount.getPercentage());
    	System.out.println("Self eDetail percentage (current month): " + curMonthCount.getPercentage());
        break;            
      case COMP_EDETAIL_COUNT:        
        Double comPreSentEDetail = new Double(Math.floor(searchQuery.getFloat(6))+0.5);
        Double comCurSentEDetail = new Double(Math.floor(searchQuery.getFloat(7))+0.5);
        Double comPreReadEDetail = new Double(Math.floor(searchQuery.getFloat(8))+0.5);
        Double comCurReadEDetail = new Double(Math.floor(searchQuery.getFloat(9))+0.5);
        Double comPrePercentage = new Double(Math.floor(searchQuery.getFloat(10))+0.5);
        Double comCurPercentage = new Double(Math.floor(searchQuery.getFloat(11))+0.5);
        preMonthCount.setSentEDetail(new Integer(comPreSentEDetail.intValue()));
        curMonthCount.setSentEDetail(new Integer(comCurSentEDetail.intValue()));
        preMonthCount.setReadEDetail(new Integer(comPreReadEDetail.intValue()));
        curMonthCount.setReadEDetail(new Integer(comCurReadEDetail.intValue()));
        preMonthCount.setPercentage(new Integer(comPrePercentage.intValue())); 
        curMonthCount.setPercentage(new Integer(comCurPercentage.intValue())); 
        
        System.out.println("Company avg eDetail count (previous month): " + preMonthCount.getSentEDetail());
    	System.out.println("Company avg eDetail count (current month): " + curMonthCount.getSentEDetail());
    	System.out.println("Company avg read eDetail count (previous month): " + preMonthCount.getReadEDetail());
    	System.out.println("Company avg read eDetail count (current month): " + curMonthCount.getReadEDetail());
        System.out.println("Company avg eDetail percentage (previous month): " + preMonthCount.getPercentage());
    	System.out.println("Company avg eDetail percentage (current month): " + curMonthCount.getPercentage());
        break; 
      case SELF_CONTACT_COUNT:
        preMonthCount.setReceivedContact(new Integer(searchQuery.getInt(5)));
        curMonthCount.setReceivedContact(new Integer(searchQuery.getInt(6)));
        
        System.out.println("Self received contact count (previous month): " + preMonthCount.getReceivedContact());
    	System.out.println("Self received contact count (current month): " + curMonthCount.getReceivedContact());
        break;   
      case COMP_CONTACT_COUNT:
        preMonthCount.setReceivedContact(new Integer(searchQuery.getInt(6)));
        curMonthCount.setReceivedContact(new Integer(searchQuery.getInt(7)));
        
        System.out.println("Company avg received contact count (previous month): " + preMonthCount.getReceivedContact());
    	System.out.println("Company avg received contact count (current month): " + curMonthCount.getReceivedContact());
        break;
      case SELF_ACTIVE_COUNT:
        preMonthCount.setRegisteredDR(new Integer(searchQuery.getInt(5))); 
        curMonthCount.setRegisteredDR(new Integer(searchQuery.getInt(6)));
        preMonthCount.setActiveCount(new Integer(searchQuery.getInt(7)));
        curMonthCount.setActiveCount(new Integer(searchQuery.getInt(8))); 
        
        System.out.println("Self registered DR count (previous month): " + preMonthCount.getRegisteredDR());
    	System.out.println("Self registered DR count (current month): " + curMonthCount.getRegisteredDR());
    	System.out.println("Self active count (previous month): " + preMonthCount.getActiveCount()); 
   	 	System.out.println("Self active count (current month): " + curMonthCount.getActiveCount()); 
        break;
      case COMP_ACTIVE_COUNT:
        preMonthCount.setRegisteredDR(new Integer(searchQuery.getInt(6)));
        curMonthCount.setRegisteredDR(new Integer(searchQuery.getInt(7)));
        preMonthCount.setActiveCount(new Integer(searchQuery.getInt(8))); 
        curMonthCount.setActiveCount(new Integer(searchQuery.getInt(9))); 
                
    	System.out.println("Company avg registered DR count (previous month): " + preMonthCount.getRegisteredDR());
    	System.out.println("Company avg registered DR count (current month): " + curMonthCount.getRegisteredDR());
    	System.out.println("Company avg active count (previous month): " + preMonthCount.getActiveCount()); 
   	 	System.out.println("Company avg active count (current month): " + curMonthCount.getActiveCount()); 
        break;
      case READ_EDETAIL_RANKING:
        Ranking curMonthRERank = (Ranking) usagePoint.get("read_edetail_cur_month");
        Ranking preMonthRERank = (Ranking) usagePoint.get("read_edetail_pre_month");
        preMonthRERank.setRanking(new Integer(searchQuery.getInt(4)));
        curMonthRERank.setRanking(new Integer(searchQuery.getInt(5))); 
        preMonthRERank.setTotalInRanking(new Integer(searchQuery.getInt(6)));
        curMonthRERank.setTotalInRanking(new Integer(searchQuery.getInt(7)));
        
        System.out.println("Read eDetail ranking count (previous month): " + preMonthRERank.getRanking());
    	System.out.println("Read eDetail ranking count (current month): " + curMonthRERank.getRanking());
    	System.out.println("Read eDetail total number in ranking (previous month): " + preMonthRERank.getTotalInRanking());
    	System.out.println("Read eDetail total number in ranking (current month): " + curMonthRERank.getTotalInRanking());
   
        break;
      case CONTACT_RANKING:
        Ranking curMonthCRank = (Ranking) usagePoint.get("contact_cur_month");
        Ranking preMonthCRank = (Ranking) usagePoint.get("contact_pre_month");
        preMonthCRank.setRanking(new Integer(searchQuery.getInt(4)));
        curMonthCRank.setRanking(new Integer(searchQuery.getInt(5))); 
        preMonthCRank.setTotalInRanking(new Integer(searchQuery.getInt(6)));
        curMonthCRank.setTotalInRanking(new Integer(searchQuery.getInt(7)));
        
        System.out.println("Contact ranking count (previous month): " + preMonthCRank.getRanking());
    	System.out.println("Contact ranking count (current month): " + curMonthCRank.getRanking());
    	System.out.println("Contact total number in ranking (previous month): " + preMonthCRank.getTotalInRanking());
    	System.out.println("Contact total number in ranking (current month): " + curMonthCRank.getTotalInRanking());
        break;
      case ACTIVE_RANKING:
        Ranking curMonthARank = (Ranking) usagePoint.get("active_cur_month");
        Ranking preMonthARank = (Ranking) usagePoint.get("active_pre_month");
        preMonthARank.setRanking(new Integer(searchQuery.getInt(4)));
        curMonthARank.setRanking(new Integer(searchQuery.getInt(5))); 
        preMonthARank.setTotalInRanking(new Integer(searchQuery.getInt(6)));
        curMonthARank.setTotalInRanking(new Integer(searchQuery.getInt(7)));
        
        System.out.println("Acitve ranking count (previous month): " + preMonthARank.getRanking());
    	System.out.println("Acitve ranking count (current month): " + curMonthARank.getRanking());
    	System.out.println("Acitve total number in ranking (previous month): " + preMonthARank.getTotalInRanking());
    	System.out.println("Acitve total number in ranking (current month): " + curMonthARank.getTotalInRanking());
        break; 
    }
  }
  
  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param searchQuery The CallableStatement with the retrieved object in it.
   * @param ranking The Ranking object that will contain the return data.
   * @param queryFor The flag indicator.
   * @return Ranking object that has the result data.
   */
  private Ranking populateTotalCountRankingObject(CallableStatement searchQuery, Ranking ranking) 
    throws SQLException
  {
    // Check the state of the resultset
    if (searchQuery == null)
      throw new NoObjectFoundException("Result was null");
    else
    {
	  ranking.setRanking(new Integer(searchQuery.getInt(5)));
	  ranking.setTotalInRanking(new Integer(searchQuery.getInt(7)));
      return ranking;
    }
  }
  
  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param result The resultset with the retrieved object in it.
   * @param reportDate The month and year this report generated.
   * @param mrId The MR this report belong to.
   * @param drId The DR this report is related to. 
   * @return UsageStatistics
   */
  private UsageStatistics populateSingleUsagePoint(ResultSet result, Date reportDate, String mrId, String drId)
    throws SQLException
  {
  	ArrayList colPerDRUsagePoint = null;
    UsageStatistics singleUsagePoint = null;
    
    // Check the state of the resultset
    if (result == null)
      throw new NoObjectFoundException("Resultset was null");
    else if(!result.next())   
      throw new NoObjectFoundException("Resultset has no data in it.");
    else
    {  
      singleUsagePoint = new UsageStatistics();
          
	  singleUsagePoint.setMrId(mrId);
      singleUsagePoint.setType(new Integer(UsageStatistics.SINGLE_DR_DATA));
      singleUsagePoint.setStatisticMonth(reportDate);
      singleUsagePoint.setDrId(drId);
      singleUsagePoint.setDrName(result.getString("NAME"));
      singleUsagePoint.setHospitalName(result.getString("KINMUSAKI_NAME"));
      singleUsagePoint.setSpecialty(result.getString("SHINRYOKA_NAME"));
      singleUsagePoint.setStatus(new Integer(result.getInt("ACTIVE")));
      singleUsagePoint.setSentEDetail(new Integer(result.getInt("SendEdetail")));
      singleUsagePoint.setReadEDetail(new Integer(result.getInt("kidokuEdetail")));
      singleUsagePoint.setReceivedContact(new Integer(result.getInt("Renraku")));        
                
      return singleUsagePoint;   
    }
  } 
  
  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Ranking populateReadEDetailRankingObject(CallableStatement searchQuery, Ranking ranking) 
    throws SQLException
  {
    // Check the state of the resultset
    if (searchQuery == null)
      throw new NoObjectFoundException("Result was null");
    else
      ranking.setTotalReadEDetail(new Integer(searchQuery.getInt(8)));
    return ranking;
  } 
  
  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param result The resultset with the retrieved object in it.
   * @return Collection
   */
  private Collection populatePerDRUsagePoint(ResultSet result, Date reportDate, String mrId) 
    throws SQLException
  {
  	ArrayList colPerDRUsagePoint = null;
    
    // Check the state of the resultset
    if (result == null)
      throw new NoObjectFoundException("Resultset was null");
    else
    {
      colPerDRUsagePoint = new ArrayList();
   
  	  // Loop through the resultset and loadup the hash data structure
	  while (result.next())
	  {
        UsageStatistics perDRUsagePoint = new UsageStatistics();
        perDRUsagePoint.setMrId(mrId);
        perDRUsagePoint.setType(new Integer(UsageStatistics.PER_DR_USAGE));
        perDRUsagePoint.setStatisticMonth(reportDate);
        perDRUsagePoint.setDrId(result.getString("DR_ID"));
        perDRUsagePoint.setDrName(result.getString("NAME"));
        perDRUsagePoint.setHospitalName(result.getString("KINMUSAKI_NAME"));
        perDRUsagePoint.setSpecialty(result.getString("SHINRYOKA_NAME"));
        perDRUsagePoint.setStatus(new Integer(result.getInt("ACTIVE")));
        perDRUsagePoint.setSentEDetail(new Integer(result.getInt("SendEdetail")));
        perDRUsagePoint.setReadEDetail(new Integer(result.getInt("KidokuEdetail")));
        perDRUsagePoint.setReceivedContact(new Integer(result.getInt("Renraku")));        
        colPerDRUsagePoint.add(perDRUsagePoint);
      }
	System.out.println("DAO collection size: " + colPerDRUsagePoint.size());
    }
    
    return colPerDRUsagePoint;   
  } 

  /**
   * Get a sql string from the environment variables in the deployment
   * descriptor
   */
  private String getSQLStatement(String statementLocation) throws NamingException
  {
    InitialContext ctx = new InitialContext();
    String sqlStatement = (String) ctx.lookup(statementLocation);
    return sqlStatement;
  }

  /**
   * This method contains the logic for retrieving ranking data or usage statistics
   * from the database.<br/>
   *
   * @param conn The JDBC connection object.
   * @param filer The filter indicates which data query is resquested.
   * @param searchObject The sql's where clause criteria.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
  	int reportType = 0;
	Object resultData = null;
    CallableStatement searchQuery = null;
    String queryString = "";
    Ranking readEdetail;
    
    if (searchObject == null)
	  throw new RuntimeException("SearchObject is passed in null.");
    else
    {    
      if (filter.equals("ranking"))
      {      	      
        // Get the current and preivous month value first
        Map searchCriteria = (Map) searchObject;
        Date currentMonth = (Date) searchCriteria.get("cur_month");
        Date previousMonth = (Date) searchCriteria.get("pre_month");
        String mrId = (String) searchCriteria.get("mrId"); 
        Ranking ranking = new Ranking();
        ranking.setRankingDate(currentMonth);
        ranking.setMrId(mrId);
      
        // These code will obtain the total count of read sent EDetail
        queryString = getSQLStatement(RANKING_READ_EDETAIL_COUNT_QUERY);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);       
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.DATE);
        searchQuery.registerOutParameter(4, Types.DATE);
        searchQuery.registerOutParameter(5, Types.INTEGER);
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.registerOutParameter(8, Types.INTEGER);
        searchQuery.registerOutParameter(9, Types.INTEGER);
        searchQuery.registerOutParameter(10, Types.INTEGER);
        searchQuery.execute();
        readEdetail = populateReadEDetailRankingObject(searchQuery, ranking);
        
        // These code will obtain the ranking and the total number in ranking
        queryString = getSQLStatement(RANKING_TOTAL_RANKING_COUNT_QUERY);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);       
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.INTEGER);
        searchQuery.registerOutParameter(5, Types.INTEGER);
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.execute();
        resultData = populateTotalCountRankingObject(searchQuery, readEdetail);            
      }
      else
        throw new NotImplementedException("Unknown multiple message filter type - " + filter);  
    }
     
    searchQuery.close();
    return resultData;
  }
  
  /**
   * This method contains the logic for adding a new EmailContact
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDR The DR object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addEmail)
    throws SQLException, NamingException
  {
  	/*
    // Check delete object type
    if (!(addEmail instanceof EmailContact))
      throw new RuntimeException("Add object is incorrect type - " + addEmail.getClass());
    EmailContact workingContact = (EmailContact) addEmail;

    // Update the EmailContact
    if (workingContact.getEmailContactId() == null)
      workingContact.setEmailContactId("" + Sequence.getNext(conn, "EmailContact"));
    PreparedStatement addEmailQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    
    addEmailQuery.setString(1, workingContact.getEmailContactId());

    if (workingContact.getName() == null)
      addEmailQuery.setNull(2, Types.VARCHAR);
    else
      addEmailQuery.setString(2, workingContact.getName());

    if (workingContact.getEmailAddress() == null)
      addEmailQuery.setNull(3, Types.VARCHAR);
    else
      addEmailQuery.setString(3, workingContact.getEmailAddress());

    int rowsAffected = addEmailQuery.executeUpdate();
    addEmailQuery.close();
    return (rowsAffected == 1);
	*/
	
	return false;
  }

  /**
   * This method contains the logic for updating an
   * existing EmailContact in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveDR The EmailContact object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
  	/*
    // Check save object type
    if (saveContact == null)
      return true;
    else if (!(saveContact instanceof EmailContact))
      throw new RuntimeException("Save object is incorrect type - " + saveContact.getClass());
    EmailContact workingContact = (EmailContact) saveContact;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    if (workingContact.getName() == null)
      saveQuery.setNull(1, Types.VARCHAR);
    else
      saveQuery.setString(1, workingContact.getName());
    if (workingContact.getEmailAddress() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingContact.getEmailAddress());
    saveQuery.setString(3, workingContact.getEmailContactId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
	*/
	
	return false;
  }

  /**
   * This method contains the logic for deleting an
   * existing EmailContact from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteDR The EmailContact object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteContact)
    throws SQLException, NamingException
  {
  	/*
    // Check delete object type
    if (!(deleteContact instanceof EmailContact))
      throw new RuntimeException("Delete object is incorrect type - " + deleteContact.getClass());
    EmailContact workingContact = (EmailContact) deleteContact;

    // Delete the DR
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingContact.getEmailContactId());
    int rowsAffected = deleteQuery.executeUpdate();
    deleteQuery.close();
    return (rowsAffected == 1);
	*/
	
	  return false;
  }

  /**
   * Create new instances in persistent storage
   */
  public boolean createMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Retrieves existing instances from persistent storage
   */
  public Object searchMultiple(Connection conn, Object searchObject, String filter)
    throws SQLException, NamingException
  {  
    if (searchObject == null)
	  throw new RuntimeException("SearchObject is passed in null.");
    else
    {
      Map searchCriteria = (Hashtable) searchObject;
      PreparedStatement query = null;
      CallableStatement searchQuery = null;
      ResultSet result = null;
      Map usagePoint = new Hashtable();        
      String queryString = ""; 
      
      // Get the current and preivous month value first
      Date currentMonth = (Date)searchCriteria.get("c_month");
      Date previousMonth = (Date)searchCriteria.get("p_month");
      String mrId = (String)searchCriteria.get("mrId");
    
      // Query for the MR usage point (self count) for the current and prevoius month  
  	  if (filter.equals(sessionEJBConstant.MR_USAGE_SELF_COUNT))   
      {      
        // Create usageStatistic object for the current month
        UsageStatistics curMonth = new UsageStatistics();
        curMonth.setMrId(mrId);
        curMonth.setStatisticMonth(currentMonth);
        curMonth.setType(new Integer(UsageStatistics.SELF_COUNT));
      
        // Create usageStatistic object for the previous month
        UsageStatistics preMonth = new UsageStatistics();
        preMonth.setMrId(mrId);
        preMonth.setStatisticMonth(previousMonth);
        preMonth.setType(new Integer(UsageStatistics.SELF_COUNT));
      
        // Put current and previous months in the hashtable for loading up the rest of the data
        usagePoint.put("cur_month", curMonth);
        usagePoint.put("pre_month", preMonth);      
        
        queryString = getSQLStatement(USAGE_POINT_SP_SELF_EDETAIL_COUNT);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);       
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.DATE);
        searchQuery.registerOutParameter(4, Types.DATE);      
        searchQuery.registerOutParameter(5, Types.INTEGER);
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.registerOutParameter(8, Types.INTEGER);      
        searchQuery.registerOutParameter(9, Types.INTEGER);
        searchQuery.registerOutParameter(10, Types.INTEGER);
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, SELF_EDETAIL_COUNT);
      
        // Get the data (MR's contact count sent from DR) from database 
        // and load up these data to the usageStatistic object 
        queryString = getSQLStatement(USAGE_POINT_SP_SELF_CONTACT_COUNT);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);       
        searchQuery.setString(2, mrId); 
        searchQuery.registerOutParameter(3, Types.DATE);
        searchQuery.registerOutParameter(4, Types.DATE);      
        searchQuery.registerOutParameter(5, Types.INTEGER);
        searchQuery.registerOutParameter(6, Types.INTEGER);   
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, SELF_CONTACT_COUNT);      
      
        // Get the data (MR's registered DR count and active count) from database 
        // and load up these data to the usageStatistic object
        queryString = getSQLStatement(USAGE_POINT_SP_SELF_ACTIVE_COUNT);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);       
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.DATE);
        searchQuery.registerOutParameter(4, Types.DATE);      
        searchQuery.registerOutParameter(5, Types.INTEGER);
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.registerOutParameter(8, Types.INTEGER);            
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, SELF_ACTIVE_COUNT);      
      }
      else if( filter.equals(sessionEJBConstant.MR_USAGE_COMPANY_COUNT))
      {    
        // Create usageStatistic object for the current month
        UsageStatistics curMonth = new UsageStatistics();
        curMonth.setMrId(mrId);
        curMonth.setStatisticMonth(currentMonth);
        curMonth.setType(new Integer(UsageStatistics.COMPANY_AVG));
      
        // Create usageStatistic object for the previous month
        UsageStatistics preMonth = new UsageStatistics();
        preMonth.setMrId(mrId);
        preMonth.setStatisticMonth(previousMonth);
        preMonth.setType(new Integer(UsageStatistics.COMPANY_AVG));
      
        // Put current and previous months in the hashtable for loading up the rest of the data
        usagePoint.put("cur_month", curMonth);
        usagePoint.put("pre_month", preMonth);
        queryString = getSQLStatement(USAGE_POINT_SP_COMPANY_EDETAIL_COUNT);     
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);      
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.DATE);
        searchQuery.registerOutParameter(5, Types.DATE);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.registerOutParameter(8, Types.INTEGER);
        searchQuery.registerOutParameter(9, Types.INTEGER);      
        searchQuery.registerOutParameter(10, Types.INTEGER);
        searchQuery.registerOutParameter(11, Types.INTEGER);
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, COMP_EDETAIL_COUNT);
        
        // Get the data (company's average on contact count sent from DR) from database 
        // and load up these data to the usageStatistic object 
        queryString = getSQLStatement(USAGE_POINT_SP_COMPANY_CONTACT_COUNT);      
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);      
        searchQuery.setString(2, mrId); 
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.DATE);
        searchQuery.registerOutParameter(5, Types.DATE);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);    
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, COMP_CONTACT_COUNT);
        
        // Get the data (Company's average on registered DR count and active count) from database 
        // and load up these data to the usageStatistic object
        queryString = getSQLStatement(USAGE_POINT_SP_COMPANY_ACTIVE_COUNT);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);        
        searchQuery.setString(2, mrId);
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.DATE);
        searchQuery.registerOutParameter(5, Types.DATE);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.registerOutParameter(8, Types.INTEGER);
        searchQuery.registerOutParameter(9, Types.INTEGER);            
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, COMP_ACTIVE_COUNT);          
	  }
      else if (filter.equals(sessionEJBConstant.MR_USAGE_RANKING_COUNT))
      {
        // Create ranking object for read sent eDetail of the current month
        Ranking curMonthRankingRER = new Ranking(mrId);
        curMonthRankingRER.setRankingDate(currentMonth);
      
        // Create ranking object for read sent eDetail of the previous month
        Ranking preMonthRankingRER = new Ranking(mrId);
        curMonthRankingRER.setRankingDate(previousMonth);      
      
        // Put current and previous months in the hashtable for loading up the rest of the data
        usagePoint.put("read_edetail_cur_month", curMonthRankingRER);
        usagePoint.put("read_edetail_pre_month", preMonthRankingRER);
                   
        queryString = getSQLStatement(USAGE_POINT_SP_READ_EDETAIL_RANKING);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);
        searchQuery.setString(2, mrId);      
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.INTEGER);
        searchQuery.registerOutParameter(5, Types.INTEGER);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);      
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, READ_EDETAIL_RANKING);
      
        // Create ranking object for contact sent by DR of the current month
        Ranking curMonthRankingCR = new Ranking(mrId);
        curMonthRankingCR.setRankingDate(currentMonth);
      
        // Create ranking object for contact sent by DR of the previous month
        Ranking preMonthRankingCR = new Ranking(mrId);
        preMonthRankingCR.setRankingDate(previousMonth);      
      
        // Put current and previous months in the hashtable for loading up the rest of the data
        usagePoint.put("contact_cur_month", curMonthRankingCR);
        usagePoint.put("contact_pre_month", preMonthRankingCR);
      
        queryString = getSQLStatement(USAGE_POINT_SP_CONTACT_RANKING);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);
        searchQuery.setString(2, mrId);      
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.INTEGER);
        searchQuery.registerOutParameter(5, Types.INTEGER);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, CONTACT_RANKING);
      
        // Create ranking object for active channel of the current month
        Ranking curMonthRankingAR = new Ranking(mrId);
        curMonthRankingAR.setRankingDate(currentMonth);
      
        // Create ranking object for active channel of the previous month
        Ranking preMonthRankingAR = new Ranking(mrId);
        curMonthRankingAR.setRankingDate(previousMonth);      
      
        // Put current and previous months in the hashtable for loading up the rest of the data
        usagePoint.put("active_cur_month", curMonthRankingAR);
        usagePoint.put("active_pre_month", preMonthRankingAR);
      
        queryString = getSQLStatement(USAGE_POINT_SP_ACTIVE_RANKING);
        searchQuery = conn.prepareCall(queryString);
        searchQuery.setDate(1, currentMonth);
        searchQuery.setString(2, mrId);      
        searchQuery.registerOutParameter(3, Types.VARCHAR);
        searchQuery.registerOutParameter(4, Types.INTEGER);
        searchQuery.registerOutParameter(5, Types.INTEGER);      
        searchQuery.registerOutParameter(6, Types.INTEGER);
        searchQuery.registerOutParameter(7, Types.INTEGER);
        searchQuery.execute();
        populateUsageStatisticsObject(searchQuery, usagePoint, ACTIVE_RANKING);
        searchQuery.close();
      }
      else if (filter.equals(sessionEJBConstant.MR_USAGE_DR_PROFILE_COUNT))
      {
        // These arrays will hold the result
        Collection curMonthDRPoint = new ArrayList(); 
        Collection preMonthDRPoint = new ArrayList();
        
        // Create the next month instance
        Calendar cdr = Calendar.getInstance();
    	cdr.add(Calendar.MONTH, 1);
        Date nextMonth = new Date((cdr.getTime()).getTime());
	
	// Create the date of first date of current month instance
        Calendar bcdr = Calendar.getInstance();
    	bcdr.set(Calendar.DAY_OF_MONTH, 1);
        Date firstOfCurMonth = new Date((bcdr.getTime()).getTime());
		
        System.out.println("SQL: " + getSQLStatement(USAGE_POINT_SEARCH_PER_DR_USAGE_CUR_MONTH));
        System.out.println("Current: " + currentMonth);
        System.out.println("Next: " + nextMonth);
        // Create the query and populate a return object for the current Month MR's DR usage point
	    query = conn.prepareStatement(getSQLStatement(USAGE_POINT_SEARCH_PER_DR_USAGE_CUR_MONTH));
	    query.setDate(1, currentMonth);
  	    query.setDate(2, nextMonth);
        query.setDate(3, currentMonth);
  	    query.setDate(4, nextMonth);
        query.setDate(5, currentMonth);
  	    query.setDate(6, nextMonth);
        query.setDate(7, currentMonth);
  	    query.setDate(8, nextMonth);
        query.setString(9, mrId);
  
	    result = query.executeQuery();
	    curMonthDRPoint = populatePerDRUsagePoint(result, currentMonth, mrId);                   
        if (curMonthDRPoint == null)
	      throw new RuntimeException(this.getClass().getName() + "MR's DR usage point (current month) result is returned null.");
        
        // Create the query and populate a return object for the previous Month MR's DR usage point        
        query = conn.prepareStatement(getSQLStatement(USAGE_POINT_SEARCH_PER_DR_USAGE_PRE_MONTH));
	    query.setDate(1, previousMonth);
  	    query.setDate(2, currentMonth);
        query.setDate(3, previousMonth);
  	    query.setDate(4, currentMonth);
        query.setDate(5, previousMonth);
  	    query.setDate(6, currentMonth);
        query.setDate(7, previousMonth);
  	    query.setDate(8, currentMonth);
        query.setString(9, mrId);
  	    query.setDate(10, firstOfCurMonth);
	    result = query.executeQuery();
        preMonthDRPoint = populatePerDRUsagePoint(result, previousMonth, mrId);
        if (preMonthDRPoint == null)
	      throw new RuntimeException(this.getClass().getName() + "MR's DR usage point (previous month) result is returned null.");
        
        // Set both current and previous month result to returning hash map and then close the database connection
        usagePoint.put("dr_point_cur_month", curMonthDRPoint);
        usagePoint.put("dr_point_pre_month", preMonthDRPoint);
        result.close();  
        query.close();              
      }
      else if (filter.equals(sessionEJBConstant.MR_SINGLE_USAGE_COUNT))   
      { 
        // Get the drId for the data query       
        String drId = (String)searchCriteria.get("drId");
        UsageStatistics curMonthSingleReport = new UsageStatistics();
        UsageStatistics preMonthSingleReport = new UsageStatistics();
        
        // Create the next month instance
        Calendar cdr = Calendar.getInstance();
    	cdr.add(Calendar.MONTH, 1);
        Date nextMonth = new Date((cdr.getTime()).getTime());
        
        // Create the query and populate a return object for the current Month MR's DR usage point
	    query = conn.prepareStatement(getSQLStatement(USAGE_POINT_SEARCH_SINGLE_DR_USAGE));
	    query.setDate(1, currentMonth);
  	    query.setDate(2, nextMonth);
        query.setDate(3, currentMonth);
  	    query.setDate(4, nextMonth);
        query.setDate(5, currentMonth);
  	    query.setDate(6, nextMonth);
        query.setDate(7, currentMonth);
  	    query.setDate(8, nextMonth);
        query.setString(9, mrId);
  	    query.setString(10, drId);
	    result = query.executeQuery();
	    curMonthSingleReport = populateSingleUsagePoint(result, currentMonth, mrId, drId);                   
        if (curMonthSingleReport == null)
	      throw new RuntimeException(this.getClass().getName() + "MR's DR usage point (current month) result is returned null.");
        
        // Create the query and populate a return object for the previous Month MR's DR usage point        
        query = conn.prepareStatement(getSQLStatement(USAGE_POINT_SEARCH_SINGLE_DR_USAGE));
	    query.setDate(1, previousMonth);
  	    query.setDate(2, currentMonth);
        query.setDate(3, previousMonth);
  	    query.setDate(4, currentMonth);
        query.setDate(5, previousMonth);
  	    query.setDate(6, currentMonth);
        query.setDate(7, previousMonth);
  	    query.setDate(8, currentMonth);
        query.setString(9, mrId);
  	    query.setString(10, drId);
	    result = query.executeQuery();
        preMonthSingleReport = populateSingleUsagePoint(result, previousMonth, mrId, drId);
        if (preMonthSingleReport == null)
	      throw new RuntimeException(this.getClass().getName() + "MR's DR usage point (previous month) result is returned null.");      
        
        // Set both current and previous month result to returning hash map and then close the database connection
        usagePoint.put("single_point_cur_month", curMonthSingleReport);
        usagePoint.put("single_point_pre_month", preMonthSingleReport);
        result.close();  
        query.close();              
      }
      else 
        throw new NotImplementedException("Unknown multiple message filter type - " + filter);  

      return usagePoint;
    }   
  }

  /**
   * Saves existing instances in persistent storage
   */
  public boolean updateMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Deletes existing instances from persistent storage
   */
  public boolean deleteMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

}

