package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.predicate.*; 

/**
 * <h3>到達コールログテーブル情報管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:52:35)
 * @author: 
 */
public class TotatsuCallLogTableManager {
    protected Connection conn;

    protected static String ATTACH_LINK_INSERT_STRING
	= "INSERT INTO totatsu_call_log ( "
	+ "totatsu_call_time, "
	+ "from_userid, "
	+ "to_userid, "
	+ "message_header_id, "
	+ "picture_cd "
	+ ") "
	+ "VALUES (SYSDATE,?,?,?,?) ";

    /**
     * TotatsuCallLogTableManager コンストラクター・コメント。
     */
    public TotatsuCallLogTableManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>到達コールログテーブルの書出し</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/02 午後 03:55:00)
     * @parm jp.ne.sonet.medipro.mr.server.entity.TotatsuCallLogTable
     */
    public void insert(TotatsuCallLogTable totatsucalllogtable) {
	try {
	    //到達コールログ
	    PreparedStatement pstmt = conn.prepareStatement(ATTACH_LINK_INSERT_STRING);

	    try {
		pstmt.setString(1, totatsucalllogtable.getFromUserID() );
		pstmt.setString(2, totatsucalllogtable.getToUserID());
		pstmt.setString(3, totatsucalllogtable.getMessageHeaderID());
		pstmt.setString(4, totatsucalllogtable.getPictureCD());

		pstmt.execute();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }
}
