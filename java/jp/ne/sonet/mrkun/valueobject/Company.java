
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * This class models a company (owner of an MR) and it's attributes
 * within the system.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: Company.java,v 1.1.2.6 2001/09/14 01:24:45 damon Exp $
 */
public class Company extends BaseValueObject
{
  private String    companyId;
  private String    name;
  private WebImage  defaultImage;
  private String    companyPrefix;
  private String    url;
  private String    wmEmailAddress;
  private String    displayRanking;

  /**
   * Constructor
   */
  public Company()
  {
  }

  public String getCompanyId()
  {
    return this.companyId;
  }

  public String getName()
  {
    return this.name;
  }

  public String getDisplayRanking()
  {
    return this.displayRanking;
  }
  
  public WebImage getDefaultImage()
  {
    return this.defaultImage;
  }

  public String getWmEmailAddress()
  {
    return this.wmEmailAddress;
  }

  public void setCompanyId(String companyId)
  {
    this.companyId = companyId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setDisplayRanking(String displayRanking)
  {
    this.displayRanking = displayRanking;
  }

  public void setDefaultImage(WebImage defaultImage)
  {
    this.defaultImage = defaultImage;
  }

  public String getCompanyPrefix()
  {
    return this.companyPrefix;
  }

  public void setCompanyPrefix(String companyPrefix)
  {
    this.companyPrefix = companyPrefix;
  }

  public String getURL()
  {
    return this.url;
  }

  public void setURL(String url)
  {
    this.url = url;
  }

  public void setWmEmailAddress(String wmEmailAddress)
  {
    this.wmEmailAddress = wmEmailAddress;
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getCompanyId() != null)
      masterElement.addContent(new Element("companyId").setText(getCompanyId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getURL() != null)
      masterElement.addContent(new Element("url").setText(getURL()));

    if (getCompanyPrefix() != null)
      masterElement.addContent(new Element("companyPrefix").setText(getCompanyPrefix()));

    if (getWmEmailAddress() != null)
      masterElement.addContent(new Element("wmEmailAddress").setText(getWmEmailAddress()));

    if (getDefaultImage() != null)
      masterElement.addContent(getDefaultImage().toXML("wmEmailAddress"));

    return masterElement;
  }

}

