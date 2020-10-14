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
 * Medipro MR送信保管Box
 */
public class SendSaveBoxList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "receive_time";
		String order = "D";
		String date_order = "D";
		String target_order = "A";
		String read_order = "A";
		String user_order = "A";
		String unread_order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

				if (request.getParameter("init") != null) {
					Vector v = (Vector)session.getValue("MrSendSaveBoxList_headerIdList");
					if (v != null) {
						Enumeration enum = v.elements();
						while (enum.hasMoreElements()) {
							session.removeValue((String)enum.nextElement());
						}
						session.removeValue("MrSendSaveBoxList_headerIdList");
					}
				}

				// 消されると困るのでとっておく
//  				if      ( keys.hasMoreElements() ) {
					sort    = (String)session.getValue("sort");
					user_order = (String)session.getValue("user_order");
					target_order = (String)session.getValue("target_order");
					date_order = (String)session.getValue("date_order");
					read_order = (String)session.getValue("read_order");
					unread_order = (String)session.getValue("unread_order");

					sort = sort == null ? "receive_time" : sort;
					user_order = user_order == null ? "A" : user_order;
					target_order = target_order == null ? "A" : target_order;
					date_order = date_order == null ? "D" : date_order;
					read_order = read_order == null ? "A" : read_order;
					unread_order = unread_order == null ? "A" : unread_order;

//  					if      ( session.getValue("sort") == null ) {
//  						sort    = "receive_time";
//  					} else {
//  						sort    = (String)session.getValue("sort");
//  					}
//  					if      ( session.getValue("user_order") == null ) {
//  						user_order      = "A";
//  					} else {
//  						user_order = (String)session.getValue("user_order");
//  					}
//  					if      ( session.getValue("target_order") == null ) {
//  						target_order      = "A";
//  					} else {
//  						target_order = (String)session.getValue("target_order");
//  					}
//  					if      ( session.getValue("date_order") == null ) {
//  						date_order      = "D";
//  						order           = "D";
//  					} else {
//  						date_order = (String)session.getValue("date_order");
//  					}
//  					if      ( session.getValue("read_order") == null ) {
//  						read_order      = "A";
//  					} else {
//  						read_order = (String)session.getValue("read_order");
//  					}
//  					if      ( session.getValue("unread_order") == null ) {
//  						unread_order      = "A";
//  					} else {
//  						unread_order = (String)session.getValue("unread_order");
//  					}
//  				}

				// reset of session
				sm.reset(request,0);

				//　削除
				session.putValue("MrSendSaveBoxList_moveconf"," ");
				session.putValue("MrSendSaveBoxList_moveaction"," ");
				session.putValue("MrSendSaveBoxList_torikeshiconf"," ");
				session.putValue("MrSendSaveBoxList_torikeshiaction"," ");
				session.putValue("MrSendSaveBoxList_removeconf"," ");
				session.putValue("MrSendSaveBoxList_removeaction"," ");
				session.putValue("MrSendSaveBoxList_msg"," ");
   

				if ( request.getParameter("MrSendSaveBoxList_submitname") != null )	{
					// 「ごみ箱へ」のイメージを押したとき
					if ( request.getParameter("MrSendSaveBoxList_submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("MrSendSaveBoxList_removeconf","ON");
							getCheckBox(request,response,session);
						}
						else {
							session.putValue("MrSendSaveBoxList_removeconf","OFF");
						}
					}
					// 「ごみ箱のOK/Cancel」のボタンを出すとき
					else if ( request.getParameter("MrSendSaveBoxList_submitname").equals("Remove") ) {
						// 「ごみ箱のOK」のボタンを押されたとき
						if ( request.getParameter("MrSendSaveBoxList_removeaction").equals("OK") ) {
							session.putValue("MrSendSaveBoxList_msg","removeOK");
							getCheckBox(request,response,session);
								// リムーブする
							msgRemove(request,response,session);
						}
						// 「ごみ箱のCancel」のボタンを押されたとき
						else if ( request.getParameter("MrSendSaveBoxList_removeaction").equals("Cancel") ) {
							session.putValue("MrSendSaveBoxList_msg","removeCancel");
							getCheckBox(request,response,session);
						}

					}
					// 「未読の場合送信取り消し」のイメージを押されたとき
					else if ( request.getParameter("MrSendSaveBoxList_submitname").equals("TorikeshiPre") ) {
						// 「取り消し」のイメージを押したとき
						if ( inputCheck(request) ) {
								//  				session.putValue("torikeshiconf","ON");
								//  				getCheckBox(request,response,session);
							session.putValue("MrSendSaveBoxList_msg","torikeshiOK");
							getCheckBox(request,response,session);
								// 取り消す
							msgTorikeshi(request,response,session);
						}
						else {
							session.putValue("MrSendSaveBoxList_torikeshiconf","OFF");
						}
					}
				}
   
				//　set of sort
				setSort(request,
						session,
						sort,
						order,
						date_order,
						target_order,
						read_order,
						user_order,
						unread_order);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrSendSaveBoxList/index.html");
			} else {
				// セッションエラーの場合
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
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
		session.putValue("MrSendSaveBoxList_headerIdList", list);
    }

    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Vector msgid = new Vector();
		String[] e = req.getParameterValues("msg_head_id");
		for     ( int index = 0; index < e.length; index++ ) {
			msgid.addElement(e[index]);
			session.putValue(e[index], "ON");
		}
		session.putValue("MrSendSaveBoxList_headerIdList", msgid);
		return msgid.elements();
    }

    boolean inputCheck(HttpServletRequest req) {
		boolean ret = true;
		if      ( req.getParameter("msg_head_id") == null ) {
			ret = false;
		}
		return ret;
    }

    void msgRemove(HttpServletRequest req,
				   HttpServletResponse res,
				   HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			Enumeration idlist = getMsgHeadId(req, session);

			MsgManager manager = new MsgManager(conn);
			manager.updateMrSendMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    void msgTorikeshi(HttpServletRequest req,
					  HttpServletResponse res,
					  HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			Enumeration idlist = getMsgHeadId(req, session);

			MsgManager manager = new MsgManager(conn);
			manager.updateMrSendCancel(idlist);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String sort,
				 String order,
				 String date_order,
				 String target_order,
				 String read_order,
				 String user_order,
				 String unread_order) {

		if ( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// 宛先
			if ( sort.equals("to_userid") ) {
				user_order      = (user_order.equals("A"))? "D":"A";
				order           = user_order;
			}

			// ターゲットランク
			else if ( sort.equals("target_rank") ) {
				target_order      = (target_order.equals("A"))? "D":"A";
				order           = target_order;
			}
  
			// 送信日時
			else if ( sort.equals("receive_time") ) {
				date_order      = (date_order.equals("A"))? "D":"A";
				order           = date_order;
			}

			// 未読
			else if ( sort.equals("receive_timed") ) {
				read_order      = (read_order.equals("A"))? "D":"A";
				order           = read_order;
			}

			// 未読日数
			if      ( sort.equals("unread_day") ) {
				unread_order      = (unread_order.equals("A"))? "D":"A";
				order           = unread_order;
			}
		}

		session.putValue("user_order", user_order);
		session.putValue("target_order", target_order);
		session.putValue("date_order", date_order);
		session.putValue("read_order", read_order);
		session.putValue("unread_order", unread_order);
		session.putValue("order"     , order);
		session.putValue("sort"      , sort);
    }  
}
