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
 * <strong>�����N�ꗗ</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkListController {

    /**
     * LinkListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public LinkListController() {
    }

    /**
     * �����N�ꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �����N�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �����N�ꗗ���
     */
    public Enumeration createDisplay(HttpServletRequest request,
				     HttpServletResponse response,
				     Common common, LinkListSession session) {

	Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    LinkLibInfoManager manager = new LinkLibInfoManager(connection);
	    enum = manager.getLinkLibList(common, session);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * �����N�ꗗ�⑫���i�f�t�H���g�����N�R�[�h�j���擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �����N�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �f�t�H���g�����N�R�[�h
     */
    public String createDisplaySub(HttpServletRequest request,
				   HttpServletResponse response,
				   Common common, LinkListSession session) {

	String defLinkCd = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbconn.getDBConnect();
	    CompanyInfoManager manager = new CompanyInfoManager(connection);
	    defLinkCd = manager.getDefaultLinkCd(common.getCompanyCd());
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

	return defLinkCd;
    }

    /**
     * �����N�����폜����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �����N�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param v �폜�Ώۃ����N�R�[�h�̃��X�g
     */
    public void deleteDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, LinkListSession session,
			      Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    LinkLibInfoManager manager = new LinkLibInfoManager(connection);
	    manager.deleteLinkLibList(common, v);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

    /**
     * �����N���i�f�t�H���g�����N�R�[�h�j���X�V����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �����N�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param linkCd �����N�R�[�h
     */
    public void updateDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, LinkListSession session,
			      String linkCd) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    CompanyInfoManager manager = new CompanyInfoManager(connection);
	    manager.setDefaultLinkCd(common, common.getCompanyCd(), linkCd);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
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

	    // �����N�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
	    String subKey = SysCnst.KEY_LINKLIST_SESSION;
	    LinkListSession subSession =
		(LinkListSession)session.getValue(subKey);
	    if ( subSession == null ) {
		subSession = new LinkListSession();
		subSession.setSortKey(SysCnst.SORTKEY_LINK_LIST);
		subSession.setOrder(SysCnst.ORDER_LINK_LIST);
		subSession.setPage(1);
		session.putValue(subKey, subSession);
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
