package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.CompanyInfo;

/**
 * �����L���O�\���ݒ�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class ActionRankingSession {
	/** ��� */
	public static final int STATE_NORMAL	= 0;		// �ʏ�
	public static final int STATE_ALERT 	= 1;		// �A���[�g�o��
	public static final int STATE_CONFIRM	= 2;		// �R���t�@�[���o��
	public static final int STATE_REPORT	= 3;		// ���ʏo��

	private int nStatus = STATE_NORMAL;					// ���
	private CompanyInfo objCompanyInfo = null;	// �����L���O�\���ݒ���

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
     * �����L���O�\���ݒ����ݒ肷��B
     * @param info �����L���O�\���ݒ���
     */
	public void setCompanyInfo(CompanyInfo info) {
		objCompanyInfo = info;
	}

	/**
     * �����L���O�\���ݒ�����擾����B
     * @return �����L���O�\���ݒ���
     */
	public CompanyInfo getCompanyInfo() {
		return objCompanyInfo;
	}

}
