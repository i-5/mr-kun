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
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 05:40:59)
 * @author: 
 */
public class DrMrInputController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrMrInput コンストラクター・コメント。
     */
    public DrMrInputController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>ＭＲ登録表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:01:24)
     * @return java.util.Enumeration (CompanyTable)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res) {
	Enumeration enum = null;
	
	try {
	    CompanyTableManager companytablemanager = new CompanyTableManager(conn);
	    enum = companytablemanager.getCompanyTable();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * <h3>本人確認</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 05:44:35)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param companyCD java.lang.String
     */
    public MrInfo mrConfirm(HttpServletRequest req,
			    HttpServletResponse res,
			    String mrID,
			    String companyCD) {
	MrInfo mrinfo = null;
	
	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    mrinfo = mrinfomanager.getMrInfoCheck(mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return mrinfo;
    }

    /**
     * <h3>ＭＲ登録</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 05:45:30)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void mrInput(HttpServletRequest req,
			HttpServletResponse res,
			String drID,
			String mrID) {
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfomanager.insertSentakuTouroku(drID, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
