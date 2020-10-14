
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.framework.log.*;

/**
 * This class implements the persistence mechanism for the Importance class.
 *
 * @author M.Mizuki
 * @version $Id: Importance_DAO.java,v 1.1.2.4 2001/10/04 06:58:41 mizuki Exp $
 */
public class Importance_DAO implements DAO_SQL
{
  final String  SEARCH_IMPORTANCE_SQL       = "java:comp/env/sql/ImportanceSearchSQL";
  final String  SEARCH_IMPORTANCE_LIST_SQL  = "java:comp/env/sql/ImportanceListSearchSQL";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection conn, ResultSet rs)
    throws SQLException, NamingException, NoObjectFoundException
  {
    // Check the state of the resultset
    if (rs == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!rs.next())
      throw new NoObjectFoundException("Resultset contained no rows");
    else
    {
      Importance  importance   = new Importance();
      importance.setImportanceId(rs.getString("target_rank"));
      importance.setName(rs.getString("target_name"));
      return importance;
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
   * Create new instances in persistent storage
   */
  public boolean createRecord(Connection  conn,
                              Object      addEmail)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Select existing instances in persistent storage
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    if (searchObject == null) return null;
    if (filter.equals("importanceId"))
    {

      // Get the search parameters out of the search Importance Id
      Map     params            = (Map) searchObject;
      String  importanceId      = (String) params.get("importanceId");
      String  mrId              = (String) params.get("mrId");

      // Get Template by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_IMPORTANCE_SQL));
      selectQuery.setObject(1, importanceId);
      selectQuery.setObject(2, mrId);

      ResultSet rs  = selectQuery.executeQuery();
      Object found = populateObject(conn,rs);

      rs.close();
      selectQuery.close();

      return found;
    }
    else
      throw new RuntimeException("Invalid query filter type");
  }

  /**
   * Update existing instances in persistent storage
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Delete existing instances in persistent storage
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteContact)
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
    if (searchObject == null) return null;
    if (filter.equals("company"))
    {
      // Get the search parameters out of the search object Company
      Company           company         = (Company) searchObject;
      String            companyId       = company.getCompanyId();

      // Get Importance by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_IMPORTANCE_LIST_SQL));
      selectQuery.setObject(1, companyId);

      ResultSet rs  = selectQuery.executeQuery();
      Collection returnSet = new ArrayList();
      try
      {
        while (true)
          returnSet.add(populateObject(conn, rs));
      }
      catch (NoObjectFoundException NOF)
      { /* Don't panic - this is the end of the list */}
      rs.close();
      selectQuery.close();
      return returnSet;
    }
    else
      throw new RuntimeException("Invalid query filter type");
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
