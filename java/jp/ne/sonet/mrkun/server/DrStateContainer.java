
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;

import java.io.*;
import java.util.*;
import java.rmi.*;


/**
 * The doctor specific parts of the state container object.
 * 
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class DrStateContainer extends UserStateContainer
{

  /**
   * Constructor
   */
  public DrStateContainer(String userId) {super(userId);}

  protected boolean isDoctor() {return true;}

  /**
   * Load up the Doctor
   */
  protected void loadUserItem(String drId)
  {
    try
    {
      // Load DRManager
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);
      userItem = drm.getDRById(drId);

      if (userItem == null)
        throw new UserNotFoundException("The dr: " + drId + " was not found.");

      opponentList = mrm.getMRProfileByDRId(drId);
      List defaultSortMrList = sortOpponents(opponentList.values(), DEFAULT_MRP_SORT_ORDER);
      sortedOpponentList = new Hashtable();
      sortedOpponentList.put(DEFAULT_MRP_SORT_ORDER, defaultSortMrList);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error loading up the DR for user: " + drId, errRemote);
    }
  }




}

 