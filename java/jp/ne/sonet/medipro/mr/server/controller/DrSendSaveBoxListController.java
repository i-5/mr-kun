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
 * <h3>医師送信保管BOX一覧管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/17 午後 08:11:29)
 * @author: 
 */
public class DrSendSaveBoxListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReciveSaveBoxListController コンストラクター・コメント。
     */
    public DrSendSaveBoxListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>送信MSG保管表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:31:36)
     * @return java.util.Enumeration (MsgInfo)
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
	    MsgManager msgmanager = new MsgManager(conn);
	    enum = msgmanager.getDrSendMsgList(drID,
					       sortKey,
					       rowType,
					       SysCnst.MSG_STATUS_SAVE);
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
     * 作成日 : (00/06/17 午後 08:33:00)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param messageHeaderID java.lang.Enumeration
     */
    public void dustBox(HttpServletRequest req,
			HttpServletResponse res,
			Enumeration messageHeaderID) {
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgmanager.updateDrSendMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_DUST); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
