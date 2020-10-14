package jp.ne.sonet.medipro.wm.server.entity;

import java.lang.String;

/**
 * <strong>会社キャッチ画像情報Entityクラス.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CatchInfo {
	/////////////////////////////////////////////
	//class variables
	//
    protected String pictureCD;		// 画像コード
    protected String pictureName;	// 画像名
    protected String picture;		// 画像
    protected String pictureType;	// 画像形式
    protected boolean defaultFlg;	// 基本画面フラグ
    protected String mrId;			// MRID

	/////////////////////////////////////////////
	//constructors
	//
	/**
	 * コンストラクタ.
	 */
    public CatchInfo() {
		pictureCD = null;
		pictureName = null;
		picture = null;
		pictureType = null;
		defaultFlg = false;
		mrId = null;
    }
    
	/////////////////////////////////////////////
	//class methods
	//
    /**
     * 画像コードの取得.
     * @return String
     */
    public String getPictureCD() {
        return pictureCD;
    }

    /**
     * 画像名の取得.
     * @return String
     */
    public String getPictureName() {
        return pictureName;
    }
    /**
     * 画像の取得.
     * @return String
     */
    public String getPicture() {
        return picture;
    }
    /**
     * 画像形式の取得.
     * @return String
     */
    public String getPictureType() {
        return pictureType;
    }

    /**
     * デフォルト画像フラグの取得.
     * @return boolean
     */
    public boolean getDefaultFlg() {
        return defaultFlg;
    }

    /**
     * 画像コードのセット.
     * @param newPictureCD String
     */
    public void setPictureCD(String newPictureCD) {
        pictureCD = newPictureCD;
    }
    /**
     * 画像名のセット.
     * @param newPictureName String
     */
    public void setPictureName(String newPictureName) {
        pictureName = newPictureName;
    }
    /**
     * 画像のセット.
     * @param newPicture String
     */
    public void setPicture(String newPicture) {
        picture = newPicture;
    }
    /**
     * 画像形式のセット.
     * @param newPictureType String
     */
    public void setPictureType(String newPictureType) {
        pictureType = newPictureType;
    }

    /**
     * デフォルト画像フラグのセット.
     * @param newDefaultFlg boolean
     */
    public void setDefaultFlg(boolean newDefaultFlg) {
        defaultFlg = newDefaultFlg;
    }

    /**
     * MRIDを取得する。
     * @return MRID
     */
    public String getMrId() {
	return mrId;
    }

    /**
     * MRIDを設定する。
     * @param id MRID
     */
    public void setMrId(String id) {
	mrId = id;
    }

    /**
     * 文字列化する.
     * @return String
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append(pictureCD + "\n");
        me.append(pictureName + "\n");
                
        return me.toString();
    }
}
