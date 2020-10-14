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
 * <strong>MR�L���b�`�摜�ꗗ�Ή��T�[�u���b�g�N���X</strong>.
 * @author  doppe
 * @version 1.00
 */
public class MrCatchListServlet extends HttpServlet {
    
    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrCatchListServlet is called");
		}

		//�Z�b�V�����`�F�b�N
		if (! new SessionManager().check(req)) {
			new DispatManager().distSession(req, res);
			return;
		}

		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

	//�����`�F�b�N
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
			!common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
			new DispatManager().distAuthority(req, res);
			return;
		}

		//��ʃp�����[�^�̎擾
		String mrId = req.getParameter("mrId");
		String page = req.getParameter("page");
		String target = req.getParameter("target");
		String action = req.getParameter("action");
		String removeOk = req.getParameter("removeOk");
		String removeCancel = req.getParameter("removeCancel");
		String save = req.getParameter("save");
		String saveOk = req.getParameter("saveOk");
		String saveCancel = req.getParameter("saveCancel");

		//�Z�b�V������������΍쐬���o�^
		MrCatchListSession session
			= (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);
		if (session == null) {
			session = new MrCatchListSession();
			req.getSession(true).putValue(SysCnst.KEY_MRCATCHLIST_SESSION, session);
		}

		//�`�F�b�N�{�b�N�X�̏�Ԃ�ێ�
		String[] selections = req.getParameterValues("selection");
		session.setCheckedList(selections);

	//status���N���A
		session.setStatus(MrCatchListSession.NORMAL);

		String defaultPictureCd = req.getParameter("picture");
		if (defaultPictureCd != null) {
			session.getMrInfo().setPictureCd(defaultPictureCd);
		}

		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngCatchList/index.html";

		try {
			session.getMrInfo().setMrId(mrId);

			if (action != null && action.equals("remove")) {
				//�폜�m�F
				session.setStatus(MrCatchListSession.REMOVE_CONFIRM);
			} else if (action != null && action.equals("add")) {
				//�ǉ���ʂ�
				session.clear();
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchUpdateServlet?mrId="
					+ session.getMrInfo().getMrId();
			} else if (action != null && action.equals("update")) {
				//�ύX��ʂ�
				session.clear();
				String pictureCd = req.getParameter("pictureCd");
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchUpdateServlet?mrId="
					+ session.getMrInfo().getMrId() + "&pictureCd=" + pictureCd;
			} else if (page != null || target != null) {
				//�y�[�W�A�\�[�g�ɑΉ�
				setSortInfo(req, res);
			} else if (removeOk != null) {
				//�폜���s
				executeRemove(req, res);
			} else if (removeCancel != null|| saveCancel != null) {
				//�폜���~
			} else if (save != null) {
				//�ۑ��m�F
				//  		session.setStatus(MrCatchListSession.SAVE_CONFIRM);
				executeSave(req, res);
				//  	    } else if (saveOk != null) {
				//  		//�ۑ����s
				//  		executeSave(req, res);
			} else if (mrId != null) {
				//�ǉ��E�ύX��ʂ���
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
     * �X�V���s.�I�������摜��MR�̃f�t�H���g�摜�ɐݒ肵�܂�.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
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
     * �폜���s.�I�������摜�����ׂč폜���܂�.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void executeRemove(HttpServletRequest req, HttpServletResponse res) {
		String[] selections = req.getParameterValues("selection");
		MrCatchListSession session = (MrCatchListSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHLIST_SESSION);

		if (selections == null) {
			session.setStatus(MrCatchListSession.NO_SELECTION);
		} else {
			//��{��ʂɑI������Ă��Ȃ������`�F�b�N
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
     * �f�t�H���g�摜�R�[�h�̓ǂݍ���.
     * @param req  �v���I�u�W�F�N�g
     * @param mrId �f�t�H���g�摜�R�[�h�̎擾�Ώ�
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
     * �\�[�g���̐ݒ�.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
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
