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
 * <h3>��t�ʎ�MMSG�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/17 �ߌ� 08:35:52)
 * @author: 
 */
public class DrReceiveMessageController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReceiveMessage �R���X�g���N�^�[�E�R�����g�B
     */
    public DrReceiveMessageController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�ʎ�MMSG�\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:37:30)
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

	    //���ǃ��b�Z�[�W��ǂ񂾏ꍇ
	    //MsgManager#updateRecvMsg�����ǂ����ǂɎ��ۂɕύX�����Ƃ�
	    if (updateCount > 0) {
		MrInfoManager mrManager = new MrInfoManager(conn);
		MrInfo mrInfo = mrManager.getMrInfo(msginfo.getHeader().getFromUserID());

		if (!mrInfo.getCompanyCD().equals("0000000000") &&
		    !mrInfo.getCompanyCD().equals("0000000010") &&
		    !mrInfo.getCompanyCD().equals("0000000012")) {  //0000000012�ɂ��|�C���g��ǉ����Ȃ� y-yamada add 1013
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
	    body.setTitle("�J���ʒm:" + tantoInfo.getName());
	    body.setMessageHonbun(msgInfo.getBody().getHonbunText());

	    message.setMsgHTable(header);
	    message.setMsgBTable(body);

	    Vector toList = new Vector();
	    toList.addElement(msgInfo.getHeader().getFromUserID());

	    //-- �Y�t�E�����N�t�@�C���Z�b�g�i���e�J���j
	    Vector attach = new Vector();
	    message.setAttachFTable(attach.elements());
	    message.setAttachLTable(attach.elements());

	    manager.insert(toList.elements(), message);
	}

	dbConnect.closeDB(connection);
    }

    /**
     * <h3>�O�̖���MSG</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:48:06)
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
//System.out.println("�O�֐V����="+msgheaderid);	
	return msgheaderid;
    }

    /**
     * <h3>���̖���MSG</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:51:11)
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
//System.out.println("���ց@�Â�="+msgheaderid);	
	return msgheaderid;
    }

    /**
     * <h3>���M</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:55:07)
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
