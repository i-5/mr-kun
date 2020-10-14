package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>�x�X�E�c�Ə��ꗗ�Z�b�V�����N���X.</strong>
 * @auther
 * @version
 */
public class BranchListSession {
    /////////////////////////////////////////////
    //class variables
    //
    private int currentRow;			// �擪�s�ԍ�
    private int maxRow;				// �ő�s��
    private boolean prev;			// �O�y�[�W�L���t���O
    private boolean next;			// ���y�[�W�L���t���O
    private String sortKey;			// �\�[�g�L�[
    private int messageState;		// ���b�Z�[�WID
    private Vector checkValue; 		// �`�F�b�N�{�^�����
    private static Hashtable orderMap;	// ���~�}�b�v
    private Vector deleteBranch;	// �폜�x�X�R�[�h
    private Vector deleteOffice;	// �폜�c�Ə��R�[�h

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
     */
    public BranchListSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("BranchListSession Created!!");
	}
	this.currentRow = 1;
	this.maxRow = 0;
	this.prev = false;
	this.next = false;
	this.sortKey = "shiten.shiten_cd";
	this.messageState = 0;
	this.checkValue = new Vector();
	this.orderMap = new Hashtable();
	orderMap.put("shiten.shiten_cd", " ASC");
	orderMap.put("eigyosyo_cd", " DESC");
	deleteBranch = new Vector();
	deleteOffice = new Vector();
    }
	
    /////////////////////////////////////////////
    //class methods
    //
    /**
     * �擪�s�ԍ���ݒ肷��.
     * @param newRow int
     */
    public void setCurrentRow(int newRow) {
	this.currentRow = newRow;
    }
    /**
     * �擪�s�ԍ����擾����.
     * @return int
     */
    public int getCurrentRow() {
	return this.currentRow;
    }
    /**
     * �ő�s����ݒ肷��.
     * @param newMaxRow int
     */
    public void setMaxRow(int newMaxRow) {
	this.maxRow = newMaxRow;
    }
    /**
     * �ő�s�����擾����.
     * @return int
     */
    public int getMaxRow() {
	return this.maxRow;
    }
    /**
     * �O�y�[�W�̗L����ݒ肷��.
     * @param prev boolean
     */
    public void setPrev(boolean prev) {
	this.prev = prev;
    }
    /**
     * �O�y�[�W�̗L�����擾����.
     * @return boolean
     */
    public boolean isPrev() {
	return this.prev;
    }
    /**
     * ���y�[�W�̗L����ݒ肷��.
     * @param prev boolean
     */
    public void setNext(boolean next) {
	this.next = next;
    }
    /**
     * ���y�[�W�̗L�����擾����.
     * @return boolean
     */
    public boolean isNext() {
	return this.next;
    }
    /**
     * �\�[�g�L�[��ݒ肷��.
     * @param newOrder String
     */
    public void setSortKey(String newSortKey) {
	this.sortKey = newSortKey;
    }
    /**
     * �\�[�g�L�[���擾����.
     * @return String
     */
    public String getSortKey() {
	return this.sortKey;
    }
    /**
     * ���b�Z�[�WID��ݒ肷��.
     * @param newMessageState int
     */
    public void setMessageState(int newMessageState) {
	this.messageState = newMessageState;
    }
    /**
     * ���b�Z�[�WID���擾����.
     * @return int
     */
    public int getMessageState() {
	return this.messageState;
    }
    /**
     * �`�F�b�N�{�^����Ԃ�ݒ肷��.
     * @param newCheckValue String
     */
    public void setCheckValue(String newCheckValue) {
	this.checkValue.add(newCheckValue);
    }
    /**
     * �`�F�b�N�{�^����Ԃ��擾����.
     * @return Vector
     */
    public Vector getCheckValue() {
	return this.checkValue;
    }
    /**
     * �`�F�b�N�{�^����Ԃ��N���A����.
     */
    public void crearCheckValue() {
	this.checkValue.removeAllElements();
    }
    /**
     * �`�F�b�N�{�^���̑I�𐔂��擾����.
     * @return int
     */
    public int getCheckSize() {
	return this.checkValue.size();
    }
    /**
     * �I�[�_�[��ݒ肷��.
     * @param order String
     */
    public void setOrder(String order) {
	orderMap.remove(sortKey);
	orderMap.put(sortKey, order);
    }
    /**
     * �I�[�_�[���擾����.
     * @return String
     */
    public String getOrder() {
	return (String)orderMap.get(sortKey);
    }
    /**
     * �I�[�_�[���t�]����.
     */
    public void reverseOrder() {
	String order = (String)orderMap.get(sortKey);
	orderMap.remove(sortKey);
	if ( order.equals(" ASC") ) {
	    order = " DESC";
	} else {
	    order = " ASC";
	}
	orderMap.put(sortKey, order);
    }
    /**
     * �폜�x�X�R�[�h��ݒ肷��.
     * @param newDeleteBranch String
     */
    public void setDeleteBranch(String newDeleteBranch) {
	this.deleteBranch.add(newDeleteBranch);
    }
    /**
     * �폜�x�X�R�[�h���擾����.
     * @return Vector
     */
    public Vector getDeleteBranch() {
	return this.deleteBranch;
    }
    /**
     * �폜�x�X�R�[�h���N���A����.
     */
    public void crearDeleteBranch() {
	this.deleteBranch.removeAllElements();
    }
    /**
     * �폜�c�Ə��R�[�h��ݒ肷��.
     * @param newDeleteOffice String
     */
    public void setDeleteOffice(String newDeleteOffice) {
	this.deleteOffice.add(newDeleteOffice);
    }
    /**
     * �폜�c�Ə��R�[�h���擾����.
     * @return Vector
     */
    public Vector getDeleteOffice() {
	return this.deleteOffice;
    }
    /**
     * �폜�c�Ə��R�[�h���N���A����.
     */
    public void crearDeleteOffice() {
	this.deleteOffice.removeAllElements();
    }
}
