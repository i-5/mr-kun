package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>��^�����C�u�������</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfo {
	protected String teikeibunCd;		// ��^���R�[�h
	protected String companyCd;			// ��ЃR�[�h
	protected String title;				// �^�C�g��
	protected String honbun;			// �{��
	protected String description;
	protected String bunruiCd;
	protected String bunruiName;

    /**
     * ExpressionLibInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public ExpressionLibInfo() {
    }

    /**
     * ��^���R�[�h��ݒ肷��B
     * @param arg ��^���R�[�h
     */
    public void setBunruiName(String arg) {
        bunruiName = arg;
    }

    /**
     * ��^���R�[�h���擾����B
     * @return ��^���R�[�h
     */
    public String getBunruiName() {
        return bunruiName;
    }

    /**
     * ��^���R�[�h��ݒ肷��B
     * @param arg ��^���R�[�h
     */
    public void setDescription(String arg) {
        description = arg;
    }

    /**
     * ��^���R�[�h���擾����B
     * @return ��^���R�[�h
     */
    public String getDescription() {
        return description;
    }

    /**
     * ��^���R�[�h��ݒ肷��B
     * @param arg ��^���R�[�h
     */
    public void setBunruiCode(String arg) {
        bunruiCd = arg;
    }

    /**
     * ��^���R�[�h���擾����B
     * @return ��^���R�[�h
     */
    public String getBunruiCode() {
        return bunruiCd;
    }


    /**
     * ��^���R�[�h��ݒ肷��B
     * @param arg ��^���R�[�h
     */
    public void setTeikeibunCd(String arg) {
        teikeibunCd = arg;
    }

    /**
     * ��^���R�[�h���擾����B
     * @return ��^���R�[�h
     */
    public String getTeikeibunCd() {
        return teikeibunCd;
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
     * �^�C�g����ݒ肷��B
     * @param arg �^�C�g��
     */
    public void setTitle(String arg) {
       title = arg;
    }

    /**
     * �^�C�g�����擾����B
     * @return �^�C�g��
     */
    public String getTitle() {
        return title;
    }

    /**
     * �{����ݒ肷��B
     * @param name �{��
     */
    public void setHonbun(String arg) {
        honbun = arg;
    }

    /**
     * �{�����擾����B
     * @return �{��
     */
    public String getHonbun() {
        return honbun;
    }

    /**
     * �����񉻂���B
     * @return ������
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
