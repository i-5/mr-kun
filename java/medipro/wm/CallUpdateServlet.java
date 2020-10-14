package medipro.wm;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>「コール内容－追加・変更」画面用サーブレット</strong>
 * @author
 * @version
 */
public class CallUpdateServlet extends HttpServlet {
    private static final String PRT_HEADER = "### CallUpdateServlet : ";

    /**
     * GETメソッド処理。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        doPost(req, res);
    }

    /**
     * POSTメソッド処理。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            // セッションチェック
            if (CallUpdateController.initSession(req, res) == false) {
                return;
            }

            HttpSession ses = req.getSession(true);
            Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            CallUpdateSession callSes =
                    (CallUpdateSession)ses.getValue(SysCnst.KEY_CALLUPDATE_SESSION);

            String cd;
            String call;
            String mode;

            // コール内容コードの設定
            if ((cd = Converter.getParameter(req, "cd")) != null) {
                callSes.setCallNaiyoCd(cd);
            }
            else {
                cd = callSes.getCallNaiyoCd();
            }
	    if (SysCnst.DEBUG) {
		log(PRT_HEADER + "cd = " + cd);
	    }

            // コール内容の設定
            if ((call = Converter.getParameter(req, "call")) != null) {
                callSes.setCallNaiyo(call);
            }
            else {
                CallUpdateController callCtrl = new CallUpdateController();
                call = callCtrl.getCallNaiyo(req, res, ses, cd);
                callSes.setCallNaiyo(call);
            }
	    if (SysCnst.DEBUG) {
		log(PRT_HEADER + "call = " + call);
	    }

            // 追加モードか変更モードか
            if ((mode = req.getParameter("update")) != null) {
                // CallListSession のメッセージとチェックを消去する
                CallListSession listSes =
		    (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);
                listSes.clear();

                // コール内容コードを変更不可にする
                if (mode.equals("update")) {
                    callSes.setMessageMode(callSes.MESMODE_NONE);
                    callSes.setUpdateMode(callSes.UPDMODE_UPDATE);
                }
                // 入力欄をクリアする
                else {
                    callSes.initSession();
                }
            }

            String target;
            String url = "wm/WmCallUpdate/index.html";

            // 実行
            if ((target = req.getParameter("execute")) != null) {
                CallUpdateController callCtrl = new CallUpdateController();
                String callNaiyoCd = callSes.getCallNaiyoCd();
                String callNaiyo   = callSes.getCallNaiyo();

                callSes.setMessageMode(callSes.MESMODE_NONE);

                // 更新実行
                if (target.equals("update")) {
                    // 「はい」
                    if (req.getParameter("yes") != null) {
                        int update = callCtrl.updateDisplay(req, res, ses,
							    callNaiyoCd, callNaiyo);
                        // 更新完了
                        if (update == 0) {
                            callSes.setMessageMode(callSes.MESMODE_UPDATE);
                        }
                        // コール内容コードが重複している
                        else if (update == 1) {
                            callSes.setMessageMode(callSes.MESMODE_CANNOT_UPDATE);
                        }
                        // その他
                        else {
                            callSes.setMessageMode(callSes.MESMODE_NONE);
                            return;
                        }
                    }
                }
                // 「保存する」
                if (req.getParameter("confirm") != null) {
                    callSes.setMessageMode(callSes.MESMODE_CONFIRM);
                }
                // 「元に戻る」
                if (req.getParameter("back") != null) {
                    callSes.initSession();
                    url = "wm/WmCallList/index.html";
                }
            }

            // Go to the next page
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + url);
        } catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(req, res, e);
        }
    }
}
