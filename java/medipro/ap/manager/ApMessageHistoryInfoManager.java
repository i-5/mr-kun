package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * APメッセージインターフェース利用履歴テーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:25:19)
 */
public class ApMessageHistoryInfoManager {

    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public ApMessageHistoryInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * 対象のメーカ(MR)が最後に取得したメッセージヘッダIDを取得します
     * @param  companyCd 対象のメーカコード
     * @param  mrId      対象のMR-ID
     * @return メッセージヘッダID
     */
    public String getLastMessageHeaderId(String companyCd,
                                         String mrId) throws SQLException {
        String sql
            = "SELECT "
            + " message_header_id"
            + " FROM ap_message_history"
            + " WHERE "
            + " company_cd = '" + companyCd + "'"
            + " AND mr_id = '" + mrId + "'";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();
			
        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getString("message_header_id");
            } else {
                insertLastMessageHeaderId(companyCd, mrId);
            }

        } finally {
            stmt.close();
        }

        //初めて外部I/Fを利用したメーカの場合
        return "000000000000000000";
    }

    /**
     * 新たなMRをAPメッセージインターフェース利用履歴に登録します。
     * @param companyCd 対象の会社コード
     * @param mrId      対象のMR-ID
     */
    private void insertLastMessageHeaderId(String companyCd,
                                           String mrId) throws SQLException {
        String sql
            = "INSERT INTO ap_message_history ("
            + " company_cd"
            + ",mr_id"
            + ",message_header_id"
            + ",update_time"
            + ") values ("
            + " '" + companyCd + "'"
            + ",'" + mrId + "'"
            + ",'000000000000000000'"
            + ",SYSDATE"
            + ")";
        Logger.log("SQL = " + sql);
	
        Statement stmt = conn.createStatement();
			
        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }

    /**
     * 対象のメーカ(MR)が最後に取得したメッセージヘッダIDを更新します。
     * @param companyCd       対象の会社コード
     * @param mrId            対象のMR-ID
     * @param messageHeaderId メッセージヘッダID
     */
    public void updateLastMessageHeaderId(String companyCd,
                                          String mrId,
                                          String messageHeaderId) throws SQLException {
        String sql
            = "UPDATE ap_message_history SET"
            + " message_header_id = '" + messageHeaderId + "'"
            + ",update_time = SYSDATE"
            + " WHERE"
            + " company_cd = '" + companyCd + "'"
            + " AND mr_id = '" + mrId + "'";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();
			
        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }
}
