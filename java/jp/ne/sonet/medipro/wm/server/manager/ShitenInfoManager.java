package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jp.ne.sonet.medipro.wm.common.exception.WmException;

/**
 * <strong>�x�X���Ǘ��N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class ShitenInfoManager {
    /** �x�X�ꗗ�擾 */
    protected static final String SHITEN_LIST_SQL
	= "SELECT"
	+ " shiten_cd"
	+ ",shiten_name"
	+ " FROM shiten"
	+ " WHERE company_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND shiten_name IS NOT NULL";

    /** �R�l�N�V���� */
    protected Connection connection;

    /**
     * DB�Ƃ�Connection��ێ�����I�u�W�F�N�g�𐶐�����.
     * @param initConnection ���̃I�u�W�F�N�g���g�p����Connection
     */
    public ShitenInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * �w�肵����ЃR�[�h�ōi�荞�񂾎x�X�R�[�h�A�x�X���̃��X�g���擾����.
     * @param  campanyCd ��ЃR�[�h
     * @return (�R�[�h�A����)�y�A�̃��X�g
     */
    public Vector getShitenList(String companyCd) {
	Vector list = new Vector();
	list.addElement(new String[]{"", ""});

	try {
	    PreparedStatement pstmt = connection.prepareStatement(SHITEN_LIST_SQL);
	    
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		String[] pair = {rs.getString("shiten_cd"),
				 rs.getString("shiten_name")};

		list.addElement(pair);
	    }

	    pstmt.close();
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return list;
    }

}
