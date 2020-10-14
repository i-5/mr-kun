
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import java.util.Collection;

/**
 * This class models a category of resource links.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: ResourceCat.java,v 1.1.2.1 2001/08/07 07:00:07 rick Exp $
 */
public class ResourceCat extends BaseValueObject
{
  private String      resourceCatId;
  private String      name;
  private String      description;
  private Company     company;
  private Collection  resourceList;

  /**
   * Constructor
   */
  public ResourceCat() {}

  public String     getResourceCatId()  {return this.resourceCatId;}
  public String     getName()           {return this.name;}
  public String     getDescription()    {return this.description;}
  public Company    getCompany()        {return this.company;}
  public Collection getResourceList()   {return this.resourceList;}

  public void setResourceCatId(String resourceCatId)    {this.resourceCatId = resourceCatId;}
  public void setName(String name)                      {this.name = name;}
  public void setDescription(String description)        {this.description = description;}
  public void setCompany(Company company)               {this.company = company;}
  public void setResourceList(Collection resourceList)  {this.resourceList = resourceList;}
}

 