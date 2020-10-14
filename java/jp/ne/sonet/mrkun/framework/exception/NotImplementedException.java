
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * This exception is used as a place holder to tell us that we are calling
 * a method that either should be implemented or should not be called.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: NotImplementedException.java,v 1.1.2.2 2001/08/07 07:00:07 rick Exp $
 */
public class NotImplementedException extends RuntimeException
{

  /**
   * Constructor
   */
  public NotImplementedException()  {super();}
  public NotImplementedException(String message)  {super(message);}
}

 