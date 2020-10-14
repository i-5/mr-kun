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
 * Medipro DRプロフィール
 */
public class DrProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				String action = request.getParameter("action");
				session.removeValue("DrProfile_inputError");
				session.removeValue("DrProfile_saveOK");

				//初期化
				if (request.getParameter("init") != null) {
					session.removeValue("DrProfile");
				} else {
					refreshSession(request, session);
				}

				//各アクション毎の分岐
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
    private void setRegion(HttpServletRequest request, HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		prop.setRegion("");
    }

    /**
     * 卒業年の設定
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
     * 専門の追加
     */
    private void addSpecialToList(HttpServletRequest request, HttpSession session) {
		String selection = request.getParameter("DrProfile_addSelection");
	
		if (selection == null) {
			return;
		}

		/***********************************************************
		 *	ＤＲのプロフィール画面で医療資格を医師にしたときに
		 *	表示が途中で切れる問題の修正
		 *	専門で何も選択せずに追加ボタンを押し
		 *	その後、更新したのが原因 1208 y-yamada add
		 ***********************************************************/
		//////////////////////////////////////////////////////////////	
		//log("DrProfile_addSelection　＝"+selection);
		//int i = selection.length();
		//log("i　＝"+i);
		/////////////////////////////////////////////////////////////
		if( selection.equals("") ) {
			//log("ＯＫ");
			return;
		}
		//ここまで	

		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
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
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");

		if (prop == null) {
			String drId = (String)session.getValue("com_drid");
			prop = new DoctorProperty(drId);
			session.putValue("DrProfile", prop);
		}

		prop.setBirthYear(getParameter(request, "DrProfile_birthYear"));
		prop.setBirthMonth(getParameter(request, "DrProfile_birthMonth"));
		prop.setBirthDay(getParameter(request, "DrProfile_birthDay"));

	//氏名
		prop.setName(getParameter(request, "DrProfile_name"));
		//氏名(かな)
		prop.setNameKana(getParameter(request, "DrProfile_nameKana"));
		//公務員
		prop.setKoumuin(getParameter(request, "DrProfile_koumu"));
		//Eメールアドレス
		prop.setEmail(getParameter(request, "DrProfile_email"));
		//勤務先
		prop.setKinmusaki(getParameter(request, "DrProfile_workPlace"));
		//MR君をご使用になる場所
		prop.setPlace(getParameter(request, "DrProfile_place"));
		//ネットワーク環境
		prop.setNetwork(getParameter(request, "DrProfile_network"));
		//資格
		prop.setShikaku(getParameter(request, "DrProfile_shikaku"));
		//勤務区分
		prop.setWorkType(getParameter(request, "DrProfile_workType"));
		//診療科目
		prop.setClinic(getParameter(request, "DrProfile_clinic"));
		//所属学会
		Vector academicList = new Vector();
		academicList.addElement(getParameter(request, "DrProfile_academic1"));
		academicList.addElement(getParameter(request, "DrProfile_academic2"));
		academicList.addElement(getParameter(request, "DrProfile_academic3"));
		academicList.addElement(getParameter(request, "DrProfile_academic4"));
		academicList.addElement(getParameter(request, "DrProfile_academic5"));
		prop.setAcademicList(academicList);
		//卒業大学
		prop.setCollege(getParameter(request, "DrProfile_college"));
		//学部
		prop.setFaculty(getParameter(request, "DrProfile_faculty"));
		//卒業年次
		prop.setGengo(getParameter(request, "DrProfile_yearName"));
		prop.setYear(getParameter(request, "DrProfile_year"));
		setYear(request, session);
		//地域1
		prop.setPrefecture(getParameter(request, "DrProfile_prefecture"));
		//地域2
		prop.setRegion(getParameter(request, "DrProfile_region"));
		//MR君に関するお知らせの要否
		prop.setNeedNotify(getParameter(request, "DrProfile_notify"));
    }

    private void executeSave(HttpSession session) {
		DoctorProperty prop = (DoctorProperty)session.getValue("DrProfile");
		if (!prop.isInputCompleted()
			|| !checkEmail(prop.getEmail(), prop.getReEmail())) {
			//1201 y-yamada add メールアドレスチェック
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
	 * メールアドレスチェック 1201 y-yamada add 
	 *****************************************/
    boolean checkEmail(String email, String reEmail) {
		int aCount = 0;
		// 半角英数字であることの確認
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
