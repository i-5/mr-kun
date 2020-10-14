package jp.ne.sonet.mrkun.framework.sessionEJB;

import javax.ejb.*;
import java.rmi.RemoteException;
import javax.ejb.EJBContext;
import javax.ejb.EJBHome;
import java.security.Principal;
import java.util.Properties;

/**
 * The base class for Session EJBs. This class implements the EJB
 * lifecycle methods and stores the context variable in protected
 * scope.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: BaseSessionEJB.java,v 1.1.1.1.2.8 2001/08/07 09:08:01 damon Exp $
 */
public abstract class BaseSessionEJB implements SessionBean
{
	
	/** Context for the Session EJB */ 
	protected SessionContext ctx = null;
	
	/** Context for the EJB */
	protected EJBContext ejb_ctx = null;

	// Methods to complete the SessionBean interface.
	public void ejbActivate() {}
	public void ejbPassivate() {}
	public void ejbRemove() {}
	public void ejbCreate() throws CreateException, RemoteException {}
	
	/**
	 * Store the current session's context information.  This method is invoked
	 * by the container after object construction and prior to ejbCreate().
	 * 
   * @param pSessionContext The session context as provided by the container.
	 */
	public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }
	
	/**
	 * Retrieve the bean's context information.
	 * @return SessionEJB context
	 */
	protected SessionContext getSessionContext()
	{
		return this.ctx;
	}
	
	/**
	 * Retrieve the bean's context information.
	 * @return EJB context
	 */
	protected EJBContext getEJBContext()
	{
		return this.ejb_ctx;
	}

	/**
	 * Retrieve the EJB context information of the bean.
	 * @param EJB context
	 */
	protected void setEJBContext(EJBContext ejb_ctx)
	{
		this.ejb_ctx = ejb_ctx;
	}
	
	/**
	* Retrieve the bean's caller credentials
	* @return EJB principal
	*/
	protected Principal getCallerPrincipal ()
	{
		return this.getEJBContext().getCallerPrincipal();
	}

	/**
	* Check if EJB caller belongs to specified role
	* @param roleName Role to compare against
	*/
	protected boolean isCallerInRole (String roleName)
	{
		return this.getEJBContext().isCallerInRole (roleName);
	}

	/**
	* Retrieve the bean's home interface object
	* @return EJB home interface object
	*/
	protected EJBHome getEJBHome()
	{
		return this.getEJBContext().getEJBHome();
	}
	
	/**
	 * Retrieve the bean's caller name
	 * @return EJB caller name
	 */
	protected String getCallerId()
	{
		try
		{
			return this.getCallerPrincipal().getName();
		}
		catch (NullPointerException exception)
		{
			return null;
		}
	}

}