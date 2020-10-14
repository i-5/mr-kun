
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import java.util.*;

/**
 * List of templates available to a particular company.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: TemplateCategory.java,v 1.1.2.1 2001/08/14 08:47:35 rick Exp $
 */
public class TemplateCategory extends BaseValueObject
{
  private String      templateCategoryId;
  private String      name;
  private String      description;
  private Company     company;
  private Collection  templateList;

  /**
   * Constructor
   */
  public TemplateCategory()
  {
    templateList = new ArrayList();
  }

  public String     getTemplateCategoryId() {return this.templateCategoryId;}
  public String     getName()               {return this.name;}
  public String     getDescription()        {return this.description;}
  public Company    getCompany()            {return this.company;}
  public Collection getTemplateList()       {return this.templateList;}

  public void setTemplateCategoryId(String templateCategoryId)  {this.templateCategoryId = templateCategoryId;}
  public void setName(String name)                              {this.name = name;}
  public void setDescription(String description)                {this.description = description;}
  public void setCompany(Company company)                        {this.company = company;}
  public void setTemplateList(Collection templateList)          {this.templateList = templateList;}

}

