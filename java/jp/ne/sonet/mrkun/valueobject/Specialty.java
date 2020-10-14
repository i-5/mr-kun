
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * Specialty class.
 * <P>
 * @author M.Mizuki</a>
 */
public class Specialty extends BaseValueObject
{
  private String specialtyCd;
  private String specialtyName;

  /**
   * Constructor
   */
  public Specialty() { }

  public String getSpecialtyCd()   {return this.specialtyCd;}
  public String getSpecialtyName() {return this.specialtyName;}

  public void setSpecialtyCd(String specialtyCd)       {this.specialtyCd = specialtyCd;}
  public void setSpecialtyName(String specialtyName)   {this.specialtyName = specialtyName;}

}

