package medipro.ap.entity;

import java.util.Date;

/**
 * ���b�Z�[�W�w�b�_�e�[�u����\������Entity�N���X
 */
public class MessageHeader {
    /** ���b�Z�[�W�w�b�_ID */
    private String messageHeaderId;
    /** ���b�Z�[�WID */
    private String messageId;
    /** ���b�Z�[�W�敪 */
    private String messageKbn;
    /** ���M��ID */
    private String fromUserid;
    /** ���M��ID */
    private String toUserid;
    /** CC�t���O */
    private String ccFlg;
    /** ���M���� */
    private String receiveTime;
    /** ���M�X�e�[�^�X */
    private String sendStatus;
    /** ��M�X�e�[�^�X */
    private String receiveStatus;

    private String _receiveTimed;
    private String _sendTorikeshiTime;
    private String _receiveDeleteTime;
    
    /**
     * 
     */
    public MessageHeader() {
    }

    /**
     * ���b�Z�[�W�w�b�_ID���擾
     */
    public String getMessageHeaderId() {
        return messageHeaderId;
    }

    /**
     * ���b�Z�[�W�w�b�_ID��ݒ�
     */
	public void setMessageHeaderId(String value) {
		this.messageHeaderId = value;
	}

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
        this.messageId = value;
    }

    /**
     * ���b�Z�[�W�敪���擾
     */
    public String getMessageKbn() {
        return messageKbn;
    }

    /**
     * ���b�Z�[�W�敪��ݒ�
     */
    public void setMessageKbn(String value) {
        this.messageKbn = value;
    }

    /**
     * ���M�����[�UID���擾
     */
    public String getFromUserid() {
        return fromUserid;
    }

    /**
     * ���M�����[�UID��ݒ�
     */
    public void setFromUserid(String value) {
        this.fromUserid = value;
    }

    /**
     * ���M�惆�[�UID���擾
     */
    public String getToUserid() {
        return toUserid;
    }

    /**
     * ���M�惆�[�UID��ݒ�
     */
    public void setToUserid(String value) {
        this.toUserid = value;
    }

    /**
     * CC�t���O���擾
     */
    public String getCcFlg() {
        return ccFlg;
    }

    /**
     * CC�t���O��ݒ�
     */
    public void setCcFlg(String value) {
        this.ccFlg = value;
    }

    /**
     * ���M���Ԃ��擾
     */
    public String getReceiveTime() {
        return receiveTime;
    }

    /**
     * ���M���Ԃ�ݒ�
     */
    public void setReceiveTime(String value) {
        this.receiveTime = value;
    }

    /**
     * ���M�X�e�[�^�X���擾
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * ���M�X�e�[�^�X��ݒ�
     */
    public void setSendStatus(String value) {
        this.sendStatus = value;
    }

    /**
     * ��M�X�e�[�^�X���擾
     */
    public String getReceiveStatus() {
        return receiveStatus;
    }

    /**
     * ��M�X�e�[�^�X��ݒ�
     */
    public void setReceiveStatus(String value) {
        this.receiveStatus = value;
    }

    /**
     * Get the value of receiveTimed.
     * @return value of receiveTimed.
     */
    public String getReceiveTimed() {
        return _receiveTimed;
    }
    
    /**
     * Set the value of receiveTimed.
     * @param receiveTimed Value to assign to receiveTimed.
     */
    public void setReceiveTimed(String receiveTimed) {
        _receiveTimed = receiveTimed;
    }
    
    /**
     * Get the value of sendTorikeshiTime.
     * @return value of sendTorikeshiTime.
     */
    public String getSendTorikeshiTime() {
        return _sendTorikeshiTime;
    }
    
    /**
     * Set the value of sendTorikeshiTime.
     * @param sendTorikeshiTime Value to assign to sendTorikeshiTime.
     */
    public void setSendTorikeshiTime(String sendTorikeshiTime) {
        _sendTorikeshiTime = sendTorikeshiTime;
    }
    
    /**
     * Get the value of receiveDeleteTime.
     * @return value of receiveDeleteTime.
     */
    public String getReceiveDeleteTime() {
        return _receiveDeleteTime;
    }
    
    /**
     * Set the value of receiveDeleteTime.
     * @param receiveDeleteTime Value to assign to receiveDeleteTime.
     */
    public void setReceiveDeleteTime(String receiveDeleteTime) {
        _receiveDeleteTime = receiveDeleteTime;
    }
    
}
