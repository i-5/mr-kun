package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>��ЃL���b�`�摜�ꗗ�Z�b�V�����N���X.</strong>
 * @auther
 * @version
 */
public class CatchListSession {
    /////////////////////////////////////////////
    //class variables
    //
    private int currentRow;			// �擪�s�ԍ�
    private int maxRow;				// �ő�s��
    private boolean prev;			// �O�y�[�W�L���t���O
    private boolean next;			// ���y�[�W�L���t���O
    private String order;			// �\�[�g��
    private int messageState;		// ���b�Z�[�WID
    private String radioValue;		// ���W�I�{�^�����
    private Vector checkValue; 		// �`�F�b�N�{�^�����
    private String defaultPicture;	// �f�t�H���g�摜�R�[�h

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
     */
    public CatchListSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("CatchListSession Created!!");
	}
	this.currentRow = 1;
	this.maxRow = 0;
	this.prev = false;
	this.next = false;
	this.order = "ASC";
	this.messageState = 0;
	this.radioValue = null;
	this.checkValue = new Vector();
	this.defaultPicture = null;
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
     * �\�[�g����ݒ肷��.
     * @param newOrder String
     */
    public void setOrder(String newOrder) {
	this.order = newOrder;
    }
    /**
     * �\�[�g�����擾����.
     * @return String
     */
    public String getOrder() {
	return this.order;
    }
    /**
     * �\�[�g�����t�]����.
     */
    public void setOrderReverse() {
	if ( this.order.equals("ASC") ) {
	    this.order = "DESC";
	}
	else {
	    this.order = "ASC";
	}
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
     * ���W�I�{�^����Ԃ�ݒ肷��.
     * @param newRadioValue String
     */
    public void setRadioValue(String newRadioValue) {
	this.radioValue = newRadioValue;
    }
    /**
     * ���W�I�{�^����Ԃ��擾����.
     * @return String
     */
    public String getRadioValue() {
	return this.radioValue;
    }
    /**
     * ���W�I�{�^����Ԃ��N���A����.
     */
    public void crearRadioValue() {
	this.radioValue = null;
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
     * �f�t�H���g�摜�R�[�h��ݒ肷��.
     * @param newDefaultCD String
     */
    public void setDefaultCD(String newDefaultCD) {
	this.defaultPicture = newDefaultCD;
    }
    /**
     * �f�t�H���g�摜�R�[�h���擾����.
     * @return String
     */
    public String getDefaultCD() {
	return this.defaultPicture;
    }
}
