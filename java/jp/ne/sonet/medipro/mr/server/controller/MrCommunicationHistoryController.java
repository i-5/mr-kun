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
 * <h3>�R�~���j�P�[�V���������Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 01:29:06)
 * @author: 
 */
public class MrCommunicationHistoryController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrCommunicationHistoryController �R���X�g���N�^�[�E�R�����g�B
     */
    public MrCommunicationHistoryController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�R�~���j�P�[�V���������̕\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 01:29:57)
     * @return java.util.Enumeration (MsgInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
     * @param dr_id java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String mrID,
				     String drID,
				     String sortKey,
				     String rowType) {
	Enumeration enum = null;
	
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    enum = msgmanager.getMrSendRecvMsgList(mrID, drID, sortKey, rowType);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * <h3>���ݔ�</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 01:32:43)
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
	    msgmanager.updateMrSendMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_DUST); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>�ۊǂa�n�w</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 01:32:20)
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
	    msgmanager.updateMrSendMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_SAVE); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>��t�����ǂ̑��M���</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 01:31:41)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param messageHeaderID java.lang.Enumeration
     */
    public void sendCancel(HttpServletRequest req,
			   HttpServletResponse res,
			   Enumeration messageHeaderID) {
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgmanager.updateMrSendCancel(messageHeaderID); 
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
