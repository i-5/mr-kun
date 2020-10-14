package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.mrkun.framework.exception.*;
//import jp.ne.sonet.medipro.mr.server.entity.*;
//import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>�|�C���g���i����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 09:02:23)
 * @author: 
 */
public class UpdatePoint {
    protected Connection conn;
	
    //�|�C���g�����擾SQL
    protected static final String GET_POINT_SQL
	= "SELECT point FROM doctor "
	+ "WHERE dr_id = ? "
	+ "AND system_cd = ? "
	+ "FOR UPDATE ";

    //��t���(POINT)�X�V�r�p�k
    protected static final String DR_POINT_UPDATESQL
	= "UPDATE doctor SET "
	+ "point = ? "
	+ "WHERE dr_id = ? "
	+ "AND system_cd = ? ";

    //POINT�g�p�������o���r�p�k
    protected static final String POINT_HIST_INSERTSQL
	= "INSERT INTO point_hist ( "
	+ "dr_id, "
	+ "system_cd, "
	+ "point, "
	+ "update_time "
	+ ") " 
	+ "VALUES (  " 
	+ "?, " 
	+ "?, " 
	+ "?, " 
	+ "SYSDATE " 
	+ ") ";

    /**
     * UpdatePoint �R���X�g���N�^�[�E�R�����g�B
     */
    public UpdatePoint(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�|�C���g���i��������</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (01/01/21 �ߑO 16:10:41)
     * @param drID java.lang.String
     *		SysCd java.lang.String
     *		Point int
     */
    public boolean UpdatePointExec(String drId, String SysCd, int Point) {
	boolean ret = false;
	
	try {
	    try {
		conn.setAutoCommit(false);

		PreparedStatement stmt = conn.prepareStatement(GET_POINT_SQL);
		stmt.setString(1, drId);
		stmt.setString(2, SysCd);
		ResultSet rs = stmt.executeQuery();
		int dr_point = 0;

		if (rs.next()) {
		    dr_point = rs.getInt("point");
		}

		rs.close();
		stmt.close();

		// �g�p�|�C���g���Z
		dr_point -= Point;

		if( dr_point >= 0 ){
		    // �g�p�|�C���g���Z
		    PreparedStatement pstmt = conn.prepareStatement(DR_POINT_UPDATESQL);
		    pstmt.setInt(1, dr_point);
		    pstmt.setString(2, drId);
		    pstmt.setString(3, SysCd);

		    pstmt.executeUpdate();
		    pstmt.close();

		    // �|�C���g�g�p����
		    PreparedStatement hpstmt = conn.prepareStatement(POINT_HIST_INSERTSQL);
		    hpstmt.setString(1, drId);
		    hpstmt.setString(2, SysCd);
		    hpstmt.setInt(3, Point);

		    hpstmt.executeUpdate();
		    hpstmt.close();

		    conn.commit();
		    ret = true;
		}else{
		    // �|�C���g�s��
		    conn.rollback();
		}

	    } catch (SQLException e) {
		conn.rollback();
		throw e;
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
		throw new ApplicationError("UpdatePoint: UpdatePointExec()",ex);
	}

	return ret;
    }

}
