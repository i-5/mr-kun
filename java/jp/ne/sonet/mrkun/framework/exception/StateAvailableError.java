
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;
import jp.ne.sonet.mrkun.server.State;

/**
 * This exception will be thrown when unhandlable application error or exception happens
 * in the master servlet.
 *
 * @author 
 * @version $Id: StateAvailableError.java,v 1.1.2.1 2001/09/06 08:40:03 damon Exp $
 */
public class StateAvailableError extends ApplicationError
{

  // Master servlet's state object, this contains 
  // page flow and execution stack information 
  private State state;
  
  /**
   * Constructor
   */    
  public StateAvailableError(String message, State pState)  
  {
  	super(message);
    this.state = pState;
    
    // Harry, please fill in the rest of the related logic to 
    // handle the state, if there is any.   Damon 9/5/2001
  }
  
  // This will return the state object that get passed in 
  // when this exception is being thrown
  public State getState()
  {
    return this.state;
  }
  
}