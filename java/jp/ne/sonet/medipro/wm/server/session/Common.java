package jp.ne.sonet.medipro.wm.server.session;

import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>���O�C���� and �萔���N���X�B</strong>
 * @author
 * @version
 */
public class Common {
    /** MR-ID */
    private String mrId = new String();
    /** ��ЃR�[�h */
    private String companyCd = new String();
    /** �x�X�R�[�h */
    private String shitenCd = new String();
    /** �c�Ə��R�[�h */
    private String eigyosyoCd = new String();
    /** MR����1�R�[�h */
    private String mrAttributeCd1 = new String();
    /** MR����2�R�[�h */
    private String mrAttributeCd2 = new String();
    /** �}�X�^�[�t���O */
    private String masterFlg = new String();
    /** �}�X�^�[�����͈�(�g�D) */
    private String masterKengenSoshiki = new String();
    /** �}�X�^�[�����͈�(����) */
    private String masterKengenAttribute = new String();
    /** �L���b�`�摜�z�[�� */
    private String catchHome = new String();
    /** �L���b�`�摜�ꎞ�ۊ�Directory */
    private String tempDir = new String();
    /** WebServer�h�L�������g���[�g */
    private String documentRoot = new String();
    /** �^�C���A�E�g */
    private int timeout = 0;

    /** MR�ꗗ�\���� */
    private int mrLine = 0;
    /** MR�L���b�`�摜�ꗗ�\���� */
    private int mrCatchLine = 0;
    /** �x�X�ꗗ�\���� */
    private int shitenLine = 0;
    /** �����ꗗ�\���� */
    private int attributeLine = 0;
    /** �T�u�}�X�^�[�ꗗ�\���� */
    private int subLine = 0;
    /** �L���b�`�摜�ꗗ�\���� */
    private int catchLine = 0;
    /** �����N�ꗗ�\���� */
    private int linkLine = 0;
    /** ��^���ꗗ�\���� */
    private int bunLine = 0;
    /** �R�[�����e�ꗗ�\���� */
    private int callLine = 0;

    /**
     * Common�𐶐����܂��B
     */
    public Common() {
        if (SysCnst.DEBUG) {
            System.err.println("Common is created");
        }
    }

    /**
     * �l�q�|�h�c��ݒ肷��B
     * @param mrId �l�q�|�h�c
     */
    public void setMrId(String mrId) {
        if (mrId == null) {
            this.mrId = "";
        } else {
            this.mrId = mrId;
        }
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
        if (companyCd == null) {
            this.companyCd = "";
        } else {
            this.companyCd = companyCd;
        }
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
        if (shitenCd == null) {
            this.shitenCd = "";
        } else {
            this.shitenCd = shitenCd;
        }
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
        if (eigyosyoCd == null) {
            this.eigyosyoCd = "";
        } else {
            this.eigyosyoCd = eigyosyoCd;
        }
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
        if (mrAttributeCd1 == null) {
            this.mrAttributeCd1 = "";
        } else {
            this.mrAttributeCd1 = mrAttributeCd1;
        }
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
        if (mrAttributeCd2 == null) {
            this.mrAttributeCd2 = "";
        } else {
            this.mrAttributeCd2 = mrAttributeCd2;
        }
    }

    /**
     * �l�q�����Q�R�[�h���擾����B
     * @return �l�q�����Q�R�[�h
     */
    public String getMrAttributeCd2() {
        return mrAttributeCd2;
    }

    /**
     * �}�X�^�[�t���O��ݒ肷��B
     * @param masterFlg �}�X�^�[�t���O
     */
    public void setMasterFlg(String masterFlg) {
        if (masterFlg == null) {
            this.masterFlg = "";
        } else {
            this.masterFlg = masterFlg;
        }
    }

    /**
     * �}�X�^�[�t���O���擾����B
     * @return �}�X�^�[�t���O
     */
    public String getMasterFlg() {
        return masterFlg;
    }

    public void setMasterKengenSoshiki(String kengen) {
        if (kengen == null) {
            this.masterKengenSoshiki = "";
        } else {
            this.masterKengenSoshiki = kengen;
        }
    }

    public String getMasterKengenSoshiki() {
        return masterKengenSoshiki;
    }
  
    public void setMasterKengenAttribute(String kengen) {
        if (kengen == null) {
            this.masterKengenAttribute = "";
        } else {
            this.masterKengenAttribute = kengen;
        }
    }

    public String getMasterKengenAttribute() {
        return masterKengenAttribute;
    }

    public String getCatchHome() {
        return catchHome;
    }

    public void setCatchHome(String home) {
        catchHome = home;
    }

    public String getTempDir() {
        return catchHome + tempDir;
    }

    public void setTempDir(String dir) {
        tempDir = dir;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String dir) {
        documentRoot = dir;
    }

    public int getTimeout() {
	return timeout;
    }

    public void setTimeout(int time) {
	timeout = time;
    }

    public int getMrLine() {
        return mrLine;
    }

    public void setMrLine(int num) {
        this.mrLine = num;
    }

    public int getMrCatchLine() {
        return mrCatchLine;
    }

    public void setMrCatchLine(int num) {
        this.mrCatchLine = num;
    }

    public int getShitenLine() {
        return shitenLine;
    }

    public void setShitenLine(int num) {
        this.shitenLine = num;
    }

    public int getAttributeLine() {
        return attributeLine;
    }

    public void setAttributeLine(int num) {
        this.attributeLine = num;
    }

    public int getSubLine() {
        return subLine;
    }

    public void setSubLine(int num) {
        this.subLine = num;
    }

    public int getCatchLine() {
        return catchLine;
    }

    public void setCatchLine(int num) {
        this.catchLine = num;
    }

    public int getLinkLine() {
        return linkLine;
    }

    public void setLinkLine(int num) {
        this.linkLine = num;
    }

    public int getBunLine() {
        return bunLine;
    }

    public void setBunLine(int num) {
        this.bunLine = num;
    }

    public int getCallLine() {
        return callLine;
    }

    public void setCallLine(int num) {
        this.callLine = num;
    }

}
