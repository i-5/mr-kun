package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.server.session.MrClientSession;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.manager.SentakuTorokuInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MessageManager;

/**
 * <strong>MR�Ǘ� - �S���ڋq�ύX��ʑΉ��T�[�u���b�g�N���X</strong>�B
 * @author
 * @version
 */
public class MrClientServlet extends HttpServlet {

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        if (SysCnst.DEBUG) {
            log("MrClientServlet called!");
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
        String back = req.getParameter("back");                        //�߂�{�^��
        String save = req.getParameter("save");                        //�ۑ��{�^��
        String leftInput = req.getParameter("leftInput");        //���̓{�^��(��)
        String rightInput = req.getParameter("rightInput");        //���̓{�^��(�E)
        String right = req.getParameter("right");                //�ړ��{�^����
        String left = req.getParameter("left");                        //�ړ��{�^����
        String saveOk = req.getParameter("saveOk");                //�ۑ����s
        String saveCancel = req.getParameter("saveCancel");        //�ۑ����~

        //MR�S���ڋq�p�Z�b�V�����I�u�W�F�N�g�̎擾 or �o�^
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        if (session == null) {
            session = new MrClientSession();
            req.getSession(true).putValue(SysCnst.KEY_MRCLIENT_SESSION, session);
        }

        //status�𐳏��...
        session.setStatus(MrClientSession.NORMAL);

        String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";

        try {
            if (back != null) {
                //�ꗗ�ɖ߂�
                nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrListServlet";
            } else if (save != null) {
                //�ۑ��m�F
                if (!session.getLeftMrId().equals("") && !session.getRightMrId().equals("")) {
                    session.setStatus(MrClientSession.SAVE_CONFIRM);
                }
            } else if (leftInput != null) {
                //����MRID����
                setLeftMrInfo(req, res);
            } else if (rightInput != null) {
                //�E��MRID����
                setRightMrInfo(req, res);
            } else if (right != null) {
                //�� �� �E��DR���ړ�
                moveDrToRight(req, res);
            } else if (left != null) {
                //�E �� ����DR���ړ�
                moveDrToLeft(req, res);
            } else if (saveOk != null) {
                //�ۑ����s
                executeSave(req, res);
            } else if (saveCancel != null) {
                //�ۑ����~
            }

            res.sendRedirect(nextPage);
        } catch (Exception ex) {
			log("", ex);
            new DispatManager().distribute(req, res, ex);
        }
    }

    /**
     * �����̏����X�V���܂�.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void setLeftMrInfo(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String mrId = req.getParameter("leftMrId");

        if (mrId.equals("") || !mrId.equals(session.getRightMrId())) {
            //�󔒂��E��MR-ID�ƈقȂ�Βʏ�ǂ���Z�b�g
            session.setLeftMrId(mrId);
        } else {
            //����MR-ID���Z�b�g�����Ƃ��̓G���[
            session.setLeftMrId("");
            session.setStatus(MrClientSession.SAME_MRID_ERROR);
        }

        session.setRightMrId(session.getRightMrId());
    }

    /**
     * �E���̏����X�V���܂��B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void setRightMrInfo(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String mrId = req.getParameter("rightMrId");

        if (mrId.equals("") || !mrId.equals(session.getLeftMrId())) {
            //�󔒂��E��MR-ID�ƈقȂ�Βʏ�ǂ���Z�b�g
            session.setRightMrId(mrId);
        } else {
            //����MR-ID���Z�b�g�����Ƃ��̓G���[
            session.setRightMrId("");
            session.setStatus(MrClientSession.SAME_MRID_ERROR);
        }
        
        session.setLeftMrId(session.getLeftMrId());
    }

    /**
     * �w�肵��������DR���E���Ɉڂ��܂��B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void moveDrToRight(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String drId = req.getParameter("leftSelection");

        if (drId == null) {
        } else {
            session.moveToRight(drId);
        }
    }

    /**
     * �w�肵���E����DR�������Ɉڂ��܂��B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void moveDrToLeft(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String drId = req.getParameter("rightSelection");

        if (drId == null) {
        } else {
            session.moveToLeft(drId);
        }
    }

    /**
     * �X�V���ʂ�DB�ɔ��f���܂��B
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void executeSave(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session
	    = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        DBConnect dbConnect = new DBConnect();
        Connection connection = dbConnect.getDBConnect();
        
        try {
            connection.setAutoCommit(false);

            SentakuTorokuInfoManager manager = new SentakuTorokuInfoManager(connection);
	    MessageManager messageManager = new MessageManager(connection);

	    //������DR�̓��ڍs�������̂�ۑ�
            Enumeration r2l = session.getLeftMr().getMovedDrList();
            while (r2l.hasMoreElements()) {
                MrClientSession.Dr dr = (MrClientSession.Dr)r2l.nextElement();
                manager.moveSentakuToroku(dr.getDrId(),
					  session.getRightMrId(),
					  session.getLeftMrId(),
					  dr.getSeq());
		messageManager.copyDrMessage(dr.getDrId(),
					     session.getRightMrId(),
					     session.getLeftMrId());
		messageManager.copyMrMessage(dr.getDrId(),
					     session.getRightMrId(),
					     session.getLeftMrId());
            }

	    //�E����DR�̓��ڍs�������̂�ۑ�
            Enumeration l2r = session.getRightMr().getMovedDrList();
            while (l2r.hasMoreElements()) {
                MrClientSession.Dr dr = (MrClientSession.Dr)l2r.nextElement();
                manager.moveSentakuToroku(dr.getDrId(),
					  session.getLeftMrId(),
					  session.getRightMrId(),
					  dr.getSeq());
		messageManager.copyDrMessage(dr.getDrId(),
					     session.getLeftMrId(),
					     session.getRightMrId());
		messageManager.copyMrMessage(dr.getDrId(),
					     session.getLeftMrId(),
					     session.getRightMrId());
            }

            session.setStatus(MrClientSession.SAVE_DONE);
	    //�����[�h�ׂ̈ɍĐݒ�
	    session.setLeftMrId(session.getLeftMrId());
	    session.setRightMrId(session.getRightMrId());

	    connection.commit();
        } catch (Exception ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) {
	    }
            throw new WmException(ex);
        } finally {
	    try {
		connection.setAutoCommit(true);
	    } catch (SQLException e) {
	    }
            dbConnect.closeDB(connection);
        }
    }

}
