package jp.ne.sonet.medipro.mr.common.exception;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/22 午後 05:09:26)
 * @author: 
 */
public class MrException extends RuntimeException {

    private Exception ex = null;
	
    /**
     * MrException コンストラクター・コメント。
     * @param e java.lang.Exception
     */
    public MrException(Exception e) {
	ex = e;
	
	try {
	    String message = e.getMessage();
	    if (message != null) {
		System.err.println(new String(message.getBytes("8859_1"), "SJIS"));
	    }
	} catch (UnsupportedEncodingException ex) {
	}
    }

    /**
     * Originalの例外を取得する.
     * @return この例外が保持する例外
     */
    /*土居さんからのソースをコピー　ここから*/
    public Exception getOriginalException() {
	if (ex instanceof MrException) {
	    return ((MrException)ex).getOriginalException();
	}

	return ex;
    }

    public void printStackTrace() {
	if (ex != null) {
	    ex.printStackTrace();
	}
	super.printStackTrace();
    }

    public void printStackTrace(PrintWriter pw) {
	if (ex != null) {
	    ex.printStackTrace(pw);
	}
	super.printStackTrace(pw);
    }

    public void printStackTrace(PrintStream ps) {
	if (ex != null) {
	    ex.printStackTrace(ps);
	}
	super.printStackTrace(ps);
    }
}
    /*土居さんからのソースをコピー　ここまで*/
