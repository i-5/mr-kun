package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;

import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

public class ActionInfoManager {
    /** 会社指定ターゲットランク一覧取得 */
    protected static final String ACTION_LIST_SQL
	= "SELECT "
	+ " target_rank"
	+ ",threshold"
	+ ",message1"
	+ ",message2"
	+ ",message3"
	+ ",message4"
	+ ",target_name"//1020 y-yamada add
	+ " FROM action, mr "
	+ " WHERE "
	+ " mr.mr_id = ?"
	+ " AND mr.company_cd = action.company_cd"
	+ " order by target_name";//1020 y-yamada addターゲットネームでソート

    
    protected Connection connection = null;

    public ActionInfoManager(Connection initConnection) {
	connection = initConnection;
    }

    /**
     * 指定した会社のターゲットランク毎のアクション情報一覧を取得する.
     * @param  companyCd 会社コード
     * @return アクション情報一覧
     */
    public Vector getActionList(String mrId) {
	Vector list = new Vector();

	try {
	    PreparedStatement pstmt = connection.prepareStatement(ACTION_LIST_SQL);
	    pstmt.setString(1, mrId);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		ActionInfo info = new ActionInfo();
		info.setTargetRank(rs.getString("target_rank"));
		info.setThreshold(rs.getInt("threshold"));
		info.setMessage1(rs.getString("message1"));
		info.setMessage2(rs.getString("message2"));
		info.setMessage3(rs.getString("message3"));
		info.setMessage4(rs.getString("message4"));
		info.setTargetName(rs.getString("target_name"));//1020 y-yamada add

		list.addElement(info);
	    }
	    
	    pstmt.close();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return list;
    }
}
