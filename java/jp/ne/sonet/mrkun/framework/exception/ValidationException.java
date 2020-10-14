
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * An exception used for identifying failures during validation.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: ValidationException.java,v 1.1.1.1.2.1 2001/07/17 05:41:38 damon Exp $
 */
public class ValidationException extends RuntimeException
{
  /**
   * Constructor
   */
  public ValidationException()                {super();}
  public ValidationException(String message)  {super(message);}
}

 