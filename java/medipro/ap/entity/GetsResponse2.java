package medipro.ap.entity;

/**
 * Describe class <code>GetsResponse2</code> here.
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/04 14:04:35)
 */
public class GetsResponse2 extends GetsResponse {
//      /** データ種別 */
//      final static int DATA_TYPE = 0;
//      /** シーケンス番号 */
//      final static int SEQUENCE_NO = 1;
//      /** メッセージID */
//      final static int MESSAGE_ID = 2;
//      /** 送信年月日 */
//      final static int DATE = 3;
//      /** 送信時刻 */
//      final static int TIME = 4;
//      /** 送信元ID */
//      final static int FROM_USER_ID = 5;
//      /** 送信先ID */
//      final static int TO_USER_ID = 6;
//      /** キャッチコピー */
//      final static int CATCHY_COPY = 7;
//      /** 本文 */
//      final static int MESSAGE_BODY = 8;
//      /** リンク */
//      final static int LINK_URL = 9;
//      /** 添付ファイル有無 */
//      final static int ATTACHMENT_FLAG = 10;
//      /** 医師氏名 */
//      final static int DOCTOR_NAME = 11;
//      /** 医師施設名 */
//      final static int DOCTOR_OFFICE = 12;
//      /** システム医師コード */
//      final static int SYSTEM_CD = 13;
//      /** メッセージ種別 */
//      final static int MESSAGE_TYPE = 14;
//      /** メッセージ数 */
//      final static int MESSAGE_COUNT = 15;
    /** 添付URL1 */
    final static int ATTACH_FILE1 = 16;
    /** 添付URL2 */
    final static int ATTACH_FILE2 = 17;
    /** 添付URL3 */
    final static int ATTACH_FILE3 = 18;
    /** 添付URL4 */
    final static int ATTACH_FILE4 = 19;
    /** 添付URL5 */
    final static int ATTACH_FILE5 = 20;
    /** 元メッセージID */
    final static int ORIGINAL_MESSAGE_ID = 21;
    /** 備考 */
    final static int REMARK = 22;

    /**
     *
     */
    public GetsResponse2() {
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
            "ATTACH_FILE1",
            "ATTACH_FILE2",
            "ATTACH_FILE3",
            "ATTACH_FILE4",
            "ATTACH_FILE5",
            "ORIGINAL_MESSAGE_ID",
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
            128,
            128,
            128,
            128,
            128,
            18,
            20,
        };

        parameterValues = new String[]{"C", "", "", "", "", "", "", "", "", "",
                                       "", "", "", "", "", "", "", "", "", "",
                                       "", "", ""};
    }

    /**
     * 添付ファイル1を設定
     */
    public void setAttachFile1(String value) {
        parameterValues[ATTACH_FILE1] = value;
    }

    /**
     * 添付ファイル2を設定
     */
    public void setAttachFile2(String value) {
        parameterValues[ATTACH_FILE2] = value;
    }

    /**
     * 添付ファイル3を設定
     */
    public void setAttachFile3(String value) {
        parameterValues[ATTACH_FILE3] = value;
    }

    /**
     * 添付ファイル4を設定
     */
    public void setAttachFile4(String value) {
        parameterValues[ATTACH_FILE4] = value;
    }

    /**
     * 添付ファイル5を設定
     */
    public void setAttachFile5(String value) {
        parameterValues[ATTACH_FILE5] = value;
    }

    /**
     * 元メッセージ番号
     */
    public void setOriginalMessageId(String value) {
        parameterValues[ORIGINAL_MESSAGE_ID] = value;
    }

    /**
     * 備考を設定
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }
}
