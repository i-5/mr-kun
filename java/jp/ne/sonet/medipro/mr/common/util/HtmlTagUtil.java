package jp.ne.sonet.medipro.mr.common.util;

import java.util.*;

/**
 * <h3>ＨＴＭＬタグ変換ユーティリティ</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/28 午前 01:32:15)
 * @author: 
 */
public final class HtmlTagUtil {
    protected static int HTML_TEXTSIZE = 4096;

    /**
     * <h3>ＨＴＭＬタグ表示用変換</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/28 午前 01:33:23)
     * @return java.lang.String
     */
    public static String convertHtmlTag(String src) {
		if (src == null) {
			return "　";
		}
	
		StringBuffer dst= new StringBuffer(HTML_TEXTSIZE);

		int len = src.length();
	
		for (int i=0; i<len; i++) {
			if (src.charAt(i) == '<') {
				dst.append("&#60");
			}
			else if(src.charAt(i) == '"') {
				dst.append("&#34");
			} else {
				dst.append(src.charAt(i));
			}
		}
	
		return convertLFtoCR(dst.toString());
    }
    /**
     * <h3>改行文字 → ＨＴＭＬタグ＜ＢＲ＞ 変換</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/28 午前 01:33:23)
     * @return java.lang.String
     */
    public static String convertLFtoCR(String src) {
		if (src == null) {
			return "　";
		}
	
		StringBuffer dst= new StringBuffer(HTML_TEXTSIZE);
	
		int len = src.length();
	
		for (int i=0; i<len; i++) {
			if (src.charAt(i) == 0x0a) {
				dst.append("<br>");
			}
			else {
				dst.append(src.charAt(i));
			}
		}

		return dst.toString();
    }

    /**
     * nullや空白を全角スペースに変換
     */
    public static String to2ByteSpace(String str) {
		if (str == null || str.trim().equals("")) {
			return "--";
		}

		return str;
    }

	public static String deleteTags(String str) {
		if (str == null || str.equals(""))
			return "　";

		//改行に変換
		str = convertTags(str, "<br>", "\n");

		//<...>を全削除
		int sIndex = 0;
		int eIndex = 0;
		StringBuffer buf = new StringBuffer();

		while ((eIndex = str.indexOf("<", sIndex)) >= 0) {

			buf.append(str.substring(sIndex, eIndex));

			if ((sIndex = str.indexOf(">", eIndex)) < 0) {
				break;
			}
			
			sIndex += 1;
		}

		buf.append(str.substring(sIndex, str.length()));

		str = buf.toString();

		//特殊文字を変換
		str = convertTags(str, "&nbsp;", " ");
		str = convertTags(str, "&amp;", "&");
		str = convertTags(str, "&lt;", "<");
		str = convertTags(str, "&gt;", ">");
		str = convertTags(str, "&quot;", "\"");

		return str;
	}

	public static String convertTags(String str, String tag, String res) {
		String cpStr = str.toUpperCase();
		String cpTag = tag.toUpperCase();
		StringBuffer buf = new StringBuffer();

		int sIndex = 0;
		int eIndex = 0;

		while ((eIndex = cpStr.indexOf(cpTag, sIndex)) >= 0) {
			buf.append(str.substring(sIndex, eIndex) + res);
			sIndex = eIndex + cpTag.length();
		}

		buf.append(str.substring(sIndex, str.length()));

		return buf.toString();
	}

	public static String createTags(String str) {
		return createLinks(str);
	}

	public static String createLinks(String str) {
		if (str == null || str.equals(""))
			return "　";
		
		str = createLink(str, "http://");
		str = createLink(str, "https://");
		str = createLink(str, "ftp://");
		str = createLink(str, "mailto:");

		return str;
	}

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

	static final double ID_LENGTH = Math.pow(10, 10);

	public static String getRandomId() {
		return String.valueOf(Math.round(Math.random() * ID_LENGTH));
	}
}
