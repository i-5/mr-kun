package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;

/**
 * �R�[�����e�ꗗ�Z�b�V�������Ǘ�
 * @auther
 * @version
 */
public class CallListSession {
    public static final int MESMODE_NONE          = 0;	// ��\��
    public static final int MESMODE_NO_CHECK      = 1;	// �`�F�b�N����
    public static final int MESMODE_CONFIRM       = 2;	// �폜�m�F
    public static final int MESMODE_CANNOT_DELETE = 3;	// �폜�s��
    public static final int MESMODE_DELETE        = 4;	// �폜����

    private int       currentRow  = 1;					// �擪�s�ԍ�
    private boolean   prev        = false;				// �O�y�[�W�L���t���O
    private boolean   next        = false;				// ���y�[�W�L���t���O
    private String    sortKey     = "call_naiyo_cd";	// �\�[�g�L�[
    private String    order       = "ASC";				// �\�[�g��
    private int       messageMode = MESMODE_NONE;		// ���b�Z�[�W�o�̓��[�h
    private Hashtable checkTable  = new Hashtable();	// �`�F�b�N�{�b�N�X�̏��

    /**
     * �擪�s�ԍ���ݒ肷��B
     * @param newRow �擪�s�ԍ�
     */
    public void setCurrentRow(int newRow) {
        this.currentRow = newRow;
    }

    /**
     * �擪�s�ԍ����擾����B
     * @return �擪�s�ԍ�
     */
    public int getCurrentRow() {
        return this.currentRow;
    }

    /**
     * �O�y�[�W�̗L����ݒ肷��B
     * @param newPrev �O�y�[�W�̗L��
     */
    public void setPrev(boolean newPrev) {
        this.prev = newPrev;
    }

    /**
     * �O�y�[�W�̗L�����擾����B
     * @return �O�y�[�W�̗L��
     */
    public boolean isPrev() {
        return this.prev;
    }

    /**
     * ���y�[�W�̗L����ݒ肷��B
     * @param newNext ���y�[�W�̗L��
     */
    public void setNext(boolean newNext) {
        this.next = newNext;
    }

    /**
     * ���y�[�W�̗L�����擾����B
     * @return ���y�[�W�̗L��
     */
    public boolean isNext() {
        return this.next;
    }

    /**
     * �\�[�g�L�[��ݒ肷��B
     * @param newSortKey �\�[�g�L�[
     */
    public void setSortKey(String newSortKey) {
        this.sortKey = newSortKey;
    }

    /**
     * �\�[�g�L�[���擾����B
     * @return �\�[�g�L�[
     */
    public String getSortKey() {
        return this.sortKey;
    }

    /**
     * �\�[�g����ݒ肷��B
     * @param newOrder �\�[�g��
     */
    public void setOrder(String newOrder) {
        this.order = newOrder;
    }

    /**
     * �\�[�g�����擾����B
     * @return �\�[�g��
     */
    public String getOrder() {
        return this.order;
    }

    /**
     * �\�[�g�����t�]����B
     */
    public void setOrderReverse() {
        if (this.order.equals("ASC")) {
            this.order = "DESC";
        }
        else {
            this.order = "ASC";
        }
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
     * �`�F�b�N��Ԃ�ݒ肷��B
     * @param target �`�F�b�N�{�b�N�X�̖��O
     * @param status �`�F�b�N���
     */
    public void setChecked(String target, boolean status) {
        checkTable.remove(target);
        checkTable.put(target, new Boolean(status));
    }

    /**
     * �`�F�b�N��Ԃ��擾����B
     * @param target �`�F�b�N�{�b�N�X�̖��O
     * @return �`�F�b�N���
     */
    public boolean isChecked(String target) {
        Boolean status = (Boolean)checkTable.get(target);
        return (status != null) ? status.booleanValue() : false;
    }

    /**
     * �`�F�b�N��Ԃ��N���A����B
     */
    public void clearChecked() {
        checkTable.clear();
    }

    /**
     * ���b�Z�[�W�ƃ`�F�b�N��Ԃ��N���A����B
     */
    public void clear() {
        this.messageMode = MESMODE_NONE;
        checkTable.clear();
    }

    /**
     * �Z�b�V������������������B
     */
    public void initSession() {
        this.currentRow  = 1;
        this.prev        = false;
        this.next        = false;
        this.sortKey     = "call_naiyo_cd";
        this.order       = "ASC";
        this.messageMode = MESMODE_NONE;
        this.checkTable  = new Hashtable();
    }
}
