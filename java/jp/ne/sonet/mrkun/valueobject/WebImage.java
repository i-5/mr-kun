
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * This class models an image for the web within the application.
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: WebImage.java,v 1.1.2.2 2001/09/12 04:40:15 rick Exp $
 */
public class WebImage extends BaseValueObject implements AssetInterface
{
  private String  webImageId;
  private String  name;
  private String  imageType;
  private String  description;
  private String  fileName;
  private byte    content[];

  /**
   * Constructor
   */
  public WebImage()
  {
  }

  public String getWebImageId()
  {
    return this.webImageId;
  }

  public String getName()
  {
    return this.name;
  }

  public String getImageType()
  {
    return this.imageType;
  }

  public String getDescription()
  {
    return this.description;
  }

  public String getFileName()
  {
    return this.fileName;
  }

  public byte[] getContent()
  {
    return this.content;
  }

  public void setWebImageId(String webImageId)
  {
    this.webImageId = webImageId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setImageType(String imageType)
  {
    this.imageType = imageType;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public void setContent(byte content[])
  {
    this.content = content;
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getWebImageId() != null)
      masterElement.addContent(new Element("webImageId").setText(getWebImageId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getImageType() != null)
      masterElement.addContent(new Element("imageType").setText(getImageType()));

    if (getFileName() != null)
      masterElement.addContent(new Element("fileName").setText(getFileName()));

    if (getDescription() != null)
      masterElement.addContent(new Element("description").setText(getDescription()));

    if (getContent() != null)
      masterElement.addContent(new Element("content").setText("You don't want to know .... the size is " + getContent().length + " bytes"));

    return masterElement;
  }
}

 