package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>リンクライブラリ情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkLibInfo {
	protected String linkCd;			// リンクコード
	protected String companyCd;			// 会社コード
	protected String linkBunruiCd;		// リンク分類コード
	protected String linkBunruiName;	// リンク分類名
	protected String description;		// added by hb010914
	protected String url;				// ＵＲＬ
	protected String honbunText;		// 本文テキスト
	protected String picture;			// 画像
	protected String naigaiLinkKbn;		// 内外リンク区分

    /**
     * LinkLibInfoオブジェクトを新規に作成する。
     */
    public LinkLibInfo() {
    }

    /**
     * リンクコードを設定する。
     * @param arg リンクコード
     */
    public void setLinkCd(String arg) {
        linkCd = arg;
    }

    /**
     * リンクコードを取得する。
     * @return リンクコード
     */
    public String getLinkCd() {
        return linkCd;
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
     * リンク分類コードを設定する。
     * @param arg リンク分類コード
     */
    public void setLinkBunruiCd(String arg) {
       linkBunruiCd = arg;
    }

    /**
     * リンク分類コードを取得する。
     * @return リンク分類コード
     */
    public String getLinkBunruiCd() {
        return linkBunruiCd;
    }

    /**
     * リンク分類名を設定する。
     * @param name リンク分類名
     */
    public void setLinkBunruiName(String arg) {
        linkBunruiName = arg;
    }

    /**
     * リンク分類名を取得する。
     * @return リンク分類名
     */
    public String getLinkBunruiName() {
        return linkBunruiName;
    }

    
    /*
    *
    * @param d The value for the description field
    */
    public void setDescription(String d)
    {
    	this.description = d;
    }
    
    /*
    *
    * @return the description field for this URL
    */
    
    public String getDescription()
    {
    	return description;
    }
    /**
     * ＵＲＬを設定する。
     * @param arg ＵＲＬ
     */
    public void setUrl(String arg) {
        url = arg;
    }

    /**
     * ＵＲＬを取得する。
     * @return ＵＲＬ
     */
    public String getUrl() {
        return url;
    }

    /**
     * 本文テキストを設定する。
     * @param arg 本文テキスト
     */
    public void setHonbunText(String arg) {
        honbunText = arg;
    }

    /**
     * 本文テキストを取得する。
     * @return 本文テキスト
     */
    public String getHonbunText() {
        return honbunText;
    }

    /**
     * 画像を設定する。
     * @param arg 画像
     */
    public void setPicture(String arg) {
        picture = arg;
    }

    /**
     * 画像を取得する。
     * @return 画像
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 内外リンク区分を設定する。
     * @param arg 内外リンク区分
     */
    public void setNaigaiLinkKbn(String arg) {
        naigaiLinkKbn = arg;
    }

    /**
     * 内外リンク区分を取得する。
     * @return 内外リンク区分
     */
    public String getNaigaiLinkKbn() {
        return naigaiLinkKbn;
    }

    /**
     * 文字列化する。
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("link_cd=" + linkCd + "\n");
        me.append("company_cd=" + companyCd + "\n");
        me.append("link_bunrui_cd=" + linkBunruiCd + "\n");
        me.append("url=" + url + "\n");
        me.append("honbun_text=" + honbunText + "\n");
        me.append("picture=" + picture + "\n");
        me.append("naigai_link_kbn=" + naigaiLinkKbn + "\n");
        return me.toString();
    }
}
