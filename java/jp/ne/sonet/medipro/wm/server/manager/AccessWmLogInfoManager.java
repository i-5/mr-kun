package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.entity.*;

public class AccessWmLogInfoManager {
    static final String INSERT_LOG
	= "INSERT INTO loginmr ("
	+ "logintime "
	+ ",mr_id "
	+ ",gamen "
	+ ",user_agent "
	+ ") VALUES ("
	+ "SYSDATE"
	+ ",?"
	+ ", '2' "//WMâÊñ ÇÃèÍçáÇÕ2
	+ ",?"
	+ ")";
	
    protected Connection conn = null;

    public AccessWmLogInfoManager(Connection initConn) {
	this.conn = initConn;
    }

    public int addLog(AccessWmLogInfo info) {
	int count = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG);

	    try {
		pstmt.setString(1, info.getWmId());
		pstmt.setString(2, info.getUserAgent());
		
		count = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}
	    
	return count;
    }
}
