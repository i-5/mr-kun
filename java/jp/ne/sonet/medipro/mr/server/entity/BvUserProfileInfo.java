package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>ＢＶプロファイル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:11:17)
 * @author: 
 */
public class BvUserProfileInfo {
	protected java.lang.String name;
	protected java.lang.String kanaName;
	protected java.lang.String kinmusaki;
/**
 * BvUserProfileTable コンストラクター・コメント。
 */
public BvUserProfileInfo() {
}
/**
 * <h3>カナ名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @return int
 */
public java.lang.String getKanaName() {
	return kanaName;
}
/**
 * <h3>勤務先名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @return java.lang.String
 */
public java.lang.String getKinmusaki() {
	return kinmusaki;
}
/**
 * <h3>名前の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * <h3>カナ名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @param newKanaName int
 */
public void setKanaName(java.lang.String newKanaName) {
	kanaName = newKanaName;
}
/**
 * <h3>勤務先名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @param newKinmusaki java.lang.String
 */
public void setKinmusaki(java.lang.String newKinmusaki) {
	kinmusaki = newKinmusaki;
}
/**
 * <h3>名前のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/25 午後 02:15:58)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
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
	me.append(name + "\n");
	me.append(kanaName + "\n");
	me.append(kinmusaki + "\n");
	return me.toString();
}
}
