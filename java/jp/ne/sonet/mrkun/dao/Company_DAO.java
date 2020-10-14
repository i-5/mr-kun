
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the Company class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Company_DAO.java,v 1.1.2.10 2001/09/14 02:14:56 damon Exp $
 */
public class Company_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/CompanyInsertSQL";
  final String  SEARCH_BY_NAME_QUERY  = "java:comp/env/sql/CompanySearchNameSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/CompanySearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/CompanyUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/CompanyDeleteSQL";

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
      Company resultCompany = new Company();
      DAO_SQL daoWebImage = new WebImage_DAO();

      resultCompany.setCompanyId(attributes.getString("company_cd"));
      resultCompany.setName(attributes.getString("company_name"));
      resultCompany.setCompanyPrefix(attributes.getString("cd_prefix"));
      resultCompany.setURL(attributes.getString("url"));
      resultCompany.setWmEmailAddress(attributes.getString("wm_mail_address"));
	  resultCompany.setDisplayRanking(attributes.getString("display_ranking"));

      // Load the defaultImage object
      String defaultImageID = attributes.getString("picture_cd");
      if (defaultImageID != null)
      {
        WebImage resultImage = (WebImage) daoWebImage.searchRecord(conn, defaultImageID, "webImageId");
        resultCompany.setDefaultImage(resultImage);
      }
      
      return resultCompany;
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
   * This method contains the logic for adding a new EmailContact
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addCompany The Company object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addCompany)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addCompany instanceof Company))
      throw new RuntimeException("Add object is incorrect type - " + addCompany.getClass());
    Company workingCompany = (Company) addCompany;

    // Add the company
    if (workingCompany.getCompanyId() == null)
      workingCompany.setCompanyId("" + Sequence.getNext(conn, "Company"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingCompany.getCompanyId());
    addQuery.setString(2, workingCompany.getName());
    addQuery.setObject(3, workingCompany.getDefaultImage());
    addQuery.setString(4, workingCompany.getCompanyPrefix());
    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a Company
   * from the database.<br/>
   * Note: While this query may return many Company objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The Company's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("companyId"))
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
   * existing Company in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveCompany The Company object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveCompany)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveCompany == null)
      return true;
    else if (!(saveCompany instanceof Company))
      throw new RuntimeException("Save object is incorrect type - " + saveCompany.getClass());
    Company workingCompany = (Company) saveCompany;

    // Save the WebImage
    DAO_SQL daoWebImage = new WebImage_DAO();
    if (!daoWebImage.updateRecord(conn, workingCompany.getDefaultImage()))
      throw new RuntimeException("Error saving Company defaultImage");

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    saveQuery.setString(1, workingCompany.getName());
    if (workingCompany.getDefaultImage() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingCompany.getDefaultImage().getWebImageId());
    if (workingCompany.getCompanyPrefix() == null)
      saveQuery.setNull(3, Types.VARCHAR);
    else
      saveQuery.setString(3, workingCompany.getCompanyPrefix());
    saveQuery.setString(3, workingCompany.getCompanyId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing Company from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteCompany The Company object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteCompany)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteCompany instanceof Company))
      throw new RuntimeException("Delete object is incorrect type - " + deleteCompany.getClass());
    Company workingCompany = (Company) deleteCompany;

    // Delete the Company
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingCompany.getCompanyId());
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

 