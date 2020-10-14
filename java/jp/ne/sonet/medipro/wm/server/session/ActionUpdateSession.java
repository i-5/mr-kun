package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.ActionInfo;

/**
 * �d�v�x�ǉ��E�ύX�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class ActionUpdateSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;		// �ʏ�
	public static final int STATE_ALERT 	= 1;		// �A���[�g�o��
	public static final int STATE_CONFIRM	= 2;		// �R���t�@�[���o��
	public static final int STATE_REPORT	= 3;		// ���ʏo��

	private int nStatus = STATE_NORMAL;					// ���
	private ActionInfo objActionInfo = null;	// �d�v�x���

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
     * �d�v�x����ݒ肷��B
     * @param info �d�v�x���
     */
	public void setActionInfo(ActionInfo info) {
		objActionInfo = info;
	}

	/**
     * �d�v�x�����擾����B
     * @return �d�v�x���
     */
	public ActionInfo getActionInfo() {
		return objActionInfo;
	}

}
