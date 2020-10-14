package medipro.ap.entity;

import java.util.Date;

import medipro.ap.util.Utility;

/**
 * メッセージボディテーブルを表現するEntityクラス
 */
public class MessageBody {
    /** メッセージID */
    private String messageId;
    /** コール内容コード */
    private String callNaiyoCd = "";
    /** 自己紹介 */
    private String jikosyokai;
    /** タイトル */
    private String title;
    /** 本文 */
    private String messageHonbun = "";
    /** 有効期限 */
    private String yukoKigen;
    /** キャッチ画像 */
    private String pictureCd;
    /** リンクURL */
    private String url = "";
    /** メーカーコード */
    private String companyCd;
    /** メッセージ種別 */
    private String messageType;

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
        messageId = value;
    }

    /**
     * コール内容コードを取得
     */
    public String getCallNaiyoCd() {
        return callNaiyoCd;
    }

    /**
     * コール内容コードを設定
     */
    public void setCallNaiyoCd(String value) {
        callNaiyoCd = value;
    }
	
    /**
     * 自己紹介を取得
     */
    public String getJikosyokai() {
        return jikosyokai;
    }

    /**
     * 自己紹介を設定
     */
    public void setJikosyokai(String value) {
        jikosyokai = value;
    }

    /**
     * メッセージタイトルを取得
     */
    public String getTitle() {
        return title;
    }

    /**
     * メッセージタイトルを設定
     */
    public void setTitle(String value) {
        if (Utility.isNull(value)) {
            title = "--";
        } else {
            title = value;
        }
    }

    /**
     * メッセージ本文を取得
     */
    public String getMessageHonbun() {
        return messageHonbun;
    }

    /**
     * メッセージ本文を設定
     */
    public void setMessageHonbun(String value) {
        messageHonbun = value;
    }

    /**
     * 有効期限を取得
     */
    public String getYukoKigen() {
        return yukoKigen;
    }

    /**
     * 有効期限を設定
     */
    public void setYukoKigen(String value) {
        yukoKigen = value;
    }

    /**
     * キャッチ画像コードを取得
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * キャッチ画像コードを設定
     */
    public void setPictureCd(String value) {
        pictureCd = value;
    }

    /**
     * リンク先URLを取得
     */
    public String getUrl() {
        return url;
    }

    /**
     * リンク先URLを設定
     */
    public void setUrl(String value) {
        url = value;
    }

    /**
     * 会社コードを取得
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * 会社コードを設定
     */
    public void setCompanyCd(String value) {
        companyCd = value;
    }

    /**
     * メッセージ種別を取得
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * メッセージ種別を設定
     */
    public void setMessageType(String value) {
        messageType = value;
    }
}
