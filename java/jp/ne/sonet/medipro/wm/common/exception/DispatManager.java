package jp.ne.sonet.medipro.wm.common.exception;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>�f�B�X�p�b�`��</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class DispatManager {
    /**
     * DispatManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public DispatManager() {
        super();
    }

    /**
     * �W���G���[�y�[�W��\������B
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
     * �W���G���[�y�[�W��\������B
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
     * �Z�b�V�����G���[�y�[�W��\������B
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
     * ���O�C���G���[�y�[�W��\������B
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
     * �h�n�G���[�y�[�W��\������B
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
     * �T�[�u���b�g�G���[�y�[�W��\������B
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
     * �����G���[�y�[�W��\������B
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







