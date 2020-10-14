
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
 * This class implements the persistence mechanism for the ResourceLink class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @author M.Mizuki
 * @version $Id: ResourceLink_DAO.java,v 1.1.2.4 2001/08/15 14:18:48 rick Exp $
 */
public class ResourceLink_DAO implements DAO_SQL
{
  final String  SEARCH_LINK_LIBRARY_SQL       = "java:comp/env/sql/LinkSearchSQL";
  final String  SEARCH_LINK_LIBRARY_LIST_SQL  = "java:comp/env/sql/LinkListSearchSQL";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection conn, ResultSet rs)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Check the state of the resultset
    if (rs == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!rs.next())
      throw new NoObjectFoundException("Resultset contained no rows");
    else
    {
      ResourceLink link = new ResourceLink();
      link.setResourceLinkId(rs.getString("link_cd"));
      link.setName(rs.getString("honbun_text"));
      link.setDescription(rs.getString("description"));
      link.setCategory(rs.getString("link_bunrui_cd"));
      link.setURL(rs.getString("url"));
      return link;
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
    if (filter.equals("linkId"))
    {

      // Get the search parameters out of the search Link Id
      String linkid = (String) searchObject;

      // Get Link Library by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_LINK_LIBRARY_SQL));
      selectQuery.setObject(1, linkid);
      ResultSet rs  = selectQuery.executeQuery();

      // set Link List
      Object found = populateObject(conn,rs);
      rs.close();
      selectQuery.close();

      // ResourceLink Object return
      return found;
    }
    else
      throw new ApplicationError("Invalid query filter type");
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
    if (filter.equals("resourceCat"))
    {
      // Get the search parameters out of the search object ResourceCat
      ResourceCat       resourceCat     = (ResourceCat) searchObject;
      Company           company         = resourceCat.getCompany();
      String            companyId       = company.getCompanyId();
      String            resourceCatId   = resourceCat.getResourceCatId();

      // Get Link Library by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_LINK_LIBRARY_LIST_SQL));
      selectQuery.setObject(1, companyId);
      selectQuery.setObject(2, resourceCatId);

      ResultSet rs  = selectQuery.executeQuery();

      // set Link List
      Collection fullSet = new ArrayList();
      try
      {
        while (true)
          fullSet.add(populateObject(conn,rs));
      }
      catch (NoObjectFoundException errNOF)
      { /* Don't panic - we've reached the end of the set */ }

      rs.close();
      selectQuery.close();

      // ResourceLink Object list return
      return fullSet;
    }
    else
      throw new ApplicationError("Invalid query filter type");
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
