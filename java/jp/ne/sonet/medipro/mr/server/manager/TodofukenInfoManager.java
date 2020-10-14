package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class TodofukenInfoManager {

    static final String TODOFUKEN_LIST_SQL
	= "SELECT"
	+ " todofuken_cd"
	+ ",todofuken_name"
	+ " FROM todofuken"
	+ " ORDER BY todofuken_cd";
    
    private Connection connection = null;

    public TodofukenInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getTodofukenList() {
	Vector list = new Vector();

	try {
	    Statement stmt = connection.createStatement();

	    try {
		ResultSet rs = stmt.executeQuery(TODOFUKEN_LIST_SQL);

		while (rs.next()) {
		    TodofukenInfo info = new TodofukenInfo();
		    info.todofukenCd = rs.getString("todofuken_cd");
		    info.todofukenName = rs.getString("todofuken_name");

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
