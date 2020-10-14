package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>��t���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 09:02:23)
 * @author: 
 */
public class DoctorInfoManager {
    protected Connection conn;
	
    //��t���p�X���[�h�X�V�r�p�k
    protected static final String DR_PASSWORD_UPDATESQL
	= "UPDATE doctor SET password = ?, update_time = SYSDATE  WHERE dr_id = ? ";

    //��t���X�V�r�p�k
    protected static final String DR_NAME_INFO_UPDATESQL
	= "UPDATE doctor SET "
	+ "name = SUBSTRB(?,1,40), "
	+ "name_kana = SUBSTRB(?,1,40), "
	+ "update_time = SYSDATE, "
	+ "koumuin = ? "
	+ "WHERE dr_id = ? ";

    //�Ζ���X�V�r�p�k
    protected static final String DR_KINMUSAKI_UPDATESQL
	= "UPDATE kinmusaki SET "
	+ "kinmusaki_name = ?, "
	+ "update_time = SYSDATE "
	+ "WHERE dr_id = ? AND seq = 1";  
	
    //��t��񏑏o���r�p�k
    protected static final String DR_NAME_INFO_INSERTSQL
	= "INSERT INTO doctor ( "
	+ "dr_id, "
	+ "dr_license_cd, "
	+ "dcf_dr_cd, "
	+ "digital_id, "
	+ "syokusyu_cd, "
	+ "name, "
	+ "name_kana, "
	+ "handle, "
	+ "sex, "
	+ "birthday, "
	+ "email, "
	+ "so_net_userid, "
	+ "mediproclub_userid, "
	+ "toroku_ymd, "
	+ "update_time, "
	+ "delete_ymd, "
	+ "point, "
	+ "password, "
	+ "mrkun_mishiyo_flg, "
	+ "koumuin "
	+ ") " 
	+ "VALUES (  " 
	+ "?,  " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "SUBSTRB(?,1,40), " 
	+ "SUBSTRB(?,1,40), " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "SYSDATE, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "0, " 
	+ "NULL, " 
	+ "'1', "
	+ "? "
	+ ") ";

    //�Ζ��揑�o���r�p�k
    protected static final String DR_KINMUSAKI_INSERTSQL
	= "INSERT INTO kinmusaki ( " 
	+ "dr_id, " 
	+ "seq, " 
	+ "dcf_shisetsu_cd, " 
	+ "todofuken_cd, " 
	+ "kinmusaki_kbn, " 
	+ "kinmusaki_zip_cd, " 
	+ "kinmusaki_name, " 
	+ "kinmusaki_address, " 
	+ "kinmusaki_buill_name, " 
	+ "tel_no, " 
	+ "yakusyoku_name, " 
	+ "start_ymd, " 
	+ "end_ymd, " 
	+ "update_time " 
	+ ") " 
	+ "VALUES ( " 
	+ "?, " 
	+ "1, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "?, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL, " 
	+ "NULL " 
	+ ")";

    //�|�C���g���ZSQL
    protected static final String DR_POINTUP_SQL
	= "UPDATE doctor SET point = point + ? WHERE dr_id = ? ";

    //�|�C���g�����擾SQL
    protected static final String GET_POINTUP_SQL
	= "SELECT to_number(naiyo1) FROM constant_master "
	+ "WHERE constant_cd = 'CALL_POINT' ";

    //MR�V�K�o�^���|�C���g�A�b�vSQL
    protected static final String MRINPUT_POINTUP_SQL
	= "UPDATE doctor SET"
	+ " point = point + (SELECT to_number(naiyo1) "
	+                   "FROM constant_master "
	+                   "WHERE constant_cd = 'MRINPUT_POINT')"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND ruiseki_mr < 10";

    protected static final String DR_POINTDOWN_SQL
	= "UPDATE doctor SET"
	+ " point = point - (SELECT to_number(naiyo1) "
	+                   "FROM constant_master "
	+                   "WHERE constant_cd = 'CALL_POINT')"
	+ " WHERE"
	+ " dr_id = ?";

    protected static final String MRINPUT_SQL
	= "UPDATE doctor SET"
	+ " ruiseki_mr = ruiseki_mr + 1"
	+ " WHERE"
	+ " dr_id = ?";

/* y-yamada add start*/
    protected static final String GET_MR_COMPANY_CD_SQL
    = "SELECT company_cd FROM mr "
    + "WHERE mr_id = ?";
/* y-yamada add end*/

/* 1024 y-yamada add */
    protected static final String GET_DEFALT_TARGETRANK_SQL
    = "SELECT target_rank FROM company "
    + "WHERE company_cd = ?";
/* 1024 y-yamada add */
/* 1027 y-yamada add start */
    protected static final String GET_DEFALT_KINMUSAKI_SQL
    = "SELECT kinmusaki_name FROM kinmusaki "
    + "WHERE dr_id = ?";
/* 1027 y-yamada add end */

    //��t���̎擾(by SYSTEM_CD)
    protected static final String GET_DR_INFO_BY_SYSTEMCD
	= "SELECT"
	+  " dr.dr_id dr_dr_id"
	+  ",dr.system_cd dr_system_cd"
	+  ",dr.name dr_name"
	+  ",dr.name_kana dr_name_kana"
	+  ",kin.kinmusaki_name kin_kinmusaki_name"
	+  ",dr.mrkun_mishiyo_flg dr_mrkun_mishiyo_flg"
	+  ",dr.point dr_point"
	+  ",dr.password dr_password"
	+  " FROM"
	+  " doctor dr"
	+  ",kinmusaki kin"
	+  " WHERE"
	+  " dr.system_cd = ?"
	+  " AND dr.dr_id = kin.dr_id";

    /**
     * DoctorInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public DoctorInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>��t���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:56:41)
     * @return jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     * @param drID java.lang.String
     */
    public DoctorInfo getDoctorInfo(String drID) {
	String sqltxt;
	DoctorInfo doctorinfo = new DoctorInfo();
	
	try {
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt +  "dr.dr_id dr_dr_id, ";
	    sqltxt = sqltxt +  "dr.system_cd dr_system_cd, ";	//2001.01.14 M.Mizuki
	    sqltxt = sqltxt +  "dr.name dr_name, ";
	    sqltxt = sqltxt +  "dr.name_kana dr_name_kana, ";
	    sqltxt = sqltxt +  "kin.kinmusaki_name kin_kinmusaki_name, ";
	    sqltxt = sqltxt +  "dr.mrkun_mishiyo_flg dr_mrkun_mishiyo_flg, ";
	    sqltxt = sqltxt +  "dr.point dr_point, ";
	    sqltxt = sqltxt +  "dr.password dr_password ";
	    sqltxt = sqltxt +  "FROM ";
	    sqltxt = sqltxt +  "doctor dr, ";
	    sqltxt = sqltxt +  "kinmusaki kin ";
	    sqltxt = sqltxt +  "WHERE ";
	    sqltxt = sqltxt +  "dr.dr_id = '" + drID + "' ";
	    sqltxt = sqltxt +  "AND dr.dr_id = kin.dr_id ";
		
	    Statement stmt = conn.createStatement();
	    try {
		ResultSet rs = stmt.executeQuery(sqltxt);
			
		while ( rs.next() ) {
		    doctorinfo.setDrID(rs.getString("dr_dr_id"));
		    doctorinfo.setSysDrCD(rs.getString("dr_system_cd"));	//2001.01.14 M.Mizuki
		    doctorinfo.setName(rs.getString("dr_name"));
		    doctorinfo.setNameKana(rs.getString("dr_name_kana"));
		    doctorinfo.setKinmusakiName(rs.getString("kin_kinmusaki_name"));
		    doctorinfo.setMrkunMishiyoFlg(rs.getString("dr_mrkun_mishiyo_flg"));
		    doctorinfo.setPoint(rs.getInt("dr_point"));
		    doctorinfo.setPassword(rs.getString("dr_password"));
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return doctorinfo;
    }

    /**
     * <h3>�a�u��t��񁨂l�q�N��t���̏��o��</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 04:08:19)
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void insert(DoctorInfo doctorinfo) {
	String sqltxt;

	try {
	    //��t��񏑏o��
	    sqltxt = DR_NAME_INFO_INSERTSQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, doctorinfo.getDrID());
		pstmt.setString(2, doctorinfo.getName().substring(0,40));
		pstmt.setString(3, doctorinfo.getNameKana().substring(0,40));
		pstmt.setString(4, doctorinfo.getKoumuin());
		pstmt.execute();
	    } finally {
		pstmt.close();
	    }

	    //�Ζ��揑�o��
	    sqltxt = DR_KINMUSAKI_INSERTSQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, doctorinfo.getDrID());
		pstmt.setString(2, doctorinfo.getKinmusakiName());
		pstmt.execute();
	    } finally {
		pstmt.close();
	    }
		
	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>�a�u��t��񁨂l�q�N��t���̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 04:08:19)
     * @param doctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     */
    public void update(DoctorInfo doctorinfo) {
	String sqltxt;

	try {
	    //��t���X�V
	    sqltxt = DR_NAME_INFO_UPDATESQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, doctorinfo.getName());
		pstmt.setString(2, doctorinfo.getNameKana());
		pstmt.setString(3, doctorinfo.getKoumuin());
		pstmt.setString(4, doctorinfo.getDrID());
		pstmt.execute();

	    } finally {
		pstmt.close();
	    }

	    //�Ζ���X�V
	    sqltxt = DR_KINMUSAKI_UPDATESQL;
		
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, doctorinfo.getKinmusakiName());
		pstmt.setString(2, doctorinfo.getDrID());
		pstmt.execute();

	    } finally {
		pstmt.close();
	    }
		
	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>��t�p�X���[�h�̍X�V</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/29 �ߌ� 07:52:03)
     * @param mrID java.lang.String
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    public void updatePassword(String drID, DoctorInfo doctorinfo) {
	String sqltxt;

	//�p�X���[�h�X�V
	sqltxt = DR_PASSWORD_UPDATESQL;
	
	try {	
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, doctorinfo.getPassword());
		pstmt.setString(2, drID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>�|�C���g�A�b�v</h3>
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/29 �ߌ� 07:52:03)
     * @param mrID java.lang.String
     * @param mrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
     */
    public int updatePoint(String drId) {
	int updateCount = 0;
	
	try {
	    try {
		conn.setAutoCommit(false);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(GET_POINTUP_SQL);
		int point = 0;

		if (rs.next()) {
		    point = rs.getInt(1);
		}

		rs.close();
		stmt.close();

		PreparedStatement pstmt = conn.prepareStatement(DR_POINTUP_SQL);
		pstmt.setInt(1, point);
		pstmt.setString(2, drId);

		updateCount = pstmt.executeUpdate();

		pstmt.close();

		conn.commit();
	    } catch (SQLException e) {
		conn.rollback();
		throw e;
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return updateCount;
    }

    public int pointDown(String drId) {
	int updateCount = 0;
	
	try {
	    try {
		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement(DR_POINTDOWN_SQL);
		pstmt.setString(1, drId);

		updateCount = pstmt.executeUpdate();

		pstmt.close();

		conn.commit();
	    } catch (SQLException e) {
		conn.rollback();
		throw e;
	    } finally {
		conn.setAutoCommit(true);
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	return updateCount;
    }
    /* 1023 y-yamada add  start */
    public String getCompanyCD(String mr_id) {
		//mr_id���� companyCD���擾
    	try
    	{
		    conn.setAutoCommit(false);
			String company_cd = null;
		    PreparedStatement pstmt = null;
    	
		    pstmt = conn.prepareStatement(GET_MR_COMPANY_CD_SQL);
			try 
			{
				pstmt.setString(1, mr_id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
				    company_cd = rs.getString("company_cd");
				}
				rs.close();
				return company_cd;
			}
			finally 
			{
				pstmt.close();
				pstmt = null;
		    }
		}
		 catch (SQLException ex) {
		    try {
			conn.rollback();
		    } catch (SQLException e) { }
		    throw new MrException(ex);
		} finally {
		    try {
			conn.setAutoCommit(false);
		    } catch (SQLException e) { }
		}
	}	



    public String getTargetRank(String companycd) {
		//companycd���� TargetRank���擾
    	try
    	{
		    conn.setAutoCommit(false);
			String targetRank = null;
		    PreparedStatement pstmt = null;
    	
		    pstmt = conn.prepareStatement(GET_DEFALT_TARGETRANK_SQL);
			try 
			{
				pstmt.setString(1, companycd);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
				    targetRank = rs.getString("target_rank");
				}
				rs.close();
				return targetRank;
			}
			finally 
			{
				pstmt.close();
				pstmt = null;
		    }
		}
		 catch (SQLException ex) {
		    try {
			conn.rollback();
		    } catch (SQLException e) { }
		    throw new MrException(ex);
		} finally {
		    try {
			conn.setAutoCommit(false);
		    } catch (SQLException e) { }
		}
	}	

    /* 1023 y-yamada add  end */

/***********************************************/
/*        1027 y-yamada add start             **/
/* �Ζ���e�[�u������f�t�H���g�̋Ζ�����擾 **/
/***********************************************/
    public String getKinmusaki(String dr_id) {
    	try
    	{
		    conn.setAutoCommit(false);
			String kinmusaki = null;
		    PreparedStatement pstmt = null;
    	
		    pstmt = conn.prepareStatement(GET_DEFALT_KINMUSAKI_SQL);
			try 
			{
				pstmt.setString(1, dr_id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
				    kinmusaki = rs.getString("kinmusaki_name");
				}
				rs.close();
				return kinmusaki;
			}
			finally 
			{
				pstmt.close();
				pstmt = null;
		    }
		}
		 catch (SQLException ex) {
		    try {
			conn.rollback();
		    } catch (SQLException e) { }
		    throw new MrException(ex);
		} finally {
		    try {
			conn.setAutoCommit(false);
		    } catch (SQLException e) { }
		}
	}	
/********** 1027 y-yamada add end ***************/

	
	
//    public int updateMrInput(String drId) {
    public int updateMrInput(String drId ,String mr_id) { //y-yamada add 
	int updateCount = 0;
	
	try {
	    conn.setAutoCommit(false);
		String company_cd = null;
	    PreparedStatement pstmt = null;

			/*y-yamada add start �J���p�j�[�R�[�h�@0.10.12�@��,�|�C���g�����Z���Ȃ��悤�ɕύX*/
		    pstmt = conn.prepareStatement(GET_MR_COMPANY_CD_SQL);
		    try {
				pstmt.setString(1, mr_id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
				    company_cd = rs.getString("company_cd");
				}
				rs.close();
		    }
		     finally {
				pstmt.close();
				pstmt = null;
		    }
			/*y-yamada add end */
		if (company_cd != null
		&&  !company_cd.equals("0000000000")  //y-yamada add
		&&  !company_cd.equals("0000000010") // y-yamaa add
		&&  !company_cd.equals("0000000012") // y-yamaa add
		) {									 // y-yamaa add
		    try {
				pstmt = conn.prepareStatement(MRINPUT_POINTUP_SQL);
				pstmt.setString(1, drId);

				updateCount = pstmt.executeUpdate();
			    } finally {
				pstmt.close();
			    }
		}									 // y-yamaa add

	    try {
		pstmt = conn.prepareStatement(MRINPUT_SQL);
		pstmt.setString(1, drId);

		updateCount = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException ex) {
	    try {
		conn.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	} finally {
	    try {
		conn.setAutoCommit(false);
	    } catch (SQLException e) { }
	}

	return updateCount;
    }

    /**
     * <h3>��t���̎擾(by SYSTEM_CD)</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:56:41)
     * @return jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
     * @param drID java.lang.String
     */
    public DoctorInfo getDoctorInfoBySystemCd(String SystemCd) {
	String sqltxt;
	DoctorInfo doctorinfo = new DoctorInfo();
	
	try {
	    PreparedStatement pstmt = conn.prepareStatement(GET_DR_INFO_BY_SYSTEMCD);
	    try {
		pstmt.setString(1, SystemCd);
		ResultSet rs = pstmt.executeQuery();
			
		while ( rs.next() ) {
		    doctorinfo.setDrID(rs.getString("dr_dr_id"));
		    doctorinfo.setSysDrCD(rs.getString("dr_system_cd"));
		    doctorinfo.setName(rs.getString("dr_name"));
		    doctorinfo.setNameKana(rs.getString("dr_name_kana"));
		    doctorinfo.setKinmusakiName(rs.getString("kin_kinmusaki_name"));
		    doctorinfo.setMrkunMishiyoFlg(rs.getString("dr_mrkun_mishiyo_flg"));
		    doctorinfo.setPoint(rs.getInt("dr_point"));
		    doctorinfo.setPassword(rs.getString("dr_password"));
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return doctorinfo;
    }


}
