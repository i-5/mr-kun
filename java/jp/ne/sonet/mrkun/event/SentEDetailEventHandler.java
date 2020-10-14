
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.event;

import java.util.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This is the MR statistics event handler class for sent EDetail event.  
 * It encapsulates logic for handling the sent EDetail event.  When event is 
 * triggered, this will perform in-memory data (sent EDetail count) update of 
 * the usage statistics (MR6 page). 
 *
 * @author Damon Lok
 * @version $Id$
 */
public class SentEDetailEventHandler extends BaseEventHandler
{
  private final String SORT_ACTIVE_RANK 			= "sortActiveRanking";
  private final String SORT_READ_EDETAIL_RANK 		= "sortReadEDetailRanking";
  private final String SORT_RECEIVED_CONTACT_RANK 	= "receivedContactRanking"; 
  
  /**
   * Constructor, this will not be used but just in case 
   */
  public SentEDetailEventHandler() 
  {
    super();
  }
  
  /**
   * Return a new instance of this class.
   */
  public BaseEventHandler newInstance()
  {  
    BaseEventHandler sentEDetailEventHandlerInstance = new SentEDetailEventHandler();
    return sentEDetailEventHandlerInstance;
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
    
    // This mr is the one who send the eDetail and triggers this event
    if ((mrId != null) && (drId != null) && 
        (stateId != null) && (messageId != null))
    {            
      //if(messageId == thisMonth)
        // Do statistics update 
        updateStatistics(mrId, drId, stateId);  
      // else
      System.out.println("SentEDetailEventHandler: " + mrId + "'s all related sent eDetail counts have been updated.");
    }
    else
      throw new ApplicationError("SentEDetailEventHandler.handleEvent:  Event parameters passed in are null.");              
  }
  
  /**
   * This method will perform sent eDetail count update on all levels of 
   * statistics data structures (i.e. CompanyLevelStatistics, MRLevelStatistics 
   * and UsageStatistics). 
   *
   * @param mr The mr that trigger the sent eDetail event
   * @param drId The doctor who mr sends the eDetail to
   */  
  private void updateStatistics(String mrId, String drId, String stateId)
  {
    // Get essential IDs for later on extracting the respective data
  	MrStateContainer mrStateContainer = (MrStateContainer) userRepository.get(stateId);
    
    if(mrStateContainer == null)
      throw new ApplicationError("SentEDetailEventHandler.updateStatistics:  MrStateContainer is null."); 
    else             
    {  
      System.out.println("MrId passed from the event hook: " + mrId);     
      MR mr = (MR) mrStateContainer.getUserItem();
      System.out.println("MrId extracted from the mrStateContainer: " + mr.getMrId());      
      
      // Double check on session integrity
      if(!mrId.equals(mr.getMrId()))
        throw new ApplicationError("SentEDetailEventHandler.updateStatistics:  mrId that trigger that event is not equals to the MrStateContainer's mrId."); 
  
  	  // Get company IDs for later on extracting the respective data  
      String companyId = mr.getCompany().getCompanyId();
    
      // Extract the company level statistics     
      CompanyLevelStatistics companyStats = (CompanyLevelStatistics) statsRepository.get(companyId);
        
      if (companyStats != null)
      {
        // Increment the statistics (first level)
        System.out.println("SentEDetailEventHandler: " + mrId + "'s company level sent eDetail count current value = " + companyStats.getSentEDetailCountThisMonth());  
        companyStats.incSentEDetailCountThisMonth(1);
        System.out.println("SentEDetailEventHandler: " + mrId + "'s company level sent eDetail count updated, value = " + companyStats.getSentEDetailCountThisMonth());
        
        // Extract the mr level statistics 
        Map mrStatsMap = companyStats.getMRStatsThisMonth();
        MRLevelStatistics mrStats = (MRLevelStatistics) mrStatsMap.get(mrId);
      
        if (mrStats != null)
        {
      	  // Increment the statistics (second level) 
          System.out.println("SentEDetailEventHandler: " + mrId + "'s mr level sent eDetail count current value = " + mrStats.getSentEDetailCount());     
      	  mrStats.incSentEDetailCount(1);
          System.out.println("SentEDetailEventHandler: " + mrId + "'s company level sent eDetail count updated, value = " + mrStats.getSentEDetailCount());
        
          // Obtain the map that holds the mrDrStatistics for this month
          Map mrStatsThisMonth = mrStats.getMrDrStats();
          
          // Get the specific mr-dr level statistics by drId and add count
          MRDRLevelStatistics mrDrStatistics = (MRDRLevelStatistics) mrStatsThisMonth.get(drId);
        
          System.out.println("SentEDetailEventHandler: " + mrId + "'s mr-dr level percentage current value = " + mrDrStatistics.getPercentage());
        
          System.out.println("SentEDetailEventHandler: " + mrId + "'s mr-dr level sent eDetail count current value = " + mrDrStatistics.getSentEDetailCount());
          mrDrStatistics.incSentEDetailCount(1);
          System.out.println("SentEDetailEventHandler: " + mrId + "'s mr-dr level sent eDetail count updated, value = " + mrDrStatistics.getSentEDetailCount());
                
          // Check the updated percentage value
          System.out.println("SentEDetailEventHandler: " + mrId + "'s mr-dr level percentage updated, value = " + mrDrStatistics.getPercentage());                 
        }
        else
          throw new ApplicationError("SentEDetailEventHandler.handleEvent:  MRLevelStatistics returned by CompanyLevelStatistics.mrStats is null.");  
      }
      else
        throw new ApplicationError("SentEDetailEventHandler.handleEvent:  CompanyLevelStatistics returned by userRepository is null.");          
    }
  }
}

 