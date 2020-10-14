package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.File;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrCatchUpdateSession;
import jp.ne.sonet.medipro.wm.server.manager.MrCatchInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;


import jp.ne.sonet.medipro.wm.common.util.*;//1025 y-yamada add
import java.io.*;

/**
 * <strong>MRキャッチ画像追加・更新画面対応サーブレット</strong>.
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateServlet extends HttpServlet {
    /** servlet configuration */
//      private ServletConfig config = null;
    /** multipart/form-data処理オブジェクト */
//      private SmartUpload smartUpload = null;
    /** multipart/form-data対応要求オブジェクト */
//      private Request request = null;

    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        if (SysCnst.DEBUG) {
            log("MrCatchUpdateServlet is called");
        }
 
        //セッションチェック
        if (!new SessionManager().check(req)) {
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

		if (!DBConnect.isConnectable()) {
            new DispatManager().distribute(req, res);
			return;
		}

		SmartUpload smartUpload = new SmartUpload();
		Request request = null;
        try {
            smartUpload.init(getServletConfig());
            smartUpload.service(req, res);
            try {
                smartUpload.upload();
                request = smartUpload.getRequest();
            } catch (Exception e) {
            }
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            new DispatManager().distribute(req, res);
			return;
        }

        //画面パラメータの取得
        String save = getParameter(req, request, "save");
        String back = getParameter(req, request, "back");
        String saveOk = getParameter(req, request, "saveOk");
        String saveCancel = getParameter(req, request, "saveCancel");
        String mrId = getParameter(req, request, "mrId");
        String pictureName = getParameter(req, request, "pictureName");

        //セッションオブジェクトの取得、無ければ新たに登録
        MrCatchUpdateSession session = (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        if (session == null) {
            session = new MrCatchUpdateSession();
            req.getSession(true).putValue(SysCnst.KEY_MRCATCHUPDATE_SESSION, session);
        }

		if (getParameter(req, request, "default") != null) {
			session.setCheck(true);
		} else {
			session.setCheck(false);
		}

        //statusをクリア
        session.setStatus(MrCatchUpdateSession.NORMAL);
        session.setPictureName(pictureName);
		session.setMrId(mrId);

        String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngCatchUpdate/index.html";

        try {
            if (save != null) {
                session.setStatus(MrCatchUpdateSession.SAVE_CONFIRM);
                makeTemporaryFile(req, res, smartUpload);
                executeSave(req, res);
            } else if (back != null) {
                req.getSession(true).removeValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
                nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchListServlet?mrId=" + mrId;
            } else if (mrId != null) {
                String pictureCd = getParameter(req, request, "pictureCd");

                if (pictureCd != null) {
                    //変更
                    session.setLoadFlag(true);
                    session.setPictureCd(pictureCd);
                } else {
                    //新規
                    session = new MrCatchUpdateSession();
                    session.setMrId(mrId);
                    req.getSession(true).putValue(SysCnst.KEY_MRCATCHUPDATE_SESSION, session);
                }
            }

            res.sendRedirect(nextPage);
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            new DispatManager().distribute(req, res, ex);
        }
    }

    /**
     * パラメータ取得先の選択を行い、適切にqueryパラメータを取得する.
     * @param req  要求オブジェクト
     * @param name パラメータ名
     * @return パラメータ値
     */
    private String getParameter(HttpServletRequest req,
								Request request,
								String name) {
        String value = "";
	//	name = Converter.convert(name); //y-yamada add
        if (request == null) {         
          //return Converter.convert(req.getParameter(name));        
          value = req.getParameter(name);
        }                                    
        else
          value = request.getParameter(name);

        //return Converter.convert(request.getParameter(name));       
        if (value == null)
	  return null;
  	try
        {
	  value = new String(value.getBytes("8859_1"), "SJIS");
	}
        catch (UnsupportedEncodingException ex)
        {
	  throw new RuntimeException("Error decoding parameter" + ex.toString());
	}
  	return value;
    }

    /**
     * 保存実行(DBへの書き込みとTEMPファイルの移動)
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    private void executeSave(HttpServletRequest req, HttpServletResponse res) {
        MrCatchUpdateSession session
			= (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);
        
        DBConnect dbConnect = new DBConnect();
        Connection connection = null;

		try {
			connection = dbConnect.getDBConnect();
			MrCatchInfoManager manager = new MrCatchInfoManager(connection);

	    //画像コードが指定されていたら追加、そうでなければ更新
			if (session.getPictureCd() == null || session.getPictureCd().equals("")) {
				manager.insertMrCatchInfo(req.getSession(true));
			} else {
				manager.updateMrCatchInfo(req.getSession(true));
			}
	    
			if (session.isChecked()) {
				MrInfoManager mrManager = new MrInfoManager(connection);
				int c = mrManager.setDefaultPictureCd(common,
													  session.getPictureCd(),
													  session.getMrId());
			}
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}

        //temporary画像が設定されていたら、指定の場所に移動する.
        if (session.getTempPicture() != null && !session.getTempPicture().equals("")) {
            java.io.File file = new java.io.File(common.getDocumentRoot() + session.getTempPicture());
            java.io.File file2 = new java.io.File(common.getDocumentRoot() + session.getCatchInfo().getPicture());

            //移動先を念のため削除
            file2.delete();
            //移動先directoryの作成
            file2.getParentFile().mkdirs();
            //移動
            file.renameTo(file2);
            //クリア
            session.setTempPicture(null);
        }

        session.setStatus(MrCatchUpdateSession.SAVE_DONE);
    }

    /**
     * 保存確認の際のtemporary画像ファイルを保存する.
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    private void makeTemporaryFile(HttpServletRequest req,
								   HttpServletResponse res,
								   SmartUpload smartUpload) {
        MrCatchUpdateSession session = (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);
        Files files = smartUpload.getFiles();

        File file = files.getFile(0);

        //ファイル指定が無かったときは何もしない
        if (file.getFileName() == null || file.getFileName().equals("")) {
            return;
        }

        String fileName = common.getTempDir() + SysCnst.SEPARATOR + common.getMrId() + file.getFileName();
        
        if (SysCnst.DEBUG) {
            log("temporaty file = " + common.getDocumentRoot() + fileName);
        }

        try {
            //保存
            System.err.println("Saving to filename: " + common.getDocumentRoot() + fileName);
            file.saveAs(common.getDocumentRoot() + fileName);
            //temporary画像名のセット
            session.setTempPicture(fileName);
            //画像ファイル拡張子のセット
            session.setExtension(file.getFileExt());
        } catch (SmartUploadException  ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            throw new WmException(ex);
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            throw new WmException(ex);
        }
    }

    /**
     * サーブレット初期化を行う.
     * @param config configurationオブジェクト
     */
//      public void init(ServletConfig config) {
//          this.config = config;
//      }

}
