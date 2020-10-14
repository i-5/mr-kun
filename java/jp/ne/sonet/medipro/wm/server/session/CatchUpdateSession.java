package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>��ЃL���b�`�摜�X�V�Z�b�V�����N���X.</strong>
 * @auther
 * @version
 */
public class CatchUpdateSession {
    /////////////////////////////////////////////
    //class variables
    //
    private String	pictureCD;		// �摜�R�[�h
    private int		messageState;	// ���b�Z�[�WID
    private String	pictureType;	// �摜�`��
    private String	pictureName;	// �摜��
    private String	picture;		// �摜(�p�X)
    private boolean updateFlg;		// �V�K�o�^or�ύX�t���O
    private String	type;			// gif or jpeg
    private String	path;			// �摜�t�@�C���p�X
    private boolean firstFlg;		// �ꗗ��ʂ���̑J�ڃt���O
    private boolean tempFlg;		// �ꎞ�t�@�C���\���t���O
    private String check;

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
     */
    public CatchUpdateSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("CatchUpdate Session Created!!");
	}
	this.pictureCD = null;
	this.messageState = 0;
	this.pictureType = null;
	this.pictureName = null;
	this.picture = null;
	this.updateFlg = false;
	this.path = null;
	this.firstFlg = true;
	this.tempFlg = false;
	this.check = "";
    }
	
    /////////////////////////////////////////////
    //class methods
    //
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
     * �摜�R�[�h��ݒ肷��.
     * @param newPictureCD String
     */
    public void setPictureCD(String newPictureCD) {
	this.pictureCD = newPictureCD;
    }
    /**
     * �摜�R�[�h���擾����.
     * @return String
     */
    public String getPictureCD() {
	return this.pictureCD;
    }
    /**
     * �摜�R�[�h���N���A����.
     */
    public void crearPictureCD() {
	this.pictureCD = null;
    }
    /**
     * �摜�`����ݒ肷��.
     * @param newPictureType String
     */
    public void setPictureType(String newPictureType) {
	this.pictureType = newPictureType;
    }
    /**
     * �摜�`�����擾����.
     * @return String
     */
    public String getPictureType() {
	return this.pictureType;
    }
    /**
     * �摜�`�����N���A����.
     */
    public void crearPictureType() {
	this.pictureType = null;
    }
    /**
     * �摜����ݒ肷��.
     * @param newPictureName String
     */
    public void setPictureName(String newPictureName) {
	this.pictureName = newPictureName;
    }
    /**
     * �摜�����擾����.
     * @return String
     */
    public String getPictureName() {
	return this.pictureName;
    }
    /**
     * �摜�����N���A����.
     */
    public void crearPictureName() {
	this.pictureName = null;
    }
    /**
     * �摜��ݒ肷��.
     * @param newPicture String
     */
    public void setPicture(String newPicture) {
	this.picture = newPicture;
    }
    /**
     * �摜���擾����.
     * @return String
     */
    public String getPicture() {
	return this.picture;
    }
    /**
     * �摜���N���A����.
     */
    public void crearPicture() {
	this.picture = null;
    }
    /**
     * �t�@�C���g���q��ݒ肷��.
     * @param newPictureName String
     */
    public void setType(String newType) {
	this.type = newType;
    }
    /**
     * �t�@�C���g���q���擾����.
     * @return String
     */
    public String getType() {
	return this.type;
    }
    /**
     * �摜�t�@�C���p�X��ݒ肷��.
     * @param newPath String
     */
    public void setPath(String newPath) {
	this.path = newPath;
    }
    /**
     * �摜�t�@�C���p�X���擾����.
     * @return String
     */
    public String getPath() {
	return this.path;
    }
    /**
     * �摜�t�@�C���p�X���N���A����.
     */
    public void setPath() {
	this.path = null;
    }
    /**
     * �V�K�o�^�E�ύX�t���O��ݒ肷��.
     * @param newUpdateFlg boolean
     */
    public void setUpdateFlg(boolean newUpdateFlg) {
	this.updateFlg = newUpdateFlg;
    }
    /**
     * �V�K�o�^�E�ύX�t���O���擾����.
     * @return boolean
     */
    public boolean isUpdate() {
	return this.updateFlg;
    }
    /**
     * �ꗗ��ʂ���̑J�ڃt���O��ݒ肷��.
     * @param newFlg boolean
     */
    public void setFirstFlg(boolean newFlg) {
	this.firstFlg = newFlg;
    }
    /**
     * �ꗗ��ʂ���̑J�ڃt���O���擾����.
     * @return boolean
     */
    public boolean isFirst() {
	return this.firstFlg;
    }
    /**
     * �ꎞ�t�@�C���\���t���O��ݒ肷��.
     * @param newFlg boolean
     */
    public void setTempFlg(boolean newFlg) {
	this.tempFlg = newFlg;
    }
    /**
     * �ꎞ�t�@�C���\���t���O���擾����.
     * @return boolean
     */
    public boolean isTemp() {
	return this.tempFlg;
    }

    public void check() {
	check = "checked";
    }

    public void unCheck() {
	check = "";
    }

    public String getCheck() {
	return check;
    }
}
