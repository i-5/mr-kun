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
 * <strong>会社キャッチ画像一覧対応Servletクラス</strong>
 * <br>基本画面の変更およびキャッチ画像の削除を行う.
 * @author
 * @version
 */
public class CatchUpdate extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**削除対象を未選択*/
    private static final int RTN_NO_CHECK = -2;
    /**基本画面を削除対象に設定*/
    private static final int RTN_ON_DEFAULT = -1;
    /**削除可能*/
    private static final int RTN_DELETE_OK = 1;
    /**会社キャッチ画像一覧JSPファイル相対パス*/
    private static final String CATCH_LIST_PATH = "wm/WmCatchList/mainframe.jsp";
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String mode;
//      private String radio;
//      private String checkList[];
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
		String mode;
		String radio;
		String checkList[] = null;
		CatchListSession cases;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else {
				HttpSession session = request.getSession(true);
				String catchKey = SysCnst.KEY_CATCH_SESSION;
				// 会社キャッチ画像セッション情報取得
				cases = (CatchListSession) session.getValue(catchKey);
				if (cases == null) {
					cases = new CatchListSession();
					session.putValue(catchKey, cases);
				}

				// コモンセッションの取得
				Common common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);
				
				DispatManager dm = new DispatManager();
				// 権限チェック
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// ウェブマスタ以外の場合
					dm.distAuthority(request, response);
				} else {
					// パラメータ取得
					mode = request.getParameter("mode");
					radio = request.getParameter("radiobutton");
					
					int rtn;
					// 画面操作の状況により処理分岐
					if (mode.equals("updatemessage")) {
						// ｢保存｣ボタン押下時処理
						// メッセージIDをセット
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_UPDATE);
						// ラジオボタン状態情報をセット
						cases.setRadioValue(radio);
					} else if (mode.equals("update")) {
						// ｢保存しますか｣メッセージフォームから｢はい｣押下時処理
						// ラジオボタン状態情報をセット
						cases.setRadioValue(radio);
						// 更新処理コール
						callUpdateCatch(session, request, response, cases);
						// メッセージIDセット
						cases.setMessageState(SysCnst.CATCH_LSIT_MSG_SAVEDONE);
					} else if (mode.equals("deletemessage")) {
						// ｢選択した画像を削除する｣押下時
						// 削除内容チェック
						rtn = checkDelete(request, cases, checkList);
						switch (rtn) {
						case RTN_NO_CHECK: // 削除画像未選択の場合
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_NOCHECK);
							break;
						case RTN_ON_DEFAULT: // デフォルト画像に設定されていた場合
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DEFAULT);
							break;
						case  RTN_DELETE_OK: // 削除可能
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DELETE);
							break;
						}
					} else if (mode.equals("delete")) {
						// ｢削除しますか｣で｢はい｣押下時
						// 削除内容チェック
						rtn = checkDelete(request, cases, checkList);
						switch ( rtn ) {
						case RTN_NO_CHECK: // 削除画像未選択の場合
							// メッセージIDセット
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_NOCHECK);
							break;
						case RTN_ON_DEFAULT: // デフォルト画像に設定されていた場合
							// メッセージIDセット
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DEFAULT);
							break;
						case  RTN_DELETE_OK: // 削除可能
							// 削除処理コール
							callDeleteCatch(session, request, response);
							// メッセージIDセット
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DELDONE);
							break;
						}
					} else if (mode.equals("nodelete")) {
						// ｢削除しますか｣で｢いいえ」押下時
						rtn = checkDelete(request, cases, checkList);
						// メッセージIDセット
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else if (mode.equals("defaultok")) {
						// ｢デフォルト画像に...｣で｢OK｣押下時
						rtn = checkDelete(request, cases, checkList);
						// メッセージIDセット
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else if (mode.equals("nocheckok")) {
						// ｢削除する画像は...｣で｢OK｣押下時
						rtn = checkDelete(request, cases, checkList);
						// メッセージIDセット
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else {
						// ラジオボタン状態情報をクリア
						cases.crearRadioValue();
						// メッセージIDセット
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					}
				}
			}
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    /**
     * callUpdateCatch.
     * <dl>基本画面更新処理をコールする</dl>
     * @param session HttpSession
     */
    public void callUpdateCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response,
								CatchListSession cases) {
		String pictureCD;

		if (cases.getRadioValue() != null) {
			// 選択した画像コードを取得
			pictureCD = cases.getRadioValue();

			CatchListController cctrl = 
				new CatchListController(request, response);
			// 基本画像更新処理コール
			cctrl.updatePicture(session, pictureCD);
		}
		// ラジオボタン状態情報をクリア
		cases.crearRadioValue();
    }
	
    /**
     * checkDelete.
     * <dl>削除対象のチェックを行う</dl>
     * @return int
     * @param request HttpServletRequest
     */
    public int checkDelete(HttpServletRequest request,
						   CatchListSession cases,
						   String[] checkList) {

		int rtn = RTN_DELETE_OK;
		try {
			String	pictureCD;			// 画像コード
			boolean defaultFlg = false;	// デフォルト画像選択フラグ
			Enumeration e = request.getParameterNames();
		  	
			// セッションのチェックリストクリア
			cases.crearCheckValue();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
		  	    
				if (key.equals("checkbox")) {
					checkList = request.getParameterValues(key);
					for (int i = 0; i < checkList.length; i++) {
						if ( cases.getDefaultCD() != null ) {
							if (cases.getDefaultCD().equals(checkList[i])) {
								// デフォルト画像がチェックされていた場合
								// フラグセット
								defaultFlg = true;
							}
						}
						pictureCD = checkList[i];
						// セッションにチェックされている画像コードをセット
						cases.setCheckValue(pictureCD);
					}
				}
			}
			if (defaultFlg == true) {
				// デフォルト画像がチェックされていた場合
				rtn = RTN_ON_DEFAULT;
			}
			if (cases.getCheckSize() == 0) {
				// チェック非選択の場合
				rtn = RTN_NO_CHECK;
			}
		}
		catch (Exception e) {
			throw new WmException(e);
		}
		return rtn;
    }
	
    /**
     * call DeleteCatch.
     * <dl>キャッチ画像削除処理をコールする</dl>
     * @param session HttpSession
     */
    public void callDeleteCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response) {
		CatchListController cctrl =  new CatchListController(request, response);
		// キャッチ画像削除処理コール
		cctrl.deletePicture(session);
    }
}
