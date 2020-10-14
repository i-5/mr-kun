package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;


/**
 * <strong>�x�X�E�c�Ə��ꗗ�Ή�Servlet�N���X</strong>
 * <br>�x�X�E�c�Ə��̍폜���s��.
 * @author
 * @version
 */
public class BranchDelete extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**�폜�Ώۂ𖢑I��*/
    private static final int RTN_NO_CHECK = -2;
    /**�폜�x�X�E�c�Ə���MR������*/
    private static final int RTN_EXIST_MR = -1;
    /**�폜�\*/
    private static final int RTN_DELETE_OK = 1;
    /**�x�X�E�c�Ə��ꗗJSP�t�@�C�����΃p�X*/
    private static final String BRANCH_LIST_PATH = "wm/WmBranchList/mainframe.jsp";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private BranchListSession brses;
//      private String mode;
//      private String checkList[];
//      private boolean existMr;
	
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
		boolean existMr = false;
		String mode = null;
		String checkList[] = null;
		BranchListSession brses = null;
		
		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				// �Z�b�V�����̎擾
				HttpSession session = request.getSession(true);
				String branchKey = SysCnst.KEY_BRANCHLIST_SESSION;
				// ��ЃL���b�`�摜�Z�b�V�������擾
				brses = (BranchListSession) session.getValue(branchKey);
			
				// �p�����[�^�擾
				mode = request.getParameter("mode");
			
				// �폜�x�X�R�[�h�N���A
				brses.crearDeleteBranch();
				// �폜�c�Ə��R�[�h�N���A
				brses.crearDeleteOffice();
				int rtn;
				// ��ʑ���̏󋵂ɂ�菈������
				if (mode.equals("deletemessage")) {
					// ��x�X�E�c�Ə����폜���飉�����
					// �`�F�b�N�{�b�N�X�I����Ԏ擾
					getCheck(request, existMr, checkList, brses);
					// �`�F�b�N���`�F�b�N
					rtn = checkParameter(request, existMr, checkList);
					if (rtn == RTN_NO_CHECK) {
						// �`�F�b�N����Ă��Ȃ��ꍇ
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NOCHECK);
					} else {
						// �`�F�b�N����Ă���ꍇ
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_CONFIRM);
					}
				} else if (mode.equals("nodelete")) {
					// ��폜���܂�����Ţ�������v������
					// �`�F�b�N�{�b�N�X�I����Ԏ擾
					getCheck(request, existMr, checkList, brses);
					// ���b�Z�[�WID�Z�b�g
					brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
				} else if (mode.equals("delete")) {
					// ��폜���܂�����Ţ�͂��������
					// �`�F�b�N�{�b�N�X�I����Ԏ擾
					getCheck(request, existMr, checkList, brses);
					// �`�F�b�N������тl�q�����`�F�b�N
					rtn = checkParameter(request, existMr, checkList);
					if (rtn == RTN_EXIST_MR) {
						// �l�q���������Ă����ꍇ
						// ���b�Z�[�WID�Z�b�g
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_CANNOT);
					} else if (rtn == RTN_NO_CHECK) {
						// �`�F�b�N����Ă��Ȃ��ꍇ
						// ���b�Z�[�WID�Z�b�g
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NOCHECK);
					} else {
						// �`�F�b�N�n�j
						BranchListController bctrl
							= new BranchListController(request, response);
						// �x�X�E�c�Ə��폜�����R�[��
						bctrl.deleteBranch(session);
						// ���b�Z�[�WID�Z�b�g
						brses.setMessageState(SysCnst.BRANCH_LIST_MSG_DONE);
					}
				} else {
					// �`�F�b�N�{�b�N�X�I����Ԏ擾
					getCheck(request, existMr, checkList, brses);
					// ���b�Z�[�WID�Z�b�g
					brses.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
				}
			}
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + BRANCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
	
    /**
     * checkParameter.
     * <dl>�폜�Ώۂ̃`�F�b�N���s��</dl>
     * @return int
     * @param request HttpServletRequest
     */
    public int checkParameter(HttpServletRequest request,
							  boolean existMr,
							  String[] checkList) {
		int rtn = RTN_DELETE_OK;
		
		if (existMr == true) {
			// MR���������Ă����ꍇ
			rtn = RTN_EXIST_MR;
		}
		if (checkList == null) {
			// �폜�Ώۖ��I���̏ꍇ
			rtn = RTN_NO_CHECK;
		}
		if (SysCnst.DEBUG) {
			log("return:"+rtn);
		}
		return rtn;
    }

    /**
     * getCheck.
     * <dl>�I������Ă���`�F�b�N�{�b�N�X�̍��ڂ��擾����</dl>
     * @param request HttpServletRequest
     */
    public void getCheck(HttpServletRequest request,
						 boolean existMr,
						 String[] checkList,
						 BranchListSession brses) {
		try {
			// MR�t���O�N���A
			existMr = false;
			checkList = null;
			// �`�F�b�N�{�b�N�X�Z�b�V�������N���A
			brses.crearCheckValue();
		  	
			Enumeration e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
		  	    
				if (key.equals("checkbox")) {
					checkList = request.getParameterValues(key);
					for (int i = 0; i < checkList.length; i++) {
						StringTokenizer stkn = 
							new StringTokenizer(checkList[i], ",");
						// �c�Ə��R�[�h�擾
						String officeCD = stkn.nextToken();
						// �x�X�R�[�h�擾
						String branchCD = stkn.nextToken();
						// MR�t���O�擾
						String mrFlg = stkn.nextToken();
						if (mrFlg.equals("true")) {
							// MR���������Ă����ꍇ
							existMr = true;
						}
						// �`�F�b�N�{�b�N�X�Z�b�V�����Ɏx�X�R�[�h���Z�b�g
						brses.setCheckValue(branchCD + "," + officeCD);
						if (officeCD.equals("nooffice")) {
							// �c�Ə����Ȃ��ꍇ
							// �폜�x�X�Z�b�V�����Ɏx�X�R�[�h���Z�b�g
							brses.setDeleteBranch(branchCD);
						} else {
							// �c�Ə�������ꍇ
							// �폜�c�Ə��Z�b�V�����ɉc�Ə��R�[�h���Z�b�g
							brses.setDeleteOffice(officeCD);
						}
					}
				}
			}
		} catch ( Exception e ) {
			throw new WmException(e);
		}
    }
}
