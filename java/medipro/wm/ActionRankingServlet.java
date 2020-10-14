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
 * Medipro 重要度追加・変更管理
 * @author 
 * @version
 */
public class ActionRankingServlet extends HttpServlet {

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
            // 重要度追加・変更画面用セッション情報取得＆初期設定
            if ( !ActionRankingController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_ACTIONRANKING_SESSION;
            ActionRankingSession subSession = (ActionRankingSession)session.getValue(subKey);

            String url = "wm/WmActionRanking/index.html";

            subSession.setStatus(ActionRankingSession.STATE_NORMAL);

            // 「確認」呼出し
            if ( request.getParameter("check") != null ) {
                ActionRankingController ctrl = new ActionRankingController();
                CompanyInfo info = ctrl.createDisplay(request, response, common);
                subSession.setCompanyInfo(info);
            }
            // 「保存」ボタン押下
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(ActionRankingSession.STATE_CONFIRM);
            }
            // 「保存しますか？」「はい」ボタン押下
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                CompanyInfo info = subSession.getCompanyInfo();
                ActionRankingController ctrl =
                    new ActionRankingController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(ActionRankingSession.STATE_REPORT);
            }
            // 「元に戻る」ボタン押下
            else if ( request.getParameter("back") != null ) {
                subSession.setCompanyInfo(null);
                url = "wm/WmActionUpdate/index.html";
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
     * @param sub 重要度の追加・更新画面情報を積んだセッション情報
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              ActionRankingSession sub) {
        CompanyInfo info = sub.getCompanyInfo();
        if ( info == null )        info = new CompanyInfo();
        info.setDisplayRanking(Converter.getParameter(request, "ranking"));
        sub.setCompanyInfo(info);
    }

}
