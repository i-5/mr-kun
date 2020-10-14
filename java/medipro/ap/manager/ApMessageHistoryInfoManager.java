package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import medipro.ap.util.Logger;

/**
 * AP���b�Z�[�W�C���^�[�t�F�[�X���p�����e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:25:19)
 */
public class ApMessageHistoryInfoManager {

    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     */
    public ApMessageHistoryInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �Ώۂ̃��[�J(MR)���Ō�Ɏ擾�������b�Z�[�W�w�b�_ID���擾���܂�
     * @param  companyCd �Ώۂ̃��[�J�R�[�h
     * @param  mrId      �Ώۂ�MR-ID
     * @return ���b�Z�[�W�w�b�_ID
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

        //���߂ĊO��I/F�𗘗p�������[�J�̏ꍇ
        return "000000000000000000";
    }

    /**
     * �V����MR��AP���b�Z�[�W�C���^�[�t�F�[�X���p�����ɓo�^���܂��B
     * @param companyCd �Ώۂ̉�ЃR�[�h
     * @param mrId      �Ώۂ�MR-ID
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
     * �Ώۂ̃��[�J(MR)���Ō�Ɏ擾�������b�Z�[�W�w�b�_ID���X�V���܂��B
     * @param companyCd       �Ώۂ̉�ЃR�[�h
     * @param mrId            �Ώۂ�MR-ID
     * @param messageHeaderId ���b�Z�[�W�w�b�_ID
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
