package jp.ne.sonet.medipro.wm.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * �u�R�[�����e�|�ǉ��E�ύX�v��ʗp�R���g���[��
 * @author
 * @version
 */
public class CallUpdateController {

    /**
     * CallUpdateController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public CallUpdateController() {
    }

    /**
     * �R�[�����e�ǉ��E�ύX
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @param callNaiyo   �R�[�����e
     * @return 0:����ɒǉ��ł���<br>
     *         1:�d�����Ă���<br>
     *        -1:���̑��̃G���[
     */
    public int updateDisplay(HttpServletRequest req,
                             HttpServletResponse res,
                             HttpSession ses,
                             String callNaiyoCd,
                             String callNaiyo) {
        DBConnect  dbconn;
        Connection conn;
        CallUpdateSession callSes =
                (CallUpdateSession)ses.getValue(SysCnst.KEY_CALLUPDATE_SESSION);

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);

                // �ǉ����[�h�̎�
                if (callSes.getUpdateMode() == callSes.UPDMODE_NEW) {
                    if (callMan.isOverlap(ses, callNaiyoCd)) {
                        return 1;
                    }
                    int rc = callMan.insertCallInfo(ses, callNaiyoCd, callNaiyo);
                    if (rc != 0) {
                        return 0;
                    }
                }

                // �X�V���[�h�̎�
                if (callSes.getUpdateMode() == callSes.UPDMODE_UPDATE) {
                    int rc = callMan.updateCallInfo(ses, callNaiyoCd, callNaiyo);
                    if (rc != 0) {
                        return 0;
                    }
                }
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return -1;
    }

    /**
     * �R�[�����e�R�[�h����R�[�����e�擾
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @return �R�[�����e
     */
    public String getCallNaiyo(HttpServletRequest req,
                               HttpServletResponse res,
                               HttpSession ses,
                               String callNaiyoCd) {
        DBConnect  dbconn;
        Connection conn;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                return callMan.searchCallNaiyo(ses, callNaiyoCd);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return null;
    }

    /**
     * �Z�b�V������������������B
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     * @return �p����
     */
    public static boolean initSession(HttpServletRequest req,
                                      HttpServletResponse res) {
        boolean bContinue = true;
        DispatManager dm = new DispatManager();

        try {
            // ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
            if (SessionManager.check(req) != true) {
                dm.distSession(req, res);
                return false;
            }

            // �Z�b�V�����`�F�b�N
            HttpSession ses = req.getSession(true);
            Common comses = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            if (comses == null) {
                dm.distSession(req, res);
                return false;
            }

            // �����`�F�b�N
            if (!comses.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
                // �E�F�u�}�X�^�ȊO�̏ꍇ
                dm.distAuthority(req, res);
                return false;
            }

            // DB�`�F�b�N
            if (!DBConnect.isConnectable()) {
                dm.distribute(req, res);
                return false;
            }

            // �R�[�����e�ǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
            String cukey = SysCnst.KEY_CALLUPDATE_SESSION;
            CallUpdateSession cuses = (CallUpdateSession)ses.getValue(cukey);
            if (cuses == null) {
                cuses = new CallUpdateSession();
                ses.putValue(cukey, cuses);
            }
        }
        catch (Exception e) {
            dm.distribute(req, res);
        }

        return bContinue;
    }
}
