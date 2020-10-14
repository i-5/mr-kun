package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>統計分析顧客別情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:34:32)
 * @author: 
 */
public class StatisticsDrInfo {
	protected String drID;
	protected String drName;
	protected String drNameKana;
	protected String targetRank;
	protected String targetName;//1025 y-yamada add
	protected int sendCount30 = 0;
	protected int sendCount180 = 0;
	protected int clickCount30 = 0;
	protected int clickCount180 = 0;
	protected int clickRate30 = 0;
	protected int clickRate180 = 0;
/**
 * StatisticsCustINfo コンストラクター・コメント。
 */
public StatisticsDrInfo() {
}
/**
 * <h3>クリック数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getClickCount180() {
	return clickCount180;
}
/**
 * <h3>クリック数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getClickCount30() {
	return clickCount30;
}
/**
 * <h3>クリック率１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getClickRate180() {
	return clickRate180;
}
/**
 * <h3>クリック数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getClickRate30() {
	return clickRate30;
}
/**
 * <h3>医師ＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 11:30:52)
 * @return java.lang.String
 */
public java.lang.String getDrID() {
	return drID;
}
/**
 * <h3>医師氏名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return java.lang.String
 */
public java.lang.String getDrName() {
	return drName;
}
/**
 * <h3>医師氏名（カナ）の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/05 午前 03:11:39)
 * @return java.lang.String
 */
public java.lang.String getDrNameKana() {
	return drNameKana;
}
/**
 * <h3>送信数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getSendCount180() {
	return sendCount180;
}
/**
 * <h3>送信数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return int
 */
public int getSendCount30() {
	return sendCount30;
}
/**
 * <h3>ターゲットランクの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return java.lang.String
 */
public java.lang.String getTargetRank() {
	return targetRank;
}
/**
 * <h3>ターゲットランクの取得</h3>
 * 1025 y-yamada add
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @return java.lang.String
 */
public java.lang.String getTargetName() {
	return targetName;
}
/**
 * <h3>クリック数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newClickCount180 int
 */
public void setClickCount180(int newClickCount180) {
	clickCount180 = newClickCount180;
}
/**
 * <h3>クリック数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newClickCount30 int
 */
public void setClickCount30(int newClickCount30) {
	clickCount30 = newClickCount30;
}
/**
 * <h3>クリック率１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newClickRate180 int
 */
public void setClickRate180(int newClickRate180) {
	clickRate180 = newClickRate180;
}
/**
 * <h3>クリック率３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newClickRate30 int
 */
public void setClickRate30(int newClickRate30) {
	clickRate30 = newClickRate30;
}
/**
 * <h3>医師ＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午後 11:30:52)
 * @param newDrID java.lang.String
 */
public void setDrID(java.lang.String newDrID) {
	drID = newDrID;
}
/**
 * <h3>医師氏名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newDrName java.lang.String
 */
public void setDrName(java.lang.String newDrName) {
	drName = newDrName;
}
/**
 * <h3>医師氏名（カナ）のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/05 午前 03:11:39)
 * @param newDrNameKana java.lang.String
 */
public void setDrNameKana(java.lang.String newDrNameKana) {
	drNameKana = newDrNameKana;
}
/**
 * <h3>送信数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newSendCount180 int
 */
public void setSendCount180(int newSendCount180) {
	sendCount180 = newSendCount180;
}
/**
 * <h3>送信数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newSendCount30 int
 */
public void setSendCount30(int newSendCount30) {
	sendCount30 = newSendCount30;
}
/**
 * <h3>ターゲットランクのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newTargetRank java.lang.String
 */
public void setTargetRank(java.lang.String newTargetRank) {
	targetRank = newTargetRank;
}
/**
 * <h3>ターゲットランクのセット</h3>
 *   1025 y-yamada add
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:44:04)
 * @param newTargetName java.lang.String
 */
public void setTargetName(java.lang.String newTargetName) {
	targetName = newTargetName;
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
	me.append("drID          = [" + drID + "]\n");
	me.append("drName        = [" + drName + "]\n");
	me.append("targetRank    = [" + targetRank + "]\n");
	me.append("targetName    = [" + targetName + "]\n");//1025 y-yamada add
	me.append("sendCount30   = [" + sendCount30 + "]\n");
	me.append("sendCount180  = [" + sendCount180 + "]\n");
	me.append("clickCount30  = [" + clickCount30 + "]\n");
	me.append("clickCount180 = [" + clickCount180 + "]\n");
	me.append("clickRate30   = [" + clickRate30 + "]\n");
	me.append("clickRate180  = [" + clickRate180 + "]\n");
	return me.toString();
}
}
