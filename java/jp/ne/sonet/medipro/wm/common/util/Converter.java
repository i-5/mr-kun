package jp.ne.sonet.medipro.wm.common.util;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;

public class Converter {

    /**
     * URLパラメータを適切なencodingに変換する。
     * @param  req           サーブレット要求オブジェクト
     * @param  parameterName パラメータ名
     * @return 適切に変換したString
     */
    public static String getParameter(HttpServletRequest req, String parameterName) {
	String value = req.getParameter(parameterName);

	if (value == null) {
	    return null;
	}

	try {
	    value = new String(value.getBytes("8859_1"), "SJIS");
	} catch (UnsupportedEncodingException ex) {
	    ex.printStackTrace();
	}

	return value;
    }

    public static String enToSjis(String inputValue) {

	if (inputValue == null) {
	    return null;
	}

	try {
	    return new String(inputValue.getBytes("8859_1"), "SJIS");
	} catch (UnsupportedEncodingException ex) {
	    ex.printStackTrace();
            throw new RuntimeException("Failed to convert SJIS request params");             
	}
    }

    static final char[][] table
	= {
	    {0xff0d, 0x2212},//－
	    {0xff5e, 0x301c},//～
	};

    public static String convert(String str) {
	if (str == null) {
	    return str;
	}

	char[] array = str.toCharArray();
	StringBuffer result = new StringBuffer("");

	for (int i = 0; i < array.length; i++) {
	    for (int j = 0; j < table.length; j++) {
		if (array[i] == table[j][0]) {
		    array[i] = table[j][1];
		}
	    }
	    result.append(array[i]);
	}

	return result.toString();
    }

}
