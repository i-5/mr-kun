package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class GengoInfoManager {

    static final String GENGO_LIST_SQL
	= "SELECT"
	+ " gengo_cd"
	+ ",gengo_name"
	+ " FROM gengo"
	+ " ORDER BY gengo_cd";

    static final String CONVERT_YEAR_SQL
	= "SELECT"
	+ " to_number(start_year) + to_number(?) - 1"
	+ " FROM gengo"
	+ " WHERE"
	+ " gengo_cd = ?";
    
    private Connection connection = null;

    public GengoInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getGengoList() {
	Vector list = new Vector();

	try {
	    Statement stmt = null;

	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(GENGO_LIST_SQL);

		while (rs.next()) {
		    GengoInfo info = new GengoInfo();
		    info.gengoCd = rs.getString("gengo_cd");
		    info.gengoName = rs.getString("gengo_name");

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

    public String getYearAD(String gengo, String year) {
	String yearAD = null;

	try {
	    PreparedStatement pstmt = null;
	    try {
		pstmt = connection.prepareStatement(CONVERT_YEAR_SQL);
		pstmt.setString(1, year);
		pstmt.setString(2, gengo);

		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) {
		    yearAD = rs.getString(1);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return yearAD;
    }
    
}
