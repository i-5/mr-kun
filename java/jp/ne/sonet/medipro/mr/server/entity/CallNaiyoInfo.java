package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�R�[�����e���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:45:52)
 * @author: 
 */
public class CallNaiyoInfo {
	protected String callNaiyoCD;
	protected String callNaiyo;
/**
 * CallNaiyoInfo �R���X�g���N�^�[�E�R�����g�B
 */
public CallNaiyoInfo(String callNaiyoCD) {
	this.callNaiyoCD = callNaiyoCD;
}
/**
 * <h3>�R�[�����e�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:47:55)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyo() {
	return callNaiyo;
}
/**
 * <h3>�R�[�����e�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:47:55)
 * @return java.lang.String
 */
public java.lang.String getCallNaiyoCD() {
	return callNaiyoCD;
}
/**
 * <h3>�R�[�����e�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:47:55)
 * @param newCallNaiyo java.lang.String
 */
public void setCallNaiyo(java.lang.String newCallNaiyo) {
	callNaiyo = newCallNaiyo;
}
/**
 * <h3>�R�[�����e�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:47:55)
 * @param newCallNaiyoCD java.lang.String
 */
public void setCallNaiyoCD(java.lang.String newCallNaiyoCD) {
	callNaiyoCD = newCallNaiyoCD;
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
	me.append(callNaiyo + "\n");
	me.append(callNaiyoCD + "\n");	
	return me.toString();
}
}
