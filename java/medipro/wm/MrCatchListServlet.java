package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.server.entity.CatchInfo;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrCatchListSession;
import jp.ne.sonet.medipro.wm.server.manager.MrCatchInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;

/**
 * <strong>MRキャッチ画像一覧対応サーブレットクラス</strong>.
 * @author  doppe
 * @version 1.00
 */
public class MrCatchListServlet extends HttpServlet {
    
    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrCatchListServlet is called");
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
		String mrId = req.getParameter("mrId");
		String page = req.getParameter("page");
		String target = req.getParameter("target");
		String action = req.getParameter("action");
		String removeOk = req.getParameter("removeOk");
		String removeCancel = req.getParameter("removeCancel");
		String save = req.getParameter("save");
		String saveOk = req.getParameter("saveOk");
		String saveCancel = req.getParameter("saveCancel");

		//セッションが無ければ作成し登録
		MrCatchListSession session
			= (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);
		if (session == null) {
			session = new MrCatchListSession();
			req.getSession(true).putValue(SysCnst.KEY_MRCATCHLIST_SESSION, session);
		}

		//チェックボックスの状態を保持
		String[] selections = req.getParameterValues("selection");
		session.setCheckedList(selections);

	//statusをクリア
		session.setStatus(MrCatchListSession.NORMAL);

		String defaultPictureCd = req.getParameter("picture");
		if (defaultPictureCd != null) {
			session.getMrInfo().setPictureCd(defaultPictureCd);
		}

		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngCatchList/index.html";

		try {
			session.getMrInfo().setMrId(mrId);

			if (action != null && action.equals("remove")) {
				//削除確認
				session.setStatus(MrCatchListSession.REMOVE_CONFIRM);
			} else if (action != null && action.equals("add")) {
				//追加画面へ
				session.clear();
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchUpdateServlet?mrId="
					+ session.getMrInfo().getMrId();
			} else if (action != null && action.equals("update")) {
				//変更画面へ
				session.clear();
				String pictureCd = req.getParameter("pictureCd");
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchUpdateServlet?mrId="
					+ session.getMrInfo().getMrId() + "&pictureCd=" + pictureCd;
			} else if (page != null || target != null) {
				//ページ、ソートに対応
				setSortInfo(req, res);
			} else if (removeOk != null) {
				//削除実行
				executeRemove(req, res);
			} else if (removeCancel != null|| saveCancel != null) {
				//削除中止
			} else if (save != null) {
				//保存確認
				//  		session.setStatus(MrCatchListSession.SAVE_CONFIRM);
				executeSave(req, res);
				//  	    } else if (saveOk != null) {
				//  		//保存実行
				//  		executeSave(req, res);
			} else if (mrId != null) {
				//追加・変更画面から
				setDefaultPictureInfo(req, mrId);
				setSortInfo(req, res);
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}
    }

    /**
     * 更新実行.選択した画像をMRのデフォルト画像に設定します.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void executeSave(HttpServletRequest req, HttpServletResponse res) {
		String defaultPictureCd = req.getParameter("picture");
		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);
		MrCatchListSession session = (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);

		if (defaultPictureCd != null) {
			DBConnect dbConnect = new DBConnect();
			Connection connection = null;

			try {
				connection = dbConnect.getDBConnect();
				MrInfoManager manager = new MrInfoManager(connection);
				manager.setDefaultPictureCd(common, defaultPictureCd, session.getMrInfo().getMrId());
				session.setDBDefaultPictureCd(defaultPictureCd);

				session.setStatus(MrCatchListSession.SAVE_DONE);
			} catch (Exception ex) {
				throw new WmException(ex);
			} finally {
				dbConnect.closeDB(connection);
			}
		}
    }

    /**
     * 削除実行.選択した画像をすべて削除します.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void executeRemove(HttpServletRequest req, HttpServletResponse res) {
		String[] selections = req.getParameterValues("selection");
		MrCatchListSession session = (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);

		if (selections == null) {
			session.setStatus(MrCatchListSession.NO_SELECTION);
		} else {
			//基本画面に選択されていないかをチェック
			for (int i = 0; i < selections.length; i++) {
				if (selections[i].equals(session.getDBDefaultPictureCd())) {
					session.setStatus(MrCatchListSession.UNABLE_TO_REMOVE);
					return;
				}
			}

			DBConnect dbConnect = new DBConnect();
			Connection connection = null;

			try {
				connection = dbConnect.getDBConnect();
				MrCatchInfoManager manager = new MrCatchInfoManager(connection);
				manager.removeCatchPictures(selections);

				session.setStatus(MrCatchListSession.REMOVE_DONE);
				session.setCheckedList(null);
			} catch (Exception ex) {
				throw new WmException(ex);
			} finally {
				dbConnect.closeDB(connection);
			}
		}
    }

    /**
     * デフォルト画像コードの読み込み.
     * @param req  要求オブジェクト
     * @param mrId デフォルト画像コードの取得対象
     */
    private void setDefaultPictureInfo(HttpServletRequest req, String mrId) {
		MrCatchListSession mclSession = (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);
		DBConnect dbConnect = new DBConnect();
		Connection connection = null;

		try {
			connection = dbConnect.getDBConnect();
			MrInfoManager manager = new MrInfoManager(connection);
			MrInfo info = manager.getMrCatchInfo(mrId);
			mclSession.setMrInfo(info);
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}
    }
    
    /**
     * ソート情報の設定.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void setSortInfo(HttpServletRequest req, HttpServletResponse res) {
		String mrId = req.getParameter("mrId");
		String page = req.getParameter("page");
		String target = req.getParameter("target");
		MrCatchListSession session = (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);
	
		if (mrId != null) {
			session.getMrInfo().setMrId(mrId);
		}

		if (target != null) {
			session.setSortTarget(target);
		}

		if (page != null && page.equals("prev")) {
			session.previousPage();
		} else if (page != null && page.equals("next")) {
			session.nextPage();
		}
    }

}
