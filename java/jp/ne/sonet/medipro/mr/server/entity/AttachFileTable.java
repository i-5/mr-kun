package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�Y�t�t�@�C���e�[�u�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 07:46:45)
 * @author: 
 */
public class AttachFileTable {
	protected String messageID;
	protected String seq;
	protected String attachFile;
	protected String fileKbn;
/**
 * AttachFileTable �R���X�g���N�^�[�E�R�����g�B
 */
public AttachFileTable() {
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getAttachFile() {
	return attachFile;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getFileKbn() {
	return fileKbn;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getMessageID() {
	return messageID;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getSeq() {
	return seq;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @param newAttachFile java.lang.String
 */
public void setAttachFile(java.lang.String newAttachFile) {
	attachFile = newAttachFile;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @param newFileKbn java.lang.String
 */
public void setFileKbn(java.lang.String newFileKbn) {
	fileKbn = newFileKbn;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @param newMessageID java.lang.String
 */
public void setMessageID(java.lang.String newMessageID) {
	messageID = newMessageID;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:20:57)
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
}
}
