package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrListSession;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>MR一覧Controllerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrListController {
    /** 前ページの存在フラグ */
    private boolean prev = false;
    /** 次ページの存在フラグ */
    private boolean next = false;

    /**
     * MR一覧を取得する.
     * @param session セッション情報
     * @return MR情報リスト
     */
    public Vector getMrInfoList(HttpServletRequest req,
				HttpServletResponse res,
				HttpSession session) {
	Vector elements = new Vector();

	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	MrListSession mrListSession
	    = (MrListSession)session.getValue(SysCnst.KEY_MRLIST_SESSION);

	if (mrListSession == null) {
	    mrListSession = new MrListSession();
	    session.putValue(SysCnst.KEY_MRLIST_SESSION, mrListSession);
	}

	
	DBConnect dbConnect = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbConnect.getDBConnect();
	    MrInfoManager manager = new MrInfoManager(connection);
	    elements = manager.getMrList(common,
					 mrListSession.getCurrentSortItem(),
					 mrListSession.getCurrentSortDirection(),
					 mrListSession.getRefMrId(),
					 mrListSession.getRefMrName());

	} catch (Exception ex) {
//  	    if (SysCnst.DEBUG) {
		ex.printStackTrace();
//  	    }
	    new DispatManager().distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	int startPoint = mrListSession.getPage() * common.getMrLine();
	Vector focusList = new Vector();

	for (int i = startPoint; i < startPoint + common.getMrLine(); i++) {
	    if (i < elements.size()) {
		focusList.addElement(elements.elementAt(i));
	    }
	}

	if (startPoint > 0) {
	    prev = true;
	} else {
	    prev = false;
	}

	if (elements.size() > startPoint + common.getMrLine()) {
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
