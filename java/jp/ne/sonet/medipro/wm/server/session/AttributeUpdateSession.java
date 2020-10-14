package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.AttributeInfo;

/**
 * �����ǉ��E�ύX�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class AttributeUpdateSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;		// �ʏ�
	public static final int STATE_ALERT 	= 1;		// �A���[�g�o��
	public static final int STATE_CONFIRM	= 2;		// �R���t�@�[���o��
	public static final int STATE_REPORT	= 3;		// ���ʏo��

	private int nStatus = STATE_NORMAL;					// ���
	private AttributeInfo objAttributeInfo = null;		// �������

	/**
     * ��Ԃ�ݒ肷��B
     * @param status ���
     */
	public void setStatus(int status) {
		this.nStatus = status;
	}

	/**
     * ��Ԃ��擾����B
     * @return ���
     */
	public int getStatus() {
		return this.nStatus;
	}
	/**
     * ��������ݒ肷��B
     * @param info �������
     */
	public void setAttributeInfo(AttributeInfo info) {
		objAttributeInfo = info;
	}

	/**
     * ���������擾����B
     * @return �������
     */
	public AttributeInfo getAttributeInfo() {
		return objAttributeInfo;
	}

}
