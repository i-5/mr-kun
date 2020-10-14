package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * ��Ѓe�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:27:34)
 */
public class CompanyInfoManager {
	
    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public CompanyInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �O�����J��ЃR�[�h �� ��ЃR�[�h
     * @param  apCompanyCd �O�����J��ЃR�[�h
     * @return ��ЃR�[�h
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

                //�Q�ȏ�͑ʖ�
                if (rs.next()) {
                    Logger.error(rs.getString("company_cd") + "���������݂���");
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
