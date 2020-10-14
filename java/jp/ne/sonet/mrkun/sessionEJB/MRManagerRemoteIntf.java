
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import java.rmi.*;
import java.util.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * The remote interface for the MR7 Session EJB MRManager.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MRManagerRemoteIntf.java,v 1.1.2.27 2001/11/13 07:56:56 rick Exp $
 */
public interface MRManagerRemoteIntf extends BaseSessionEJBRemoteIntf
{
  /**
   * Get the MR object by its ID from persistent storage
   */
  public MR getMRById(String mrId, String sessionId) throws RemoteException;

  /**
   * Get the MR object by its name from persistent storage
   */
  public MR getMRByName(String name, String sessionId) throws RemoteException;

  /**
   * Save the already persisted DR object into persistent storage
   */
  public void updateMR(MR userMR, String sessionId) throws RemoteException;

  /**
   * Save a DRInformation object to persistent storage
   */
  public void updateDRInfo(DRInformation drInfo, String sessionId) throws RemoteException;
  
  /**
   * Get the DRInformation objects that associated with the MR.
   */
  public DRInformation getDRInformation(String mrId, String drId, String sessionId) throws RemoteException;

  /**
   * Get the list of DRInformation objects that associated with the MR.
   */
  public Collection getDRInformationList(String mrId, String sessionId) throws RemoteException;
 
  /**
   * This obtains the data for pages MR2.0 and MR 5.0 - it uses the optimised
   * DRInfo retrieval query (some fields may not be present - check first).
   */
  public Collection getDRInformationList(String mrId, Map messageCounts, String sessionId) throws RemoteException;

  /**
   * Get DRInformation list with MR's message count for newly received Contact 
   * and DR's unread sent EDetail.
   */
  public Collection getDRListWithMessageCount(MR mr, Map mrMessageHashdata, String sessionId) throws RemoteException;

  /**
   * Gets all the Importance list of Company
   */
  public Collection getImportanceList(Company company, String sessionId) throws RemoteException;
 
  /**
   * Gets a webImage by its Id.
   */
  public WebImage getWebImageById(String webImageId, String sessionId) throws RemoteException;

  /**
   * Gets all the webImages for a given MR.
   */
  public Collection getWebImagesByMrId(String mrId, String sessionId) throws RemoteException;

  /**
   * This method attempts an MR login, handling the not found and
   * bad password exception cases.
   */
  public MR loginMR(String login, String password, String userAgent, String sessionId)
    throws UserNotFoundException, LoginFailedException, RemoteException;

  /**
   * Retrieve a collection of MR objects matching the DrId specified
   */
  public Map getMRProfileByDRId(String drId, String sessionId) throws RemoteException;

  /**
   * Retrieve an MrProfile object matching the DrId and MrId specified
   */
  public MrProfile getMRProfileById(String mrId, String drId, String sessionId) throws RemoteException;
  
 /**
  * Retrieve the doctor's memo field for MrProfile object with the DrId and MrId specified
  */
  public String getDRMemo(String mrId, String drId, String sessionId) throws RemoteException;

  /**
   * Retrieve the mr's banner position  
   */
  public String getBannerPosition(String mrId, String drId, String sessionId) throws RemoteException;

 /**
  * Retrieve list of MrProfile to be deleted 
  */
  public Collection getDeletingMRProfileList(String[] deleteMRIdList, String drId, String sessionId) throws RemoteException;

 /**
  * Remove registed mr(s) from dr's mr profile list 
  */
  public void removeMR(Collection colDeleteMRList, String drId, String sessionId) throws RemoteException;

  /**
   * Send a big brother cc of the new edetail sent but the MR
   */
  public void notifyBigBrotherEDetail(MR userMR, String drId, Message msg, String plainText, String sessionId) throws RemoteException;
}

 