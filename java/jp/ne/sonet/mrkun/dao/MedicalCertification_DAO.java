
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
 * This class implements the persistence mechanism for the MedicalCertification class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MedicalCertification_DAO.java,v 1.1.2.1 2001/09/11 00:33:58 rick Exp $
 */
public class MedicalCertification_DAO implements DAO_SQL
{
  final String SEARCH_MED_CERTIFICATION_SQL = "java:comp/env/sql/MedicalCertificationSearchSQL";

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
    throw new NotImplementedException();
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
    Collection returnList = new ArrayList();

    // Get Link Library Category by database
    PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_MED_CERTIFICATION_SQL));
    ResultSet rs  = selectQuery.executeQuery();

    while ( rs.next() )
    {
      MedicalCertification medCert = new MedicalCertification();
      medCert.setMedicalCertificationId(rs.getString("shikaku_cd"));
      medCert.setName(rs.getString("shikaku_name"));
      medCert.setMemo(rs.getString("shikaku_name"));
      returnList.add(medCert);
    }
    rs.close();
    selectQuery.close();
    return returnList;
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
