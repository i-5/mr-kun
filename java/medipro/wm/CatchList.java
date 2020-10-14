package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>��ЃL���b�`�摜�ꗗ�Ή�Servlet�N���X</strong>
 * <br>�\�[�g��������уy�[�W�ړ����������s��.
 * @author
 * @version
 */
public class CatchList extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**��ЃL���b�`�摜�ꗗJSP�t�@�C�����΃p�X*/
    private final String CATCH_LIST_PATH = "wm/WmCatchList/mainframe.jsp";
	
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String page;
//      private int current;
//      private int pageRow;
//      private Common common;
//      private CatchListSession cases;
	
    ////////////////////////////////////////////////////
    //class method
    //
	
    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    	doPost(request, response);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String page;
		int current;
		int pageRow;
		Common common;
		CatchListSession cases;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String catchKey = SysCnst.KEY_CATCH_SESSION;
				HttpSession session = request.getSession(true);
				// �R�����Z�b�V�����擾
				common = (Common) session.getValue(comKey);

				DispatManager disp = new DispatManager();
				// �����`�F�b�N
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// �E�F�u�}�X�^�ȊO�̏ꍇ
					disp.distAuthority(request, response);
				}
				else {
					// �L���b�`�摜�ꗗ�Z�b�V�����擾
					cases = (CatchListSession) session.getValue(catchKey);
					if (cases == null) {
						cases = new CatchListSession();
						session.putValue(catchKey, cases);
					}

					// ���b�Z�[�WID�Z�b�g
					cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					// �p�����[�^�擾
					page = request.getParameter("page");
					if (page == null ) {
						// �\�[�g������
						// �\�[�g�����t�]
						cases.setOrderReverse();
						// �擪�s���P�ɃZ�b�g
						cases.setCurrentRow(1);
					}
					else {
						// �y�[�W�ړ�����
						// ���y�[�W�̐擪�s���擾
						current = cases.getCurrentRow();
						// �P�y�[�W�̍s�����擾
						pageRow = common.getCatchLine();
						if (page.equals("next")) {
							// ���y�[�W������
							cases.setCurrentRow(current + pageRow);
						}
						else {
							// �O�y�[�W������
							if (current > 1) {
								cases.setCurrentRow(current - pageRow);
							}
						}
					}
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
