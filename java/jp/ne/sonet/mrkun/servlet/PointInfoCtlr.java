
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
// Comment out by Damon 011017: import jp.ne.sonet.mrkun.dao.DAOFacade;
import jp.ne.sonet.medipro.mr.*;
import jp.ne.sonet.mrkun.framework.servlet.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * Point使用与信(Gifty2 Call)
 */
public class PointInfoCtlr extends BaseServlet {
    public void doAction(HttpServletRequest request, HttpServletResponse response) {
	Connection conn = null;

	String company_cd = request.getParameter("COMPANY_CD");
	String store_cd = request.getParameter("STORE_CD");
	String drId = request.getParameter("MEMBER_ID");
	String system_cd = request.getParameter("MEMBER_PWD");
	String point_qt = request.getParameter("POINT_QT");

	try {
		conn = getConnection();
		UpdatePoint up = new UpdatePoint( conn );

//		response.setContentType( "text/plain; charset=iso-2022-jp" );
//		PrintWriter pw = response.getWriter();

		if( company_cd == null || company_cd.compareTo("01010002") !=0 )
			//			pw.println( "false" );
			ReturnMassage( false, company_cd, store_cd, drId, system_cd, point_qt, response );

		else if( store_cd == null || store_cd.compareTo("00000000") !=0 )
			//			pw.println( "false" );
			ReturnMassage( false, company_cd, store_cd, drId, system_cd, point_qt, response );

		else if( drId == null || system_cd == null || point_qt == null )
				//			pw.println( "false" );
			ReturnMassage( false, company_cd, store_cd, drId, system_cd, point_qt, response );

		else{
			if( up.UpdatePointExec( drId, system_cd, Integer.parseInt( point_qt ) ) )
				//				pw.println( "true" );
				ReturnMassage( true, company_cd, store_cd, drId, system_cd, point_qt, response );

			//			else	pw.println( "false" );
			else	ReturnMassage( false, company_cd, store_cd, drId, system_cd, point_qt, response );
		}

	} catch (Exception e) {
		try
		{
			ReturnMassage( false, company_cd, store_cd, drId, system_cd, point_qt, response );
		}
		catch (IOException errIO) {}
	} finally {
		try
		{
			if (conn != null)
				conn.close();
		}
		catch (SQLException errSQL) {}
	}
    }
    /**
     * 結果を指定URLに返信する
     **/
    protected void ReturnMassage( boolean ret, String company_cd, String store_cd,
				  String drId, String system_cd, String point_qt, 
				  HttpServletResponse response ) throws IOException {

//change 2001.11.02 M.Mizuki	StringBuffer URL = new StringBuffer( "https://www2.hkr.ne.jp/SPS/spwl100S/thankyou.asp" );
	StringBuffer URL = new StringBuffer( "https://www.pgps.net/spwl100S/thankyou.asp" );

	if( ret ){
		URL.append( "?COMPANY_CD=" + company_cd + "&STORE_CD=" + store_cd +
				"&MEMBER_ID=" + drId + "&MEMBER_PWD=" + system_cd +
				"&POINT_QT=" + point_qt );
	} else {
		URL.append( "?COMPANY_CD=" + company_cd + "&STORE_CD=" + store_cd +
				"&MEMBER_ID=" + drId + "&MEMBER_PWD=" + system_cd +
				"&POINT_QT=0" );
	}

	response.sendRedirect( URL.toString() );
  }

  // This method will return the JDBC connection instance - Added by Damon 011015
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
      throw new ApplicationError("In MessageHelperManager.getConnection: ClassNotFoundException", cnfExc);
    }
    catch(InstantiationException iExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: InstantiationException", iExc);
    }
    catch(IllegalAccessException iaExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: IllegalAccessException", iaExc);
    }
  }   
}
