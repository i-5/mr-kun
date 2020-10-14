// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.exception;

import java.io.*;

/**
 * <P>This class represents an unexpected error in the application that cannot 
 * be handled. This error is intended to be handled at the highest level, along
 * with runtime errors like NullPointerExceptions.
 *
 * @author 	Damon Lok
 * @version $Id: ApplicationError.java,v 1.1.1.1.2.3 2001/09/11 07:13:08 damon Exp $ 
 * @since   JDK1.3
 */
public class ApplicationError extends Error
{
	/** Nested exception */
	private Throwable nestedError = null;
    private String url = "";
   
    public static void main(String avg[])
    {
      ApplicationError e1 = new ApplicationError("Testing print stacktrace - no nested");
      ApplicationError e2 = new ApplicationError("Testing print stacktrace - nested exception", new RuntimeException("blah"));
      PrintWriter p = new PrintWriter(System.out);
      e1.printStackTrace(p);
      e2.printStackTrace(p);
      p.flush();
      p.close();
    }  
   
	/**
	 * Create an application exception with a useful message for the
	 * system administrator.
	 * @param pMsg Error message for to be used for administrative troubleshooting
	 */
	public ApplicationError(String pMsg)
	{
		super (pMsg);
	}
	
    /**
	 * Create an application exception with a useful message for the
     * system administrator and a nested throwable object.
	 * @param pMsg Error message for administrative troubleshooting
     * @param pError The actual exception that occurred
	 */
	public ApplicationError(String pMsg, Throwable pError)
	{
		super (pMsg);
        this.setNestedError(pError);
	}
    
	/**
	 * Create an application exception with a useful message for the
	 * system administrator, a nested throwable object and the url of
     * the page the redirection should go to.
	 * @param pMsg Error message for administrative troubleshooting
	 * @param pError The actual exception that occurred
	 */
	public ApplicationError(String pMsg, Throwable pError, String sourceURL)
	{
		super (pMsg);
	    this.url = sourceURL;
	}
    
    /**
	 * Get the url to redirect to. 
	 * @return The url 
	 */
	public String getURL(){return this.url;}

	/**
	 * Get the nested error or exception
	 * @return The nested error or exception
	 */
	public Throwable getNestedError(){return this.nestedError;}
    
  /**
   * Set the nested error or exception
   * @param pError The nested error or exception
   */
  private void setNestedError(Throwable pError){this.nestedError = pError;}

	/**
   * Output the details of this exception.
   * @return The detailed exception message
   */
  public String toString()
  {
  	StringBuffer outputString = new StringBuffer();
    outputString.append("APPLICATION ERROR: ")
                .append("\n\r  SysAdmin message: ")
                .append("\n\r  ")
                .append(super.toString());
    if ( this.getNestedError() != null )
      outputString.append("\n\r   Nested Error/Exception: ")
                  .append(this.getNestedError().toString());
    return outputString.toString();
  }

  public void printStackTrace(PrintWriter p)
  {
  	System.out.println("Execution is here.");
  	if (this.nestedError == null)
    {
      System.out.println("this.nestedError == null.");  
      this.setNestedError(new ApplicationError("Unknown error/exception has been caught in MRKUN_ErrorHandlerCtlr."));
    }
    else
    {
      System.out.println("this.nestedError != null.");  
      this.nestedError.printStackTrace(p);    
    }
    p.write("\n");
    super.printStackTrace(p);
  }
  
}