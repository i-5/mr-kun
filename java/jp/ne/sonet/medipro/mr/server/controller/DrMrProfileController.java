package jp.ne.sonet.medipro.mr.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*; 
import jp.ne.sonet.medipro.mr.common.exception.*; 
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * <h3>MR個人情報管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:53:44)
 * @author: 
 */
public class DrMrProfileController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrDrPofile コンストラクター・コメント。
     */
    public DrMrProfileController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>ＭＲ個人情報表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:01:24)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public TantoInfo createDisplay(HttpServletRequest req,
				   HttpServletResponse res,
				   String drID,
				   String mrID) {
	TantoInfo tantoinfo = null;
	
	try {
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
	    tantoinfo = tantomanager.getMrInfo( drID, mrID );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return tantoinfo;
    }

    /**
     * <h3>ＭＲ登録削除</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void mrDelete(HttpServletRequest req,
			 HttpServletResponse res,
			 String drID,
			 String mrID) {
	try {
	    TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);
	    tantoinfomanager.insertSentakuTourokuHist(drID, mrID);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * <h3>メモを保存</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 04:02:36)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param drID java.lang.String
     * @param mrID java.lang.String
     * @param drMemo java.lang.String
     */
    public void mrSave(HttpServletRequest req,
		       HttpServletResponse res,
		       String drID,
		       String mrID,
		       String drMemo ) {
	try {
	    TantoInfoManager tantomanager = new TantoInfoManager(conn);
	    tantomanager.updateDrMemo(drID, mrID ,drMemo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}
    }
}
