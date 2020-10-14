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
 * <h3>医師初期プロフィール管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:53:44)
 * @author: 
 */
public class DrInitProfileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrPofile コンストラクター・コメント。
     */
    public DrInitProfileController() {
	
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
	
    }

    /**
     * <h3>医師初期プロフィール表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:01:24)
     * @return jp.ne.sonet.medipro.mr.server.entity.BvUserProfileInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     */
    public BvUserProfileInfo createDisplay(HttpServletRequest req,
					   HttpServletResponse res,
					   String drID) {
	BvUserProfileInfo bvuserprofileinfo = null;
	
	DBConnect dbconnSyb = new DBConnect();
	Connection connSyb = dbconnSyb.getDBConnect();
	
	
	try {
	    BvUserProfileInfoManager bvuserprofilemanager
		= new BvUserProfileInfoManager(connSyb);
	    bvuserprofileinfo = bvuserprofilemanager.getBvUserProfile(drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(connSyb);
	}

	return bvuserprofileinfo;
    }

    /**
     * <h3>医師初期プロフィール登録</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/18 午後 05:36:11)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void profileInput(HttpServletRequest req,
			     HttpServletResponse res,
			     DoctorInfo doctorinfo) {
	try {
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    doctorinfomanager.insert(doctorinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>医師初期プロフィール更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/18 午後 05:36:11)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void profileUpdate(HttpServletRequest req,
			      HttpServletResponse res,
			      DoctorInfo doctorinfo) {
	try {
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    doctorinfomanager.update(doctorinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
