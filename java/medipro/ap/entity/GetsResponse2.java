package medipro.ap.entity;

/**
 * Describe class <code>GetsResponse2</code> here.
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/04 14:04:35)
 */
public class GetsResponse2 extends GetsResponse {
//      /** �f�[�^��� */
//      final static int DATA_TYPE = 0;
//      /** �V�[�P���X�ԍ� */
//      final static int SEQUENCE_NO = 1;
//      /** ���b�Z�[�WID */
//      final static int MESSAGE_ID = 2;
//      /** ���M�N���� */
//      final static int DATE = 3;
//      /** ���M���� */
//      final static int TIME = 4;
//      /** ���M��ID */
//      final static int FROM_USER_ID = 5;
//      /** ���M��ID */
//      final static int TO_USER_ID = 6;
//      /** �L���b�`�R�s�[ */
//      final static int CATCHY_COPY = 7;
//      /** �{�� */
//      final static int MESSAGE_BODY = 8;
//      /** �����N */
//      final static int LINK_URL = 9;
//      /** �Y�t�t�@�C���L�� */
//      final static int ATTACHMENT_FLAG = 10;
//      /** ��t���� */
//      final static int DOCTOR_NAME = 11;
//      /** ��t�{�ݖ� */
//      final static int DOCTOR_OFFICE = 12;
//      /** �V�X�e����t�R�[�h */
//      final static int SYSTEM_CD = 13;
//      /** ���b�Z�[�W��� */
//      final static int MESSAGE_TYPE = 14;
//      /** ���b�Z�[�W�� */
//      final static int MESSAGE_COUNT = 15;
    /** �Y�tURL1 */
    final static int ATTACH_FILE1 = 16;
    /** �Y�tURL2 */
    final static int ATTACH_FILE2 = 17;
    /** �Y�tURL3 */
    final static int ATTACH_FILE3 = 18;
    /** �Y�tURL4 */
    final static int ATTACH_FILE4 = 19;
    /** �Y�tURL5 */
    final static int ATTACH_FILE5 = 20;
    /** �����b�Z�[�WID */
    final static int ORIGINAL_MESSAGE_ID = 21;
    /** ���l */
    final static int REMARK = 22;

    /**
     *
     */
    public GetsResponse2() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MESSAGE_ID",
            "DATE",
            "TIME",
            "FROM_USER_ID",
            "TO_USER_ID",
            "CATCH_COPY",
            "MESSAGE_BODY",
            "LINK_URL",
            "ATTACHMENT_FLAG",
            "DOCTOR_NAME",
            "DOCTOR_OFFICE",
            "SYSTEM_CD",
            "MESSAGE_TYPE",
            "MESSAGE_COUNT",
            "ATTACH_FILE1",
            "ATTACH_FILE2",
            "ATTACH_FILE3",
            "ATTACH_FILE4",
            "ATTACH_FILE5",
            "ORIGINAL_MESSAGE_ID",
            "REMARK",
        };

        parameterSizes = new int[]{
            1,
            22,
            18,
            8,
            6,
            10,
            10,
            64,
            2000,
            256,
            1,
            40,
            64,
            10,
            1,
            5,
            128,
            128,
            128,
            128,
            128,
            18,
            20,
        };

        parameterValues = new String[]{"C", "", "", "", "", "", "", "", "", "",
                                       "", "", "", "", "", "", "", "", "", "",
                                       "", "", ""};
    }

    /**
     * �Y�t�t�@�C��1��ݒ�
     */
    public void setAttachFile1(String value) {
        parameterValues[ATTACH_FILE1] = value;
    }

    /**
     * �Y�t�t�@�C��2��ݒ�
     */
    public void setAttachFile2(String value) {
        parameterValues[ATTACH_FILE2] = value;
    }

    /**
     * �Y�t�t�@�C��3��ݒ�
     */
    public void setAttachFile3(String value) {
        parameterValues[ATTACH_FILE3] = value;
    }

    /**
     * �Y�t�t�@�C��4��ݒ�
     */
    public void setAttachFile4(String value) {
        parameterValues[ATTACH_FILE4] = value;
    }

    /**
     * �Y�t�t�@�C��5��ݒ�
     */
    public void setAttachFile5(String value) {
        parameterValues[ATTACH_FILE5] = value;
    }

    /**
     * �����b�Z�[�W�ԍ�
     */
    public void setOriginalMessageId(String value) {
        parameterValues[ORIGINAL_MESSAGE_ID] = value;
    }

    /**
     * ���l��ݒ�
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }
}
