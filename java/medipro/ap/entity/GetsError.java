package medipro.ap.entity;

/**
 * (4)MR�N��M���b�Z�[�W���M����Error
 */
public class GetsError extends Response {
    /** �f�[�^��� */
    final static int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    final static int SEQUENCE_NO = 1;
    /** �G���[�ԍ� */
    final static int ERROR_NO = 2;
    /** ���l */
    final static int REMARK = 3;

    /**
     * 
     */
    public GetsError() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "ERROR_NO",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            2,
            25,
        };

        parameterValues = new String[]{"6", "", "", ""};
    }

    /**
     * �V�[�P���X�ԍ���ݒ�
     */
    public void setSequenceNo(String value) {
        parameterValues[SEQUENCE_NO] = value;
    }

    /**
     * �G���[�ԍ���ݒ�
     */
    public void setErrorNo(String value) {
        parameterValues[ERROR_NO] = value;
    }

    /**
     * ���l��ݒ�
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }
}
