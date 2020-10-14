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
 * <strong>��^�����C�u�������Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfoManager {
    protected Connection conn;  // �R�l�N�V�����E�I�u�W�F�N�g

    /** ��^�����ꗗ�擾���N�G�X�g */
    protected static final String EXPRESSIONLIBRARY_SELECT_SQL
	= "SELECT"
	+ " teikeibun_lib.teikeibun_cd"
	+ ",teikeibun_lib.company_cd"
	+ ", teikeibun_bunrui.teikeibun_bunrui_cd"
	+ ", teikeibun_bunrui.bunrui_name"
	+ ", teikeibun_lib.description"
	+ ", teikeibun_lib.title"
	+ " FROM"
	+ " teikeibun_lib , teikeibun_bunrui "
	+ " WHERE"
	+ " teikeibun_bunrui.teikeibun_bunrui_cd=teikeibun_lib.teikeibun_bunrui_cd "
	+ " AND teikeibun_lib.company_cd = ?"
	+ " AND teikeibun_lib.delete_ymd IS NULL"
	;
                                    
    /** ��^�����擾���N�G�X�g */
    protected static final String EXPRESSIONLIBRARY_SELECT_INFO_SQL
	= "SELECT"
	+ " teikeibun_lib.teikeibun_cd"
	+ ",teikeibun_lib.company_cd"
	+ ", teikeibun_bunrui.teikeibun_bunrui_cd"
	+ ", teikeibun_bunrui.bunrui_name"
	+ ", teikeibun_lib.description"  
	+ ",teikeibun_lib.title"
	+ ",teikeibun_lib.honbun"
	+ " FROM"
	+ " teikeibun_lib , teikeibun_bunrui "
	+ " WHERE"
	+ " teikeibun_bunrui.teikeibun_bunrui_cd=teikeibun_lib.teikeibun_bunrui_cd "
	+ " AND teikeibun_lib.teikeibun_cd = ?"
	;
                                    
    /** ��^�����폜���N�G�X�g */
    protected static final String EXPRESSIONLIBRARY_DELETE_SQL
	= "UPDATE teikeibun_lib SET"
	+ " update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", teikeibun_lib.delete_ymd = SYSDATE"
	;

    /** ��^�����X�V���N�G�X�g */
    protected static final String EXPRESSIONLIBRARY_UPDATE_SQL
	= "UPDATE teikeibun_lib SET"
	+ " title = ?"
	+ ", description = ?"
	+ ", teikeibun_bunrui_cd = ?"
	+ ", honbun = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE teikeibun_cd = ?"
	;

    /** ��^�����ǉ����N�G�X�g */
    protected static final String EXPRESSIONLIBRARY_INSERT_SQL
	= "INSERT INTO teikeibun_lib ("
	+ " teikeibun_cd, company_cd, title, description" 
    + " ,teikeibun_bunrui_cd, honbun"
	+ ", update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE)"
	;

    /** �ǉ���^���R�[�h�擾 */
    protected static final String EXPRESSIONLIBRARY_MAX_CD_SQL
	= "SELECT"
	+ " to_char(teikeibun_cd.nextval, '0000000000') as next_teikeibun_cd"
	+ " FROM dual";

    /**
     * ExpressionLibInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public ExpressionLibInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * ��^���ꗗ�����擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param session ��^���ꗗ��ʂ̐ݒ����ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return ��^���ꗗ���
     */
    public Enumeration getExpressionLibList(Common common,
					    ExpressionListSession session) {

        Vector list = new Vector();
        
        try {
            //�r�p�k��
            String sqltxt = EXPRESSIONLIBRARY_SELECT_SQL;
	    sqltxt += " ORDER BY "
		+ session.getSortKey() + " "
		+ session.getOrder();
		System.out.println("ExpressionLibInfoManager.getExpressionLibList: sql="+sqltxt);
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, common.getCompanyCd());
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                int i = 0;
		int begin = (session.getPage() - 1) * common.getBunLine();
		int end = begin + common.getBunLine();
                while ( rs.next() ) {
		    if ( i >= begin && i < end ) {
			ExpressionLibInfo info = new ExpressionLibInfo();
			info.setTeikeibunCd(rs.getString("teikeibun_cd"));
			info.setCompanyCd(rs.getString("company_cd"));
			info.setBunruiCode(rs.getString("teikeibun_bunrui_cd"));
			info.setBunruiName(rs.getString("bunrui_name"));
			info.setDescription(rs.getString("description"));
			info.setTitle(rs.getString("title"));
			list.addElement(info);
		    }
                    if ( ++i >= end )	break;
                }
		session.setPrevPage(begin >= common.getBunLine());
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
     * ��^�������폜����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param vec �폜�Ώے�^���R�[�h�̃��X�g
     */
    public void deleteExpressionLibList(Common common, Vector vec) {
        try {
	    String sqltxt = EXPRESSIONLIBRARY_DELETE_SQL;
	    sqltxt += " WHERE teikeibun_cd='" + (String)vec.elementAt(0) + "'";
	    for ( int i = 1; i < vec.size(); i++ ) {
		sqltxt +=
		    " OR teikeibun_cd='" + (String)vec.elementAt(i) + "'";
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
     * ��^�������擾����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param companyCd ��ЃR�[�h
     * @param linkCd ��^���R�[�h
     * @return ��^�����
     */
    public ExpressionLibInfo getExpressionLibInfo(Common common,
						  String companyCd,
						  String teikeibunCd) {

	ExpressionLibInfo info = null;
        try {
            //�r�p�k��
            String sqltxt = EXPRESSIONLIBRARY_SELECT_INFO_SQL;
		System.out.println("sql="+sqltxt);
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, teikeibunCd);
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    info = new ExpressionLibInfo();
                    info.setTeikeibunCd(rs.getString("teikeibun_cd"));
                    info.setCompanyCd(rs.getString("company_cd"));
		    info.setBunruiCode(rs.getString("teikeibun_bunrui_cd"));
		    info.setBunruiName(rs.getString("bunrui_name"));
		    info.setDescription(rs.getString("description"));
                    info.setTitle(rs.getString("title"));
                    info.setHonbun(rs.getString("honbun"));
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
     * ��^�������X�V����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info ��^�����
     */
    public void updateExpressionLibInfo(Common common,
					ExpressionLibInfo info) {

        try {
	    String sqltxt = EXPRESSIONLIBRARY_UPDATE_SQL;
		System.out.println("sql="+sqltxt);
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, info.getTitle());
                pstmt.setString(2, info.getDescription());
                pstmt.setString(3, info.getBunruiCode());
                pstmt.setString(4, info.getHonbun());
                pstmt.setString(5, common.getMrId());
                pstmt.setString(6, info.getTeikeibunCd());
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
     * ��^������ǉ�����B
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param info ��^�����
     * @return ���ۂɒǉ�������
     */
    public int insertExpressionLibInfo(Common common,
				       ExpressionLibInfo info) {

	int rc = 0;
		
	try {
	    conn.setAutoCommit(false);

	    // ��^���R�[�h�����̔�
	    String code = "";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(EXPRESSIONLIBRARY_MAX_CD_SQL);
	    if (rs.next()) {
		code = (rs.getString("next_teikeibun_cd")).trim();
	    }
	    stmt.close();
		System.out.println("teikeibun_cd="+code);
			
	    // ��^�����ǉ�
            PreparedStatement pstmt =
		conn.prepareStatement(EXPRESSIONLIBRARY_INSERT_SQL);
	    pstmt.setString(1, code);
	    pstmt.setString(2, common.getCompanyCd());
	    System.out.println("company code="+common.getCompanyCd());
	    pstmt.setString(3, info.getTitle());
	    System.out.println("title="+info.getTitle());
	    pstmt.setString(4, info.getDescription());
	    System.out.println("description="+info.getDescription());
	    pstmt.setString(5, info.getBunruiCode());
	    System.out.println("bunrui code="+info.getBunruiCode());
	    pstmt.setString(6, info.getHonbun());
	    System.out.println("honbun="+info.getHonbun());
	    pstmt.setString(7, common.getMrId());
	    System.out.println("mr_id="+common.getMrId());

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
