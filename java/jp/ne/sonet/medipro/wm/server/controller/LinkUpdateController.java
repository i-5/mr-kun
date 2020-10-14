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
 * <strong>リンク追加・更新</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkUpdateController {

    /**
     * LinkUpdateController オブジェクトを新規に作成する。
     */
    public LinkUpdateController() {
    }

    /**
     * リンク情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code リンクコード
     * @return リンク情報
     */
    public LinkLibInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {

        LinkLibInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            LinkLibInfoManager manager = new LinkLibInfoManager(connection);
            info = manager.getLinkLibInfo(common, common.getCompanyCd(), code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * リンク情報を更新（追加・変更）する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info リンク情報
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              LinkLibInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            LinkLibInfoManager manager = new LinkLibInfoManager(connection);
            if ( info.getLinkCd() == null || info.getLinkCd().equals("") ) {
                manager.insertLinkLibInfo(common, info);
            }
            else {
                manager.updateLinkLibInfo(common, info);
            }
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * リンク分類一覧情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @return リンク分類一覧情報
     */
    public Enumeration createDisplaySub(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Common common) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
	    connection = dbconn.getDBConnect();
            LinkClassInfoManager manager = new LinkClassInfoManager(connection);
            enum = manager.getLinkClassList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
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

            // リンク追加・更新画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_LINKUPDATE_SESSION;
            LinkUpdateSession subSession =
                (LinkUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new LinkUpdateSession();
                session.putValue(subKey, subSession);
            }

            // リンク一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_LINKLIST_SESSION;
            LinkListSession preSession =
                (LinkListSession)session.getValue(preKey);
            if ( preSession != null )   preSession.clear();

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
