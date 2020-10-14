
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

/**
 * This class represents the statistics for an MR-DR relationship.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class MRDRLevelStatistics
{
  private String  mrId;
  private String  drId;
  private int     sentEDetailCount;
  private int     readEDetailCount;
  private int     receivedContactCount;

  public  Object  mrDrLevelSemaphore = new Boolean(true);

  /**
   * Constructor
   */
  public MRDRLevelStatistics(String mrId, String drId)
  {
    this.mrId = mrId;
    this.drId = drId;
  }

  public String getMrId()                 {return this.mrId;}
  public String getDrId()                 {return this.drId;}
  public int    getSentEDetailCount()     {return this.sentEDetailCount;}
  public int    getReadEDetailCount()     {return this.readEDetailCount;}
  public int    getReceivedContactCount() {return this.receivedContactCount;}

  public void setSentEDetailCount(int count)     {this.sentEDetailCount = count;}
  public void setReadEDetailCount(int count)     {this.readEDetailCount = count;}
  public void setReceivedContactCount(int count) {this.receivedContactCount = count;}

  public void incSentEDetailCount(int count)     {this.sentEDetailCount += count;}
  public void incReadEDetailCount(int count)     {this.readEDetailCount += count;}
  public void incReceivedContactCount(int count) {this.receivedContactCount += count;}

  /**********************************************************************************
   *
   * Added the following attributes and methods - Damon 011023
   *
   **********************************************************************************/
  
  // Attributes for holding active count value, can't be derived directly  --- WRONG
  // private int   activeCount;
  
  // Getter and setter for activeCount
  public int	getActiveCount() {return this.readEDetailCount + this.receivedContactCount;}

  // Return the calculated percentage of eDetail sent count versus eDetail read by DR count
  public int getPercentage()
  {
    if (this.sentEDetailCount == 0)
      return 0;
    else
      return ((this.readEDetailCount * 100) / this.sentEDetailCount);
  }
}

