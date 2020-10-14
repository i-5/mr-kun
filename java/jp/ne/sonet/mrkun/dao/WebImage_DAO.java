
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the WebImage class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: WebImage_DAO.java,v 1.1.2.10 2001/08/23 09:55:10 rick Exp $
 */
public class WebImage_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/WebImageInsertSQL";
  final String  SEARCH_BY_NAME_QUERY  = "java:comp/env/sql/WebImageSearchNameSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/WebImageSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/WebImageUpdateSQL";
  final String  SEARCH_BY_MRID_QUERY  = "java:comp/env/sql/WebImageSearchMRIDSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/WebImageDeleteSQL";

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
      WebImage resultWebImage = new WebImage();
      resultWebImage.setWebImageId(attributes.getString("picture_cd"));
      resultWebImage.setName(attributes.getString("picture_name"));
      resultWebImage.setImageType(attributes.getString("picture_type"));
      resultWebImage.setDescription(attributes.getString("jikosyokai"));
//hb010821: url is not the actual URL      resultWebImage.setFileName(attributes.getString("url"));
	resultWebImage.setFileName(attributes.getString("picture")); // hb010821: this holds the relative URL
      //resultWebImage.setContent(attributes.getBytes("content"));
      return resultWebImage;
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
   * This method contains the logic for adding a new WebImage
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addWebImage The WebImage object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addWebImage)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addWebImage instanceof WebImage))
      throw new RuntimeException("Add object is incorrect type - " + addWebImage.getClass());
    WebImage workingWebImage = (WebImage) addWebImage;

    // Add the company
    if (workingWebImage.getWebImageId() == null)
      workingWebImage.setWebImageId("" + Sequence.getNext(conn, "WebImage"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingWebImage.getWebImageId());
    if (workingWebImage.getName() == null)
      addQuery.setNull(2, Types.VARCHAR);
    else
      addQuery.setString(2, workingWebImage.getName());
    if (workingWebImage.getImageType() == null)
      addQuery.setNull(3, Types.VARCHAR);
    else
      addQuery.setString(3, workingWebImage.getImageType());
    if (workingWebImage.getDescription() == null)
      addQuery.setNull(4, Types.VARCHAR);
    else
      addQuery.setString(4, workingWebImage.getDescription());
    if (workingWebImage.getFileName() == null)
      addQuery.setNull(5, Types.VARCHAR);
    else
      addQuery.setString(5, workingWebImage.getFileName());
    if (workingWebImage.getName() == null)
      addQuery.setNull(6, Types.VARCHAR);
    else
      addQuery.setBytes(6, workingWebImage.getContent());


    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a WebImage
   * from the database.<br/>
   * Note: While this query may return many WebImage objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The WebImage's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    if (searchObject == null)
      return null;
    // Create the query
    String queryString = "";
    if (filter.equals("webImageId"))
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
    else if (filter.equals("name"))
      queryString = getSQLStatement(SEARCH_BY_NAME_QUERY);
    else if (filter.equals("mrId"))
      queryString = getSQLStatement(SEARCH_BY_MRID_QUERY);
    else
      throw new RuntimeException("Invalid query filter type");

    PreparedStatement searchQuery = conn.prepareStatement(queryString);
    searchQuery.setObject(1, searchObject);
    ResultSet results = searchQuery.executeQuery();
    Object found = null;
    if (filter.equals("mrId"))
    {
      // Loop through the results trapping a not found exception
      Collection imageList = new ArrayList();
      try
      {
        while (true)
          imageList.add(populateObject(results));
      }
      catch (NoObjectFoundException errNOF)
      { /* Don't panic - we expect this when we hit the end of the list */ }
      found = imageList;
    }
    else
    {
      found = populateObject(results);
    }
    results.close();
    searchQuery.close();
    return found;
  }

  /**
   * This method contains the logic for updating an
   * existing Company in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveWebImage The WebImage object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveWebImage)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveWebImage == null)
      return true;
    else if (!(saveWebImage instanceof WebImage))
      throw new RuntimeException("Save object is incorrect type - " + saveWebImage.getClass());
    WebImage workingWebImage = (WebImage) saveWebImage;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    if (workingWebImage.getName() == null)
      saveQuery.setNull(1, Types.VARCHAR);
    else
      saveQuery.setString(1, workingWebImage.getName());
    if (workingWebImage.getImageType() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingWebImage.getImageType());
    if (workingWebImage.getDescription() == null)
      saveQuery.setNull(3, Types.VARCHAR);
    else
      saveQuery.setString(3, workingWebImage.getDescription());
    if (workingWebImage.getFileName() == null)
      saveQuery.setNull(4, Types.VARCHAR);
    else
      saveQuery.setString(4, workingWebImage.getFileName());
    if (workingWebImage.getContent() == null)
      saveQuery.setNull(5, Types.VARCHAR);
    else
      saveQuery.setBytes(5, workingWebImage.getContent());

    saveQuery.setString(6, workingWebImage.getWebImageId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing WebImage from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteWebImage The WebImage object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteWebImage)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteWebImage instanceof WebImage))
      throw new RuntimeException("Delete object is incorrect type - " + deleteWebImage.getClass());
    WebImage workingWebImage = (WebImage) deleteWebImage;

    // Delete the WebImage
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingWebImage.getWebImageId());
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


