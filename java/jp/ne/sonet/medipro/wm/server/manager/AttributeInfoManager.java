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
 * <strong>ＭＲ属性情報管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class AttributeInfoManager {
    protected Connection conn;  // コネクション・オブジェクト

    /** ＭＲ属性情報一覧取得リクエスト */
    protected static final String ATTRIBUTE_SELECT_LIST_SQL
	= "SELECT"
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	;
                                    
    /** ＭＲ属性情報取得リクエスト */
    protected static final String ATTRIBUTE_SELECT_INFO_SQL
	= "SELECT"
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " mr_attribute_cd = ?"
	;
                                    
    /** リンク情報削除リクエスト */
    protected static final String ATTRIBUTE_DELETE_SQL
	= "UPDATE mr_attribute SET"
	+ "  update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", delete_ymd = SYSDATE"
	;

    /** リンク情報更新リクエスト */
    protected static final String ATTRIBUTE_UPDATE_SQL
	= "UPDATE mr_attribute SET"
	+ "  mr_attribute_cd = ?"
	+ ", mr_attribute_name = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE mr_attribute_cd = ?"
	;

    /** リンク情報追加リクエスト */
    protected static final String ATTRIBUTE_INSERT_SQL
	= "INSERT INTO mr_attribute ("
	+ " mr_attribute_cd, mr_attribute_name, company_cd"
	+ ", update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, SYSDATE)"
	;

    /** 追加リンクコード取得 */
    protected static final String ATTRIBUTE_MAX_CD_SQL
	= "SELECT"
	+ " to_char(mr_attribute_cd.nextval, '0000') as next_mr_attribute_cd"
	+ " FROM dual";

    /**
     * AttributeInfoManager オブジェクトを新規に作成する。
     * @param conn コネクション・オブジェクト
     */
    public AttributeInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * ＭＲ属性一覧情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session ＭＲ属性一覧画面の設定情報を積んだセッションオブジェクト
     * @return ＭＲ属性一覧情報
     */
    public Enumeration getAttributeList(Common common,
					AttributeListSession session) {

        Vector list = new Vector();
        
        try {
            //ＳＱＬ文
            String sqltxt = ATTRIBUTE_SELECT_LIST_SQL;
	    sqltxt += " ORDER BY "
		+ session.getSortKey() + " "
		+ session.getOrder();
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL 文を設定
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
				// パラメータを設定
		pstmt.setString(1, common.getCompanyCd());
                // SQL 実行
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
     * ＭＲ属性一覧情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code 会社コード
     * @return ＭＲ属性一覧情報
     */
    public Enumeration getAttributeList(Common common, String code) {

        Vector list = new Vector();
        
        try {
            //ＳＱＬ文
            String sqltxt = ATTRIBUTE_SELECT_LIST_SQL;
	    sqltxt += " ORDER BY mr_attribute_cd ASC";
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL 文を設定
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
				// パラメータを設定
		pstmt.setString(1, code);
                // SQL 実行
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
     * ＭＲ属性情報を削除する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param vec 削除対象ＭＲ属性コードのリスト
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
                // パラメータを設定
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
     * ＭＲ属性情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code ＭＲ属性コード
     * @return ＭＲ属性情報
     */
    public AttributeInfo getAttributeInfo(Common common, String code) {

	AttributeInfo info = null;
        try {
            //ＳＱＬ文
            String sqltxt = ATTRIBUTE_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL 文を設定
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, code);
                // SQL 実行
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
     * ＭＲ属性情報を更新する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info ＭＲ属性情報
     */
    public void updateAttributeInfo(Common common, AttributeInfo info) {

        try {
	    String sqltxt = ATTRIBUTE_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
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
     * ＭＲ属性情報を追加する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info ＭＲ属性情報
     * @return 実際に追加した数
     */
    public int insertAttributeInfo(Common common, AttributeInfo info) {

	int rc = 0;
		
	try {
	    conn.setAutoCommit(false);

	    // ＭＲ属性コード自動採番
	    String linkcd = "";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(ATTRIBUTE_MAX_CD_SQL);
	    if (rs.next()) {
		linkcd = (rs.getString("next_mr_attribute_cd")).trim();
	    }
	    stmt.close();
			
	    // ＭＲ属性情報追加
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
