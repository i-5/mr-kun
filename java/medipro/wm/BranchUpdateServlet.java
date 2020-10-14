package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.util.*;

/**
 * <strong>�x�X�E�c�Ə��ǉ��E�ύX�Ή�Servlet�N���X</strong>
 * <br>�x�X�E�c�Ə��̒ǉ��E�ύX���s��.
 * @author
 * @version
 */
public class BranchUpdateServlet extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**�������Ȃ�*/
    private static final int RTN_NONE = -1;
    /**�x�X���I��*/
    private static final int RTN_NO_SELECT = -2;
    /**�x�X�ǉ�*/
    private static final int RTN_ADD_BRANCH = 1;
    /**�x�X�X�V*/
    private static final int RTN_UPDATE_BRANCH = 2;
    /**�c�Ə��ǉ�*/
    private static final int RTN_ADD_OFFICE = 3;
    /**�c�Ə��X�V*/
    private static final int RTN_UPDATE_OFFICE = 4;
    /**�x�X�E�c�Ə��X�V*/
    private static final int RTN_UPDATE_BOTH = 5;
    /**�x�X�E�c�Ə��ǉ�*/
    private static final int RTN_ADD_BOTH = 6;
    /**�x�X�X�V�E�c�Ə��ǉ�*/
    private static final int RTN_ADD_OFFICE_UPDATE_BRANCH = 7;

    /**�x�X�E�c�Ə��ꗗhtml�t�@�C�����΃p�X*/
    private static final String BRANCH_LIST_PATH = "wm/WmBranchList/index.html";
    /**�x�X�E�c�Ə��ǉ��E�ύXhtml�t�@�C�����΃p�X*/
    private static final String BRANCH_UPDATE_PATH = "wm/WmBranchUpdate/index.html";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private BranchUpdateSession buses;
//      private String mode;
//      private String select;
//      private String select2;
//      private String textfield;
//      private String textfield2;
//      private String path;

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
		String mode;
		String select;
		String select2;
		String textfield;
		String textfield2;
		String path = null;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				// �Z�b�V�����̎擾
				HttpSession session = request.getSession(true);
				String brUpdKey = SysCnst.KEY_BRANCHUPDATE_SESSION;
				BranchUpdateSession buses
					= (BranchUpdateSession) session.getValue(brUpdKey);
				if (buses == null) {
					buses = new BranchUpdateSession();
					session.putValue(brUpdKey, buses);
				}

				// �p�����[�^�擾
				mode = request.getParameter("mode");
				select = request.getParameter("select");
				select2 = request.getParameter("select2");
				textfield = Converter.getParameter(request, "textfield");
				textfield2 = Converter.getParameter(request, "textfield2");
				if (SysCnst.DEBUG) {
					log("mode:"+mode);
					log("select:"+select);
					log("select2:"+select2);
					log("textfield:"+textfield);
					log("textfield2:"+textfield2);
				}
				if (mode.equals("change")) {
					// �x�X���X�g�{�b�N�X�I��ύX��
					buses.setBranchCD(select);
					buses.setBranchName(null);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					}
					else {
						buses.setNewBranch(false);
					}
					buses.setOfficeCD(select2);
					buses.setOfficeName(null);
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					
					if (buses.getMessageState() == SysCnst.BRANCH_UPDATE_MSG_NOSELECT) {
						// ��x�X��I������...����b�Z�[�W�\������Ă����ꍇ
						// ���b�Z�[�WID�Z�b�g
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("list")) {
					// ����ɖ߂飉�����
					buses.setBranchCD(null);
					buses.setBranchName(null);
					buses.setOfficeCD(null);
					buses.setOfficeName(null);
					path = BRANCH_LIST_PATH;
				}
				else if (mode.equals("updatemessage")) {
					// ��ۑ��������
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_CONFIRM);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					} else {
						buses.setNewBranch(false);
					}
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("update")) {
					// ��ۑ����܂�����Ţ�͂��������
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					} else {
						buses.setNewBranch(false);
					}
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					BranchListController bctrl = 
						new BranchListController(request, response);
					int rtn = checkParameter(request, textfield, textfield2, buses);
					if (SysCnst.DEBUG) {
						log("####rtn:"+rtn);
					}
					switch (rtn) {
					case RTN_NONE:	// �������Ȃ�
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
						break;
					case RTN_NO_SELECT:	// �G���[���b�Z�[�W
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NOSELECT);
						break;
					case RTN_ADD_BRANCH:	// �x�X�ǉ�
						bctrl.addBranch(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_BRANCH:	// �x�X�X�V
						bctrl.updateBranch(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_OFFICE:	// �c�Ə��ǉ�
						bctrl.addOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_OFFICE:	// �c�Ə��X�V
						bctrl.updateOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_BOTH:	// �x�X�E�c�Ə��X�V
						bctrl.updateBranch(session);
						BranchListController bctrl2 = 
							new BranchListController(request, response);
						bctrl2.updateOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_BOTH:	// �x�X�E�c�Ə��ǉ�
						bctrl.addBranch(session);
						if (buses.getBranchCD() != null) {
							BranchListController bctrl3 = 
								new BranchListController(request, response);
							bctrl3.addOffice(session);
						}
						//					buses.setBranchCD(select);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_OFFICE_UPDATE_BRANCH:	// �x�X�X�V�E�c�Ə��ǉ�
						bctrl.updateBranch(session);
						BranchListController bctrl4 = 
							new BranchListController(request, response);
						bctrl4.addOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("done")) {
					// ��ۑ����܂�����ŢOK�������
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				} else if (mode.equals("noupdate")) {
					// ��ۑ����܂�����Ţ�������������
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				} else {
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + path);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
	
    /**
     * checkParameter.
     * @return int
     * @param request HttpServletRequest
     */
    public int checkParameter(HttpServletRequest request,
							  String textfield,
							  String textfield2,
							  BranchUpdateSession buses) {

		if (buses.isNewBranch() == true) {
			// �x�X�V�K�ǉ���
			if (!textfield.equals("")) {
				// �x�X�e�L�X�g���͂���
				if (!textfield2.equals("")) {
					// �c�Ə��e�L�X�g���͂���
					// �x�X�E�c�Ə��ǉ�
					return RTN_ADD_BOTH;
				} else {
					// �c�Ə��e�L�X�g������
					// �x�X�ǉ�
					return RTN_ADD_BRANCH;
				}
			} else {
				// �x�X�e�L�X�g�����͂̏ꍇ
				if (!textfield2.equals("")) {
					// �c�Ə��e�L�X�g���͂���
					// �G���[
					return RTN_NO_SELECT;
				} else {
					// �c�Ə��e�L�X�g������
					// �������Ȃ�
					return RTN_NONE;
				}
			}
		} else {
			if (buses.isNewOffice() == true) {
				// �x�X�V�K�ǉ��ȊO�A�c�Ə��V�K�ǉ���
				if (!textfield.equals("")) {
					// �x�X�e�L�X�g���͂���
					if (!textfield2.equals("")) {
						// �c�Ə��e�L�X�g���͂���̏ꍇ
						if (buses.getOriginalBranch().equals(textfield)) {
							// �x�X�ύX����Ă��Ȃ��ꍇ
							// �c�Ə��ǉ�
							return RTN_ADD_OFFICE;
						}
						else {
							// �x�X�ύX����Ă����ꍇ
							// �x�X�X�V�E�c�Ə��ǉ�
							return RTN_ADD_OFFICE_UPDATE_BRANCH;
						}
					} else {
						// �c�Ə��e�L�X�g�����͂̏ꍇ
						if (buses.getOriginalBranch().equals(textfield)) {
							// �x�X�ύX����Ă��Ȃ��ꍇ
							// �������Ȃ�
							return RTN_NONE;
						} else {
							// �x�X�ύX����Ă����ꍇ
							// �x�X�X�V
							return RTN_UPDATE_BRANCH;
						}
					}
				} else {
					// �x�X�e�L�X�g������
					// �G���[���b�Z�[�W
					return RTN_NO_SELECT;
				}
			} else {
				// �x�X�V�K�ǉ��ȊO�A�c�Ə��V�K�ǉ��ȊO
				if (textfield != null) {
					// �x�X�e�L�X�gnull�ȊO(�E�F�u�A�x�X�T�u��)
					if (!textfield.equals("")) {
						// �x�X�e�L�X�g���͂���
						if (!textfield2.equals("")) {
							// �c�Ə��e�L�X�g���͂���
							if (buses.getOriginalBranch().equals(textfield)) {
								// �x�X�ύX����Ă��Ȃ��ꍇ
								if (buses.getOriginalOffice().equals(textfield2)) {
									// �c�Ə��ύX����Ă��Ȃ��ꍇ
									// �������Ȃ�
									return RTN_NONE;
								} else {
									// �c�Ə��ύX����Ă����ꍇ
									// �c�Ə��X�V
									return RTN_UPDATE_OFFICE;
								}
							} else {
								// �x�X�ύX����Ă����ꍇ
								if (buses.getOriginalOffice().equals(textfield2)) {
									// �c�Ə��ύX����Ă��Ȃ��ꍇ
									// �x�X�X�V
									return RTN_UPDATE_BRANCH;
								} else {
									// �c�Ə��ύX����Ă����ꍇ
									// �x�X�E�c�Ə��X�V
									return RTN_UPDATE_BOTH;
								}
							}
						} else {
							// �c�Ə��e�L�X�g���͂Ȃ�
							if (buses.getOriginalBranch().equals(textfield)) {
								// �x�X�ύX����Ă��Ȃ��ꍇ
								// �������Ȃ�
								return RTN_NONE;
							} else {
								// �x�X�ύX����Ă����ꍇ
								// �x�X�X�V
								return RTN_UPDATE_BRANCH;
							}
						}
					} else {
						// �x�X�e�L�X�g���͂Ȃ�
						if (!textfield2.equals("")) {
							// �c�Ə��e�L�X�g���͂���
							if (buses.getOriginalOffice().equals(textfield2)) {
								// �c�Ə��ύX����Ă��Ȃ��ꍇ
								// �������Ȃ�
								return RTN_NONE;
							} else {
								// �c�Ə��ύX����Ă����ꍇ
								// �c�Ə��X�V
								return RTN_UPDATE_OFFICE;
							}
						} else {
							// �c�Ə��e�L�X�g���͂Ȃ�
							// �������Ȃ�
							return RTN_NONE;
						}
					}
				} else {
					// �x�X�e�L�X�gnull(�c�Ə��T�u��)
					if (!textfield2.equals("")) {
						// �c�Ə��e�L�X�g���͂���
						if (buses.getOriginalOffice().equals(textfield2)) {
							// �c�Ə��ύX����Ă��Ȃ��ꍇ
							// �������Ȃ�
							return RTN_NONE;
						} else {
							// �c�Ə��ύX����Ă����ꍇ
							// �c�Ə��X�V
							return RTN_UPDATE_OFFICE;
						}
					} else {
						// �c�Ə��e�L�X�g���͂Ȃ�
						// �������Ȃ�
						return RTN_NONE;
					}
				}
			}
		}
    }
}
