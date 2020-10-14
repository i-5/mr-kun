package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>フロント情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:01:04)
 * @author: 
 */
public class FrontInfo {
	protected MrInfo leftmrinfo;
	protected String leftmrID;
	protected int leftMsgCount;
	protected MsgInfo leftmsginfo;
	protected CompanyTable leftcompanytable;
	protected MrInfo rightmrinfo;
	protected String rightmrID;
	protected int rightMsgCount;
	protected MsgInfo rightmsginfo;
	protected CompanyTable rightcompanytable;
	protected int newMsgCount;
	protected DoctorInfo doctorinfo;
/**
 * Fronthfo コンストラクター・コメント。
 */
public FrontInfo() {
	super();
}
/**
 * <h3>医師情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 04:35:21)
 * @return jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public DoctorInfo getDoctorinfo() {
	return doctorinfo;
}
/**
 * <h3>ＭＲ左会社情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:55:32)
 * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public CompanyTable getLeftcompanytable() {
	return leftcompanytable;
}
/**
 * <h3>左ＭＲＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 10:33:17)
 * @return java.lang.String
 */
public java.lang.String getLeftmrID() {
	return leftmrID;
}
/**
 * <h3>ＭＲ左ＭＲ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:28:55)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getLeftmrinfo() {
	return leftmrinfo;
}
/**
 * <h3>ＭＲ左メッセージカウントの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @return int
 */
public int getLeftMsgCount() {
	return leftMsgCount;
}
/**
 * <h3>ＭＲ左ＭＳＧ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:00:34)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getLeftmsginfo() {
	return leftmsginfo;
}
/**
 * <h3>新規メッセージの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @return int
 */
public int getNewMsgCount() {
	return newMsgCount;
}
/**
 * <h3>ＭＲ右会社情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:55:32)
 * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public CompanyTable getRightcompanytable() {
	return rightcompanytable;
}
/**
 * <h3>右ＭＲＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 10:33:17)
 * @return java.lang.String
 */
public java.lang.String getRightmrID() {
	return rightmrID;
}
/**
 * <h3>ＭＲ右ＭＲ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:28:55)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getRightmrinfo() {
	return rightmrinfo;
}
/**
 * <h3>ＭＲ右メッセージカウントの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @return int
 */
public int getRightMsgCount() {
	return rightMsgCount;
}
/**
 * <h3>ＭＲ右ＭＳＧ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:00:34)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getRightmsginfo() {
	return rightmsginfo;
}
/**
 * <h3>医師情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 04:35:21)
 * @param newDoctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public void setDoctorinfo(DoctorInfo newDoctorinfo) {
	doctorinfo = newDoctorinfo;
}
/**
 * <h3>ＭＲ左会社情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:55:32)
 * @param newLeftcompanytable jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public void setLeftcompanytable(CompanyTable newLeftcompanytable) {
	leftcompanytable = newLeftcompanytable;
}
/**
 * <h3>左ＭＲＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 10:33:17)
 * @param newLeftmrID java.lang.String
 */
public void setLeftmrID(java.lang.String newLeftmrID) {
	leftmrID = newLeftmrID;
}
/**
 * <h3>ＭＲ左ＭＲ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:28:55)
 * @param newLeftmrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setLeftmrinfo(MrInfo newLeftmrinfo) {
	leftmrinfo = newLeftmrinfo;
}
/**
 * <h3>ＭＲ左メッセージカウントのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @param newLeftMsgCount int
 */
public void setLeftMsgCount(int newLeftMsgCount) {
	leftMsgCount = newLeftMsgCount;
}
/**
 * <h3>ＭＲ左ＭＳＧ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:00:34)
 * @param newLeftmsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setLeftmsginfo(MsgInfo newLeftmsginfo) {
	leftmsginfo = newLeftmsginfo;
}
/**
 * <h3>新規メッセージのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @param newNewMsgCount int
 */
public void setNewMsgCount(int newNewMsgCount) {
	newMsgCount = newNewMsgCount;
}
/**
 * <h3>ＭＲ右会社情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:55:32)
 * @param newRightcompanytable jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public void setRightcompanytable(CompanyTable newRightcompanytable) {
	rightcompanytable = newRightcompanytable;
}
/**
 * <h3>右ＭＲＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 10:33:17)
 * @param newRightmrID java.lang.String
 */
public void setRightmrID(java.lang.String newRightmrID) {
	rightmrID = newRightmrID;
}
/**
 * <h3>ＭＲ右ＭＲ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:28:55)
 * @param newRightmrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setRightmrinfo(MrInfo newRightmrinfo) {
	rightmrinfo = newRightmrinfo;
}
/**
 * <h3>ＭＲ右メッセージカウントのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 11:15:03)
 * @param newRightMsgCount int
 */
public void setRightMsgCount(int newRightMsgCount) {
	rightMsgCount = newRightMsgCount;
}
/**
 * <h3>ＭＲ右ＭＳＧ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:00:34)
 * @param newRightmsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setRightmsginfo(MsgInfo newRightmsginfo) {
	rightmsginfo = newRightmsginfo;
}
/**
 * <h3>文字列化する</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:51:35)
 * @return java.lang.String
 */
 public String toString() {
	StringBuffer me = new StringBuffer();
	me.append("*********医師情報*********" + "\n");
	me.append(doctorinfo + "\n");
	me.append("*********ＭＲ左情報*********" + "\n");
	me.append(leftmrinfo + "\n");
	me.append(leftmrID + "\n");
	me.append(leftMsgCount + "\n");
	me.append(leftmsginfo + "\n");
	me.append(leftcompanytable + "\n");
	me.append("*********ＭＲ右情報*********" + "\n");
	me.append(rightmrinfo + "\n");
	me.append(rightmrID + "\n");
	me.append(rightMsgCount + "\n");
	me.append(rightmsginfo + "\n");
	me.append(rightcompanytable + "\n");
	me.append("*********未読*********" + "\n");
	me.append(newMsgCount + "\n");
	
	return me.toString();
}
}
