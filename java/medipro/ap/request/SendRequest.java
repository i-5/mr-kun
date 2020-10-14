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
 * (1)メッセージ作成依頼応答
 */
public class SendRequest extends Request {

    /** データ種別 */
    final static int DATA_TYPE = 0;
    /** シーケンス番号 */
    final static int SEQUENCE_NO = 1;
    /** 作成時間 */
    final static int CREATE_DATE = 2;
    /** メッセージ状態 */
    final static int MESSAGE_STATUS = 3;
    /** MR-ID */
    final static int MR_ID = 4;
    /** キャッチ画像番号 */
    final static int PICTURE_CD= 5;
    /** 自己紹介 */
    final static int JIKOSYOKAI = 6;
    /** キャッチコピー */
    final static int CATCHY_COPY = 7;
    /** メッセージタイプ */
    final static int MESSAGE_TYPE = 8;
    /** 本文 */
    final static int MESSAGE_BODY = 9;
    /** リンク */
    final static int LINK_URL = 10;
    /** 有効期限日 */
    final static int APPLIED_END_DATE = 11;
    /** 送信医師コード１ */
    final static int SYSTEM_CD_1 = 12;
    /** 送信医師コード２ */
    final static int SYSTEM_CD_2 = 13;
    /** 送信医師コード３ */
    final static int SYSTEM_CD_3 = 14;
    /** 送信医師コード４ */
    final static int SYSTEM_CD_4 = 15;
    /** 送信医師コード５ */
    final static int SYSTEM_CD_5 = 16;
    /** 送信医師コード６ */
    final static int SYSTEM_CD_6 = 17;
    /** 送信医師コード７ */
    final static int SYSTEM_CD_7 = 18;
    /** 送信医師コード８ */
    final static int SYSTEM_CD_8 = 19;
    /** 送信医師コード９ */
    final static int SYSTEM_CD_9 = 20;
    /** 送信医師コード１０ */
    final static int SYSTEM_CD_10 = 21;
    /** 備考 */
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
     * 要求内容にエラーが合った場合はそれを返す
     */
    public Response getError() {
        SendResponse response = new SendResponse();
        response.setSequenceNo(getParameter(SEQUENCE_NO));
        response.setErrorNo(SendResponse.LACK);

        return response;
    }

    /**
	 * 要求を実行する
	 * @return 実行結果格納オブジェクト
	 */
    public Result execute() {
        Result result = new Result();
        Connection conn = null;

        try {
            conn = Utility.getConnection();

            //MRがDBに登録されているかをチェック
            MrInfoManager mrManager = new MrInfoManager(conn);
            if (!mrManager.exist(getParameter(MR_ID))) {
                Logger.log(getParameter(MR_ID) + "(MR)は登録されていない");
                SendResponse response = new SendResponse();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_MR);
                result.addResponse(response);
                return result;
            }

            //キャッチ画像がDBに登録されているかチェック
            if (Utility.isNotNull(getParameter(PICTURE_CD))) {
                CatchPictureInfoManager picManager = new CatchPictureInfoManager(conn);
                if (!picManager.exist(getParameter(PICTURE_CD))) {
                    Logger.log(getParameter(PICTURE_CD) + "(画像)は登録されていない");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.NO_PICTURE);
                    result.addResponse(response);
                    return result;
                }
            }

            //有効期限日チェック
            if (Utility.isNotNull(getParameter(APPLIED_END_DATE))) {
                if (!isValidDate(getParameter(APPLIED_END_DATE))) {
                    Logger.log(getParameter(APPLIED_END_DATE) + "は不正な有効期限");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.ILLEGAL_APPLIED_DATE);
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
                SendResponse response = new SendResponse();
                response.setSequenceNo(getParameter(SEQUENCE_NO));
                response.setErrorNo(SendResponse.NO_COMPANY);
                result.addResponse(response);
                return result;
            }

            // 宛先のチェック＆セット
            //ここではsystemcd → dr_idへの変換を実装(1-10まで)
            //エラーになった医師がいたら終了?
            DoctorInfoManager drManager = new DoctorInfoManager(conn);
            String[] drIds = drManager.toDoctorId(getSystemCds());

            //DBに登録されていない医師はnullがセットされて返ってくるので
            //ここでエラー応答を作成
            for (int i = 0; i < drIds.length; i++) {
                if (drIds[i] == null) {
                    Logger.log(getSystemCds()[i] + "(医師)は登録されていない");
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));
                    response.setErrorNo(SendResponse.NO_DR);
                    response.setErrorDoctorId(getSystemCds()[i]);
                    result.addResponse(response);
                }
            }

            //MRからDRへ
            MessageHeader header = new MessageHeader();
            MessageBody body = new MessageBody();

            //メッセージ区分
            header.setMessageKbn("1");
            //送信者ID
            header.setFromUserid(getParameter(MR_ID));
            //CCフラグ
            header.setCcFlg("0");
            //送信時刻
            header.setReceiveTime(toDateString(getParameter(CREATE_DATE)));
            //メーカーコード
            body.setCompanyCd(companyCd);
            //自己紹介
            body.setJikosyokai(getParameter(JIKOSYOKAI));
            //キャッチコピー
            body.setTitle(getParameter(CATCHY_COPY));
            //本文(htmlタグ処理)
            body.setMessageHonbun(Utility.createLinks(getParameter(MESSAGE_BODY)));
            //  			body.setMessageHonbun(getParameter(MESSAGE_BODY));

            //有効期限
            body.setYukoKigen(getParameter(APPLIED_END_DATE));
            //キャッチ画像
            body.setPictureCd(getParameter(PICTURE_CD));
            //添付リンク
            body.setUrl(getParameter(LINK_URL));
            //メッセージ種別
            body.setMessageType(getParameter(MESSAGE_TYPE));

            //メッセージをDBに追加
            MessageInfoManager manager = new MessageInfoManager(conn);

            //各医師毎に成否によらず送信
            for (int i = 0; i < drIds.length; i++) {
				//システム医師コードが登録されていた医師にのみ送信する
                if (drIds[i] != null) {
                    SendResponse response = new SendResponse();
                    response.setSequenceNo(getParameter(SEQUENCE_NO));

                    //成功と失敗の場合で結果が異なる
                    try {
                        Logger.log(drIds[i] + "に送信中...");
                        header.setToUserid(drIds[i]);
                        manager.insert(header, body);
                        Logger.log("成功");
						
                        response.setMessageId(header.getMessageHeaderId());
                        response.setErrorNo(SendResponse.NORMAL);
                        response.setErrorDoctorId(getSystemCds()[i]);
                        result.addResponse(response);
                    } catch (SQLException ex) {
                        Logger.log(drIds[i] + "への送信に失敗", ex);
                        response.setErrorNo(SendResponse.UNKNOWN_ERROR);
                        response.setErrorDoctorId(getSystemCds()[i]);
                        result.addResponse(response);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.error("メッセージ送信中に障害が発生", ex);
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
	 * 空白でない医師コードの配列を作成する。
	 * 10人の医師コードで設定されていない箇所を省く。
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
	 * 有効日付のフォーマットと、今日以降であるかのチェック
	 * フォーマットは"yyyyMMdd"
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
	 * メッセージ作成時刻に年月日を設定する。
	 * フォーマットは"yyyyMMddHHMMSS"、このうち年月日には今日の日付を設定する。
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
