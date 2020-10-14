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
 * <strong>ＭＲ属性一覧</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeListController {

    /**
     * AttributeListController オブジェクトを新規に作成する。
     */
    public AttributeListController() {
    }

    /**
     * ＭＲ属性一覧情報を取得する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session ＭＲ属性一覧画面の設定情報を積んだセッションオブジェクト
     * @return ＭＲ属性一覧情報
     */
    public Enumeration createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     AttributeListSession session) {
        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            enum = manager.getAttributeList(common, session);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * ＭＲ属性情報を削除する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session ＭＲ属性一覧画面の設定情報を積んだセッションオブジェクト
     * @param v 削除対象ＭＲ属性コードのリスト
     */
    public void deleteDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
			      AttributeListSession session,
                              Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            manager.deleteAttributeList(common, v);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * ＭＲ属性情報を削除する。
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session ＭＲ属性一覧画面の設定情報を積んだセッションオブジェクト
     * @param v 削除対象ＭＲ属性コードのリスト
     * @return 削除可否
     */
    public boolean isDeleteDisplay(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Common common,
				   AttributeListSession session,
                                   Vector v) {
        boolean bDelete = false;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            bDelete = (manager.hasMrAttribute(v) ? false : true);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return bDelete;
    }

    /**
     * セッション情報（ＭＲ属性一覧管理情報）を初期化する。
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

            // ＭＲ属性一覧画面用セッション情報取得＆初期設定
            String subKey = SysCnst.KEY_ATTRIBUTELIST_SESSION;
            AttributeListSession subSession =
                (AttributeListSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new AttributeListSession();
                subSession.setSortKey(SysCnst.SORTKEY_ATTRIBUTE_LIST);
                subSession.setOrder(SysCnst.ORDER_ATTRIBUTE_LIST);
                subSession.setPage(1);
                session.putValue(subKey, subSession);
            }

            // 支店・営業所一覧画面用セッション情報取得＆初期設定
            String preKey = SysCnst.KEY_BRANCHLIST_SESSION;
            BranchListSession preSession =
                (BranchListSession)session.getValue(preKey);
            if ( preSession != null )   {
                preSession.crearCheckValue();
                preSession.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
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
