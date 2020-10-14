/*
 * IsNullTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;


import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which evaluates an expression and if the expression
 * is null evaluates the body tag.
 * <p>
 * If the expression if a String, then if the String is empty
 * the body tag is evaluated.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:isNull expression="&lt;%= name %&gt;"&gt;
 * Name is null!
 * &lt;/x:isNull&gt;
 * </pre>
 *
 * @author 
 */
 
public class IsNullTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The expression to evaluate.
   */
   protected Object expression;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the expression.
   */
  public Object getExpression()
  {
    return this.expression;
  }
  
  /**
   * Set the expression.
   */
  public void setExpression(Object expression)
  {
    this.expression = expression;
  }

  // --------------------------------------------------------- Public Methods

   
  /**
   * Process the start tag: if the expression evaluates to null,
   * evaluate the body of the tag otherwise skip it.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG or SKIP_BODY
   */
  public int doStartTag()
  throws JspException 
  {
    int retVal = SKIP_BODY;
    
    if (evaluate())
    {
        retVal = EVAL_BODY_TAG;
    }
    
    return retVal;
  }
  

  /**
   * Process the body of the tag: we only get here if the expression
   * evaluated to null.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to not evaluate the body again
   */
  public int doAfterBody()
  throws JspException
  {
    try
    {
      String body = getBodyContent().getString();
      
      // Write contents of the body to the parent's out stream. 
      try 
      {
        getPreviousOut().print(body);
      }
      catch(IOException e)
      {
        throw new JspException("IO Error: " + e.getMessage());
      }
    }
    catch (Exception e)
    {
      // not much to do here
    }
    
    return SKIP_BODY;
  }
  
  /**
   * Process the end tag: nothing to do here.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_PAGE to tell the JSP page to continue evaluating page
   */
  public int doEndTag() 
  throws JspException 
  {
    return EVAL_PAGE;
  }
  
  /**
   * Evaluate the expression.
   *
   * @return true if the expression is null or a String is empty.
   */
  public boolean evaluate()
  {
    boolean retVal = false;
    
    try
    {
      if (expression == null)
      {
        retVal = true;
      }
      else
      {
        if (expression.getClass().getName().equals("java.lang.String"))
          if (((String)expression).length() == 0)
            retVal = true;
      }
    }
    catch (Exception e)
    {
      retVal = false;
    }
    
    return retVal;
  }
}