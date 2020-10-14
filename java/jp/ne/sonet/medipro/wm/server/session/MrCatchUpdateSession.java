package jp.ne.sonet.medipro.wm.server.session;

import java.io.File;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.CatchInfo;

/**
 * <strong>MRキャッチ画像追加・更新画面用セッションクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateSession implements HttpSessionBindingListener {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    //
    /** 通常 */
    public static final int NORMAL = 0;
    /** 保存確認 */
    public static final int SAVE_CONFIRM = 1;
    /** 保存完了 */
    public static final int SAVE_DONE = 2;

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** 現在のstatus */
    private int status = NORMAL;
    /** MR-ID */
    private String mrId = new String();
    /** キャッチ画像情報 */
    private CatchInfo info = null;
    /** DBから読み込む必要を示すフラグ */
    private boolean loadFlag = false;
    /** 一時ファイル名 */
    private String tempPicture = null;
    /** 拡張子 */
    private String extension = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MRキャッチ画像セッションオブジェクトを生成する.
     */
    public MrCatchUpdateSession() {
        info = new CatchInfo();

        if (SysCnst.DEBUG) {
            System.err.println("MrCatchUpdateSession is created");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * 最新の情報をDBから読み込む必要があるか確認する.
     * @return 読み込む必要があればtrue
     */
    public boolean isNeedToLoad() {
        return loadFlag;
    }

    /**
     * 最新の情報をDBから読み込む必要があるかどうかを設定する.
     * @param flag 読み込みを要求する場合はtrue
     */
    public void setLoadFlag(boolean flag) {
        loadFlag = flag;
    }

    /**
     * 現在のステータスを取得する.
     * @return ステータス
     */
    public int getStatus() {
        return status;
    }

    /**
     * ステータスを設定する.
     * @param status 設定するステータス
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 対象とする画像が属するMR-IDを取得する.
     * @return MR-ID
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * 対象とする画像が属するMR-IDを設定する.
     * @param mrId 設定するMR-ID
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /**
     * 画像名を取得する.
     * @return 画像名称
     */
    public String getPictureName() {
        return info.getPictureName() == null ? "" : info.getPictureName();
    }

    /**
     * 画像名を設定する.
     * @param name 設定する画像名
     */
    public void setPictureName(String name) {
        info.setPictureName(name);
    }

    /**
     * 画像コードを取得する.
     * @return 画像コード
     */
    public String getPictureCd() {
        return info.getPictureCD() == null ? "" : info.getPictureCD();
    }

    /**
     * 画像コードを設定する.
     * @param cd 設定する画像コード
     */
    public void setPictureCd(String cd) {
        info.setPictureCD(cd);
    }

    /**
     * 画像ファイル名を取得する.
     * ただし、一時画像が設定されている場合はそちらを返す.
     * @return 画像ファイル名
     */
    public String getPicture() {
        if (tempPicture == null || tempPicture.equals("")) {
            return info.getPicture() == null ? "" : info.getPicture();
        }
        
        return tempPicture;
    }

    /**
     * 画像ファイル名を設定する.
     * @param path 設定する画像ファイル名
     */
    public void setPicture(String path) {
        info.setPicture(path);
    }

    /**
     * 一時画像ファイル名を取得する.
     * @return 一時画像ファイル名
     */
    public String getTempPicture() {
        return tempPicture;
    }

    /**
     * 一時画像ファイル名を設定する.
     * @param path 設定する一時画像ファイル名
     */
    public void setTempPicture(String path) {
        tempPicture = path;
    }

    /**
     * 一時画像ファイルの拡張子を設定する.
     * @param ext 拡張子(gif, jpg等)
     */
    public void setExtension(String ext) {
        extension = ext;
    }

    /**
     * 一時画像ファイルの拡張子を取得する.
     * @return 拡張子
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 現在保持する画像情報を取得する.
     * @return 画像情報
     */
    public CatchInfo getCatchInfo() {
        return info;
    }

    /**
     * 画像情報を設定する.
     * @param info 設定する画像情報
     */
    public void setCatchInfo(CatchInfo info) {
        this.info = info;
    }

    public boolean isChecked() {
	return info.getDefaultFlg();
    }

    public void setCheck(boolean flag) {
	info.setDefaultFlg(flag);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // 以下 HttpSessionBindingListenerのconcreate
    //
    /**
     * このセッションオブジェクトがboundされるとき呼ばれる.
     * @param event 渡されるイベント
     */
    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * このセッションがunboundされるとき呼ばれる.
     * 一時画像ファイルが残っていれば削除する.
     * @param event 渡されるイベント
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (SysCnst.DEBUG) {
            System.err.println("MrCatchUpdateSession is unbound");
        }

        if (tempPicture == null || tempPicture.trim().equals("")) {
            return;
        }

        try {
            new File(tempPicture).delete();
        } catch (Exception ex) {
        }
    }
}
