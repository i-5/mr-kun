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
 * <h3>��t�����v���t�B�[���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 03:53:44)
 * @author: 
 */
public class DrInitProfileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrPofile �R���X�g���N�^�[�E�R�����g�B
     */
    public DrInitProfileController() {
	
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
	
    }

    /**
     * <h3>��t�����v���t�B�[���\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 04:01:24)
     * @return jp.ne.sonet.medipro.mr.server.entity.BvUserProfileInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     */
    public BvUserProfileInfo createDisplay(HttpServletRequest req,
					   HttpServletResponse res,
					   String drID) {
	BvUserProfileInfo bvuserprofileinfo = null;
	
	DBConnect dbconnSyb = new DBConnect();
	Connection connSyb = dbconnSyb.getDBConnect();
	
	
	try {
	    BvUserProfileInfoManager bvuserprofilemanager
		= new BvUserProfileInfoManager(connSyb);
	    bvuserprofileinfo = bvuserprofilemanager.getBvUserProfile(drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(connSyb);
	}

	return bvuserprofileinfo;
    }

    /**
     * <h3>��t�����v���t�B�[���o�^</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 05:36:11)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void profileInput(HttpServletRequest req,
			     HttpServletResponse res,
			     DoctorInfo doctorinfo) {
	try {
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    doctorinfomanager.insert(doctorinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>��t�����v���t�B�[���X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 05:36:11)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void profileUpdate(HttpServletRequest req,
			      HttpServletResponse res,
			      DoctorInfo doctorinfo) {
	try {
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    doctorinfomanager.update(doctorinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
