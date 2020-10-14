package medipro.wm;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>�u�R�[�����e�|�ǉ��E�ύX�v��ʗp�T�[�u���b�g</strong>
 * @author
 * @version
 */
public class CallUpdateServlet extends HttpServlet {
    private static final String PRT_HEADER = "### CallUpdateServlet : ";

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
            if (CallUpdateController.initSession(req, res) == false) {
                return;
            }

            HttpSession ses = req.getSession(true);
            Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            CallUpdateSession callSes =
                    (CallUpdateSession)ses.getValue(SysCnst.KEY_CALLUPDATE_SESSION);

            String cd;
            String call;
            String mode;

            // �R�[�����e�R�[�h�̐ݒ�
            if ((cd = Converter.getParameter(req, "cd")) != null) {
                callSes.setCallNaiyoCd(cd);
            }
            else {
                cd = callSes.getCallNaiyoCd();
            }
	    if (SysCnst.DEBUG) {
		log(PRT_HEADER + "cd = " + cd);
	    }

            // �R�[�����e�̐ݒ�
            if ((call = Converter.getParameter(req, "call")) != null) {
                callSes.setCallNaiyo(call);
            }
            else {
                CallUpdateController callCtrl = new CallUpdateController();
                call = callCtrl.getCallNaiyo(req, res, ses, cd);
                callSes.setCallNaiyo(call);
            }
	    if (SysCnst.DEBUG) {
		log(PRT_HEADER + "call = " + call);
	    }

            // �ǉ����[�h���ύX���[�h��
            if ((mode = req.getParameter("update")) != null) {
                // CallListSession �̃��b�Z�[�W�ƃ`�F�b�N����������
                CallListSession listSes =
		    (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);
                listSes.clear();

                // �R�[�����e�R�[�h��ύX�s�ɂ���
                if (mode.equals("update")) {
                    callSes.setMessageMode(callSes.MESMODE_NONE);
                    callSes.setUpdateMode(callSes.UPDMODE_UPDATE);
                }
                // ���͗����N���A����
                else {
                    callSes.initSession();
                }
            }

            String target;
            String url = "wm/WmCallUpdate/index.html";

            // ���s
            if ((target = req.getParameter("execute")) != null) {
                CallUpdateController callCtrl = new CallUpdateController();
                String callNaiyoCd = callSes.getCallNaiyoCd();
                String callNaiyo   = callSes.getCallNaiyo();

                callSes.setMessageMode(callSes.MESMODE_NONE);

                // �X�V���s
                if (target.equals("update")) {
                    // �u�͂��v
                    if (req.getParameter("yes") != null) {
                        int update = callCtrl.updateDisplay(req, res, ses,
							    callNaiyoCd, callNaiyo);
                        // �X�V����
                        if (update == 0) {
                            callSes.setMessageMode(callSes.MESMODE_UPDATE);
                        }
                        // �R�[�����e�R�[�h���d�����Ă���
                        else if (update == 1) {
                            callSes.setMessageMode(callSes.MESMODE_CANNOT_UPDATE);
                        }
                        // ���̑�
                        else {
                            callSes.setMessageMode(callSes.MESMODE_NONE);
                            return;
                        }
                    }
                }
                // �u�ۑ�����v
                if (req.getParameter("confirm") != null) {
                    callSes.setMessageMode(callSes.MESMODE_CONFIRM);
                }
                // �u���ɖ߂�v
                if (req.getParameter("back") != null) {
                    callSes.initSession();
                    url = "wm/WmCallList/index.html";
                }
            }

            // Go to the next page
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + url);
        } catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(req, res, e);
        }
    }
}
