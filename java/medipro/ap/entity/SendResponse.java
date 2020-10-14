package medipro.ap.entity;

/**
 * (2)���b�Z�[�W�쐬�˗�����
 */
public class SendResponse extends Response {

    /** �f�[�^��� */
    final static int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    final static int SEQUENCE_NO = 1;
    /** ���b�Z�[�WID */
    final static int MESSAGE_ID = 2;
    /** �G���[�ԍ� */
    final static int ERROR_NO = 3;
    /** �G���[��tID */
    final static int ERROR_DOCTOR_ID = 4;
    /** ���l */
    final static int REMARK = 5;

    /**
     * 
     */
    public SendResponse() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "ERROR_NO",
            "ERROR_DOCTOR_ID",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            2,
            10,
            7,
        };

        parameterValues = new String[]{"2", "", "", "", "", ""};
    }

    /**
     * �V�[�P���X�ԍ���ݒ�
     */
    public void setSequenceNo(String value) {
        parameterValues[SEQUENCE_NO] = value;
    }

    /**
     * ���b�Z�[�WID��ݒ�
     */
    public void setMessageId(String value) {
        parameterValues[MESSAGE_ID] = value;
    }

    /**
     * �G���[�ԍ���ݒ�
     */
    public void setErrorNo(String value) {
        parameterValues[ERROR_NO] = value;
    }

    /**
     * �G���[��t�V�X�e��ID��ݒ�
     */
    public void setErrorDoctorId(String value) {
        parameterValues[ERROR_DOCTOR_ID] = value;
    }

    /**
     * ���l��ݒ�
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }

}
