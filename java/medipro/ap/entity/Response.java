package medipro.ap.entity;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 抽象応答クラス
 */
public abstract class Response {
    /** 正常終了 or データ無し */
    public final static String NORMAL = "00";
    /** 項目不足 */
    public final static String LACK = "01";
    /** 未登録MR */
    public final static String NO_MR = "02";
    /** 未登録DR */
    public final static String NO_DR = "03";
    /** 未登録画像 */
    public final static String NO_PICTURE = "04";
    /** 不良有効期限日 */
    public final static String ILLEGAL_APPLIED_DATE = "05";
    /** 未登録会社 */
    public final static String NO_COMPANY = "06";
    /** その他不良 */
    public final static String UNKNOWN_ERROR = "99";
    /** パラメータ名配列 */
    protected String[] parameterNames;
    /** パラメータ値配列 */
    protected String[] parameterValues;
    /** パラメータ桁数配列 */
    protected int[] parameterSizes;

    /**
     * ストリームに自分の保持する内容を書き込む
     */
    public void write(OutputStream os) throws IOException {
        for (int i = 0; i < parameterSizes.length; i++) {
            byte[] val = null;
			
            if (parameterValues[i] == null) {
                val = new byte[0];
            } else {
                val = parameterValues[i].getBytes("SJIS");
            }
            byte[] out = new byte[parameterSizes[i]];

            if (val.length < parameterSizes[i]) {
                System.arraycopy(val, 0, out, 0, val.length);
            } else {
                System.arraycopy(val, 0, out, 0, out.length);
            }
            os.write(out);
        }
    }

    public void write(PrintWriter pw) throws IOException {
        for (int i = 0; i < parameterSizes.length; i++) {
            if (i != 0) {
                pw.print(",'");
            } else {
                pw.print("'");
            }
            if (parameterValues[i] != null) {
                pw.print(parameterValues[i].replace('\n',' ').replace('\r',' '));
            }
        }
        pw.println();
    }

}
