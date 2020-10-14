package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.manager.*;

/**
 * Point使用与信(Gifty2 Call)
 */
public class PointInfo extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			UpdatePoint up = new UpdatePoint( conn );

			String company_cd = request.getParameter("COMPANY_CD");
			String store_cd = request.getParameter("STORE_CD");
			String drId = request.getParameter("MEMBER_ID");
			String system_cd = request.getParameter("MEMBER_PWD");
			String point_qt = request.getParameter("POINT_QT");

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
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    /**
     * 結果を指定URLに返信する
     **/
    protected void ReturnMassage( boolean ret, String company_cd, String store_cd,
								  String drId, String system_cd, String point_qt, 
								  HttpServletResponse response ) throws IOException {

//		StringBuffer URL = new StringBuffer( "https://www2.hkr.ne.jp/SPS/spwl100S/thankyou.asp" );
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

}
