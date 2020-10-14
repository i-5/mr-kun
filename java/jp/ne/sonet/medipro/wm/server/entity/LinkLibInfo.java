package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>�����N���C�u�������</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkLibInfo {
	protected String linkCd;			// �����N�R�[�h
	protected String companyCd;			// ��ЃR�[�h
	protected String linkBunruiCd;		// �����N���ރR�[�h
	protected String linkBunruiName;	// �����N���ޖ�
	protected String description;		// added by hb010914
	protected String url;				// �t�q�k
	protected String honbunText;		// �{���e�L�X�g
	protected String picture;			// �摜
	protected String naigaiLinkKbn;		// ���O�����N�敪

    /**
     * LinkLibInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public LinkLibInfo() {
    }

    /**
     * �����N�R�[�h��ݒ肷��B
     * @param arg �����N�R�[�h
     */
    public void setLinkCd(String arg) {
        linkCd = arg;
    }

    /**
     * �����N�R�[�h���擾����B
     * @return �����N�R�[�h
     */
    public String getLinkCd() {
        return linkCd;
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
     * �����N���ރR�[�h��ݒ肷��B
     * @param arg �����N���ރR�[�h
     */
    public void setLinkBunruiCd(String arg) {
       linkBunruiCd = arg;
    }

    /**
     * �����N���ރR�[�h���擾����B
     * @return �����N���ރR�[�h
     */
    public String getLinkBunruiCd() {
        return linkBunruiCd;
    }

    /**
     * �����N���ޖ���ݒ肷��B
     * @param name �����N���ޖ�
     */
    public void setLinkBunruiName(String arg) {
        linkBunruiName = arg;
    }

    /**
     * �����N���ޖ����擾����B
     * @return �����N���ޖ�
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
     * �t�q�k��ݒ肷��B
     * @param arg �t�q�k
     */
    public void setUrl(String arg) {
        url = arg;
    }

    /**
     * �t�q�k���擾����B
     * @return �t�q�k
     */
    public String getUrl() {
        return url;
    }

    /**
     * �{���e�L�X�g��ݒ肷��B
     * @param arg �{���e�L�X�g
     */
    public void setHonbunText(String arg) {
        honbunText = arg;
    }

    /**
     * �{���e�L�X�g���擾����B
     * @return �{���e�L�X�g
     */
    public String getHonbunText() {
        return honbunText;
    }

    /**
     * �摜��ݒ肷��B
     * @param arg �摜
     */
    public void setPicture(String arg) {
        picture = arg;
    }

    /**
     * �摜���擾����B
     * @return �摜
     */
    public String getPicture() {
        return picture;
    }

    /**
     * ���O�����N�敪��ݒ肷��B
     * @param arg ���O�����N�敪
     */
    public void setNaigaiLinkKbn(String arg) {
        naigaiLinkKbn = arg;
    }

    /**
     * ���O�����N�敪���擾����B
     * @return ���O�����N�敪
     */
    public String getNaigaiLinkKbn() {
        return naigaiLinkKbn;
    }

    /**
     * �����񉻂���B
     * @return ������
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
