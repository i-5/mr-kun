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
 * Medipro DRアンケート
 */
public class Enquete extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
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
				// セッションエラーの場合
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
     * 市町村のクリア
     */
//    private void setRegion(HttpServletRequest request, HttpSession session) {
//		DoctorProperty prop = (DoctorProperty)session.getValue("DrEnquete");
//		prop.setRegion("");
//    }

    /**
     * 卒業年の設定
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
     * 専門の追加
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
     * 専門の削除
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
     * 文字コード変換
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
     *　画面情報をセッションに...
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

		//MR君をご使用になる場所
//		prop.setPlace(getParameter(request, "DrEnquete_place"));
		//ネットワーク環境
//		prop.setNetwork(getParameter(request, "DrEnquete_network"));
		//資格
		prop.setShikaku(getParameter(request, "DrEnquete_shikaku"));
		//勤務区分
		prop.setWorkType(getParameter(request, "DrEnquete_workType"));
		//診療科目
		prop.setClinic(getParameter(request, "DrEnquete_clinic"));
		//所属学会
//		Vector academicList = new Vector();
//		academicList.addElement(getParameter(request, "DrEnquete_academic1"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic2"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic3"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic4"));
//		academicList.addElement(getParameter(request, "DrEnquete_academic5"));
//		prop.setAcademicList(academicList);
		//卒業大学
//		prop.setCollege(getParameter(request, "DrEnquete_college"));
		//学部
//		prop.setFaculty(getParameter(request, "DrEnquete_faculty"));
		//卒業年次
//		prop.setGengo(getParameter(request, "DrEnquete_yearName"));
//		prop.setYear(getParameter(request, "DrEnquete_year"));
//		setYear(request, session);
		//地域1
//		prop.setPrefecture(getParameter(request, "DrEnquete_prefecture"));
		//地域2
//		prop.setRegion(getParameter(request, "DrEnquete_region"));
		//自宅
//		prop.setAddress(getParameter(request, "DrEnquete_address"));
		//自宅TEL
//		prop.setTel(getParameter(request, "DrEnquete_tel"));
		//勤務先
//		prop.setOfficeAddress(getParameter(request, "DrEnquete_officeAddress"));
		//勤務先TEL
//		prop.setOfficeTel(getParameter(request, "DrEnquete_officeTel"));
		//Medipro Member
//		prop.setMediproMember(getParameter(request, "DrEnquete_medipro"));
		//Sonet Member
//		prop.setSonetMember(getParameter(request, "DrEnquete_sonet"));
		//MR君に関するお知らせの要否
//		prop.setNeedNotify(getParameter(request, "DrEnquete_notify"));
    }

    /**
     * 保存実行
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
			//登録
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
     * 定数マスタから値の取得
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
