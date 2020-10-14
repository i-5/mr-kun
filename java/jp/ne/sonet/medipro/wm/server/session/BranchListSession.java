package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>支店・営業所一覧セッションクラス.</strong>
 * @auther
 * @version
 */
public class BranchListSession {
    /////////////////////////////////////////////
    //class variables
    //
    private int currentRow;			// 先頭行番号
    private int maxRow;				// 最大行数
    private boolean prev;			// 前ページ有無フラグ
    private boolean next;			// 次ページ有無フラグ
    private String sortKey;			// ソートキー
    private int messageState;		// メッセージID
    private Vector checkValue; 		// チェックボタン状態
    private static Hashtable orderMap;	// 昇降マップ
    private Vector deleteBranch;	// 削除支店コード
    private Vector deleteOffice;	// 削除営業所コード

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * コンストラクタ.
     */
    public BranchListSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("BranchListSession Created!!");
	}
	this.currentRow = 1;
	this.maxRow = 0;
	this.prev = false;
	this.next = false;
	this.sortKey = "shiten.shiten_cd";
	this.messageState = 0;
	this.checkValue = new Vector();
	this.orderMap = new Hashtable();
	orderMap.put("shiten.shiten_cd", " ASC");
	orderMap.put("eigyosyo_cd", " DESC");
	deleteBranch = new Vector();
	deleteOffice = new Vector();
    }
	
    /////////////////////////////////////////////
    //class methods
    //
    /**
     * 先頭行番号を設定する.
     * @param newRow int
     */
    public void setCurrentRow(int newRow) {
	this.currentRow = newRow;
    }
    /**
     * 先頭行番号を取得する.
     * @return int
     */
    public int getCurrentRow() {
	return this.currentRow;
    }
    /**
     * 最大行数を設定する.
     * @param newMaxRow int
     */
    public void setMaxRow(int newMaxRow) {
	this.maxRow = newMaxRow;
    }
    /**
     * 最大行数を取得する.
     * @return int
     */
    public int getMaxRow() {
	return this.maxRow;
    }
    /**
     * 前ページの有無を設定する.
     * @param prev boolean
     */
    public void setPrev(boolean prev) {
	this.prev = prev;
    }
    /**
     * 前ページの有無を取得する.
     * @return boolean
     */
    public boolean isPrev() {
	return this.prev;
    }
    /**
     * 次ページの有無を設定する.
     * @param prev boolean
     */
    public void setNext(boolean next) {
	this.next = next;
    }
    /**
     * 次ページの有無を取得する.
     * @return boolean
     */
    public boolean isNext() {
	return this.next;
    }
    /**
     * ソートキーを設定する.
     * @param newOrder String
     */
    public void setSortKey(String newSortKey) {
	this.sortKey = newSortKey;
    }
    /**
     * ソートキーを取得する.
     * @return String
     */
    public String getSortKey() {
	return this.sortKey;
    }
    /**
     * メッセージIDを設定する.
     * @param newMessageState int
     */
    public void setMessageState(int newMessageState) {
	this.messageState = newMessageState;
    }
    /**
     * メッセージIDを取得する.
     * @return int
     */
    public int getMessageState() {
	return this.messageState;
    }
    /**
     * チェックボタン状態を設定する.
     * @param newCheckValue String
     */
    public void setCheckValue(String newCheckValue) {
	this.checkValue.add(newCheckValue);
    }
    /**
     * チェックボタン状態を取得する.
     * @return Vector
     */
    public Vector getCheckValue() {
	return this.checkValue;
    }
    /**
     * チェックボタン状態をクリアする.
     */
    public void crearCheckValue() {
	this.checkValue.removeAllElements();
    }
    /**
     * チェックボタンの選択数を取得する.
     * @return int
     */
    public int getCheckSize() {
	return this.checkValue.size();
    }
    /**
     * オーダーを設定する.
     * @param order String
     */
    public void setOrder(String order) {
	orderMap.remove(sortKey);
	orderMap.put(sortKey, order);
    }
    /**
     * オーダーを取得する.
     * @return String
     */
    public String getOrder() {
	return (String)orderMap.get(sortKey);
    }
    /**
     * オーダーを逆転する.
     */
    public void reverseOrder() {
	String order = (String)orderMap.get(sortKey);
	orderMap.remove(sortKey);
	if ( order.equals(" ASC") ) {
	    order = " DESC";
	} else {
	    order = " ASC";
	}
	orderMap.put(sortKey, order);
    }
    /**
     * 削除支店コードを設定する.
     * @param newDeleteBranch String
     */
    public void setDeleteBranch(String newDeleteBranch) {
	this.deleteBranch.add(newDeleteBranch);
    }
    /**
     * 削除支店コードを取得する.
     * @return Vector
     */
    public Vector getDeleteBranch() {
	return this.deleteBranch;
    }
    /**
     * 削除支店コードをクリアする.
     */
    public void crearDeleteBranch() {
	this.deleteBranch.removeAllElements();
    }
    /**
     * 削除営業所コードを設定する.
     * @param newDeleteOffice String
     */
    public void setDeleteOffice(String newDeleteOffice) {
	this.deleteOffice.add(newDeleteOffice);
    }
    /**
     * 削除営業所コードを取得する.
     * @return Vector
     */
    public Vector getDeleteOffice() {
	return this.deleteOffice;
    }
    /**
     * 削除営業所コードをクリアする.
     */
    public void crearDeleteOffice() {
	this.deleteOffice.removeAllElements();
    }
}
