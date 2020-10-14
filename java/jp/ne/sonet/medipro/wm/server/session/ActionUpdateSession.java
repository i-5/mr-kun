package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.ActionInfo;

/**
 * 重要度追加・変更セッション情報管理
 * @auther
 * @version
 */
public class ActionUpdateSession {
	/** 状態 */
	public static final int STATE_NORMAL	= 0;		// 通常
	public static final int STATE_ALERT 	= 1;		// アラート出力
	public static final int STATE_CONFIRM	= 2;		// コンファーム出力
	public static final int STATE_REPORT	= 3;		// 結果出力

	private int nStatus = STATE_NORMAL;					// 状態
	private ActionInfo objActionInfo = null;	// 重要度情報

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
     * 重要度情報を設定する。
     * @param info 重要度情報
     */
	public void setActionInfo(ActionInfo info) {
		objActionInfo = info;
	}

	/**
     * 重要度情報を取得する。
     * @return 重要度情報
     */
	public ActionInfo getActionInfo() {
		return objActionInfo;
	}

}
