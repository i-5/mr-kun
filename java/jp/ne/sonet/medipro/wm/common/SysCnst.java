package jp.ne.sonet.medipro.wm.common;

/**
 * Medipro 共通コンスタンス
 * <br>
 * サーバ、クライアント間で共通に使用するコンスタンス.
 * 
 * @author
 * @version
 */
public class SysCnst {

    ////////////////////////////////////////////////////////////////
    // DB接続情報
    /** ドライバクラス名 */
    public static final String DB_DRIVER = "weblogic.jdbc.pool.Driver";
    /** URL */
    public static final String DB_URL = "jdbc:weblogic:pool:oraclePool";
    /** URL(テスト用) */
    public static final String DB_TEST_URL = "jdbc:oracle:thin:@ultra5:1521:TICD";
    /** ドライバクラス名(テスト用) */
    public static final String DB_TEST_DRIVER = "oracle.jdbc.driver.OracleDriver";
    /** ユーザID(テスト用) */
    public static final String DB_TEST_USER = "MWMDBOWN";
    /** パスワード(テスト用) */
    public static final String DB_TEST_PASS = "MWMDBOWN";

    ////////////////////////////////////////////////////////////////
    // エントリポイント
    /** Servletエントリポイント */
    public static final String SERVLET_ENTRY_POINT;
    /** HTMLエントリポイント */
    public static final String HTML_ENTRY_POINT = "/medipro/";

    ////////////////////////////////////////////////////////////////
    // セッション情報取得キー
    /** 共用セッション情報取得キー */
    public static final String KEY_COMMON_SESSION = "wm_common";
    /** MR一覧セッションKey */
    public static final String KEY_MRLIST_SESSION = "WM_MR_LIST";
    /** MR変更・追加セッションKey */
    public static final String KEY_MRUPDATE_SESSION = "WM_MR_UPDATE";
    /** MR担当顧客変更セッションKEY */
    public static final String KEY_MRCLIENT_SESSION = "WM_MR_CLIENT";
    /** MRキャッチ画像一覧セッションKEY*/
    public static final String KEY_MRCATCHLIST_SESSION = "WM_MR_CATCH_LIST";
    /** MRキャッチ画像追加・変更セッションKEY*/
    public static final String KEY_MRCATCHUPDATE_SESSION = "WM_MR_CATCH_UPDATE";
    /** サブマスター一覧画面用セッション情報取得キー */
    public static final String KEY_SUBLIST_SESSION = "WM_SUB_LIST";
    /** サブマスター追加・変更画面用セッション情報取得キー */
    public static final String KEY_SUBUPDATE_SESSION = "WM_SUB_UPDATE";
    /** 会社キャッチ画像一覧画面用セッション情報取得キー */
    public static final String KEY_CATCH_SESSION = "WM_CATCH_LIST";
    /** 会社キャッチ画像追加・更新画面用セッション情報取得キー */
    public static final String KEY_CATCHUPDATE_SESSION = "WM_CATCH_UPDATE";
    /** リンク一覧画面用セッション情報取得キー */
    public static final String KEY_LINKLIST_SESSION = "WM_LINK_LIST";
    /** リンク追加・変更画面用セッション情報取得キー */
    public static final String KEY_LINKUPDATE_SESSION = "WM_LINK_UPDATE";
    /** リンク分類追加・変更画面用セッション情報取得キー */
    public static final String KEY_LINKCLASSUPDATE_SESSION = "WM_LINK_CLASS_UPDATE";
    /** 定型文一覧画面用セッション情報取得キー */
    public static final String KEY_EXPRESSIONLIST_SESSION = "WM_EXPRESSION_LIST";
    /** 定型文追加・変更画面用セッション情報取得キー */
    public static final String KEY_EXPRESSIONUPDATE_SESSION = "WM_EXPRESSION_UPDATE";
    /** hb010814 added this to handle template categories */
    public static final String KEY_TEIKEICLASSUPDATE_SESSION = "WM_TEIKEI_CLASS_UPDATE";

    /** コール内容一覧画面用セッション情報取得キー */
    public static final String KEY_CALLLIST_SESSION = "WM_CALL_LIST";
    /** コール内容追加・変更画面用セッション情報取得キー */
    public static final String KEY_CALLUPDATE_SESSION = "WM_CALL_UPDATE";
    /** 支店・営業所一覧画面用セッション情報取得キー */
    public static final String KEY_BRANCHLIST_SESSION = "WM_BRANCH_LIST";
    /** 支店・営業所追加・変更画面用セッション情報取得キー */
    public static final String KEY_BRANCHUPDATE_SESSION = "WM_BRANCH_UPDATE";
    /** 属性一覧画面用セッション情報取得キー */
    public static final String KEY_ATTRIBUTELIST_SESSION = "WM_ATTRIBUTE_LIST";
    /** 属性追加・変更画面用セッション情報取得キー */
    public static final String KEY_ATTRIBUTEUPDATE_SESSION = "WM_ATTRIBUTE_UPDATE";
    /** 重要度一覧画面用セッション情報取得キー */
    public static final String KEY_ACTION_SESSION = "WM_ACTION";
    /** 重要度追加・変更画面用セッション情報取得キー */
    public static final String KEY_ACTIONUPDATE_SESSION = "WM_ACTION_UPDATE";
    /** ランキング設定画面用セッション情報取得キー */
    public static final String KEY_ACTIONRANKING_SESSION = "WM_ACTION_RANKING";

    ////////////////////////////////////////////////////////////////
    // 各画面共通情報
    /** マスターフラグ(ウェブ) */
    public static final String FLG_MASTER_WEB = "1";
    /** マスターフラグ(サブ) */
    public static final String FLG_MASTER_SUB = "2";
    /** マスター権限(支店) */
    public static final String FLG_AUTHORITY_BRANCH = "1";
    /** マスター権限(営業所) */
    public static final String FLG_AUTHORITY_OFFICE = "2";
    /** マスター権限(属性１) */
    public static final String FLG_AUTHORITY_ATTRIBUTE1 = "1";
    /** マスター権限(属性２) */
    public static final String FLG_AUTHORITY_ATTRIBUTE2 = "2";
    
    ////////////////////////////////////////////////////////////////
    // サブマスター一覧画面情報
    /** サブマスター一覧デフォルトソートキー */
    public static final String SORTKEY_SUBMASTER_LIST = "mr.mr_id";
    /** サブマスター一覧デフォルトオーダー */
    public static final String ORDER_SUBMASTER_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // リンク一覧画面情報
    /** リンク一覧デフォルトソートキー */
    public static final String SORTKEY_LINK_LIST = "link_lib.link_bunrui_cd";
    /** リンク一覧デフォルトオーダー */
    public static final String ORDER_LINK_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // 属性一覧画面情報
    /** 属性一覧デフォルトソートキー */
    public static final String SORTKEY_ATTRIBUTE_LIST =        "mr_attribute.mr_attribute_cd";
    /** 属性一覧デフォルトオーダー */
    public static final String ORDER_ATTRIBUTE_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // 定型文一覧画面情報
    /** 定型文一覧デフォルトソートキー */
    public static final String SORTKEY_EXPRESSION_LIST = "teikeibun_lib.title";
    /** 定型文一覧デフォルトオーダー */
    public static final String ORDER_EXPRESSION_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // 支店・営業所画面
    /** 支店・営業所メッセージ(メッセージなし) */
    public static final int BRANCH_LIST_MSG_NONE = 0;
    /** 支店・営業所メッセージ(MSG1) */
    public static final int BRANCH_LIST_MSG_PLURAL = 1;
    /** 支店・営業所メッセージ(MSG2) */
    public static final int BRANCH_LIST_MSG_NOCHECK = 2;
    /** 支店・営業所メッセージ(MSG3) */
    public static final int BRANCH_LIST_MSG_CONFIRM = 3;
    /** 支店・営業所メッセージ(MSG4) */
    public static final int BRANCH_LIST_MSG_CANNOT = 4;
    /** 支店・営業所メッセージ(MSG5) */
    public static final int BRANCH_LIST_MSG_DONE = 5;
    
    /** 支店・営業所メッセージ(メッセージなし) */
    public static final int BRANCH_UPDATE_MSG_NONE = 0;
    /** 支店・営業所メッセージ(MSG1) */
    public static final int BRANCH_UPDATE_MSG_CONFIRM = 1;
    /** 支店・営業所メッセージ(MSG2) */
    public static final int BRANCH_UPDATE_MSG_NOSELECT = 2;
    /** 支店・営業所メッセージ(MSG3) */
    public static final int BRANCH_UPDATE_MSG_DONE = 3;

    ////////////////////////////////////////////////////////////////
    // 会社キャッチ画像
    /** 会社キャッチ画像一覧メッセージ(メッセージなし) */
    public static final int CATCH_LIST_MSG_NONE = 0;
    /** 会社キャッチ画像一覧メッセージ(MSG1) */
    public static final int CATCH_LIST_MSG_NOCHECK = 1;
    /** 会社キャッチ画像一覧メッセージ(MSG2) */
    public static final int CATCH_LIST_MSG_DELETE = 2;
    /** 会社キャッチ画像一覧メッセージ(MSG3) */
    public static final int CATCH_LIST_MSG_DEFAULT = 3;
    /** 会社キャッチ画像一覧メッセージ(MSG4) */
    public static final int CATCH_LIST_MSG_UPDATE = 4;
    /** 会社キャッチ画像一覧メッセージ(MSG5) */
    public static final int CATCH_LSIT_MSG_SAVEDONE = 5;
    /** 会社キャッチ画像一覧メッセージ(MSG6) */
    public static final int CATCH_LIST_MSG_DELDONE = 6;
        
    /** 会社キャッチ画像一覧画像形式(上下) */
    public static final int PICTURE_TYPE_VERTICAL = 1;
    /** 会社キャッチ画像一覧画像形式(左右) */
    public static final int PICTURE_TYPE_HORIZONTAL = 2;
    /** 会社キャッチ画像一覧(全面) */
    public static final int PICTURE_TYPE_WHOLE = 3;

    /** 会社キャッチ画像追加・更新メッセージ(メッセージなし) */
    public static final int CATCH_UPDATE_MSG_NONE = 0;
    /** 会社キャッチ画像追加・更新メッセージ(MSG1) */
    public static final int CATCH_UPDATE_MSG_CONFIRM = 1;
    /** 会社キャッチ画像追加・更新メッセージ(MSG2) */
    public static final int CATCH_UPDATE_MSG_DONE = 2;

    //////////////////////////////////////////////////////////////////////
    // MR管理
    /** MR役職コードDefault値 */
    public static final String MR_YAKUSYOKU_CD_DEFAULT = "0001";
    /** MR営業日区分Default値 */
    public static final String MR_EIGYO_DATE_KBN_DEFAULT = "0";
    /** MR営業時間区分Default値 */
    public static final String MR_EIGYO_TIME_KBN_DEFAULT = "0";
    /** MR営業開始時間Default値 */
    public static final String MR_EIGYO_START_TIME_DEFAULT = "to_date('0900', 'hh24mi')";
    /** MR営業終了時間Default値 */
    public static final String MR_EIGYO_END_TIME_DEFAULT = "to_date('1800', 'hh24mi')";
    /** ウェブマスタ表記名 */
    public static final String WEBMASTER_LABEL = "ウェブ";
    /** サブマスタ表記名 */
    public static final String SUBMASTER_LABEL = "サブ";

    ////////////////////////////////////////////////////////////////
    // 重要度一覧画面情報
    /** 重要度一覧デフォルトソートキー */
    public static final String SORTKEY_ACTION_LIST = "action.target_name";
    /** 重要度一覧デフォルトオーダー */
    public static final String ORDER_ACTION_LIST = "ASC";
    

    /** ファイルセパレータ */
//      public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String SEPARATOR = "/";
        
    /** デバッグフラグ */
    public static final boolean DEBUG = false;

    /** テスト環境フラグ */
//    public static final boolean TEST = true;
      public static final boolean TEST = false;

    static {
        if (TEST) {
            SERVLET_ENTRY_POINT = "/medipro/servlet/";
        } else {
            SERVLET_ENTRY_POINT = "/";
        }
    }

    /**
     * SysCnst コンストラクター・コメント。
     */
    public SysCnst() {
    }
}
