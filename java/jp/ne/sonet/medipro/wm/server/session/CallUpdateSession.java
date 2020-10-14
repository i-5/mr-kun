package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;

/**
 * �R�[�����e�ǉ��E�ύX�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class CallUpdateSession {
    public static final int MESMODE_NONE          = 0;	// ��\��
    public static final int MESMODE_CONFIRM       = 1;	// �X�V�m�F
    public static final int MESMODE_CANNOT_UPDATE = 2;	// �X�V�s��
    public static final int MESMODE_UPDATE        = 3;	// �X�V����

    public static final int UPDMODE_NEW           = 0;	// �R�[�h�ύX��(�V�K�ǉ�)
    public static final int UPDMODE_UPDATE        = 1;	// �R�[�h�ύX�s��(�X�V)

    private String callNaiyoCd = null;			// �R�[�����e�R�[�h
    private String callNaiyo   = null;			// �R�[�����e
    private int    messageMode = MESMODE_NONE;	// ���b�Z�[�W�o�̓��[�h
    private int    updateMode  = UPDMODE_NEW;	// �X�V���[�h

    /**
     * �R�[�����e�R�[�h��ݒ肷��B
     * @param callNaiyoCd �R�[�����e�R�[�h
     */
    public void setCallNaiyoCd(String callNaiyoCd) {
        this.callNaiyoCd = callNaiyoCd;
    }

    /**
     * �R�[�����e�R�[�h���擾����B
     * @return �R�[�����e�R�[�h
     */
    public String getCallNaiyoCd() {
        return this.callNaiyoCd;
    }

    /**
     * �R�[�����e��ݒ肷��B
     * @param callNaiyo �R�[�����e
     */
    public void setCallNaiyo(String callNaiyo) {
        this.callNaiyo = callNaiyo;
    }

    /**
     * �R�[�����e���擾����B
     * @return �R�[�����e
     */
    public String getCallNaiyo() {
        return this.callNaiyo;
    }

    /**
     * ���b�Z�[�W���[�h��ݒ肷��B
     * @param mode ���b�Z�[�W���[�h
     */
    public void setMessageMode(int mode) {
        this.messageMode = mode;
    }

    /**
     * ���b�Z�[�W���[�h���擾����B
     * @return ���b�Z�[�W���[�h
     */
    public int getMessageMode() {
        return this.messageMode;
    }

    /**
     * �X�V���[�h��ݒ肷��B
     * @param mode �X�V���[�h
     */
    public void setUpdateMode(int mode) {
        this.updateMode = mode;
    }

    /**
     * �X�V���[�h���擾����B
     * @return �X�V���[�h
     */
    public int getUpdateMode() {
        return this.updateMode;
    }

    /**
     * �Z�b�V������������������B
     */
    public void initSession() {
        this.callNaiyoCd = null;
        this.callNaiyo   = null;
        this.messageMode = MESMODE_NONE;
        this.updateMode  = UPDMODE_NEW;
    }
}
