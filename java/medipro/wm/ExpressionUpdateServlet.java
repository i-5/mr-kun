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
 * Medipro 定型文追加・変更管理
 * @author 
 * @version
 */
public class ExpressionUpdateServlet extends HttpServlet {

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
            // 定型文追加・変更画面用セッション情報取得＆初期設定
            if ( !ExpressionUpdateController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_EXPRESSIONUPDATE_SESSION;
            ExpressionUpdateSession subSession = (ExpressionUpdateSession)session.getValue(subKey);

            String url = "wm/WmExpressionUpdate/index.html";

            subSession.setStatus(ExpressionUpdateSession.STATE_NORMAL);

            // 「確認」呼出し
            if ( request.getParameter("check") != null ) {
                String code = request.getParameter("teikeibun_cd");
                if ( code != null ) {
                    ExpressionUpdateController ctrl = new ExpressionUpdateController();
                    ExpressionLibInfo info = ctrl.createDisplay(request, response, common, code);
                    subSession.setExpressionLibInfo(info);
                }
                else {
                    subSession.setExpressionLibInfo(null);
                }
            }
            // 「保存」ボタン押下
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(ExpressionUpdateSession.STATE_CONFIRM);
            }
            // 「保存しますか？」「はい」ボタン押下
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                ExpressionLibInfo info = subSession.getExpressionLibInfo();
                ExpressionUpdateController ctrl =
                    new ExpressionUpdateController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(ExpressionUpdateSession.STATE_REPORT);
            }
            // 「元に戻る」ボタン押下
            else if ( request.getParameter("back") != null ) {
                subSession.setExpressionLibInfo(null);
                url = "wm/WmExpressionList/index.html";
            }
            else {
                updateTarget(request, response, common, subSession);
            }
            if ( SysCnst.DEBUG ) {
                log("URL="+url);
            }
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT+url);
        } 
        catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request, response, e);
        }
    }

    /**
     * 対象の状態更新
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ログイン者の情報を積んだセッション情報
     * @param sub 定型文の追加・更新画面情報を積んだセッション情報
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              ExpressionUpdateSession sub) {
        ExpressionLibInfo info = sub.getExpressionLibInfo();
        if ( info == null )        info = new ExpressionLibInfo();
        info.setTitle(Converter.getParameter(request, "title"));
        info.setDescription(Converter.getParameter(request, "description"));
        info.setBunruiCode(Converter.getParameter(request, "bunrui"));
        info.setHonbun(Converter.getParameter(request, "honbun"));
        sub.setExpressionLibInfo(info);
    }

}
