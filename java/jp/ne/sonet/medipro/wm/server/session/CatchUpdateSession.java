package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>会社キャッチ画像更新セッションクラス.</strong>
 * @auther
 * @version
 */
public class CatchUpdateSession {
    /////////////////////////////////////////////
    //class variables
    //
    private String	pictureCD;		// 画像コード
    private int		messageState;	// メッセージID
    private String	pictureType;	// 画像形式
    private String	pictureName;	// 画像名
    private String	picture;		// 画像(パス)
    private boolean updateFlg;		// 新規登録or変更フラグ
    private String	type;			// gif or jpeg
    private String	path;			// 画像ファイルパス
    private boolean firstFlg;		// 一覧画面からの遷移フラグ
    private boolean tempFlg;		// 一時ファイル表示フラグ
    private String check;

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * コンストラクタ.
     */
    public CatchUpdateSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("CatchUpdate Session Created!!");
	}
	this.pictureCD = null;
	this.messageState = 0;
	this.pictureType = null;
	this.pictureName = null;
	this.picture = null;
	this.updateFlg = false;
	this.path = null;
	this.firstFlg = true;
	this.tempFlg = false;
	this.check = "";
    }
	
    /////////////////////////////////////////////
    //class methods
    //
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
     * 画像コードを設定する.
     * @param newPictureCD String
     */
    public void setPictureCD(String newPictureCD) {
	this.pictureCD = newPictureCD;
    }
    /**
     * 画像コードを取得する.
     * @return String
     */
    public String getPictureCD() {
	return this.pictureCD;
    }
    /**
     * 画像コードをクリアする.
     */
    public void crearPictureCD() {
	this.pictureCD = null;
    }
    /**
     * 画像形式を設定する.
     * @param newPictureType String
     */
    public void setPictureType(String newPictureType) {
	this.pictureType = newPictureType;
    }
    /**
     * 画像形式を取得する.
     * @return String
     */
    public String getPictureType() {
	return this.pictureType;
    }
    /**
     * 画像形式をクリアする.
     */
    public void crearPictureType() {
	this.pictureType = null;
    }
    /**
     * 画像名を設定する.
     * @param newPictureName String
     */
    public void setPictureName(String newPictureName) {
	this.pictureName = newPictureName;
    }
    /**
     * 画像名を取得する.
     * @return String
     */
    public String getPictureName() {
	return this.pictureName;
    }
    /**
     * 画像名をクリアする.
     */
    public void crearPictureName() {
	this.pictureName = null;
    }
    /**
     * 画像を設定する.
     * @param newPicture String
     */
    public void setPicture(String newPicture) {
	this.picture = newPicture;
    }
    /**
     * 画像を取得する.
     * @return String
     */
    public String getPicture() {
	return this.picture;
    }
    /**
     * 画像をクリアする.
     */
    public void crearPicture() {
	this.picture = null;
    }
    /**
     * ファイル拡張子を設定する.
     * @param newPictureName String
     */
    public void setType(String newType) {
	this.type = newType;
    }
    /**
     * ファイル拡張子を取得する.
     * @return String
     */
    public String getType() {
	return this.type;
    }
    /**
     * 画像ファイルパスを設定する.
     * @param newPath String
     */
    public void setPath(String newPath) {
	this.path = newPath;
    }
    /**
     * 画像ファイルパスを取得する.
     * @return String
     */
    public String getPath() {
	return this.path;
    }
    /**
     * 画像ファイルパスをクリアする.
     */
    public void setPath() {
	this.path = null;
    }
    /**
     * 新規登録・変更フラグを設定する.
     * @param newUpdateFlg boolean
     */
    public void setUpdateFlg(boolean newUpdateFlg) {
	this.updateFlg = newUpdateFlg;
    }
    /**
     * 新規登録・変更フラグを取得する.
     * @return boolean
     */
    public boolean isUpdate() {
	return this.updateFlg;
    }
    /**
     * 一覧画面からの遷移フラグを設定する.
     * @param newFlg boolean
     */
    public void setFirstFlg(boolean newFlg) {
	this.firstFlg = newFlg;
    }
    /**
     * 一覧画面からの遷移フラグを取得する.
     * @return boolean
     */
    public boolean isFirst() {
	return this.firstFlg;
    }
    /**
     * 一時ファイル表示フラグを設定する.
     * @param newFlg boolean
     */
    public void setTempFlg(boolean newFlg) {
	this.tempFlg = newFlg;
    }
    /**
     * 一時ファイル表示フラグを取得する.
     * @return boolean
     */
    public boolean isTemp() {
	return this.tempFlg;
    }

    public void check() {
	check = "checked";
    }

    public void unCheck() {
	check = "";
    }

    public String getCheck() {
	return check;
    }
}
