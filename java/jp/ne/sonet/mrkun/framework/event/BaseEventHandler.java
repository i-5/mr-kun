
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.event;

import java.util.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This is the base class for the handling of the events affecting the
 * UserStateContainer objects. This functions as a plug-in class, only
 * being called by the UserStateFactory for encapsulation / synchronization
 * reasons.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public abstract class BaseEventHandler implements Runnable
{
  protected Map     userRepository;
  protected Map     statsRepository;
  protected Map     eventParams;

  private Boolean handlerSemaphore = new Boolean(true);

  /**
   * Constructor - does nothing
   */
  protected BaseEventHandler() {}

  public void setUserRepository(Map userState)
  {
    synchronized (handlerSemaphore) {this.userRepository = userState;}
  }

  public void setStatisticsRepository(Map stats)
  {
    synchronized (handlerSemaphore) {this.statsRepository = stats;}
  }

  public void setParams(Map params)
  {
    synchronized (handlerSemaphore) {this.eventParams = eventParams;}
  }
  
  /**
   * The trigger method - this is called by UserStateFactory to start processing
   * the event.
   * @param eventParams The parameters required for this event to be handled.
   * @param spawnAsThread If true, the processing is threaded out.
   */
  public void startEvent(boolean spawnAsThread)
  {
    if (spawnAsThread)
    {
      Thread execution = new Thread(this);
      execution.start();
    }
    else
    {
      synchronized (this.handlerSemaphore) {this.handleEvent();}
    }
  }

  public void run()
  {
    synchronized (this.handlerSemaphore)  {this.handleEvent();}
  }

  /**
   * This is the method that all event subclasses must implement. The
   * implementation will pull parameters from the eventParams map, and
   * process the event using the userRepository, messageRepository
   * and statsRepository objects.
   */
  public abstract void handleEvent();
  
  /**
   * This should just return a new instance of the implementation class. There
   * is no expectation of any properties being set.
   */
  public abstract BaseEventHandler newInstance();

 /**
   * This will interface with the RankingList class (for sorting) that handles 
   * the actual ranking update. 
   *
   * @param mrId The mrId that trigger the read eDetail event
   * @param readEDetailCount This holds the value of the message count
   */  
  public void updateRanking(RankingList rankingList, String mrId, String sortFilter,  
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

 