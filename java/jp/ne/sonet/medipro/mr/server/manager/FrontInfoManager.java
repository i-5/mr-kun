package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 


/**
 * <h3>�t�����g���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:32:52)
 * @author: 
 */
public class FrontInfoManager {
    protected Connection conn;

    protected static final String MR_MAINSQL
	= "SELECT sen.mr_id sen_mr_id "
	+ "FROM sentaku_toroku sen "
	+ "WHERE sen.dr_id = ? AND sen.sentaku_kbn = ? ";

    protected static final String MR_MSG_COUNT_MAINSQL
	= "SELECT NVL(Count(message_header_id),0) count "
	+ "FROM message_header mh, message_body mb ";

    protected static final String MR_MSG_MAINSQL
	= "SELECT message_header_id, title "
	+ "FROM message_header mh, message_body mb ";

    protected static final String MR_OPTION
	= "WHERE "
	+ "mh.to_userid = ? "
	+ "AND mh.from_userid = ? "
	+ "AND mh.receive_timed IS NULL "
	+ "AND mh.send_torikeshi_time IS NULL "
//	+ "AND mh.message_kbn <> '3' "
	+ "AND mh.message_kbn = '1' " //1117 y-yamada add NO.55 �l�q����̑��M�f�[�^�̂�
	+ "AND mh.receive_status <> '3' "
	+ "AND mh.message_id = mb.message_id "
	+ "AND ((mb.yuko_kigen IS NULL) "
	+ "OR (TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD'))) ";
	
    protected static final String MR_UNREAD_MSG_COUNT_MAINSQL
	= "SELECT "
	+ "Count(message_header_id) count "
	+ "FROM "
	+ "message_header mh, "
	+ "message_body mb, "
	+ "sentaku_toroku sen "
	+ "WHERE "
	+ "mh.to_userid = ? "
	+ "AND mh.receive_timed IS NULL "
	+ "AND mh.send_torikeshi_time IS NULL "
	+ "AND mh.message_kbn <> '3' "
	+ "AND mh.receive_status <> '3' "
	+ "AND mh.message_id = mb.message_id "
	+ "AND ((mb.yuko_kigen IS NULL) "
	+ "OR (TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD'))) "
	+ "AND mh.to_userid = sen.dr_id "
	+ "AND mh.from_userid = sen.mr_id ";
				
    protected static final String MR_UNREAD_COUNT_MAINSQL
	= "SELECT "
	+ "Count(DISTINCT mh.from_userid) count "
	+ "FROM "
	+ "message_header mh, "
	+ "message_body mb, "
	+ "sentaku_toroku sen "
	+ "WHERE "
	+ "mh.to_userid = ? "
	+ "AND mh.receive_timed IS NULL "
	+ "AND mh.send_torikeshi_time IS NULL "
//	+ "AND mh.message_kbn <> '3' "
	+ "AND mh.message_kbn = '1' " //1117 y-yamada add NO.55 �l�q����̑��M�f�[�^�̂�
	+ "AND mh.receive_status <> '3' "
	+ "AND sen.sentaku_kbn = '3' "
	+ "AND mh.message_id = mb.message_id "
	+ "AND ((mb.yuko_kigen IS NULL) "
	+ "OR (TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD'))) "
	+ "AND mh.to_userid = sen.dr_id "
	+ "AND mh.from_userid = sen.mr_id ";

    /**
     * FrontInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public FrontInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�l�q�ŏ�i���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/26 �ߑO 11:36:30)
     * @return jp.ne.sonet.medipro.mr.server.entity.Fronthfo
     * @param drID java.lang.String
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException ��O�L�q
     */
    public FrontInfo getFrontInfo(String drID) {

	ResultSet rs;
	PreparedStatement pstmt;
	ResultSet rs1;
	PreparedStatement pstmt1;
		
	FrontInfo frontinfo =new FrontInfo();
	String sqltxt;

	try {

	    //��t���Z�b�g
	    DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
	    frontinfo.setDoctorinfo(doctorinfomanager.getDoctorInfo(drID));

	    //���b�Z�[�W�J�E���^�[�N���A
	    frontinfo.setLeftMsgCount(0);
	    frontinfo.setRightMsgCount(0);
		
	    //�l�q�����̎擾
	    getMrSub(frontinfo, drID , "1");

	    //�l�q�E���̎擾
	    getMrSub(frontinfo, drID , "2");

	    //���ǃ��b�Z�[�W�擾�r�p�k��
	    sqltxt = MR_UNREAD_COUNT_MAINSQL;
			
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, drID);
		rs   = pstmt.executeQuery();
		frontinfo.setNewMsgCount(0);
		while ( rs.next() ) {
		    frontinfo.setNewMsgCount(rs.getInt("count"));
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }

	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return frontinfo;
    }

    /**
     * <h3>�l�q�ŏ�i���̎擾�i�⏕�j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 02:39:44)
     * @param tantoinfo jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param drID java.lang.String
     * @param mrFlag java.lang.String
     */
    private void getMrSub(FrontInfo frontinfo, String drID, String mrFlag) {

	ResultSet rs;
	PreparedStatement pstmt;
	ResultSet rs1;
	PreparedStatement pstmt1;
	int count = 0;
	String sqltxt;
	String mrID = null;

	try {
	    //�l�q���r�p�k��
	    sqltxt = MR_MAINSQL;
	
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, drID);
		pstmt.setString(2, mrFlag);
		rs   = pstmt.executeQuery();
	
		while ( rs.next() ) {
		    mrID = rs.getString("sen_mr_id");
				
		    MrInfoManager mrinfomanager = new MrInfoManager(conn);
		    CompanyTableManager companytablemanager = new CompanyTableManager(conn);
		    if (mrFlag.equals("1")) {	//�l�q��
			frontinfo.setLeftmrID(mrID);
			frontinfo.setLeftmrinfo(mrinfomanager.getMrInfo(mrID));
			frontinfo.setLeftcompanytable(companytablemanager.getCompanyTable(mrID));
		    } else {
			frontinfo.setRightmrID(mrID);
			frontinfo.setRightmrinfo(mrinfomanager.getMrInfo(mrID));
			frontinfo.setRightcompanytable(companytablemanager.getCompanyTable(mrID));
		    }

				//�l�q���b�Z�[�W�J�E���g�r�p�k��
		    sqltxt = MR_MSG_COUNT_MAINSQL + MR_OPTION;
				
		    pstmt1 = conn.prepareStatement(sqltxt);
		    try {
			// �p�����[�^��ݒ�
			pstmt1.setString(1, drID);
			pstmt1.setString(2, rs.getString("sen_mr_id"));
			rs1   = pstmt1.executeQuery();
			while ( rs1.next() ) {
			    count = rs1.getInt("count");
			    if (mrFlag.equals("1")) {	//�l�q��
				frontinfo.setLeftMsgCount(count);
			    } else {			//�l�q�E
				frontinfo.setRightMsgCount(count);
			    }
			}
		    } finally {
			//rs.close();
			pstmt1.close();
		    }
		}
			
		//if ( count == 1 ) {
				//���b�Z�[�W���Z�b�g
		sqltxt = MR_MSG_MAINSQL + MR_OPTION;
				
		pstmt1 = conn.prepareStatement(sqltxt);
		try {
		    // �p�����[�^��ݒ�
		    pstmt1.setString(1, drID);
		    pstmt1.setString(2, mrID);
		    rs1   = pstmt1.executeQuery();
		    while ( rs1.next() ) {
			MsgManager msgmanager = new MsgManager(conn);
			if (mrFlag.equals("1")) {	//�l�q��
			    frontinfo.setLeftmsginfo(msgmanager.getDrRecvMessage(rs1.getString("message_header_id")));
			} else {			//�l�q�E
			    frontinfo.setRightmsginfo(msgmanager.getDrRecvMessage(rs1.getString("message_header_id")));
			}
			break;
		    }
		} finally {
		    //rs.close();
		    pstmt1.close();
		}
		//}
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	
    }
}
