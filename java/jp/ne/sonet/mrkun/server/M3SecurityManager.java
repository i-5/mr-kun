
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import java.rmi.*;
import java.security.*;

/**
 * This class handles the authentication of an MRKun user, checking the
 * validity of the password, and if successful, returning a populated
 * instance of the value object associated with this kind of user.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: M3SecurityManager.java,v 1.1.2.13 2001/11/13 07:56:55 rick Exp $
 */
public class M3SecurityManager
{
  /**
   * Constructor
   */
  public M3SecurityManager()  {}

  /**
   * Attempts to login as either an MR or a DR. Tries an MR first, and
   * if unsuccessful, tries a DR.
   */
  public Object authenticate(String login, String password, String userKey, String sessionId)
    throws UserNotFoundException, LoginFailedException
  {
    try
    {
      // Try to login as an MR first
      return authenticateMR(login, password, userKey, sessionId);
    }
    catch (UserNotFoundException errUNF)
    {
      // If not found as an MR, try a DR
      return authenticateDR(login, password, userKey, sessionId);
    }
  }

  /**
   * Tries to authenticate an MR using the database. The MRManager bean is loaded
   * and if found, the supplied password is compared with the database.
   * @param login The user's loginId.
   * @param password The user's password.
   * @param userAgent The user's browser Agent.
   * @exception UserNotFoundException If there is no MR with the supplied loginId.
   * @exception LoginFailedException If the password supplied doesn't match the
   *                                 one in the database.
   */
//  public MR authenticateMR(String login, String password, String userKey)
  public MR authenticateMR(String login, String password, String userAgent, String sessionId)
    throws UserNotFoundException, LoginFailedException
  {
    try
    {
      // Get the MRManager bean
      MRManagerRemoteIntf mrm = (MRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.MRMANAGER_HOME);

      // Validate the details
      MR loggedInMR = mrm.loginMR(login, password, userAgent, sessionId);

      // Store the object in the UserStateFactory
      //UserStateContainer state = UserStateFactory.getState(userKey);
      //state.setUserItem(loggedInMR);
      return loggedInMR;
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Remote error while authenticating", errRemote);
    }	
  }	
	
  /**
   * doctor ID is attested using hash data. Moreover, the character 
   * which cannot be used is changed.
   * @param login The user's loginId.
   * @param hashValue The user's hash code.
   * @exception LoginFailedException If the hash code supplied doesn't match
   */
  public String authenticateDR(String login, String hashValue, String agent, String sessionId)
    throws LoginFailedException, ApplicationError
  {
    // Rewrite the drId if necessary here
    String drId = null;

    if (hashIsValid(login, hashValue))
    {
      //Doctor Id space change
      drId = changeDrId(login);
    }
    else
      throw new LoginFailedException("Hash supplied for user " + login + " was incorrect.");

    try
    {
      // Get the DRManager bean
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);

      // The history of Doctor login information is recorded.
      if(agent == null) agent = "";
      drm.setLogDR(drId, agent, sessionId);
//    System.out.println("M3SecurityManager.authenticateDR: stored login info for dr=:"+drId+agent); 
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Remote error while authenticating Log", errRemote);
    }

    return drId;
  }


  /**
   * Tries to authenticate a DR using the database. The DRManager bean is loaded.
   * @param login The user's loginId.
   * @exception UserNotFoundException If there is no DR with the supplied loginId.
   */
  public DR loginDR(String drId, String sessionId)
    throws UserNotFoundException
  {
    try
    {
      // Get the DRManager bean
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);

      // Validate the details
      DR userDR = drm.loginDR(drId, sessionId);

      return userDR;
    }
    catch (RemoteException errRemote)
    {
//      return null;
      throw new ApplicationError("Remote error while authenticating", errRemote);
    }
    catch (NoObjectFoundException errRemote)
    {
      return null;
//      throw new ApplicationError("Remote error while authenticating", errRemote);
    }
  }

  /**
   * Checks to see if the hash supplied is a valid MD5 hash of the
   * drId and a secret code known to us.
   * @param drId The DR object's login Id.
   * @param passwordHash The supplied hash for authentication.
   * @returns True if the hash is valid for the drId and a secret code.
   */
  protected boolean hashIsValid(String drId, String passwordHash)
  {
    try
    {
      //HashÉRÅ[Éhïœä∑
      String str = drId + HttpConstant.MEDIPRO_HASH_KEY_CODE;

      byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
      StringBuffer s = new StringBuffer();
      for (int i = 0; i < digest.length; i++) {
        s.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
        s.append(Integer.toString(digest[i] & 0x0f, 16));
      }

      if( passwordHash == null || passwordHash.equals("") ){
        passwordHash = s.toString();
      }

      //î‰är
      if ( passwordHash.equalsIgnoreCase( s.toString() ) ){
        return true;	//OK
      }else{
        return false;
      }
    }
    catch (NoSuchAlgorithmException err)
    {
      throw new ApplicationError("security: Hash(MD5) Error", err);
    }	

  }

  /**
   * space to "~~"
   **/
  public String changeDrId( String drId )
  {
    String newID="";
    int length = drId.length();
    int i = 0;
    while(i < length ) {
      int j=i +1;
      String s = drId.substring(i , j );//àÍï∂éöÇ√Ç¬éÊìæ
      if ( s.equals(" ") == true ) {
        s = "~~";
      }
      newID = newID.concat(s);//ÇìÇí«â¡Ç∑ÇÈ
      i++;
    }
    return newID;
  }
}

