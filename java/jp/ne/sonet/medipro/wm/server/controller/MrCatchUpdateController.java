package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.CatchInfo;
import jp.ne.sonet.medipro.wm.server.manager.MrCatchInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrCatchUpdateSession;

/**
 * <strong>MRキャッチ画像追加・更新Controllerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateController {

    /**
     * MRキャッチ画像情報を取得する.
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     * @return MRキャッチ画像一覧
     */
    public MrCatchUpdateSession getMrCatchUpdateSession(HttpServletRequest req,
							HttpServletResponse res,
							HttpSession session) {
	MrCatchUpdateSession mcuSession
	    = (MrCatchUpdateSession)session.getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);

	if (mcuSession == null) {
	    mcuSession = new MrCatchUpdateSession();
	    session.putValue(SysCnst.KEY_MRCATCHUPDATE_SESSION, mcuSession);
	}

	//変更時
	if (mcuSession.isNeedToLoad()) {
	    CatchInfo info = new CatchInfo();
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    //変更時
	    try {
		dbConnect = new DBConnect();
		connection = dbConnect.getDBConnect();

		MrCatchInfoManager manager = new MrCatchInfoManager(connection);
		info = manager.getMrCatchInfo(mcuSession.getPictureCd());

	    } catch (Exception ex) {
		if (SysCnst.DEBUG) {
		    ex.printStackTrace();
		}
		new DispatManager().distribute(req, res);
	    } finally {
		dbConnect.closeDB(connection);
	    }

	    mcuSession.setCatchInfo(info);
	    mcuSession.setLoadFlag(false);
	}

	return mcuSession;
    }
}
