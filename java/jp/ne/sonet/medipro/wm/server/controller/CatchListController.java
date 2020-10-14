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
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>会社キャッチ画像一覧Controllerクラス.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CatchListController {
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
    public CatchListController(HttpServletRequest req, HttpServletResponse res) {
	request = req;
	response = res;
//		dbconn = new DBConnect();
//		conn = dbconn.getDBConnect();
    }
	
    /////////////////////////////////////////////
    //class methods
    //

    /**
     * 会社画像一覧表示.
     * @return Vector
     * @param session HttpSession
     */
    public Vector createDisplay(HttpSession session) {

	Vector catchList = null;
		
	CatchListSession cases = (CatchListSession) session.getValue
	    (SysCnst.KEY_CATCH_SESSION);
	if (cases == null) {
	    System.out.println("CatchListSession is null(controller)!");
	    cases = new CatchListSession();
	    session.putValue(SysCnst.KEY_CATCH_SESSION, cases);
	}
		
	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchList = catchmanager.getCatchInfo();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}

	return catchList;
    }

    /**
     * 会社画像更新画面表示.
     * @return jp.ne.sonet.medipro.wm.server.entity.CatchInfo
     * @param session HttpSession
     */
    public CatchInfo createDisplayUpdate(HttpSession session) {

	CatchInfo caInfo = null;
		
	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    caInfo = catchmanager.getCatch();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}

	return caInfo;
    }

    /**
     * デフォルト画像更新.
     * @param session javax.servlet.http.HttpSession
     * @param pictureCD java.lang.String
     */
    public void updatePicture(HttpSession session, String pictureCD) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.changeDefault(pictureCD);
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
     * デフォルト画像削除.
     * @param session javax.servlet.http.HttpSession
     */
    public void deletePicture(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.deleteCatch();
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
     * 画像更新.
     * @param session javax.servlet.session.HttpSession
     */
    public void updateCatch(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.changeCatch();

	} catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * 画像追加.
     * @param session javax.servlet.session.HttpSession
     */
    public void addCatch(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.insertCatch();
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
     * セッション情報（会社キャッチ画像一覧管理情報）を初期化する.
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
	    Common common = (Common) session.getValue(comKey);
	    if ( common == null ) {
		dm.distSession(request, response);
		return false;
	    }
	    // 権限チェック
	    if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// ウェブマスタ以外の場合
		dm.distAuthority(request, response);
		return false;
	    }
			
	    // DBコネクトチェック
	    if (new DBConnect().isConnectable() == false) {
		dm.distribute(request, response);
		return false;
	    }
	    // 会社キャッチ画像一覧画面用セッション情報取得＆初期設定
	    String catchKey = SysCnst.KEY_CATCH_SESSION;
	    CatchListSession cuses =
		(CatchListSession) session.getValue(catchKey);
	    if (cuses == null) {
		cuses = new CatchListSession();
		session.putValue(catchKey, cuses);
	    }
	}
	catch (Exception e) {
	    dm.distribute(request, response);
	}

	return bContinue;
    }

    /**
     * セッション情報（会社キャッチ画像追加・変更管理情報）を初期化する.
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
	    Common common = (Common) session.getValue(comKey);
	    if (common == null) {
		dm.distSession(request, response);
		return false;
	    }
	    // 権限チェック
	    if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// ウェブマスタ以外の場合
		dm.distAuthority(request, response);
		return false;
	    }
			
	    // DBコネクトチェック
	    if (new DBConnect().isConnectable() == false) {
		dm.distribute(request, response);
		return false;
	    }
	    // 会社キャッチ画像追加・変更画面用セッション情報取得＆初期設定
	    String catchKey = SysCnst.KEY_CATCHUPDATE_SESSION;
	    CatchUpdateSession cuses =
		(CatchUpdateSession) session.getValue(catchKey);
	    if (cuses == null) {
		cuses = new CatchUpdateSession();
		session.putValue(catchKey, cuses);
	    }
	}
	catch (Exception e) {
	    dm.distribute(request, response);
	}

	return bContinue;
    }
}
