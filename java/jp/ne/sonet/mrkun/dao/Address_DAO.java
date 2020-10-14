
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the Address class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Address_DAO.java,v 1.1.2.5 2001/08/07 02:03:31 rick Exp $
 */
public class Address_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/AddressInsertSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/AddressSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/AddressUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/AddressDeleteSQL";

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
      Address resultAddress = new Address();
      resultAddress.setAddressId(attributes.getString("addressId"));
      resultAddress.setAreaCode(attributes.getString("areaCode"));
      resultAddress.setLocation(attributes.getString("location"));
      return resultAddress;
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
   * This method contains the logic for adding a new Address
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addAddress The Address object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addAddress)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addAddress instanceof Address))
      throw new RuntimeException("Add object is incorrect type - " + addAddress.getClass());
    Address workingAddress = (Address) addAddress;

    // Add the company
    if (workingAddress.getAddressId() == null)
      workingAddress.setAddressId("" + Sequence.getNext(conn, "Address"));
    PreparedStatement addQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addQuery.setString(1, workingAddress.getAddressId());
    addQuery.setString(2, workingAddress.getAreaCode());
    addQuery.setString(3, workingAddress.getLocation());
    int rowsAffected = addQuery.executeUpdate();
    addQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving a Address
   * from the database.<br/>
   * Note: While this query may return many Address objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("addressId"))
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
   * existing Address in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveAddress The Address object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveAddress)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveAddress == null)
      return true;
    else if (!(saveAddress instanceof Address))
      throw new RuntimeException("Save object is incorrect type - " + saveAddress.getClass());
    Address workingAddress = (Address) saveAddress;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    saveQuery.setString(1, workingAddress.getAreaCode());
    saveQuery.setString(2, workingAddress.getLocation());
    saveQuery.setString(3, workingAddress.getAddressId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing Address from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteAddress The Address object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteAddress)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteAddress instanceof Address))
      throw new RuntimeException("Delete object is incorrect type - " + deleteAddress.getClass());
    Address workingAddress = (Address) deleteAddress;

    // Delete the Company
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingAddress.getAddressId());
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


