package medipro.ap.entity;

/**
 * (F)メッセージ取消依頼応答
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/05 23:56:55)
 */
public class CancelResponse extends Response {

    /** 既読エラー */
    public final static String ALREADY_RECEIVED = "03";
    public final static String ALREADY_TORIKESHI = "07";
    public final static String NO_MESSAGE = "08";
    public final static String NOT_OWNER = "09";

    /** データ種別 */
    final static int DATA_TYPE = 0;
    /** シーケンス番号 */
    final static int SEQUENCE_NO = 1;
    /** メッセージID */
    final static int MESSAGE_ID = 2;
    /** エラー番号 */
    final static int ERROR_NO = 3;
    /** 備考 */
    final static int REMARK = 4;
    
    public CancelResponse() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "ERROR_NO",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            2,
            7,
        };

        parameterValues = new String[]{"F", "", "", "", ""};
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
     * 備考を設定
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }
    
}
