package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.LinkLibInfo;

/**
 * リンク一覧セッション情報管理
 * @auther
 * @version
 */
public class LinkUpdateSession {
	/** 状態 */
	public static final int STATE_NORMAL	= 0;		// 通常
	public static final int STATE_ALERT 	= 1;		// アラート出力
	public static final int STATE_CONFIRM	= 2;		// コンファーム出力
	public static final int STATE_REPORT	= 3;		// 結果出力
	public static final int TEXT_ERR	= 4;		// "タイトルに"＆"があった"

	private int nStatus = STATE_NORMAL;					// 状態
	private LinkLibInfo objLinkLibInfo = null;			// リンク情報

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
     * リンク情報を設定する。
     * @param info リンク情報
     */
	public void setLinkLibInfo(LinkLibInfo info) {
		objLinkLibInfo = info;
	}

	/**
     * リンク情報を取得する。
     * @return リンク情報
     */
	public LinkLibInfo getLinkLibInfo() {
		return objLinkLibInfo;
	}

}
