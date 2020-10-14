package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import javax.servlet.http.*;
import weblogic.event.actions.ActionDef;
import jp.ne.sonet.mrkun.valueobject.*;
import java.util.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import java.lang.*;

/**
 * This class implements the UpdateStatistics event handler (action), when the evaluator 
 * for this event returns true for the action invocation, this will be called by 
 * Weblogic to perform the action for the event, in this case, communicating with
 * the state manager to calculate the statistics for sent eDetail in real time.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventUpdateStatisticsAction implements ActionDef 
{
  T3ServicesDef services = null;

  /**
   * This will save the weblogic services object.
   *  
   * @param services The T3ServicesDef object that provide the weblogic environment services.       
   */ 
  public void setServices(T3ServicesDef services) 
  {
    this.services = services;
  }

  /**
   * This will get all the parameters that were set in the event 
   * registration time.
   *  
   * @param eventParams The ParamSet object that holds the event parameters.       
   */  
  public void registerInit(ParamSet params)
       throws ParamSetException 
  {}  

  /**
   * The main action method that will be called by the Weblogic 
   * when the UpdateStatistics event is triggered and evaluator 
   * of this event return true for the action invocation. 
   *
   * @param event The EventMessageDef object, the actual event object required by the weblogic. 
   */  
  public void action(EventMessageDef event) 
  {
    try 
    { 
      String eventHandlerClass = "";    
      ParamSet eventParamSet = event.getParameters();
      Map parameters = (Map) eventParamSet.getValue("EVENT_PARAMETERS").asObject();
      String eventType = eventParamSet.getValue("EVENT_TYPE").asString();
      
      // Extract the parameters that passed in by event hook    
      String drId = (String) parameters.get("drId");
      String mrId = (String) parameters.get("mrId");
      String messageId = (String) parameters.get("messageId");
      String stateId = (String) parameters.get("stateId");
      
      if ((mrId != null) && (eventType != null) && (drId != null) && (stateId != null))
      {
      	Map eventParams = new Hashtable();
        eventParams.put("mrId", mrId); 
        eventParams.put("drId", drId);
        eventParams.put("stateId", stateId);
        
      	System.out.println("\n\r*** Event Action for: " + eventType + " ***");
        System.out.println("EventUpdateStatisticsAction: Event handling for " + eventType + " is being invoked at " + new Date());
        System.out.println("EventUpdateStatisticsAction: MR ID = " + mrId);        
        System.out.println("EventUpdateStatisticsAction: DR ID = " + drId);
                         
        if (messageId != null)
        {
          eventParams.put("MESSAGE", messageId);	
          System.out.println("EventUpdateStatisticsAction: Message ID = " + messageId);          
        }
        
        if (eventType.equals(EventTopicConstant.STATISTICS_EDTETAIL_SENT))
          eventHandlerClass = "SentEDetailEventHandler";
        else if (eventType.equals(EventTopicConstant.STATISTICS_EDTETAIL_READ))
          eventHandlerClass = "ReadEDetailEventHandler";
        else if (eventType.equals(EventTopicConstant.STATISTICS_CONTACT_SENT))
          eventHandlerClass = "ReceivedContactEventHandler";
        else if (eventType.equals(EventTopicConstant.STATISTICS_DR_ADDED_MR))
          eventHandlerClass = "AddedMrEventHandler";
        
        // UserStateFactory.handleEvent(eventHandlerClass, eventParams, false);            
      
      }
      else
      {
        throw new ApplicationError(this.getClass().getName() + ": Paremeters (mr/eventType) in ParamSet object are null.");     
      }
    } 
    catch (ParamSetException psExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": ParamSet object passed in is invalid.", psExc);     
    }
  }
}
