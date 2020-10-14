
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
 * This class implements the persistence mechanism for the ResourceCat class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @author M.Mizuki
 * @version $Id: ResourceCat_DAO.java,v 1.1.2.3 2001/08/14 14:15:02 rick Exp $
 */
public class ResourceCat_DAO implements DAO_SQL
{
  final String  SEARCH_LINK_CATEGORY_SQL      = "java:comp/env/sql/LinkCategorySearchSQL";

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
    if (!(searchObject instanceof Company))
	    throw new RuntimeException("Invalid searchObject type - " + searchObject.getClass() +". SearchObject should be in MR type.");
    else
    {
      // Get the search parameters out of the search object company
      Company     company     = (Company) searchObject;
      String      company_cd  = company.getCompanyId();
      Collection  returnList  = new ArrayList();

      // Get Link Library Category by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_LINK_CATEGORY_SQL));
      selectQuery.setObject(1, company_cd);

      ResultSet rs  = selectQuery.executeQuery();

      DAO_SQL daoResourceLink  = new ResourceLink_DAO();

      while ( rs.next() )
      {
        ResourceCat linkcat = new ResourceCat();
        linkcat.setResourceCatId(rs.getString("link_bunrui_cd"));
        linkcat.setName(rs.getString("bunrui_name"));
        linkcat.setDescription("");
        linkcat.setCompany(company);
        linkcat.setResourceList((Collection) daoResourceLink.searchMultiple(conn, linkcat, "resourceCat"));
	      returnList.add(linkcat);
      }
      rs.close();
      selectQuery.close();
      return returnList;
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
