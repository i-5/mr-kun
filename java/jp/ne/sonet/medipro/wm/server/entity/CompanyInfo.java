package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>��Џ��</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CompanyInfo {
    protected String companyCd;		// ��ЃR�[�h
    protected String companyKbn;	// ��Ћ敪
    protected String companyKbnNaiyo;	// ��Ћ敪���e
    protected String companyName;	// ��Ж�
    protected String cdPrefix;		// �R�[�hprefix
    protected String pictureCd;		// �f�t�H���g�摜�R�[�h
    protected String linkCd;		// �f�t�H���g�����N�R�[�h
    protected String targetRank;	// �^�[�Q�b�g�����N
    protected String displayRanking;	// ��ʕ\��

    /**
     * CompanyInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public CompanyInfo() {
    }

    /**
     * ��ЃR�[�h��ݒ肷��B
     * @param arg ��ЃR�[�h
     */
    public void setCompanyCd(String arg) {
        companyCd = arg;
    }

    /**
     * ��ЃR�[�h���擾����B
     * @return ��ЃR�[�h
     */
    public String getCompanyCd() {
        return companyCd;
    }

    /**
     * ��Ћ敪��ݒ肷��B
     * @param arg ��Ћ敪
     */
    public void setCompanyKbn(String arg) {
        companyKbn = arg;
    }

    /**
     * ��Ћ敪���擾����B
     * @return ��Ћ敪
     */
    public String getCompanyKbn() {
        return companyKbn;
    }

    /**
     * ��Ћ敪���e��ݒ肷��B
     * @param arg ��Ћ敪���e
     */
    public void setCompanyKbnNaiyo(String arg) {
        companyKbnNaiyo = arg;
    }

    /**
     * ��Ћ敪���e���擾����B
     * @return ��Ћ敪���e
     */
    public String getCompanyKbnNaiyo() {
        return companyKbnNaiyo;
    }

    /**
     * ��Ж���ݒ肷��B
     * @param arg ��Ж�
     */
    public void setCompanyName(String arg) {
        companyName = arg;
    }

    /**
     * ��Ж����擾����B
     * @return ��Ж�
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * �R�[�hprefix��ݒ肷��B
     * @param arg �R�[�hprefix
     */
    public void setCdPrefix(String arg) {
        cdPrefix = arg;
    }

    /**
     * �R�[�hprefix���擾����B
     * @return �R�[�hprefix
     */
    public String getCdPrefix() {
        return cdPrefix;
    }

    /**
     * �f�t�H���g�摜�R�[�h��ݒ肷��B
     * @param arg �f�t�H���g�摜�R�[�h
     */
    public void setPictureCd(String arg) {
        pictureCd = arg;
    }

    /**
     * �f�t�H���g�摜�R�[�h���擾����B
     * @return �f�t�H���g�摜�R�[�h
     */
    public String getPictureCd() {
        return pictureCd;
    }

    /**
     * �f�t�H���g�����N�R�[�h��ݒ肷��B
     * @param arg �f�t�H���g�����N�R�[�h
     */
    public void setLinkCd(String arg) {
        linkCd = arg;
    }

    /**
     * �f�t�H���g�����N�R�[�h���擾����B
     * @return �f�t�H���g�����N�R�[�h
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
     * �����L���O�\����ݒ肷��B
     * @param arg �����L���O�\���ݒ�
     */
    public void setDisplayRanking(String arg) {
        displayRanking = arg;
    }

    /**
     * �����L���O�\���ݒ���擾����B
     * @return �����L���O�\���ݒ�
     */
    public String getDisplayRanking() {
        return displayRanking;
    }

    /**
     * �����񉻂���B
     * @return ������
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
