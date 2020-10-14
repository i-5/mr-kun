package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>「コール内容－一覧」画面用サーブレット</strong>
 * @author
 * @version
 */
public class CallListServlet extends HttpServlet {
    private static final String PRT_HEADER = "### CallListServlet : ";

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
            if (CallListController.initSession(req, res) == false) {
                return;
            }

            HttpSession ses = req.getSession(true);
            Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            CallListSession callSes =
                    (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);
            String target;

            // メッセージとチェック状態のクリア
            callSes.setMessageMode(callSes.MESMODE_NONE);
            callSes.clearChecked();

            // ページ移動
            if ((target = req.getParameter("page")) != null) {
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "page = " + target);
		}
                int curRow  = callSes.getCurrentRow();
                int pageRow = comSes.getCallLine();

                // 次ページへ
                if (target.equals("next")) {
                    callSes.setCurrentRow(curRow + pageRow);
                }
                // 前ページへ
                else {
                    callSes.setCurrentRow(curRow - pageRow);
                }
            }

            // ソート変更
            else if ((target = req.getParameter("sort")) != null) {
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "sort = " + target);
		}
                callSes.setSortKey(target);
                callSes.setOrderReverse();
                callSes.setCurrentRow(1);
            }

            // メッセージモードの変更
            else if ((target = req.getParameter("execute")) != null) {
                Vector v = getCallCd(req);

                // チェック状態を読む
                for (int i = 0; i < v.size(); i++) {
                    callSes.setChecked((String)v.elementAt(i), true);
                }

                // エラーメッセージ(OK)
                if (req.getParameter("mode").equals("delete")) {
                    // 削除確認
                    if (v.size() > 0) {
                        callSes.setMessageMode(callSes.MESMODE_CONFIRM);
                    }
                    // チェックが無い
                    else {
                        callSes.setMessageMode(callSes.MESMODE_NO_CHECK);
                    }
                }
                // エラーメッセージ(はい/いいえ)
                else if (target.equals("update")) {
                    if (req.getParameter("yes") != null) {
                        // 削除実行
                        if (v.size() > 0) {
                            CallListController callCtrl = new CallListController();
                            int stat = callCtrl.deleteDisplay(req, res, ses, v);
                            // 削除不可
                            if (stat == 0) {
                                callSes.setMessageMode(callSes.MESMODE_CANNOT_DELETE);
                            }
                            // 削除完了
                            else {
                                callSes.setMessageMode(callSes.MESMODE_DELETE);
                                callSes.clearChecked();
                            }
                        }
                        // チェックが無い
                        else {
                            callSes.setMessageMode(callSes.MESMODE_NO_CHECK);
                        }
                    }
                }
            }

            // Go to the next page
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "wm/WmCallList/index.html");
        } catch (Exception e) {
			log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(req, res, e);
        }
    }

    /**
     * チェックされているコール内容コードを取得する。
     * @param req 要求オブジェクト
     * @return 各要素がコール内容コードとなっているベクタ<br>
     *         チェックが無ければ null
     */
    private Vector getCallCd(HttpServletRequest req) {
        // コール内容コードの取り出し
        Vector v   = new Vector();
        String s[] = req.getParameterValues("checkbox");
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                s[i] = encode(s[i]);
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "param = " + s[i]);
		}
                v.addElement(s[i]);
            }
        }
        return v;
    }

    /**
     *
     */
    private String encode(String value) {
        if (value != null) {
            try {
                value = new String(value.getBytes("8859_1"), "SJIS");
            }
            catch (UnsupportedEncodingException e) {
                log("", e);
            }
        }
        return value;
    }
}
