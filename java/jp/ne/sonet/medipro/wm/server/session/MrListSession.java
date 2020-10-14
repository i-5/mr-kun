package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>MR管理一覧用セッションクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrListSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** 通常 */
    public static final int NORMAL = 0;
    /** 削除対象非存在 */
    public static final int NO_SELECTION = 1;
    /** 削除確認 */
    public static final int REMOVE_CONFIRM = 2;
    /** 担当顧客画面移動前確認 */
    public static final int REPLACE_CONFIRM = 3;
    /** 削除完了 */
    public static final int REMOVE_DONE = 4;
    /** 担当顧客存在 */
    public static final int HAS_DR_IN_CHARGE = 5;
    /** 昇順 */
    private static final String ASCENDING = "ASC";
    /** 降順 */
    private static final String DESCENDING = "DESC";
    /** デフォルトソート項目 */
    private static final String DEFAULT_SORT_ITEM = "mr.mr_id";

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
    /** チェックボックスの選択状態 */
    private Hashtable checkedMap = null;
    /** 現在の表示ページ */
    private int page = 0;
    /** 現在のstatus */
    private int status = NORMAL;

    private String refMrId = null;
    private String refMrName = null;

    static {
	//画面ソートkey → テーブルカラム名
	toTableColumnMap = new Hashtable();
	toTableColumnMap.put("id", "mr.mr_id");
	toTableColumnMap.put("name", "mr.name_kana");
	toTableColumnMap.put("shiten", "mr.shiten_cd");
	toTableColumnMap.put("eigyosyo", "mr.eigyosyo_cd");
	toTableColumnMap.put("attribute1", "mr.mr_attribute_cd1");
	toTableColumnMap.put("attribute2", "mr.mr_attribute_cd2");
	toTableColumnMap.put("master", "mr.master_flg");
	toTableColumnMap.put("nyusya", "mr.nyusya_year");
    }

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * テーブルカラム名に対する初期ソート方向のセットを行う.
     */
    public MrListSession() {
	toDirectionMap = new Hashtable();
	toDirectionMap.put("mr.mr_id", ASCENDING);
	toDirectionMap.put("mr.name_kana", ASCENDING);
	toDirectionMap.put("mr.shiten_cd", ASCENDING);
	toDirectionMap.put("mr.eigyosyo_cd", ASCENDING);
	toDirectionMap.put("mr.mr_attribute_cd1", ASCENDING);
	toDirectionMap.put("mr.mr_attribute_cd2", ASCENDING);
	toDirectionMap.put("mr.master_flg", ASCENDING);
	toDirectionMap.put("mr.nyusya_year", ASCENDING);

	checkedMap = new Hashtable();
	
	if (SysCnst.DEBUG) {
	    System.err.println("MrListSession is created");
	}
    }

    //////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * 現在のソート項目名を取得する.
     * @return 現在のソート対象テーブルカラム名
     */
    public String getCurrentSortItem() {
	return currentSortItem;
    }

    /**
     * 現在のソート項目のソート方向を取得する.
     * @return 現在のソート方向(ASC or DESC)
     */
    public String getCurrentSortDirection() {
	return (String)toDirectionMap.get(currentSortItem);
    }

    /**
     * ページ以外の情報をクリアする。
     */
    public void clear() {
	setCheckedList(null);
	setStatus(NORMAL);
    }

    /**
     * 現在の表示ページを取得する.
     * @return ページナンバー
     */
    public int getPage() {
	return page;
    }

    /**
     * ページを進める.
     */
    public void nextPage() {
	page++;
    }

    /**
     * ページを戻す.
     */
    public void previousPage() {
	page--;
	
	page = page < 0 ? 0 : page;
    }

    /**
     * 現在のソート項目の設定、かつソート方向の反転を行う.
     * @param columnName 設定するソート項目名
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
     * 指定したカラムのソート方向を反転する.
     * @param sortItem ソート対象テーブルカラム名
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
     * 指定したMRのチェックボックスの状態を調べる.
     * @param  mrId 調べるMRのID
     * @return チェックされていたらtrue
     */
    public boolean isChecked(String mrId) {
	if (checkedMap.get(mrId) != null) {
	    return true;
	}

	return false;
    }

    /**
     * MRIDの配列を受け取り、チェック状態にする.
     * @param checkedMrIds チェックするMRIDのリスト
     */
    public void setCheckedList(String[] checkedMrIds) {
	checkedMap.clear();

	if (checkedMrIds != null) {
	    for (int i = 0; i < checkedMrIds.length; i++) {
		checkedMap.put(checkedMrIds[i], "checked");
	    }
	}
    }

    /**
     * 現在のstatusを取得する.
     * @return status
     */
    public int getStatus() {
	return status;
    }

    /**
     * 現在のstatusを設定する.
     * @param status 設定status
     */
    public void setStatus(int status) {
	this.status = status;
    }

    public String getRefMrId() {
	return refMrId;
    }

    public void setRefMrId(String value) {
	refMrId = value;
    }

    public String getRefMrName() {
	return refMrName;
    }

    public void setRefMrName(String value) {
	refMrName = value;
    }
    
}
