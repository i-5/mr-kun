package medipro.wm;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.mr.common.util.HtmlTagUtil;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>Medipro Webmaster ログイン.</strong>
 * @author
 * @version
 */
public class LoginServlet extends HttpServlet {

    /**
     * ログインサービスの定義.
     * @param request  要求オブジェクト
     * @param response 応答オブジェクト
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
	if (SysCnst.DEBUG) {
	    log("LoginServlet called!");
	}

	// DB Connection
	DBConnect dbconn = new DBConnect();
	Connection connection  = null;
	
	try {
	    connection  = dbconn.getDBConnect();

	    //セッションの新規作成
	    HttpSession session = request.getSession(true);

            
            /* <hb010716
             * this - as in medipro.mr.Login
             * does not seem necessary and creates problems with Tomcat
             * sessions
             * hb010716>
             
	    if (!session.isNew()) {
		//セッション情報のリセット
		session.invalidate();
		session = request.getSession(true);
	    }
            */
	    String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
	    String submit  = request.getParameter("submitaction");
	    session.removeValue("login_err");
	    session.removeValue("pwdchg_err");
	    session.removeValue("login_wmid");

	    if (submit.equals("login")) {
		//ログインボタン
		if (!loginCheck(request, connection)) {
		    session.putValue("login_err", "pwderror");
		    nextPage = null;
		}
	    } else if (submit.equals("passchg")) {
		if (!passwdChange(request, connection)) {
		    session.putValue("pwdchg_err", "pwderror");
		    nextPage = null;
		}
	    } else {
		//それ以外はとりあえずエラー...
		throw new Exception("実装されてません!");
	    }

            /*
             * <hb010801
             * this should actually be done for all the SessionManager.check() calls
            String sessionId = HtmlTagUtil.getRandomId();
            session.putValue(SysCnst.MR_SESSION_ID, sessionId);
             * hb010801>
             */

	    if (nextPage == null) {
		session.putValue("login_wmid", request.getParameter("com_mrid"));
		nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmLogin/index.jsp";
	    } else {
		//共通変数の取得
		Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

		//ウェブマスターとサブマスター以外はエラー画面へ
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
		    !common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
		    new DispatManager().distAuthority(request, response);
		    return;
		}

		//定数の取得
		ConstantMasterInfoManager manager = new ConstantMasterInfoManager(connection);
		manager.refreshCommon(common);
		
		
		/**************************************************************
		MRのuser-agentをloginmrテーブルに保存する 1212 y-yamada add
		***************************************************************/
		String mrId = request.getParameter("com_mrid");
		if( mrId != null )
		{	
			session.putValue("wmId" , mrId );
			new AccessWmAnalyzer(mrId).analyze(request);
		}

		//セッションタイムアウトの設定
		session.setMaxInactiveInterval(common.getTimeout());

		Cookie cookie = new Cookie("Mrid", mrId);
		cookie.setMaxAge(864000);
		cookie.setPath("/");
		response.addCookie(cookie);
	    }

	    // Go to the next page
	    response.sendRedirect(nextPage);
	} catch (Exception e) {
	    //エラー画面に遷移
		log("", e);
	    new DispatManager().distribute(request,response, e);
	} finally {
	    // DB Connection close
	    dbconn.closeDB(connection);
	}
    }

    /**
     * infoの内容をCommonに反映する。
     */
    private void refreshCommon(MrInfo info, HttpSession session) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	if (common == null) {
	    common = new Common();
	}

	common.setMrId(info.getMrId());
	common.setCompanyCd(info.getCompanyCd());
	common.setShitenCd(info.getShitenCd());
	common.setEigyosyoCd(info.getEigyosyoCd());
	common.setMrAttributeCd1(info.getMrAttributeCd1());
	common.setMrAttributeCd2(info.getMrAttributeCd2());
	common.setMasterFlg(info.getMasterFlg());
	common.setMasterKengenSoshiki(info.getMasterKengenSoshiki());
	common.setMasterKengenAttribute(info.getMasterKengenAttribute());

	session.putValue(SysCnst.KEY_COMMON_SESSION, common);
    }

    /**
     * MRのIDとpasswordがDBに登録されているかチェックする.
        */
    private boolean loginCheck(HttpServletRequest req,
			       Connection connection) throws Exception {
	String  mrId = req.getParameter("com_mrid");	//id
	String  password = req.getParameter("passwd");	//password

	//MR情報の取得
	MrInfoManager manager = new MrInfoManager(connection);
   	MrInfo  mrInfo = manager.getMrLoginInfo(mrId);

	if (mrInfo == null) {
	    return false;
	} else if (password.equals(mrInfo.getPassword())) {
	    refreshCommon(mrInfo, req.getSession());
            // hb010717
            HttpSession session = req.getSession();
            session.putValue("daikou",mrInfo.getDaikou());
            session.putValue("connection",((Hashtable) mrInfo.getDaikou()).get("connection"));
 	    session.putValue("com_mrid", req.getParameter("com_mrid"));
 	    session.putValue("com_mrname", mrInfo.getName());

	    return true;
	}

	return false;
    }

    /**
     * パスワード変更.
     */
    private boolean passwdChange(HttpServletRequest req,
				 Connection connection) throws Exception {
	String  mrid = req.getParameter("com_mrid");
	String  passwd1 = req.getParameter("newPasswd1");

	//ログインチェック
	if (loginCheck(req, connection)) {
	    MrInfoManager manager = new MrInfoManager(connection);
	    MrInfo  mrinfo = manager.getMrLoginInfo(mrid);
	    mrinfo.setPassword(passwd1);
	    manager.updatePassword(mrinfo);
	    return true;
	}

	return false;
    }
}
