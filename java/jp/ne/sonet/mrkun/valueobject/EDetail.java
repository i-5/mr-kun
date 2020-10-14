
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import java.util.*;
import java.text.*;

import org.jdom.*;

/**
 * This class implements the Message interface, modelling the typical
 * message sent by an MR to a DR.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class EDetail extends BaseValueObject implements Message, Cloneable
{
  private String      messageId;
  private Date        expireDate;
  private String      eDetailCategory;
  private String      recipient;
  private String      sender;
  private String      title;
  private String      body;
  private Date        dateSent;
  private Date        dateRead;
  private Date        sentDeleteTime;
  private Date        receivedDeleteTime;
  private Collection  attachmentList;
  private Collection  resourceList;
  private WebImage    image;
  private Date        cancelTime;

  /**
   * Constructor
   */
  public EDetail()
  {
    attachmentList  = new ArrayList();
    resourceList    = new ArrayList();
  }

  public String      getMessageId()          {return this.messageId;}
  public Date        getExpireDate()         {return this.expireDate;}
  public String      getEDetailCategory()    {return this.eDetailCategory;}
  public String      getRecipient()          {return this.recipient;}
  public String      getSender()             {return this.sender;}
  public String      getTitle()              {return this.title;}
  public String      getBody()               {return this.body;}
  public Date        getSentDate()           {return this.dateSent;}
  public Date        getReadDate()           {return this.dateRead;}
  public Date        getSentDeleteDate()     {return this.sentDeleteTime;}
  public Date        getReceivedDeleteDate() {return this.receivedDeleteTime;}
  public Collection  getAttachmentList()     {return this.attachmentList;}
  public Collection  getResourceList()       {return this.resourceList;}
  public Date        getCancelTime()         {return this.cancelTime;}
  public WebImage    getImage()              {return this.image;} 

  public void setMessageId(String messageId)          {this.messageId = messageId;}
  public void setExpireDate(Date expireDate)          {this.expireDate = expireDate;}
  public void setEDetailCategory(String eCat)         {this.eDetailCategory = eCat;}
  public void setRecipient(String recipient)          {this.recipient = recipient;}
  public void setSender(String sender)                {this.sender = sender;}
  public void setTitle(String title)                  {this.title = title;}
  public void setBody(String body)                    {this.body = body;}
  public void setSentDate(Date sentDate)              {this.dateSent = sentDate;}
  public void setReadDate(Date readDate)              {this.dateRead = readDate;}
  public void setSentDeleteDate(Date sdt)             {this.sentDeleteTime = sdt;}
  public void setReceivedDeleteDate(Date rdt)         {this.receivedDeleteTime = rdt;}
  public void setAttachmentList(Collection atl)       {this.attachmentList = atl;}
  public void setResourceList(Collection resList)     {this.resourceList = resList;}
  public void setCancelTime(Date cancelTime)          {this.cancelTime = cancelTime;}
  public void setImage(WebImage image)                {this.image = image;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);
    masterElement.addAttribute("type", "EDetail");

    if (getMessageId() != null)
      masterElement.addContent(new Element("messageId").setText(getMessageId()));

    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    
    if (getExpireDate() != null)
      masterElement.addContent(new Element("expireDate").setText(sdfDates.format(getExpireDate())));

    if (getEDetailCategory() != null)
      masterElement.addContent(new Element("edetailCategory").setText(getEDetailCategory()));

    if (getRecipient() != null)
      masterElement.addContent(new Element("recipient").setText(getRecipient()));

    if (getSender() != null)
      masterElement.addContent(new Element("sender").setText(getSender()));

    if (getTitle() != null)
      masterElement.addContent(new Element("title").setText(getTitle()));

    if (getBody() != null)
      masterElement.addContent(new Element("body").setText(getBody()));

    if (getSentDate() != null)
      masterElement.addContent(new Element("sentDate").setText(sdfDates.format(getSentDate())));

    if (getReadDate() != null)
      masterElement.addContent(new Element("readDate").setText(sdfDates.format(getReadDate())));

    if (getSentDeleteDate() != null)
      masterElement.addContent(new Element("sentDeleteDate").setText(sdfDates.format(getSentDeleteDate())));

    if (getReceivedDeleteDate() != null)
      masterElement.addContent(new Element("receiveDeleteDate").setText(sdfDates.format(getReceivedDeleteDate())));

    if (getAttachmentList() != null)
      masterElement.addContent(collectionToXML("attachmentList", getAttachmentList(), "attachmentLink"));

    if (getResourceList() != null)
      masterElement.addContent(collectionToXML("resourceList", getResourceList(), "resourceLink"));

    if (getImage() != null)
      masterElement.addContent(getImage().toXML("image"));

    if (getCancelTime() != null)
      masterElement.addContent(new Element("cancelDate").setText(sdfDates.format(getCancelTime())));

    return masterElement;
  }
}
