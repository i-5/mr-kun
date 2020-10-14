
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.event;

import java.util.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This is the MR statistics event handler class for dr read EDetail event.  
 * It encapsulates logic for handling the read EDetail event read by the dr.  
 * When event is triggered, this will perform in-memory data (read EDetail count) 
 * update of the usage statistics (MR6 page). 
 *
 * @author Damon Lok
 * @version $Id$
 */
public class ReadEDetailEventHandler extends BaseEventHandler
{
  private final String SORT_ACTIVE_RANK 			= "sortActiveRanking";
  private final String SORT_READ_EDETAIL_RANK 		= "sortReadEDetailRanking";
  private final String SORT_RECEIVED_CONTACT_RANK 	= "receivedContactRanking"; 
  
  /**
   * Constructor, this will not be used but just in case 
   */
  public ReadEDetailEventHandler() 
  {
    super();
  }
  
  /**
   * Return a new instance of this class.
   */
  public BaseEventHandler newInstance()
  {  
    BaseEventHandler ReadEDetailEventHandlerInstance = new ReadEDetailEventHandler();
    return ReadEDetailEventHandlerInstance;
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
    
    // This dr is the one who read the eDetail and triggers this event
    if ((mrId != null) && (drId != null) && 
        (stateId != null) && (messageId != null))
    {            
      //if(messageId == thisMonth)
        // Do statistics update 
        updateStatistics(mrId, drId, stateId);  
      // else
        
      
      System.out.println("ReadEDetailEventHandler: " + mrId + "'s all related read eDetail counts have been updated.");
    }
    else
      throw new ApplicationError("ReadEDetailEventHandler.handleEvent:  Event parameters passed in are null.");              
  }
  
  /**
   * This method will perform read eDetail count update on all levels of 
   * statistics data structures (i.e. CompanyLevelStatistics, MRLevelStatistics 
   * and UsageStatistics). 
   *
   * @param mrId The mr that trigger the read eDetail event
   * @param drId The doctor who mr sends the eDetail to
   * @param stateId The session that the user is current in
   */  									   
  private void updateStatistics(String mrId, String drId, String stateId)
  {
  	// Get essential IDs for later on extracting the respective data
  	MrStateContainer mrStateContainer = (MrStateContainer) userRepository.get(stateId);
    
    if(mrStateContainer == null)
      throw new ApplicationError("ReadEDetailEventHandler.updateStatistics:  MrStateContainer is null."); 
    else             
    {  
      System.out.println("MrId passed from the event hook: " + mrId);     
      MR mr = (MR) mrStateContainer.getUserItem();
      System.out.println("MrId extracted from the mrStateContainer: " + mr.getMrId());      
      
      // Double check on session integrity
      if(!mrId.equals(mr.getMrId()))
        throw new ApplicationError("ReadEDetailEventHandler.updateStatistics:  mrId that trigger that event is not equals to the MrStateContainer's mrId."); 
      
      String companyId = mr.getCompany().getCompanyId();
    
      // Extract the company level statistics     
      CompanyLevelStatistics companyStats = (CompanyLevelStatistics) statsRepository.get(companyId);
        
      if (companyStats != null)
      {
        // Increment the statistics (first level)
        System.out.println("ReadEDetailEventHandler: " + mrId + "'s company level read eDetail count current value = " + companyStats.getReadEDetailCountThisMonth());  
        companyStats.incReadEDetailCountThisMonth(1);
        System.out.println("ReadEDetailEventHandler: " + mrId + "'s company level read eDetail count updated, value = " + companyStats.getReadEDetailCountThisMonth());
        
        // Extract the mr level statistics for this month
        Map mrStatsMap = companyStats.getMRStatsThisMonth();
        MRLevelStatistics mrStats = (MRLevelStatistics) mrStatsMap.get(mrId);
      
        if (mrStats != null)
        {
          System.out.println("ReadEDetailEventHandler: " + mrId + "'s mr level read eDetail count current value = " + mrStats.getReadEDetailCount()); 
        
          // Increment the statistics (second level) 
          mrStats.incReadEDetailCount(1);
        
          System.out.println("ReadEDetailEventHandler: " + mrId + "'s company level read eDetail count updated, value = " + mrStats.getReadEDetailCount());
        
          // Obtain the map that holds the mrDrStatistics for this month
          Map mrDrStatsMap = mrStats.getMrDrStats();
          
          // Get the specific mr-dr level statistics by drId and add count
          MRDRLevelStatistics mrDrStatistics = (MRDRLevelStatistics) mrDrStatsMap.get(drId);
        
          System.out.println("ReadEDetailEventHandler: " + mrId + "'s mr-dr level percentage current value = " + mrDrStatistics.getPercentage());
          System.out.println("ReadEDetailEventHandler: " + mrId + "'s mr-dr level read eDetail count current value = " + mrDrStatistics.getReadEDetailCount());
        
          // Check if the mr is currently inactive
          if(mrDrStatistics.getActiveCount() > 0)
            mrStats.incActiveDRCount(1);
        
          mrDrStatistics.incReadEDetailCount(1);
        

          System.out.println("ReadEDetailEventHandler: " + mrId + "'s mr-dr level read eDetail count updated, value = " + mrDrStatistics.getReadEDetailCount());
                
          // Check the updated percentage value
          System.out.println("ReadEDetailEventHandler: " + mrId + "'s mr-dr level percentage updated, value = " + mrDrStatistics.getPercentage());                 
      
          // Update the read eDetail count ranking        
          updateRanking(companyStats.getRankingReadEDetailThisMonth(), mrId, SORT_READ_EDETAIL_RANK, mrStats.getReadEDetailCount(), mrStatsMap);              
        
          // Update the received active doctor count ranking        
          updateRanking(companyStats.getRankingActiveThisMonth(), mrId, SORT_ACTIVE_RANK, mrStats.getActiveDRCount(), mrStatsMap);              
        }
        else
          throw new ApplicationError("ReadEDetailEventHandler.updateStatistics:  MRLevelStatistics returned by CompanyLevelStatistics.mrStats is null.");  
      }
      else
        throw new ApplicationError("ReadEDetailEventHandler.updateStatistics:  CompanyLevelStatistics returned by userRepository is null.");          
    }
  }
}

 