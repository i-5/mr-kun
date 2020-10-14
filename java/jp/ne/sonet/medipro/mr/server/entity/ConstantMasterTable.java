package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>定数マスタテーブル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:31:40)
 * @author: 
 */
public class ConstantMasterTable {
	protected String name;
	protected String naiyo1;
	protected String naiyo2;
	protected String naiyo3;
/**
 * ConstantMasterTable コンストラクター・コメント。
 */
public ConstantMasterTable() {
}
/**
 * <h3>内容１の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @return java.lang.String
 */
public java.lang.String getNaiyo1() {
	return naiyo1;
}
/**
 * <h3>内容２の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @return java.lang.String
 */
public java.lang.String getNaiyo2() {
	return naiyo2;
}
/**
 * <h3>内容３の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @return java.lang.String
 */
public java.lang.String getNaiyo3() {
	return naiyo3;
}
/**
 * <h3>名称の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * <h3>内容１のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @param newNaiyo1 java.lang.String
 */
public void setNaiyo1(java.lang.String newNaiyo1) {
	naiyo1 = newNaiyo1;
}
/**
 * <h3>内容２のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @param newNaiyo2 java.lang.String
 */
public void setNaiyo2(java.lang.String newNaiyo2) {
	naiyo2 = newNaiyo2;
}
/**
 * <h3>内容３のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
 * @param newNaiyo3 java.lang.String
 */
public void setNaiyo3(java.lang.String newNaiyo3) {
	naiyo3 = newNaiyo3;
}
/**
 * <h3>名称のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:35:39)
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
	me.append(naiyo1 + "\n");
	me.append(naiyo2 + "\n");
	me.append(naiyo3 + "\n");
	return me.toString();
}
}
