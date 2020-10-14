package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class ShinryokaInfoManager {

    static final String SHINRYOKA_LIST_SQL
	= "SELECT"
	+ " shinryoka_cd"
	+ ",shinryoka_name"
	+ " FROM shinryoka"
	+ " ORDER BY shinryoka_cd";
    
    private Connection connection = null;

    public ShinryokaInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getShinryokaList() {
	Vector list = new Vector();

	try {
	    Statement stmt = null;

	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(SHINRYOKA_LIST_SQL);

		while (rs.next()) {
		    ShinryokaInfo info = new ShinryokaInfo();
		    info.shinryokaCd = rs.getString("shinryoka_cd");
		    info.shinryokaName = rs.getString("shinryoka_name");

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
