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
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>「コール内容－一覧」画面用コントローラ</strong>
 * @author
 * @version
 */
public class CallListController {

    /**
     * CallListController オブジェクトを新規に作成する。
     */
    public CallListController() {
    }

    /**
     * コール内容一覧表示
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     * @param ses セッションオブジェクト
     * @return コール内容(各要素はCallInfoオブジェクト)
     */
    public Vector createDisplay(HttpServletRequest req,
                                HttpServletResponse res,
                                HttpSession ses) {
        DBConnect  dbconn;
        Connection conn;
        Vector     callList = null;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                callList = callMan.getCallInfo(ses);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return callList;
    }

    /**
     * 選択したコール内容を削除する。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     * @param ses セッションオブジェクト
     * @param callList 削除するコール内容コード(各要素はStringオブジェクト)
     * @return 1:削除できた 0:削除できなかった
     */
    public int deleteDisplay(HttpServletRequest req,
                             HttpServletResponse res,
                             HttpSession ses,
                             Vector callList) {
        DBConnect  dbconn;
        Connection conn;
        int        stat = 0;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                stat = callMan.deleteCallInfo(ses, callList);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return stat;
    }

    /**
     * セッション情報を初期化する。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     * @return 継続可否
     */
    public static boolean initSession(HttpServletRequest req,
                                      HttpServletResponse res) {
        boolean bContinue = true;
        DispatManager dm = new DispatManager();

        try {
            // ログイン情報のチェック (セッション情報の取得)
            if (SessionManager.check(req) != true) {
                dm.distSession(req, res);
                return false;
            }

            // セッションチェック
            HttpSession ses = req.getSession(true);
            Common comses = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            if (comses == null) {
                dm.distSession(req, res);
                return false;
            }

            // 権限チェック
            if (!comses.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
                // ウェブマスタ以外の場合
                dm.distAuthority(req, res);
                return false;
            }

            // DBチェック
            if (!DBConnect.isConnectable()) {
                dm.distribute(req, res);
                return false;
            }

            // コール内容一覧画面用セッション情報取得＆初期設定
            String clkey = SysCnst.KEY_CALLLIST_SESSION;
            CallListSession clses = (CallListSession)ses.getValue(clkey);
            if (clses == null) {
                clses = new CallListSession();
                ses.putValue(clkey, clses);
            }
        }
        catch (Exception e) {
            dm.distribute(req, res);
        }

        return bContinue;
    }
}
