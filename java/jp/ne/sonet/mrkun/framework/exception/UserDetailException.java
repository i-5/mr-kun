
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * This exception will be thrown when application has sensible error message to 
 * display to the user.
 *
 * @author Damon
 * @version $Id: UserDetailException.java,v 1.1.2.1 2001/09/06 08:39:35 damon Exp $
 */
public class UserDetailException extends RuntimeException
{

  /**
   * Constructor
   */
  public UserDetailException()                 {super();}
  public UserDetailException(String message)  {super(message);}
}