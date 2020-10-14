package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

/**
 * �����ꗗ�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class AttributeListSession {
    /** ��� */
    public static final int STATE_NORMAL    = 0;    // �ʏ�
    public static final int STATE_ALERT_1   = 1;    // �A���[�g�o��
    public static final int STATE_ALERT_2   = 2;    // �A���[�g�o��
    public static final int STATE_CONFIRM_1 = 3;    // �R���t�@�[���o��
    public static final int STATE_CONFIRM_2 = 4;    // �R���t�@�[���o��
    public static final int STATE_REPORT_1  = 5;    // ���ʏo��
    public static final int STATE_REPORT_2  = 6;    // ���ʏo��

    private int nStatus = STATE_NORMAL;             // ���
    private int nNowPage = 1;                       // ���y�[�W
    private boolean bHasPrevPage = false;           // �O�y�[�W�L���t���O
    private boolean bHasNextPage = false;           // ���y�[�W�L���t���O
    private String strSortKey = "link_lib.link_bunrui_cd";  // �\�[�g�L�[
    private static Hashtable mapOrder = new Hashtable();// ���~�}�b�v
    static {
        mapOrder.put("link_lib.link_bunrui_cd", "ASC");
        mapOrder.put("link_lib.honbun_text", "DESC");
    }
    private Hashtable mapCheckbox = new Hashtable();// �`�F�b�N�{�b�N�X�}�b�v

    /**
     * �\���y�[�W��ݒ肷��B
     * @param arg �\���y�[�W
     */
    public void setPage(int arg) {
        nNowPage = arg;
    }

    /**
     * �\���y�[�W���擾����B
     * @return �\���y�[�W
     */
    public int getPage() {
        return nNowPage;
    }

    /**
     * �O�y�[�W�̗L����ݒ肷��B
     * @param arg �O�y�[�W�̗L��
     */
    public void setPrevPage(boolean arg) {
        bHasPrevPage = arg;
    }

    /**
     * �O�y�[�W�̗L�����擾����B
     * @return �O�y�[�W�̗L��
     */
    public boolean hasPrevPage() {
        return bHasPrevPage;
    }

    /**
     * ���y�[�W�̗L����ݒ肷��B
     * @param arg ���y�[�W�̗L��
     */
    public void setNextPage(boolean arg) {
        bHasNextPage = arg;
    }

    /**
     * ���y�[�W�̗L�����擾����B
     * @return ���y�[�W�̗L��
     */
    public boolean hasNextPage() {
        return bHasNextPage;
    }

    /**
     * �\�[�g�L�[��ݒ肷��B
     * @param arg �\�[�g�L�[
     */
    public void setSortKey(String arg) {
        strSortKey = arg;
    }

    /**
     * �\�[�g�L�[���擾����B
     * @return �\�[�g�L�[
     */
    public String getSortKey() {
        return strSortKey;
    }

    /**
     * �I�[�_�[��ݒ肷��B
     * @param arg �I�[�_�[
     */
    public void setOrder(String arg) {
        mapOrder.remove(strSortKey);
        mapOrder.put(strSortKey, arg);
    }

    /**
     * �I�[�_�[���擾����B
     * @return �I�[�_�[
     */
    public String getOrder() {
        return (String)mapOrder.get(strSortKey);
    }

    /**
     * ��Ԃ�ݒ肷��B
     * @param arg ���
     */
    public void setStatus(int arg) {
        nStatus = arg;
    }

    /**
     * ��Ԃ��擾����B
     * @return ���
     */
    public int getStatus() {
        return nStatus;
    }

    /**
     * �`�F�b�N��Ԃ�ݒ肷��B
     * @param arg1 �`�F�b�N�{�b�N�X��
     * @param arg2 �`�F�b�N���
     */
    public void setCheck(String arg1, String arg2) {
        mapCheckbox.remove(arg1);
        mapCheckbox.put(arg1, arg2);
    }

    /**
     * �`�F�b�N��Ԃ��擾����B
     * @param arg �`�F�b�N�{�b�N�X��
     * @return �`�F�b�N���
     */
    public String getCheck(String arg) {
        return (String)mapCheckbox.get(arg);
    }

    /**
     * �`�F�b�N��Ԃ��N���A����B
     */
    public void clearCheck() {
        mapCheckbox.clear();
    }

    /**
     * ���b�Z�[�W�A�`�F�b�N��Ԃ��N���A����B
     */
    public void clear() {
        setStatus(STATE_NORMAL);
        clearCheck();
    }

}
