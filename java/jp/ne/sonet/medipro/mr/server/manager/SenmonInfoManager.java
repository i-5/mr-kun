package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class SenmonInfoManager {

    static final String SENMON_LIST_SQL
	= "SELECT"
	+ " senmon_cd"
	+ ",senmon_name"
	+ " FROM senmon"
	+ " ORDER BY senmon_cd";
    
    private Connection connection = null;

    public SenmonInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getSenmonList() {
	Vector list = new Vector();

	try {
	    Statement stmt = null;

	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(SENMON_LIST_SQL);

		while (rs.next()) {
		    SenmonInfo info = new SenmonInfo();
		    info.senmonCd = rs.getString("senmon_cd");
		    info.senmonName = rs.getString("senmon_name");

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
