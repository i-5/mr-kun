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
 * <h3>医師ログイン管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 04:30:09)
 * @author: 
 */
public class DrLoginController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrLogin コンストラクター・コメント。
     */
    public DrLoginController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>ＭＲパスワードの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/29 午後 09:19:21)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    public void updatePassWord(HttpServletRequest req,
			       HttpServletResponse res,
			       String drID,
			       DoctorInfo doctorinfo) {
	try {
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    doctorinfomanager.updatePassword(drID, doctorinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
