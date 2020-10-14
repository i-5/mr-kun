package medipro.ap.entity;

/**
 * (4)MR君受信メッセージ送信応答Error
 */
public class GetsError extends Response {
    /** データ種別 */
    final static int DATA_TYPE = 0;
    /** シーケンス番号 */
    final static int SEQUENCE_NO = 1;
    /** エラー番号 */
    final static int ERROR_NO = 2;
    /** 備考 */
    final static int REMARK = 3;

    /**
     * 
     */
    public GetsError() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "ERROR_NO",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            2,
            25,
        };

        parameterValues = new String[]{"6", "", "", ""};
    }

    /**
     * シーケンス番号を設定
     */
    public void setSequenceNo(String value) {
        parameterValues[SEQUENCE_NO] = value;
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
