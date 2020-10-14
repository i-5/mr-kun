
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import javax.ejb.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;

/**
 * Home interface for the MessageManager Stateless Session EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: MessageManagerHomeIntf.java,v 1.1.2.1 2001/07/24 07:13:51 rick Exp $
 */
public interface MessageManagerHomeIntf extends BaseSessionEJBHomeIntf
{
  public MessageManagerRemoteIntf create() throws CreateException, RemoteException;
}


