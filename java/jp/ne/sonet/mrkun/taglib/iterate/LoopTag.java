/*
 * LoopTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.iterate;


import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which loops over an HTML body a specified number of times.
 * <p>
 * The start and end parameter are inclusive, i.e. start="1" and 
 * end="3" will loop three times.
 * <p>
 * The current counter value is available inside the loop using the
 * optional counterVariable parameter.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:loop start="1" end="3" counterVariable="i"&gt;
 * Loop number &lt;%= i %&gt;&lt;br&gt;
 * &lt;/x:loop&gt;
 * </pre>
 *
 * @author 
 */
 
public class LoopTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * The counter variable for the loop.
   */
   protected int counter;
   
  /**
   * The name of the counter variable as seen inside the body tag.
   */
   protected String counterVariable = null;
  
  /**
   * The start value for the loop.
   */
   protected int start = 0;
  
  /**
   * The end value for the loop, inclusive.
   */
   protected int end;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the counter variable name
   */
  public String getCounterVariable()
  {
    return this.counterVariable;
  }
  
  /**
   * Set the start value.
   */
  public void setCounterVariable(String counterVariable)
  {
    this.counterVariable = counterVariable;
  }

  /**
   * Return the start value
   */
  public int getStart()
  {
    return this.start;
  }
  
  /**
   * Set the start value.
   */
  public void setStart(int start)
  {
    this.start = start;
  }

  /**
   * Return the end value
   */
  public int getEnd()
  {
    return this.end;
  }
  
  /**
   * Set the end value.
   */
  public void setEnd(int end)
  {
    this.end = end;
  }

  // --------------------------------------------------------- Public Methods

   
  /**
   * Process the start tag: initialize the counter.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG to tell the JSP page to continue evaluating the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    this.counter = this.start;
    if (this.counterVariable != null)
      pageContext.setAttribute(this.counterVariable, new Integer(this.counter));
    
    if (hasMoreElements())
      return EVAL_BODY_TAG;
    else
      return SKIP_BODY;
  }
  

  /**
   * Process the body of the tag: check it the end of the loop has
   * been reached. If not, evaluate the body again.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY or EVAL_BODY_TAG
   */
  public int doAfterBody()
  throws JspException
  {
    this.counter++;
    if (this.counterVariable != null)
      pageContext.setAttribute(this.counterVariable, new Integer(this.counter));

    if (hasMoreElements())
      return EVAL_BODY_TAG;
    else
      return SKIP_BODY;
  }
  
  /**
   * Process the end tag: print out the bodies.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_PAGE to tell the JSP page to continue evaluating page
   */
  public int doEndTag() 
  throws JspException 
  {
    try
    {
      if (bodyContent != null)
        bodyContent.writeOut(pageContext.getOut());
    }
    catch(IOException e)
    {
      throw new JspException("IO Error: " + e.getMessage());
    }
 
    return EVAL_PAGE;
  }
  
  public boolean hasMoreElements()
  {
    boolean retVal = false;
    
    if (this.counter <= this.end) 
    {
      retVal = true;
    }
    
    return retVal;
  }
}