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
 * Medipro DR���M�ۊ�Box
 */
public class SendSaveBoxList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "receive_time";
		String order = "D";
		String date_order = "D";
		String user_order = "A";

		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				if (request.getParameter("init") != null) {
					Vector list = (Vector)session.getValue("DrSendSaveBoxList_headerIdList");
					if (list != null) {
						Enumeration enum = list.elements();
						while (enum.hasMoreElements()) {
							session.removeValue((String)enum.nextElement());
						}
						session.removeValue("DrSendSaveBoxList_headerIdList");
					}
		    
				}

				Enumeration keys = request.getParameterNames();

				//�������ƍ���̂łƂ��Ă���
//  				if ( keys.hasMoreElements() ) {
					sort = (String)session.getValue("sort");
					date_order = (String)session.getValue("date_order");
					user_order = (String)session.getValue("user_order");

					sort = sort == null ? "receive_time" : sort;
					date_order = date_order == null ? "D" : date_order;
					user_order = user_order == null ? "A" : user_order;

//  					if      ( session.getValue("sort") == null ) {
//  						sort    = "receive_time";
//  					} else {
//  						sort    = (String)session.getValue("sort");
//  					}
//  					if      ( session.getValue("date_order") == null ) {
//  						date_order      = "D";
//  					} else {
//  						date_order = (String)session.getValue("date_order");
//  					}
//  					if      ( session.getValue("user_order") == null ) {
//  						user_order      = "A";
//  					} else {
//  						user_order = (String)session.getValue("user_order");
//  					}
//  				}

				// reset of session
				sm.reset(request,0);

				// �ۑ��܂��͍폜����
				session.putValue("DrSendSaveBoxList_saveconf"," ");
				session.putValue("DrSendSaveBoxList_saveaction"," ");
				session.putValue("DrSendSaveBoxList_moveconf"," ");
				session.putValue("DrSendSaveBoxList_moveaction"," ");
				session.putValue("DrSendSaveBoxList_removeconf"," ");
				session.putValue("DrSendSaveBoxList_removeaction"," ");
				session.putValue("DrSendSaveBoxList_action"," ");
				session.putValue("DrSendSaveBoxList_msg"," ");

				if ( request.getParameter("DrSendSaveBoxList_submitname") != null ) {
					// �u���ݔ��ցv�̃C���[�W���������Ƃ�
					if ( request.getParameter("DrSendSaveBoxList_submitname").equals("RemovePre") ) {
						if ( inputCheck(request) ) {
							session.putValue("DrSendSaveBoxList_removeconf","ON");
							getCheckBox(request,response,session);
						}
						else {
							session.putValue("DrSendSaveBoxList_removeconf","OFF");
						}
					}
					// �u���ݔ���OK/Cancel�v�̃{�^�����o���Ƃ�
					else if ( request.getParameter("DrSendSaveBoxList_submitname").equals("Remove") ) {
						// �u���ݔ���OK�v�̃{�^���������ꂽ�Ƃ�
						if ( request.getParameter("DrSendSaveBoxList_removeaction").equals("OK") ) {
							session.putValue("DrSendSaveBoxList_msg","removeOK");
							getCheckBox(request,response,session);
							// �����[�u����
							msgRemove(request,response,session);
						}
						// �u���ݔ���Cancel�v�̃{�^���������ꂽ�Ƃ�
						else if ( request.getParameter("DrSendSaveBoxList_removeaction").equals("Cancel") ) {
							session.putValue("DrSendSaveBoxList_msg","removeCancel");
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
						user_order);

				// Go to the next page
				// response.sendRedirect("/medipro/dr/DrSendSaveBoxList/index.html");
				response.sendRedirect("/medipro/dr/DrSendSaveBoxList/index.html?" + request.getQueryString());
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
		session.putValue("DrSendSaveBoxList_headerIdList", list);
    }

    Enumeration getMsgHeadId(HttpServletRequest req, HttpSession session) {
		Vector msgid = new Vector();
		String[] e = req.getParameterValues("msg_head_id");
		for ( int index = 0; index < e.length; index++ ) {
			msgid.addElement(e[index]);
			session.putValue(e[index], "ON");
		}
		session.putValue("DrSendSaveBoxList_headerIdList", msgid);
		return msgid.elements();
    }

    boolean inputCheck(HttpServletRequest req) {
		boolean ret = true;
		if ( req.getParameter("msg_head_id") == null ) {
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
			Enumeration idlist   = getMsgHeadId(req,session);

			MsgManager manager = new MsgManager(conn);
			manager.updateDrSendMsgStatus(idlist, SysCnst.MSG_STATUS_DUST);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String sort,
				 String order,
				 String date_order,
				 String user_order) {

		if ( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// ��M����
			if ( sort.equals("receive_time") ) {
				date_order = (date_order.equals("A"))? "D":"A";
				order  = date_order;
			}
			// ����
			else if ( sort.equals("to_userid") ) {
				user_order = (user_order.equals("A"))? "D":"A";
				order  = user_order;
			}
		}

		session.putValue("date_order", date_order);
		session.putValue("user_order", user_order);
		session.putValue("order"     , order);
		session.putValue("sort"      , sort);
    }
}
