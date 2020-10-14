
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This class implements the persistence mechanism for the DR class.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DR_DAO.java,v 1.1.1.1.2.28 2001/12/20 09:14:07 rick Exp $
 */
public class DR_DAO implements DAO_SQL
{
  final String  SAVE_MEMO_QUERY               = "java:comp/env/sql/DRSaveMemoSQL";
  final String  ADD_MEMO_QUERY                = "java:comp/env/sql/DRInsertMemoSQL";
  final String  ADD_QUERY                     = "java:comp/env/sql/DRInsertSQL";
  final String  SEQ_DR_QUERY                  = "java:comp/env/sql/DRSystemCdSeqSQL";
  final String  ADD_HOSPITAL_QUERY            = "java:comp/env/sql/DRInsertHospitalSQL";
  final String  SEARCH_BY_NAME_QUERY          = "java:comp/env/sql/DRSearchNameSQL";
  final String  SEARCH_BY_ID_QUERY            = "java:comp/env/sql/DRSearchIDSQL";
  final String  SAVE_QUERY                    = "java:comp/env/sql/DRUpdateSQL";
  final String  REMOVE_QUERY                  = "java:comp/env/sql/DRDeleteSQL";
  final String  ADD_INFORMATION_QUERY         = "java:comp/env/sql/MRInformationInsertSQL";
  final String  SEQ_INFORMATION_QUERY         = "java:comp/env/sql/MRInformationSeqSQL";
  final String  FRONT_COUNT_QUERY             = "java:comp/env/sql/MRInformationFrontCountSQL";
  final String  GET_TARGETRANK_QUERY          = "java:comp/env/sql/CompanyTargetRankSQL";
  final String  SEARCH_QUALIFICATION_QUERY    = "java:comp/env/sql/DRQualificationSQL";
  final String  SEARCH_SPECIALTY_QUERY        = "java:comp/env/sql/DRSpecialtySQL";
  final String  ADD_ENQUETE_QUERY             = "java:comp/env/sql/EnqueteLogInitInsertSQL";
  final String  UDPATE_BANNER_POSITION_QUERY  = "java:comp/env/sql/UpdateMrBannerPositionSQL";
  final String  SAVE_DR_MRI                   = "java:comp/env/sql/DRUpdateMRISQL";
  final String  REMOVE_DR_MRI                 = "java:comp/env/sql/DRDeleteMRISQL";
  final String  LOAD_DR_MRI                   = "java:comp/env/sql/DRLoadMRISQL";
  final String  LOAD_PROFILE                  = "java:comp/env/sql/DRLoadProfileSQL";
  final String  SAVE_PROFILE                  = "java:comp/env/sql/DRSaveProfileSQL";
  final String  DELETE_PROFILE                = "java:comp/env/sql/DRDeleteProfileSQL";
  final String  SAVE_HOSPITAL                 = "java:comp/env/sql/DRSaveHospitalSQL";
  final String  SEARCH_HISTORY_BY_ID_QUERY    = "java:comp/env/sql/MRInformationSearchHistoryByIDSQL";
  final String  DR_MR_FRONT_COUNT_SQL         = "java:comp/env/sql/MRInformationFrontCountSQL";
  final String  DEFAULT_MR_CHECK_SQL          = "java:comp/env/sql/DefaultMRFrontCheck";
  final String  SEARCH_POINT_QUERY            = "java:comp/env/sql/SelectDoctorPoint";
  final String  INSERT_LOGIN_INFO_QUERY       = "java:comp/env/sql/InsertLoginInfo";

  final String  PLACE                 = "0000000001";
  final String  NETWORK               = "0000000002";
  final String  CLINIC                = "0000000004";
  final String  PREFECTURE            = "0000000010";
  final String  SHIKAKU               = "0000000015";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection conn, ResultSet attributes)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
//      throw new NoObjectFoundException("Resultset contained no rows");
        return null;
    else
    {
//      DAO_SQL daoEmail = new EmailContact_DAO();
      DR resultDR = new DR();

      resultDR.setDrId(attributes.getString("dr_Id"));
      resultDR.setSystemDrCd(attributes.getString("system_cd"));
      resultDR.setKanaName(attributes.getString("kanaName"));
      resultDR.setKanjiName(attributes.getString("kanjiName"));
      resultDR.setHospital(attributes.getString("hospital"));
//      resultDR.setMedCertification(attributes.getString("medCertification"));
      resultDR.setWorkArea(getProfileValue(conn, PREFECTURE, resultDR.getDrId()));
      resultDR.setUsageArea(getProfileValue(conn, PLACE, resultDR.getDrId()));
      resultDR.setNetworkEnv(getProfileValue(conn, NETWORK, resultDR.getDrId()));
      resultDR.setSpecialty(getProfileValue(conn, CLINIC, resultDR.getDrId()));
      resultDR.setMedCertification(getProfileValue(conn, SHIKAKU, resultDR.getDrId()));
      resultDR.setEmail(attributes.getString("email"));
      resultDR.setCurrentUsagePoint(new Integer(attributes.getInt("point")));
      resultDR.setOldUsagePoint(new Integer(attributes.getInt("lastYearPoint")));

      String publicOfficialValue = attributes.getString("publicOfficial");
      boolean publicOfficial = ((publicOfficialValue != null) && publicOfficialValue.equals("1"));
      resultDR.setPublicOfficial(new Boolean(publicOfficial));

      String mrKunMailValue = attributes.getString("mrkun_spam_allowed");
      boolean mrKunMail = ((mrKunMailValue != null) && mrKunMailValue.equals("1"));
      resultDR.setMRKunMail(new Boolean(mrKunMail));

      String genericSpamValue = attributes.getString("generic_spam_allowed");
      boolean genericSpam = ((genericSpamValue != null) && genericSpamValue.equals("1"));
      resultDR.setEmailInfo(new Boolean(genericSpam));
      
      resultDR.setMRList(loadMRMap(conn, resultDR.getDrId()));

      return resultDR;
    }
  }

  /**
   * Loads a normalised field from the DoctorProfile table
   */
  private String getProfileValue(Connection conn, String itemCode, String drId)
    throws SQLException, NamingException
  {
    if (itemCode == null) return null;
    
    // Look up this item code and DR in the doctor_profile table
    PreparedStatement profileQry = conn.prepareStatement(getSQLStatement(LOAD_PROFILE));
    profileQry.setString(1, drId);
    profileQry.setString(2, itemCode);
    String item = null;
    ResultSet rstProfile = profileQry.executeQuery();
    if (rstProfile.next())
      item = rstProfile.getString("item");
    rstProfile.close();
    profileQry.close();
    return item;
  }

  /**
   * Loads a normalised field from the DoctorProfile table
   */
  private boolean saveProfileValue(Connection conn, String drId, String itemCode, String itemValue)
    throws SQLException, NamingException
  {
    return saveProfileValue(conn, drId, itemCode, itemValue, "1" );
  }

  private boolean saveProfileValue(Connection conn, String drId, String itemCode, String itemValue, String seq)
    throws SQLException, NamingException
  {
    if (itemCode == null)
      throw new ApplicationError("itemCode is null");
    else if (drId == null)
      throw new ApplicationError("drId is null");

    // Delete this item code and DR in the doctor_profile table
    PreparedStatement profileDelQry = conn.prepareStatement(getSQLStatement(DELETE_PROFILE));
    profileDelQry.setString(1, drId);
    profileDelQry.setString(2, itemCode);
    profileDelQry.executeUpdate();
    profileDelQry.close();

    // Look up this item code and DR in the doctor_profile table
    PreparedStatement profileQry = conn.prepareStatement(getSQLStatement(SAVE_PROFILE));
    profileQry.setString(1, drId);
    profileQry.setString(2, itemCode);
    profileQry.setString(3, seq);
    profileQry.setString(4, itemValue);
    int rowsAffected = profileQry.executeUpdate();
    profileQry.close();
    return (rowsAffected == 1);
  }

  /**
   * This method loads the MRInfo list into the DR object.
   */
  private Map loadMRMap(Connection conn, String drId)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Get a list of forwards
    PreparedStatement mrInfoQuery = conn.prepareStatement(getSQLStatement(LOAD_DR_MRI));
    mrInfoQuery.setString(1, drId);
    ResultSet mriSet = mrInfoQuery.executeQuery();
    Map returnList = new Hashtable();

    // Add them to the list
    while (mriSet.next())
      returnList.put(mriSet.getString("mr_id"), mriSet.getString("mr_id"));
    mriSet.close();
    return returnList;
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
   * This method contains the logic for adding a new
   * DR into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDR The DR object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addObject)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (addObject instanceof DR)
    {
      DR workingDR = (DR) addObject;

      // default MRID and CompanyCD
      DAO_SQL daoDBConstant = new DatabaseConstant_DAO();
      DatabaseConstant mrId = (DatabaseConstant)daoDBConstant.searchRecord(conn,
                               HttpConstant.ENQUETE_MR_ID,"constantId");
      DatabaseConstant companyCd = (DatabaseConstant)daoDBConstant.searchRecord(conn,
                               HttpConstant.ENQUETE_COMPANY_CD,"constantId");

      try
      {
        conn.setAutoCommit(false);

        // Get Doctor seq No.
        String systemCd = null;
        Statement searchQuery = conn.createStatement();
        ResultSet results = searchQuery.executeQuery(getSQLStatement(SEQ_DR_QUERY));
        if (results.next()) {
          systemCd = results.getString("counter").trim();
        }
        results.close();
        searchQuery.close();
        if (systemCd == null)
          throw new SQLException("Dr data has not been registered into DB or there was unjust registration.");

        // Insert the DR
        PreparedStatement addDRQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
        addDRQuery.setString(1, workingDR.getDrId());
        addDRQuery.setString(2, systemCd);
        addDRQuery.setString(3, workingDR.getKanjiName());
        addDRQuery.setString(4, workingDR.getKanaName());
        addDRQuery.setString(5, workingDR.getEmail());
        addDRQuery.setString(6, workingDR.isPublicOfficial().booleanValue() ? "1" : null);
        int rowsAffected = addDRQuery.executeUpdate();
        addDRQuery.close();
        if (rowsAffected != 1)
          throw new SQLException("Dr data has not been registered into DB or there was unjust registration.");

        // Insert the Hospital
        PreparedStatement addHospitalQuery = conn.prepareStatement(getSQLStatement(ADD_HOSPITAL_QUERY));
        addHospitalQuery.setString(1, workingDR.getDrId());
        addHospitalQuery.setString(2, workingDR.getHospital());
        rowsAffected = addHospitalQuery.executeUpdate();
        addHospitalQuery.close();
        if (rowsAffected != 1)
          throw new SQLException("Hospital data has not been registered into DB.");

        // Insert the MRInfomation Proc
        addMRInfomation( conn, workingDR.getDrId(), mrId.getNaiyo1(), "");

        // Insert the Enquete log
        PreparedStatement addEnqueteQuery = conn.prepareStatement(getSQLStatement(ADD_ENQUETE_QUERY));
        addEnqueteQuery.setObject(1, workingDR.getDrId());
        rowsAffected = addEnqueteQuery.executeUpdate();
        addEnqueteQuery.close();
        if (rowsAffected != 1)
          throw new SQLException("Enquete Log data has not been registered into DB.");

        conn.commit();
      }
      catch( SQLException ex )
      {
        conn.rollback();
        throw new ApplicationError("DR_DAO.class: createRecord(): SQLException", ex );
      }
      finally
      {
        conn.setAutoCommit(true);
      }
      return true;
    }
    else if (addObject instanceof Map)
    {
      Map sqlParams = (Map) addObject;
      String mrId = (String) sqlParams.get("mrId");
      String drId = (String) sqlParams.get("drId");
      String memo = (String) sqlParams.get("memo");
      String agent = (String) sqlParams.get("agent");
      String action = (String) sqlParams.get("action");
      if ( action != null)
      {
      	if ( action.equals("login")	 && (drId != null) )
      	{
			 return(addLoginInfo(conn,drId,agent));
      	}
        else
        {
			throw new ApplicationError("DR_DAO.createRecord: wrong action name of no drId set");        	
        }
      }
      if ((mrId == null) || (mrId.equals("")) || (drId == null) || (drId.equals("")) || (memo == null))
        throw new ApplicationError("Required parameter mrId or drId were not specified or memo field is null");
      else
      {
        // Insert the MRInfomation Proc
        try
        {
          conn.setAutoCommit(false);
          addMRInfomation( conn, drId, mrId, memo);
          conn.commit();
        }
        catch( SQLException ex )
        {
          conn.rollback();
          throw new ApplicationError("DR_DAO.class: createRecord(memo): SQLException", ex );
        }
        finally
        {
          conn.setAutoCommit(true);
        }
      }
      return true;

    }
    else
      throw new RuntimeException("Add object is incorrect type - " + addObject.getClass());
  }

  
  private boolean addLoginInfo(Connection conn, String drId, String agent)
  	throws SQLException, NamingException
  {
	  PreparedStatement addLoginQuery = conn.prepareStatement(getSQLStatement(INSERT_LOGIN_INFO_QUERY));
	  addLoginQuery.setString(1, drId);
	  addLoginQuery.setString(2, agent);
	  int rowsAffected = addLoginQuery.executeUpdate();
	  addLoginQuery.close();
	  if (rowsAffected != 1)
	  {
		  throw new SQLException("Enquete Log data has not been registered into DB.");
	  }
      else
      {
      	return(true);	
      }
  	
  }
  /**
   *  Doctor add MRInfomation
   **/

  private void addMRInfomation(Connection conn, String drId, String mrId, String memo)
  throws SQLException, NamingException
  {

    int counter;
    String seqCounter = null;
    String sentakuKbn;
    String targetRank = null;

    // Get Infomation seq No.
    Statement searchQuery1 = conn.createStatement();
    ResultSet results1 = searchQuery1.executeQuery(getSQLStatement(SEQ_INFORMATION_QUERY));
    if (results1.next()) {
      seqCounter = results1.getString("counter").trim();
    }
    results1.close();
    searchQuery1.close();

    // Get Defalt Target Rank By Company
    PreparedStatement searchQuery2 = conn.prepareStatement(getSQLStatement(GET_TARGETRANK_QUERY));
    searchQuery2.setObject(1, mrId);
    ResultSet results2 = searchQuery2.executeQuery();
    if (results2.next()) {
      targetRank = results2.getString("target_rank");
    }
    results2.close();
    searchQuery2.close();

    // Front MR counting
    int mrCount = 0;
    PreparedStatement searchQuery3 = conn.prepareStatement(getSQLStatement(DR_MR_FRONT_COUNT_SQL));
    searchQuery3.setString(1, drId);
    ResultSet results3 = searchQuery3.executeQuery();

    if (results3.next())
    {
      mrCount = results3.getInt("counter");
    }
    results3.close();
    searchQuery3.close();

    if (mrCount == 0) {
        sentakuKbn = "1";
    } else if (mrCount == 1) {
        sentakuKbn = replaceSentakuKbn(conn, drId, 1);
    } else if (mrCount == 2) {
        sentakuKbn = replaceSentakuKbn(conn, drId, 2);
    } else {
        sentakuKbn = "3";
    }

    // Insert the MRInfomation
    PreparedStatement addInfomationQuery = conn.prepareStatement(getSQLStatement(ADD_INFORMATION_QUERY));
    addInfomationQuery.setObject(1, drId);
    addInfomationQuery.setObject(2, mrId);
    addInfomationQuery.setObject(3, seqCounter);
    addInfomationQuery.setObject(4, sentakuKbn);
    addInfomationQuery.setObject(5, targetRank);
    addInfomationQuery.setObject(6, "à„ét");
    addInfomationQuery.setObject(7, drId);
    int rowsAffected = addInfomationQuery.executeUpdate();
    addInfomationQuery.close();
    if (rowsAffected != 1)
      throw new SQLException("MR Infomation data has not been registered into DB.");
  }

  /**
   *Å@It will shift, if default MR exists.
   */
  private String replaceSentakuKbn(Connection conn, String drId, int count)
  throws SQLException, NamingException
  {
    String sentakuKbn = null;
    String infoDrID = null;
    String infoMrID = null;
    String infoSentakuKbn = null;

    // default MR's existence check
    PreparedStatement pstmt = conn.prepareStatement(getSQLStatement(DEFAULT_MR_CHECK_SQL));
    pstmt.setString(1, drId);
    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
      infoDrID = rs.getString("dr_id");
      infoMrID = rs.getString("mr_id");
      infoSentakuKbn = rs.getString("sentaku_kbn");
    }
    pstmt.close();

    // It removes, when set as the front.
    if (infoMrID != null) {
      pstmt = conn.prepareStatement(getSQLStatement(UDPATE_BANNER_POSITION_QUERY));

      if (count == 2) {		// In 2 persons, it removes.
        pstmt.setString(1, "3");
        if(infoSentakuKbn.equals("2"))
          sentakuKbn = "2";
        else
          sentakuKbn = "1";
      } else {		// In the case of 1 person, it is to the right.
        pstmt.setString(1, "2");
        sentakuKbn = "1";
      }
      pstmt.setString(2, infoDrID);
      pstmt.setString(3, infoMrID);

      int count1 = pstmt.executeUpdate();
      pstmt.close();
    } else {
      sentakuKbn = "3";
    }

    return sentakuKbn;
  }


  /**
   * This method contains the logic for retrieving
   * a DR from the database, where a DR name is all we have.<br/>
   * Note: While this query may return many DR objects if the name
   * field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The DR's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NoObjectFoundException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("searchSentakuTorokuHistory"))
    {
      Map     params  = (Map) searchObject;
      String  drId    = (String) params.get("drId");
      String  mrId    = (String) params.get("mrId");
      System.out.println("Checking SentakuTorokuHistory - drId: " + drId + " mrId:" + mrId);
      PreparedStatement searchQuery = conn.prepareStatement(getSQLStatement(SEARCH_HISTORY_BY_ID_QUERY));
      searchQuery.setString(1, drId);
      searchQuery.setString(2, mrId);
      ResultSet results = searchQuery.executeQuery();
      boolean found = results.next();
      results.close();
      searchQuery.close();
      System.out.println("Found : " + found);
      return found?(new Object()):null;
    }
    else if (filter.equals("point"))
    {
      Integer point = null;
      String  drId    = (String) searchObject;
      PreparedStatement searchQuery = conn.prepareStatement(getSQLStatement(SEARCH_POINT_QUERY));
      searchQuery.setString(1, drId);
      ResultSet results = searchQuery.executeQuery();
      if( results.next() )
      {
        point = new Integer( results.getInt("point") );
      }
      else
        throw new NoObjectFoundException("Resultset contained no rows");
      results.close();
      searchQuery.close();
      return point;
    }
    else
    {
      if (filter.equals("drId"))
        queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
      else if (filter.equals("name"))
        queryString = getSQLStatement(SEARCH_BY_NAME_QUERY);
      else
        throw new ApplicationError("Invalid query filter type - " + filter);

      PreparedStatement searchQuery = conn.prepareStatement(queryString);
      searchQuery.setObject(1, searchObject);
      ResultSet results = searchQuery.executeQuery();
      Object found = populateObject(conn, results);
      results.close();
      searchQuery.close();
      return found;
    }
  }

  /**
   * This method contains the logic for updating an
   * existing DR in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveDR The DR object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveObject)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveObject instanceof DR)
    { 
      DR workingDR = (DR) saveObject;

      // Save the objects in dependent tables
      if (!saveProfileValue(conn, workingDR.getDrId(), PREFECTURE, workingDR.getWorkArea()))
        throw new ApplicationError("Error saving the work area field in DR " + workingDR.getDrId());
      if (!saveProfileValue(conn, workingDR.getDrId(), PLACE, workingDR.getUsageArea()))
        throw new ApplicationError("Error saving the usage area field in DR " + workingDR.getDrId());
      if (!saveProfileValue(conn, workingDR.getDrId(), NETWORK, workingDR.getNetworkEnv()))
        throw new ApplicationError("Error saving the network field in DR " + workingDR.getDrId());
      if (!saveProfileValue(conn, workingDR.getDrId(), SHIKAKU, workingDR.getMedCertification()))
        throw new ApplicationError("Error saving the Medical Qualification field in DR " + workingDR.getDrId());
      if (!saveProfileValue(conn, workingDR.getDrId(), CLINIC, workingDR.getSpecialty()))
        throw new ApplicationError("Error saving the Specialty field in DR " + workingDR.getDrId());

      // Save the hospital
      PreparedStatement hospitalQuery = conn.prepareStatement(getSQLStatement(SAVE_HOSPITAL));
      if (workingDR.getHospital() != null)
        hospitalQuery.setString(1, workingDR.getHospital());
      else
        hospitalQuery.setNull(1, Types.VARCHAR);
      hospitalQuery.setString(2, workingDR.getDrId());
      int hospitalRowsAffected = hospitalQuery.executeUpdate();
      hospitalQuery.close();
      if (hospitalRowsAffected != 1)
        throw new ApplicationError("Error saving the hospital name in the DR object for DR: " + workingDR.getDrId());

      // Create the query
      PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
      saveQuery.setString (1, workingDR.getKanaName());
      saveQuery.setString (2, workingDR.getKanjiName());
//      if (workingDR.getMedCertification() != null)
//        saveQuery.setString (3, workingDR.getMedCertification());
//      else
        saveQuery.setNull(3, Types.VARCHAR);

      if ((workingDR.isPublicOfficial() != null) &&
          workingDR.isPublicOfficial().booleanValue())
        saveQuery.setString (4, "1");
      else
        saveQuery.setNull (4, Types.VARCHAR);

      if (workingDR.getEmail() != null)
        saveQuery.setString (5, workingDR.getEmail());
      else
        saveQuery.setNull(5, Types.VARCHAR);

      if ((workingDR.getMRKunMail() != null) &&
          workingDR.getMRKunMail().booleanValue())
        saveQuery.setString (6, "1");
      else
        saveQuery.setNull (6, Types.VARCHAR);

      if ((workingDR.getEmailInfo() != null) &&
          workingDR.getEmailInfo().booleanValue())
        saveQuery.setString (7, "1");
      else
        saveQuery.setNull (7, Types.VARCHAR);

      if (workingDR.getCurrentUsagePoint() != null)
        saveQuery.setInt (8, workingDR.getCurrentUsagePoint().intValue());
      else
        saveQuery.setNull (8, Types.INTEGER);

      saveQuery.setString (9, workingDR.getDrId());

      int rowsAffected = saveQuery.executeUpdate();
      saveQuery.close();
      return (rowsAffected == 1);
    }
    else if (saveObject instanceof Map)
    {
      Map sqlParams = (Map) saveObject;
      String mrId = (String) sqlParams.get("mrId");
      String drId = (String) sqlParams.get("drId");
      String memo = (String) sqlParams.get("memo");
      if ((mrId == null) || (mrId.equals("")) || (drId == null) || (drId.equals("")) || (memo == null))
        throw new ApplicationError("Required parameter mrId or drId were not specified or memo field is null");
      else
      {
        // Save the memo
        PreparedStatement memoSaveQuery = conn.prepareStatement(getSQLStatement(SAVE_MEMO_QUERY));
        if (memo.equals(""))
          memoSaveQuery.setNull(1, Types.VARCHAR);
        else
          memoSaveQuery.setString(1, memo);
        memoSaveQuery.setString(2, mrId);
        memoSaveQuery.setString(3, drId);  
        int affectedRows = memoSaveQuery.executeUpdate();
        memoSaveQuery.close();
        return (affectedRows == 1);      
      }
    }
    else
      throw new ApplicationError("Save object is incorrect type - " + saveObject.getClass());
  }

  /**
   * This method contains the logic for deleting an
   * existing DR from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteDR The DR object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteDR)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteDR instanceof DR))
      throw new RuntimeException("Delete object is incorrect type - " + deleteDR.getClass());
    DR workingDR = (DR) deleteDR;

    // Delete the DR
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingDR.getDrId());
    int rowsAffected = deleteQuery.executeUpdate();
    deleteQuery.close();
    return (rowsAffected == 1);
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
    if (filter.equals("drQualification"))
    {
      Collection list = new ArrayList();
      Statement searchQuery = conn.createStatement();
      ResultSet results = searchQuery.executeQuery(getSQLStatement(SEARCH_QUALIFICATION_QUERY));
      while (results.next()) {
        MedicalQualification qualification = new MedicalQualification();
        qualification.setQualificationCd(results.getString("shikaku_cd"));
        qualification.setQualificationName(results.getString("shikaku_name"));
        list.add(qualification);
      }
      results.close();
      searchQuery.close();
      return list;
    }
    else if (filter.equals("drSpecialty"))
    {
      Collection list = new ArrayList();
      Statement searchQuery = conn.createStatement();
      ResultSet results = searchQuery.executeQuery(getSQLStatement(SEARCH_SPECIALTY_QUERY));
      while (results.next()) {
        Specialty specialty = new Specialty();
        specialty.setSpecialtyCd(results.getString("shinryoka_cd"));
        specialty.setSpecialtyName(results.getString("shinryoka_name"));
        list.add(specialty);
      }
      results.close();
      searchQuery.close();
      return list;
    }
    else
    {
      throw new NotImplementedException();
    }
  }

  /**
   * Saves existing instances in persistent storage
   */
  public boolean updateMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
  	int rowsAffected = 0;
    
  	// Check update object type
    if (!(modifyingObject instanceof Map) || (modifyingObject == null))
      throw new NotImplementedException("ModifyingObject is in incorrect type or null - " + modifyingObject.getClass());
    else
    {
      Map sqlParams = (Map) modifyingObject;
      String drId = (String) sqlParams.get("drId");
      Collection colMrList = (Collection) sqlParams.get("mrList");
      if((drId == null) || (colMrList == null))
        throw new RuntimeException(this.getClass().getName() + ": Parameters are passed in null from the DRManager.");
      else
      {
        // For each mr update his/her new banner position in the database
        for (Iterator itr = colMrList.iterator(); itr.hasNext(); )
        {                
          // Extract the required parameters and perform update
          MrProfile mrProfile = (MrProfile) itr.next();
          PreparedStatement sqlQuery = conn.prepareStatement(getSQLStatement(UDPATE_BANNER_POSITION_QUERY));
          sqlQuery.setString(1, mrProfile.getMrBannerPosition());
          sqlQuery.setString(2, drId);
          sqlQuery.setString(3, mrProfile.getMrId());
          rowsAffected = sqlQuery.executeUpdate();
          sqlQuery.close();
        }
        return (rowsAffected == 1);
      }
    }
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

