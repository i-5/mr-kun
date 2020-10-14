package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import weblogic.event.evaluators.EvaluateDef;

/**
 * This class implements the LogUser event evaluator, when a successful 
 * authentication is triggered in login time, this will be called by Weblogic
 * to evaluate the happened event to determine whether the action should be invoked 
 * or not.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventLogUserEvaluator implements EvaluateDef 
{

  String  regLogin = "";
  String  regAuthCondition = "";
  private boolean verbose = false;

  T3ServicesDef services = null;

  // Saves the services object
  public void setServices(T3ServicesDef services) 
  {
    this.services = services;
  }

  // Gets the registration parameters we will use 
  // to evaluate events
  public void registerInit(ParamSet params)
       throws ParamSetException 
  {
    regLogin = params.getValue("LOGIN").asString();
    regAuthCondition = params.getValue("AUTHENTICATION").asString();
  }

  // This evaluate method that will be called by the Weblogic when 
  // event is triggered to determine the action should be invoked or not
  public boolean evaluate(EventMessageDef event)
       throws ParamSetException
  {
    // Get the event parameters 
    ParamSet eventParams = event.getParameters();

    // Compare the value of the event "LOGIN" parameter
    // to the value set for "LOGIN" at registration time
    if ((eventParams.getValue("LOGIN").asString())
                       .equals(regLogin)) 
    {
      String eventAuthCondition = eventParams.getValue("AUTHENTICATION").asString();

      // Then determine whether the event value equals 
      // the trigger value set at registration time
      if (eventAuthCondition.equals(regAuthCondition))
	    return true;
    }
    return false;
  }
}
