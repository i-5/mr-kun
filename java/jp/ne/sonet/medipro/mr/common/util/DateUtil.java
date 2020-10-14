package jp.ne.sonet.medipro.mr.common.util;

import java.util.*;
import java.text.*;

/**
 * <h3>���t/�����t�H�[�}�b�g���[�e�B���e�B</h3>
 * <br>
 * Date�^���玞��������ɕϊ�����.<br>
 *   Date -> "yyyyMMdd HHmmss.SSS"
 * ���������񂩂�Date�^�ɕϊ�����.<br>
 *   "yyyyMMdd HHmmss.SSS" -> Date 
 *   "yyyy/MM/dd"
 *   "yyyyMMdd"
 * ���������񂩂���t�����A�܂��͎������������o��.<br>
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
     * �w�肵��������ǉ�����D
     * �쐬�� : (00/07/04 �ߑO 04:48:04)
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
     * ���t�������Date�^�ɕϊ�����.
     * �Ή��t�H�[�}�b�g�͈ȉ��̒ʂ�.
     *   "yyyyMMdd HHmmss.SSS"
     *   "yy/MM/dd"
     *   "yyyy/MM/dd"
     *   "yyyyMMdd"
     * 
     * @param dateString ���t������ 
     * @return Date. ���t�Ƃ��ĕϊ��ł��Ȃ������ꍇ��null.
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
	
	// "yy/MM/dd" ������� FMT_DATE1���O�Ŕ��肷�邱��
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
     * ���������񂩂�w������I�t�Z�b�g�������t�̎�����������擾����.
     * "yyyyMMdd HHmmss.SSS" + offset���� "yyyyMMdd HHmmss.SSS"���擾.
     * 
     * @param dateString ����̎���������
     *        offset     �I�t�Z�b�g����
     * @return �����������Ԃ�.���������񂪓��t�Ƃ��Đ������Ȃ��ꍇ��null��Ԃ�.
     */
    public static String toStr(String dateString, int offset) {
	// �����Date�ϊ�
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
     * Date�^������������ɕϊ�����.
     * "yyyyMMdd HHmmss.SSS"�t�H�[�}�b�g�ɕϊ�.
     * 
     * @param currentTime ������ɕϊ����鎞���l 
     * @return �ϊ����ꂽ����������
     */
    public static String toStr(Date currentTime) {
	SimpleDateFormat fmt = new SimpleDateFormat(FMT_DATETIME);
	fmt.getCalendar().setLenient(false);
	fmt.setLenient(false);

	return fmt.format(currentTime);
    }
    /**
     * ���������񂩂���t�����擾����.
     * "yyyyMMdd hhmmss.SSS" ���� "yyyy/MM/dd"���擾.
     * 
     * @param dateString ���������� 
     * @return ���������񂩂�擾�������t��. ���������񂪓��t�Ƃ��Đ������Ȃ��ꍇ��null��Ԃ�.
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
     * ���������񂩂���t�����擾����.
     * "yyyyMMdd HHmmss.SSS" ���� "yy/MM/dd"���擾.
     * 
     * @param dateString ���������� 
     * @return ���������񂩂�擾�������t��. ���������񂪓��t�Ƃ��Đ������Ȃ��ꍇ��null��Ԃ�.
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
     * ���������񂩂玞�������擾����.
     * "yyyyMMdd HHmmss.SSS" ���� "HH:mm:ss"���擾.
     * 
     * @param dateString ���������� 
     * @return ���������񂩂�擾����������. ���������񂪓��t�Ƃ��Đ������Ȃ��ꍇ��null��Ԃ�.
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
