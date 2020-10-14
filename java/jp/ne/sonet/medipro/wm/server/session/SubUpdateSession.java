package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * サブマスター追加・変更画面用セッション情報管理
 * @auther
 * @version
 */
public class SubUpdateSession {
	/** 状態 */
	public static final int STATE_NORMAL	= 0;	// 通常
	public static final int STATE_ALERT_1 	= 1;	// アラート出力
	public static final int STATE_ALERT_2 	= 2;	// アラート出力
	public static final int STATE_CONFIRM	= 3;	// コンファーム出力
	public static final int STATE_REPORT	= 4;	// 結果出力

	private int nStatus = STATE_NORMAL;				// 状態
	private MrInfo objMrInfo = null;				// ＭＲ情報

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
     * ＭＲ情報を設定する。
     * @param info ＭＲ情報
     */
	public void setMrInfo(MrInfo info) {
		objMrInfo = info;
	}

	/**
     * ＭＲ情報を取得する。
     * @return ＭＲ情報
     */
	public MrInfo getMrInfo() {
		return objMrInfo;
	}

}
