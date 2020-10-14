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
 * <strong>��^���ꗗ</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionListController {

    /**
     * ExpressionListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public ExpressionListController() {
    }

    /**
     * ��^���ꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session ��^���ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return ��^���ꗗ���
     */
    public Enumeration createDisplay(HttpServletRequest request,
				     HttpServletResponse response,
				     Common common,
				     ExpressionListSession session) {

	Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbconn.getDBConnect();
	    ExpressionLibInfoManager manager =
		new ExpressionLibInfoManager(connection);
	    enum = manager.getExpressionLibList(common, session);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}

        return enum;
    }

    /**
     * ��^�������폜����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session ��^���ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param v �폜�Ώے�^���R�[�h�̃��X�g
     */
    public void deleteDisplay(HttpServletRequest request,
			      HttpServletResponse response,
			      Common common, ExpressionListSession session,
			      Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    ExpressionLibInfoManager manager =
		new ExpressionLibInfoManager(connection);
	    manager.deleteExpressionLibList(common, v);
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

    /**
     * �Z�b�V�������i��^���ꗗ�Ǘ����j������������B
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

	    // ��^���ꗗ��ʗp�Z�b�V�������擾�������ݒ�
	    String subKey = SysCnst.KEY_EXPRESSIONLIST_SESSION;
	    ExpressionListSession subSession =
		(ExpressionListSession)session.getValue(subKey);
	    if ( subSession == null ) {
		subSession = new ExpressionListSession();
		subSession.setSortKey(SysCnst.SORTKEY_EXPRESSION_LIST);
		subSession.setOrder(SysCnst.ORDER_EXPRESSION_LIST);
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
