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
 * <strong>MR�Ǘ� - MR�̌�����ʑΉ�Servlet�N���X</strong>�B
 * @author  doppe
 * @version 1.00
 */
public class MrReferenceServlet extends HttpServlet {

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrReferenceServlet called!");
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

	//MR�ꗗ�p�Z�b�V�����I�u�W�F�N�g�̎擾
	//������ΐV���ɓo�^
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
