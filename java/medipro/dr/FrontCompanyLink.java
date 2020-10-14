package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;

/**
 * Medipro DRフロント入口
 */
public class FrontCompanyLink extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			// DB Connection
			DBConnect dbconn = new DBConnect();
			Connection conn  = dbconn.getDBConnect();

			String linkUrl		= request.getParameter("url");
			String mrid		= request.getParameter("mrid");
			String pict		= request.getParameter("pict");
			String drid		= request.getParameter("drid");

			try {
				TotatsuCallLogTableManager manager
					= new TotatsuCallLogTableManager (conn);
				TotatsuCallLogTable calltbl = new TotatsuCallLogTable();
				// Set Parameter
				calltbl.setFromUserID(mrid);
				calltbl.setToUserID(drid);
				calltbl.setPictureCD(pict);
				// 到達コールログテーブル Insert
				manager.insert(calltbl);
			} finally {
				dbconn.closeDB(conn);
			}

			// Go to the next page
			response.sendRedirect(linkUrl);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

}
