package medipro.ap.entity;

/**
 * (2)メッセージ作成依頼応答
 */
public class SendResponse extends Response {

    /** データ種別 */
    final static int DATA_TYPE = 0;
    /** シーケンス番号 */
    final static int SEQUENCE_NO = 1;
    /** メッセージID */
    final static int MESSAGE_ID = 2;
    /** エラー番号 */
    final static int ERROR_NO = 3;
    /** エラー医師ID */
    final static int ERROR_DOCTOR_ID = 4;
    /** 備考 */
    final static int REMARK = 5;

    /**
     * 
     */
    public SendResponse() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "ERROR_NO",
            "ERROR_DOCTOR_ID",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            2,
            10,
            7,
        };

        parameterValues = new String[]{"2", "", "", "", "", ""};
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
     * エラー番号を設定
     */
    public void setErrorNo(String value) {
        parameterValues[ERROR_NO] = value;
    }

    /**
     * エラー医師システムIDを設定
     */
    public void setErrorDoctorId(String value) {
        parameterValues[ERROR_DOCTOR_ID] = value;
    }

    /**
     * 備考を設定
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }

}
