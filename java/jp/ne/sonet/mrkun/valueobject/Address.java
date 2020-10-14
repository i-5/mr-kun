
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * Models an address
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: Address.java,v 1.1.2.2 2001/09/12 04:40:15 rick Exp $
 */
public class Address extends BaseValueObject
{
  private String  addressId;
  private String  areaCode;
  private String  location;

  /**
   * Constructor
   */
  public Address()
  {
  }

  public String getAreaCode()
  {
    return this.areaCode;
  }

  public String getLocation()
  {
    return this.location;
  }

  public String getAddressId()
  {
    return this.addressId;
  }

  public void setAreaCode(String areaCode)
  {
    this.areaCode = areaCode;
  }

  public void setLocation(String location)
  {
    this.location = location;
  }

  public void setAddressId(String addressId)
  {
    this.addressId = addressId;
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getAreaCode() != null)
      masterElement.addContent(new Element("areaCode").setText(getAreaCode()));

    if (getLocation() != null)
      masterElement.addContent(new Element("location").setText(getLocation()));

    return masterElement;
  }

}


