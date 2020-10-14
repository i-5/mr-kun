package medipro.ap.entity;

/**
 * (4)MR�N��M���b�Z�[�W���M����.
 * <pre>
 * 01 dataType
 * 02 sequenceNo
 * 03 messageId      �� message_header.message_id
 * 04 date           �� message_header.receive_time
 * 05 time           �� message_header.receive_time
 * 06 fromUserId     �� message_header.from_userid �� doctor.system_cd
 * 07 toUserId       �� message_header.to_userid
 * 08 catchCopy      �� message_body.title
 * 09 messageBody    �� message_body.message_honbun
 * 10 linkUrl        �� message_body.url
 * 11 attachmentFlag �� attch_file��message_id������
 * 12 doctorName     �� sentaku_toroku.name
 * 13 doctorOffice   �� sentaku_toroku.kinmusaki
 * 14 systemCd       �� doctor.system_cd
 * 15 messageType    �� message_header.message_kbn
 * 16 messageCount
 * 17 remark
 * </pre>
 */
public class GetsResponse extends Response {
    /** �f�[�^��� */
    final static int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    final static int SEQUENCE_NO = 1;
    /** ���b�Z�[�WID */
    final static int MESSAGE_ID = 2;
    /** ���M�N���� */
    final static int DATE = 3;
    /** ���M���� */
    final static int TIME = 4;
    /** ���M��ID */
    final static int FROM_USER_ID = 5;
    /** ���M��ID */
    final static int TO_USER_ID = 6;
    /** �L���b�`�R�s�[ */
    final static int CATCHY_COPY = 7;
    /** �{�� */
    final static int MESSAGE_BODY = 8;
    /** �����N */
    final static int LINK_URL = 9;
    /** �Y�t�t�@�C���L�� */
    final static int ATTACHMENT_FLAG = 10;
    /** ��t���� */
    final static int DOCTOR_NAME = 11;
    /** ��t�{�ݖ� */
    final static int DOCTOR_OFFICE = 12;
    /** �V�X�e����t�R�[�h */
    final static int SYSTEM_CD = 13;
    /** ���b�Z�[�W��� */
    final static int MESSAGE_TYPE = 14;
    /** ���b�Z�[�W�� */
    final static int MESSAGE_COUNT = 15;
    /** ���l */
    final static int REMARK = 16;

    /**
     *
     */
    public GetsResponse() {
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
            34,
        };

        parameterValues = new String[]{"5", "", "", "", "", "", "", "", "", "",
                                       "", "", "", "", "", "", ""};
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
     * ���M�N������ݒ�(YYYYMMDD)
     */
    public void setDate(String value) {
        parameterValues[DATE] = value;
    }

    /**
     * ���M������ݒ�(HHMMSS)
     */
    public void setTime(String value) {
        parameterValues[TIME] = value;
    }

    /**
     * ���M�����[�UID��ݒ�
     */
    public void setFromUserId(String value) {
        parameterValues[FROM_USER_ID] = value;
    }

    /**
     * ���M�惆�[�UID��ݒ�
     */
    public void setToUserId(String value) {
        parameterValues[TO_USER_ID] = value;
    }

    /**
     * �L���b�`�R�s�[��ݒ�
     */
    public void setCatchyCopy(String value) {
        parameterValues[CATCHY_COPY] = value;
    }

    /**
     * ���b�Z�[�W�{����ݒ�
     */
    public void setMessageBody(String value) {
        parameterValues[MESSAGE_BODY] = value;
    }

    /**
     * �����N��URL��ݒ�
     */
    public void setLinkUrl(String value) {
        parameterValues[LINK_URL] = value;
    }

    /**
     * �Y�t�t�@�C���̗L����ݒ�
     */
    public void setAttachmentFlag(String value) {
        parameterValues[ATTACHMENT_FLAG] = value;
    }

    /**
     * ��t������ݒ�
     */
    public void setDoctorName(String value) {
        parameterValues[DOCTOR_NAME] = value;
    }

    /**
     * ��t�Ζ����ݒ�
     */
    public void setDoctorOffice(String value) {
        parameterValues[DOCTOR_OFFICE] = value;
    }

    /**
     * �V�X�e����t�R�[�h��ݒ�
     */
    public void setSystemCd(String value) {
        parameterValues[SYSTEM_CD] = value;
    }

    /**
     * ���b�Z�[�W��ʂ�ݒ�
     */
    public void setMessageType(String value) {
        parameterValues[MESSAGE_TYPE] = value;
    }

    /**
     * �S���b�Z�[�W����ݒ�
     */
    public void setMessageCount(String value) {
        parameterValues[MESSAGE_COUNT] = value;
    }

    /**
     * ���l��ݒ�
     */
    public void setRemark(String value) {
        parameterValues[REMARK] = value;
    }

}
