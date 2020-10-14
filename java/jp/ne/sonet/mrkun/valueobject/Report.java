
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

import java.util.*;

/**
 * This class models E-detail report statistics (I think).
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Report.java,v 1.1.2.2 2001/08/08 04:24:38 rick Exp $
 */
public class Report extends BaseValueObject
{

  private Date    reportDate;
  private Integer sentEDetail;
  private Integer readEDetail;
  private Integer totalReceivedMessages;

  /**
   * Constructor
   */
  public Report()  {}

  public Date getReportDate()               {return this.reportDate;}
  public Integer getSentEDetail()           {return this.sentEDetail;}
  public Integer getReadEDetail()           {return this.readEDetail;}
  public Integer getTotalReceivedMessages() {return this.totalReceivedMessages;}

  public void setReportDate(Date reportDate)      {this.reportDate = reportDate;}
  public void setSentEDetail(Integer s)           {this.sentEDetail = s;}
  public void setReadEDetail(Integer r)           {this.readEDetail = r;}
  public void setTotalReceivedMessages(Integer t) {this.totalReceivedMessages = t;}


}

 