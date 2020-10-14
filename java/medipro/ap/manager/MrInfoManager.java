package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * MRテーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:28:47)
 */
public class MrInfoManager {
	
    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public MrInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * 指定したMR-IDがDBに登録されているかチェックする
     * @param  mrId 確認対象のMR-ID
     * @return 存在すればtrue
     */
    public boolean exist(String mrId) {
        String sql
            = "SELECT"
            + " count(mr_id) mr_num"
            + " FROM"
            + " mr"
            + " WHERE"
            + " mr_id = '" + mrId + "'"
            + " AND delete_ymd is Null";
        Logger.log("SQL = " + sql);

        try {
            Statement stmt = conn.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    if (rs.getInt("mr_num") == 1) {
                        return true;
                    }
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.error(sql, ex);
        }

        return false;
    }

    /**
     * デフォルト画像コード取得
     * @param  mrId 対象のMR-ID
     * @return 画像コード(設定されていなければ空)
     */
    public String getDefaultPictureCd(String mrId) {
        String sql
            = "SELECT"
            + " picture_cd"
            + " FROM"
            + " mr"
            + " WHERE"
            + " mr_id = '" + mrId + "'";
        Logger.log("SQL = " + sql);
	
        try {
            Statement stmt = conn.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    return rs.getString("picture_cd");
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.error(sql, ex);
        }

        return "";
    }

    /**
     * 自己紹介の取得
     * @param  mrId 対象のMR-ID
     * @return 自己紹介(設定されていなければ空)
     */
    public String getJikosyokai(String mrId) {
        String sql
            = "SELECT"
            + " jikosyokai"
            + " FROM"
            + " mr"
            + " WHERE"
            + " mr_id = '" + mrId + "'";
        Logger.log("SQL = " + sql);

        try {
            Statement stmt = conn.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    return rs.getString("jikosyokai");
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.error(sql, ex);
        }

        return "";
    }
}
