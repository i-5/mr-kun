package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>統計分析情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:26:41)
 * @author: 
 */
public class StatisticsInfo {
	protected MrInfo mrinfo;
	protected int mrCallCount30 = 0;
	protected int mrCallCount180 = 0;
	protected int mrInsertCustCount30 = 0;
	protected int mrInsertCustCount180 = 0;
	protected float mrSendCount30 = 0;
	protected float mrSendCount180 = 0;
	protected int mrClickRate30 = 0;
	protected int mrClickRate180 = 0;
	protected int coCallCount30 = 0;
	protected int coCallCount180 = 0;
	protected int coInsertCustCount30 = 0;
	protected int coInsertCustCount180 = 0;
	protected float coSendCount30 = 0;
	protected float coSendCount180 = 0;
	protected int coClickRate30 = 0;
	protected int coClickRate180 = 0;
/**
 * StatisticsInfo コンストラクター・コメント。
 */
public StatisticsInfo() {
}
/**
 * <h3>会社到達コール数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoCallCount180() {
	return coCallCount180;
}
/**
 * <h3>会社到達コール数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoCallCount30() {
	return coCallCount30;
}
/**
 * <h3>会社クリック率１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoClickRate180() {
	return coClickRate180;
}
/**
 * <h3>会社クリック率３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoClickRate30() {
	return coClickRate30;
}
/**
 * <h3>会社登録顧客数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoInsertCustCount180() {
	return coInsertCustCount180;
}
/**
 * <h3>会社登録顧客数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getCoInsertCustCount30() {
	return coInsertCustCount30;
}
/**
 * <h3>会社送信数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public float getCoSendCount180() {
	return coSendCount180;
}
/**
 * <h3>会社送信数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public float getCoSendCount30() {
	return coSendCount30;
}
/**
 * <h3>ＭＲ到達コール数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrCallCount180() {
	return mrCallCount180;
}
/**
 * <h3>ＭＲ到達コール数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrCallCount30() {
	return mrCallCount30;
}
/**
 * <h3>ＭＲクリック率１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrClickRate180() {
	return mrClickRate180;
}
/**
 * <h3>ＭＲクリック率３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrClickRate30() {
	return mrClickRate30;
}
/**
 * <h3>ＭＲ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:35:36)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getMrinfo() {
	return mrinfo;
}
/**
 * <h3>ＭＲ登録顧客数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrInsertCustCount180() {
	return mrInsertCustCount180;
}
/**
 * <h3>ＭＲ登録顧客数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public int getMrInsertCustCount30() {
	return mrInsertCustCount30;
}
/**
 * <h3>ＭＲ送信数１８０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public float getMrSendCount180() {
	return mrSendCount180;
}
/**
 * <h3>ＭＲ送信数３０の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @return int
 */
public float getMrSendCount30() {
	return mrSendCount30;
}
/**
 * <h3>会社到達コール数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoCallCount180 int
 */
public void setCoCallCount180(int newCoCallCount180) {
	coCallCount180 = newCoCallCount180;
}
/**
 * <h3>会社到達コール数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoCallCount30 int
 */
public void setCoCallCount30(int newCoCallCount30) {
	coCallCount30 = newCoCallCount30;
}
/**
 * <h3>会社クリック率１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoClickRate180 int
 */
public void setCoClickRate180(int newCoClickRate180) {
	coClickRate180 = newCoClickRate180;
}
/**
 * <h3>会社クリック率３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoClickRate30 int
 */
public void setCoClickRate30(int newCoClickRate30) {
	coClickRate30 = newCoClickRate30;
}
/**
 * <h3>会社登録顧客数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoInsertCustCount180 int
 */
public void setCoInsertCustCount180(int newCoInsertCustCount180) {
	coInsertCustCount180 = newCoInsertCustCount180;
}
/**
 * <h3>会社登録顧客数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoInsertCustCount30 int
 */
public void setCoInsertCustCount30(int newCoInsertCustCount30) {
	coInsertCustCount30 = newCoInsertCustCount30;
}
/**
 * <h3>会社送信数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoSendCount180 int
 */
public void setCoSendCount180(float newCoSendCount180) {
	coSendCount180 = newCoSendCount180;
}
/**
 * <h3>会社送信数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newCoSendCount30 int
 */
public void setCoSendCount30(float newCoSendCount30) {
	coSendCount30 = newCoSendCount30;
}
/**
 * <h3>ＭＲ到達コール数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrCallCount180 int
 */
public void setMrCallCount180(int newMrCallCount180) {
	mrCallCount180 = newMrCallCount180;
}
/**
 * <h3>ＭＲ到達コール数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrCallCount30 int
 */
public void setMrCallCount30(int newMrCallCount30) {
	mrCallCount30 = newMrCallCount30;
}
/**
 * <h3>ＭＲクリック率１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrClickRate180 int
 */
public void setMrClickRate180(int newMrClickRate180) {
	mrClickRate180 = newMrClickRate180;
}
/**
 * <h3>ＭＲクリック率３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrClickRate30 int
 */
public void setMrClickRate30(int newMrClickRate30) {
	mrClickRate30 = newMrClickRate30;
}
/**
 * <h3>ＭＲ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:35:36)
 * @param newMrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setMrinfo(MrInfo newMrinfo) {
	mrinfo = newMrinfo;
}
/**
 * <h3>ＭＲ登録顧客数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrInsertCustCount180 int
 */
public void setMrInsertCustCount180(int newMrInsertCustCount180) {
	mrInsertCustCount180 = newMrInsertCustCount180;
}
/**
 * <h3>ＭＲ登録顧客数３０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrInsertCustCount30 int
 */
public void setMrInsertCustCount30(int newMrInsertCustCount30) {
	mrInsertCustCount30 = newMrInsertCustCount30;
}
/**
 * <h3>ＭＲ送信数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrSendCount180 int
 */
public void setMrSendCount180(float newMrSendCount180) {
	mrSendCount180 = newMrSendCount180;
}
/**
 * <h3>ＭＲ送信数１８０のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/04 午前 12:02:48)
 * @param newMrSendCount30 int
 */
public void setMrSendCount30(float newMrSendCount30) {
	mrSendCount30 = newMrSendCount30;
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
	me.append("mrinfo               = [" + mrinfo + "]\n");
	me.append("mrCallCount30        = [" + mrCallCount30 + "]\n");
	me.append("mrCallCount180       = [" + mrCallCount180 + "]\n");
	me.append("mrInsertCustCount30  = [" + mrInsertCustCount30 + "]\n");
	me.append("mrInsertCustCount180 = [" + mrInsertCustCount180 + "]\n");
	me.append("mrSendCount30        = [" + mrSendCount30 + "]\n");
	me.append("mrSendCount180       = [" + mrSendCount180 + "]\n");
	me.append("mrClickRate30        = [" + mrClickRate30 + "]\n");
	me.append("mrClickRate180       = [" + mrClickRate180 + "]\n");
	me.append("coCallCount30        = [" + coCallCount30 + "]\n");
	me.append("coCallCount180       = [" + coCallCount180 + "]\n");
	me.append("coInsertCustCount30  = [" + coInsertCustCount30 + "]\n");
	me.append("coInsertCustCount180 = [" + coInsertCustCount180 + "]\n");
	me.append("coSendCount30        = [" + coSendCount30 + "]\n");
	me.append("coSendCount180       = [" + coSendCount180 + "]\n");
	me.append("coClickRate30        = [" + coClickRate30 + "]\n");
	me.append("coClickRate180       = [" + coClickRate180 + "]\n");
	return me.toString();
}
}
