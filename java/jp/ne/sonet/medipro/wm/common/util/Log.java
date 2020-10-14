package jp.ne.sonet.medipro.wm.common.util;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * �T�[�u���b�g���O�N���X
 * ���O�t�@�C���ɋL�^���c�������ꍇ�Ɏg�p���邱��.
 */
public class Log {
    private ServletContext context;

    public Log(HttpServlet servlet) {
	context = servlet.getServletConfig().getServletContext();
    }

    public void print(String message) {
	context.log(message);
    }

    public void print(String message, Throwable e) {
	context.log(message, e);
    }
    
}
