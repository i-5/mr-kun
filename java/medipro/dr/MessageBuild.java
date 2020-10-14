package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
* Medipro DRメッセージ作成
*/

public class MessageBuild extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				sm.reset(request,0);

				HttpSession session = request.getSession(true);

				if (request.getParameter("init") != null) {
					session.removeValue("mrid_hash");
					session.removeValue("message");
					session.removeValue("msg_title");
					session.removeValue("link_url");
					session.removeValue("allcheck");
					session.removeValue("confirm");
				}
				session.removeValue("no_check_to");
				session.removeValue("sendmail");

				// 送信前確認
				if (request.getParameter("submitaction") != null) {
					//チェックボックス選択の保持
					setCheckedList(request, session);

					if (isChecked(request, session)) {
						if (existOutOfWorkTimeMr(request)) {
							session.putValue("confirm","ON");
						} else {
							sendMessageGo(request, response, session);
							session.putValue("sendmail","OK");
						}
					} else {  // 宛先なし
						session.putValue("no_check_to","NG");
					}
				}
				// メッセージ送信
				else if (request.getParameter("send_ok") != null) {
					session.removeValue("confirm");
					//チェックボックス選択の保持
					setCheckedList(request, session);

					if (isChecked(request, session)) {
						sendMessageGo(request, response, session);
						session.putValue("sendmail","OK");
					} else {  // 宛先なし
						session.putValue("no_check_to","NG");
					}
				}
				else if (request.getParameter("send_cancel") != null) {
					session.removeValue("confirm");
				}

				// Go to the next page
				// response.sendRedirect("/medipro/dr/DrMessageBuild/index.html");
				response.sendRedirect("/medipro/dr/DrMessageBuild/index.html?" + request.getQueryString());
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

    /**
     * 営業時間外のMRを選択していないかチェックする.
     * @return 選択されていたらtrue
     */
    boolean existOutOfWorkTimeMr(HttpServletRequest req) {
		String[] list = req.getParameterValues("mr_id");
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		MrInfoManager manager = new MrInfoManager(conn);

		try {
			for (int i = 0; i < list.length; i++) {
				if (manager.outOfWorkTime(list[i])) {
					return true;
				}
			}
		} finally {
			dbconn.closeDB(conn);
		}

		return false;
    }

    /*
     * 宛先選択でチェックされたMR-ID一覧をハッシュに格納する.
     */
    void setCheckedList(HttpServletRequest req, HttpSession session) {
		if (req.getParameterValues("mr_id") == null) {
			session.removeValue("mrid_hash");
		} else {
			Hashtable hash = new Hashtable();
			String[] lists = req.getParameterValues("mr_id");
			for (int index = 0; index < lists.length; index++) {
				hash.put(lists[index], lists[index]);
			}
			session.putValue("mrid_hash", hash);
		}
    }

    /**
     * 宛先選択されているかチェックする.
     * @return 一人でも選択されていたらtrue
     */
    boolean isChecked(HttpServletRequest req, HttpSession session) {
		if (req.getParameterValues("mr_id") == null) {
			return false;
		} else {
			return true;
		}
    }

    /**
     * メッセージ送信.
     */
    void sendMessageGo(HttpServletRequest req,
					   HttpServletResponse res,
					   HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		MessageTableManager manager = new MessageTableManager(conn);
		MessageTable msgtbl  = new MessageTable();
		MessageHeaderTable msgtblhead = new MessageHeaderTable();
		MessageBodyTable msgtblbody = new MessageBodyTable();

		// 選択されたMRをセット
		Vector mr = new Vector();
		String[] e = req.getParameterValues("mr_id");
		for ( int index = 0; index < e.length; index++ ) {
			mr.addElement(e[index]);
		}
		msgtblhead.setMessageKbn("2");
		msgtblhead.setFromUserID((String)session.getValue("com_drid"));

		if ( session.getValue("msg_title") != null ) {
			msgtblbody.setTitle((String)session.getValue("msg_title"));
		}

		if ( session.getValue("message") != null ) {
			msgtblbody.setMessageHonbun((String)session.getValue("message"));
		}

		if ( session.getValue("link_url") != null ) {
			msgtblbody.setUrl((String)session.getValue("link_url"));
		}

		msgtbl.setMsgHTable(msgtblhead);
		msgtbl.setMsgBTable(msgtblbody);

        //-- 添付・リンクファイルセット（内容カラ）
		AttachFileTable  atfiletbl = new AttachFileTable();
		AttachLinkTable  atlinktbl = new AttachLinkTable();
		Vector  attachtbl_work  = new Vector(); 

		msgtbl.setAttachFTable(attachtbl_work.elements());
		msgtbl.setAttachLTable(attachtbl_work.elements());

		try {
			// Message送信
			String msgID = manager.insert(mr.elements(), msgtbl);
		} finally {
			dbconn.closeDB(conn);
		}
    }

}
