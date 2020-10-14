package jp.ne.sonet.medipro.wm.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.common.*; 

/**
 * <strong>会社情報管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CompanyInfoManager {
    protected Connection conn;  // コネクション・オブジェクト

    /** 会社情報取得リクエスト */
    protected static final String COMPANY_SELECT_INFO_SQL
	= "SELECT"
	+ " company.company_cd"
	+ ",company.company_kbn"
	+ ",company_kbn.company_kbn_naiyo"
	+ ",company.company_name"
	+ ",company.cd_prefix"
	+ ",company.picture_cd"
	+ ",company.link_cd"
	+ ",company.display_ranking"
	+ " FROM"
	+ " company, company_kbn"
	+ " WHERE"
	+ " company.company_cd = ?"
	+ " AND company.company_kbn = company_kbn.company_kbn"
	;
                                    
    /** デフォルトリンク取得リクエスト */
    protected static final String COMPANY_SELECT_LINK_CD_SQL
	= "SELECT"
	+ " link_cd"
	+ " FROM"
	+ " company"
	+ " WHERE"
	+ " company.company_cd = ?"
	;
                                    
    /** デフォルトリンク更新リクエスト */
    protected static final String COMPANY_UPDATE_LINK_CD_SQL
	= "UPDATE company SET"
	+ " link_cd = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE company_cd = ?"
	;

    /** デフォルトターゲットランク取得 */
    protected static final String COMPANY_SELECT_TARGET_SQL
	= "SELECT"
	+ " target_rank"
	+ " FROM company"
	+ " WHERE"
	+ " company_cd = ?";

    /** デフォルトターゲットランク更新 */
    protected static final String COMPANY_UPDATE_TARGET_SQL
	= "UPDATE company SET"
	+ " target_rank = ?"
	+ ",update_userid = ?"
	+ ",update_time = SYSDATE"
	+ " WHERE company_cd = ?";

    /** ランキング表示設定更新 */
    protected static final String COMPANY_UPDATE_RANKING_SQL
	= "UPDATE company SET"
	+ " display_ranking = ?"
	+ ",update_userid = ?"
	+ ",update_time = SYSDATE"
	+ " WHERE company_cd = ?";


    /** プレフィックスコード取得リクエスト */
    protected static final String COMPANY_SELECT_CD_PREFIX_SQL
	= "SELECT"
	+ " cd_prefix"
	+ " FROM company"
	+ " WHERE company_cd = ?";

    /** 追加会社コード取得 */
    protected static final String COMPANY_SELECT_NEXT_CD_SQL
	= "SELECT"
	+ " to_char(company_cd.nextval, '0000000000') AS next_company_cd"
	+ " FROM company";

    /**
     * CompanyInfoManager オブジェクトを新規に作成する。
     * @param conn コネクション・オブジェクト
     */
    public CompanyInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * 会社情報を取得する。
     * @param companyCd 会社コード
     * @return 会社情報
     */
    public CompanyInfo getCompanyInfo(String companyCd) {

	CompanyInfo info = null;
        try {
            //ＳＱＬ文
            String sqltxt = COMPANY_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL 文を設定
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, companyCd);
                // SQL 実行
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    info = new CompanyInfo();
                    info.setCompanyCd(rs.getString("company_cd"));
                    info.setCompanyKbn(rs.getString("company_kbn"));
                    info.setCompanyKbnNaiyo(rs.getString("company_kbn_naiyo"));
                    info.setCompanyName(rs.getString("company_name"));
                    info.setCdPrefix(rs.getString("cd_prefix"));
                    info.setPictureCd(rs.getString("picture_cd"));
                    info.setLinkCd(rs.getString("link_cd"));
                    info.setDisplayRanking(rs.getString("display_ranking"));
                }
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return info;
    }

    /**
     * デフォルトリンク情報を取得する。
     * @param companyCd 会社コード
     * @return リンクコード
     */
    public String getDefaultLinkCd(String companyCd) {
	String linkCd = "";
        try {
	    String sqltxt = COMPANY_SELECT_LINK_CD_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, companyCd);
                // SQL 実行
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    linkCd = rs.getString("link_cd");
                }
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return linkCd;
    }

    /**
     * デフォルトリンク情報を更新する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param companyCd 会社コード
     * @param linkCd リンクコード
     */
    public void setDefaultLinkCd(Common common, String companyCd, String linkCd) {
        try {
	    String sqltxt = COMPANY_UPDATE_LINK_CD_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, linkCd);
                pstmt.setString(2, common.getMrId());
                pstmt.setString(3, companyCd);
                pstmt.executeUpdate();
            }
            finally {
                pstmt.close();
            }
            conn.commit();
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * prefixコードを取得する。
     * @param companyCd 会社コード
     * @return prefixコード
     */
    public String getCdPrefix(String companyCd) {
	String cdPrefix = "";
        try {
	    String sqltxt = COMPANY_SELECT_CD_PREFIX_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, companyCd);
                // SQL 実行
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    cdPrefix = rs.getString("cd_prefix");
                }
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return cdPrefix;
    }

    /**
     * 追加会社コードを取得する。
     * @return 追加会社コード
     */
    public String getNextCompanyCd() {
	String maxCompanyCd = "";
        try {
	    String sqltxt = COMPANY_SELECT_NEXT_CD_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // SQL 実行
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    maxCompanyCd = rs.getString("next_company_cd");
                }
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return maxCompanyCd;
    }

    /**
     * ターゲットランクの取得
     */
    public String getTargetRank(String companyCd) {
	try {
	    PreparedStatement pstmt = conn.prepareStatement(COMPANY_SELECT_TARGET_SQL);
	    try {
		pstmt.setString(1, companyCd);
		
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    return rs.getString("target_rank");
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return "";
    }

    /**
     * ターゲットランクの更新.
     */
    public int updateTargetRank(String companyCd,
				String mrId,
				String targetRank) {
	int updateCount = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(COMPANY_UPDATE_TARGET_SQL);
	    try {
		pstmt.setString(1, targetRank);
		pstmt.setString(2, mrId);
		pstmt.setString(3, companyCd);

		updateCount = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return updateCount;
    }

    /**
     * ランキング表示設定の更新.
     */
    public int updateDisplayRanking(String companyCd,
				String mrId,
				String displayRanking) {
	int updateCount = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(COMPANY_UPDATE_RANKING_SQL);
	    try {
		pstmt.setString(1, displayRanking);
		pstmt.setString(2, mrId);
		pstmt.setString(3, companyCd);

		updateCount = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return updateCount;
    }

}
