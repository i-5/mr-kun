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
 * <h3>統計情報全体管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:43:14)
 * @author: 
 */
public class MrStatisticsController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrStatistics コンストラクター・コメント。
     */
    public MrStatisticsController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>統計分析-全体表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/17 午後 08:31:36)
     * @return jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param mr_id java.lang.String
     */
    public StatisticsInfo createDisplay(HttpServletRequest req,
					HttpServletResponse res,
					String mr_id) {
	StatisticsInfo statisticsinfo = null;

	try{
	    StatisticsInfoManager statisticsinfomanager
		= new StatisticsInfoManager(conn);
	    statisticsinfo = statisticsinfomanager.getStatisticsInfo(mr_id);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return statisticsinfo;
    }
}
