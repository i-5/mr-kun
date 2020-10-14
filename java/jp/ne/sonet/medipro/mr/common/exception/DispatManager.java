package jp.ne.sonet.medipro.mr.common.exception;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/22 午後 07:03:24)
 * @author: 
 */
public class DispatManager {
    /**
     * Distribute コンストラクター・コメント。
     */
    public DispatManager() {
		super();
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 06:53:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException 例外記述
     */
    public void distribute(HttpServletRequest req, HttpServletResponse res) {
		try {
			res.sendRedirect("/medipro/err/Err.jsp");
		} catch (Exception e) {
			//throw new MrException(e);
		}
    }

    /**
     * DBエラーコード処理機能付き.
     */
    public void distribute(HttpServletRequest req,
						   HttpServletResponse res,
						   Exception ex) {
        try {
			if (ex instanceof MrException) {
				Exception e = ((MrException)ex).getOriginalException();
		
				if (e instanceof SQLException) {
					HttpSession session = req.getSession(true);
					String errorCode
						= new Integer(((SQLException)e).getErrorCode()).toString();
					session.putValue("errorCode", errorCode);
					res.sendRedirect("/medipro/err/DBErr.jsp");
					return;
				}
			}
        } catch (IOException e) {}

		this.distribute(req, res);
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 06:53:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException 例外記述
     */
    public void distSession(HttpServletRequest req, HttpServletResponse res) {
		try {
			// 2001.04.24 追加 -->
			// 極めて稀だが正常にセッションを取得できなかった場合
			HttpSession session = req.getSession();
			if (session.getValue("IllegalStateError") != null) {
				session.removeValue("IllegalStateError");
				res.sendRedirect("/medipro/err/checkErr.jsp");
				// <-- 2001.04.24
			} else {
				res.sendRedirect("/medipro/err/SessionErr.jsp");
			}
		} catch (Exception e) {
			//throw new MrException(e);
		}
    }
}
