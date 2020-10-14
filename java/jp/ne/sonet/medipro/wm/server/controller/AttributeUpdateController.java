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
 * <strong>ＭＲ属性追加・更新</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeUpdateController {

    /**
     * AttributeUpdateController オブジェクトを新規に作成する。
     */
    public AttributeUpdateController() {
    }

    /**
     * ＭＲ属性情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code ＭＲ属性コード
     * @return ＭＲ属性情報
     */
    public AttributeInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {
        AttributeInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            info =
                manager.getAttributeInfo(common, code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * ＭＲ属性情報を更新（追加・変更）する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info ＭＲ属性情報
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              AttributeInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
	    connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            if ( info.getMrAttributeCd() == null ||
                 info.getMrAttributeCd().equals("") ) {
                manager.insertAttributeInfo(common, info);
            }
            else {
                manager.updateAttributeInfo(common, info);
            }
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * ＭＲ属性一覧情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @return ＭＲ属性一覧情報
     */
    public Enumeration createDisplaySub(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Common common) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            enum = manager.getAttributeList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * セッション情報（ＭＲ属性管理情報）を初期化する。
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

            // ＭＲ属性追加・更新画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_ATTRIBUTEUPDATE_SESSION;
            AttributeUpdateSession subSession =
                (AttributeUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new AttributeUpdateSession();
                session.putValue(subKey, subSession);
            }

            // ＭＲ属性一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_ATTRIBUTELIST_SESSION;
            AttributeListSession preSession =
                (AttributeListSession)session.getValue(preKey);
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
