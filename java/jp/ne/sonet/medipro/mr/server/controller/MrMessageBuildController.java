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
 * <h3> MSG�i�摜�E�^�C�g���j��ʊǗ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 02:10:44)
 * @author: 
 */
public class MrMessageBuildController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrMessageBuild1Controller �R���X�g���N�^�[�E�R�����g�B
     */
    public MrMessageBuildController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>MSG�쐬�̕\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 02:14:20)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     */
    public MrInfo createDisplay(HttpServletRequest req,
				HttpServletResponse res,
				String mrID) {
	MrInfo mrinfo = null; 

	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    mrinfo = mrinfomanager.getMrSpInfo(mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return mrinfo;
    }

    /**
     * <h3>�S����t���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/06 �ߌ� 02:15:04)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public TantoInfo getDrInfo(HttpServletRequest req,
			       HttpServletResponse res,
			       String mrID,
			       String drID) {
	TantoInfo tantoinfo = null; 

	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfo = tantoinfomanager.getDrInfo(mrID,drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return tantoinfo;
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/06 �ߌ� 04:59:29)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mrID java.lang.String
     * @param drID java.lang.String
     */
    public MsgInfo getNewSendMsg(HttpServletRequest req,
				 HttpServletResponse res,
				 String mrID,
				 String drID) {
	MsgInfo msginfo = null; 

	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    String message_header_id = msgmanager.getNewSendMsg(mrID,drID);
	    msginfo = msgmanager.getMrSendMessage(message_header_id);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return msginfo;
    }
}
