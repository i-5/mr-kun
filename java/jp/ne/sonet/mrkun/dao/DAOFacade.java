
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import javax.sql.*;
import javax.naming.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import jp.ne.sonet.mrkun.dao.*;
import jp.ne.sonet.mrkun.log.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * The facade class that abstracts all database access. This class
 * constructs a DAO class on the basis of the type of class you
 * are trying to save. It appends "_DAO" to the end of the class name
 * and instantiates the new class as an implementation of the DAO_SQL
 * interface. All database requests are passed off to the DAO_SQL
 * implementation.<br/>
 * <br/>
 * eg if the class you wish to save is
 * <code>jp.net.sonet.mrkun.framework.valueobject.DR</code>, this
 * class will try to load
 * <code>jp.net.sonet.mrkun.framework.valueobject.DR_DAO</code> as
 * the appropriate DAO class.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DAOFacade.java,v 1.1.1.1.2.9 2001/11/13 07:56:55 rick Exp $
 */
public class DAOFacade
{
  static final   String      JDBC_LOOKUP = "jdbc:weblogic:pool:mrkun";
  static final   String      JDBC_USER   = "mrkunUser";
  static   final   String      JDBC_PASS   = "mummamia";
  static final   String      DAO_PREFIX  = "jp.ne.sonet.mrkun.dao.";
  static final   String      DAO_SUFFIX  = "_DAO";

  private DAO_SQL     daoImpl;
  private Collection  dataContainer;

  /**
   * Constructor - loads the appropriate DAO class based on the
   * supplied argument. See class description above for details.
   */
  public DAOFacade(Class saveType)
  {
    if (saveType == null)
      throw new RuntimeException("No save type specified");
    else
    {
      // Get the class name
      StringBuffer className = new StringBuffer(saveType.getName());
      className.delete(0, className.toString().lastIndexOf('.') + 1);
      className.insert(0, DAO_PREFIX);
      className.append(DAO_SUFFIX);
      Class daoClass = null;
      try
      {
        daoClass = Class.forName(className.toString());
        daoImpl = (DAO_SQL) daoClass.newInstance();
      }
      catch (ClassNotFoundException errCNF)
      {
        throw new RuntimeException("Couldn't load DAO class. " + errCNF);
      }
      catch (InstantiationException errInst)
      {
        throw new RuntimeException("Couldn't load DAO class. " + errInst);
      }
      catch (IllegalAccessException errIA)
      {
        throw new RuntimeException("Couldn't load DAO class. " + errIA);
      }
    }
  }

  public static Connection getConnection()  throws SQLException
  {
    /*
     * This code is used when the resource factory method of obtaining
     * a datasource is used.
     *
     * Context initial = new InitialContext();
     * DataSource datasource = (DataSource) initial.lookup(JDBC_LOOKUP);
     * return datasource.getConnection();
     */
    try
    {
      Driver myDriver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
      Properties connProps = new Properties();
      connProps.setProperty("user", JDBC_USER);
      connProps.setProperty("password", JDBC_PASS);
      Connection conn = myDriver.connect(JDBC_LOOKUP, connProps);
      return conn;
    }
    catch (SQLException errSQL)
    {
      throw errSQL;
    }
    catch (Exception errOther)
    {
      throw new SQLException("Driver could not be loaded - " + errOther);
    }
  }

  /**
   * Create a new instance of the object in persistent storage
   */
  public boolean createRecord(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createRecord\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createRecord\",\"DAOGotConnection\"");
      boolean result = daoImpl.createRecord(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createRecord\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createRecord\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error creating record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Retrieves an existing instance of the object from persistent storage
   */
  public Object searchRecord(Object searchObject, String filter, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchRecord\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchRecord\",\"DAOGotConnection\"");
      Object result = daoImpl.searchRecord(jdbcConn, searchObject, filter);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchRecord\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchRecord\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error finding record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Saves an existing instance of the object in persistent storage
   */
  public boolean updateRecord(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateRecord\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateRecord\",\"DAOGotConnection\"");
      boolean result = daoImpl.updateRecord(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateRecord\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateRecord\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
      StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error saving record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Deletes an existing instance of the object from persistent storage
   */
  public boolean deleteRecord(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteRecord\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteRecord\",\"DAOGotConnection\"");
      boolean result = daoImpl.deleteRecord(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteRecord\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteRecord\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
      StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error creating record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Creates existing instances of the object in persistent storage
   */
  public boolean createMultiple(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createMultiple\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createMultiple\",\"DAOGotConnection\"");
      boolean result = daoImpl.createMultiple(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createMultiple\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"createMultiple\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error creating record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Retrieves an existing instance of the object from persistent storage
   */
  public Object searchMultiple(Object searchObject, String filter, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchMultiple\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchMultiple\",\"DAOGotConnection\"");
      Object result = daoImpl.searchMultiple(jdbcConn, searchObject, filter);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchMultiple\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"searchMultiple\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error finding record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Saves existing instances of the object in persistent storage
   */
  public boolean updateMultiple(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateMultiple\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateMultiple\",\"DAOGotConnection\"");
      boolean result = daoImpl.updateMultiple(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateMultiple\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"updateMultiple\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error updating record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

  /**
   * Deletes existing instances of the object from persistent storage
   */
  public boolean deleteMultiple(Object modifyingObject, String sessionId)
  {
    Connection jdbcConn = null;
    try
    {
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteMultiple\",\"DAOStart\"");
      jdbcConn = getConnection();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteMultiple\",\"DAOGotConnection\"");
      boolean result = daoImpl.deleteMultiple(jdbcConn, modifyingObject);
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteMultiple\",\"DAOProcessed\"");
      jdbcConn.close();
      ThreadLogger.log("\"" + sessionId + "\",\"" + daoImpl.getClass().getName() + "\",\"deleteMultiple\",\"DAOFinished\"");
      return result;
    }
    catch (NamingException errNaming)
    {
      throw new RuntimeException("Error retrieving environment settings - " + errNaming);
    }
    catch (SQLException errSQL)
    {
	    StringWriter s = new StringWriter();
      PrintWriter p = new PrintWriter(s);
      errSQL.printStackTrace(p);
      throw new RuntimeException("Error deleting record - " + s.toString());
    }
    finally
    {
      if (jdbcConn != null)
        try   {jdbcConn.close();}
        catch (SQLException errSQL) {}
    }
  }

}

