package jp.ne.sonet.medipro.mr.server.controller;

import java.util.*;
import java.sql.*;
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
 * <h3>MR受信一覧管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/17 午後 06:19:26)
 * @author: 
 */
public class MrReceiveListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * ReceiveListController コンストラクター・コメント。
     */
    public MrReceiveListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>受信MSG一覧表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 06:40:20)
     * @return java.util.Enumeration (MsgInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String mrID,
				     String sortKey,
				     String rowType) {
	Enumeration enum = null;
		
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    enum = msgmanager.getMrRecvMsgList(mrID,
					       sortKey,
					       rowType,
					       SysCnst.MSG_STATUS_NOMAL);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
		
	return enum;
    }
	
    /**
     * <h3>ごみ箱</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 07:14:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param messageHeaderID java.lang.Enumeration
     */
    public void dustBox(HttpServletRequest req,
			HttpServletResponse res,
			Enumeration messageHeaderID) {
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgmanager.updateMrRecvMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_DUST); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}

	dbconn.closeDB(conn);
    }

    /**
     * <h3>保管BOX</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 07:02:23)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param messageHeaderID java.lang.Enumeration
     */
    public void saveBox(HttpServletRequest req,
			HttpServletResponse res,
			Enumeration messageHeaderID) {
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgmanager.updateMrRecvMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_SAVE); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
