
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the DatabaseConstant class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DatabaseConstant_DAO.java,v 1.1.2.1 2001/08/15 07:44:42 rick Exp $
 */
public class DatabaseConstant_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/ConstantInsertSQL";
  final String  SEARCH_BY_NAME_QUERY  = "java:comp/env/sql/ConstantSearchNameSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/ConstantSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/ConstantUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/ConstantDeleteSQL";

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
      DatabaseConstant resultConst = new DatabaseConstant();

      resultConst.setConstantId(attributes.getString("constant_cd"));
      resultConst.setName(attributes.getString("name"));
      resultConst.setNaiyo1(attributes.getString("naiyo1"));
      resultConst.setNaiyo2(attributes.getString("naiyo2"));
      resultConst.setNaiyo3(attributes.getString("naiyo3"));
      return resultConst;
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
   * This method contains the logic for adding a new DatabaseConstant
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addCompany The DatabaseConstant object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addConst)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addConst instanceof DatabaseConstant))
      throw new RuntimeException("Add object is incorrect type - " + addConst.getClass());
    DatabaseConstant workingConst = (DatabaseConstant) addConst;

    // Add the company
    if (workingConst.getConstantId() == null)
      workingConst.setConstantId("" + Sequence.getNext(conn, "DatabaseConstant"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingConst.getConstantId());
    addQuery.setString(2, workingConst.getName());
    if (workingConst.getNaiyo1() == null)
      addQuery.setNull(3, Types.VARCHAR);
    else
      addQuery.setString(3, workingConst.getNaiyo1());
    if (workingConst.getNaiyo2() == null)
      addQuery.setNull(4, Types.VARCHAR);
    else
      addQuery.setString(4, workingConst.getNaiyo2());
    if (workingConst.getNaiyo3() == null)
      addQuery.setNull(5, Types.VARCHAR);
    else
      addQuery.setString(5, workingConst.getNaiyo3());
    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a DatabaseConstant
   * from the database.<br/>
   * Note: While this query may return many DatabaseConstant objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The DatabaseConstant's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("constantId"))
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
    else if (filter.equals("name"))
      queryString = getSQLStatement(SEARCH_BY_NAME_QUERY);
    else
      throw new RuntimeException("Invalid query filter type");

    PreparedStatement searchQuery = conn.prepareStatement(queryString);
    searchQuery.setObject(1, searchObject);
    ResultSet results = searchQuery.executeQuery();
    Object found = populateObject(conn, results);
    results.close();
    searchQuery.close();
    return found;
  }

  /**
   * This method contains the logic for updating an
   * existing DatabaseConstant in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveCompany The DatabaseConstant object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveConst)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveConst == null)
      return true;
    else if (!(saveConst instanceof DatabaseConstant))
      throw new RuntimeException("Save object is incorrect type - " + saveConst.getClass());
    DatabaseConstant workingConst = (DatabaseConstant) saveConst;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    saveQuery.setString(1, workingConst.getName());
    if (workingConst.getNaiyo1() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingConst.getNaiyo1());
    if (workingConst.getNaiyo2() == null)
      saveQuery.setNull(3, Types.VARCHAR);
    else
      saveQuery.setString(3, workingConst.getNaiyo2());
    if (workingConst.getNaiyo3() == null)
      saveQuery.setNull(4, Types.VARCHAR);
    else
      saveQuery.setString(4, workingConst.getNaiyo3());
    saveQuery.setString(5, workingConst.getConstantId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing Company from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteCompany The DatabaseConstant object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteConst)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteConst instanceof DatabaseConstant))
      throw new RuntimeException("Delete object is incorrect type - " + deleteConst.getClass());
    DatabaseConstant workingConst = (DatabaseConstant) deleteConst;

    // Delete the Company
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingConst.getConstantId());
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


