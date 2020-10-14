
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import org.jdom.*;

/**
 * This class models an Attachment to a message.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: AttachmentLink.java,v 1.1.2.2 2001/09/12 08:35:49 rick Exp $
 */
public class AttachmentLink extends BaseValueObject implements AssetInterface
{
  private String  attachmentLinkId;
  private String  name;
  private String  fileName;
  private String  description;
  private byte    content[];

  /**
   * Constructor
   */
  public AttachmentLink()  {}

  public String getAttachmentLinkId()   {return this.attachmentLinkId;}
  public String getName()               {return this.name;}
  public String getFileName()           {return this.fileName;}
  public String getDescription()        {return this.description;}
  public byte[] getContent()            {return this.content;}

  public void setAttachmentLinkId(String attachmentLinkId)  {this.attachmentLinkId = attachmentLinkId;}
  public void setName(String name)                          {this.name = name;}
  public void setFileName(String fileName)                  {this.fileName = fileName;}
  public void setDescription(String description)            {this.description = description;}
  public void setContent(byte content[])                    {this.content = content;}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getAttachmentLinkId() != null)
      masterElement.addContent(new Element("attachmentLinkId").setText(getAttachmentLinkId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getFileName() != null)
      masterElement.addContent(new Element("fileName").setText(getFileName()));

    if (getDescription() != null)
      masterElement.addContent(new Element("description").setText(getDescription()));

    if (getContent() != null)
      masterElement.addContent(new Element("content").setText("Too much info for your pretty little eyes .... size is: " + getContent().length + " bytes"));

    return masterElement;
  }

}

 