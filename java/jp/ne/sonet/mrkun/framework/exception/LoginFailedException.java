
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * Exception denoting the failure of a login processed by the
 * M3SecurityManager
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: LoginFailedException.java,v 1.1.2.1 2001/07/18 09:57:20 rick Exp $
 */
public class LoginFailedException extends RuntimeException
{

  /**
   * Constructor
   */
  public LoginFailedException()                 {super();}
  public LoginFailedException(String message)  {super(message);}
}

