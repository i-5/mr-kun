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
 * <h3>MR個別送信MSG管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/17 午後 08:35:52)
 * @author: 
 */
public class MrSendMessageController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReceiveMessage コンストラクター・コメント。
     */
    public MrSendMessageController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>個別送信MSG表示</h3>
     * 	
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:37:30)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param messageHeaderID java.lang.String
     */
    public MsgInfo createDisplay(HttpServletRequest req,
				 HttpServletResponse res,
				 String messageHeaderID) {
	MsgInfo msginfo = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msginfo = msgmanager.getMrSendMessage(messageHeaderID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return msginfo;
    }

    /**
     * <h3>前のMSG</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:48:06)
     * @return java.lang.String
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String lastMessage(HttpServletRequest req,
			      HttpServletResponse res,
			      String mrID,
			      String messageHeaderID) {
	String msgheaderid = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgheaderid = msgmanager.getMrLastSendMsg(mrID, messageHeaderID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
//System.out.println("前へ新しい="+msgheaderid);	
	return msgheaderid;
    }

    /**
     * <h3>次のMSG</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:51:11)
     * @return java.lang.String
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String nextMessage(HttpServletRequest req,
			      HttpServletResponse res,
			      String mrID,
			      String messageHeaderID) {
			      
	String msgheaderid = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgheaderid = msgmanager.getMrNextSendMsg(mrID, messageHeaderID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
//System.out.println("次へ　古い="+msgheaderid);	

	return msgheaderid;
    }

}
