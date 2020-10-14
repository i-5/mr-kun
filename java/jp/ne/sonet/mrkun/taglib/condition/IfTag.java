/*
 * IfTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which evaluates the body of the tag if the expression is true.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:if expression="&lt;%= (1==1) %&gt"&gt;
 * Math rules.
 * &lt;/x:if&gt;
 * </pre>
 *
 * @author 
 */
 
public class IfTag implements Tag
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * The page context. Set by the servlet engine.
   */
  PageContext pageContext;

  /**
   * The parent tag of this tag. Set by the servlet engine.
   */
  Tag parent;

  /**
   * The expression to evaluate.
   */
   protected boolean expression;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the parent.
   */
  public Tag getParent()
  {
    return parent;
  }

  /**
   * Set the parent.
   */
  public void setParent(final Tag parent)
  {
    this.parent = parent;
  }
  
  /**
   * Set the page context.
   */
  public void setPageContext(final PageContext pageContext)
  {
    this.pageContext = pageContext;
  }
  
  /**
   * Return the expression.
   */
  public boolean getExpression()
  {
    return this.expression;
  }
  
  /**
   * Set the expression.
   */
  public void setExpression(boolean expression)
  {
    this.expression = expression;
  }


  // --------------------------------------------------------- Public Methods
  
  /**
   * Release the state. Called by the servlet engine.
   */
  public void release()
  {
  }


  /**
   * Process the start tag: encode the url and output an HTML link command.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG to tell the JSP page to continue evaluating the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    int retVal = SKIP_BODY;

    if (this.evaluate()) 
    {
      retVal = EVAL_BODY_INCLUDE;
    }
      
    return retVal;
  }


  /**
   * Process the end tag: output the end tag of the HTML link.
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
   * @return true if the expression evaluates to true.
   */
  public boolean evaluate()
  {
    return expression;
  }
}