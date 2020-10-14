package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>ＭＲ情報</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class MrInfo {
    // hb010716 added this for 代行配信
    protected Hashtable daikou = new Hashtable();
    /** MR-ID */
    protected String mrId = null;
    /** 会社コード */
    protected String companyCd = null;
    /** 支店コード */
    protected String shitenCd = null;
    /** 営業所コード */
    protected String eigyosyoCd;
    /** MR属性１コード */
    protected String mrAttributeCd1 = null;
    /** MR属性２コード */
    protected String mrAttributeCd2 = null;
    /** デフォルト画像コード */
    protected String pictureCd = null;
    /** 画像ファイル内容 */
    protected String picture = null;
    /** 氏名 */
    protected String name = null;
    /** 氏名(カナ) */
    protected String nameKana = null;
    /** 入社年 */
    protected int nyusyaYear = 0;
    /** パスワード */
    protected String password = null;
    /** マスターフラグ */
    protected String masterFlg = null;
    /** 支店名称 */
    protected String shitenName = null;
    /** 営業所名称 */
    protected String eigyosyoName = null;
    /** MR属性1名称 */
    protected String mrAttributeName1 = null;
    /** MR属性2名称 */
    protected String mrAttributeName2 = null;
    /** マスター権限範囲(組織) */
    protected String masterKengenSoshiki = null;
    /** マスター権限範囲(属性) */
    protected String masterKengenAttribute = null;
    /** TEL */
    protected String telNo = null;
    /** FAX */
    protected String faxNo = null;

    /**
     * MrInfoオブジェクトを新規に作成する。
     */
    public MrInfo() {
    }

    public Hashtable getDaikou()
    {
        return this.daikou;
    }
    public void setDaikou(String key, Object value) {
        this.daikou.put(key,value);
    }

    /**
     * ＭＲ－ＩＤを設定する。
     * @param mrId ＭＲ－ＩＤ
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
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
        this.companyCd = companyCd;
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
        this.shitenCd = shitenCd;
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
        this.eigyosyoCd = eigyosyoCd;
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
        this.mrAttributeCd1 = mrAttributeCd1;
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
        this.mrAttributeCd2 = mrAttributeCd2;
    }

    /**
     * ＭＲ属性２コードを取得する。
     * @return ＭＲ属性２コード
     */
    public String getMrAttributeCd2() {
        return mrAttributeCd2;
    }

    /**
     * デフォルト画像コードを設定する。
     * @param pictureCd デフォルト画像コード
     */
    public void setPictureCd(String pictureCd) {
        this.pictureCd = pictureCd;
    }

    /**
     * デフォルト画像コードを取得する。
     * @return デフォルト画像コード
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * デフォルト画像コードを設定する。
     * @param pictureCd デフォルト画像コード
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * デフォルト画像コードを取得する。
     * @return デフォルト画像コード
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 氏名を設定する。
     * @param name 氏名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 氏名を取得する。
     * @return 氏名
     */
    public String getName() {
        return name;
    }

    /**
     * 氏名（カナ）を設定する。
     * @param nameKana 氏名（カナ）
     */
    public void setNameKana(String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     * 氏名（カナ）を取得する。
     * @return 氏名（カナ）
     */
    public String getNameKana() {
        return nameKana;
    }

    /**
     * 入社年を設定する。
     * @param nyusyaYear 入社年
     */
    public void setNyusyaYear(int nyusyaYear) {
        this.nyusyaYear = nyusyaYear;
    }

    /**
     * 入社年を取得する。
     * @return 入社年
     */
    public int getNyusyaYear() {
        return nyusyaYear;
    }

    /**
     * パスワードを設定する。
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * パスワードを取得する。
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * マスターフラグを設定する。
     * @param masterFlg マスターフラグ
     */
    public void setMasterFlg(String masterFlg) {
        this.masterFlg = masterFlg;
    }

    /**
     * マスターフラグを取得する。
     * @return マスターフラグ
     */
    public String getMasterFlg() {
        return masterFlg;
    }

    /**
     * 支店名称を設定する。
     * @param shitenName 支店名称
     */
    public void setShitenName(String shitenName) {
        this.shitenName = shitenName;
    }

    /**
     * 支店名称を取得する。
     * @return 支店名称
     */
    public String getShitenName() {
        return shitenName;
    }

    /**
     * 営業所名称を設定する。
     * @param eigyosyoName 営業所名称
     */
    public void setEigyosyoName(String eigyosyoName) {
        this.eigyosyoName = eigyosyoName;
    }

    /**
     * 営業所名称を取得する。
     * @return 営業所名称
     */
    public String getEigyosyoName() {
        return eigyosyoName;
    }

    /**
     * ＭＲ属性１名称を設定する。
     * @param mrAttributeName1 ＭＲ属性１名称
     */
    public void setMrAttributeName1(String mrAttributeName1) {
        this.mrAttributeName1 = mrAttributeName1;
    }

    /**
     * ＭＲ属性１名称を取得する。
     * @return ＭＲ属性１名称
     */
    public String getMrAttributeName1() {
        return mrAttributeName1;
    }

    /**
     * ＭＲ属性２名称を設定する。
     * @param mrAttributeName1 ＭＲ属性２名称
     */
    public void setMrAttributeName2(String mrAttributeName2) {
        this.mrAttributeName2 = mrAttributeName2;
    }

    /**
     * ＭＲ属性２名称を取得する。
     * @return ＭＲ属性２名称
     */
    public String getMrAttributeName2() {
        return mrAttributeName2;
    }

    /**
     * マスター権限範囲(組織)を取得する。
     * @return マスター権限範囲(組織)
     */
    public String getMasterKengenSoshiki() {
        return masterKengenSoshiki;
    }

    /**
     * マスター権限範囲(組織)を設定する。
     * @param kengen マスター権限範囲(組織)
     */
    public void setMasterKengenSoshiki(String kengen) {
        this.masterKengenSoshiki = kengen;
    }

    /**
     * マスター権限範囲(属性)を取得する。
     * @return マスター権限範囲(属性)
     */
    public String getMasterKengenAttribute() {
        return masterKengenAttribute;
    }

    /**
     * マスター権限範囲(属性)を設定する。
     * @param kengen マスター権限範囲(属性)
     */
    public void setMasterKengenAttribute(String kengen) {
        this.masterKengenAttribute = kengen;
    }

    /**
     * 電話番号を取得する。
     * @return 電話番号
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * 電話番号を設定する。
     * @param no 電話番号
     */
    public void setTelNo(String no) {
        telNo = no;
    }

    /**
     * 電話番号を取得する。
     * @return 電話番号
     */
    public String getFaxNo() {
        return faxNo;
    }

    /**
     * 電話番号を設定する。
     * @param no 電話番号
     */
    public void setFaxNo(String no) {
        faxNo = no;
    }

    /**
     * 文字列化する。
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("mr_id=" + mrId + "\n");
        me.append("company_cd=" + companyCd + "\n");
        me.append("shiten_cd=" + shitenCd + "\n");
        me.append("eigyosyo_cd=" + eigyosyoCd + "\n");
        me.append("mr_attribute_cd1=" + mrAttributeCd1 + "\n");
        me.append("mr_attribute_cd2=" + mrAttributeCd2 + "\n");
        me.append("picture_cd=" + pictureCd + "\n");
        me.append("picture=" + picture + "\n");
        me.append("name=" + name + "\n");
        me.append("name_kana=" + nameKana + "\n");
        me.append("nyusya_year=" + nyusyaYear + "\n");
        me.append("password=" + password + "\n");
        me.append("master_flg=" + masterFlg + "\n");
        me.append("shiten_name=" + shitenName + "\n");
        me.append("eigyosyo_name=" + eigyosyoName + "\n");
        me.append("mr_attribute_name1=" + mrAttributeName1 + "\n");
        me.append("mr_attribute_name2=" + mrAttributeName2 + "\n");
        me.append("master_kengen_soshiki=" + masterKengenSoshiki + "\n");
        me.append("master_kengen_attribute=" + masterKengenAttribute + "\n");
        me.append("tel_no=" + telNo + "\n");
        me.append("fax_no=" + faxNo + "\n");
        return me.toString();
    }
}
