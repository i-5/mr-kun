package medipro.ap.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.lang.StringBuffer;
import java.text.DecimalFormat;

import jp.ne.sonet.mrkun.dao.DAOFacade;

/**
 * ユーティリティクラス
 * DBとの接続設定や文字コード設定など
 */
public class Utility {
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
        }
    }

    static final String DRIVER = "weblogic.jdbc.pool.Driver";
    static final String DRIVER_URL = "jdbc:weblogic:pool:mrkun";
    static final String ENCODING = "SJIS";
    //static final String DECODING = "SJIS";
    static final String DECODING = "8859_1";

    /**
     * DBとのConnectionを取得する.
     * @return アプリケーションで使用するDBへのConnection
     */
    public static Connection getConnection() throws SQLException {
        try {
//              return DriverManager.getConnection(DRIVER_URL);
            return DAOFacade.getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * 文字列に内容があるか検査する
     */
    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        } else if (str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * isNullの逆
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * Stringを指定のエンコーディングでbyte[]に変換する
     */
    public static byte[] toBytes(String str) {
        try {
			return str.getBytes(DECODING);
       //     return str.getBytes();
        } catch (Exception ex) {
        }

        return new byte[0];
    }

    /**
     * byte[]を指定のエンコーディングでStringに変換する
     */
    public static String toString(byte[] bytes) {
        try {
			return new String(bytes, ENCODING);
       //     return new String(bytes);
        } catch (Exception ex) {
        }

        return new String();
    }

    /**
     * 数値のフォーマットを行います。
     */
    public static String format(long value, String formatString) {
        DecimalFormat formatter = new DecimalFormat(formatString);
        return formatter.format(value);
    }

    /**
     * http://,https://,ftp://,mailto:の文字列を検索し、URLリンクに
     * 変換した結果を返す。
     * @param  str 変換対象文字列
     * @return 変換後文字列
     */
    public static String createLinks(String str) {
        if (str == null || str.equals(""))
            return "　";
		
        str = createLink(str, "http://");
        str = createLink(str, "https://");
        str = createLink(str, "ftp://");
        str = createLink(str, "mailto:");

        return str;
    }

    /**
     * 指定したtagを検索し、URLリンクに変換した結果を返す。
     * @param  str 変換対象文字列
     * @param  tag 変換タグ
     * @return 変換後文字列
     */
    private static String createLink(String str, String tag) {
        //空白と改行で区切る
        StringTokenizer st = new StringTokenizer(str, " \n\r", true);
        StringBuffer buff = new StringBuffer();

        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            int index = 0;
            //最大一つ
            if ((index = line.indexOf(tag)) >= 0) {
                String prev = line.substring(0, index);
                buff.append(prev);

                String next = line.substring(index);
                int lastIndex = next.length();
                if ((lastIndex = next.indexOf("<")) >= 0) {
                    String extStr = next.substring(lastIndex);
                    next = next.substring(0, lastIndex);
                    buff.append("<a href=\"" + next + "\" target=\"body_window\">"
                                + next + "</a>");
                    buff.append(extStr);
                } else {
                    buff.append("<a href=\"" + next + "\" target=\"body_window\">"
                                + next + "</a>");
                }

            } else {
				//ヒットしなければそのまま
                buff.append(line);
            }
        }

        return buff.toString();
    }

}
