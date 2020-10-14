package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.LinkClassInfo;

/**
 * リンク分類追加・更新画面用セッション情報管理
 * @auther
 * @version
 */
public class LinkClassUpdateSession {
	/** 状態 */
	public static final int STATE_NORMAL	= 0;		// 通常
	public static final int STATE_ALERT 	= 1;		// アラート出力
	public static final int STATE_CONFIRM	= 2;		// コンファーム出力
	public static final int STATE_REPORT	= 3;		// 結果出力

	private int nStatus = STATE_NORMAL;					// 状態
	private LinkClassInfo objLinkClassInfo = null;		// リンク分類情報

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
     * リンク分類情報を設定する。
     * @param info リンク分類情報
     */
	public void setLinkClassInfo(LinkClassInfo info) {
		objLinkClassInfo = info;
	}

	/**
     * リンク分類情報を取得する。
     * @return リンク分類情報
     */
	public LinkClassInfo getLinkClassInfo() {
		return objLinkClassInfo;
	}

}
