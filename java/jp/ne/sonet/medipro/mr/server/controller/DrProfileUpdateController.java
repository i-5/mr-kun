package jp.ne.sonet.medipro.mr.server.controller;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class DrProfileUpdateController {
    
    public DrProfileUpdateController() {
    }

    /**
     * DBから読み込み
     */
    public DoctorProperty getDrProperty(HttpServletRequest req,
					HttpServletResponse res,
					HttpSession session) {
	String drId = (String)session.getValue("com_drid");
	DoctorProperty prop = null;
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    DrProfileInfoManager manager = new DrProfileInfoManager(connection);
	    prop = manager.getDoctorProperty(drId);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return prop;
    }

    public boolean isEnqueteEnd(HttpServletRequest req,
				HttpServletResponse res,
				HttpSession session) {
	String drId = (String)session.getValue("com_drid");
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    DrProfileInfoManager manager = new DrProfileInfoManager(connection);
	    return manager.isEnqueteEnd(drId);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return false;
    }

    /**
     * 診療科一覧の取得
     */
    public Enumeration getShinryokaList(HttpServletRequest req,
					HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    ShinryokaInfoManager manager = new ShinryokaInfoManager(connection);
	    list = manager.getShinryokaList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 専門一覧の取得
     */
    public Enumeration getSenmonList(HttpServletRequest req,
				     HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    SenmonInfoManager manager = new SenmonInfoManager(connection);
	    list = manager.getSenmonList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 学部一覧の取得
     */
    public Enumeration getGakubuList(HttpServletRequest req,
				     HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    GakubuInfoManager manager = new GakubuInfoManager(connection);
	    list = manager.getGakubuList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 元号一覧の取得
     */
    public Enumeration getGengoList(HttpServletRequest req,
				    HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    GengoInfoManager manager = new GengoInfoManager(connection);
	    list = manager.getGengoList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 都道府県一覧の取得
     */
    public Enumeration getTodofukenList(HttpServletRequest req,
					HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    TodofukenInfoManager manager = new TodofukenInfoManager(connection);
	    list = manager.getTodofukenList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 市町村一覧の取得
     */
    public Enumeration getShicyosonList(HttpServletRequest req,
					HttpServletResponse res,
					String todofuken) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    ShicyosonInfoManager manager = new ShicyosonInfoManager(connection);
	    list = manager.getShicyosonList(todofuken);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }

    /**
     * 資格一覧の取得
     */
    public Enumeration getShikakuList(HttpServletRequest req,
				      HttpServletResponse res) {
	Vector list = new Vector();
	DBConnect dbConnect = new DBConnect();
	Connection connection = dbConnect.getDBConnect();

	try {
	    ShikakuInfoManager manager = new ShikakuInfoManager(connection);
	    list = manager.getShikakuList();
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	} finally {
	    dbConnect.closeDB(connection);
	}

	return list.elements();
    }
}
