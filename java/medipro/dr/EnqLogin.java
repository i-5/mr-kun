package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.security.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;

import java.net.URLDecoder;//1106 y-yamada add

/**
 * Medipro DR�t�����g����
 */
public class EnqLogin extends HttpServlet {
    //���o�^���J�ڃy�[�W
//    static final String ENTRY_PAGE = "/medipro/dr/DrFront/mr_regist.jsp";
    //�s���A�N�Z�X���J�ڃy�[�W
    static final String ILLEGAL_PAGE = "/medipro/err/Err.jsp";
    //���펞�J�ڃy�[�W
//    static final String FRONT_PAGE = "/medipro/dr/DrFront/index.jsp";
    static final String ENQUETE_PAGE = "/medipro/dr/DrEnquete/mainframe.jsp";
    private boolean debugMode = false;
    private String KeyCd = "ENQSYS";

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
		    HttpSession session = request.getSession(true);
		
			String nextPage = null;

		    // session check & reset
		    if (!session.isNew()) {
				session.invalidate();
				session = request.getSession(true);
		    }

		    //���ڃ��O�C��
		    String SysCd = request.getParameter("code");
		    String HashMd5 = request.getParameter("pid");

		    //Hash�R�[�h�ϊ�
		    String str = SysCd + KeyCd;
		    
		    byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
		    StringBuffer s = new StringBuffer();
		    for (int i = 0; i < digest.length; i++) {
			s.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
			s.append(Integer.toString(digest[i] & 0x0f, 16));
		    }

		    //��r
		    if ( !HashMd5.equals( s.toString() ) ){
				log("HashMd5=" + HashMd5);
				log("SysHash=" + s.toString());
				log("str=" + str);
				nextPage = ILLEGAL_PAGE;
		    } else {
				String sessionId = HtmlTagUtil.getRandomId();
				session.putValue(SysCnst.MR_SESSION_ID, sessionId);

				DoctorInfo info = getDoctorInfoBySystemCd(SysCd);
				//DB�ɓo�^����Ă��邩�`�F�b�N
				if (info.getDrID() == null) {
				    nextPage = ILLEGAL_PAGE;
				    session.putValue("DrEntry_drId", info.getDrID());
				    session.putValue("DrEntry_init", "yes");
				} else {
				    initSession(request, info);
				    nextPage = ENQUETE_PAGE;
				}
		    }
		
			/**************************************************************
			 *  ��t��user-agent��logindr�e�[�u���ɕۑ����� 1207 y-yamada add
			 ***************************************************************/
//			if(drId != null ) {
//				new AccessAnalyzer(drId).analyze(request);
//			}

			// Go to the next page
			response.sendRedirect(nextPage);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    /**
     * �J�� or �e�X�g���p
     */
/***
    private String getCookieID(HttpServletRequest request) {
		// initialization
		String id = null;
		Cookie thisCookie = null;

		// cookie�̎��f�[�^�����ׂĂƂ��Ă���
		Cookie[] cookies = request.getCookies();

		for(int i=0; i < cookies.length; i++) {
			thisCookie = cookies[i];
			if (thisCookie.getName().equals("drID")) {
				return thisCookie.getValue();
			}
		}

		return null;
    }
***/
    /**
     * �w�肵��DR-ID��DB�ɓo�^����Ă��邩�`�F�b�N����
     */
    private DoctorInfo getDoctorInfoBySystemCd(String SysCd) {
        DBConnect dbConnect = new DBConnect();
        Connection connection  = null;

		try {
			connection = dbConnect.getDBConnect();
			DoctorInfoManager manager = new DoctorInfoManager(connection);
			return manager.getDoctorInfoBySystemCd(SysCd);
		} finally {
			dbConnect.closeDB(connection);
		}
    }

    /**
     * �Z�b�V�����֍Œ���K�v�ȏ���ςݍ���
     */
    private void initSession(HttpServletRequest request, DoctorInfo info) {
		//�ŏ��ɃZ�b�V�����̏�����
		HttpSession session = request.getSession(true);
		if (session.isNew() == false) {
			session.invalidate();
			session = request.getSession(true);
		}

		// �Z�b�V�����L�����Ԑݒ�
		int session_time = Integer.parseInt(getConstTable("TIMEOUT", 1));
		session.setMaxInactiveInterval(session_time);

		session.putValue("com_drid", info.getDrID());
		session.putValue("com_DrName", info.getName());
		String mishiyoFlg = info.getMrkunMishiyoFlg();

		if (mishiyoFlg.equals("1")) {
			session.putValue("com_MrMishiyo", "ON");
		}

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

    /**
     * �萔�}�X�^����l���擾����.
     */
    private String getConstTable(String keyword, int colno) {
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
