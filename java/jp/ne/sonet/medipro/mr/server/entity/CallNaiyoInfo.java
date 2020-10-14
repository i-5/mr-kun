package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>コール内容情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:45:52)
 * @author: 
 */
public class CallNaiyoInfo {
	protected String callNaiyoCD;
	protected String callNaiyo;
/**
 * CallNaiyoInfo コンストラクター・コメント。
 */
public CallNaiyoInfo(String callNaiyoCD) {
	this.callNaiyoCD = callNaiyoCD;
}
/**
 * <h3>コール内容の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:47:55)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyo() {
	return callNaiyo;
}
/**
 * <h3>コール内容コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:47:55)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyoCD() {
	return callNaiyoCD;
}
/**
 * <h3>コール内容のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:47:55)
 * @param newCallNaiyo java.lang.String
 */
public void setCallNaiyo(java.lang.String newCallNaiyo) {
	callNaiyo = newCallNaiyo;
}
/**
 * <h3>コール内容コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:47:55)
 * @param newCallNaiyoCD java.lang.String
 */
public void setCallNaiyoCD(java.lang.String newCallNaiyoCD) {
	callNaiyoCD = newCallNaiyoCD;
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
	me.append(callNaiyo + "\n");
	me.append(callNaiyoCD + "\n");	
	return me.toString();
}
}
