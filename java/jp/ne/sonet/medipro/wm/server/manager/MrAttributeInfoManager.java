package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import jp.ne.sonet.medipro.wm.common.exception.WmException; 

/**
 * <strong>MR属性テーブル管理クラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrAttributeInfoManager {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** 有効なMR属性コード、属性名のリスト取得 */
    protected static final String ATTRIBUTE_LIST_SQL
	= "SELECT"
	+ " mr_attribute_cd"
	+ ",mr_attribute_name"
	+ " FROM mr_attribute"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND mr_attribute_name IS NOT NULL";

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    /** コネクション */
    protected Connection connection = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    /**
     * DBとのConnectionを保持するオブジェクトを生成する.
     * @param initConnection このオブジェクトが使用するConnection
     */
    public MrAttributeInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * 指定した会社コードで絞り込んだMR属性コード、属性名のリストを取得する.
     * @param  campanyCd 会社コード
     * @return (コード、名称)ペアのリスト
     */
    public Vector getMrAttributeList(String companyCd) {
	Vector list = new Vector();
	list.addElement(new String[]{"", ""});

	try {
	    PreparedStatement pstmt = connection.prepareStatement(ATTRIBUTE_LIST_SQL);
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		String[] pair = {rs.getString("mr_attribute_cd"),
				 rs.getString("mr_attribute_name")};
		list.addElement(pair);
	    }
      
	    pstmt.close();
	} catch (SQLException ex) {
	    throw new WmException(ex);
	}

	return list;
    }
}
