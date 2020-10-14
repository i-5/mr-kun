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
 * <strong>リンクライブラリ情報管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class LinkLibInfoManager {
    protected Connection conn;  // コネクション・オブジェクト

    /** リンク情報一覧取得リクエスト */
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
                                    
    /** リンク情報取得リクエスト */
    protected static final String LINKLIBRARY_SELECT_INFO_SQL
	= LINKLIBRARY_SELECT_SQL
	+ " AND link_lib.link_cd = ?"
	;
                                    
    /** リンク情報削除リクエスト */
    protected static final String LINKLIBRARY_DELETE_SQL
	= "UPDATE link_lib SET"
	+ " update_userid = ?"
	+ ", update_time = SYSDATE"
	+ ", link_lib.delete_ymd = SYSDATE"
	;

    /** リンク情報更新リクエスト */
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

    /** リンク情報追加リクエスト */
    protected static final String LINKLIBRARY_INSERT_SQL
	= "INSERT INTO link_lib ("
	+ " link_cd, company_cd, link_bunrui_cd, url, honbun_text"
	+ ", description, picture, niagai_link_kbn, update_userid, update_time"
	+ ") VALUES (?, ?, ?, ?, ?, ?, NULL, NULL, ?, SYSDATE)"
	;

    /** 追加リンクコード取得 */
    protected static final String LINKLIBRARY_MAX_CD_SQL
	= "SELECT"
	+ " to_char(link_cd.nextval, '0000000000') as next_link_cd"
	+ " FROM dual";

    /**
     * LinkLibInfoManager オブジェクトを新規に作成する。
     * @param conn コネクション・オブジェクト
     */
    public LinkLibInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * リンク一覧情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param session リンク一覧画面の設定情報を積んだセッションオブジェクト
     * @return リンク一覧情報
     */
    public Enumeration getLinkLibList(Common common, LinkListSession session) {

        Vector list = new Vector();
        
        try {
            //ＳＱＬ文
            String sqltxt = LINKLIBRARY_SELECT_SQL;
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
     * リンク情報を削除する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param vec 削除対象リンクコードのリスト
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
     * リンク情報を取得する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param companyCd 会社コード
     * @param linkCd リンクコード
     * @return リンク情報
     */
    public LinkLibInfo getLinkLibInfo(Common common,
				      String companyCd,
				      String linkCd) {

        LinkLibInfo info = null;
        try {
            //ＳＱＬ文
            String sqltxt = LINKLIBRARY_SELECT_INFO_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            // SQL 文を設定
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
                pstmt.setString(1, companyCd);
                pstmt.setString(2, linkCd);
                // SQL 実行
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
     * リンク情報を更新する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info リンク情報
     */
    public void updateLinkLibInfo(Common common, LinkLibInfo info) {

        try {
            String sqltxt = LINKLIBRARY_UPDATE_SQL;
	    if ( SysCnst.DEBUG ) {
		System.out.println("sql="+sqltxt);
	    }
            PreparedStatement pstmt = conn.prepareStatement(sqltxt);
            try {
                // パラメータを設定
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
     * リンク情報を追加する。
     * @param common ログイン者の情報を積んだセッションオブジェクト
     * @param info リンク情報
     * @return 実際に追加した数
     */
    public int insertLinkLibInfo(Common common, LinkLibInfo info) {

        int rc = 0;
        
        try {
            conn.setAutoCommit(false);

            // リンクコード自動採番
            String linkcd = "";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(LINKLIBRARY_MAX_CD_SQL);
            if (rs.next()) {
                linkcd = (rs.getString("next_link_cd")).trim();
            }
            stmt.close();
            
            // リンク情報追加
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
