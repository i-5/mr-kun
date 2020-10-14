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
 * <strong>�T�u�}�X�^�[�ꗗ</strong>
 * <br>
 * @author 
 * @version 
 */
public class SubListController {

    /**
     * SubListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public SubListController() {
    }

    /**
     * �T�u�}�X�^�[���ꗗ�\������B
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param ses javax.servlet.http.HttpSession
     * @return �v�f���X�g�i�l�q���j
     */
    public Enumeration createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HttpSession session) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            enum = manager.getSubmasters(session);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �T�u�}�X�^�[�̌��������グ��B
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param ses javax.servlet.http.HttpSession
     * @param v �l�q�|�h�c���X�g
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              Vector v) {

	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            manager.updateSubmaster(session, v);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * �Z�b�V�������i�T�u�}�X�^�[�ꗗ�Ǘ����j������������B
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

            // �T�u�}�X�^�[�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_SUBLIST_SESSION;
            SubListSession subListSession =
                (SubListSession)session.getValue(subKey);
            if ( subListSession == null ) {
                subListSession = new SubListSession();
                subListSession.setSortKey(SysCnst.SORTKEY_SUBMASTER_LIST);
                subListSession.setOrder(SysCnst.ORDER_SUBMASTER_LIST);
                subListSession.setPage(1);
                session.putValue(subKey, subListSession);
            }

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
