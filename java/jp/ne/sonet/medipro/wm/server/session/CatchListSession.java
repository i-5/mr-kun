package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>会社キャッチ画像一覧セッションクラス.</strong>
 * @auther
 * @version
 */
public class CatchListSession {
    /////////////////////////////////////////////
    //class variables
    //
    private int currentRow;			// 先頭行番号
    private int maxRow;				// 最大行数
    private boolean prev;			// 前ページ有無フラグ
    private boolean next;			// 次ページ有無フラグ
    private String order;			// ソート順
    private int messageState;		// メッセージID
    private String radioValue;		// ラジオボタン状態
    private Vector checkValue; 		// チェックボタン状態
    private String defaultPicture;	// デフォルト画像コード

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * コンストラクタ.
     */
    public CatchListSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("CatchListSession Created!!");
	}
	this.currentRow = 1;
	this.maxRow = 0;
	this.prev = false;
	this.next = false;
	this.order = "ASC";
	this.messageState = 0;
	this.radioValue = null;
	this.checkValue = new Vector();
	this.defaultPicture = null;
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
     * ソート順を設定する.
     * @param newOrder String
     */
    public void setOrder(String newOrder) {
	this.order = newOrder;
    }
    /**
     * ソート順を取得する.
     * @return String
     */
    public String getOrder() {
	return this.order;
    }
    /**
     * ソート順を逆転する.
     */
    public void setOrderReverse() {
	if ( this.order.equals("ASC") ) {
	    this.order = "DESC";
	}
	else {
	    this.order = "ASC";
	}
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
     * ラジオボタン状態を設定する.
     * @param newRadioValue String
     */
    public void setRadioValue(String newRadioValue) {
	this.radioValue = newRadioValue;
    }
    /**
     * ラジオボタン状態を取得する.
     * @return String
     */
    public String getRadioValue() {
	return this.radioValue;
    }
    /**
     * ラジオボタン状態をクリアする.
     */
    public void crearRadioValue() {
	this.radioValue = null;
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
     * デフォルト画像コードを設定する.
     * @param newDefaultCD String
     */
    public void setDefaultCD(String newDefaultCD) {
	this.defaultPicture = newDefaultCD;
    }
    /**
     * デフォルト画像コードを取得する.
     * @return String
     */
    public String getDefaultCD() {
	return this.defaultPicture;
    }
}
