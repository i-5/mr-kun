
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import org.jdom.*;

/**
 * This class models a Link resource. These are provided from a lookup
 * table in the database.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author M.Mizuki
 * @version $Id: ResourceLink.java,v 1.1.2.3 2001/09/12 08:35:49 rick Exp $
 */
public class ResourceLink extends BaseValueObject implements AssetInterface
{
  private String  resourceLinkId;
  private String  name;
  private String  description;
  private String  category;
  private String  url;

  /**
   * Constructor
   */
  public ResourceLink()  {}

  public String getResourceLinkId() {return this.resourceLinkId;}
  public String getName()           {return this.name;}
  public String getDescription()    {return this.description;}
  public String getCategory()       {return this.category;}
  public String getURL()            {return this.url;}
  public String getSecURL(){
	return ProxySec.getURL(this.url);
  }

  public void setResourceLinkId(String resourceLinkId)  {this.resourceLinkId = resourceLinkId;}
  public void setName(String name)                      {this.name = name;}
  public void setDescription(String description)        {this.description = description;}
  public void setCategory(String category)              {this.category = category;}
  public void setURL(String url)                        {this.url = url;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getResourceLinkId() != null)
      masterElement.addContent(new Element("resourceLinkId").setText(getResourceLinkId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getCategory() != null)
      masterElement.addContent(new Element("category").setText(getCategory()));

    if (getDescription() != null)
      masterElement.addContent(new Element("description").setText(getDescription()));

    if (getURL() != null)
      masterElement.addContent(new Element("url").setText(getURL()));

    return masterElement;
  }
}

 