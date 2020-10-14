
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * Exception indicating that the object being retrieved from
 * the database was not found.
 * <P>
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 */
public class NoObjectFoundException extends RuntimeException
{

  /**
   * Constructor
   */
  public NoObjectFoundException()               {super();}
  public NoObjectFoundException(String message) {super(message);}
}

 