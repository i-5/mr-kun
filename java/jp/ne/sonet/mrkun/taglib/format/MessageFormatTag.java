/*
 * MessageFormatTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.format;


import java.io.*;
import java.text.*;
  
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which displays message formatted according to the specified format.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:messageFormat pattern="Error {0} occurred: {1}" arguments="&lt;%= errorArgs %&gt;" /&gt;
 * will display Error 42 occurred: Life is undefined.
 * </pre>
 *
 * @author
 */
public class MessageFormatTag extends TagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The pattern to pass to MessageFormat.
   */
  private String pattern;

  /**
   * The array of arguments.
   */
  private Object[] arguments;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the pattern.
   */
  public String getPattern()
  {
    return this.pattern;
  }
  
  /**
   * Set the pattern.
   */
  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }

  /**
   * Return the arguments.
   */
  public Object[] getArguments()
  {
    return this.arguments;
  }
  
  /**
   * Set the arguments.
   */
  public void setArguments(Object[] arguments)
  {
    this.arguments = arguments;
  }

  
  // --------------------------------------------------------- Public Methods

  /**
   * Process the start tag: nothing to do here.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to skip the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    return SKIP_BODY;
  }
  

  /**
   * Process the end tag: output the current date in the specified format.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_PAGE to tell the JSP page to continue evaluating page
   */
  public int doEndTag() 
  throws JspException 
  {
    if (arguments != null && pattern != null) {
        MessageFormat messageFormat = new MessageFormat(pattern);
    
        try
        {
          pageContext.getOut().write(messageFormat.format(arguments));
        }
        catch(IOException e)
        {
          throw new JspException("IO Error: " + e.getMessage());
        }
    }
    
    return EVAL_PAGE;
  }
}