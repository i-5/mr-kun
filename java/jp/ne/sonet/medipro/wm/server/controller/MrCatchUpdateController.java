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
 * <strong>MR�L���b�`�摜�ǉ��E�X�VController�N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateController {

    /**
     * MR�L���b�`�摜�����擾����.
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @return MR�L���b�`�摜�ꗗ
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

	//�ύX��
	if (mcuSession.isNeedToLoad()) {
	    CatchInfo info = new CatchInfo();
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    //�ύX��
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
