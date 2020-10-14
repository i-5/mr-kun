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
 * <strong>�����N���ޏ��Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkClassInfoManager {
    protected Connection conn;  // �R�l�N�V�����E�I�u�W�F�N�g

    /** �����N���ޏ��ꗗ�擾���N�G�X�g */
    protected static final String LINKCLASS_SELECT_LIST_SQL
	= "SELECT"
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ " FROM link_bunrui"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	;
                                    
    /** �����N���ޏ��擾���N�G�X�g */
    protected static final String LINKCLASS_SELECT_INFO_SQL
	= "SELECT"
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ " FROM link_bunrui"
	+ " WHERE"
	+ " link_bunrui_cd = ?"
	;
                                    
    /** �����N���폜���N�G�X�g */
    protected static final String LINKCLASS_DELETE_SQL
	= "UPDATE link_bunrui SET"
	+ "  update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", delete_ymd = SYSDATE"
	;

    /** �����N���X�V���N�G�X�g */
    protected static final String LINKCLASS_UPDATE_SQL
	= "UPDATE link_bunrui SET"
	+ "  link_bunrui_cd = ?"
	+ ", bunrui_name = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE link_bunrui_cd = ?"
	;

    /** �����N���ǉ����N�G�X�g */
    protected static final String LINKCLASS_INSERT_SQL
	= "INSERT INTO link_bunrui ("
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ ", update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, SYSDATE)"
	;

    /** �ǉ������N�R�[�h�擾 */
    protected static final String LINKCLASS_MAX_CD_SQL
	= "SELECT"
	+ " to_char(link_bunrui_cd.nextval, '0000') as next_link_bunrui_cd"
	+ " FROM dual";

    /**
     * LinkClassInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public LinkClassInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * �����N���ވꗗ�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code ��ЃR�[�h
     * @return �����N���ވꗗ���
     */
    public Enumeration getLinkClassList(Common common, String code) {

        Vector list = new Vector();
        
        try {
            //�r�p�k��
            String sqltxt = LINKCLASS_SELECT_LIST_SQL;
	    sqltxt += " ORDER BY link_bunrui_cd";
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
				// �p�����[�^��ݒ�
		pstmt.setString(1, code);
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                while ( rs.next() ) {
                    LinkClassInfo info = new LinkClassInfo();
                    info.setLinkBunruiCd(rs.getString("link_bunrui_cd"));
                    info.setBunruiName(rs.getString("bunrui_name"));
                    info.setCompanyCd(rs.getString("company_cd"));
		    list.addElement(info);
		}
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return list.elements();
    }

    /**
     * �����N���ޏ����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code �����N���ރR�[�h
     * @return �����N���ޏ��
     */
    public LinkClassInfo getLinkClassInfo(Common common, String code) {

	LinkClassInfo info = null;
        try {
            //�r�p�k��
            String sqltxt = LINKCLASS_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, code);
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    info = new LinkClassInfo();
                    info.setLinkBunruiCd(rs.getString("link_bunrui_cd"));
                    info.setBunruiName(rs.getString("bunrui_name"));
                    info.setCompanyCd(rs.getString("company_cd"));
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
     * �����N���ޏ����X�V����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���ޏ��
     */
    public void updateLinkClassInfo(Common common, LinkClassInfo info) {

        try {
	    String sqltxt = LINKCLASS_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, info.getLinkBunruiCd());
                pstmt.setString(2, info.getBunruiName());
                pstmt.setString(3, common.getMrId());
                pstmt.setString(4, info.getLinkBunruiCd());
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
     * �����N���ޏ���ǉ�����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���ޏ��
     * @return ���ۂɒǉ�������
     */
    public int insertLinkClassInfo(Common common, LinkClassInfo info) {

	int rc = 0;
		
	try {
	    conn.setAutoCommit(false);

	    // �����N���ރR�[�h�����̔�
	    String linkcd = "";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(LINKCLASS_MAX_CD_SQL);
	    if (rs.next()) {
		linkcd = (rs.getString("next_link_bunrui_cd")).trim();
	    }
	    stmt.close();
			
	    // �����N���ޏ��ǉ�
            PreparedStatement pstmt =
		conn.prepareStatement(LINKCLASS_INSERT_SQL);
	    pstmt.setString(1, linkcd);
	    pstmt.setString(2, info.getBunruiName());
            pstmt.setString(3, common.getCompanyCd());
	    pstmt.setString(4, common.getMrId());

	    rc = pstmt.executeUpdate();
            pstmt.close();
	    conn.commit();
        } catch (SQLException ex) {
	    try {
		conn.rollback();
		conn.setAutoCommit(true);
	    }
	    catch (SQLException e) {
	    }
            throw new WmException(ex);
        }
	return rc;
    }

}
