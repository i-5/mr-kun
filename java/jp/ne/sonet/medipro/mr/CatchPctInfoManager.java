package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.mrkun.framework.exception.*;


/**
 * @author: Harry Behrens
 */
public class CatchPctInfoManager {
    protected Connection conn;

    /**
     * CatchPctInfoManager コンストラクター・コメント。
     */
    public CatchPctInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * @return java.util.Enumeration (CatchPctInfo)
     * @param mrID java.lang.String
     */
    public Enumeration getCatchPct(String mrID) {
	CatchPctInfo catchPct;
	Vector enum = new Vector();
	String sqltxt = null;

	try {
	    sqltxt = "SELECT picture_cd FROM mr WHERE mr_id = '" + mrID + "'";
	    Statement stmt = conn.createStatement();
	    String defaultPictureCd = null;

	    try {
		ResultSet rs = stmt.executeQuery(sqltxt);
		if (rs.next()) {
		    defaultPictureCd = rs.getString("picture_cd");
		}
	    } finally {
		stmt.close();
	    }
	    
	    //ＳＱＬ文
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt +  "catch_picture.picture_cd, ";
	    sqltxt = sqltxt +  "catch_picture.picture_name, ";
	    sqltxt = sqltxt +  "catch_picture.picture, ";
	    sqltxt = sqltxt +  "catch_picture.picture_type ";
	    sqltxt = sqltxt +  "FROM ";
	    sqltxt = sqltxt +  "catch_picture ";

	    // begin 2000/08/23 ENICOM辻森 会社の画像を加える
	    sqltxt = sqltxt +  "WHERE catch_picture.delete_ymd is null ";
	    sqltxt = sqltxt +  "AND catch_picture.mr_id = '" + mrID + "' OR ";
	    sqltxt = sqltxt +  "(catch_picture.delete_ymd is null and catch_picture.mr_id is null AND catch_picture.company_cd = ";
	    sqltxt = sqltxt +  "(select company_cd from mr where delete_ymd is null and mr_id = '" + mrID + "'))";
	    sqltxt = sqltxt +  "ORDER BY catch_picture.mr_id, catch_picture.picture_cd";
	    // 2000/09/13 ENICOM辻森　mr_idでソート、MR画像を先に出す
	    // end 2000/08/23 ENICOM辻森 会社の画像を加える

	    //sqltxt = sqltxt +  "WHERE";
	    //sqltxt = sqltxt +  "mr_id = '" + mrID + "' ";
	    //sqltxt = sqltxt +  "AND delete_ymd is null ORDER BY ";
	    //sqltxt = sqltxt +  "picture_cd";
	
	    stmt = conn.createStatement();
	    try {
		ResultSet rs   = stmt.executeQuery(sqltxt);
		    
		while ( rs.next() ) {
		    catchPct = new CatchPctInfo();
		    catchPct.setPictureCD(rs.getString("picture_cd"));
		    catchPct.setPictureName(rs.getString("picture_name"));
		    catchPct.setPicture(rs.getString("picture"));
		    catchPct.setPictureType(rs.getString("picture_type"));
		    //デフォルトが指定されている場合の処理
		    if (defaultPictureCd != null &&
			defaultPictureCd.equals(rs.getString("picture_cd"))) {
			enum.add(0, catchPct);
		    } else {
			enum.addElement(catchPct);
		    }
		}
	    } finally {
		//rs.close();
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new ApplicationError("CatchPctInfoManager.getCatchPct: SQLException",e);
	}

	return enum.elements();
    }

    /**
     * @return jp.ne.sonet.medipro.mr.CatchPctInfo
     * @param pictureCD java.lang.String
     */
    public CatchPctInfo getCatchPctInfo(String pictureCD) {

	CatchPctInfo catchPct = null;
	String sqltxt = null;

	try {
	    //ＳＱＬ文
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt + "picture, ";
	    sqltxt = sqltxt + "picture_type ";
	    sqltxt = sqltxt + "FROM ";
	    sqltxt = sqltxt + "catch_picture ";
	    sqltxt = sqltxt + "WHERE ";
	    sqltxt = sqltxt + "picture_cd = '" + pictureCD + "' ";
	    sqltxt = sqltxt + "AND delete_ymd is null ";
	
	    Statement stmt = conn.createStatement();
	    try {
		ResultSet rs = stmt.executeQuery(sqltxt);
		    
		while ( rs.next() ) {
		    catchPct = new CatchPctInfo();
		    catchPct.setPicture(rs.getString("picture"));
		    catchPct.setPictureType(rs.getString("picture_type"));
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	throw new ApplicationError("CatchPctInfoManager.getCatchPctInfo: SQLException",e);
	}


	return catchPct;
    }
}
