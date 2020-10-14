package jp.ne.sonet.medipro.wm.common.servlet;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.manager.*;


/**
 * ユーザ環境取得・解析用モジュール.
 */
public class AccessWmAnalyzer {
    private String wmid = null;

    public AccessWmAnalyzer(String wmid) {
	this.wmid = wmid;
    }

    public void analyze(HttpServletRequest req) {

	AccessWmLogInfo info = new AccessWmLogInfo();
	info.setWmId(wmid);

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
		AccessWmLogInfoManager manager = new AccessWmLogInfoManager(conn);
		manager.addLog(info);
		conn.commit();
	    } catch (SQLException e) {
		conn.rollback();
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	} finally {
	    dbConnect.closeDB(conn);
	}
    }
}
