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
 * <strong>リンク分類情報管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkClassInfoManager {
    protected Connection conn;  // コネクション・オブジェクト

    /** リンク分類情報一覧取得リクエスト */
    protected static final String LINKCLASS_SELECT_LIST_SQL
	= "SELECT"
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ " FROM link_bunrui"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	;
                                    
    /** リンク分類情報取得リクエスト */
    protected static final String LINKCLASS_SELECT_INFO_SQL
	= "SELECT"
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ " FROM link_bunrui"
	+ " WHERE"
	+ " link_bunrui_cd = ?"
	;
                                    
    /** リンク情報削除リクエスト */
    protected static final String LINKCLASS_DELETE_SQL
	= "UPDATE link_bunrui SET"
	+ "  update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", delete_ymd = SYSDATE"
	;

    /** リンク情報更新リクエスト */
    protected static final String LINKCLASS_UPDATE_SQL
	= "UPDATE link_bunrui SET"
	+ "  link_bunrui_cd = ?"
	+ ", bunrui_name = ?"
	+ ", update_userid = ?"
	+ ", update_time = SYSDATE"
	+ " WHERE link_bunrui_cd = ?"
	;

    /** リンク情報追加リクエスト */
    protected static final String LINKCLASS_INSERT_SQL
	= "INSERT INTO link_bunrui ("
	+ " link_bunrui_cd, bunrui_name, company_cd"
	+ ", update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, SYSDATE)"
	;

    /** 追加リンクコード取得 */
    protected static final String LINKCLASS_MAX_CD_SQL
	= "SELECT"
	+ " to_char(link_bunrui_cd.nextval, '0000') as next_link_bunrui_cd"
	+ " FROM dual";

    /**
     * LinkClassInfoManager オブジェクトを新規に作成する。
     * @param conn コネクション・オブジェクト
     */
    public LinkClassInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * リンク分類一覧情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code 会社コード
     * @return リンク分類一覧情報
     */
    public Enumeration getLinkClassList(Common common, String code) {

        Vector list = new Vector();
        
        try {
            //ＳＱＬ文
            String sqltxt = LINKCLASS_SELECT_LIST_SQL;
	    sqltxt += " ORDER BY link_bunrui_cd";
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
     * リンク分類情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param code リンク分類コード
     * @return リンク分類情報
     */
    public LinkClassInfo getLinkClassInfo(Common common, String code) {

	LinkClassInfo info = null;
        try {
            //ＳＱＬ文
            String sqltxt = LINKCLASS_SELECT_INFO_SQL;
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
     * リンク分類情報を更新する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info リンク分類情報
     */
    public void updateLinkClassInfo(Common common, LinkClassInfo info) {

        try {
	    String sqltxt = LINKCLASS_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
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
     * リンク分類情報を追加する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info リンク分類情報
     * @return 実際に追加した数
     */
    public int insertLinkClassInfo(Common common, LinkClassInfo info) {

	int rc = 0;
		
	try {
	    conn.setAutoCommit(false);

	    // リンク分類コード自動採番
	    String linkcd = "";
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery(LINKCLASS_MAX_CD_SQL);
	    if (rs.next()) {
		linkcd = (rs.getString("next_link_bunrui_cd")).trim();
	    }
	    stmt.close();
			
	    // リンク分類情報追加
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
