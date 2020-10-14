package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class AccessLogInfoManager {
    static final String INSERT_LOG
	= "INSERT INTO logindr ("
	+ "logintime "
	+ ",dr_id "
	+ ",user_agent"
	+ ") VALUES ("
	+ "SYSDATE "
	+ ",?"
	+ ",?"
	+ ")";
	
    protected Connection conn = null;

    public AccessLogInfoManager(Connection initConn) {
	this.conn = initConn;
    }

    public int addLog(AccessLogInfo info) {
	int count = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG);

	    try {
		pstmt.setString(1, info.getDrId());
		pstmt.setString(2, info.getUserAgent());
		
		count = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}
	    
	return count;
    }
}
