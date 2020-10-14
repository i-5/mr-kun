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
 * Medipro MR�R�~���j�P�|�V��������
 */
public class CommunicationHistory extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String M19_sort = "receive_time";	//�����\�[�g����
		String M19_order = "D";				//�����\�[�g����
		String M19_date_order = "D";
		String M19_title_order = "A";
		String M19_status_order = "A";
		String M19_read_order = "A";
		String M19_unread_order = "A";

		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if (sm.check(request, 1)) {
				HttpSession session = request.getSession(true);

				//������
				if (request.getParameter("init") != null) {
					session.removeValue("msgid_hash");
					//y-yamada 1011 add  start  ���Z�b�g�l���Z�b�V�����ɐݒ肷��
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

				//�������ƍ���̂łƂ��Ă���
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

				// �ۑ��܂��͍폜����
				session.removeValue("msgid_hash"); // �O�̏�������
				getCheckBox(request,response,session); //�V�n�b�V���̃Z�b�V�������쐬
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

				// �������\�b�h�̌Ăяo���̂���
				MsgManager msgmanager = new MsgManager(conn);
				String[] e = request.getParameterValues("msg_head_id");
				String mrid = (String)session.getValue("com_mrid");

				if (request.getParameter("submitname") != null) {
					// �u�ۊǂ���v�̃C���[�W���������Ƃ�
					if (request.getParameter("submitname").equals("SavePre")) {
						if (inputCheck(request)) {
//  							session.putValue("M19_saveconf","ON");
							session.putValue("M19_msg","saveOK");
							// �Z�[�u����
							msgSave(request,response,session);
						} else {
							session.putValue("M19_saveconf","OFF");
						}
					}
					// �u���ݔ��ցv�̃C���[�W���������Ƃ�
					else if ( request.getParameter("submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("M19_removeconf","ON");
						} else {
							session.putValue("M19_removeconf","OFF");
						}
					}
					// �u���ݔ���OK/Cancel�v�̃{�^�����o���Ƃ�
					else if ( request.getParameter("submitname").equals("Remove") ) {
						// �u���ݔ���OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("M19_removeaction").equals("OK") ) {
							session.putValue("M19_msg","removeOK");
							// �����[�u����
							msgRemove(request,response,session);
						}
						// �u���ݔ���Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if ( request.getParameter("M19_removeaction").equals("Cancel") ) {
							session.putValue("M19_msg","removeCancel");
						}

					}
					// �u���ǂ̏ꍇ���M�������v�̃C���[�W�������ꂽ�Ƃ�
					else if ( request.getParameter("submitname").equals("TorikeshiPre") ) {
						// �u�������v�̃C���[�W���������Ƃ�
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
					// �u��������OK/Cancel�v�̃{�^�����o���Ƃ�
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

    // �ǂ̃{�^����������Ă�������ʂ��悤�ɂ��Ă�̂ŁA�`�F�b�N����Ēl������ꍇ�A
    // ����msg_head_id��get����ăn�b�V���̒��ɖ��߂��A�ЂƂ̃Z�b�V�����ɂȂ�
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

    // ��ō�����n�b�V�����x�N�^�[�ɓ���Ă�
    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Hashtable msg_hash = (Hashtable)session.getValue("msgid_hash");
		String  msgbuff  = null;
		Vector  msg_vec  = new Vector();
		Enumeration enum  = msg_hash.elements(); // �l�̕�����z��        �ɂ��Ď��o��
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
			// Message Header ID �擾
			Enumeration  idlist = getMsgHeadId(req, session);
			Enumeration  idlist2 = getMsgHeadId(req, session);

			// �ۊǏ���
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
			// Message Header ID �擾
			Enumeration  idlist = getMsgHeadId(req, session);
			Enumeration  idlist2 = getMsgHeadId(req, session);

			// ���ݔ�����
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
			// Message Header ID �擾
			Enumeration  idlist = getMsgHeadId(req, session);

			// ���M����������
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

			// ����M
			if ( M19_sort.equals("message_kbn") ) {
				M19_status_order = (M19_status_order.equals("A")) ? "D":"A";
				M19_order = M19_status_order;
			}
			// ����
			else if ( M19_sort.equals("receive_time") ) {
				M19_date_order = (M19_date_order.equals("A")) ? "D":"A";
				M19_order = M19_date_order;
			}
			// �^�C�g��
			else if ( M19_sort.equals("title") ) {
				M19_title_order = (M19_title_order.equals("A")) ? "D":"A";
				M19_order = M19_title_order;
			}
			// ����
			else if ( M19_sort.equals("receive_timed") ) {
				M19_read_order = (M19_read_order.equals("A")) ? "D":"A";
				M19_order = M19_read_order;
			}
			// ���Ǔ���
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
		// ��ʖ��̃Z�b�g
		session.putValue("gamen"      , "M19");
	}
}
