package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import java.util.*;

/**
 * This class implements the UpdateStatistic event, the event that will trigger the state 
 * manager to update the respective MR's statistic and ranking calculation 
 * in real time.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventUpdateStatistics
{    
  public EventUpdateStatistics() 
  {}
  
  /**
   * This method acts as the external interface to publish the event. 
   * It sets all the passed in parameters to the ParamSet for the event evaluation
   * and action to use for real-time calculation later on.  
   *
   * @param eventTopic (required) The event topic that specifies the type of statistics data to updated.
   * @param dr (not required) The DR object.       
   * @param mr (required) The MR object that triggers the event.
   * @param msg (not required) The Message object.
   */
  public void publishEvent(String eventTopic, Map parameters)  
  {
    // Create the ParamSet that will be used by the event evaluator class to 
    // evaluate whether the Action method should be called to handle this event trigger.
    try
    {
      ParamSet eventParams = new ParamSet();
      eventParams.setParam("UPDATE_STATISTICS", true);
      eventParams.setParam("EVENT_TYPE", eventTopic);
      eventParams.setParam("EVENT_PARAMETERS", parameters); 
      doPublishEvent(eventTopic, eventParams); 
    }
    catch (ParamSetException psExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": ParamSet object passed in is invalid.", psExc);     
    }           
  }
  
  /**
   * This method acts as the internal interface to publish the event to weblogic. 
   * It talks to the weblogic API for the event submission as well as passing the 
   * ParamSet object associated with the submiting event. 
   *
   * @param eventTopic (required) The event topic that specifies the type of statistics data to updated.
   * @param eventParams (required) The ParamSet object that holds the event parameters.       
   */
  private void doPublishEvent(String eventTopic, ParamSet eventParams)
  {
    try
    {      
      // Get the Weblogic service and then to the topic tree 
      // and finally submit the event for the topic specified in the eventTopic parameter.
      T3ServicesDef services = weblogic.common.T3Services.getT3Services();
      EventTopicDef topic =
        services.events().getEventTopic(eventTopic);
      String status = topic.submit(eventParams);
      System.out.println("Event " + eventTopic + " has been published to the weblogic topic tree. Status: " + status);
    } 
    catch (T3Exception t3Exc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": Unable to connect to Weblogic server.", t3Exc);
    }
    catch (EventGenerationException egExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": Unable to submit the EventLogUser.", egExc);
    }  
  }
}