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
 * <strong>�l�q�������Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeInfoManager {
    protected Connection conn;  // �R�l�N�V�����E�I�u�W�F�N�g

    /** �l�q�������ꗗ�擾���N�G�X�g */
    protected static final String ATTRIBUTE_SELECT_LIST_SQL
	= "SELECT"
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	;
                                    
    /** �l�q�������擾���N�G�X�g */
    protected static final String ATTRIBUTE_SELECT_INFO_SQL
	= "SELECT"
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " mr_attribute_cd = ?"
	;
                                    
    /** �����N���폜���N�G�X�g */
    protected static final String ATTRIBUTE_DELETE_SQL
	= "UPDATE mr_attribute SET"
	+ "  update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", delete_ymd = SYSDATE"
	;

    /** �����N���X�V���N�G�X�g */
    protected static final String ATTRIBUTE_UPDATE_SQL
	= "UPDATE mr_attribute SET"
	+ "  mr_attribute_cd = ?"
	+ ", mr_attribute_name = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE mr_attribute_cd = ?"
	;

    /** �����N���ǉ����N�G�X�g */
    protected static final String ATTRIBUTE_INSERT_SQL
	= "INSERT INTO mr_attribute ("
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ ", update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, SYSDATE)"
	;

    /** �ǉ������N�R�[�h�擾 */
    protected static final String ATTRIBUTE_MAX_CD_SQL
	= "SELECT"
	+ " to_char(mr_attribute_cd.nextval, '0000') as next_mr_attribute_cd"
	+ " FROM dual";

    /**
     * AttributeInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public AttributeInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * �l�q�����ꗗ�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session �l�q�����ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return �l�q�����ꗗ���
     */
    public Enumeration getAttributeList(Common common,
					AttributeListSession session) {

        Vector list = new Vector();
        
        try {
            //�r�p�k��
            String sqltxt = ATTRIBUTE_SELECT_LIST_SQL;
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
		int begin = (session.getPage()-1) * common.getAttributeLine();
		int end = begin + common.getAttributeLine();
                while ( rs.next() ) {
		    if ( i >= begin && i < end ) {
			AttributeInfo info = new AttributeInfo();
			info.setMrAttributeCd(
					      rs.getString("mr_attribute_cd"));
			info.setMrAttributeName(
						rs.getString("mr_attribute_name"));
			info.setCompanyCd(rs.getString("company_cd"));
			list.addElement(info);
		    }
                    if ( ++i >= end )	break;
                }
		session.setPrevPage(begin >= common.getAttributeLine());
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
     * �l�q�����ꗗ�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code ��ЃR�[�h
     * @return �l�q�����ꗗ���
     */
    public Enumeration getAttributeList(Common common, String code) {

        Vector list = new Vector();
        
        try {
            //�r�p�k��
            String sqltxt = ATTRIBUTE_SELECT_LIST_SQL;
	    sqltxt += " ORDER BY mr_attribute_cd ASC";
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
                    AttributeInfo info = new AttributeInfo();
                    info.setMrAttributeCd(rs.getString("mr_attribute_cd"));
                    info.setMrAttributeName(rs.getString("mr_attribute_name"));
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
     * �l�q���������폜����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param vec �폜�Ώۂl�q�����R�[�h�̃��X�g
     */
    public void deleteAttributeList(Common common, Vector vec) {
        try {
	    String sqltxt = ATTRIBUTE_DELETE_SQL;
	    sqltxt += " WHERE mr_attribute_cd='"
		+ (String)vec.elementAt(0) + "'";
	    for ( int i = 1; i < vec.size(); i++ ) {
		sqltxt += " OR mr_attribute_cd='"
		    + (String)vec.elementAt(i) + "'";
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
     * �l�q���������擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param code �l�q�����R�[�h
     * @return �l�q�������
     */
    public AttributeInfo getAttributeInfo(Common common, String code) {

	AttributeInfo info = null;
        try {
            //�r�p�k��
            String sqltxt = ATTRIBUTE_SELECT_INFO_SQL;
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
                    info = new AttributeInfo();
                    info.setMrAttributeCd(rs.getString("mr_attribute_cd"));
                    info.setMrAttributeName(rs.getString("mr_attribute_name"));
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
     * �l�q���������X�V����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �l�q�������
     */
    public void updateAttributeInfo(Common common, AttributeInfo info) {

        try {
	    String sqltxt = ATTRIBUTE_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, info.getMrAttributeCd());
                pstmt.setString(2, info.getMrAttributeName());
                pstmt.setString(3, common.getMrId());
                pstmt.setString(4, info.getMrAttributeCd());
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
     * �l�q��������ǉ�����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info �l�q�������
     * @return ���ۂɒǉ�������
     */
    public int insertAttributeInfo(Common common, AttributeInfo info) {

	int rc = 0;
		
	try {
	    conn.setAutoCommit(false);

	    // �l�q�����R�[�h�����̔�
	    String linkcd = "";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(ATTRIBUTE_MAX_CD_SQL);
	    if (rs.next()) {
		linkcd = (rs.getString("next_mr_attribute_cd")).trim();
	    }
	    stmt.close();
			
	    // �l�q�������ǉ�
            PreparedStatement pstmt =
		conn.prepareStatement(ATTRIBUTE_INSERT_SQL);
	    pstmt.setString(1, linkcd);
	    pstmt.setString(2, info.getMrAttributeName());
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
