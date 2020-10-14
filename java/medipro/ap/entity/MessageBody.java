package medipro.ap.entity;

import java.util.Date;

import medipro.ap.util.Utility;

/**
 * ���b�Z�[�W�{�f�B�e�[�u����\������Entity�N���X
 */
public class MessageBody {
    /** ���b�Z�[�WID */
    private String messageId;
    /** �R�[�����e�R�[�h */
    private String callNaiyoCd = "";
    /** ���ȏЉ� */
    private String jikosyokai;
    /** �^�C�g�� */
    private String title;
    /** �{�� */
    private String messageHonbun = "";
    /** �L������ */
    private String yukoKigen;
    /** �L���b�`�摜 */
    private String pictureCd;
    /** �����NURL */
    private String url = "";
    /** ���[�J�[�R�[�h */
    private String companyCd;
    /** ���b�Z�[�W��� */
    private String messageType;

    /**
     * ���b�Z�[�WID���擾
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * ���b�Z�[�WID��ݒ�
     */
    public void setMessageId(String value) {
        messageId = value;
    }

    /**
     * �R�[�����e�R�[�h���擾
     */
    public String getCallNaiyoCd() {
        return callNaiyoCd;
    }

    /**
     * �R�[�����e�R�[�h��ݒ�
     */
    public void setCallNaiyoCd(String value) {
        callNaiyoCd = value;
    }
	
    /**
     * ���ȏЉ���擾
     */
    public String getJikosyokai() {
        return jikosyokai;
    }

    /**
     * ���ȏЉ��ݒ�
     */
    public void setJikosyokai(String value) {
        jikosyokai = value;
    }

    /**
     * ���b�Z�[�W�^�C�g�����擾
     */
    public String getTitle() {
        return title;
    }

    /**
     * ���b�Z�[�W�^�C�g����ݒ�
     */
    public void setTitle(String value) {
        if (Utility.isNull(value)) {
            title = "--";
        } else {
            title = value;
        }
    }

    /**
     * ���b�Z�[�W�{�����擾
     */
    public String getMessageHonbun() {
        return messageHonbun;
    }

    /**
     * ���b�Z�[�W�{����ݒ�
     */
    public void setMessageHonbun(String value) {
        messageHonbun = value;
    }

    /**
     * �L���������擾
     */
    public String getYukoKigen() {
        return yukoKigen;
    }

    /**
     * �L��������ݒ�
     */
    public void setYukoKigen(String value) {
        yukoKigen = value;
    }

    /**
     * �L���b�`�摜�R�[�h���擾
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * �L���b�`�摜�R�[�h��ݒ�
     */
    public void setPictureCd(String value) {
        pictureCd = value;
    }

    /**
     * �����N��URL���擾
     */
    public String getUrl() {
        return url;
    }

    /**
     * �����N��URL��ݒ�
     */
    public void setUrl(String value) {
        url = value;
    }

    /**
     * ��ЃR�[�h���擾
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * ��ЃR�[�h��ݒ�
     */
    public void setCompanyCd(String value) {
        companyCd = value;
    }

    /**
     * ���b�Z�[�W��ʂ��擾
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * ���b�Z�[�W��ʂ�ݒ�
     */
    public void setMessageType(String value) {
        messageType = value;
    }
}
