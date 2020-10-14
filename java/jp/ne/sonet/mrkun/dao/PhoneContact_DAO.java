
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the PhoneContact class.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: PhoneContact_DAO.java,v 1.1.2.5 2001/08/07 02:03:31 rick Exp $
 */
public class PhoneContact_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/PhoneContactInsertSQL";
  final String  SEARCH_BY_NAME_QUERY  = "java:comp/env/sql/PhoneContactSearchNameSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/PhoneContactSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/PhoneContactUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/PhoneContactDeleteSQL";

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
      PhoneContact resultContact = new PhoneContact();
      resultContact.setPhoneContactId(attributes.getString("phoneContactId"));
      resultContact.setName(attributes.getString("name"));
      resultContact.setPhoneNumber(attributes.getString("phoneNumber"));
      return resultContact;
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
   * This method contains the logic for adding a new PhoneContact
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDR The PhoneContact object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addPhone)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addPhone instanceof PhoneContact))
      throw new RuntimeException("Add object is incorrect type - " + addPhone.getClass());
    PhoneContact workingContact = (PhoneContact) addPhone;

    // add the PhoneContact
    if (workingContact.getPhoneContactId() == null)
      workingContact.setPhoneContactId("" + Sequence.getNext(conn, "PhoneContact"));
    PreparedStatement addPhoneQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    addPhoneQuery.setString(1, workingContact.getPhoneContactId());
    addPhoneQuery.setString(2, workingContact.getName());
    addPhoneQuery.setString(3, workingContact.getPhoneNumber());
    int rowsAffected = addPhoneQuery.executeUpdate();
    addPhoneQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving an PhoneContact
   * from the database.<br/>
   * Note: While this query may return many PhoneContact objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param searchObject The filter for the query.
   * @param filter The type of filter to be applied eg Id, name, etc
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("phoneContactId"))
      queryString = getSQLStatement(SEARCH_BY_ID_QUERY);
    else if (filter.equals("name"))
      queryString = getSQLStatement(SEARCH_BY_NAME_QUERY);
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
   * existing PhoneContact in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveContact The PhoneContact object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveContact == null)
      return true;
    else if (!(saveContact instanceof PhoneContact))
      throw new RuntimeException("Save object is incorrect type - " + saveContact.getClass());
    PhoneContact workingContact = (PhoneContact) saveContact;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));


    if (workingContact.getName() == null)
      saveQuery.setNull(1, Types.VARCHAR);
    else
      saveQuery.setString(1, workingContact.getName());

    if (workingContact.getPhoneNumber() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingContact.getPhoneNumber());

    saveQuery.setString(3, workingContact.getPhoneContactId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing PhoneContact from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteContact The PhoneContact object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteContact)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteContact instanceof PhoneContact))
      throw new RuntimeException("Delete object is incorrect type - " + deleteContact.getClass());
    PhoneContact workingContact = (PhoneContact) deleteContact;

    // Delete the DR
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingContact.getPhoneContactId());
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

