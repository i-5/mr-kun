package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.Vector;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class ShicyosonInfoManager {

    static final String SHICYOSON_LIST_SQL
	= "SELECT"
	+ " shicyoson_cd"
	+ ",shicyoson_name"
	+ " FROM shicyoson"
	+ " WHERE todofuken_cd = ?"
	+ " ORDER BY todofuken_cd, shicyoson_cd";
    
    private Connection connection = null;

    public ShicyosonInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * êfó√â»èÓïÒàÍóóÇéÊìæÇµÇ‹Ç∑
     */
    public Vector getShicyosonList(String todofuken) {
	Vector list = new Vector();

	try {
	    PreparedStatement pstmt = null;

	    try {
		pstmt = connection.prepareStatement(SHICYOSON_LIST_SQL);
		pstmt.setString(1, todofuken);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    ShicyosonInfo info = new ShicyosonInfo();
		    info.shicyosonCd = rs.getString("shicyoson_cd");
		    info.shicyosonName = rs.getString("shicyoson_name");

		    list.addElement(info);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return list;
    }
    
}
