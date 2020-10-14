
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.event;

import java.util.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This is the MR statistics event handler class for DR has added a MR.  
 * It encapsulates logic for handling the adding of a new MRDRLevelStatistics 
 * object, that represents the new mr-dr registration, to the MrDrStatsThisMonth 
 * map.  When event is triggered, this will perform in-memory data update.
 *
 * @author Damon Lok
 * @version $Id$
 */
public class AddedMREventHandler extends BaseEventHandler
{
  
  /**
   * Constructor, this will not be used but just in case 
   */
  public AddedMREventHandler() 
  {
    super();   
  }
  
  /**
   * Return a new instance of this class.
   */
  public BaseEventHandler newInstance()
  {  
    BaseEventHandler AddedMREventHandlerInstance = new AddedMREventHandler();
    return AddedMREventHandlerInstance;
  }
    
  /**
   * This method will be called by userStateFactory for initiating the event handling
   * process.
   */
  public void handleEvent()
  {
  	String mrId = (String) eventParams.get("mrId");
    String drId = (String) eventParams.get("drId");
    String stateId = (String) eventParams.get("stateId");
    
    // This mr is the one who sends a eDetail and triggers this event
    if ((mrId != null) && (drId != null) && (stateId != null))
    {            
      // Do statistics update 
      updateStatistics(mrId, drId, stateId);  
      
      System.out.println("AddedMREventHandler: " + mrId + "'s all related read eDetail counts have been updated.");
    }
    else
      throw new ApplicationError("AddedMREventHandler.handleEvent:  Event parameters passed in are null.");              
  }
  
  /**
   * This method will perform update on MrDrStatsThisMonth map by adding a new 
   * MRDRLevelStatistics object, that represents the new mr-dr registration, to 
   * the MrDrStatsThisMonth map.
   *
   * @param mr The mr that trigger the read eDetail event
   * @param drId The doctor who mr sends the eDetail to
   */  
  private void updateStatistics(String mrId, String drId, String stateId)
  {
  	// Get essential IDs for later on extracting the respective data
  	MrStateContainer mrStateContainer = (MrStateContainer) userRepository.get(stateId);
    
    if(mrStateContainer == null)
      throw new ApplicationError("AddedMREventHandler.updateStatistics:  MrStateContainer is null."); 
    else             
    {  
      System.out.println("MrId passed from the event hook: " + mrId);     
      MR mr = (MR) mrStateContainer.getUserItem();
      System.out.println("MrId extracted from the mrStateContainer: " + mr.getMrId());      
      
      // Double check on session integrity
      if(!mrId.equals(mr.getMrId()))
        throw new ApplicationError("AddedMREventHandler.updateStatistics:  mrId that trigger that event is not equals to the MrStateContainer's mrId."); 
        	
      String companyId = mr.getCompany().getCompanyId();
    
      // Extract the company level statistics     
      CompanyLevelStatistics companyStats = (CompanyLevelStatistics) statsRepository.get(companyId);
        
      if (companyStats != null)
      {
        // Extract the mr level statistics for this month
        Map mrStatsMap = companyStats.getMRStatsThisMonth();
        MRLevelStatistics mrStats = (MRLevelStatistics) mrStatsMap.get(mrId);
      
        if (mrStats != null)
        {        
 
          // Obtain the map that holds the mrDrStatistics for this month
          Map mrStatsThisMonth = mrStats.getMrDrStats();
          
          // Add the specific mr-dr level statistics by mrId and drId
          MRDRLevelStatistics mrDrStatistics = new MRDRLevelStatistics(mrId, drId);        
          mrDrStatistics.setSentEDetailCount(0);  
    	  mrDrStatistics.setReadEDetailCount(0); 
    	  mrDrStatistics.setReceivedContactCount(0);       
          mrStatsThisMonth.put(drId, mrDrStatistics);    
        
          System.out.println("AddedMREventHandler: New MRDRLevelStatistics for MR: " + mrId + " DR: " + drId + " added to the mrStats map.");         
        }
        else
          throw new ApplicationError("AddedMREventHandler.handleEvent:  MRLevelStatistics returned by CompanyLevelStatistics.mrStats is null.");  
      }
      else
        throw new ApplicationError("AddedMREventHandler.handleEvent:  CompanyLevelStatistics returned by userRepository is null.");          
    }
  }
}

 