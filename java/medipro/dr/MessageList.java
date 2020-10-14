package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro DR未読メッセージ一覧
 */
public class MessageList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if	( sm.check(request, 0) ) {
				sm.reset(request,0);

				HttpSession session = request.getSession(true);
				getDoctorName(request, session);

				// Go to the next page
				// response.sendRedirect("/medipro/dr/DrMessageList/index.html");
				response.sendRedirect("/medipro/dr/DrMessageList/index.html?" + request.getQueryString());
			} else {
				// セッションエラーの場合
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    void getDoctorName(HttpServletRequest req, HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		String	mrName		= null;
		String	mrCoName	= null;
		String	mrID		= req.getParameter("mr_id");

		try {
			MrInfoManager manager	= new MrInfoManager(conn);
			MrInfo	mrinfo		= manager.getMrInfo(mrID);
			mrName			= mrinfo.getName();
			mrCoName		= mrinfo.getCompanyName();

			session.putValue("d04_mrName", mrName);
			session.putValue("d04_mrCoName", mrCoName);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }
}
