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
 * <br>�x�X�E�c�Ə��̒ǉ��E�ύX��ʂ��Ăяo��.
 * @author
 * @version
 */
public class BranchUpdate extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**�x�X�E�c�Ə��ǉ��E�ύXhtml�t�@�C�����΃p�X*/
    private static final String BRANCH_UPDATE_PATH = "wm/WmBranchUpdate/index.html";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private String shitenCD;
//      private String eigyosyoCD;
//      private BranchUpdateSession buses;
//      private BranchListSession brses;
    
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
		String shitenCD;
		String eigyosyoCD;
		BranchUpdateSession buses;
		BranchListSession brses;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				// �Z�b�V�����̎擾
				HttpSession session = request.getSession(true);
				String brUpdKey = SysCnst.KEY_BRANCHUPDATE_SESSION;
				buses = (BranchUpdateSession) session.getValue(brUpdKey);
				if ( buses == null ) {
					buses = new BranchUpdateSession();
					session.putValue(brUpdKey, buses);
				}
				brses = (BranchListSession) session.getValue
					(SysCnst.KEY_BRANCHLIST_SESSION);
				if (brses != null) {
					brses.crearCheckValue();
					brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
				}
				// ���b�Z�[�WID�̃Z�b�g
				buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
				// �p�����[�^�擾
				shitenCD = request.getParameter("shitenCD");
				eigyosyoCD = request.getParameter("eigyosyoCD");
				if ( shitenCD != null ) {
					buses.setBranchCD(shitenCD);
					buses.setNewBranch(false);
				} else {
					buses.setBranchCD(null);
					buses.setBranchName(null);
					buses.setNewBranch(true);
				}
				if (eigyosyoCD != null ) {
					buses.setOfficeCD(eigyosyoCD);
					buses.setNewOffice(false);
				} else {
					buses.setOfficeCD(null);
					buses.setNewOffice(true);
					buses.setOfficeName(null);
				}
			}
			// Go to the next page
			response.sendRedirect
				(SysCnst.HTML_ENTRY_POINT + BRANCH_UPDATE_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
