package jp.ne.sonet.medipro.wm.server.session;

import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>ログイン者 and 定数情報クラス。</strong>
 * @author
 * @version
 */
public class Common {
    /** MR-ID */
    private String mrId = new String();
    /** 会社コード */
    private String companyCd = new String();
    /** 支店コード */
    private String shitenCd = new String();
    /** 営業所コード */
    private String eigyosyoCd = new String();
    /** MR属性1コード */
    private String mrAttributeCd1 = new String();
    /** MR属性2コード */
    private String mrAttributeCd2 = new String();
    /** マスターフラグ */
    private String masterFlg = new String();
    /** マスター権限範囲(組織) */
    private String masterKengenSoshiki = new String();
    /** マスター権限範囲(属性) */
    private String masterKengenAttribute = new String();
    /** キャッチ画像ホーム */
    private String catchHome = new String();
    /** キャッチ画像一時保管Directory */
    private String tempDir = new String();
    /** WebServerドキュメントルート */
    private String documentRoot = new String();
    /** タイムアウト */
    private int timeout = 0;

    /** MR一覧表示数 */
    private int mrLine = 0;
    /** MRキャッチ画像一覧表示数 */
    private int mrCatchLine = 0;
    /** 支店一覧表示数 */
    private int shitenLine = 0;
    /** 属性一覧表示数 */
    private int attributeLine = 0;
    /** サブマスター一覧表示数 */
    private int subLine = 0;
    /** キャッチ画像一覧表示数 */
    private int catchLine = 0;
    /** リンク一覧表示数 */
    private int linkLine = 0;
    /** 定型文一覧表示数 */
    private int bunLine = 0;
    /** コール内容一覧表示数 */
    private int callLine = 0;

    /**
     * Commonを生成します。
     */
    public Common() {
        if (SysCnst.DEBUG) {
            System.err.println("Common is created");
        }
    }

    /**
     * ＭＲ－ＩＤを設定する。
     * @param mrId ＭＲ－ＩＤ
     */
    public void setMrId(String mrId) {
        if (mrId == null) {
            this.mrId = "";
        } else {
            this.mrId = mrId;
        }
    }

    /**
     * ＭＲ－ＩＤを取得する。
     * @return ＭＲ－ＩＤ
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * 会社コードを設定する。
     * @param companyCd 会社コード
     */
    public void setCompanyCd(String companyCd) {
        if (companyCd == null) {
            this.companyCd = "";
        } else {
            this.companyCd = companyCd;
        }
    }

    /**
     * 会社コードを取得する。
     * @return 会社コード
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * 支店コードを設定する。
     * @param shitenCd 支店コード
     */
    public void setShitenCd(String shitenCd) {
        if (shitenCd == null) {
            this.shitenCd = "";
        } else {
            this.shitenCd = shitenCd;
        }
    }

    /**
     * 支店コードを取得する。
     * @return 支店コード
     */
    public String getShitenCd() {
        return shitenCd;
    }

    /**
     * 営業所コードを設定する。
     * @param eigyosyoCd 営業所コード
     */
    public void setEigyosyoCd(String eigyosyoCd) {
        if (eigyosyoCd == null) {
            this.eigyosyoCd = "";
        } else {
            this.eigyosyoCd = eigyosyoCd;
        }
    }

    /**
     * 営業所コードを取得する。
     * @return 営業所コード
     */
    public String getEigyosyoCd() {
        return eigyosyoCd;
    }

    /**
     * ＭＲ属性１コードを設定する。
     * @param mrAttributeCd1 ＭＲ属性１コード
     */
    public void setMrAttributeCd1(String mrAttributeCd1) {
        if (mrAttributeCd1 == null) {
            this.mrAttributeCd1 = "";
        } else {
            this.mrAttributeCd1 = mrAttributeCd1;
        }
    }

    /**
     * ＭＲ属性１コードを取得する。
     * @return ＭＲ属性１コード
     */
    public String getMrAttributeCd1() {
        return mrAttributeCd1;
    }

    /**
     * ＭＲ属性２コードを設定する。
     * @param mrAttributeCd1 ＭＲ属性２コード
     */
    public void setMrAttributeCd2(String mrAttributeCd2) {
        if (mrAttributeCd2 == null) {
            this.mrAttributeCd2 = "";
        } else {
            this.mrAttributeCd2 = mrAttributeCd2;
        }
    }

    /**
     * ＭＲ属性２コードを取得する。
     * @return ＭＲ属性２コード
     */
    public String getMrAttributeCd2() {
        return mrAttributeCd2;
    }

    /**
     * マスターフラグを設定する。
     * @param masterFlg マスターフラグ
     */
    public void setMasterFlg(String masterFlg) {
        if (masterFlg == null) {
            this.masterFlg = "";
        } else {
            this.masterFlg = masterFlg;
        }
    }

    /**
     * マスターフラグを取得する。
     * @return マスターフラグ
     */
    public String getMasterFlg() {
        return masterFlg;
    }

    public void setMasterKengenSoshiki(String kengen) {
        if (kengen == null) {
            this.masterKengenSoshiki = "";
        } else {
            this.masterKengenSoshiki = kengen;
        }
    }

    public String getMasterKengenSoshiki() {
        return masterKengenSoshiki;
    }
  
    public void setMasterKengenAttribute(String kengen) {
        if (kengen == null) {
            this.masterKengenAttribute = "";
        } else {
            this.masterKengenAttribute = kengen;
        }
    }

    public String getMasterKengenAttribute() {
        return masterKengenAttribute;
    }

    public String getCatchHome() {
        return catchHome;
    }

    public void setCatchHome(String home) {
        catchHome = home;
    }

    public String getTempDir() {
        return catchHome + tempDir;
    }

    public void setTempDir(String dir) {
        tempDir = dir;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String dir) {
        documentRoot = dir;
    }

    public int getTimeout() {
	return timeout;
    }

    public void setTimeout(int time) {
	timeout = time;
    }

    public int getMrLine() {
        return mrLine;
    }

    public void setMrLine(int num) {
        this.mrLine = num;
    }

    public int getMrCatchLine() {
        return mrCatchLine;
    }

    public void setMrCatchLine(int num) {
        this.mrCatchLine = num;
    }

    public int getShitenLine() {
        return shitenLine;
    }

    public void setShitenLine(int num) {
        this.shitenLine = num;
    }

    public int getAttributeLine() {
        return attributeLine;
    }

    public void setAttributeLine(int num) {
        this.attributeLine = num;
    }

    public int getSubLine() {
        return subLine;
    }

    public void setSubLine(int num) {
        this.subLine = num;
    }

    public int getCatchLine() {
        return catchLine;
    }

    public void setCatchLine(int num) {
        this.catchLine = num;
    }

    public int getLinkLine() {
        return linkLine;
    }

    public void setLinkLine(int num) {
        this.linkLine = num;
    }

    public int getBunLine() {
        return bunLine;
    }

    public void setBunLine(int num) {
        this.bunLine = num;
    }

    public int getCallLine() {
        return callLine;
    }

    public void setCallLine(int num) {
        this.callLine = num;
    }

}
