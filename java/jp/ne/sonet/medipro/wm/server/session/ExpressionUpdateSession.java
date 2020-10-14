package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.server.entity.ExpressionLibInfo;

/**
 * 定型文追加・変更セッション情報管理
 * @auther
 * @version
 */
public class ExpressionUpdateSession {
	/** 状態 */
	public static final int STATE_NORMAL	= 0;		// 通常
	public static final int STATE_ALERT 	= 1;		// アラート出力
	public static final int STATE_CONFIRM	= 2;		// コンファーム出力
	public static final int STATE_REPORT	= 3;		// 結果出力

	private int nStatus = STATE_NORMAL;					// 状態
	private ExpressionLibInfo objExpressionLibInfo = null;	// 定型文情報

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
     * 定型文情報を設定する。
     * @param info 定型文情報
     */
	public void setExpressionLibInfo(ExpressionLibInfo info) {
		objExpressionLibInfo = info;
	}

	/**
     * 定型文情報を取得する。
     * @return 定型文情報
     */
	public ExpressionLibInfo getExpressionLibInfo() {
		return objExpressionLibInfo;
	}

}
