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
 * <strong>�����N���ޒǉ��E�X�V</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class TeikeiClassUpdateController {

    /**
     * TeikeiClassUpdateController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public TeikeiClassUpdateController() {
    }

    /**
     * �����N���ޏ����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code �����N���ރR�[�h
     * @return �����N���ޏ��
     */
    public TeikeiClassInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {

        TeikeiClassInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            TeikeiClassInfoManager manager = new TeikeiClassInfoManager(connection);
            info = manager.getTeikeiClassInfo(common, code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * �����N���ޏ����X�V�i�ǉ��E�ύX�j����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���ޏ��
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              TeikeiClassInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

        try {
            connection = dbconn.getDBConnect();
            TeikeiClassInfoManager manager = new TeikeiClassInfoManager(connection);
            if ( info.getBunruiCode() == null ||
                 info.getBunruiCode().equals("") ) {
                manager.insertTeikeiClassInfo(common, info);
            }
            else {
                manager.updateTeikeiClassInfo(common, info);
            }
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}
    }

    /**
     * �����N���ވꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �����N���ވꗗ���
     */
    public Enumeration createDisplaySub(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Common common) {

        Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            TeikeiClassInfoManager manager = new TeikeiClassInfoManager(connection);
            enum = manager.getTeikeiClassList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �Z�b�V�������i�����N���ފǗ����j������������B
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

            // �����N���ޒǉ��E�X�V��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_TEIKEICLASSUPDATE_SESSION;
            TeikeiClassUpdateSession subSession =
                (TeikeiClassUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new TeikeiClassUpdateSession();
                session.putValue(subKey, subSession);
            }

            // �����N�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String preKey = SysCnst.KEY_EXPRESSIONLIST_SESSION;
            ExpressionListSession preSession =
                (ExpressionListSession)session.getValue(preKey);
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
