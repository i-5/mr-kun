package jp.ne.sonet.mrkun.framework.event;

import weblogic.common.*;
import weblogic.event.common.*;
import javax.servlet.http.*;
import weblogic.event.actions.ActionDef;
import jp.ne.sonet.mrkun.valueobject.*;
import java.util.Date;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class implements the LogUser event handler (action), when the evaluator 
 * for this event returns true for the action invocation, this will be called by 
 * Weblogic to perform the action for the event, in this case, log the user information.
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventLogUserAction implements ActionDef 
{
  T3ServicesDef services = null;

  // Saves the services object
  public void setServices(T3ServicesDef services) 
  {
    this.services = services;
  }

  // Gets the registration parameters
  public void registerInit(ParamSet params)
       throws ParamSetException 
  {}  
  
  // The main action method that will be called by the Weblogic 
  // when the LogUser event is triggered and evaluator of this event return true for 
  // the action invocation
  public void action(EventMessageDef event) 
  {
    try 
    {
      String userId = "";
      DR dr = null;
      MR mr = null;
       
      ParamSet eventParamSet = event.getParameters();
      Object user = eventParamSet.getValue("USER").asObject();
      HttpServletRequest request = (HttpServletRequest) eventParamSet.getValue("HTTP_SERVLET_REQUEST").asObject();
      
      if (user instanceof MR)
      {
      	mr = (MR) user;
        userId = mr.getMrId(); 
      }
      if(user instanceof DR)
      {
        dr = (DR) user;
        userId = dr.getDrId();
      }   
        
      String userBrowser = request.getHeader("User-Agent");
      String host = request.getHeader("Host");	
      System.out.println(this.getClass().getName() + ": UserId=" + userId);
      System.out.println(this.getClass().getName() + ": User login time=" + new Date());
      System.out.println(this.getClass().getName() + ": Browser=" + userBrowser);
      System.out.println(this.getClass().getName() + ": Host=" + host);
    } 
    catch (ParamSetException e) 
    {
      throw new ApplicationError(this.getClass().getName() + ": No User object in ParamSet.");
    }
  }
}
