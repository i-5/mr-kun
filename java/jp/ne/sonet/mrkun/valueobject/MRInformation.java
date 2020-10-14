
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * This clss models ancillary information about a Medical Representative.
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: MRInformation.java,v 1.1.1.1.2.2 2001/08/23 03:51:10 behrens Exp $
 */
public class MRInformation extends BaseValueObject
{
  private String mrId;
  private String mrPreference;
  private String memo;


  public MRInformation() {}
  
  public String getMemo()         {return this.memo;}
  public String getMrId()         {return this.mrId;}
  public String getMrPreference() {return this.mrPreference;}

  public void setMemo(String memo)
  {
    this.memo = memo;
  }

  public void setMrId(String mrId)
  {
    this.mrId = mrId;
  }

  public void setMrPreference(String mrPreference)
  {
    this.mrPreference = mrPreference;
  }
  
  
}

 