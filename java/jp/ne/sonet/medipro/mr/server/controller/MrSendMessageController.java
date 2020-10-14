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
 * <h3>MR�ʑ��MMSG�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/17 �ߌ� 08:35:52)
 * @author: 
 */
public class MrSendMessageController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReceiveMessage �R���X�g���N�^�[�E�R�����g�B
     */
    public MrSendMessageController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�ʑ��MMSG�\��</h3>
     * 	
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:37:30)
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
     * <h3>�O��MSG</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:48:06)
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
//System.out.println("�O�֐V����="+msgheaderid);	
	return msgheaderid;
    }

    /**
     * <h3>����MSG</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:51:11)
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
//System.out.println("���ց@�Â�="+msgheaderid);	

	return msgheaderid;
    }

}
