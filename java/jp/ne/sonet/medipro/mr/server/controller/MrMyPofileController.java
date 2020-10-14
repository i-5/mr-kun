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
 * <h3>���ȏ��ݒ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 04:04:02)
 * @author: 
 */
public class MrMyPofileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrMyPofile �R���X�g���N�^�[�E�R�����g�B
     */
    public MrMyPofileController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>���ȏ��ݒ�\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:05:02)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
     */
    public MrInfo createDisplay(HttpServletRequest req,
				HttpServletResponse res,
				String mrID) {
	MrInfo mrinfo = null;
	
	try {
	    MrInfoManager mrmanager = new MrInfoManager(conn);
	    mrinfo = mrmanager.getMrInfo(mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return mrinfo;
    }

    /**
     * <h3>�ۑ�</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:05:44)
     * @param  jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
     */
    public void saveProfile(HttpServletRequest req,
			    HttpServletResponse res,
			    MrInfo mrinfo,
			    String mrID) {
	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    mrinfomanager.updateMrProfile(mrinfo, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
