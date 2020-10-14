package jp.ne.sonet.medipro.wm.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*; 
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.common.servlet.*; 
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;

/**
 * <strong>定型文一覧</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ActionListController {

    /**
     * ActionListController オブジェクトを新規に作成する。
     */
    public ActionListController() {
    }

    /**
     * 重要度一覧情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session 重要度一覧画面の設定情報を積んだセッションオブジェクト
     * @return 重要度一覧情報
     */
    public Enumeration createDisplay(HttpServletRequest request,
				     HttpServletResponse response,
				     Common common,
				     ActionListSession session) {

	Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbconn.getDBConnect();
	    ActionInfoManager manager =
		new ActionInfoManager(connection);
	    enum = manager.getTargetRankList(common.getCompanyCd(), session).elements();
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * 定型文情報を削除する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session 定型文一覧画面の設定情報を積んだセッションオブジェクト
     * @param v 削除対象定型文コードのリスト
     */
    public void deleteDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, ActionListSession session,
			      Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    ActionInfoManager manager =
		new ActionInfoManager(connection);
	    manager.delete(common.getCompanyCd(), v );
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

   /**
    *  getDefaultTargetRank
    */
    public String getDefaultTargetRank(HttpServletRequest req,
				    HttpServletResponse res,
				    HttpSession session) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	String targetRank = null;

	DBConnect dbConnect = new DBConnect();
	Connection con = null;

	try {
	    con = dbConnect.getDBConnect();

	    CompanyInfoManager manager = new CompanyInfoManager(con);
	    targetRank = manager.getTargetRank(common.getCompanyCd());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	} finally {
	    dbConnect.closeDB(con);
	}

	return targetRank;
    }

    /**
     * セッション情報（重要度一覧管理情報）を初期化する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @return 継続可否
     */
    public static boolean initSession(HttpServletRequest request,
				      HttpServletResponse response) {

	boolean bContinue = true;
	DispatManager dm = new DispatManager();
	try {
	    // ログイン情報のチェック（セッション情報の取得）
	    if ( SessionManager.check(request) != true ) {
		dm.distSession(request, response);
		return false;
	    }
	    HttpSession session = request.getSession(true);
	    String comKey = SysCnst.KEY_COMMON_SESSION;
	    Common common = (Common)session.getValue(comKey);

	    // 権限チェック
	    if ( ! common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) ) {
		dm.distAuthority(request, response);
		return false;
	    }

	    // 重要度一覧画面用セッション情報取得＆初期設定
	    String subKey = SysCnst.KEY_ACTION_SESSION;
	    ActionListSession subSession =
		(ActionListSession)session.getValue(subKey);
	    if ( subSession == null ) {
		subSession = new ActionListSession();
		subSession.setSortKey(SysCnst.SORTKEY_ACTION_LIST);
		subSession.setOrder(SysCnst.ORDER_ACTION_LIST);
		session.putValue(subKey, subSession);
	    }

	    //DBインスタンスチェック
	    if (!DBConnect.isConnectable()) {
		dm.distribute(request, response);
		return false;
	    }
	} catch (Exception e) {
	    dm.distribute(request, response);
	}

	return bContinue;
    }

}
