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
 * メッセージ関連テーブルにアクセスするクラス
 * @author  doppe
 * @version 1.0 (created at 2001/12/07 10:29:32)
 */
public class MessageInfoManager {

    /** DBコネクション */
    protected Connection conn = null;

    /**
     * 指定したコネクションを使用するManagerを生成
     * @param  initConnection
     */
    public MessageInfoManager(Connection initConnection) {
        conn = initConnection;
    }

    /**
     * シーケンスから次のメッセージヘッダIDを取得する。
     * @return メッセージヘッダID
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

            throw new SQLException("通常はありえない");
        } finally {
            stmt.close();
        }
    }

    /**
     * シーケンスから次のメッセージIDを取得する。
     * @return メッセージID
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

            throw new SQLException("通常はありえない");
        } finally {
            stmt.close();
        }
    }

    /**
     * メッセージヘッダテーブルにレコードを追加する。
     * @param header ヘッダオブジェクト
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
     * メッセージボディテーブルにレコードを追加する。
     * @param body ボディオブジェクト
     * @exception SQLException
     */
    private void insertMessageBody(MessageBody body) throws SQLException {
        String url = "";

        //メッセージタイプによる処理種別
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
     * 外部インターフェースメッセージテーブルに登録する。
     * @param messageHeaderId 追加するメッセージヘッダID
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
     * 新規メッセージを追加する。
     * @param header ヘッダオブジェクト
     * @param body   ボディオブジェクト
     * @exception SQLException
     */
    public void insert(MessageHeader header, MessageBody body) throws SQLException {

        try {
            conn.setAutoCommit(false);

            header.setMessageHeaderId(getNewMessageHeaderId());
            header.setMessageId(getNewMessageId());
            body.setMessageId(header.getMessageId());

            //デフォルト画像の設定
            if (Utility.isNull(body.getPictureCd())) {
                MrInfoManager manager = new MrInfoManager(conn);
                body.setPictureCd(manager.getDefaultPictureCd(header.getFromUserid()));
            }

            //デフォルト自己紹介
            if (Utility.isNull(body.getJikosyokai())) {
                MrInfoManager manager = new MrInfoManager(conn);
                body.setJikosyokai(manager.getJikosyokai(header.getFromUserid()));
            }

            //メッセージヘッダ追加
            insertMessageHeader(header);
            //メッセージボディ追加
            insertMessageBody(body);
            //外部IF利用履歴を更新
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
     * 新規メッセージを取得する
     * mrIdが"ALLDATA"の場合はcompanyCdの対象となるメッセージ全て
     * @param  dataType  データ種別
     * @param  companyCd 会社コード
     * @param  mrId      MR-ID
     * @param  flag      "1"だと既読処理を行う
     * @return メッセージ一覧
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
                    } else if (message[5].startsWith("MR新規登録通知")) {
                        message[12] = "2";
                    } else if (message[5].startsWith("開封通知")) {
                        message[12] = "3";
                    } else {
                        continue;
                    }

                    ret.add(message);

                    //既読処理
                    if (flag.equals("1")) {
                        String[] lastElement = (String[])ret.lastElement();
                        updateMessage(lastElement[0]);
                    }
                }

            } finally {
                stmt.close();
            }

            //AP利用履歴に記録
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
     * 新規メッセージを取得する
     * mrIdが"ALLDATA"の場合はcompanyCdの対象となるメッセージ全て
     * @param  dataType  データ種別
     * @param  companyCd 会社コード
     * @param  mrId      MR-ID
     * @param  flag      "1"だと既読処理を行う
     * @return メッセージ一覧
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

                    //直前とメッセージIDが等しい = 添付ファイルだけ違う
                    if (message[0].equals(preMessageNo)) {
                        String[] preMessage = (String[])ret.lastElement();
                        preMessage[13 + attachCount] = rs.getString("attach_file");
                        attachCount++;
                    } else {
                        //最後に処理したメッセージ番号を記憶
                        preMessageNo = message[0];
                        //添付ファイル数を1にセット
                        attachCount = 1;

                        if (message[12].equals("2")) {
                            //医師送信メッセージ
                            message[12] = "1";
                            ret.add(message);
                        } else if (message[12].equals("3")) {
                            //システム送信メッセージ
                            if (message[5].startsWith("MR新規登録通知")) {
                                message[12] = "2";
                                ret.add(message);
                            } else if (message[5].startsWith("開封通知")) {
                                //message[12] = "3";
                                //既読処理は行うが結果に含めない
                            }
                        }

                        //既読処理
                        if (flag.equals("1")) {
                            updateMessage(message[0]);
                        }
                    }
                }

            } finally {
                stmt.close();
            }

            //開封通知を元メッセージから作成
            //本当の開封通知のメッセージIDは不明なため、既読処理は行わない
            Vector receivedList = getReceivedMessages(dataType,
                                                      companyCd,
                                                      mrId,
                                                      lastMessageId);

            //最終取得メッセージIDの抽出
//              String lastMessageId = null;

            if (ret.size() != 0 && receivedList.size() != 0) {
                String[] last = (String[])ret.lastElement();
                String[] receivedLast = (String[])receivedList.lastElement();
                //大きいメッセージIDの方を履歴に登録
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

            //AP利用履歴に記録
            manager.updateLastMessageHeaderId(companyCd, mrId, lastMessageId);
            Logger.log(lastMessageId);

            //開封通知をリストに追加
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
     * 開封通知メッセージを取得(送信メッセージを元に作成)する
     * mrIdが"ALLDATA"の場合はcompanyCdの対象となるメッセージ全て
     * @param  dataType  データ種別
     * @param  companyCd 会社コード
     * @param  mrId      MR-ID
     * @param  lastMessageId
     * @return メッセージ一覧
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
            + ",'' url"		//固定
            + ",0 attach_type"	//固定
            + ",st.name name"
            + ",st.kinmusaki kinmusaki"
            + ",dr.system_cd system_cd"
            + ",3 message_type"	//固定
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

                //本文を修正
                message[6] = message[9] + "\n"
                    + message[10] + "\n"
                    + message[6];
                //サブジェクトを修正
                message[5] = "開封通知: " + message[9];
                //元メッセージ番号
                message[18] = message[0];
                //元メッセージ番号を一部変換し、メッセージ番号に
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
     * メッセージ既読処理を行う。
     * @param messageHeaderId 処理対象のメッセージヘッダID
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
     * 指定したメッセージヘッダを取得します。
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
     * 指定したメッセージを取り消します。
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
