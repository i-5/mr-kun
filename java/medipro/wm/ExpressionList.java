package medipro.wm;

import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.mr.common.exception.DispatManager;
import jp.ne.sonet.medipro.mr.common.exception.MrException;
import jp.ne.sonet.medipro.mr.common.servlet.SessionManager;
import jp.ne.sonet.medipro.mr.common.util.DBConnect;
import jp.ne.sonet.medipro.mr.common.util.HtmlTagUtil;
import jp.ne.sonet.medipro.mr.server.entity.ExpressionLibInfo;
import jp.ne.sonet.medipro.mr.server.manager.ExpressionLibInfoManager;

/**
 * <strong>��^�����C�u�����Q�ƃT�[�u���b�g�N���X</strong>
 * @author
 * @version
 */
public class ExpressionList extends HttpServlet {

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		/* <hb010801
                 * don't wanna get into the rewriting of SysCnst 
                 * so will skip this for WM
                 SessionManager sm = new SessionManager();
		if (!sm.check(req, 1)) {
			DispatManager dm = new DispatManager();
			dm.distSession(req, res);
			return;
		}
                hb010801>
                 */
		HttpSession session = req.getSession(true);

		String action = req.getParameter("action");
		String currentPage = req.getParameter("currentPage");
		String expressionCd = req.getParameter("expressionCd");
		String nextPage = "/medipro/wm/WmDaikouHaishin/MessageBuild6/mainframe.jsp";

		try {
			if (expressionCd != null) {
				//mainframe(�E��)�ɖ{����\��
				session.putValue("expressionCd", expressionCd);
			} else if (action != null) {
				if (action.equals("back")) {
					//�Ăяo����ʂɖ߂�
					String selection = req.getParameter("selection");
					session.removeValue("expressionCd");
					session.removeValue("currentPage");
					session.removeValue("message");

					if (selection != null) {
						String text = getHonbun(selection);
			
						if (text != null) {
							session.putValue("message", HtmlTagUtil.convertLFtoCR(text));
						}
					}
					nextPage = "/medipro/wm/WmDaikouHaishin/MessageBuild2/index.html";
				} else {
					//�O�y�[�W�A���y�[�W
					if (action.equals("prev")) {
						currentPage = new Integer(new Integer(currentPage).intValue() - 1).toString();
					} else {
						currentPage = new Integer(new Integer(currentPage).intValue() + 1).toString();
					}
					session.removeValue("expressionCd");
					session.putValue("currentPage", currentPage);
					nextPage = "/medipro/wm/WmDaikouHaishin/MessageBuild6/index.html";
				}
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}
    }

    /**
     * ���W�I�{�^���őI�����ꂽ��^����DB����擾����.
     * @param expressionCd ��^���R�[�h
     * @return ��^��
     */
    private String getHonbun(String expressionCd) {
		ExpressionLibInfo info = null;
		DBConnect dbconn = new DBConnect();
		Connection connection = null;

		try {
			connection = dbconn.getDBConnect();
			ExpressionLibInfoManager manager = new ExpressionLibInfoManager(connection);
			info = manager.getExpressionLibInfo(expressionCd);
		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbconn.closeDB(connection);
		}

		if (info == null || info.getHonbun() == null) {
			return "";
		}

		return info.getHonbun();
    }
    
}
