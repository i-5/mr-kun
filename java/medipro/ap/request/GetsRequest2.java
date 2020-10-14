package medipro.ap.request;

import java.sql.Connection;
import java.util.Vector;
import java.sql.SQLException;
import medipro.ap.entity.GetsError2;
import medipro.ap.entity.Result;
import medipro.ap.manager.MrInfoManager;
import medipro.ap.entity.SendResponse;
import medipro.ap.manager.MessageInfoManager;
import medipro.ap.util.Utility;
import medipro.ap.entity.GetsResponse2;
import java.util.Iterator;
import medipro.ap.util.Logger;
import medipro.ap.manager.CompanyInfoManager;
import medipro.ap.entity.Response;

/**
 * (A,B)MR君受信メッセージ送信要求
 *
 * @author  doppe
 * @version 1.0 (created at 2001/12/04 13:51:20)
 */
public class GetsRequest2 extends GetsRequest {

    public GetsRequest2() {
        super();
    }

    /**
     * 要求内容にエラーが合った場合はそれを返す
     * @return 
     */
    public Response getError() {
        GetsError2 response = new GetsError2();
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
        Vector messages = new Vector();

        try {
            conn = Utility.getConnection();

            //MRがDBに登録されているかをチェック
            //ALLDATAの場合は通す
            if (!getParameter(MR_ID).equals("ALLDATA")) {
                MrInfoManager mrManager = new MrInfoManager(conn);
                if (!mrManager.exist(getParameter(MR_ID))) {
                    Logger.log(getParameter(MR_ID) + "(MR)は登録されていない");
                    GetsError2 response = new GetsError2();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(GetsError2.NO_MR);
                    result.addResponse(response);
                    return result;
                }
            }

            //会社チェック
            //シーケンスの頭10桁はメーカーコード
            String apCompanyCd = getParameter(SEQUENCE_NO).substring(0, 10);
            CompanyInfoManager companyManager = new CompanyInfoManager(conn);
            String companyCd = companyManager.getCompanyCd(apCompanyCd);
            if (companyCd.equals("")) {
                Logger.log(apCompanyCd + "は登録されてない会社");
                GetsError2 response = new GetsError2();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            //メッセージ一覧を取得
            MessageInfoManager manager = new MessageInfoManager(conn);
            messages = manager.getMessages2(getParameter(DATA_TYPE),
                                            companyCd,
                                            getParameter(MR_ID),
                                            getParameter(CHECK_READ));


        } catch (SQLException ex) {
            Logger.error("データベースとの間で何らかの不整合?", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            }
        }


        //データ無しエラー
        if (messages.size() == 0) {
            Logger.log("メッセージは一件もありません");
            GetsError2 response = new GetsError2();
            response.setSequenceNo(getParameter(SEQUENCE_NO));
            response.setErrorNo(GetsError2.NORMAL);
            result.addResponse(response);
            return result;
        } else {
            Logger.log("メッセージ数 = " + messages.size());
			
            Iterator i = messages.iterator();
            while (i.hasNext()) {
                String[] elements = (String[])i.next();
                GetsResponse2 response = new GetsResponse2();
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
                response.setAttachFile1(elements[13]);
                response.setAttachFile2(elements[14]);
                response.setAttachFile3(elements[15]);
                response.setAttachFile4(elements[16]);
                response.setAttachFile5(elements[17]);
                response.setOriginalMessageId(elements[18]);
                
                //0詰めで5桁
                String count = Utility.format(messages.size(), "00000");
                response.setMessageCount(count);

                result.addResponse(response);
            }
        }

        return result;
    }
}
