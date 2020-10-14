package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * �Y�t�����N�e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:25:31)
 */
public class AttachLinkInfoManager {
	
    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public AttachLinkInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �V�[�P���X�𔭍s���܂��B
     * @return �V�[�P���X�ԍ�
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

            throw new SQLException("�ʏ�͂��肦�Ȃ�");
        } finally {
            stmt.close();
        }
    }

    /**
     * �Y�t�����N�e�[�u���Ƀ����N��o�^���܂�
     * @param messageId �����N���g�p���Ă��郁�b�Z�[�WID
     * @param url       �o�^���郊���NURL
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
