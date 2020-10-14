
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import javax.servlet.http.*;
import java.security.*;
import java.util.*;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class is a singleton factory that creates an instance of
 * UserStateContainer. It ensures that there is a maximum of one instance of
 * a UserStateContainer in existence per user session. It is also responsible
 * for destroying UserStateContainer objects when the user ends their session.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class UserStateFactory
{
  final static int STATE_TIMEOUT_MINUTES = 60;

  // Note the implementation classes of the Map interfaces here MUST be
  // threadsafe for all the below - eg java.util.Hashtable
  private static Map  userStateRepository   = null;
  private static Map  statisticsRepository  = null;
  private static Map  eventHandlerPlugins   = null;

  public static Object allStatsSemaphore = new Boolean(true);

  static
  {
    init();
    eventHandlerPlugins = new Hashtable();
    handleEvent("InitialiseStatistics", null, false);
  }

  private static void init()
  {
    userStateRepository   = new Hashtable();
    statisticsRepository  = new Hashtable();
  }

  public static UserStateContainer getState(String userKey)
  {
    return getState(userKey, null, false);
  }

  public static UserStateContainer getState(HttpServletRequest request)
  {
    return getState(getStateKey(request), null, false);
  }

  /**
   * Loads the state container for this session key. If a userId is supplied,
   * this is assumed to be a login case, and the state object is initialised.
   * @param userKey The unique id on which the state object is keyed.
   * @param userId If non-null, this user is loaded when there is no state
   * present for this key.
   * @param isDoctor True if the intended userId is a drId. Unused if the userId
   * is null.
   */
  public static synchronized UserStateContainer getState(String userKey, String userId, boolean isDoctor)
  {
    if (userStateRepository == null)
      init();
      
    // Throw out old state objects (this could be moved into a thread with a
    // timer, but the repository would then be exposed ... yokunai. We would
    // need to synchronize the respository itself as well, not just this method.)
    Calendar calWhen = Calendar.getInstance();
    calWhen.setTime(new Date());
    calWhen.add(Calendar.MINUTE, STATE_TIMEOUT_MINUTES);
    Date timeout = calWhen.getTime();
    for (Iterator i = userStateRepository.keySet().iterator(); i.hasNext(); )
    {
      String loopKey = (String) i.next();
      UserStateContainer loopItem = (UserStateContainer) userStateRepository.get(loopKey);
      if (timeout.before(loopItem.getLastAccessed()))
        userStateRepository.remove(loopKey);
    }

    // Look for the state object matching this user's session
    UserStateContainer userState = (UserStateContainer) userStateRepository.get(userKey);
    if ((userState == null) && (userId == null))
      throw new SessionFailedException("No userId specified, and there is no session in scope");
    else if (userState == null)
    {
      System.out.println("Initialising state container from the database: " + userId);
      if (isDoctor)
        userState = new DrStateContainer(userId);
      else
        userState = new MrStateContainer(userId, statisticsRepository);
      userStateRepository.put(userKey, userState);
    }

    return userState;
  }

  /**
   * Retrieves the login cookie with session info
   */
  public static String getStateKey(HttpServletRequest req)
  {
    // Find the cookie
    Cookie cookieList[] = req.getCookies();
    for (int n = 0; n < cookieList.length; n++)
      if (cookieList[n].getName().equals(HttpConstant.LOGIN_COOKIE_NAME))
        return cookieList[n].getValue().trim();

    // If not found, look for the hash in the request attributes
    if (req.getAttribute(HttpConstant.LOGIN_COOKIE_NAME) != null)
      return (String) req.getAttribute(HttpConstant.LOGIN_COOKIE_NAME);
    throw new SessionFailedException("Session login cookie not found");
  }

  /**
   * Creates the login cookie with session info
   */
  public static String createStateKey(HttpServletResponse rsp, String userId, boolean isDoctor)
  {
    try
    {
      // Build the cookie value
      String loginKey = (isDoctor ? "DR" : "MR") + ":" + userId + ":" + new Date();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(loginKey.getBytes());

      StringBuffer hashedKey = new StringBuffer(64);
      byte hashedArray[] = md5.digest();
      for (int n = 0; n < hashedArray.length; n++)
        hashedKey.append(hashedArray[n] + "-");

      Cookie loginCookie = new Cookie(HttpConstant.LOGIN_COOKIE_NAME, hashedKey.toString());
      loginCookie.setMaxAge(-1);
      rsp.addCookie(loginCookie);
      return hashedKey.toString();
    }
    catch (NoSuchAlgorithmException errNSA)
    {
      throw new ApplicationError("Couldn't load MD5 algorithm", errNSA);
    }
  }

  /**
   * Handles an event that modifies the content of the state container
   * @param eventId The Id of the event operation. This is used to look up
   *                the plug-in class for processing the event.
   * @param spawnAsThread If true, the event is handled asynchronously.
   */
  public static void handleEvent(String eventId, Map eventParams, boolean spawnAsThread)
  {
    if (eventHandlerPlugins == null)
      eventHandlerPlugins = new Hashtable();

    // Check for this event's handler plug-in
    BaseEventHandler handlerTemplate = (BaseEventHandler) eventHandlerPlugins.get(eventId);
    BaseEventHandler handler = null;
    try
    {
      if (handlerTemplate == null)
      {
        // Try to load the plug in class
        Class handlerClass = Class.forName("jp.ne.sonet.mrkun.event." + eventId);
        handler = (BaseEventHandler) handlerClass.newInstance();
        eventHandlerPlugins.put(eventId, handler.newInstance());
      }
      else
        handler = handlerTemplate.newInstance();
    }
    catch (ClassNotFoundException errCNF)
      {throw new ApplicationError("Couldn't find the class for eventId: " + eventId, errCNF);}
    catch (IllegalAccessException errIA)
      {throw new ApplicationError("Couldn't access the class for eventId: " + eventId, errIA);}
    catch (InstantiationException errInst)
      {throw new ApplicationError("Couldn't instantiate the class for eventId: " + eventId, errInst);}

    // Initialise the objects that the thread needs
    handler.setUserRepository(userStateRepository);
    handler.setStatisticsRepository(statisticsRepository);
    handler.setParams(eventParams);

    // Execute the event handler
    handler.startEvent(spawnAsThread);
  }
}
