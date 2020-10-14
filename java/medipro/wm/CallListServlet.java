package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>�u�R�[�����e�|�ꗗ�v��ʗp�T�[�u���b�g</strong>
 * @author
 * @version
 */
public class CallListServlet extends HttpServlet {
    private static final String PRT_HEADER = "### CallListServlet : ";

    /**
     * GET���\�b�h�����B
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        doPost(req, res);
    }

    /**
     * POST���\�b�h�����B
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            // �Z�b�V�����`�F�b�N
            if (CallListController.initSession(req, res) == false) {
                return;
            }

            HttpSession ses = req.getSession(true);
            Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            CallListSession callSes =
                    (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);
            String target;

            // ���b�Z�[�W�ƃ`�F�b�N��Ԃ̃N���A
            callSes.setMessageMode(callSes.MESMODE_NONE);
            callSes.clearChecked();

            // �y�[�W�ړ�
            if ((target = req.getParameter("page")) != null) {
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "page = " + target);
		}
                int curRow  = callSes.getCurrentRow();
                int pageRow = comSes.getCallLine();

                // ���y�[�W��
                if (target.equals("next")) {
                    callSes.setCurrentRow(curRow + pageRow);
                }
                // �O�y�[�W��
                else {
                    callSes.setCurrentRow(curRow - pageRow);
                }
            }

            // �\�[�g�ύX
            else if ((target = req.getParameter("sort")) != null) {
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "sort = " + target);
		}
                callSes.setSortKey(target);
                callSes.setOrderReverse();
                callSes.setCurrentRow(1);
            }

            // ���b�Z�[�W���[�h�̕ύX
            else if ((target = req.getParameter("execute")) != null) {
                Vector v = getCallCd(req);

                // �`�F�b�N��Ԃ�ǂ�
                for (int i = 0; i < v.size(); i++) {
                    callSes.setChecked((String)v.elementAt(i), true);
                }

                // �G���[���b�Z�[�W(OK)
                if (req.getParameter("mode").equals("delete")) {
                    // �폜�m�F
                    if (v.size() > 0) {
                        callSes.setMessageMode(callSes.MESMODE_CONFIRM);
                    }
                    // �`�F�b�N������
                    else {
                        callSes.setMessageMode(callSes.MESMODE_NO_CHECK);
                    }
                }
                // �G���[���b�Z�[�W(�͂�/������)
                else if (target.equals("update")) {
                    if (req.getParameter("yes") != null) {
                        // �폜���s
                        if (v.size() > 0) {
                            CallListController callCtrl = new CallListController();
                            int stat = callCtrl.deleteDisplay(req, res, ses, v);
                            // �폜�s��
                            if (stat == 0) {
                                callSes.setMessageMode(callSes.MESMODE_CANNOT_DELETE);
                            }
                            // �폜����
                            else {
                                callSes.setMessageMode(callSes.MESMODE_DELETE);
                                callSes.clearChecked();
                            }
                        }
                        // �`�F�b�N������
                        else {
                            callSes.setMessageMode(callSes.MESMODE_NO_CHECK);
                        }
                    }
                }
            }

            // Go to the next page
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "wm/WmCallList/index.html");
        } catch (Exception e) {
			log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(req, res, e);
        }
    }

    /**
     * �`�F�b�N����Ă���R�[�����e�R�[�h���擾����B
     * @param req �v���I�u�W�F�N�g
     * @return �e�v�f���R�[�����e�R�[�h�ƂȂ��Ă���x�N�^<br>
     *         �`�F�b�N��������� null
     */
    private Vector getCallCd(HttpServletRequest req) {
        // �R�[�����e�R�[�h�̎��o��
        Vector v   = new Vector();
        String s[] = req.getParameterValues("checkbox");
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                s[i] = encode(s[i]);
		if (SysCnst.DEBUG) {
		    log(PRT_HEADER + "param = " + s[i]);
		}
                v.addElement(s[i]);
            }
        }
        return v;
    }

    /**
     *
     */
    private String encode(String value) {
        if (value != null) {
            try {
                value = new String(value.getBytes("8859_1"), "SJIS");
            }
            catch (UnsupportedEncodingException e) {
                log("", e);
            }
        }
        return value;
    }
}
