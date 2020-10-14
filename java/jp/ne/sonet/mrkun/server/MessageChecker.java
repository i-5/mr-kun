// Copyright (c) 2001 So-net M3, Inc

package jp.ne.sonet.mrkun.server;

import java.sql.*;
import java.util.*;
import java.lang.*;
import jp.ne.sonet.mrkun.server.dbUtilConstant;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * Class name:  MessageChecker
 *  
 * This class encapsulates the logic for querying the doctor's new eDetail from 
 * MRs.  It will interface with the ConnectionHandler and access the database
 * for sql query.
 *
 * @author Damon Lok
 * @version $Id: MessageChecker.java,v 1.1.2.1 2001/12/19 01:48:15 damon Exp $ 
 */  
public class MessageChecker 
{

  // The sql query string for checking new mr waiting for doctor  
  private final String CHECK_DR_NEW_EDETAIL_SQL_1 =   
  "SELECT dr_id, count(*) mr_count FROM ( ";
  
  private final String CHECK_DR_NEW_EDETAIL_SQL_2 = 
  "SELECT  m.mr_id, m.name, c.company_name, dr_id, " + 
  "max(mess.message_header_id) unread_edetail_id, count(*) unread_edetail_count " +
  "FROM mr m, company c, ( " +
  "SELECT " +  
  "h.message_header_id, " +
  "h.to_userid dr_id, " + 
  "h.from_userid mr_id " +
  "FROM  message_header h, message_body b " +
  "WHERE  h.message_id = b.message_id " +
  "AND h.receive_status = '1' " +
  "AND h.to_userid IN ("; 
  
  private final String CHECK_DR_NEW_EDETAIL_SQL_3 = 
  ") AND h.message_kbn IN ('1', '4') " +
  "AND h.receive_timed is NULL " +
  "AND  ( (b.yuko_kigen is NULL) OR (b.yuko_kigen >= trunc(SYSDATE)) ) " + 
  ") mess " +
  "WHERE mess.mr_id = m.mr_id AND mess.dr_id IN ("; 
  
  private final String CHECK_DR_NEW_EDETAIL_SQL_4 = 
  ") AND c.company_cd = m.company_cd " +
  "GROUP BY m.mr_id, m.name, c.company_name, dr_id";
  
  private final String CHECK_DR_NEW_EDETAIL_SQL_5 =
  ") GROUP BY dr_id"; 
  
  private final String CHECK_VERSION_SQL = 
  "SELECT naiyo1 FROM constant_master WHERE constant_cd = 'DESKTOPMRKUN_CURRVER'";
  
  // Client's user mode
  private String userMode;
  
  // Client's doctor ID
  //private String drId;
  
  // Doctor's MR count
  private int mrCount;
  
  // MR List contains maps of mrName and mrCompany
  private Collection mrList;
  
  // Constructor 
  public MessageChecker() 
  {
  	mrCount = 0;
    mrList = new ArrayList();  
  }
  
  public String getUserMode()
  {
    return this.userMode;
  }

  public void setUserMode(String userMode)
  {
    this.userMode = userMode;
  }
  
  // This is the external interface and will be called by the ConnectionHandler
  public Collection checkMessage(ArrayList drList)
  { 
    return dataBaseCheckMessage(getSQLString(drList), drList);     
  }
  
  // The database access method for sql query of doctor's new eDetail
  private Collection dataBaseCheckMessage(String sqlString, ArrayList drList)
  {
  	int count = 0;
    Map mrInfo = null;
    ArrayList tempIdList = new ArrayList();
    ArrayList tempCountList = new ArrayList();
    ArrayList mrList = new ArrayList();	
  	try
    {
      // System.out.println("SQL String: " + CHECK_NEW_EDETAIL_SQL);
      Connection conn = getConnection();
      PreparedStatement searchQuery = conn.prepareStatement(sqlString); 	  
      ResultSet results = searchQuery.executeQuery();
      
      
      while (results.next())
      {
        mrInfo = new HashMap();
          
        if (userMode.equals(DesktopMRkunConstant.Single))
        {
          String temp = results.getString("name");
          if((temp.equals("")) || (temp == null))
            mrInfo.put("mrName", "");
          else 
            mrInfo.put("mrName", temp);
        
          temp = results.getString("company_name");
          if((temp.equals("")) || (temp == null))
            mrInfo.put("mrCompany", "");
          else 
            mrInfo.put("mrCompany", temp);  
              
          mrList.add(mrInfo);                 
        }
        else
        {
          tempIdList.add(results.getString("dr_id"));
          tempCountList.add(new Integer(results.getInt("mr_count")));
        }
      }
      results.close();
      conn.close();
    }
    catch(SQLException errSQL)
    {
      System.out.println(DesktopMRkunConstant.DataBase_Error + errSQL);
      throw new ApplicationError("In MessageChecker.dataBaseCheckMessage: SQLException ", errSQL);
    } 
    if (userMode.equals(DesktopMRkunConstant.Multiple))
    {
      for (int index=0; index < drList.size(); index++)
      {
        if (tempIdList.contains(drList.get(index)))
        {
          mrList.add(tempCountList.get(count));
          count++;
        }
        else
          mrList.add(new Integer(0));  
      }
      for (int index=0; index < drList.size(); index++)
      {
        System.out.println("DrId: " + (String)drList.get(index));
        System.out.println("DrId: " + (Integer)mrList.get(index));
      }
    }

    System.out.println("In MessageChecker.dataBaseCheckMessage: Database access is normal and connection has been closed.");
    return mrList; 
  }

  // This method will build and return the SQL string
  private String getSQLString(ArrayList drList)
  {
    String drIdList = "";
    for (int index=0; index < drList.size(); index++)
    {
      String drId = (String) drList.get(index);
      drIdList = drIdList + "'" + drId + "'";
      if(index != drList.size()-1)
        drIdList = drIdList + ",";
    }
    
    if (userMode.equals(DesktopMRkunConstant.Single))
    {
      System.out.println("SQL: " + CHECK_DR_NEW_EDETAIL_SQL_2 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_3 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_4);
      return(CHECK_DR_NEW_EDETAIL_SQL_2 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_3 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_4);
    }
    else
    {      
      System.out.println("SQL: " + CHECK_DR_NEW_EDETAIL_SQL_1 + CHECK_DR_NEW_EDETAIL_SQL_2 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_3 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_4 + CHECK_DR_NEW_EDETAIL_SQL_5);
      return(CHECK_DR_NEW_EDETAIL_SQL_1 + CHECK_DR_NEW_EDETAIL_SQL_2 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_3 + drIdList + CHECK_DR_NEW_EDETAIL_SQL_4 + CHECK_DR_NEW_EDETAIL_SQL_5);  
    }
  }
  
  // This method will return the JDBC connection instance
  private Connection getConnection() throws SQLException
  {
  	Connection conn = null;
    
    try
    {
      Driver driver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
      Properties properties = new Properties();
      properties.setProperty("user", dbUtilConstant.JDBC_USER);
      properties.setProperty("password", dbUtilConstant.JDBC_PASS);
      conn = driver.connect(dbUtilConstant.JDBC_LOOKUP, properties);
      return conn;
   	}
    catch(ClassNotFoundException cnfExc)
    {
      System.out.println(DesktopMRkunConstant.DataBase_Error + cnfExc);
      throw new ApplicationError("In MessageChecker.getConnection: ClassNotFoundException ", cnfExc);
    }
    catch(InstantiationException iExc)
    {
      System.out.println(DesktopMRkunConstant.DataBase_Error + iExc);
      throw new ApplicationError("In MessageChecker.getConnection: InstantiationException ", iExc);
    }
    catch(IllegalAccessException iaExc)
    {
      System.out.println(DesktopMRkunConstant.DataBase_Error + iaExc);
      throw new ApplicationError("In MessageChecker.getConnection: IllegalAccessException ", iaExc);
    }
  }
  
  // This will query the database for the current Desktop MRkun verion
  public String queryVersion() 
  {
  	String version = "";
    try
    {
      Connection conn = getConnection();
      PreparedStatement searchQuery = conn.prepareStatement(CHECK_VERSION_SQL); 	  
      ResultSet result = searchQuery.executeQuery();
      if (result == null)
      {
        result.close();
        conn.close();
        throw new NoObjectFoundException("Resultset was null");
      }
      else if (!result.next())
      {
        result.close();
        conn.close();   
        throw new NoObjectFoundException("Resultset has no data in it.");
      }
      else
      {
      	version = result.getString("naiyo1");
        result.close();
        conn.close();
        return version;
      }
    }
    catch(SQLException errSQL)
    {
      System.out.println(DesktopMRkunConstant.DataBase_Error + errSQL);
      throw new ApplicationError("In MessageChecker.queryVerion: SQLException ", errSQL);
    } 
  }
}