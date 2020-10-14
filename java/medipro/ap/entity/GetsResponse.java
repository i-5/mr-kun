package medipro.ap.entity;

/**
 * (4)MR君受信メッセージ送信応答.
 * <pre>
 * 01 dataType
 * 02 sequenceNo
 * 03 messageId      → message_header.message_id
 * 04 date           → message_header.receive_time
 * 05 time           → message_header.receive_time
 * 06 fromUserId     → message_header.from_userid → doctor.system_cd
 * 07 toUserId       → message_header.to_userid
 * 08 catchCopy      → message_body.title
 * 09 messageBody    → message_body.message_honbun
 * 10 linkUrl        → message_body.url
 * 11 attachmentFlag → attch_fileにmessage_idがある
 * 12 doctorName     → sentaku_toroku.name
 * 13 doctorOffice   → sentaku_toroku.kinmusaki
 * 14 systemCd       → doctor.system_cd
 * 15 messageType    → message_header.message_kbn
 * 16 messageCount
 * 17 remark
 * </pre>
 */
public class GetsResponse extends Response {
    /** データ種別 */
    final static int DATA_TYPE = 0;
    /** シーケンス番号 */
    final static int SEQUENCE_NO = 1;
    /** メッセージID */
    final static int MESSAGE_ID = 2;
    /** 送信年月日 */
    final static int DATE = 3;
    /** 送信時刻 */
    final static int TIME = 4;
    /** 送信元ID */
    final static int FROM_USER_ID = 5;
    /** 送信先ID */
    final static int TO_USER_ID = 6;
    /** キャッチコピー */
    final static int CATCHY_COPY = 7;
    /** 本文 */
    final static int MESSAGE_BODY = 8;
    /** リンク */
    final static int LINK_URL = 9;
    /** 添付ファイル有無 */
    final static int ATTACHMENT_FLAG = 10;
    /** 医師氏名 */
    final static int DOCTOR_NAME = 11;
    /** 医師施設名 */
    final static int DOCTOR_OFFICE = 12;
    /** システム医師コード */
    final static int SYSTEM_CD = 13;
    /** メッセージ種別 */
    final static int MESSAGE_TYPE = 14;
    /** メッセージ数 */
    final static int MESSAGE_COUNT = 15;
    /** 備考 */
    final static int REMARK = 16;

    /**
     *
     */
    public GetsResponse() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "DATE",
            "TIME",
            "FROM_USER_ID",
            "TO_USER_ID",
            "CATCH_COPY",
            "MESSAGE_BODY",
            "LINK_URL",
            "ATTACHMENT_FLAG",
            "DOCTOR_NAME",
            "DOCTOR_OFFICE",
            "SYSTEM_CD",
            "MESSAGE_TYPE",
            "MESSAGE_COUNT",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            8,
            6,
            10,
            10,
            64,
            2000,
            256,
            1,
            40,
            64,
            10,
            1,
            5,
            34,
        };

        parameterValues = new String[]{"5", "", "", "", "", "", "", "", "", "",
                                       "", "", "", "", "", "", ""};
    }

    /**
     * シーケンス番号を設定
     */
    public void setSequenceNo(String value) {
        parameterValues[SEQUENCE_NO] = value;
    }

    /**
     * メッセージIDを設定
     */
    public void setMessageId(String value) {
        parameterValues[MESSAGE_ID] = value;
    }

    /**
     * 送信年月日を設定(YYYYMMDD)
     */
    public void setDate(String value) {
        parameterValues[DATE] = value;
    }

    /**
     * 送信時刻を設定(HHMMSS)
     */
    public void setTime(String value) {
        parameterValues[TIME] = value;
    }

    /**
     * 送信元ユーザIDを設定
     */
    public void setFromUserId(String value) {
        parameterValues[FROM_USER_ID] = value;
    }

    /**
     * 送信先ユーザIDを設定
     */
    public void setToUserId(String value) {
        parameterValues[TO_USER_ID] = value;
    }

    /**
     * キャッチコピーを設定
     */
    public void setCatchyCopy(String value) {
        parameterValues[CATCHY_COPY] = value;
    }

    /**
     * メッセージ本文を設定
     */
    public void setMessageBody(String value) {
        parameterValues[MESSAGE_BODY] = value;
    }

    /**
     * リンク先URLを設定
     */
    public void setLinkUrl(String value) {
        parameterValues[LINK_URL] = value;
    }

    /**
     * 添付ファイルの有無を設定
     */
    public void setAttachmentFlag(String value) {
        parameterValues[ATTACHMENT_FLAG] = value;
    }

    /**
     * 医師氏名を設定
     */
    public void setDoctorName(String value) {
        parameterValues[DOCTOR_NAME] = value;
    }

    /**
     * 医師勤務先を設定
     */
    public void setDoctorOffice(String value) {
        parameterValues[DOCTOR_OFFICE] = value;
    }

    /**
     * システム医師コードを設定
     */
    public void setSystemCd(String value) {
        parameterValues[SYSTEM_CD] = value;
    }

    /**
     * メッセージ種別を設定
     */
    public void setMessageType(String value) {
        parameterValues[MESSAGE_TYPE] = value;
    }

    /**
     * 全メッセージ数を設定
     */
    public void setMessageCount(String value) {
        parameterValues[MESSAGE_COUNT] = value;
    }

    /**
     * 備考を設定
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }

}
