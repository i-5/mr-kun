package jp.ne.sonet.medipro.mr.common.servlet;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;


/**
 * ユーザ環境取得・解析用モジュール.
 */
public class AccessAnalyzer {
    private String drId = null;

    public AccessAnalyzer(String drId) {
	this.drId = drId;
    }

    public void analyze(HttpServletRequest req) {

	AccessLogInfo info = new AccessLogInfo();
	info.setDrId(drId);

	Enumeration names = req.getHeaderNames();
	while (names.hasMoreElements()) {
	    String name = (String)names.nextElement();
	    if (name.toLowerCase().equals("user-agent")) {
		info.setUserAgent(req.getHeader(name));
		break;
	    }
	}

	DBConnect dbConnect = new DBConnect();
	Connection conn = dbConnect.getDBConnect();

	try {
	    conn.setAutoCommit(false);

	    try {
		AccessLogInfoManager manager = new AccessLogInfoManager(conn);
		manager.addLog(info);
		conn.commit();
	    } catch (SQLException e) {
		conn.rollback();
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	} finally {
	    dbConnect.closeDB(conn);
	}
    }
}
