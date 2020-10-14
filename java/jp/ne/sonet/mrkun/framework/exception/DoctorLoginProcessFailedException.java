
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

/**
 * This exception will be thrown when DR login process failed in 
 * undetected error.  
 *
 * @author Damon Lok
 * @version $Id: DoctorLoginProcessFailedException.java,v 1.1.2.1 2001/09/17 06:45:28 damon Exp $
 */
public class DoctorLoginProcessFailedException extends RuntimeException
{

  /**
   * Constructor
   */
  public DoctorLoginProcessFailedException()                {super();}
  public DoctorLoginProcessFailedException(String message)  {super(message);}
}

 