package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <strong>��^�����C�u�������</strong>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfo {
    /** ��^���R�[�h */
    protected String teikeibunCd = null;
    /** ��ЃR�[�h */
    protected String companyCd = null;
    /** �^�C�g�� */
    protected String title = null;
    /** �{�� */
    protected String honbun = null;

    /**
     * ExpressionLibInfo�I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public ExpressionLibInfo() {
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
