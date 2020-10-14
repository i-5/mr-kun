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
 * <h3>フロント管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 04:31:22)
 * @author: 
 */
public class DrFrontController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrFrontController コンストラクター・コメント。
     */
    public DrFrontController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>フロント表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 06:40:20)
     * @return jp.ne.sonet.medipro.mr.server.entity.FrontInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     */
    public FrontInfo createDisplay(HttpServletRequest req,
				   HttpServletResponse res,
				   String drID) {
	FrontInfo frontinfo = null;
	
	try {
	    FrontInfoManager frontmanager = new FrontInfoManager(conn);
	    frontinfo =frontmanager.getFrontInfo(drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return frontinfo;
    }
}
