package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.predicate.*; 

/**
 * <h3>���B�R�[�����O�e�[�u�����Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߌ� 03:52:35)
 * @author: 
 */
public class TotatsuCallLogTableManager {
    protected Connection conn;

    protected static String ATTACH_LINK_INSERT_STRING
	= "INSERT INTO totatsu_call_log ( "
	+ "totatsu_call_time, "
	+ "from_userid, "
	+ "to_userid, "
	+ "message_header_id, "
	+ "picture_cd "
	+ ") "
	+ "VALUES (SYSDATE,?,?,?,?) ";

    /**
     * TotatsuCallLogTableManager �R���X�g���N�^�[�E�R�����g�B
     */
    public TotatsuCallLogTableManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>���B�R�[�����O�e�[�u���̏��o��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/02 �ߌ� 03:55:00)
     * @parm jp.ne.sonet.medipro.mr.server.entity.TotatsuCallLogTable
     */
    public void insert(TotatsuCallLogTable totatsucalllogtable) {
	try {
	    //���B�R�[�����O
	    PreparedStatement pstmt = conn.prepareStatement(ATTACH_LINK_INSERT_STRING);

	    try {
		pstmt.setString(1, totatsucalllogtable.getFromUserID() );
		pstmt.setString(2, totatsucalllogtable.getToUserID());
		pstmt.setString(3, totatsucalllogtable.getMessageHeaderID());
		pstmt.setString(4, totatsucalllogtable.getPictureCD());

		pstmt.execute();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }
}
