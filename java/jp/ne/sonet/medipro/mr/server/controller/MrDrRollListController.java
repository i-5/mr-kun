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
 * <h3>�ڋq����ꗗ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 03:57:17)
 * @author: 
 */
public class MrDrRollListController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrRollList �R���X�g���N�^�[�E�R�����g�B
     */
    public MrDrRollListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>�ڋq����ꗗ�\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 03:59:33)
     * @return java.util.Enumeration (TantoInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
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
		DoctorInfoManager docManager = new DoctorInfoManager(conn);//1106 y-yamada add 
		String companyCD = docManager.getCompanyCD(mrID);//1106 y-yamada add �J���p�j�[�R�[�h���擾
	    TantoInfoManager tanmanager = new TantoInfoManager(conn);
	    enum = tanmanager.getDrInfo(mrID,sortKey,rowType , companyCD);//1106 y-yamada add
//	    enum = tanmanager.getDrInfo(mrID,sortKey,rowType);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }
}
