package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * @author: Harry Behrens
 */
public class MessageTableManager {
    protected Connection conn;


    //���b�Z�[�W�w�b�_ INSERT SQL
    protected static final String MESSAGE_HEADER_INSERT_STRING
		= "INSERT INTO message_header ( "
		+ "message_header_id, " 
		+ "message_id, " 
		+ "message_kbn, " 
		+ "from_userid, " 
		+ "to_userid, " 
		+ "cc_flg, " 
		+ "receive_time, " 
		+ "send_torikeshi_time, " 
		+ "send_save_time, " 
		+ "send_delete_time, " 
		+ "send_status, " 
		+ "receive_timed, " 
		+ "receive_save_time, " 
		+ "receive_delete_time, " 
		+ "receive_status " 
		+ ") " 
		+ "VALUES (?,?,?,?,?,?,SYSDATE,NULL,NULL,NULL,'1',NULL,NULL,NULL,'1') ";

    protected static final String MESSAGE_BODY_INSERT_STRING
		= "INSERT INTO message_body ( " 
		+ "message_id, " 
		+ "call_naiyo_cd, " 
		+ "jikosyokai, " 
		+ "title, " 
		+ "message_honbun, " 
		+ "yuko_kigen, " 
		+ "picture_cd, " 
		+ "url, "
		+ "company_cd "
		+ ") "
		+ "VALUES (?,?,?,?,?,TO_DATE(?,'YYYYMMDD'),?,?,?) ";
	
    protected static final String ATTACH_FILE_INSERT_STRING
		= "INSERT INTO attach_file ( "
		+ "message_id, " 
		+ "seq, " 
		+ "attach_file, " 
		+ "file_kbn "
		+ ") " 
		+ "VALUES (?,?,?,?) ";

	
    protected static final String ATTACH_LINK_INSERT_STRING
		= "INSERT INTO attach_link ( "
		+ "message_id, " 
		+ "seq, " 
		+ "url, " 
		+ "honbun_text, " 
		+ "picture, " 
		+ "naigai_link_kbn "
		+ ") "
		+ "VALUES (?,?,?,?,?,?) ";

	//ENQUETE_SEND_LOG�}���pSQL	Ver 1.6 2001.07 M.Mizuki
	static final String ENQUETE_SEND_LOG_INSERT_SQL
		= "INSERT INTO enquete_send_log ("
		+ " dr_id"
		+ ",enq_id"
		+ ",message_header_id"
		+ ",send_date"
		+ " ) VALUES ("
		+ " ?"
		+ ",?"
		+ ",?"
		+ ",SYSDATE"
		+ " )";

	//ENQUETE_ID�X�V�pSQL	Ver 1.6 2001.07 M.Mizuki
	static final String ENQUETE_ID_UPDATE_SQL
		= "UPDATE enquete_id"
		+ " SET status=1"
		+ " WHERE enq_id = ?";

    protected static final String MESSAGE_HEADER_ID_COUNTER_STRING
		= "SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || TRIM(TO_CHAR(message_header_id.nextval,'0000')) counter FROM dual";

    protected static final String MESSAGE_ID_COUNTER_STRING
		= "SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || TRIM(TO_CHAR(message_id.nextval,'0000')) counter FROM dual";

    protected static final String ATTACH_LINK_SEQ_STRING
		= "SELECT TRIM(TO_CHAR(attach_link_seq.nextval,'00')) counter FROM dual";
	
    /**
     * MessageTableManager �R���X�g���N�^�[�E�R�����g�B
     */
    public MessageTableManager(Connection conn) {
		this.conn = conn;
    }

    public String insert(Enumeration toUser,
						 MessageTable messagetable) {
		return this.insert(toUser, messagetable, null, null, null);
	}

    public String insert(Enumeration toUser,
						 MessageTable messagetable,
						 String bccList,
						 String textMessage ) {
		return this.insert(toUser, messagetable, bccList, textMessage, null );
	}

    /**
     * @return java.lang.String
     * @param toUser java.util.Enumeration (String)
     * @param messagetable jp.ne.sonet.medipro.mr.MessageTable
     */
    public String insert(Enumeration toUser,
						 MessageTable messagetable,
						 String bccList,
						 String textMessage,
						 String enqueteId ) {
		PreparedStatement pstmt = null;
		ResultSet rs;
		Statement stmt;
		Vector idList = new Vector();//messageHeaderId���X�g
		Vector drList = new Vector();//��tID���X�g

		MessageHeaderTable messageheadertable = messagetable.getMsgHTable();
		MessageBodyTable messagebodytable = messagetable.getMsgBTable();
		Enumeration enumattachfile = messagetable.getAttachFTable();
		Enumeration enumattachlink = messagetable.getAttachLTable();
		
		try {
			conn.setAutoCommit(false);
		
			stmt = conn.createStatement();
			try {
				rs = stmt.executeQuery(MESSAGE_ID_COUNTER_STRING);
				while ( rs.next() ) {
					messagebodytable.setMessageID(rs.getString("counter"));
				}
			} finally {
				stmt.close();
			}	

			for (; toUser.hasMoreElements();) 
			{
				String touser = (String)toUser.nextElement();		
                System.out.println("MessageTableManager.insert/5: sending to "+touser);
				drList.addElement(touser);

				String ccflg = messageheadertable.getCcFlg();
				if (messageheadertable.getMessageKbn().equals("2") ||
					messageheadertable.getMessageKbn().equals("3")) 
				{
					// hb010828 TODO uncomment this ccflg = mailSend(touser,messagetable);
				}
				
				stmt = conn.createStatement();
				try 
				{
					rs = stmt.executeQuery(MESSAGE_HEADER_ID_COUNTER_STRING);
					while ( rs.next() ) {
						messageheadertable.setMessageHeaderID(rs.getString("counter"));
					}
					idList.addElement(messageheadertable.getMessageHeaderID());
				} finally 
				{
					stmt.close();
				}	

				pstmt = conn.prepareStatement(MESSAGE_HEADER_INSERT_STRING);
				try 
				{
					pstmt.setString(1, messageheadertable.getMessageHeaderID() );
					pstmt.setString(2, messagebodytable.getMessageID() );
					pstmt.setString(3, messageheadertable.getMessageKbn());
					pstmt.setString(4, messageheadertable.getFromUserID());
					pstmt.setString(5, touser);
					pstmt.setString(6, ccflg);
								
					pstmt.execute();

				} finally 
				{
 					pstmt.close();
				}

				// enquete ���MLog Ver1.6 2001.07 M.Mizuki
				if( enqueteId != null ){
				    pstmt = conn.prepareStatement(ENQUETE_SEND_LOG_INSERT_SQL);
				    try {
					pstmt.setString(1, touser);
					pstmt.setString(2, enqueteId);
					pstmt.setString(3, messageheadertable.getMessageHeaderID() );
					pstmt.execute();

				    } finally {
					pstmt.close();
				    }
				}
			}

			//���b�Z�[�W�{��
			pstmt = conn.prepareStatement(MESSAGE_BODY_INSERT_STRING);
			try {
				pstmt.setString(1, messagebodytable.getMessageID() );
				pstmt.setString(2, messagebodytable.getCallNaiyoCD());
				pstmt.setString(3, messagebodytable.getJikosyokai());
			
				if ( messagebodytable.getTitle().equals("") ) {
					pstmt.setString(4, "�^�C�g������");
				}
				else {
					pstmt.setString(4, messagebodytable.getTitle());
				}

				pstmt.setString(5, messagebodytable.getMessageHonbun());
				pstmt.setString(6, messagebodytable.getYukoKigen());
				pstmt.setString(7, messagebodytable.getPictureCD());
				pstmt.setString(8, messagebodytable.getUrl());
				pstmt.setString(9, messagebodytable.getCompanyCD());
							
				pstmt.execute();

      } catch (SQLException errSQL) {
        if (errSQL.getErrorCode() == 1461)
          throw new ApplicationError("The message body field was too long", errSQL);
        else
          throw errSQL;
			} finally {
				pstmt.close();
			}

			//�Y�t�t�@�C��		
			for (; enumattachfile.hasMoreElements();) {		//�v�f���L��܂�
				AttachFileTable attachfiletable = (AttachFileTable) enumattachfile.nextElement();		//���̗v�f
				pstmt = conn.prepareStatement(ATTACH_FILE_INSERT_STRING);
				try {
					pstmt.setString(1, messagebodytable.getMessageID() );
					pstmt.setString(2, attachfiletable.getSeq());
					pstmt.setString(3, attachfiletable.getAttachFile());
					pstmt.setString(4, attachfiletable.getFileKbn());

				System.out.println("MessageTableManager.insert: insert with: "+ ATTACH_FILE_INSERT_STRING);
				System.out.println("MessageTableManager.insert: seq = "+attachfiletable.getSeq() + "attachfile = "+attachfiletable.getAttachFile());
				
					pstmt.execute();
				System.out.println("MessageTableManager.insert: after pstmt.exeute(ATTACH_FILE_INSERT_STRING)");
					
				} finally {
					pstmt.close();
				}
			}

			//�Y�t�����N
			for (; enumattachlink.hasMoreElements();) {		//�v�f���L��܂�
				AttachLinkTable attachlinktable = (AttachLinkTable) enumattachlink.nextElement();		//���̗v�f
			
				pstmt = conn.prepareStatement(ATTACH_LINK_INSERT_STRING);
				try {
					pstmt.setString(1, messagebodytable.getMessageID() );
					stmt = conn.createStatement();
					try {
						rs = stmt.executeQuery(ATTACH_LINK_SEQ_STRING);
						System.out.println("MessageTableManager.insert: after pstmt.exeute(ATTACH_LINK_INSERT_STRING)");
						
						while ( rs.next() ) {
							pstmt.setString(2, rs.getString("counter"));
						}
					} finally {
						stmt.close();
					}
					pstmt.setString(3, attachlinktable.getUrl());
					pstmt.setString(4, attachlinktable.getHonbuText());
					pstmt.setString(5, attachlinktable.getPicture());
					pstmt.setString(6, attachlinktable.getNaigaiLinkKbn());
					pstmt.execute();
				
				} finally {
					pstmt.close();
				}
			}

			// enquete ���MLog Ver1.6 2001.07 M.Mizuki
			if( enqueteId != null ){
			    pstmt = conn.prepareStatement(ENQUETE_ID_UPDATE_SQL);
			    try {
				pstmt.setString(1, enqueteId);
				pstmt.execute();
				System.out.println("MessageTableManager.insert: after pstmt.exeute(ENQUETE_ID_UPDATE_SQL)");
				
			    } finally {
				pstmt.close();
			    }
			}

			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
			}
			throw new ApplicationError("MessageTableManager",e);
		} catch (ClassCastException e) {
			throw new ApplicationError("MessageTableManager",e);
		} finally {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException ex) {
			}
		}

		return messageheadertable.getMessageHeaderID();
    }

	/**
	 * BCC���X�g���X��Email�A�h���X�ɕ�������
	 * @param  bccList ';'�ŋ�؂����A�h���X�ꗗ
	 * @return �������ʃA�h���X�ꗗ
	 * @version MR1.5
	 */
	private Vector parseBccList(String bccList) {
		Vector list = new Vector();

		if (bccList == null || bccList.trim().equals("")) {
			return list;
		}

		StringTokenizer st = new StringTokenizer(bccList, ";");
		
		while (st.hasMoreTokens()) {
			String address = st.nextToken();
			if (!address.trim().equals("")) {
				list.addElement(address);
			}
		}

		return list;
	}

	/**
	 * BCC�S���Ƀ��[���𑗂�
	 * @param  idList    ���b�Z�[�W�w�b�_ID���X�g
	 * @param  drList    ��tID���X�g
	 * @param  bccList   BCC��
	 * @param  mrId      MR-ID
	 * @param  companyCd MR�̉�ЃR�[�h
	 * @param  title     ���b�Z�[�W�^�C�g��
	 * @param  text      ���b�Z�[�W�{��
	 * @version MR1.5
	 */
	private void sendBCCMail(Vector idList,
							 Vector drList,
							 String bccList,
							 String mrId,
							 String companyCd,
							 String title,
							 String text,
							 String textMessage) 
	{
		return;
	}

		
    /**
     * @param toUser java.util.Enumeration
     * @param messagetable jp.ne.sonet.medipro.mr.MessageTable
     */
    public String mailSend(String toUser, MessageTable messagetable) {
		String ccflg = null;

		//�萔�}�X�^�[�e�[�u�����̎擾�ismtp�T�[�o�j
		ConstantMasterTableManager constantmastertablemanager
			= new ConstantMasterTableManager(conn);
		ConstantMasterTable constantmaster
			= constantmastertablemanager.getConstantMasterTable("SMTPSRV");
	
		MailUtil mailUtil = new MailUtil(constantmaster.getNaiyo1());
	
		//�萔�}�X�^�[�e�[�u�����̎擾�i���[����From�A�h���X�j
		constantmaster = constantmastertablemanager.getConstantMasterTable("FROMADD");
		mailUtil.setFrom(constantmaster.getNaiyo1(), constantmaster.getNaiyo2());
	
		//�l�q���̎擾
		MrInfoManager mrinfomanager = new MrInfoManager(conn);
		MrInfo mrinfo = mrinfomanager.getMrInfo(toUser);
		//��t���̎擾
		DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
		DoctorInfo doctorinfo
			= doctorinfomanager.getDoctorInfo(messagetable.getMsgHTable().getFromUserID());

		mailUtil.setSubject(messagetable.getMsgBTable().getTitle());
		String messagetxt = doctorinfo.getName() + "\n"
			+ doctorinfo.getKinmusakiName() + "\n"
			+ messagetable.getMsgBTable().getMessageHonbun();
		mailUtil.setText(HtmlTagUtil.deleteTags(messagetxt));
	
	
		if (mrinfo.getCcEmail1() != null) {
			mailUtil.setTo(mrinfo.getCcEmail1(), mrinfo.getName());
			mailUtil.send();
			ccflg = "1";
		}

		if (mrinfo.getCcEmail2() != null) {
			mailUtil.setTo(mrinfo.getCcEmail2(), mrinfo.getName());
			mailUtil.send();
			ccflg = "1";
		}
		
		if (mrinfo.getCcEmail3() != null) {
			mailUtil.setTo(mrinfo.getCcEmail3(), mrinfo.getName());
			mailUtil.send();
			ccflg = "1";
		}

		if (mrinfo.getCcEmail4() != null) {
			mailUtil.setTo(mrinfo.getCcEmail4(), mrinfo.getName());
			mailUtil.send();
			ccflg = "1";
		}
	
		return ccflg;
    }
}
