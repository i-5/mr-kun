
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * Models a certification option for a DR.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MedicalCertification.java,v 1.1.2.1 2001/09/11 00:33:58 rick Exp $
 */
public class MedicalCertification extends BaseValueObject
{
  private String medCertificationId;
  private String name;
  private String memo;

  /**
   * Constructor
   */
  public MedicalCertification() {}

  public String getMedicalCertificationId() {return this.medCertificationId; }
  public String getName()                   {return this.name;}
  public String getMemo()                   {return this.memo;}

  public void setMedicalCertificationId(String mcId)  {this.medCertificationId = mcId;}
  public void setName(String name)                    {this.name = name;}
  public void setMemo(String memo)                    {this.memo = memo;}
}

 