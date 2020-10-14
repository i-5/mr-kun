package medipro.mr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro MR自己情報設定
 */
public class Myprofile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				sm.reset(request,0);

				if ( request.getParameter("MrMyProfile_update_action") != null ) {
					String from_time = request.getParameter("from_time");
					String end_time = request.getParameter("end_time");
					if (!eigyouTimeCheck(from_time, end_time) ) {
						session.putValue("MrMyProfile_eigyotimeErr", "ON");
						//1201 y-yamada add 営業時間エラーの場合はリムーブする
						session.removeValue("MrMyProfile_update_end");
					} else {
						updateMrInfo(session);
						// added by matsuura 2000/10/3						
						session.removeValue("MrMyProfile_eigyotimeErr");
						session.putValue("MrMyProfile_update_end", "OK");
					}
				} else if ( request.getParameter("MrMyProfile_save_yes") != null ) {
					updateMrInfo(session);
				} else {
					session.removeValue("MrMyProfile_update_end");
					session.removeValue("MrMyProfile_eigyotimeErr");
				}

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrMyprofile/index.html");
			} else {
				// セッションエラーの場合
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    boolean eigyouTimeCheck(String from, String end) {
		boolean ret = false;

		int fromTime = Integer.parseInt(from.replace(':' , '0'));
		int endTime = Integer.parseInt(end.replace (':' , '0'));

		if ( fromTime > endTime ) {
			ret = false;
		} else {
			ret = true;
		}
		return ret;
    }

    void updateMrInfo(HttpSession session) {
		//1201 y-yamada add Email addresのチェック
		//1201 y-yamada add  ccもチェック
		if(!checkEmail((String)session.getValue("Email"))
		   || !checkEmail((String)session.getValue("mailCC1"))
		   || !checkEmail((String)session.getValue("mailCC2"))
		   || !checkEmail((String)session.getValue("mailCC3"))
		   || !checkEmail((String)session.getValue("mailCC4")))	{
			session.putValue("MrProfile_inputError", "error");//エラーメッセージ表示
			return;
		}
		
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			String  mrID    = (String)session.getValue("com_mrid");
			MrInfoManager   mrinfomanager = new MrInfoManager(conn);
			MrInfo  mrinfo  = new MrInfo();

			mrinfo.setJikosyokai((String)session.getValue("jikosyokai"));
			mrinfo.setTelNo((String)session.getValue("tel"));
			mrinfo.setKeitaiNo((String)session.getValue("tel2"));
			mrinfo.setFaxNo((String)session.getValue("fax"));
			mrinfo.setZipCD((String)session.getValue("zipCD"));
			mrinfo.setEmail((String)session.getValue("Email"));
			mrinfo.setAddress((String)session.getValue("address"));
			mrinfo.setCcEmail1((String)session.getValue("mailCC1"));
			mrinfo.setCcEmail2((String)session.getValue("mailCC2"));
			mrinfo.setCcEmail3((String)session.getValue("mailCC3"));
			mrinfo.setCcEmail4((String)session.getValue("mailCC4"));
			mrinfo.setEigyoDateKbn((String)session.getValue("eigyobi"));
			mrinfo.setEigyoTimeKbn((String)session.getValue("eigyotime"));
			mrinfo.setEigyoStartTime((String)session.getValue("from_time"));
			mrinfo.setEigyoEndTime((String)session.getValue("end_time"));

			mrinfomanager.updateMrProfile(mrinfo, mrID);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }
    
	/*****************************************
	 * メールアドレスチェック 1201 y-yamada add 
	 *****************************************/
    boolean checkEmail(String email) {
		if ( email.length() == 0 ) {
			return true;
		}
		
		int aCount = 0;
		// 半角英数字であることの確認
		for (int i = 0; i < email.length(); i++) 
			{
				char c = email.charAt(i);
				if ((c >= 'a' && c <= 'z') || 
					(c >= 'A' && c <= 'Z') ||
					(c >= '0' && c <= '9') ||
					(c == '.') || (c == '_') || (c == '-') ||
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
