
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.dao.*;
import java.util.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.server.*;

/**
 * The implementation class for the DRManager bean.
 * This bean is stateless in nature, and uses DAO support classes to
 * write persistent information to the database.
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DRManager.java,v 1.1.1.1.2.27 2001/11/13 07:56:55 rick Exp $
 */
public class DRManager extends BaseSessionEJB
{
  static final String MR_REGISTRATION_POINTS  = "MRINPUT_POINT";
  static final String FRONT_LEFT              = "1";
  static final String FRONT_RIGHT             = "2";
  
  /**
   * Load an existing DR object from persistent storage, using
   * the supplied drId as the key for the search.
   */
  public DR getDRById(String drId, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    return (DR) drDAO.searchRecord(drId, "drId", sessionId);
  }

  /**
   * Load an existing DR object from persistent storage, using
   * the supplied drId as the key for the search. Also load up the MrProfileMap
   */
  public DR loginDR(String drId, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    DR loggedInDR = (DR) drDAO.searchRecord(drId, "drId", sessionId);

    // Load the DR's MRProfile objects
    if (loggedInDR != null)
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      loggedInDR.setMrProfileMap(mrm.getMRProfileByDRId(drId, sessionId));
//      Map params = new Hashtable();
//      params.put("drId",drId);
//      params.put("action","login");
//      drDAO.createRecord(params);
      System.out.println("DRManager.loginDR: stored login info for dr=:"+drId); 
      return loggedInDR;
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error loading the MRProfile objects", errRemote);
    }
    else
      return null;
  }

  /**
   * The history of login information is recorded.
   */
  public void setLogDR(String drId, String agent, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    Map params = new Hashtable();
    params.put("drId",drId);
    params.put("agent",agent);
    params.put("action","login");
    drDAO.createRecord(params, sessionId);
//    System.out.println("DRManager.setLogDR: stored login info for dr=:"+drId); 
  }

  /**
   * Load an existing DR object from persistent storage, using
   * the supplied name as the key for the search.
   */
  public DR getDRByName(String name, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    return (DR) drDAO.searchRecord(name, "name", sessionId);
  }

  /**
   * Adds a new DR object to persistent storage
   */
  public void addDR(DR addedDR, String sessionId)
  {
    // Get a DAO and perform the remove query
    DAOFacade drDAO = new DAOFacade(DR.class);
    drDAO.createRecord(addedDR, sessionId);
  }

  /**
   * Removes a DR object from persistent storage.
   */
  public void deleteDR(DR deletedDR, String sessionId)
  {
    // Get a DAO and perform the remove query
    DAOFacade drDAO = new DAOFacade(DR.class);
    drDAO.deleteRecord(deletedDR, sessionId);
  }

  /**
   * Save the already persisted DR object into persistent storage
   */
  public void updateDR(DR userDR, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade drDAO = new DAOFacade(DR.class);
    drDAO.updateRecord(userDR, sessionId);
  }

  /**
   * Returns contextual help associated with this object.
   */
  public String getHelp()
  {
    return "DRManager class help message - hi there";
  }


  public MRInformation getMRInformation(String drId, String mrId, String sessionId)
  {
    return null;
  }

  /**
   * hb010823: Get MRProfile for a specific MR
   */
  public MrProfile getMrProfile(String mrId, String sessionId)
  {
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf)
                      new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
  
      MR mr = mrm.getMRById(mrId, sessionId);
      return(new MrProfile(mrId,mr));
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Remote error using the MRManager bean", errRemote);
    }
  }

  /**
   * Get MRInformation list with DR's message count for newly received EDetail 
   * and MR's unread sent Contact.
   */
  public Collection getMRListWithMessageCount(DR dr, Map drMessageHashdata, String sessionId)
  {
    return null;
  }
  
 /**
  * Retrieve an MrProfile object matching the DrId and MrId specified
  */
  public void updateDRMemo(String mrId, String drId, String memo, String sessionId)
  {
    // System.out.println("Testing hot deployment!!!  Hot deploy works if this shows up.");
    
    // Store the update parameter to the Map container
    Map memoParam = new Hashtable();
    memoParam.put("mrId", mrId);
    memoParam.put("drId", drId);
    memoParam.put("memo", memo);

    // Get a DAO and perform the save query
    DAOFacade drDAO = new DAOFacade(DR.class);
    if(!drDAO.updateRecord(memoParam, sessionId))
      throw new ApplicationError("Fail to update dr memo.");
  }

  /**
   * Add a new MrProfile to persistent storage, and also update
   * the points if this MR has not been added before.
   */
  public void addDRMemo(String mrId, DR userDR, String memo, String sessionId)
  {
    // Store the update parameter to the Map container
    Map memoParam = new Hashtable();
    memoParam.put("mrId", mrId);
    memoParam.put("drId", userDR.getDrId());
    memoParam.put("memo", memo);

    // Get the ids of the two banner mrs
    String leftId = "";
    String rightId = "";
    for (Iterator i = userDR.getMRProfileMap().values().iterator(); i.hasNext(); )
    {
      MrProfile mrpCurrent = (MrProfile) i.next();
      if (mrpCurrent.getMrBannerPosition().equals(FRONT_LEFT))
        leftId = mrpCurrent.getMrId();
      else if (mrpCurrent.getMrBannerPosition().equals(FRONT_RIGHT))
        rightId = mrpCurrent.getMrId();
    }

    DAOFacade drDAO = new DAOFacade(DR.class);

    // Check if this mr has already been registered to this dr
    if (drDAO.searchRecord(memoParam, "searchSentakuTorokuHistory", sessionId) == null)
    {
      addPoints(userDR, HttpConstant.MR_REGISTRATION_POINTS, sessionId);
    }

    // Get a DAO and perform the save query
    drDAO.createRecord(memoParam, sessionId);

    // Reload the DR's MRProfileMap
    try
    {
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      userDR.setMrProfileMap(mrm.getMRProfileByDRId(userDR.getDrId(), sessionId));
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error reloading the MrProfileMap", errRemote);
    }

    // Update the MR Banner position
    DAOFacade daoConstant = new DAOFacade(DatabaseConstant.class);
    DatabaseConstant emzoId = (DatabaseConstant) daoConstant.searchRecord("ENQUETE_MR_ID", "constantId", sessionId);
    String EMZO_ID = emzoId.getNaiyo1();

    if (leftId.equals("") && rightId.equals(""))
      updateMRBannerPosition(userDR.getMRProfileMap().values(), userDR.getDrId(), mrId, "", sessionId);
    else if (leftId.equals("") || rightId.equals(""))
      updateMRBannerPosition(userDR.getMRProfileMap().values(), userDR.getDrId(), mrId, leftId.equals("")?rightId:leftId, sessionId);
    else if (leftId.equals(EMZO_ID))
      updateMRBannerPosition(userDR.getMRProfileMap().values(), userDR.getDrId(), mrId, rightId, sessionId);
    else if (rightId.equals(EMZO_ID))
      updateMRBannerPosition(userDR.getMRProfileMap().values(), userDR.getDrId(), leftId, mrId, sessionId);

    // Send an email to the MR to notify of the registration
    notifyMRRegistration(userDR, mrId, sessionId);

  }

  /**
   * Returns list of medical qualification with this object.
   */
  public Collection getMedicalQualificationList(String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    return (Collection) drDAO.searchMultiple(null, "drQualification", sessionId);
  }

  /**
   * Returns list of Specialty with this object.
   */
  public Collection getSpecialtyList(String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(DR.class);
    return (Collection) drDAO.searchMultiple(null, "drSpecialty", sessionId);
  }

  /**
   * Returns Enquete information with this object.
   */
  public EnqueteInfo getEnqueteInfo(String drId, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(EnqueteInfo.class);
    return (EnqueteInfo) drDAO.searchRecord( drId, "drId", sessionId);
  }

  /**
   * Returns Enquete information with this object.
   */
  public EnqueteInfo getEnqueteInfoByEnqId(EnqueteInfo enqueteInfo, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(EnqueteInfo.class);
    return (EnqueteInfo) drDAO.searchRecord( enqueteInfo, "enqueteInfo", sessionId);
  }

  /**
   * Returns list of Enquete Question with this object.
   */
  public Collection getEnqueteQuestion(EnqueteInfo enqueteinfo, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(EnqueteInfo.class);
    return (Collection) drDAO.searchMultiple( enqueteinfo, "Question", sessionId);
  }

  /**
   * Add Enquete Answer with this object.
   */
  public void addEnqueteAnswer(EnqueteInfo enqueteInfo, String sessionId)
  {
    // Get a DAO and perform the load query
    DAOFacade drDAO = new DAOFacade(EnqueteInfo.class);
    drDAO.createMultiple( enqueteInfo, sessionId );
  }

 /**
  * Update the mr's banner position
  */  
  public void updateMRBannerPosition(Collection colMRList, String drId, String firstPosition, String secondPosition, String sessionId)
  {
    // Pass the sql where clause parameter to the DAO
    Map sqlParam = new Hashtable();
    sqlParam.put("drId", drId);
  
    // Make sure parameters are passed in not null
    if((colMRList == null) || (drId == null) ||
       (firstPosition == null) || (secondPosition == null)) 
      throw new ApplicationError(this.getClass().getName() + ": Parameters are passed from DR 7 servlet are null");   
    else
    {
      // For each MrProfile check for the current position and update to the new one
      for (Iterator itr = colMRList.iterator(); itr.hasNext(); )
      {
        MrProfile mrProfile = (MrProfile) itr.next();
        if (mrProfile.getMrId().equals(firstPosition))
          mrProfile.setMrBannerPosition("1");  
        else if (mrProfile.getMrId().equals(secondPosition))
	      mrProfile.setMrBannerPosition("2");
        else
          mrProfile.setMrBannerPosition("3");                          
      }
      
      // Invoke the DAO for database update
      sqlParam.put("mrList", colMRList);     
      DAOFacade drDAO = new DAOFacade(DR.class);
      if(!drDAO.updateMultiple(sqlParam, sessionId))
        throw new ApplicationError("Fail to update mr's banner position."); 
    }
  }

  public void addPoints(DR userDR, String triggerAction, String sessionId)
  {
    // Add the points to the user
    DAOFacade dbDAO = new DAOFacade(DatabaseConstant.class);
    DatabaseConstant points = (DatabaseConstant) dbDAO.searchRecord(triggerAction, "constantId", sessionId);
//    int currentPoints = userDR.getCurrentUsagePoint().intValue();
    DAOFacade drDAO = new DAOFacade(DR.class);
    Integer pointsFound = (Integer)drDAO.searchRecord(userDR.getDrId(), "point", sessionId);
    int currentPoints = 0;
    if (pointsFound != null) currentPoints = pointsFound.intValue();
    currentPoints += new Integer(points.getNaiyo1()).intValue();
    userDR.setCurrentUsagePoint(new Integer(currentPoints));
    updateDR(userDR, sessionId);
    //throw new ApplicationError("Because I can");
  }

  /**
   * Builds the email that goes to the MR to notify him/her that the DR has registered them
   */
  public void notifyMRRegistration(DR userDR, String mrId, String sessionId)
  {
    // Load up the MR object
    MR newMR = ((MrProfile) userDR.getMRProfileMap().get(mrId)).getMR();

    // Build the subject and body
    String subject = SystemMessages.get("dr_newMRRegistrationSubject", "japanese");
    String bodyMessage = SystemMessages.get("dr_newMRRegistrationBody", "japanese");
    String senderName  = SystemMessages.get("dr_newMRRegistrationSender", "japanese");

    // Figure out the sender
    String senderAddress = null;
    if (newMR.getCompany().getWmEmailAddress() != null)
      senderAddress = newMR.getCompany().getWmEmailAddress();
    else
      senderAddress = "m3center@so-net.ne.jp";

    // Build the message body
    // NOTE: This is probably really wasteful, but I'm in a hurry (RK)
    String newSubject = StringUtils.globalReplace(
                              StringUtils.globalReplace(subject,
                                                        "[#DOCTOR_NAME]",
                                                        userDR.getKanjiName()),
                              "[#DOCTOR_HOSPITAL_NAME]",
                              userDR.getHospital());
    String newBody = StringUtils.globalReplace(
                        StringUtils.globalReplace(
                              StringUtils.globalReplace(bodyMessage,
                                                        "[#DOCTOR_NAME]",
                                                        userDR.getKanjiName()),
                              "[#DOCTOR_HOSPITAL_NAME]",
                              userDR.getHospital()),
                        "[#DOCTOR_SYSTEM_CODE]",
                        userDR.getSystemDrCd());

    // Build recipient method
    Collection recipientList = new ArrayList();
    for (Iterator i = newMR.getForwardEmailList().iterator(); i.hasNext(); )
    {
      String recipientEmail = (String) i.next();
      EmailContact ec = new EmailContact();
      ec.setName(newMR.getKanjiName());
      ec.setEmailAddress(recipientEmail);
      recipientList.add(ec);
    }

    // Call the sendEmailMessage method
    MessageHelperManager mhm = new MessageHelperManager();
    mhm.sendEmailMessage(senderAddress, senderName, recipientList, newSubject, newBody, sessionId);

    // Send a new Contact to the mr
    Contact mrrContact = new Contact();
    mrrContact.setSender(userDR.getDrId());
    mrrContact.setRecipient(mrId);
    mrrContact.setTitle(newSubject);
    mrrContact.setBody(StringUtils.cleanForJS(newBody, false, true)); // Use JS cleaner here to put <BR>s in.
    Collection conRcpt = new ArrayList();
    conRcpt.add(mrId);
    System.out.println("Sending new contact : title - " + newSubject);
    System.out.println("Body:");
    System.out.println("------------------------");
    System.out.println(StringUtils.cleanForJS(newBody, false, true));
    System.out.println("------------------------");
    mhm.addMessage(userDR, conRcpt, mrrContact, newBody, 3, sessionId);
  }

  /**
   * Builds the email that goes to the MR to notify him/her that the DR has read a new edetail
   */
  public void notifyNewEdetail(DR userDR, String mrId, String title, String sessionId)
  {
    // Load up the MR object
    MR newMR = ((MrProfile) userDR.getMRProfileMap().get(mrId)).getMR();

    // Build the subject and body
    String subject = SystemMessages.get("dr_OpenedNewEDetailSubject", "japanese");
    String bodyMessage = SystemMessages.get("dr_OpenedNewEDetailBody", "japanese");
    String senderName  = SystemMessages.get("dr_OpenedNewEDetailSender", "japanese");

    // Figure out the sender
    String senderAddress = null;
    if (newMR.getCompany().getWmEmailAddress() != null)
      senderAddress = newMR.getCompany().getWmEmailAddress();
    else
      senderAddress = "m3center@so-net.ne.jp";

    // Build the message body
    // NOTE: This is probably really wasteful, but I'm in a hurry (RK)
    String newSubject = StringUtils.globalReplace(subject, "[#DOCTOR_NAME]", userDR.getKanjiName());
    String newBody = StringUtils.globalReplace(
                        StringUtils.globalReplace(
                              StringUtils.globalReplace(bodyMessage,
                                                        "[#DOCTOR_NAME]",
                                                        userDR.getKanjiName()),
                              "[#DOCTOR_HOSPITAL_NAME]",
                              userDR.getHospital()),
                        "[#MESSAGE_SUBJECT]",
                        title);


    // Build recipient method
    Collection recipientList = new ArrayList();
    for (Iterator i = newMR.getForwardEmailList().iterator(); i.hasNext(); )
    {
      String recipientEmail = (String) i.next();
      EmailContact ec = new EmailContact();
      System.out.println("DRManager.notifyNewEdetail: adding "+recipientEmail+" to recipientList");
      ec.setName(newMR.getKanjiName());
      ec.setEmailAddress(recipientEmail);
      recipientList.add(ec);
    }

    // Call the sendEmailMessage method
    MessageHelperManager mhm = new MessageHelperManager();
    mhm.sendEmailMessage(senderAddress, senderName, recipientList, newSubject, newBody, sessionId);

    // Send a new Contact to the mr
    Contact nedContact = new Contact();
    nedContact.setTitle(newSubject);
    nedContact.setBody(StringUtils.cleanForJS(newBody, false, true));
    Collection conRcpt = new ArrayList();
    conRcpt.add(mrId);
    System.out.println("Sending new contact : title - " + newSubject);
    System.out.println("Body:");
    System.out.println("------------------------");
    System.out.println(StringUtils.cleanForJS(newBody, false, true));
    System.out.println("------------------------");
    mhm.addMessage(userDR, conRcpt, nedContact, newBody, 3, sessionId);
  }
  
  /**
   * Send the email to the mr notifying that he/she has just been deleted by the DR.
   */
  public void sendDeletedMREmail(Collection colEmailMRList, DR dr, String sessionId)
  {
  	System.out.println("Starting sendDeletedMREmail........");
    MessageHelperManager messageHelper = null;
    StringBuffer emailBody = new StringBuffer();
    StringBuffer subject = new StringBuffer();
    String senderEmailAddress = "";
    Collection recipientIdList = new ArrayList();
    
  	if(colEmailMRList == null)
    	throw new ApplicationError("MR list is passed in null, cannot send email.");
    else
    {
      messageHelper = new MessageHelperManager();
      emailBody.append("\n")
               .append(dr.getKanjiName())
               .append("\n")
               .append(dr.getHospital())
               .append("\n")
               .append(SystemMessages.get("dr_deleteMRBodyLine"))
               .append("\n")
               .append(SystemMessages.get("dr_deleteMRClientLine"))
               .append(dr.getKanjiName())
               .append(" ")
               .append(dr.getHospital());
               
      subject.append(SystemMessages.get("dr_deleteMRSubjectLine"))
             .append(dr.getKanjiName())
             .append(" ")
             .append(dr.getHospital());
      
      // Instantiate a contact to send the deletion notification later
      Contact contact = new Contact();
      contact.setTitle(subject.toString());
      contact.setBody(StringUtils.cleanForJS(emailBody.toString()));
      
      // For each deleted mr, send them the email
      for (Iterator itr = colEmailMRList.iterator(); itr.hasNext(); )
	  {
      	// Extracted each deleted mr from the list
        MR mr= (MR) itr.next();
        Collection colForwardEmailList = mr.getForwardEmailList();
        Collection recipientList = new ArrayList(); 
        
        // Add mr Id for sending the notification contact 
        recipientIdList.add(mr.getMrId());
        
        for (Iterator emailItr = colForwardEmailList.iterator(); emailItr.hasNext(); )
	    {
      	  // Create the EmailContact type object for sending the email
          EmailContact emailContact = new EmailContact();
          String forwardEmail = (String) emailItr.next();
          emailContact.setEmailAddress(forwardEmail);
          emailContact.setName(mr.getKanjiName());                 
          recipientList.add(emailContact);
	    }          
        
        // If mr's company web master email is null, then use the m3 default one
        if(mr.getCompany().getWmEmailAddress() == null)
          senderEmailAddress = SystemMessages.get("system_m3DefaultEmailAddess");
        else
          senderEmailAddress = mr.getCompany().getWmEmailAddress();
        
        // Invoke message helper manager to send the actual email
        messageHelper.sendEmailMessage(senderEmailAddress, SystemMessages.get("dr_deleteMRMRKUN"), 
                                       recipientList, subject.toString(), emailBody.toString(), sessionId);
      }
      
      // Send the mr deletion notification contact
      messageHelper.addMessage(dr, recipientIdList, contact, emailBody.toString(), 3, sessionId);    
    }
  }
 
 /**
  * This will return the emSend the email to the mr notifying that he/she has just been deleted by the DR.
  */ 
  public String getEmzoId(String sessionId)
  {
    DAOFacade daoConstant = new DAOFacade(DatabaseConstant.class);
    DatabaseConstant emzoId = (DatabaseConstant) daoConstant.searchRecord("ENQUETE_MR_ID", "constantId", sessionId);
    return emzoId.getNaiyo1();
  }

  /**
   * Handles the case where a doctor reads a new edetail.
   */
  public void markNewMessageAsRead(DR userDR, Message msg, String sessionId)
  {
    MessageHelperManager messmgr = new MessageHelperManager();
    messmgr.setMessageAsRead(msg, sessionId);
    addPoints(userDR, HttpConstant.EDETAIL_READ_POINTS, sessionId);
    notifyNewEdetail(userDR, msg.getSender(), msg.getTitle(), sessionId);
  }  
}


