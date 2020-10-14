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
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro DR送信一覧
 */
public class SendList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "receive_time";
		String order = "D";
		String date_order = "D";
		String user_order = "A";
		String read_order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				if (request.getParameter("init") != null) {
					Vector list = (Vector)session.getValue("DrSendList_headerIdList");
					if (list != null) {
						Enumeration enum = list.elements();
						while (enum.hasMoreElements()) {
							session.removeValue((String)enum.nextElement());
						}
						session.removeValue("DrSendList_headerIdList");
					}
					session.removeValue("date_order");
					session.removeValue("user_order");
					session.removeValue("read_order");
					session.removeValue("order");
					session.removeValue("sort");
				}

				Enumeration keys = request.getParameterNames();

				//消されると困るのでとっておく
//  				if (keys.hasMoreElements()) {
					sort = (String)session.getValue("sort");
					date_order = (String)session.getValue("date_order");
					user_order = (String)session.getValue("user_order");
					read_order = (String)session.getValue("read_order");

					sort = sort == null ? "receive_time" : sort;
					date_order = date_order == null ? "D" : date_order;
					user_order = user_order == null ? "A" : user_order;
					read_order = read_order == null ? "A" : read_order;

//  					if (session.getValue("sort") == null) {
//  						sort = "receive_time";
//  					} else {
//  						sort = (String)session.getValue("sort");
//  					}
//  					if (session.getValue("date_order") == null) {
//  						date_order = "D";
//  					} else {
//  						date_order = (String)session.getValue("date_order");
//  					}
//  					if (session.getValue("user_order") == null) {
//  						user_order = "A";
//  					} else {
//  						user_order = (String)session.getValue("user_order");
//  					}
//  					if (session.getValue("read_order") == null) {
//  						read_order = "A";
//  					} else {
//  						read_order = (String)session.getValue("read_order");
//  					}
//  				}

				// 保存または削除処理
				session.putValue("DrSendList_saveconf"," ");
				session.putValue("DrSendList_saveaction"," ");
				session.putValue("DrSendList_moveconf"," ");
				session.putValue("DrSendList_moveaction"," ");
				session.putValue("DrSendList_removeconf"," ");
				session.putValue("DrSendList_removeaction"," ");
				session.putValue("DrSendList_action"," ");
				session.putValue("DrSendList_msg"," ");
   
				if ( request.getParameter("DrSendList_submitname") != null ) {
					// 「保管する」のイメージを押したとき
					if  ( request.getParameter("DrSendList_submitname").equals("SavePre") ) {
						if (inputCheck(request)) {
//  			    session.putValue("saveconf","ON");
//  			    getCheckBox(request,response,session);
							// DB Connection
							DBConnect dbconn = new DBConnect();
							Connection conn = dbconn.getDBConnect();
							// 件数メソッドの呼び出しのため
							MsgManager msgmanager = new MsgManager(conn);
							String[] e = request.getParameterValues("msg_head_id");
							String drid = (String)session.getValue("com_drid");
							boolean ret = false;

							try {
								// 件数のチェックをする
								ret = msgmanager.isSendSaveBoxCheck(drid,e.length);
							} finally {
								// DBclose
								dbconn.closeDB(conn);
							}
							if ( ret ) {
								session.putValue("DrSendList_msg","saveOK");
								getCheckBox(request,response,session);
								// セーブする
								msgSave(request,response,session);
							}
							else {
								session.putValue("DrSendList_msg","kensuNG");
								getCheckBox(request,response,session);
							}
						}
						else {
							session.putValue("DrSendList_saveconf","OFF");
						}
					}
					// 「ごみ箱へ」のイメージを押したとき
					else if ( request.getParameter("DrSendList_submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("DrSendList_removeconf","ON");
							getCheckBox(request,response,session);
						}
						else {
							session.putValue("DrSendList_removeconf","OFF");
						}
					}
					// 「ごみ箱のOK/Cancel」のボタンを出すとき
					else if ( request.getParameter("DrSendList_submitname").equals("Remove") ) {
						// 「ごみ箱のOK」のボタンを押されたとき
						if ( request.getParameter("DrSendList_removeaction").equals("OK") ) {
							session.putValue("DrSendList_msg","removeOK");
							getCheckBox(request,response,session);
							// リムーブする
							msgRemove(request,response,session);
						}
						// 「ごみ箱のCancel」のボタンを押されたとき
						else if ( request.getParameter("DrSendList_removeaction").equals("Cancel") ) {
							session.putValue("DrSendList_msg","removeCancel");
							getCheckBox(request,response,session);
						}

					}
					// 「保管のOK/Cancel」のボタンを出すとき(2回目）
					else if ( request.getParameter("DrSendList_submitname").equals("SaveCnt") ) {
						// 「保管のOK」のボタンを押されたとき
						if ( request.getParameter("DrSendList_action").equals("OK") ) {
							session.putValue("DrSendList_msg","CntOK");
							getCheckBox(request,response,session);
							// セーブする
							msgSave(request,response,session);
						}
						// 「保管のCancel」のボタンを押されたとき
						else if ( request.getParameter("DrSendList_action").equals("Cancel") ) {
							session.putValue("DrSendList_msg","CntCancel");
							getCheckBox(request,response,session);
						}
     
					}
				}
   
				// set of sort
				setSort(request,
						session,
						sort,
						order,
						date_order,
						user_order,
						read_order);

				// Go to the next page
				// response.sendRedirect("/medipro/dr/DrSendList/index.html");
				response.sendRedirect("/medipro/dr/DrSendList/index.html?" + request.getQueryString());
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

    void getCheckBox(HttpServletRequest req,
					 HttpServletResponse res,
					 HttpSession session) {
		String[] e = req.getParameterValues("msg_head_id");
		Vector list = new Vector();
		for     ( int index = 0; index < e.length; index++ ) {
			session.putValue(e[index],"ON");
			list.addElement(e[index]);
		}
		session.putValue("DrSendList_headerIdList", list);
    } 

    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Vector msgid = new Vector();
		String[] e = req.getParameterValues("msg_head_id");
		for ( int index = 0; index < e.length; index++ ) {
			msgid.addElement(e[index]);
			session.putValue(e[index], "ON");
		}
		session.putValue("DrSendList_headerIdList", msgid);
		return msgid.elements();
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
		Enumeration  idlist = getMsgHeadId(req, session);

		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateDrSendMsgStatus(idlist, SysCnst.MSG_STATUS_SAVE);
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}
    }

    void msgRemove(HttpServletRequest req,
				   HttpServletResponse res,
				   HttpSession session) {
		Enumeration  idlist = getMsgHeadId(req, session);
 
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();
		
		try {
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateDrSendMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}
    }

    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String sort,
				 String order,
				 String date_order,
				 String user_order,
				 String read_order) {

		if (req.getParameter("sort") != null) {
			sort = req.getParameter("sort");

			// 送信日時
			if (sort.equals("receive_time")) {
				date_order = (date_order.equals("A"))? "D":"A";
				order = date_order;
			}
			// 宛先
			else if (sort.equals("to_userid")) {
				user_order = (user_order.equals("A"))? "D":"A";
				order = user_order;
			}
			// 相手未読
			else if (sort.equals("receive_timed")) {
				read_order = (read_order.equals("A"))? "D":"A";
				order = read_order;
			}
		}
		session.putValue("date_order", date_order);
		session.putValue("user_order", user_order);
		session.putValue("read_order", read_order);
		session.putValue("order"     , order);
		session.putValue("sort"      , sort);
    }
}
