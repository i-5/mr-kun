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
 * <h3>�ڋq�ꗗ�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 �ߌ� 01:22:48)
 * @author: 
 */
public class MrDrListController {
    protected Connection conn;
    protected DBConnect dbconn;
    protected Hashtable actionTable;
    protected String defaultTargetRank;

    /**
     * MrDrLIstController �R���X�g���N�^�[�E�R�����g�B
     */
    public MrDrListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();

	actionTable = new Hashtable();
    }

    /**
     * <h3>�ڋq�ꗗ�\��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 �ߌ� 01:23:20) 
     * @return java.util.Enumeration (TantoInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
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
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
		DoctorInfoManager docManager = new DoctorInfoManager(conn);//1106 y-yamada add 
		String companyCD = docManager.getCompanyCD(mrID);//1106 y-yamada add �J���p�j�[�R�[�h���擾
//System.out.println("companyCD="+companyCD);
	    enum = tantomanager.getDrMsgInfo(mrID, sortKey, rowType, companyCD);// 1106 y-yamada add
	    //enum = tantomanager.getDrMsgInfo(mrID, sortKey, rowType);

	    ActionInfoManager actionManager = new ActionInfoManager(conn);
	    Enumeration actionList = actionManager.getActionList(mrID).elements();
	    while (actionList.hasMoreElements()) {
		ActionInfo info = (ActionInfo)actionList.nextElement();
		actionTable.put(info.getTargetRank(), info);
	    }

	    CompanyTableManager companyManager = new CompanyTableManager(conn);
	    defaultTargetRank = companyManager.getTargetRank(mrID);
	} catch (MrException e) {
	    e.printStackTrace();
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * �w�肵���S���֌W����\������M���@�����擾����.
     */
    public String getSignal(HttpSession session, TantoInfo tantoInfo) {
	int lastOpenDay = tantoInfo.getLastOpenDay();
	int sendCount = tantoInfo.getSendCount();
	ActionInfo info = null;
	int threshold = 0;

	//�^�[�Q�b�g�����N���ݒ肳��Ă���Ȃ�A�Ή�����A�N�V���������擾
	if (tantoInfo.getTargetRank() != null) {
	    info = (ActionInfo)actionTable.get(tantoInfo.getTargetRank());
	    if (info != null) {
		threshold = info.getThreshold();
	    }
	}

	//�Y��������̂������ꍇ��Default�̃A�N�V���������g�p
//  	if (tantoInfo.getTargetRank() == null || info == null) {
//  	    info = (ActionInfo)actionTable.get("DEFAULT");
//  	}

	String signalImage = new String();

	if (info == null) {
	    signalImage = (String)session.getValue("com_actionPicDefault");
	} else if (lastOpenDay >= threshold && sendCount == 0) {
	    signalImage = (String)session.getValue("com_actionPic1");
	} else if (lastOpenDay == -1 && sendCount == 0) {	//20010607 Mizuki
	    signalImage = (String)session.getValue("com_actionPic1");
	} else if (lastOpenDay >= threshold && sendCount > 0) {
	    signalImage = (String)session.getValue("com_actionPic2");
	} else if (lastOpenDay == -1 && sendCount > 0) {	//20010607 Mizuki
	    signalImage = (String)session.getValue("com_actionPic2");
	} else if (lastOpenDay < threshold && sendCount == 0) {
	    signalImage = (String)session.getValue("com_actionPic3");
	} else if (lastOpenDay < threshold && sendCount > 0) {
	    signalImage = (String)session.getValue("com_actionPic4");
	}
	
	return signalImage;
    }

    /**
     * �w�肵���S���֌W����\�����郁�b�Z�[�W���擾����.
     */
    public String getMessage(TantoInfo tantoInfo) {
	int lastOpenDay = tantoInfo.getLastOpenDay();
	int sendCount = tantoInfo.getSendCount();
	ActionInfo info = null;
	int threshold = 0;

	//�^�[�Q�b�g�����N���ݒ肳��Ă���Ȃ�A�Ή�����A�N�V���������擾
	if (tantoInfo.getTargetRank() != null) {
	    info = (ActionInfo)actionTable.get(tantoInfo.getTargetRank());
	    if (info != null) {
		threshold = info.getThreshold();
	    }
	}

	//�Y��������̂������ꍇ��Default�̃A�N�V���������g�p
//  	if (tantoInfo.getTargetRank() == null || info == null) {
//  	    info = (ActionInfo)actionTable.get("DEFAULT");
//  	}

	String message = new String();

	if (info == null) {
	    info = (ActionInfo)actionTable.get(defaultTargetRank);
	    if (info != null) {
		message = info.getMessage1();
	    }
	} else if (lastOpenDay >= threshold && sendCount == 0) {
	    message = info.getMessage1();
	} else if (lastOpenDay == -1 && sendCount == 0) {	//20010607 Mizuki
	    message = info.getMessage1();
	} else if (lastOpenDay >= threshold && sendCount > 0) {
	    message = info.getMessage2();
	} else if (lastOpenDay == -1 && sendCount > 0) {	//20010607 Mizuki
	    message = info.getMessage2();
	} else if (lastOpenDay < threshold && sendCount == 0) {
	    message = info.getMessage3();
	} else if (lastOpenDay < threshold && sendCount > 0) {
	    message = info.getMessage4();
	}
	
	return message;
    }
}
