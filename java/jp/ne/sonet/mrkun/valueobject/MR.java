
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import java.util.*;
import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * This class models information related to a specific Medical Representative.
 * It is used as a value object for the purpose of transferring details
 * between the MR_ProfileCtlr servlet, the MRManager session bean and the
 * mr7.jsp view template.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: MR.java,v 1.1.2.10 2001/09/12 04:40:15 rick Exp $
 */
public class MR extends User
{
  private String          mrId;
  private Company         company;
  private WebImage        pictureDefault;
  private String          password;
  private Address         address;
  private Boolean         isOpen;
  private Boolean         openHoursSetting;
  private Integer         openDays; //0: N/A; 1: Mon-Fri; 2: Mon-Sat
  private String          openTime;  //  HH:mm
  private String          closeTime;  // HH:mm
  private String          jikoSyokai;
  private Collection      drList;
  private List      	    forwardEmail;
  private String		      email;
  private String          ccEmail;
  private PhoneContact    companyPhone;
  private PhoneContact    cellularPhone;
  private PhoneContact    faxPhone;

  /**
   * Constructor
   */
  public MR()
  {
    forwardEmail = new Vector();
    drList = new ArrayList();
  }

  public String getMrId()
  {
    return this.mrId;
  }

  public Company getCompany()
  {
    return this.company;
  }

  public WebImage getPictureDefault()
  {
    return this.pictureDefault;
  }

  public String getPassword()
  {
    return this.password;
  }

  public PhoneContact getCompanyPhone()
  {
    return this.companyPhone;
  }

  public String getEmail()
  {
    return this.email;
  }

  public Address getAddress()
  {
    return this.address;
  }

  public List getForwardEmailList()
  {
    return this.forwardEmail;
  }

  public void addForwardEmail(String addContact)
  {
    if (forwardEmail == null)
      forwardEmail = new Vector();
    forwardEmail.add(addContact);
  }

  public void deleteForwardEmail(int index)
  {
    if (forwardEmail != null)
      forwardEmail.remove(index);
  }

  public Boolean isOpen()
  {
    return this.isOpen;
  }

  public Boolean getOpenHoursSetting()
  {
    return this.openHoursSetting;
  }

  public Integer getOpenDays()
  {
    return this.openDays;
  }

  public String getOpenTime()
  {
    return this.openTime;
  }

  public String getCloseTime()
  {
    return this.closeTime;
  }

  public String getJikoSyokai()
  {
    return this.jikoSyokai;
  }


  public String getCCEmail()
  {
    return this.ccEmail;
  }

  public PhoneContact getFaxPhone()
  {
    return this.faxPhone;
  }

  public PhoneContact getCellularPhone()
  {
    return this.cellularPhone;
  }


  public void setMrId(String mrId)
  {
    this.mrId = mrId;
  }

  public void setCompany(Company company)
  {
    this.company = company;
  }

  public void setPictureDefault(WebImage pictureDefault)
  {
    this.pictureDefault = pictureDefault;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setCompanyPhone(PhoneContact companyPhone)
  {
    this.companyPhone = companyPhone;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  public void setIsOpen(Boolean isOpen)
  {
    this.isOpen = isOpen;
  }

  public void setOpenHoursSetting(Boolean openHoursSetting)
  {
    this.openHoursSetting = openHoursSetting;
  }

  public void setOpenDays(Integer openDays)
  {
    this.openDays = openDays;
  }

  public void setOpenTime(String openTime)
  {
    this.openTime = openTime;
  }

  public void setCloseTime(String closeTime)
  {
    this.closeTime = closeTime;
  }

  public void setCCEmail(String ccEmail)
  {
    this.ccEmail = ccEmail;
  }

  public void setJikoSyokai(String j)
  {
    this.jikoSyokai = j	;
    
  }
  public void setFaxPhone(PhoneContact faxPhone)
  {
    this.faxPhone = faxPhone;
  }

  public void setCellularPhone(PhoneContact cellularPhone)
  {
    this.cellularPhone = cellularPhone;
  }

  public Collection getDRList()
  {
    return this.drList;
  }

  public void addDRInformation(String drId)
  {
    this.drList.add(drId);
  }

  public void deleteDRInformation(String drId)
  {
    this.drList.remove(drId);
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getMrId() != null)
      masterElement.addContent(new Element("mrId").setText(getMrId()));

    if (getCompany() != null)
      masterElement.addContent(getCompany().toXML("company"));

    if (getPictureDefault() != null)
      masterElement.addContent(getPictureDefault().toXML("pictureDefault"));

    if (getPassword() != null)
      masterElement.addContent(new Element("password").setText(getPassword()));

    if (getAddress() != null)
      masterElement.addContent(getAddress().toXML("address"));

    if (isOpen() != null)
      masterElement.addContent(new Element("isOpen").setText("" + isOpen().booleanValue()));

    if (getOpenHoursSetting() != null)
      masterElement.addContent(new Element("openHoursSetting").setText("" + getOpenHoursSetting().booleanValue()));

    if (getOpenDays() != null)
      masterElement.addContent(new Element("openDays").setText("" + getOpenDays().intValue()));

    if (getOpenTime() != null)
      masterElement.addContent(new Element("openTime").setText(getOpenTime()));

    if (getCloseTime() != null)
      masterElement.addContent(new Element("closeTime").setText(getCloseTime()));

    if (getJikoSyokai() != null)
      masterElement.addContent(new Element("jikosyokai").setText(getJikoSyokai()));

    if (getDRList() != null)
      masterElement.addContent(collectionToXML("drInformationList", getDRList(), "drInformation"));

    if (getForwardEmailList() != null)
      masterElement.addContent(collectionToXML("forwardEmailList", getForwardEmailList(), "email"));

    if (getEmail() != null)
      masterElement.addContent(new Element("email").setText(getEmail()));

    if (getCCEmail() != null)
      masterElement.addContent(new Element("ccEmail").setText(getCCEmail()));

    if (getCompanyPhone() != null)
      masterElement.addContent(getCompanyPhone().toXML("companyPhone"));

    if (getCellularPhone() != null)
      masterElement.addContent(getCellularPhone().toXML("cellularPhone"));

    if (getFaxPhone() != null)
      masterElement.addContent(getFaxPhone().toXML("faxPhone"));

    return masterElement;
  }
}

