
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import java.util.*;
import java.text.*;

import org.jdom.*;


/**
 * This clss models ancillary information about a Medical Representative.
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: MrProfile.java,v 1.1.2.11 2001/09/14 07:11:20 rick Exp $
 */
public class MrProfile extends BaseValueObject
{
  private String  mrId;
  private MR      mr;
  private int     receivedMessages;//Number of new messages (EDetails) sent by this MR
  private String  unreadMessageId;
  private String  unreadSubject;
  private String  mrBannerPosition;
  private Date    unreadDate;
  private String  drMemo;
  
  public MrProfile() {}
  
  public MrProfile(String mrId, MR mr) 
  {
  	this.mrId = mrId;
    this.mr = mr;	
  }

  public MR getMR()	
  {
  	return this.mr;
  }
  public String getMrId()         {return this.mrId;}
  
  public String getDrMemo()		  {return this.drMemo;}
  
  public String getName()         {return this.mr.getKanjiName();}

  public String getKanaName()         {return this.mr.getKanaName();}

  public String getJikoSyokai()         {return this.mr.getJikoSyokai();}
  
  public String getCompany()
  {
    if (this.mr.getCompany() != null)
      return this.mr.getCompany().getName();
    else
      return null;
  }

  public String getCompanyURL()
  {
    if (this.mr.getCompany() != null)
      return this.mr.getCompany().getURL();
    else
      return null;
  }

  public String getCompanyLogo()
  {
    if (this.mr.getCompany() != null)
      if (this.mr.getCompany().getDefaultImage() != null)
        return this.mr.getCompany().getDefaultImage().getFileName();
      else
        return null;
    else
      return null;
  }

  public String getPicture()
  {
    if (this.mr.getPictureDefault() != null)
      return this.mr.getPictureDefault().getFileName();
    else
      return null;
  }

  public String getEmail()
  {
    if (this.mr.getEmail() != null)
      return this.mr.getEmail();
    else
      return null;
  }
  
  public Address getAddress()
  {
    if (this.mr.getAddress() != null)
      return this.mr.getAddress();
    else
      return null;
  }
  
  public String getKanjiName()
  {
    if (this.mr.getKanjiName() != null)
      return this.mr.getKanjiName();
    else
      return null;
  }

  public String getCompanyPhone()
  {
    if (this.mr.getCompanyPhone().getPhoneNumber() != null)
      return this.mr.getCompanyPhone().getPhoneNumber();
    else
      return null;
  }  
  
  public String getCellularPhone()
  {
    if (this.mr.getCellularPhone().getPhoneNumber() != null)
      return this.mr.getCellularPhone().getPhoneNumber();
    else
      return null;
  }  
  
  public String getFaxPhone()
  {
    if (this.mr.getFaxPhone().getPhoneNumber() != null)
      return this.mr.getFaxPhone().getPhoneNumber();
    else
      return null;
  }  

  public void setMrId(String mrId)
  {
    this.mrId = mrId;
  }
  
  public void setDrMemo(String drMemo)		  
  {
    this.drMemo = drMemo;
  }

  public String getMrBannerPosition()
  {
    return this.mrBannerPosition;
  }
  
  public void setMrBannerPosition(String mrBannerPosition)
  {
    this.mrBannerPosition = mrBannerPosition;
  }

  /**
   * Purpose: return receivedMessage	 
   * @param
   * @exception 
   * @since 
   * Pre-condition: none
   * Post-condition: none
   * @roseuid 3B1DD2B30028
   */
  public int getReceivedMessage()
  {
  	return this.receivedMessages;
  }
  
  /**
   * Purpose: set receivedMessage	 
   * @param Integer receivedMessage
   * @exception 
   * @since 
   * Pre-condition: none
   * Post-condition: none
   * @roseuid 3B1DD2B30028
   */
  public void setReceivedMessage(int receivedMessages)
  {
  	this.receivedMessages = receivedMessages;
  }

  public String getUnreadMessageId()                      {return this.unreadMessageId;}
  public void setUnreadMessageId(String unreadMessageId)  {this.unreadMessageId = unreadMessageId;}

  public String getUnreadSubject()                        {return this.unreadSubject;}
  public void setUnreadSubject(String unreadSubject)      {this.unreadSubject = unreadSubject;}

  public Date getUnreadDate()                             {return this.unreadDate;}
  public void setUnreadDate(Date unreadDate)              {this.unreadDate = unreadDate;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getMR() != null)
      masterElement.addContent(getMR().toXML("mr"));

    if (getDrMemo() != null)
      masterElement.addContent(new Element("drMemo").setText(getDrMemo()));

    if (getMrBannerPosition() != null)
      masterElement.addContent(new Element("mrBannerPosition").setText(getMrBannerPosition()));

    masterElement.addContent(new Element("receivedMessage").setText("" + getReceivedMessage()));

    if (getUnreadMessageId() != null)
      masterElement.addContent(new Element("unreadMessageId").setText(getUnreadMessageId()));

    if (getUnreadSubject() != null)
      masterElement.addContent(new Element("unreadSubject").setText(getUnreadSubject()));

    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    if (getUnreadDate() != null)
      masterElement.addContent(new Element("unreadDate").setText(sdfDates.format(getUnreadDate())));

    return masterElement;
  }
}

