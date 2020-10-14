package medipro.ap.entity;

/**
 * (F)���b�Z�[�W����˗�����
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/05 23:56:55)
 */
public class CancelResponse extends Response {

    /** ���ǃG���[ */
    public final static String ALREADY_RECEIVED = "03";
    public final static String ALREADY_TORIKESHI = "07";
    public final static String NO_MESSAGE = "08";
    public final static String NOT_OWNER = "09";

    /** �f�[�^��� */
    final static int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    final static int SEQUENCE_NO = 1;
    /** ���b�Z�[�WID */
    final static int MESSAGE_ID = 2;
    /** �G���[�ԍ� */
    final static int ERROR_NO = 3;
    /** ���l */
    final static int REMARK = 4;
    
    public CancelResponse() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "ERROR_NO",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            2,
            7,
        };

        parameterValues = new String[]{"F", "", "", "", ""};
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
     * ���l��ݒ�
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }
    
}
