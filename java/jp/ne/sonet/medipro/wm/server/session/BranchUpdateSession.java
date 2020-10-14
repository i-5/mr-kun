package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>�x�X�E�c�Ə��ǉ��E�X�V�Z�b�V�����N���X.</strong>
 * @auther
 * @version
 */
public class BranchUpdateSession {
    /////////////////////////////////////////////
    //class variables
    //
    private String	branchCD;		// �x�X�R�[�h
    private String	officeCD;		// �c�Ə��R�[�h
    private boolean	newBranch;		// �x�X�V�K�o�^�t���O
    private boolean newOffice;		// �c�Ə��V�K�o�^�t���O
    private String	branchName;		// �x�X��(���ݓ��͂���Ă���)
    private String	officeName;		// �c�Ə���(���ݓ��͂���Ă���)
    private int		messageState;	// ���b�Z�[�WID
    private String	originalBranch;	// �x�X��(�ύX�O)
    private String	originalOffice;	// �c�Ə���(�ύX�O)

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
	 */
    public BranchUpdateSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("BranchUpdateSession Created!!");
	}
	this.branchCD = null;
	this.officeCD = null;
	this.newBranch = true;
	this.newOffice = true;
	this.branchName = null;
	this.officeName = null;
	this.messageState = 0;
	this.originalBranch = null;
	this.originalOffice = null;
    }
	
    /////////////////////////////////////////////
    //class methods
    //
    /**
     * �x�X�R�[�h��ݒ肷��.
     * @param newCD String
     */
    public void setBranchCD(String newCD) {
	this.branchCD = newCD;
    }
    /**
     * �x�X�R�[�h���擾����.
     * @return String
     */
    public String getBranchCD() {
	return this.branchCD;
    }
    /**
     * �c�Ə��R�[�h��ݒ肷��.
     * @param newCD String
     */
    public void setOfficeCD(String newCD) {
	this.officeCD = newCD;
    }
    /**
     * �c�Ə��R�[�h���擾����.
     * @return String
     */
    public String getOfficeCD() {
	return this.officeCD;
    }
    /**
     * �x�X�V�K�o�^�t���O��ݒ肷��.
     * @param flg boolean
     */
    public void setNewBranch(boolean flg) {
	this.newBranch = flg;
    }
    /**
     * �x�X�V�K�o�^�t���O���擾����.
     * @return boolean
     */
    public boolean isNewBranch() {
	return this.newBranch;
    }
    /**
     * �c�Ə��V�K�o�^�t���O��ݒ肷��.
     * @param flg boolean
     */
    public void setNewOffice(boolean flg) {
	this.newOffice = flg;
    }
    /**
     * �c�Ə��V�K�o�^�t���O���擾����.
     * @return boolean
     */
    public boolean isNewOffice() {
	return this.newOffice;
    }
    /**
     * �x�X����ݒ肷��.
     * @param newName String
     */
    public void setBranchName(String newName) {
	this.branchName = newName;
    }
    /**
     * �x�X�����擾����.
     * @return String
     */
    public String getBranchName() {
	return this.branchName;
    }
    /**
     * �c�Ə�����ݒ肷��.
     * @param newName String
     */
    public void setOfficeName(String newName) {
	this.officeName = newName;
    }
    /**
     * �c�Ə������擾����.
     * @return String
     */
    public String getOfficeName() {
	return this.officeName;
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
     * �ύX�O��(DB���)�x�X����ݒ肷��.
     * @param newName String
     */
    public void setOriginalBranch(String newName) {
	this.originalBranch = newName;
    }
    /**
     * �ύX�O��(DB���)�x�X�����擾����.
     * @return String
     */
    public String getOriginalBranch() {
	return this.originalBranch;
    }
    /**
     * �ύX�O��(DB���)�c�Ə�����ݒ肷��.
     * @param newName String
     */
    public void setOriginalOffice(String newName) {
	this.originalOffice = newName;
    }
    /**
     * �ύX�O��(DB���)�c�Ə������擾����.
     * @return String
     */
    public String getOriginalOffice() {
	return this.originalOffice;
    }
}
