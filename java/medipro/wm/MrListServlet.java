package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrListSession;
import jp.ne.sonet.medipro.wm.server.session.MrClientSession;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.SentakuTorokuInfoManager;

/**
 * <strong>MR管理 - MRの一覧画面対応Servletクラス</strong>。
 * @author  doppe
 * @version 1.00
 */
public class MrListServlet extends HttpServlet {

    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrListServlet called!");
		}

		//セッションチェック
		if (! new SessionManager().check(req)) {
			new DispatManager().distSession(req, res);
			return;
		}

		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

	//権限チェック
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
			!common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
			new DispatManager().distAuthority(req, res);
			return;
		}

		//画面パラメータの取得
		String action = req.getParameter("action");
		String newTarget = req.getParameter("sortTarget");
		String page = req.getParameter("page");
		String removeOk = req.getParameter("removeOk");
		String removeCancel = req.getParameter("removeCancel");
		String replaceOk = req.getParameter("replaceOk");

	//MR一覧用セッションオブジェクトの取得
	//無ければ新たに登録
		MrListSession mrListSession =
			(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		if (mrListSession == null) {
			mrListSession = new MrListSession();
			req.getSession(true).putValue(SysCnst.KEY_MRLIST_SESSION, mrListSession);
		}

		//statusを正常に...
		mrListSession.setStatus(MrListSession.NORMAL);
	
		//チェックボックスの選択状態を保存
		String[] selections = req.getParameterValues("selection");
		MrListSession sess = (MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		sess.setCheckedList(selections);

		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";

		try {
			if (removeOk != null) {
				//削除実行
				executeRemove(req, res, selections);
			} else if (removeCancel != null) {
				//削除中止
				mrListSession.setStatus(MrListSession.NORMAL);
			} else if (replaceOk != null) {
				//担当顧客変更
				if (setReplaceInfo(req, res, selections, true)) {
					sess.clear();
					nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";
				}
			} else if (action == null) {
				//一覧表示
				mrListSession.setRefMrId("");
				mrListSession.setRefMrName("");
				setSortInfo(req, res, newTarget, page);
			} else if (action.equals("remove")) {
				//削除確認
				setRemoveInfo(req, res, selections);
			} else if (action.equals("add")) {
				//追加画面
				setAddInfo(req, res);
				sess.clear();
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrUpdate/index.html";
			} else if (action.equals("update")) {
				//変更画面
				sess.clear();
				String mrId = req.getParameter("mrId");
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrUpdateServlet?action=update&mrId=" + mrId;
			} else if (action.equals("replace")) {
				//担当顧客変更
				if (setReplaceInfo(req, res, selections, false)) {
					sess.clear();
					nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";
				}
			} else {
				//一覧表示
				mrListSession.setRefMrId("");
				mrListSession.setRefMrName("");
				setSortInfo(req, res, newTarget, page);
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}

    }

    /**
     * MR管理 - 担当顧客変更画面移動のチェック or 前準備。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private boolean setReplaceInfo(HttpServletRequest req,
								   HttpServletResponse res,
								   String[] selections,
								   boolean moveFlag) throws IOException {
		//まずは担当顧客変更画面用セッションオブジェクトの生成と登録
		MrClientSession mcSession = new MrClientSession();
		req.getSession(true).putValue(SysCnst.KEY_MRCLIENT_SESSION, mcSession);

		if (selections != null) {
			if (selections.length > 0) {
				//1個だけチェック
				mcSession.setLeftMrId(selections[0]);
			}
			if (selections.length > 1) {
				//2個チェック
				mcSession.setRightMrId(selections[1]);
			}
		}

		if (selections == null || selections.length < 3 || moveFlag) {
			//チェックが2個以下だったらそのまま画面遷移
			return true;
		} else {
			//チェックが3個以上だったら警告を出力
			MrListSession mlSession =
				(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
			mlSession.setStatus(MrListSession.REPLACE_CONFIRM);
			return false;
		}
    }

    /**
     * MR管理 - MRの追加・変更画面への移動。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void setAddInfo(HttpServletRequest req,
							HttpServletResponse res) throws IOException {
		req.getSession(true).removeValue(SysCnst.KEY_MRUPDATE_SESSION);
    }

    /**
     * MR管理 - 一覧表示ソート条件の設定。
     * @param 
     */
    private void setSortInfo(HttpServletRequest req,
							 HttpServletResponse res,
							 String newTarget,
							 String page) throws IOException {
		//セッション情報の取得
		MrListSession mrListSession =
			(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);

		if (newTarget != null) {
			mrListSession.setSortTarget(newTarget);
		}

		if (page != null && page.equals("prev")) {
			mrListSession.previousPage();
		} else if (page != null && page.equals("next")) {
			mrListSession.nextPage();
		}
    }

    /**
     * 削除情報のセット。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void setRemoveInfo(HttpServletRequest req,
							   HttpServletResponse res,
							   String[] selections) throws IOException {
		MrListSession session =
			(MrListSession)req.getSession().getValue(SysCnst.KEY_MRLIST_SESSION);
	
		if (selections == null) {
			session.setStatus(MrListSession.NO_SELECTION);
		} else {
			for (int i = 0; i < selections.length; i++) {
				if (hasDrInCharge(selections[i])) {
					session.setStatus(MrListSession.HAS_DR_IN_CHARGE);
					return;
				}
			}
			session.setStatus(MrListSession.REMOVE_CONFIRM);
		}
    }

    /**
     * 担当する医師が存在するか検査する.
     * @param mrId 検査対象のMR-ID
     * @return 一人でも存在したらtrue
     */
    private boolean hasDrInCharge(String mrId) {
		DBConnect dbConnect = new DBConnect();
		Connection connection = null;
		int chargeCount = 0;
	    
		try {
			connection = dbConnect.getDBConnect();
			SentakuTorokuInfoManager manager = new SentakuTorokuInfoManager(connection);
			chargeCount = manager.getDrCountInCharge(mrId);
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}

		if (chargeCount > 0) {
			return true;
		}

		return false;
    }

    /**
     * 削除実行。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void executeRemove(HttpServletRequest req,
							   HttpServletResponse res,
							   String[] selections) throws IOException {
		MrListSession session =
			(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		Common common =
			(Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

		if (selections == null) {
			session.setStatus(MrListSession.NO_SELECTION);
		} else {
			for (int i = 0; i < selections.length; i++) {
				if (hasDrInCharge(selections[i])) {
					session.setStatus(MrListSession.HAS_DR_IN_CHARGE);
					return;
				}
			}

			DBConnect dbConnect = new DBConnect();
			Connection connection = null;
	    
			try {
				connection = dbConnect.getDBConnect();
				MrInfoManager manager = new MrInfoManager(connection);
				manager.removeMr(selections, common);
				session.setStatus(MrListSession.REMOVE_DONE);
			} catch (Exception ex) {
				throw new WmException(ex);
			} finally {
				dbConnect.closeDB(connection);
			}
		}	
    }
}
