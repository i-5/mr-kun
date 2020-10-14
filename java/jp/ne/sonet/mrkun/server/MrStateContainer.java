
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.text.*;

import org.jdom.*;

/**
 * The MR specific parts of the state container object.
 * 
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class MrStateContainer extends UserStateContainer
{
  protected Map     driMessageCounts;

  protected Integer overallRanking;

  // These are the positions within the ranking vectors for this month
  protected Integer edetailsReadTMRanking;
  protected Integer edetailsSentTMRanking;
  protected Integer contactsSentTMRanking;
  protected Integer activeDRsTMRanking;

  // These are the positions within the ranking vectors for last month
  protected Integer edetailsReadLMRanking;
  protected Integer edetailsSentLMRanking;
  protected Integer contactsSentLMRanking;
  protected Integer activeDRsLMRanking;

  protected CompanyLevelStatistics companyStats; // Reference to the statistics repository

  /**
   * Constructor
   */
  public MrStateContainer(String userId, Map statisticsRepository)
  {
    super(userId);
    MR user = (MR) this.getUserItem();
    synchronized (UserStateFactory.allStatsSemaphore)
    {
      this.companyStats = (CompanyLevelStatistics)
                statisticsRepository.get(user.getCompany().getCompanyId());
      this.loadRankings();
    }
  }

  protected boolean isDoctor() {return false;}

  /**
   * Load up the MR from the database
   */
  protected void loadUserItem(String mrId)
  {
    try
    {
      // Load MRManager
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      userItem = mrm.getMRById(mrId);

      if (userItem == null)
        throw new UserNotFoundException("The mr: " + mrId + " was not found.");

      driMessageCounts = new Hashtable();
      Collection drInfos = mrm.getDRInformationList(mrId, driMessageCounts);

      // Put the collection into a map ... this should probably be done by the ejb in future
      opponentList = new Hashtable();
      for (Iterator i = drInfos.iterator(); i.hasNext(); )
      {
        DRInformation dri = (DRInformation) i.next();
        opponentList.put(dri.getDrId(), dri);
      }

      // Default sort and stash
      List defaultSortDrList = sortOpponents(drInfos, DEFAULT_DRI_SORT_ORDER);
      sortedOpponentList = new Hashtable();
      sortedOpponentList.put(DEFAULT_DRI_SORT_ORDER, defaultSortDrList);

      // ReportManager EJB
      ReportManagerRemoteIntf rpm = (ReportManagerRemoteIntf) new EJBUtil().
                                  getManager(HttpConstant.REPORTMANAGER_HOME);

      // Get the ranking object
      this.overallRanking = rpm.getUserRanking(mrId).getRanking();

    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error loading up the MR for user: " + mrId, errRemote);
    }
  }

  /**
   * Retrieve the stats for this DR/MR combination. This is used in the MR3.0
   * page.
   */
  public Map getSingleUserStats(String drId)
  {
    lastAccessed = new Date();
    Map monthPair = new Hashtable();

    MRLevelStatistics mrStatsTM = getMRStats(true);
    MRLevelStatistics mrStatsLM = getMRStats(false);

    // Work out stats dates
    Date dtThisMonth = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(dtThisMonth);
    cal.add(Calendar.MONTH, -1);
    Date dtLastMonth = cal.getTime();
    
    if (mrStatsTM != null)
    {
      // Get the dr's stats
      MRDRLevelStatistics tmMemory = (MRDRLevelStatistics)
                                mrStatsTM.getMrDrStats().get(drId);

      // Lock this month for this MR and DR while we build stats
      UsageStatistics thisMonth = new UsageStatistics();
      thisMonth.setStatisticMonth(dtThisMonth);
      if (tmMemory != null)
      {
        synchronized (tmMemory.mrDrLevelSemaphore)
        {
          thisMonth.setReadEDetail(new Integer(tmMemory.getReadEDetailCount()));
          thisMonth.setSentEDetail(new Integer(tmMemory.getSentEDetailCount()));
          thisMonth.setReceivedContact(new Integer(tmMemory.getReceivedContactCount()));
        }
      }
      else
      {
        thisMonth.setReadEDetail(new Integer(0));
        thisMonth.setSentEDetail(new Integer(0));
        thisMonth.setReceivedContact(new Integer(0));
      }
      monthPair.put("dr_point_cur_month", thisMonth);
    }

    if (mrStatsLM != null)
    {
      MRDRLevelStatistics lmMemory = (MRDRLevelStatistics)
                                mrStatsLM.getMrDrStats().get(drId);

      // Lock last month for this MR and DR while we build stats
      UsageStatistics lastMonth = new UsageStatistics();
      lastMonth.setStatisticMonth(dtLastMonth);
      if (lmMemory != null)
      {
        synchronized (lmMemory.mrDrLevelSemaphore)
        {
          lastMonth.setReadEDetail(new Integer(lmMemory.getReadEDetailCount()));
          lastMonth.setSentEDetail(new Integer(lmMemory.getSentEDetailCount()));
          lastMonth.setReceivedContact(new Integer(lmMemory.getReceivedContactCount()));
        }
      }
      else
      {
        lastMonth.setReadEDetail(new Integer(0));
        lastMonth.setSentEDetail(new Integer(0));
        lastMonth.setReceivedContact(new Integer(0));
      }
      monthPair.put("dr_point_pre_month", lastMonth);
    }
    return monthPair;
  }

  /**
   * Retrieve the statistics for all DRs. This is used in the bottom half of
   * the MR6.0 page. It's purpose is mainly just an adapter between what the
   * page wants and what the state object holds, but this will thin out over
   * time.
   */
  public Map getAllDrStats()
  {
    lastAccessed = new Date();

    // Get the list of DRs, and the stats objects
    List drList = this.getOpponentList("name");
    MRLevelStatistics mrStatsTM = getMRStats(true);
    MRLevelStatistics mrStatsLM = getMRStats(false);

    if (!drList.isEmpty())
    {
      Map perDRStats = new Hashtable();

      // Work out stats dates
      Date dtThisMonth = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dtThisMonth);
      cal.add(Calendar.MONTH, -1);
      Date dtLastMonth = cal.getTime();

      // Build the per dr stats object for the stats page
      Map thisMonthStats = mrStatsTM.getMrDrStats();
      Map lastMonthStats = mrStatsLM.getMrDrStats();

      // Iterate through the stats maps and build a single consolidated map for
      // both months, using the opponentList to build the doctor info.
      // first: this month
      for (Iterator i = drList.iterator(); i.hasNext(); )
      {
        DRInformation drInfo = (DRInformation) i.next();
        Map monthPair = new Hashtable();

        UsageStatistics thisMonth = new UsageStatistics();
        thisMonth.setStatisticMonth(dtThisMonth);
        thisMonth.setDrId(drInfo.getDrId());
        thisMonth.setType(new Integer(UsageStatistics.PER_DR_USAGE));
        thisMonth.setDrName(drInfo.getName());
        thisMonth.setHospitalName(drInfo.getHospital());
        thisMonth.setMrId(((MR) userItem).getMrId());
        thisMonth.setSpecialty(drInfo.getDivision());

        if (mrStatsTM != null)
        {
          MRDRLevelStatistics tmMemory = (MRDRLevelStatistics) mrStatsTM.getMrDrStats().get(drInfo.getDrId());
          if (tmMemory != null)
          {
            synchronized (tmMemory.mrDrLevelSemaphore)
            {
              thisMonth.setReadEDetail(new Integer(tmMemory.getReadEDetailCount()));
              thisMonth.setSentEDetail(new Integer(tmMemory.getSentEDetailCount()));
              thisMonth.setReceivedContact(new Integer(tmMemory.getReceivedContactCount()));
              int activeCount = tmMemory.getReadEDetailCount() + tmMemory.getReceivedContactCount();
              thisMonth.setStatus(activeCount > 0 ? new Integer(activeCount) : new Integer(-1));
            }
          }
          else
          {
            thisMonth.setReadEDetail(new Integer(0));
            thisMonth.setSentEDetail(new Integer(0));
            thisMonth.setReceivedContact(new Integer(0));
            thisMonth.setStatus(new Integer(-1));
          }
          monthPair.put("dr_point_cur_month", thisMonth);
        }
        
        if (mrStatsLM != null)
        {
          MRDRLevelStatistics lmMemory = (MRDRLevelStatistics) mrStatsLM.getMrDrStats().get(drInfo.getDrId());
          if (lmMemory != null)
          {
            UsageStatistics lastMonth = new UsageStatistics();
            lastMonth.setStatisticMonth(dtLastMonth);
            lastMonth.setType(new Integer(UsageStatistics.PER_DR_USAGE));
            lastMonth.setDrId(drInfo.getDrId());
            lastMonth.setDrName(drInfo.getName());
            lastMonth.setHospitalName(drInfo.getHospital());
            lastMonth.setMrId(((MR) userItem).getMrId());
            lastMonth.setSpecialty(drInfo.getDivision());
            synchronized (lmMemory.mrDrLevelSemaphore)
            {
              lastMonth.setReadEDetail(new Integer(lmMemory.getReadEDetailCount()));
              lastMonth.setSentEDetail(new Integer(lmMemory.getSentEDetailCount()));
              lastMonth.setReceivedContact(new Integer(lmMemory.getReceivedContactCount()));
              int activeCount = lmMemory.getReadEDetailCount() + lmMemory.getReceivedContactCount();
              lastMonth.setStatus(activeCount > 0 ? new Integer(activeCount) : new Integer(-1));
            }
            monthPair.put("dr_point_pre_month", lastMonth);
          }
        }
        perDRStats.put(drInfo.getDrId(), monthPair);
      }
      return perDRStats;
    }
    else
      return null;
  }

  /**
   * Retrieves the ranking object used in MR2.0 page.
   */
  public Ranking getRanking()
  {
    lastAccessed = new Date();
    MRLevelStatistics mrStats = getMRStats(true);
    if (mrStats != null)
    {
      Integer totalMRCount = null;
      synchronized (companyStats.companyLevelSemaphore)
        {totalMRCount = new Integer(companyStats.getMRStatsThisMonth().size());}
      return new Ranking(this.overallRanking, totalMRCount);
    }
    else
      throw new ApplicationError("No stats available for this MR for this month");
  }

  public Integer getEdetailsReadTMRanking()
  {
    lastAccessed = new Date();
    return this.edetailsReadTMRanking;
  }

  public Integer getContactsSentTMRanking()
  {
    lastAccessed = new Date();
    return this.contactsSentTMRanking;
  }

  public Integer getActiveDRsTMRanking()
  {
    lastAccessed = new Date();
    return this.activeDRsTMRanking;
  }

  public void setEdetailsReadTMRanking(Integer edetailsReadTMRanking)
  {
    lastAccessed = new Date();
    this.edetailsReadTMRanking = edetailsReadTMRanking;
  }

  public void setContactsSentTMRanking(Integer contactsSentTMRanking)
  {
    lastAccessed = new Date();
    this.contactsSentTMRanking = contactsSentTMRanking;
  }

  public void setActiveDRsTMRanking(Integer activeDRsTMRanking)
  {
    lastAccessed = new Date();
    this.activeDRsTMRanking = activeDRsTMRanking;
  }

  public Map getDRIMessageCount()
  {
    lastAccessed = new Date();
    return this.driMessageCounts;
  }

  /**
   * Retrieves the components of the MR statistics, and packages them
   * into UsageStatistics objects for the stats page.
   */
  public Map getSelfUsageStats()
  {
    lastAccessed = new Date();
    Map mrAvg = new Hashtable();

    // Build a map with a pair of UsageStatistics objects
    MR user = (MR) userItem;
    MRLevelStatistics mrStatsTM = getMRStats(true);
    MRLevelStatistics mrStatsLM = getMRStats(false);

    // Work out stats dates
    Date dtThisMonth = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(dtThisMonth);
    cal.add(Calendar.MONTH, -1);
    Date dtLastMonth = cal.getTime();

    if (mrStatsTM != null)
    {
      synchronized (mrStatsTM.mrLevelSemaphore)
      {
        UsageStatistics thisMonth = new UsageStatistics();
        thisMonth.setStatisticMonth(dtThisMonth);
        thisMonth.setReadEDetail(new Integer(mrStatsTM.getReadEDetailCount()));
        thisMonth.setSentEDetail(new Integer(mrStatsTM.getSentEDetailCount()));
        thisMonth.setReceivedContact(new Integer(mrStatsTM.getReceivedContactCount()));
        thisMonth.setPercentage(new Integer(mrStatsTM.getPercentage()));
        thisMonth.setRegisteredDR(new Integer(mrStatsTM.getRegisteredDRCount()));
        thisMonth.setActiveCount(new Integer(mrStatsTM.getActiveDRCount()));
        mrAvg.put(HttpConstant.CURRENT_MONTH, thisMonth);
      }
    }

    if (mrStatsLM != null)
    {
      synchronized (mrStatsLM.mrLevelSemaphore)
      {
        UsageStatistics lastMonth = new UsageStatistics();
        lastMonth.setStatisticMonth(dtLastMonth);
        lastMonth.setReadEDetail(new Integer(mrStatsLM.getReadEDetailCount()));
        lastMonth.setSentEDetail(new Integer(mrStatsLM.getSentEDetailCount()));
        lastMonth.setReceivedContact(new Integer(mrStatsLM.getReceivedContactCount()));
        lastMonth.setPercentage(new Integer(mrStatsLM.getPercentage()));
        lastMonth.setRegisteredDR(new Integer(mrStatsLM.getRegisteredDRCount()));
        lastMonth.setActiveCount(new Integer(mrStatsLM.getActiveDRCount()));

        mrAvg.put(HttpConstant.PREVIOUS_MONTH, lastMonth);
      }
    }
    return mrAvg;
  }

  /**
   * Retrieves the components of the company statistics, and packages them
   * into UsageStatistics objects for the stats page.
   */
  public Map getCompanyAvgStats()
  {
    lastAccessed = new Date();
    Map compAvg = new Hashtable();

    // Build a map with a pair of UsageStatistics objects
    if (this.companyStats != null)
    {
      // Work out stats dates
      Date dtThisMonth = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dtThisMonth);
      cal.add(Calendar.MONTH, -1);
      Date dtLastMonth = cal.getTime();

      synchronized (this.companyStats.companyLevelSemaphore)
      {
        UsageStatistics thisMonth = new UsageStatistics();
        thisMonth.setStatisticMonth(dtThisMonth);
        thisMonth.setReadEDetail(new Integer(this.companyStats.getAvgReadEDetailCountThisMonth()));
        thisMonth.setSentEDetail(new Integer(this.companyStats.getAvgSentEDetailCountThisMonth()));
        thisMonth.setReceivedContact(new Integer(this.companyStats.getAvgReceivedContactCountThisMonth()));
        thisMonth.setPercentage(new Integer(this.companyStats.getPercentageThisMonth()));
        thisMonth.setRegisteredDR(new Integer(this.companyStats.getAvgRegisteredDRCountThisMonth()));
        thisMonth.setActiveCount(new Integer(this.companyStats.getAvgActiveDRCountThisMonth()));

        UsageStatistics lastMonth = new UsageStatistics();
        lastMonth.setStatisticMonth(dtLastMonth);
        lastMonth.setReadEDetail(new Integer(this.companyStats.getAvgReadEDetailCountLastMonth()));
        lastMonth.setSentEDetail(new Integer(this.companyStats.getAvgSentEDetailCountLastMonth()));
        lastMonth.setReceivedContact(new Integer(this.companyStats.getAvgReceivedContactCountLastMonth()));
        lastMonth.setPercentage(new Integer(this.companyStats.getPercentageLastMonth()));
        lastMonth.setRegisteredDR(new Integer(this.companyStats.getAvgRegisteredDRCountLastMonth()));
        lastMonth.setActiveCount(new Integer(this.companyStats.getAvgActiveDRCountLastMonth()));

        compAvg.put(HttpConstant.CURRENT_MONTH, thisMonth);
        compAvg.put(HttpConstant.PREVIOUS_MONTH, lastMonth);
      }
      return compAvg;
    }
    else
      return null;
  }

  /**
   * Build ranking objects for this mr. This is intended for use in building
   * the top half of the MR6.0 stats page.
   */
  public Map getRankingStats()
  {
    lastAccessed = new Date();
    if (this.companyStats != null)
    {
      Map rankingMap = new Hashtable();

      Integer totalInRankingTM = new Integer(this.companyStats.getMRStatsThisMonth().size());
      Integer totalInRankingLM = new Integer(this.companyStats.getMRStatsLastMonth().size());

      synchronized (this.companyStats.companyLevelSemaphore)
      {
        Ranking edtlReadThisMonth = new Ranking(this.edetailsReadTMRanking, totalInRankingTM);
        Ranking edtlSentThisMonth = new Ranking(this.edetailsReadTMRanking, totalInRankingTM);
        Ranking contThisMonth     = new Ranking(this.contactsSentTMRanking, totalInRankingTM);
        Ranking regoThisMonth     = new Ranking(this.activeDRsTMRanking, totalInRankingTM);

        Ranking edtlReadLastMonth = new Ranking(this.edetailsReadTMRanking, totalInRankingLM);
        Ranking edtlSentLastMonth = new Ranking(this.edetailsReadTMRanking, totalInRankingLM);
        Ranking contLastMonth     = new Ranking(this.contactsSentTMRanking, totalInRankingLM);
        Ranking regoLastMonth     = new Ranking(this.activeDRsLMRanking, totalInRankingLM);

        rankingMap.put(HttpConstant.READ_EDETAIL_CUR_MONTH, edtlReadThisMonth);
        rankingMap.put(HttpConstant.READ_EDETAIL_PRE_MONTH, edtlReadLastMonth);
        rankingMap.put(HttpConstant.CONTACT_CUR_MONTH, contThisMonth);
        rankingMap.put(HttpConstant.CONTACT_PRE_MONTH, contLastMonth);
        rankingMap.put(HttpConstant.ACTIVE_CUR_MONTH, regoThisMonth);
        rankingMap.put(HttpConstant.ACTIVE_PRE_MONTH, regoLastMonth);
      }
      return rankingMap;
    }
    else
      return null;
  }

  /**
   * Retrieve this MR's statistics object
   */
  protected MRLevelStatistics getMRStats(boolean thisMonth)
  {
    MR user = (MR) userItem;
    MRLevelStatistics retVal = null;
    synchronized (this.companyStats.companyLevelSemaphore)
    {
      if (thisMonth)
        retVal = (MRLevelStatistics) this.companyStats.getMRStatsThisMonth().get(user.getMrId());
      else
        retVal = (MRLevelStatistics) this.companyStats.getMRStatsLastMonth().get(user.getMrId());
    }
    return retVal;
  }

  /**
   * Serializes the contents of the object to an XML element.
   *
   * @param The name of the element under which the content will be serialized.
   */
  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);
    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    if (this.getDRIMessageCount() != null)
      masterElement.addContent(mapToXML("messageCountList", this.getDRIMessageCount(), "messageCount"));
    if (this.overallRanking != null)
      masterElement.addContent(new Element("ranking").setText("" + this.overallRanking));
    return masterElement;
  }

  /**
   * Load up the ranking for this user
   */
  public void loadRankings()
  {
    MR user = (MR) userItem;
    String mrId = user.getMrId();
    
    // Read Edetails this month
    this.edetailsReadTMRanking = new Integer(
            this.companyStats.getRankingReadEDetailThisMonth().indexOf(mrId));

    // Read Edetails last month
    this.edetailsReadLMRanking = new Integer(
            this.companyStats.getRankingReadEDetailLastMonth().indexOf(mrId));

    // Read contacts this month
    this.contactsSentTMRanking = new Integer(
            this.companyStats.getRankingReceivedContactThisMonth().indexOf(mrId));

    // Read contacts last month
    this.contactsSentLMRanking = new Integer(
            this.companyStats.getRankingReceivedContactLastMonth().indexOf(mrId));

    // Registered DRs this month
    this.activeDRsTMRanking = new Integer(
            this.companyStats.getRankingActiveThisMonth().indexOf(mrId));

    // Registered DRs last month
    this.activeDRsLMRanking = new Integer(
            this.companyStats.getRankingActiveLastMonth().indexOf(mrId));

  }
}

