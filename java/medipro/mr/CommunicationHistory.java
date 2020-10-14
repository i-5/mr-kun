package medipro.mr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro MRコミュニケ－ション履歴
 */
public class CommunicationHistory extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String M19_sort = "receive_time";	//初期ソート項目
		String M19_order = "D";				//初期ソート方向
		String M19_date_order = "D";
		String M19_title_order = "A";
		String M19_status_order = "A";
		String M19_read_order = "A";
		String M19_unread_order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if (sm.check(request, 1)) {
				HttpSession session = request.getSession(true);

				//初期化
				if (request.getParameter("init") != null) {
					session.removeValue("msgid_hash");
					//y-yamada 1011 add  start  リセット値をセッションに設定する
					session.putValue("M19_sort" ,M19_sort );
					session.putValue("M19_date_order", M19_date_order);
					session.putValue("M19_order" , M19_order);
					session.putValue("M19_status_order" ,M19_status_order);
					session.putValue("M19_title_order" , M19_title_order);
					session.putValue("M19_read_order" ,M19_read_order);
					session.putValue("M19_unread_order" ,M19_unread_order);
					//y-yamada 1011 add  end
				}

				Enumeration keys = request.getParameterNames();

				//消されると困るのでとっておく
//  				if (keys.hasMoreElements()) {
					M19_sort = (String)session.getValue("M19_sort");
					M19_status_order = (String)session.getValue("M19_status_order");
					M19_date_order = (String)session.getValue("M19_date_order");
					M19_title_order = (String)session.getValue("M19_title_order");
					M19_read_order = (String)session.getValue("M19_read_order");
					M19_unread_order = (String)session.getValue("M19_unread_order");

					M19_sort = M19_sort == null ? "receive_time" : M19_sort;
					M19_status_order = M19_status_order == null ? "A" : M19_status_order;
					M19_date_order = M19_date_order == null ? "D" : M19_date_order;
					M19_title_order = M19_title_order ==  null ? "A" : M19_title_order;
					M19_read_order = M19_read_order == null ? "A" : M19_read_order;
					M19_unread_order = M19_unread_order == null ? "A" : M19_unread_order;

//  					if (session.getValue("M19_sort") == null) {
//  						M19_sort = "receive_time";
//  					} else {
//  						M19_sort = (String)session.getValue("M19_sort");
//  					}

//  					if (session.getValue("M19_status_order") == null) {
//  						M19_status_order = "A";
//  					} else {
//  						M19_status_order = (String)session.getValue("M19_status_order");
//  					}

//  					if (session.getValue("M19_date_order") == null) {
//  						M19_date_order = "D";
//  					} else {
//  						M19_date_order = (String)session.getValue("M19_date_order");
//  					}

//  					if ( session.getValue("M19_title_order") == null) {
//  						M19_title_order = "A";
//  					} else {
//  						M19_title_order = (String)session.getValue("M19_title_order");
//  					}

//  					if (session.getValue("M19_read_order") == null) {
//  						M19_read_order = "A";
//  					} else {
//  						M19_read_order = (String)session.getValue("M19_read_order");
//  					}

//  					if (session.getValue("M19_unread_order") == null) {
//  						M19_unread_order = "A";
//  					} else {
//  						M19_unread_order = (String)session.getValue("M19_unread_order");
//  					}
//  				}

				// reset of session
				sm.reset(request,1); 

				// 保存または削除処理
				session.removeValue("msgid_hash"); // 前の情報をけす
				getCheckBox(request,response,session); //新ハッシュのセッションを作成
				session.putValue("M19_saveconf"," ");
				session.putValue("M19_saveaction"," ");
				session.putValue("M19_removeconf"," ");
				session.putValue("M19_removeaction"," ");
				session.putValue("M19_torikeshiconf"," ");
				session.putValue("M19_torikeshiaction"," ");
				session.putValue("M19_action"," ");
				session.putValue("M19_msg"," ");

				// DB connection
				DBConnect dbconn = new DBConnect();
				Connection conn = dbconn.getDBConnect();

				// 件数メソッドの呼び出しのため
				MsgManager msgmanager = new MsgManager(conn);
				String[] e = request.getParameterValues("msg_head_id");
				String mrid = (String)session.getValue("com_mrid");

				if (request.getParameter("submitname") != null) {
					// 「保管する」のイメージを押したとき
					if (request.getParameter("submitname").equals("SavePre")) {
						if (inputCheck(request)) {
//  							session.putValue("M19_saveconf","ON");
							session.putValue("M19_msg","saveOK");
							// セーブする
							msgSave(request,response,session);
						} else {
							session.putValue("M19_saveconf","OFF");
						}
					}
					// 「ごみ箱へ」のイメージを押したとき
					else if ( request.getParameter("submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("M19_removeconf","ON");
						} else {
							session.putValue("M19_removeconf","OFF");
						}
					}
					// 「ごみ箱のOK/Cancel」のボタンを出すとき
					else if ( request.getParameter("submitname").equals("Remove") ) {
						// 「ごみ箱のOK」のボタンを押されたとき
						if ( request.getParameter("M19_removeaction").equals("OK") ) {
							session.putValue("M19_msg","removeOK");
							// リムーブする
							msgRemove(request,response,session);
						}
						// 「ごみ箱のCancel」のボタンを押されたとき
						else if ( request.getParameter("M19_removeaction").equals("Cancel") ) {
							session.putValue("M19_msg","removeCancel");
						}

					}
					// 「未読の場合送信取り消し」のイメージを押されたとき
					else if ( request.getParameter("submitname").equals("TorikeshiPre") ) {
						// 「取り消し」のイメージを押したとき
						if ( inputCheck(request) ) {
//  							session.putValue("M19_torikeshiconf","ON");
							if (msgTorikeshi(request,response,session) > 0) {
								session.putValue("M19_msg","torikeshiOK");
							} else {
								session.putValue("M19_torikeshiconf","OFF");
							}
						} else {
							session.putValue("M19_torikeshiconf","OFF");
						}
					}
					// 「取り消しのOK/Cancel」のボタンを出すとき
				}

				// DBclose
				dbconn.closeDB(conn);

				// set of sort
				setSort(request,
						session,
						M19_date_order,
						M19_title_order,
						M19_status_order,
						M19_read_order,
						M19_unread_order,
						M19_order,
						M19_sort);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrCommunicationHistory/index.html");
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

    // どのボタンが押されてもここを通すようにしてるので、チェックされて値がある場合、
    // 毎回msg_head_idはgetされてハッシュの中に埋められ、ひとつのセッションになる
    void getCheckBox(HttpServletRequest req,
					 HttpServletResponse res,
					 HttpSession session) {
		Hashtable hash = new Hashtable();
		String[]  msgIDs = req.getParameterValues("msg_head_id");
 
		if ( msgIDs != null ) {
			for ( int i=0; i < msgIDs.length; i++ ) {
				String msgid = (String)msgIDs[i];
				hash.put(msgid, msgid);
			}
			session.putValue("msgid_hash", hash);
		} else {
			hash.put("EOF", "EOF");
			session.putValue("msgid_hash", hash);
		}
    } 

    // 上で作ったハッシュをベクターに入れてる
    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Hashtable msg_hash = (Hashtable)session.getValue("msgid_hash");
		String  msgbuff  = null;
		Vector  msg_vec  = new Vector();
		Enumeration enum  = msg_hash.elements(); // 値の部分を配列        にして取り出す
		while ( enum.hasMoreElements() ) {
			msgbuff = (String)enum.nextElement();
			msg_vec.addElement(msgbuff);
		}
		return msg_vec.elements();

    }
      
    boolean inputCheck(HttpServletRequest req) {
		boolean ret = true;
		if ( req.getParameter("msg_head_id") == null ) {
			ret = false;
		}
		return ret;
    }

    void msgSave(HttpServletRequest req,
				 HttpServletResponse res,
				 HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			// Message Header ID 取得
			Enumeration  idlist = getMsgHeadId(req, session);
			Enumeration  idlist2 = getMsgHeadId(req, session);

			// 保管処理
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateMrSendMsgStatus(idlist, SysCnst.MSG_STATUS_SAVE);
			msgmanager.updateMrRecvMsgStatus(idlist2, SysCnst.MSG_STATUS_SAVE);
		} finally {
			// DBclose
			dbconn.closeDB(conn);
		}
    }

    void msgRemove(HttpServletRequest req,
				   HttpServletResponse res,
				   HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			// Message Header ID 取得
			Enumeration  idlist = getMsgHeadId(req, session);
			Enumeration  idlist2 = getMsgHeadId(req, session);

			// ごみ箱処理
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateMrRecvMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
			msgmanager.updateMrSendMsgStatus(idlist2, SysCnst.MSG_STATUS_DUST);
		} finally {
			// DBclose
			dbconn.closeDB(conn);
		}
    }

    int msgTorikeshi(HttpServletRequest req,
					 HttpServletResponse res,
					 HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();
		int count = 0;

		try {
			// Message Header ID 取得
			Enumeration  idlist = getMsgHeadId(req, session);

			// 送信取り消し処理
			MsgManager msgmanager = new MsgManager(conn);
			count = msgmanager.updateMrSendCancel(idlist);

			// DBclose
		} finally {
			dbconn.closeDB(conn);
		}

		return count;
    }

    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String M19_date_order,
				 String M19_title_order,
				 String M19_status_order,
				 String M19_read_order,
				 String M19_unread_order,
				 String M19_order,
				 String M19_sort) {
		if ( req.getParameter("M19_sort") != null ) {
			M19_sort = req.getParameter("M19_sort");

			// 送受信
			if ( M19_sort.equals("message_kbn") ) {
				M19_status_order = (M19_status_order.equals("A")) ? "D":"A";
				M19_order = M19_status_order;
			}
			// 日時
			else if ( M19_sort.equals("receive_time") ) {
				M19_date_order = (M19_date_order.equals("A")) ? "D":"A";
				M19_order = M19_date_order;
			}
			// タイトル
			else if ( M19_sort.equals("title") ) {
				M19_title_order = (M19_title_order.equals("A")) ? "D":"A";
				M19_order = M19_title_order;
			}
			// 未読
			else if ( M19_sort.equals("receive_timed") ) {
				M19_read_order = (M19_read_order.equals("A")) ? "D":"A";
				M19_order = M19_read_order;
			}
			// 未読日数
			else if ( M19_sort.equals("recvDay") ) {
				M19_unread_order = (M19_unread_order.equals("A")) ? "D":"A";
				M19_order = M19_unread_order;
			}
		}

		session.putValue("M19_status_order",M19_status_order);
		session.putValue("M19_date_order",  M19_date_order);
		session.putValue("M19_title_order", M19_title_order);
		session.putValue("M19_read_order",  M19_read_order);
		session.putValue("M19_unread_order",M19_unread_order);
		session.putValue("M19_order",       M19_order);
		session.putValue("M19_sort",        M19_sort);
		// 画面名のセット
		session.putValue("gamen"      , "M19");
	}
}
