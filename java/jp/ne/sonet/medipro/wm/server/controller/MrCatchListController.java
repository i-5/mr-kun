package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.manager.MrCatchInfoManager;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrCatchListSession;

/**
 * <strong>MRキャッチ画像一覧Controllerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchListController {
    /** 前ページの存在フラグ */
    private boolean prev = false;
    /** 次ページの存在フラグ */
    private boolean next = false;

    /**
     * MRキャッチ画像一覧を取得する.
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     * @return MRキャッチ画像一覧
     */
    public Vector getMrCatchList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	MrCatchListSession mclSession = (MrCatchListSession)session.getValue(SysCnst.KEY_MRCATCHLIST_SESSION);

	if (mclSession == null) {
	    mclSession = new MrCatchListSession();
	    session.putValue(SysCnst.KEY_MRCATCHLIST_SESSION, mclSession);
	}

	DBConnect dbConnect = new DBConnect();
	Connection connection = null;
	Vector list = new Vector();

	try {
	    connection = dbConnect.getDBConnect();
	    MrCatchInfoManager manager = new MrCatchInfoManager(connection);
	    list = manager.getMrCatchList(mclSession.getMrInfo().getMrId(),
					  mclSession.getCurrentSortDirection());
	} catch (Exception ex) {
	    if (SysCnst.DEBUG) {
		ex.printStackTrace();
	    }
	    new DispatManager().distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	int startPoint = mclSession.getPage() * common.getMrCatchLine();
	Vector focusList = new Vector();

	for (int i = startPoint; i < startPoint + common.getMrCatchLine(); i++) {
	    if (i < list.size()) {
		focusList.addElement(list.elementAt(i));
	    }
	}

	if (startPoint > 0) {
	    prev = true;
	} else {
	    prev = false;
	}

	if (list.size() > startPoint + common.getMrCatchLine()) {
	    next = true;
	} else {
	    next = false;
	}

	return focusList;
    }

    /**
     * 前ページが存在するかチェックする.
     * @return 存在したらtrue
     */
    public boolean hasPrev() {
	return prev;
    }

    /**
     * 次ページが存在するかチェックする.
     * @return 存在したらtrue
     */
    public boolean hasNext() {
	return next;
    }

}
