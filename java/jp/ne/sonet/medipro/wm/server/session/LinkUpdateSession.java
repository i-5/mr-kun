package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.LinkLibInfo;

/**
 * �����N�ꗗ�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class LinkUpdateSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;		// �ʏ�
	public static final int STATE_ALERT 	= 1;		// �A���[�g�o��
	public static final int STATE_CONFIRM	= 2;		// �R���t�@�[���o��
	public static final int STATE_REPORT	= 3;		// ���ʏo��
	public static final int TEXT_ERR	= 4;		// "�^�C�g����"��"��������"

	private int nStatus = STATE_NORMAL;					// ���
	private LinkLibInfo objLinkLibInfo = null;			// �����N���

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
     * �����N����ݒ肷��B
     * @param info �����N���
     */
	public void setLinkLibInfo(LinkLibInfo info) {
		objLinkLibInfo = info;
	}

	/**
     * �����N�����擾����B
     * @return �����N���
     */
	public LinkLibInfo getLinkLibInfo() {
		return objLinkLibInfo;
	}

}
