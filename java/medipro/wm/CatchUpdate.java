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
 * <strong>��ЃL���b�`�摜�ꗗ�Ή�Servlet�N���X</strong>
 * <br>��{��ʂ̕ύX����уL���b�`�摜�̍폜���s��.
 * @author
 * @version
 */
public class CatchUpdate extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**�폜�Ώۂ𖢑I��*/
    private static final int RTN_NO_CHECK = -2;
    /**��{��ʂ��폜�Ώۂɐݒ�*/
    private static final int RTN_ON_DEFAULT = -1;
    /**�폜�\*/
    private static final int RTN_DELETE_OK = 1;
    /**��ЃL���b�`�摜�ꗗJSP�t�@�C�����΃p�X*/
    private static final String CATCH_LIST_PATH = "wm/WmCatchList/mainframe.jsp";
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String mode;
//      private String radio;
//      private String checkList[];
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
		String mode;
		String radio;
		String checkList[] = null;
		CatchListSession cases;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else {
				HttpSession session = request.getSession(true);
				String catchKey = SysCnst.KEY_CATCH_SESSION;
				// ��ЃL���b�`�摜�Z�b�V�������擾
				cases = (CatchListSession) session.getValue(catchKey);
				if (cases == null) {
					cases = new CatchListSession();
					session.putValue(catchKey, cases);
				}

				// �R�����Z�b�V�����̎擾
				Common common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);
				
				DispatManager dm = new DispatManager();
				// �����`�F�b�N
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// �E�F�u�}�X�^�ȊO�̏ꍇ
					dm.distAuthority(request, response);
				} else {
					// �p�����[�^�擾
					mode = request.getParameter("mode");
					radio = request.getParameter("radiobutton");
					
					int rtn;
					// ��ʑ���̏󋵂ɂ�菈������
					if (mode.equals("updatemessage")) {
						// ��ۑ���{�^������������
						// ���b�Z�[�WID���Z�b�g
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_UPDATE);
						// ���W�I�{�^����ԏ����Z�b�g
						cases.setRadioValue(radio);
					} else if (mode.equals("update")) {
						// ��ۑ����܂�������b�Z�[�W�t�H�[�����碂͂������������
						// ���W�I�{�^����ԏ����Z�b�g
						cases.setRadioValue(radio);
						// �X�V�����R�[��
						callUpdateCatch(session, request, response, cases);
						// ���b�Z�[�WID�Z�b�g
						cases.setMessageState(SysCnst.CATCH_LSIT_MSG_SAVEDONE);
					} else if (mode.equals("deletemessage")) {
						// ��I�������摜���폜���飉�����
						// �폜���e�`�F�b�N
						rtn = checkDelete(request, cases, checkList);
						switch (rtn) {
						case RTN_NO_CHECK: // �폜�摜���I���̏ꍇ
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_NOCHECK);
							break;
						case RTN_ON_DEFAULT: // �f�t�H���g�摜�ɐݒ肳��Ă����ꍇ
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DEFAULT);
							break;
						case  RTN_DELETE_OK: // �폜�\
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DELETE);
							break;
						}
					} else if (mode.equals("delete")) {
						// ��폜���܂�����Ţ�͂��������
						// �폜���e�`�F�b�N
						rtn = checkDelete(request, cases, checkList);
						switch ( rtn ) {
						case RTN_NO_CHECK: // �폜�摜���I���̏ꍇ
							// ���b�Z�[�WID�Z�b�g
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_NOCHECK);
							break;
						case RTN_ON_DEFAULT: // �f�t�H���g�摜�ɐݒ肳��Ă����ꍇ
							// ���b�Z�[�WID�Z�b�g
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DEFAULT);
							break;
						case  RTN_DELETE_OK: // �폜�\
							// �폜�����R�[��
							callDeleteCatch(session, request, response);
							// ���b�Z�[�WID�Z�b�g
							cases.setMessageState(SysCnst.CATCH_LIST_MSG_DELDONE);
							break;
						}
					} else if (mode.equals("nodelete")) {
						// ��폜���܂�����Ţ�������v������
						rtn = checkDelete(request, cases, checkList);
						// ���b�Z�[�WID�Z�b�g
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else if (mode.equals("defaultok")) {
						// ��f�t�H���g�摜��...��ŢOK�������
						rtn = checkDelete(request, cases, checkList);
						// ���b�Z�[�WID�Z�b�g
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else if (mode.equals("nocheckok")) {
						// ��폜����摜��...��ŢOK�������
						rtn = checkDelete(request, cases, checkList);
						// ���b�Z�[�WID�Z�b�g
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					} else {
						// ���W�I�{�^����ԏ����N���A
						cases.crearRadioValue();
						// ���b�Z�[�WID�Z�b�g
						cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
					}
				}
			}
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_LIST_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    /**
     * callUpdateCatch.
     * <dl>��{��ʍX�V�������R�[������</dl>
     * @param session HttpSession
     */
    public void callUpdateCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response,
								CatchListSession cases) {
		String pictureCD;

		if (cases.getRadioValue() != null) {
			// �I�������摜�R�[�h���擾
			pictureCD = cases.getRadioValue();

			CatchListController cctrl = 
				new CatchListController(request, response);
			// ��{�摜�X�V�����R�[��
			cctrl.updatePicture(session, pictureCD);
		}
		// ���W�I�{�^����ԏ����N���A
		cases.crearRadioValue();
    }
	
    /**
     * checkDelete.
     * <dl>�폜�Ώۂ̃`�F�b�N���s��</dl>
     * @return int
     * @param request HttpServletRequest
     */
    public int checkDelete(HttpServletRequest request,
						   CatchListSession cases,
						   String[] checkList) {

		int rtn = RTN_DELETE_OK;
		try {
			String	pictureCD;			// �摜�R�[�h
			boolean defaultFlg = false;	// �f�t�H���g�摜�I���t���O
			Enumeration e = request.getParameterNames();
		  	
			// �Z�b�V�����̃`�F�b�N���X�g�N���A
			cases.crearCheckValue();
			while (e.hasMoreElements()) {
				String key = (String)e.nextElement();
		  	    
				if (key.equals("checkbox")) {
					checkList = request.getParameterValues(key);
					for (int i = 0; i < checkList.length; i++) {
						if ( cases.getDefaultCD() != null ) {
							if (cases.getDefaultCD().equals(checkList[i])) {
								// �f�t�H���g�摜���`�F�b�N����Ă����ꍇ
								// �t���O�Z�b�g
								defaultFlg = true;
							}
						}
						pictureCD = checkList[i];
						// �Z�b�V�����Ƀ`�F�b�N����Ă���摜�R�[�h���Z�b�g
						cases.setCheckValue(pictureCD);
					}
				}
			}
			if (defaultFlg == true) {
				// �f�t�H���g�摜���`�F�b�N����Ă����ꍇ
				rtn = RTN_ON_DEFAULT;
			}
			if (cases.getCheckSize() == 0) {
				// �`�F�b�N��I���̏ꍇ
				rtn = RTN_NO_CHECK;
			}
		}
		catch (Exception e) {
			throw new WmException(e);
		}
		return rtn;
    }
	
    /**
     * call DeleteCatch.
     * <dl>�L���b�`�摜�폜�������R�[������</dl>
     * @param session HttpSession
     */
    public void callDeleteCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response) {
		CatchListController cctrl =  new CatchListController(request, response);
		// �L���b�`�摜�폜�����R�[��
		cctrl.deletePicture(session);
    }
}
