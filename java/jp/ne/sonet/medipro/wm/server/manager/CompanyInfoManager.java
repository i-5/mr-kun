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
 * <strong>��Џ��Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CompanyInfoManager {
    protected Connection conn;  // �R�l�N�V�����E�I�u�W�F�N�g

    /** ��Џ��擾���N�G�X�g */
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
                                    
    /** �f�t�H���g�����N�擾���N�G�X�g */
    protected static final String COMPANY_SELECT_LINK_CD_SQL
	= "SELECT"
	+ " link_cd"
	+ " FROM"
	+ " company"
	+ " WHERE"
	+ " company.company_cd = ?"
	;
                                    
    /** �f�t�H���g�����N�X�V���N�G�X�g */
    protected static final String COMPANY_UPDATE_LINK_CD_SQL
	= "UPDATE company SET"
	+ " link_cd = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE company_cd = ?"
	;

    /** �f�t�H���g�^�[�Q�b�g�����N�擾 */
    protected static final String COMPANY_SELECT_TARGET_SQL
	= "SELECT"
	+ " target_rank"
	+ " FROM company"
	+ " WHERE"
	+ " company_cd = ?";

    /** �f�t�H���g�^�[�Q�b�g�����N�X�V */
    protected static final String COMPANY_UPDATE_TARGET_SQL
	= "UPDATE company SET"
	+ " target_rank = ?"
	+ ",update_userid = ?"
	+ ",update_time = SYSDATE"
	+ " WHERE company_cd = ?";

    /** �����L���O�\���ݒ�X�V */
    protected static final String COMPANY_UPDATE_RANKING_SQL
	= "UPDATE company SET"
	+ " display_ranking = ?"
	+ ",update_userid = ?"
	+ ",update_time = SYSDATE"
	+ " WHERE company_cd = ?";


    /** �v���t�B�b�N�X�R�[�h�擾���N�G�X�g */
    protected static final String COMPANY_SELECT_CD_PREFIX_SQL
	= "SELECT"
	+ " cd_prefix"
	+ " FROM company"
	+ " WHERE company_cd = ?";

    /** �ǉ���ЃR�[�h�擾 */
    protected static final String COMPANY_SELECT_NEXT_CD_SQL
	= "SELECT"
	+ " to_char(company_cd.nextval, '0000000000') AS next_company_cd"
	+ " FROM company";

    /**
     * CompanyInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public CompanyInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * ��Џ����擾����B
     * @param companyCd ��ЃR�[�h
     * @return ��Џ��
     */
    public CompanyInfo getCompanyInfo(String companyCd) {

	CompanyInfo info = null;
        try {
            //�r�p�k��
            String sqltxt = COMPANY_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, companyCd);
                // SQL ���s
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
     * �f�t�H���g�����N�����擾����B
     * @param companyCd ��ЃR�[�h
     * @return �����N�R�[�h
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
                // �p�����[�^��ݒ�
                pstmt.setString(1, companyCd);
                // SQL ���s
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
     * �f�t�H���g�����N�����X�V����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param companyCd ��ЃR�[�h
     * @param linkCd �����N�R�[�h
     */
    public void setDefaultLinkCd(Common common, String companyCd, String linkCd) {
        try {
	    String sqltxt = COMPANY_UPDATE_LINK_CD_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
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
     * prefix�R�[�h���擾����B
     * @param companyCd ��ЃR�[�h
     * @return prefix�R�[�h
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
                // �p�����[�^��ݒ�
                pstmt.setString(1, companyCd);
                // SQL ���s
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
     * �ǉ���ЃR�[�h���擾����B
     * @return �ǉ���ЃR�[�h
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
                // SQL ���s
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
     * �^�[�Q�b�g�����N�̎擾
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
     * �^�[�Q�b�g�����N�̍X�V.
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
     * �����L���O�\���ݒ�̍X�V.
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
