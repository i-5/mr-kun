package jp.ne.sonet.medipro.mr.server.entity;

/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:52:59)
 * @author: 
 */
public class AttachFileInfo {
	protected String seq;
	protected String attachFullPath;
	protected String inputFullPath;
	protected long maxFileSize;
	protected String inputFile;
/**
 * AttachFileInfo �R���X�g���N�^�[�E�R�����g�B
 */
public AttachFileInfo() {
	super();
}
/**
 * <h3>�Y�t�t�@�C�����i�t���p�X�j�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getAttachFullPath() {
	return attachFullPath;
}
/**
 * <h3>�i�[�t�@�C�����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 11:31:58)
 * @return java.lang.String
 */
public java.lang.String getInputFile() {
	return inputFile;
}
/**
 * <h3>�i�[�t�@�C�����i�t���p�X�j�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getInputFullPath() {
	return inputFullPath;
}
/**
 * <h3>�l�`�w�t�@�C���T�C�Y�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 03:34:28)
 * @return long
 */
public long getMaxFileSize() {
	return maxFileSize;
}
/**
 * <h3>�r�d�p�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @return java.lang.String
 */
public java.lang.String getSeq() {
	return seq;
}
/**
 * <h3>�Y�t�t�@�C�����i�t���p�X�j�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @param newAttachFullPath java.lang.String
 */
public void setAttachFullPath(java.lang.String newAttachFullPath) {
	attachFullPath = newAttachFullPath;
}
/**
 * <h3>�i�[�t�@�C�����̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 11:31:58)
 * @param newInputFile java.lang.String
 */
public void setInputFile(java.lang.String newInputFile) {
	inputFile = newInputFile;
}
/**
 * <h3>�i�[�t�@�C�����i�t���p�X�j�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @param newInputFullPath java.lang.String
 */
public void setInputFullPath(java.lang.String newInputFullPath) {
	inputFullPath = newInputFullPath;
}
/**
 * <h3>�l�`�w�t�@�C���T�C�Y�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 03:34:28)
 * @param newMaxFileSize long
 */
public void setMaxFileSize(long newMaxFileSize) {
	maxFileSize = newMaxFileSize;
}
/**
 * <h3>�r�d�p�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 02:59:30)
 * @param newSeq java.lang.String
 */
public void setSeq(java.lang.String newSeq) {
	seq = newSeq;
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
	me.append(seq + "\n");
	me.append(maxFileSize + "\n");
	me.append(attachFullPath + "\n");
	me.append(inputFullPath + "\n");
	me.append(inputFile + "\n");	
	return me.toString();
}
}
