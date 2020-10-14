package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.*;


/**
 * Medipro サブマスター追加・更新管理
 * @author
 * @version
 */
public class SubUpdateServlet extends HttpServlet {

    /**
     * doGet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    /**
     * doPost
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // サブマスター一覧画面用セッション情報取得＆初期設定
            if ( SubUpdateController.initSession(request, response) != true ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common com = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_SUBUPDATE_SESSION;
            SubUpdateSession sub = (SubUpdateSession)session.getValue(subKey);
            if ( sub == null ) {
                sub = new SubUpdateSession();
                session.putValue(subKey, sub);
            }

            String url = "wm/WmSubUpdate/index.html";
            sub.setStatus(SubUpdateSession.STATE_NORMAL);

            // 「確認」ボタン押下
            if ( request.getParameter("check") != null ) {
                sub.setStatus(checkTarget(request, response, com, sub, false));
            }
            // 「保存」ボタン押下
            else if ( request.getParameter("confirm") != null ) {
                int status = checkTarget(request, response, com, sub, true);
                if ( status == SubUpdateSession.STATE_NORMAL ) {
                    status = SubUpdateSession.STATE_CONFIRM;
                }
                sub.setStatus(status);
            }
            // 「保存しますか？」「はい」ボタン押下
            else if ( request.getParameter("update") != null ) {
                int status = checkTarget(request, response, com, sub, true);
                if ( status == SubUpdateSession.STATE_NORMAL ) {
                    MrInfo info = sub.getMrInfo();
                    SubUpdateController ctrl = new SubUpdateController();
                    ctrl.updateDisplay(request, response, session, info);
                    status = SubUpdateSession.STATE_REPORT;
                }
                sub.setStatus(status);
            }
            // 「元に戻る」ボタン押下
            else if ( request.getParameter("back") != null ) {
                sub.setMrInfo(null);
                url = "wm/WmSubList/index.html";
            }
            else {
                updateTarget(request, response, com, sub);
            }
            if ( SysCnst.DEBUG ) {
                log("URL="+url);
            }
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT+url);
        } catch (Exception e) {
			log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request, response, e);
        }
    }

    /**
     * 対象ＭＲの状態更新
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ログイン者の情報を積んだセッション情報
     * @param sub サブマスター追加・更新画面情報を積んだセッション情報
     */
    private void updateTarget(HttpServletRequest request,
                            HttpServletResponse response,
                            Common common,
                            SubUpdateSession sub) {
        MrInfo info = sub.getMrInfo();
        if ( info == null ) {
            info = new MrInfo();
            info.setName("");
            info.setShitenName("");
            info.setEigyosyoName("");
            info.setMrAttributeName1("");
            info.setMrAttributeName2("");
        }
        String soshiki = request.getParameter("soshiki");
        String attribute = request.getParameter("attribute");
        info.setMrId(Converter.getParameter(request, "mrid"));
        info.setMasterKengenSoshiki(soshiki.equals("0") ? null : soshiki);
        info.setMasterKengenAttribute(attribute.equals("0") ? null : attribute);
        if ( soshiki.equals("0") && attribute.equals("0") ) {
            info.setMasterFlg(null);
        }
        else {
            info.setMasterFlg(SysCnst.FLG_MASTER_SUB);
        }
        sub.setMrInfo(info);
    }

    /**
     * 対象ＭＲの権限チェック
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ログイン者の情報を積んだセッション情報
     * @param sub サブマスター追加・更新画面情報を積んだセッション情報
     * @param bUpdateKengen 権限状態反映フラグ
     * @return 状態コード
     */
    private int checkTarget(HttpServletRequest request,
                            HttpServletResponse response,
                            Common common,
                            SubUpdateSession sub,
                            boolean bUpdateKengen) {
        int state = SubUpdateSession.STATE_NORMAL;
        MrInfo info = null;
        SubUpdateController ctrl = new SubUpdateController();
        String mid = request.getParameter("mrid");
        if ( SysCnst.DEBUG ) {
            log("MR-ID="+mid);
        }
        if ( mid != null ) {
            info = ctrl.createDisplay(request, response, mid);
            if ( info == null || info.getMrId() == null ) {
                if ( info == null ) {
                    info = new MrInfo();
                }
                info.setMrId(mid);
                info.setName("");
                info.setShitenName("");
                info.setEigyosyoName("");
                info.setMrAttributeName1("");
                info.setMrAttributeName2("");
                state = SubUpdateSession.STATE_ALERT_1;
            }
            else {
                if ( SysCnst.DEBUG ) {
                    log("login-user="+common);
                    log("target-user="+info);
                }
                if ( common.getMrId().equals(info.getMrId()) ||
                     ( info.getMasterFlg() != null &&
                       info.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) ||
                     ( common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB) &&
                       !common.getShitenCd().equals(info.getShitenCd()) ) ) {
                    state = SubUpdateSession.STATE_ALERT_2;
                }
                if ( bUpdateKengen ) {
                    String soshiki = request.getParameter("soshiki");
                    String attribute = request.getParameter("attribute");
                    info.setMasterKengenSoshiki(
                                                soshiki.equals("0") ? null : soshiki);
                    info.setMasterKengenAttribute(
                                                  attribute.equals("0") ? null : attribute);
                    if ( soshiki.equals("0") && attribute.equals("0") ) {
                        info.setMasterFlg(null);
                    }
                    else {
                        info.setMasterFlg(SysCnst.FLG_MASTER_SUB);
                    }
                }
            }
        }
        sub.setMrInfo(info);
        return state;
    }
}

