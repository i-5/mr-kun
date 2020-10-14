package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.TeikeiClassInfo;

/**
 * �����N���ޒǉ��E�X�V��ʗp�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class TeikeiClassUpdateSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;		// �ʏ�
	public static final int STATE_ALERT 	= 1;		// �A���[�g�o��
	public static final int STATE_CONFIRM	= 2;		// �R���t�@�[���o��
	public static final int STATE_REPORT	= 3;		// ���ʏo��

	private int nStatus = STATE_NORMAL;					// ���
	private TeikeiClassInfo objTeikeiClassInfo = null;		// �����N���ޏ��

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
     * �����N���ޏ���ݒ肷��B
     * @param info �����N���ޏ��
     */
	public void setTeikeiClassInfo(TeikeiClassInfo info) {
		objTeikeiClassInfo = info;
	}

	/**
     * �����N���ޏ����擾����B
     * @return �����N���ޏ��
     */
	public TeikeiClassInfo getTeikeiClassInfo() {
		return objTeikeiClassInfo;
	}

}
