package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>会社情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CompanyInfo {
    protected String companyCd;		// 会社コード
    protected String companyKbn;	// 会社区分
    protected String companyKbnNaiyo;	// 会社区分内容
    protected String companyName;	// 会社名
    protected String cdPrefix;		// コードprefix
    protected String pictureCd;		// デフォルト画像コード
    protected String linkCd;		// デフォルトリンクコード
    protected String targetRank;	// ターゲットランク
    protected String displayRanking;	// 画面表示

    /**
     * CompanyInfoオブジェクトを新規に作成する。
     */
    public CompanyInfo() {
    }

    /**
     * 会社コードを設定する。
     * @param arg 会社コード
     */
    public void setCompanyCd(String arg) {
        companyCd = arg;
    }

    /**
     * 会社コードを取得する。
     * @return 会社コード
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * 会社区分を設定する。
     * @param arg 会社区分
     */
    public void setCompanyKbn(String arg) {
        companyKbn = arg;
    }

    /**
     * 会社区分を取得する。
     * @return 会社区分
     */
    public String getCompanyKbn() {
        return companyKbn;
    }

    /**
     * 会社区分内容を設定する。
     * @param arg 会社区分内容
     */
    public void setCompanyKbnNaiyo(String arg) {
        companyKbnNaiyo = arg;
    }

    /**
     * 会社区分内容を取得する。
     * @return 会社区分内容
     */
    public String getCompanyKbnNaiyo() {
        return companyKbnNaiyo;
    }

    /**
     * 会社名を設定する。
     * @param arg 会社名
     */
    public void setCompanyName(String arg) {
        companyName = arg;
    }

    /**
     * 会社名を取得する。
     * @return 会社名
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * コードprefixを設定する。
     * @param arg コードprefix
     */
    public void setCdPrefix(String arg) {
        cdPrefix = arg;
    }

    /**
     * コードprefixを取得する。
     * @return コードprefix
     */
    public String getCdPrefix() {
        return cdPrefix;
    }

    /**
     * デフォルト画像コードを設定する。
     * @param arg デフォルト画像コード
     */
    public void setPictureCd(String arg) {
        pictureCd = arg;
    }

    /**
     * デフォルト画像コードを取得する。
     * @return デフォルト画像コード
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * デフォルトリンクコードを設定する。
     * @param arg デフォルトリンクコード
     */
    public void setLinkCd(String arg) {
        linkCd = arg;
    }

    /**
     * デフォルトリンクコードを取得する。
     * @return デフォルトリンクコード
     */
    public String getLinkCd() {
        return linkCd;
    }

    public void setTargetRank(String value) {
	targetRank = value;
    }

    public String getTargetRank() {
	return targetRank;
    }

    /**
     * ランキング表示を設定する。
     * @param arg ランキング表示設定
     */
    public void setDisplayRanking(String arg) {
        displayRanking = arg;
    }

    /**
     * ランキング表示設定を取得する。
     * @return ランキング表示設定
     */
    public String getDisplayRanking() {
        return displayRanking;
    }

    /**
     * 文字列化する。
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("company_cd=" + companyCd + "\n");
        me.append("company_kbn=" + companyKbn + "\n");
        me.append("company_name=" + companyName + "\n");
        me.append("cd_prefix=" + cdPrefix + "\n");
        me.append("picture_cd=" + pictureCd + "\n");
        me.append("link_cd=" + linkCd + "\n");
        return me.toString();
    }
}
