package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * キャッチ画像テーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:26:42)
 */
public class CatchPictureInfoManager {
	
    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public CatchPictureInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * 指定したキャッチ画像コードがDBに登録されているかチェックする
     * @param  pictureCd 確認対象の画像コード
     * @return 存在すればtrue
     */
    public boolean exist(String pictureCd) {
        String sql
            = "SELECT"
            + " count(picture_cd) picture_num"
            + " FROM"
            + " catch_picture"
            + " WHERE"
            + " picture_cd = '" + pictureCd + "'"
            + " AND delete_ymd is Null";
        Logger.log("SQL = " + sql);

        try {
            Statement stmt = conn.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    if (rs.getInt("picture_num") == 1) {
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

}
