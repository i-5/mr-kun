package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jp.ne.sonet.medipro.wm.common.exception.WmException;

/**
 * <strong>支店情報管理クラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class ShitenInfoManager {
    /** 支店一覧取得 */
    protected static final String SHITEN_LIST_SQL
	= "SELECT"
	+ " shiten_cd"
	+ ",shiten_name"
	+ " FROM shiten"
	+ " WHERE company_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND shiten_name IS NOT NULL";

    /** コネクション */
    protected Connection connection;

    /**
     * DBとのConnectionを保持するオブジェクトを生成する.
     * @param initConnection このオブジェクトが使用するConnection
     */
    public ShitenInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     * 指定した会社コードで絞り込んだ支店コード、支店名のリストを取得する.
     * @param  campanyCd 会社コード
     * @return (コード、名称)ペアのリスト
     */
    public Vector getShitenList(String companyCd) {
	Vector list = new Vector();
	list.addElement(new String[]{"", ""});

	try {
	    PreparedStatement pstmt = connection.prepareStatement(SHITEN_LIST_SQL);
	    
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		String[] pair = {rs.getString("shiten_cd"),
				 rs.getString("shiten_name")};

		list.addElement(pair);
	    }

	    pstmt.close();
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return list;
    }

}
