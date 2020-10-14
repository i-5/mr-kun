package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * @author: Harry Behrens
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
     * @param ccAddr java.lang.String
     * @param ccName java.lang.String
     */
    public void addCc(String ccAddr, String ccName) {
	try {
	    message.addRecipient(Message.RecipientType.CC,
				 new InternetAddress(ccAddr, ccName, "ISO-2022-JP"));
	} catch (java.io.UnsupportedEncodingException e) {
	    throw new ApplicationError("MailUtil",e);
	} catch (MessagingException e) {
	    throw new ApplicationError("MailUtil",e);
	}
    }

    /**
     */
    public void send() 
    {
	  	Thread th = new Thread(this);
	  	Date from = new Date();
	  	th.start();
        Date start = new Date();
        java.util.Date end = new java.util.Date();
        
        SimpleDateFormat f = new SimpleDateFormat ("yyyy.MM.dd/hh:mm:ss:SSSS");
        System.out.println("MailUtil.send: sent mail from "
         +f.format(start)+" to "+f.format(end));
    }

    public void run() 
    {
		try 
	    {
		    // ���[���T�[�o�ɐڑ����A���M����
	//  	    session.setDebug(true);
		    Transport trans = session.getTransport();
		    trans.connect();
		    try {
			trans.send(message);
		    } finally {
			trans.close();
		    }
		} 
	    catch (javax.mail.NoSuchProviderException e) 
	    {
		    //���M�G���[���O���o��
		    e.printStackTrace();
		    //throw new ApplicationError("MailUtil",e);
		} 
	    catch (javax.mail.MessagingException e) 
	    {
		    //���M�G���[���O���o��
		    //throw new ApplicationError("MailUtil",e);
		    e.printStackTrace();
		}
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 02:51:45)
     * @param fromAddress java.lang.String
     * @param fromName java.lang.String
     */
    public void setFrom(String fromAddress, String fromName) {
	try {
	    message.setFrom(new InternetAddress(fromAddress, fromName, "ISO-2022-JP"));
	}
	catch (java.io.UnsupportedEncodingException e) {
	    throw new ApplicationError("MailUtil",e);
	}
	catch (MessagingException e) {
	    throw new ApplicationError("MailUtil",e);
	}
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 03:08:23)
     * @param subject java.lang.String
     */
    public void setSubject(String subject) {
	try {
	    message.setSubject(subject, "ISO-2022-JP");
	} catch (MessagingException e) {
	    throw new ApplicationError("MailUtil",e);
	}
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 03:09:04)
     * @param text java.lang.String
     */
    public void setText(String text) {
	try {
	    message.setText(text, "ISO-2022-JP");
	} catch (MessagingException e) {
	    throw new ApplicationError("MailUtil",e);
	}

    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 03:03:45)
     */
    public void setTo(String toAddr, String toName) {
	try {
	    message.setRecipient(Message.RecipientType.TO,
				 new InternetAddress(toAddr, toName, "ISO-2022-JP"));
	} catch (java.io.UnsupportedEncodingException e) {
	    throw new ApplicationError("MailUtil",e);
	} catch (MessagingException e) {
	    throw new ApplicationError("MailUtil",e);
	}
    }

	public static boolean checkEmailAddress(String address) {
		if (address == null || address.trim().equals("")) {
			return false;
		}
	
		int aCount = 0;
		// ���p�p�����ł��邱�Ƃ̊m�F
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
