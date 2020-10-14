
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import java.util.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the DatabaseConstant class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: EDetailCategory_DAO.java,v 1.1.2.3 2001/12/20 08:59:09 rick Exp $
 */
public class EDetailCategory_DAO implements DAO_SQL
{
  final String  SEARCH_BY_COMPANY_QUERY  = "java:comp/env/sql/CategorySearchCompanySQL";
  final String  SEARCH_BY_NAME_QUERY     = "java:comp/env/sql/CategorySearchNameSQL";
  final String  SEARCH_BY_ID_QUERY       = "java:comp/env/sql/CategorySearchIDSQL";

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
      EDetailCategory resultCat = new EDetailCategory();

      resultCat.setEdetailCategoryId(attributes.getString("call_naiyo_cd"));
      resultCat.setName(attributes.getString("call_naiyo"));
      return resultCat;
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
   * This method contains the logic for adding a new EDetailCategory
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addCat The EDetailCategory object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addConst)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * This method contains the logic for retrieving a EDetailCategory
   * from the database.<br/>
   * Note: While this query may return many EDetailCategory objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The EDetailCategory's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("edetailCategoryId"))
    {
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
      PreparedStatement searchQuery = conn.prepareStatement(queryString);
      Map params = (Map) searchObject;
      String categoryId = (String) params.get("categoryId");
      String companyId = (String) params.get("companyId");
      searchQuery.setObject(1, categoryId);
      searchQuery.setObject(2, companyId);
      ResultSet results = searchQuery.executeQuery();
      Object found = populateObject(conn, results);
      results.close();
      searchQuery.close();
      return found;
    }
    else if (filter.equals("name"))
    {
      queryString = getSQLStatement(SEARCH_BY_NAME_QUERY);
      PreparedStatement searchQuery = conn.prepareStatement(queryString);
      searchQuery.setObject(1, searchObject);
      ResultSet results = searchQuery.executeQuery();
      Object found = populateObject(conn, results);
      results.close();
      searchQuery.close();
      return found;
    }
    else
      throw new ApplicationError("Invalid query filter type");

  }

  /**
   * This method contains the logic for updating an
   * existing EDetailCategory in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveObject The EDetailCategory object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * This method contains the logic for deleting an
   * existing EDetailCategory from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteObject The EDetailCategory object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
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
    // Create the query
    String queryString = "";
    if (filter.equals("companyId"))
      queryString = getSQLStatement(SEARCH_BY_COMPANY_QUERY);
    else
      throw new ApplicationError("Invalid query filter type");

    PreparedStatement searchQuery = conn.prepareStatement(queryString);
    searchQuery.setObject(1, searchObject);
    ResultSet results = searchQuery.executeQuery();
    Collection returnSet = new ArrayList();
    try
    {
      while (true)
        returnSet.add(populateObject(conn, results));
    }
    catch (NoObjectFoundException NOF)
    { /* Don't panic - this is the end of the list */}
    results.close();
    searchQuery.close();
    return returnSet;
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


