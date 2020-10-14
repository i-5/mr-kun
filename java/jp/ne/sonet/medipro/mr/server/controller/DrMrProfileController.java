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
 * <h3>MR�l���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 03:53:44)
 * @author: 
 */
public class DrMrProfileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrPofile �R���X�g���N�^�[�E�R�����g�B
     */
    public DrMrProfileController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�l�q�l���\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:01:24)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public TantoInfo createDisplay(HttpServletRequest req,
				   HttpServletResponse res,
				   String drID,
				   String mrID) {
	TantoInfo tantoinfo = null;
	
	try {
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
	    tantoinfo = tantomanager.getMrInfo( drID, mrID );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return tantoinfo;
    }

    /**
     * <h3>�l�q�o�^�폜</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void mrDelete(HttpServletRequest req,
			 HttpServletResponse res,
			 String drID,
			 String mrID) {
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfomanager.insertSentakuTourokuHist(drID, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>������ۑ�</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     * @param drMemo java.lang.String
     */
    public void mrSave(HttpServletRequest req,
		       HttpServletResponse res,
		       String drID,
		       String mrID,
		       String drMemo ) {
	try {
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
	    tantomanager.updateDrMemo(drID, mrID ,drMemo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
