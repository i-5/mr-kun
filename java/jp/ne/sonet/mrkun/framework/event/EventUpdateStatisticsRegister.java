package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.actions.*;
import weblogic.event.common.*;
import weblogic.event.evaluators.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class implements the registration of interest of UpdateStatistics event 
 * (5 different event topic in this event).  It performs the registration of the
 * interest of event topic to the Weblogic event topic tree.  It will pass the 
 * respective evaluator and the action class to the topic tree along with the registration.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventUpdateStatisticsRegister 
{
  T3ServicesDef services = null;
  
  /**
   * This constructor ask for T3ServicesDef (the weblogic services instance).
   *  
   * @param services The T3ServicesDef object that provide the weblogic environment services.       
   */    
  public EventUpdateStatisticsRegister(T3ServicesDef services) 
  {
    this.services = services;
  }

  /**
   * The main method that will register the interest of the event topic  
   * event and pass along with the evaluator and the action class. 
   *
   * @param eventTopic (required) The event topic that specifies the type of statistics data to updated.
   */ 
  public void registerEvent(String eventTopic)  
  {
    try
    {
      // Create a ParamSet to be used by the Evaluate method as each
      // Event is received to decide whether the Action method should
      // be called.
      ParamSet evRegParams = new ParamSet();
      evRegParams.setParam("UPDATE_STATISTICS", true);

      // Create another ParamSet to be used by the Action method,
      // but in this case we don't need any.
      ParamSet acRegParams = new ParamSet();

      // Create an EventTopicDef for the topic specified in eventTopic, 
      // and register an interest in it with the EventEDetailSentEvaluator 
      // class and the EventEDetailSentAction class.
      EventTopicDef topic =
        services.events().getEventTopic(eventTopic);
      Evaluate eval = 
        new Evaluate("jp.ne.sonet.mrkun.framework.event.EventUpdateStatisticsEvaluator",
	                         evRegParams);
      Action action = 
        new Action("jp.ne.sonet.mrkun.framework.event.EventUpdateStatisticsAction",
	                       acRegParams);
    
      // Submit the EventRegistration to the WebLogic Server
      EventRegistrationDef er = topic.register(eval, action);      
      
      int regid = er.getID();      
      System.out.println("Registration ID is " + regid);
    } 
    catch (ParamSetException psExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": ParamSet object passed in is invalid.", psExc);     
    }
    catch (T3Exception t3Exc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": Unable to connect to Weblogic server.", t3Exc);
    }
    catch (EventRegistrationException erExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": Unable to register interest in this event.", erExc);
    }          
  }
}