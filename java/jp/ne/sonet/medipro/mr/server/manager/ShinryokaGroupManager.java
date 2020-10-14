package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;
import java.sql.Date;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.server.entity.*;


public class ShinryokaGroupManager {

	//SINRYOKA_GROUP取得用SQL
	static final String SINRYOKA_GROUP_GET_SQL
		= "SELECT"
		+ " ka.shinryoka_group shinryoka_group"
		+ ",gr.group_name group_name"
		+ " FROM"
		+ " shinryoka ka"
		+ ",shinryoka_group gr"
		+ " WHERE ka.shinryoka_group = gr.shinryoka_group"
		+ " AND ka.shinryoka_cd = ?";


    private String group_name = null;

    protected Connection connection = null;

    public ShinryokaGroupManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     *　診療科グループを取得します。
     */
    public String getShinryokaGroup(String ShinryokaCd) throws MrException {
	PreparedStatement pstmt = null;
	String group_cd = null;

	try {
	    try {
		pstmt = connection.prepareStatement(SINRYOKA_GROUP_GET_SQL);

		pstmt.setString(1, ShinryokaCd);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    group_name = rs.getString("group_name");
		    group_cd = rs.getString("shinryoka_group");
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return group_cd;
    }

    /**
     *　診療科グループ名を返します。
     */
    public String getGroupName() {
	return group_name;
    }

}

