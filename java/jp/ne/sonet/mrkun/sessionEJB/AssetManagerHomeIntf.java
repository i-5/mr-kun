
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * Home interface for the AssetManager Stateless Session EJB.
 *
 * @author Damon Lok
 * @author M.Mizuki
 * @version 1
 */
public interface AssetManagerHomeIntf extends BaseSessionEJBHomeIntf
{
  public AssetManagerRemoteIntf create() throws CreateException, RemoteException;
}


