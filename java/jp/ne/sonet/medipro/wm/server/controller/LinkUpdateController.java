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
 * <strong>�����N�ǉ��E�X�V</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkUpdateController {

    /**
     * LinkUpdateController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public LinkUpdateController() {
    }

    /**
     * �����N�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code �����N�R�[�h
     * @return �����N���
     */
    public LinkLibInfo createDisplay(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Common common,
                                     String code) {

        LinkLibInfo info = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            LinkLibInfoManager manager = new LinkLibInfoManager(connection);
            info = manager.getLinkLibInfo(common, common.getCompanyCd(), code);
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return info;
    }

    /**
     * �����N�����X�V�i�ǉ��E�ύX�j����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���
     */
    public void updateDisplay(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              LinkLibInfo info) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
        try {
            connection = dbconn.getDBConnect();
            LinkLibInfoManager manager = new LinkLibInfoManager(connection);
            if ( info.getLinkCd() == null || info.getLinkCd().equals("") ) {
                manager.insertLinkLibInfo(common, info);
            }
            else {
                manager.updateLinkLibInfo(common, info);
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
            LinkClassInfoManager manager = new LinkClassInfoManager(connection);
            enum = manager.getLinkClassList(common, common.getCompanyCd());
        } catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
            dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �Z�b�V�������i�����N�ꗗ�Ǘ����j������������B
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

            // �����N�ǉ��E�X�V��ʗp�Z�b�V�������擾�������ݒ�
            String subKey = SysCnst.KEY_LINKUPDATE_SESSION;
            LinkUpdateSession subSession =
                (LinkUpdateSession)session.getValue(subKey);
            if ( subSession == null ) {
                subSession = new LinkUpdateSession();
                session.putValue(subKey, subSession);
            }

            // �����N�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            String preKey = SysCnst.KEY_LINKLIST_SESSION;
            LinkListSession preSession =
                (LinkListSession)session.getValue(preKey);
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
