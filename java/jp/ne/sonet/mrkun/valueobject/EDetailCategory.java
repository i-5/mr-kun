
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * A Class class.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class EDetailCategory extends BaseValueObject
{
  private String  edetailCategoryId;
  private String  name;

  /**
   * Constructor
   */
  public EDetailCategory()  {}

  public String getEdetailCategoryId()  {return this.edetailCategoryId;}
  public String getName()               {return this.name;}

  public void setEdetailCategoryId(String edetailCategoryId) {this.edetailCategoryId = edetailCategoryId;}
  public void setName(String name) {this.name = name;}

}

 