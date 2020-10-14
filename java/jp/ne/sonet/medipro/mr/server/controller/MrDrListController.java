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
 * <h3>顧客一覧管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 01:22:48)
 * @author: 
 */
public class MrDrListController {
    protected Connection conn;
    protected DBConnect dbconn;
    protected Hashtable actionTable;
    protected String defaultTargetRank;

    /**
     * MrDrLIstController コンストラクター・コメント。
     */
    public MrDrListController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();

	actionTable = new Hashtable();
    }

    /**
     * <h3>顧客一覧表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 01:23:20) 
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
		String companyCD = docManager.getCompanyCD(mrID);//1106 y-yamada add カンパニーコードを取得
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
     * 指定した担当関係から表示する信号機情報を取得する.
     */
    public String getSignal(HttpSession session, TantoInfo tantoInfo) {
	int lastOpenDay = tantoInfo.getLastOpenDay();
	int sendCount = tantoInfo.getSendCount();
	ActionInfo info = null;
	int threshold = 0;

	//ターゲットランクが設定されているなら、対応するアクション情報を取得
	if (tantoInfo.getTargetRank() != null) {
	    info = (ActionInfo)actionTable.get(tantoInfo.getTargetRank());
	    if (info != null) {
		threshold = info.getThreshold();
	    }
	}

	//該当するものが無い場合はDefaultのアクション情報を使用
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
     * 指定した担当関係から表示するメッセージを取得する.
     */
    public String getMessage(TantoInfo tantoInfo) {
	int lastOpenDay = tantoInfo.getLastOpenDay();
	int sendCount = tantoInfo.getSendCount();
	ActionInfo info = null;
	int threshold = 0;

	//ターゲットランクが設定されているなら、対応するアクション情報を取得
	if (tantoInfo.getTargetRank() != null) {
	    info = (ActionInfo)actionTable.get(tantoInfo.getTargetRank());
	    if (info != null) {
		threshold = info.getThreshold();
	    }
	}

	//該当するものが無い場合はDefaultのアクション情報を使用
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
