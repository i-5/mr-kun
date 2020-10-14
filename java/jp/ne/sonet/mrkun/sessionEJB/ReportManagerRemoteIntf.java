
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import java.rmi.*;
import java.util.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * The remote interface for ReportManager.
 *
 * @author Damon Lok
 * @version 1 
 */
public interface ReportManagerRemoteIntf extends BaseSessionEJBRemoteIntf
{  
  /**
   * Get the Ranking object by MR ID from persistent storage
   */
  public Ranking getUserRanking(String mrId, String sessionId) throws RemoteException;

  /**
   * Get the usage statistics object by MR ID from persistent storage
   */
  public Map getUsagePoint(String mrId, String sessionId) throws RemoteException;
  
  /**
   * Get the single usage statistics object by MR ID and DR ID from persistent storage
   */
  public Map getSingleUsagePoint(String mrId, String drId, String sessionId) throws RemoteException;
}

 