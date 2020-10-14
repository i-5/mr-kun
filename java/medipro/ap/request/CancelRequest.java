package medipro.ap.request;

import medipro.ap.entity.Response;
import medipro.ap.entity.CancelResponse;
import medipro.ap.entity.Result;
import java.sql.Connection;
import medipro.ap.util.Utility;
import medipro.ap.util.Logger;
import java.sql.SQLException;
import medipro.ap.manager.MessageInfoManager;
import medipro.ap.manager.CompanyInfoManager;
import medipro.ap.manager.MrInfoManager;
import medipro.ap.entity.MessageHeader;

/**
 * (E)���b�Z�[�W����v��
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/05 23:57:55)
 */
public class CancelRequest extends Request {

    /** �f�[�^��� */
    public static final int DATA_TYPE = 0;
    /** �V�[�P���X�ԍ� */
    public static final int SEQUENCE_NO = 1;
    /** MR-ID */
    public static final int MR_ID = 2;
    /** ���b�Z�[�W�ԍ� */
    public static final int MESSAGE_ID = 3;
    /** ���l */
    public static final int COMMENT = 4;

    public CancelRequest() {
        parameterNames = new String[]{
            "DATA_TYPE",
            "SEQUENCE_NO",
            "MR_ID",
            "MESSAGE_ID",
            "COMMENT",
        };

        parameterSizes = new int[]{
            1,
            22,
            10,
            18,
            17,
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
        CancelResponse response = new CancelResponse();
        response.setSequenceNo(getParameter(SEQUENCE_NO));
        response.setErrorNo(Response.LACK);

        return response;
    }

    /**
     * Describe <code>execute</code> method here.
     *
     * @return 
     */
    public Result execute() {
        Result result = new Result();
        Connection conn = null;

        try {
            conn = Utility.getConnection();
            CancelResponse response = new CancelResponse();
            response.setSequenceNo(getParameter(SEQUENCE_NO));
            response.setMessageId(getParameter(MESSAGE_ID));
                
            //MR��DB�ɓo�^����Ă��邩���`�F�b�N
            MrInfoManager mrManager = new MrInfoManager(conn);
            if (!mrManager.exist(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "(MR)�͓o�^����Ă��Ȃ�");

                response.setErrorNo(CancelResponse.NO_MR);
                result.addResponse(response);
                return result;
            }

            //��Ѓ`�F�b�N
            //�V�[�P���X�̓�10���̓��[�J�[�R�[�h
            String apCompanyCd = getParameter(SEQUENCE_NO).substring(0, 10);
            CompanyInfoManager companyManager = new CompanyInfoManager(conn);
            String companyCd = companyManager.getCompanyCd(apCompanyCd);
            if (companyCd.equals("")) {
                Logger.log(apCompanyCd + "�͓o�^����ĂȂ����");

                response.setErrorNo(CancelResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            MessageInfoManager manager = new MessageInfoManager(conn);

            MessageHeader header = manager.getMessageHeader(getParameter(MESSAGE_ID));

            if (header == null) {
                Logger.log("���b�Z�[�W��������܂���B(MESSAGE_NO="
                           + getParameter(MESSAGE_ID) +")");
                response.setErrorNo(CancelResponse.NO_MESSAGE);
                result.addResponse(response);
                return result;
            } else if (!header.getFromUserid().equals(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "�����M�������b�Z�[�W�ł͂���܂���B");

                response.setErrorNo(CancelResponse.NOT_OWNER);
                result.addResponse(response);
                return result;
            } else if (header.getReceiveTimed() != null) {
                Logger.log("���ɊJ������Ă��܂��B");

                response.setErrorNo(CancelResponse.ALREADY_RECEIVED);
                result.addResponse(response);
                return result;
            } else if (header.getSendTorikeshiTime() != null) {
                Logger.log("���Ɏ�������Ă��܂��B");

                response.setErrorNo(CancelResponse.ALREADY_TORIKESHI);
                result.addResponse(response);
                return result;
            }


            try {
                //�������
                manager.cancel(getParameter(MESSAGE_ID));

                response.setErrorNo(CancelResponse.NORMAL);
                result.addResponse(response);
            } catch (SQLException ex) {
                Logger.log("���Ǐ����Ɏ��s���܂����B", ex);
                response.setErrorNo(CancelResponse.UNKNOWN_ERROR);
                result.addResponse(response);
            }

        } catch (Exception ex) {
            Logger.error("", ex);
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
}
