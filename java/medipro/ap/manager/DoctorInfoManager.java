package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * ��t�e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:28:22)
 */
public class DoctorInfoManager {
	
    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public DoctorInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �V�X�e����t�R�[�h�z�����tID�z��ɕϊ����܂��B
     * �ϊ��Ɏ��s�����ꍇ(���o�^��t)��null
     * @param  systemCd �V�X�e����t�R�[�h�z��
     * @return ��t�R�[�h�z��
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
