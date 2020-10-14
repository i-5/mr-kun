package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * 会社テーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:27:34)
 */
public class CompanyInfoManager {
	
    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     */
    public CompanyInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * 外部公開会社コード → 会社コード
     * @param  apCompanyCd 外部公開会社コード
     * @return 会社コード
     */
    public String getCompanyCd(String apCompanyCd) {
        String sql
            = "SELECT"
            + " company_cd"
            + " FROM"
            + " company"
            + " WHERE"
            + " ap_company_cd = '" + apCompanyCd + "'";
        Logger.log("SQL = " + sql);
		
        String companyCd = "";

        try {
            Statement stmt = conn.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    companyCd = rs.getString("company_cd");
                }

                //２つ以上は駄目
                if (rs.next()) {
                    Logger.error(rs.getString("company_cd") + "が複数存在する");
                    companyCd = "";
                }
				
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.error(sql, ex);
        }

        return companyCd;
    }
}
