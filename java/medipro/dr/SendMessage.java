package medipro.dr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;

/**
 * Medipro DR送信メッセージ詳細
 */
public class SendMessage extends HttpServlet {
	
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if	( sm.check(request, 0) ) {
				sm.reset(request,0);

				// Go to the next page
				// response.sendRedirect("/medipro/dr/DrSendMessage/index.html");
				response.sendRedirect("/medipro/dr/DrSendMessage/index.html?" + request.getQueryString());
			} else {
				// セッションエラーの場合
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

}

