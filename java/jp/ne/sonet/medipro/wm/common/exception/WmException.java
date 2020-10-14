package jp.ne.sonet.medipro.wm.common.exception;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/22 �ߌ� 05:09:26)
 * @author: 
 */
public class WmException extends RuntimeException {

    private Exception ex = null;

    /**
     * WmException �R���X�g���N�^�[�E�R�����g�B
     * @param e java.lang.Exception
     */
    public WmException(Exception e) {
	ex = e;
	Date date = new Date();
    
	System.err.println("*** WmException : "+ date.toString());

	try {
	    String message = e.getMessage();
	    if (message != null) {
		System.err.println(new String(message.getBytes("8859_1"), "SJIS"));
	    }
	} catch (UnsupportedEncodingException ex) {
	}

	e.printStackTrace();
    }

    public WmException() {
	super();
    }

    public WmException(String message) {
	super(message);
    }

    public Exception getOriginalException() {
	if (ex instanceof WmException) {
	    return ((WmException)ex).getOriginalException();
	}

	return ex;
    }
    
    /* �y�����񂩂�̃\�[�X���R�s�[ ��������*/
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
    /* �y�����񂩂�̃\�[�X���R�s�[ �����܂�*/
    
}
