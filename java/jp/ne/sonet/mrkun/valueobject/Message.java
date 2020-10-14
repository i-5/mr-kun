
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

import java.util.*;
import org.jdom.*;

/**
 * This interface serves to define the behaviours consistent across all
 * message types between DRs and MRs.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Message.java,v 1.1.2.8 2001/09/12 08:35:49 rick Exp $
 */
public interface Message
{
  public static final Integer  SENT          = new Integer(0);
  public static final Integer  RECEIVED      = new Integer(1);
  public static final Integer  SENT_RECEIVED = new Integer(2);

  public String      getMessageId();
  public String      getRecipient();
  public String      getSender();
  public String      getTitle();
  public String      getBody();
  public Date        getSentDate();
  public Date        getReadDate();
  public Date        getSentDeleteDate();
  public Date        getReceivedDeleteDate();
  public Collection  getAttachmentList();

  public void setMessageId(String messageId);
  public void setRecipient(String recipient);
  public void setSender(String sender);
  public void setTitle(String title);
  public void setBody(String body);
  public void setSentDate(Date sentDate);
  public void setReadDate(Date readDate);
  public void setSentDeleteDate(Date sdt);
  public void setReceivedDeleteDate(Date rdt);
  public void setAttachmentList(Collection atl);

  public Element toXML(String elementName);
}

