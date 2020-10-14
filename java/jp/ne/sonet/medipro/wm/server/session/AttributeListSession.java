package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

/**
 * 属性一覧セッション情報管理
 * @auther
 * @version
 */
public class AttributeListSession {
    /** 状態 */
    public static final int STATE_NORMAL    = 0;    // 通常
    public static final int STATE_ALERT_1   = 1;    // アラート出力
    public static final int STATE_ALERT_2   = 2;    // アラート出力
    public static final int STATE_CONFIRM_1 = 3;    // コンファーム出力
    public static final int STATE_CONFIRM_2 = 4;    // コンファーム出力
    public static final int STATE_REPORT_1  = 5;    // 結果出力
    public static final int STATE_REPORT_2  = 6;    // 結果出力

    private int nStatus = STATE_NORMAL;             // 状態
    private int nNowPage = 1;                       // 現ページ
    private boolean bHasPrevPage = false;           // 前ページ有無フラグ
    private boolean bHasNextPage = false;           // 次ページ有無フラグ
    private String strSortKey = "link_lib.link_bunrui_cd";  // ソートキー
    private static Hashtable mapOrder = new Hashtable();// 昇降マップ
    static {
        mapOrder.put("link_lib.link_bunrui_cd", "ASC");
        mapOrder.put("link_lib.honbun_text", "DESC");
    }
    private Hashtable mapCheckbox = new Hashtable();// チェックボックスマップ

    /**
     * 表示ページを設定する。
     * @param arg 表示ページ
     */
    public void setPage(int arg) {
        nNowPage = arg;
    }

    /**
     * 表示ページを取得する。
     * @return 表示ページ
     */
    public int getPage() {
        return nNowPage;
    }

    /**
     * 前ページの有無を設定する。
     * @param arg 前ページの有無
     */
    public void setPrevPage(boolean arg) {
        bHasPrevPage = arg;
    }

    /**
     * 前ページの有無を取得する。
     * @return 前ページの有無
     */
    public boolean hasPrevPage() {
        return bHasPrevPage;
    }

    /**
     * 次ページの有無を設定する。
     * @param arg 次ページの有無
     */
    public void setNextPage(boolean arg) {
        bHasNextPage = arg;
    }

    /**
     * 次ページの有無を取得する。
     * @return 次ページの有無
     */
    public boolean hasNextPage() {
        return bHasNextPage;
    }

    /**
     * ソートキーを設定する。
     * @param arg ソートキー
     */
    public void setSortKey(String arg) {
        strSortKey = arg;
    }

    /**
     * ソートキーを取得する。
     * @return ソートキー
     */
    public String getSortKey() {
        return strSortKey;
    }

    /**
     * オーダーを設定する。
     * @param arg オーダー
     */
    public void setOrder(String arg) {
        mapOrder.remove(strSortKey);
        mapOrder.put(strSortKey, arg);
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
     * @param arg 状態
     */
    public void setStatus(int arg) {
        nStatus = arg;
    }

    /**
     * 状態を取得する。
     * @return 状態
     */
    public int getStatus() {
        return nStatus;
    }

    /**
     * チェック状態を設定する。
     * @param arg1 チェックボックス名
     * @param arg2 チェック状態
     */
    public void setCheck(String arg1, String arg2) {
        mapCheckbox.remove(arg1);
        mapCheckbox.put(arg1, arg2);
    }

    /**
     * チェック状態を取得する。
     * @param arg チェックボックス名
     * @return チェック状態
     */
    public String getCheck(String arg) {
        return (String)mapCheckbox.get(arg);
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
