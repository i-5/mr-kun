package jp.ne.sonet.medipro.mr.common;

/**
 * <h3>共通コンスタンス.</h3> 
 * <br>
 * サーバ、クライアント間で共通に使用するコンスタンス.
 * 
 * @author
 */
public class SysCnst {

    // 画面区分 
    public static final String MODE_MR = "1";	// 画面区分  ＭＲ画面
    public static final String MODE_DR = "2";	// 画面区分  医師画面
    // メッセージ状態区分
    public static final String MSG_STATUS_NOMAL = "1";	// メッセージ状態区分  一覧
    public static final String MSG_STATUS_SAVE  = "2";	// メッセージ状態区分  保管ＢＯＸ
    public static final String MSG_STATUS_DUST  = "3";	// メッセージ状態区分  ごみ箱
    // 顧客管理一覧ソートキー 
    // 顧客管理一覧ソートキー  氏名
    public static final String SORTKEY_DR_NAME          = "dr.name";
    // 顧客管理一覧ソートキー  ターゲットランク
    public static final String SORTKEY_TARGET_RANK      = "sen.target_rank";
    public static final String SORTKEY_ACTION           = "action";
    // 顧客管理一覧ソートキー  未読受信メッセージ
    public static final String SORTKEY_RECV_COUNT       = "recvCount";
    // 顧客管理一覧ソートキー  新しい開封知らせ
    public static final String SORTKEY_NEW_OPEN_COUNT   = "newOpenCount";
    // 顧客管理一覧ソートキー  前回開封からの日数
    public static final String SORTKEY_LAST_OPEN_DAY    = "lastOpenDay";
    // 顧客管理一覧ソートキー  未読送信メッセージ
    public static final String SORTKEY_SEND_COUNT       = "sendCount";
    // 顧客管理一覧ソートキー  最新送信の未読日数
    public static final String SORTKEY_SEND_NO_READ_DAY = "sendNoReadDay";
    // ＭＲ名簿ソートキー
    // ＭＲ名簿ソートキー  選択区分
    public static final String SORTKEY_SENTAKU_KBN      = "sentaku_kbn";
    // ＭＲ名簿ソートキー  氏名
    public static final String SORTKEY_COMPANY_NAME     = "company_name";
    // ＭＲ名簿ソートキー  会社名
    public static final String SORTKEY_NAME             = "name";

    //受信一覧＆受信ＢＯＸ一覧ソートキー
    // 受信ソートキー  受信日時
    public static final String SORTKEY_RECV_RECEIVE_TIME     = "receive_time";
    // 受信ソートキー  送信者
    public static final String SORTKEY_RECV_FROM_USER        = "from_userid";
    // 受信ソートキー  未読
    public static final String SORTKEY_RECV_RECEIVE_TIMED    = "receive_timed";

    //送信一覧＆送信ＢＯＸ一覧ソートキー
    // 送信ソートキー  宛先
    public static final String SORTKEY_SEND_TO_USER          = "to_userid";
    // 送信ソートキー  ターゲットランク
    public static final String SORTKEY_SEND_TARGET_RANK      = "target_rank";
    // 送信ソートキー  送信日時
    public static final String SORTKEY_SEND_RECEIVE_TIME     = "receive_time";
    // 送信ソートキー  未読
    public static final String SORTKEY_SEND_RECEIVE_TIMED    = "receive_timed";
    // 送信ソートキー  未読日数
    public static final String SORTKEY_SEND_UNREAD_DAY       = "unread_day";

    //顧客名簿ソートキー
    // 顧客名簿ソートキー  所属先・勤務先
    public static final String SORTKEY_DR_KINMUSAKI          = "kinmusaki";
    // 顧客名簿ソートキー  氏名
    public static final String SORTKEY_DR_DR_ID              = "dr_id";
    // 顧客名簿ソートキー  ターゲットランク
    public static final String SORTKEY_DR_TARGET_RANK        = "sen.target_rank";
    // 顧客名簿ソートキー  職種
    public static final String SORTKEY_DR_SYOKUSYU           = "syokusyu";
    // 顧客名簿ソートキー  専門領域１
    public static final String SORTKEY_DR_SENMON1            = "senmon1";
    // 顧客名簿ソートキー  専門領域２
    public static final String SORTKEY_DR_SENMON2            = "senmon2";
    // 顧客名簿ソートキー  専門領域３
    public static final String SORTKEY_DR_SENMON3            = "senmon3";

    //統計情報顧客別ソートキー
    // 統計情報顧客別ソートキー  氏名
    public static final String SORTKEY_STAT_NAME             = "dr_id";
    // 統計情報顧客別ソートキー  ターゲットランク
    public static final String SORTKEY_STAT_TARGET_RANK      = "target_rank";
    // 統計情報顧客別ソートキー  送信数３０
    public static final String SORTKEY_STAT_SEND_COUNT30     = "send_count30";
    // 統計情報顧客別ソートキー  クリック数３０
    public static final String SORTKEY_STAT_CKICK_COUNT30    = "click_count30";
    // 統計情報顧客別ソートキー  クリック率３０
    public static final String SORTKEY_STAT_CKICK_RATE30     = "click_rate30";
    // 統計情報顧客別ソートキー  送信数１８０
    public static final String SORTKEY_STAT_SEND_COUNT180    = "send_count180";
    // 統計情報顧客別ソートキー  クリック数１８０
    public static final String SORTKEY_STAT_CKICK_COUNT180   = "click_count180";
    // 統計情報顧客別ソートキー  クリック率１８０
    public static final String SORTKEY_STAT_CKICK_RATE180    = "click_rate180";

    //コミュニケーション履歴ソートキー
    // コミュニケーション履歴ソートキー  メッセージ区分
    public static final String SORTKEY_COMMUNI_MESSAGE_KBN   = "message_kbn";
    // コミュニケーション履歴ソートキー  送受信日時
    public static final String SORTKEY_COMMUNI_RECEIVE_TIME  = "receive_time";
    // コミュニケーション履歴ソートキー  タイトル
    public static final String SORTKEY_COMMUNI_TITLE         = "title";
    // コミュニケーション履歴ソートキー  未読
    public static final String SORTKEY_COMMUNI_RECEIVE_TIMED = "receive_timed";
    // コミュニケーション履歴ソートキー  未読日数
    public static final String SORTKEY_COMMUNI_UNREAD_DAY    = "recvDay";

    //added by doi
    //メッセージ区分設定値
    public static final String MESSAGE_KBN_TO_MR = "1";
    public static final String MESSAGE_KBN_TO_DR = "2";
    public static final String MESSAGE_KBN_TO_OTHER = "3";

	public static final String MR_SESSION_ID = "MR_SESSION_ID";
	
    /**
     * SysCnst コンストラクター・コメント。
     */
    public SysCnst() {
    }
}
