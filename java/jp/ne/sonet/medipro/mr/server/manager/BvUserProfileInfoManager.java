package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>ＢＶプロファイル情報管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:58:52)
 * @author: 
 */
public class BvUserProfileInfoManager {
    protected Connection conn;

    protected static final String BV_PROFILE_MAIN_SQL
	= "SELECT NAME, KANANAME, KINMUSAKI FROM BV_USER BVU, BV_USER_PROFILE BVUP "
	+ "WHERE BVU.USER_ALIAS = ? AND BVU.USER_ID = BVUP.USER_ID";
	
    /**
     * BvUserProfileInfo コンストラクター・コメント。
     */
    public BvUserProfileInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>ＢＶプロファイル情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/25 午後 03:25:47)
     * @return jp.ne.sonet.medipro.mr.server.manager.BvUserProfileInfo
     * @param drID java.lang.String
     */
    public BvUserProfileInfo getBvUserProfile(String drID) {
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
	BvUserProfileInfo bvuserprofileinfo = new BvUserProfileInfo();
	
	//会社テーブル情報の取得
	try {
	    //会社ＳＱＬ文
	    sqltxt = BV_PROFILE_MAIN_SQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, drID);
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    bvuserprofileinfo.setName(rs.getString("NAME"));
		    bvuserprofileinfo.setKanaName(rs.getString("KANANAME"));
		    bvuserprofileinfo.setKinmusaki(rs.getString("KINMUSAKI"));
		}		
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return bvuserprofileinfo;
    }
}
