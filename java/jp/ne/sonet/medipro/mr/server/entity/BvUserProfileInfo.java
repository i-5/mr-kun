package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�a�u�v���t�@�C�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:11:17)
 * @author: 
 */
public class BvUserProfileInfo {
	protected java.lang.String name;
	protected java.lang.String kanaName;
	protected java.lang.String kinmusaki;
/**
 * BvUserProfileTable �R���X�g���N�^�[�E�R�����g�B
 */
public BvUserProfileInfo() {
}
/**
 * <h3>�J�i���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @return int
 */
public java.lang.String getKanaName() {
	return kanaName;
}
/**
 * <h3>�Ζ��於�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @return java.lang.String
 */
public java.lang.String getKinmusaki() {
	return kinmusaki;
}
/**
 * <h3>���O�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * <h3>�J�i���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @param newKanaName int
 */
public void setKanaName(java.lang.String newKanaName) {
	kanaName = newKanaName;
}
/**
 * <h3>�Ζ��於�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @param newKinmusaki java.lang.String
 */
public void setKinmusaki(java.lang.String newKinmusaki) {
	kinmusaki = newKinmusaki;
}
/**
 * <h3>���O�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/25 �ߌ� 02:15:58)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
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
	me.append(name + "\n");
	me.append(kanaName + "\n");
	me.append(kinmusaki + "\n");
	return me.toString();
}
}
