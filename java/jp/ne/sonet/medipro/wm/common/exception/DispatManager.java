package jp.ne.sonet.medipro.wm.common.exception;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>ディスパッチャ</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class DispatManager {
    /**
     * DispatManager オブジェクトを新規に作成する。
     */
    public DispatManager() {
        super();
    }

    /**
     * 標準エラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distribute(HttpServletRequest req,
			   HttpServletResponse res) {
        try {
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT+"err/Err.jsp");
        }
	catch (IOException e) {}
    }


    /**
     * 標準エラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distribute(HttpServletRequest req,
			   HttpServletResponse res,
			   Exception ex) {
        try {
	    if (ex instanceof WmException) {
		Exception e = ((WmException)ex).getOriginalException();
		
		if (e instanceof SQLException) {
		    HttpSession session = req.getSession(true);
		    String errorCode
			= new Integer(((SQLException)e).getErrorCode()).toString();
		    session.putValue("errorCode", errorCode);
		    res.sendRedirect(SysCnst.HTML_ENTRY_POINT+"err/DBErr.jsp");
		    return;
		}
	    }
        } catch (IOException e) {}

	this.distribute(req, res);
    }

    /**
     * セッションエラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distSession(HttpServletRequest req,
			    HttpServletResponse res) {
        try {
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "err/SessionErr.jsp");
        }
	catch (IOException e) {}
    }

    /**
     * ログインエラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distLogin(HttpServletRequest req,
			  HttpServletResponse res) {
        try {
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "err/LoginErr.jsp");
        }
	catch (IOException e) {}
    }

    /**
     * ＩＯエラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distIO(HttpServletRequest req,
		       HttpServletResponse res) {
        try {
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "err/IOErr.jsp");
        }
	catch (IOException e) {}
    }

    /**
     * サーブレットエラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distServlet(HttpServletRequest req,
			    HttpServletResponse res) {
        try {
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "err/ServletErr.jsp");
        }
	catch (IOException e) {}
    }

    /**
     * 権限エラーページを表示する。
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     */
    public void distAuthority(HttpServletRequest req,
			      HttpServletResponse res) {
        try {
//              res.sendError(HttpServletResponse.SC_NOT_FOUND);
            res.sendRedirect(SysCnst.HTML_ENTRY_POINT + "err/AuthorityErr.jsp");
        }
	catch (IOException e) {}
    }

}







