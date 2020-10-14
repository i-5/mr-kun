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
 * <strong>MR�ꗗController�N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrListController {
    /** �O�y�[�W�̑��݃t���O */
    private boolean prev = false;
    /** ���y�[�W�̑��݃t���O */
    private boolean next = false;

    /**
     * MR�ꗗ���擾����.
     * @param session �Z�b�V�������
     * @return MR��񃊃X�g
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
     * �O�y�[�W�����݂��邩�`�F�b�N����.
     * @return ���݂�����true
     */
    public boolean hasPrev() {
	return prev;
    }

    /**
     * ���y�[�W�����݂��邩�`�F�b�N����.
     * @return ���݂�����true
     */
    public boolean hasNext() {
	return next;
    }
				   
}
