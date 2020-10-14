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
 * Medipro サブマスター一覧管理
 * @author
 * @version
 */
public class SubListServlet extends HttpServlet {

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
            if ( SubListController.initSession(request, response) != true ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String subKey = SysCnst.KEY_SUBLIST_SESSION;
            SubListSession subSession =        (SubListSession)session.getValue(subKey);

            String target = null;
            subSession.setStatus(SubListSession.STATE_NORMAL);

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
                Vector v = getMrId(request, subSession);
                if ( target.equals("confirm") == true ) {
                    if ( v.size() > 0 ) {
                        subSession.setStatus(SubListSession.STATE_CONFIRM);
                    }
                    else {
                        subSession.setStatus(SubListSession.STATE_ALERT);
                    }
                }
                if ( target.equals("update") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            SubListController ctrl = new SubListController();
                            ctrl.updateDisplay(request, response, session, v);
                            subSession.clearCheck();
                            subSession.setStatus(SubListSession.STATE_REPORT);
                        }
                        else { // 必要か？
                            subSession.setStatus(SubListSession.STATE_ALERT);
                        }
                    }
                }
            }
            // Go to the next page
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT+"/wm/WmSubList/index.html");
        } catch (Exception e) {
			log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request,response, e);
        }
    }

    /**
     * ＭＲ－ＩＤの取り出しとチェック状態の更新
     * @param request HttpServletRequest
     * @param session SubListSession
     * @return ＭＲ－ＩＤリスト
     */
    private Vector getMrId(HttpServletRequest request, SubListSession session)
    {
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
