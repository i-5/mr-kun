package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class ShikakuInfoManager {

    static final String SHIKAKU_LIST_SQL
	= "SELECT"
	+ " shikaku_cd"
	+ ",shikaku_name"
	+ " FROM shikaku"
	+ " ORDER BY shikaku_cd";
    
    private Connection connection = null;

    public ShikakuInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getShikakuList() {
	Vector list = new Vector();

	try {
	    Statement stmt = null;

	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(SHIKAKU_LIST_SQL);

		while (rs.next()) {
		    ShikakuInfo info = new ShikakuInfo();
		    info.shikakuCd = rs.getString("shikaku_cd");
		    info.shikakuName = rs.getString("shikaku_name");

		    list.addElement(info);
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return list;
    }
    
}
