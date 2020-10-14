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
 * <strong>リンク一覧</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkListController {

    /**
     * LinkListController オブジェクトを新規に作成する。
     */
    public LinkListController() {
    }

    /**
     * リンク一覧情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session リンク一覧画面の設定情報を積んだセッションオブジェクト
     * @return リンク一覧情報
     */
    public Enumeration createDisplay(HttpServletRequest request,
				     HttpServletResponse response,
				     Common common, LinkListSession session) {

	Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    LinkLibInfoManager manager = new LinkLibInfoManager(connection);
	    enum = manager.getLinkLibList(common, session);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * リンク一覧補足情報（デフォルトリンクコード）を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session リンク一覧画面の設定情報を積んだセッションオブジェクト
     * @return デフォルトリンクコード
     */
    public String createDisplaySub(HttpServletRequest request,
				   HttpServletResponse response,
				   Common common, LinkListSession session) {

	String defLinkCd = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbconn.getDBConnect();
	    CompanyInfoManager manager = new CompanyInfoManager(connection);
	    defLinkCd = manager.getDefaultLinkCd(common.getCompanyCd());
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

	return defLinkCd;
    }

    /**
     * リンク情報を削除する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session リンク一覧画面の設定情報を積んだセッションオブジェクト
     * @param v 削除対象リンクコードのリスト
     */
    public void deleteDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, LinkListSession session,
			      Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    LinkLibInfoManager manager = new LinkLibInfoManager(connection);
	    manager.deleteLinkLibList(common, v);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

    /**
     * リンク情報（デフォルトリンクコード）を更新する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session リンク一覧画面の設定情報を積んだセッションオブジェクト
     * @param linkCd リンクコード
     */
    public void updateDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, LinkListSession session,
			      String linkCd) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    CompanyInfoManager manager = new CompanyInfoManager(connection);
	    manager.setDefaultLinkCd(common, common.getCompanyCd(), linkCd);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

    /**
     * セッション情報（リンク一覧管理情報）を初期化する。
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

	    // リンク一覧画面用セッション情報取得＆初期設定
	    String subKey = SysCnst.KEY_LINKLIST_SESSION;
	    LinkListSession subSession =
		(LinkListSession)session.getValue(subKey);
	    if ( subSession == null ) {
		subSession = new LinkListSession();
		subSession.setSortKey(SysCnst.SORTKEY_LINK_LIST);
		subSession.setOrder(SysCnst.ORDER_LINK_LIST);
		subSession.setPage(1);
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
