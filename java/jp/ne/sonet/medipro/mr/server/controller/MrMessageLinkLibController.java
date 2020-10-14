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
 * <h3> MSG（ライブラリ２）画面管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 午後 03:18:48)
 * @author: 
 */
public class MrMessageLinkLibController {
    protected Connection conn;
    protected DBConnect dbconn;

    /**
     * MrmessageBulid5Controller コンストラクター・コメント。
     */
    public MrMessageLinkLibController() {
	dbconn = new DBConnect();
	conn = dbconn.getDBConnect();
    }

    /**
     * <h3>MSG-ライブラリ２部分の作成</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 03:12:37)
     * @return java.util.Enumeration (LinkLibInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param linkCD java.lang.String
     * @param companyCD java.lang.String
     */
    public Enumeration createDisplay(HttpServletRequest req,
				     HttpServletResponse res,
				     String linkCD,
				     String companyCD) {
	Enumeration enum = null;

	try {
	    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
	    enum = linklibinfomanager.getLinkLibLastNext( linkCD , companyCD, "2");
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * <h3>前の２０の表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 03:23:28)
     * @return java.util.Enumeration (LinkLibInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param linkCD java.lang.String
     * @param companyCD java.lang.String
     */
    public Enumeration last20LinkLib(HttpServletRequest req,
				     HttpServletResponse res,
				     String linkCD,
				     String companyCD) {
	Enumeration enum = null;

	try {
	    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
	    enum = linklibinfomanager.getLinkLibLastNext( linkCD , companyCD, "1");
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

    /**
     * <h3>次の２０の表示</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 午後 03:24:08)
     * @return java.util.Enumeration (LinkLibInfo)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @param linkCD java.lang.String
     * @param companyCD java.lang.String
     */
    public Enumeration next20LinkLib(HttpServletRequest req,
				     HttpServletResponse res,
				     String linkCD,
				     String companyCD) {
	Enumeration enum = null;

	try {
	    LinkLibInfoManager linklibinfomanager = new LinkLibInfoManager(conn);
	    enum = linklibinfomanager.getLinkLibLastNext( linkCD , companyCD, "2");
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbconn.closeDB(conn);
	}

	return enum;
    }

}
