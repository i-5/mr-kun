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
 * <strong>�l�q�����ꗗ</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeListController {

    /**
     * AttributeListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public AttributeListController() {
    }

    /**
     * �l�q�����ꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �l�q�����ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �l�q�����ꗗ���
     */
    public Enumeration createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     AttributeListSession session) {
        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            enum = manager.getAttributeList(common, session);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �l�q���������폜����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �l�q�����ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param v �폜�Ώۂl�q�����R�[�h�̃��X�g
     */
    public void deleteDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
			      AttributeListSession session,
                              Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            manager.deleteAttributeList(common, v);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * �l�q���������폜����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �l�q�����ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param v �폜�Ώۂl�q�����R�[�h�̃��X�g
     * @return �폜��
     */
    public boolean isDeleteDisplay(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Common common,
				   AttributeListSession session,
                                   Vector v) {
        boolean bDelete = false;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            MrInfoManager manager = new MrInfoManager(connection);
            bDelete = (manager.hasMrAttribute(v) ? false : true);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return bDelete;
    }

    /**
     * �Z�b�V�������i�l�q�����ꗗ�Ǘ����j������������B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
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
            if ( ! common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) ) {
                dm.distAuthority(request, response);
                return false;
            }

            // �l�q�����ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_ATTRIBUTELIST_SESSION;
            AttributeListSession subSession =
                (AttributeListSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new AttributeListSession();
                subSession.setSortKey(SysCnst.SORTKEY_ATTRIBUTE_LIST);
                subSession.setOrder(SysCnst.ORDER_ATTRIBUTE_LIST);
                subSession.setPage(1);
                session.putValue(subKey, subSession);
            }

            // �x�X�E�c�Ə��ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String preKey = SysCnst.KEY_BRANCHLIST_SESSION;
            BranchListSession preSession =
                (BranchListSession)session.getValue(preKey);
            if ( preSession != null )   {
                preSession.crearCheckValue();
                preSession.setMessageState(SysCnst.BRANCH_LIST_MSG_NONE);
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
