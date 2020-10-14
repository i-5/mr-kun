package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * 医師テーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:28:22)
 */
public class DoctorInfoManager {
	
    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public DoctorInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * システム医師コード配列を医師ID配列に変換します。
     * 変換に失敗した場合(未登録医師)はnull
     * @param  systemCd システム医師コード配列
     * @return 医師コード配列
     */
    public String[] toDoctorId(String[] systemCd) {
        String sql
            = "SELECT"
            + " dr_id"
            + " FROM"
            + " doctor"
            + " WHERE"
            + " delete_ymd is Null";

        String sqlbuf = "";

        String[] drId = new String[systemCd.length];
		
        try {
            Statement stmt = conn.createStatement();

            try {
                for (int i = 0; i < systemCd.length; i++) {
                    try {
                        sqlbuf = sql + " AND system_cd = '" + systemCd[i] + "'";

                        Logger.log("SQL = " + sqlbuf);
                        ResultSet rs = stmt.executeQuery(sqlbuf);

                        if (rs.next()) {
                            drId[i] = rs.getString("dr_id");
                        }
                    } catch (SQLException ex) {
                    }
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.error(sqlbuf, ex);
        }

        return drId;
    }
}
