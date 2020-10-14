package medipro.dr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.common.util.*;

/**
 * Medipro DRフロントでの表示制御
 */
public class ReceiveCtl extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String msg;
		String nextpage = null;

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if	( sm.check(request, 0) ) {
				sm.reset(request,0);

				// Go to the next page
				//response.sendRedirect("/medipro/dr/DrWaitingRoom/index.html");

				// 次の画面をセット
				if ( request.getParameter("com_mrid") == null ) {
					msg = "「MRからIDをもらってください」";
					response.sendRedirect("/medipro/dr/DrMrInput/index.html");
				} else  {
					String kensu = request.getParameter("kensu");
					if ( kensu.equals("0")) {
						//nextpage = "/";
					} else if ( kensu.equals("1")) {
						HttpSession session = request.getSession(true);
						session.putValue("DrReceiveSaveBoxList", "NO");//1124 y-yamada add NO.62
						nextpage = "/medipro/dr/DrReceiveMessage/index.html";
					} else {
						nextpage = "/medipro/dr/DrMessageList/index.html";
					}
					response.sendRedirect(nextpage);
				}
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
