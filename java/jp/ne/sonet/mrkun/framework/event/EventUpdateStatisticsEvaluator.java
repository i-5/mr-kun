package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import weblogic.event.evaluators.EvaluateDef;

/**
 * This class implements the UpdateStatistics event evaluator, when MR sends
 * an eDetail to the DR, the EventEDetailSent will be triggered.  Then this class
 * will be called by Weblogic to evaluate the happened event to determine 
 * whether the action should be invoked or not.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventUpdateStatisticsEvaluator implements EvaluateDef 
{

  boolean regEDetailSent = false;
  private boolean verbose = false;

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
   * This will gets the registration parameters that will be used 
   * for evaluating the events.
   *  
   * @param params The ParamSet object that holds the event parameters.       
   */  
  public void registerInit(ParamSet params)
       throws ParamSetException 
  {
    regEDetailSent = params.getValue("UPDATE_STATISTICS").asBoolean();
  }

  /**
   * This evaluate method that will be called by the Weblogic when 
   * event is triggered to determine the action should be invoked or not. 
   *
   * @param event The EventMessageDef object, the actual event object required by the weblogic. 
   */  
  public boolean evaluate(EventMessageDef event)
       throws ParamSetException
  {
    // Get the event parameters 
    ParamSet eventParams = event.getParameters();
    boolean eventEDetailSent = eventParams.getValue("UPDATE_STATISTICS").asBoolean();
    
    // Determine whether the event value equals 
    // the trigger value set at registration time 
    // (in this case UPDATE_STATISTICS is true). 
    if (eventEDetailSent == regEDetailSent) 
	    return true;
    return false;
  }
}
