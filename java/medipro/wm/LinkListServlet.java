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
 * Medipro リンク一覧管理
 * @author
 * @version
 */
public class LinkListServlet extends HttpServlet {

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
            // リンク一覧画面用セッション情報取得＆初期設定
            if ( LinkListController.initSession(request, response) != true ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_LINKLIST_SESSION;
            LinkListSession subSession = (LinkListSession)session.getValue(subKey);

            String url = "wm/WmLinkList/index.html";
            String target = null;
            subSession.setStatus(LinkListSession.STATE_NORMAL);

            String defLinkCd = request.getParameter("radiobutton");
            subSession.setDefaultLinkCd(defLinkCd);

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
                Vector v = getLinkCd(request, subSession);
                                // 確認
                if ( target.equals("confirm") == true ) {
                    // 保存
                    if ( request.getParameter("update") != null ) {
                        subSession.setStatus(LinkListSession.STATE_CONFIRM_2);
                    }
                    // 削除
                    else {
                        if ( v.size() > 0 ) {
                            LinkListController ctrl = new LinkListController();
                            boolean bDelete = isDelete(request, response, common, subSession, ctrl, v);
                            subSession.setStatus(bDelete ? LinkListSession.STATE_CONFIRM_1
                                                 : LinkListSession.STATE_ALERT_2
                                                 );
                        }
                        else {
                            subSession.setStatus(LinkListSession.STATE_ALERT_1);
                        }
                    }
                }
                                // 保存
                if ( target.equals("update") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        LinkListController ctrl = new LinkListController();
                        ctrl.updateDisplay(request, response,
                                           common, subSession,
                                           subSession.getDefaultLinkCd());
                        subSession.setStatus(LinkListSession.STATE_REPORT_2);
                    }
                }
                                // 削除
                if ( target.equals("delete") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            LinkListController ctrl = new LinkListController();
                            boolean bDelete = isDelete(request, response, common, subSession, ctrl, v);
                            if ( bDelete ) {
                                ctrl.deleteDisplay(request, response,
                                                   common, subSession, v);
                                subSession.clearCheck();
                                subSession.setStatus(LinkListSession.STATE_REPORT_1);
                            }
                            else {
                                subSession.setStatus(LinkListSession.STATE_ALERT_2);
                            }
                        }
                        else { // 必要か？
                            subSession.setStatus(LinkListSession.STATE_ALERT_1);
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
     * リンクコードの取り出しとチェック状態の更新
     * @param request HttpServletRequest
     * @param session LinkListSession
     * @return リンクコード
     */
    private Vector getLinkCd(HttpServletRequest request,
                             LinkListSession session) {
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

    /**
     * 削除実行確認
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param com 共用セッション情報
     * @param ses リンク一覧画面用セッション情報
     * @param ctrl リンク一覧画面制御オブジェクト
     * @param list 削除対象のリンクコードリスト
     * @return リンクコード
     */
    private boolean isDelete(HttpServletRequest req, HttpServletResponse res,
                             Common com, LinkListSession ses,
                             LinkListController ctrl, Vector list) {
        boolean bDelete = true;
        String code = ctrl.createDisplaySub(req, res, com, ses);
        if ( code != null ) {
            for ( int i = 0; i < list.size() ; i ++ ) {
                if ( code.equals((String)list.elementAt(i)) == true ) {
                    bDelete = false;
                    break;
                }
            }
        }
        return bDelete;
    }
}
