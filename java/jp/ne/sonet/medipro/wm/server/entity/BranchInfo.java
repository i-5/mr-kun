package jp.ne.sonet.medipro.wm.server.entity;

import java.lang.String;

/**
 * <strong>支店・営業所情報Entityクラス.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class BranchInfo {
	/////////////////////////////////////////////
	//class variables
	//
	protected String shitenCD;		// 支店コード
	protected String shitenName;	// 支店名
	protected String eigyosyoCD;	// 営業所コード
	protected String eigyosyoName;	// 営業所名
	protected int	 maxRow;		// 最大行数
	protected boolean mrFlg;		// ＭＲ所属フラグ
	protected boolean shitenFlg;	// 支店フラグ

	/////////////////////////////////////////////
	//constructors
	//
	/**
	 * コンストラクタ.
	 */
	public BranchInfo() {
		shitenCD = null;
		shitenName = null;
		eigyosyoCD = null;
		eigyosyoName = null;
		maxRow = 0;
		mrFlg = false;
		shitenFlg = false;
	}
	
	/////////////////////////////////////////////
	//class methods
	//

	/**
	 * 支店コードの取得.
	 * @return String
	 */
	public String getShitenCD() {
		return shitenCD;
	}
	/**
	 * 支店名の取得.
	 * @return String
	 */
	public String getShitenName() {
		return shitenName;
	}
	/**
	 * 営業所コードの取得.
	 * @return String
	 */
	public String getEigyosyoCD() {
		return eigyosyoCD;
	}
	/**
	 * 営業所名の取得.
	 * @return String
	 */
	public String getEigyosyoName() {
		return eigyosyoName;
	}
	/**
	 * 最大行数の取得.
	 * @return int
	 */
	public int getMaxRow() {
		return maxRow;
	}
	/**
	 * ＭＲ所属フラグの取得.
	 * @return boolean
	 */
	public boolean isMr() {
		return mrFlg;
	}
	/**
	 * 支店フラグの取得.
	 * @return boolean
	 */
	public boolean isShiten() {
		return shitenFlg;
	}
	/**
	 * 支店コードのセット.
	 * @param newShitenCD String
	 */
	public void setShitenCD(String newShitenCD) {
		shitenCD = newShitenCD;
	}
	/**
	 * 支店名のセット.
	 * @param newShitenName String
	 */
	public void setShitenName(String newShitenName) {
		shitenName = newShitenName;
	}
	/**
	 * 営業所コードのセット.
	 * @param newDrRecvMsgNoCount String
	 */
	public void setEigyosyoCD(String newEigyosyoCD) {
		eigyosyoCD = newEigyosyoCD;
	}
	/**
	 * 営業所名のセット.
	 * @param newEigyosyoName String
	 */
	public void setEigyosyoName(String newEigyosyoName) {
		eigyosyoName = newEigyosyoName;
	}
	/**
	 * 最大行数のセット.
	 * @param newMaxRow int
	 */
	public void setMaxRow(int newMaxRow) {
		maxRow = newMaxRow;
	}
	/**
	 * ＭＲ所属フラグのセット.
	 * @param newMrFlg boolean
	 */
	public void setMrFlg(boolean newMrFlg) {
		mrFlg = newMrFlg;
	}
	/**
	 * 支店フラグのセット.
	 * @param newShitenFlg boolean
	 */
	public void setShitenFlg(boolean newShitenFlg) {
		shitenFlg = newShitenFlg;
	}

	/**
	 * 文字列化する.
	 * @return String
	 */
	public String toString() {
		StringBuffer me = new StringBuffer();
		me.append(shitenCD + "\n");
		me.append(shitenName + "\n");
		me.append(eigyosyoCD + "\n");
		me.append(eigyosyoName + "\n");
			
		return me.toString();
	}
}
