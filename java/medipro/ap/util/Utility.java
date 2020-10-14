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
 * ���[�e�B���e�B�N���X
 * DB�Ƃ̐ڑ��ݒ�╶���R�[�h�ݒ�Ȃ�
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
     * DB�Ƃ�Connection���擾����.
     * @return �A�v���P�[�V�����Ŏg�p����DB�ւ�Connection
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
     * ������ɓ��e�����邩��������
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
     * isNull�̋t
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * String���w��̃G���R�[�f�B���O��byte[]�ɕϊ�����
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
     * byte[]���w��̃G���R�[�f�B���O��String�ɕϊ�����
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
     * ���l�̃t�H�[�}�b�g���s���܂��B
     */
    public static String format(long value, String formatString) {
        DecimalFormat formatter = new DecimalFormat(formatString);
        return formatter.format(value);
    }

    /**
     * http://,https://,ftp://,mailto:�̕�������������AURL�����N��
     * �ϊ��������ʂ�Ԃ��B
     * @param  str �ϊ��Ώە�����
     * @return �ϊ��㕶����
     */
    public static String createLinks(String str) {
        if (str == null || str.equals(""))
            return "�@";
		
        str = createLink(str, "http://");
        str = createLink(str, "https://");
        str = createLink(str, "ftp://");
        str = createLink(str, "mailto:");

        return str;
    }

    /**
     * �w�肵��tag���������AURL�����N�ɕϊ��������ʂ�Ԃ��B
     * @param  str �ϊ��Ώە�����
     * @param  tag �ϊ��^�O
     * @return �ϊ��㕶����
     */
    private static String createLink(String str, String tag) {
        //�󔒂Ɖ��s�ŋ�؂�
        StringTokenizer st = new StringTokenizer(str, " \n\r", true);
        StringBuffer buff = new StringBuffer();

        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            int index = 0;
            //�ő���
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
				//�q�b�g���Ȃ���΂��̂܂�
                buff.append(line);
            }
        }

        return buff.toString();
    }

}
