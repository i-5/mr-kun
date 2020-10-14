package jp.ne.sonet.medipro.wm.server.session;

import java.util.Enumeration;
import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.WmException;

import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MR�̒ǉ��E�ύX��ʗp�Z�b�V�������N���X</strong>.
 * @author
 * @version
 */
public class MrUpdateSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** �ʏ� */
    public static final int NORMAL = 0;
    /** �ۑ��m�F */
    public static final int SAVE_CONFIRM = 1;
    /** �ۑ����� */
    public static final int SAVE_DONE = 2;
    /** �J�i��������� */
    public static final int NAMEKANA_NOINPUT = 3;
    /** ��������� */
    public static final int NAME_NOINPUT = 4;
    /** �p�X���[�h����� */
    public static final int PASSWORD_NOINPUT = 5;
    /** ���l�łȂ� */
    public static final int NUMBER_FORMAT_ERROR = 6;
	// 2001.03.09 Y.Yama ��ƈ˗��� MRK00005�ɑΉ� add
	/** MR ID�ɋ󔒂��܂܂�Ă��� */
	public static final int INNER_SPACE_ERROR = 7;
	// add end
	// 2001.03.19 Y.Yama ��ƈ˗��� MRK00009�ɑΉ� add
	/** MR ID�Ƀ^�u���܂܂�Ă��� */
	public static final int INNER_TAB_ERROR = 8;

    //////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** MR��� */
    private MrInfo mrInfo = null;
    /** ���ДN(String) */
    private String nyusyaYear = "";
    /** MR��ID�����DB����ǂݍ��ނ��������t���O */
    private boolean loadFlag = false;
    /** �x�X�� �� �x�XCD ���X�g*/
    private Hashtable shitenList = null;
    /** �c�Ə��� �� �c�Ə�CD ���X�g */
    private Hashtable eigyosyoList = null;
    /** ������ �� ����CD ���X�g */
    private Hashtable attributeList = null;
    /** ���݂�status */
    private int status = NORMAL;
    /** �g�D(�x�X or �c�Ə�)���ύX���ꂽ���Ƃ������t���O */
    private boolean soshikiDeprivation = false;
    /** ����(����1 or ����2)���ύX���ꂽ���Ƃ������t���O */
    private boolean attributeDeprivation = false;
    
    private boolean adding = true;

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MR�ǉ��E�X�V�p�̃Z�b�V�����I�u�W�F�N�g�𐶐����܂�.
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
     * �y�[�W�ȊO�̏����N���A����B
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
     * ���݂�status���擾����B
     * @return status
     */
    public int getStatus() {
	return status;
    }

    /**
     * ���݂�status��ݒ肷��B
     * @param status �ݒ�status
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

