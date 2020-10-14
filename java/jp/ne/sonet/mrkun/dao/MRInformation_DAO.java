
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the MRInformation class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MRInformation_DAO.java,v 1.1.2.5 2001/08/07 02:03:31 rick Exp $
 */
public class MRInformation_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/MRInformationInsertSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/MRInformationSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/MRInformationUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/MRInformationDeleteSQL";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(ResultSet attributes)
    throws SQLException
  {
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
      throw new NoObjectFoundException("Resultset contained no rows");
    else
    {
      MRInformation resultMRI = new MRInformation();
      resultMRI.setMrId(attributes.getString("mrId"));
      resultMRI.setMrPreference(attributes.getString("mrPreference"));
      resultMRI.setMemo(attributes.getString("memo"));
      return resultMRI;
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
   * This method contains the logic for adding a new MRInformation
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addMRI The MRInformation object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addMRI)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addMRI instanceof MRInformation))
      throw new RuntimeException("Add object is incorrect type - " + addMRI.getClass());
    MRInformation workingMRI = (MRInformation) addMRI;

    // Add the MRInfo
    if (workingMRI.getMrId() == null)
      workingMRI.setMrId("" + Sequence.getNext(conn, "MRInformation"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingMRI.getMrId());
    addQuery.setString(2, workingMRI.getMrPreference());
    addQuery.setString(3, workingMRI.getMemo());
    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a MRInformation
   * from the database.<br/>
   * Note: While this query may return many MRInformation objects
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
    if (filter.equals("mrId"))
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
    else
      throw new RuntimeException("Invalid query filter type");

    PreparedStatement searchQuery = conn.prepareStatement(queryString);
    searchQuery.setObject(1, searchObject);
    ResultSet results = searchQuery.executeQuery();
    Object found = populateObject(results);
    results.close();
    searchQuery.close();
    return found;
  }

  /**
   * This method contains the logic for updating an
   * existing MRInformation in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveMRI The MRInformation object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveMRI)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveMRI == null)
      return true;
    else if (!(saveMRI instanceof MRInformation))
      throw new RuntimeException("Save object is incorrect type - " + saveMRI.getClass());
    MRInformation workingMRI = (MRInformation) saveMRI;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    saveQuery.setString(1, workingMRI.getMrPreference());
    saveQuery.setString(2, workingMRI.getMemo());
    saveQuery.setString(3, workingMRI.getMrId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing MRInformation from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteMRI The MRInformation object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteMRI)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteMRI instanceof MRInformation))
      throw new RuntimeException("Delete object is incorrect type - " + deleteMRI.getClass());
    MRInformation workingMRI = (MRInformation) deleteMRI;

    // Delete the Company
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingMRI.getMrId());
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


