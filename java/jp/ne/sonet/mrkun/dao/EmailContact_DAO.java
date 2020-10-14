
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the EmailContact class.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: EmailContact_DAO.java,v 1.1.2.5 2001/08/07 02:03:31 rick Exp $
 */
public class EmailContact_DAO implements DAO_SQL
{
  final String  ADD_QUERY             = "java:comp/env/sql/EmailContactInsertSQL";
  final String  SEARCH_BY_NAME_QUERY  = "java:comp/env/sql/EmailContactSearchNameSQL";
  final String  SEARCH_BY_ID_QUERY    = "java:comp/env/sql/EmailContactSearchIDSQL";
  final String  SAVE_QUERY            = "java:comp/env/sql/EmailContactUpdateSQL";
  final String  REMOVE_QUERY          = "java:comp/env/sql/EmailContactDeleteSQL";

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
      EmailContact resultContact = new EmailContact();
      resultContact.setEmailContactId(attributes.getString("emailContactId"));
      resultContact.setName(attributes.getString("name"));
      resultContact.setEmailAddress(attributes.getString("emailAddress"));
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
   * This method contains the logic for adding a new EmailContact
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDR The DR object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addEmail)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(addEmail instanceof EmailContact))
      throw new RuntimeException("Add object is incorrect type - " + addEmail.getClass());
    EmailContact workingContact = (EmailContact) addEmail;

    // Update the EmailContact
    if (workingContact.getEmailContactId() == null)
      workingContact.setEmailContactId("" + Sequence.getNext(conn, "EmailContact"));
    PreparedStatement addEmailQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));
    
    addEmailQuery.setString(1, workingContact.getEmailContactId());

    if (workingContact.getName() == null)
      addEmailQuery.setNull(2, Types.VARCHAR);
    else
      addEmailQuery.setString(2, workingContact.getName());

    if (workingContact.getEmailAddress() == null)
      addEmailQuery.setNull(3, Types.VARCHAR);
    else
      addEmailQuery.setString(3, workingContact.getEmailAddress());

    int rowsAffected = addEmailQuery.executeUpdate();
    addEmailQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for retrieving an EmailContact
   * from the database.<br/>
   * Note: While this query may return many EmailContact objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param name The EmailContact's name as a filter for the query.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    // Create the query
    String queryString = "";
    if (filter.equals("emailContactId"))
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
   * existing EmailContact in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveDR The EmailContact object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
    // Check save object type
    if (saveContact == null)
      return true;
    else if (!(saveContact instanceof EmailContact))
      throw new RuntimeException("Save object is incorrect type - " + saveContact.getClass());
    EmailContact workingContact = (EmailContact) saveContact;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    if (workingContact.getName() == null)
      saveQuery.setNull(1, Types.VARCHAR);
    else
      saveQuery.setString(1, workingContact.getName());
    if (workingContact.getEmailAddress() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingContact.getEmailAddress());
    saveQuery.setString(3, workingContact.getEmailContactId());
    int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * This method contains the logic for deleting an
   * existing EmailContact from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteDR The EmailContact object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteContact)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteContact instanceof EmailContact))
      throw new RuntimeException("Delete object is incorrect type - " + deleteContact.getClass());
    EmailContact workingContact = (EmailContact) deleteContact;

    // Delete the DR
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, workingContact.getEmailContactId());
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

