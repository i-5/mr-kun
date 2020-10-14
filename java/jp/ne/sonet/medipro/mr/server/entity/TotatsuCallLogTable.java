package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>到達ログテーブル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:30:55)
 * @author: 
 */

public class TotatsuCallLogTable {
	protected String totatsuCallTime;
	protected String fromUserID;
	protected String toUserID;
	protected String messageHeaderID;
	protected String pictureCD;
/**
 * TotatsuCallLogTable コンストラクター・コメント。
 */
public TotatsuCallLogTable() {
	super();
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @return java.lang.String
 */
public java.lang.String getFromUserID() {
	return fromUserID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @return java.lang.String
 */
public java.lang.String getMessageHeaderID() {
	return messageHeaderID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @return java.lang.String
 */
public java.lang.String getTotatsuCallTime() {
	return totatsuCallTime;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @return java.lang.String
 */
public java.lang.String getToUserID() {
	return toUserID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @param newFromUserID java.lang.String
 */
public void setFromUserID(java.lang.String newFromUserID) {
	fromUserID = newFromUserID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @param newMessageHeaderID java.lang.String
 */
public void setMessageHeaderID(java.lang.String newMessageHeaderID) {
	messageHeaderID = newMessageHeaderID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @param newPictureCD java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @param newTotatsuCallTime java.lang.String
 */
public void setTotatsuCallTime(java.lang.String newTotatsuCallTime) {
	totatsuCallTime = newTotatsuCallTime;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午後 03:26:30)
 * @param newToUserID java.lang.String
 */
public void setToUserID(java.lang.String newToUserID) {
	toUserID = newToUserID;
}
}
