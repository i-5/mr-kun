package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.*;
import jp.ne.sonet.mrkun.framework.exception.*;
/**
 * @author: Harry Behrens
 */
public class ConstantMasterTableManager {
    protected Connection conn;
    protected static final String CONSTANT_MASTER_SQL
	= "SELECT  name, naiyo1, naiyo2, naiyo3 "
	+ "FROM constant_master "
	+ "WHERE constant_cd = ?";

    /**
     * ConstantMasterTableManager �R���X�g���N�^�[�E�R�����g�B
     */
    public ConstantMasterTableManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * @return jp.ne.sonet.medipro.mr.ConstantMasterTable
     * @param constantCD java.lang.String
     */
    public ConstantMasterTable getConstantMasterTable(String constantCD) {
	ConstantMasterTable constantmastertable = new ConstantMasterTable();
	String sqltxt;
	
	//�萔�}�X�^�e�[�u�����̎擾
	try {
	    //���萔�}�X�^�[�r�p�k��
	    sqltxt = CONSTANT_MASTER_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, constantCD);
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    constantmastertable.setName(rs.getString("name"));
		    constantmastertable.setNaiyo1(rs.getString("naiyo1"));
		    constantmastertable.setNaiyo2(rs.getString("naiyo2"));
		    constantmastertable.setNaiyo3(rs.getString("naiyo3"));
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new ApplicationError("ConstantMasterTableManager.getConstantMasterTable: SQLException",e);
	}	

	return constantmastertable;
    }
}
