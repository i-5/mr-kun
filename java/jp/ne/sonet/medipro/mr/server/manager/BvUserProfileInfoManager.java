package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>�a�u�v���t�@�C�����Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:58:52)
 * @author: 
 */
public class BvUserProfileInfoManager {
    protected Connection conn;

    protected static final String BV_PROFILE_MAIN_SQL
	= "SELECT NAME, KANANAME, KINMUSAKI FROM BV_USER BVU, BV_USER_PROFILE BVUP "
	+ "WHERE BVU.USER_ALIAS = ? AND BVU.USER_ID = BVUP.USER_ID";
	
    /**
     * BvUserProfileInfo �R���X�g���N�^�[�E�R�����g�B
     */
    public BvUserProfileInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�a�u�v���t�@�C�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/25 �ߌ� 03:25:47)
     * @return jp.ne.sonet.medipro.mr.server.manager.BvUserProfileInfo
     * @param drID java.lang.String
     */
    public BvUserProfileInfo getBvUserProfile(String drID) {
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
	BvUserProfileInfo bvuserprofileinfo = new BvUserProfileInfo();
	
	//��Ѓe�[�u�����̎擾
	try {
	    //��Ђr�p�k��
	    sqltxt = BV_PROFILE_MAIN_SQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, drID);
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    bvuserprofileinfo.setName(rs.getString("NAME"));
		    bvuserprofileinfo.setKanaName(rs.getString("KANANAME"));
		    bvuserprofileinfo.setKinmusaki(rs.getString("KINMUSAKI"));
		}		
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return bvuserprofileinfo;
    }
}
