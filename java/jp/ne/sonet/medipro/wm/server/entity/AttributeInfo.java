package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>�l�q�������</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeInfo {
	protected String mrAttributeCd;		// �l�q�����R�[�h
	protected String mrAttributeName;	// �l�q������
	protected String companyCd;			// ��ЃR�[�h

    /**
     * AttributeInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public AttributeInfo() {
    }

    /**
     * �l�q�����R�[�h��ݒ肷��B
     * @param arg �l�q�����R�[�h
     */
    public void setMrAttributeCd(String arg) {
        mrAttributeCd = arg;
    }

    /**
     * �l�q�����R�[�h���擾����B
     * @return �l�q�����R�[�h
     */
    public String getMrAttributeCd() {
        return mrAttributeCd;
    }

    /**
     * �l�q��������ݒ肷��B
     * @param arg �l�q������
     */
    public void setMrAttributeName(String arg) {
        mrAttributeName = arg;
    }

    /**
     * �l�q���������擾����B
     * @return �l�q������
     */
    public String getMrAttributeName() {
        return mrAttributeName;
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
        me.append("mr_attribute_cd=" + mrAttributeCd + "\n");
        me.append("mr_attribute_name=" + mrAttributeName + "\n");
        me.append("company_cd=" + companyCd + "\n");
        return me.toString();
    }
}
