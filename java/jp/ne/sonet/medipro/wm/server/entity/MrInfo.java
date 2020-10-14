package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>�l�q���</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class MrInfo {
    // hb010716 added this for ��s�z�M
    protected Hashtable daikou = new Hashtable();
    /** MR-ID */
    protected String mrId = null;
    /** ��ЃR�[�h */
    protected String companyCd = null;
    /** �x�X�R�[�h */
    protected String shitenCd = null;
    /** �c�Ə��R�[�h */
    protected String eigyosyoCd;
    /** MR�����P�R�[�h */
    protected String mrAttributeCd1 = null;
    /** MR�����Q�R�[�h */
    protected String mrAttributeCd2 = null;
    /** �f�t�H���g�摜�R�[�h */
    protected String pictureCd = null;
    /** �摜�t�@�C�����e */
    protected String picture = null;
    /** ���� */
    protected String name = null;
    /** ����(�J�i) */
    protected String nameKana = null;
    /** ���ДN */
    protected int nyusyaYear = 0;
    /** �p�X���[�h */
    protected String password = null;
    /** �}�X�^�[�t���O */
    protected String masterFlg = null;
    /** �x�X���� */
    protected String shitenName = null;
    /** �c�Ə����� */
    protected String eigyosyoName = null;
    /** MR����1���� */
    protected String mrAttributeName1 = null;
    /** MR����2���� */
    protected String mrAttributeName2 = null;
    /** �}�X�^�[�����͈�(�g�D) */
    protected String masterKengenSoshiki = null;
    /** �}�X�^�[�����͈�(����) */
    protected String masterKengenAttribute = null;
    /** TEL */
    protected String telNo = null;
    /** FAX */
    protected String faxNo = null;

    /**
     * MrInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
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
     * �l�q�|�h�c��ݒ肷��B
     * @param mrId �l�q�|�h�c
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /**
     * �l�q�|�h�c���擾����B
     * @return �l�q�|�h�c
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * ��ЃR�[�h��ݒ肷��B
     * @param companyCd ��ЃR�[�h
     */
    public void setCompanyCd(String companyCd) {
        this.companyCd = companyCd;
    }

    /**
     * ��ЃR�[�h���擾����B
     * @return ��ЃR�[�h
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * �x�X�R�[�h��ݒ肷��B
     * @param shitenCd �x�X�R�[�h
     */
    public void setShitenCd(String shitenCd) {
        this.shitenCd = shitenCd;
    }

    /**
     * �x�X�R�[�h���擾����B
     * @return �x�X�R�[�h
     */
    public String getShitenCd() {
        return shitenCd;
    }

    /**
     * �c�Ə��R�[�h��ݒ肷��B
     * @param eigyosyoCd �c�Ə��R�[�h
     */
    public void setEigyosyoCd(String eigyosyoCd) {
        this.eigyosyoCd = eigyosyoCd;
    }

    /**
     * �c�Ə��R�[�h���擾����B
     * @return �c�Ə��R�[�h
     */
    public String getEigyosyoCd() {
        return eigyosyoCd;
    }

    /**
     * �l�q�����P�R�[�h��ݒ肷��B
     * @param mrAttributeCd1 �l�q�����P�R�[�h
     */
    public void setMrAttributeCd1(String mrAttributeCd1) {
        this.mrAttributeCd1 = mrAttributeCd1;
    }

    /**
     * �l�q�����P�R�[�h���擾����B
     * @return �l�q�����P�R�[�h
     */
    public String getMrAttributeCd1() {
        return mrAttributeCd1;
    }

    /**
     * �l�q�����Q�R�[�h��ݒ肷��B
     * @param mrAttributeCd1 �l�q�����Q�R�[�h
     */
    public void setMrAttributeCd2(String mrAttributeCd2) {
        this.mrAttributeCd2 = mrAttributeCd2;
    }

    /**
     * �l�q�����Q�R�[�h���擾����B
     * @return �l�q�����Q�R�[�h
     */
    public String getMrAttributeCd2() {
        return mrAttributeCd2;
    }

    /**
     * �f�t�H���g�摜�R�[�h��ݒ肷��B
     * @param pictureCd �f�t�H���g�摜�R�[�h
     */
    public void setPictureCd(String pictureCd) {
        this.pictureCd = pictureCd;
    }

    /**
     * �f�t�H���g�摜�R�[�h���擾����B
     * @return �f�t�H���g�摜�R�[�h
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * �f�t�H���g�摜�R�[�h��ݒ肷��B
     * @param pictureCd �f�t�H���g�摜�R�[�h
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * �f�t�H���g�摜�R�[�h���擾����B
     * @return �f�t�H���g�摜�R�[�h
     */
    public String getPicture() {
        return picture;
    }

    /**
     * ������ݒ肷��B
     * @param name ����
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * �������擾����B
     * @return ����
     */
    public String getName() {
        return name;
    }

    /**
     * �����i�J�i�j��ݒ肷��B
     * @param nameKana �����i�J�i�j
     */
    public void setNameKana(String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     * �����i�J�i�j���擾����B
     * @return �����i�J�i�j
     */
    public String getNameKana() {
        return nameKana;
    }

    /**
     * ���ДN��ݒ肷��B
     * @param nyusyaYear ���ДN
     */
    public void setNyusyaYear(int nyusyaYear) {
        this.nyusyaYear = nyusyaYear;
    }

    /**
     * ���ДN���擾����B
     * @return ���ДN
     */
    public int getNyusyaYear() {
        return nyusyaYear;
    }

    /**
     * �p�X���[�h��ݒ肷��B
     * @param password �p�X���[�h
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * �p�X���[�h���擾����B
     * @return �p�X���[�h
     */
    public String getPassword() {
        return password;
    }

    /**
     * �}�X�^�[�t���O��ݒ肷��B
     * @param masterFlg �}�X�^�[�t���O
     */
    public void setMasterFlg(String masterFlg) {
        this.masterFlg = masterFlg;
    }

    /**
     * �}�X�^�[�t���O���擾����B
     * @return �}�X�^�[�t���O
     */
    public String getMasterFlg() {
        return masterFlg;
    }

    /**
     * �x�X���̂�ݒ肷��B
     * @param shitenName �x�X����
     */
    public void setShitenName(String shitenName) {
        this.shitenName = shitenName;
    }

    /**
     * �x�X���̂��擾����B
     * @return �x�X����
     */
    public String getShitenName() {
        return shitenName;
    }

    /**
     * �c�Ə����̂�ݒ肷��B
     * @param eigyosyoName �c�Ə�����
     */
    public void setEigyosyoName(String eigyosyoName) {
        this.eigyosyoName = eigyosyoName;
    }

    /**
     * �c�Ə����̂��擾����B
     * @return �c�Ə�����
     */
    public String getEigyosyoName() {
        return eigyosyoName;
    }

    /**
     * �l�q�����P���̂�ݒ肷��B
     * @param mrAttributeName1 �l�q�����P����
     */
    public void setMrAttributeName1(String mrAttributeName1) {
        this.mrAttributeName1 = mrAttributeName1;
    }

    /**
     * �l�q�����P���̂��擾����B
     * @return �l�q�����P����
     */
    public String getMrAttributeName1() {
        return mrAttributeName1;
    }

    /**
     * �l�q�����Q���̂�ݒ肷��B
     * @param mrAttributeName1 �l�q�����Q����
     */
    public void setMrAttributeName2(String mrAttributeName2) {
        this.mrAttributeName2 = mrAttributeName2;
    }

    /**
     * �l�q�����Q���̂��擾����B
     * @return �l�q�����Q����
     */
    public String getMrAttributeName2() {
        return mrAttributeName2;
    }

    /**
     * �}�X�^�[�����͈�(�g�D)���擾����B
     * @return �}�X�^�[�����͈�(�g�D)
     */
    public String getMasterKengenSoshiki() {
        return masterKengenSoshiki;
    }

    /**
     * �}�X�^�[�����͈�(�g�D)��ݒ肷��B
     * @param kengen �}�X�^�[�����͈�(�g�D)
     */
    public void setMasterKengenSoshiki(String kengen) {
        this.masterKengenSoshiki = kengen;
    }

    /**
     * �}�X�^�[�����͈�(����)���擾����B
     * @return �}�X�^�[�����͈�(����)
     */
    public String getMasterKengenAttribute() {
        return masterKengenAttribute;
    }

    /**
     * �}�X�^�[�����͈�(����)��ݒ肷��B
     * @param kengen �}�X�^�[�����͈�(����)
     */
    public void setMasterKengenAttribute(String kengen) {
        this.masterKengenAttribute = kengen;
    }

    /**
     * �d�b�ԍ����擾����B
     * @return �d�b�ԍ�
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * �d�b�ԍ���ݒ肷��B
     * @param no �d�b�ԍ�
     */
    public void setTelNo(String no) {
        telNo = no;
    }

    /**
     * �d�b�ԍ����擾����B
     * @return �d�b�ԍ�
     */
    public String getFaxNo() {
        return faxNo;
    }

    /**
     * �d�b�ԍ���ݒ肷��B
     * @param no �d�b�ԍ�
     */
    public void setFaxNo(String no) {
        faxNo = no;
    }

    /**
     * �����񉻂���B
     * @return ������
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
