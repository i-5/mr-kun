package jp.ne.sonet.medipro.mr;

/**
 * <h3>�Y�t�����N�e�[�u�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 07:47:52)
 * @author: 
 */
public class AttachLinkTable {
	protected String messageID;
	protected String seq;
	protected String url;
	protected String honbuText;
	protected String picture;
	protected String naigaiLinkKbn;
/**
 * AttachLinkTable �R���X�g���N�^�[�E�R�����g�B
 */
public AttachLinkTable() {
}
/**
 * @return java.lang.String
 */
public java.lang.String getHonbuText() {
	return honbuText;
}
/**
 * @return java.lang.String
 */
public java.lang.String getMessageID() {
	return messageID;
}
/**
 * @return java.lang.String
 */
public java.lang.String getNaigaiLinkKbn() {
	return naigaiLinkKbn;
}
/**
 * @return java.lang.String
 */
public java.lang.String getPicture() {
	return picture;
}
/**
 * @return java.lang.String
 */
public java.lang.String getSeq() {
	return seq;
}
/**
 * @return java.lang.String
 */
public java.lang.String getUrl() {
	return url;
}
/**
 * @param newHonbuText java.lang.String
 */
public void setHonbuText(java.lang.String newHonbuText) {
	honbuText = newHonbuText;
}
/**
 * @param newMessageID java.lang.String
 */
public void setMessageID(java.lang.String newMessageID) {
	messageID = newMessageID;
}
/**
 * @param newNaigaiLinkKbn java.lang.String
 */
public void setNaigaiLinkKbn(java.lang.String newNaigaiLinkKbn) {
	naigaiLinkKbn = newNaigaiLinkKbn;
}
/**
 * @param newPicture java.lang.String
 */
public void setPicture(java.lang.String newPicture) {
	picture = newPicture;
}
/**
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
}
/**
 * @param newUrl java.lang.String
 */
public void setUrl(java.lang.String newUrl) {
	url = newUrl;
}
}
