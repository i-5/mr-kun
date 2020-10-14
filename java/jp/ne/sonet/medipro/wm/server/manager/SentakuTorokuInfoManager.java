package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.server.entity.SentakuTorokuInfo;

/**
 * <strong>選択登録テーブルManagerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class SentakuTorokuInfoManager {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** 指定したMR管理下DRリスト取得 */
    protected static final String DR_LIST_SQL
        = "SELECT"
        + " sentaku_toroku.dr_id"
        + ",sentaku_toroku.mr_id"
        + ",sentaku_toroku.seq"
        + ",sentaku_toroku.name"
        + ",sentaku_toroku.kinmusaki"
        + ",sentaku_toroku.maker_shisetsu_id"
        + ",doctor.name AS dr_name"
        + " FROM sentaku_toroku, doctor"
        + " WHERE"
        + " sentaku_toroku.mr_id = ?"
        + " AND sentaku_toroku.dr_id = doctor.dr_id";

    /** 選択登録追加 */
    protected static final String SENTAKU_TOROKU_INSERT_SQL
        = "INSERT INTO sentaku_toroku ( "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME " //1218 更新時間を追加する
        + ") "
        + "SELECT "
        + "DR_ID,"
        + "?,"
        + "?,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "SYSDATE "//1218 更新時間を入れる
        + "FROM "
        + "sentaku_toroku "
        + "WHERE "
        + "dr_id = ? "
        + "AND mr_id = ? "
        + "AND seq = ?";

    /** 選択登録履歴追加 */
    protected static final String SENTAKU_TOROKU_HIST_INSERT_SQL
        = "INSERT INTO sentaku_toroku_hist ( "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME "//1221 アップデートタイムも移動
        + ") "
        + "SELECT "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "SYSDATE,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME "//1221 アップデートタイムも移動
        + "FROM "
        + "sentaku_toroku "
        + "WHERE "
        + "dr_id = ? "
        + "AND mr_id = ? "
        + "AND seq = ?";

    /** 選択登録履歴削除 */
    protected static final String SENTAKU_TOROKU_DELETE_SQL
        = "DELETE FROM sentaku_toroku"
        + " WHERE dr_id = ?"
        + " AND mr_id = ?"
        + " AND seq = ?";

    /** 選択登録シーケンス */
    protected static final String SENTAKU_TOROKU_SEQ_SQL
        = "SELECT TO_CHAR(sentaku_toroku_seq.nextval,'000') counter FROM dual";

    /** 担当DR存在チェック */
    protected static final String IN_CHARGE_COUNT_SQL
	= "SELECT count(*)"
        + " FROM sentaku_toroku"
        + " WHERE"
        + " sentaku_toroku.mr_id = ?";

    /** 担当関係カウント　*/
    protected static final String SENTAKU_TOROKU_COUNT_SQL
	= "SELECT"
	+ " count(*) counter"
	+ " FROM sentaku_toroku"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND mr_id = ?";

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** DBへのコネクション */
    protected Connection connection = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * DBへの接続に使うconnectionを設定する。
     * @param initConnection connectionオブジェクト
     */
    public SentakuTorokuInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * 指定したMR-IDで、選択登録テーブルからDR一覧を取得する。
     * @param  mrId 絞り込み対象のMR-ID
     * @return DR一覧
     */
    public Vector getDrList(String mrId) {
        Vector list = new Vector();

        try {
            PreparedStatement pstmt = connection.prepareStatement(DR_LIST_SQL);
            
	    try {
		pstmt.setString(1, mrId);

		ResultSet rs = pstmt.executeQuery();
            
		while (rs.next()) {
		    SentakuTorokuInfo info = new SentakuTorokuInfo();
		    info.setDrId(rs.getString("dr_id"));
		    info.setMrId(rs.getString("mr_id"));
		    info.setSeq(rs.getInt("seq"));
		    info.setName(rs.getString("name"));
		    info.setKinmusaki(rs.getString("kinmusaki"));
		    info.setMakerShisetsuId(rs.getString("maker_shisetsu_id"));
		    info.setDrName(rs.getString("dr_name"));

		    list.addElement(info);
		}
	    } finally {
		pstmt.close();
	    }
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return list;
    }

    /**
     * 指定したMRが管理するDRを他のMRの管理に移し、元のレコードを選択登録履歴テーブルに移す。
     * @param drId     移動対象レコードのDR-ID
     * @param fromMrId 移動元レコードのMR-ID
     * @param toMrId   移動先レコードのMR-ID
     * @param seq      移動対象レコードのシーケンス
     */
    public void moveSentakuToroku(String drId, String fromMrId, String toMrId, int seq) {
        try {
            //自動Commitオフ
            connection.setAutoCommit(false);

            //新しい関係のシーケンスを取得
            Statement stmt = connection.createStatement();
	    String seqCount = null;

	    try {
		ResultSet rs = stmt.executeQuery(SENTAKU_TOROKU_SEQ_SQL);
		if (rs.next()) {
		    seqCount = rs.getString("counter").trim();
		}
	    } finally {
		stmt.close();
	    }

	    //既に関係を持っていないかカウント
	    int relationCount = 0;
	    PreparedStatement pstmt
		= connection.prepareStatement(SENTAKU_TOROKU_COUNT_SQL);
	    
	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, toMrId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
		    relationCount = rs.getInt("counter");
		}
	    } finally {
		pstmt.close();
	    }

	    //現在担当関係にない場合新しい関係にコピー
	    if (relationCount == 0) {
		pstmt = connection.prepareStatement(SENTAKU_TOROKU_INSERT_SQL);

		try {
		    pstmt.setString(1, toMrId);
		    pstmt.setInt(2, new Integer(seqCount).intValue());
		    pstmt.setString(3, drId);
		    pstmt.setString(4, fromMrId);
		    pstmt.setInt(5, seq);

		    int rc = pstmt.executeUpdate();
		} finally {
		    pstmt.close();
		}
	    }

            //元の関係を履歴にコピー
            pstmt = connection.prepareStatement(SENTAKU_TOROKU_HIST_INSERT_SQL);

	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, fromMrId);
		pstmt.setInt(3, seq);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

            //元の関係を削除
            pstmt = connection.prepareStatement(SENTAKU_TOROKU_DELETE_SQL);

	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, fromMrId);
		pstmt.setInt(3, seq);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
            
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
            }

            throw new WmException(ex);
        } finally {
            try {
		connection.setAutoCommit(true);
            } catch (SQLException e) {
            }
	}
    }

    /**
     * 指定したMR-IDで、選択登録テーブルに担当関係にあるDR数を取得する.
     * @param  mrId 検査対象のMR-ID
     * @return 担当しているDR数
     */
    public int getDrCountInCharge(String mrId) {
	int chargeCount = 0;

        try {
            PreparedStatement pstmt = connection.prepareStatement(IN_CHARGE_COUNT_SQL);

            try {
		pstmt.setString(1, mrId);
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    chargeCount = rs.getInt(1);
		}
	    } finally {
		pstmt.close();
	    }
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

	return chargeCount;
    }
}
