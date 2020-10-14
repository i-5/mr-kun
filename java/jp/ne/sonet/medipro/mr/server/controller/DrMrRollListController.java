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
 * <h3>MR����ꗗ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 03:57:17)
 * @author: 
 */
public class DrMrRollListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrRollList �R���X�g���N�^�[�E�R�����g�B
     */
    public DrMrRollListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�m�F</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 05:36:51)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param liftmrID java.util.Enumeration
     * @param rightmrID java.util.Enumeration
     */
    public void confirm(HttpServletRequest req,
			HttpServletResponse res,
			String drID,
			String liftmrID,
			String rightmrID) {
	try {
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
	    tantomanager.updateSentaku( drID, liftmrID, rightmrID );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>�l�q����ꗗ</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 03:59:33)
     * @return java.util.Enumeration (TantoInfo)
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
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    enum = tantoinfomanager.getMrInfo(drID, sortKey, rowType);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }
}
