package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

/**
 * <h3>��Ѓe�[�u���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 05:35:29)
 * @author: 
 */
public class CompanyTableManager {
    protected Connection conn;
	
    protected static final String COMPANY_SQL
	= "SELECT"
	+ " company_cd"
	+ ",company_kbn"
	+ ",company_name"
	+ ",cd_prefix"
	+ ",picture_cd"
	+ ",link_cd"
	+ " FROM company"
	+ " WHERE company_kbn <> '0'"
	+ " ORDER BY company_name_kana";

    protected static final String COMPANY_MR_SQL
	= "SELECT com.company_cd com_company_cd,"
	+ "com.company_kbn com_company_kbn, "
	+ "com.company_name com_company_name, "
	+ "com.cd_prefix com_cd_prefix, "
	+ "com.picture_cd com_picture_cd, "
	+ "com.link_cd com_link_cd "
	+ "FROM company com, mr mr "
	+ "WHERE "
	+ "mr.mr_id = ? "
	+ "AND mr.company_cd = com.company_cd ";

    protected static final String COMPANY_CO_SQL
	= "SELECT com.company_cd com_company_cd,"
	+ "com.company_kbn com_company_kbn, "
	+ "com.company_name com_company_name, "
	+ "com.cd_prefix com_cd_prefix, "
	+ "com.picture_cd com_picture_cd, "
	+ "com.link_cd com_link_cd "
	+ "FROM company com "
	+ "WHERE "
	+ "com.company_cd = ?";

    protected static final String COMPANY_PREFIX_SQL
	= "SELECT com.company_cd com_company_cd,"
	+ "com.company_kbn com_company_kbn, "
	+ "com.company_name com_company_name, "
	+ "com.cd_prefix com_cd_prefix, "
	+ "com.picture_cd com_picture_cd, "
	+ "com.link_cd com_link_cd "
	+ "FROM company com "
	+ "WHERE "
	+ "com.cd_prefix = ?";

    protected static final String DEFAULT_TARGET_RANK_SQL
	= "SELECT"
	+ " company.target_rank"
	+ " FROM company, mr"
	+ " WHERE"
	+ " mr.mr_id = ?"
	+ " AND mr.company_cd = company.company_cd";

    /**
     * Default�^�[�Q�b�g�����N�̎擾.
     */
    public String getTargetRank(String mrId) {
	try {
	    PreparedStatement pstmt = conn.prepareStatement(DEFAULT_TARGET_RANK_SQL);
	    try {
		pstmt.setString(1, mrId);

		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) {
		    return rs.getString("target_rank");
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
	
	return null;
    }
									
    /**
     * CompanyTableManager �R���X�g���N�^�[�E�R�����g�B
     */
    public CompanyTableManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>��Ѓe�[�u�����̎擾(��ЃR�[�h)</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/02 �ߌ� 01:19:32)
     * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
     * @param companyCD java.lang.String
     */
    public CompanyTable getCompanyInfo(String companyCD) {
	String sqltxt;
	CompanyTable companytable = new CompanyTable();
	
	//��Ѓe�[�u�����̎擾
	try {
	    //��Ђr�p�k��
	    sqltxt = COMPANY_CO_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, companyCD);
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    companytable.setCompanyCD(rs.getString("com_company_cd"));
		    companytable.setCompanyKbn(rs.getString("com_company_kbn"));
		    companytable.setCompanyName(rs.getString("com_company_name"));
		    companytable.setCdPrefix(rs.getString("com_cd_prefix"));
		    companytable.setPictureCD(rs.getString("com_picture_cd"));
		    companytable.setLinkCD(rs.getString("com_link_cd"));

		    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
		    companytable.setLinklibinfo(linklibinfomanager.getLinkLibInfo(rs.getString("com_link_cd")));
				
		}
				
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return companytable;
    }

    /**
     * <h3>��Ѓe�[�u�����̎擾(�R�[�hprefix)</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/02 �ߌ� 01:19:32)
     * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
     * @param cdPrefix java.lang.String
     */
    public CompanyTable getCompanyPrefix(String cdPrefix) {
	String sqltxt;
	CompanyTable companytable = new CompanyTable();
	
	//��Ѓe�[�u�����̎擾
	try {

	    //��Ђr�p�k��
	    sqltxt = COMPANY_PREFIX_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, cdPrefix);
		ResultSet rs   = pstmt.executeQuery();
		while ( rs.next() ) {
		    companytable.setCompanyCD(rs.getString("com_company_cd"));
		    companytable.setCompanyKbn(rs.getString("com_company_kbn"));
		    companytable.setCompanyName(rs.getString("com_company_name"));
		    companytable.setCdPrefix(rs.getString("com_cd_prefix"));
		    companytable.setPictureCD(rs.getString("com_picture_cd"));
		    companytable.setLinkCD(rs.getString("com_link_cd"));

		    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
		    companytable.setLinklibinfo(linklibinfomanager.getLinkLibInfo(rs.getString("com_link_cd")));
				
		}
				
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return companytable;
    }

    /**
     * <h3>��Ѓe�[�u�����[�S��]�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/30 �ߌ� 06:49:58)
     * @return java.util.Enumeration (CompanyTable)
     */
    public Enumeration getCompanyTable() {
	Vector companytablelist = new Vector();
	String sqltxt;
	
	//��Ѓe�[�u�����̎擾
	try {
	    //��Ђr�p�k��
	    sqltxt = COMPANY_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    CompanyTable companytable = new CompanyTable();
		    companytable.setCompanyCD(rs.getString("company_cd"));
		    companytable.setCompanyKbn(rs.getString("company_kbn"));
		    companytable.setCompanyName(rs.getString("company_name"));
		    companytable.setCdPrefix(rs.getString("cd_prefix"));
		    companytable.setPictureCD(rs.getString("picture_cd"));
		    companytable.setLinkCD(rs.getString("link_cd"));

		    companytablelist.addElement(companytable);
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return companytablelist.elements();
    }

    /**
     * <h3>��Ѓe�[�u�����̎擾(�l�q�|�h�c)</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/02 �ߌ� 01:19:32)
     * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
     * @param mrID java.lang.String
     */
    public CompanyTable getCompanyTable(String mrID) {
	String sqltxt;
	CompanyTable companytable = new CompanyTable();
	
	//��Ѓe�[�u�����̎擾
	try {

	    //��Ђr�p�k��
	    sqltxt = COMPANY_MR_SQL;
		
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		pstmt.setString(1, mrID);
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    companytable.setCompanyCD(rs.getString("com_company_cd"));
		    companytable.setCompanyKbn(rs.getString("com_company_kbn"));
		    companytable.setCompanyName(rs.getString("com_company_name"));
		    companytable.setCdPrefix(rs.getString("com_cd_prefix"));
		    companytable.setPictureCD(rs.getString("com_picture_cd"));
		    companytable.setLinkCD(rs.getString("com_link_cd"));

		    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
		    companytable.setLinklibinfo(linklibinfomanager.getLinkLibInfo(rs.getString("com_link_cd")));
				
		}
	    } finally { 
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	return companytable;
    }

	public String getWebMasterEmailAddress(String companyCd) {
		try {
			String sql = "SELECT wm_mail_address from company where "
				+ "company_cd = '" + companyCd + "'";

			Statement st = conn.createStatement();

			try {
				ResultSet rs = st.executeQuery(sql);
				if (rs.next()) {
					return rs.getString("wm_mail_address");
				} else {
					throw new Exception("webmaster��Email�����ݒ�");
				}
			} finally {
				st.close();
			}
			
		} catch (Exception e) {
			throw new MrException(e);
		}
	}
}
