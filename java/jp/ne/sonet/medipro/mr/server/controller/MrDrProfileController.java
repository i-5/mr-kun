package jp.ne.sonet.medipro.mr.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*; 
import jp.ne.sonet.medipro.mr.common.exception.*; 
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * <h3>顧客のプロフィール管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:53:44)
 * @author: 
 */
public class MrDrProfileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrPofile コンストラクター・コメント。
     */
    public MrDrProfileController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>顧客のプロフィール表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:01:24)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param drID java.lang.String
     */
    public TantoInfo createDisplay(HttpServletRequest req,
				   HttpServletResponse res,
				   String mrID,
				   String drID) {
	TantoInfo taninfo = null;
	
	try {
	    TantoInfoManager tan = new TantoInfoManager(conn);
	    taninfo = tan.getDrInfo(mrID,drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return taninfo;
    }

    /**
     * <h3>顧客削除</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param drID java.lang.String
     */
    public void drDelete(HttpServletRequest req,
			 HttpServletResponse res,
			 String mrID,
			 String drID) {
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfomanager.insertSentakuTourokuHist(drID, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>変更保存</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param tantoinfo jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param mrID java.lang.String
     * @param drID java.lang.String
     */
    public void drSave(HttpServletRequest req,
		       HttpServletResponse res,
		       TantoInfo tantoinfo,
		       String mrID,
		       String drID) {
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfomanager.updateDrProfile(tantoinfo, drID, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * ターゲットランク一覧の取得
     */
    public Enumeration getTargetRankList(HttpServletRequest req,
					 HttpServletResponse res,
					 String mrID) {
	Vector list = new Vector();
	
	try {
	    ActionInfoManager manager = new ActionInfoManager(conn);
	    list = manager.getActionList(mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return list.elements();
    }
}
