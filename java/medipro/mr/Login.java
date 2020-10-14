package medipro.mr;

/**
* Medipro MRログイン
*/

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;


public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			boolean nextflg = false;
			String nextpage = null;
			String submit = (String)request.getParameter("submitaction");
			String mrName = "";

			HttpSession session = request.getSession(true);
			// 念のためクリア
			session.removeValue("mr_login_err");
			session.removeValue("mr_pwdchg_err");
			session.removeValue("login_mrid");

			if (submit.equals("login")) {
				//ログイン時
				if (loginCheck(request, conn)) {
					mrName = (String)session.getValue("com_mrname");
					nextflg  = true;
				} else {
					nextflg  = false;
					session.putValue("mr_login_err", "pwderror");
				}
			} else if (submit.equals("passchg")) {
				//パスワード変更時
				if (passwdChange(request, conn)) {
					nextflg  = true;
				} else {
					nextflg  = false;
					session.putValue("mr_pwdchg_err", "pwderror");
				}
			}

			if (nextflg) {
				// ログイン時間の更新
				loginUpdate(request, conn);
		
				/**************************************************************
				 * MRのuser-agentをloginmrテーブルに保存する 1212 y-yamada add
				 ***************************************************************/
				String mrId = request.getParameter("com_mrid");
				if( mrId != null ) {
					new AccessMrAnalyzer(mrId).analyze(request);
				}
                                
                                /* <hb010703
                                 * maybe one day I will understand this...
                                 *
				 * session check & reset
				if (session.isNew() == false) {
					 session.invalidate();
					session = request.getSession(true);
				}
                                 * hb010703> 
                                 */
				// セッション有効時間設定
				int session_time = Integer.parseInt(getConstTable("TIMEOUT", 2));
				session.setMaxInactiveInterval(session_time);

				session.putValue("com_mrname", mrName);

				initSessionParameter(session);

				// set mrID 
				session.putValue("com_mrid", request.getParameter("com_mrid"));

				// Cookie Set
				Cookie my_cookie = new Cookie("Mrid", mrId);

				// 10日間有効
				my_cookie.setMaxAge(864000);
				my_cookie.setPath("/");
				response.addCookie(my_cookie);

				// 2001.04.24 追加 -->
				// sessionにキーをhiddenとして埋め込む
				String sessionId = HtmlTagUtil.getRandomId();
				session.putValue(SysCnst.MR_SESSION_ID, sessionId);

//  		nextpage = "/medipro.mr.DrList";
				nextpage = "/medipro.mr.ReceiveList?ID=" + mrId;
				// <-- 2001.04.24
			} else {
				// セッションに登録
				session.putValue("login_mrid", request.getParameter("com_mrid"));
				nextpage = "/medipro/mr/MrLogin/index.jsp";
			}

			// Go to the next page
			response.sendRedirect(nextpage);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }

    /**
     * IDとpasswordの組み合わせがDBに登録されているかチェックする.
     * 登録されていれば、セッションにMR氏名"com_mrname"を登録する.
     */
    boolean loginCheck(HttpServletRequest req, Connection conn) {
		boolean  ret = true;
		String  mrid = req.getParameter("com_mrid");
		String  passwd = req.getParameter("passwd");

		MrInfoManager manager = new MrInfoManager(conn);
		MrInfo  mrinfo = manager.getMrLoginInfo(mrid);

		if (mrinfo == null) {
			ret = false;
		} else {
			String mrPwd = mrinfo.getPassword();

			if (passwd.equals(mrPwd)) {
				HttpSession session = req.getSession(true);
				session.putValue("com_mrname", mrinfo.getName());
				ret = true;
			} else {
				ret = false;
			}
		}

		return ret;
    }

    /**
     * passwordを変更する.
     */
    boolean passwdChange(HttpServletRequest req, Connection conn) {
		String  mrid = req.getParameter("com_mrid");
		String  passwd1 = req.getParameter("newPasswd1");
		boolean  ret = true;

		if (loginCheck(req, conn)) {
			MrInfoManager manager = new MrInfoManager(conn);
			MrInfo  mrinfo = manager.getMrInfo(mrid);
			mrinfo.setPassword(passwd1);
			manager.updatePassword(mrid, mrinfo);
			ret = true;
		} else {
			ret = false;
		}
		return ret;
    }

    /**
     * 定数マスタから、指定したコードの指定したからむの内容を取得する.
     */
    String getConstTable(String keyword, int colno) {
		String value = null;

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager = new ConstantMasterTableManager(conn);
			ConstantMasterTable constmanager = manager.getConstantMasterTable(keyword);

			if (colno == 1) {
				value = constmanager.getNaiyo1().trim();
			} else {
				value = constmanager.getNaiyo2().trim();
			}
		} finally {
			dbconn.closeDB(conn);
		}

		return value;
    }

    /**
     * 前回ログイン時から、担当を外れた医師一覧を取得します.
     * @param  req  HttpServletRequestインスタンス
     * @param  conn コネクション
     * @return 医師名一覧
     */
//      Vector getRelationEndDoctorList(HttpServletRequest req, Connection conn) {
//  	String mrId = req.getParameter("com_mrid");
//  	TantoInfoManager manager = new TantoInfoManager(conn);
//  	Vector list = manager.getEndRelationList(mrId);
//  	return list;
//      }

    /**
     * 前回ログイン時から、未読から既読に変化したメッセージ一覧を取得します.
     * @param  req  HttpServletRequestインスタンス
     * @param  conn コネクション
     * @return 医師名一覧
     */
//      Vector getReceivedMessageList(HttpServletRequest req, Connection conn) {
//  	String mrId = req.getParameter("com_mrid");
//  	MsgManager manager = new MsgManager(conn);
//  	Vector list = manager.getReceivedMessageList(mrId);
//  	return list;
//      }

    void loginUpdate(HttpServletRequest req, Connection conn) {
		String  mrid = req.getParameter("com_mrid");

		MrInfoManager manager = new MrInfoManager(conn);
		manager.updateLogin(mrid);
    }

    /**
     * 必要な情報をDBから取得し、セッションにセットする.
     */
    void initSessionParameter(HttpSession session) {
		String statCall30 = getConstTable("STATGRAPH_CALL", 1);
		String statCall180 = getConstTable("STATGRAPH_CALL", 2);
		String statCust30 = getConstTable("STATGRAPH_CUST", 1);
		String statCust180 = getConstTable("STATGRAPH_CUST", 2);
		String statSend30 = getConstTable("STATGRAPH_SEND", 1);
		String statSend180 = getConstTable("STATGRAPH_SEND", 2);
		String statRatio30 = getConstTable("STATGRAPH_RATIO", 1);
		String statRatio180 = getConstTable("STATGRAPH_RATIO", 2);
		String actionPic1 = getConstTable("ACTION_PIC1", 1);
		String actionPic2 = getConstTable("ACTION_PIC2", 1);
		String actionPic3 = getConstTable("ACTION_PIC3", 1);
		String actionPic4 = getConstTable("ACTION_PIC4", 1);
		String actionPicDefault = getConstTable("ACTION_DEFAULT", 1);

		session.putValue("com_call30", statCall30);
		session.putValue("com_call180", statCall180);
		session.putValue("com_cust30", statCust30);
		session.putValue("com_cust180", statCust180);
		session.putValue("com_send30", statSend30);
		session.putValue("com_send180", statSend180);
		session.putValue("com_ratio30", statRatio30);
		session.putValue("com_ratio180", statRatio180);
		session.putValue("com_actionPic1", actionPic1);
		session.putValue("com_actionPic2", actionPic2);
		session.putValue("com_actionPic3", actionPic3);
		session.putValue("com_actionPic4", actionPic4);
		session.putValue("com_actionPicDefault", actionPicDefault);

		// set 受信BOX Max Count 
		String receive_cnt = getConstTable("RECEIVEMAX", 1);
		session.putValue("com_receive_cnt", receive_cnt);

		// set 受信保管BOX Max Count 
		String receive_save_cnt = getConstTable("RECEIVEMAX", 2);
		session.putValue("com_receive_save_cnt", receive_save_cnt);

		// set 送信BOX Max Count 
		String send_cnt = getConstTable("SENDMAX", 1);
		session.putValue("com_send_cnt", send_cnt);

		// set 送信保管BOX Max Count 
		String send_save_cnt = getConstTable("SENDMAX", 2);
		session.putValue("com_send_save_cnt", send_save_cnt);
    }
}
