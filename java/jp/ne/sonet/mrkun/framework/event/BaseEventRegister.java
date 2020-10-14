package jp.ne.sonet.mrkun.framework.eventTrace;

import weblogic.common.*;
import weblogic.event.actions.*;
import weblogic.event.common.*;
import weblogic.event.evaluators.*;

public class BaseEventRegister {

  public void Register{

    // Connect to the WebLogic Server using the URL supplied as the first
    // command-line argument
    T3Client t3 = null;
    try {
      t3 = new T3Client(argv[0]);
      t3.connect();

      // Create a ParamSet to be used by the Evaluate method as each
      // Event is received to decide whether the Action method should
      // be called. We take the second and third command-line
      // arguments as values.
      ParamSet evRegParams = new ParamSet();
      evRegParams.setParam("LOGIN", "login");
      evRegParams.setParam("AUTHENICATION", true);

      // Create another ParamSet to be used by the Action method to
      // specify where to send the mail. We take the last two
      // command-line arguments as values.
      ParamSet acRegParams = new ParamSet();
      acRegParams.setParam("SMTPhost",  argv[3]);
      acRegParams.setParam("Addressee", argv[4]);

      // Create an EventTopicDef for the topic "STOCKS", and register
      // an interest in it with the EvaluateStocks evaluate class and
      // the ActionEmail action class.
      EventTopicDef topic =
        t3.services.events().getEventTopic("STOCKS");
      Evaluate eval = 
          new Evaluate("tutorial.event.stocks.EvaluateStocks",
	                           evRegParams);
      Action action = 
          new Action("tutorial.event.stocks.MailStockInfo",
	                         acRegParams);

      // Submit the EventRegistration to the WebLogic Server
      EventRegistrationDef er = topic.register(eval, action);
      
      // Submit the Event to the Weblogic Server
      String status = topic.submit(eventParameters);
      int regid = er.getID();
      System.out.println("Registration ID is " + regid);
    }
    finally {
      try {t3.disconnect();} catch (Exception e) {;}
    }
  }
}
