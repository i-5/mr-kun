package medipro.ap.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import medipro.ap.entity.MessageBody;
import medipro.ap.entity.MessageHeader;
import medipro.ap.util.Logger;
import medipro.ap.util.Utility;

/**
 * ���b�Z�[�W�֘A�e�[�u���ɃA�N�Z�X����N���X
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:29:32)
 */
public class MessageInfoManager {

    /** DB�R�l�N�V���� */
    protected Connection conn = null;

    /**
     * �w�肵���R�l�N�V�������g�p����Manager�𐶐�
     * @param  initConnection
     */
    public MessageInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * �V�[�P���X���玟�̃��b�Z�[�W�w�b�_ID���擾����B
     * @return ���b�Z�[�W�w�b�_ID
     * @exception SQLException
     */
    private String getNewMessageHeaderId() throws SQLException {
        String sql
            = "SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || "
            + "TRIM(TO_CHAR(message_header_id.nextval,'0000')) counter FROM dual";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("counter");
            }

            throw new SQLException("�ʏ�͂��肦�Ȃ�");
        } finally {
            stmt.close();
        }
    }

    /**
     * �V�[�P���X���玟�̃��b�Z�[�WID���擾����B
     * @return ���b�Z�[�WID
     * @exception SQLException
     */
    private String getNewMessageId() throws SQLException {
        String sql
            = "SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || "
            + "TRIM(TO_CHAR(message_id.nextval,'0000')) counter FROM dual";
        Logger.log("SQL = " + sql);
	
        Statement stmt = conn.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("counter");
            }

            throw new SQLException("�ʏ�͂��肦�Ȃ�");
        } finally {
            stmt.close();
        }
    }

    /**
     * ���b�Z�[�W�w�b�_�e�[�u���Ƀ��R�[�h��ǉ�����B
     * @param header �w�b�_�I�u�W�F�N�g
     * @exception SQLException
     */
    private void insertMessageHeader(MessageHeader header) throws SQLException {
        String sql
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
            + "VALUES ("
            + " '" + header.getMessageHeaderId() + "'"
            + ",'" + header.getMessageId() + "'"
            + ",'" + header.getMessageKbn() + "'"
            + ",'" + header.getFromUserid() + "'"
            + ",'" + header.getToUserid() + "'"
            + ",'" + header.getCcFlg() + "'"
//              + ",TO_DATE('" + header.getReceiveTime() + "', 'YYYYMMDDHH24MISS')"
            + ",SYSDATE"
            + ",NULL,NULL,NULL,'1',NULL,NULL,NULL,'1') ";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();
			
        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }

    /**
     * ���b�Z�[�W�{�f�B�e�[�u���Ƀ��R�[�h��ǉ�����B
     * @param body �{�f�B�I�u�W�F�N�g
     * @exception SQLException
     */
    private void insertMessageBody(MessageBody body) throws SQLException {
        String url = "";

        //���b�Z�[�W�^�C�v�ɂ�鏈�����
        if (body.getMessageType().equals("2")) {
            body.setMessageHonbun("");
            url = body.getUrl();
        } else if (body.getUrl() != null) {
            AttachLinkInfoManager manager = new AttachLinkInfoManager(conn);
            manager.insertLink(body.getMessageId(), body.getUrl());
        }

        String sql
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
            + "VALUES ("
            + " '" + body.getMessageId() + "'"
            + ",'" + body.getCallNaiyoCd() + "'"
            + ",'" + body.getJikosyokai() + "'"
            + ",'" + body.getTitle() + "'"
            + ",'" + body.getMessageHonbun() + " '"
            + ",TO_DATE('" + body.getYukoKigen() + "','YYYYMMDD')"
            + ",'" + body.getPictureCd() + "'"
            + ",'" + url + "'"
            + ",'" + body.getCompanyCd() + "') ";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();

        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }

    /**
     * �O���C���^�[�t�F�[�X���b�Z�[�W�e�[�u���ɓo�^����B
     * @param messageHeaderId �ǉ����郁�b�Z�[�W�w�b�_ID
     * @exception SQLException
     */
    private void insertApMessage(String messageHeaderId) throws SQLException {
        String sql
            = "INSERT INTO ap_message ( "
            + " message_header_id"
            + ",data_type"
            + ") values ("
            + " '" + messageHeaderId + "'"
            + ",'1'"
            + ")";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();

        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }

    /**
     * �V�K���b�Z�[�W��ǉ�����B
     * @param header �w�b�_�I�u�W�F�N�g
     * @param body   �{�f�B�I�u�W�F�N�g
     * @exception SQLException
     */
    public void insert(MessageHeader header, MessageBody body) throws SQLException {

        try {
            conn.setAutoCommit(false);

            header.setMessageHeaderId(getNewMessageHeaderId());
            header.setMessageId(getNewMessageId());
            body.setMessageId(header.getMessageId());

            //�f�t�H���g�摜�̐ݒ�
            if (Utility.isNull(body.getPictureCd())) {
                MrInfoManager manager = new MrInfoManager(conn);
                body.setPictureCd(manager.getDefaultPictureCd(header.getFromUserid()));
            }

            //�f�t�H���g���ȏЉ�
            if (Utility.isNull(body.getJikosyokai())) {
                MrInfoManager manager = new MrInfoManager(conn);
                body.setJikosyokai(manager.getJikosyokai(header.getFromUserid()));
            }

            //���b�Z�[�W�w�b�_�ǉ�
            insertMessageHeader(header);
            //���b�Z�[�W�{�f�B�ǉ�
            insertMessageBody(body);
            //�O��IF���p�������X�V
            insertApMessage(header.getMessageHeaderId());

            conn.commit();
			
        } catch (SQLException ex) {
            Logger.error("", ex);
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex2) {
            }
        }
    }

    /**
     * �V�K���b�Z�[�W���擾����
     * mrId��"ALLDATA"�̏ꍇ��companyCd�̑ΏۂƂȂ郁�b�Z�[�W�S��
     * @param  dataType  �f�[�^���
     * @param  companyCd ��ЃR�[�h
     * @param  mrId      MR-ID
     * @param  flag      "1"���Ɗ��Ǐ������s��
     * @return ���b�Z�[�W�ꗗ
     * @exception SQLException
     */
    public Vector getMessages(String dataType,
                              String companyCd,
                              String mrId,
                              String flag) throws SQLException {
        String sql
            = "SELECT "
            + " mh.message_header_id message_no"
            + ",to_char(mh.receive_time, 'YYYYMMDD') hiduke"
            + ",to_char(mh.receive_time, 'HH24MISS') jikoku"
            + ",dr.system_cd fromid"
            + ",mh.to_userid toid"
            + ",mb.title title"
            + ",mb.message_honbun honbun"
            + ",mb.url url"
            + ",decode(af.file_kbn,'0','1','0') attach_type"
            + ",st.name name"
            + ",st.kinmusaki kinmusaki"
            + ",dr.system_cd system_cd"
            + ",mh.message_kbn message_type"
            + " FROM message_header mh,message_body mb,sentaku_toroku st,"
            + "      doctor dr,attach_file af,mr"
            + " WHERE"
            + " mh.message_id = mb.message_id"
            + " AND mh.from_userid = dr.dr_id"
            + " AND mh.to_userid = st.mr_id"
            + " AND mh.to_userid = mr.mr_id"
            + " AND dr.dr_id = st.dr_id"
            + " AND mh.message_id = af.message_id(+)"
            + " AND mh.message_kbn in ('2','3') ";


        Vector ret = new Vector();

        try {
            conn.setAutoCommit(false);

            ApMessageHistoryInfoManager manager = new ApMessageHistoryInfoManager(conn);
            String lastMessageId = manager.getLastMessageHeaderId(companyCd, mrId);

            sql += " AND mr.company_cd = '" + companyCd + "'";

            if (dataType.equals("3")) {
                sql += " AND mh.message_header_id > '" + lastMessageId + "'";
            } else if (dataType.equals("4")) {
                sql += " AND (mh.message_header_id > '" + lastMessageId + "' OR "
                    + "to_char(mh.receive_time, 'YYYYMMDD') > "
                    + "to_char(SYSDATE-3, 'YYYYMMDD')) ";
            }

            if (!mrId.equals("ALLDATA")) {
                sql += " AND mr.mr_id = '" + mrId + "'";
            }

            sql += " ORDER BY message_no";

            Logger.log("SQL = " + sql);

            Statement stmt = conn.createStatement();
			
            try {
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String[] message = new String[13];
                    message[0] = rs.getString("message_no");
                    message[1] = rs.getString("hiduke");
                    message[2] = rs.getString("jikoku");
                    message[3] = rs.getString("fromid");
                    message[4] = rs.getString("toid");
                    message[5] = rs.getString("title");
                    message[6] = rs.getString("honbun");
                    message[7] = rs.getString("url");
                    message[8] = rs.getString("attach_type");
                    message[9] = rs.getString("name");
                    message[10] = rs.getString("kinmusaki");
                    message[11] = rs.getString("system_cd");
                    message[12] = rs.getString("message_type");
					
                    if (message[12].equals("2")) {
                        message[12] = "1";
                    } else if (message[5].startsWith("MR�V�K�o�^�ʒm")) {
                        message[12] = "2";
                    } else if (message[5].startsWith("�J���ʒm")) {
                        message[12] = "3";
                    } else {
                        continue;
                    }

                    ret.add(message);

                    //���Ǐ���
                    if (flag.equals("1")) {
                        String[] lastElement = (String[])ret.lastElement();
                        updateMessage(lastElement[0]);
                    }
                }

            } finally {
                stmt.close();
            }

            //AP���p�����ɋL�^
            if (ret.size() != 0) {
                String[] last = (String[])ret.lastElement();
                manager.updateLastMessageHeaderId(companyCd, mrId, last[0]);
                Logger.log(last[0]);
            }

            conn.commit();
        } catch (SQLException ex) {
            Logger.error(sql, ex);
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex2) {
            }
        }
		
        return ret;
    }

    /**
     * �V�K���b�Z�[�W���擾����
     * mrId��"ALLDATA"�̏ꍇ��companyCd�̑ΏۂƂȂ郁�b�Z�[�W�S��
     * @param  dataType  �f�[�^���
     * @param  companyCd ��ЃR�[�h
     * @param  mrId      MR-ID
     * @param  flag      "1"���Ɗ��Ǐ������s��
     * @return ���b�Z�[�W�ꗗ
     * @exception SQLException
     */
    public Vector getMessages2(String dataType,
                               String companyCd,
                               String mrId,
                               String flag) throws SQLException {
        String sql
            = "SELECT "
            + " mh.message_header_id message_no"
            + ",to_char(mh.receive_time, 'YYYYMMDD') hiduke"
            + ",to_char(mh.receive_time, 'HH24MISS') jikoku"
            + ",dr.system_cd fromid"
            + ",mh.to_userid toid"
            + ",mb.title title"
            + ",mb.message_honbun honbun"
            + ",mb.url url"
            + ",decode(af.file_kbn,'0','1','0') attach_type"
            + ",st.name name"
            + ",st.kinmusaki kinmusaki"
            + ",dr.system_cd system_cd"
            + ",mh.message_kbn message_type"
            + ",af.attach_file attach_file"
            + " FROM message_header mh,message_body mb,sentaku_toroku st,"
            + "      doctor dr,attach_file af,mr"
            + " WHERE"
            + " mh.message_id = mb.message_id"
            + " AND mh.from_userid = dr.dr_id"
            + " AND mh.to_userid = st.mr_id"
            + " AND mh.to_userid = mr.mr_id"
            + " AND dr.dr_id = st.dr_id"
            + " AND mh.message_id = af.message_id(+)"
            + " AND mh.message_kbn in ('2','3') ";

        Vector ret = new Vector();

        try {
            conn.setAutoCommit(false);

            ApMessageHistoryInfoManager manager = new ApMessageHistoryInfoManager(conn);
            String lastMessageId = manager.getLastMessageHeaderId(companyCd, mrId);

            sql += " AND mr.company_cd = '" + companyCd + "'";

            if (dataType.equals("A")) {
                sql += " AND mh.message_header_id > '" + lastMessageId + "'";
            } else if (dataType.equals("B")) {
                sql += " AND (mh.message_header_id > '" + lastMessageId + "' OR "
                    + "to_char(mh.receive_time, 'YYYYMMDD') > "
                    + "to_char(SYSDATE-3, 'YYYYMMDD')) ";
            }

            if (!mrId.equals("ALLDATA")) {
                sql += " AND mr.mr_id = '" + mrId + "'";
            }

            sql += " ORDER BY message_no";

            Logger.log("SQL = " + sql);

            Statement stmt = conn.createStatement();
			
            try {
                ResultSet rs = stmt.executeQuery(sql);
                String preMessageNo = null;
                int attachCount = 1;

                while (rs.next()) {
                    String[] message = new String[19];
                    message[0] = rs.getString("message_no");
                    message[1] = rs.getString("hiduke");
                    message[2] = rs.getString("jikoku");
                    message[3] = rs.getString("fromid");
                    message[4] = rs.getString("toid");
                    message[5] = rs.getString("title");
                    message[6] = rs.getString("honbun");
                    message[7] = rs.getString("url");
                    message[8] = rs.getString("attach_type");
                    message[9] = rs.getString("name");
                    message[10] = rs.getString("kinmusaki");
                    message[11] = rs.getString("system_cd");
                    message[12] = rs.getString("message_type");
                    message[13] = rs.getString("attach_file");

                    //���O�ƃ��b�Z�[�WID�������� = �Y�t�t�@�C�������Ⴄ
                    if (message[0].equals(preMessageNo)) {
                        String[] preMessage = (String[])ret.lastElement();
                        preMessage[13 + attachCount] = rs.getString("attach_file");
                        attachCount++;
                    } else {
                        //�Ō�ɏ����������b�Z�[�W�ԍ����L��
                        preMessageNo = message[0];
                        //�Y�t�t�@�C������1�ɃZ�b�g
                        attachCount = 1;

                        if (message[12].equals("2")) {
                            //��t���M���b�Z�[�W
                            message[12] = "1";
                            ret.add(message);
                        } else if (message[12].equals("3")) {
                            //�V�X�e�����M���b�Z�[�W
                            if (message[5].startsWith("MR�V�K�o�^�ʒm")) {
                                message[12] = "2";
                                ret.add(message);
                            } else if (message[5].startsWith("�J���ʒm")) {
                                //message[12] = "3";
                                //���Ǐ����͍s�������ʂɊ܂߂Ȃ�
                            }
                        }

                        //���Ǐ���
                        if (flag.equals("1")) {
                            updateMessage(message[0]);
                        }
                    }
                }

            } finally {
                stmt.close();
            }

            //�J���ʒm�������b�Z�[�W����쐬
            //�{���̊J���ʒm�̃��b�Z�[�WID�͕s���Ȃ��߁A���Ǐ����͍s��Ȃ�
            Vector receivedList = getReceivedMessages(dataType,
                                                      companyCd,
                                                      mrId,
                                                      lastMessageId);

            //�ŏI�擾���b�Z�[�WID�̒��o
//              String lastMessageId = null;

            if (ret.size() != 0 && receivedList.size() != 0) {
                String[] last = (String[])ret.lastElement();
                String[] receivedLast = (String[])receivedList.lastElement();
                //�傫�����b�Z�[�WID�̕��𗚗��ɓo�^
                if (last[0].compareTo(receivedLast[18]) < 0) {
                    lastMessageId = receivedLast[1] + receivedLast[2] + "9999";
                } else {
                    lastMessageId = last[0];
                }
            } else if (ret.size() != 0) {
                lastMessageId = ((String[])ret.lastElement())[0];
            } else if (receivedList.size() != 0) {
                String[] message = (String[])receivedList.lastElement();
                lastMessageId = message[1] + message[2] + "9999";
            }

            //AP���p�����ɋL�^
            manager.updateLastMessageHeaderId(companyCd, mrId, lastMessageId);
            Logger.log(lastMessageId);

            //�J���ʒm�����X�g�ɒǉ�
            ret.addAll(receivedList);

            conn.commit();
        } catch (SQLException ex) {
            Logger.error(sql, ex);
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex2) {
            }
        }
		
        return ret;
    }

    /**
     * �J���ʒm���b�Z�[�W���擾(���M���b�Z�[�W�����ɍ쐬)����
     * mrId��"ALLDATA"�̏ꍇ��companyCd�̑ΏۂƂȂ郁�b�Z�[�W�S��
     * @param  dataType  �f�[�^���
     * @param  companyCd ��ЃR�[�h
     * @param  mrId      MR-ID
     * @param  lastMessageId
     * @return ���b�Z�[�W�ꗗ
     * @exception SQLException
     */
    private Vector getReceivedMessages(String dataType,
                                       String companyCd,
                                       String mrId,
                                       String lastMessageId) throws SQLException {
        String sql
            = "SELECT "
            + " mh.message_header_id message_no"
            + ",to_char(mh.receive_timed, 'YYYYMMDD') hiduke"
            + ",to_char(mh.receive_timed, 'HH24MISS') jikoku"
            + ",dr.system_cd fromid"
            + ",mh.from_userid toid"
            + ",mb.title title"
            + ",mb.message_honbun honbun"
            + ",'' url"		//�Œ�
            + ",0 attach_type"	//�Œ�
            + ",st.name name"
            + ",st.kinmusaki kinmusaki"
            + ",dr.system_cd system_cd"
            + ",3 message_type"	//�Œ�
            + " FROM message_header mh,message_body mb,sentaku_toroku st,doctor dr,mr"
            + " WHERE"
            + " mh.message_kbn = '1' "
            + " AND mh.message_id = mb.message_id"
            + " AND mh.from_userid = mr.mr_id"
            + " AND mh.to_userid = dr.dr_id"
            + " AND st.dr_id = dr.dr_id"
            + " AND st.mr_id = mr.mr_id"
            + " AND mr.company_cd = '" + companyCd + "' ";

        if (dataType.equals("A")) {
            sql += " AND to_char(mh.receive_timed,'YYYYMMDDHH24MISS') > '" + lastMessageId.substring(0,14) + "' ";
        } else if (dataType.equals("B")) {
            sql += " AND (to_char(mh.receive_timed,'YYYYMMDDHH24MISS') > '" + lastMessageId.substring(0,14) + "' "
                + "OR to_char(mh.receive_timed, 'YYYYMMDD') > "
                + "to_char(SYSDATE-3, 'YYYYMMDD')) ";
        }

        if (!mrId.equals("ALLDATA")) {
            sql += " AND mr.mr_id = '" + mrId + "' ";
        }

        sql += " ORDER BY message_no";
        Logger.log("SQL = " + sql);

        Vector ret = new Vector();
        Statement stmt = conn.createStatement();
			
        try {
            ResultSet rs = stmt.executeQuery(sql);
            String preMessageNo = null;
            int attachCount = 1;

            while (rs.next()) {
                String[] message = new String[19];
                message[0] = rs.getString("message_no");
                message[1] = rs.getString("hiduke");
                message[2] = rs.getString("jikoku");
                message[3] = rs.getString("fromid");
                message[4] = rs.getString("toid");
                message[5] = rs.getString("title");
                message[6] = rs.getString("honbun");
                message[7] = rs.getString("url");
                message[8] = rs.getString("attach_type");
                message[9] = rs.getString("name");
                message[10] = rs.getString("kinmusaki");
                message[11] = rs.getString("system_cd");
                message[12] = rs.getString("message_type");

                //�{�����C��
                message[6] = message[9] + "\n"
                    + message[10] + "\n"
                    + message[6];
                //�T�u�W�F�N�g���C��
                message[5] = "�J���ʒm: " + message[9];
                //�����b�Z�[�W�ԍ�
                message[18] = message[0];
                //�����b�Z�[�W�ԍ����ꕔ�ϊ����A���b�Z�[�W�ԍ���
                message[0] = message[18].substring(0, 12) + "A"
                    + message[18].substring(13);
                
                ret.add(message);
            }

        } finally {
            stmt.close();
        }

        return ret;
    }

    /**
     * ���b�Z�[�W���Ǐ������s���B
     * @param messageHeaderId �����Ώۂ̃��b�Z�[�W�w�b�_ID
     * @exception SQLException
     */
    private void updateMessage(String messageHeaderId) throws SQLException {
        String sql
            = "UPDATE message_header SET "
            + " receive_timed = SYSDATE "
            + " WHERE "
            + " message_header_id = '" + messageHeaderId + "'"
            + "AND receive_timed IS NULL";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();
			
        try {
            stmt.executeUpdate(sql);
        } finally {
            stmt.close();
        }
    }

    /**
     * �w�肵�����b�Z�[�W�w�b�_���擾���܂��B
     *
     * @param  messageHeaderId
     * @return 
     * @exception SQLException
     */
    public MessageHeader getMessageHeader(String messageHeaderId) throws SQLException {
        String sql
            = "SELECT "
            + " message_header_id"
            + ",message_id"
            + ",message_kbn"
            + ",from_userid"
            + ",to_userid"
            + ",cc_flg"
            + ",receive_time"
            + ",send_status"
            + ",receive_status"
            + ",receive_timed"
            + ",send_torikeshi_time"
            + ",receive_delete_time"
            + " FROM message_header"
            + " WHERE "
            + " message_header_id = '" + messageHeaderId + "'";
        Logger.log("SQL = " + sql);

        Statement stmt = conn.createStatement();
        MessageHeader header = null;
			
        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                header = new MessageHeader();
                header.setMessageHeaderId(rs.getString(1));
                header.setMessageId(rs.getString(2));
                header.setMessageKbn(rs.getString(3));
                header.setFromUserid(rs.getString(4));
                header.setToUserid(rs.getString(5));
                header.setCcFlg(rs.getString(6));
                header.setReceiveTime(rs.getString(7));
                header.setSendStatus(rs.getString(8));
                header.setReceiveStatus(rs.getString(9));
                header.setReceiveTimed(rs.getString(10));
                header.setSendTorikeshiTime(rs.getString(11));
                header.setReceiveDeleteTime(rs.getString(12));
            }
        } finally {
            stmt.close();
        }

        return header;
    }

    /**
     * �w�肵�����b�Z�[�W���������܂��B
     *
     * @param  messageHeaderId
     * @exception SQLException
     */
    public void cancel(String messageHeaderId) throws SQLException {
        String sql = "UPDATE message_header SET send_status = '3', "
            + "send_torikeshi_time = SYSDATE, receive_status = '3', "
            + "receive_delete_time = SYSDATE "
            + "WHERE message_header_id = '" + messageHeaderId + "' ";
        Logger.log("SQL = " + sql);

        try {
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate(sql);
            } finally {
                stmt.close();
            }

            conn.commit();
			
        } catch (SQLException ex) {
            Logger.error("", ex);
            try {
                conn.rollback();
            } catch (SQLException ex2) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex2) {
            }
        }
    }
}
