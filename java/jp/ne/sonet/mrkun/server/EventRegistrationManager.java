package jp.ne.sonet.mrkun.server;

import java.util.*;
import weblogic.common.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * This startup class implements the main execution of all event interest 
 * registration.  It performs as a centralizied module that invokes all 
 * registration.  It will be loaded in memory during the Weblogic startup 
 * time. 
 *
 * @author Damon Lok
 * @version $Id:$
 */
public class EventRegistrationManager implements T3StartupDef
{

  String wlsServer = "";
  T3ServicesDef services = null;

  // Default constructor
  public EventRegistrationManager()
  {}

  // Constructor that take the Weblogic server instance as argument
  public EventRegistrationManager(String wlsServer)
  {
    this.wlsServer = wlsServer;
  }

  /**
  * This method is required by the T3StartupDef interface.
  * We do not need any T3Services, so it is left empty.
  * @see weblogic.common.T3StartupDef
  */
  public void setServices(T3ServicesDef services)
  {
    this.services = services;
  }

  /**
  * This method is required by the T3StartupDef interface.
  * It is called by WebLogic Server when the class is deployed as a startup class.
  * This method will execute all the event interest registrations to the Weblogic event topic tree.
  */
  public String startup(String name, Hashtable ht) throws java.lang.Exception
  {
  	System.out.println("\n\r" + name + ": Starting up...");
    
    // Register the interest of LogUser event 
    //EventLogUserRegister eventLogUser = new EventLogUserRegister(services);
    //eventLogUser.registerEvent();
    //System.out.println(name + ": Has registered the interest of EventLogUser to the topic tree.");
    
    // Register the interest of UpdateStatistics event (5 event topics involved)
    EventUpdateStatisticsRegister eventUpdateStatistics = new EventUpdateStatisticsRegister(services);
    eventUpdateStatistics.registerEvent(EventTopicConstant.STATISTICS_EDTETAIL_SENT);
    System.out.println(name + ": Has registered the interest of " + EventTopicConstant.STATISTICS_EDTETAIL_SENT + " to the topic tree.");
    eventUpdateStatistics.registerEvent(EventTopicConstant.STATISTICS_EDTETAIL_READ);
    System.out.println(name + ": Has registered the interest of " + EventTopicConstant.STATISTICS_EDTETAIL_READ + " to the topic tree.");
    eventUpdateStatistics.registerEvent(EventTopicConstant.STATISTICS_CONTACT_SENT);
    System.out.println(name + ": Has registered the interest of " + EventTopicConstant.STATISTICS_CONTACT_SENT + " to the topic tree.");
//    eventUpdateStatistics.registerEvent(EventTopicConstant.STATISTICS_CONTACT_READ);
//    System.out.println(name + ": Has registered the interest of " + EventTopicConstant.STATISTICS_CONTACT_READ + " to the topic tree.");
    eventUpdateStatistics.registerEvent(EventTopicConstant.STATISTICS_DR_ADDED_MR);
    System.out.println(name + ": Has registered the interest of " + EventTopicConstant.STATISTICS_DR_ADDED_MR + " to the topic tree.");
        
    return "Event topics interest registration are all complete.\n\r";      
  }
}