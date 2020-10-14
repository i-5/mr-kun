package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MRキャッチ画像一覧画面用セッションクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchListSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** 通常 */
    public static final int NORMAL = 0;
    /** 削除対象非存在 */
    public static final int NO_SELECTION = 1;
    /** 削除確認 */
    public static final int REMOVE_CONFIRM = 2;
    /** 削除完了 */
    public static final int REMOVE_DONE = 3;
    /** 削除不可 */
    public static final int UNABLE_TO_REMOVE = 4;
    /** 保存確認 */
    public static final int SAVE_CONFIRM = 5;
    /** 保存完了*/
    public static final int SAVE_DONE = 6;
    /** 昇順 */
    private static final String ASCENDING = "ASC";
    /** 降順 */
    private static final String DESCENDING = "DESC";
    /** デフォルトソート項目 */
    private static final String DEFAULT_SORT_ITEM = "catch_picture.picture_name";
    //////////////////////////////////////////////////////////////////////
    // class valiables
    //
    /** ソート項目とテーブルカラム名のマップ */
    private static Hashtable toTableColumnMap = null;
    //////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** 現在のソート項目(テーブルカラム名) */
    private String currentSortItem = DEFAULT_SORT_ITEM;
    /** 各カラムのソート方向 */
    private Hashtable toDirectionMap = null;
    /** 現在の表示ページ */
    private int page = 0;
    /** チェックボックスの選択状態 */
    private Hashtable checkedMap = null;
    /** DBのデフォルトPictureCd */
    private String dbDefaultPictureCd = new String();
    /** MR情報 */
    private MrInfo info = null;
    /** 現在のstatus */
    private int status = NORMAL;

    static {
	toTableColumnMap = new Hashtable();
	toTableColumnMap.put("title", "catch_picture.picture_name");
    }

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MRキャッチ画像一覧のセッションを生成する.
     */
    public MrCatchListSession() {
	toDirectionMap = new Hashtable();
	toDirectionMap.put("catch_picture.picture_name", ASCENDING);

	checkedMap = new Hashtable();
	info = new MrInfo();

	if (SysCnst.DEBUG) {
	    System.err.println("MrCatchListSession is created");
	}
    }

    //////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * ページ以外の情報をクリアする。
     */
    public void clear() {
	setCheckedList(null);
	setStatus(NORMAL);
    }

    /**
     * 現在保持するMR情報を取得する.
     * @return MR情報
     */
    public MrInfo getMrInfo() {
	return info;
    }

    /**
     * MR情報を設定する.
     * @param info 設定するMR情報
     */
    public void setMrInfo(MrInfo info) {
	this.info = info;
	dbDefaultPictureCd = info.getPictureCd();
    }

    /**
     * デフォルト画像コードを取得する.
     * @return 現在設定されているデフォルト画像コード
     */
    public String getDBDefaultPictureCd() {
	return dbDefaultPictureCd;
    }
    
    /**
     * デフォルト画像コードを設定する.
     * @param pictureCd 設定するデフォルト画像コード
     */
    public void setDBDefaultPictureCd(String pictureCd) {
	dbDefaultPictureCd = pictureCd;
    }

    /**
     * 現在のソート項目名を取得する.
     * @return ソート対象カラム名
     */
    public String getCurrentSortItem() {
	return currentSortItem;
    }

    /**
     * 現在のソート項目のソート方向を取得する.
     * @return ソート対象のソート方向(ASCENDING or DESCENDING)
     */
    public String getCurrentSortDirection() {
	return (String)toDirectionMap.get(currentSortItem);
    }

    /**
     * 現在のページ番号を取得する.
     * @return 現在表示しているページ(0～)
     */
    public int getPage() {
	return page;
    }

    /**
     * 一ページ進める.
     */
    public void nextPage() {
	page++;
    }

    /**
     * 一ページ戻す.
     */
    public void previousPage() {
	page--;

	page = page < 0 ? 0 : page;
    }

    /**
     * 指定したカラム名をソート対象に設定し、ソート方向を前回の方向から反転させる.
     * @param columnName ソート対象とするカラム名
     */
    public void setSortTarget(String columnName) {
	//対応するテーブルカラム名を取得
	currentSortItem = (String)toTableColumnMap.get(columnName);
	
	//不正だったらデフォルトに
	if (currentSortItem == null) {
	    currentSortItem = DEFAULT_SORT_ITEM;
	}

	//ソート方向を反転
	reverseDirection(currentSortItem);

	//ページをリセット
	page = 0;
    }

    /**
     * 指定したカラムのソート方向を反転させる.
     * @param sortItem 実テーブルカラム名
     */
    private void reverseDirection(String sortItem) {
	String direction = (String)toDirectionMap.get(sortItem);
	
	if (direction.equals(DESCENDING)){
	    toDirectionMap.put(sortItem, ASCENDING);
	} else {
	    toDirectionMap.put(sortItem, DESCENDING);
	}
    }

    /**
     * 現在のチェックボックスの選択状態を取得する.
     * @param key 項目を一意に識別できるkey
     */
    public boolean isChecked(String key) {
	if (checkedMap.get(key) != null) {
	    return true;
	}

	return false;
    }

    /**
     * チェックボックスの選択状態を設定する.
     * @param checkedItems チェックする項目のkey
     */
    public void setCheckedList(String[] checkedItems) {
	checkedMap.clear();

	if (checkedItems != null) {
	    for (int i = 0; i < checkedItems.length; i++) {
		checkedMap.put(checkedItems[i], "checked");
	    }
	}
    }

    /**
     * 現在設定されているステータスを取得する.
     * @return ステータス
     */
    public int getStatus() {
	return status;
    }

    /**
     * ステータスを設定する.
     * @param status 設定するステータス
     */
    public void setStatus(int status) {
	this.status = status;
    }


}
