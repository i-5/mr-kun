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
public class ActionListController {

    /**
     * ActionListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public ActionListController() {
    }

    /**
     * �d�v�x�ꗗ�����擾����B
     * @param request javax.servlet.http.HttpServletRequest
     * @param response javax.servlet.http.HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �d�v�x�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �d�v�x�ꗗ���
     */
    public Enumeration createDisplay(HttpServletRequest request,
				     HttpServletResponse response,
				     Common common,
				     ActionListSession session) {

	Enumeration enum = null;
	DBConnect dbconn = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbconn.getDBConnect();
	    ActionInfoManager manager =
		new ActionInfoManager(connection);
	    enum = manager.getTargetRankList(common.getCompanyCd(), session).elements();
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
			      Common common, ActionListSession session,
			      Vector v) {
	DBConnect dbconn = new DBConnect();
	Connection connection = null;

	try {
	    connection = dbconn.getDBConnect();
	    ActionInfoManager manager =
		new ActionInfoManager(connection);
	    manager.delete(common.getCompanyCd(), v );
	} catch (WmException e) {
            DispatManager dm= new DispatManager();
            dm.distribute(request, response);
        } finally {
	    dbconn.closeDB(connection);
	}
    }

   /**
    *  getDefaultTargetRank
    */
    public String getDefaultTargetRank(HttpServletRequest req,
				    HttpServletResponse res,
				    HttpSession session) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	String targetRank = null;

	DBConnect dbConnect = new DBConnect();
	Connection con = null;

	try {
	    con = dbConnect.getDBConnect();

	    CompanyInfoManager manager = new CompanyInfoManager(con);
	    targetRank = manager.getTargetRank(common.getCompanyCd());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	} finally {
	    dbConnect.closeDB(con);
	}

	return targetRank;
    }

    /**
     * �Z�b�V�������i�d�v�x�ꗗ�Ǘ����j������������B
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

	    // �d�v�x�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
	    String subKey = SysCnst.KEY_ACTION_SESSION;
	    ActionListSession subSession =
		(ActionListSession)session.getValue(subKey);
	    if ( subSession == null ) {
		subSession = new ActionListSession();
		subSession.setSortKey(SysCnst.SORTKEY_ACTION_LIST);
		subSession.setOrder(SysCnst.ORDER_ACTION_LIST);
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
