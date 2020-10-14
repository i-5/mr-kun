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
 * <strong>サブマスター一覧</strong>
 * <br>
 * @author 
 * @version 
 */
public class SubListController {

    /**
     * SubListController オブジェクトを新規に作成する。
     */
    public SubListController() {
    }

    /**
     * サブマスターを一覧表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param ses javax.servlet.http.HttpSession
     * @return 要素リスト（ＭＲ情報）
     */
    public Enumeration createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HttpSession session) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            enum = manager.getSubmasters(session);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * サブマスターの権限を取り上げる。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param ses javax.servlet.http.HttpSession
     * @param v ＭＲ－ＩＤリスト
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              Vector v) {

	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            manager.updateSubmaster(session, v);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * セッション情報（サブマスター一覧管理情報）を初期化する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
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
            String m_flg = common.getMasterFlg();
            String s_flg = common.getMasterKengenSoshiki();
            if ( ! ( m_flg.equals(SysCnst.FLG_MASTER_WEB) ||
                    ( m_flg.equals(SysCnst.FLG_MASTER_SUB) &&
                      s_flg.equals(SysCnst.FLG_AUTHORITY_BRANCH) ) ) ) {
                dm.distAuthority(request, response);
                return false;
            }

            // サブマスター一覧画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_SUBLIST_SESSION;
            SubListSession subListSession =
                (SubListSession)session.getValue(subKey);
            if ( subListSession == null ) {
                subListSession = new SubListSession();
                subListSession.setSortKey(SysCnst.SORTKEY_SUBMASTER_LIST);
                subListSession.setOrder(SysCnst.ORDER_SUBMASTER_LIST);
                subListSession.setPage(1);
                session.putValue(subKey, subListSession);
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
