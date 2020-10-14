
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import java.util.*;
import java.text.*;
import org.jdom.*;

/**
 * This class models a message from a DR to an MR.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Contact.java,v 1.1.2.3 2001/09/12 08:35:49 rick Exp $
 */
public class Contact extends BaseValueObject implements Message
{
  private String      messageId;
  private String      recipient;
  private String      sender;
  private String      title;
  private String      body;
  private Date        dateSent;
  private Date        dateRead;
  private Date        sentDeleteTime;
  private Date        receivedDeleteTime;
  private Collection  attachmentList;

  /**
   * Constructor
   */
  public Contact()  
  {
  	attachmentList  = new ArrayList();
  }

  public String      getMessageId()          {return this.messageId;}
  public String      getRecipient()          {return this.recipient;}
  public String      getSender()             {return this.sender;}
  public String      getTitle()              {return this.title;}
  public String      getBody()               {return this.body;}
  public Date        getSentDate()           {return this.dateSent;}
  public Date        getReadDate()           {return this.dateRead;}
  public Date        getSentDeleteDate()     {return this.sentDeleteTime;}
  public Date        getReceivedDeleteDate() {return this.receivedDeleteTime;}
  public Collection  getAttachmentList()     {return this.attachmentList;}

  public void setMessageId(String messageId)          {this.messageId = messageId;}
  public void setRecipient(String recipient)          {this.recipient = recipient;}
  public void setSender(String sender)                {this.sender = sender;}
  public void setTitle(String title)                  {this.title = title;}
  public void setBody(String body)                    {this.body = body;}
  public void setSentDate(Date sentDate)              {this.dateSent = sentDate;}
  public void setReadDate(Date readDate)              {this.dateRead = readDate;}
  public void setSentDeleteDate(Date sdt)             {this.sentDeleteTime = sdt;}
  public void setReceivedDeleteDate(Date rdt)         {this.receivedDeleteTime = rdt;}
  public void setAttachmentList(Collection atl)       {this.attachmentList = atl;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);
    masterElement.addAttribute("type", "Contact");

    if (getMessageId() != null)
      masterElement.addContent(new Element("messageId").setText(getMessageId()));

    if (getRecipient() != null)
      masterElement.addContent(new Element("recipient").setText(getRecipient()));

    if (getSender() != null)
      masterElement.addContent(new Element("sender").setText(getSender()));

    if (getTitle() != null)
      masterElement.addContent(new Element("title").setText(getTitle()));

    if (getBody() != null)
      masterElement.addContent(new Element("body").setText(getBody()));

    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    
    if (getSentDate() != null)
      masterElement.addContent(new Element("sentDate").setText(sdfDates.format(getSentDate())));

    if (getReadDate() != null)
      masterElement.addContent(new Element("readDate").setText(sdfDates.format(getReadDate())));

    if (getSentDeleteDate() != null)
      masterElement.addContent(new Element("sentDeleteDate").setText(sdfDates.format(getSentDeleteDate())));

    if (getReceivedDeleteDate() != null)
      masterElement.addContent(new Element("receiveDeleteDate").setText(sdfDates.format(getReceivedDeleteDate())));

    if (getAttachmentList() != null)
      masterElement.addContent(collectionToXML("attachmentList", getAttachmentList(), "attachment"));

    return masterElement;
  }
}

