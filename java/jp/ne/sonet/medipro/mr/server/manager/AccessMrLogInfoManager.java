package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class AccessMrLogInfoManager {
    static final String INSERT_LOG
	= "INSERT INTO loginmr ("
	+ "logintime "
	+ ",mr_id "
	+ ",gamen "
	+ ",user_agent "
	+ ") VALUES ("
	+ "SYSDATE"
	+ ",?"
	+ ", '1' "
	+ ",?"
	+ ")";
	
    protected Connection conn = null;

    public AccessMrLogInfoManager(Connection initConn) {
	this.conn = initConn;
    }

    public int addLog(AccessMrLogInfo info) {
	int count = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG);

	    try {
		pstmt.setString(1, info.getMrId());
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
