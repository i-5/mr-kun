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
 * <br>キャッチ画像の追加・変更画面を呼び出す.
 * @author
 * @version
 */
public class CatchAddition extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**会社キャッチ画像追加・変更htmlファイル相対パス*/
    private static final String CATCH_UPDATE_PATH = "wm/WmCatchUpdate/index.html";
	
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String pictureCD;			// 画像コード
//      private Common common;				// コモンセッション
//      private CatchUpdateSession cuses;	// キャッチ画像追加・変更セッション
//      private CatchListSession cases;		// キャッチ画像一覧セッション
	
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
		String pictureCD;			// 画像コード
		Common common;				// コモンセッション
		CatchUpdateSession cuses;	// キャッチ画像追加・変更セッション
		CatchListSession cases;		// キャッチ画像一覧セッション

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else if (new DBConnect().isConnectable() == false) {
				new DispatManager().distribute(request, response);
			}
			else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String updKey = SysCnst.KEY_CATCHUPDATE_SESSION;
				HttpSession session = request.getSession(true);
				// コモンセッションの取得
				common = (Common) session.getValue(comKey);
				// キャッチ画像追加・変更セッションの取得
				cuses = (CatchUpdateSession) session.getValue(updKey);
				// キャッチ画像一覧セッションの取得
				cases = (CatchListSession) session.getValue
					(SysCnst.KEY_CATCH_SESSION);
				
				DispatManager dm = new DispatManager();
				// 権限チェック
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// ウェブマスタ以外の場合
					dm.distAuthority(request, response);
				}
				
				if (cuses == null) {
					cuses = new CatchUpdateSession();
					session.putValue(updKey, cuses);
					cuses.setFirstFlg(true);
				}
				
				if (cases != null) {
					cases.crearCheckValue();
					cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
				}
				
				// セッションのタイトルクリア
				cuses.setPictureName(null);
				cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
				cuses.unCheck();
				// パラメータ取得
				pictureCD = request.getParameter("pictureCD");
				if (pictureCD != null) {
					// 更新
					// セッションに画像コードセット
					cuses.setPictureCD(pictureCD);
					// 更新フラグセット
					cuses.setUpdateFlg(true);
				}
				else {
					// 新規追加時
					// 更新フラグセット
					cuses.setUpdateFlg(false);
					// セッションの画像コードクリア
					cuses.setPictureCD(null);
					cuses.setPictureType("2");
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_UPDATE_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
