
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import java.util.*;

import jp.ne.sonet.mrkun.server.*;

/**
 * This class represents a container for the statistics details for a
 * particular company. It has summary information (concerning all MRs
 * within this company) and a map containing the statistics for all MRs
 * in this company.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class CompanyLevelStatistics
{
  private String  companyId;

  /**
   * Summary totals for stats for this company. These are used to calculate
   * the company average fields.
   */
  private int     sentEDetailCountThisMonth;
  private int     sentEDetailCountLastMonth;

  private int     readEDetailCountThisMonth;
  private int     readEDetailCountLastMonth;

  private int     receivedContactCountThisMonth;
  private int     receivedContactCountLastMonth;

  private int     activeDRCountThisMonth;
  private int     activeDRCountLastMonth;

  private int     registeredDRCountThisMonth;
  private int     registeredDRCountLastMonth;

  /**
   * This contains all the MRs that belong to this company. It is a map
   * of MRLevelStatistics objects, keyed on mrId.
   */
  private Map     mrStatsThisMonth;
  private Map     mrStatsLastMonth;

  /**
   * This is the public semaphore for this company's totals. This object
   * should be used by any subclass of StateEventHandler to synchronize
   * when setting or getting any of the private fields within this
   * instance of the CompanyLevelStatistics.
   */
  public  Object  companyLevelSemaphore = new Boolean(true);

  /**
   * Lists for maintaining the rankings within this company of MRs. These
   * are essentially lists of mrIds, which are in turn keys on the mrStats
   * map.
   */
  private RankingList rankingReadEDetailThisMonth;
  private RankingList rankingReadEDetailLastMonth;
  private RankingList rankingReceivedContactThisMonth;
  private RankingList rankingReceivedContactLastMonth;
  private RankingList rankingActiveThisMonth;
  private RankingList rankingActiveLastMonth;

  public CompanyLevelStatistics(String companyId)
  {
    // Initialise all the member variables
    this.companyId                       = companyId;
    this.mrStatsThisMonth                = new Hashtable();
    this.mrStatsLastMonth                = new Hashtable();
    this.rankingReadEDetailThisMonth     = new RankingList();
    this.rankingReadEDetailLastMonth     = new RankingList();
    this.rankingReceivedContactThisMonth = new RankingList();
    this.rankingReceivedContactLastMonth = new RankingList();
    this.rankingActiveThisMonth          = new RankingList();
    this.rankingActiveLastMonth          = new RankingList();
  }

  public String getCompanyId()                     {return this.companyId;}

  // Getters for the sum values for each month
  public Map    getMRStatsThisMonth()              {return this.mrStatsThisMonth;}
  public int    getSentEDetailCountThisMonth()     {return this.sentEDetailCountThisMonth;}
  public int    getReadEDetailCountThisMonth()     {return this.readEDetailCountThisMonth;}
  public int    getReceivedContactCountThisMonth() {return this.receivedContactCountThisMonth;}
  public int    getActiveDRCountThisMonth()        {return this.activeDRCountThisMonth;}
  public int    getRegisteredDRCountThisMonth()    {return this.registeredDRCountThisMonth;}

  public Map    getMRStatsLastMonth()              {return this.mrStatsLastMonth;}
  public int    getSentEDetailCountLastMonth()     {return this.sentEDetailCountLastMonth;}
  public int    getReadEDetailCountLastMonth()     {return this.readEDetailCountLastMonth;}
  public int    getReceivedContactCountLastMonth() {return this.receivedContactCountLastMonth;}
  public int    getActiveDRCountLastMonth()        {return this.activeDRCountLastMonth;}
  public int    getRegisteredDRCountLastMonth()    {return this.registeredDRCountLastMonth;}

  // Setters for the sum values for each month
  public void setSentEDetailCountThisMonth(int count)     {this.sentEDetailCountThisMonth = count;}
  public void setReadEDetailCountThisMonth(int count)     {this.readEDetailCountThisMonth = count;}
  public void setReceivedContactCountThisMonth(int count) {this.receivedContactCountThisMonth = count;}
  public void setActiveDRCountThisMonth(int count)        {this.activeDRCountThisMonth = count;}
  public void setRegisteredDRCountThisMonth(int count)    {this.registeredDRCountThisMonth = count;}

  public void setSentEDetailCountLastMonth(int count)     {this.sentEDetailCountLastMonth = count;}
  public void setReadEDetailCountLastMonth(int count)     {this.readEDetailCountLastMonth = count;}
  public void setReceivedContactCountLastMonth(int count) {this.receivedContactCountLastMonth = count;}
  public void setActiveDRCountLastMonth(int count)        {this.activeDRCountLastMonth = count;}
  public void setRegisteredDRCountLastMonth(int count)    {this.registeredDRCountLastMonth = count;}

  // Getters for the 4 ranking lists for each month
  public RankingList getRankingReadEDetailThisMonth()     {return this.rankingReadEDetailThisMonth;}
  public RankingList getRankingReceivedContactThisMonth() {return this.rankingReceivedContactThisMonth;}
  public RankingList getRankingActiveThisMonth()          {return this.rankingActiveThisMonth;}

  public RankingList getRankingReadEDetailLastMonth()     {return this.rankingReadEDetailLastMonth;}
  public RankingList getRankingReceivedContactLastMonth() {return this.rankingReceivedContactLastMonth;}
  public RankingList getRankingActiveLastMonth()          {return this.rankingActiveLastMonth;}

  // Incrementers for the sum values for each month
  public void incSentEDetailCountThisMonth(int count)     {this.sentEDetailCountThisMonth += count;}
  public void incReadEDetailCountThisMonth(int count)     {this.readEDetailCountThisMonth += count;}
  public void incReceivedContactCountThisMonth(int count) {this.receivedContactCountThisMonth += count;}
  public void incActiveDRCountThisMonth(int count)        {this.activeDRCountThisMonth += count;}
  public void incRegisteredDRCountThisMonth(int count)    {this.registeredDRCountThisMonth += count;}

  public void incSentEDetailCountLastMonth(int count)     {this.sentEDetailCountLastMonth += count;}
  public void incReadEDetailCountLastMonth(int count)     {this.readEDetailCountLastMonth += count;}
  public void incReceivedContactCountLastMonth(int count) {this.receivedContactCountLastMonth += count;}
  public void incActiveDRCountLastMonth(int count)        {this.activeDRCountLastMonth += count;}
  public void incRegisteredDRCountLastMonth(int count)    {this.registeredDRCountLastMonth += count;}

  // Getters for the average values for each month
  public int getAvgSentEDetailCountThisMonth()
  {
    if (this.mrStatsThisMonth.size() == 0)
      return 0;
    else
      return this.sentEDetailCountThisMonth / this.mrStatsThisMonth.size();
  }
  
  public int getAvgReadEDetailCountThisMonth()
  {
    if (this.mrStatsThisMonth.size() == 0)
      return 0;
    else
      return this.readEDetailCountThisMonth / this.mrStatsThisMonth.size();
  }

  public int getAvgReceivedContactCountThisMonth()
  {
    if (this.mrStatsThisMonth.size() == 0)
      return 0;
    else
      return this.receivedContactCountThisMonth / this.mrStatsThisMonth.size();
  }

  public int getAvgActiveDRCountThisMonth()
  {
    if (this.mrStatsThisMonth.size() == 0)
      return 0;
    else
      return this.activeDRCountThisMonth / this.mrStatsThisMonth.size();
  }

  public int getAvgRegisteredDRCountThisMonth()
  {
    if (this.mrStatsThisMonth.size() == 0)
      return 0;
    else
      return this.registeredDRCountThisMonth / this.mrStatsThisMonth.size();
  }

  public int getAvgSentEDetailCountLastMonth()
  {
    if (this.mrStatsLastMonth.size() == 0)
      return 0;
    else
      return this.sentEDetailCountLastMonth / this.mrStatsLastMonth.size();
  }

  public int getAvgReadEDetailCountLastMonth()
  {
    if (this.mrStatsLastMonth.size() == 0)
      return 0;
    else
      return this.readEDetailCountLastMonth / this.mrStatsLastMonth.size();
  }
  public int getAvgReceivedContactCountLastMonth()
  {
    if (this.mrStatsLastMonth.size() == 0)
      return 0;
    else
      return this.receivedContactCountLastMonth / this.mrStatsLastMonth.size();
  }

  public int getAvgActiveDRCountLastMonth()
  {
    if (this.mrStatsLastMonth.size() == 0)
      return 0;
    else
      return this.activeDRCountLastMonth / this.mrStatsLastMonth.size();
  }

  public int getAvgRegisteredDRCountLastMonth()
  {
    if (this.mrStatsLastMonth.size() == 0)
      return 0;
    else
      return this.registeredDRCountLastMonth / this.mrStatsLastMonth.size();
  }

  // Return the calculated percentage of eDetail sent count versus eDetail read by DR count
  public int getPercentageThisMonth()
  {
    if (this.sentEDetailCountThisMonth == 0)
      return 0;
    else
      return ((this.readEDetailCountThisMonth * 100 ) / this.sentEDetailCountThisMonth);
  }

  public int getPercentageLastMonth()
  {
    if (this.sentEDetailCountLastMonth == 0)
      return 0;
    else
      return ((this.readEDetailCountLastMonth * 100 ) / this.sentEDetailCountLastMonth);
  }
}

