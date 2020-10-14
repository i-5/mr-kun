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
 * <h3>MR���M�ۊ�BOX�ꗗ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/17 �ߌ� 08:11:29)
 * @author: 
 */
public class MrSendSaveBoxListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrReciveSaveBoxListController �R���X�g���N�^�[�E�R�����g�B
     */
    public MrSendSaveBoxListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>���MMSG�ۊǈꗗ</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:31:36)
     * @return java.util.Enumeration (MsgInfo)
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
	    enum = msgmanager.getMrSendMsgList(mrID,
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
     * <h3>���ݔ�</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 08:33:00)
     * @param message_header_id java.lang.Enumeration
     */
    public void dustBox(HttpServletRequest req,
			HttpServletResponse res,
			Enumeration messageHeaderID) {
	try {
	    MsgManager msgmanager = new MsgManager(conn);
	    msgmanager.updateMrSendMsgStatus(messageHeaderID, SysCnst.MSG_STATUS_DUST); 
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
     * @param message_header_id java.lang.Enumeration
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
