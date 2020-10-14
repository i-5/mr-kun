
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

/**
 * This class models an EDetaili template.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Template.java,v 1.1.2.4 2001/08/14 14:15:02 rick Exp $
 */
public class Template extends Object
{
  private String templateId;
  private String title;
  private String body;
  private String description;
  private String category;

  /**
   * Constructor
   */
  public Template()  {}

  public String getTemplateId()     {return this.templateId;}
  public String getTitle()          {return this.title;}
  public String getBody()           {return this.body;}
  public String getDescription()    {return this.description;}
  public String getCategory()       {return this.category;}

  public void setTemplateId(String templateId)    {this.templateId = templateId;}
  public void setTitle(String title)              {this.title = title;}
  public void setBody(String body)                {this.body = body;}
  public void setDescription(String description)  {this.description = description;}
  public void setCategory(String category)        {this.category = category;}
}

