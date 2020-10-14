package medipro.dr;

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

/**
 * Medipro DR受信メッセージ詳細
 */
public class ReceiveMessage extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				sm.reset(request,1);
				HttpSession session = request.getSession(true);

				if (request.getParameter("sendMessage") != null) {
					// メッセージ送信
					if (sendMessageGo(request, response, session)) {
						session.putValue("sendmail", "OK");
					} else {
						session.putValue("mrstate", "delete");
					}
				} else if (request.getParameter("redirect_url") != null) {
					String messageId = request.getParameter("msg_head_id");
					processClickEvent(session, messageId);

// 01/01/22 M.Mizuki		    String url = request.getParameter("redirect_url");
					String url = ProxySec.getURL( request.getParameter("redirect_url") );
					response.sendRedirect(url);
					return;
				} else {
					session.removeValue("message");
					session.removeValue("sendmail");
					session.removeValue("mrstate");
					// added by matsuura 2000/10/3 これでいいのかな...??
					session.removeValue("msg_title");
				}

				// Go to the next page
				session.putValue("DrReceiveSaveBoxList", "NO");//1124 y-yamada add NO.62
				response.sendRedirect("/medipro/dr/DrReceiveMessage/index.html");
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

    boolean sendMessageGo(HttpServletRequest req,
						  HttpServletResponse res,
						  HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			String mrId = (String)session.getValue("from_mrid");
			String drId = (String)session.getValue("com_drid");

			TantoInfoManager tantoManager = new TantoInfoManager(conn);
			if (!tantoManager.hasRelation(drId, mrId)) {
				return false;
			}

			MessageTableManager manager = new MessageTableManager(conn);
			MessageTable msgtbl  = new MessageTable();
			MessageHeaderTable msgtblhead = new MessageHeaderTable();
			MessageBodyTable msgtblbody = new MessageBodyTable();

			Vector mr = new Vector();
			mr.addElement(mrId);

			msgtblhead.setMessageKbn("2");
			msgtblhead.setFromUserID(drId);

			if ( session.getValue("msg_title") != null ) {
				msgtblbody.setTitle((String)session.getValue("msg_title"));
			}

			if ( session.getValue("message") != null ) {
				msgtblbody.setMessageHonbun((String)session.getValue("message"));
			}

			msgtbl.setMsgHTable(msgtblhead);
			msgtbl.setMsgBTable(msgtblbody);

			//-- 添付・リンクファイルセット（内容カラ）
			AttachFileTable  atfiletbl = new AttachFileTable();
			AttachLinkTable  atlinktbl = new AttachLinkTable();
			Vector  attachtbl_work  = new Vector(); 

			msgtbl.setAttachFTable(attachtbl_work.elements());
			msgtbl.setAttachLTable(attachtbl_work.elements());
 
			// Message送信
			String msgID = manager.insert(mr.elements(), msgtbl);
		} finally {
			dbconn.closeDB(conn);
		}

		return true;
    }

    void processClickEvent(HttpSession session, String messageHeaderId) {
		String drId = (String)session.getValue("com_drid");

	// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();
	
		try {
			MsgManager msgmanager = new MsgManager(conn);
			int updateCount = msgmanager.updateRecvMsg(messageHeaderId);
			MsgInfo msginfo = msgmanager.getDrRecvMessage(messageHeaderId);

			//未読メッセージを読んだ場合
			//MsgManager#updateRecvMsgが未読を既読に実際に変更したとき
			if (updateCount > 0) {
				//アンケートの場合のみアップしない
				String mrId = getConstTable("ENQUETE_MR_ID", 1);
				MrInfoManager mrManager = new MrInfoManager(conn);
				MrInfo mrInfo = mrManager.getMrInfo(msginfo.getHeader().getFromUserID());

				if (!mrInfo.getCompanyCD().equals("0000000000") &&
					!mrInfo.getCompanyCD().equals("0000000010") &&
					!mrInfo.getCompanyCD().equals("0000000012") && // y-yamada add 12にもポイントを追加しない
					!mrId.equals(msginfo.getHeader().getFromUserID())) {
					DoctorInfoManager drManager = new DoctorInfoManager(conn);
					drManager.updatePoint(drId);
				}
				sendReceivedMessage(msginfo);
			}

			TotatsuCallLogTable totatsucalltable = new TotatsuCallLogTable();
			totatsucalltable.setFromUserID(msginfo.getHeader().getFromUserID());
			totatsucalltable.setToUserID(msginfo.getHeader().getToUserID());
			totatsucalltable.setMessageHeaderID(msginfo.getHeader().getMessageHeaderID());
			//totatsucalltable.setPictureCD(null);
			TotatsuCallLogTableManager totatsucalltablemanager =
				new TotatsuCallLogTableManager(conn);
			totatsucalltablemanager.insert(totatsucalltable);
		} catch (MrException e) {
			throw new MrException(e);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    void sendReceivedMessage(MsgInfo msgInfo) {
		DBConnect dbConnect = new DBConnect();
		Connection connection = dbConnect.getDBConnect();

		MessageTableManager manager = new MessageTableManager(connection);
		MessageTable message = new MessageTable();
		MessageHeaderTable header = new MessageHeaderTable();
		MessageBodyTable body = new MessageBodyTable();

		TantoInfoManager tantoManager = new TantoInfoManager(connection);

		try {
			TantoInfo tantoInfo = tantoManager.getDrInfo(msgInfo.getHeader().getFromUserID(),
														 msgInfo.getHeader().getToUserID());
	
			header.setMessageKbn(SysCnst.MESSAGE_KBN_TO_OTHER);
			header.setFromUserID(msgInfo.getHeader().getToUserID());
			body.setTitle("開封通知:" + tantoInfo.getName());
			body.setMessageHonbun(msgInfo.getBody().getHonbunText());

			message.setMsgHTable(header);
			message.setMsgBTable(body);

			Vector toList = new Vector();
			toList.addElement(msgInfo.getHeader().getFromUserID());

			//-- 添付・リンクファイルセット（内容カラ）
			Vector attach = new Vector();
			message.setAttachFTable(attach.elements());
			message.setAttachLTable(attach.elements());

			manager.insert(toList.elements(), message);
		} finally {
			dbConnect.closeDB(connection);
		}
    }

    String getConstTable(String keyword, int colno) {
		String value = null;

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager = new ConstantMasterTableManager(conn);
			ConstantMasterTable constmanager = manager.getConstantMasterTable(keyword);

			if ( colno == 1 ) {
				value = constmanager.getNaiyo1().trim();
			} else {
				value = constmanager.getNaiyo2().trim();
			}
		} finally {
			dbconn.closeDB(conn);
		}

		return value;
    }
}
