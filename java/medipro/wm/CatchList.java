package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>会社キャッチ画像一覧対応Servletクラス</strong>
 * <br>ソート処理およびページ移動処理をを行う.
 * @author
 * @version
 */
public class CatchList extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**会社キャッチ画像一覧JSPファイル相対パス*/
    private final String CATCH_LIST_PATH = "wm/WmCatchList/mainframe.jsp";
	
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String page;
//      private int current;
//      private int pageRow;
//      private Common common;
//      private CatchListSession cases;
	
    ////////////////////////////////////////////////////
    //class method
    //
	
    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    	doPost(request, response);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String page;
		int current;
		int pageRow;
		Common common;
		CatchListSession cases;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String catchKey = SysCnst.KEY_CATCH_SESSION;
				HttpSession session = request.getSession(true);
				// コモンセッション取得
				common = (Common) session.getValue(comKey);

				DispatManager disp = new DispatManager();
				// 権限チェック
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// ウェブマスタ以外の場合
					disp.distAuthority(request, response);
				}
				else {
					// キャッチ画像一覧セッション取得
					cases = (CatchListSession) session.getValue(catchKey);
					if (cases == null) {
						cases = new CatchListSession();
						session.putValue(catchKey, cases);
					}

					// メッセージIDセット
					cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					// パラメータ取得
					page = request.getParameter("page");
					if (page == null ) {
						// ソート時処理
						// ソート順を逆転
						cases.setOrderReverse();
						// 先頭行を１にセット
						cases.setCurrentRow(1);
					}
					else {
						// ページ移動処理
						// 現ページの先頭行を取得
						current = cases.getCurrentRow();
						// １ページの行数を取得
						pageRow = common.getCatchLine();
						if (page.equals("next")) {
							// 次ページ押下時
							cases.setCurrentRow(current + pageRow);
						}
						else {
							// 前ページ押下時
							if (current > 1) {
								cases.setCurrentRow(current - pageRow);
							}
						}
					}
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
