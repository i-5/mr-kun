package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * �T�u�}�X�^�[�ǉ��E�ύX��ʗp�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class SubUpdateSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;	// �ʏ�
	public static final int STATE_ALERT_1 	= 1;	// �A���[�g�o��
	public static final int STATE_ALERT_2 	= 2;	// �A���[�g�o��
	public static final int STATE_CONFIRM	= 3;	// �R���t�@�[���o��
	public static final int STATE_REPORT	= 4;	// ���ʏo��

	private int nStatus = STATE_NORMAL;				// ���
	private MrInfo objMrInfo = null;				// �l�q���

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
     * �l�q����ݒ肷��B
     * @param info �l�q���
     */
	public void setMrInfo(MrInfo info) {
		objMrInfo = info;
	}

	/**
     * �l�q�����擾����B
     * @return �l�q���
     */
	public MrInfo getMrInfo() {
		return objMrInfo;
	}

}
