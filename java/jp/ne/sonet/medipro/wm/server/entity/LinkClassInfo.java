package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>リンク分類情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkClassInfo {
	protected String linkBunruiCd;		// リンク分類コード
	protected String bunruiName;		// 分類名
	protected String companyCd;			// 会社コード

    /**
     * LinkClassInfoオブジェクトを新規に作成する。
     */
    public LinkClassInfo() {
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
     * 分類名を設定する。
     * @param arg 分類名
     */
    public void setBunruiName(String arg) {
        bunruiName = arg;
    }

    /**
     * 分類名を取得する。
     * @return 分類名
     */
    public String getBunruiName() {
        return bunruiName;
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
     * 文字列化する。
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("link_bunrui_cd=" + linkBunruiCd + "\n");
        me.append("bunrui_name=" + bunruiName + "\n");
        me.append("company_cd=" + companyCd + "\n");
        return me.toString();
    }
}
