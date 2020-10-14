package medipro.ap.request;

import java.sql.Connection;
import java.util.Vector;
import java.sql.SQLException;
import medipro.ap.entity.GetsError;
import medipro.ap.entity.Result;
import medipro.ap.manager.MrInfoManager;
import medipro.ap.entity.SendResponse;
import medipro.ap.manager.MessageInfoManager;
import medipro.ap.util.Utility;
import medipro.ap.entity.GetsResponse;
import java.util.Iterator;
import medipro.ap.util.Logger;
import medipro.ap.manager.CompanyInfoManager;
import medipro.ap.entity.Response;


/**
 * (3)MR�N��M���b�Z�[�W���M�v��
 */
public class GetsRequest extends Request {

    /** �f�[�^��� */
    public static final int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    public static final int SEQUENCE_NO = 1;
    /** MR-ID */
    public static final int MR_ID = 2;
    /** ���ǃ`�F�b�N */
    public static final int CHECK_READ = 3;
    /** ���l */
    public static final int COMMENT = 4;

    /**
     *
     */
    public GetsRequest() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MR_ID",
            "CHECK_READ",
            "COMMENT",
        };

        parameterSizes = new int[]{
            1,
            22,
            10,
            1,
            16,
        };

        required = new boolean[]{
            true,
            true,
            true,
            true,
            false,
        };

        parameterValues = new String[parameterNames.length];
    }

    /**
     * �v�����e�ɃG���[���������ꍇ�͂����Ԃ�
     */
    public Response getError() {
        GetsError response = new GetsError();
        response.setSequenceNo(getParameter(SEQUENCE_NO));
        response.setErrorNo(Response.LACK);

        return response;
    }

    /**
     * �v�������s����
     * @return ���s���ʊi�[�I�u�W�F�N�g
     */
    public Result execute() {
        Result result = new Result();

        Connection conn = null;
        Vector messages = new Vector();

        try {
            conn = Utility.getConnection();

            //MR��DB�ɓo�^����Ă��邩���`�F�b�N
            //ALLDATA�̏ꍇ�͒ʂ�
            if (!getParameter(MR_ID).equals("ALLDATA")) {
                MrInfoManager mrManager = new MrInfoManager(conn);
                if (!mrManager.exist(getParameter(MR_ID))) {
                    Logger.log(getParameter(MR_ID) + "(MR)�͓o�^����Ă��Ȃ�");
                    GetsError response = new GetsError();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(GetsError.NO_MR);
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
                GetsError response = new GetsError();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            //���b�Z�[�W�ꗗ���擾
            MessageInfoManager manager = new MessageInfoManager(conn);
            messages = manager.getMessages(getParameter(DATA_TYPE),
                                           companyCd,
                                           getParameter(MR_ID),
                                           getParameter(CHECK_READ));


        } catch (SQLException ex) {
            Logger.error("�f�[�^�x�[�X�Ƃ̊Ԃŉ��炩�̕s����?", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }


        //�f�[�^�����G���[
        if (messages.size() == 0) {
            Logger.log("���b�Z�[�W�͈ꌏ������܂���");
            GetsError response = new GetsError();
            response.setSequenceNo(getParameter(SEQUENCE_NO));
            response.setErrorNo(GetsError.NORMAL);
            result.addResponse(response);
            return result;
        } else {
            Logger.log("���b�Z�[�W�� = " + messages.size());
			
            Iterator i = messages.iterator();
            while (i.hasNext()) {
                String[] elements = (String[])i.next();
                GetsResponse response = new GetsResponse();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setMessageId(elements[0]);
                response.setDate(elements[1]);
                response.setTime(elements[2]);
                response.setFromUserId(elements[3]);
                response.setToUserId(elements[4]);
                response.setCatchyCopy(elements[5]);
                response.setMessageBody(elements[6]);
                response.setLinkUrl(elements[7]);
                response.setAttachmentFlag(elements[8]);
                response.setDoctorName(elements[9]);
                response.setDoctorOffice(elements[10]);
                response.setSystemCd(elements[11]);
                response.setMessageType(elements[12]);

                //0�l�߂�5��
                String count = Utility.format(messages.size(), "00000");
                response.setMessageCount(count);

                result.addResponse(response);
            }
        }

        return result;
    }

}
