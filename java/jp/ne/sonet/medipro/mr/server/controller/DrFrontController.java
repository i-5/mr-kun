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
 * <h3>�t�����g�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 04:31:22)
 * @author: 
 */
public class DrFrontController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * DrFrontController �R���X�g���N�^�[�E�R�����g�B
     */
    public DrFrontController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�t�����g�\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/17 �ߌ� 06:40:20)
     * @return jp.ne.sonet.medipro.mr.server.entity.FrontInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     */
    public FrontInfo createDisplay(HttpServletRequest req,
				   HttpServletResponse res,
				   String drID) {
	FrontInfo frontinfo = null;
	
	try {
	    FrontInfoManager frontmanager = new FrontInfoManager(conn);
	    frontinfo =frontmanager.getFrontInfo(drID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return frontinfo;
    }
}
