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
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * 「コール内容－追加・変更」画面用コントローラ
 * @author
 * @version
 */
public class CallUpdateController {

    /**
     * CallUpdateController オブジェクトを新規に作成する。
     */
    public CallUpdateController() {
    }

    /**
     * コール内容追加・変更
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     * @param ses セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @param callNaiyo   コール内容
     * @return 0:正常に追加できた<br>
     *         1:重複している<br>
     *        -1:その他のエラー
     */
    public int updateDisplay(HttpServletRequest req,
                             HttpServletResponse res,
                             HttpSession ses,
                             String callNaiyoCd,
                             String callNaiyo) {
        DBConnect  dbconn;
        Connection conn;
        CallUpdateSession callSes =
                (CallUpdateSession)ses.getValue(SysCnst.KEY_CALLUPDATE_SESSION);

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);

                // 追加モードの時
                if (callSes.getUpdateMode() == callSes.UPDMODE_NEW) {
                    if (callMan.isOverlap(ses, callNaiyoCd)) {
                        return 1;
                    }
                    int rc = callMan.insertCallInfo(ses, callNaiyoCd, callNaiyo);
                    if (rc != 0) {
                        return 0;
                    }
                }

                // 更新モードの時
                if (callSes.getUpdateMode() == callSes.UPDMODE_UPDATE) {
                    int rc = callMan.updateCallInfo(ses, callNaiyoCd, callNaiyo);
                    if (rc != 0) {
                        return 0;
                    }
                }
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return -1;
    }

    /**
     * コール内容コードからコール内容取得
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     * @param ses セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @return コール内容
     */
    public String getCallNaiyo(HttpServletRequest req,
                               HttpServletResponse res,
                               HttpSession ses,
                               String callNaiyoCd) {
        DBConnect  dbconn;
        Connection conn;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                return callMan.searchCallNaiyo(ses, callNaiyoCd);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return null;
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
            // ログイン情報のチェック（セッション情報の取得）
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

            // コール内容追加・変更画面用セッション情報取得＆初期設定
            String cukey = SysCnst.KEY_CALLUPDATE_SESSION;
            CallUpdateSession cuses = (CallUpdateSession)ses.getValue(cukey);
            if (cuses == null) {
                cuses = new CallUpdateSession();
                ses.putValue(cukey, cuses);
            }
        }
        catch (Exception e) {
            dm.distribute(req, res);
        }

        return bContinue;
    }
}
