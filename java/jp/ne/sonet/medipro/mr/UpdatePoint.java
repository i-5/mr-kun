package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.mrkun.framework.exception.*;
//import jp.ne.sonet.medipro.mr.server.entity.*;
//import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>ポイント商品交換</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 09:02:23)
 * @author: 
 */
public class UpdatePoint {
    protected Connection conn;
	
    //ポイント増分取得SQL
    protected static final String GET_POINT_SQL
	= "SELECT point FROM doctor "
	+ "WHERE dr_id = ? "
	+ "AND system_cd = ? "
	+ "FOR UPDATE ";

    //医師情報(POINT)更新ＳＱＬ
    protected static final String DR_POINT_UPDATESQL
	= "UPDATE doctor SET "
	+ "point = ? "
	+ "WHERE dr_id = ? "
	+ "AND system_cd = ? ";

    //POINT使用履歴書出しＳＱＬ
    protected static final String POINT_HIST_INSERTSQL
	= "INSERT INTO point_hist ( "
	+ "dr_id, "
	+ "system_cd, "
	+ "point, "
	+ "update_time "
	+ ") " 
	+ "VALUES (  " 
	+ "?, " 
	+ "?, " 
	+ "?, " 
	+ "SYSDATE " 
	+ ") ";

    /**
     * UpdatePoint コンストラクター・コメント。
     */
    public UpdatePoint(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>ポイント商品交換処理</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (01/01/21 午前 16:10:41)
     * @param drID java.lang.String
     *		SysCd java.lang.String
     *		Point int
     */
    public boolean UpdatePointExec(String drId, String SysCd, int Point) {
	boolean ret = false;
	
	try {
	    try {
		conn.setAutoCommit(false);

		PreparedStatement stmt = conn.prepareStatement(GET_POINT_SQL);
		stmt.setString(1, drId);
		stmt.setString(2, SysCd);
		ResultSet rs = stmt.executeQuery();
		int dr_point = 0;

		if (rs.next()) {
		    dr_point = rs.getInt("point");
		}

		rs.close();
		stmt.close();

		// 使用ポイント減算
		dr_point -= Point;

		if( dr_point >= 0 ){
		    // 使用ポイント減算
		    PreparedStatement pstmt = conn.prepareStatement(DR_POINT_UPDATESQL);
		    pstmt.setInt(1, dr_point);
		    pstmt.setString(2, drId);
		    pstmt.setString(3, SysCd);

		    pstmt.executeUpdate();
		    pstmt.close();

		    // ポイント使用履歴
		    PreparedStatement hpstmt = conn.prepareStatement(POINT_HIST_INSERTSQL);
		    hpstmt.setString(1, drId);
		    hpstmt.setString(2, SysCd);
		    hpstmt.setInt(3, Point);

		    hpstmt.executeUpdate();
		    hpstmt.close();

		    conn.commit();
		    ret = true;
		}else{
		    // ポイント不足
		    conn.rollback();
		}

	    } catch (SQLException e) {
		conn.rollback();
		throw e;
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
		throw new ApplicationError("UpdatePoint: UpdatePointExec()",ex);
	}

	return ret;
    }

}
