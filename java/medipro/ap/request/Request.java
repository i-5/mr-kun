package medipro.ap.request;

import javax.servlet.http.HttpServletRequest;

import medipro.ap.util.Logger;
import medipro.ap.util.Utility;
import medipro.ap.util.APException;
import medipro.ap.entity.Result;
import medipro.ap.entity.Response;

/**
 * 抽象要求クラス
 */
public abstract class Request {
    /** パラメータ名 */
    protected String[] parameterNames;
    /** パラメータ値 */
    protected String[] parameterValues;
    /** パラメータ桁数 */
    protected int[] parameterSizes;
    /** 必須 */
    protected boolean[] required;
    /** エラーフラグ */
    public boolean error = false;

    /** 要求処理 */
    public abstract Result execute();
    /** エラー要求 */
    public abstract Response getError();

    /**
     * HTTP要求のデータタイプを見て相当する要求オブジェクトを生成します。
     * @throws Exception   規定外のデータ種別
     */
    public static Request makeRequest(HttpServletRequest request)
        throws Exception {
        String value = request.getParameter("VALUE");
        byte[] bytes = Utility.toBytes(value);

        Logger.log("バイト数= " + bytes.length);

        Request ret;

        if (value.startsWith("1")) {
            ret = new SendRequest();
        } else if (value.startsWith("3") || value.startsWith("4")) {
            ret = new GetsRequest();
        } else if (value.startsWith("A") || value.startsWith("B")) {
            ret = new GetsRequest2();
        } else if (value.startsWith("E")) {
            ret = new CancelRequest();
        } else {
            throw new Exception("対象外のデータ種別");
        }

        try {
            ret.parseInput(bytes);
        } catch (APException ex) {
            ret.error = true;
            Logger.log("項目不足", ex);
        }

        return ret;
    }

    public boolean isError() {
        return error;
    }

    /**
     * 各パラメータの取得と桁数チェック
     * バイト列からフォーマットに従ってパラメータを切り出します。
     * @throws APException 項目不足
     */
    private void parseInput(byte[] bytes) throws APException {
        int begin = 0;//切り出し開始位置

        try {
            for (int i = 0; i < parameterSizes.length; i++) {
                byte[] param = new byte[parameterSizes[i]];
                Logger.log("index = " + begin + "-" + (begin + param.length));
                System.arraycopy(bytes, begin, param, 0, param.length);
                begin += parameterSizes[i];
                parameterValues[i] = Utility.toString(param).trim();
				//必須
                if (required[i] && Utility.isNull(parameterValues[i])) {
                    throw new Exception();
                }

                Logger.log(parameterNames[i] + " = \"" + parameterValues[i] + "\"");
            }
        } catch (Exception ex) {
            Logger.error("", ex);
            throw new APException("項目不足");
        }
    }

    /**
     * 指定indexのパラメータ値を取得します。
     * @param  index
     * @return 値(String)
     */
    public String getParameter(int index) {
        return parameterValues[index];
    }

    /**
     * 指定indexのパラメータ値を設定します。
     * @param index index
     * @param value 設定値(String)
     */
    public void setParameter(int index, String value) {
        parameterValues[index] = value;
    }

}
