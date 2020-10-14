package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.*;


/**
 * Medipro ページ管理
 */
public class NaviServlet extends HttpServlet {

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
            // セッションタイムアウトチェック
            if (!SessionManager.check(request)) {
                new DispatManager().distSession(request, response);
                return;
            }

            // 各画面用セッション情報のリセット
			//              SessionManager.reset(request);

            // Go to the next page
            String url = request.getParameter("url");
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT + url);
        } catch (Exception e) {
			log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request,response, e);
        }
    }
}
