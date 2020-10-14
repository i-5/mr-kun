package medipro.ap.request;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import medipro.ap.entity.MessageBody;
import medipro.ap.entity.MessageHeader;
import medipro.ap.entity.Response;
import medipro.ap.entity.Result;
import medipro.ap.entity.SendResponse;
import medipro.ap.manager.CatchPictureInfoManager;
import medipro.ap.manager.CompanyInfoManager;
import medipro.ap.manager.DoctorInfoManager;
import medipro.ap.manager.MessageInfoManager;
import medipro.ap.manager.MrInfoManager;
import medipro.ap.util.Logger;
import medipro.ap.util.Utility;

/**
 * (1)���b�Z�[�W�쐬�˗�����
 */
public class SendRequest extends Request {

    /** �f�[�^��� */
    final static int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    final static int SEQUENCE_NO = 1;
    /** �쐬���� */
    final static int CREATE_DATE = 2;
    /** ���b�Z�[�W��� */
    final static int MESSAGE_STATUS = 3;
    /** MR-ID */
    final static int MR_ID = 4;
    /** �L���b�`�摜�ԍ� */
    final static int PICTURE_CD= 5;
    /** ���ȏЉ� */
    final static int JIKOSYOKAI = 6;
    /** �L���b�`�R�s�[ */
    final static int CATCHY_COPY = 7;
    /** ���b�Z�[�W�^�C�v */
    final static int MESSAGE_TYPE = 8;
    /** �{�� */
    final static int MESSAGE_BODY = 9;
    /** �����N */
    final static int LINK_URL = 10;
    /** �L�������� */
    final static int APPLIED_END_DATE = 11;
    /** ���M��t�R�[�h�P */
    final static int SYSTEM_CD_1 = 12;
    /** ���M��t�R�[�h�Q */
    final static int SYSTEM_CD_2 = 13;
    /** ���M��t�R�[�h�R */
    final static int SYSTEM_CD_3 = 14;
    /** ���M��t�R�[�h�S */
    final static int SYSTEM_CD_4 = 15;
    /** ���M��t�R�[�h�T */
    final static int SYSTEM_CD_5 = 16;
    /** ���M��t�R�[�h�U */
    final static int SYSTEM_CD_6 = 17;
    /** ���M��t�R�[�h�V */
    final static int SYSTEM_CD_7 = 18;
    /** ���M��t�R�[�h�W */
    final static int SYSTEM_CD_8 = 19;
    /** ���M��t�R�[�h�X */
    final static int SYSTEM_CD_9 = 20;
    /** ���M��t�R�[�h�P�O */
    final static int SYSTEM_CD_10 = 21;
    /** ���l */
    final static int COMMENT = 22;

    /**
     *
     */
    public SendRequest() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "CREATE_DATE",
            "MESSAGE_STATUS",
            "MR_ID",
            "PICTURE_CD",
            "JIKOSYOKAI",
            "CATCHY_COPY",
            "MESSAGE_TYPE",
            "MESSAGE_BODY",
            "LINK_URL",
            "APPLIED_END_DATE",
            "SYSTEM_CD_1",
            "SYSTEM_CD_2",
            "SYSTEM_CD_3",
            "SYSTEM_CD_4",
            "SYSTEM_CD_5",
            "SYSTEM_CD_6",
            "SYSTEM_CD_7",
            "SYSTEM_CD_8",
            "SYSTEM_CD_9",
            "SYSTEM_CD_10",
            "COMMENT",
        };

        parameterSizes = new int[]{
            1,
            22,
            6,
            1,
            10,
            10,
            64,
            64,
            1,
            2000,
            256,
            8,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            57,
        };

        required = new boolean[]{
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
        };

        parameterValues = new String[parameterNames.length];
    }

    /**
     * �v�����e�ɃG���[���������ꍇ�͂����Ԃ�
     */
    public Response getError() {
        SendResponse response = new SendResponse();
        response.setSequenceNo(getParameter(SEQUENCE_NO));
        response.setErrorNo(SendResponse.LACK);

        return response;
    }

    /**
	 * �v�������s����
	 * @return ���s���ʊi�[�I�u�W�F�N�g
	 */
    public Result execute() {
        Result result = new Result();
        Connection conn = null;

        try {
            conn = Utility.getConnection();

            //MR��DB�ɓo�^����Ă��邩���`�F�b�N
            MrInfoManager mrManager = new MrInfoManager(conn);
            if (!mrManager.exist(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "(MR)�͓o�^����Ă��Ȃ�");
                SendResponse response = new SendResponse();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_MR);
                result.addResponse(response);
                return result;
            }

            //�L���b�`�摜��DB�ɓo�^����Ă��邩�`�F�b�N
            if (Utility.isNotNull(getParameter(PICTURE_CD))) {
                CatchPictureInfoManager picManager = new CatchPictureInfoManager(conn);
                if (!picManager.exist(getParameter(PICTURE_CD))) {
                    Logger.log(getParameter(PICTURE_CD) + "(�摜)�͓o�^����Ă��Ȃ�");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.NO_PICTURE);
                    result.addResponse(response);
                    return result;
                }
            }

            //�L���������`�F�b�N
            if (Utility.isNotNull(getParameter(APPLIED_END_DATE))) {
                if (!isValidDate(getParameter(APPLIED_END_DATE))) {
                    Logger.log(getParameter(APPLIED_END_DATE) + "�͕s���ȗL������");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.ILLEGAL_APPLIED_DATE);
                    result.addResponse(response);
                    return result;
                }
            }

            //��Ѓ`�F�b�N
            //�V�[�P���X�̓�10���̓��[�J�[�R�[�h
            String apCompanyCd = getParameter(SEQUENCE_NO).substring(0, 10);
            CompanyInfoManager companyManager = new CompanyInfoManager(conn);
            String companyCd = companyManager.getCompanyCd(apCompanyCd);
            if (companyCd.equals("")) {
                Logger.log(apCompanyCd + "�͓o�^����ĂȂ����");
                SendResponse response = new SendResponse();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            // ����̃`�F�b�N���Z�b�g
            //�����ł�systemcd �� dr_id�ւ̕ϊ�������(1-10�܂�)
            //�G���[�ɂȂ�����t��������I��?
            DoctorInfoManager drManager = new DoctorInfoManager(conn);
            String[] drIds = drManager.toDoctorId(getSystemCds());

            //DB�ɓo�^����Ă��Ȃ���t��null���Z�b�g����ĕԂ��Ă���̂�
            //�����ŃG���[�������쐬
            for (int i = 0; i < drIds.length; i++) {
                if (drIds[i] == null) {
                    Logger.log(getSystemCds()[i] + "(��t)�͓o�^����Ă��Ȃ�");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.NO_DR);
                    response.setErrorDoctorId(getSystemCds()[i]);
                    result.addResponse(response);
                }
            }

            //MR����DR��
            MessageHeader header = new MessageHeader();
            MessageBody body = new MessageBody();

            //���b�Z�[�W�敪
            header.setMessageKbn("1");
            //���M��ID
            header.setFromUserid(getParameter(MR_ID));
            //CC�t���O
            header.setCcFlg("0");
            //���M����
            header.setReceiveTime(toDateString(getParameter(CREATE_DATE)));
            //���[�J�[�R�[�h
            body.setCompanyCd(companyCd);
            //���ȏЉ�
            body.setJikosyokai(getParameter(JIKOSYOKAI));
            //�L���b�`�R�s�[
            body.setTitle(getParameter(CATCHY_COPY));
            //�{��(html�^�O����)
            body.setMessageHonbun(Utility.createLinks(getParameter(MESSAGE_BODY)));
            //  			body.setMessageHonbun(getParameter(MESSAGE_BODY));

            //�L������
            body.setYukoKigen(getParameter(APPLIED_END_DATE));
            //�L���b�`�摜
            body.setPictureCd(getParameter(PICTURE_CD));
            //�Y�t�����N
            body.setUrl(getParameter(LINK_URL));
            //���b�Z�[�W���
            body.setMessageType(getParameter(MESSAGE_TYPE));

            //���b�Z�[�W��DB�ɒǉ�
            MessageInfoManager manager = new MessageInfoManager(conn);

            //�e��t���ɐ��ۂɂ�炸���M
            for (int i = 0; i < drIds.length; i++) {
				//�V�X�e����t�R�[�h���o�^����Ă�����t�ɂ̂ݑ��M����
                if (drIds[i] != null) {
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));

                    //�����Ǝ��s�̏ꍇ�Ō��ʂ��قȂ�
                    try {
                        Logger.log(drIds[i] + "�ɑ��M��...");
                        header.setToUserid(drIds[i]);
                        manager.insert(header, body);
                        Logger.log("����");
						
                        response.setMessageId(header.getMessageHeaderId());
                        response.setErrorNo(SendResponse.NORMAL);
                        response.setErrorDoctorId(getSystemCds()[i]);
                        result.addResponse(response);
                    } catch (SQLException ex) {
                        Logger.log(drIds[i] + "�ւ̑��M�Ɏ��s", ex);
                        response.setErrorNo(SendResponse.UNKNOWN_ERROR);
                        response.setErrorDoctorId(getSystemCds()[i]);
                        result.addResponse(response);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.error("���b�Z�[�W���M���ɏ�Q������", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }

        return result;
    }

    /**
	 * �󔒂łȂ���t�R�[�h�̔z����쐬����B
	 * 10�l�̈�t�R�[�h�Őݒ肳��Ă��Ȃ��ӏ����Ȃ��B
	 */ 
    private String[] getSystemCds() {
        Vector cds = new Vector();

        for (int i = 12; i < 22; i++) {
            if (Utility.isNotNull(getParameter(i))) {
                cds.addElement(getParameter(i));
            }
        }

        Object[] buf = cds.toArray();
        String[] ret = new String[buf.length];
        System.arraycopy(buf, 0, ret, 0, buf.length);

        return ret;
    }

    /**
	 * �L�����t�̃t�H�[�}�b�g�ƁA�����ȍ~�ł��邩�̃`�F�b�N
	 * �t�H�[�}�b�g��"yyyyMMdd"
	 */
    private boolean isValidDate(String dateString) {
        Date date = null;

        try {
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            date = formatter.parse(dateString, pos);
        } catch (Exception ex) {
            return false;
        }
		
        if (date == null) {
            return false;
        } else if (date.before(new Date())) {
            return false;
        }

        return true;
    }

    /**
	 * ���b�Z�[�W�쐬�����ɔN������ݒ肷��B
	 * �t�H�[�}�b�g��"yyyyMMddHHMMSS"�A���̂����N�����ɂ͍����̓��t��ݒ肷��B
	 */
    private String toDateString(String dateString) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
        String yyyymmdd = formatter1.format(new Date());

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat("HHMMSS");
        formatter.parse(dateString, pos);

        return yyyymmdd + dateString;
    }
}
