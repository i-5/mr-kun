package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>添付ファイルテーブル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 07:46:45)
 * @author: 
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
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getAttachFile() {
	return attachFile;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getFileKbn() {
	return fileKbn;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getMessageID() {
	return messageID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @return java.lang.String
 */
public java.lang.String getSeq() {
	return seq;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @param newAttachFile java.lang.String
 */
public void setAttachFile(java.lang.String newAttachFile) {
	attachFile = newAttachFile;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @param newFileKbn java.lang.String
 */
public void setFileKbn(java.lang.String newFileKbn) {
	fileKbn = newFileKbn;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @param newMessageID java.lang.String
 */
public void setMessageID(java.lang.String newMessageID) {
	messageID = newMessageID;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:20:57)
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
}
}
