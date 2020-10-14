package jp.ne.sonet.medipro.mr;

/**
 * @author: Harry Behrens
 */
public class AttachFileTable {
	protected String messageID;
	protected String seq;
	protected String attachFile;
	protected String fileKbn;
/**
 * AttachFileTable コンストラクター・コメント。
 */
public AttachFileTable() {
}
/**
 * @return java.lang.String
 */
public java.lang.String getAttachFile() {
	return attachFile;
}
/**
 * @return java.lang.String
 */
public java.lang.String getFileKbn() {
	return fileKbn;
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
public java.lang.String getSeq() {
	return seq;
}
/**
 * @param newAttachFile java.lang.String
 */
public void setAttachFile(java.lang.String newAttachFile) {
	attachFile = newAttachFile;
}
/**
 * @param newFileKbn java.lang.String
 */
public void setFileKbn(java.lang.String newFileKbn) {
	fileKbn = newFileKbn;
}
/**
 * @param newMessageID java.lang.String
 */
public void setMessageID(java.lang.String newMessageID) {
	messageID = newMessageID;
}
/**
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
}
}
