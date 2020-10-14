package jp.ne.sonet.medipro.mr.common.exception;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/22 �ߌ� 07:03:24)
 * @author: 
 */
public class DispatManager {
    /**
     * Distribute �R���X�g���N�^�[�E�R�����g�B
     */
    public DispatManager() {
		super();
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 06:53:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException ��O�L�q
     */
    public void distribute(HttpServletRequest req, HttpServletResponse res) {
		try {
			res.sendRedirect("/medipro/err/Err.jsp");
		} catch (Exception e) {
			//throw new MrException(e);
		}
    }

    /**
     * DB�G���[�R�[�h�����@�\�t��.
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
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 06:53:07)
     * @param req javax.servlet.http.HttpServletRequest
     * @param res javax.servlet.http.HttpServletResponse
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException ��O�L�q
     */
    public void distSession(HttpServletRequest req, HttpServletResponse res) {
		try {
			// 2001.04.24 �ǉ� -->
			// �ɂ߂ċH��������ɃZ�b�V�������擾�ł��Ȃ������ꍇ
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
