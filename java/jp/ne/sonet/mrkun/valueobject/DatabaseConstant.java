
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * A Class class.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class DatabaseConstant extends BaseValueObject
{
  private String constantId;
  private String name;
  private String naiyo1;
  private String naiyo2;
  private String naiyo3;

  /**
   * Constructor
   */
  public DatabaseConstant() { }

  public String getConstantId()     {return this.constantId;}
  public String getName()           {return this.name;}
  public String getNaiyo1()         {return this.naiyo1;}
  public String getNaiyo2()         {return this.naiyo2;}
  public String getNaiyo3()         {return this.naiyo3;}

  public void setConstantId(String constantId)  {this.constantId = constantId;}
  public void setName(String name)              {this.name = name;}
  public void setNaiyo1(String naiyo1)          {this.naiyo1 = naiyo1;}
  public void setNaiyo2(String naiyo2)          {this.naiyo2 = naiyo2;}
  public void setNaiyo3(String naiyo3)          {this.naiyo3 = naiyo3;}

}

