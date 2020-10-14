
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * medicalQualification class.
 * <P>
 * @author M.Mizuki</a>
 */
public class MedicalQualification extends BaseValueObject
{
  private String qualificationCd;
  private String qualificationName;

  /**
   * Constructor
   */
  public MedicalQualification() { }

  public String getQualificationCd()   {return this.qualificationCd;}
  public String getQualificationName() {return this.qualificationName;}

  public void setQualificationCd(String qualificationCd)       {this.qualificationCd = qualificationCd;}
  public void setQualificationName(String qualificationName)   {this.qualificationName = qualificationName;}

}

