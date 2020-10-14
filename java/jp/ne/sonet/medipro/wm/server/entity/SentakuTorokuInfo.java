package jp.ne.sonet.medipro.wm.server.entity;

/**
 * <strong>�I��o�^�e�[�u�����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class SentakuTorokuInfo {
    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** DR-ID */
    protected String drId = null;
    /** MR-ID */
    protected String mrId = null;
    /** �V�[�P���X */
    protected int seq = 0;
    /** ��t���� */
    protected String name = null;
    /** �Ζ��� */
    protected String kinmusaki = null;
    /** ���[�J�[�{��ID */
    protected String makerShisetsuId = null;
    /** DR�e�[�u���ɂ������t���� */
    protected String drName = null;

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * DR-ID���擾����.
     * @return DR-ID
     */
    public String getDrId() {
        return drId;
    }

    /**
     * DR-ID��ݒ肷��.
     * @param drId �ݒ肷��DR-ID
     */
    public void setDrId(String drId) {
        this.drId = drId;
    }

    /**
     * MR-ID���擾����.
     * @return MR-ID
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * MR-ID��ݒ肷��.
     * @param mrId �ݒ肷��MR-ID
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /**
     * �V�[�P���X���擾����.
     * @return �V�[�P���X
     */
    public int getSeq() {
        return seq;
    }

    /**
     * �V�[�P���X��ݒ肷��.
     * @param val �V�[�P���X
     */
    public void setSeq(int val) {
        seq = val;
    }

    /**
     * ��t�������擾����.
     * @return �I��o�^�e�[�u���̈�t����
     */
    public String getName() {
        return name;
    }

    /**
     * ��t������ݒ肷��.
     * @param name �ݒ肷���t����
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * �Ζ�����擾����.
     * @return �Ζ���
     */
    public String getKinmusaki() {
        return kinmusaki;
    }

    /**
     * �Ζ����ݒ肷��.
     * @param kinmusaki �ݒ肷��Ζ���
     */
    public void setKinmusaki(String kinmusaki) {
        this.kinmusaki = kinmusaki;
    }

    /**
     * ���[�J�[�{��ID���擾����.
     * @return ���[�J�[�{��ID
     */
    public String getMakerShisetsuId() {
        return makerShisetsuId;
    }

    /**
     * ���[�J�[�{��ID��ݒ肷��.
     * @param makerShisetsuId �ݒ肷�郁�[�J�[�{��ID
     */
    public void setMakerShisetsuId(String makerShisetsuId) {
        this.makerShisetsuId = makerShisetsuId;
    }

    /**
     * ��t�������擾����.
     * @return ��t�e�[�u���̈�t����
     */
    public String getDrName() {
        return drName;
    }

    /**
     * ��t������ݒ肷��.
     * @param drName �ݒ肷���t����
     */
    public void setDrName(String drName) {
        this.drName = drName;
    }

    /**
     * ���̃I�u�W�F�N�g�̕�����\�����擾����.
     * @return �p�����[�^�̔z��\��������
     */
    public String toString() {
        return "("
            + drId + ","
            + mrId + ","
            + seq + ","
            + name + ","
            + kinmusaki + ","
            + makerShisetsuId + ","
            + drName + ")";
    }

}
