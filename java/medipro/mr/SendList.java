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
 * Medipro MR���M�ꗗ
 */
public class SendList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "receive_time";
		String order = "D";
		String date_order = "D";
		String target_order = "A";
		String user_order = "A";
		String read_order = "A";
		String unread_order = "A";

		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

				if (request.getParameter("init") != null) {
					Vector v = (Vector)session.getValue("MrSendList_headerIdList");
					if (v != null) {
						Enumeration enum = v.elements();
						while (enum.hasMoreElements()) {
							session.removeValue((String)enum.nextElement());
						}
						session.removeValue("MrSendList_headerIdList");
					}
					//y-yamada add 1011 start
					session.removeValue("date_order");
					session.removeValue("user_order");
					session.removeValue("read_order");
					session.removeValue("order");
					session.removeValue("sort");
					//y-yamada add 1011 start
				}

				//�������ƍ���̂łƂ��Ă���
//  				if ( keys.hasMoreElements() ) {
					sort  = (String)session.getValue("sort");
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


//  					if ( session.getValue("sort") == null ) {
//  						sort    = "receive_time";
//  					} else {
//  						sort    = (String)session.getValue("sort");
//  					}
//  					if ( session.getValue("user_order") == null ) {
//  						user_order      = "A";
//  					} else {
//  						user_order = (String)session.getValue("user_order");
//  					}
//  					if ( session.getValue("target_order") == null ) {
//  						target_order      = "A";
//  					} else {
//  						target_order = (String)session.getValue("target_order");
//  					}
//  					if ( session.getValue("date_order") == null ) {
//  						date_order      = "D";
//  					} else {
//  						date_order = (String)session.getValue("date_order");
//  					}
//  					if ( session.getValue("read_order") == null ) {
//  						read_order      = "A";
//  					} else {
//  						read_order = (String)session.getValue("read_order");
//  					}
//  					if ( session.getValue("unread_order") == null ) {
//  						unread_order      = "A";
//  					} else {
//  						unread_order = (String)session.getValue("unread_order");
//  					}
//  				}
  
				// reset of session 
				sm.reset(request,0);

				// �ۑ��܂��͍폜����
				session.putValue("MrSendList_saveconf"," ");
				session.putValue("MrSendList_saveaction"," ");
				session.putValue("MrSendList_torikeshiconf"," ");
				session.putValue("MrSendList_torikeshiaction"," ");
				session.putValue("MrSendList_removeconf"," ");
				session.putValue("MrSendList_removeaction"," ");
				session.putValue("MrSendList_msg"," ");
   
				if (request.getParameter("MrSendList_submitname") != null) {
					// �u�ۊǂ���v�̃C���[�W���������Ƃ�
					if ( request.getParameter("MrSendList_submitname").equals("SavePre") ) {
						if ( inputCheck(request) ) {
							//         session.putValue("saveconf","ON");
							getCheckBox(request,response,session);
							// DB connection
							DBConnect dbconn = new DBConnect();
							Connection conn = dbconn.getDBConnect();
							boolean ret = false;

							try {
								// �������\�b�h�̌Ăяo���̂���
								MsgManager msgmanager = new MsgManager(conn);
								String[] e = request.getParameterValues("msg_head_id");
								String mrid = (String)session.getValue("com_mrid");
							
								// �����̃`�F�b�N������
								ret = msgmanager.isSendSaveBoxCheck(mrid,e.length);
							} finally {
								// DBclose
								dbconn.closeDB(conn);
							}

							if ( ret ) {
								session.putValue("MrSendList_msg","saveOK");
								getCheckBox(request,response,session);
								// �Z�[�u����
								msgSave(request,response,session);
							}
							else {
								session.putValue("MrSendList_msg","kensuNG");
								getCheckBox(request,response,session);
							}
						}
						else {
							session.putValue("MrSendList_saveconf","OFF");
						}
					}
					// �u���ݔ��ցv�̃C���[�W���������Ƃ�
					else if ( request.getParameter("MrSendList_submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("MrSendList_removeconf","ON");
							getCheckBox(request,response,session);
						}
						else {
							session.putValue("MrSendList_removeconf","OFF");
						}
					}
					// �u���ݔ���OK/Cancel�v�̃{�^�����o���Ƃ�
					else if ( request.getParameter("MrSendList_submitname").equals("Remove") ) {
						// �u���ݔ���OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("MrSendList_removeaction").equals("OK") ) {
							session.putValue("MrSendList_msg","removeOK");
							getCheckBox(request,response,session);
							// �����[�u����
							msgRemove(request,response,session);
						}
						// �u���ݔ���Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if (request.getParameter("MrSendList_removeaction").equals("Cancel")) {
							session.putValue("MrSendList_msg","removeCancel");
							getCheckBox(request,response,session);
						}

					}
					// �u�ۊǂ�OK/Cancel�v�̃{�^�����o���Ƃ�(2��ځj
					else if ( request.getParameter("MrSendList_submitname").equals("SaveCnt") ) {
						// �u�ۊǂ�OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("MrSendList_action").equals("OK") ) {
							session.putValue("MrSendList_msg","CntOK");
							getCheckBox(request,response,session);
							// �Z�[�u����
							msgSave(request,response,session);
						}
						// �u�ۊǂ�Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if ( request.getParameter("MrSendList_action").equals("Cancel") ) {
							session.putValue("MrSendList_msg","CntCancel");
							getCheckBox(request,response,session);
						}
     
					}
					// �u���ǂ̏ꍇ���M�������v�̃C���[�W�������ꂽ�Ƃ�
					else if (request.getParameter("MrSendList_submitname").equals("TorikeshiPre")) {
						// �u�������v�̃C���[�W���������Ƃ�
						if ( inputCheck(request) ) {
							//         session.putValue("torikeshiconf","ON");
							//         getCheckBox(request,response,session);
							getCheckBox(request,response,session);
							// ������
							if (msgTorikeshi(request,response,session) > 0) {
								session.putValue("MrSendList_msg","torikeshiOK");
							} else {
								session.putValue("MrSendList_torikeshiconf","OFF");
							}
						}
						else {
							session.putValue("MrSendList_torikeshiconf","OFF");
						}
					}
				}

				// set of sort
				setSort(request,
						session,
						sort,
						order,
						date_order,
						target_order,
						user_order,
						read_order,
						unread_order);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrSendList/index.html");
			} else {
				// �Z�b�V�����G���[�̏ꍇ
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
		session.putValue("MrSendList_headerIdList", list);
    } 

    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Vector msgid = new Vector();
		String[] e = req.getParameterValues("msg_head_id");
		for ( int index = 0; index < e.length; index++ ) {
			msgid.addElement(e[index]);
			session.putValue(e[index], "ON");
		}
		session.putValue("MrSendList_headerIdList", msgid);
		return msgid.elements();
    }
      
    boolean inputCheck(HttpServletRequest req) {
		boolean ret = true;
		if ( req.getParameter("msg_head_id") == null ) {
			ret = false;
		}
		return ret;
    }

    void msgSave(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			Enumeration  idlist = getMsgHeadId(req, session);

			MsgManager manager = new MsgManager(conn);
			manager.updateMrSendMsgStatus(idlist, SysCnst.MSG_STATUS_SAVE);
		} finally {
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
			Enumeration  idlist = getMsgHeadId(req, session);

			MsgManager manager = new MsgManager(conn);
			manager.updateMrSendMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    int msgTorikeshi(HttpServletRequest req,
					 HttpServletResponse res,
					 HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			Enumeration  idlist = getMsgHeadId(req, session);

			MsgManager manager = new MsgManager(conn);
			return manager.updateMrSendCancel(idlist);
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
				 String user_order,
				 String read_order,
				 String unread_order) {

		if (req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			if (sort == null) {
				sort = "to_userid";
			}

			// ����
			if (sort.equals("to_userid")) {
				user_order      = (user_order.equals("A"))? "D":"A";
				order           = user_order;
			} 
			// �^�[�Q�b�g�����N
			else if (sort.equals("target_rank")) {
				target_order      = (target_order.equals("A"))? "D":"A";
				order           = target_order;
			}
			// ���M����
			else if (sort.equals("receive_time")) {
				date_order      = (date_order.equals("A"))? "D":"A";
				order           = date_order;
			}
			// ����
			else if (sort.equals("receive_timed")) {
				read_order      = (read_order.equals("A"))? "D":"A";
				order           = read_order;
			}
			// ���Ǔ���
			else if (sort.equals("unread_day")) {
				unread_order      = (unread_order.equals("A"))? "D":"A";
				order           = unread_order;
			}
		}

		session.putValue("user_order", user_order);
		session.putValue("target_order",target_order);
		session.putValue("date_order", date_order);
		session.putValue("read_order", read_order);
		session.putValue("unread_order",unread_order);
		session.putValue("order"     , order);
		session.putValue("sort"      , sort);
    }
}
