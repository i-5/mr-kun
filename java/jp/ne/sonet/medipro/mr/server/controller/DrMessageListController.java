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
 * <h3>����MSG�ꗗ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 04:58:54)
 * @author: 
 */
public class DrMessageListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrMessageList �R���X�g���N�^�[�E�R�����g�B
     */
    public DrMessageListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>���ǃ��b�Z�[�W�̕\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 06:40:20)
     * @return java.util.Enumeration (WaitingRoomInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param dr_id java.lang.String
     * @param mr_id java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String drID,
				     String mrID) {
	Enumeration enum = null;

	try {
	    WaitingRoomInfoManager waitingroominfomanager
		= new WaitingRoomInfoManager(conn);
	    enum = waitingroominfomanager.getWaitingRoomUnRead(drID,mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }
}
