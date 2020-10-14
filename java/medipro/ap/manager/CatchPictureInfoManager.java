package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * �L���b�`�摜�e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:26:42)
 */
public class CatchPictureInfoManager {
	
    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public CatchPictureInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �w�肵���L���b�`�摜�R�[�h��DB�ɓo�^����Ă��邩�`�F�b�N����
     * @param  pictureCd �m�F�Ώۂ̉摜�R�[�h
     * @return ���݂����true
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
