package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>ＭＲ情報管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:52:18)
 * @author: 
 */
public class MrInfoManager {
    protected Connection conn;
    protected static final String MR_PASSWORD_UPDATESQL
	= "UPDATE mr SET password = ?, update_userid = ?, update_time = SYSDATE  WHERE mr_id = ? "; 
    protected static final String MR_LOGIN_UPDATESQL
	= "UPDATE mr SET previous_login_time = login_time, login_time = SYSDATE WHERE mr_id = ? "; 
    protected static final String MR_INFO_MAINSQL
	= "SELECT " 
	+ "mr.mr_id mr_id, "
        + "mr.company_cd mr_company_cd, " 
	+ "mr.jikosyokai mr_jikosyokai, " 
	+ "mr.name mr_name, "
	+ "mr.name_kana mr_name_kana, " 
	+ "mr.password mr_password, " 
	+ "mr.tel_no mr_tel_no, " 
	+ "mr.keitai_no mr_keitai_no, " 
	+ "mr.fax_no mr_fax_no, " 
	+ "mr.email mr_email, " 
	+ "mr.zip_cd mr_zip_cd, " 
	+ "mr.address mr_address, " 
	+ "mr.cc_email1 mr_cc_email1, " 
	+ "mr.cc_email2 mr_cc_email2, " 
	+ "mr.cc_email3 mr_cc_email3, " 
	+ "mr.cc_email4 mr_cc_email4, " 
	+ "mr.eigyo_date_kbn mr_eigyo_date_kbn, " 
	+ "mr.eigyo_time_kbn mr_eigyo_time_kbn, " 
	+ "TO_CHAR(mr.eigyo_start_time, 'HH24:MI') mr_eigyo_start_time, " 
	+ "TO_CHAR(mr.eigyo_end_time, 'HH24:MI') mr_eigyo_end_time, " 
	+ "mr.previous_login_time mr_previous_login_time, " 
	+ "comp.company_name comp_company_name, " 
	+ "comp.picture_cd comp_picture_cd, " 
	+ "mr.picture_cd mr_picture_cd, " 
	+ "mrya.mr_yakusyoku mrya_mr_yakusyoku " 
	+ "FROM " 
	+ "mr mr, " 
	+ "company comp, " 
	+ "mr_yakusyoku mrya ";

    protected static final String MR_INFO_OPTIONALSQL
	= "WHERE " 
	+ "mr.mr_id = ? " 
	+ "AND mr.company_cd = comp.company_cd " 
	+ "AND mr.mr_yakusyoku_cd = mrya.mr_yakusyoku_cd ";

    protected static final String MR_INFO_LOGIN_OPTIONALSQL
	= "WHERE mr.mr_id = ? "
	+ "AND mr.company_cd = comp.company_cd "
	+ "AND mr.mr_yakusyoku_cd = mrya.mr_yakusyoku_cd "
	+ "AND mr.delete_ymd is null ";

    protected static final String MR_INFO_CHECK_OPTIONALSQL
	= "WHERE " 
	+ "mr.mr_id = ? "
	+ "AND mr.company_cd = comp.company_cd " 
	+ "AND mr.mr_yakusyoku_cd = mrya.mr_yakusyoku_cd "
	+ "AND mr.delete_ymd is null ";

    protected static final String MR_PROFILE_UPDATE
	= "UPDATE mr SET " 
	+ "jikosyokai = ?," 
	+ "tel_no = ?," 
	+ "keitai_no = ?," 
	+ "fax_no = ?," 
	+ "email = ?," 
	+ "zip_cd = ?," 
	+ "address = ?," 
	+ "cc_email1 = ?," 
	+ "cc_email2 = ?," 
	+ "cc_email3 = ?," 
	+ "cc_email4 = ?," 
	+ "eigyo_date_kbn = ?," 
	+ "eigyo_time_kbn = ?," 
	+ "eigyo_start_time = TO_DATE(?,'HH24:MI')," 
	+ "eigyo_end_time = TO_DATE(?,'HH24:MI'), "
	+ "update_userid = ?, "
	+ "update_time = SYSDATE " 
	+ "WHERE " 
	+ "mr_id = ? ";

    protected static final String MR_WORKTIME_RANGE_CHECK_SQL
	= "select count(*) from mr "
	+ "where "
	+ "mr_id = ? "
	+ "and ((eigyo_date_kbn = '1' and (to_char(sysdate, 'D') = '1' or to_char(sysdate, 'D') = '7')) "
	+ "or (eigyo_date_kbn = '2' and to_char(sysdate, 'D') = '1') "
	+ "or (eigyo_time_kbn = '1' and (to_number(to_char(eigyo_start_time, 'hh24mi')) > "
	+ "to_number(to_char(sysdate, 'hh24mi')) "
	+ "or to_number(to_char(eigyo_end_time, 'hh24mi')) < "
	+ "to_number(to_char(sysdate, 'hh24mi'))))) ";
									
    /**
     * MrInfoManager コンストラクター・コメント。
     */
    public MrInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>ＭＲ情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:05:10)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param mrID java.lang.String
     */
    public MrInfo getMrInfo(String mrID) {

	MrInfo mrinfo = null;
	String sqltxt = null;
	ResultSet rs;
	PreparedStatement pstmt;
	
	try {
	
	    //ＳＱＬ文
	    sqltxt = MR_INFO_MAINSQL + MR_INFO_OPTIONALSQL;
	
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
		// SQL 実行
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    mrinfo = new MrInfo();
		    getMrInfoSub(rs, mrinfo);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return mrinfo;
    }

    /**
     * <h3>ＭＲ情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:05:10)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param mrID java.lang.String
     */
    public MrInfo getMrLoginInfo(String mrID) {

	MrInfo mrinfo = null;
	String sqltxt = null;
	ResultSet rs;
	PreparedStatement pstmt;
	
	try {
	
	    //ＳＱＬ文
	    sqltxt = MR_INFO_MAINSQL + MR_INFO_LOGIN_OPTIONALSQL;
	
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
		// SQL 実行
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    mrinfo = new MrInfo();
		    getMrInfoSub(rs, mrinfo);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return mrinfo;
    }

    /**
     * <h3>ＭＲ情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:05:10)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param mrID java.lang.String
     */
    public MrInfo getMrInfoCheck(String mrID) {

	MrInfo mrinfo = null;
	String sqltxt = null;
	ResultSet rs;
	PreparedStatement pstmt;
	
	try {
	
	    //ＳＱＬ文
	    sqltxt = MR_INFO_MAINSQL + MR_INFO_CHECK_OPTIONALSQL;
	
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
//  		pstmt.setString(2, companyCD);
		// SQL 実行
		rs   = pstmt.executeQuery();
		while (rs.next()) {
		    mrinfo = new MrInfo();
		    getMrInfoSub(rs, mrinfo);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return mrinfo;
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 07:46:44)
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    private void getMrInfoSub(ResultSet rs, MrInfo mrinfo) {

	try {
	    mrinfo.setCompanyCD(rs.getString("mr_company_cd"));
	    mrinfo.setJikosyokai(rs.getString("mr_jikosyokai"));
	    mrinfo.setId(rs.getString("mr_id"));
	    mrinfo.setName(rs.getString("mr_name"));
	    mrinfo.setNameKana(rs.getString("mr_name_kana"));
	    mrinfo.setPassword(rs.getString("mr_password"));
	    mrinfo.setTelNo(rs.getString("mr_tel_no"));
	    mrinfo.setKeitaiNo(rs.getString("mr_keitai_no"));
	    mrinfo.setFaxNo(rs.getString("mr_fax_no"));
	    mrinfo.setEmail(rs.getString("mr_email"));
	    mrinfo.setZipCD(rs.getString("mr_zip_cd"));
	    mrinfo.setAddress(rs.getString("mr_address"));
	    mrinfo.setCcEmail1(rs.getString("mr_cc_email1"));
	    mrinfo.setCcEmail2(rs.getString("mr_cc_email2"));
	    mrinfo.setCcEmail3(rs.getString("mr_cc_email3"));
	    mrinfo.setCcEmail4(rs.getString("mr_cc_email4"));
	    mrinfo.setEigyoDateKbn(rs.getString("mr_eigyo_date_kbn"));
	    mrinfo.setEigyoTimeKbn(rs.getString("mr_eigyo_time_kbn"));
	    mrinfo.setEigyoStartTime(rs.getString("mr_eigyo_start_time"));
	    mrinfo.setEigyoEndTime(rs.getString("mr_eigyo_end_time"));
	    mrinfo.setPreviousLoginTime(rs.getString("mr_previous_login_time"));
	    mrinfo.setCompanyName(rs.getString("comp_company_name"));
	    mrinfo.setMrYakusyoku(rs.getString("mrya_mr_yakusyoku"));
		
	    CatchPctInfoManager catchpct = new CatchPctInfoManager(conn);
            mrinfo.setPictureCd(rs.getString("mr_picture_cd"));
	    mrinfo.setMrCatchpctinfo(catchpct.getCatchPctInfo(rs.getString("mr_picture_cd")));
	    mrinfo.setCoCatchpctinfo(catchpct.getCatchPctInfo(rs.getString("comp_picture_cd")));		
	
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>ＭＲ情報の取得（コール内容、画像情報）</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:05:10)
     * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param mrID java.lang.String
     */
    public MrInfo getMrSpInfo(String mrID) {

	MrInfo mrinfo = null;
	String sqltxt = null;
	ResultSet rs;
	PreparedStatement pstmt;

	try {
	
	    //ＳＱＬ文
	    sqltxt = MR_INFO_MAINSQL + MR_INFO_OPTIONALSQL;
	
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
		// SQL 実行
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    mrinfo = new MrInfo();
		    getMrInfoSub(rs, mrinfo);

		    CallNaiyoInfoManager callnaiyouinfomanager = new CallNaiyoInfoManager(conn);
		    mrinfo.setCallnaiyoinfo(callnaiyouinfomanager.getCallNaiyou(rs.getString("mr_company_cd")));

		    CatchPctInfoManager catchpctinfomanager = new CatchPctInfoManager(conn);
		    mrinfo.setCatchpctinfo(catchpctinfomanager.getCatchPct(mrID));
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return mrinfo;
    }

    /**
     * <h3>ＭＲログインの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/29 午後 07:52:03)
     * @param mrID java.lang.String
     */
    public void updateLogin(String mrID) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;

	//ログイン更新
	sqltxt = MR_LOGIN_UPDATESQL;
	
	try {	
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, mrID);
		pstmt.executeUpdate();

	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    conn.commit();
		
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>ＭＲプロフィールの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午前 12:32:14)
     * @param tantoinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     * @param mrID java.lang.String
     */
    public void updateMrProfile(MrInfo mrinfo, String mrID) {

	String updsql;
	PreparedStatement pstmt = null;
		
	try {
		
	    conn.setAutoCommit(false);
		
	    //ＭＲプロフィール更新	
	    updsql = MR_PROFILE_UPDATE;
	    pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, mrinfo.getJikosyokai());
		pstmt.setString(2, mrinfo.getTelNo());
		pstmt.setString(3, mrinfo.getKeitaiNo());
		pstmt.setString(4, mrinfo.getFaxNo());
		pstmt.setString(5, mrinfo.getEmail());
		pstmt.setString(6, mrinfo.getZipCD());
		pstmt.setString(7, mrinfo.getAddress());
		pstmt.setString(8, mrinfo.getCcEmail1());
		pstmt.setString(9, mrinfo.getCcEmail2());
		pstmt.setString(10, mrinfo.getCcEmail3());
		pstmt.setString(11, mrinfo.getCcEmail4());
		pstmt.setString(12, mrinfo.getEigyoDateKbn());
		pstmt.setString(13, mrinfo.getEigyoTimeKbn());
		pstmt.setString(14, mrinfo.getEigyoStartTime());
		pstmt.setString(15, mrinfo.getEigyoEndTime());
		pstmt.setString(16, mrID);
		pstmt.setString(17, mrID);
		pstmt.executeUpdate();
	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    conn.commit();
		
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>ＭＲパスワードの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/29 午後 07:52:03)
     * @param mrID java.lang.String
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    public void updatePassword(String mrID, MrInfo mrinfo) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;

	//パスワード更新
	sqltxt = MR_PASSWORD_UPDATESQL;
	
	try {	
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, mrinfo.getPassword());
		pstmt.setString(2, mrID);
		pstmt.setString(3, mrID);
		pstmt.executeUpdate();

	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    conn.commit();
		
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * 営業時間のチェック.
     * @param  mrId チェック対象のMRID
     * @return 現在営業時間外だったらtrue
     */
    public boolean outOfWorkTime(String mrId) {
	int count = 0;

	try {
	    PreparedStatement pstmt = conn.prepareStatement(MR_WORKTIME_RANGE_CHECK_SQL);
	    pstmt.setString(1, mrId);

	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
		count = rs.getInt(1);
	    }

	    pstmt.close();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	if (count == 1) {
	    return true;
	}

	return false;
    }
}
