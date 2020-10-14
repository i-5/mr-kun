package medipro.dr;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro DR��M���b�Z�[�W�ꗗ
 */
public class ReceiveList extends HttpServlet {

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
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				if (request.getParameter("init") != null) {
					Vector list = (Vector)session.getValue("DrReceiveList_headerIdList");
					if (list != null) {
				
						Enumeration enum = list.elements();
						while (enum.hasMoreElements()) {
							session.removeValue((String)enum.nextElement());
						}
						session.removeValue("DrReceiveList_headerIdList");
					}
					session.removeValue("date_order");
					session.removeValue("user_order");
					session.removeValue("read_order");
					session.removeValue("order");
					session.removeValue("sort");
				}

				Enumeration keys  = request.getParameterNames();

				//�������ƍ���̂Ŏ���Ă���
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
//  					if ( session.getValue("user_order") == null ) {
//  						user_order  = "A"; 
//  					} else {
//  						user_order = (String)session.getValue("user_order");
//  					}
//  					if ( session.getValue("read_order") == null ) {
//  						read_order  = "A";
//  					} else { 
//  						read_order = (String)session.getValue("read_order");
//  					}
//  				}
    
				// reset of session
				sm.reset(request,0);

				// �ۑ��܂��͍폜����
				session.putValue("DrReceiveList_saveconf"," ");
				session.putValue("DrReceiveList_saveaction"," ");
				session.putValue("DrReceiveList_moveconf"," ");
				session.putValue("DrReceiveList_moveaction"," ");
				session.putValue("DrReceiveList_removeconf", " ");
				session.putValue("DrReceiveList_removeaction", " ");
				session.putValue("DrReceiveList_action", " ");
				session.putValue("DrReceiveList_msg"," ");
   

				// DB connection
				DBConnect dbconn = new DBConnect();
				Connection conn = dbconn.getDBConnect();

				// �������\�b�h�̌Ăяo���̂���
				MsgManager msgmanager = new MsgManager(conn);
				String[] e = request.getParameterValues("msg_head_id");
				String drid = (String)session.getValue("com_drid");

				if ( request.getParameter("DrReceiveList_submitname") != null ) {
					// �u�ۊǂ���v�̃C���[�W���������Ƃ�
					if  ( request.getParameter("DrReceiveList_submitname").equals("SavePre") ) {
						if ( inputCheck(request) ) {

//  				session.putValue("saveconf","ON");
//  				getCheckBox(request,response,session);
							// �����̃`�F�b�N������
							boolean ret = false;
							try {
								ret = msgmanager.isRecvSaveBoxCheck(drid,e.length);
							} finally {
								// DBclose
								dbconn.closeDB(conn);
							}
							if ( ret ) {
								session.putValue("DrReceiveList_msg","saveOK");
								getCheckBox(request,response,session);
								// �Z�[�u����
								msgSave(request,response,session);
							}
							else {
								session.putValue("DrReceiveList_msg","kensuNG");
								getCheckBox(request,response,session);
							}
						}
						else {
					
							session.putValue("DrReceiveList_saveconf","OFF");
						}
					}
					// �u���ݔ��ցv�̃C���[�W���������Ƃ�
					else if ( request.getParameter("DrReceiveList_submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {

							session.putValue("DrReceiveList_removeconf","ON");
							getCheckBox(request,response,session);
						}
						else {
					
							session.putValue("DrReceiveList_removeconf","OFF");
						}
					}
					// �u���ݔ���OK/Cancel�v�̃{�^�����o���Ƃ�
					else if ( request.getParameter("DrReceiveList_submitname").equals("Remove") ) {
				
						// �u���ݔ���OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("DrReceiveList_removeaction").equals("OK") ) {
							session.putValue("DrReceiveList_msg","removeOK");
							getCheckBox(request,response,session);
							// �����[�u����
							msgRemove(request,response,session);
						}
						// �u���ݔ���Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if ( request.getParameter("DrReceiveList_removeaction").equals("Cancel") ) {

							session.putValue("DrReceiveList_msg","removeCancel");
							getCheckBox(request,response,session);
						}

					}
					// �u�ۊǂ�OK/Cancel�v�̃{�^�����o���Ƃ�(2��ځj
					else if ( request.getParameter("DrReceiveList_submitname").equals("SaveCnt") ) {
				
						// �u�ۊǂ�OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("DrReceiveList_action").equals("OK") ) {

							session.putValue("DrReceiveList_msg","CntOK");
							getCheckBox(request,response,session);
							// �Z�[�u����
							msgSave(request,response,session);
						}
						// �u�ۊǂ�Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if ( request.getParameter("DrReceiveList_action").equals("Cancel") ) {
					
							session.putValue("DrReceiveList_msg","CntCancel");
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
				// response.sendRedirect("/medipro/dr/DrReceiveList/index.html");
				response.sendRedirect("/medipro/dr/DrReceiveList/index.html?" + request.getQueryString());
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
	
		session.putValue("DrReceiveList_headerIdList", list);
    } 

    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Vector msgid = new Vector();
		String[] e = req.getParameterValues("msg_head_id");
		for ( int index = 0; index < e.length; index++ ) {
			msgid.addElement(e[index]);
			session.putValue(e[index],"ON");
		}
		session.putValue("DrReceiveList_headerIdList", msgid);
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

		// DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateDrRecvMsgStatus(idlist, SysCnst.MSG_STATUS_SAVE);
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}
    }

    void msgRemove(HttpServletRequest req,
				   HttpServletResponse res,
				   HttpSession session) {
		Enumeration  idlist = getMsgHeadId(req, session);

		// DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			MsgManager msgmanager = new MsgManager(conn);
			msgmanager.updateDrRecvMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
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

		String oldorder, neworder;

		if ( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// ��M����
			if ( sort.equals("receive_time") ) {
				date_order = (date_order.equals("A"))? "D":"A";
				order  = date_order;
			}
			// ���M��
			else if ( sort.equals("from_userid") ) {
				user_order = (user_order.equals("A"))? "D":"A";
				order   = user_order;
			}
			//�@����
			else if ( sort.equals("receive_timed") ) {
				read_order = (read_order.equals("A"))? "D":"A";
				order  = read_order;
			}
		}

		session.putValue("date_order", date_order);
		session.putValue("user_order", user_order);
		session.putValue("read_order", read_order);
		session.putValue("order"     , order);
		session.putValue("sort"     , sort);
    }

} 
