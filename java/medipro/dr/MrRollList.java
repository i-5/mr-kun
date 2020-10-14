package medipro.dr;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro DRMR名簿
 */
public class MrRollList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "sentaku_kbn";
		String order = "A";
		String name_order = "A";
		String company_order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if (sm.check(request, 0)) {
				HttpSession session = request.getSession(true);
				Enumeration keys  = request.getParameterNames();

				if (request.getParameter("init") != null) {
					session.removeValue("name_order");
					session.removeValue("company_order");
					session.removeValue("order");
					session.removeValue("sort");
				}
	
				//消されると困るので取っておく
//  				if (keys.hasMoreElements()) {
					sort    = (String)session.getValue("sort");
					name_order    = (String)session.getValue("name_order");
					company_order = (String)session.getValue("company_order");
					
					sort = sort == null ? "sentaku_kbn" : sort;
					name_order = name_order == null ? "A" : name_order;
					company_order = company_order == null ? "A" : company_order;

//  					if (session.getValue("sort") == null){
//  						sort    = "sentaku_kbn";
//  					} else {
//  						sort    = (String)session.getValue("sort");
//  					}
//  					if (session.getValue("name_order") == null) {
//  						name_order = "A";
//  					} else {
//  						name_order    = (String)session.getValue("name_order");
//  					}
//  					if (session.getValue("company_order") == null) {
//  						company_order = "A";
//  					} else {
//  						company_order = (String)session.getValue("company_order");
//  					}
//  				}

				// reset of session
				sm.reset(request,0);

				// 更新処理(確認ボタンが押されたとき）
				session.putValue("updateconf"," ");

				if (request.getParameter("submitname") != null) {
					if (request.getParameter("submitname").equals("Update") ) {
						// ボタンが押されてるかどうか確認
						//if	( inputCheck(request) ) {
						session.putValue("updateconf","ON");
						// 更新する
						mrUpdate(request,response,session);
						//}
					}
				}

				// set of sort
				setSort(request,
						session,
						sort,
						order,
						name_order,
						company_order);

				// Go to the next page
				response.sendRedirect("/medipro/dr/DrMrRollList/index.html?" + request.getQueryString());
			}
			else {
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

    void mrUpdate(HttpServletRequest req,
				  HttpServletResponse res,
				  HttpSession session) {
		String leftid	 = null;
		String rightid	 = null;
		String drid	 = (String)session.getValue("com_drid");
		Enumeration enum = null;

		if ( req.getParameter("liftmrID") != null ) {
			leftid = req.getParameter("liftmrID");
		}
		if ( req.getParameter("rightmrID") != null ) {
			rightid = req.getParameter("rightmrID");
		}
		// 右と左のMRのIDが一緒ではないことを確認、同じだったら右をすて
		if (leftid != null) {
			if (leftid.equals(rightid)) {
				rightid = " ";
			}
		}
	
		// DB connection
		DBConnect dbconn 	= new DBConnect();
		Connection conn		= dbconn.getDBConnect();

		try {
			TantoInfoManager tantomanager = new TantoInfoManager(conn);
			tantomanager.updateSentaku( drid, leftid, rightid );
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}
    }
 
    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String sort,
				 String order,
				 String name_order,
				 String company_order) {

		if (req.getParameter("sort") != null) {
			sort = req.getParameter("sort");

			if (sort.equals("name")) {
				name_order      = (name_order.equals("A"))? "D":"A";
				order           = name_order;
			} else if (sort.equals("company_name")) {
				company_order   = (company_order.equals("A"))? "D":"A";
				order           = company_order;
			}
		}
		session.putValue("name_order", name_order);
		session.putValue("company_order", company_order);
		session.putValue("order", order);
		session.putValue("sort", sort);
    }
}
