package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>リンクライブラリ情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:23:45)
 * @author: 
 */
public class LinkLibInfo {
	protected String linkCD;
	protected String bunruiName;
	protected String url;
	protected String honbunText;
	protected String picture;
	protected String niagaiLinkKbn;
	
	protected java.lang.String linkBunruiCD;
/**
 * LinkLibInfo コンストラクター・コメント。
 */
public LinkLibInfo() {
}
/**
 * <h3>分類名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @return java.lang.String
 */
public java.lang.String getBunruiName() {
	return bunruiName;
}
/**
 * <h3>本文テキストの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @return java.lang.String
 */
public java.lang.String getHonbunText() {
	return honbunText;
}
/**
 * <h3>リンク分類コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 05:42:28)
 * @return java.lang.String
 */
public java.lang.String getLinkBunruiCD() {
	return linkBunruiCD;
}
/**
 * <h3>リンクコードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @return java.lang.String
 */
public java.lang.String getLinkCD() {
	return linkCD;
}
/**
 * <h3>内外リンク区分の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @return java.lang.String
 */
public java.lang.String getNiagaiLinkKbn() {
	return niagaiLinkKbn;
}
/**
 * <h3>画像の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @return java.lang.String
 */
public java.lang.String getPicture() {
	return picture;
}
/**
 * <h3>ＵＲＬの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @return java.lang.String
 */
public java.lang.String getUrl() {
	return url;
}
/**
 * <h3>分類名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @param newBunruiName java.lang.String
 */
public void setBunruiName(java.lang.String newBunruiName) {
	bunruiName = newBunruiName;
}
/**
 * <h3>本文テキストのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @param newHonbunText java.lang.String
 */
public void setHonbunText(java.lang.String newHonbunText) {
	honbunText = newHonbunText;
}
/**
 * <h3>リンク分類コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 05:42:28)
 * @param newLinkBunruiCD java.lang.String
 */
public void setLinkBunruiCD(java.lang.String newLinkBunruiCD) {
	linkBunruiCD = newLinkBunruiCD;
}
/**
 * <h3>リンクコードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @param newLinkCD java.lang.String
 */
public void setLinkCD(java.lang.String newLinkCD) {
	linkCD = newLinkCD;
}
/**
 * <h3>内外リンク区分のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @param newNiagaiLinkKbn java.lang.String
 */
public void setNiagaiLinkKbn(java.lang.String newNiagaiLinkKbn) {
	niagaiLinkKbn = newNiagaiLinkKbn;
}
/**
 * <h3>画像のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 09:58:27)
 * @param newPicture java.lang.String
 */
public void setPicture(java.lang.String newPicture) {
	picture = newPicture;
}
/**
 * <h3>ＵＲＬのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:40:53)
 * @param newUrl java.lang.String
 */
public void setUrl(java.lang.String newUrl) {
	url = newUrl;
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
	me.append(linkCD + "\n");
	me.append(url + "\n");
	me.append(honbunText + "\n");
	me.append(linkBunruiCD + "\n");
	me.append(bunruiName + "\n");
	me.append(niagaiLinkKbn + "\n");
	me.append(picture + "\n");
	return me.toString();
}
}
