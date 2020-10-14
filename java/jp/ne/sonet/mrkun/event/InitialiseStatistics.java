
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.event;

import java.util.*;
import java.sql.*;
import java.text.*;

import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.dao.DAOFacade;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * Pre loads all the statistics into the state manager. Locking is very
 * important here, because this event will most often be threaded out.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class InitialiseStatistics extends BaseEventHandler
{
  /**
   * Returns a fresh instance of this event handler class. This is useful
   * for templating of the event handlers.
   */
  public BaseEventHandler newInstance()
  {
    return new InitialiseStatistics();
  }

  /**
   * This method contains the code for loading and initialising the statistics
   * repository
   */
  public void handleEvent()
  {
    // Call the message EJB to get the summary of statistics objects
    //ReportManagerRemoteIntf rm = (ReportManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.REPORTMANAGER_HOME);
    //rm.loadStatistics(this.statsRepository);
    synchronized (UserStateFactory.allStatsSemaphore)
    {
      this.statsRepository.clear();
      loadStatistics(this.statsRepository);
    }
  }

  private String STATS_QUERY =
    "SELECT  mr.company_cd company_id, " +
    "        mr.mr_id, " +
    "        mess.dr_id, " +
    "        dr.registered_drs, " +
    "        sum(mess.sent_contacts) sent_contacts, " +
    "        sum(mess.sent_edetails) sent_edetails, " +
    "        sum(mess.read_edetails) read_edetails " +
    "FROM " +
    "     ( " +
    "       SELECT  from_userid mr_id, " +
    "               to_userid dr_id, " +
    "               0 sent_contacts, " +
    "               count(1) sent_edetails, " +
    "               sum(decode(h.receive_timed, null, 0, 1)) read_edetails " +
    "       FROM    message_header h " +
    "       WHERE   h.message_kbn = 1 " +
    "       AND     h.from_userid <> 'ADM0000000' " +
    "       AND     send_torikeshi_time IS NULL " +
    "       AND     h.receive_time >= TO_DATE(?, 'dd/MM/yyyy') " +
    "       AND     h.receive_time <  TO_DATE(?, 'dd/MM/yyyy') " +
    "       GROUP BY h.from_userid, h.to_userid " +
    "      UNION ALL " +
    "       SELECT  to_userid mr_id, " +
    "               from_userid dr_id, " +
    "               count(1) sent_contacts, " +
    "               0 sent_edetails, " +
    "               0 read_edetails " +
    "       FROM    message_header h " +
    "       WHERE   h.message_kbn = 2 " +
    "       AND     h.to_userid <> 'ADM0000000' " +
    "       AND     h.receive_time >= TO_DATE(?, 'dd/MM/yyyy') " +
    "       AND     h.receive_time <  TO_DATE(?, 'dd/MM/yyyy') " +
    "       GROUP BY h.to_userid, h.from_userid " +
    "     ) mess, " +
    "     ( " +
    "       SELECT  mr_id, " +
    "               count(1) registered_drs " +
    "       FROM    sentaku_toroku " +
    "       WHERE   (end_ymd IS NULL OR end_ymd >= TO_DATE(?, 'dd/MM/yyyy')) " +
    "       AND     start_ymd < TO_DATE(?, 'dd/MM/yyyy') " +
    "       GROUP BY mr_id " +
    "     ) dr, " +
    "     mr " +
    "WHERE  	mr.mr_id = dr.mr_id (+) " +
    "AND	    mr.mr_id = mess.mr_id (+) " +
    "AND    	(mr.toroku_ymd IS NULL OR mr.toroku_ymd < TO_DATE(?, 'dd/MM/yyyy')) " +
    "AND    	(mr.delete_ymd IS NULL OR mr.delete_ymd >= TO_DATE(?, 'dd/MM/yyyy')) " +
    "GROUP BY mr.company_cd, mr.mr_id, mess.dr_id, dr.registered_drs";
/*
    "SELECT	mr.company_cd company_id, " +
    "	mr.mr_id, " +
    "	mess.dr_id, " +
    "	sum(mess.sent_contacts) sent_contacts, " +
    "	sum(mess.sent_edetails) sent_edetails, " +
    "	sum(mess.read_edetails) read_edetails " +
    "FROM	( " +
    "	SELECT 	from_userid mr_id, " +
    "		to_userid dr_id, " +
    "		0 sent_contacts, " +
    "		count(1) sent_edetails, " +
    "		sum(decode(h.receive_timed, null, 0, 1)) read_edetails " +
    "	FROM	message_header h " +
    "	WHERE	h.message_kbn = 1 " +
    "	AND	h.from_userid <> 'ADM0000000' " +
    "	AND	h.send_torikeshi_time IS NULL " +
    "	AND 	h.receive_time >= TO_DATE(?, 'dd/MM/yyyy') " +
    "	AND 	h.receive_time <  TO_DATE(?, 'dd/MM/yyyy') " +
    "	GROUP BY h.from_userid, h.to_userid " +
    "	UNION ALL " +
    "	SELECT 	to_userid mr_id, " +
    "		from_userid dr_id, " +
    "		count(1) sent_contacts, " +
    "		0 sent_edetails, " +
    "		0 read_edetails " +
    "	FROM	message_header h " +
    "	WHERE	h.message_kbn = 2 " +
    "	AND	h.to_userid <> 'ADM0000000' " +
    "	AND 	h.receive_time >= TO_DATE(?, 'dd/MM/yyyy') " +
    "	AND 	h.receive_time <  TO_DATE(?, 'dd/MM/yyyy') " +
    "	GROUP BY h.to_userid, h.from_userid " +
    "	) mess, " +
    "	mr " +
    "WHERE	mr.mr_id = mess.mr_id (+) " +
    "AND    (mr.delete_ymd IS NULL OR mr.delete_ymd >= TO_DATE(?, 'dd/MM/yyyy')) " +
    "GROUP BY mr.company_cd, mr.mr_id, mess.dr_id ";
*/

  public void loadStatistics(Map statisticsRepository)
  {
    Connection conn = null;
    try
    {
      conn = DAOFacade.getConnection();
      PreparedStatement qry = conn.prepareStatement(STATS_QUERY);

      System.out.println(STATS_QUERY);

      // Get month strings
      DateFormat sdf = new SimpleDateFormat("01/MM/yyyy");
      String thisMonth = sdf.format(new java.util.Date());
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      String lastMonth = sdf.format(cal.getTime());
      cal.add(Calendar.MONTH, +2);
      String nextMonth = sdf.format(cal.getTime());

      // Execute for last month
      System.out.println("Loading last month stats");
      qry.setString(1, lastMonth);
      qry.setString(2, thisMonth);
      qry.setString(3, lastMonth);
      qry.setString(4, thisMonth);
      qry.setString(5, thisMonth);
      qry.setString(6, thisMonth);
      qry.setString(7, thisMonth);
      qry.setString(8, thisMonth);

      ResultSet rst1 = qry.executeQuery();
      populateStatsMonth(rst1, statisticsRepository, false);
      rst1.close();

      // Execute for this month
      System.out.println("Loading this month stats");
      qry.setString(1, thisMonth);
      qry.setString(2, nextMonth);
      qry.setString(3, thisMonth);
      qry.setString(4, nextMonth);
      qry.setString(5, nextMonth);
      qry.setString(6, nextMonth);
      qry.setString(7, nextMonth);
      qry.setString(8, nextMonth);

      ResultSet rst2 = qry.executeQuery();
      populateStatsMonth(rst2, statisticsRepository, true);
      rst2.close();
      qry.close();

      // For each company, create the sort orders
      for (Iterator i = statisticsRepository.keySet().iterator(); i.hasNext(); )
      {
        String companyId = (String) i.next();
        CompanyLevelStatistics compStats =
            (CompanyLevelStatistics) statisticsRepository.get(companyId);

        createSortOrderLists(compStats);
      }

      // We should also iterate through all users logged in and update their rankings here
      for (Iterator i = this.userRepository.values().iterator(); i.hasNext(); )
      {
        Object state = i.next();
        if (state instanceof MrStateContainer)
          ((MrStateContainer) state).loadRankings();
      }
    }
    catch (SQLException errSQL)
    {
      throw new ApplicationError("Error loading stats", errSQL);
    }
    finally
    {
      try
      {
        if ((conn != null) && !conn.isClosed())
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
  }

  private void populateStatsMonth(ResultSet attributes, Map repository, boolean thisMonth)
    throws SQLException
  {
    int nRowCount = 0;
    while (attributes.next())
    {
      // Get the company object
      String companyId = attributes.getString("company_id");
      CompanyLevelStatistics comp = (CompanyLevelStatistics) repository.get(companyId);

      if (comp == null)
      {
        comp = new CompanyLevelStatistics(companyId);
        repository.put(companyId, comp);
      }

      // Get the MR object
      String mrId = attributes.getString("mr_id");
      MRLevelStatistics mrStats = null;
      if (thisMonth)
        mrStats = (MRLevelStatistics) comp.getMRStatsThisMonth().get(mrId);
      else
        mrStats = (MRLevelStatistics) comp.getMRStatsLastMonth().get(mrId);

      // Build a new MRLevelStatistics object, since we need one
      if (mrStats == null)
      {
        mrStats = new MRLevelStatistics(mrId);
        int registeredDRCount = attributes.getInt("registered_drs");
        mrStats.setRegisteredDRCount(registeredDRCount);
        if (thisMonth)
        {
          comp.incRegisteredDRCountThisMonth(registeredDRCount);
          comp.getMRStatsThisMonth().put(mrId, mrStats);
        }
        else
        {
          comp.incRegisteredDRCountLastMonth(registeredDRCount);
          comp.getMRStatsLastMonth().put(mrId, mrStats);
        }
      }

      // Get the MR-DR level object for the appropriate month
      String drId = attributes.getString("dr_id");
      //System.out.println("One row processed Company:" + companyId + " MrId:" + mrId + " DrId:" + drId);
      if (drId != null)
      {
        MRDRLevelStatistics mrDrStats = (MRDRLevelStatistics) mrStats.getMrDrStats().get(drId);

        if (mrDrStats == null)
        {
          mrDrStats = new MRDRLevelStatistics(mrId, drId);
          mrStats.getMrDrStats().put(drId, mrDrStats);
        }

        // Set the counts
        int readED = attributes.getInt("read_edetails");
        int sentED = attributes.getInt("sent_edetails");
        int sentCo = attributes.getInt("sent_contacts");

        mrDrStats.setReadEDetailCount(readED);
        mrDrStats.setSentEDetailCount(sentED);
        mrDrStats.setReceivedContactCount(sentCo);

        mrStats.incReadEDetailCount(readED);
        mrStats.incSentEDetailCount(sentED);
        mrStats.incReceivedContactCount(sentCo);
        if (mrDrStats.getActiveCount() > 0)
          mrStats.incActiveDRCount(1);

        if (thisMonth)
        {
          // Update company level
          comp.incReadEDetailCountThisMonth(readED);
          comp.incSentEDetailCountThisMonth(sentED);
          comp.incReceivedContactCountThisMonth(sentCo);
        }
        else
        {
          // Update company level
          comp.incReadEDetailCountLastMonth(readED);
          comp.incSentEDetailCountLastMonth(sentED);
          comp.incReceivedContactCountLastMonth(sentCo);
        }

      }
      nRowCount++;
    }
    System.out.println("Rows processed: " + nRowCount);
  }

  /**
   * Builds the sort orders for the various rankings
   */
  private void createSortOrderLists(CompanyLevelStatistics compStats)
  {
    System.out.println("Creating ranking lists for company: " + compStats.getCompanyId());
    Object sortArrayThisMonth[] = compStats.getMRStatsThisMonth().values().toArray();
    Object sortArrayLastMonth[] = compStats.getMRStatsLastMonth().values().toArray();

    // Sort by active drs this month
    //System.out.println("Sorting by active this month");
    Comparator statsComp = new StatisticsRankingComparator("active", false);
    Arrays.sort(sortArrayThisMonth, statsComp);
    List activeThisMonth = Arrays.asList(sortArrayThisMonth);
    insertIntoRanking(activeThisMonth, compStats.getRankingActiveThisMonth(), statsComp);

    // Sort by active drs last month
    //System.out.println("Sorting by active last month");
    statsComp = new StatisticsRankingComparator("active", false);
    Arrays.sort(sortArrayLastMonth, statsComp);
    List activeLastMonth = Arrays.asList(sortArrayLastMonth);
    insertIntoRanking(activeLastMonth, compStats.getRankingActiveLastMonth(), statsComp);

    // Sort by read edetails this month
    //System.out.println("Sorting by read EDetails this month");
    statsComp = new StatisticsRankingComparator("read_edetails", false);
    Arrays.sort(sortArrayThisMonth, statsComp);
    List readEDThisMonth = Arrays.asList(sortArrayThisMonth);
    insertIntoRanking(readEDThisMonth, compStats.getRankingReadEDetailThisMonth(), statsComp);

    // Sort by read edetails last month
    //System.out.println("Sorting by read EDetails last month");
    statsComp = new StatisticsRankingComparator("read_edetails", false);
    Arrays.sort(sortArrayLastMonth, statsComp);
    List readEDLastMonth = Arrays.asList(sortArrayLastMonth);
    insertIntoRanking(readEDLastMonth, compStats.getRankingReadEDetailLastMonth(), statsComp);

    // Sort by received contacts this month
    //System.out.println("Sorting by received Contacts this month");
    statsComp = new StatisticsRankingComparator("received_contacts", false);
    Arrays.sort(sortArrayThisMonth, statsComp);
    List recdCOThisMonth = Arrays.asList(sortArrayThisMonth);
    insertIntoRanking(recdCOThisMonth, compStats.getRankingReceivedContactThisMonth(), statsComp);

    // Sort by received contacts last month
    //System.out.println("Sorting by received Contacts last month");
    statsComp = new StatisticsRankingComparator("received_contacts", false);
    Arrays.sort(sortArrayLastMonth, statsComp);
    List recdCOLastMonth = Arrays.asList(sortArrayLastMonth);
    insertIntoRanking(recdCOLastMonth, compStats.getRankingReceivedContactLastMonth(), statsComp);

  }

  /**
   * Takes a ranking list and inserts the MrIDs for that sorted list into
   * the destination ranking list, determining along the way if any objects
   * need to be equally ranked.
   */
  private void insertIntoRanking(List sortedList, RankingList destList, Comparator comp)
  {
    int previousRank = 0;
    for (int loopCounter = 0; loopCounter < sortedList.size(); loopCounter++)
    {
      MRLevelStatistics mrls = (MRLevelStatistics) sortedList.get(loopCounter);
      String mrId = mrls.getMrId();
      if (loopCounter == 0)
        previousRank = destList.insert(1, mrId);
      // Compare this with the one before it
      else if (comp.compare(sortedList.get(loopCounter - 1),
                            sortedList.get(loopCounter)) == 0)
        previousRank = destList.insert(previousRank, mrId);
      else
        previousRank = destList.insert(loopCounter + 1, mrId);
    }
  }



}

