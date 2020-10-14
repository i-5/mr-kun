package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>ＭＲ属性情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeInfo {
	protected String mrAttributeCd;		// ＭＲ属性コード
	protected String mrAttributeName;	// ＭＲ属性名
	protected String companyCd;			// 会社コード

    /**
     * AttributeInfoオブジェクトを新規に作成する。
     */
    public AttributeInfo() {
    }

    /**
     * ＭＲ属性コードを設定する。
     * @param arg ＭＲ属性コード
     */
    public void setMrAttributeCd(String arg) {
        mrAttributeCd = arg;
    }

    /**
     * ＭＲ属性コードを取得する。
     * @return ＭＲ属性コード
     */
    public String getMrAttributeCd() {
        return mrAttributeCd;
    }

    /**
     * ＭＲ属性名を設定する。
     * @param arg ＭＲ属性名
     */
    public void setMrAttributeName(String arg) {
        mrAttributeName = arg;
    }

    /**
     * ＭＲ属性名を取得する。
     * @return ＭＲ属性名
     */
    public String getMrAttributeName() {
        return mrAttributeName;
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
        me.append("mr_attribute_cd=" + mrAttributeCd + "\n");
        me.append("mr_attribute_name=" + mrAttributeName + "\n");
        me.append("company_cd=" + companyCd + "\n");
        return me.toString();
    }
}
