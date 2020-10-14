package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

/**
 * �T�u�}�X�^�[�ꗗ�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class SubListSession {
    /** ��� */
    public static final int STATE_NORMAL    = 0;    // �ʏ�
    public static final int STATE_ALERT     = 1;    // �A���[�g�o��
    public static final int STATE_CONFIRM   = 2;    // �R���t�@�[���o��
    public static final int STATE_REPORT    = 3;    // ���ʏo��

    private int nStatus = STATE_NORMAL; // ���
    private int nNowPage = 1;               // ���y�[�W
    private boolean bHasPrevPage = false;   // �O�y�[�W�L���t���O
    private boolean bHasNextPage = false;   // ���y�[�W�L���t���O
    private String strSortKey = "mr.mr_id"; // �\�[�g�L�[
    private static Hashtable mapOrder = new Hashtable();    // ���~�}�b�v
    static {
        mapOrder.put("mr.shiten_cd", "DESC");
        mapOrder.put("mr.eigyosyo_cd", "DESC");
        mapOrder.put("attr_cd", "DESC");
        mapOrder.put("mr.mr_id", "DESC");
        mapOrder.put("mr.name", "ASC");
    }
    private Hashtable mapCheckbox = new Hashtable();    // �`�F�b�N�{�b�N�X�}�b�v

    /**
     * �\���y�[�W��ݒ肷��B
     * @param page �\���y�[�W
     */
    public void setPage(int page) {
        this.nNowPage = page;
    }

    /**
     * �\���y�[�W���擾����B
     * @return �\���y�[�W
     */
    public int getPage() {
        return this.nNowPage;
    }

    /**
     * �O�y�[�W�̗L����ݒ肷��B
     * @param prev �O�y�[�W�̗L��
     */
    public void setPrevPage(boolean prev) {
        this.bHasPrevPage = prev;
    }

    /**
     * �O�y�[�W�̗L�����擾����B
     * @return �O�y�[�W�̗L��
     */
    public boolean hasPrevPage() {
        return this.bHasPrevPage;
    }

    /**
     * ���y�[�W�̗L����ݒ肷��B
     * @param prev ���y�[�W�̗L��
     */
    public void setNextPage(boolean next) {
        this.bHasNextPage = next;
    }

    /**
     * ���y�[�W�̗L�����擾����B
     * @return ���y�[�W�̗L��
     */
    public boolean hasNextPage() {
        return this.bHasNextPage;
    }

    /**
     * �\�[�g�L�[��ݒ肷��B
     * @param key �\�[�g�L�[
     */
    public void setSortKey(String key) {
        this.strSortKey = key;
    }

    /**
     * �\�[�g�L�[���擾����B
     * @return �\�[�g�L�[
     */
    public String getSortKey() {
        return this.strSortKey;
    }

    /**
     * �I�[�_�[��ݒ肷��B
     * @param order �I�[�_�[
     */
    public void setOrder(String order) {
        mapOrder.remove(strSortKey);
        mapOrder.put(strSortKey, order);
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
     * �`�F�b�N��Ԃ�ݒ肷��B
     * @param target �`�F�b�N�{�b�N�X��
     * @param status �`�F�b�N���
     */
    public void setCheck(String target, String status) {
        mapCheckbox.remove(target);
        mapCheckbox.put(target, status);
    }

    /**
     * �`�F�b�N��Ԃ��擾����B
     * @param target �`�F�b�N�{�b�N�X��
     * @return �`�F�b�N���
     */
    public String getCheck(String target) {
        return (String)mapCheckbox.get(target);
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
