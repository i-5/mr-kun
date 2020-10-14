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
 * <strong>MR�Ǘ� - MR�̈ꗗ��ʑΉ�Servlet�N���X</strong>�B
 * @author  doppe
 * @version 1.00
 */
public class MrListServlet extends HttpServlet {

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrListServlet called!");
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
		String action = req.getParameter("action");
		String newTarget = req.getParameter("sortTarget");
		String page = req.getParameter("page");
		String removeOk = req.getParameter("removeOk");
		String removeCancel = req.getParameter("removeCancel");
		String replaceOk = req.getParameter("replaceOk");

	//MR�ꗗ�p�Z�b�V�����I�u�W�F�N�g�̎擾
	//������ΐV���ɓo�^
		MrListSession mrListSession =
			(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		if (mrListSession == null) {
			mrListSession = new MrListSession();
			req.getSession(true).putValue(SysCnst.KEY_MRLIST_SESSION, mrListSession);
		}

		//status�𐳏��...
		mrListSession.setStatus(MrListSession.NORMAL);
	
		//�`�F�b�N�{�b�N�X�̑I����Ԃ�ۑ�
		String[] selections = req.getParameterValues("selection");
		MrListSession sess = (MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
		sess.setCheckedList(selections);

		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";

		try {
			if (removeOk != null) {
				//�폜���s
				executeRemove(req, res, selections);
			} else if (removeCancel != null) {
				//�폜���~
				mrListSession.setStatus(MrListSession.NORMAL);
			} else if (replaceOk != null) {
				//�S���ڋq�ύX
				if (setReplaceInfo(req, res, selections, true)) {
					sess.clear();
					nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";
				}
			} else if (action == null) {
				//�ꗗ�\��
				mrListSession.setRefMrId("");
				mrListSession.setRefMrName("");
				setSortInfo(req, res, newTarget, page);
			} else if (action.equals("remove")) {
				//�폜�m�F
				setRemoveInfo(req, res, selections);
			} else if (action.equals("add")) {
				//�ǉ����
				setAddInfo(req, res);
				sess.clear();
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrUpdate/index.html";
			} else if (action.equals("update")) {
				//�ύX���
				sess.clear();
				String mrId = req.getParameter("mrId");
				nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrUpdateServlet?action=update&mrId=" + mrId;
			} else if (action.equals("replace")) {
				//�S���ڋq�ύX
				if (setReplaceInfo(req, res, selections, false)) {
					sess.clear();
					nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";
				}
			} else {
				//�ꗗ�\��
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
     * MR�Ǘ� - �S���ڋq�ύX��ʈړ��̃`�F�b�N or �O�����B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private boolean setReplaceInfo(HttpServletRequest req,
								   HttpServletResponse res,
								   String[] selections,
								   boolean moveFlag) throws IOException {
		//�܂��͒S���ڋq�ύX��ʗp�Z�b�V�����I�u�W�F�N�g�̐����Ɠo�^
		MrClientSession mcSession = new MrClientSession();
		req.getSession(true).putValue(SysCnst.KEY_MRCLIENT_SESSION, mcSession);

		if (selections != null) {
			if (selections.length > 0) {
				//1�����`�F�b�N
				mcSession.setLeftMrId(selections[0]);
			}
			if (selections.length > 1) {
				//2�`�F�b�N
				mcSession.setRightMrId(selections[1]);
			}
		}

		if (selections == null || selections.length < 3 || moveFlag) {
			//�`�F�b�N��2�ȉ��������炻�̂܂܉�ʑJ��
			return true;
		} else {
			//�`�F�b�N��3�ȏゾ������x�����o��
			MrListSession mlSession =
				(MrListSession)req.getSession(true).getValue(SysCnst.KEY_MRLIST_SESSION);
			mlSession.setStatus(MrListSession.REPLACE_CONFIRM);
			return false;
		}
    }

    /**
     * MR�Ǘ� - MR�̒ǉ��E�ύX��ʂւ̈ړ��B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void setAddInfo(HttpServletRequest req,
							HttpServletResponse res) throws IOException {
		req.getSession(true).removeValue(SysCnst.KEY_MRUPDATE_SESSION);
    }

    /**
     * MR�Ǘ� - �ꗗ�\���\�[�g�����̐ݒ�B
     * @param 
     */
    private void setSortInfo(HttpServletRequest req,
							 HttpServletResponse res,
							 String newTarget,
							 String page) throws IOException {
		//�Z�b�V�������̎擾
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
     * �폜���̃Z�b�g�B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
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
     * �S�������t�����݂��邩��������.
     * @param mrId �����Ώۂ�MR-ID
     * @return ��l�ł����݂�����true
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
     * �폜���s�B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
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
