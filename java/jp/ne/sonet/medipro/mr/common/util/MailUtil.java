package jp.ne.sonet.medipro.mr.common.util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

/**
 * <h3>メール送信ユーティリティ</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/01 午後 02:36:46)
 * @author: 
 */
public class MailUtil implements Runnable {
    protected static final String PROTO_SMTP = "smtp";
    //
    protected Session session;
    protected MimeMessage message;

    /**
     *
     */
    public MailUtil(String mailHost) {
	Properties prop = new Properties();
	prop.put("mail.host", mailHost);
	prop.put("mail.transport.protocol", PROTO_SMTP);

	session = Session.getInstance(prop, null);
	message = new MimeMessage(session);
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:06:02)
     * @param ccAddr java.lang.String
     * @param ccName java.lang.String
     */
    public void addCc(String ccAddr, String ccName) {
	try {
	    message.addRecipient(Message.RecipientType.CC,
				 new InternetAddress(ccAddr, ccName, "ISO-2022-JP"));
	} catch (java.io.UnsupportedEncodingException e) {
	    throw new MrException(e);
	} catch (MessagingException e) {
	    throw new MrException(e);
	}
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:11:27)
     */
    public void send() {
//  	Thread th = new Thread(this);
//  	th.start();
	try {
	    // メールサーバに接続し、送信する
  	    session.setDebug(true);
	    Transport trans = session.getTransport();
	    trans.connect();
	    try {
		trans.send(message);
	    } finally {
		trans.close();
	    }
	} catch (javax.mail.NoSuchProviderException e) {
	    //送信エラーログ書出し
  	    e.printStackTrace();
	    //throw new MrException(e);
	} catch (javax.mail.MessagingException e) {
	    //送信エラーログ書出し
	    //throw new MrException(e);
  	    e.printStackTrace();
	}
    }

    public void run() {
	try {
	    // メールサーバに接続し、送信する
//  	    session.setDebug(true);
	    Transport trans = session.getTransport();
	    trans.connect();
	    try {
		trans.send(message);
	    } finally {
		trans.close();
	    }
	} catch (javax.mail.NoSuchProviderException e) {
	    //送信エラーログ書出し
	    e.printStackTrace();
	    //throw new MrException(e);
	} catch (javax.mail.MessagingException e) {
	    //送信エラーログ書出し
	    //throw new MrException(e);
	    e.printStackTrace();
	}
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 02:51:45)
     * @param fromAddress java.lang.String
     * @param fromName java.lang.String
     */
    public void setFrom(String fromAddress, String fromName) {
	try {
	    message.setFrom(new InternetAddress(fromAddress, fromName, "ISO-2022-JP"));
	}
	catch (java.io.UnsupportedEncodingException e) {
	    throw new MrException(e);
	}
	catch (MessagingException e) {
	    throw new MrException(e);
	}
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:08:23)
     * @param subject java.lang.String
     */
    public void setSubject(String subject) {
	try {
	    message.setSubject(subject, "ISO-2022-JP");
	} catch (MessagingException e) {
	    throw new MrException(e);
	}
    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:09:04)
     * @param text java.lang.String
     */
    public void setText(String text) {
	try {
	    message.setText(text, "ISO-2022-JP");
	} catch (MessagingException e) {
	    throw new MrException(e);
	}

    }

    /**
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:03:45)
     */
    public void setTo(String toAddr, String toName) {
	try {
	    message.setRecipient(Message.RecipientType.TO,
				 new InternetAddress(toAddr, toName, "ISO-2022-JP"));
	} catch (java.io.UnsupportedEncodingException e) {
	    throw new MrException(e);
	} catch (MessagingException e) {
	    throw new MrException(e);
	}
    }

	public static boolean checkEmailAddress(String address) {
		if (address == null || address.trim().equals("")) {
			return false;
		}
	
		int aCount = 0;
		// 半角英数字であることの確認
		for (int i = 0; i < address.length(); i++) {
			char c = address.charAt(i);
			if ((c >= 'a' && c <= 'z') || 
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				(c == '.') || (c == '_') || (c == '-') || (c == '/') ||
				(c == '@')) {
				if (c =='@') aCount++;
			} else {
				return false;
			}
		}

		if (aCount == 1) return true;
		return false;
	}
}
