package jp.ne.sonet.medipro.mr;

/**
 * <h3>�萔�}�X�^�e�[�u�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/30 �ߌ� 06:31:40)
 * @author: 
 */
public class ConstantMasterTable {
	protected String name;
	protected String naiyo1;
	protected String naiyo2;
	protected String naiyo3;
/**
 * ConstantMasterTable �R���X�g���N�^�[�E�R�����g�B
 */
public ConstantMasterTable() {
}
/**
 * @return java.lang.String
 */
public java.lang.String getNaiyo1() {
	return naiyo1;
}
/**
 * @return java.lang.String
 */
public java.lang.String getNaiyo2() {
	return naiyo2;
}
/**
 * @return java.lang.String
 */
public java.lang.String getNaiyo3() {
	return naiyo3;
}
/**
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * @param newNaiyo1 java.lang.String
 */
public void setNaiyo1(java.lang.String newNaiyo1) {
	naiyo1 = newNaiyo1;
}
/**
 * @param newNaiyo2 java.lang.String
 */
public void setNaiyo2(java.lang.String newNaiyo2) {
	naiyo2 = newNaiyo2;
}
/**
 * @param newNaiyo3 java.lang.String
 */
public void setNaiyo3(java.lang.String newNaiyo3) {
	naiyo3 = newNaiyo3;
}
/**
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * @return java.lang.String
 */
public String toString() {
	StringBuffer me = new StringBuffer();
	me.append(name + "\n");
	me.append(naiyo1 + "\n");
	me.append(naiyo2 + "\n");
	me.append(naiyo3 + "\n");
	return me.toString();
}
}
