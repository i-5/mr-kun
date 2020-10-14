package medipro.ap.entity;

import java.util.Date;

/**
 * メッセージヘッダテーブルを表現するEntityクラス
 */
public class MessageHeader {
    /** メッセージヘッダID */
    private String messageHeaderId;
    /** メッセージID */
    private String messageId;
    /** メッセージ区分 */
    private String messageKbn;
    /** 送信元ID */
    private String fromUserid;
    /** 送信先ID */
    private String toUserid;
    /** CCフラグ */
    private String ccFlg;
    /** 送信日時 */
    private String receiveTime;
    /** 送信ステータス */
    private String sendStatus;
    /** 受信ステータス */
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
     * メッセージヘッダIDを取得
     */
    public String getMessageHeaderId() {
        return messageHeaderId;
    }

    /**
     * メッセージヘッダIDを設定
     */
	public void setMessageHeaderId(String value) {
		this.messageHeaderId = value;
	}

    /**
     * メッセージIDを取得
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * メッセージIDを設定
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * メッセージ区分を取得
     */
    public String getMessageKbn() {
        return messageKbn;
    }

    /**
     * メッセージ区分を設定
     */
    public void setMessageKbn(String value) {
        this.messageKbn = value;
    }

    /**
     * 送信元ユーザIDを取得
     */
    public String getFromUserid() {
        return fromUserid;
    }

    /**
     * 送信元ユーザIDを設定
     */
    public void setFromUserid(String value) {
        this.fromUserid = value;
    }

    /**
     * 送信先ユーザIDを取得
     */
    public String getToUserid() {
        return toUserid;
    }

    /**
     * 送信先ユーザIDを設定
     */
    public void setToUserid(String value) {
        this.toUserid = value;
    }

    /**
     * CCフラグを取得
     */
    public String getCcFlg() {
        return ccFlg;
    }

    /**
     * CCフラグを設定
     */
    public void setCcFlg(String value) {
        this.ccFlg = value;
    }

    /**
     * 送信時間を取得
     */
    public String getReceiveTime() {
        return receiveTime;
    }

    /**
     * 送信時間を設定
     */
    public void setReceiveTime(String value) {
        this.receiveTime = value;
    }

    /**
     * 送信ステータスを取得
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * 送信ステータスを設定
     */
    public void setSendStatus(String value) {
        this.sendStatus = value;
    }

    /**
     * 受信ステータスを取得
     */
    public String getReceiveStatus() {
        return receiveStatus;
    }

    /**
     * 受信ステータスを設定
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
