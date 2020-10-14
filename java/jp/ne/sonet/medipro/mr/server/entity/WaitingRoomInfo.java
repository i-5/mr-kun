package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>待合室情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/28 午前 12:46:32)
 * @author: 
 */
public class WaitingRoomInfo {
	protected MrInfo mrinfo;
	protected int msgCount;
	protected MsgInfo msginfo;
	protected String mrID;
/**
 * WaitingRoom コンストラクター・コメント。
 */
public WaitingRoomInfo() {
	super();
}
/**
 * <h3>ＭＲＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午後 06:42:34)
 * @return java.lang.String
 */
public java.lang.String getMrID() {
	return mrID;
}
/**
 * <h3>ＭＲ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午前 04:21:02)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getMrinfo() {
	return mrinfo;
}
/**
 * <h3>ＭＲメッセージカウンターの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午前 04:21:02)
 * @return int
 */
public int getMsgCount() {
	return msgCount;
}
/**
 * <h3>ＭＳＧ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:08:42)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getMsginfo() {
	return msginfo;
}
/**
 * <h3>ＭＲＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午後 06:42:34)
 * @param newMrID java.lang.String
 */
public void setMrID(java.lang.String newMrID) {
	mrID = newMrID;
}
/**
 * <h3>ＭＲ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午前 04:21:02)
 * @param newMrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setMrinfo(MrInfo newMrinfo) {
	mrinfo = newMrinfo;
}
/**
 * <h3>ＭＲメッセージカウンターのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/28 午前 04:21:02)
 * @param newMsgCount int
 */
public void setMsgCount(int newMsgCount) {
	msgCount = newMsgCount;
}
/**
 * <h3>ＭＳＧ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 04:08:42)
 * @param newMsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setMsginfo(MsgInfo newMsginfo) {
	msginfo = newMsginfo;
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
	me.append(msginfo + "\n");
	me.append(mrinfo + "\n");
	me.append(msgCount + "\n");
	me.append(mrID + "\n");
	
	return me.toString();
}
}
