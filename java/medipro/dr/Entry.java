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
 * ��t�o�^���Servlet�N���X.
 * Front�T�[�u���b�g�ŃZ�b�V������DrEntry_init��DrEntry_drId��
 * �Z�b�g����Ă���Γ����.
 */
public class Entry extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			String nextPage = "/medipro/dr/DrEntry/index.html";

			session.removeValue("DrEntry_confirm");

			if (session.getValue("DrEntry_init") != null &&
				session.getValue("DrEntry_drId") != null) {
				String drId = (String)session.getValue("DrEntry_drId");

				DoctorProperty prop = new DoctorProperty(drId);
				session.putValue("DrEntry", prop);

				session.removeValue("DrEntry_init");
			} else if (request.getParameter("DrEntry_submit") != null) {
				//�o�^�{�^��
				refreshSession(request, session);
				if (checkInput(session)) {
					session.putValue("DrEntry_confirm", "ON");
					nextPage = "/medipro/dr/DrEntry/entry.html";
				}
			} else if (request.getParameter("DrEntry_save") != null) {
				//�m�F�{�^��
				if (executeSave(request,response,session) > 0) {
					session.putValue("com_drid", session.getValue("DrEntry_drId"));
//					nextPage = "/medipro.dr.InitMrInput";
					nextPage = "/medipro/dr/DrEnquete/mainframe.jsp";
				}
			}

			// Go to the next page
			response.sendRedirect(nextPage);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    /**
     * �����R�[�h�ϊ�
     */
    private String getParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		try {
			return new String(value.getBytes("8859_1"), "SJIS");
		} catch (Exception e) {
			return "";
		}
    }

    /**
     *�@��ʏ����Z�b�V������...
     */
    private void refreshSession(HttpServletRequest request, HttpSession session) {
		session.removeValue("DrEntry_inputError");

		DoctorProperty prop = (DoctorProperty)session.getValue("DrEntry");

		//����
		prop.setName(getParameter(request, "DrEntry_name"));
		//����(����)
		prop.setNameKana(getParameter(request, "DrEntry_nameKana"));
		//�Ζ���
		prop.setKinmusaki(getParameter(request, "DrEntry_kinmusaki"));
		//������
		prop.setKoumuin(getParameter(request, "DrEntry_koumuin"));
		//���N����
		prop.setBirthYear(getParameter(request, "DrEntry_birthYear"));
		prop.setBirthMonth(getParameter(request, "DrEntry_birthMonth"));
		prop.setBirthDay(getParameter(request, "DrEntry_birthDay"));
		//E���[���A�h���X
		prop.setEmail(getParameter(request, "DrEntry_email"));
		prop.setReEmail(getParameter(request, "DrEntry_reEmail"));
	
    }

    private boolean checkInput(HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrEntry");

		if (!prop.isEntryCompleted() ||
			!checkEmail(prop.getEmail(), prop.getReEmail())) {
			session.putValue("DrEntry_inputError", "error");
			return false;
		}

		return true;
    }

    /**
     *�@�o�^���s
     */
    private int executeSave(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrEntry");
		int count = 0;

		DBConnect dbConnect = new DBConnect();
		Connection con = dbConnect.getDBConnect();

		try {
			//��t�e�[�u���ւ̓o�^
			DrProfileInfoManager drManager = new DrProfileInfoManager(con);
			count = drManager.insert(prop);
	    
			if (count != 1) {
				return count;
			}

			String mrId = getConstTable("ENQUETE_MR_ID", 1);
			String companyCd = getConstTable("ENQUETE_COMPANY_CD", 1);
			String pictureCd = getConstTable("ENQUETE_PICTURE_CD", 1);

			TantoInfoManager tantoInfoManager = new TantoInfoManager(con);
			tantoInfoManager.insertSentakuTouroku(prop.getDrId(), mrId);
	    
			MrInfoManager mrInfoManager = new MrInfoManager(con);
			MrInfo mrInfo = mrInfoManager.getMrInfo(mrId);

	    //�A���P�[�g�̐ݒ�
/********** MRK Ver1.6 2001.07 M.Mizuki
			MessageTableManager manager = new MessageTableManager(con);
			MessageTable table = new MessageTable();
			MessageHeaderTable header = new MessageHeaderTable();
			MessageBodyTable body = new MessageBodyTable();

			header.setMessageKbn("1");
			header.setFromUserID(mrId);
			header.setCcFlg("0");
			body.setTitle("�V�K�o�^�L��������܂��I\n�܂��͂������N���b�N�I");
			body.setCompanyCD(companyCd);
			body.setUrl("/medipro/dr/DrEnquete/mainframe.jsp");
			body.setPictureCD(pictureCd);
			body.setJikosyokai(mrInfo.getJikosyokai());
			table.setAttachFTable(new Vector().elements());
			table.setAttachLTable(new Vector().elements());

			table.setMsgHTable(header);
			table.setMsgBTable(body);
			Vector v = new Vector();
			v.addElement(prop.getDrId());

			manager.insert(v.elements(), table);
**********/

		// �A���P�[�g���M�L�^ MRK Ver1.6 2001.07 M.Mizuki
			EnqueteInfo enqinfo = new EnqueteInfo( prop.getDrId(), "0000" );
			session.putValue("UserEnquete", enqinfo);

			EnqueteController enqcont = new EnqueteController( enqinfo, con );
			enqcont.getMaxEnqueteId( request, response );
			enqinfo.setMessageHeaderId( "" );
			enqcont.setEnqueteSendLog( request, response );

		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbConnect.closeDB(con);
		}

		return count;
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

    /**
     *�@���[���A�h���X�̘_���`�F�b�N
     */
    boolean checkEmail(String email, String reEmail) {
		if (email == null || reEmail == null || !email.equals(reEmail)) {
			return false;
		}
	
		int aCount = 0;
		// ���p�p�����ł��邱�Ƃ̊m�F
		for (int i = 0; i < email.length(); i++) {
			char c = email.charAt(i);
			if ((c >= 'a' && c <= 'z') || 
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				(c == '.') || (c == '_') || (c == '-') || (c == '/') ||
				(c == '@')) {
				if (c =='@') aCount++;
			} else {
				return false;
			}
		}
		if (aCount == 1) return true;
		return false;
    }
}
