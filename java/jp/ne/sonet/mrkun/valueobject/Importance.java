
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.BaseValueObject;
import org.jdom.*;

/**
 * This class models an EDetaili template.
 *
 * @author M.Mizuki
 * @version $Id: Importance.java,v 1.1.2.2 2001/09/12 04:40:15 rick Exp $
 */
public class Importance extends BaseValueObject
{
  private String importanceId;
  private String name;

  /**
   * Constructor
   */
  public Importance()  {}

  public String getImportanceId()   {return this.importanceId;}
  public String getName()           {return this.name;}

  public void setImportanceId(String importanceId) {this.importanceId = importanceId;}
  public void setName(String name)                 {this.name = name;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getImportanceId() != null)
      masterElement.addContent(new Element("importanceId").setText(getImportanceId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    return masterElement;
  }

}

