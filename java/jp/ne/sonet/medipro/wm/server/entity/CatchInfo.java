package jp.ne.sonet.medipro.wm.server.entity;

import java.lang.String;

/**
 * <strong>��ЃL���b�`�摜���Entity�N���X.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CatchInfo {
	/////////////////////////////////////////////
	//class variables
	//
    protected String pictureCD;		// �摜�R�[�h
    protected String pictureName;	// �摜��
    protected String picture;		// �摜
    protected String pictureType;	// �摜�`��
    protected boolean defaultFlg;	// ��{��ʃt���O
    protected String mrId;			// MRID

	/////////////////////////////////////////////
	//constructors
	//
	/**
	 * �R���X�g���N�^.
	 */
    public CatchInfo() {
		pictureCD = null;
		pictureName = null;
		picture = null;
		pictureType = null;
		defaultFlg = false;
		mrId = null;
    }
    
	/////////////////////////////////////////////
	//class methods
	//
    /**
     * �摜�R�[�h�̎擾.
     * @return String
     */
    public String getPictureCD() {
        return pictureCD;
    }

    /**
     * �摜���̎擾.
     * @return String
     */
    public String getPictureName() {
        return pictureName;
    }
    /**
     * �摜�̎擾.
     * @return String
     */
    public String getPicture() {
        return picture;
    }
    /**
     * �摜�`���̎擾.
     * @return String
     */
    public String getPictureType() {
        return pictureType;
    }

    /**
     * �f�t�H���g�摜�t���O�̎擾.
     * @return boolean
     */
    public boolean getDefaultFlg() {
        return defaultFlg;
    }

    /**
     * �摜�R�[�h�̃Z�b�g.
     * @param newPictureCD String
     */
    public void setPictureCD(String newPictureCD) {
        pictureCD = newPictureCD;
    }
    /**
     * �摜���̃Z�b�g.
     * @param newPictureName String
     */
    public void setPictureName(String newPictureName) {
        pictureName = newPictureName;
    }
    /**
     * �摜�̃Z�b�g.
     * @param newPicture String
     */
    public void setPicture(String newPicture) {
        picture = newPicture;
    }
    /**
     * �摜�`���̃Z�b�g.
     * @param newPictureType String
     */
    public void setPictureType(String newPictureType) {
        pictureType = newPictureType;
    }

    /**
     * �f�t�H���g�摜�t���O�̃Z�b�g.
     * @param newDefaultFlg boolean
     */
    public void setDefaultFlg(boolean newDefaultFlg) {
        defaultFlg = newDefaultFlg;
    }

    /**
     * MRID���擾����B
     * @return MRID
     */
    public String getMrId() {
	return mrId;
    }

    /**
     * MRID��ݒ肷��B
     * @param id MRID
     */
    public void setMrId(String id) {
	mrId = id;
    }

    /**
     * �����񉻂���.
     * @return String
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append(pictureCD + "\n");
        me.append(pictureName + "\n");
                
        return me.toString();
    }
}
