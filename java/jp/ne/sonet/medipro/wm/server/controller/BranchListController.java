package jp.ne.sonet.medipro.wm.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*; 
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>�x�X�E�c�Ə��ꗗController�N���X.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class BranchListController {
	/////////////////////////////////////////////
	//class variables
	//
	protected Connection conn;
	protected DBConnect dbconn;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	/////////////////////////////////////////////
	//constructors
	//
	/**
	 * �R���X�g���N�^.
	 */
	public BranchListController(HttpServletRequest req, HttpServletResponse res) {
		request = req;
		response = res;
//		dbconn = new DBConnect();
//		conn = dbconn.getDBConnect();
		
	}
	/**
	 * �x�X�E�c�Ə��ꗗ�\��.
	 * @return Vector
	 * @param session HttpSession
	 */
	public Vector createDisplay(HttpSession session) {

		Vector branchList = null;
		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();
			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchList = branchmanager.getBranchInfo();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}

		return branchList;
	}
	/**
	 * �x�X���X�g�擾.
	 * @return Vector
	 * @param session HttpSession
	 */
	public Vector getBranchList(HttpSession session) {

		Vector branchList = null;
		
		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchList = branchmanager.getBranchSet();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}

		return branchList;
	}
	/**
	 * �c�Ə��x�X���X�g�擾.
	 * @return Vector
	 * @param session HttpSession
	 * @param branchCD String
	 */
	public Vector getOfficeList(HttpSession session, String branchCD) {

		Vector branchList = null;
		
		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchList = branchmanager.getOfficeSet(branchCD);
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}

		return branchList;
	}
	/**
	 * �x�X�E�c�Ə��폜.
	 * @param session HttpSession
	 */
	public void deleteBranch(HttpSession session) {

		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchmanager.deleteOffice();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}
	}
	/**
	 * �x�X�ǉ�.
	 * @param session HttpSession
	 */
	public void addBranch(HttpSession session) {

		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchmanager.insertBranch();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}
	}
	/**
	 * �c�Ə��ǉ�.
	 * @param session HttpSession
	 */
	public void addOffice(HttpSession session) {

		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchmanager.insertOffice();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}
	}
	/**
	 * �x�X�X�V.
	 * @param session HttpSession
	 */
	public void updateBranch(HttpSession session) {

		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchmanager.updateShiten();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}
	}
	/**
	 * �c�Ə��X�V.
	 * @param session HttpSession
	 */
	public void updateOffice(HttpSession session) {

		try{
			dbconn = new DBConnect();
			conn = dbconn.getDBConnect();

			BranchInfoManager branchmanager = 
					new BranchInfoManager(conn, session);
			branchmanager.updateEigyosyo();
		}
		catch (WmException e) {
			DispatManager dm = new DispatManager();
			dm.distribute(request,response);
			throw new WmException(e);
		}
		finally {
			dbconn.closeDB(conn);
		}
	}

    /**
     * �Z�b�V�������i�x�X�E�c�Ə��ꗗ�Ǘ����j������������.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
	 * @return boolean
     */
    public boolean initSession(HttpServletRequest request,
			HttpServletResponse response) {

		boolean bContinue = true;
		DispatManager dm = new DispatManager();
		try {
			// ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
			if (SessionManager.check(request) != true) {
System.out.println("session err!");
				dm.distSession(request, response);
				return false;
			}
			HttpSession session = request.getSession(true);
			String comKey = SysCnst.KEY_COMMON_SESSION;
			Common common = (Common)session.getValue(comKey);
			if (common == null) {
				dm.distSession(request, response);
				return false;
			}
			// �����`�F�b�N
			if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
				// �T�u�}�X�^�̏ꍇ
				if (!(common.getMasterKengenSoshiki().
						equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
					 common.getMasterKengenSoshiki().
					 	equals(SysCnst.FLG_AUTHORITY_OFFICE))) {
					// �x�X����щc�Ə��̃T�u�}�X�^�łȂ�
					dm.distAuthority(request, response);
					return false;
				}
			}
			else if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^�ł��Ȃ�
				dm.distAuthority(request, response);
				return false;
			}
			
			// DB�R�l�N�g�`�F�b�N
			if (new DBConnect().isConnectable() == false) {
				dm.distribute(request, response);
				return false;
			}
			// �x�X�E�c�Ə��ꗗ��ʗp�Z�b�V�������擾�������ݒ�
			String branchKey = SysCnst.KEY_BRANCHLIST_SESSION;
			BranchListSession brses =
				(BranchListSession)session.getValue(branchKey);
			if (brses == null) {
				brses = new BranchListSession();
				session.putValue(branchKey, brses);
			}
		}
		catch (Exception e) {
			dm.distribute(request, response);
		}

		return bContinue;
    }
    /**
     * �Z�b�V�������i�x�X�E�c�Ə��ǉ��E�ύX�Ǘ����j������������.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
	 * @return boolean
     */
    public boolean initUpdateSession(HttpServletRequest request,
					 			   	  HttpServletResponse response) {
		boolean bContinue = true;
		DispatManager dm = new DispatManager();
		try {
			// ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
			if (SessionManager.check(request) != true) {
				dm.distSession(request, response);
				return false;
			}
			HttpSession session = request.getSession(true);
			String comKey = SysCnst.KEY_COMMON_SESSION;
			Common common = (Common)session.getValue(comKey);
			if (common == null) {
				dm.distSession(request, response);
				return false;
			}
			// �����`�F�b�N
			if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
				// �T�u�}�X�^�̏ꍇ
				if (!(common.getMasterKengenSoshiki().
						equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
					 common.getMasterKengenSoshiki().
					 	equals(SysCnst.FLG_AUTHORITY_OFFICE))) {
					// �x�X����щc�Ə��̃T�u�}�X�^�łȂ�
					dm.distAuthority(request, response);
					return false;
				}
			}
			else if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^�ł��Ȃ�
				dm.distAuthority(request, response);
				return false;
			}
			
			// DB�R�l�N�g�`�F�b�N
			if (new DBConnect().isConnectable() == false) {
				dm.distribute(request, response);
				return false;
			}
			
			// �x�X�E�c�Ə��ǉ��E�X�V��ʗp�Z�b�V�������擾�������ݒ�
			String branchKey = SysCnst.KEY_BRANCHUPDATE_SESSION;
			BranchUpdateSession brses =
				(BranchUpdateSession)session.getValue(branchKey);
			if (brses == null) {
				brses = new BranchUpdateSession();
				session.putValue(branchKey, brses);
			}
		}
		catch (Exception e) {
			dm.distribute(request, response);
		}

		return bContinue;
    }
}
