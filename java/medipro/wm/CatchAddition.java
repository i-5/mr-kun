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
 * <br>�L���b�`�摜�̒ǉ��E�ύX��ʂ��Ăяo��.
 * @author
 * @version
 */
public class CatchAddition extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**��ЃL���b�`�摜�ǉ��E�ύXhtml�t�@�C�����΃p�X*/
    private static final String CATCH_UPDATE_PATH = "wm/WmCatchUpdate/index.html";
	
    ////////////////////////////////////////////////////
    //class variable
    //
//      private String pictureCD;			// �摜�R�[�h
//      private Common common;				// �R�����Z�b�V����
//      private CatchUpdateSession cuses;	// �L���b�`�摜�ǉ��E�ύX�Z�b�V����
//      private CatchListSession cases;		// �L���b�`�摜�ꗗ�Z�b�V����
	
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
		String pictureCD;			// �摜�R�[�h
		Common common;				// �R�����Z�b�V����
		CatchUpdateSession cuses;	// �L���b�`�摜�ǉ��E�ύX�Z�b�V����
		CatchListSession cases;		// �L���b�`�摜�ꗗ�Z�b�V����

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			}
			else if (new DBConnect().isConnectable() == false) {
				new DispatManager().distribute(request, response);
			}
			else {
				String comKey = SysCnst.KEY_COMMON_SESSION;
				String updKey = SysCnst.KEY_CATCHUPDATE_SESSION;
				HttpSession session = request.getSession(true);
				// �R�����Z�b�V�����̎擾
				common = (Common) session.getValue(comKey);
				// �L���b�`�摜�ǉ��E�ύX�Z�b�V�����̎擾
				cuses = (CatchUpdateSession) session.getValue(updKey);
				// �L���b�`�摜�ꗗ�Z�b�V�����̎擾
				cases = (CatchListSession) session.getValue
					(SysCnst.KEY_CATCH_SESSION);
				
				DispatManager dm = new DispatManager();
				// �����`�F�b�N
				if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
					// �E�F�u�}�X�^�ȊO�̏ꍇ
					dm.distAuthority(request, response);
				}
				
				if (cuses == null) {
					cuses = new CatchUpdateSession();
					session.putValue(updKey, cuses);
					cuses.setFirstFlg(true);
				}
				
				if (cases != null) {
					cases.crearCheckValue();
					cases.setMessageState(SysCnst.CATCH_LIST_MSG_NONE);
				}
				
				// �Z�b�V�����̃^�C�g���N���A
				cuses.setPictureName(null);
				cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
				cuses.unCheck();
				// �p�����[�^�擾
				pictureCD = request.getParameter("pictureCD");
				if (pictureCD != null) {
					// �X�V
					// �Z�b�V�����ɉ摜�R�[�h�Z�b�g
					cuses.setPictureCD(pictureCD);
					// �X�V�t���O�Z�b�g
					cuses.setUpdateFlg(true);
				}
				else {
					// �V�K�ǉ���
					// �X�V�t���O�Z�b�g
					cuses.setUpdateFlg(false);
					// �Z�b�V�����̉摜�R�[�h�N���A
					cuses.setPictureCD(null);
					cuses.setPictureType("2");
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + CATCH_UPDATE_PATH);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
}
