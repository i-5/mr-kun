
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.event;

import java.util.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This is the MR statistics event handler class for dr received contact event.  
 * It encapsulates logic for handling the received contact event read by the dr.  
 * When event is triggered, this will perform in-memory data (received contact count) 
 * update of the usage statistics (MR6 page). 
 *
 * @author Damon Lok
 * @version $Id$
 */
public class ReceivedContactEventHandler extends BaseEventHandler
{
  private final String SORT_ACTIVE_RANK 			= "sortActiveRanking";
  private final String SORT_READ_EDETAIL_RANK 		= "sortReadEDetailRanking";
  private final String SORT_RECEIVED_CONTACT_RANK 	= "receivedContactRanking"; 
  
  /**
   * Constructor, this will not be used but just in case 
   */
  public ReceivedContactEventHandler() 
  {
    super();
  }
  
  /**
   * Return a new instance of this class.
   */
  public BaseEventHandler newInstance()
  {  
    BaseEventHandler ReceivedContactEventHandlerInstance = new ReceivedContactEventHandler();
    return ReceivedContactEventHandlerInstance;
  }
    
  /**
   * This method will be called by userStateFactory for initiating the event handling
   * process.
   */
  public void handleEvent()
  {
  	String mrId = (String) eventParams.get("mrId");
    String drId = (String) eventParams.get("drId");
    String messageId = (String) eventParams.get("messageId");
    String stateId = (String) eventParams.get("stateId");
    
    // This mr is the one who sends a eDetail and triggers this event
    if ((mrId != null) && (drId != null) && 
        (stateId != null) && (messageId != null))
    {            
      //if(messageId == thisMonth)
        // Do statistics update 
      updateStatistics(mrId, drId, stateId);   
      
      System.out.println("ReceivedContactEventHandler: " + mrId + "'s all related received contact counts have been updated.");
    }
    else
      throw new ApplicationError("ReceivedContactEventHandler.handleEvent:  Event parameters passed in are null.");              
  }
  
  /**
   * This method will perform received contact count update on all levels of 
   * statistics data structures (i.e. CompanyLevelStatistics, MRLevelStatistics 
   * and UsageStatistics). 
   *
   * @param mr The mr that trigger the received contact event
   * @param drId The doctor who mr sends the eDetail to
   */  
  private void updateStatistics(String mrId, String drId, String stateId)
  {
    // Get essential IDs for later on extracting the respective data
  	MrStateContainer mrStateContainer = (MrStateContainer) userRepository.get(stateId);
    
    if(mrStateContainer == null)
      throw new ApplicationError("ReceivedContactEventHandler.updateStatistics:  MrStateContainer is null."); 
    else             
    {  
      System.out.println("MrId passed from the event hook: " + mrId);     
      MR mr = (MR) mrStateContainer.getUserItem();
      System.out.println("MrId extracted from the mrStateContainer: " + mr.getMrId());      
      
      // Double check on session integrity
      if(!mrId.equals(mr.getMrId()))
        throw new ApplicationError("ReceivedContactEventHandler.updateStatistics:  mrId that trigger that event is not equals to the MrStateContainer's mrId."); 
      
      String companyId = mr.getCompany().getCompanyId();
    
      // Extract the company level statistics     
      CompanyLevelStatistics companyStats = (CompanyLevelStatistics) statsRepository.get(companyId);
        
      if (companyStats != null)
      {
        // Increment the statistics (first level)
        System.out.println("ReceivedContactEventHandler: " + mrId + "'s company level received contact count current value = " + companyStats.getReceivedContactCountThisMonth());  
        companyStats.incReceivedContactCountThisMonth(1);
        System.out.println("ReceivedContactEventHandler: " + mrId + "'s company level received contact count updated, value = " + companyStats.getReceivedContactCountThisMonth());
        
        // Extract the mr level statistics 
        Map mrStatsMap = companyStats.getMRStatsThisMonth();
        MRLevelStatistics mrStats = (MRLevelStatistics) mrStatsMap.get(mrId);
      
        if (mrStats != null)
        {
      	  // Increment the statistics (second level) 
          System.out.println("ReceivedContactEventHandler: " + mrId + "'s mr level received contact count current value = " + mrStats.getReceivedContactCount());     
      	  mrStats.incReceivedContactCount(1);
          System.out.println("ReceivedContactEventHandler: " + mrId + "'s company level received contact count updated, value = " + mrStats.getReceivedContactCount());
        
          // Obtain the map that holds the mrDrStatistics for this month
          Map mrDrStatsMap = mrStats.getMrDrStats();
          
          // Get the specific mr-dr level statistics by drId and add count
          MRDRLevelStatistics mrDrStatistics = (MRDRLevelStatistics) mrDrStatsMap.get(drId);
        
          System.out.println("ReceivedContactEventHandler: " + mrId + "'s mr-dr level percentage current value = " + mrDrStatistics.getPercentage());

          System.out.println("ReceivedContactEventHandler: " + mrId + "'s mr-dr level received contact count current value = " + mrDrStatistics.getReceivedContactCount());
        
          // Check if the mr is currently inactive
          if(mrDrStatistics.getActiveCount() > 0)
            mrStats.incActiveDRCount(1);
        
          mrDrStatistics.incReceivedContactCount(1);
          System.out.println("ReceivedContactEventHandler: " + mrId + "'s mr-dr level received contact count updated, value = " + mrDrStatistics.getReceivedContactCount());
        
          // Check the updated percentage value
          System.out.println("ReceivedContactEventHandler: " + mrId + "'s mr-dr level percentage updated, value = " + mrDrStatistics.getPercentage());   
        
          // Update the received contact count ranking        
          updateRanking(companyStats.getRankingReceivedContactThisMonth(), mrId, SORT_RECEIVED_CONTACT_RANK, mrStats.getReceivedContactCount(), mrStatsMap);              
        
          // Update the received active doctor count ranking        
          updateRanking(companyStats.getRankingActiveThisMonth(), mrId, SORT_ACTIVE_RANK, mrStats.getActiveDRCount(), mrStatsMap);              
          
        }
        else
          throw new ApplicationError("ReceivedContactEventHandler.handleEvent:  MRLevelStatistics returned by CompanyLevelStatistics.mrStats is null.");  
      }
      else
        throw new ApplicationError("ReceivedContactEventHandler.handleEvent:  CompanyLevelStatistics returned by userRepository is null.");          
    }
  }
  
  /**
   * This will interface with the RankingList class (for sorting) that handles 
   * the actual ranking update. 
   *
   * @param mrId The mrId that trigger the read eDetail event
   * @param readEDetailCount This holds the value of the message count
   */  
  private void updateRanking(RankingList rankingList, String mrId, String sortFilter,  
  	 					     int newCount, Map mrStatsMap)
  {  
    int previousRankMessageCount = 0; 
    
    // get the current rank from the MrStateContainer
    MrStateContainer mrStateContainer = (MrStateContainer) userRepository.get(mrId);
    Ranking ranking = mrStateContainer.getRanking();
    int mrRank = ranking.getRanking().intValue();
    int previousRank = rankingList.getRankAbove(mrRank);    
    
    // Obtain the preiovs rank mrId list that contains the equal rank
    // and extract the first mr from it 
    Collection mrIdList = (Collection) rankingList.get(previousRank);
    
    if(mrIdList == null)
      throw new ApplicationError("(" + sortFilter + ") updateRanking:  mrIdList is null.");
    else
    { 
      // Obtain the first mr from the collection that holds the equal rank   
      Iterator itr = mrIdList.iterator(); 
      String firstMrId = (String) itr.next();
  
      // Extract the first mr's read eDetail count value 
      MRLevelStatistics previousMrStats = (MRLevelStatistics) mrStatsMap.get(firstMrId); 
      
      if(previousMrStats == null)
        throw new ApplicationError("(" + sortFilter + ") updateRanking:  previousMrStats is null.");
      else
      {    
        // Get the previous rank according to the type of sorting
        if(sortFilter.equals("SORT_READ_EDETAIL_RANK"))
          previousRankMessageCount = previousMrStats.getReadEDetailCount();
        else if(sortFilter.equals("SORT_ACTIVE_RANK"))
          previousRankMessageCount = previousMrStats.getActiveDRCount();
        else if(sortFilter.equals("SORT_RECEIVED_CONTACT_RANK"))  
          previousRankMessageCount = previousMrStats.getReceivedContactCount();  
       
        // Check previous rank to see if it's either at or after 1st rank 
        if (( previousRank > 0) || (previousRank == 0))
        { 
          // previous and current mr's count is equal       
          if (previousRankMessageCount == newCount)
          {
      	    // Update the ranking position
            rankingList.remove(mrRank, mrId);
            rankingList.insert(previousRank, mrId, true);
          }
          else 
          { 
            if( previousRank > 0) 
            {
              rankingList.remove(mrRank, mrId);
              rankingList.insert(previousRank, mrId, false); 
            }
          }
        }
      }    
    }
  }
}

 