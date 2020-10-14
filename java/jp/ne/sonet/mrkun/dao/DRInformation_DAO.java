
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import java.util.*;

/**
 * This class implements the persistence mechanism for the DRInformation class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DRInformation_DAO.java,v 1.1.2.13 2001/10/04 06:58:41 mizuki Exp $
 */
public class DRInformation_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/DRInformationInsertSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/DRInformationSearchIDSQL";
  final String  SEARCH_BY_MR_QUERY    = "java:comp/env/sql/DRInformationSearchMRSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/DRInformationUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/DRInformationDeleteSQL";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection conn, ResultSet attributes)
    throws SQLException, NamingException
  {
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
      throw new NoObjectFoundException("Resultset contained no rows");
    else
    {
      DRInformation resultDRI = new DRInformation();
      resultDRI.setDrId(attributes.getString("dr_id"));
      resultDRI.setName(attributes.getString("name"));
      resultDRI.setHospital(attributes.getString("kinmusaki"));
      resultDRI.setClientId(attributes.getString("maker_cust_id"));
      resultDRI.setOccupation(attributes.getString("syokusyu"));
      resultDRI.setDivision(attributes.getString("senmon1"));
      resultDRI.setPosition(attributes.getString("yakusyoku"));
      resultDRI.setGraduatedUniversity(attributes.getString("sotsugyo_daigaku"));
      resultDRI.setHobby(attributes.getString("syumi"));
      resultDRI.setYearOfGraduation(attributes.getString("sotsugyo_year"));
      resultDRI.setMemo(attributes.getString("sonota"));
      resultDRI.setHospitalId(attributes.getString("maker_shisetsu_id"));
      resultDRI.setMrId(attributes.getString("mr_id"));

      // get Importance
      DAO_SQL daoImportance   = new Importance_DAO();
      String importanceId = attributes.getString("target_rank");

      if( importanceId == null ){
        Importance tempImportance = new Importance();
        tempImportance.setImportanceId("");
        if(tempImportance.getImportanceId() == null)
        {System.out.println("Null pointer");}
        tempImportance.setName("");
        resultDRI.setImportance(tempImportance);
      }else{
        Map params = new Hashtable();
        params.put("importanceId", importanceId);
        params.put("mrId", resultDRI.getMrId());
        Importance resultImportance = (Importance) daoImportance.searchRecord(conn, params, "importanceId");
        resultDRI.setImportance(resultImportance);
      }

      return resultDRI;
    }
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
   * This method contains the logic for adding a new DRInformation
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDRI The DRInformation object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addDRI)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addDRI instanceof DRInformation))
      throw new RuntimeException("Add object is incorrect type - " + addDRI.getClass());
    DRInformation workingDRI = (DRInformation) addDRI;

    // Add the DRInfo
    if (workingDRI.getDrId() == null)
      workingDRI.setDrId("" + Sequence.getNext(conn, "DRInformation"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingDRI.getMrId());
    addQuery.setString(2, workingDRI.getDrId());
    addQuery.setString(3, workingDRI.getName());
    addQuery.setString(4, workingDRI.getHospital());
    addQuery.setString(5, workingDRI.getClientId());
    addQuery.setString(6, workingDRI.getImportance().getImportanceId());
    addQuery.setString(7, workingDRI.getOccupation());
    addQuery.setString(8, workingDRI.getDivision());
    addQuery.setString(9, workingDRI.getPosition());
    addQuery.setString(10, workingDRI.getGraduatedUniversity());
    addQuery.setString(11, workingDRI.getHobby());
    addQuery.setString(12, workingDRI.getYearOfGraduation());
    addQuery.setString(13, workingDRI.getMemo());
    addQuery.setString(14, workingDRI.getHospitalId());
    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a DRInformation
   * from the database.<br/>
   * Note: While this query may return many DRInformation objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param searchObject The value to be filtered for
   * @param filter The field to filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("drId"))
    {
      Map filterValues = (Map) searchObject;
      String drId = (String) filterValues.get("drId");
      String mrId = (String) filterValues.get("mrId");
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
      PreparedStatement searchQuery = conn.prepareStatement(queryString);
      searchQuery.setObject(1, drId);
      searchQuery.setObject(2, mrId);
      ResultSet results = searchQuery.executeQuery();
      Object found = populateObject(conn,results);
      results.close();
      searchQuery.close();
      return found;
    }
    else if (filter.equals("mrId"))
    {
      String mrId = (String) searchObject;
      queryString = getSQLStatement(SEARCH_BY_MR_QUERY);
      PreparedStatement searchQuery = conn.prepareStatement(queryString);
      searchQuery.setObject(1, mrId);
      searchQuery.setObject(2, mrId);
      ResultSet  results = searchQuery.executeQuery();
      Collection returnSet = new ArrayList();
      try
      {
        while (true)
          returnSet.add(populateObject(conn,results));
      }
      catch (NoObjectFoundException errNOF)
      { /* Don't panic ... this is the end of the list */ }
      results.close();
      searchQuery.close();
      return returnSet;
    }
    else
      throw new RuntimeException("Invalid query filter type");
  }

  /**
   * This method contains the logic for updating an
   * existing DRInformation in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveDRI The DRInformation object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveDRI)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveDRI == null)
      return true;
    else if (!(saveDRI instanceof DRInformation))
      throw new RuntimeException("Save object is incorrect type - " + saveDRI.getClass());
    DRInformation workingDRI = (DRInformation) saveDRI;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    saveQuery.setString(1, workingDRI.getName());
    saveQuery.setString(2, workingDRI.getHospital());
    saveQuery.setString(3, workingDRI.getClientId());
    saveQuery.setString(4, workingDRI.getImportance().getImportanceId());
    saveQuery.setString(5, workingDRI.getOccupation());
    saveQuery.setString(6, workingDRI.getDivision());
    saveQuery.setString(7, workingDRI.getPosition());
    saveQuery.setString(8, workingDRI.getGraduatedUniversity());
    saveQuery.setString(9, workingDRI.getHobby());
    saveQuery.setString(10, workingDRI.getYearOfGraduation());
    saveQuery.setString(11, workingDRI.getMemo());
    saveQuery.setString(12, workingDRI.getHospitalId());
    saveQuery.setString(13, workingDRI.getDrId());
    saveQuery.setString(14, workingDRI.getMrId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing DRInformation from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteDRI The DRInformation object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteDRI)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteDRI instanceof DRInformation))
      throw new RuntimeException("Delete object is incorrect type - " + deleteDRI.getClass());
    DRInformation workingDRI = (DRInformation) deleteDRI;

    // Delete the Company
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingDRI.getDrId());
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
    throw new NotImplementedException();
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
