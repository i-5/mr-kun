package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.*;
import java.util.*;

import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;

public class ActionInfoManager {
    protected Connection conn;

    //アクションマスタから一覧を取得
    static final String TARGET_RANK_LIST_SQL
	= "SELECT"
	+ " target_name"   //1018 y-yamada add 
	+ ",target_rank"
	+ " FROM action"
	+ " WHERE"
	+ " company_cd = ?";


    //指定したアクション情報を取得
    static final String ACTION_INFO_SQL
	= "SELECT"
	+ " company_cd"
	+ ",target_rank"
	+ ",threshold"
	+ ",message1"
	+ ",message2"
	+ ",message3"
	+ ",message4"
	+ ",target_name"  //y-yamada add 1017 
	+ " FROM action"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND target_rank = ?";

    //アクション情報を更新
    static final String ACTION_UPDATE_SQL
	= "UPDATE action SET"
	+ " target_rank = ?"
	+ ",threshold = ?"
	+ ",message1 = ?"
	+ ",message2 = ?"
	+ ",message3 = ?"
	+ ",message4 = ?"
	+ ",update_userid = ?"
	+ ",update_time = SYSDATE"
	+ ",target_name = ?"  //y-yamada add 1017 
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND target_rank = ?";

    //アクション情報を追加(未使用)
    static final String ACTION_INSERT_SQL
	= "INSERT INTO action ("
	+ " target_rank"
	+ ",company_cd"
	+ ",threshold"
	+ ",message1"
	+ ",message2"
	+ ",message3"
	+ ",message4"
	+ ",update_userid"
	+ ",update_time"
	+ ",target_name" // 1019 y-yamada add
	+ ") values ("
	+ " to_char(target_rank.nextval,'FM0999999999')"//oracleからデータをもらう
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",SYSDATE"
	+ ",?" //1019 y-yamada add
	+ ")";

    // 重要度の削除の前準備
    static final String ACTION_MOVE_SQL
	= "UPDATE sentaku_toroku"
	+ " SET target_rank = (select target_rank from company where company_cd = ?)"
	+ " WHERE mr_id in (select mr_id from mr where company_cd = ?)"
	+ " AND target_rank = ?";

    // 重要度の削除
    static final String ACTION_DELETE_SQL
	= "DELETE from action"
	+ " WHERE company_cd = ?"
	+ " AND target_rank = ?";


    public ActionInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * ターゲットランクリストの取得
     */
    public Vector getTargetRankList(String companyCd, ActionListSession session) {
	Vector list = new Vector();
	
	try {
            //ＳＱＬ文
            String sqltxt = TARGET_RANK_LIST_SQL;
	    sqltxt += " ORDER BY "
		+ session.getSortKey() + " "
		+ session.getOrder();
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);

	    try {
		pstmt.setString(1, companyCd);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    ActionInfo info = new ActionInfo();
		    info.setTargetName(rs.getString("target_name")); //1018 y-yamada add
		    info.setTargetRank(rs.getString("target_rank"));
		    list.addElement(info);
		}

	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return list;
    }

    public ActionInfo getActionInfo(String companyCd,
				    String targetRank) {
	ActionInfo info = new ActionInfo();

	try {
	    PreparedStatement pstmt = conn.prepareStatement(ACTION_INFO_SQL);

	    try {
		pstmt.setString(1, companyCd);
		pstmt.setString(2, targetRank);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    info.setCompanyCd(rs.getString("company_cd"));
		    info.setTargetRank(rs.getString("target_rank"));
		    info.setTargetName(rs.getString("target_name"));
		    info.setThreshold(rs.getInt("threshold"));
		    info.setMessage1(rs.getString("message1"));
		    info.setMessage2(rs.getString("message2"));
		    info.setMessage3(rs.getString("message3"));
		    info.setMessage4(rs.getString("message4"));
		}

	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return info;
    }

    public int update(String mrId, ActionInfo info) {
	int updateCount = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(ACTION_UPDATE_SQL);
 		if(info.getTargetName()==null || ((String)info.getTargetName()).equals(""))//1019 y-yamada add
		    throw new WmException("ターゲットランクを入力してください"); //1019 y-yamada add

	    try {
		pstmt.setString(1, info.getTargetRank());
		pstmt.setInt(2, info.getThreshold());
		pstmt.setString(3, info.getMessage1());
		pstmt.setString(4, info.getMessage2());
		pstmt.setString(5, info.getMessage3());
		pstmt.setString(6, info.getMessage4());
		pstmt.setString(7, mrId);
		pstmt.setString(8, info.getTargetName());
		pstmt.setString(9, info.getCompanyCd());
		pstmt.setString(10, info.getTargetRank());

		updateCount = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return updateCount;
    }

    public int insert(String mrId, String companyCd, ActionInfo info) {
	int updateCount = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(ACTION_INSERT_SQL);

	    try {
		// pstmt.setString(1, info.getTargetRank());
		pstmt.setString(1, companyCd);
		pstmt.setInt(2, info.getThreshold());
		pstmt.setString(3, info.getMessage1());
		pstmt.setString(4, info.getMessage2());
		pstmt.setString(5, info.getMessage3());
		pstmt.setString(6, info.getMessage4());
		pstmt.setString(7, mrId);
		pstmt.setString(8,info.getTargetName());//1018 y-yamada add

		updateCount = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return updateCount;
    }

    public int delete(String companyCd, Vector vec) {
	int updateCount = 0;
	PreparedStatement pstmt=null;
        try {
	    try {
		pstmt = conn.prepareStatement(ACTION_MOVE_SQL);
		for ( int i = 0; i < vec.size(); i++ ) {
		    String code = (String)vec.elementAt(i);
		    pstmt.setString(1, companyCd);
		    pstmt.setString(2, companyCd);
		    pstmt.setString(3, code);
		    pstmt.executeUpdate();
		}
	    } finally {
		pstmt.close();
	    }

	    try {
		pstmt = conn.prepareStatement(ACTION_DELETE_SQL);
		for ( int i = 0; i < vec.size(); i++ ) {
		    String code = (String)vec.elementAt(i);
		    pstmt.setString(1, companyCd);
		    pstmt.setString(2, code);
		    updateCount =+ pstmt.executeUpdate();
		}
	    } finally {
		pstmt.close();
	    }

            conn.commit();
	    return updateCount;

	} catch (SQLException ex) {
	    throw new WmException(ex);
	}
    }

}
