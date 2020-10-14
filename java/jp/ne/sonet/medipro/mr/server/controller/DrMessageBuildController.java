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
 * <h3>MRへMSG送信管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 05:05:58)
 * @author: 
 */
public class DrMessageBuildController extends MrDrListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrMessageBuildController コンストラクター・コメント。
     */
    public DrMessageBuildController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>MSG作成表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 06:40:20)
     * @return java.util.Enumeration (TantoInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String drID,
				     String sortKey,
				     String rowType) {
	Enumeration enum = null;
		
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    enum = tantoinfomanager.getMrInfo(drID, sortKey, rowType);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * <h3>送信</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 05:11:22)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.util.Enumeration
     * @param messagetable jp.ne.sonet.medipro.mr.server.entity.MessageTable
     */
    public void sendMessage(HttpServletRequest req,
			    HttpServletResponse res,
			    Enumeration mrID,
			    MessageTable messagetable) {
	try {
	    MessageTableManager messagetablemanager = new MessageTableManager(conn);
	    messagetablemanager.insert(mrID, messagetable );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
