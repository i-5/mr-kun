package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jp.ne.sonet.medipro.wm.common.exception.WmException;

/**
 * <strong>�c�Ə����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class EigyosyoInfoManager {
    /** �c�Ə����X�g�擾 */
    protected static final String EIGYOSYO_LIST_SQL
        = "SELECT"
        + " eigyosyo_cd"
        + ",eigyosyo_name"
        + " FROM eigyosyo"
        + " WHERE"
        + " company_cd = ?"
        + " AND shiten_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND eigyosyo_name IS NOT NULL";

    /** �R�l�N�V�����I�u�W�F�N�g */
    protected Connection connection = null;

    /**
     * ����Manager���p����DB�ւ̃R�l�N�V������ݒ肷��.
     * @param initConnection �ݒ肷��R�l�N�V����
     */
    public EigyosyoInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    /**
     * ��ЃR�[�h�A�x�X�R�[�h�ōi�荞�񂾉c�Ə����X�g���擾����.
     * @param companyCd  ��ЃR�[�h
     * @param eigyosyoCd �c�Ə��R�[�h
     * @param (�R�[�h�A����)�̃��X�g
     */
    public Vector getEigyosyoList(String companyCd, String shitenCd) {
        Vector list = new Vector();
        //�󔒂̒ǉ�
        list.addElement(new String[]{"", ""});

        try {
            PreparedStatement pstmt = connection.prepareStatement(EIGYOSYO_LIST_SQL);
            pstmt.setString(1, companyCd);
            pstmt.setString(2, shitenCd);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] pair = {rs.getString("eigyosyo_cd"),
                                 rs.getString("eigyosyo_name")};
                list.addElement(pair);
            }
      
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return list;
    }
}
