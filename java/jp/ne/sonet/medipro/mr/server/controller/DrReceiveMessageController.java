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
 * <h3>医師個別受信MSG管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/17 午後 08:35:52)
 * @author: 
 */
public class DrReceiveMessageController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReceiveMessage コンストラクター・コメント。
     */
    public DrReceiveMessageController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>個別受信MSG表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:37:30)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param message_header_id java.lang.String
     */
    public MsgInfo createDisplay(HttpServletRequest req,
				 HttpServletResponse res,
				 String messageHeaderID,
				 String drId) {
	MsgInfo msginfo = null;
	int updateCount = 0;
	
	try{
	    MsgManager msgmanager = new MsgManager(conn);
	    updateCount = msgmanager.updateRecvMsg(messageHeaderID);
	    msginfo = msgmanager.getDrRecvMessage(messageHeaderID);

	    //未読メッセージを読んだ場合
	    //MsgManager#updateRecvMsgが未読を既読に実際に変更したとき
	    if (updateCount > 0) {
		MrInfoManager mrManager = new MrInfoManager(conn);
		MrInfo mrInfo = mrManager.getMrInfo(msginfo.getHeader().getFromUserID());

		if (!mrInfo.getCompanyCD().equals("0000000000") &&
		    !mrInfo.getCompanyCD().equals("0000000010") &&
		    !mrInfo.getCompanyCD().equals("0000000012")) {  //0000000012にもポイントを追加しない y-yamada add 1013
		    DoctorInfoManager drManager = new DoctorInfoManager(conn);
		    drManager.updatePoint(drId);
		}

		sendReceivedMessage(msginfo);
	    }

	    TotatsuCallLogTable totatsucalltable = new TotatsuCallLogTable();
	    totatsucalltable.setFromUserID(msginfo.getHeader().getFromUserID());
	    totatsucalltable.setToUserID(msginfo.getHeader().getToUserID());
	    totatsucalltable.setMessageHeaderID(msginfo.getHeader().getMessageHeaderID());
	    //totatsucalltable.setPictureCD(null);
	    TotatsuCallLogTableManager totatsucalltablemanager =
		new TotatsuCallLogTableManager(conn);
	    totatsucalltablemanager.insert(totatsucalltable);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
	
	return msginfo;

    }

    void sendReceivedMessage(MsgInfo msgInfo) {
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	MessageTableManager manager = new MessageTableManager(connection);
	MessageTable message = new MessageTable();
	MessageHeaderTable header = new MessageHeaderTable();
	MessageBodyTable body = new MessageBodyTable();

	TantoInfoManager tantoManager = new TantoInfoManager(connection);
	TantoInfo tantoInfo = tantoManager.getDrInfo(msgInfo.getHeader().getFromUserID(),
						     msgInfo.getHeader().getToUserID());
	
	if (tantoInfo != null) {
	    header.setMessageKbn(SysCnst.MESSAGE_KBN_TO_OTHER);
	    header.setFromUserID(msgInfo.getHeader().getToUserID());
	    body.setTitle("開封通知:" + tantoInfo.getName());
	    body.setMessageHonbun(msgInfo.getBody().getHonbunText());

	    message.setMsgHTable(header);
	    message.setMsgBTable(body);

	    Vector toList = new Vector();
	    toList.addElement(msgInfo.getHeader().getFromUserID());

	    //-- 添付・リンクファイルセット（内容カラ）
	    Vector attach = new Vector();
	    message.setAttachFTable(attach.elements());
	    message.setAttachLTable(attach.elements());

	    manager.insert(toList.elements(), message);
	}

	dbConnect.closeDB(connection);
    }

    /**
     * <h3>前の未読MSG</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:48:06)
     * @return java.lang.String
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String lastMessage(HttpServletRequest req,
			      HttpServletResponse res,
			      String drID,
			      String messageHeaderID) {
	String msgheaderid = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgheaderid = msgmanager.getLastRecvMsg(drID, messageHeaderID);
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
     * <h3>次の未読MSG</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:51:11)
     * @return java.lang.String
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String nextMessage(HttpServletRequest req,
			      HttpServletResponse res,
			      String drID,
			      String messageHeaderID) {
	String msgheaderid = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgheaderid = msgmanager.getNextRecvMsg(drID, messageHeaderID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
//System.out.println("次へ　古い="+msgheaderid);	
	return msgheaderid;
    }

    /**
     * <h3>送信</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:55:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.util.Enumeration
     * @param messagetable jp.ne.sonet.medipro.mr.server.entity.MessageTable
     */
    public void replyMessage(HttpServletRequest req,
			     HttpServletResponse res,
			     Enumeration mrID,
			     MessageTable messagetable)
	{
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
