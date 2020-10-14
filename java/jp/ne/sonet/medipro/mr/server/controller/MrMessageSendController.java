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
 * <h3>MSG�i�m�F�j��ʊǗ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 03:30:17)
 * @author: 
 */
public class MrMessageSendController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrMessageSend �R���X�g���N�^�[�E�R�����g�B
     */
    public MrMessageSendController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>���M</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 03:33:04)
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
