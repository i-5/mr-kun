
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * Exception denoting the failure to find the userid presented to the
 * M3SecurityManager.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: UserNotFoundException.java,v 1.1.2.1 2001/07/18 09:57:20 rick Exp $
 */
public class UserNotFoundException extends RuntimeException
{

  /**
   * Constructor
   */
  public UserNotFoundException()                {super();}
  public UserNotFoundException(String message)  {super(message);}
}

 