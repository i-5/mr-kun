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
 * (E)メッセージ取消要求
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/05 23:57:55)
 */
public class CancelRequest extends Request {

    /** データ種別 */
    public static final int DATA_TYPE = 0;
    /** シーケンス番号 */
    public static final int SEQUENCE_NO = 1;
    /** MR-ID */
    public static final int MR_ID = 2;
    /** メッセージ番号 */
    public static final int MESSAGE_ID = 3;
    /** 備考 */
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
     * 要求内容にエラーが合った場合はそれを返す
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
                
            //MRがDBに登録されているかをチェック
            MrInfoManager mrManager = new MrInfoManager(conn);
            if (!mrManager.exist(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "(MR)は登録されていない");

                response.setErrorNo(CancelResponse.NO_MR);
                result.addResponse(response);
                return result;
            }

            //会社チェック
            //シーケンスの頭10桁はメーカーコード
            String apCompanyCd = getParameter(SEQUENCE_NO).substring(0, 10);
            CompanyInfoManager companyManager = new CompanyInfoManager(conn);
            String companyCd = companyManager.getCompanyCd(apCompanyCd);
            if (companyCd.equals("")) {
                Logger.log(apCompanyCd + "は登録されてない会社");

                response.setErrorNo(CancelResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            MessageInfoManager manager = new MessageInfoManager(conn);

            MessageHeader header = manager.getMessageHeader(getParameter(MESSAGE_ID));

            if (header == null) {
                Logger.log("メッセージが見つかりません。(MESSAGE_NO="
                           + getParameter(MESSAGE_ID) +")");
                response.setErrorNo(CancelResponse.NO_MESSAGE);
                result.addResponse(response);
                return result;
            } else if (!header.getFromUserid().equals(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "が送信したメッセージではありません。");

                response.setErrorNo(CancelResponse.NOT_OWNER);
                result.addResponse(response);
                return result;
            } else if (header.getReceiveTimed() != null) {
                Logger.log("既に開封されています。");

                response.setErrorNo(CancelResponse.ALREADY_RECEIVED);
                result.addResponse(response);
                return result;
            } else if (header.getSendTorikeshiTime() != null) {
                Logger.log("既に取り消されています。");

                response.setErrorNo(CancelResponse.ALREADY_TORIKESHI);
                result.addResponse(response);
                return result;
            }


            try {
                //取消処理
                manager.cancel(getParameter(MESSAGE_ID));

                response.setErrorNo(CancelResponse.NORMAL);
                result.addResponse(response);
            } catch (SQLException ex) {
                Logger.log("既読処理に失敗しました。", ex);
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
