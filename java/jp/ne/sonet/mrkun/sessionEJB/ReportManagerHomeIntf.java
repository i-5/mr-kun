
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * Home interface for the ReportManager Stateless Session EJB.
 *
 * @author Damon Lok
 * @version 1
 */
public interface ReportManagerHomeIntf extends BaseSessionEJBHomeIntf
{
  public ReportManagerRemoteIntf create() throws CreateException, RemoteException;
}


