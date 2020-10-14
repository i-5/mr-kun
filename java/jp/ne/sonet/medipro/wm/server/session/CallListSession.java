package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;

/**
 * コール内容一覧セッション情報管理
 * @auther
 * @version
 */
public class CallListSession {
    public static final int MESMODE_NONE          = 0;	// 非表示
    public static final int MESMODE_NO_CHECK      = 1;	// チェック無し
    public static final int MESMODE_CONFIRM       = 2;	// 削除確認
    public static final int MESMODE_CANNOT_DELETE = 3;	// 削除不可
    public static final int MESMODE_DELETE        = 4;	// 削除完了

    private int       currentRow  = 1;					// 先頭行番号
    private boolean   prev        = false;				// 前ページ有無フラグ
    private boolean   next        = false;				// 次ページ有無フラグ
    private String    sortKey     = "call_naiyo_cd";	// ソートキー
    private String    order       = "ASC";				// ソート順
    private int       messageMode = MESMODE_NONE;		// メッセージ出力モード
    private Hashtable checkTable  = new Hashtable();	// チェックボックスの状態

    /**
     * 先頭行番号を設定する。
     * @param newRow 先頭行番号
     */
    public void setCurrentRow(int newRow) {
        this.currentRow = newRow;
    }

    /**
     * 先頭行番号を取得する。
     * @return 先頭行番号
     */
    public int getCurrentRow() {
        return this.currentRow;
    }

    /**
     * 前ページの有無を設定する。
     * @param newPrev 前ページの有無
     */
    public void setPrev(boolean newPrev) {
        this.prev = newPrev;
    }

    /**
     * 前ページの有無を取得する。
     * @return 前ページの有無
     */
    public boolean isPrev() {
        return this.prev;
    }

    /**
     * 次ページの有無を設定する。
     * @param newNext 次ページの有無
     */
    public void setNext(boolean newNext) {
        this.next = newNext;
    }

    /**
     * 次ページの有無を取得する。
     * @return 次ページの有無
     */
    public boolean isNext() {
        return this.next;
    }

    /**
     * ソートキーを設定する。
     * @param newSortKey ソートキー
     */
    public void setSortKey(String newSortKey) {
        this.sortKey = newSortKey;
    }

    /**
     * ソートキーを取得する。
     * @return ソートキー
     */
    public String getSortKey() {
        return this.sortKey;
    }

    /**
     * ソート順を設定する。
     * @param newOrder ソート順
     */
    public void setOrder(String newOrder) {
        this.order = newOrder;
    }

    /**
     * ソート順を取得する。
     * @return ソート順
     */
    public String getOrder() {
        return this.order;
    }

    /**
     * ソート順を逆転する。
     */
    public void setOrderReverse() {
        if (this.order.equals("ASC")) {
            this.order = "DESC";
        }
        else {
            this.order = "ASC";
        }
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
     * チェック状態を設定する。
     * @param target チェックボックスの名前
     * @param status チェック状態
     */
    public void setChecked(String target, boolean status) {
        checkTable.remove(target);
        checkTable.put(target, new Boolean(status));
    }

    /**
     * チェック状態を取得する。
     * @param target チェックボックスの名前
     * @return チェック状態
     */
    public boolean isChecked(String target) {
        Boolean status = (Boolean)checkTable.get(target);
        return (status != null) ? status.booleanValue() : false;
    }

    /**
     * チェック状態をクリアする。
     */
    public void clearChecked() {
        checkTable.clear();
    }

    /**
     * メッセージとチェック状態をクリアする。
     */
    public void clear() {
        this.messageMode = MESMODE_NONE;
        checkTable.clear();
    }

    /**
     * セッション情報を初期化する。
     */
    public void initSession() {
        this.currentRow  = 1;
        this.prev        = false;
        this.next        = false;
        this.sortKey     = "call_naiyo_cd";
        this.order       = "ASC";
        this.messageMode = MESMODE_NONE;
        this.checkTable  = new Hashtable();
    }
}
