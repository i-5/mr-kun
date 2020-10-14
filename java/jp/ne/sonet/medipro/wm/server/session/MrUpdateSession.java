package jp.ne.sonet.medipro.wm.server.session;

import java.util.Enumeration;
import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.WmException;

import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MRの追加・変更画面用セッション情報クラス</strong>.
 * @author
 * @version
 */
public class MrUpdateSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** 通常 */
    public static final int NORMAL = 0;
    /** 保存確認 */
    public static final int SAVE_CONFIRM = 1;
    /** 保存完了 */
    public static final int SAVE_DONE = 2;
    /** カナ氏名非入力 */
    public static final int NAMEKANA_NOINPUT = 3;
    /** 氏名非入力 */
    public static final int NAME_NOINPUT = 4;
    /** パスワード非入力 */
    public static final int PASSWORD_NOINPUT = 5;
    /** 数値でない */
    public static final int NUMBER_FORMAT_ERROR = 6;
	// 2001.03.09 Y.Yama 作業依頼書 MRK00005に対応 add
	/** MR IDに空白が含まれている */
	public static final int INNER_SPACE_ERROR = 7;
	// add end
	// 2001.03.19 Y.Yama 作業依頼書 MRK00009に対応 add
	/** MR IDにタブが含まれている */
	public static final int INNER_TAB_ERROR = 8;

    //////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** MR情報 */
    private MrInfo mrInfo = null;
    /** 入社年(String) */
    private String nyusyaYear = "";
    /** MRのIDを基にDBから読み込むかを示すフラグ */
    private boolean loadFlag = false;
    /** 支店名 → 支店CD リスト*/
    private Hashtable shitenList = null;
    /** 営業所名 → 営業所CD リスト */
    private Hashtable eigyosyoList = null;
    /** 属性名 → 属性CD リスト */
    private Hashtable attributeList = null;
    /** 現在のstatus */
    private int status = NORMAL;
    /** 組織(支店 or 営業所)が変更されたことを示すフラグ */
    private boolean soshikiDeprivation = false;
    /** 属性(属性1 or 属性2)が変更されたことを示すフラグ */
    private boolean attributeDeprivation = false;
    
    private boolean adding = true;

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MR追加・更新用のセッションオブジェクトを生成します.
     */
    public MrUpdateSession() {
	mrInfo = new MrInfo();
	shitenList = new Hashtable();
	eigyosyoList = new Hashtable();
	attributeList = new Hashtable();

	if (SysCnst.DEBUG) {
	    System.err.println("MrUpdateSession is created");
	}
    }

    /**
     * ページ以外の情報をクリアする。
     */
    public void clear() {
	setStatus(NORMAL);
    }

    public boolean isAdding() {
	return adding;
    }

    public void setAdding(boolean flag) {
	adding = flag;
    }

    /**
     *
     */
    public boolean isSoshikiDeprivation() {
	return soshikiDeprivation;
    }

    /**
     *
     */
    public void setSoshikiDeprivation(boolean flag) {
	soshikiDeprivation = flag;
    }

    /**
     *
     */
    public boolean isAttributeDeprivation() {
	return attributeDeprivation;
    }

    /**
     *
     */
    public void setAttributeDeprivation(boolean flag) {
	attributeDeprivation = flag;
    }

    /**
     *
     */
    public void setShitenList(Hashtable table) {
	shitenList = table;
    }

    /**
     *
     */
    public void setEigyosyoList(Hashtable table) {
	eigyosyoList = table;
    }

    /**
     *
     */
    public void setAttributeList(Hashtable table) {
	attributeList = table;
    }

    /**
     *
     */
    public MrInfo getMrInfo() {
	return mrInfo;
    }

    /**
     *
     */
    public void setMrInfo(MrInfo info) {
	if (info != null) {
	    this.mrInfo = info;
	    nyusyaYear = new Integer(info.getNyusyaYear()).toString();
	} else {
	    this.mrInfo = new MrInfo();
	}
    }

    /**
     *
     */
    public boolean isNeedToLoad() {
	return loadFlag;
    }

    /**
     *
     */
    public void setLoadFlag(boolean flag) {
	loadFlag = flag;
    }

    /**
     *
     */

    /**
     * 現在のstatusを取得する。
     * @return status
     */
    public int getStatus() {
	return status;
    }

    /**
     * 現在のstatusを設定する。
     * @param status 設定status
     */
    public void setStatus(int status) {
	this.status = status;
    }

    /**
     *
     */
    public String getCompanyCd() {
	return (mrInfo.getCompanyCd() == null ? "" : mrInfo.getCompanyCd());
    }

    /**
     *
     */
    public void setCompanyCd(String val) {
	mrInfo.setCompanyCd(val);
    }
    
    /**
     *
     */
    public String getMrId() {
	return (mrInfo.getMrId() == null ? "" : mrInfo.getMrId());
    }

    /**
     *
     */
    public void setMrId(String val) {
	mrInfo.setMrId(val);
    }

    /**
     *
     */
    public String getNameKana() {
	return (mrInfo.getNameKana()  == null ? "" : mrInfo.getNameKana());
    }

    /**
     *
     */
    public void setNameKana(String val) {
	mrInfo.setNameKana(val);
    }

    /**
     *
     */
    public String getName() {
	return (mrInfo.getName() == null ? "" : mrInfo.getName());
    }

    /**
     *
     */
    public void setName(String val) {
	mrInfo.setName(val);
    }

    /**
     *
     */
    public String getShitenCd() {
	return mrInfo.getShitenCd();
    }

    /**
     *
     */
    public void setShitenCd(String val) {
	mrInfo.setShitenCd(val);
    }

    /**
     *
     */
    public String getShitenName() {
	return (mrInfo.getShitenName() == null ? "" : mrInfo.getShitenName());
    }

    /**
     *
     */
    public void setShitenName(String val) {
	mrInfo.setShitenName(val);
	if (val != null) {
	    setShitenCd((String)shitenList.get(val));
	}
    }

    /**
     *
     */
    public String getEigyosyoName() {
	return (mrInfo.getEigyosyoName() == null ? "" : mrInfo.getEigyosyoName()) ;
    }

    /**
     *
     */
    public void setEigyosyoName(String val) {
	mrInfo.setEigyosyoName(val);
	if (val != null) {
	    setEigyosyoCd((String)eigyosyoList.get(val));
	}
    }

    /**
     *
     */
    public String getEigyosyoCd() {
	return mrInfo.getEigyosyoCd();
    }

    /**
     *
     */
    public void setEigyosyoCd(String val) {
	mrInfo.setEigyosyoCd(val);
    }

    /**
     *
     */
    public String getMrAttributeName1() {
	return (mrInfo.getMrAttributeName1() == null ? "" : mrInfo.getMrAttributeName1());
    }

    /**
     *
     */
    public void setMrAttributeName1(String val) {
	mrInfo.setMrAttributeName1(val);
	if (val != null) {
	    setMrAttributeCd1((String)attributeList.get(val));
	}
    }

    /**
     *
     */
    public String getMrAttributeCd1() {
	return mrInfo.getMrAttributeCd1();
    }

    /**
     *
     */
    public void setMrAttributeCd1(String val) {
	mrInfo.setMrAttributeCd1(val);
    }

    /**
     *
     */
    public String getMrAttributeName2() {
	return (mrInfo.getMrAttributeName2() == null ? "" : mrInfo.getMrAttributeName2());
    }

    /**
     *
     */
    public void setMrAttributeName2(String val) {
	mrInfo.setMrAttributeName2(val);
	if (val != null) {
	    setMrAttributeCd2((String)attributeList.get(val));
	}
    }

    /**
     *
     */
    public String getMrAttributeCd2() {
	return mrInfo.getMrAttributeCd2();
    }

    /**
     *
     */
    public void setMrAttributeCd2(String val) {
	mrInfo.setMrAttributeCd2(val);
    }

    /**
     *
     */
    public String getNyusyaYear() {
	return nyusyaYear;
    }

    /**
     *
     */
    public void setNyusyaYear(String val) {
	nyusyaYear = val;
	try {
	    mrInfo.setNyusyaYear(new Integer(val).intValue());
	} catch (NumberFormatException ex) {
	}
    }

    /**
     *
     */
    public String getPassword() {
	return (mrInfo.getPassword() == null ? "" : mrInfo.getPassword());
    }

    /**
     *
     */
    public void setPassword(String val) {
	mrInfo.setPassword(val);
    }

    /**
     *
     */
    public String getPicture() {
	return mrInfo.getPicture();
    }

    /**
     *
     */
    public void setPicture(String val) {
	mrInfo.setPicture(val);
    }

    /**
     *
     */
    public String getPictureCd() {
	return (mrInfo.getPictureCd() == null ? "" : mrInfo.getPictureCd());
    }

    /**
     *
     */
    public void setPictureCd(String val) {
	mrInfo.setPictureCd(val);
    }
}

