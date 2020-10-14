
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import java.rmi.RemoteException;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import java.lang.*;
import java.util.*;

/**
 * The remote interface for the session bean supporting the DR10 page
 * (ie the DR profile show/edit/cancel DR profile)
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DRManagerRemoteIntf.java,v 1.1.1.1.2.15 2001/11/13 07:56:55 rick Exp $
 */
public interface DRManagerRemoteIntf extends BaseSessionEJBRemoteIntf
{
  /**
   * Load an existing DR object from persistent storage, using
   * the supplied drId as the key for the search.
   */
  public DR getDRById(String drId, String sessionId) throws RemoteException;

  /**
   * Load an existing DR object from persistent storage, using
   * the supplied name as the key for the search.
   */
  public DR getDRByName(String name, String sessionId) throws RemoteException;

  /**
   * Load an existing DR object from persistent storage, using
   * the supplied drId as the key for the search. Also load up the MrProfileMap
   */
  public DR loginDR(String drId, String sessionId) throws RemoteException;

  /**
   * The history of login information is recorded.
   */
  public void setLogDR(String drId, String agent, String sessionId)throws RemoteException;

  /**
   * Adds a new DR object to persistent storage
   */
  public void addDR(DR addedDR, String sessionId) throws RemoteException;

  /**
   * Removes a DR object from persistent storage.
   */
  public void deleteDR(DR deletedDR, String sessionId) throws RemoteException;

  /**
   * Save the already persisted DR object into persistent storage
   */
  public void updateDR(DR userDR, String sessionId) throws RemoteException;

  /**
   * Returns contextual help associated with this object.
   */
  public String getHelp() throws RemoteException;

  /**
   * Returns a specific MRInformation object, as specified by the drId and mrId.
   */
  public MRInformation getMRInformation(String drId, String mrId, String sessionId) throws RemoteException;

  /**
   * hb010823: 
   * Get the MRProfile that is associated with the MR identified by mrId
   */
  public MrProfile getMrProfile(String mrId, String sessionId) throws RemoteException;
 
  /**
   * Get DRInformation list with MR's message count for newly received Contact 
   * and DR's unread sent EDetail.
   */
  public Collection getMRListWithMessageCount(DR dr, Map drMessageHashdata, String sessionId) throws RemoteException;

  /**
   * Add a new MrProfile to persistent storage
   * @param drId The DR who owns the
   */
  public void addDRMemo(String mrId, DR userDR, String memo, String sessionId) throws RemoteException;

 /**
  * Retrieve an MrProfile object matching the DrId and MrId specified
  */
  public void updateDRMemo(String mrId, String drId, String memo, String sessionId) throws RemoteException;

  /**
   * Returns list of medical qualification with this object.
   */
  public Collection getMedicalQualificationList(String sessionId) throws RemoteException;

  /**
   * Returns list of Specialty with this object.
   */
  public Collection getSpecialtyList(String sessionId) throws RemoteException;

  /**
   * Returns Enquete information with this object.
   */
  public EnqueteInfo getEnqueteInfo(String drId, String sessionId) throws RemoteException;

  /**
   * Returns Enquete information with this object.
   */
  public EnqueteInfo getEnqueteInfoByEnqId(EnqueteInfo enqueteInfo, String sessionId) throws RemoteException;

  /**
   * Returns list of Enquete Question with this object.
   */
  public Collection getEnqueteQuestion(EnqueteInfo enqueteInfo, String sessionId) throws RemoteException;

  /**
   * Add Enquete Answer with this object.
   */
  public void addEnqueteAnswer(EnqueteInfo enqueteInfo, String sessionId) throws RemoteException;

 /**
  * Update the mr's banner position
  */
  public void updateMRBannerPosition(Collection colMRList, String drId, String firstPosition, String secondPosition, String sessionId) throws RemoteException;

  /**
   * Adds the specified activity's point value to the DR and saves the DR.
   */
  public void addPoints(DR userDR, String triggerAction, String sessionId) throws RemoteException;

  /**
   * Send an email that this dr has registered a new MR
   */
  public void notifyMRRegistration(DR userDR, String mrId, String sessionId) throws RemoteException;

  /**
   * Send an email that this dr has registered a new MR
   */
  public void notifyNewEdetail(DR userDR, String mrId, String subject, String sessionId) throws RemoteException;

  /**
   * Send the email to the mr notifying that he/she has just been deleted by the DR.
   */
  public void sendDeletedMREmail(Collection colEmailMRList, DR dr, String sessionId) throws RemoteException;

  /**
   * This will return the emSend the email to the mr notifying that he/she has just been deleted by the DR.
   */ 
  public String getEmzoId(String sessionId) throws RemoteException;

  /**
   * Handles the case where a doctor reads a new edetail.
   */
  public void markNewMessageAsRead(DR userDR, Message msg, String sessionId) throws RemoteException;
}
