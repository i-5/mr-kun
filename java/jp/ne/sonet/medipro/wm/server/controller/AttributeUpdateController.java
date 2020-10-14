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
 * <strong>�l�q�����ǉ��E�X�V</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeUpdateController {

    /**
     * AttributeUpdateController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public AttributeUpdateController() {
    }

    /**
     * �l�q���������擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code �l�q�����R�[�h
     * @return �l�q�������
     */
    public AttributeInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {
        AttributeInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            info =
                manager.getAttributeInfo(common, code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * �l�q���������X�V�i�ǉ��E�ύX�j����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �l�q�������
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              AttributeInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
	    connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            if ( info.getMrAttributeCd() == null ||
                 info.getMrAttributeCd().equals("") ) {
                manager.insertAttributeInfo(common, info);
            }
            else {
                manager.updateAttributeInfo(common, info);
            }
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * �l�q�����ꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �l�q�����ꗗ���
     */
    public Enumeration createDisplaySub(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Common common) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            AttributeInfoManager manager = new AttributeInfoManager(connection);
            enum = manager.getAttributeList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �Z�b�V�������i�l�q�����Ǘ����j������������B
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

            // �l�q�����ǉ��E�X�V��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_ATTRIBUTEUPDATE_SESSION;
            AttributeUpdateSession subSession =
                (AttributeUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new AttributeUpdateSession();
                session.putValue(subKey, subSession);
            }

            // �l�q�����ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String preKey = SysCnst.KEY_ATTRIBUTELIST_SESSION;
            AttributeListSession preSession =
                (AttributeListSession)session.getValue(preKey);
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
