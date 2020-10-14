package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import jp.ne.sonet.medipro.wm.common.exception.WmException; 

/**
 * <strong>MR�����e�[�u���Ǘ��N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrAttributeInfoManager {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** �L����MR�����R�[�h�A�������̃��X�g�擾 */
    protected static final String ATTRIBUTE_LIST_SQL
	= "SELECT"
	+ " mr_attribute_cd"
	+ ",mr_attribute_name"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND mr_attribute_name IS NOT NULL";

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    /** �R�l�N�V���� */
    protected Connection connection = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    /**
     * DB�Ƃ�Connection��ێ�����I�u�W�F�N�g�𐶐�����.
     * @param initConnection ���̃I�u�W�F�N�g���g�p����Connection
     */
    public MrAttributeInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * �w�肵����ЃR�[�h�ōi�荞��MR�����R�[�h�A�������̃��X�g���擾����.
     * @param  campanyCd ��ЃR�[�h
     * @return (�R�[�h�A����)�y�A�̃��X�g
     */
    public Vector getMrAttributeList(String companyCd) {
	Vector list = new Vector();
	list.addElement(new String[]{"", ""});

	try {
	    PreparedStatement pstmt = connection.prepareStatement(ATTRIBUTE_LIST_SQL);
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		String[] pair = {rs.getString("mr_attribute_cd"),
				 rs.getString("mr_attribute_name")};
		list.addElement(pair);
	    }
      
	    pstmt.close();
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return list;
    }
}
