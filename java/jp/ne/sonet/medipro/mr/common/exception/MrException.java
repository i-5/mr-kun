package jp.ne.sonet.medipro.mr.common.exception;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/22 �ߌ� 05:09:26)
 * @author: 
 */
public class MrException extends RuntimeException {

    private Exception ex = null;
	
    /**
     * MrException �R���X�g���N�^�[�E�R�����g�B
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
     * Original�̗�O���擾����.
     * @return ���̗�O���ێ������O
     */
    /*�y�����񂩂�̃\�[�X���R�s�[�@��������*/
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
    /*�y�����񂩂�̃\�[�X���R�s�[�@�����܂�*/
