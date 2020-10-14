
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * A container class for the MR3.0 stats.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DRStatistics.java,v 1.1.2.1 2001/08/08 04:27:43 rick Exp $
 */
public class DRStatistics extends BaseValueObject
{
  private DRInformation drInfo;
  private String        drStatus;
  private Report        currMonth;
  private Report        prevMonth;
  
  /**
   * Constructor
   */
  public DRStatistics()  { }

  public DRInformation getDrInfo() {return this.drInfo;}
  public String getDrStatus() {return this.drStatus;}
  public Report getCurrMonth() {return this.currMonth;}
  public Report getPrevMonth() {return this.prevMonth;}

  public void setDrInfo(DRInformation dri) {this.drInfo = dri;}
  public void setDrStatus(String drStatus) {this.drStatus = drStatus;}
  public void setCurrMonth(Report currMonth) {this.currMonth = currMonth;}
  public void setPrevMonth(Report prevMonth) {this.prevMonth = prevMonth;}


}

 