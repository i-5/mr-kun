package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

/**
 * <h3>ＭＳＧ本文情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 23:26:05)
 * @author: 
 */
public class MsgBodyInfo {
	protected String messageID;
	protected String honbunText;
	protected String picture;
	protected String jikosyokai;
	protected String title;
	protected String url;
	protected Enumeration attachfiletable;
	protected Enumeration attachlinktable;
	protected String pictureType;
	protected java.lang.String pictureCD;
	protected java.lang.String callNaiyoCD;
/**
 * MsgBody コンストラクター・コメント。
 */
public MsgBodyInfo(String messageID) {
	this.messageID = messageID;
}
/**
 * <h3>添付ファイル内容の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/24 午後 08:00:55)
 * @return java.lang.String
 */
public Enumeration getAttachfileinfo() {
	return attachfiletable;
}
/**
 * <h3>添付リンク画像の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/24 午後 08:28:36)
 * @return java.lang.String
 */
public Enumeration getAttachilinktable() {
	return attachlinktable;
}
/**
 * <h3>コール内容コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/06 午後 07:14:27)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyoCD() {
	return callNaiyoCD;
}
/**
 * <h3>ＭＳＧ本文の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getHonbunText() {
	return honbunText;
}
/**
 * <h3>自己紹介の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getJikosyokai() {
	return jikosyokai;
}
/**
 * <h3>メッセージＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getMessageID() {
	return messageID;
}
/**
 * <h3>画像ファイル内容の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
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
 * 作成日 : (00/07/06 午後 06:46:38)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>画像形式の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 08:26:34)
 * @return java.lang.String
 */
public java.lang.String getPictureType() {
	return pictureType;
}
/**
 * <h3>タイトルの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getTitle() {
	return title;
}
/**
 * <h3>直接リンクコードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/20 午後 08:45:02)
 * @return java.lang.String
 */
public java.lang.String getUrl() {
	return url;
}
/**
 * <h3>添付ファイル内容のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/24 午後 08:00:55)
 * @param newAttachFile java.lang.String
 */
public void setAttachfiletable(Enumeration newAttachfiletable) {
	attachfiletable = newAttachfiletable;
}
/**
 * <h3>添付リンク画像のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/24 午後 08:28:36)
 * @param newAttachiLinkPicture java.lang.String
 */
public void setAttachiLinkPicture(Enumeration newAttachlinktable) {
	attachlinktable = newAttachlinktable;
}
/**
 * <h3>コール内容コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/06 午後 07:14:27)
 * @param newCallNaiyoCD java.lang.String
 */
public void setCallNaiyoCD(java.lang.String newCallNaiyoCD) {
	callNaiyoCD = newCallNaiyoCD;
}
/**
 * <h3>ＭＳＧ本文のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @param newHonbun_text java.lang.String
 */
public void setHonbunText(java.lang.String newHonbunText) {
	honbunText = newHonbunText;
}
/**
 * <h3>自己紹介のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @param newJikosyokai java.lang.String
 */
public void setJikosyokai(java.lang.String newJikosyokai) {
	jikosyokai = newJikosyokai;
}
/**
 * <h3>画像ファイル内容のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
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
 * 作成日 : (00/07/06 午後 06:46:38)
 * @param newPictureCD java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
}
/**
 * <h3>画像形式のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 08:26:34)
 * @param newPictureType java.lang.String
 */
public void setPictureType(java.lang.String newPictureType) {
	pictureType = newPictureType;
}
/**
 * <h3>タイトルのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:32:32)
 * @param newTitle java.lang.String
 */
public void setTitle(java.lang.String newTitle) {
	title = newTitle;
}
/**
 * <h3>直接リンクコードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/20 午後 08:45:02)
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
	me.append("honbun_text        = [" + honbunText + "]\n");
	me.append("jikosyokai         = [" + jikosyokai + "]\n");
	me.append("message_id         = [" + messageID + "]\n");
	me.append("picture            = [" + picture + "]\n");
	me.append("pictureType        = [" + pictureType + "]\n");
	me.append("title              = [" + title + "]\n");
	me.append("url                = [" + url + "]\n");
	me.append("attachfile         = [" + attachfiletable + "]\n");
	me.append("attachlink         = [" + attachlinktable + "]\n");
	return me.toString();
}
}
