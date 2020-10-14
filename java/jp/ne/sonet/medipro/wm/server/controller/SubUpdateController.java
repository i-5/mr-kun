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
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;

/**
 * <strong>�T�u�}�X�^�[�ǉ��E�ύX</strong>
 * <br>
 * @author 
 * @version 
 */
public class SubUpdateController {

    /**
     * SubUpdateController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public SubUpdateController() {
    }

    /**
     * �T�u�}�X�^�[����\������B
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrId �l�q�|�h�c
     * @return �l�q���
     */
    public MrInfo createDisplay(HttpServletRequest request,
                                HttpServletResponse response,
                                String mrId) {

        MrInfo info = null;
	DBConnect db = new DBConnect();
	Connection connection = null;

        try {
            connection = db.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            info = manager.getMrInfo(mrId);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            db.closeDB(connection);
	}

        return info;
    }

    /**
     * �T�u�}�X�^�[�����X�V�i�ǉ��E�ύX�j����B
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param res javax.servlet.http.HttpSession
     * @param info �l�q���
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              MrInfo info) {
	DBConnect db = new DBConnect();
	Connection connection = null;
	
        try {
            connection = db.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            manager.updateSubmaster(session, info);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            db.closeDB(connection);
	}
    }

    /**
     * �Z�b�V�������i�T�u�}�X�^�[�ǉ��E�X�V�Ǘ����j������������B
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @return �p����
     */
    public static boolean initSession(HttpServletRequest request,
                                      HttpServletResponse response) {

        boolean bContinue = true;
        DispatManager dm = new DispatManager();
        try {
            // ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
            if ( SessionManager.check(request) != true ) {
                dm.distSession(request, response);
                return false;
            }
            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            // �����`�F�b�N
            String m_flg = common.getMasterFlg();
            String s_flg = common.getMasterKengenSoshiki();
            if ( ! ( m_flg.equals(SysCnst.FLG_MASTER_WEB) ||
                    ( m_flg.equals(SysCnst.FLG_MASTER_SUB) &&
                      s_flg.equals(SysCnst.FLG_AUTHORITY_BRANCH) ) ) ) {
                dm.distAuthority(request, response);
                return false;
            }

            // �T�u�}�X�^�[�ǉ��E�X�V��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_SUBUPDATE_SESSION;
            SubUpdateSession subUpdateSession =
                (SubUpdateSession)session.getValue(subKey);
            if ( subUpdateSession == null ) {
                subUpdateSession = new SubUpdateSession();
                session.putValue(subKey, subUpdateSession);
            }

            // �T�u�}�X�^�[�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String preKey = SysCnst.KEY_SUBLIST_SESSION;
            SubListSession preSession =
                (SubListSession)session.getValue(preKey);
            if ( preSession != null )   preSession.clear();

	    //DB�C���X�^���X�`�F�b�N
	    if (!DBConnect.isConnectable()) {
		dm.distribute(request, response);
		return false;
	    }
        } catch (Exception e) {
            dm.distribute(request, response);
        }

        return bContinue;
    }

}
