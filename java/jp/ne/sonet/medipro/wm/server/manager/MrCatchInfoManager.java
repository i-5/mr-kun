package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrCatchUpdateSession;
import jp.ne.sonet.medipro.wm.server.entity.CatchInfo;
import jp.ne.sonet.medipro.wm.common.exception.WmException; 
import jp.ne.sonet.medipro.wm.common.SysCnst; 

/**
 * <strong>MRキャッチ画像管理クラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchInfoManager {
    /** キャッチ画像一覧取得 */
    protected static final String MR_CATCH_LIST_SQL
        = "SELECT"
        + " catch_picture.picture_cd"
        + ",catch_picture.picture_name"
        + ",catch_picture.picture"
        + " FROM catch_picture"
        + " WHERE"
        + " catch_picture.mr_id = ?"
        + " AND catch_picture.delete_ymd IS NULL";

    /** キャッチ画像削除 */
    protected static final String MR_CATCH_REMOVE_SQL
        = "UPDATE catch_picture SET"
        + " delete_ymd = SYSDATE"
        + " WHERE"
        + " picture_cd = ?";

    /** キャッチ画像情報取得 */
    protected static final String MR_CATCH_INFO_SQL
        = "SELECT"
        + " catch_picture.picture_cd"
        + ",catch_picture.picture_name"
        + ",catch_picture.picture"
        + ",catch_picture.mr_id"
	+ ",mr.picture_cd AS defaultCd"
        + " FROM catch_picture, mr "
        + " WHERE"
        + " catch_picture.picture_cd = ?"
	+ " AND catch_picture.mr_id = mr.mr_id (+)"
	+ " AND catch_picture.picture_cd = mr.picture_cd (+)";

    /** キャッチ画像シーケンス */
    protected static final String MR_CATCH_SEQ_SQL
        = "SELECT TO_CHAR(picture_cd.nextval, '0000000') as next_code from dual";

    /** キャッチ画像追加 */
    protected static final String MR_CATCH_INSERT_SQL
        = "INSERT INTO catch_picture "
        + "(picture_cd"
        + ",company_cd"
        + ",mr_id"
        + ",picture_name"
        + ",picture"
        + ",picture_type"
        + ",update_userid"
        + ",update_time"
        + ") values ("
        + " ?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",SYSDATE"
        + ")";

    /** キャッチ画像情報更新 */
    protected static final String MR_CATCH_UPDATE_SQL
        = "UPDATE catch_picture SET"
        + " picture_name = ?"
        + ",picture = ?"
        + ",update_userid = ?"
        + ",update_time = SYSDATE"
        + " WHERE"
        + " picture_cd = ?";

    /** 会社プレフィックス取得 */
    protected static final String COMPANY_CD_PREFIX_SQL
        = "SELECT"
        + " cd_prefix"
        + " FROM"
        + " company"
        + " WHERE"
        + " company_cd = ?";

    /** コネクション */
    protected Connection connection = null;

    /**
     * Connectionの保持.
     * @param initConnection DBとの接続
     */
    public MrCatchInfoManager(Connection initConnection) {
        connection = initConnection;
    }

    /**
     * MRキャッチ画像一覧を取得する.
     * @param  mrId          対象となるMRのID
     * @param  sortDirection ソート方向
     * @return キャッチ画像情報
     */
    public Vector getMrCatchList(String mrId, String sortDirection) {
        Vector list = new Vector();

        try {
            String sql = MR_CATCH_LIST_SQL + " ORDER BY catch_picture.picture_name " + sortDirection;

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, mrId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CatchInfo info = new CatchInfo();
                info.setPictureCD(rs.getString("picture_cd"));
                info.setPictureName(rs.getString("picture_name"));
                info.setPicture(rs.getString("picture"));
                list.addElement(info);
            }

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return list;
    }

    /**
     * 指定した画像コードのキャッチ画像をすべて削除する.
     * @param  pictureCds 画像コード配列
     * @return 実際に削除した数
     */
    public int removeCatchPictures(String[] pictureCds) {
        int rc = 0;

        try {
            PreparedStatement pstmt = connection.prepareStatement(MR_CATCH_REMOVE_SQL);

            for (int i = 0; i < pictureCds.length; i++) {
                pstmt.setString(1, pictureCds[i]);
                rc += pstmt.executeUpdate();
            }
            
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return rc;
    }

    /**
     * MRキャッチ画像情報を取得する.
     * @param pictureCd 画像コード
     * @return 画像情報
     */
    public CatchInfo getMrCatchInfo(String pictureCd) {
        CatchInfo info = new CatchInfo();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(MR_CATCH_INFO_SQL);
            pstmt.setString(1, pictureCd);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                info.setPictureCD(rs.getString("picture_cd"));
                info.setPictureName(rs.getString("picture_name"));
                info.setPicture(rs.getString("picture"));
                info.setMrId(rs.getString("mr_id"));
		if (rs.getString("defaultCd") != null) {
		    info.setDefaultFlg(true);
		} else {
		    info.setDefaultFlg(false);
		}
		System.err.println(rs.getString("defaultCd"));
            }
            
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return info;
    }

    /**
     * 新規追加時の画像コードを取得する.
     * @return 画像コード
     * @throws SQLException 
     */
    private String getNextSequence() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(MR_CATCH_SEQ_SQL);
        rs.next();
        String seq = rs.getString("next_code").trim();
        stmt.close();

        return seq;
    }

    /**
     * 会社プレフィックスコードを取得する。
     * @param companyCd 会社コード
     * @return プレフィックスコード
     * @throws SQLException 
     */
    private String getCampanyCdPrefix(String companyCd) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(COMPANY_CD_PREFIX_SQL);
        pstmt.setString(1, companyCd);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String cdPrefix = rs.getString("cd_prefix");
        pstmt.close();

        return cdPrefix;
    }

    /**
     * MRキャッチ画像を追加する.
     * @param session セッション情報
     * @return 実際に追加した数
     */
    public int insertMrCatchInfo(HttpSession session) {
        Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
        MrCatchUpdateSession mcuSession
	    = (MrCatchUpdateSession)session.getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        CatchInfo info = mcuSession.getCatchInfo();
        int insertCount = 0;

        try {
            //自動Commitオフ
            connection.setAutoCommit(false);

            String seq = getNextSequence();
            String cdPrefix = getCampanyCdPrefix(common.getCompanyCd());
            String path = common.getCatchHome() + SysCnst.SEPARATOR + cdPrefix + SysCnst.SEPARATOR
                + seq.charAt(seq.length()-1) + SysCnst.SEPARATOR + seq + "." + mcuSession.getExtension();
            info.setPicture(path);

            //追加
            PreparedStatement pstmt = connection.prepareStatement(MR_CATCH_INSERT_SQL);
            pstmt.setString(1, seq);
            pstmt.setString(2, common.getCompanyCd());
            pstmt.setString(3, mcuSession.getMrId());
            pstmt.setString(4, info.getPictureName());
            pstmt.setString(5, info.getPicture());
            pstmt.setString(6, new Integer(SysCnst.PICTURE_TYPE_HORIZONTAL).toString());
            pstmt.setString(7, common.getMrId());
            
	    try {
		insertCount = pstmt.executeUpdate();
		connection.commit();
	    } catch (SQLException e) {
                connection.rollback();
	    } finally {
                connection.setAutoCommit(true);
	    }
	    mcuSession.setPictureCd(seq);
            pstmt.close();
        } catch (Exception ex) {
            throw new WmException(ex);
	}

        return insertCount;
    }

    /**
     * MRキャッチ画像情報を更新する.
     * @param session セッション情報
     * @return 実際に更新した数
     */
    public int updateMrCatchInfo(HttpSession session) {
        Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
        MrCatchUpdateSession mcuSession
	    = (MrCatchUpdateSession)session.getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        CatchInfo info = mcuSession.getCatchInfo();
        int updateCount = 0;

        try {
	    if (mcuSession.getTempPicture() != null) {
		String cdPrefix = getCampanyCdPrefix(common.getCompanyCd());
		String path = common.getCatchHome() + SysCnst.SEPARATOR + cdPrefix + SysCnst.SEPARATOR
		    + info.getPictureCD().charAt(info.getPictureCD().length()-1) + SysCnst.SEPARATOR + info.getPictureCD()
		    + "." + mcuSession.getExtension();
		info.setPicture(path);
	    }

            PreparedStatement pstmt = connection.prepareStatement(MR_CATCH_UPDATE_SQL);
            pstmt.setString(1, info.getPictureName());
            pstmt.setString(2, info.getPicture());
            pstmt.setString(3, common.getMrId());
            pstmt.setString(4, info.getPictureCD());

            updateCount = pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception ex) {
            throw new WmException(ex);
        }

        return updateCount;
    }
}
