package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.util.Enumeration;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.SubListSession;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.common.exception.WmException; 
import jp.ne.sonet.medipro.wm.common.SysCnst; 
import java.util.*; 

/**
 * <strong>�l�q���Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
/* <hb010717
 * this class needs to be rewritten to be (quasi-) persistent
 * right now it gets re-created per request/need
 * this has the effect of destroying all info retrieved from the database at login time
 */

public class MrInfoManager {
    /** �T�u�}�X�^�[�ꗗ�擾���N�G�X�g */
    protected static String MR_SUBMASTER_MAIN_SQL
        = "SELECT"
        + " mr.mr_id, mr.name, mr.master_kengen_soshiki,"
        + " mr.master_kengen_attribute, shiten.shiten_name,"
        + " eigyosyo.eigyosyo_name, mr_attribute.mr_attribute_name"
        + " FROM"
        + " shiten, eigyosyo, mr_attribute, "
        + "((SELECT"
        + " mr_id, shiten_cd, eigyosyo_cd, name,"
        + " master_kengen_soshiki, master_kengen_attribute,"
        + " mr_attribute_cd1 attr_cd "
        + "FROM mr "
        + "WHERE company_cd = ?"
        + " AND master_flg = '" + SysCnst.FLG_MASTER_SUB + "'"
        + " AND master_kengen_attribute = '" + SysCnst.FLG_AUTHORITY_ATTRIBUTE1 + "'"
        + " AND delete_ymd IS NULL"
        + ")"
        + " UNION (SELECT"
        + " mr_id, shiten_cd, eigyosyo_cd, name,"
        + " master_kengen_soshiki, master_kengen_attribute,"
        + " mr_attribute_cd2 attr_cd "
        + "FROM mr "
        + "WHERE company_cd = ?"
        + " AND master_flg = '" + SysCnst.FLG_MASTER_SUB + "'"
        + " AND master_kengen_attribute = '" + SysCnst.FLG_AUTHORITY_ATTRIBUTE2 + "'"
        + " AND delete_ymd IS NULL"
        + ")"
        + " UNION (SELECT"
        + " mr_id, shiten_cd, eigyosyo_cd, name,"
        + " master_kengen_soshiki, master_kengen_attribute,"
        + " NULL attr_cd "
        + "FROM mr "
        + "WHERE company_cd = ?"
        + " AND master_flg = '" + SysCnst.FLG_MASTER_SUB + "'"
        + " AND master_kengen_attribute <> '" + SysCnst.FLG_AUTHORITY_ATTRIBUTE1 + "'"
        + " AND master_kengen_attribute <> '" + SysCnst.FLG_AUTHORITY_ATTRIBUTE2 + "'"
        + " AND delete_ymd IS NULL"
        + ")"
        + " UNION (SELECT"
        + " mr_id, shiten_cd, eigyosyo_cd, name,"
        + " master_kengen_soshiki, master_kengen_attribute,"
        + " NULL attr_cd"
        + " FROM mr"
        + " WHERE company_cd = ?"
        + " AND master_flg = '" + SysCnst.FLG_MASTER_SUB + "'"
        + " AND master_kengen_attribute IS NULL"
        + " AND delete_ymd IS NULL"
        + ")) mr"
        + " WHERE"
        + " mr.shiten_cd = shiten.shiten_cd (+)"
        + " AND mr.eigyosyo_cd = eigyosyo.eigyosyo_cd (+)"
        + " AND mr.attr_cd = mr_attribute.mr_attribute_cd (+)"
        ;
                                    
    /** �T�u�}�X�^�[�X�V�i�������グ�j���N�G�X�g */
    protected static String MR_SUBMASTER_UPDATE_SQL
        = "UPDATE mr SET"
        + " master_flg=NULL"
        + ",master_kengen_soshiki=NULL"
        + ",master_kengen_attribute=NULL"
        + ",update_userid=?"
        + ",update_time=SYSDATE"
        ;

    /** �T�u�}�X�^�[�X�V�i�ǉ��E�ύX�j���N�G�X�g */
    protected static String MR_SUBMASTER_UPDATE_ATTR_SQL
        = "UPDATE mr SET"
        + " master_flg=?"
        + ",master_kengen_soshiki=?"
        + ",master_kengen_attribute=?"
        + ",update_userid=?"
        + ",update_time=SYSDATE"
        + " WHERE mr_id=?"
        ;

    /** ���O�C�������擾 */
    protected static final String MR_LOGIN_SQL
        = "SELECT "
        + "mr.mr_id"
        + ",mr.name"
        + ",mr.password"
        + ",mr.company_cd"
        + ",mr.shiten_cd"
        + ",mr.eigyosyo_cd"
        + ",mr.mr_attribute_cd1"
        + ",mr.mr_attribute_cd2"
        + ",mr.master_flg"
        + ",mr.master_kengen_soshiki"
        + ",mr.master_kengen_attribute" 
        + " FROM mr"
        + " WHERE mr_id = ?"
        + " AND delete_ymd IS NULL";

    // added by hb010716
   protected static final String MR_ATTRIBUTE_SQL
        = "SELECT distinct "
        + "att.mr_attribute_name, att.mr_attribute_cd "
        + " FROM mr_attribute att "
        + " WHERE att.company_cd = ?"
        + " AND delete_ymd IS NULL";

    // added by hb010717
   protected static final String MR_SHITEN_SQL
        = "SELECT distinct "
        + "shiten.shiten_name, shiten.shiten_cd"
        + " FROM shiten shiten, company company "
        + " WHERE shiten.company_cd=company.company_cd and company.company_cd = ?"
        + " AND delete_ymd IS NULL";

    // added by hb010717
   protected static final String MR_EIGYOSYO_SQL
        = "SELECT distinct "
        + "eigyosyo.eigyosyo_name, eigyosyo.eigyosyo_cd"
        + " FROM eigyosyo eigyosyo, company company "
        + " WHERE eigyosyo.company_cd=company.company_cd and company.company_cd = ?"
        + " AND delete_ymd IS NULL";

   
    /** �p�X���[�h�X�V */
    protected static final String MR_PASSWORD_CHANGE_SQL
        = "UPDATE mr SET"
        + " password = ?"
        + ",update_userid = ?"
        + ",update_time = SYSDATE"
        + " WHERE"
        + " mr_id = ?";

    /** MR�ꗗ - �擾 */
    protected static final String MR_LIST_SQL
        = "SELECT"
        + " mr.mr_id"
        + ",mr.name"
        + ",shiten.shiten_name"
        + ",eigyosyo.eigyosyo_name"
        + ",A.mr_attribute_name AS mr_attribute1"
        + ",B.mr_attribute_name AS mr_attribute2"
        + ",mr.master_flg"
        + ",mr.nyusya_year"
        + " FROM mr, shiten, eigyosyo, mr_attribute A, mr_attribute B"
        + " WHERE"
        + " mr.shiten_cd=shiten.shiten_cd (+)"
        + " AND mr.eigyosyo_cd=eigyosyo.eigyosyo_cd (+)"
        + " AND mr.mr_attribute_cd1=A.mr_attribute_cd (+)"
        + " AND mr.mr_attribute_cd2=B.mr_attribute_cd (+)"
        + " AND mr.delete_ymd IS NULL";

    /** MR�ꗗ - �폜 */
    protected static final String MR_DELETE_SQL
        = "UPDATE mr SET"
        + " update_userid = ?"
        + ",delete_ymd = SYSDATE"
        + ",update_time = SYSDATE"
        + " WHERE"
        + " mr_id = ?";

    /** MR�ǉ��E�X�V - �擾 */
    protected static final String MR_INFO_SQL
        = "SELECT"
        + " mr.mr_id"
        + ",mr.name_kana"
        + ",mr.name"
        + ",mr.company_cd"
        + ",mr.shiten_cd"
        + ",shiten.shiten_name"
        + ",mr.eigyosyo_cd"
        + ",eigyosyo.eigyosyo_name"
        + ",mr.mr_attribute_cd1"
        + ",A.mr_attribute_name AS mr_attribute1"
        + ",mr.mr_attribute_cd2"
        + ",B.mr_attribute_name AS mr_attribute2"
        + ",mr.nyusya_year"
        + ",mr.password"
        + ",mr.master_flg"
        + ",mr.master_kengen_soshiki"
        + ",mr.master_kengen_attribute"
        + ",mr.tel_no"
        + ",mr.fax_no"
        + ",catch_picture.picture"
        + " FROM mr,shiten,eigyosyo,mr_attribute A, mr_attribute B,catch_picture"
        + " WHERE"
        + " mr.mr_id = ?"
        + " AND mr.delete_ymd IS NULL"
        + " AND mr.shiten_cd=shiten.shiten_cd (+)"
        + " AND mr.eigyosyo_cd=eigyosyo.eigyosyo_cd (+)"
        + " AND mr.mr_attribute_cd1=A.mr_attribute_cd (+)"
        + " AND mr.mr_attribute_cd2=B.mr_attribute_cd (+)"
        + " AND mr.picture_cd = catch_picture.picture_cd (+)";

    /** MR�ǉ��E�X�V - �X�V */
    protected static final String MR_UPDATE_SQL
        = "UPDATE mr SET"
        + " shiten_cd = ?"
        + ",eigyosyo_cd = ?"
        + ",mr_attribute_cd1 = ?"
        + ",mr_attribute_cd2 = ?"
        + ",name = ?"
        + ",name_kana = ?"
        + ",nyusya_year = ?"
        + ",password = ?"
        + ",master_flg = ?"
        + ",master_kengen_soshiki = ?"
        + ",master_kengen_attribute = ?"
        + ",tel_no = ?"
        + ",fax_no = ?"
        + ",update_userid = ?"
        + ",update_time = SYSDATE"
        + " WHERE"
        + " mr_id = ?";

    /** MR�ǉ��E�X�V - �ǉ� */
    protected static final String MR_INSERT_SQL
        = "INSERT INTO mr ("
        + " mr_id"
        + ",company_cd"
        + ",mr_yakusyoku_cd"
        + ",shiten_cd"
        + ",eigyosyo_cd"
        + ",mr_attribute_cd1"
        + ",mr_attribute_cd2"
        + ",name"
        + ",name_kana"
        + ",nyusya_year"
        + ",password"
        + ",eigyo_date_kbn"
        + ",eigyo_time_kbn"
        + ",eigyo_start_time"
        + ",eigyo_end_time"
        + ",update_userid"
        + ",update_time"
        + ",toroku_userid"//1212 y-yamada add
        + ",toroku_ymd"//1212 y-yamada add
        + ") values ("
        + " ?"
        + ",?"
        + ",'" + SysCnst.MR_YAKUSYOKU_CD_DEFAULT + "'"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",?"
        + ",'" + SysCnst.MR_EIGYO_DATE_KBN_DEFAULT + "'"
        + ",'" + SysCnst.MR_EIGYO_TIME_KBN_DEFAULT + "'"
        + "," + SysCnst.MR_EIGYO_START_TIME_DEFAULT
        + "," + SysCnst.MR_EIGYO_END_TIME_DEFAULT
        + ",?"
        + ",SYSDATE"
        + ",?"//1213  y-yamada add
        + ",SYSDATE"//1213  y-yamada add
        + ")";

    /** MR�ǉ��E�X�V - MR-ID�擾 */
    protected static final String MR_NEXT_ID_SQL
        = "SELECT"
        + " max(B.cd_prefix) ||"
        + " to_char(max(to_number(substr(mr_id,4,5)))+1,'FM09999') ||"
        + " chr(mod(max(to_number(substr(mr_id,4,5)))+1, 26)+65) ||"
        + " mod(to_number(to_char(sysdate,'yyyymmddhh24miss')),10)"
        + " AS next_id"
        + " FROM mr A, company B"
        + " WHERE"
        + " B.company_cd = ?"
        + " AND A.company_cd = B.company_cd";

    /** MR�L���b�`�摜�ꗗ - �f�t�H���g�摜�R�[�h�擾 */
    protected static final String MR_CATCH_INFO_SQL
        = "SELECT"
        + " mr_id"
        + ",name"
        + ",picture_cd"
        + " FROM mr"
        + " WHERE mr_id = ?";

    /** MR�L���b�`�摜�ꗗ - �f�t�H���g�摜�R�[�h�ݒ� */
    protected static final String MR_CATCH_DEFAULTSET_SQL
        = "UPDATE mr SET"
        + " picture_cd = ?"
        + ",update_userid = ?"
        + ",update_time = SYSDATE"
        + " WHERE"
        + " mr_id = ?";
        
        
        
        
    /*�l�q�e�[�u������master_flg���擾*/
        protected static final String GET_MASTER_FLG
        = "SELECT"
        + " master_flg "
        + " FROM mr"
        + " WHERE mr_id = ?";
        
    /* WM_HIST �e�[�u����insert���� */
        protected static final String INSERT_WM_HIST_SQL
        = "INSERT INTO wm_hist ("
        + " mr_id"
        + ",master_flg"
        + ",start_ymd"
        + ") values ("
        + " ?"
        + ",?"
        + ",SYSDATE"
        + ")";
        
    /** �f�[�^�̎擾 */
    protected static final String HANBETU_SQL
        = "SELECT "
        + " mr_id"
        + " FROM wm_hist"
        + " WHERE mr_id = ?"
        + " AND end_ymd IS NULL";
        
        
    /** �f�[�^�̎擾 */
    protected static final String UPDATE_WM_HIST_SQL
        = "UPDATE wm_hist  SET "
        + " end_ymd = SYSDATE "
        + " WHERE "
        + " mr_id = ? "
        + " AND end_ymd IS NULL";
        

    /** �R�l�N�V�����E�I�u�W�F�N�g */
    protected Connection conn = null;

    /**
     * MrInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public MrInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * �w��̂l�q���������l�q�̑��݂��`�F�b�N����B
     * @param vec �l�q�����R�[�h���X�g
     */
    public boolean hasMrAttribute(Vector vec) {
        int num = 0;

        try {
            String sqltxt = "SELECT COUNT(mr_id) as count FROM mr ";
            sqltxt += " WHERE mr_attribute_cd1='" + vec.elementAt(0) + "'";
            sqltxt += " OR mr_attribute_cd2='" + vec.elementAt(0) + "'";
            for ( int i = 1; i < vec.size(); i++ ) {
                String code = (String)vec.elementAt(i);
                sqltxt += " OR mr_attribute_cd1='" + code + "'";
                sqltxt += " OR mr_attribute_cd2='" + code + "'";
            }
            if ( SysCnst.DEBUG ) {
                System.out.println("sql="+sqltxt);
            }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                if ( rs.next() ) {
                    num = rs.getInt("count");
                }
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        if ( SysCnst.DEBUG ) {
            System.out.println("count="+num);
        }
        return (num > 0);
    }

    /**
     * �T�u�}�X�^�[�̂l�q�����擾����B
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @return �v�f�i�l�q���j���X�g
     */
    public Enumeration getSubmasters(HttpSession session) {

        Vector list = new Vector();
        Common common =
            (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
        SubListSession subSession =
            (SubListSession)session.getValue(SysCnst.KEY_SUBLIST_SESSION);
        
        try {
            //�r�p�k��
            String sqltxt = MR_SUBMASTER_MAIN_SQL;
            // �x�X�̃T�u�}�X�^�[�ł���ꍇ�͏����x�X�̉c�Ə��݂̂�ΏۂƂ���B
            if ( common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB) &&
                 common.getMasterKengenSoshiki().equals(
                        SysCnst.FLG_AUTHORITY_BRANCH) ) {
                sqltxt += " AND mr.shiten_cd='"+ common.getShitenCd() + "'";
                sqltxt += " AND mr.master_kengen_soshiki='"
                        + SysCnst.FLG_AUTHORITY_OFFICE + "'";
            }
            sqltxt += " ORDER BY "
                + subSession.getSortKey() + " "
                + subSession.getOrder();
            if ( SysCnst.DEBUG ) {
                System.out.println("sql="+sqltxt);
            }
            // SQL ����ݒ�
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, common.getCompanyCd());
                pstmt.setString(2, common.getCompanyCd());
                pstmt.setString(3, common.getCompanyCd());
                pstmt.setString(4, common.getCompanyCd());
                // SQL ���s
                ResultSet rs = pstmt.executeQuery();
                int i = 0;
                int begin = (subSession.getPage() - 1) * common.getSubLine();
                int end = begin + common.getSubLine();
                while ( rs.next() ) {
                    if ( i >= begin && i < end ) {
                        MrInfo info = new MrInfo();
                        info.setMrId(rs.getString("mr_id"));
                        info.setName(rs.getString("name"));
                        String shiten_name = "-";
                        String eigyosyo_name = "-";
                        String attr_name = "-";
                        String soshiki = rs.getString("master_kengen_soshiki");
                        String attr = rs.getString("master_kengen_attribute");
                        if ( soshiki != null ) {
                            if ( soshiki.equals(SysCnst.FLG_AUTHORITY_BRANCH) ) {
                                shiten_name = rs.getString("shiten_name");
                            }
                            else if ( soshiki.equals(SysCnst.FLG_AUTHORITY_OFFICE) ) {
                                shiten_name = rs.getString("shiten_name");
                                eigyosyo_name = rs.getString("eigyosyo_name");
                            }
                        }
                        if ( attr != null ) {
                            if ( attr.equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) ||
                                 attr.equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2) ) {
                                attr_name = rs.getString("mr_attribute_name");
                            }
                        }
                        info.setMasterKengenSoshiki(soshiki);
                        info.setMasterKengenAttribute(attr);
                        info.setMrAttributeName1(attr_name);
                        info.setMrAttributeName2(attr_name);
                        info.setShitenName(shiten_name);
                        info.setEigyosyoName(eigyosyo_name);
                        list.addElement(info);
                    }
                    if ( ++i >= end )        break;
                }
                subSession.setPrevPage(begin >= common.getSubLine());
                subSession.setNextPage(i < end ? false : rs.next());
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
     * �T�u�}�X�^�[�������X�V����B�i���������グ��j
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @param vec �l�q�|�h�c���X�g
     */
    public void updateSubmaster(HttpSession session, Vector vec) {
        Common common =
            (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
        try {
//System.err.println("�폜");
//            String sqltxt = MR_SUBMASTER_UPDATE_SQL;
//            sqltxt += " WHERE mr_id='" + vec.elementAt(0) + "'";
//            for ( int i = 1; i < vec.size(); i++ ) {
            for ( int i = 0; i < vec.size(); i++ ) {///////////////////////////////
//                String mrid = (String)vec.elementAt(i);
				String sqltxt = MR_SUBMASTER_UPDATE_SQL;
//                sqltxt += " OR mr_id='" + mrid + "'";
				sqltxt += " WHERE mr_id='" + vec.elementAt(i) + "'";
//            }
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
	            
	            
	            PreparedStatement pstmt2 = conn.prepareStatement(UPDATE_WM_HIST_SQL);
	            try {
	                // �p�����[�^��ݒ�
	                pstmt2.setString(1, (String)vec.elementAt(i));
	                pstmt2.executeUpdate();
	            }
	            finally {
	                pstmt2.close();
	            }
	            conn.commit();
	            
	            
	            
	        }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * �T�u�}�X�^�[�����X�V����B�i�ǉ��E�X�V�j
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @param info �l�q���
     */
    public void updateSubmaster(HttpSession session, MrInfo info) {
        Common common =
            (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
        try {
//System.err.println("�ǉ��E�ύX�@");
//System.err.println("�ǉ��E�ύX�@"+info.getMrId());
            String sqltxt = MR_SUBMASTER_UPDATE_ATTR_SQL;
            if ( SysCnst.DEBUG ) {
                System.out.println("sql="+sqltxt);
            }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // �p�����[�^��ݒ�
                pstmt.setString(1, info.getMasterFlg());
                pstmt.setString(2, info.getMasterKengenSoshiki());
                pstmt.setString(3, info.getMasterKengenAttribute());
                pstmt.setString(4, common.getMrId());
                pstmt.setString(5, info.getMrId());
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


		/*
		�x�X�E�c�Ə��� �Ɓ@MR����  ��null�̂Ƃ��͍폜
		*/
		if( info.getMasterKengenSoshiki()==null && info.getMasterKengenAttribute()==null )
		{
	        try {
	            String sqltxt = UPDATE_WM_HIST_SQL;
	            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	            try {
	                // �p�����[�^��ݒ�
	                pstmt.setString(1, info.getMrId());
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







        
        /*�@�X�V���邩���ʁ@*/
        MrInfo mrInfo = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(HANBETU_SQL);
            pstmt.setString(1, info.getMrId());

            ResultSet rs = pstmt.executeQuery();

            //�ŏ��Ƀ}�b�`����MR���擾
            if (rs.next()) {
                mrInfo = new MrInfo();
                if(rs.getString("mr_id")!=null)
                	mrInfo.setMrId(rs.getString("mr_id"));
            }

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }
        
        /*
        wm_hist�e�[�u���ɂ��Ȃ�mrid��end_ymd��null�̃f�[�^�����������Ƃ�
        */
        
        if(mrInfo == null &&  info.getMasterFlg()!=null )
        {
//System.err.println("�v�l�Q�g�h�r�s");	        
		        /*��������ǉ��@wm_hist�e�[�u���ɒl������*/
		        try {
		            String sqltxt = INSERT_WM_HIST_SQL;
		            if ( SysCnst.DEBUG ) {
		                System.out.println("sql="+sqltxt);
		            }
		            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
		            try {
		                // �p�����[�^��ݒ�
		                pstmt.setString(1, info.getMrId());
		                pstmt.setString(2, info.getMasterFlg());
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
        
        
        
        
    }

    /**
     * Loging���ɕK�v��MR�����擾����.
     * @param  mrId �ΏۂƂȂ�MR��ID
     * @return MR���
     */
    public MrInfo getMrLoginInfo(String mrId) {
        MrInfo mrInfo = null;
        Hashtable daikou = new Hashtable();

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_LOGIN_SQL);
            pstmt.setString(1, mrId);

            ResultSet rs = pstmt.executeQuery();

            //�ŏ��Ƀ}�b�`����MR���擾
            if (rs.next()) {
                mrInfo = new MrInfo();
                mrInfo.setMrId(rs.getString("mr_id"));
                mrInfo.setName(rs.getString("name"));
                mrInfo.setPassword(rs.getString("password"));
                mrInfo.setCompanyCd(rs.getString("company_cd"));
                mrInfo.setShitenCd(rs.getString("shiten_cd"));
                mrInfo.setEigyosyoCd(rs.getString("eigyosyo_cd"));
                mrInfo.setMrAttributeCd1(rs.getString("mr_attribute_cd1"));
                mrInfo.setMrAttributeCd2(rs.getString("mr_attribute_cd2"));
                mrInfo.setMasterFlg(rs.getString("master_flg"));
                mrInfo.setMasterKengenSoshiki(rs.getString("master_kengen_soshiki"));
                mrInfo.setMasterKengenAttribute(rs.getString("master_kengen_attribute"));
            }

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_ATTRIBUTE_SQL);
            pstmt.setString(1, mrInfo.getCompanyCd());

            
            
            
            
            mrInfo.setDaikou("mr_company_cd",mrInfo.getCompanyCd());
            ResultSet rs = pstmt.executeQuery();
            Vector attribute_name = new Vector();
            Vector attribute_cd = new Vector();
            //�ŏ��Ƀ}�b�`����MR���擾
            while (rs.next()) { 
                String att = rs.getString("mr_attribute_name");
                attribute_name.add(att);
                String cd = rs.getString("mr_attribute_cd");
                attribute_cd.add(cd);
            }
           mrInfo.setDaikou("mr_attribute_name",attribute_name);
           mrInfo.setDaikou("mr_attribute_cd",attribute_cd);

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        // get the unique SHITEN codes
        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_SHITEN_SQL);
            pstmt.setString(1, mrInfo.getCompanyCd());

            ResultSet rs = pstmt.executeQuery();
            Vector shiten_name = new Vector();
            Vector shiten_cd = new Vector();
            //�ŏ��Ƀ}�b�`����MR���擾
            while (rs.next()) { 
                String att = rs.getString("shiten_name");
                shiten_name.add(att);
                att = rs.getString("shiten_cd");
                shiten_cd.add(att);
            }
           mrInfo.setDaikou("shiten_name",shiten_name);
           mrInfo.setDaikou("shiten_cd",shiten_cd);

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }


        // get the unique EIGYOSYO codes
        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_EIGYOSYO_SQL);
            pstmt.setString(1, mrInfo.getCompanyCd());

            ResultSet rs = pstmt.executeQuery();
            Vector eigyosyo_name = new Vector();
            Vector eigyosyo_cd = new Vector();
            //�ŏ��Ƀ}�b�`����MR���擾
            while (rs.next()) { 
                String att = rs.getString("eigyosyo_name");
                eigyosyo_name.add(att);
                att = rs.getString("eigyosyo_cd");
                eigyosyo_cd.add(att);
            }
           mrInfo.setDaikou("eigyosyo_name",eigyosyo_name);
           mrInfo.setDaikou("eigyosyo_cd",eigyosyo_cd);

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        mrInfo.setDaikou("connection", conn);

        return mrInfo;
    }


    /**
     * �p�X���[�h��ύX����.
     * @param info MR���
     * @return ���ۂɍX�V�������R�[�h��
     */
    public int updatePassword(MrInfo info) {
        int updateCount = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_PASSWORD_CHANGE_SQL);
            pstmt.setString(1, info.getPassword());
            pstmt.setString(2, info.getMrId());
            pstmt.setString(3, info.getMrId());

            updateCount = pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return updateCount;
    }

    /**
     * MR���̈ꗗ���擾����.
     * @param  common ���O�C������MR�̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param  sortTarget    �\�[�g�Ώۍ��ږ�
     * @param  sortDirection �\�[�g����
     * @param  refMrId       ��������(MR-ID)
     * @param  refMrName     ��������(����)
     * @return MR�ꗗ���
     */
    public Vector getMrList(Common common,
			    String sortTarget,
			    String sortDirection,
			    String refMrId,
			    String refMrName) {
        Vector v = new Vector();

        try {
            Statement stmt = conn.createStatement();

            //��ЃR�[�h�̐ݒ�
            String sql = MR_LIST_SQL + " and mr.company_cd = '" + common.getCompanyCd() + "'";

	    //���������̐ςݍ���
	    if (refMrId != null && !refMrId.trim().equals("")) {
		sql += " and mr.mr_id like '" + refMrId + "%'";
	    }

	    if (refMrName != null && !refMrName.trim().equals("")) {
		sql += " and mr.name like '" + refMrName + "%'";
	    }

            String soshikiSql = new String();
            String attributeSql = new String();

            if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
                //�E�F�u�}�X�^�͑S�f�[�^�擾
            } else if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
                //�T�u�}�X�^
                if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
                    //�x�X�R�[�h�ōi�荞��
                    soshikiSql += " mr.shiten_cd = '" + common.getShitenCd() + "'";
                } else if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
                    //�x�X�R�[�h�Ɖc�Ə��R�[�h�ōi�荞��
                    soshikiSql += " (mr.shiten_cd = '" + common.getShitenCd() + "'";
                    soshikiSql += " and mr.eigyosyo_cd = '" + common.getEigyosyoCd() + "')";
                }

                if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1)) {
                    //����1�R�[�h�ōi�荞��
                    attributeSql += " (mr.mr_attribute_cd1 = '" + common.getMrAttributeCd1() + "'"
                        +  " or mr.mr_attribute_cd2 = '" + common.getMrAttributeCd1() + "')";
                } else if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
                    //����2�R�[�h�ōi�荞��
                    attributeSql += " (mr.mr_attribute_cd1 = '" + common.getMrAttributeCd2() + "'"
                        +  " or mr.mr_attribute_cd2 = '" + common.getMrAttributeCd2() + "')";
                }

                if (soshikiSql.equals("")) {
                    sql += " and " + attributeSql;
                } else if (attributeSql.equals("")) {
                    sql += " and " + soshikiSql;
                } else {
                    sql += " and ((" + soshikiSql + ") or (" + attributeSql + "))";
                }

            }

            sql += " ORDER BY " + sortTarget + " " + sortDirection;

            ResultSet rs = stmt.executeQuery(sql);
  
            while (rs.next()) {
                MrInfo mrInfo = new MrInfo();
                mrInfo.setMrId(rs.getString("mr_id"));
                mrInfo.setName(rs.getString("name"));
                mrInfo.setShitenName(rs.getString("shiten_name"));
                mrInfo.setEigyosyoName(rs.getString("eigyosyo_name"));
                mrInfo.setMrAttributeName1(rs.getString("mr_attribute1"));
                mrInfo.setMrAttributeName2(rs.getString("mr_attribute2"));
                String masterFlg = rs.getString("master_flg");

                if (masterFlg == null) {
                    mrInfo.setMasterFlg("");
                } else if (masterFlg.equals(SysCnst.FLG_MASTER_WEB)) {
                    mrInfo.setMasterFlg(SysCnst.WEBMASTER_LABEL);
                } else if (masterFlg.equals(SysCnst.FLG_MASTER_SUB)) {
                    mrInfo.setMasterFlg(SysCnst.SUBMASTER_LABEL);
                } else {
                    mrInfo.setMasterFlg("");
                }

                mrInfo.setNyusyaYear(rs.getInt("nyusya_year"));

                v.addElement(mrInfo);
            }
  
            stmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }
  
        return v;
    }

    /**
     * MR�ǉ��E�ύX��ʗp��MR�����擾����.
     * @param  mrId �擾�Ώۂ�MRID
     * @return MR���
     */
    public MrInfo getMrInfo(String mrId) {
        MrInfo mrInfo = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_INFO_SQL);
            pstmt.setString(1, mrId);

            ResultSet rs = pstmt.executeQuery();

            //�ŏ��Ƀ}�b�`����MR���擾
            if (rs.next()) {
                mrInfo = new MrInfo();
                mrInfo.setMrId(rs.getString("mr_id"));
                mrInfo.setCompanyCd(rs.getString("company_cd"));
                mrInfo.setNameKana(rs.getString("name_kana"));
                mrInfo.setName(rs.getString("name"));
                mrInfo.setShitenCd(rs.getString("shiten_cd"));
                mrInfo.setShitenName(rs.getString("shiten_name"));
                mrInfo.setEigyosyoCd(rs.getString("eigyosyo_cd"));
                mrInfo.setEigyosyoName(rs.getString("eigyosyo_name"));
                mrInfo.setMrAttributeCd1(rs.getString("mr_attribute_cd1"));
                mrInfo.setMrAttributeName1(rs.getString("mr_attribute1"));
                mrInfo.setMrAttributeCd2(rs.getString("mr_attribute_cd2"));
                mrInfo.setMrAttributeName2(rs.getString("mr_attribute2"));
                mrInfo.setNyusyaYear(rs.getInt("nyusya_year"));
                mrInfo.setPassword(rs.getString("password"));
                mrInfo.setMasterKengenSoshiki(rs.getString("master_kengen_soshiki"));
                mrInfo.setMasterKengenAttribute(rs.getString("master_kengen_attribute"));
                mrInfo.setTelNo(rs.getString("tel_no"));
                mrInfo.setFaxNo(rs.getString("fax_no"));
                mrInfo.setPicture(rs.getString("picture"));
                mrInfo.setMasterFlg(rs.getString("master_flg"));
            }

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

      return mrInfo;
    }

    /**
     * MR�����X�V����.
     * @param  mrInfo �X�VMR���
     * @param  common ���O�C������MR�̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return ���ۂɍX�V������
     */
    public int updateMr(MrInfo mrInfo, Common common) {
        int rc = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_UPDATE_SQL);
            
            pstmt.setString(1, mrInfo.getShitenCd());
            pstmt.setString(2, mrInfo.getEigyosyoCd());
            pstmt.setString(3, mrInfo.getMrAttributeCd1());
            pstmt.setString(4, mrInfo.getMrAttributeCd2());
            pstmt.setString(5, mrInfo.getName());
            pstmt.setString(6, mrInfo.getNameKana());
            pstmt.setInt(7, mrInfo.getNyusyaYear());
            pstmt.setString(8, mrInfo.getPassword());
            pstmt.setString(9, mrInfo.getMasterFlg());
            pstmt.setString(10, mrInfo.getMasterKengenSoshiki());
            pstmt.setString(11, mrInfo.getMasterKengenAttribute());
            pstmt.setString(12, mrInfo.getTelNo());
            pstmt.setString(13, mrInfo.getFaxNo());
            pstmt.setString(14, common.getMrId());
            pstmt.setString(15, mrInfo.getMrId());

            rc = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return rc;
    }

    /**
     * MR�����폜����(���ۂɂ͍폜�N�������Z�b�g����).
     * @param  mrInfo �폜����MR-ID���X�g
     * @param  common ���O�C������MR�̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return ���ۂɍX�V������
     */
    public int removeMr(String[] mrIds, Common common) {
        int rc = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_DELETE_SQL);

            pstmt.setString(1, common.getMrId());

            for (int i = 0; i < mrIds.length; i++) {
                pstmt.setString(2, mrIds[i]);
                rc += pstmt.executeUpdate();
            }
            
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return rc;
    }

    /**
     * MR��ǉ�����.
     * @param  mrInfo �ǉ�MR���
     * @param  common ���O�C������MR�̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @return ���ۂɒǉ�������
     */
//    public int insertMr(MrInfo mrInfo, Common common) {
    public int insertMr(MrInfo mrInfo, Common common , String wmId ) {
        int rc = 0;
        
        try {
            conn.setAutoCommit(false);
	    String prefix = common.getMrId().substring(0, 3);

            // MR-ID�����ݒ�̏ꍇ��DB���玩���̔�
            if (mrInfo.getMrId() == null || mrInfo.getMrId().equals("")) {
                PreparedStatement pstmt = conn.prepareStatement(MR_NEXT_ID_SQL);
                pstmt.setString(1, common.getCompanyCd());

                ResultSet rs = pstmt.executeQuery();
                rs.next();
                mrInfo.setMrId(rs.getString("next_id"));
                pstmt.close();
		prefix = "";
	    }

            PreparedStatement pstmt = conn.prepareStatement(MR_INSERT_SQL);
            
            pstmt.setString(1, prefix + mrInfo.getMrId());
            pstmt.setString(2, common.getCompanyCd());
            pstmt.setString(3, mrInfo.getShitenCd());
            pstmt.setString(4, mrInfo.getEigyosyoCd());
            pstmt.setString(5, mrInfo.getMrAttributeCd1());
            pstmt.setString(6, mrInfo.getMrAttributeCd2());
            pstmt.setString(7, mrInfo.getName());
            pstmt.setString(8, mrInfo.getNameKana());
            pstmt.setInt(9, mrInfo.getNyusyaYear());
            pstmt.setString(10, mrInfo.getPassword());
            pstmt.setString(11, common.getMrId());
            pstmt.setString(12, wmId );

            rc = pstmt.executeUpdate();

            pstmt.close();

            conn.commit();

	    mrInfo.setMrId(prefix + mrInfo.getMrId());
        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }

            throw new WmException(ex);
        }

        return rc;
    }

    /**
     * MR�L���b�`�摜�����擾����.
     * @param  mrId �擾�ΏۂƂȂ�MR��ID
     * @return MR���
     */
    public MrInfo getMrCatchInfo(String mrId) {
        MrInfo info = new MrInfo();

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_CATCH_INFO_SQL);
            pstmt.setString(1, mrId);

            ResultSet rs = pstmt.executeQuery();

            //�ŏ��Ƀ}�b�`����MR���擾
            if (rs.next()) {
                info.setMrId(rs.getString("mr_id"));
                info.setName(rs.getString("name"));
                info.setPictureCd(rs.getString("picture_cd"));
            }

            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return info;
    }

    /**
     * �f�t�H���gMR�L���b�`�摜��ݒ肷��.
     * @param  common    ���O�C������MR�̏���ς񂾃Z�b�V�����I�u�W�F�N�g
     * @param  pictureCd �ݒ肷��摜�R�[�h
     * @param  mrId      �ݒ�ΏۂƂȂ�MR��ID
     * @return MR���
     */
    public int setDefaultPictureCd(Common common, String pictureCd, String mrId) {
        int rc = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(MR_CATCH_DEFAULTSET_SQL);
            pstmt.setString(1, pictureCd);
            pstmt.setString(2, common.getMrId());
            pstmt.setString(3, mrId);

            rc = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return rc;
    }
}
  
