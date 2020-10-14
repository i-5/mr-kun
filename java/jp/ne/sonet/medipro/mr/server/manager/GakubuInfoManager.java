package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class GakubuInfoManager {

    static final String GAKUBU_LIST_SQL
	= "SELECT"
	+ " gakubu_cd"
	+ ",gakubu_name"
	+ " FROM gakubu"
	+ " ORDER BY gakubu_cd";
    
    private Connection connection = null;

    public GakubuInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * f—Ã‰Èî•ñˆê——‚ğæ“¾‚µ‚Ü‚·
     */
    public Vector getGakubuList() {
	Vector list = new Vector();

	try {
	    Statement stmt = null;

	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(GAKUBU_LIST_SQL);

		while (rs.next()) {
		    GakubuInfo info = new GakubuInfo();
		    info.gakubuCd = rs.getString("gakubu_cd");
		    info.gakubuName = rs.getString("gakubu_name");

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
