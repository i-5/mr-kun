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
 * <h3>MSG（確認）画面管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:30:17)
 * @author: 
 */
public class MrMessageSendController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrMessageSend コンストラクター・コメント。
     */
    public MrMessageSendController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>送信</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 03:33:04)
     * @return java.lang.String
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.Util.Enumeration
     * @param messagetable jp.ne.sonet.medipro.mr.server.entity.MessageTable
     */
    public String sendMessage(HttpServletRequest req,
			      HttpServletResponse res,
			      Enumeration drID,
			      MessageTable messagetable) {
	String messageheaderid = null;
	
	try {
	    MessageTableManager messagetablemanager = new MessageTableManager(conn);
	    messageheaderid = messagetablemanager.insert(drID, messagetable );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return messageheaderid;
    }
}
