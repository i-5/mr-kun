package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

/**
 * <h3>定数マスタテーブル管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:46:46)
 * @author: 
 */
public class ConstantMasterTableManager {
    protected Connection conn;
    protected static final String CONSTANT_MASTER_SQL
	= "SELECT  name, naiyo1, naiyo2, naiyo3 "
	+ "FROM constant_master "
	+ "WHERE constant_cd = ?";

    /**
     * ConstantMasterTableManager コンストラクター・コメント。
     */
    public ConstantMasterTableManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>定数マスタテーブル情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/30 午後 06:49:58)
     * @return jp.ne.sonet.medipro.mr.server.entity.ConstantMasterTable
     * @param constantCD java.lang.String
     */
    public ConstantMasterTable getConstantMasterTable(String constantCD) {
	ConstantMasterTable constantmastertable = new ConstantMasterTable();
	String sqltxt;
	
	//定数マスタテーブル情報の取得
	try {
	    //メ定数マスターＳＱＬ文
	    sqltxt = CONSTANT_MASTER_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, constantCD);
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    constantmastertable.setName(rs.getString("name"));
		    constantmastertable.setNaiyo1(rs.getString("naiyo1"));
		    constantmastertable.setNaiyo2(rs.getString("naiyo2"));
		    constantmastertable.setNaiyo3(rs.getString("naiyo3"));
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return constantmastertable;
    }
}
