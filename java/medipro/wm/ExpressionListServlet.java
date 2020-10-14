package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;


/**
 * Medipro 定型文一覧管理
 * @author
 * @version
 */
public class ExpressionListServlet extends HttpServlet {

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
            // 定型文一覧画面用セッション情報取得＆初期設定
            if ( ! ExpressionListController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_EXPRESSIONLIST_SESSION;
            ExpressionListSession subSession = (ExpressionListSession)session.getValue(subKey);

            String url = "wm/WmExpressionList/index.html";
            String target = null;
            subSession.setStatus(ExpressionListSession.STATE_NORMAL);

            // ソート処理
            if ( (target = request.getParameter("sort")) != null ) {
                subSession.setSortKey(target);
                String order = subSession.getOrder();
                order = order.equals("ASC") ? "DESC" : "ASC";
                subSession.setOrder(order);
                subSession.setPage(1);
                subSession.clearCheck();
            }
            // ページ切り替え処理
            else if ( (target = request.getParameter("page")) != null ) {
                int page = subSession.getPage();
                if ( target.equals("prev") && subSession.hasPrevPage() ) {
                    page--;
                }
                else if ( target.equals("next") && subSession.hasNextPage() ) {
                    page++;
                }
                subSession.setPage(page);
                subSession.clearCheck();
            }
            // 実行処理
            else if ( (target = request.getParameter("execute")) != null ) {
                Vector v = getTeikeibunCd(request, subSession);
                // 確認
                if ( target.equals("confirm") == true ) {
                    if ( v.size() > 0 ) {
                        subSession.setStatus(ExpressionListSession.STATE_CONFIRM);
                    }
                    else {
                        subSession.setStatus(ExpressionListSession.STATE_ALERT);
                    }
                }
                // 削除
                else if ( target.equals("delete") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            ExpressionListController ctrl =
                                new ExpressionListController();
                            ctrl.deleteDisplay(request, response,
                                               common, subSession, v);
                            subSession.clearCheck();
                            subSession.setStatus(ExpressionListSession.STATE_REPORT);
                        }
                        else { // 必要か？
                            subSession.setStatus(ExpressionListSession.STATE_ALERT);
                        }
                    }
                }
            }
            // Go to the next page
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT + url);
        } catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request,response, e);
        }
    }

    /**
     * 定型文コードの取り出しとチェック状態の更新
     * @param request HttpServletRequest
     * @param session ExpressionListSession
     * @return 定型文コード
     */
    private Vector getTeikeibunCd(HttpServletRequest request,
                                  ExpressionListSession session) {
        Vector v = new Vector();
        session.clearCheck();
        String s[] = request.getParameterValues("checkbox");
        if ( s != null ) {
            for ( int i = 0; i < s.length; i++ ) {
                v.addElement(s[i]);
                session.setCheck(s[i], "checked");
            }
        }
        return v;
    }
}
