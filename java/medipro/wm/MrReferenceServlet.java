package medipro.wm;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.common.util.Converter;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrListSession;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;

/**
 * <strong>MR管理 - MRの検索画面対応Servletクラス</strong>。
 * @author  doppe
 * @version 1.00
 */
public class MrReferenceServlet extends HttpServlet {

    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrReferenceServlet called!");
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

	//MR一覧用セッションオブジェクトの取得
	//無ければ新たに登録
		MrListSession sess = 
			(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		if (sess == null) {
			sess = new MrListSession();
			req.getSession(true).putValue(SysCnst.KEY_MRLIST_SESSION, sess);
		}

		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";


		try {
			if (req.getParameter("submit") != null) {
				sess.setRefMrId(Converter.getParameter(req, "refMrId"));
				sess.setRefMrName(Converter.getParameter(req, "refMrName"));
			} else if (req.getParameter("back") != null) {
				sess.setRefMrId("");
				sess.setRefMrName("");
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
			} else {
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrReference/index.html";
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}

    }
}
