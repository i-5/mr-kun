package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro DR�v���t�B�[���X�V���
 */
public class UpdProfileConf extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			String nextpage = null;

			// �������y�[�W����̑J�ڂ����`�F�b�N
			if ( request.getHeader("Referer") == null ) {
				nextpage = "/medipro/err/Cookie.jsp";
			} else {
				// Referer���URL���擾���A�g�������A�啶���ϊ�����B
				String getpage   = request.getHeader("Referer").trim();
				getpage   = getpage.toUpperCase();

				// �萔�}�X�^���O��ʂ�URL���擾
				String rightpage   = getProfileUpdURL("PROFILEUPD_URL",1);
				if ( !getpage.equals(rightpage) ) {
					nextpage = "/medipro/err/Cookie.jsp";
				} else {
					// get Cookie
					// �N�b�L�[���������Ƃ�邩�̃`�F�b�N
					String DrID  = getCookieID(request);
					if ( DrID == null ) {
						nextpage = "/medipro/err/Cookie.jsp";
					}
					else {
						// DB Connection
						DBConnect dbconn = new DBConnect();
						Connection conn  = dbconn.getDBConnect();
						HttpSession session     = request.getSession(true);
						// �Z�b�V�����L�����Ԑݒ�
						int session_time = Integer.parseInt(getConstTable("TIMEOUT", 1));
						session.setMaxInactiveInterval(session_time);
  
						// set ��MBOX Max Count 
						String receive_cnt = getConstTable("RECEIVEMAX", 1);
						session.putValue("com_receive_cnt", receive_cnt);
   
						// set ��M�ۊ�BOX Max Count 
						String receive_save_cnt = getConstTable("RECEIVEMAX", 2);
						session.putValue("com_receive_save_cnt", receive_save_cnt);
   
						// set ���MBOX Max Count 
						String send_cnt = getConstTable("SENDMAX", 1);
						session.putValue("com_send_cnt", send_cnt);
   
						// set ���M�ۊ�BOX Max Count 
						String send_save_cnt = getConstTable("SENDMAX", 2);
						session.putValue("com_send_save_cnt", send_save_cnt);
  
						// MR���g�p�t���O�ɂ���ʂ�؂�ւ�
						if      ( getDrInfo(session, DrID, conn) ) {
							nextpage = "/medipro/dr/DrUpdProfile/regist.jsp";
						} else {
							nextpage = "/medipro/dr/DrUpdProfile/end.html";
						} 
    
						// DB Connection close
						dbconn.closeDB(conn);
					}
				}
			}
			// Go to the next page
			response.sendRedirect(nextpage);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    String getProfileUpdURL(String keyword, int colno) {
		String url = null;

		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager     = new ConstantMasterTableManager(conn);
			ConstantMasterTable     constmanager    = manager.getConstantMasterTable(keyword);

			if      ( colno == 1 ) {
				// �萔�}�X�^�e�[�u�����l���擾���A���̑O��̃X�y�[�X���g����
				url   = constmanager.getNaiyo1().trim();
				// �啶���ɕϊ�
				url     = url.toUpperCase();
			}
		} finally {
			dbconn.closeDB(conn);
		}
 
		return url;
    }

    String getCookieID(HttpServletRequest request) {

		// initialization
		String id = null;
		Cookie thisCookie = null;

		// cookie�̎��f�[�^�����ׂĂƂ��Ă���
		//Cookie[] cookies = " " + request.getCookies() + ";";
		Cookie[] cookies = request.getCookies();
 
		for(int i=0; i < cookies.length; i++) {
			thisCookie = cookies[i];
			if (thisCookie.getName().equals("drID")) {
				id = thisCookie.getValue();
				break;
			}
		}
		return(id);
    }

    boolean getDrInfo(HttpSession session, String drid, Connection conn) {
		DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
		boolean            ret          = false;

		// �Z�b�V�����ɓo�^
		session.putValue("com_drid", drid);

		if      ( doctorinfomanager.getDoctorInfo(drid).getDrID() == null ) {
			session.putValue("com_MrMishiyo", "ON");
			ret = false;
		} else {
			// �Z�b�V�����ɓo�^
			session.putValue("com_DrName", doctorinfomanager.getDoctorInfo(drid).getName());
			String  mr_flg  = doctorinfomanager.getDoctorInfo(drid).getMrkunMishiyoFlg();

			if      ( mr_flg.equals("1") ) {
				session.putValue("com_MrMishiyo", "ON");
				ret = false;
			} else {
				session.removeValue("com_MrMishiyo");
				ret = true;
			}
		}

		return ret;
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

