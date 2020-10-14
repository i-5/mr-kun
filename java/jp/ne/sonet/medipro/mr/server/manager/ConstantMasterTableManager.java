package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

/**
 * <h3>�萔�}�X�^�e�[�u���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/30 �ߌ� 06:46:46)
 * @author: 
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
     * <h3>�萔�}�X�^�e�[�u�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 06:49:58)
     * @return jp.ne.sonet.medipro.mr.server.entity.ConstantMasterTable
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
	    throw new MrException(e);
	}	

	return constantmastertable;
    }
}
