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
 * <h3>未読MSG一覧管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 04:58:54)
 * @author: 
 */
public class DrMessageListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrMessageList コンストラクター・コメント。
     */
    public DrMessageListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>未読メッセージの表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 06:40:20)
     * @return java.util.Enumeration (WaitingRoomInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param dr_id java.lang.String
     * @param mr_id java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String drID,
				     String mrID) {
	Enumeration enum = null;

	try {
	    WaitingRoomInfoManager waitingroominfomanager
		= new WaitingRoomInfoManager(conn);
	    enum = waitingroominfomanager.getWaitingRoomUnRead(drID,mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }
}
