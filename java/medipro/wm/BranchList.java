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
 * <strong>�x�X�E�c�Ə��ꗗ�Ή�Servlet�N���X</strong>
 * <br>�\�[�g��������уy�[�W�ړ����������s��.
 * @author
 * @version
 */
public class BranchList extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**�x�X�E�c�Ə��ꗗJSP�t�@�C�����΃p�X*/
    private final String BRANCH_LIST_PATH = "wm/WmBranchList/mainframe.jsp";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private Common common;
//      private BranchListSession brses;
//      private String sortKey;
//      private String page;
//      private int current;
//      private int pageRow;
	
    ////////////////////////////////////////////////////
    //class method
    //

    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)	{
    	doPost(request, response);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		Common common;
		BranchListSession brses;
		String sortKey;
		String page;
		int current;
		int pageRow;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String branchKey = SysCnst.KEY_BRANCHLIST_SESSION;
				// �Z�b�V�����̎擾
				HttpSession session = request.getSession(true);
				common = (Common) session.getValue(comKey);
				brses = (BranchListSession) session.getValue(branchKey);
				if (brses == null) {
					brses = new BranchListSession();
					session.putValue(branchKey, brses);
				}
				// �`�F�b�N��ԃN���A
				brses.crearCheckValue();
				// ���b�Z�[�W�h�c�Z�b�g
				brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
			
				// �p�����[�^�擾
				page = request.getParameter("page");

				if (page == null) {
					// �\�[�g���؂�ւ���
					sortKey = request.getParameter("sortKey");

					if (sortKey != null) {
						// �\�[�g�L�[�̃Z�b�g
						brses.setSortKey(sortKey);
						// �\�[�g���t�]
						brses.reverseOrder();
						// �擪�s���P�ɃZ�b�g
						brses.setCurrentRow(1);
					}
				} else {
					// �y�[�W�؂�ւ���
					current = brses.getCurrentRow();
					pageRow = common.getShitenLine();
					if (page.equals("next")) {
						// ���y�[�W����
						brses.setCurrentRow(current + pageRow);
					} else {
						// �O�y�[�W����
						if (current > 1) {
							brses.setCurrentRow(current - pageRow);
						}
					}
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + BRANCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
