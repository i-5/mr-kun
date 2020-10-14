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
 * <strong>支店・営業所一覧対応Servletクラス</strong>
 * <br>ソート処理およびページ移動処理をを行う.
 * @author
 * @version
 */
public class BranchList extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**支店・営業所一覧JSPファイル相対パス*/
    private final String BRANCH_LIST_PATH = "wm/WmBranchList/mainframe.jsp";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private Common common;
//      private BranchListSession brses;
//      private String sortKey;
//      private String page;
//      private int current;
//      private int pageRow;
	
    ////////////////////////////////////////////////////
    //class method
    //

    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)	{
    	doPost(request, response);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Common common;
		BranchListSession brses;
		String sortKey;
		String page;
		int current;
		int pageRow;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String branchKey = SysCnst.KEY_BRANCHLIST_SESSION;
				// セッションの取得
				HttpSession session = request.getSession(true);
				common = (Common) session.getValue(comKey);
				brses = (BranchListSession) session.getValue(branchKey);
				if (brses == null) {
					brses = new BranchListSession();
					session.putValue(branchKey, brses);
				}
				// チェック状態クリア
				brses.crearCheckValue();
				// メッセージＩＤセット
				brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
			
				// パラメータ取得
				page = request.getParameter("page");

				if (page == null) {
					// ソート順切り替え時
					sortKey = request.getParameter("sortKey");

					if (sortKey != null) {
						// ソートキーのセット
						brses.setSortKey(sortKey);
						// ソート順逆転
						brses.reverseOrder();
						// 先頭行を１にセット
						brses.setCurrentRow(1);
					}
				} else {
					// ページ切り替え時
					current = brses.getCurrentRow();
					pageRow = common.getShitenLine();
					if (page.equals("next")) {
						// 次ページ処理
						brses.setCurrentRow(current + pageRow);
					} else {
						// 前ページ処理
						if (current > 1) {
							brses.setCurrentRow(current - pageRow);
						}
					}
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + BRANCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
