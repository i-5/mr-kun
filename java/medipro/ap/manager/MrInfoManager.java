package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * MR�e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:28:47)
 */
public class MrInfoManager {
	
    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public MrInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �w�肵��MR-ID��DB�ɓo�^����Ă��邩�`�F�b�N����
     * @param  mrId �m�F�Ώۂ�MR-ID
     * @return ���݂����true
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
     * �f�t�H���g�摜�R�[�h�擾
     * @param  mrId �Ώۂ�MR-ID
     * @return �摜�R�[�h(�ݒ肳��Ă��Ȃ���΋�)
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
     * ���ȏЉ�̎擾
     * @param  mrId �Ώۂ�MR-ID
     * @return ���ȏЉ�(�ݒ肳��Ă��Ȃ���΋�)
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
