
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * Models a set of phone contact details.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: PhoneContact.java,v 1.1.2.2 2001/09/12 04:40:15 rick Exp $
 */
public class PhoneContact extends BaseValueObject
{
  private String  phoneContactId;
  private String  name;
  private String  phoneNumber;

  /**
   * Constructor
   */
  public PhoneContact()
  {
  }

  public String getName()
  {
    return this.name;
  }

  public String getPhoneNumber()
  {
    return this.phoneNumber;
  }

  public String getPhoneContactId()
  {
    return this.phoneContactId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public void setPhoneContactId(String phoneContactId)
  {
    this.phoneContactId = phoneContactId;
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getPhoneContactId() != null)
      masterElement.addContent(new Element("phoneContactId").setText(getPhoneContactId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getPhoneNumber() != null)
      masterElement.addContent(new Element("phoneNumber").setText(getPhoneNumber()));

    return masterElement;
  }

}


