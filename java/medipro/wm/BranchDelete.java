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
 * <strong>支店・営業所一覧対応Servletクラス</strong>
 * <br>支店・営業所の削除を行う.
 * @author
 * @version
 */
public class BranchDelete extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**削除対象を未選択*/
    private static final int RTN_NO_CHECK = -2;
    /**削除支店・営業所にMRが所属*/
    private static final int RTN_EXIST_MR = -1;
    /**削除可能*/
    private static final int RTN_DELETE_OK = 1;
    /**支店・営業所一覧JSPファイル相対パス*/
    private static final String BRANCH_LIST_PATH = "wm/WmBranchList/mainframe.jsp";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private BranchListSession brses;
//      private String mode;
//      private String checkList[];
//      private boolean existMr;
	
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
		boolean existMr = false;
		String mode = null;
		String checkList[] = null;
		BranchListSession brses = null;
		
		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				// セッションの取得
				HttpSession session = request.getSession(true);
				String branchKey = SysCnst.KEY_BRANCHLIST_SESSION;
				// 会社キャッチ画像セッション情報取得
				brses = (BranchListSession) session.getValue(branchKey);
			
				// パラメータ取得
				mode = request.getParameter("mode");
			
				// 削除支店コードクリア
				brses.crearDeleteBranch();
				// 削除営業所コードクリア
				brses.crearDeleteOffice();
				int rtn;
				// 画面操作の状況により処理分岐
				if (mode.equals("deletemessage")) {
					// ｢支店・営業所を削除する｣押下時
					// チェックボックス選択状態取得
					getCheck(request, existMr, checkList, brses);
					// チェック数チェック
					rtn = checkParameter(request, existMr, checkList);
					if (rtn == RTN_NO_CHECK) {
						// チェックされていない場合
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NOCHECK);
					} else {
						// チェックされている場合
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_CONFIRM);
					}
				} else if (mode.equals("nodelete")) {
					// ｢削除しますか｣で｢いいえ」押下時
					// チェックボックス選択状態取得
					getCheck(request, existMr, checkList, brses);
					// メッセージIDセット
					brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
				} else if (mode.equals("delete")) {
					// ｢削除しますか｣で｢はい｣押下時
					// チェックボックス選択状態取得
					getCheck(request, existMr, checkList, brses);
					// チェック数およびＭＲ所属チェック
					rtn = checkParameter(request, existMr, checkList);
					if (rtn == RTN_EXIST_MR) {
						// ＭＲが所属していた場合
						// メッセージIDセット
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_CANNOT);
					} else if (rtn == RTN_NO_CHECK) {
						// チェックされていない場合
						// メッセージIDセット
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NOCHECK);
					} else {
						// チェックＯＫ
						BranchListController bctrl
							= new BranchListController(request, response);
						// 支店・営業所削除処理コール
						bctrl.deleteBranch(session);
						// メッセージIDセット
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_DONE);
					}
				} else {
					// チェックボックス選択状態取得
					getCheck(request, existMr, checkList, brses);
					// メッセージIDセット
					brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
				}
			}
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + BRANCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
	
    /**
     * checkParameter.
     * <dl>削除対象のチェックを行う</dl>
     * @return int
     * @param request HttpServletRequest
     */
    public int checkParameter(HttpServletRequest request,
							  boolean existMr,
							  String[] checkList) {
		int rtn = RTN_DELETE_OK;
		
		if (existMr == true) {
			// MRが所属していた場合
			rtn = RTN_EXIST_MR;
		}
		if (checkList == null) {
			// 削除対象未選択の場合
			rtn = RTN_NO_CHECK;
		}
		if (SysCnst.DEBUG) {
			log("return:"+rtn);
		}
		return rtn;
    }

    /**
     * getCheck.
     * <dl>選択されているチェックボックスの項目を取得する</dl>
     * @param request HttpServletRequest
     */
    public void getCheck(HttpServletRequest request,
						 boolean existMr,
						 String[] checkList,
						 BranchListSession brses) {
		try {
			// MRフラグクリア
			existMr = false;
			checkList = null;
			// チェックボックスセッションをクリア
			brses.crearCheckValue();
		  	
			Enumeration e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
		  	    
				if (key.equals("checkbox")) {
					checkList = request.getParameterValues(key);
					for (int i = 0; i < checkList.length; i++) {
						StringTokenizer stkn = 
							new StringTokenizer(checkList[i], ",");
						// 営業所コード取得
						String officeCD = stkn.nextToken();
						// 支店コード取得
						String branchCD = stkn.nextToken();
						// MRフラグ取得
						String mrFlg = stkn.nextToken();
						if (mrFlg.equals("true")) {
							// MRが所属していた場合
							existMr = true;
						}
						// チェックボックスセッションに支店コードをセット
						brses.setCheckValue(branchCD + "," + officeCD);
						if (officeCD.equals("nooffice")) {
							// 営業所がない場合
							// 削除支店セッションに支店コードをセット
							brses.setDeleteBranch(branchCD);
						} else {
							// 営業所がある場合
							// 削除営業所セッションに営業所コードをセット
							brses.setDeleteOffice(officeCD);
						}
					}
				}
			}
		} catch ( Exception e ) {
			throw new WmException(e);
		}
    }
}
