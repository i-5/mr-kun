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
 * <strong>サブマスター追加・変更</strong>
 * <br>
 * @author 
 * @version 
 */
public class SubUpdateController {

    /**
     * SubUpdateController オブジェクトを新規に作成する。
     */
    public SubUpdateController() {
    }

    /**
     * サブマスター情報を表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrId ＭＲ－ＩＤ
     * @return ＭＲ情報
     */
    public MrInfo createDisplay(HttpServletRequest request,
                                HttpServletResponse response,
                                String mrId) {

        MrInfo info = null;
	DBConnect db = new DBConnect();
	Connection connection = null;

        try {
            connection = db.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            info = manager.getMrInfo(mrId);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            db.closeDB(connection);
	}

        return info;
    }

    /**
     * サブマスター情報を更新（追加・変更）する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param res javax.servlet.http.HttpSession
     * @param info ＭＲ情報
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              MrInfo info) {
	DBConnect db = new DBConnect();
	Connection connection = null;
	
        try {
            connection = db.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            manager.updateSubmaster(session, info);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            db.closeDB(connection);
	}
    }

    /**
     * セッション情報（サブマスター追加・更新管理情報）を初期化する。
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

            // サブマスター追加・更新画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_SUBUPDATE_SESSION;
            SubUpdateSession subUpdateSession =
                (SubUpdateSession)session.getValue(subKey);
            if ( subUpdateSession == null ) {
                subUpdateSession = new SubUpdateSession();
                session.putValue(subKey, subUpdateSession);
            }

            // サブマスター一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_SUBLIST_SESSION;
            SubListSession preSession =
                (SubListSession)session.getValue(preKey);
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
