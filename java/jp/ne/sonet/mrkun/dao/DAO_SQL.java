
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * Interface used with the DAO pattern. This interface defines
 * all the behaviours needed for database independent storage of
 * of MRKun entity objects.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DAO_SQL.java,v 1.1.1.1.2.4 2001/08/06 14:53:51 rick Exp $
 */
public interface DAO_SQL
{
  /**
   * Create a new instance of the object in persistent storage
   */
  public boolean createRecord(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

  /**
   * Retrieves an existing instance of the object from persistent storage
   */
  public Object searchRecord(Connection conn, Object searchObject, String filter)
    throws SQLException, NamingException;

  /**
   * Saves an existing instance of the object in persistent storage
   */
  public boolean updateRecord(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

  /**
   * Deletes an existing instance of the object from persistent storage
   */
  public boolean deleteRecord(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

  /**
   * Create new instances in persistent storage
   */
  public boolean createMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

  /**
   * Retrieves existing instances from persistent storage
   */
  public Object searchMultiple(Connection conn, Object searchObject, String filter)
    throws SQLException, NamingException;

  /**
   * Saves existing instances in persistent storage
   */
  public boolean updateMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

  /**
   * Deletes existing instances from persistent storage
   */
  public boolean deleteMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException;

}

 