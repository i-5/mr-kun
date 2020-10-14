package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * 添付リンクテーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:25:31)
 */
public class AttachLinkInfoManager {
	
    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public AttachLinkInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * シーケンスを発行します。
     * @return シーケンス番号
     */
    private String getSequence() throws SQLException {
        String sql
            = "SELECT "
            + "TRIM(TO_CHAR(attach_link_seq.nextval,'00')) counter"
            + " FROM dual";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("counter");
            }

            throw new SQLException("通常はありえない");
        } finally {
            stmt.close();
        }
    }

    /**
     * 添付リンクテーブルにリンクを登録します
     * @param messageId リンクを使用しているメッセージID
     * @param url       登録するリンクURL
     */
    public void insertLink(String messageId, String url)  throws SQLException {
        String sql
            = "INSERT INTO attach_link ( "
            + " message_id" 
            + ",seq" 
            + ",url" 
            + ",honbun_text" 
            + ",picture" 
            + ",naigai_link_kbn"
            + ")"
            + " VALUES ("
            + " '" + messageId + "'"
            + ",'" + getSequence() + "'"
            + ",'" + url + "'"
            + ",'" + url + "'"
            + ",NULL"
            + ",NULL) ";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();

        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }
}
