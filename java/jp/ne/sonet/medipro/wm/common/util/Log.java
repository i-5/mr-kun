package jp.ne.sonet.medipro.wm.common.util;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * サーブレットログクラス
 * ログファイルに記録を残したい場合に使用すること.
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
