package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrAttributeInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.ShitenInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.EigyosyoInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrUpdateSession;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>MR追加・更新Controllerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrUpdateController {

    /**
     * コードと名称のペアからなるVectorをコードをkeyとするHashtableに変換する.
     * @param list コードと名称のペアで構成された配列
     * @return コードをkeyとするHashtable
     */
    private Hashtable toHashtable(Vector list) {
	Hashtable table = new Hashtable();

	Enumeration e = list.elements();
	while (e.hasMoreElements()) {
	    String[] pair = (String[])e.nextElement();
	    table.put(pair[1], pair[0]);
	}
	return table;
    }

    /**
     * Commonに設定された会社コードを基に、支店コード、支店名のリストを生成します。
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     * @return コードと名称のペアからなる配列
     */
    public Vector getShitenList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    ShitenInfoManager manager = new ShitenInfoManager(con);
	    list = manager.getShitenList(common.getCompanyCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setShitenList(toHashtable(list));
	
	return list;
    }

    /**
     * Commonに設定された会社コード、支店コードを元に営業所リストを生成します。
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     * @return コードと名称のペアからなる配列
     */
    public Vector getEigyosyoList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    EigyosyoInfoManager manager = new EigyosyoInfoManager(con);

	    list = manager.getEigyosyoList(common.getCompanyCd(), mrUpdateSession.getShitenCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setEigyosyoList(toHashtable(list));

	return list;
    }

    /**
     * Commonに設定された会社コードを基に、属性コード、属性名のリストを生成します.
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     * @return コードと名称のペアからなる配列
     */
    public Vector getMrAttributeList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    MrAttributeInfoManager manager = new MrAttributeInfoManager(con);
	    list = manager.getMrAttributeList(common.getCompanyCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setAttributeList(toHashtable(list));

	return list;
    }

    /**
     * 必要なMR情報をセッションに積んで、そのセッションを取得します.
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     */
    public MrUpdateSession getMrUpdateSession(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	MrInfo info = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	Connection con = null;

	//セッションが無いときは生成して登録
	if (mrUpdateSession == null) {
	    mrUpdateSession = new MrUpdateSession();
	    session.putValue(SysCnst.KEY_MRUPDATE_SESSION, mrUpdateSession);
	}

	if (mrUpdateSession.isNeedToLoad()) {
	    //変更時
	    try {
		DBConnect dbConnect = new DBConnect();
		con = dbConnect.getDBConnect();
      
		MrInfoManager manager = new MrInfoManager(con);
		info = manager.getMrInfo(mrUpdateSession.getMrId());

		dbConnect.closeDB(con);
	    } catch (Exception ex) {
		ex.printStackTrace();
		new DispatManager().distribute(req, res);
	    }

	    mrUpdateSession.setMrInfo(info);
	    mrUpdateSession.setLoadFlag(false);
	    mrUpdateSession.setAdding(false);
	} else {
	    //追加時
	    //権限によってデフォルト値を設定

	    //支店 or 営業所のサブマスターで属性のサブマスターを兼任していない
	    if ((common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		 common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) &&
		!common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) &&
		!common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		//支店名のデフォルト値を設定
		mrUpdateSession.setShitenCd(common.getShitenCd());

		//さらに営業所のサブマスターだった場合は営業所名のデフォルト値を設定
		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    mrUpdateSession.setEigyosyoCd(common.getEigyosyoCd());
		}
	    }

	    //属性1のサブマスターで支店・営業所のサブマスターを兼任していない
	    if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//属性1のデフォルト値を設定
		mrUpdateSession.setMrAttributeCd1(common.getMrAttributeCd1());
	    }

	    //属性2のサブマスターで支店・営業所のサブマスターを兼任していない
	    if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//属性2のデフォルト値を設定
		mrUpdateSession.setMrAttributeCd2(common.getMrAttributeCd2());
	    }

	}

	return mrUpdateSession;
    }

}
