package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.actions.*;
import weblogic.event.common.*;
import weblogic.event.evaluators.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class implements the LogUser event register.  It performs the registration 
 * of the interest of LOGIN event to the Weblogic event topic tree.  It will pass 
 * the respective evaluator and the action class to the topic tree along with the
 * registration.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventLogUserRegister 
{
  T3ServicesDef services = null;
  
  // This constructor ask for T3ServicesDef (the weblogic services instance) 
  public EventLogUserRegister(T3ServicesDef services) 
  {
    this.services = services;
  }
  
  // The main method that will register the interest of the Login event
  // and pass along with the evaluator and the action class
  public void registerEvent()  
  {
    try
    {
      // Create a ParamSet to be used by the Evaluate method as each
      // Event is received to decide whether the Action method should
      // be called.
      ParamSet evRegParams = new ParamSet();
      evRegParams.setParam("LOGIN", "Login");
      evRegParams.setParam("AUTHENTICATION", "authenicated");

      // Create another ParamSet to be used by the Action method to
      // specify where to how to log the user.
      ParamSet acRegParams = new ParamSet();

      // Create an EventTopicDef for the topic "LOGIN", and register
      // an interest in it with the EventLogUserEvaluator class and
      // the EventLogUserAction class.
      EventTopicDef topic =
        services.events().getEventTopic("LOGIN");
      Evaluate eval = 
        new Evaluate("jp.ne.sonet.mrkun.framework.event.EventLogUserEvaluator",
	                         evRegParams);
      Action action = 
        new Action("jp.ne.sonet.mrkun.framework.event.EventLogUserAction",
	                       acRegParams);
    
      // Submit the EventRegistration to the WebLogic Server
      EventRegistrationDef er = topic.register(eval, action);
      int regid = er.getID();
      System.out.println("Registration ID is " + regid);
    } 
    catch (ParamSetException psExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": No User object in ParamSet", psExc);     
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