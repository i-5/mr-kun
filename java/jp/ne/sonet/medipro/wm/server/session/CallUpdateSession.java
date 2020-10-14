package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;

/**
 * コール内容追加・変更セッション情報管理
 * @auther
 * @version
 */
public class CallUpdateSession {
    public static final int MESMODE_NONE          = 0;	// 非表示
    public static final int MESMODE_CONFIRM       = 1;	// 更新確認
    public static final int MESMODE_CANNOT_UPDATE = 2;	// 更新不可
    public static final int MESMODE_UPDATE        = 3;	// 更新完了

    public static final int UPDMODE_NEW           = 0;	// コード変更可(新規追加)
    public static final int UPDMODE_UPDATE        = 1;	// コード変更不可(更新)

    private String callNaiyoCd = null;			// コール内容コード
    private String callNaiyo   = null;			// コール内容
    private int    messageMode = MESMODE_NONE;	// メッセージ出力モード
    private int    updateMode  = UPDMODE_NEW;	// 更新モード

    /**
     * コール内容コードを設定する。
     * @param callNaiyoCd コール内容コード
     */
    public void setCallNaiyoCd(String callNaiyoCd) {
        this.callNaiyoCd = callNaiyoCd;
    }

    /**
     * コール内容コードを取得する。
     * @return コール内容コード
     */
    public String getCallNaiyoCd() {
        return this.callNaiyoCd;
    }

    /**
     * コール内容を設定する。
     * @param callNaiyo コール内容
     */
    public void setCallNaiyo(String callNaiyo) {
        this.callNaiyo = callNaiyo;
    }

    /**
     * コール内容を取得する。
     * @return コール内容
     */
    public String getCallNaiyo() {
        return this.callNaiyo;
    }

    /**
     * メッセージモードを設定する。
     * @param mode メッセージモード
     */
    public void setMessageMode(int mode) {
        this.messageMode = mode;
    }

    /**
     * メッセージモードを取得する。
     * @return メッセージモード
     */
    public int getMessageMode() {
        return this.messageMode;
    }

    /**
     * 更新モードを設定する。
     * @param mode 更新モード
     */
    public void setUpdateMode(int mode) {
        this.updateMode = mode;
    }

    /**
     * 更新モードを取得する。
     * @return 更新モード
     */
    public int getUpdateMode() {
        return this.updateMode;
    }

    /**
     * セッション情報を初期化する。
     */
    public void initSession() {
        this.callNaiyoCd = null;
        this.callNaiyo   = null;
        this.messageMode = MESMODE_NONE;
        this.updateMode  = UPDMODE_NEW;
    }
}
