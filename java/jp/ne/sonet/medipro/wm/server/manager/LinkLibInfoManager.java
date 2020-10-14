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
 * <strong>�����N���C�u�������Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkLibInfoManager {
    protected Connection conn;  // �R�l�N�V�����E�I�u�W�F�N�g

    /** �����N���ꗗ�擾���N�G�X�g */
    protected static final String LINKLIBRARY_SELECT_SQL
	= "SELECT"
	+ " link_lib.link_cd"
	+ ",link_lib.company_cd"
	+ ",link_lib.link_bunrui_cd"
	+ ",link_bunrui.bunrui_name"
	+ ",link_lib.url"
	+ ",link_lib.description"
	+ ",link_lib.honbun_text"
	+ ",link_lib.picture"
	+ ",link_lib.niagai_link_kbn"
	+ " FROM"
	+ " link_lib, link_bunrui"
	+ " WHERE"
	+ " link_lib.company_cd = ?"
	+ " AND link_lib.link_bunrui_cd = link_bunrui.link_bunrui_cd (+)"
	+ " AND link_lib.delete_ymd IS NULL"
	;
                                    
    /** �����N���擾���N�G�X�g */
    protected static final String LINKLIBRARY_SELECT_INFO_SQL
	= LINKLIBRARY_SELECT_SQL
	+ " AND link_lib.link_cd = ?"
	;
                                    
    /** �����N���폜���N�G�X�g */
    protected static final String LINKLIBRARY_DELETE_SQL
	= "UPDATE link_lib SET"
	+ " update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", link_lib.delete_ymd = SYSDATE"
	;

    /** �����N���X�V���N�G�X�g */
    protected static final String LINKLIBRARY_UPDATE_SQL
	= "UPDATE link_lib SET"
	+ " link_bunrui_cd = ?"
	+ ", url = ?"
	+ ", honbun_text = ?"
	+ ", description = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE link_cd = ?"
	;

    /** �����N���ǉ����N�G�X�g */
    protected static final String LINKLIBRARY_INSERT_SQL
	= "INSERT INTO link_lib ("
	+ " link_cd, company_cd, link_bunrui_cd, url, honbun_text"
	+ ", description, picture, niagai_link_kbn, update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, ?, ?, NULL, NULL, ?, SYSDATE)"
	;

    /** �ǉ������N�R�[�h�擾 */
    protected static final String LINKLIBRARY_MAX_CD_SQL
	= "SELECT"
	+ " to_char(link_cd.nextval, '0000000000') as next_link_cd"
	+ " FROM dual";

    /**
     * LinkLibInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public LinkLibInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * �����N�ꗗ�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �����N�ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �����N�ꗗ���
     */
    public Enumeration getLinkLibList(Common common, LinkListSession session) {

        Vector list = new Vector();
        
        try {
            //�r�p�k��
            String sqltxt = LINKLIBRARY_SELECT_SQL;
            sqltxt += " ORDER BY "
		+ session.getSortKey() + " "
		+ session.getOrder();
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, common.getCompanyCd());
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                int i = 0;
                int begin = (session.getPage() - 1) * common.getLinkLine();
                int end = begin + common.getLinkLine();
                while ( rs.next() ) {
                    if ( i >= begin && i < end ) {
                        LinkLibInfo info = new LinkLibInfo();
                        info.setLinkCd(rs.getString("link_cd"));
                        info.setCompanyCd(rs.getString("company_cd"));
                        info.setLinkBunruiCd(rs.getString("link_bunrui_cd"));
                        info.setLinkBunruiName(rs.getString("bunrui_name"));
                        info.setUrl(rs.getString("url"));
                        info.setDescription(rs.getString("description"));
                        info.setHonbunText(rs.getString("honbun_text"));
                        info.setPicture(rs.getString("picture"));
                        info.setNaigaiLinkKbn(rs.getString("niagai_link_kbn"));
                        list.addElement(info);
                    }
                    if ( ++i >= end )   break;
                }
                session.setPrevPage(begin >= common.getLinkLine());
                session.setNextPage(i < end ? false : rs.next());
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
     * �����N�����폜����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param vec �폜�Ώۃ����N�R�[�h�̃��X�g
     */
    public void deleteLinkLibList(Common common, Vector vec) {
        try {
            String sqltxt = LINKLIBRARY_DELETE_SQL;
            sqltxt += " WHERE link_cd='" + (String)vec.elementAt(0) + "'";
            for ( int i = 1; i < vec.size(); i++ ) {
                sqltxt += " OR link_cd='" + (String)vec.elementAt(i) + "'";
            }
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, common.getMrId());
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
     * �����N�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param companyCd ��ЃR�[�h
     * @param linkCd �����N�R�[�h
     * @return �����N���
     */
    public LinkLibInfo getLinkLibInfo(Common common,
				      String companyCd,
				      String linkCd) {

        LinkLibInfo info = null;
        try {
            //�r�p�k��
            String sqltxt = LINKLIBRARY_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, companyCd);
                pstmt.setString(2, linkCd);
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    info = new LinkLibInfo();
                    info.setLinkCd(rs.getString("link_cd"));
                    info.setCompanyCd(rs.getString("company_cd"));
                    info.setLinkBunruiCd(rs.getString("link_bunrui_cd"));
                    info.setLinkBunruiName(rs.getString("bunrui_name"));
                    info.setUrl(rs.getString("url"));
                    info.setDescription(rs.getString("description"));
                    info.setHonbunText(rs.getString("honbun_text"));
                    info.setPicture(rs.getString("picture"));
                    info.setNaigaiLinkKbn(rs.getString("niagai_link_kbn"));
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
     * �����N�����X�V����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���
     */
    public void updateLinkLibInfo(Common common, LinkLibInfo info) {

        try {
            String sqltxt = LINKLIBRARY_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, info.getLinkBunruiCd());
                pstmt.setString(2, info.getUrl());
                pstmt.setString(3, info.getHonbunText());
                pstmt.setString(4, info.getDescription());
                pstmt.setString(5, common.getMrId());
                pstmt.setString(6, info.getLinkCd());
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
     * �����N����ǉ�����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �����N���
     * @return ���ۂɒǉ�������
     */
    public int insertLinkLibInfo(Common common, LinkLibInfo info) {

        int rc = 0;
        
        try {
            conn.setAutoCommit(false);

            // �����N�R�[�h�����̔�
            String linkcd = "";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(LINKLIBRARY_MAX_CD_SQL);
            if (rs.next()) {
                linkcd = (rs.getString("next_link_cd")).trim();
            }
            stmt.close();
            
            // �����N���ǉ�
            PreparedStatement pstmt =
		conn.prepareStatement(LINKLIBRARY_INSERT_SQL);
            pstmt.setString(1, linkcd);
            pstmt.setString(2, common.getCompanyCd());
            pstmt.setString(3, info.getLinkBunruiCd());
            pstmt.setString(4, info.getUrl());
            pstmt.setString(5, info.getHonbunText());
            pstmt.setString(6, info.getDescription());
            pstmt.setString(7, common.getMrId());

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
