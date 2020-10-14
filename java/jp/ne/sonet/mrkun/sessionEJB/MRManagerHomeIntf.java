
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * Home interface for the MRManager Stateless Session EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MRManagerHomeIntf.java,v 1.1.2.2 2001/07/19 04:52:06 rick Exp $
 */
public interface MRManagerHomeIntf extends BaseSessionEJBHomeIntf
{
  public MRManagerRemoteIntf create() throws CreateException, RemoteException;
}

 