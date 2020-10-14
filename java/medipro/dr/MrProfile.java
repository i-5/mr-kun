package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * Medipro DRMR�l���
 */
public class MrProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

				// reset of session
				sm.reset(request,0);

				// initialization
				session.putValue("memoconf"," ");
				session.putValue("kaijoconf"," ");
				session.putValue("kaijoaction"," ");
				session.putValue("msg"," ");

				// �u������ۑ�����v�������ꂽ�Ƃ�
				if ( request.getParameter("Memo") != null ) {
					// �ۑ�����
					session.getValue("memo");
					msgMemo(request,response,session);
					session.putValue("memoconf","ON");
				}

				// �uMR�̓o�^����������v�{�^���������ꂽ�Ƃ�
				if ( request.getParameter("KaijoPre") != null ) {
					session.putValue("kaijoconf","ON");
				}

				// �uMR�o�^����OK�v�������ꂽ��
				if ( request.getParameter("submitname") != null ) {
					if ( request.getParameter("submitname").equals("Kaijo") ) {
						if ( request.getParameter("kaijoaction").equals("OK") ) {
							session.putValue("msg","kaijoOK");
							msgKaijo(request,response,session);
						}
						else if ( request.getParameter("kaijoaction").equals("Cancel") ) {
							session.putValue("msg","kaijoCancel");    
						}
					}
				}

				if (request.getParameter("nomr") != null) {
					session.removeValue("nomr");
					response.sendRedirect("/medipro.dr.MrRollList");
					return;
				}

				// Go to the next page
				response.sendRedirect("/medipro/dr/DrMrProfile/index.html");
			} else {
				// �Z�b�V�����G���[�̏ꍇ
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response,e);
		}
    }

    void msgMemo(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		// �ۑ����郁���̓��e���Q�b�g
		String drid = (String)session.getValue("com_drid");
		String mrid = (String)session.getValue("mrid");
		String mem = (String)session.getValue("memo");

		//  DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			TantoInfoManager tantomanager = new TantoInfoManager(conn);
			tantomanager.updateDrMemo(drid, mrid ,mem );
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}

    }

    void msgKaijo(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		// �������邽�߂̓��e���Q�b�g
		String drid = (String)session.getValue("com_drid");
		String mrid = (String)session.getValue("mrid");

		//  DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			//MR�ɍ폜�ʒm
			sendRemovedMessage(drid, mrid);

			TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
			tantoinfomanager.insertSentakuTourokuHist(drid, mrid);
		} finally {
			// DB close
			dbconn.closeDB(conn);
		}
    }

    /**
     * MR�ɍ폜�ʒm�𑗕t����.
     * @param drId DR-ID
     * @param mrId MR-ID
     */
    void sendRemovedMessage(String drId, String mrId) {
		DBConnect dbConnect = new DBConnect();
		Connection connection = dbConnect.getDBConnect();

		try {
			MessageTableManager manager = new MessageTableManager(connection);
			MessageTable message = new MessageTable();
			MessageHeaderTable header = new MessageHeaderTable();
			MessageBodyTable body = new MessageBodyTable();

			TantoInfoManager tantoManager = new TantoInfoManager(connection);
			TantoInfo tantoInfo = tantoManager.getDrInfo(mrId, drId);
			String drName = tantoInfo.getName() == null ? "" : tantoInfo.getName();
			String kinmusaki
				= tantoInfo.getKinmusaki() == null ? "" : tantoInfo.getKinmusaki();

			header.setMessageKbn(SysCnst.MESSAGE_KBN_TO_OTHER);
			header.setFromUserID(drId);
			body.setTitle("MR�폜�ʒm:" + drName + " " + kinmusaki);
			body.setMessageHonbun("MR���납��폜����܂����B\n"
								  + "�ڋq:" + drName + " " + kinmusaki);

			message.setMsgHTable(header);
			message.setMsgBTable(body);

			Vector toList = new Vector();
			toList.addElement(mrId);

			//-- �Y�t�E�����N�t�@�C���Z�b�g�i���e�J���j
			Vector attach = new Vector();
			message.setAttachFTable(attach.elements());
			message.setAttachLTable(attach.elements());

			manager.insert(toList.elements(), message);
		} finally {
			dbConnect.closeDB(connection);
		}
    }

}
