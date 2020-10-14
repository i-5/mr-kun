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
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>�u�R�[�����e�|�ꗗ�v��ʗp�R���g���[��</strong>
 * @author
 * @version
 */
public class CallListController {

    /**
     * CallListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public CallListController() {
    }

    /**
     * �R�[�����e�ꗗ�\��
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @return �R�[�����e(�e�v�f��CallInfo�I�u�W�F�N�g)
     */
    public Vector createDisplay(HttpServletRequest req,
                                HttpServletResponse res,
                                HttpSession ses) {
        DBConnect  dbconn;
        Connection conn;
        Vector     callList = null;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                callList = callMan.getCallInfo(ses);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return callList;
    }

    /**
     * �I�������R�[�����e���폜����B
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @param callList �폜����R�[�����e�R�[�h(�e�v�f��String�I�u�W�F�N�g)
     * @return 1:�폜�ł��� 0:�폜�ł��Ȃ�����
     */
    public int deleteDisplay(HttpServletRequest req,
                             HttpServletResponse res,
                             HttpSession ses,
                             Vector callList) {
        DBConnect  dbconn;
        Connection conn;
        int        stat = 0;

        try {
            dbconn = new DBConnect();
            conn   = dbconn.getDBConnect();

            try {
                CallInfoManager callMan = new CallInfoManager(conn);
                stat = callMan.deleteCallInfo(ses, callList);
            }
            finally {
                dbconn.closeDB(conn);
            }
        }
        catch (WmException e) {
            DispatManager dm = new DispatManager();
            dm.distribute(req, res);
        }

        return stat;
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
            // ���O�C�����̃`�F�b�N (�Z�b�V�������̎擾)
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

            // �R�[�����e�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String clkey = SysCnst.KEY_CALLLIST_SESSION;
            CallListSession clses = (CallListSession)ses.getValue(clkey);
            if (clses == null) {
                clses = new CallListSession();
                ses.putValue(clkey, clses);
            }
        }
        catch (Exception e) {
            dm.distribute(req, res);
        }

        return bContinue;
    }
}
