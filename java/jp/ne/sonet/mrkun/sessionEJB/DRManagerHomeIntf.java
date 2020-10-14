
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import java.rmi.RemoteException;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * Home interface for the DRManager bean.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DRManagerHomeIntf.java,v 1.1.1.1.2.2 2001/07/24 02:16:07 rick Exp $
 */
public interface DRManagerHomeIntf extends BaseSessionEJBHomeIntf
{
  public DRManagerRemoteIntf create() throws CreateException, RemoteException;
}

 