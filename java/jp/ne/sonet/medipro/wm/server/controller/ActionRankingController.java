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
 * <strong>ランキング表示設定</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ActionRankingController {

    /**
     * ActionRankingController オブジェクトを新規に作成する。
     */
    public ActionRankingController() {
    }

    /**
     * ランキング表示設定情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code ランキング表示設定コード
     * @return ランキング表示設定情報
     */
    public CompanyInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common) {
        CompanyInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            CompanyInfoManager manager = new CompanyInfoManager(connection);
            info = manager.getCompanyInfo(common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * ランキング表示設定情報を更新する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info ランキング表示設定情報
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              CompanyInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            CompanyInfoManager manager = new CompanyInfoManager(connection);
            manager.updateDisplayRanking(common.getCompanyCd(), common.getMrId(), info.getDisplayRanking());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * セッション情報（ランキング表示設定一覧管理情報）を初期化する。
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

            // ランキング表示設定追加・更新画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_ACTIONRANKING_SESSION;
            ActionRankingSession subSession =
                (ActionRankingSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new ActionRankingSession();
                session.putValue(subKey, subSession);
            }

            // ランキング表示設定一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_ACTION_SESSION;
            ActionListSession preSession =
                (ActionListSession)session.getValue(preKey);
            if ( preSession != null )   preSession.clear();

	    //DBインスタンスチェック
	    if (!DBConnect.isConnectable()) {
		dm.distribute(request, response);
		return false;
	    }
        } catch (Exception e) {
            dm.distribute(request, response);
            return false;
        }

        return bContinue;
    }

}
