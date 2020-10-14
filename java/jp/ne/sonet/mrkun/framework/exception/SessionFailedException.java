
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * This exception will be thrown when session timeout or invalidation happens.
 *
 * @author Damon
 * @version $Id: SessionFailedException.java,v 1.1.2.2 2001/09/06 08:39:47 damon Exp $
 */
public class SessionFailedException extends RuntimeException
{

  /**
   * Constructor
   */
  public SessionFailedException()                 {super();}
  public SessionFailedException(String message)  {super(message);}
}