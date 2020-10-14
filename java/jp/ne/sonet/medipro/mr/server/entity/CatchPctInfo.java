package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>キャッチ画像情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:18:38)
 * @author: 
 */
public class CatchPctInfo {
	protected String picture;
	protected String pictureType;
	protected java.lang.String pictureCD;
	protected java.lang.String pictureName;
/**
 * CatchPctInfo コンストラクター・コメント。
 */
public CatchPctInfo() {
}
/**
 * <h3>画像ファイル内容の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPicture() {
	return picture;
}
/**
 * <h3>画像コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>画像名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:14:51)
 * @return java.lang.String
 */
public java.lang.String getPictureName() {
	return pictureName;
}
/**
 * <h3>画像形式の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPictureType() {
	return pictureType;
}
/**
 * <h3>画像ファイル内容のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:24:03)
 * @param newPicture java.lang.String
 */
public void setPicture(java.lang.String newPicture) {
	picture = newPicture;
}
/**
 * <h3>画像コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:14:51)
 * @param newPicture_cd java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
}
/**
 * <h3>画像名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:14:51)
 * @param newPicture_name java.lang.String
 */
public void setPictureName(java.lang.String newPictureName) {
	pictureName = newPictureName;
}
/**
 * <h3>画像形式のセット</h3>
 * 
 * <br>

 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:24:03)
 * @param newPictureType java.lang.String
 */
public void setPictureType(java.lang.String newPictureType) {
	pictureType = newPictureType;
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
	me.append(pictureCD + "\n");
	me.append(pictureName + "\n");
	me.append(picture + "\n");
	me.append(pictureType + "\n");	
	return me.toString();
}
}
