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
 * 作成日 : (00/06/19 午後 04:18:34)
 * @author: 
 */
public class MrLoginController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrLogin コンストラクター・コメント。
     */
    public MrLoginController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>ログイン画面の表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 02:14:20)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     */
    public MrInfo createDisplay(HttpServletRequest req,
				HttpServletResponse res,
				String mrID) {
	MrInfo mrinfo = null; 

	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    mrinfo = mrinfomanager.getMrInfo(mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return mrinfo;
    }

    /**
     * <h3>ＭＲパスワードの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/29 午後 09:19:21)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    public void updatePassWord(HttpServletRequest req,
			       HttpServletResponse res,
			       String mrID,
			       MrInfo mrinfo) {
	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    mrinfomanager.updatePassword(mrID, mrinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
