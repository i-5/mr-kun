package medipro.dr;

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

import java.net.URLDecoder;//1106 y-yamada add

/**
 * Medipro DRフロント入口
 */
public class Front extends HttpServlet {
    //未登録時遷移ページ
    static final String ENTRY_PAGE = "/medipro/dr/DrFront/mr_regist.jsp";
    //不正アクセス時遷移ページ
    static final String ILLEGAL_PAGE = "/medipro/err/Cookie.jsp";
    //正常時遷移ページ
    static final String FRONT_PAGE = "/medipro/dr/DrFront/index.jsp";
    private boolean debugMode = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
		    HttpSession session = request.getSession(true);
		
			/*************************************************************************/
			Enumeration headerName = request.getHeaderNames();
			while( headerName.hasMoreElements() ) {
				String HeaderName = (String)headerName.nextElement();
				log(HeaderName+"　=　"+request.getHeader(HeaderName));
			}
			Enumeration parameterName = request.getParameterNames();
			while( parameterName.hasMoreElements())	{
				String ParameterName = (String)parameterName.nextElement();
				log("ParameterName　=　"+ParameterName);
			}
			Cookie thisCookie = null;
			Cookie[] cookies = request.getCookies();
			for(int i=0; i < cookies.length; i++) {
			
				thisCookie = cookies[i];
				log((String)thisCookie.getName()+"="+thisCookie.getValue());
				if(thisCookie.getName().equals("bvtomrurl"))
					{
						session.putValue("bvtomrurl" , thisCookie.getValue() );
					}

			}
			/***************************************************************************/
		
			String debugFlag = "true"; // hb010730; System.getProperty("medipro.debug");
			try {
				debugMode = new Boolean(debugFlag).booleanValue();
				if (debugMode) {
					log("################################");
					log("# Caution! starting DEBUG MODE #");
					log("# This mode use a cookie id!   #");
					log("################################");
				}
			} catch (Exception e) {
			}

			String nextPage = null;

		    //一回目ログイン
		    String drId = request.getParameter("user_id");

		    // session check & reset
		    if (!session.isNew()) {
				session.invalidate();
				session = request.getSession(true);
		    }

		    if (debugMode) {
			    drId = getCookieID(request);
			    drId = URLDecoder.decode( drId );// 1107 y-yamada add
			    session.putValue("MODE", "test");
		    }

		    log("DR-ID=" + drId);
		    
		    if (drId == null) {
				log("error for unset doctor id");
				nextPage = ILLEGAL_PAGE;
		    } else {
				/***********************************************/
			    /*ここから半角スペースを変換する 1107 y-yamada */
			    /***********************************************/
			    String newID="";
				int length = drId.length();
				log("length="+length);
				int i = 0;
				while(i < length ) {
					int j=i +1;
					String s = drId.substring(i , j );//一文字づつ取得
					if ( s.equals(" ") == true ) {
						s = "~~";
					}
					newID = newID.concat(s);//ｓを追加する
					i++;
				}
				drId = newID; //drIDに変換コードを入れなおす
				log("変換後drID="+drId);

				String sessionId = HtmlTagUtil.getRandomId();
				session.putValue(SysCnst.MR_SESSION_ID, sessionId);

				DoctorInfo info = getDoctorInfo(drId);
				//DBに登録されているかチェック
				if (info.getDrID() == null) {
				    nextPage = ENTRY_PAGE;
				    session.putValue("DrEntry_drId", drId);
				    session.putValue("DrEntry_init", "yes");
				} else {
				    initSession(request, info);
				    nextPage = FRONT_PAGE;
				}
		    }
		
			/**************************************************************
			 *  医師のuser-agentをlogindrテーブルに保存する 1207 y-yamada add
			 ***************************************************************/
			if(drId != null ) {
				new AccessAnalyzer(drId).analyze(request);
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
     * 開発 or テスト環境用
     */
    private String getCookieID(HttpServletRequest request) {
		// initialization
		String id = null;
		Cookie thisCookie = null;

		// cookieの持つデータをすべてとってくる
		Cookie[] cookies = request.getCookies();

		for(int i=0; i < cookies.length; i++) {
			thisCookie = cookies[i];
			if (thisCookie.getName().equals("drID")) {
				return thisCookie.getValue();
			}
		}

		return null;
    }

    /**
     * 指定したDR-IDがDBに登録されているかチェックする
     */
    private DoctorInfo getDoctorInfo(String drId) {
        DBConnect dbConnect = new DBConnect();
        Connection connection  = null;

		try {
			connection = dbConnect.getDBConnect();
			DoctorInfoManager manager = new DoctorInfoManager(connection);
			return manager.getDoctorInfo(drId);
		} finally {
			dbConnect.closeDB(connection);
		}
    }

    /**
     * セッションへ最低限必要な情報を積み込む
     */
    private void initSession(HttpServletRequest request, DoctorInfo info) {
		//最初にセッションの初期化
		HttpSession session = request.getSession(true);
		if (session.isNew() == false) {
			session.invalidate();
			session = request.getSession(true);
		}

		// セッション有効時間設定
		int session_time = Integer.parseInt(getConstTable("TIMEOUT", 1));
		session.setMaxInactiveInterval(session_time);

		session.putValue("com_drid", info.getDrID());
		session.putValue("com_DrName", info.getName());
		String mishiyoFlg = info.getMrkunMishiyoFlg();

		if (mishiyoFlg.equals("1")) {
			session.putValue("com_MrMishiyo", "ON");
		}

		// set 受信BOX Max Count 
		String receive_cnt = getConstTable("RECEIVEMAX", 1);
		session.putValue("com_receive_cnt", receive_cnt);

		// set 受信保管BOX Max Count 
		String receive_save_cnt = getConstTable("RECEIVEMAX", 2);
		session.putValue("com_receive_save_cnt", receive_save_cnt);

		// set 送信BOX Max Count 
		String send_cnt = getConstTable("SENDMAX", 1);
		session.putValue("com_send_cnt", send_cnt);

		// set 送信保管BOX Max Count 
		String send_save_cnt = getConstTable("SENDMAX", 2);
		session.putValue("com_send_save_cnt", send_save_cnt);
    }

    /**
     * 定数マスタから値を取得する.
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
