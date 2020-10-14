package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

/**
 * <h3>�l�r�f�{�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:26:05)
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
 * MsgBody �R���X�g���N�^�[�E�R�����g�B
 */
public MsgBodyInfo(String messageID) {
	this.messageID = messageID;
}
/**
 * <h3>�Y�t�t�@�C�����e�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 08:00:55)
 * @return java.lang.String
 */
public Enumeration getAttachfileinfo() {
	return attachfiletable;
}
/**
 * <h3>�Y�t�����N�摜�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 08:28:36)
 * @return java.lang.String
 */
public Enumeration getAttachilinktable() {
	return attachlinktable;
}
/**
 * <h3>�R�[�����e�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/06 �ߌ� 07:14:27)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyoCD() {
	return callNaiyoCD;
}
/**
 * <h3>�l�r�f�{���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getHonbunText() {
	return honbunText;
}
/**
 * <h3>���ȏЉ�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getJikosyokai() {
	return jikosyokai;
}
/**
 * <h3>���b�Z�[�W�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getMessageID() {
	return messageID;
}
/**
 * <h3>�摜�t�@�C�����e�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getPicture() {
	return picture;
}
/**
 * <h3>�摜�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/06 �ߌ� 06:46:38)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>�摜�`���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 08:26:34)
 * @return java.lang.String
 */
public java.lang.String getPictureType() {
	return pictureType;
}
/**
 * <h3>�^�C�g���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @return java.lang.String
 */
public java.lang.String getTitle() {
	return title;
}
/**
 * <h3>���ڃ����N�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 08:45:02)
 * @return java.lang.String
 */
public java.lang.String getUrl() {
	return url;
}
/**
 * <h3>�Y�t�t�@�C�����e�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 08:00:55)
 * @param newAttachFile java.lang.String
 */
public void setAttachfiletable(Enumeration newAttachfiletable) {
	attachfiletable = newAttachfiletable;
}
/**
 * <h3>�Y�t�����N�摜�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 08:28:36)
 * @param newAttachiLinkPicture java.lang.String
 */
public void setAttachiLinkPicture(Enumeration newAttachlinktable) {
	attachlinktable = newAttachlinktable;
}
/**
 * <h3>�R�[�����e�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/06 �ߌ� 07:14:27)
 * @param newCallNaiyoCD java.lang.String
 */
public void setCallNaiyoCD(java.lang.String newCallNaiyoCD) {
	callNaiyoCD = newCallNaiyoCD;
}
/**
 * <h3>�l�r�f�{���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @param newHonbun_text java.lang.String
 */
public void setHonbunText(java.lang.String newHonbunText) {
	honbunText = newHonbunText;
}
/**
 * <h3>���ȏЉ�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @param newJikosyokai java.lang.String
 */
public void setJikosyokai(java.lang.String newJikosyokai) {
	jikosyokai = newJikosyokai;
}
/**
 * <h3>�摜�t�@�C�����e�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @param newPicture java.lang.String
 */
public void setPicture(java.lang.String newPicture) {
	picture = newPicture;
}
/**
 * <h3>�摜�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/06 �ߌ� 06:46:38)
 * @param newPictureCD java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
}
/**
 * <h3>�摜�`���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 08:26:34)
 * @param newPictureType java.lang.String
 */
public void setPictureType(java.lang.String newPictureType) {
	pictureType = newPictureType;
}
/**
 * <h3>�^�C�g���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:32:32)
 * @param newTitle java.lang.String
 */
public void setTitle(java.lang.String newTitle) {
	title = newTitle;
}
/**
 * <h3>���ڃ����N�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 08:45:02)
 * @param newUrl java.lang.String
 */
public void setUrl(java.lang.String newUrl) {
	url = newUrl;
}
/**
 * <h3>�����񉻂���</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:51:35)
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
