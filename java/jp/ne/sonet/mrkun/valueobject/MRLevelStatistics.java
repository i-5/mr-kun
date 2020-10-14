
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import java.util.*;

/**
 * This class contains the statistics for a particular MR for a particular month.
 * This consists basically of a Map with stats pertaining to this MR-DR relationship.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class MRLevelStatistics
{
  private String  mrId;

  /**
   * Summary totals for stats for this MR. These are used to calculate
   * the MR total fields.
   */
  private int     sentEDetailCount;
  private int     readEDetailCount;
  private int     receivedContactCount;
  private int     registeredDRCount;
  private int     activeDRCount;

  /**
   * These two maps are keyed on drId, and contain instances of UsageStatistics.
   */
  private Map     mrDrStats;

  /**
   * This is the public semaphore for this MR's totals. This object
   * should be used by any subclass of StateEventHandler to synchronize
   * when setting or getting any of the private fields within this
   * instance of MRLevelStatistics.
   */
  public  Object  mrLevelSemaphore = new Boolean(true);

  /**
   * Constructor
   */
  public MRLevelStatistics(String mrId)
  {
    this.mrId = mrId;
    this.mrDrStats = new Hashtable();
  }

  // The getter list - this is all just dumb gets
  public String getMrId()                 {return this.mrId;}
  public int    getSentEDetailCount()     {return this.sentEDetailCount;}
  public int    getReadEDetailCount()     {return this.readEDetailCount;}
  public int    getReceivedContactCount() {return this.receivedContactCount;}
  public int    getRegisteredDRCount()    {return this.registeredDRCount;}
  public int    getActiveDRCount()        {return this.activeDRCount;}
  public Map    getMrDrStats()            {return this.mrDrStats;}

  // The setter list - dumb sets. Try to avoid using these.
  public void setSentEDetailCount(int count)     {this.sentEDetailCount = count;}
  public void setReadEDetailCount(int count)     {this.readEDetailCount = count;}
  public void setReceivedContactCount(int count) {this.receivedContactCount = count;}
  public void setRegisteredDRCount(int count)    {this.registeredDRCount = count;}
  public void setActiveDRCount(int count)        {this.activeDRCount = count;}

  // The incrementers - the event handlers should use these, not the setters.
  public void incSentEDetailCount(int count)     {this.sentEDetailCount += count;}
  public void incReadEDetailCount(int count)     {this.readEDetailCount += count;}
  public void incReceivedContactCount(int count) {this.receivedContactCount += count;}
  public void incRegisteredDRCount(int count)    {this.registeredDRCount += count;}
  public void incActiveDRCount(int count)        {this.activeDRCount += count;}

  // Return the calculated percentage of eDetail sent count versus eDetail read by DR count
  public int getPercentage()
  {
    if (this.sentEDetailCount == 0)
      return 0;
    else
      return ((this.readEDetailCount * 100) / this.sentEDetailCount);
  }
}

