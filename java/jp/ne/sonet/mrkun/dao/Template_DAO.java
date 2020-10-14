
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
 * This class implements the persistence mechanism for the Template class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @author M.Mizuki
 * @version $Id: Template_DAO.java,v 1.1.2.3 2001/08/16 13:26:26 mizuki Exp $
 */
public class Template_DAO implements DAO_SQL
{
  final String  SEARCH_TEMPLATE_SQL       = "java:comp/env/sql/TemplateSearchSQL";
  final String  SEARCH_TEMPLATE_LIST_SQL  = "java:comp/env/sql/TemplateListSearchSQL";

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
      Template  template   = new Template();
      template.setTemplateId(rs.getString("teikeibun_cd"));
      template.setTitle(rs.getString("title"));
      template.setBody(rs.getString("honbun"));
      template.setDescription(rs.getString("description"));
      template.setCategory(rs.getString("teikeibun_bunrui_cd"));
      return template;
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
    if (filter.equals("templateId"))
    {

      // Get the search parameters out of the search Template Id
      String        templateId    = (String) searchObject;

      // Get Template by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_TEMPLATE_SQL));
      selectQuery.setObject(1, templateId);

      ResultSet rs  = selectQuery.executeQuery();
      Object found = populateObject(conn,rs);

      rs.close();
      selectQuery.close();

      // Template Object return
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
    if (filter.equals("templateCategory"))
    {
      // Get the search parameters out of the search object TemplateCategory
      TemplateCategory  templateCat     = (TemplateCategory) searchObject;
      Company           company         = templateCat.getCompany();
      String            companyId       = company.getCompanyId();
      String            templateCatId   = templateCat.getTemplateCategoryId();

      // Get Template by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_TEMPLATE_LIST_SQL));
      selectQuery.setObject(1, companyId);
      selectQuery.setObject(2, templateCatId);

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
