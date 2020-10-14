package medipro.mr;

/**
* Medipro MR���O�C��
*/

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;


public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			boolean nextflg = false;
			String nextpage = null;
			String submit = (String)request.getParameter("submitaction");
			String mrName = "";

			HttpSession session = request.getSession(true);
			// �O�̂��߃N���A
			session.removeValue("mr_login_err");
			session.removeValue("mr_pwdchg_err");
			session.removeValue("login_mrid");

			if (submit.equals("login")) {
				//���O�C����
				if (loginCheck(request, conn)) {
					mrName = (String)session.getValue("com_mrname");
					nextflg  = true;
				} else {
					nextflg  = false;
					session.putValue("mr_login_err", "pwderror");
				}
			} else if (submit.equals("passchg")) {
				//�p�X���[�h�ύX��
				if (passwdChange(request, conn)) {
					nextflg  = true;
				} else {
					nextflg  = false;
					session.putValue("mr_pwdchg_err", "pwderror");
				}
			}

			if (nextflg) {
				// ���O�C�����Ԃ̍X�V
				loginUpdate(request, conn);
		
				/**************************************************************
				 * MR��user-agent��loginmr�e�[�u���ɕۑ����� 1212 y-yamada add
				 ***************************************************************/
				String mrId = request.getParameter("com_mrid");
				if( mrId != null ) {
					new AccessMrAnalyzer(mrId).analyze(request);
				}
                                
                                /* <hb010703
                                 * maybe one day I will understand this...
                                 *
				 * session check & reset
				if (session.isNew() == false) {
					 session.invalidate();
					session = request.getSession(true);
				}
                                 * hb010703> 
                                 */
				// �Z�b�V�����L�����Ԑݒ�
				int session_time = Integer.parseInt(getConstTable("TIMEOUT", 2));
				session.setMaxInactiveInterval(session_time);

				session.putValue("com_mrname", mrName);

				initSessionParameter(session);

				// set mrID 
				session.putValue("com_mrid", request.getParameter("com_mrid"));

				// Cookie Set
				Cookie my_cookie = new Cookie("Mrid", mrId);

				// 10���ԗL��
				my_cookie.setMaxAge(864000);
				my_cookie.setPath("/");
				response.addCookie(my_cookie);

				// 2001.04.24 �ǉ� -->
				// session�ɃL�[��hidden�Ƃ��Ė��ߍ���
				String sessionId = HtmlTagUtil.getRandomId();
				session.putValue(SysCnst.MR_SESSION_ID, sessionId);

//  		nextpage = "/medipro.mr.DrList";
				nextpage = "/medipro.mr.ReceiveList?ID=" + mrId;
				// <-- 2001.04.24
			} else {
				// �Z�b�V�����ɓo�^
				session.putValue("login_mrid", request.getParameter("com_mrid"));
				nextpage = "/medipro/mr/MrLogin/index.jsp";
			}

			// Go to the next page
			response.sendRedirect(nextpage);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }

    /**
     * ID��password�̑g�ݍ��킹��DB�ɓo�^����Ă��邩�`�F�b�N����.
     * �o�^����Ă���΁A�Z�b�V������MR����"com_mrname"��o�^����.
     */
    boolean loginCheck(HttpServletRequest req, Connection conn) {
		boolean  ret = true;
		String  mrid = req.getParameter("com_mrid");
		String  passwd = req.getParameter("passwd");

		MrInfoManager manager = new MrInfoManager(conn);
		MrInfo  mrinfo = manager.getMrLoginInfo(mrid);

		if (mrinfo == null) {
			ret = false;
		} else {
			String mrPwd = mrinfo.getPassword();

			if (passwd.equals(mrPwd)) {
				HttpSession session = req.getSession(true);
				session.putValue("com_mrname", mrinfo.getName());
				ret = true;
			} else {
				ret = false;
			}
		}

		return ret;
    }

    /**
     * password��ύX����.
     */
    boolean passwdChange(HttpServletRequest req, Connection conn) {
		String  mrid = req.getParameter("com_mrid");
		String  passwd1 = req.getParameter("newPasswd1");
		boolean  ret = true;

		if (loginCheck(req, conn)) {
			MrInfoManager manager = new MrInfoManager(conn);
			MrInfo  mrinfo = manager.getMrInfo(mrid);
			mrinfo.setPassword(passwd1);
			manager.updatePassword(mrid, mrinfo);
			ret = true;
		} else {
			ret = false;
		}
		return ret;
    }

    /**
     * �萔�}�X�^����A�w�肵���R�[�h�̎w�肵������ނ̓��e���擾����.
     */
    String getConstTable(String keyword, int colno) {
		String value = null;

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager = new ConstantMasterTableManager(conn);
			ConstantMasterTable constmanager = manager.getConstantMasterTable(keyword);

			if (colno == 1) {
				value = constmanager.getNaiyo1().trim();
			} else {
				value = constmanager.getNaiyo2().trim();
			}
		} finally {
			dbconn.closeDB(conn);
		}

		return value;
    }

    /**
     * �O�񃍃O�C��������A�S�����O�ꂽ��t�ꗗ���擾���܂�.
     * @param  req  HttpServletRequest�C���X�^���X
     * @param  conn �R�l�N�V����
     * @return ��t���ꗗ
     */
//      Vector getRelationEndDoctorList(HttpServletRequest req, Connection conn) {
//  	String mrId = req.getParameter("com_mrid");
//  	TantoInfoManager manager = new TantoInfoManager(conn);
//  	Vector list = manager.getEndRelationList(mrId);
//  	return list;
//      }

    /**
     * �O�񃍃O�C��������A���ǂ�����ǂɕω��������b�Z�[�W�ꗗ���擾���܂�.
     * @param  req  HttpServletRequest�C���X�^���X
     * @param  conn �R�l�N�V����
     * @return ��t���ꗗ
     */
//      Vector getReceivedMessageList(HttpServletRequest req, Connection conn) {
//  	String mrId = req.getParameter("com_mrid");
//  	MsgManager manager = new MsgManager(conn);
//  	Vector list = manager.getReceivedMessageList(mrId);
//  	return list;
//      }

    void loginUpdate(HttpServletRequest req, Connection conn) {
		String  mrid = req.getParameter("com_mrid");

		MrInfoManager manager = new MrInfoManager(conn);
		manager.updateLogin(mrid);
    }

    /**
     * �K�v�ȏ���DB����擾���A�Z�b�V�����ɃZ�b�g����.
     */
    void initSessionParameter(HttpSession session) {
		String statCall30 = getConstTable("STATGRAPH_CALL", 1);
		String statCall180 = getConstTable("STATGRAPH_CALL", 2);
		String statCust30 = getConstTable("STATGRAPH_CUST", 1);
		String statCust180 = getConstTable("STATGRAPH_CUST", 2);
		String statSend30 = getConstTable("STATGRAPH_SEND", 1);
		String statSend180 = getConstTable("STATGRAPH_SEND", 2);
		String statRatio30 = getConstTable("STATGRAPH_RATIO", 1);
		String statRatio180 = getConstTable("STATGRAPH_RATIO", 2);
		String actionPic1 = getConstTable("ACTION_PIC1", 1);
		String actionPic2 = getConstTable("ACTION_PIC2", 1);
		String actionPic3 = getConstTable("ACTION_PIC3", 1);
		String actionPic4 = getConstTable("ACTION_PIC4", 1);
		String actionPicDefault = getConstTable("ACTION_DEFAULT", 1);

		session.putValue("com_call30", statCall30);
		session.putValue("com_call180", statCall180);
		session.putValue("com_cust30", statCust30);
		session.putValue("com_cust180", statCust180);
		session.putValue("com_send30", statSend30);
		session.putValue("com_send180", statSend180);
		session.putValue("com_ratio30", statRatio30);
		session.putValue("com_ratio180", statRatio180);
		session.putValue("com_actionPic1", actionPic1);
		session.putValue("com_actionPic2", actionPic2);
		session.putValue("com_actionPic3", actionPic3);
		session.putValue("com_actionPic4", actionPic4);
		session.putValue("com_actionPicDefault", actionPicDefault);

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
    }
}
