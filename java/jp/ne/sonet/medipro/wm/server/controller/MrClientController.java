package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.manager.SentakuTorokuInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrClientSession;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>MR管理-担当顧客変更画面Controllerクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrClientController {

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * 必要なデータをDBから読み込みセッションにセッションにセットします。
     * @param req     要求オブジェクト
     * @param res     応答オブジェクト
     * @param session セッションオブジェクト
     */
    public MrClientSession getMrClientSession(HttpServletRequest req,
					      HttpServletResponse res,
					      HttpSession session) {
	MrClientSession mrClientSession
	    = (MrClientSession)session.getValue(SysCnst.KEY_MRCLIENT_SESSION);

	//セッションが無いときは生成して登録
	if (mrClientSession == null) {
	    mrClientSession = new MrClientSession();
	    session.putValue(SysCnst.KEY_MRCLIENT_SESSION, mrClientSession);
	}

	//左側の情報を取得
	if (mrClientSession.isNeedToLeftMrLoad()) {
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    try {
		connection = dbConnect.getDBConnect();
		MrInfoManager mrManager = new MrInfoManager(connection);
		MrInfo mrInfo = mrManager.getMrInfo(mrClientSession.getLeftMr().getMrId());

		if (mrInfo == null) {
		    mrInfo = new MrInfo();
		    mrInfo.setMrId(mrClientSession.getLeftMr().getMrId());
		    mrClientSession.setStatus(MrClientSession.NO_MR_EXIST_ERROR);
		    mrClientSession.setLeftMrInfo(mrInfo, null);
		} else {
		    if (!hasUpdatePermission(session, mrInfo)) {
			//権限を持っていなかったらMR-IDを除いてクリア
			mrInfo = new MrInfo();
			mrInfo.setMrId(mrClientSession.getLeftMr().getMrId());
			mrClientSession.setLeftMrInfo(mrInfo, null);
			mrClientSession.setStatus(MrClientSession.AUTHORITY_ERROR);
		    } else {
			SentakuTorokuInfoManager senManager
			    = new SentakuTorokuInfoManager(connection);
			Vector drList
			    = senManager.getDrList(mrClientSession.getLeftMr().getMrId());

			mrClientSession.setLeftMrInfo(mrInfo, drList);
		    }
		}

	    } catch (Exception ex) {
		if (SysCnst.DEBUG) {
		    ex.printStackTrace();
		}
		new DispatManager().distribute(req, res);
	    } finally {
		dbConnect.closeDB(connection);
	    }
	}

	//右側の情報を取得
	if (mrClientSession.isNeedToRightMrLoad()) {
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    try {
		connection = dbConnect.getDBConnect();
		MrInfoManager mrManager = new MrInfoManager(connection);
		MrInfo mrInfo
		    = mrManager.getMrInfo(mrClientSession.getRightMr().getMrId());

		if (mrInfo == null) {
		    mrInfo = new MrInfo();
		    mrInfo.setMrId(mrClientSession.getRightMr().getMrId());
		    mrClientSession.setStatus(MrClientSession.NO_MR_EXIST_ERROR);
		    mrClientSession.setRightMrInfo(mrInfo, null);
		} else {
		    if (!hasUpdatePermission(session, mrInfo)) {
			//権限を持っていなかったらMR-IDを除いてクリア
			mrInfo = new MrInfo();
			mrInfo.setMrId(mrClientSession.getRightMr().getMrId());
			mrClientSession.setRightMrInfo(mrInfo, null);
			mrClientSession.setStatus(MrClientSession.AUTHORITY_ERROR);
		    } else {
			SentakuTorokuInfoManager
			    senManager = new SentakuTorokuInfoManager(connection);
			Vector drList
			    = senManager.getDrList(mrClientSession.getRightMr().getMrId());

			mrClientSession.setRightMrInfo(mrInfo, drList);
		    }
		}

	    } catch (Exception ex) {
		if (SysCnst.DEBUG) {
		    ex.printStackTrace();
		}
		new DispatManager().distribute(req, res);
	    } finally {
		dbConnect.closeDB(connection);
	    }
	}

	return mrClientSession;
    }

    /**
     * ログインユーザーの権限と入力したMR-IDの権限とを比較チェックする。
     * @param session セッション情報
     * @param info    入力したMR情報
     * @return        変更権限があったらtrue
     */
    private boolean hasUpdatePermission(HttpSession session, MrInfo info) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	if (!common.getCompanyCd().equals(info.getCompanyCd())) {
	    //違う会社
	    return false;
	} else if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
	    //ウェブマスターならOK

	    return true;
	} else if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
	    //サブマスター

//  	    if (info.getMasterFlg() != null && info.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
//  		//ウェブマスタだったら不可
//  		return false;
//  	    }

	    if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
		//支店のサブマスター

		if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) ||
		    common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		    //兼任してる場合は
		    return true;
		} else if (common.getShitenCd().equals(info.getShitenCd())) {
		    //そうでなくても支店コードが同じだったら
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//営業所のサブマスター

		if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) ||
		    common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		    //兼任してる場合は
		    return true;
//  		} else if (info.getMasterKengenSoshiki() != null &&
//  			   info.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
		    //支店のサブマスタは不可
//  		    return false;
		} else if (common.getShitenCd().equals(info.getShitenCd()) &&
			   common.getEigyosyoCd().equals(info.getEigyosyoCd())) {
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1)) {
		//属性1のサブマスター

		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		    common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    //兼任してる場合は
		    return true;
		} else if (common.getMrAttributeCd1().equals(info.getMrAttributeCd1()) ||
			   common.getMrAttributeCd1().equals(info.getMrAttributeCd2())) {
		    //そうでなくても属性1 or 2コードが同じだったら
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		//属性2のサブマスター

		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		    common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    //兼任してる場合は
		    return true;
		} else if (common.getMrAttributeCd2().equals(info.getMrAttributeCd1()) ||
			   common.getMrAttributeCd2().equals(info.getMrAttributeCd2())) {
		    //そうでなくても属性1 or 2コードが同じだったら
		    return true;
		}

		return false;
	    }
	}

	return false;
    }
}
