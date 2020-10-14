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
* Medipro DR���b�Z�[�W�쐬
*/

public class MessageBuild extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
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

				// ���M�O�m�F
				if (request.getParameter("submitaction") != null) {
					//�`�F�b�N�{�b�N�X�I���̕ێ�
					setCheckedList(request, session);

					if (isChecked(request, session)) {
						if (existOutOfWorkTimeMr(request)) {
							session.putValue("confirm","ON");
						} else {
							sendMessageGo(request, response, session);
							session.putValue("sendmail","OK");
						}
					} else {  // ����Ȃ�
						session.putValue("no_check_to","NG");
					}
				}
				// ���b�Z�[�W���M
				else if (request.getParameter("send_ok") != null) {
					session.removeValue("confirm");
					//�`�F�b�N�{�b�N�X�I���̕ێ�
					setCheckedList(request, session);

					if (isChecked(request, session)) {
						sendMessageGo(request, response, session);
						session.putValue("sendmail","OK");
					} else {  // ����Ȃ�
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

    /**
     * �c�Ǝ��ԊO��MR��I�����Ă��Ȃ����`�F�b�N����.
     * @return �I������Ă�����true
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
     * ����I���Ń`�F�b�N���ꂽMR-ID�ꗗ���n�b�V���Ɋi�[����.
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
     * ����I������Ă��邩�`�F�b�N����.
     * @return ��l�ł��I������Ă�����true
     */
    boolean isChecked(HttpServletRequest req, HttpSession session) {
		if (req.getParameterValues("mr_id") == null) {
			return false;
		} else {
			return true;
		}
    }

    /**
     * ���b�Z�[�W���M.
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

		// �I�����ꂽMR���Z�b�g
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

        //-- �Y�t�E�����N�t�@�C���Z�b�g�i���e�J���j
		AttachFileTable  atfiletbl = new AttachFileTable();
		AttachLinkTable  atlinktbl = new AttachLinkTable();
		Vector  attachtbl_work  = new Vector(); 

		msgtbl.setAttachFTable(attachtbl_work.elements());
		msgtbl.setAttachLTable(attachtbl_work.elements());

		try {
			// Message���M
			String msgID = manager.insert(mr.elements(), msgtbl);
		} finally {
			dbconn.closeDB(conn);
		}
    }

}
