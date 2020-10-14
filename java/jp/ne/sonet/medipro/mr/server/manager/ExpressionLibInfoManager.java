package jp.ne.sonet.medipro.mr.server.manager;

import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ne.sonet.medipro.mr.server.entity.ExpressionLibInfo;
import jp.ne.sonet.medipro.mr.common.exception.MrException; 

/**
 * <strong>定型文ライブラリ情報管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfoManager {
    /** 定型文情報一覧取得 */
    protected static final String EXPRESSION_LIST_SQL
	= "SELECT"
	+ " teikeibun_cd"
	+ ",title"
	+ " FROM teikeibun_lib"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL";

    /** 定型文本文取得 */
    protected static final String EXPRESSION_INFO_SQL
	= "SELECT"
	+ " honbun"
	+ " FROM teikeibun_lib"
	+ " WHERE"
	+ " teikeibun_cd = ?";
                                    
    /** コネクション・オブジェクト */
    protected Connection connection = null;

    /**
     * ExpressionLibInfoManager オブジェクトを新規に作成する。
     * @param conn コネクション・オブジェクト
     */
    public ExpressionLibInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    /**
     * 定型文一覧情報を取得する。
     * @param  campanyCd 会社コード
     * @return 定型文一覧情報
     */
    public Vector getExpressionLibList(String companyCd) {
        Vector list = new Vector();
        
        try {
	    PreparedStatement pstmt = connection.prepareStatement(EXPRESSION_LIST_SQL);
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		ExpressionLibInfo info = new ExpressionLibInfo();
		info.setTeikeibunCd(rs.getString("teikeibun_cd"));
		info.setTitle(rs.getString("title"));
		
		list.addElement(info);
	    }

	    pstmt.close();
        } catch (SQLException e) {
            throw new MrException(e);
        }
	
        return list;
    }

    /**
     * 定型文情報を取得する。
     * @param  expressionCd 定型文コード
     * @return 定型文情報
     */
    public ExpressionLibInfo getExpressionLibInfo(String expressionCd) {
	ExpressionLibInfo info = null;

        try {
	    PreparedStatement pstmt = connection.prepareStatement(EXPRESSION_INFO_SQL);
	    pstmt.setString(1, expressionCd);

	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
		info = new ExpressionLibInfo();
		info.setHonbun(rs.getString("honbun"));
	    }
	    
	    pstmt.close();
        } catch (SQLException e) {
            throw new MrException(e);
        }
	
	return info;
    }

}
