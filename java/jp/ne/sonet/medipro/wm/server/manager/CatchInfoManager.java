package jp.ne.sonet.medipro.wm.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;


/**
 * <strong>会社キャッチ画像情報管理</strong>
 * <br>会社キャッチ画像情報をDBから取得またはDB更新処理.
 * @author: 
 * @version:
 */
public class CatchInfoManager {
    /////////////////////////////////////////////
    //constants
    //
    /** 基本SQL */
    private static final String CATCH_INFO_MAINSQL = 
	"SELECT picture_cd, picture_name, picture, picture_type " + 
	"FROM catch_picture " +
	"WHERE delete_ymd is null AND mr_id is null AND company_cd = ";

	/** 画像取得SQL */
    private static final String CATCH_INFO_SQL = 
	"SELECT picture_cd, picture_name, picture, picture_type " + 
	"FROM catch_picture " +
	"WHERE picture_cd = ";

	/** デフォルト画像取得SQL */
    private static final String DEFAULT_PICTURE = 
	"SELECT picture_cd " + 
	"FROM company " + 
	"WHERE company_cd = ";
		
	/** デフォルト画像更新SQL */
    private static final String DEFAULT_UPDATE = 
	"UPDATE company set picture_cd = ?, update_userid= ?, " + 
	"update_time = sysdate " +
	"WHERE company_cd = ? ";

	/** キャッチ画像追加SQL */
    private static final String CATCH_INSERT = 
	"INSERT INTO catch_picture " +
	"(PICTURE_CD, COMPANY_CD, MR_ID, PICTURE_NAME, PICTURE, PICTURE_TYPE, " +
	"JIKOSYOKAI, TITLE, URL, UPDATE_TIME, UPDATE_USERID, DELETE_YMD) " +
	"VALUES(?, ?, null, ?, ?, ?, null, ?, null, sysdate, null, null) ";
		
    /** キャッチ画像削除SQL */
    private static final String CATCH_DELETE = 
	"UPDATE catch_picture set delete_ymd = sysdate " + 
	"WHERE picture_cd IN";

    /** キャッチ画像形式更新SQL */
    private static final String CATCH_TYPE_UPDATE = 
	"UPDATE catch_picture set picture_name = ?, picture = ?, " + 
	"picture_type = ?, update_userid = ?, update_time = sysdate " + 
	"WHERE picture_cd = ? ";

	/** キャッチ画像ファイルホーム取得SQL */
    private static final String CATCH_HOME = 
	"SELECT naiyo1 FROM constant_master " + 
	"WHERE constant_cd = 'CATCHHOME'";

    /** 会社prefix取得SQL */
    private static final String COMPANY_PREFIX = 
	"SELECT cd_prefix FROM company " + 
	"WHERE company_cd = ";

    /** 新規追加時画像コード取得SQL */
    private static final String PICTURE_CD = 
	"SELECT to_char(picture_cd.nextval, '0000000') as seq_picture FROM dual";
		
    /////////////////////////////////////////////
    //class variables
    //
    protected Connection conn;
    private Common common;
    private CatchListSession cases;
    private CatchUpdateSession cuses;
    private String comCD;				// 会社コード
    private String mrID;				// MRID

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * コンストラクタ.
     * @param conn Connection
     * @param session HttpSession
     */
    public CatchInfoManager(Connection conn, HttpSession session) {
	this.conn = conn;
	// セッションの取得
	common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);
	cases = (CatchListSession) session.getValue(SysCnst.KEY_CATCH_SESSION);
	cuses = (CatchUpdateSession) session.getValue(SysCnst.KEY_CATCHUPDATE_SESSION);
	// 会社コード取得
	comCD = common.getCompanyCd();
	// MRID取得
	mrID = common.getMrId();
    }

    /////////////////////////////////////////////
    //class methods
    //
    /**
     * 会社キャッチ画像情報(ソート指定)の取得.
     * @return Vector
     */
    public Vector getCatchInfo() {

	ResultSet	rs;
	Statement	stmt;
	CatchInfo	caInfo = null;
	Vector		catchList;
	String		sql;
	String		defaultCD = null;
	int			cnt;
		
	catchList = new Vector();
		
	try {
	    // デフォルト画像取得SQL文
	    sql = DEFAULT_PICTURE;
	    sql += "'" + comCD + "'";
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
	    }
	    stmt = conn.createStatement();
	    try {
		rs = stmt.executeQuery(sql);
		if ( rs.next() ) {
		    defaultCD = rs.getString("picture_cd");
		    cases.setDefaultCD(defaultCD);
		}
	    } 
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	    // 会社キャッチ画像取得SQL文
	    sql = CATCH_INFO_MAINSQL;
	    sql += "'" + comCD + "'";
	    sql += " ORDER BY picture_cd " + cases.getOrder();
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
	    }
	    stmt = conn.createStatement();
	    try {
				// SQL 実行
		rs   = stmt.executeQuery(sql);
		cnt = 0;
		while (rs.next()) {
		    caInfo = new CatchInfo();
		    caInfo.setPictureCD(rs.getString("picture_cd"));
		    caInfo.setPictureName(rs.getString("picture_name"));
		    caInfo.setPictureType(rs.getString("picture_type"));
		    caInfo.setPicture(rs.getString("picture"));
		    // デフォルト画像コードと一致するものにフラグセット
		    if (defaultCD != null) {
			if (defaultCD.equals(rs.getString("picture_cd"))) {
			    caInfo.setDefaultFlg(true);
			}
			else {
			    caInfo.setDefaultFlg(false);
			}
		    }
		    else {
			caInfo.setDefaultFlg(false);
		    }
		    // ページ開始行からページ表示行数のみ格納する
		    if (cnt >= (cases.getCurrentRow() - 1) && 
			cnt < (cases.getCurrentRow() + 
			       common.getCatchLine() - 1) ) {
			catchList.add(caInfo);
		    }
		    cnt++;
		}
				// 最大行数のセット
		cases.setMaxRow(cnt);
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	}
	catch (SQLException e) 
	    {
		throw new WmException(e);
	    }
	return catchList;
    }

    /**
     * 会社キャッチ画像情報の取得.
     * @return jp.ne.sonet.medipro.wm.server.entity.CatchInfo
     */
    public CatchInfo getCatch() {

	ResultSet	rs;
	Statement	stmt;
	CatchInfo	caInfo = null;
	String		sql;
		
	String pictureCD = cuses.getPictureCD();
		
	try {
	    // デフォルト画像取得SQL文
	    sql = DEFAULT_PICTURE;
	    sql += "'" + comCD + "'";
	    stmt = conn.createStatement();
	    try {
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
		    String picCd = rs.getString("picture_cd");
		    if (pictureCD.equals(picCd)) {
			cuses.check();
		    } else {
			cuses.unCheck();
		    }
		}
	    } catch (SQLException e) {
		throw new WmException(e);
	    } finally {
		stmt.close();
	    }

	    // デフォルト画像取得SQL文
	    sql = CATCH_INFO_SQL;
	    sql += "'" + pictureCD + "'";
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
	    }
	    stmt = conn.createStatement();
	    try {
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
		    caInfo = new CatchInfo();
		    caInfo.setPictureType(rs.getString("picture_type"));
		    caInfo.setPictureName(rs.getString("picture_name"));
		    caInfo.setPicture(rs.getString("picture"));
		    cuses.setPicture(rs.getString("picture"));
		    if (cuses.isFirst() == true) {
			cuses.setPictureType(rs.getString("picture_type"));
		    }
		}
	    } 
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	}
	catch (SQLException e) 
	    {
		throw new WmException(e);
	    }
	return caInfo;
    }

    /**
     * デフォルト画像更新.
     * @param pictureCD String
     */
    public void changeDefault(String pictureCD) {

	ResultSet	rs;
	String		sql;
	PreparedStatement	prest;
		
	try {
	    // デフォルト画像取得SQL文
	    sql = DEFAULT_UPDATE;
	    prest = conn.prepareStatement(sql);
			
	    try {
		prest.setString(1, pictureCD);
		prest.setString(2, mrID);
		prest.setString(3, comCD);
				
		conn.setAutoCommit(false);
		prest.executeUpdate();
	    } catch (SQLException e) {
		conn.rollback();
		throw new WmException(e);
	    } finally {
		conn.commit();
		prest.close();
	    }
	} catch (SQLException e) {
	    throw new WmException(e);
	}
    }

    /**
     * デフォルト画像削除.
     */
    public void deleteCatch() {

	ResultSet	rs;
	String		sql;
	Statement	stmt;
	Vector		pictureList;
		
	// 削除する画像コード取得
	pictureList = (Vector) (cases.getCheckValue().clone());
	// チェックボックスの状態リストクリア
	cases.crearCheckValue();
	if (SysCnst.DEBUG) {
	    for (int j = 0; j < pictureList.size(); j++){
		System.out.println("delete CD :"+ pictureList.elementAt(j));
	    }
	}
	try {
	    // デフォルト画像取得SQL文
	    sql = CATCH_DELETE;
	    sql += "(";
	    for (int i = 0; i < pictureList.size(); i++) {
		if (i != 0) {
		    sql += ",";
		}
		sql += "'" + pictureList.elementAt(i) + "'";
	    }
	    sql += ")";
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
	    }
	    stmt = conn.createStatement();
			
	    try {
		conn.setAutoCommit(false);
		stmt.executeUpdate(sql);
	    } 
	    catch (SQLException e) {
		conn.rollback();
		throw new WmException(e);
	    }
	    finally {
		conn.commit();
		stmt.close();
	    }
	}
	catch (SQLException e) 
	    {
		throw new WmException(e);
	    }
    }

    /**
     * キャッチ画像更新.
     */
    public void changeCatch() {

	String		sql;
	PreparedStatement	prest;
	Statement	stmt;
	ResultSet	rs;
		
	String pictureCD = cuses.getPictureCD();
	String pictureType = cuses.getPictureType();
	String pictureName = cuses.getPictureName();
	String picture = cuses.getPicture();
	String prefix = null;
		
	cuses.crearPictureType();
	cuses.crearPictureName();

	try {
	    if (cuses.getPath() != null) {
		picture = common.getCatchHome();
		// 会社prefix取得SQL文
		sql = COMPANY_PREFIX;
		sql += "'" + comCD + "'";
				
		stmt = conn.createStatement();
				
		try {
		    rs = stmt.executeQuery(sql);
		    while (rs.next()) {
			prefix = rs.getString("cd_prefix");
		    }
		}
		catch (SQLException e) {
		    throw new WmException(e);
		}
		finally {
		    stmt.close();
		}
				
		String type = cuses.getType();
				
				// 画像ファイルディレクトリ編集
		picture += "/" + prefix + "/";
		char end = pictureCD.charAt(pictureCD.length() - 1);
		picture += end + "/" + pictureCD;
		picture += "." + type;
				
				// 画像ファイルパスをセッションにセット
		cuses.setPicture(picture);
	    }
	    // 画像更新SQL文
	    sql = CATCH_TYPE_UPDATE;
	    prest = conn.prepareStatement(sql);
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
		System.out.println("pictureCD :" + pictureCD);
		System.out.println("pictureName : "+pictureName);
		System.out.println("pictureType : "+pictureType);
		System.out.println("mrID : "+mrID);
	    }
	    try {
				// パラメータセット
		prest.setString(1, pictureName);
		prest.setString(2, picture);
		prest.setString(3, pictureType);
		prest.setString(4, mrID);
		prest.setString(5, pictureCD);
				
		conn.setAutoCommit(false);
		prest.executeUpdate();
	    } catch (SQLException e) {
		conn.rollback();
		throw new WmException(e);
	    } finally {
		conn.commit();
		prest.close();
		conn.setAutoCommit(true);
	    }

	    if (!cuses.getCheck().equals("")) {
		this.changeDefault(pictureCD);
	    }
	} catch (SQLException e) {
	    throw new WmException(e);
	}
    }

    /**
     * キャッチ画像追加.
     */
    public void insertCatch() {

	ResultSet	rs;
	String		sql;
	PreparedStatement	prest;
	Statement	stmt;
		
	String pictureType = cuses.getPictureType();	// 画像形式
	String pictureName = cuses.getPictureName();	// 画像名
	String type = cuses.getType();					// gif or jpeg
	String picture = common.getCatchHome();			// キャッチ画像ホーム
	String prefix = null;
	String pictureCD = null;
		

	//	cuses.crearPictureType();
	//	cuses.crearPictureName();
	//	cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
	try {
	    // 新規追加時画像コード取得SQL文
	    sql = PICTURE_CD;
			
	    stmt = conn.createStatement();
	    try {
		if (SysCnst.DEBUG) {
		    System.err.println(sql);
		}
		rs = stmt.executeQuery(sql);
		while ( rs.next() ) {
		    //System.err.println("pictureCD:"+rs.getString("seq_picture"));
		    pictureCD = (rs.getString("seq_picture")).trim();
		}
	    }
	    catch (SQLException e) {
		//	System.out.println(sql);
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	    // 会社prefix取得SQL文
	    sql = COMPANY_PREFIX;
	    sql += "'" + comCD + "'";
			
	    stmt = conn.createStatement();
	    if (SysCnst.DEBUG) {
		System.err.println(sql);
	    }
	    try {
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
		    //System.err.println("cdprefix:"+rs.getString("cd_prefix"));

		    prefix = rs.getString("cd_prefix");
		}
	    }
	    catch (SQLException e) {
		//	System.out.println(sql);
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
			
	    // 画像ファイルディレクトリ編集
	    picture += "/" + prefix + "/";
	    char end = pictureCD.charAt(pictureCD.length() - 1);
	    picture += end + "/" + pictureCD;
	    picture += "." + type;
	    // 画像ファイルパスをセッションにセット
	    cuses.setPicture(picture);
	    cuses.setPictureCD(pictureCD);
	    // 画像追加SQL文
	    sql = CATCH_INSERT;
	    prest = conn.prepareStatement(sql);
	    if (SysCnst.DEBUG) {
		System.out.println("pictureCD : "+pictureCD);
		System.out.println("pictureName : "+pictureName);
		System.out.println("pictureType : "+pictureType);
		System.out.println("picture : "+picture);
		System.out.println("comCD : "+comCD);
		System.out.println("mrID : "+mrID);
	    }
	    try {
				// パラメータセット
		prest.setString(1, pictureCD);
		prest.setString(2, comCD);
		prest.setString(3, pictureName);
		prest.setString(4, picture);
		prest.setString(5, pictureType);
		prest.setString(6, mrID);
				
		conn.setAutoCommit(false);
		prest.executeUpdate();
	    } 
	    catch (SQLException e) {
		//	System.out.println(sql);
		conn.rollback();
		throw new WmException(e);
	    }
	    finally {
		conn.commit();
		prest.close();
	    }

	    if (!cuses.getCheck().equals("")) {
		this.changeDefault(pictureCD);
	    }
	} catch (SQLException e) {
	    throw new WmException(e);
	}
    }
}
