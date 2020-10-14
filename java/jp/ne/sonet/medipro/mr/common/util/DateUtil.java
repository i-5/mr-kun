package jp.ne.sonet.medipro.mr.common.util;

import java.util.*;
import java.text.*;

/**
 * <h3>日付/時刻フォーマットユーティリティ</h3>
 * <br>
 * Date型から時刻文字列に変換する.<br>
 *   Date -> "yyyyMMdd HHmmss.SSS"
 * 時刻文字列からDate型に変換する.<br>
 *   "yyyyMMdd HHmmss.SSS" -> Date 
 *   "yyyy/MM/dd"
 *   "yyyyMMdd"
 * 時刻文字列から日付部分、または時刻部分を取り出す.<br>
 *   "yyyyMMdd HHmmss.SSS" -> "yyyy/MM/dd"
 *   "yyyyMMdd HHmmss.SSS" -> "HH:mm:ss"
 * <br>
 * 
 * @author
 */
public class DateUtil {
    ////////////////////////////////////////////////////////////
    //constants
    //
    private static final String FMT_DATETIME = "yyyyMMdd HHmmss.SSS";
    private static final String FMT_DATE1    = "yyyy/MM/dd";
    private static final String FMT_DATE2    = "yyyyMMdd";
    private static final String FMT_DATE3    = "yy/MM/dd";
    private static final String FMT_TIME     = "HH:mm:ss";
    /**
     * 指定した日数を追加する．
     * 作成日 : (00/07/04 午前 04:48:04)
     * @return java.util.Date
     * @param standardDate java.util.Date
     * @param days long
     */
    public Date addDays(Date standardDate, long days) {

	Date returnDate = new Date();
	long day = 86400000L * days;
	day = Math.abs( standardDate.getTime() + day );
	returnDate.setTime(day);
	
	return returnDate;
    }
    /**
     * 日付文字列をDate型に変換する.
     * 対応フォーマットは以下の通り.
     *   "yyyyMMdd HHmmss.SSS"
     *   "yy/MM/dd"
     *   "yyyy/MM/dd"
     *   "yyyyMMdd"
     * 
     * @param dateString 日付文字列 
     * @return Date. 日付として変換できなかった場合はnull.
     */
    public static Date toDate(String dateString) {
	ParsePosition pos = new ParsePosition(0);

	// "yyyyMMdd HHmmss.SSS"
	SimpleDateFormat fmt = new SimpleDateFormat(FMT_DATETIME);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	Date currentTime = fmt.parse(dateString, pos);
	if (currentTime!=null) {
	    return currentTime;
	}
	
	// "yy/MM/dd" こちらを FMT_DATE1より前で判定すること
	fmt = new SimpleDateFormat(FMT_DATE3);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);
	
	currentTime = fmt.parse(dateString, pos);
	if (currentTime!=null) {
	    return currentTime;
	}

	// "yyyy/MM/dd"
	fmt = new SimpleDateFormat(FMT_DATE1);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);
	
	currentTime = fmt.parse(dateString, pos);
	if (currentTime!=null) {
	    return currentTime;
	}

	// "yyyyMMdd"
	fmt = new SimpleDateFormat(FMT_DATE2);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);
	
	currentTime = fmt.parse(dateString, pos);
	if (currentTime!=null) {
	    return currentTime;
	}
	
	return null;
    }
    /**
     * 時刻文字列から指定日数オフセットした日付の時刻文字列を取得する.
     * "yyyyMMdd HHmmss.SSS" + offset日の "yyyyMMdd HHmmss.SSS"を取得.
     * 
     * @param dateString 基準日の時刻文字列
     *        offset     オフセット日数
     * @return 時刻文字列を返す.時刻文字列が日付として正しくない場合はnullを返す.
     */
    public static String toStr(String dateString, int offset) {
	// 基準日をDate変換
	Date currentTime = toDate(dateString);
	if (currentTime==null) {
	    return null;
	}
	// Offset
	Calendar resultTime = Calendar.getInstance();
	resultTime.setTime(currentTime);
	resultTime.add(Calendar.DATE, offset);
	
	return toStr(resultTime.getTime());
    }
    /**
     * Date型を時刻文字列に変換する.
     * "yyyyMMdd HHmmss.SSS"フォーマットに変換.
     * 
     * @param currentTime 文字列に変換する時刻値 
     * @return 変換された時刻文字列
     */
    public static String toStr(Date currentTime) {
	SimpleDateFormat fmt = new SimpleDateFormat(FMT_DATETIME);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	return fmt.format(currentTime);
    }
    /**
     * 時刻文字列から日付部を取得する.
     * "yyyyMMdd hhmmss.SSS" から "yyyy/MM/dd"を取得.
     * 
     * @param dateString 時刻文字列 
     * @return 時刻文字列から取得した日付部. 時刻文字列が日付として正しくない場合はnullを返す.
 */
    public static String toStrDate(String dateString) {
	// "yyyy/MM/dd"
	SimpleDateFormat fmt= new SimpleDateFormat(FMT_DATE1);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	Date currentTime = toDate(dateString);
	if (currentTime==null) {
	    return null;
	}
		
	return fmt.format(currentTime);
    }
    /**
     * 時刻文字列から日付部を取得する.
     * "yyyyMMdd HHmmss.SSS" から "yy/MM/dd"を取得.
     * 
     * @param dateString 時刻文字列 
     * @return 時刻文字列から取得した日付部. 時刻文字列が日付として正しくない場合はnullを返す.
     */
    public static String toStrDate2Y(String dateString) {
	// "yy/MM/dd"
	SimpleDateFormat fmt= new SimpleDateFormat(FMT_DATE3);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	Date currentTime = toDate(dateString);
	if (currentTime==null) {
	    return null;
	}
		
	return fmt.format(currentTime);
    }
    /**
     * 時刻文字列から時刻部を取得する.
     * "yyyyMMdd HHmmss.SSS" から "HH:mm:ss"を取得.
     * 
     * @param dateString 時刻文字列 
     * @return 時刻文字列から取得した時刻部. 時刻文字列が日付として正しくない場合はnullを返す.
     */
    public static String toStrTime(String dateString) {
	// HH:mm:ss
	SimpleDateFormat fmt= new SimpleDateFormat(FMT_TIME);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	Date currentTime = toDate(dateString);
	if (currentTime==null) {
	    return null;
	}
	return fmt.format(currentTime);
    }
}
