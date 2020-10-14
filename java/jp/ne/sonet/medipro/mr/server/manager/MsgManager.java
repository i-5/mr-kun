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
 * <h3>�l�r�f�Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:37:51)
 * @author: 
 */
public class MsgManager {
    protected Connection conn;
	
    protected static final String ATTACH_FIEL_SQL
	= "SELECT attach_file, file_kbn "
	+ "FROM attach_file "
	+ "WHERE message_id = ? ORDER BY seq";
    protected static final String ATTACH_LINK_SQL
	= "SELECT url, honbun_text, picture, naigai_link_kbn "
	+ "FROM attach_link "
	+ "WHERE message_id = ? ORDER BY seq";

    //�ʑ���M���b�Z�[�W�p���C���r�p�k	
    protected static final String MESSAGE_MAINSQL
	= "SELECT "
	+ "mh.message_header_id mh_message_header_id, "
	+ "mb.message_id mb_message_id, "
	+ "mh.message_kbn mh_message_kbn, " 
	+ "mh.from_userid mh_from_userid, " 
	+ "mh.to_userid mh_to_userid, " 
	+ "TO_CHAR(mh.receive_time,'YYYY/MM/DD HH24:MI') recvtime, " 
	+ "mr.name mr_name, " 
	+ "st.name dr_name, " 
	+ "mb.title mb_title, " 
	+ "mb.message_honbun mb_message_honbun, " 
	+ "mb.url mb_url, " 
	+ "mb.jikosyokai mb_jikosyokai, "
	+ "mb.picture_cd mb_picture_cd, "
	+ "mb.call_naiyo_cd mb_call_naiyo_cd, "
	+ "ct.picture ct_picture, "
	+ "ct.picture_type ct_picture_type, " 
	+ "co.company_name co_company_name, " 
	+ "kin.kinmusaki_name kin_kinmusaki_name " 
	+ "FROM " 
	+ "message_header mh, " 
	+ "message_body mb, " 
	+ "mr mr," 
	+ "doctor dr, " 
	+ "company co, " 
	+ "kinmusaki kin, " 
	+ "catch_picture ct, "
	+ "sentaku_toroku st ";

///////////�c�q��M���b�Z�[�W�\�����
    protected static final String MESSAGE_DR_RECV_OPTIONALSQL
	= "WHERE "
	+ "mh.message_header_id = ? "
	+ "AND mh.message_id = mb.message_id "
	+ "AND mh.from_userid = mr.mr_id "
	+ "AND mh.to_userid = dr.dr_id "
	+ "AND mh.message_kbn = '1' "//1124 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id "
	+ "AND mr.company_cd = co.company_cd "
	+ "AND mb.picture_cd = ct.picture_cd(+) "
	+ "AND mh.from_userid = st.mr_id(+) "
	+ "AND mh.to_userid = st.dr_id(+) ";

///////////�c�q���M���b�Z�[�W�\�����
    protected static final String MESSAGE_DR_SEND_OPTIONALSQL
	= "WHERE " 
	+ "mh.message_header_id = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id "
	+ "AND mh.message_kbn = '2' "//1124 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "
	+ "AND mh.from_userid = st.dr_id(+) "
	+ "AND mh.to_userid = st.mr_id(+) ";
											
/////�l�q��M���b�Z�[�W�\������	
    protected static final String MESSAGE_MR_RECV_OPTIONALSQL
	= "WHERE " 
	+ "mh.message_header_id = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id "
	+ "AND (mh.message_kbn = '2' OR mh.message_kbn = '3' OR mh.message_kbn = '5' ) "//1124 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "
	+ "AND mh.from_userid = st.dr_id(+) "
	+ "AND mh.to_userid = st.mr_id(+) ";
											
///////////�l�q���M���b�Z�[�W�\�����
    protected static final String MESSAGE_MR_SEND_OPTIONALSQL
	= "WHERE " 
	+ "mh.message_header_id = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = mr.mr_id "
	+ "AND mh.to_userid = dr.dr_id " 
	+ "AND (mh.message_kbn = '1' OR mh.message_kbn = '4' ) "//1124 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "
	+ "AND mh.from_userid = st.mr_id(+) "
	+ "AND mh.to_userid = st.dr_id(+) ";

    protected static final String SEND_SAVE_MESSAGE_COUNT_SQL
	= "SELECT count(message_header_id) counter FROM message_header "
	+ "WHERE from_userid = ? AND send_status = '2'";

    protected static final String RECV_SAVE_MESSAGE_COUNT_SQL
	= "SELECT count(message_header_id) counter FROM message_header "
	+ "WHERE to_userid = ? AND receive_status = '2'";

    protected static final String RECV_MESSAGE_UPDATE
	= "UPDATE message_header SET receive_timed = SYSDATE "
	+ "WHERE message_header_id = ? AND  receive_timed IS NULL";

    //����M���b�Z�[�W�ꗗ���ۊǂa�n�w�p���C���r�p�k	
    protected static final String MESSAGE_LIST_MAINSQL
	= "SELECT "
	+ "mh.message_header_id mh_message_header_id, "
	+ "mb.message_id mb_message_id, "
	+ "mh.message_kbn mh_message_kbn, "
	+ "mh.from_userid mh_from_userid, "
	+ "mh.to_userid mh_to_userid, "
	+ "mh.send_status mh_send_status, "
	+ "mh.receive_status mh_receive_status, "
	+ "TO_CHAR(mh.receive_time,'YYYY/MM/DD HH24:MI') recvtime, "
	+ "TO_CHAR(mh.receive_timed,'YYYY/MM/DD HH24:MI:SS') recvtimed, "
	+ "TO_CHAR(mh.send_torikeshi_time,'YYYY/MM/DD HH24:MI:SS') sendtorikeshitime, "
	+ "TO_CHAR(mb.yuko_kigen,'YYYY/MM/DD') yukokigen, "
	+ "TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') - TO_DATE(TO_CHAR(mh.receive_time,'YYYYMMDD'),'YYYYMMDD') recvDay, "
	+ "mr.name mr_name, "
	+ "mr.name_kana mr_name_kana, "
	+ "sen.name dr_name, "
	+ "dr.name_kana dr_name_kana, " 
	+ "mb.title mb_title, " 
	+ "mb.jikosyokai mb_jikosyokai, " 
	+ "mb.url mb_url, "
	+ "co.company_name co_company_name, " 
	+ "kin.kinmusaki_name kin_kinmusaki_name, "
	+ "sen.target_rank sen_target_rank, "
	+ "TO_CHAR(SYSDATE,'YYYY/MM/DD') system_date "
	+ "FROM " 
	+ "message_header mh, " 
	+ "message_body mb, " 
	+ "mr mr," 
	+ "doctor dr, " 
	+ "company co, " 
	+ "kinmusaki kin, "
	+ "catch_picture ct, "//
	+ "sentaku_toroku sen ";

    //����M���b�Z�[�W�ꗗ���ۊǂa�n�w�p���C���r�p�k2
    /*1025 y-yamada add  MESSAGE_LIST_MAINSQL �͑���SQL��
    �g�ݍ��킹�Ďg�����ߎ����ꂽ�炠�������Ńg���u�����̂�
    �^�[�Q�b�g�����N�����擾����Ƃ��́@���������g�� */	
    protected static final String MESSAGE_LIST_MAINSQL2
	= "SELECT "
	+ "mh.message_header_id mh_message_header_id, "
	+ "mb.message_id mb_message_id, "
	+ "mh.message_kbn mh_message_kbn, "
	+ "mh.from_userid mh_from_userid, "
	+ "mh.to_userid mh_to_userid, "
	+ "mh.send_status mh_send_status, "
	+ "mh.receive_status mh_receive_status, "
	+ "TO_CHAR(mh.receive_time,'YYYY/MM/DD HH24:MI') recvtime, "
	+ "TO_CHAR(mh.receive_timed,'YYYY/MM/DD HH24:MI:SS') recvtimed, "
	+ "TO_CHAR(mh.send_torikeshi_time,'YYYY/MM/DD HH24:MI:SS') sendtorikeshitime, "
	+ "TO_CHAR(mb.yuko_kigen,'YYYY/MM/DD') yukokigen, "
	+ "TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') - TO_DATE(TO_CHAR(mh.receive_time,'YYYYMMDD'),'YYYYMMDD') recvDay, "
	+ "mr.name mr_name, "
	+ "mr.name_kana mr_name_kana, "
	+ "sen.name dr_name, "
	+ "dr.name_kana dr_name_kana, " 
	+ "mb.title mb_title, " 
	+ "mb.jikosyokai mb_jikosyokai, " 
	+ "mb.url mb_url, "
	+ "co.company_name co_company_name, " 
	+ "kin.kinmusaki_name kin_kinmusaki_name, "
	+ "mb.company_cd mb_company_cd,  "//1122 y-yamada add NO.58 (by sugiyama)
	+ "sen.target_rank sen_target_rank, "
//	+ "act.target_name act_target_name, "//1025 y-yamada add 1122 y-yamada del NO.58 (by sugiyama)
	+ "TO_CHAR(SYSDATE,'YYYY/MM/DD') system_date "
	+ "FROM " 
	+ "message_header mh, " 
	+ "message_body mb, " 
	+ "mr mr," 
	+ "doctor dr, " 
	+ "company co, " 
	+ "kinmusaki kin, "
	+ "catch_picture ct, "///
	+ "sentaku_toroku sen ";
	//+ "message_header_action mh_act, "//1120 y-yamada add NO.58
	//+ "action act "//1025 y-yamada add 1122 y-yamada del NO.58 (by sugiyama)

											
    //��t��M�ꗗ�I������
    protected static final String DR_RECV_MESSAGE_LIST_OPTIONALSQL
	= "WHERE " 
	+ "mh.to_userid = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = mr.mr_id " 
	+ "AND mh.to_userid = dr.dr_id " 
	+ "AND mh.message_kbn = '1' " //1110 y-yamada add NO.47 message_kbn=1�͈�t��M���b�Z�[�W
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd "//////
	+ "AND mh.from_userid = sen.mr_id(+) " 
	+ "AND mh.to_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //��t��M�ۊǂa�n�w�I������
    protected static final String DR_RECV_MESSAGE_SAVEBOX_OPTIONALSQL
	= "WHERE " 
	+ "mh.to_userid = ? " 
	+ "AND mh.receive_status <> '1' " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = mr.mr_id " 
	+ "AND mh.to_userid = dr.dr_id " 
	+ "AND mh.message_kbn = '1' " //1110 y-yamada add NO.47 message_kbn=1 �́@��t��M���b�Z�[�W
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd "/////////////
	+ "AND mh.from_userid = sen.mr_id(+) " 
	+ "AND mh.to_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //��t���M�ꗗ�I������
    protected static final String DR_SEND_MESSAGE_LIST_OPTIONALSQL
	= "WHERE " 
	+ "mh.from_userid = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id " 
	+ "AND mh.message_kbn = '2' "
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "/////////////
	+ "AND mh.to_userid = sen.mr_id(+) " 
	+ "AND mh.from_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //��t���M�ۊǂa�n�w�I������
    protected static final String DR_SEND_MESSAGE_SAVEBOX_OPTIONALSQL
	= "WHERE " 
	+ "mh.from_userid = ? "
	+ "AND mh.send_status <> '1' " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id " 
	+ "AND mh.message_kbn = '2'"
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "/////////////
	+ "AND mh.to_userid = sen.mr_id(+) " 
	+ "AND mh.from_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //�l�q��M�ꗗ�I������
    protected static final String MR_RECV_MESSAGE_LIST_OPTIONALSQL
	= "WHERE " 
	+ "mh.to_userid = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id "
    + "AND (message_kbn = '2' OR message_kbn = '3' OR message_kbn = '5')" //1114 y-yamada add NO.52
//	+ "AND mh.from_userid = sen.dr_id "//1110 y-yamada add NO.48  sentaku_toroku �e�[�u����
//	+ "AND mh.to_userid = sen.mr_id "  //               �o�^����Ă���f�[�^�̂ݑI������(1120���Ȃ��Ă��ǂ��炵��) 
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "/////////////
	+ "AND mh.to_userid = sen.mr_id(+) " 
	+ "AND mh.from_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //�l�q��M�ۊǂa�n�w�I������
    protected static final String MR_RECV_MESSAGE_SAVEBOX_OPTIONALSQL
	= "WHERE " 
	+ "mh.to_userid = ? " 
	+ "AND mh.receive_status <> '1' " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = dr.dr_id " 
	+ "AND mh.to_userid = mr.mr_id " 
    + "AND (message_kbn = '2'  OR message_kbn = '3' OR message_kbn = '5')" //1114 y-yamada add NO.52
//	+ "AND mh.from_userid = sen.dr_id "//1110 y-yamada add NO.48  sentaku_toroku �e�[�u����
//	+ "AND mh.to_userid = sen.mr_id "  //               �o�^����Ă���f�[�^�̂ݑI������(1120���Ȃ��Ă������炵��) 
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd(+) "/////////////
	+ "AND mh.to_userid = sen.mr_id(+) " 
	+ "AND mh.from_userid = sen.dr_id(+) "
	+ "ORDER BY mh.receive_time DESC ";

    //�l�q���M�ꗗ�I������
    protected static final String MR_SEND_MESSAGE_LIST_OPTIONALSQL
	=  "WHERE " 
	+ "mh.from_userid = ? " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = mr.mr_id " 
	+ "AND mh.to_userid = dr.dr_id " 
    + "AND (message_kbn = '1' OR message_kbn = '4')" //1114 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd "/////////////
	+ "AND mh.from_userid = sen.mr_id(+) " 
	+ "AND mh.to_userid = sen.dr_id(+) "
//	+ "AND act.target_rank  = sen.target_rank "//1025 y-yamada add 1121 del y-yamada NO.58 (by sugiyama)
//	+ "AND mb.company_cd=act.company_cd "// 1105 y-yamada add 1121 del y-yamada NO.58 (by sugiyama)

	
	+ "ORDER BY mh.receive_time DESC ";

    //�l�q���M�ۊǂa�n�w�I������
    protected static final String MR_SEND_MESSAGE_SAVEBOX_OPTIONALSQL
	=  "WHERE " 
	+ "mh.from_userid = ? " 
	+ "AND mh.send_status <> '1' " 
	+ "AND mh.message_id = mb.message_id " 
	+ "AND mh.from_userid = mr.mr_id " 
	+ "AND mh.to_userid = dr.dr_id " 
    + "AND (message_kbn = '1'  OR message_kbn = '4')" //1114 y-yamada add NO.52
	+ "AND dr.dr_id = kin.dr_id " 
	+ "AND mr.company_cd = co.company_cd " 
	+ "AND mb.picture_cd = ct.picture_cd "/////////////
	+ "AND mh.from_userid = sen.mr_id(+) " 
	+ "AND mh.to_userid = sen.dr_id(+) "
//	+ "AND sen.target_rank = act.target_rank "//1025 y-yamada add 1121 del y-yamada NO.58 (by sugiyama)
//	+ "AND mb.company_cd=act.company_cd "// 1105 y-yamada add 1121 del y-yamada NO.58 (by sugiyama)
	+ "ORDER BY mh.receive_time DESC ";

    //�R�~���j�P�[�V��������p���C���r�p�k
    protected static final String COMMUNICATION_HISTORY_MAINSQL
	= "SELECT " 
	+ "mh.message_header_id mh_message_header_id, " 
	+ "mb.message_id mb_message_id, " 
	+ "mh.message_kbn mh_message_kbn, " 
	+ "mh.from_userid mh_from_userid, " 
	+ "mh.to_userid mh_to_userid, " 
	+ "TO_CHAR(mh.receive_time,'YYYY/MM/DD HH24:MI') recvtime, " 
	+ "TO_CHAR(mh.receive_timed,'YYYY/MM/DD HH24:MI:SS') recvtimed, " 
	+ "TO_CHAR(mh.send_torikeshi_time,'YYYY/MM/DD HH24:MI:SS') sendtorikeshitime, " 
	+ "TO_CHAR(mb.yuko_kigen,'YYYY/MM/DD HH24:MI:SS') yukokigen, " 
	+ "TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') - TO_DATE(TO_CHAR(mh.receive_time,'YYYYMMDD'),'YYYYMMDD') recvDay, " 
	+ "mb.title mb_title, " 
	+ "TO_CHAR(SYSDATE,'YYYY/MM/DD HH24:MI:SS') system_date " 
	+ "FROM " 
	+ "message_header mh, " 
	+ "message_body mb " 
	+ "WHERE " 
//	+ "((mh.from_userid = ? AND mh.to_userid = ? AND mh.send_status <> '3') " 
//	+ "OR (mh.to_userid = ? AND mh.from_userid = ? AND mh.receive_status <> '3')) "
	+ "((mh.from_userid = ? AND mh.to_userid = ? AND mh.send_status <> '3' AND (mh.message_kbn=1 OR mh.message_kbn=4)   ) " //1114y-yamada �l�q����c�q NO.52
	+ "OR (mh.to_userid = ? AND mh.from_userid = ? AND mh.receive_status <> '3'  AND (mh.message_kbn=2 OR mh.message_kbn=5)  )) "//1114y-yamada �c�q����l�q NO.52
	+ "AND mh.message_id = mb.message_id ";

    protected static final String READ_MESSAGE_DR_SQL
	= "select doctor.name AS name, title, receive_time "
	+ "from message_header, message_body, mr, doctor "
	+ "where from_userid = ? "
	+ "and from_userid = mr.mr_id "
	+ "and to_userid = doctor.dr_id "
	+ "and message_header.message_id = message_body.message_id "
	+ "and receive_timed > mr.login_time ";
    
    /**
     * MsgManager �R���X�g���N�^�[�E�R�����g�B
     */
    public MsgManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�Y�t�t�@�C���m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 03:27:52)
     * @return java.util.Enumeration (AttachFileTable)
     * @param messageID java.lang.String
     */
    public Enumeration getAttachFileTable(String messageID) {

	ResultSet rs;
	PreparedStatement pstmt;
	Vector attachfiletablelist = new Vector();
	String sqltxt;
	
	//�Y�t�t�@�C�����̎擾
	try {

	    //���b�Z�[�W���Z�b�g
	    sqltxt = ATTACH_FIEL_SQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, messageID);
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    AttachFileTable attachfiletable = new AttachFileTable();
		    attachfiletable.setAttachFile(rs.getString("attach_file"));
		    attachfiletable.setFileKbn(rs.getString("file_kbn"));
			  	
		    attachfiletablelist.addElement(attachfiletable);
		}
				
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return attachfiletablelist.elements();
    }

    /**
     * <h3>�Y�t�����N�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 03:27:52)
     * @return java.util.Enumeration (AttachLinkTable)
     * @param messageID java.lang.String
     */
    public Enumeration getAttachLinkTable(String messageID) {

	ResultSet rs;
	PreparedStatement pstmt;
	Vector attachlinktablelist = new Vector();
	String sqltxt;
	
	//�Y�t�����N���̎擾
	try {

	    //�Y�t�����N���r�p�k��
	    sqltxt = ATTACH_LINK_SQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, messageID);
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    AttachLinkTable attachlinktable = new AttachLinkTable();
		    attachlinktable.setUrl(rs.getString("url"));
		    attachlinktable.setHonbuText(rs.getString("honbun_text"));
		    attachlinktable.setPicture(rs.getString("picture"));
		    attachlinktable.setNaigaiLinkKbn(rs.getString("naigai_link_kbn"));
			  	
		    attachlinktablelist.addElement(attachlinktable);
		}
				
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return attachlinktablelist.elements();
    }

    /**
     * <h3>��t��M�ʂl�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:41:04)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param messageHeaderID java.lang.String
     * @param mode java.lang.String
     */
    public MsgInfo getDrRecvMessage(String messageHeaderID) {

	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg = null;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	try {
	    //�r�p�k��
	    sqltxt = MESSAGE_MAINSQL + MESSAGE_DR_RECV_OPTIONALSQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, messageHeaderID);
		rs   = pstmt.executeQuery();
		
		while ( rs.next() ) {
		    fromname = rs.getString("mr_name");
		    toname = rs.getString("dr_name");
		    fromcompanyname = rs.getString("co_company_name");
		    tocompanyname = rs.getString("kin_kinmusaki_name");		
				
		    msg = new MsgInfo(rs.getString("mh_message_header_id"),
				      rs.getString("mh_message_kbn"),
				      rs.getString("mh_from_userid"),
				      rs.getString("mh_to_userid"),
				      rs.getString("recvtime"),
				      fromname,
				      toname,
				      fromcompanyname,
				      tocompanyname,
				      rs.getString("mb_message_id"));
		    header = msg.getHeader();
		    body   = msg.getBody();
			
		    body.setPicture(rs.getString("ct_picture"));
		    body.setPictureType(rs.getString("ct_picture_type"));
		    body.setTitle(rs.getString("mb_title"));
		    body.setHonbunText(rs.getString("mb_message_honbun"));
		    body.setUrl(rs.getString("mb_url"));
		    body.setJikosyokai(rs.getString("mb_jikosyokai"));
		    body.setAttachfiletable(getAttachFileTable(rs.getString("mb_message_id")));
		    body.setAttachiLinkPicture(getAttachLinkTable(rs.getString("mb_message_id")));
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
		
	return msg;

    }

    /**
     * <h3>��t��M�l�r�f�w�b�_�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߑO 02:39:37)
     * @return java.util.Enumeration (MsgInfo)
     * @param toUser java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     * @param status java.lang.String
     */
    public Enumeration getDrRecvMsgList(String toUser,
					String sortKey,
					String rowType,
					String status) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;
	OrderedSet MsgInfoList = new OrderedSet();
	BinaryPredicate pred;

	pred = getDrRecvSortPredicate(sortKey, rowType);
	MsgInfoList = new OrderedSet(pred,true);

	try {
	    //�萔�}�X�^�[�e�[�u�����̎擾�i��M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("RECEIVEMAX");
		
	    int teisuNaiyou1 = 0;
	    if ( status.equals("1")) {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo1()).intValue();
		//�ꗗ�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + DR_RECV_MESSAGE_LIST_OPTIONALSQL;
	    }
	    else {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();
		//�ۊǂa�n�w�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + DR_RECV_MESSAGE_SAVEBOX_OPTIONALSQL;
	    }	
		
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, toUser);
		// SQL ���s
		rs   = pstmt.executeQuery();
		for (int i = 0; i < teisuNaiyou1; i++){
		    if ( rs.next() ) {
				if ( rs.getString("mh_receive_status").equals(status) ) {
					fromname = rs.getString("mr_name");
					toname = rs.getString("dr_name");
					fromcompanyname = rs.getString("co_company_name");
					tocompanyname = rs.getString("kin_kinmusaki_name");
						
					msg = new MsgInfo(rs.getString("mh_message_header_id"),
									  rs.getString("mh_message_kbn"),
									  rs.getString("mh_from_userid"),
									  rs.getString("mh_to_userid"),
									  rs.getString("recvtime"),
									  fromname,
									  toname,
									  fromcompanyname,
									  tocompanyname,
									  rs.getString("mb_message_id"));
					header = msg.getHeader();
					body   = msg.getBody();

					String yukokigen = "";
					if (rs.getString("yukokigen") != null) {
						yukokigen = rs.getString("yukokigen");
					}

					if (rs.getString("yukokigen") != null &&
						yukokigen.compareTo(rs.getString("system_date")) < 0 &&
						rs.getString("recvtimed") == null &&
						rs.getString("sendtorikeshitime") == null) {
						continue;
					} else if (rs.getString("recvtimed") == null &&
							   rs.getString("sendtorikeshitime") == null &&
							   (rs.getString("yukokigen") == null ||
								yukokigen.compareTo(rs.getString("system_date")) >= 0)
							   && !rs.getString("mh_message_kbn").equals("3")) {
						header.setNoRead("��");
						header.setNoReadDay(rs.getInt("recvDay"));
					} else {
						header.setNoRead("");
						header.setNoReadDay(0);
					}

					header.setFromNameKana(rs.getString("mr_name_kana"));
					header.setToNameKana(rs.getString("dr_name_kana"));
					body.setTitle(rs.getString("mb_title"));
					body.setJikosyokai(rs.getString("mb_jikosyokai"));
					body.setUrl(rs.getString("mb_url"));
					body.setAttachiLinkPicture(getAttachLinkTable(rs.getString("mb_message_id")));

					MsgInfoList.add(msg);
				}
					
		    } else {
				break;
		    }
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return MsgInfoList.elements();
    }

    /**
     * <h3>��t��M�l�r�f�\�[�g�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getDrRecvSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_RECV_RECEIVE_TIME;
	}
	if (rowType == null) {
	    rowType = "D";
	}

	BinaryPredicate pred = new ReceiveTimeAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_RECV_RECEIVE_TIME)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimeAscendPredicate();
	    } else {
		pred = new ReceiveTimeDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_RECV_FROM_USER)) {
	    if (rowType.equals("A")) {
		pred = new JikosyokaiAscendPredicate();
	    } else {
		pred = new JikosyokaiDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_RECV_RECEIVE_TIMED)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimedAscendPredicate();
	    } else {
		pred = new ReceiveTimedDescendPredicate();
	    }
	}
	
	return pred;
	
    }

    /**
     * <h3>��t���M�ʂl�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:41:04)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param messageHeaderID java.lang.String
     */
    public MsgInfo getDrSendMessage(String messageHeaderID) {
	
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg = null;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	try {
	    //�r�p�k��
	    sqltxt = MESSAGE_MAINSQL + MESSAGE_DR_SEND_OPTIONALSQL;

	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, messageHeaderID);
		rs   = pstmt.executeQuery();
		
		while ( rs.next() ) {
		    fromname = rs.getString("dr_name");
		    toname = rs.getString("mr_name");
		    fromcompanyname = rs.getString("kin_kinmusaki_name");	
		    tocompanyname = rs.getString("co_company_name");
				
		    msg = new MsgInfo(rs.getString("mh_message_header_id"),
				      rs.getString("mh_message_kbn"),
				      rs.getString("mh_from_userid"),
				      rs.getString("mh_to_userid"),
				      rs.getString("recvtime"),
				      fromname,
				      toname,
				      fromcompanyname,
				      tocompanyname,
				      rs.getString("mb_message_id"));
		    header = msg.getHeader();
		    body   = msg.getBody();
			
		    body.setTitle(rs.getString("mb_title"));
		    body.setHonbunText(rs.getString("mb_message_honbun"));
		    body.setUrl(rs.getString("mb_url"));
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
		
	return msg;

    }

    /**
     * <h3>��t���M�l�r�f�w�b�_�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:45:40)
     * @param fromUser java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     * @param status java.lang.String
     * @return java.util.Enumeration (MsgInfo)
     */
    public Enumeration getDrSendMsgList(String fromUser,
					String sortKey,
					String rowType,
					String status) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	OrderedSet MsgInfoList = new OrderedSet();
	BinaryPredicate pred;

	pred = getDrSendSortPredicate(sortKey, rowType);
	MsgInfoList = new OrderedSet(pred,true);
	
	try {
			
	    //�萔�}�X�^�[�e�[�u�����̎擾�i��M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("SENDMAX");
		
	    int teisuNaiyou1 = 0;
	    if ( status.equals("1")) {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo1()).intValue();
		//�ꗗ�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + DR_SEND_MESSAGE_LIST_OPTIONALSQL;
	    } else {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();
		//�ۊǂa�n�w�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + DR_SEND_MESSAGE_SAVEBOX_OPTIONALSQL;
	    }		
			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, fromUser);
		// SQL ���s
		rs   = pstmt.executeQuery();
		for (int i = 0; i < teisuNaiyou1; i++){
		    if ( rs.next() ) {
			if ( rs.getString("mh_send_status").equals(status) ) {
			    fromname = rs.getString("dr_name");
			    toname = rs.getString("mr_name");
			    fromcompanyname = rs.getString("kin_kinmusaki_name");
			    tocompanyname = rs.getString("co_company_name");
						
			    msg = new MsgInfo(rs.getString("mh_message_header_id"),
					      rs.getString("mh_message_kbn"),
					      rs.getString("mh_from_userid"),
					      rs.getString("mh_to_userid"),
					      rs.getString("recvtime"),
					      fromname,
					      toname,
					      fromcompanyname,
					      tocompanyname,
					      rs.getString("mb_message_id"));
			    header = msg.getHeader();
			    body   = msg.getBody();

			    String yukokigen = "";
			    if ( rs.getString("yukokigen") != null ) {
				yukokigen = rs.getString("yukokigen");
			    }
						
			    if (rs.getString("recvtimed") == null &&
				rs.getString("sendtorikeshitime") == null
				&& (rs.getString("yukokigen") == null ||
				    yukokigen.compareTo(rs.getString("system_date")) >= 0)
				&& rs.getString("mh_message_kbn").equals("3") == false) {
				header.setNoRead("��");
			    } else {
				header.setNoRead("");
			    }

			    header.setFromNameKana(rs.getString("dr_name_kana"));
			    header.setToNameKana(rs.getString("mr_name_kana"));
			    body.setTitle(rs.getString("mb_title"));
						
			    MsgInfoList.add(msg);
						
			}
		    } else {
			break;
		    }
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return MsgInfoList.elements();
    }

    /**
     * <h3>��t���M�l�r�f�\�[�g�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getDrSendSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_SEND_RECEIVE_TIME;
	}
	if (rowType == null) {
	    rowType = "D";
	}

	BinaryPredicate pred = new ToNameAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_SEND_TO_USER)) {
	    if (rowType.equals("A")) {
		pred = new ToNameAscendPredicate();
	    } else {
		pred = new ToNameDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_RECEIVE_TIME)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimeAscendPredicate();
	    } else {
		pred = new ReceiveTimeDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_RECEIVE_TIMED)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimedAscendPredicate();
	    } else {
		pred = new ReceiveTimedDescendPredicate();
	    }
	}
	
	return pred;
	
    }

    /**
     * <h3>�O��(�V����)���ǎ�M�l�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:20:39)
     * @return java.lang.String
     * @param toUserID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String getLastRecvMsg(String toUserID, String messageHeaderID) {

	String sqltxt;
	String msgheaderid = null;
	
	
	/*******************************/
	/*������BOX���ꗗ�����ʁ@�@�@�@*/
	/*******************************/
	String sqltxthanbetu;
	String receiveStatus = null;
	String messageKbn = null;
	PreparedStatement pstmt;
	try
	{
		sqltxthanbetu = "SELECT receive_status ,message_kbn ";
	    sqltxthanbetu = sqltxthanbetu + "FROM message_header ";
	    sqltxthanbetu = sqltxthanbetu + "WHERE message_header_id = ? ";


		
	    ResultSet rs;
	    pstmt = conn.prepareStatement(sqltxthanbetu);
	    try {
			pstmt.setString(1, messageHeaderID);
			rs   = pstmt.executeQuery();
			if ( rs.next() ) {
			    receiveStatus = rs.getString("receive_status");
			    messageKbn = rs.getString("message_kbn");
			}
	    } finally {
			pstmt.close();
	    }
	} catch (SQLException e) {
		    throw new MrException(e);
	
	
	}
	
//System.out.println("receiveStatus�O�@=�@"+receiveStatus);	
//System.out.println("messageKbn�O�@=�@"+messageKbn);	
	
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "MIN(mh.message_header_id) headrid ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "message_header mh, ";
	    sqltxt = sqltxt + "message_body mb, ";
	    sqltxt = sqltxt + "mr mr, ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "doctor dr, ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "company co, ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "kinmusaki kin, ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "catch_picture ct, ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "sentaku_toroku st ";//1127 y-yamada add NO.62,63
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "mh.to_userid = '" + toUserID + "' ";
	    sqltxt = sqltxt + "AND mh.message_header_id > '" + messageHeaderID + "' ";
	    sqltxt = sqltxt + "AND mh.receive_timed IS NULL ";
	    sqltxt = sqltxt + "AND mh.send_torikeshi_time IS NULL ";
	    sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
	    sqltxt = sqltxt + "AND ((mb.yuko_kigen IS NULL) OR ( TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD') ) ) ";
		if( messageKbn.equals("2") || messageKbn.equals("3") || messageKbn.equals("5"))									//1122 y-yamada add NO.62,63
		{//MR�̎�M												//1122 y-yamada add NO.62,63
			//�����͂l�q��M���b�Z�[�W�\�������Ɠ����@1127 y-yamada add NO.62,63
			sqltxt = sqltxt + "AND mh.from_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND (mh.message_kbn = '2' OR mh.message_kbn = '3' OR mh.message_kbn = '5') ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id  ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd  ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd(+)  ";
			sqltxt = sqltxt + "AND mh.from_userid = st.dr_id(+)  ";
			sqltxt = sqltxt + "AND mh.to_userid = st.mr_id(+) ";
		}														//1122 y-yamada add NO.62,63
		else													//1122 y-yamada add NO.62,63
		{//DR�̎�M												//1122 y-yamada add NO.62,63
			//�����͂c�q��M���b�Z�[�W�\���Ɠ���
			sqltxt = sqltxt + "AND mh.from_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.message_kbn = '1' ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd ";
			sqltxt = sqltxt + "AND mh.from_userid = st.mr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.dr_id(+) ";
		}														//1122 y-yamada add NO.62,63
		if(receiveStatus.equals("2"))
		{//��M�{�b�N�X
		    sqltxt = sqltxt + "AND mh.receive_status = '2' ";
		}
		else
		{//��M�ꗗ
		    sqltxt = sqltxt + "AND mh.receive_status = '1' ";
		}
	    sqltxt = sqltxt + "ORDER BY mh.message_header_id ";

	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		if ( rs.next() ) {
		    msgheaderid = rs.getString("headrid");
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return msgheaderid;
    }

    /**
     * <h3>�O��(�V����)���M�l�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:20:39)
     * @return java.lang.String
     * @param toUserID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String getMrLastSendMsg(String toUserID, String messageHeaderID) {
	return this.getDrLastSendMsg(toUserID, messageHeaderID, false);
    }

    public String getDrLastSendMsg(String toUserID,
				   String messageHeaderID,
				   boolean flag ) {
	String sqltxt;
	String msgheaderid = null;
	
	/*******************************/
	/*������BOX���ꗗ�����ʁ@�@�@�@*/
	/*******************************/
	String sqltxthanbetu;
	String sendStatus = null;
	PreparedStatement pstmt;
	try
	{
		sqltxthanbetu = "SELECT send_status ";
	    sqltxthanbetu = sqltxthanbetu + "FROM message_header ";
	    sqltxthanbetu = sqltxthanbetu + "WHERE message_header_id = ? ";


		
	    ResultSet rs;
	    pstmt = conn.prepareStatement(sqltxthanbetu);
	    try {
			pstmt.setString(1, messageHeaderID);
			rs   = pstmt.executeQuery();
			if ( rs.next() ) {
			    sendStatus = rs.getString("send_status");
			}
	    } finally {
			pstmt.close();
	    }
	} catch (SQLException e) {
		    throw new MrException(e);
	
	
	}
	
//System.out.println("sendStatus�O�@=�@"+sendStatus);	
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "MIN(mh.message_header_id) headrid ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "message_header mh, ";
	    sqltxt = sqltxt + "message_body mb, "; //1127 y-yamada add NO.60
	    sqltxt = sqltxt + "mr mr, ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "doctor dr, ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "company co, ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "kinmusaki kin, ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "catch_picture ct, ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "sentaku_toroku st ";//1127 y-yamada add NO.60
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "mh.from_userid = '" + toUserID + "' ";
	    sqltxt = sqltxt + "AND mh.send_delete_time IS NULL ";
	    sqltxt = sqltxt + "AND mh.message_kbn <> '3' ";
	    sqltxt = sqltxt + "AND mh.message_header_id > '" + messageHeaderID + "' ";
	    if (flag) {
			//�c�q�̑��M
	//		sqltxt = sqltxt + "AND message_header.message_kbn <> '4' ";//1110 y-yamada del NO.47
			//sqltxt = sqltxt + "AND message_header.message_kbn < '4' ";//1110 y-yamada add NO.47 �ړ����b�Z�[�W��4,5
			//�c�q�̑��M���b�Z�[�W�\����ʂƓ��������@1127 y-yamada add 
			sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
			sqltxt = sqltxt + "AND mh.from_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.message_kbn = '2' ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd(+) ";
			sqltxt = sqltxt + "AND mh.from_userid = st.dr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.mr_id(+) ";
	    }
	    else
	    {//�l�q�̑��M���b�Z�[�W�Ɠ��������@1127 y-yamada add 
			sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
			sqltxt = sqltxt + "AND mh.from_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND (mh.message_kbn = '1' OR mh.message_kbn = '4') ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd ";
			sqltxt = sqltxt + "AND mh.from_userid = st.mr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.dr_id(+) ";
	    
	    }
	    if(sendStatus.equals("2"))
	    {//�{�b�N�X
		    sqltxt = sqltxt + "AND mh.send_status = '2' ";
		}
		else
		{//�ꗗ
		    sqltxt = sqltxt + "AND mh.send_status = '1' ";
		}
	    sqltxt = sqltxt + "ORDER BY mh.message_header_id ";
	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		if ( rs.next() ) {
		    msgheaderid = rs.getString("headrid");
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return msgheaderid;
    }

    /**
     * <h3>�l�q��M�ʂl�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:41:04)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param messageHeaderID java.lang.String
     */
    public MsgInfo getMrRecvMessage(String messageHeaderID) {

	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg = null;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	try {
	    //�r�p�k��
	    sqltxt = MESSAGE_MAINSQL + MESSAGE_MR_RECV_OPTIONALSQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, messageHeaderID);
		rs   = pstmt.executeQuery();
			
		while ( rs.next() ) {
		    fromname = rs.getString("dr_name");
		    toname = rs.getString("mr_name");
		    fromcompanyname = rs.getString("kin_kinmusaki_name");
		    tocompanyname = rs.getString("co_company_name");
				
		    msg = new MsgInfo(rs.getString("mh_message_header_id"),
				      rs.getString("mh_message_kbn"),
				      rs.getString("mh_from_userid"),
				      rs.getString("mh_to_userid"),
				      rs.getString("recvtime"),
				      fromname,
				      toname,
				      fromcompanyname,
				      tocompanyname,
				      rs.getString("mb_message_id"));
		    header = msg.getHeader();
		    body   = msg.getBody();
				
		    body.setTitle(rs.getString("mb_title"));
		    body.setHonbunText(rs.getString("mb_message_honbun"));
		    body.setUrl(rs.getString("mb_url"));
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
		
	return msg;

    }

    /**
     * <h3>�l�q��M�l�r�f�w�b�_�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:45:40)
     * @param toUser java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     * @param status java.lang.String
     * @return java.util.Enumeration (MsgInfo)
     */
    public Enumeration getMrRecvMsgList(String toUser,
					String sortKey,
					String rowType,
					String status) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;
	OrderedSet MsgInfoList = new OrderedSet();
	BinaryPredicate pred;

	pred = getMrRecvSortPredicate(sortKey, rowType);
	MsgInfoList = new OrderedSet(pred,true);

	try {
	    //�萔�}�X�^�[�e�[�u�����̎擾�i��M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("RECEIVEMAX");
		
	    int teisuNaiyou1 = 0;
	    if ( status.equals("1")) {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo1()).intValue();
		//�ꗗ�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + MR_RECV_MESSAGE_LIST_OPTIONALSQL;
	    }
	    else {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();
		//�ۊǂa�n�w�p�r�p�k��
		sqltxt = MESSAGE_LIST_MAINSQL + MR_RECV_MESSAGE_SAVEBOX_OPTIONALSQL;
	    }			

	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, toUser);
		// SQL ���s
		rs   = pstmt.executeQuery();
		for (int i = 0; i < teisuNaiyou1; i++){
		    if ( rs.next() ) {

			if ( rs.getString("mh_receive_status").equals(status) ) {
			    fromname = rs.getString("dr_name");
			    toname = rs.getString("mr_name");
			    fromcompanyname = rs.getString("kin_kinmusaki_name");
			    tocompanyname = rs.getString("co_company_name");
						
			    msg = new MsgInfo(rs.getString("mh_message_header_id"),
					      rs.getString("mh_message_kbn"),
					      rs.getString("mh_from_userid"),
					      rs.getString("mh_to_userid"),
					      rs.getString("recvtime"),
					      fromname,
					      toname,
					      fromcompanyname,
					      tocompanyname,
					      rs.getString("mb_message_id"));
			    header = msg.getHeader();
			    body   = msg.getBody();

			    String yukokigen = "";
			    if ( rs.getString("yukokigen") != null ) {
				yukokigen = rs.getString("yukokigen");
			    }
						
			    if (rs.getString("recvtimed") == null &&
				rs.getString("sendtorikeshitime") == null &&
				(rs.getString("yukokigen") == null ||
				 yukokigen.compareTo(rs.getString("system_date")) >= 0)) {
				header.setNoRead("��");
			    } else {
				header.setNoRead("");
			    }

			    header.setFromNameKana(rs.getString("dr_name_kana"));
			    header.setToNameKana(rs.getString("mr_name_kana"));
			    body.setTitle(rs.getString("mb_title"));
					
			    MsgInfoList.add(msg);
			}
		    } else {
			break;
		    }
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return MsgInfoList.elements();
    }

    /**
     * <h3>�l�q��M�l�r�f�\�[�g�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getMrRecvSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_RECV_RECEIVE_TIME;
	}
	if (rowType == null) {
	    rowType = "D";
	}

	BinaryPredicate pred = new ReceiveTimeAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_RECV_RECEIVE_TIME)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimeAscendPredicate();
	    } else {
		pred = new ReceiveTimeDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_RECV_FROM_USER)) {
	    if (rowType.equals("A")) {
		pred = new FromNameAscendPredicate();
	    } else {
		pred = new FromNameDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_RECV_RECEIVE_TIMED)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimedAscendPredicate();
	    } else {
		pred = new ReceiveTimedDescendPredicate();
	    }
	}
	
	return pred;
	
    }

    /**
     * <h3>�l�q���M�ʂl�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:41:04)
     * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
     * @param messageHeaderID java.lang.String
     * @param mode java.lang.String
     */
    public MsgInfo getMrSendMessage(String messageHeaderID) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg = null;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	try {
	    //�r�p�k��
	    sqltxt = MESSAGE_MAINSQL + MESSAGE_MR_SEND_OPTIONALSQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		
		
		pstmt.setString(1, messageHeaderID);
		rs   = pstmt.executeQuery();
		
		while ( rs.next() ) {
		    fromname = rs.getString("mr_name");
		    toname = rs.getString("dr_name");
		    fromcompanyname = rs.getString("co_company_name");
		    tocompanyname = rs.getString("kin_kinmusaki_name");	
		    
				
		    msg = new MsgInfo(rs.getString("mh_message_header_id"),
				      rs.getString("mh_message_kbn"),
				      rs.getString("mh_from_userid"),
				      rs.getString("mh_to_userid"),
				      rs.getString("recvtime"),
				      fromname,
				      toname,
				      fromcompanyname,
				      tocompanyname,
				      rs.getString("mb_message_id"));
		    header = msg.getHeader();
		    body   = msg.getBody();

		    body.setCallNaiyoCD(rs.getString("mb_call_naiyo_cd"));
		    body.setPictureCD(rs.getString("mb_picture_cd"));
		    body.setPicture(rs.getString("ct_picture"));
		    body.setPictureType(rs.getString("ct_picture_type"));
		    body.setTitle(rs.getString("mb_title"));
		    body.setHonbunText(rs.getString("mb_message_honbun"));
		    body.setUrl(rs.getString("mb_url"));
		    body.setJikosyokai(rs.getString("mb_jikosyokai"));

		    body.setAttachfiletable(getAttachFileTable(rs.getString("mb_message_id")));
		    body.setAttachiLinkPicture(getAttachLinkTable(rs.getString("mb_message_id")));
				
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
		
	return msg;

    }

    /**
     * <h3>�l�q���M�l�r�f�w�b�_�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߑO 02:39:37)
     * @return java.util.Enumeration (MsgInfo)
     * @param fromUser java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     * @param status java.lang.String
     */
    public Enumeration getMrSendMsgList(String fromUser,
					String sortKey,
					String rowType,
					String status) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	OrderedSet MsgInfoList = new OrderedSet();
	BinaryPredicate pred;

	pred = getMrSendSortPredicate(sortKey, rowType);
	MsgInfoList = new OrderedSet(pred,true);
	
	
	try {
	    //�萔�}�X�^�[�e�[�u�����̎擾�i��M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("SENDMAX");
		
	    int teisuNaiyou1 = 0;
	    if ( status.equals("1")) {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo1()).intValue();
		//�ꗗ�p�r�p�k��
		//sqltxt = MESSAGE_LIST_MAINSQL + MR_SEND_MESSAGE_LIST_OPTIONALSQL;
		
		sqltxt = MESSAGE_LIST_MAINSQL2 + MR_SEND_MESSAGE_LIST_OPTIONALSQL; //1025 y-yamada add
	    } else {
		teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();
		//�ۊǂa�n�w�p�r�p�k��
		//sqltxt = MESSAGE_LIST_MAINSQL + MR_SEND_MESSAGE_SAVEBOX_OPTIONALSQL;
		sqltxt = MESSAGE_LIST_MAINSQL2 + MR_SEND_MESSAGE_SAVEBOX_OPTIONALSQL; //1025 y-yamada add
	    }		
	    //�r�p�k��
	    //	    sqltxt = MESSAGE_LIST_MAINSQL + MR_SEND_MESSAGE_LIST_OPTIONALSQL;

	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);

	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, fromUser);
		// SQL ���s
		rs   = pstmt.executeQuery();
		for (int i = 0; i < teisuNaiyou1; i++){
		    if ( rs.next() ) {
			if ( rs.getString("mh_send_status").equals(status) ) {
			    fromname = rs.getString("mr_name");
			    toname = rs.getString("dr_name");
			    fromcompanyname = rs.getString("co_company_name");
			    tocompanyname = rs.getString("kin_kinmusaki_name");
						
			    msg = new MsgInfo(rs.getString("mh_message_header_id"),
					      rs.getString("mh_message_kbn"),
					      rs.getString("mh_from_userid"),
					      rs.getString("mh_to_userid"),
					      rs.getString("recvtime"),
					      fromname,
					      toname,
					      fromcompanyname,
					      tocompanyname,
					      rs.getString("mb_message_id"));
			    header = msg.getHeader();
			    body   = msg.getBody();

			    String yukokigen = "";
			    if ( rs.getString("yukokigen") != null ) {
				yukokigen = rs.getString("yukokigen");
			    }

//			    if (rs.getString("recvtimed") == null &&
//				rs.getString("sendtorikeshitime") == null &&
//				(rs.getString("yukokigen") == null ||
//				 yukokigen.compareTo(rs.getString("system_date")) >= 0) 
//				&& rs.getString("mh_message_kbn").equals("3") == false) 
			    if (rs.getString("recvtimed") == null )//1221 ���̏����ύX y-yamada
				{
				header.setNoRead("��");
				header.setNoReadDay(rs.getInt("recvDay"));
							
			    } else {
				header.setNoRead("");
				header.setNoReadDay(0);
			    }

			    header.setTargetRank(rs.getString("sen_target_rank"));
//System.out.println("�n�܂�");
//System.out.println("sen_target_rank�@=�@"+rs.getString("sen_target_rank"));
			    
/**********************************/
//targetrank��null�ł͂Ȃ�������^�[�Q�b�g�����N�����擾����
//1121 y-yamada add  NO.58 (by sugiyama) 
//System.out.println("mb_company_cd�@=�@"+rs.getString("mb_company_cd"));
			    if( rs.getString("sen_target_rank") != null  )
			    {
					TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
					String target_name = tantoinfomanager.getTargetName( rs.getString("sen_target_rank") ,
																		 rs.getString("mb_company_cd")  );
					header.setTargetName(target_name );
					
				}
//*********************************/
//System.out.println("�I���");

			    //header.setTargetName(rs.getString("act_target_name"));//1025 y-yamada add 1121 y-yamada add  NO.58 (by sugiyama)
			    header.setFromNameKana(rs.getString("mr_name_kana"));
			    header.setToNameKana(rs.getString("dr_name_kana"));
			    body.setTitle(rs.getString("mb_title"));
					
			    MsgInfoList.add(msg);

			}
		    } else {
			break;
		    }
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return MsgInfoList.elements();
    }

    /**
     * <h3>�l�q����M�l�r�f�w�b�_�m�W���n�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߑO 02:39:37)
     * @return java.util.Enumeration (MsgInfo)
     * @param mrID java.lang.String
     * @param drID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     * @param status java.lang.String
     */
    public Enumeration getMrSendRecvMsgList(String mrID,
					    String drID,
					    String sortKey,
					    String rowType) {
	ResultSet rs;
	PreparedStatement pstmt;
	MsgInfo msg;
	MsgHeaderInfo header;
	MsgBodyInfo body;	
	String sqltxt;
	String fromname;
	String toname;
	String fromcompanyname;
	String tocompanyname;

	OrderedSet MsgInfoList = new OrderedSet();
	BinaryPredicate pred;

	pred = getMrSendRecvSortPredicate(sortKey, rowType);
	MsgInfoList = new OrderedSet(pred,true);
	
	try {
	    //�r�p�k��
	    sqltxt = COMMUNICATION_HISTORY_MAINSQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, mrID);
		pstmt.setString(2, drID);
		pstmt.setString(3, mrID);
		pstmt.setString(4, drID);
		// SQL ���s
		rs   = pstmt.executeQuery();

		while ( rs.next() ) {
		    msg = new MsgInfo(rs.getString("mh_message_header_id"),
				      rs.getString("mh_message_kbn"),
				      rs.getString("mh_from_userid"),
				      rs.getString("mh_to_userid"),
				      rs.getString("recvtime"),
				      "",
				      "",
				      "",
				      "",
				      rs.getString("mb_message_id"));
		    header = msg.getHeader();
		    body   = msg.getBody();

		    String yukokigen = "";
		    if ( rs.getString("yukokigen") != null ) {
			yukokigen = rs.getString("yukokigen");
		    }
				
		    if (rs.getString("recvtimed") == null &&
			rs.getString("sendtorikeshitime") == null &&
			rs.getString("mh_message_kbn").equals("3") == false) {
			header.setNoRead("��");
			header.setNoReadDay(rs.getInt("recvDay"));
		    }
		    else {
			header.setNoRead("");
			header.setNoReadDay(0);
		    }
				
		    body.setTitle(rs.getString("mb_title"));
		    MsgInfoList.add(msg);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return MsgInfoList.elements();
    }

    /**
     * <h3>����M�l�r�f�\�[�g�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getMrSendRecvSortPredicate(String sortKey, String rowType) {
	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_COMMUNI_MESSAGE_KBN;
	}
	if (rowType == null) {
	    rowType = "D";
	}

	BinaryPredicate pred = new MrCommMessageKbnAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_COMMUNI_MESSAGE_KBN)) {
	    if (rowType.equals("A")) {
		pred = new MrCommMessageKbnAscendPredicate();
	    } else {
		pred = new MrCommMessageKbnDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_COMMUNI_RECEIVE_TIME)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimeAscendPredicate();
	    } else {
		pred = new ReceiveTimeDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_COMMUNI_TITLE)) {
	    if (rowType.equals("A")) {
		pred = new MrCommTitleAscendPredicate();
	    } else {
		pred = new MrCommTitleDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_COMMUNI_RECEIVE_TIMED)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimedAscendPredicate();
	    } else {
		pred = new ReceiveTimedDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_COMMUNI_UNREAD_DAY)) {
	    if (rowType.equals("A")) {
		pred = new UnReadDayAscendPredicate();
	    } else {
		pred = new UnReadDayDescendPredicate();
	    }
	}
		
	return pred;
    }

    /**
     * <h3>��t���M�l�r�f�\�[�g�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getMrSendSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_SEND_TO_USER;
	}
	if (rowType == null) {
	    rowType = "D";
	}

	BinaryPredicate pred = new ToNameAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_SEND_TO_USER)) {
	    if (rowType.equals("A")) {
		pred = new ToNameAscendPredicate();
	    } else {
		pred = new ToNameDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_RECEIVE_TIME)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimeAscendPredicate();
	    } else {
		pred = new ReceiveTimeDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_RECEIVE_TIMED)) {
	    if (rowType.equals("A")) {
		pred = new ReceiveTimedAscendPredicate();
	    } else {
		pred = new ReceiveTimedDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_UNREAD_DAY)) {
	    if (rowType.equals("A")) {
		pred = new UnReadDayAscendPredicate();
	    } else {
		pred = new UnReadDayDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_TARGET_RANK)) {
	    if (rowType.equals("A")) {
		pred = new TargetRankAscendPredicate();
	    } else {
		pred = new TargetRankDescendPredicate();
	    }
	}
		
	return pred;
    }

    /**
     * <h3>��t���Ă̍ŐV���M�l�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:20:39)
     * @return java.lang.String
     * @param toUserID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String getNewSendMsg(String fromUserID, String toUserID) {

	String sqltxt;
	String msgheaderid = null;
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "MAX(message_header_id) headrid ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "message_header ";
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "from_userid = '" + fromUserID + "' ";
	    sqltxt = sqltxt + "AND to_userid = '" + toUserID + "' ";
	    sqltxt = sqltxt + "AND ( message_kbn = '1' OR message_kbn = '4' )";//1116 y-yamada add NO.54
	    																	//�l�q���瑗�����f�[�^�̂ݎ擾
	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		if ( rs.next() ) {
		    msgheaderid = rs.getString("headrid");
		}
	    } finally {
		//rs.close();
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return msgheaderid;
    }

    /**
     * <h3>����(�Â�)���ǎ�M�l�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:20:39)
     * @return java.lang.String
     * @param toUserID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String getNextRecvMsg(String toUserID, String messageHeaderID) {
	String sqltxt;
	String msgheaderid = null;
	
	/*******************************/
	/*������BOX���ꗗ�����ʁ@�@�@�@*/
	/*******************************/
	String sqltxthanbetu;
	String receiveStatus = null;
	String messageKbn = null;
	PreparedStatement pstmt;
	try
	{
		sqltxthanbetu = "SELECT receive_status ,message_kbn ";
	    sqltxthanbetu = sqltxthanbetu + "FROM message_header ";
	    sqltxthanbetu = sqltxthanbetu + "WHERE message_header_id = ? ";


		
	    ResultSet rs;
	    pstmt = conn.prepareStatement(sqltxthanbetu);
	    try {
			pstmt.setString(1, messageHeaderID);
			rs   = pstmt.executeQuery();
			if ( rs.next() ) {
			    receiveStatus = rs.getString("receive_status");
			    messageKbn = rs.getString("message_kbn");
			}
	    } finally {
			pstmt.close();
	    }
	} catch (SQLException e) {
		    throw new MrException(e);
	
	
	}
	
//System.out.println("receiveStatus���@=�@"+receiveStatus);	
//System.out.println("messageKbn���@=�@"+messageKbn);	
	
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "MAX(mh.message_header_id) headrid ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "message_header mh, ";
	    sqltxt = sqltxt + "message_body mb, ";
	    sqltxt = sqltxt + "mr mr, ";//1127 y-yamada add
	    sqltxt = sqltxt + "doctor dr, ";//1127 y-yamada add
	    sqltxt = sqltxt + "company co, ";//1127 y-yamada add
	    sqltxt = sqltxt + "kinmusaki kin, ";//1127 y-yamada add
	    sqltxt = sqltxt + "catch_picture ct, ";//1127 y-yamada add
	    sqltxt = sqltxt + "sentaku_toroku st ";//1127 y-yamada add
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "mh.to_userid = '" + toUserID + "' ";
	    sqltxt = sqltxt + "AND mh.message_header_id < '" + messageHeaderID + "' ";
	    sqltxt = sqltxt + "AND mh.receive_timed IS NULL ";
	    sqltxt = sqltxt + "AND mh.send_torikeshi_time IS NULL ";
	    sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
	    sqltxt = sqltxt + "AND ((mb.yuko_kigen IS NULL) OR ( TO_CHAR(SYSDATE,'YYYYMMDD') <= TO_CHAR(mb.yuko_kigen,'YYYYMMDD') ) ) ";
		if( messageKbn.equals("2") || messageKbn.equals("3") || messageKbn.equals("5") )									//1122 y-yamada add NO.62,63
		{//�l�q����M											//1122 y-yamada add NO.62,63
		//�l�q��M���b�Z�[�W�\�������Ɠ����@1127 y-yamada add
			sqltxt = sqltxt + "AND mh.from_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND (mh.message_kbn = '2' OR mh.message_kbn = '3' OR mh.message_kbn = '5' ) ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd(+) ";
			sqltxt = sqltxt + "AND mh.from_userid = st.dr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.mr_id(+) ";
		}														//1122 y-yamada add NO.62,63
		else													//1122 y-yamada add NO.62,63
		{//�c�q��M���b�Z�[�W�\�������Ɠ����@1127 y-yamada add NO.62,63
			sqltxt = sqltxt + "AND mh.from_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.message_kbn = '1' ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd ";
			sqltxt = sqltxt + "AND mh.from_userid = st.mr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.dr_id(+) ";
		}														//1122 y-yamada add NO.62,63
		
		if(receiveStatus.equals("2"))
		{//��M�{�b�N�X��
	    	sqltxt = sqltxt + "AND mh.receive_status = '2' ";
	    }
	    else
	    {//��M�ꗗ
	    	sqltxt = sqltxt + "AND mh.receive_status = '1' ";
	    }
	    sqltxt = sqltxt + "ORDER BY mh.message_header_id DESC ";

	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
			
		if ( rs.next() ) {
		    msgheaderid = rs.getString("headrid");
		}
	    } finally {
		//rs.close();
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return msgheaderid;
    }

    /**
     * <h3>����(�Â�)���M�l�r�f�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:20:39)
     * @return java.lang.String
     * @param toUserID java.lang.String
     * @param messageHeaderID java.lang.String
     */
    public String getMrNextSendMsg(String toUserID, String messageHeaderID) {
	return this.getDrNextSendMsg(toUserID, messageHeaderID, false);
    }

    public String getDrNextSendMsg(String toUserID,
				   String messageHeaderID,
				   boolean flag) {
	String sqltxt;
	String msgheaderid = null;
	
	
	/*******************************/
	/*������BOX���ꗗ�����ʁ@�@�@�@*/
	/*******************************/
	String sqltxthanbetu;
	String sendStatus = null;
	PreparedStatement pstmt;
	try
	{
		sqltxthanbetu = "SELECT send_status ";
	    sqltxthanbetu = sqltxthanbetu + "FROM message_header ";
	    sqltxthanbetu = sqltxthanbetu + "WHERE message_header_id = ? ";


		
	    ResultSet rs;
	    pstmt = conn.prepareStatement(sqltxthanbetu);
	    try {
			pstmt.setString(1, messageHeaderID);
			rs   = pstmt.executeQuery();
			if ( rs.next() ) {
			    sendStatus = rs.getString("send_status");
			}
	    } finally {
			pstmt.close();
	    }
	} catch (SQLException e) {
		    throw new MrException(e);
	
	
	}
	
//System.out.println("sendStatus���@=�@"+sendStatus);	
	
	
	
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "MAX(mh.message_header_id) headrid ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "message_header mh, ";
	    sqltxt = sqltxt + "message_body mb, ";//1127 y-yamada add
	    sqltxt = sqltxt + "mr mr, ";//1127 y-yamada add
	    sqltxt = sqltxt + "doctor dr, ";//1127 y-yamada add
	    sqltxt = sqltxt + "company co, ";//1127 y-yamada add
	    sqltxt = sqltxt + "kinmusaki kin, ";//1127 y-yamada add
	    sqltxt = sqltxt + "catch_picture ct, ";//1127 y-yamada add
	    sqltxt = sqltxt + "sentaku_toroku st ";//1127 y-yamada add
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "mh.from_userid = '" + toUserID + "' ";
	    sqltxt = sqltxt + "AND mh.send_delete_time IS NULL ";
	    sqltxt = sqltxt + "AND mh.message_kbn <> '3' ";
	    sqltxt = sqltxt + "AND mh.message_header_id < '" + messageHeaderID + "' ";
	    if (flag) {
		//�c�q���M
		//�c�q���M���b�Z�[�W�\�������Ɠ����@1127 y-yamada add
		//sqltxt = sqltxt + "AND message_header.message_kbn <> '4' ";
			//sqltxt = sqltxt + "AND message_header.message_kbn < '4' ";//1110 y-yamada add NO.47 �ړ����b�Z�[�W��4,5
			sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
			sqltxt = sqltxt + "AND mh.from_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.message_kbn = '2' ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd(+) ";
			sqltxt = sqltxt + "AND mh.from_userid = st.dr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.mr_id(+) ";
	    }
	    else
	    {//�l�q���M
	    //�l�q���M���b�Z�[�W�\�������Ɠ����@1127 y-yamada add
			sqltxt = sqltxt + "AND mh.message_id = mb.message_id ";
			sqltxt = sqltxt + "AND mh.from_userid = mr.mr_id ";
			sqltxt = sqltxt + "AND mh.to_userid = dr.dr_id ";
			sqltxt = sqltxt + "AND (mh.message_kbn = '1' OR mh.message_kbn = '4')  ";
			sqltxt = sqltxt + "AND dr.dr_id = kin.dr_id ";
			sqltxt = sqltxt + "AND mr.company_cd = co.company_cd ";
			sqltxt = sqltxt + "AND mb.picture_cd = ct.picture_cd ";
			sqltxt = sqltxt + "AND mh.from_userid = st.mr_id(+) ";
			sqltxt = sqltxt + "AND mh.to_userid = st.dr_id(+) ";
		}
	    
	    if(sendStatus.equals("2"))
	    {//���M�{�b�N�X
		    sqltxt = sqltxt + "AND mh.send_status = '2' ";
		}
		else
		{//���M�ꗗ
		    sqltxt = sqltxt + "AND mh.send_status = '1' ";
		}
	    sqltxt = sqltxt + "ORDER BY mh.message_header_id DESC ";

	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		if ( rs.next() ) {
		    msgheaderid = rs.getString("headrid");
		}
	    } finally {
		//rs.close();
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return msgheaderid;
    }

    /**
     * <h3>��M�ۊǂa�n�w�����`�F�b�N</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 06:26:32)
     * @return boolean
     * @param toUserID java.lang.String
     * @param messageCount int
     */
    public boolean isRecvSaveBoxCheck(String toUserID, int messageCount) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
	boolean check = true;
	
	try {
	    //�萔�}�X�^�[�e�[�u�����̎擾�i��M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("RECEIVEMAX");
	    int teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();

	    //�r�p�k��
	    sqltxt = RECV_SAVE_MESSAGE_COUNT_SQL;
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, toUserID);
		rs   = pstmt.executeQuery();
		
		if ( rs.next() ) {
		    int saveCount = rs.getInt("counter") + messageCount;
		    if ( saveCount > teisuNaiyou1 ) check = false;
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return check;
    }

    /**
     * <h3>���M�ۊǂa�n�w�����`�F�b�N</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 06:26:32)
     * @return boolean
     * @param fromUserID java.lang.String
     * @param messageCount int
     */
    public boolean isSendSaveBoxCheck(String fromUserID, int messageCount) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
	boolean check = true;
	
	try {
	    //�萔�}�X�^�[�e�[�u�����̎擾�i���M���b�Z�[�W�����j
	    ConstantMasterTableManager constantmastertablemanager
		= new ConstantMasterTableManager(conn);
	    ConstantMasterTable constantmaster
		= constantmastertablemanager.getConstantMasterTable("SENDMAX");
	    int teisuNaiyou1 = Integer.valueOf(constantmaster.getNaiyo2()).intValue();

	    //�r�p�k��
	    sqltxt = SEND_SAVE_MESSAGE_COUNT_SQL;
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, fromUserID);
		rs   = pstmt.executeQuery();
		
		if ( rs.next() ) {
		    int saveCount = rs.getInt("counter") + messageCount;
		    if ( saveCount > teisuNaiyou1 ) check = false;
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return check;
    }

    /**
     * <h3>��t��M�l�r�f��Ԃ̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:30:16)
     * @param messageHeaderID java.util.Enumeration
     * @param receive_status java.lang.String
     */
    public void updateDrRecvMsgStatus(Enumeration messageHeaderID, String status) {
	
	try {
		
	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    for (; messageHeaderID.hasMoreElements();) {	//�v�f���L��܂�
		String msgheadrid = (String)messageHeaderID.nextElement();//���̗v�f��

		String updsql = "UPDATE message_header SET receive_status = ?, ";
		if ( status.equals(SysCnst.MSG_STATUS_SAVE) ) {			
		    updsql = updsql + "receive_save_time = SYSDATE ";
		}
		else {
		    updsql = updsql + "receive_delete_time = SYSDATE ";
		}
		updsql = updsql + "WHERE message_header_id = ? "
		    + "AND (message_kbn = '1' OR message_kbn = '4')";
			
		pstmt = conn.prepareStatement(updsql);
		try {
		    pstmt.setString(1, status);
		    pstmt.setString(2, msgheadrid);
		    pstmt.executeUpdate();
		} finally {
		    //rs.close();
		    pstmt.close();
		}
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>��t���M�l�r�f��Ԃ̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:30:16)
     * @param messageHeaderID java.util.Enumeration
     * @param receive_status java.lang.String
     */
    public void updateDrSendMsgStatus(Enumeration messageHeaderID, String status) {
	
	try {

	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    for (; messageHeaderID.hasMoreElements();) {	//�v�f���L��܂�
		String msgheadrid = (String)messageHeaderID.nextElement();//���̗v�f��

		String updsql = "UPDATE message_header SET send_status = ?, ";
		if ( status.equals(SysCnst.MSG_STATUS_SAVE) ) {			
		    updsql = updsql + "send_save_time = SYSDATE ";
		} else {
		    updsql = updsql + "send_delete_time = SYSDATE ";
		}
		updsql = updsql + "WHERE message_header_id = ? "
//		    + "AND (message_kbn = '2' OR message_kbn = '4')";
		    + "AND (message_kbn = '2' OR message_kbn = '5')";//1110 y-yamada add NO.47
		pstmt = conn.prepareStatement(updsql);
		try {
		    pstmt.setString(1, status);
		    pstmt.setString(2, msgheadrid);
		    pstmt.executeUpdate();
		} finally {
				//rs.close();
		    pstmt.close();
		}
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>�l�q��M�l�r�f��Ԃ̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:30:16)
     * @param messageHeaderID java.util.Enumeration
     * @param receive_status java.lang.String
     */
    public void updateMrRecvMsgStatus(Enumeration messageHeaderID, String status) {
	
	try {

	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    for (; messageHeaderID.hasMoreElements();) {//�v�f���L��܂�
		String msgheadrid = (String)messageHeaderID.nextElement();//���̗v�f��
			
		String updsql = "UPDATE message_header SET receive_status = ?, ";
		if ( status.equals(SysCnst.MSG_STATUS_SAVE) ) {	
		    updsql = updsql + "receive_save_time = SYSDATE ";
		} else {
		    updsql = updsql + "receive_delete_time = SYSDATE ";
		}
		updsql = updsql + "WHERE message_header_id = ? ";//AND message_kbn = '2'";
			
		pstmt = conn.prepareStatement(updsql);
		try {
		    pstmt.setString(1, status);
		    pstmt.setString(2, msgheadrid);
		    pstmt.executeUpdate();
		} finally {
				//rs.close();
		    pstmt.close();
		}
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>�l�q�p��t�����ǂ̑��M���</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:41:50)
     * @param messageHeaderID java.lang.Enumeration
     */
    public int updateMrSendCancel(Enumeration messageHeaderID) {
	int updateCount = 0;
	
	try {

	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    for (; messageHeaderID.hasMoreElements();) {	//�v�f���L��܂�
		String msgheadrid = (String)messageHeaderID.nextElement();//���̗v�f��
			
		String updsql = "UPDATE message_header SET send_status = '3', "
		    + "send_torikeshi_time = SYSDATE, receive_status = '3', "
		    + "receive_delete_time = SYSDATE "
		    + "WHERE message_header_id = ? "
		    + "AND (message_kbn = '1' OR message_kbn = '4')"
		    + "AND receive_timed IS NULL";

		pstmt = conn.prepareStatement(updsql);

		try {
		    pstmt.setString(1, msgheadrid);
		    updateCount += pstmt.executeUpdate();
		} finally {
		    //rs.close();
		    pstmt.close();
		}
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return updateCount;
    }

    /**
     * <h3>�l�q���M�l�r�f��Ԃ̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:30:16)
     * @param messageHeaderID java.util.Enumeration
     * @param receive_status java.lang.String
     */
    public void updateMrSendMsgStatus(Enumeration messageHeaderID, String status) {
	
	try {

	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    for (; messageHeaderID.hasMoreElements();) {	//�v�f���L��܂�
		String msgheadrid = (String)messageHeaderID.nextElement();//���̗v�f��

		String updsql = "UPDATE message_header SET send_status = ?, ";
		if ( status.equals(SysCnst.MSG_STATUS_SAVE) ) {	
		    updsql = updsql + "send_save_time = SYSDATE ";
		} else {
		    updsql = updsql + "send_delete_time = SYSDATE ";
		}
		updsql = updsql + "WHERE message_header_id = ?"
		    + " AND (message_kbn = '1' OR message_kbn = '4')";
			
		pstmt = conn.prepareStatement(updsql);
		try {
		    pstmt.setString(1, status);
		    pstmt.setString(2, msgheadrid);
		    pstmt.executeUpdate();
		} finally {
		    //rs.close();
		    pstmt.close();
		}
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>��M�l�r�f�J�����t�̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 11:30:16)
     * @param messageHeaderID java.lang.String
     */
    public int updateRecvMsg(String messageHeaderID) {
	int updateCount = 0;
	
	try {
	    PreparedStatement pstmt = null;
	    conn.setAutoCommit(false);
		
	    pstmt = conn.prepareStatement(RECV_MESSAGE_UPDATE);
	    try {
		pstmt.setString(1, messageHeaderID);
		updateCount = pstmt.executeUpdate();
	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return updateCount;
    }

    public Vector getReceivedMessageList(String mrId) {
	Vector list = new Vector();

	try {
	    PreparedStatement pstmt = null;

	    try {
		conn.prepareStatement(READ_MESSAGE_DR_SQL);
		pstmt.setString(1, mrId);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    ReceivedMessageLog log = new ReceivedMessageLog();
		    log.setName(rs.getString("name"));
		    log.setTitle(rs.getString("title"));
		    log.setReceiveTime(rs.getDate("receive_time"));

		    list.addElement(log);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return list;
    }
    
}
