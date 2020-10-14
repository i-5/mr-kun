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
 * Medipro DR�v���t�B�[��
 */
public class DrProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				String action = request.getParameter("action");
				session.removeValue("DrProfile_inputError");
				session.removeValue("DrProfile_saveOK");

				//������
				if (request.getParameter("init") != null) {
					session.removeValue("DrProfile");
				} else {
					refreshSession(request, session);
				}

				//�e�A�N�V�������̕���
				if (request.getParameter("DrProfile_add") != null) {
					addSpecialToList(request, session);
				} else if (request.getParameter("DrProfile_delete") != null) {
					deleteSpecialToList(request, session);
				} else if (action != null) {
					if (action.equals("region")) {
						setRegion(request, session);
					} else if (action.equals("shikaku")) {
					} else if (action.equals("year")) {
					} else {
						executeSave(session);
					}
				}

				// Go to the next page
				response.sendRedirect("/medipro/dr/DrProfileUpdate/index.html");
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

    /**
     * �s�����̃N���A
     */
    private void setRegion(HttpServletRequest request, HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		prop.setRegion("");
    }

    /**
     * ���ƔN�̐ݒ�
     */
    private void setYear(HttpServletRequest request, HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");

		DBConnect dbConnect = new DBConnect();
		Connection con = dbConnect.getDBConnect();

		try {
			GengoInfoManager manager = new GengoInfoManager(con);
			String yearAD = manager.getYearAD(prop.getGengo(), prop.getYear());
			prop.setYearAD(yearAD);
		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbConnect.closeDB(con);
		}
    }

    /**
     * ���̒ǉ�
     */
    private void addSpecialToList(HttpServletRequest request, HttpSession session) {
		String selection = request.getParameter("DrProfile_addSelection");
	
		if (selection == null) {
			return;
		}

		/***********************************************************
		 *	�c�q�̃v���t�B�[����ʂň�Î��i����t�ɂ����Ƃ���
		 *	�\�����r���Ő؂����̏C��
		 *	���ŉ����I�������ɒǉ��{�^��������
		 *	���̌�A�X�V�����̂����� 1208 y-yamada add
		 ***********************************************************/
		//////////////////////////////////////////////////////////////	
		//log("DrProfile_addSelection�@��"+selection);
		//int i = selection.length();
		//log("i�@��"+i);
		/////////////////////////////////////////////////////////////
		if( selection.equals("") ) {
			//log("�n�j");
			return;
		}
		//�����܂�	

		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		Vector list = prop.getSpecialList();

		if (!list.contains(selection)) {
			list.addElement(selection);
		}
		prop.setSpecialList(list);
    }

    /**
     * ���̍폜
     */
    private void deleteSpecialToList(HttpServletRequest request, HttpSession session) {
		String selection = request.getParameter("DrProfile_deleteSelection");
	
		if (selection == null) {
			return;
		}

		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		Vector list = prop.getSpecialList();
		list.remove(selection);
		prop.setSpecialList(list);
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
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");

		if (prop == null) {
			String drId = (String)session.getValue("com_drid");
			prop = new DoctorProperty(drId);
			session.putValue("DrProfile", prop);
		}

		prop.setBirthYear(getParameter(request, "DrProfile_birthYear"));
		prop.setBirthMonth(getParameter(request, "DrProfile_birthMonth"));
		prop.setBirthDay(getParameter(request, "DrProfile_birthDay"));

	//����
		prop.setName(getParameter(request, "DrProfile_name"));
		//����(����)
		prop.setNameKana(getParameter(request, "DrProfile_nameKana"));
		//������
		prop.setKoumuin(getParameter(request, "DrProfile_koumu"));
		//E���[���A�h���X
		prop.setEmail(getParameter(request, "DrProfile_email"));
		//�Ζ���
		prop.setKinmusaki(getParameter(request, "DrProfile_workPlace"));
		//MR�N�����g�p�ɂȂ�ꏊ
		prop.setPlace(getParameter(request, "DrProfile_place"));
		//�l�b�g���[�N��
		prop.setNetwork(getParameter(request, "DrProfile_network"));
		//���i
		prop.setShikaku(getParameter(request, "DrProfile_shikaku"));
		//�Ζ��敪
		prop.setWorkType(getParameter(request, "DrProfile_workType"));
		//�f�ÉȖ�
		prop.setClinic(getParameter(request, "DrProfile_clinic"));
		//�����w��
		Vector academicList = new Vector();
		academicList.addElement(getParameter(request, "DrProfile_academic1"));
		academicList.addElement(getParameter(request, "DrProfile_academic2"));
		academicList.addElement(getParameter(request, "DrProfile_academic3"));
		academicList.addElement(getParameter(request, "DrProfile_academic4"));
		academicList.addElement(getParameter(request, "DrProfile_academic5"));
		prop.setAcademicList(academicList);
		//���Ƒ�w
		prop.setCollege(getParameter(request, "DrProfile_college"));
		//�w��
		prop.setFaculty(getParameter(request, "DrProfile_faculty"));
		//���ƔN��
		prop.setGengo(getParameter(request, "DrProfile_yearName"));
		prop.setYear(getParameter(request, "DrProfile_year"));
		setYear(request, session);
		//�n��1
		prop.setPrefecture(getParameter(request, "DrProfile_prefecture"));
		//�n��2
		prop.setRegion(getParameter(request, "DrProfile_region"));
		//MR�N�Ɋւ��邨�m�点�̗v��
		prop.setNeedNotify(getParameter(request, "DrProfile_notify"));
    }

    private void executeSave(HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		if (!prop.isInputCompleted()
			|| !checkEmail(prop.getEmail(), prop.getReEmail())) {
			//1201 y-yamada add ���[���A�h���X�`�F�b�N
			session.putValue("DrProfile_inputError", "error");
			return;
		}

		DBConnect dbConnect = new DBConnect();
		Connection con = dbConnect.getDBConnect();

		try {
			DrProfileInfoManager manager = new DrProfileInfoManager(con);
			manager.set(prop);
			session.putValue("DrProfile_saveOK", "OK");
		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbConnect.closeDB(con);
		}
    }

	/*****************************************
	 * ���[���A�h���X�`�F�b�N 1201 y-yamada add 
	 *****************************************/
    boolean checkEmail(String email, String reEmail) {
		int aCount = 0;
		// ���p�p�����ł��邱�Ƃ̊m�F
		for (int i = 0; i < email.length(); i++) 
			{
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
