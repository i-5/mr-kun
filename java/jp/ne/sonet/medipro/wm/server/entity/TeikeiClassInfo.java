package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>�����N���ޏ��</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class TeikeiClassInfo {
	protected String bunruiCode;		// �����N���ރR�[�h
	protected String bunruiName;		// ���ޖ�
	protected String companyCd;			// ��ЃR�[�h

    /**
     * LinkClassInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public TeikeiClassInfo() {
    }

    /**
     * �����N���ރR�[�h��ݒ肷��B
     * @param arg �����N���ރR�[�h
     */
    public void setBunruiCode(String arg) {
        bunruiCode = arg;
    }

    /**
     * �����N���ރR�[�h���擾����B
     * @return �����N���ރR�[�h
     */
    public String getBunruiCode() {
        return bunruiCode;
    }

    /**
     * ���ޖ���ݒ肷��B
     * @param arg ���ޖ�
     */
    public void setBunruiName(String arg) {
        bunruiName = arg;
    }

    /**
     * ���ޖ����擾����B
     * @return ���ޖ�
     */
    public String getBunruiName() {
        return bunruiName;
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
     * �����񉻂���B
     * @return ������
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append("teikeibun_bunrui_cd=" + bunruiCode + "\n");
        me.append("bunrui_name=" + bunruiName + "\n");
        me.append("company_cd=" + companyCd + "\n");
        return me.toString();
    }
}
