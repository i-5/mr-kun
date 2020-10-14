package jp.ne.sonet.medipro.mr.server.controller;

import java.util.Vector;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.mr.common.util.DBConnect;
import jp.ne.sonet.medipro.mr.common.exception.DispatManager; 
import jp.ne.sonet.medipro.mr.common.exception.MrException;
import jp.ne.sonet.medipro.mr.server.manager.ExpressionLibInfoManager;
import jp.ne.sonet.medipro.mr.server.entity.ExpressionLibInfo;

/**
 * <strong>定型文一覧Controller</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionListController {

    /**
     * ExpressionListController オブジェクトを新規に作成する。
     */
    public ExpressionListController() {
    }

    /**
     * 定型文一覧情報を取得する。
     * @param request   javax.servlet.http.HttpServletRequest
     * @param response  javax.servlet.http.HttpServletResponse
     * @param companyCd 会社コード
     * @return 定型文一覧情報
     */
    public Vector getExpressionList(HttpServletRequest request,
				    HttpServletResponse response,
				    String companyCd) {
	DBConnect dbconn = new DBConnect();
	Connection conn = dbconn.getDBConnect();
	Vector list = new Vector();

	try {

	    ExpressionLibInfoManager manager = new ExpressionLibInfoManager(conn);
	    list = manager.getExpressionLibList(companyCd);
	} catch (MrException e) {
            new DispatManager().distribute(request, response);
        } finally {
	    dbconn.closeDB(conn);
	}

        return list;
    }

    /**
     * 定型文情報を取得する。
     * @param request      javax.servlet.http.HttpServletRequest
     * @param response     javax.servlet.http.HttpServletResponse
     * @param expressionCd 定型文コード
     * @return 定型文情報
     */
    public ExpressionLibInfo getExpressionInfo(HttpServletRequest request,
					       HttpServletResponse response,
					       String expressionCd) {
	DBConnect dbconn = new DBConnect();
	Connection conn = dbconn.getDBConnect();
	ExpressionLibInfo info = null;

	try {
	    ExpressionLibInfoManager manager = new ExpressionLibInfoManager(conn);
	    info = manager.getExpressionLibInfo(expressionCd);
	} catch (MrException e) {
            new DispatManager().distribute(request, response);
        } finally {
	    dbconn.closeDB(conn);
	}

        return info;
    }

}
