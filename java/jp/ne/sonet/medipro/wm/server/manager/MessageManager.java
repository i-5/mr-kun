package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.*;
import java.util.Vector;
import java.util.Enumeration;

import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.entity.*;

public class MessageManager {
    private static final String ATTACH_FILE_IMAGE = "1";
    private static final String ATTACH_FILE_OTHER = "0";
    private static final String MESSAGE_KBN_MR = "1";
    private static final String MESSAGE_KBN_DR = "2";
    private static final String MESSAGE_KBN_SYSTEM = "3";
    private static final String MESSAGE_KBN_MR_COPY = "4";
    private static final String MESSAGE_KBN_DR_COPY = "5";

    //�ړ����郁�b�Z�[�W���X�g
    private static final String COPY_MESSAGE_LIST_SQL
	= "SELECT"
	+ " *"
	+ " FROM message_header"
	+ " WHERE"
	+ " from_userid = ?"
	+ " AND to_userid = ?"
	+ " AND receive_delete_time IS NULL"
	+ " AND send_delete_time IS NULL"
	+ " AND send_torikeshi_time IS NULL"
	+ " AND message_kbn <> '3'";

	/************************************************/
    //�c�q���狌�l�q�ňړ����郁�b�Z�[�W���X�g
    // 1109 y-yamada add No.46
    /************************************************/
    private static final String COPY_DRMESSAGE_LIST_SQL
	= "SELECT"
	+ " *"
	+ " FROM message_header"
	+ " WHERE"
	+ " from_userid = ?"//drID
	+ " AND to_userid = ?"//oldMrId
	+ " AND (message_kbn = '2' OR message_kbn = '5' ) "
	+ " AND receive_delete_time IS NULL";//���l�q���폜���Ă��Ȃ���M����

	/************************************************/
    //���l�q����c�q�ňړ����郁�b�Z�[�W���X�g
    // 1109 y-yamada add No.46
    /************************************************/
    private static final String COPY_MRMESSAGE_LIST_SQL
	= "SELECT"
	+ " *"
	+ " FROM message_header"
	+ " WHERE"
	+ " from_userid = ?"//oldMrId
	+ " AND to_userid = ?"//drId
	+ " AND (message_kbn = '1' OR message_kbn = '4' ) "
	+ " AND send_delete_time IS NULL";//���l�q���폜���Ă��Ȃ����M����



    //�R�s�[��MessageHeaderID�̎擾
    protected static final String MESSAGE_HEADER_ID_COUNTER
	= "SELECT"
	+ " TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')"
	+ " || TRIM(TO_CHAR(message_header_id.nextval,'0000')) counter"
	+ " FROM dual";

    //�R�s�[��MessageID�̎擾
    protected static final String MESSAGE_ID_COUNTER
	= "SELECT"
	+ " TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')"
	+ " || TRIM(TO_CHAR(message_id.nextval,'0000')) counter"
	+ " FROM dual";

    //MessageHeader�̃R�s�[
    protected static final String MESSAGE_HEADER_COPY_SQL
	= "INSERT INTO message_header ("
	+ " message_header_id"
	+ ",message_id"
	+ ",message_kbn"
	+ ",from_userid"
	+ ",to_userid"
	+ ",cc_flg"
	+ ",receive_time"
	+ ",send_torikeshi_time"
	+ ",send_save_time"
	+ ",send_delete_time"
	+ ",send_status"
	+ ",receive_timed"
	+ ",receive_save_time"
	+ ",receive_delete_time"
	+ ",receive_status"
	+ ")"
	+ " SELECT"
	+ " ?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",cc_flg"
	+ ",receive_time"
	+ ",send_torikeshi_time"
	+ ",send_save_time"
	+ ",send_delete_time"
	+ ",send_status"
	+ ",receive_timed"
	+ ",receive_save_time"
	+ ",receive_delete_time"
	+ ",receive_status"
	+ " FROM message_header"
	+ " WHERE"
	+ " message_header_id = ?";
    
    //MessageBody�̃R�s�[
    protected static final String MESSAGE_BODY_COPY_SQL
	= "INSERT INTO message_body ("
	+ " message_id"
	+ ",call_naiyo_cd"
	+ ",jikosyokai"
	+ ",title"
	+ ",message_honbun"
	+ ",yuko_kigen"
	+ ",picture_cd"
	+ ",url"
	+ ",company_cd"
	+ ")"
	+ " SELECT"
	+ " ?"
	+ ",call_naiyo_cd"
	+ ",jikosyokai"
	+ ",'�ړ��F' || title"
	+ ",message_honbun"
	+ ",yuko_kigen"
	+ ",picture_cd"
	+ ",url"
	+ ",company_cd"
	+ " FROM message_body"
	+ " WHERE"
	+ " message_id = ?";

    //AttachImage,AttachFile�̃R�s�[���X�g
    protected static final String ATTACH_FILE_LIST_SQL
	= " SELECT"
	+ " seq"
	+ ",message_id"
	+ ",attach_file"
	+ ",file_name"
	+ ",file_kbn"
	+ " FROM attach_file"
	+ " WHERE"
	+ " message_id = ?"
	+ " AND file_kbn = ?";

    //AttachImage,AttachFile�̃R�s�[
    protected static final String ATTACH_FILE_COPY_SQL
	= "INSERT INTO attach_file ("
	+ " seq"
	+ ",message_id"
	+ ",attach_file"
	+ ",file_name"
	+ ",file_kbn"
	+ ") values ("
	+ " ?,?,?,?,?"
	+ ")";

   //AttachLink�̃R�s�[���X�g
    protected static final String ATTACH_LINK_LIST_SQL
	= "SELECT"
	+ " seq"
	+ ",message_id"
	+ ",url"
	+ ",honbun_text"
	+ ",picture"
	+ ",naigai_link_kbn"
	+ " FROM attach_link"
	+ " WHERE"
	+ " message_id = ?";

   //AttachLink�̃R�s�[
    protected static final String ATTACH_LINK_COPY_SQL
	= "INSERT INTO attach_link ("
	+ " seq"
	+ ",message_id"
	+ ",url"
	+ ",honbun_text"
	+ ",picture"
	+ ",naigai_link_kbn"
	+ ") values ("
	+ " ?,?,?,?,?,?"
	+ ")";

    //AttachFile�̃V�[�P���X�擾
    protected static final String ATTACH_FILE_SEQ_STRING
	= "SELECT TRIM(TO_CHAR(attach_file_seq.nextval,'00')) counter FROM dual";

    //AttachLink�̃V�[�P���X�擾
    protected static final String ATTACH_LINK_SEQ_STRING
	= "SELECT TRIM(TO_CHAR(attach_link_seq.nextval,'00')) counter FROM dual";


    private Connection connection;

    public MessageManager(Connection initConnection) {
	connection = initConnection;
    }

    /**
     * �Y�t�t�@�C���̃R�s�[�Ώۈꗗ���擾����.
     * @param messageId �Ώۂɂ���ID
     * @param fileKbn   1:�摜 0:����ȊO
     * @return AttachFileInfo�C���X�^���X���X�g
     */
    private Vector getAttachFileList(String messageId,
				     String fileKbn) throws SQLException {
	PreparedStatement pstmt = null;
	Vector list = new Vector();
	
	try {
	    pstmt = connection.prepareStatement(ATTACH_FILE_LIST_SQL);
	    pstmt.setString(1, messageId);
	    pstmt.setString(2, fileKbn);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		AttachFileInfo info = new AttachFileInfo();
		info.seq = rs.getString("seq");
		info.messageId = rs.getString("message_id");
		info.attachFile = rs.getString("attach_file");
		info.fileName = rs.getString("file_name");
		info.fileKbn = rs.getString("file_kbn");

		list.addElement(info);
	    }
	} finally {
	    pstmt.close();
	}

	return list;
    }

    /**
     * �Y�t�����N�̃R�s�[�Ώۈꗗ���擾����.
     * @param messageId �Ώۂɂ���ID
     * @return AttachLinkInfo�C���X�^���X���X�g
     */
    private Vector getAttachLinkList(String messageId) throws SQLException {
	PreparedStatement pstmt = null;
	Vector list = new Vector();

	try {
	    pstmt = connection.prepareStatement(ATTACH_LINK_LIST_SQL);

	    pstmt.setString(1, messageId);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		AttachLinkInfo info = new AttachLinkInfo();
		info.seq = rs.getString("seq");
		info.messageId = rs.getString("message_id");
		info.url = rs.getString("url");
		info.honbunText = rs.getString("honbun_text");
		info.picture = rs.getString("picture");
		info.naigaiLinkKbn = rs.getString("naigai_link_kbn");

		list.addElement(info);
	    }
	} finally {
	    pstmt.close();
	}

	return list;
    }

    /**
     * �Y�t�t�@�C�����e��V����SEQ�̃��R�[�h�Ƃ��č쐬���܂�(�V�[�P���X�̂ݒu�������܂�).
     */
//  private int copyToNewAttachFile(AttachFileInfo info) throws SQLException {//1214 y-yamada del
    private int copyToNewAttachFile(AttachFileInfo info , String messageId  ) throws SQLException {//1214 y-yamada add
	PreparedStatement pstmt = null;
	Statement stmt = null;

	//�V�[�P���X�̎擾��info�ւ̃Z�b�g
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(ATTACH_FILE_SEQ_STRING);
	    
	    if (rs.next()) {
		info.seq = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}

	//info�����ۂɑ}��
	int count = 0;
	try {
	    pstmt = connection.prepareStatement(ATTACH_FILE_COPY_SQL);

	    pstmt.setString(1, info.seq);
//	    pstmt.setString(2, info.messageId);//1214 y-yamada del
	    pstmt.setString(2, messageId);//1214 �V�������b�Z�[�WID������� y-yamada add
	    pstmt.setString(3, info.attachFile);
	    pstmt.setString(4, info.fileName);
	    pstmt.setString(5, info.fileKbn);

	    count = pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	return count;
    }

    /**
     * �Y�t�����N���e��V����SEQ�̃��R�[�h�Ƃ��č쐬���܂�(�V�[�P���X�̂ݒu�������܂�).
     */
//  private int copyToNewAttachLink(AttachLinkInfo info) throws SQLException {//1214 y-yamada del
    private int copyToNewAttachLink(AttachLinkInfo info ,String messageId ) throws SQLException {//1214 y-yamada add
	PreparedStatement pstmt = null;
	Statement stmt = null;

	//�V�[�P���X�̎擾��info�ւ̃Z�b�g
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(ATTACH_LINK_SEQ_STRING);
	    
	    if (rs.next()) {
		info.seq = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}
	
	//info�����ۂɑ}��
	int count = 0;
	try {
	    pstmt = connection.prepareStatement(ATTACH_LINK_COPY_SQL);

	    pstmt.setString(1, info.seq);
//	    pstmt.setString(2, info.messageId);//1214 y-yamada del
	    pstmt.setString(2, messageId);//1214 y-yamada add
	    pstmt.setString(3, info.url);
	    pstmt.setString(4, info.honbunText);
	    pstmt.setString(5, info.picture);
	    pstmt.setString(6, info.naigaiLinkKbn);

	    count = pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	return count;
    }

    /****************************************************************
     * �c�q����V�l�q�̃��b�Z�[�W���R�s�[���č쐬
     1109 y-yamada add No.46
     ****************************************************************/
    private void copyToNewDrMessage(String drId,
				 String newMrId,
				 String oldMessageHeaderId,
				 String oldMessageId) throws SQLException {
	Statement stmt = null;
	PreparedStatement pstmt = null;

	//�VmessageHeaderId�̎擾
	String messageHeaderId = null;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(MESSAGE_HEADER_ID_COUNTER);
	    if (rs.next()) {
		messageHeaderId = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}

	//�VmessageId�̎擾
	String messageId = null;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(MESSAGE_ID_COUNTER);
	    if (rs.next()) {
		messageId = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}

	//MessageHeader�̃R�s�[
	try {
	    pstmt = connection.prepareStatement(MESSAGE_HEADER_COPY_SQL);
	    pstmt.setString(1, messageHeaderId);
	    pstmt.setString(2, messageId);
	    pstmt.setString(3, "5");//�c�q���l�q�Ɉړ������Ƃ��̃t���O
	    pstmt.setString(4, drId);
	    pstmt.setString(5, newMrId);
	    pstmt.setString(6, oldMessageHeaderId);

	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	//MessageBody�̃R�s�[
	try {
	    pstmt = connection.prepareStatement(MESSAGE_BODY_COPY_SQL);
	    pstmt.setString(1, messageId);
	    pstmt.setString(2, oldMessageId);
		
	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	//AttachImage�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector imageList = getAttachFileList(oldMessageId, ATTACH_FILE_IMAGE);
	if (imageList.size() > 0) {
//	    copyToNewAttachFile((AttachFileInfo)imageList.lastElement());//1214 y-yamada del
	    copyToNewAttachFile((AttachFileInfo)imageList.lastElement() , messageId );//1214 y-yamada add
	}

	//AttachFile�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector fileList = getAttachFileList(oldMessageId, ATTACH_FILE_OTHER);
	if (fileList.size() > 0) {
//	    copyToNewAttachFile((AttachFileInfo)fileList.lastElement());//1214 y-yamada del
	    copyToNewAttachFile((AttachFileInfo)fileList.lastElement() , messageId );//1214 y-yamada add
	}

	//AttachLink�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector linkList = getAttachLinkList(oldMessageId);
	if (linkList.size() > 0) {
//	    copyToNewAttachLink((AttachLinkInfo)linkList.lastElement());//1214 y-yamada del
	    copyToNewAttachLink((AttachLinkInfo)linkList.lastElement() , messageId);//1214 y-yamada add
	}

    }


    /****************************************************************
     * �V�l�q����c�q�̃��b�Z�[�W���R�s�[���č쐬
     1109 y-yamada add No.46
     ****************************************************************/
    private void copyToNewMrMessage(String newMrId,
				 String drId,
				 String oldMessageHeaderId,
				 String oldMessageId) throws SQLException {
	Statement stmt = null;
	PreparedStatement pstmt = null;

	//�VmessageHeaderId�̎擾
	String messageHeaderId = null;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(MESSAGE_HEADER_ID_COUNTER);
	    if (rs.next()) {
		messageHeaderId = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}

	//�VmessageId�̎擾
	String messageId = null;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(MESSAGE_ID_COUNTER);
	    if (rs.next()) {
		messageId = rs.getString("counter");
	    }
	} finally {
	    stmt.close();
	}

	//MessageHeader�̃R�s�[
	try {
	    pstmt = connection.prepareStatement(MESSAGE_HEADER_COPY_SQL);
	    pstmt.setString(1, messageHeaderId);
	    pstmt.setString(2, messageId);
	    pstmt.setString(3, "4");//�l�q���c�q���ړ������Ƃ��̃t���O
	    pstmt.setString(4, newMrId);
	    pstmt.setString(5, drId);
	    pstmt.setString(6, oldMessageHeaderId);

	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	//MessageBody�̃R�s�[
	try {
	    pstmt = connection.prepareStatement(MESSAGE_BODY_COPY_SQL);
	    pstmt.setString(1, messageId);
	    pstmt.setString(2, oldMessageId);
		
	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	//AttachImage�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector imageList = getAttachFileList(oldMessageId, ATTACH_FILE_IMAGE);
	if (imageList.size() > 0) {
	    //copyToNewAttachFile((AttachFileInfo)imageList.lastElement());//1214 y-yamada del
	    copyToNewAttachFile((AttachFileInfo)imageList.lastElement() ,messageId );//1214 y-yamada add
	}

	//AttachFile�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector fileList = getAttachFileList(oldMessageId, ATTACH_FILE_OTHER);
	if (fileList.size() > 0) {
	    //copyToNewAttachFile((AttachFileInfo)fileList.lastElement());//1214 y-yamada del
	    copyToNewAttachFile((AttachFileInfo)fileList.lastElement() ,messageId );//1214 y-yamada add
	}

	//AttachLink�̃R�s�[(�Ō�̃��R�[�h�̂�)
	Vector linkList = getAttachLinkList(oldMessageId);
	if (linkList.size() > 0) {
	    //copyToNewAttachLink((AttachLinkInfo)linkList.lastElement());//1214 y-yamada del
	    copyToNewAttachLink((AttachLinkInfo)linkList.lastElement() ,messageId );//1214 y-yamada add
	}

    }





    /************************************************
     * �c�q���狌�l�q�̃��b�Z�[�W�ꗗ���擾����.
     1109 y-yamada add NO.46
     ************************************************/
    private Vector getCopyDrMessageList(String drId, String oldMrId) throws SQLException {
	Vector list = new Vector();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	    
	try {
//	    pstmt = connection.prepareStatement(COPY_MESSAGE_LIST_SQL);
	    pstmt = connection.prepareStatement(COPY_DRMESSAGE_LIST_SQL);//1109 y-yamada add No.46
	    pstmt.setString(1, drId);
	    pstmt.setString(2, oldMrId);
		
	    rs = pstmt.executeQuery();
		
	    while (rs.next()) {
		MessageHeaderInfo info = new MessageHeaderInfo();
		info.messageHeaderId = rs.getString("message_header_id");
		info.messageId = rs.getString("message_id");
		info.messageKbn = rs.getString("message_kbn");
		info.fromUserId = rs.getString("from_userid");
		info.toUserId = rs.getString("to_userid");
		info.ccFlg = rs.getString("cc_flg");
		info.receiveTime = rs.getDate("receive_time");
		info.sendTorikeshiTime = rs.getDate("send_torikeshi_time");
		info.sendSaveTime = rs.getDate("send_save_time");
		info.sendDeleteTime = rs.getDate("send_delete_time");
		info.sendStatus = rs.getString("send_status");
		info.receiveTimed = rs.getDate("receive_timed");
		info.receiveSaveTime = rs.getDate("receive_save_time");
		info.receiveDeleteTime = rs.getDate("receive_delete_time");
		info.receiveStatus = rs.getString("receive_status");

		list.addElement(info);
	    }
	} finally {
	    pstmt.close();
	}

	return list;
    }

    /************************************************
     * ���l�q����c�q�̃��b�Z�[�W�ꗗ���擾����.
     1109 y-yamada add NO.46
     ************************************************/
    private Vector getCopyMrMessageList(String oldMrId, String drId) throws SQLException {
	Vector list = new Vector();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	    
	try {
	    pstmt = connection.prepareStatement(COPY_MRMESSAGE_LIST_SQL);
	    pstmt.setString(1, oldMrId);
	    pstmt.setString(2, drId);
		
	    rs = pstmt.executeQuery();
		
	    while (rs.next()) {
		MessageHeaderInfo info = new MessageHeaderInfo();
		info.messageHeaderId = rs.getString("message_header_id");
		info.messageId = rs.getString("message_id");
		info.messageKbn = rs.getString("message_kbn");
		info.fromUserId = rs.getString("from_userid");
		info.toUserId = rs.getString("to_userid");
		info.ccFlg = rs.getString("cc_flg");
		info.receiveTime = rs.getDate("receive_time");
		info.sendTorikeshiTime = rs.getDate("send_torikeshi_time");
		info.sendSaveTime = rs.getDate("send_save_time");
		info.sendDeleteTime = rs.getDate("send_delete_time");
		info.sendStatus = rs.getString("send_status");
		info.receiveTimed = rs.getDate("receive_timed");
		info.receiveSaveTime = rs.getDate("receive_save_time");
		info.receiveDeleteTime = rs.getDate("receive_delete_time");
		info.receiveStatus = rs.getString("receive_status");

		list.addElement(info);
	    }
	} finally {
	    pstmt.close();
	}

	return list;
    }





    public void copyDrMessage(String drId,
			     String oldMrId,
			     String newMrId) {
	try {
	    Vector list = getCopyDrMessageList(drId, oldMrId);
	    Enumeration e = list.elements();
	    int count = 0;
	
	    while (e.hasMoreElements()) {
		MessageHeaderInfo info = (MessageHeaderInfo)e.nextElement();
//System.out.println("info.fromUserId�@�Ƃ�"+info.fromUserId);
		//copyToNewMessage(info.fromUserId,//D��Id  //1109 y-yamada del NO.46
		copyToNewDrMessage(info.fromUserId,//D��Id  //1109 y-yamada add NO.46
				 newMrId,
				 info.messageHeaderId,
				 info.messageId);
	    }
	} catch (SQLException e) {
	    throw new WmException(e);
	}
    }

    public void copyMrMessage(String drId,
			      String oldMrId,
			      String newMrId) {

	try {
//	    Vector list = getCopyDrMessageList(oldMrId, drId);//1109 y-yamada del NO.46
	    Vector list = getCopyMrMessageList(oldMrId, drId);//1109 y-yamada add NO.46
	    Enumeration e = list.elements();
	    int count = 0;
	
	    while (e.hasMoreElements()) {
		MessageHeaderInfo info = (MessageHeaderInfo)e.nextElement();
//		copyToNewMessage(newMrId,
		copyToNewMrMessage(newMrId,
				 drId,
				 info.messageHeaderId,
				 info.messageId);
	    }
	} catch (SQLException e) {
	    throw new WmException(e);
	}
    }

    /**
     * �e�X�g�p(thin�h���C�o�g�p)
     */
    public static void main(String[] args) throws Exception {
	Class.forName("oracle.jdbc.driver.OracleDriver");

	Connection con
	    = DriverManager.getConnection("jdbc:oracle:thin:@messala:1521:medipro",
					  "mr",
					  "mr");

	MessageManager manager = new MessageManager(con);
	manager.copyDrMessage("kamiya", "TKD0000001", "TKD0000002");
	manager.copyMrMessage("kamiya", "TKD0000001", "TKD0000002");
    }
}
