package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

/**
 * サブマスター一覧セッション情報管理
 * @auther
 * @version
 */
public class SubListSession {
    /** 状態 */
    public static final int STATE_NORMAL    = 0;    // 通常
    public static final int STATE_ALERT     = 1;    // アラート出力
    public static final int STATE_CONFIRM   = 2;    // コンファーム出力
    public static final int STATE_REPORT    = 3;    // 結果出力

    private int nStatus = STATE_NORMAL; // 状態
    private int nNowPage = 1;               // 現ページ
    private boolean bHasPrevPage = false;   // 前ページ有無フラグ
    private boolean bHasNextPage = false;   // 次ページ有無フラグ
    private String strSortKey = "mr.mr_id"; // ソートキー
    private static Hashtable mapOrder = new Hashtable();    // 昇降マップ
    static {
        mapOrder.put("mr.shiten_cd", "DESC");
        mapOrder.put("mr.eigyosyo_cd", "DESC");
        mapOrder.put("attr_cd", "DESC");
        mapOrder.put("mr.mr_id", "DESC");
        mapOrder.put("mr.name", "ASC");
    }
    private Hashtable mapCheckbox = new Hashtable();    // チェックボックスマップ

    /**
     * 表示ページを設定する。
     * @param page 表示ページ
     */
    public void setPage(int page) {
        this.nNowPage = page;
    }

    /**
     * 表示ページを取得する。
     * @return 表示ページ
     */
    public int getPage() {
        return this.nNowPage;
    }

    /**
     * 前ページの有無を設定する。
     * @param prev 前ページの有無
     */
    public void setPrevPage(boolean prev) {
        this.bHasPrevPage = prev;
    }

    /**
     * 前ページの有無を取得する。
     * @return 前ページの有無
     */
    public boolean hasPrevPage() {
        return this.bHasPrevPage;
    }

    /**
     * 次ページの有無を設定する。
     * @param prev 次ページの有無
     */
    public void setNextPage(boolean next) {
        this.bHasNextPage = next;
    }

    /**
     * 次ページの有無を取得する。
     * @return 次ページの有無
     */
    public boolean hasNextPage() {
        return this.bHasNextPage;
    }

    /**
     * ソートキーを設定する。
     * @param key ソートキー
     */
    public void setSortKey(String key) {
        this.strSortKey = key;
    }

    /**
     * ソートキーを取得する。
     * @return ソートキー
     */
    public String getSortKey() {
        return this.strSortKey;
    }

    /**
     * オーダーを設定する。
     * @param order オーダー
     */
    public void setOrder(String order) {
        mapOrder.remove(strSortKey);
        mapOrder.put(strSortKey, order);
    }

    /**
     * オーダーを取得する。
     * @return オーダー
     */
    public String getOrder() {
        return (String)mapOrder.get(strSortKey);
    }

    /**
     * 状態を設定する。
     * @param status 状態
     */
    public void setStatus(int status) {
        this.nStatus = status;
    }

    /**
     * 状態を取得する。
     * @return 状態
     */
    public int getStatus() {
        return this.nStatus;
    }

    /**
     * チェック状態を設定する。
     * @param target チェックボックス名
     * @param status チェック状態
     */
    public void setCheck(String target, String status) {
        mapCheckbox.remove(target);
        mapCheckbox.put(target, status);
    }

    /**
     * チェック状態を取得する。
     * @param target チェックボックス名
     * @return チェック状態
     */
    public String getCheck(String target) {
        return (String)mapCheckbox.get(target);
    }

    /**
     * チェック状態をクリアする。
     */
    public void clearCheck() {
        mapCheckbox.clear();
    }

    /**
     * メッセージ、チェック状態をクリアする。
     */
    public void clear() {
        setStatus(STATE_NORMAL);
        clearCheck();
    }
}
