package jp.ne.sonet.medipro.wm.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>コール内容情報管理</strong>
 * @author
 * @version
 */
public class CallInfoManager {
    protected Connection conn;

    /** 基本SQL */
    protected static final String CALL_INFO_MAINSQL =
	"SELECT call_naiyo_cd, call_naiyo " +
	"FROM call_naiyo " +
	"WHERE delete_ymd IS NULL AND company_cd = ";

    /** コール内容更新SQL */
    protected static final String CALL_UPDATE_SQL =
	"UPDATE call_naiyo SET call_naiyo = ? " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** コール内容追加SQL */
    protected static final String CALL_INSERT_SQL =
	"INSERT INTO call_naiyo VALUES(?, ?, ?, ?, SYSDATE, NULL)";

    /** コール内容削除SQL */
    protected static final String CALL_DELETE_SQL =
	"UPDATE call_naiyo SET delete_ymd = SYSDATE";

    /** コール内容重複検査SQL */
    protected static final String CALL_OVERLAP_SQL =
	"SELECT call_naiyo FROM call_naiyo " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** コール内容検索SQL */
    protected static final String CALL_SEARCH_SQL =
	"SELECT call_naiyo FROM call_naiyo " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** コール内容コードを使用しているメッセージIDを取得するSQL */
    protected static final String MESSAGE_SEARCH_SQL =
	"SELECT message_id " +
	"FROM message_body";

    /**
     * CallInfoManager オブジェクトを新規に作成する。
     * @param conn コネクションオブジェクト
     */
    public CallInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * コール内容情報(ソート指定)の取得
     * @param ses セッションオブジェクト
     * @return コール内容情報(各要素はCallInfoオブジェクト)
     */
    public Vector getCallInfo(HttpSession ses) {
        ResultSet rs;
        Statement stmt;
        CallInfo  callInfo = null;
        Vector    callList;
        String    sql;
        int       cnt;
        int       curRow;
        int       pageRow;

        callList = new Vector();
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        CallListSession callSes = (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);

        try {
            // SQL 生成
            sql = CALL_INFO_MAINSQL;
            sql += "'" + comSes.getCompanyCd() + "'";
            sql += " ORDER BY " + callSes.getSortKey() + " " + callSes.getOrder();

            stmt = conn.createStatement();
            try {
                // SQL 実行
		if (SysCnst.DEBUG) {
		    System.err.println("### CallInfoManager : sql = " + sql);
		}
                rs      = stmt.executeQuery(sql);
                curRow  = callSes.getCurrentRow();
                pageRow = comSes.getCallLine();
                cnt     = 0;
                while (rs.next()) {
                    callInfo = new CallInfo();
                    callInfo.setCallCD(rs.getString("call_naiyo_cd"));
                    callInfo.setCallNaiyo(rs.getString("call_naiyo"));

                    // ページ開始行からページ表示行数のみ格納する
                    if (cnt >= (curRow - 1) && cnt < (curRow + pageRow - 1)) {
                        callList.add(callInfo);
                    }
                    cnt++;
                }

                // 前ページ/次ページの有無を設定
                callSes.setPrev(curRow != 1);
                callSes.setNext(cnt - curRow >= pageRow);
            }
            catch (SQLException e) {
                throw new WmException(e);
            }
            finally {
                stmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return callList;
    }

    /**
     * コール内容コードを削除する
     * @param ses      セッションオブジェクト
     * @param callList コール内容コードリスト(各要素はStringオブジェクト)
     * @return 1:削除できた 0:削除できなかった
     */
    public int deleteCallInfo(HttpSession ses, Vector callList) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        try {
            String    sql;
            ResultSet rs;
            Statement stmt;

            // メッセージ本体でコール内容コードを使用しているものが無いか確認
            sql = MESSAGE_SEARCH_SQL;
            sql += " WHERE call_naiyo_cd = '" + callList.elementAt(0) + "'";
            for (int i = 1; i < callList.size(); i++) {
                sql += " OR call_naiyo_cd = '" + callList.elementAt(i) + "'";
            }
	    if (SysCnst.DEBUG) {
		System.out.println("### CallInfoManager : sql = " + sql);
	    }
            stmt = conn.createStatement();
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next() == true) {
                    // 削除不可
                    return 0;
                }
            }
            finally {
                stmt.close();
            }

            // 削除処理
            sql = CALL_DELETE_SQL;
            sql += " WHERE call_naiyo_cd = '" + callList.elementAt(0) + "'";
            for (int i = 1; i < callList.size(); i++) {
                sql += " OR call_naiyo_cd = '" + callList.elementAt(i) + "'";
            }
	    if (SysCnst.DEBUG) {
		System.out.println("### CallInfoManager : sql = " + sql);
	    }
            stmt = conn.createStatement();
            try {
                int cnt = stmt.executeUpdate(sql);
                return (cnt != 0) ? 1 : 0;
            }
            finally {
                stmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * コール内容を追加する
     * @param ses         セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @param callNaiyo   コール内容
     * @return 追加した数
     */
    public int insertCallInfo(HttpSession ses, String callNaiyoCd, String callNaiyo) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        int rc;

        try {
            pstmt = conn.prepareStatement(CALL_INSERT_SQL);
            try {
                // コール内容追加
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                pstmt.setString(3, callNaiyo);
                pstmt.setString(4, comSes.getMrId());
                rc = pstmt.executeUpdate();
                return rc;
            }
            finally {
                pstmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * コール内容を変更する
     * @param ses         セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @param callNaiyo   コール内容
     * @return 変更した数
     */
    public int updateCallInfo(HttpSession ses, String callNaiyoCd, String callNaiyo) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        int rc;

        try {
            pstmt = conn.prepareStatement(CALL_UPDATE_SQL);
            try {
                // コール内容変更
                pstmt.setString(1, callNaiyo);
                pstmt.setString(2, callNaiyoCd);
                pstmt.setString(3, comSes.getCompanyCd());
                rc = pstmt.executeUpdate();
                return rc;
            }
            finally {
                pstmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * コール内容の重複検査
     * @param ses         セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @return 重複していた場合はtrue
     */
    public boolean isOverlap(HttpSession ses, String callNaiyoCd) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        ResultSet rs;

        try {
            pstmt = conn.prepareStatement(CALL_OVERLAP_SQL);
            try {
                // 重複検査
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                rs = pstmt.executeQuery();
                return rs.next();
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * コール内容コードからコール内容を検索する
     * @param ses セッションオブジェクト
     * @param callNaiyoCd コール内容コード
     * @return コール内容
     */
    public String searchCallNaiyo(HttpSession ses, String callNaiyoCd) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        ResultSet rs;
        String callNaiyo = null;

        try {
            pstmt = conn.prepareStatement(CALL_SEARCH_SQL);
            try {
                // 検索
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    callNaiyo = rs.getString("call_naiyo");
                }
                return callNaiyo;
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }
}
