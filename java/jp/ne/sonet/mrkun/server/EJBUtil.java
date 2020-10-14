// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import javax.ejb.*;
import jp.ne.sonet.mrkun.server.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

/**
 *
 * This class will provide services for ejb related operations.
 *
 * @author Damon Lok
 * @version $Id: EJBUtil.java,v 1.1.2.7 2001/08/31 09:06:48 damon Exp $
 *
 */
public class EJBUtil
{
  /**
   * Gets a session EJB remote interface given a constant defining the
   * object name. This method will never return null.
   *
   * @param pManagerName constant defining the object type
   *
   * @return a remote interface to a session EJB
   */
  public BaseSessionEJBRemoteIntf getManager(String pManagerName)
  { 
  	BaseSessionEJBRemoteIntf remoteInterface = null;
  	
  	if (pManagerName == null)
  	{
  	  // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName()
         			    + ": " + pManagerName + " is null.");
  	}

  	try
	  {
	    //Get the real stuff: remote interface of the bean
	    InitialContext initial = new InitialContext();
      Object objHome = initial.lookup(pManagerName);

	    if (pManagerName.equals(HttpConstant.MESSAGEMANAGER_HOME))
	    {
  	    MessageManagerHomeIntf homeInterface = (MessageManagerHomeIntf)
                PortableRemoteObject.narrow(objHome, MessageManagerHomeIntf.class);
	      remoteInterface = homeInterface.create();
	    }

  	  if (pManagerName.equals(HttpConstant.MRMANAGER_HOME))
	    {
	      MRManagerHomeIntf homeInterface = (MRManagerHomeIntf)
                PortableRemoteObject.narrow(objHome, MRManagerHomeIntf.class);
	      remoteInterface = homeInterface.create();
  	  }
	    if (pManagerName.equals(HttpConstant.DRMANAGER_HOME))
	    {
	      DRManagerHomeIntf homeInterface = (DRManagerHomeIntf)
                PortableRemoteObject.narrow(objHome, DRManagerHomeIntf.class);
	    	remoteInterface = homeInterface.create();
  	  }
	    if (pManagerName.equals(HttpConstant.REPORTMANAGER_HOME))
	    {
	      ReportManagerHomeIntf homeInterface = (ReportManagerHomeIntf)
                PortableRemoteObject.narrow(objHome, ReportManagerHomeIntf.class);
  	  	remoteInterface = homeInterface.create();
	    }
	    if (pManagerName.equals(HttpConstant.ASSETMANAGER_HOME))
	    {
	      AssetManagerHomeIntf homeInterface = (AssetManagerHomeIntf)
                PortableRemoteObject.narrow(objHome, AssetManagerHomeIntf.class);
  	  	remoteInterface = homeInterface.create();
	    }
	  }
    catch (NamingException e)
	  {
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Could not resolve the JNDI name "
                    + pManagerName, e);
	  }
    catch (CreateException e)
	  {
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Could not create "
                    + pManagerName + "HomeIntf.", e);
	  }
	  catch (RemoteException e)
	  {
      // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName() + ": Remote network problem occurs (RemoteException.)"
                    , e);
  	}

    if (remoteInterface == null)
  	{
	    // Throw ApplicationError
      throw new ApplicationError(this.getClass().getName()
         			    + ": Returned " + remoteInterface + " is null.");
    }
  	return remoteInterface;
  }

}