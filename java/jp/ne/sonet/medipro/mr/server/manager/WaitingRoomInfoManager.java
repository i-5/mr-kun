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
 * <h3>�ҍ������Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 04:25:11)
 * @author: 
 */
public class WaitingRoomInfoManager {
    protected Connection conn;

    protected static String MR_WAIT_MAINSQL
	= "SELECT sen.mr_id sen_mr_id "
	+ "FROM sentaku_toroku sen "
	+ "WHERE sen.dr_id = ? "
	+ "AND sen.sentaku_kbn = '3' "
	+ "ORDER BY sentaku_kbn";
	
    protected static String MR_WAIT_MSG_COUNT_MAINSQL
	= "SELECT NVL(Count(message_header_id),0) count "
	+ "FROM message_header mh, message_body mb ";

    protected static String MR_WAIT_MSG_MAINSQL
	= "SELECT message_header_id, title, TO_CHAR(receive_time,'YYYY/MM/DD HH24:MI') receivetime FROM message_header mh, message_body mb ";

    protected static String MR_WAIT_OPTION = 
	"WHERE " + 
	"mh.to_userid = ? " + 
	"AND mh.from_userid = ? " + 
	"AND mh.receive_timed IS NULL " + 
	"AND mh.send_torikeshi_time IS NULL " + 
//	"AND mh.message_kbn <> '3' " +
	"AND mh.message_kbn = '1' " +//1117 y-yamada add NO.55 �l�q����̑��M�f�[�^�̂�
	"AND mh.receive_status <> '3' " + 
	"AND mh.message_id = mb.message_id " + 
	"AND ((mb.yuko_kigen IS NULL) OR (TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD')  )) ";

    /**
     * WaitingRoomInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public WaitingRoomInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�ҍ����i�l�q�ʁj���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߌ� 02:45:20)
     * @return java.util.Enumeration (WaitingRoomInfo)
     * @param drID java.lang.String
     */
    public Enumeration getWaitingRoomInfo(String drID) {

	ResultSet rs;
	PreparedStatement pstmt;
	ResultSet rs1;
	PreparedStatement pstmt1;
	OrderedSet waitingroominfolist = new OrderedSet();
	//Vector waitingroominfolist = new Vector();
	int count = 0;
	String sqltxt;
	String mrID = null;

	BinaryPredicate pred = new WaitRoomMsgDescendPredicate();
	waitingroominfolist = new OrderedSet(pred,true);
	
	//�ҍ����l�q���̎擾
	try {
	    //�l�q�ҍ����r�p�k��
	    sqltxt = MR_WAIT_MAINSQL;
	
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, drID);
		rs   = pstmt.executeQuery();
	
		while ( rs.next() ) {
		    mrID = rs.getString("sen_mr_id");

		    WaitingRoomInfo waitingroominfo =new WaitingRoomInfo();

		    waitingroominfo.setMrID(mrID);
		    MrInfoManager mrinfomanager = new MrInfoManager(conn);
		    waitingroominfo.setMrinfo(mrinfomanager.getMrInfo(mrID));
		    //���b�Z�[�W�J�E���^�[�N���A
		    waitingroominfo.setMsgCount(0);

		    //�l�q���b�Z�[�W�J�E���g�r�p�k��
		    sqltxt = MR_WAIT_MSG_COUNT_MAINSQL + MR_WAIT_OPTION;
				
		    pstmt1 = conn.prepareStatement(sqltxt);
		    try {
			// �p�����[�^��ݒ�
			pstmt1.setString(1, drID);
			pstmt1.setString(2, rs.getString("sen_mr_id"));
			rs1   = pstmt1.executeQuery();
			while ( rs1.next() ) {
			    count = rs1.getInt("count");
			    waitingroominfo.setMsgCount(count);
			}
		    } finally {
			pstmt1.close();
		    }

		    if ( count != 0 ) {
			//���b�Z�[�W���Z�b�g
			sqltxt = MR_WAIT_MSG_MAINSQL + MR_WAIT_OPTION + " ORDER BY receive_time DESC";
					
			pstmt1 = conn.prepareStatement(sqltxt);
			try {
			    // �p�����[�^��ݒ�
			    pstmt1.setString(1, drID);
			    pstmt1.setString(2, mrID);
			    rs1   = pstmt1.executeQuery();
			    while ( rs1.next() ) {
				MsgManager msgmanager = new MsgManager(conn);
				waitingroominfo.setMsginfo(msgmanager.getDrRecvMessage(rs1.getString("message_header_id")));
				break;
			    }
			} finally {
			    pstmt1.close();
			}
			waitingroominfolist.add(waitingroominfo);
		    }
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return waitingroominfolist.elements();
    }
    /**
     * <h3>�ҍ����i���ǃ��b�Z�[�W�j���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߌ� 02:45:20)
     * @return java.util.Enumeration (WaitingRoomInfo)
     * @param drID java.lang.String
     * @param drID java.lang.String
     */
    public Enumeration getWaitingRoomUnRead(String drID, String mrID) {

	ResultSet rs;
	PreparedStatement pstmt;
	OrderedSet waitingroominfolist = new OrderedSet();
	String sqltxt;
	
	BinaryPredicate pred = new WaitRoomMsgDescendPredicate();
	waitingroominfolist = new OrderedSet(pred,true);
	
	//�ҍ����l�q���̎擾
	try {
	    MrInfoManager mrinfomanager = new MrInfoManager(conn);
	    MrInfo mrinfo = mrinfomanager.getMrInfo(mrID);
	    MsgManager msgmanager = new MsgManager(conn);
		
	    //���b�Z�[�W���Z�b�g
	    sqltxt = MR_WAIT_MSG_MAINSQL + MR_WAIT_OPTION + " ORDER BY receive_time DESC";
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, drID);
		pstmt.setString(2, mrID);
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {

		    WaitingRoomInfo waitingroominfo =new WaitingRoomInfo();
		    waitingroominfo.setMrinfo(mrinfo);
		    waitingroominfo.setMsginfo(msgmanager.getDrRecvMessage(rs.getString("message_header_id")));
		    waitingroominfolist.add(waitingroominfo);
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return waitingroominfolist.elements();
    }
}
