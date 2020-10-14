package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.server.session.MrClientSession;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.manager.SentakuTorokuInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MessageManager;

/**
 * <strong>MR管理 - 担当顧客変更画面対応サーブレットクラス</strong>。
 * @author
 * @version
 */
public class MrClientServlet extends HttpServlet {

    /**
     * サービス定義.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        if (SysCnst.DEBUG) {
            log("MrClientServlet called!");
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
        String back = req.getParameter("back");                        //戻るボタン
        String save = req.getParameter("save");                        //保存ボタン
        String leftInput = req.getParameter("leftInput");        //入力ボタン(左)
        String rightInput = req.getParameter("rightInput");        //入力ボタン(右)
        String right = req.getParameter("right");                //移動ボタン→
        String left = req.getParameter("left");                        //移動ボタン←
        String saveOk = req.getParameter("saveOk");                //保存実行
        String saveCancel = req.getParameter("saveCancel");        //保存中止

        //MR担当顧客用セッションオブジェクトの取得 or 登録
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        if (session == null) {
            session = new MrClientSession();
            req.getSession(true).putValue(SysCnst.KEY_MRCLIENT_SESSION, session);
        }

        //statusを正常に...
        session.setStatus(MrClientSession.NORMAL);

        String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngClientUpdate/index.html";

        try {
            if (back != null) {
                //一覧に戻る
                nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrListServlet";
            } else if (save != null) {
                //保存確認
                if (!session.getLeftMrId().equals("") && !session.getRightMrId().equals("")) {
                    session.setStatus(MrClientSession.SAVE_CONFIRM);
                }
            } else if (leftInput != null) {
                //左側MRID入力
                setLeftMrInfo(req, res);
            } else if (rightInput != null) {
                //右側MRID入力
                setRightMrInfo(req, res);
            } else if (right != null) {
                //左 → 右へDRを移動
                moveDrToRight(req, res);
            } else if (left != null) {
                //右 → 左へDRを移動
                moveDrToLeft(req, res);
            } else if (saveOk != null) {
                //保存実行
                executeSave(req, res);
            } else if (saveCancel != null) {
                //保存中止
            }

            res.sendRedirect(nextPage);
        } catch (Exception ex) {
			log("", ex);
            new DispatManager().distribute(req, res, ex);
        }
    }

    /**
     * 左側の情報を更新します.
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void setLeftMrInfo(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String mrId = req.getParameter("leftMrId");

        if (mrId.equals("") || !mrId.equals(session.getRightMrId())) {
            //空白か右のMR-IDと異なれば通常どおりセット
            session.setLeftMrId(mrId);
        } else {
            //同一MR-IDをセットしたときはエラー
            session.setLeftMrId("");
            session.setStatus(MrClientSession.SAME_MRID_ERROR);
        }

        session.setRightMrId(session.getRightMrId());
    }

    /**
     * 右側の情報を更新します。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void setRightMrInfo(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String mrId = req.getParameter("rightMrId");

        if (mrId.equals("") || !mrId.equals(session.getLeftMrId())) {
            //空白か右のMR-IDと異なれば通常どおりセット
            session.setRightMrId(mrId);
        } else {
            //同一MR-IDをセットしたときはエラー
            session.setRightMrId("");
            session.setStatus(MrClientSession.SAME_MRID_ERROR);
        }
        
        session.setLeftMrId(session.getLeftMrId());
    }

    /**
     * 指定した左側のDRを右側に移します。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void moveDrToRight(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String drId = req.getParameter("leftSelection");

        if (drId == null) {
        } else {
            session.moveToRight(drId);
        }
    }

    /**
     * 指定した右側のDRを左側に移します。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void moveDrToLeft(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        String drId = req.getParameter("rightSelection");

        if (drId == null) {
        } else {
            session.moveToLeft(drId);
        }
    }

    /**
     * 更新結果をDBに反映します。
     * @param req 要求オブジェクト
     * @param res 返答オブジェクト
     */
    private void executeSave(HttpServletRequest req, HttpServletResponse res) {
        MrClientSession session
	    = (MrClientSession)req.getSession(true).getValue(SysCnst.KEY_MRCLIENT_SESSION);
        DBConnect dbConnect = new DBConnect();
        Connection connection = dbConnect.getDBConnect();
        
        try {
            connection.setAutoCommit(false);

            SentakuTorokuInfoManager manager = new SentakuTorokuInfoManager(connection);
	    MessageManager messageManager = new MessageManager(connection);

	    //左側のDRの内移行したものを保存
            Enumeration r2l = session.getLeftMr().getMovedDrList();
            while (r2l.hasMoreElements()) {
                MrClientSession.Dr dr = (MrClientSession.Dr)r2l.nextElement();
                manager.moveSentakuToroku(dr.getDrId(),
					  session.getRightMrId(),
					  session.getLeftMrId(),
					  dr.getSeq());
		messageManager.copyDrMessage(dr.getDrId(),
					     session.getRightMrId(),
					     session.getLeftMrId());
		messageManager.copyMrMessage(dr.getDrId(),
					     session.getRightMrId(),
					     session.getLeftMrId());
            }

	    //右側のDRの内移行したものを保存
            Enumeration l2r = session.getRightMr().getMovedDrList();
            while (l2r.hasMoreElements()) {
                MrClientSession.Dr dr = (MrClientSession.Dr)l2r.nextElement();
                manager.moveSentakuToroku(dr.getDrId(),
					  session.getLeftMrId(),
					  session.getRightMrId(),
					  dr.getSeq());
		messageManager.copyDrMessage(dr.getDrId(),
					     session.getLeftMrId(),
					     session.getRightMrId());
		messageManager.copyMrMessage(dr.getDrId(),
					     session.getLeftMrId(),
					     session.getRightMrId());
            }

            session.setStatus(MrClientSession.SAVE_DONE);
	    //リロードの為に再設定
	    session.setLeftMrId(session.getLeftMrId());
	    session.setRightMrId(session.getRightMrId());

	    connection.commit();
        } catch (Exception ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) {
	    }
            throw new WmException(ex);
        } finally {
	    try {
		connection.setAutoCommit(true);
	    } catch (SQLException e) {
	    }
            dbConnect.closeDB(connection);
        }
    }

}
