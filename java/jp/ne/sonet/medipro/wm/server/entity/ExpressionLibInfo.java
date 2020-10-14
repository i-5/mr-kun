package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>定型文ライブラリ情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfo {
	protected String teikeibunCd;		// 定型文コード
	protected String companyCd;			// 会社コード
	protected String title;				// タイトル
	protected String honbun;			// 本文
	protected String description;
	protected String bunruiCd;
	protected String bunruiName;

    /**
     * ExpressionLibInfoオブジェクトを新規に作成する。
     */
    public ExpressionLibInfo() {
    }

    /**
     * 定型文コードを設定する。
     * @param arg 定型文コード
     */
    public void setBunruiName(String arg) {
        bunruiName = arg;
    }

    /**
     * 定型文コードを取得する。
     * @return 定型文コード
     */
    public String getBunruiName() {
        return bunruiName;
    }

    /**
     * 定型文コードを設定する。
     * @param arg 定型文コード
     */
    public void setDescription(String arg) {
        description = arg;
    }

    /**
     * 定型文コードを取得する。
     * @return 定型文コード
     */
    public String getDescription() {
        return description;
    }

    /**
     * 定型文コードを設定する。
     * @param arg 定型文コード
     */
    public void setBunruiCode(String arg) {
        bunruiCd = arg;
    }

    /**
     * 定型文コードを取得する。
     * @return 定型文コード
     */
    public String getBunruiCode() {
        return bunruiCd;
    }


    /**
     * 定型文コードを設定する。
     * @param arg 定型文コード
     */
    public void setTeikeibunCd(String arg) {
        teikeibunCd = arg;
    }

    /**
     * 定型文コードを取得する。
     * @return 定型文コード
     */
    public String getTeikeibunCd() {
        return teikeibunCd;
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
     * タイトルを設定する。
     * @param arg タイトル
     */
    public void setTitle(String arg) {
       title = arg;
    }

    /**
     * タイトルを取得する。
     * @return タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * 本文を設定する。
     * @param name 本文
     */
    public void setHonbun(String arg) {
        honbun = arg;
    }

    /**
     * 本文を取得する。
     * @return 本文
     */
    public String getHonbun() {
        return honbun;
    }

    /**
     * 文字列化する。
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("teikeibun_cd=" + teikeibunCd + "\n");
        me.append("company_cd=" + companyCd + "\n");
        me.append("title=" + title + "\n");
        me.append("honbun=" + honbun + "\n");
        return me.toString();
    }
}
