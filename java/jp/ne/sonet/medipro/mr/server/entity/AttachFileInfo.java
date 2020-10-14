package jp.ne.sonet.medipro.mr.server.entity;

/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:52:59)
 * @author: 
 */
public class AttachFileInfo {
	protected String seq;
	protected String attachFullPath;
	protected String inputFullPath;
	protected long maxFileSize;
	protected String inputFile;
/**
 * AttachFileInfo コンストラクター・コメント。
 */
public AttachFileInfo() {
	super();
}
/**
 * <h3>添付ファイル名（フルパス）の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getAttachFullPath() {
	return attachFullPath;
}
/**
 * <h3>格納ファイル名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 11:31:58)
 * @return java.lang.String
 */
public java.lang.String getInputFile() {
	return inputFile;
}
/**
 * <h3>格納ファイル名（フルパス）の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getInputFullPath() {
	return inputFullPath;
}
/**
 * <h3>ＭＡＸファイルサイズの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 03:34:28)
 * @return long
 */
public long getMaxFileSize() {
	return maxFileSize;
}
/**
 * <h3>ＳＥＱの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getSeq() {
	return seq;
}
/**
 * <h3>添付ファイル名（フルパス）のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @param newAttachFullPath java.lang.String
 */
public void setAttachFullPath(java.lang.String newAttachFullPath) {
	attachFullPath = newAttachFullPath;
}
/**
 * <h3>格納ファイル名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 11:31:58)
 * @param newInputFile java.lang.String
 */
public void setInputFile(java.lang.String newInputFile) {
	inputFile = newInputFile;
}
/**
 * <h3>格納ファイル名（フルパス）のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @param newInputFullPath java.lang.String
 */
public void setInputFullPath(java.lang.String newInputFullPath) {
	inputFullPath = newInputFullPath;
}
/**
 * <h3>ＭＡＸファイルサイズのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 03:34:28)
 * @param newMaxFileSize long
 */
public void setMaxFileSize(long newMaxFileSize) {
	maxFileSize = newMaxFileSize;
}
/**
 * <h3>ＳＥＱのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/02 午前 02:59:30)
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
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
	me.append(seq + "\n");
	me.append(maxFileSize + "\n");
	me.append(attachFullPath + "\n");
	me.append(inputFullPath + "\n");
	me.append(inputFile + "\n");	
	return me.toString();
}
}
