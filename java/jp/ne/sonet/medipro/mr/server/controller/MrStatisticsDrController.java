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
 * <h3>統計情報個別管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:48:42)
 * @author: 
 */
public class MrStatisticsDrController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrStatisticsDr コンストラクター・コメント。
     */
    public MrStatisticsDrController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>統計分析-顧客別表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:31:36)
     * @return java.util.Enumeration
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
     * @param sortKey java.lang.String
     * @param row_type java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String mr_id,
				     String sortKey,
				     String rowType) {
	Enumeration statisticsdrinfolist = null;
	
	try {
		
	    StatisticsDrInfoManager statisticsdrinfomanager
		= new StatisticsDrInfoManager(conn);
		DoctorInfoManager docManager = new DoctorInfoManager(conn);//1106 y-yamada add 
		String companyCD = docManager.getCompanyCD(mr_id);//1106 y-yamada add カンパニーコードを取得
	    statisticsdrinfolist
		= statisticsdrinfomanager.getSatisticsDrInfo(mr_id, sortKey, rowType , companyCD);//1106 y-yamada add
		//= statisticsdrinfomanager.getSatisticsDrInfo(mr_id, sortKey, rowType);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return statisticsdrinfolist;
    }

}
