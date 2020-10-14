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
 * <strong>定型文追加・更新</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionUpdateController {

    /**
     * ExpressionUpdateController オブジェクトを新規に作成する。
     */
    public ExpressionUpdateController() {
    }

    /**
     * 定型文情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code 定型文コード
     * @return 定型文情報
     */
    public ExpressionLibInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {
        ExpressionLibInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            ExpressionLibInfoManager manager = new ExpressionLibInfoManager(connection);
            info = manager.getExpressionLibInfo(common, common.getCompanyCd(), code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * 定型文情報を更新（追加・変更）する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info 定型文情報
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              ExpressionLibInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            ExpressionLibInfoManager manager = new ExpressionLibInfoManager(connection);

            if ( info.getTeikeibunCd() == null ||
                 info.getTeikeibunCd().equals("") ) {
                manager.insertExpressionLibInfo(common, info);
            } else {
                manager.updateExpressionLibInfo(common, info);
            }
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * セッション情報（定型文一覧管理情報）を初期化する。
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

            // 定型文追加・更新画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_EXPRESSIONUPDATE_SESSION;
            ExpressionUpdateSession subSession =
                (ExpressionUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new ExpressionUpdateSession();
                session.putValue(subKey, subSession);
            }

            // 定型文一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_EXPRESSIONLIST_SESSION;
            ExpressionListSession preSession =
                (ExpressionListSession)session.getValue(preKey);
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
            TeikeiClassInfoManager manager = new TeikeiClassInfoManager(connection);
            enum = manager.getTeikeiClassList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }



}
