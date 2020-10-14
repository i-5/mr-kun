
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.log.*;
import jp.ne.sonet.mrkun.valueobject.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * This class is intended to be an alternative to the SessionManager
 * class. The constructor accepts a request object, retrieving the
 * variables required from the user's session state.<br>
 *
 * This class is intended to be re-instantiated with every request, ie
 * <code>SessionManager mgr = new SessionManager(request,
 * new Boolean(false));</code>. The underlying HttpSession management
 * ensures that we retain the same session variable as last time.<br>
 *
 * Following a login, <code>postLogin()</code> must be called, followed
 * by <code>setUserItem()</code>. These two methods setup the static
 * hashtables for admin snooping and the identification of the user's
 * type.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: SessionManager.java,v 1.1.1.1.2.13 2001/09/06 04:15:45 rick Exp $
 */
public class SessionManager
{
  final String SESSION_DATA = "user_object";
  public final static String MRPROFILE_LIST = "mrProfileList";

  private HttpSession userSession;

  /**
   * A map containing all the application users currently logged in.
   */
  public static Map loginNames = new Hashtable();

  /**
   * A map containing all the userItem objects (eg DR, MR, etc) currently
   * in use.
   */
  //public static Map userItems  = new Hashtable();

  /**
   * Constructor
   */
  public SessionManager(HttpServletRequest request, Boolean create)
  {
    // Check for an existing session
    if (create == null)
      throw new IllegalArgumentException("create must be true or false");
    else
      userSession = request.getSession(create.booleanValue());
  }

  /**
   * This method is called after a successful login, and registers
   * the user as being logged in within the admin snooping hashtables.
   */
  public synchronized void postLogin(String uid)
  {
  	// Let same user has multiple sessions
    if (!loginNames.containsKey(userSession.getId()))
      loginNames.put(userSession.getId(), uid);
  }

  /**
   * This method is called just before logging out, removing the
   * record of the user's session within the admin snooping hashtables.
   */
  public synchronized void preLogout(String uid)
  {
    if (!loginNames.containsKey(userSession.getId()))
      loginNames.remove(userSession.getId());
  }

  /**
   * Returns the current user's details object (eg MR, DR)
   */
  public synchronized Object getUserItem()
  {
    if (userSession == null)
      throw new SessionFailedException("Session timeout or invalid. Please login again.");
    else
      return userSession.getAttribute(SESSION_DATA);
  }

  /**
   * Sets the current user's details object (eg MR, DR)
   */
  public synchronized void setUserItem(Object value)
  {
    if (userSession == null)
      throw new SessionFailedException("Session timeout or invalid. Please login again.");
    else
      userSession.setAttribute(SESSION_DATA, value);
  }

  /**
   * Retrieves a standard httpsession variable.
   */
  public Object getSessionItem(String key)
  {
    if (userSession == null)
      throw new SessionFailedException("Session timeout or invalid. Please login again.");
    else
      return userSession.getAttribute(key);
  }

  /**
   * Sets a standard httpsession variable.
   */
  public void setSessionItem(String key, Object value)
  {
    if (userSession == null)
      throw new SessionFailedException("Session timeout or invalid. Please login again.");
    else
      userSession.setAttribute(key, value);
  }

}

