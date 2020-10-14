package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import javax.servlet.http.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class implements the LogUser event, the event that will log user details
 * when a successful authentication triggered in login time.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventLogUser 
{
  HttpServletRequest request = null;
  Object user = null;
  
  public EventLogUser(HttpServletRequest request, Object user) 
  {
    this.request = request;
    this.user = user;
  }
  
  public void submitEvent()  
  {
    try
    {
      // Create the ParamSet that will be used by the EventLogUserEvaluator class to 
      // evaluate whether the Action method should be called to handle this event trigger.
      ParamSet eventParams = new ParamSet();
      eventParams.setParam("LOGIN", "Login");
      eventParams.setParam("AUTHENTICATION", "authenicated");
      eventParams.setParam("HTTP_SERVLET_REQUEST", request);
      eventParams.setParam("USER", user);

      // Get the Weblogic service and then to the topic tree 
      // and finally submit the event for the topic "LOGIN".
      T3ServicesDef services = weblogic.common.T3Services.getT3Services();
      EventTopicDef topic =
        services.events().getEventTopic("LOGIN");
      String status = topic.submit(eventParams);
      System.out.println("EventLogUser has been submitted to the weblogic topic tree. Status: " + status);
    } 
    catch (ParamSetException psExc) 
    {
      throw new ApplicationError(this.getClass().getName() + ": No User object in ParamSet", psExc);     
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