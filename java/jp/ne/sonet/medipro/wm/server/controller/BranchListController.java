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
 * <strong>支店・営業所一覧Controllerクラス.</strong>
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
	 * コンストラクタ.
	 */
	public BranchListController(HttpServletRequest req, HttpServletResponse res) {
		request = req;
		response = res;
//		dbconn = new DBConnect();
//		conn = dbconn.getDBConnect();
		
	}
	/**
	 * 支店・営業所一覧表示.
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
	 * 支店リスト取得.
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
	 * 営業所支店リスト取得.
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
	 * 支店・営業所削除.
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
	 * 支店追加.
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
	 * 営業所追加.
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
	 * 支店更新.
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
	 * 営業所更新.
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
     * セッション情報（支店・営業所一覧管理情報）を初期化する.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
	 * @return boolean
     */
    public boolean initSession(HttpServletRequest request,
			HttpServletResponse response) {

		boolean bContinue = true;
		DispatManager dm = new DispatManager();
		try {
			// ログイン情報のチェック（セッション情報の取得）
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
			// 権限チェック
			if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
				// サブマスタの場合
				if (!(common.getMasterKengenSoshiki().
						equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
					 common.getMasterKengenSoshiki().
					 	equals(SysCnst.FLG_AUTHORITY_OFFICE))) {
					// 支店および営業所のサブマスタでない
					dm.distAuthority(request, response);
					return false;
				}
			}
			else if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// ウェブマスタでもない
				dm.distAuthority(request, response);
				return false;
			}
			
			// DBコネクトチェック
			if (new DBConnect().isConnectable() == false) {
				dm.distribute(request, response);
				return false;
			}
			// 支店・営業所一覧画面用セッション情報取得＆初期設定
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
     * セッション情報（支店・営業所追加・変更管理情報）を初期化する.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
	 * @return boolean
     */
    public boolean initUpdateSession(HttpServletRequest request,
					 			   	  HttpServletResponse response) {
		boolean bContinue = true;
		DispatManager dm = new DispatManager();
		try {
			// ログイン情報のチェック（セッション情報の取得）
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
			// 権限チェック
			if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
				// サブマスタの場合
				if (!(common.getMasterKengenSoshiki().
						equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
					 common.getMasterKengenSoshiki().
					 	equals(SysCnst.FLG_AUTHORITY_OFFICE))) {
					// 支店および営業所のサブマスタでない
					dm.distAuthority(request, response);
					return false;
				}
			}
			else if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// ウェブマスタでもない
				dm.distAuthority(request, response);
				return false;
			}
			
			// DBコネクトチェック
			if (new DBConnect().isConnectable() == false) {
				dm.distribute(request, response);
				return false;
			}
			
			// 支店・営業所追加・更新画面用セッション情報取得＆初期設定
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
