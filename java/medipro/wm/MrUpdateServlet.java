package medipro.wm;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;



import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.util.Converter;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrUpdateSession;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MR管理 - MRの追加・変更画面対応Servletクラス.</strong>
 * @author
 * @version
 */
public class MrUpdateServlet extends HttpServlet {

    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrUpdateServlet called!");
		}

		//セッションチェック
		if (! new SessionManager().check(req)) {
			new DispatManager().distSession(req, res);
			return;
		}

		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

	//権限チェック
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
			!common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
			new DispatManager().distAuthority(req, res);
			return;
		}
	

		//画面パラメータの取得
		String action = req.getParameter("action");		//操作名
		String mrId = req.getParameter("id");			//MR IDクリック時
		String back = req.getParameter("back");			//戻るボタン
		String save = req.getParameter("save");			//保存ボタン
		String updateOk = req.getParameter("updateOk");		//保存実行ボタン
		String updateCancel = req.getParameter("updateCancel");	//保存中止ボタン
		String inputOk = req.getParameter("inputOk");		//入力不足ボタン
		String shitenNew = Converter.getParameter(req, "shiten");//支店名

	//MR一追加・変更セッションオブジェクトの取得
	//無ければ新たに登録
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);
		if (session == null) {
			session = new MrUpdateSession();
			req.getSession(true).putValue(SysCnst.KEY_MRUPDATE_SESSION, session);
		}
		String shitenOld = session.getShitenCd();		//変更前の支店コード

//  	session.setStatus(MrUpdateSession.NORMAL);
		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrUpdate/index.html";

		try {
			//とりあえず画面項目をセッションに保持
			setMrInfo(session, req);

			if (back != null) {
				//MR一覧に戻る
                req.getSession(true).removeValue(SysCnst.KEY_MRUPDATE_SESSION);
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
			} else if (save != null) {
		
				//更新確認
				if (action.equals("change")) {
					//支店を変更したのでとりあえず営業所をクリア
					session.setEigyosyoCd("");
				} else {
					if (checkAllItem(req)) {
						//  			session.setStatus(MrUpdateSession.SAVE_CONFIRM);
						executeUpdate(req, res);
					}
				}
				//  	    } else if (updateOk != null) {
				//  		//更新実行
				//  		executeUpdate(req, res);
			} else if (inputOk != null) {
				//入力不足OKボタン
				session.setStatus(MrUpdateSession.NORMAL);
				//  	    } else if (updateCancel != null) {
				//  		//更新Cancelボタン
				//  		session.setStatus(MrUpdateSession.NORMAL);
			} else if (action.equals("update")) {
				//更新画面
				session.clear();
				session.setLoadFlag(true);
			} else {
				//MR一覧に戻る
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
				session.setStatus(MrUpdateSession.NORMAL);
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}
    }

    /**
     * queryパラメータをsessionパラメータにコピー
     * @param session セッションオブジェクト
     * @param req     要求オブジェクト
     */
    private void setMrInfo(MrUpdateSession session, HttpServletRequest req) {
		session.setMrId(req.getParameter("mrId"));
		session.setNameKana(Converter.getParameter(req, "nameKana"));
		session.setName(Converter.getParameter(req, "name"));
		if (Converter.getParameter(req, "shiten") != null &&
			!Converter.getParameter(req, "shiten").equals(session.getShitenCd())) {
			session.setSoshikiDeprivation(true);
		}
		session.setShitenCd(Converter.getParameter(req, "shiten"));
		if (Converter.getParameter(req, "eigyosyo") != null &&
			!Converter.getParameter(req, "eigyosyo").equals(session.getEigyosyoCd())) {
			session.setSoshikiDeprivation(true);
		}
		session.setEigyosyoCd(Converter.getParameter(req, "eigyosyo"));
		if (Converter.getParameter(req, "attribute1") != null &&
			!Converter.getParameter(req, "attribute1").equals(session.getMrAttributeCd1())) {
			session.setAttributeDeprivation(true);
		}
		session.setMrAttributeCd1(Converter.getParameter(req, "attribute1"));
		if (Converter.getParameter(req, "attribute2") != null &&
			!Converter.getParameter(req, "attribute2").equals(session.getMrAttributeCd2())) {
			session.setAttributeDeprivation(true);
		}
		session.setMrAttributeCd2(Converter.getParameter(req, "attribute2"));
		session.setPassword(req.getParameter("password"));
		session.setPictureCd(req.getParameter("pictureCd"));
		session.setNyusyaYear(Converter.getParameter(req, "nyusyaYear"));
    }

    private boolean checkAllItem(HttpServletRequest req) {
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);

	// 2001/03/09 Y.Yama 作業依頼書 MRK00005に対応 add
	// MR IDに空白が含まれているかチェック
	// この際末尾の空白もチェック対象としてほしいとM3水城さんより要求あり
	// なのでtrimはしない
		String reqMrId = req.getParameter("mrId");
		if (reqMrId != null){
			//空白を含んだMR IDの場合はセッションに空白エラーをセット
			if (reqMrId.indexOf(" ") >= 0){
				session.setStatus(MrUpdateSession.INNER_SPACE_ERROR);
				return false;
			}else if(reqMrId.indexOf("\t") >= 0){
				session.setStatus(MrUpdateSession.INNER_TAB_ERROR);
				return false;
			}
			// add end
			// change (MRK00005に伴いif -> else ifに変更)
			//	if (req.getParameter("nameKana") == null || req.getParameter("nameKana").equals("")) {
		} else if (req.getParameter("nameKana") == null || req.getParameter("nameKana").equals("")) {
			session.setStatus(MrUpdateSession.NAMEKANA_NOINPUT);
			return false;
		} else if (req.getParameter("name") == null || req.getParameter("name").equals("")) {
			session.setStatus(MrUpdateSession.NAME_NOINPUT);
			return false;
		} else if (req.getParameter("password") == null || req.getParameter("password").equals("")) {
			session.setStatus(MrUpdateSession.PASSWORD_NOINPUT);
			return false;
		} else {
			String val = Converter.getParameter(req, "nyusyaYear");
			if (val == null || val.equals("")) {
			} else {
				try {
					new Integer(val);
				} catch (NumberFormatException ex) {
					session.setStatus(MrUpdateSession.NUMBER_FORMAT_ERROR);
					return false;
				}
			}
		}
	
		return true;
    }

    /**
     * 更新実行.必須入力チェックも行います.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void executeUpdate(HttpServletRequest req, HttpServletResponse res) {
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);
		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

		if (!checkAllItem(req)) {
			return;
		}

		MrInfo info = session.getMrInfo();
		DBConnect dbConnect = new DBConnect();
		Connection connection = null;

		try {
		
			HttpSession httpsession = req.getSession(true);
			String wmId = (String)httpsession.getValue("wmId");//1213 y-yamada add　登録するwmのIdを渡す
		
			connection = dbConnect.getDBConnect();
			MrInfoManager manager = new MrInfoManager(connection);
			String mrId = info.getMrId();
			//MRIDがセットされていれば更新、でなければ追加
			//  	    if (mrId == null || mrId.equals("")) {
			if (session.isAdding()) {
				//manager.insertMr(info, common);
				manager.insertMr(info, common, wmId);//1213 y-yamada add　登録するwmのIdを渡す
				session.setAdding(false);
			} else {
				if (info.getMasterFlg() != null &&
					info.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				} else {
					//支店・営業所が変更されたらnullをセット
					if (session.isSoshikiDeprivation()) {
						info.setMasterKengenSoshiki(null);
						info.setTelNo(null);
						info.setFaxNo(null);
					}
		
					//属性1・属性2が変更されたらnullをセット
					if (session.isAttributeDeprivation()) {
						info.setMasterKengenAttribute(null);
					}

					//サブマスターでなくする
					if (info.getMasterKengenSoshiki() == null &&
						info.getMasterKengenAttribute() == null) {
						info.setMasterFlg(null);
					}
				}
		
				manager.updateMr(info, common);
			}
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}

		session.setStatus(MrUpdateSession.SAVE_DONE);
    }

}
