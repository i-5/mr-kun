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
 * Medipro DR�A���P�[�g
 */
public class Enquete extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if (sm.check(request, 0)) {
				HttpSession session = request.getSession(true);
				String action = request.getParameter("action");

				if (request.getParameter("init") != null) {
					session.removeValue("DrEnquete");
				} else {
					refreshSession(request, session);
				}


				if (request.getParameter("DrEnquete_add") != null) {
					addSpecialToList(request, session);
				} else if (request.getParameter("DrEnquete_delete") != null) {
					deleteSpecialToList(request, session);
				} else if (action != null) {
//					if (action.equals("region")) {
//						setRegion(request, session);
//					} else if (action.equals("shikaku") || action.equals("year")) {
					if (action.equals("shikaku")) {
					} else {
						executeSave(session);
					}
				}

				// Go to the next page
				response.sendRedirect("/medipro/dr/DrEnquete/mainframe.jsp");
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
//    private void setRegion(HttpServletRequest request, HttpSession session) {
//		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");
//		prop.setRegion("");
//    }

    /**
     * ���ƔN�̐ݒ�
     */
//    private void setYear(HttpServletRequest request, HttpSession session) {
//		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");
//
//		DBConnect dbConnect = new DBConnect();
//		Connection con = dbConnect.getDBConnect();
//
//		try {
//			GengoInfoManager manager = new GengoInfoManager(con);
//			String yearAD = manager.getYearAD(prop.getGengo(), prop.getYear());
//			prop.setYearAD(yearAD);
//		} catch (Exception ex) {
//			throw new MrException(ex);
//		} finally {
//			dbConnect.closeDB(con);
//		}
//    }

    /**
     * ���̒ǉ�
     */
    private void addSpecialToList(HttpServletRequest request, HttpSession session) {
		String selection = request.getParameter("DrEnquete_addSelection");
	
		if (selection == null) {
			return;
		}

		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");
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
		String selection = request.getParameter("DrEnquete_deleteSelection");
	
		if (selection == null) {
			return;
		}

		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");
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
		session.removeValue("DrEnquete_inputError");

		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");

		if (prop == null) {
			String drId = (String)session.getValue("com_drid");
			prop = new DoctorProperty(drId);
			session.putValue("DrEnquete", prop);
		}

		prop.setBirthYear(getParameter(request, "DrEnquete_birthYear"));
		prop.setBirthMonth(getParameter(request, "DrEnquete_birthMonth"));
		prop.setBirthDay(getParameter(request, "DrEnquete_birthDay"));

		//MR�N�����g�p�ɂȂ�ꏊ
//		prop.setPlace(getParameter(request, "DrEnquete_place"));
		//�l�b�g���[�N��
//		prop.setNetwork(getParameter(request, "DrEnquete_network"));
		//���i
		prop.setShikaku(getParameter(request, "DrEnquete_shikaku"));
		//�Ζ��敪
		prop.setWorkType(getParameter(request, "DrEnquete_workType"));
		//�f�ÉȖ�
		prop.setClinic(getParameter(request, "DrEnquete_clinic"));
		//�����w��
//		Vector academicList = new Vector();
//		academicList.addElement(getParameter(request, "DrEnquete_academic1"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic2"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic3"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic4"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic5"));
//		prop.setAcademicList(academicList);
		//���Ƒ�w
//		prop.setCollege(getParameter(request, "DrEnquete_college"));
		//�w��
//		prop.setFaculty(getParameter(request, "DrEnquete_faculty"));
		//���ƔN��
//		prop.setGengo(getParameter(request, "DrEnquete_yearName"));
//		prop.setYear(getParameter(request, "DrEnquete_year"));
//		setYear(request, session);
		//�n��1
//		prop.setPrefecture(getParameter(request, "DrEnquete_prefecture"));
		//�n��2
//		prop.setRegion(getParameter(request, "DrEnquete_region"));
		//����
//		prop.setAddress(getParameter(request, "DrEnquete_address"));
		//����TEL
//		prop.setTel(getParameter(request, "DrEnquete_tel"));
		//�Ζ���
//		prop.setOfficeAddress(getParameter(request, "DrEnquete_officeAddress"));
		//�Ζ���TEL
//		prop.setOfficeTel(getParameter(request, "DrEnquete_officeTel"));
		//Medipro Member
//		prop.setMediproMember(getParameter(request, "DrEnquete_medipro"));
		//Sonet Member
//		prop.setSonetMember(getParameter(request, "DrEnquete_sonet"));
		//MR�N�Ɋւ��邨�m�点�̗v��
//		prop.setNeedNotify(getParameter(request, "DrEnquete_notify"));
    }

    /**
     * �ۑ����s
     */
    private void executeSave(HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");

		if (!prop.isEnqueteCompleted()) {
			session.putValue("DrEnquete_inputError", "error");
			return;
		}

		DBConnect dbConnect = new DBConnect();
		Connection con = dbConnect.getDBConnect();

		try {
			//�o�^
			DrProfileInfoManager manager = new DrProfileInfoManager(con);
			manager.setEnquete(prop);

			session.putValue("DrEnquete_completed", "OK");
		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbConnect.closeDB(con);
		}
    }

    /**
     * �萔�}�X�^����l�̎擾
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
