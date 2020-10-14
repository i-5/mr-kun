/*
 * IsEmptyTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;


import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * A tag which checks the size of a collection and if it is
 * empty evaluates the body tag.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:isEmpty collection="&lt;%= my_list %&gt;"&gt;
 * my_list is empty!
 * &lt;/x:isEmpty&gt;
 * </pre>
 *
 * @author 
 */
 
public class IsEmptyTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The name of the collection.
   */
   private String collection;
   
  
  // ------------------------------------------------------------- Properties

  /**
   * Return the name of the collection.
   */
  public String getCollection()
  {
    return this.collection;
  }
  
  /**
   * Set the name of the collection.
   */
  public void setCollection(String collection)
  {
    this.collection = collection;
  }


  // --------------------------------------------------------- Public Methods
   
  /**
   * Process the start tag: if the collection is empty,
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
   * Process the body of the tag: we only get here if the collection
   * is empty.
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
      // not much we can do here
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
   * Evaluate the collection.
   *
   * @return true if the collection is null or empty.
   */
  public boolean evaluate()
  {
    boolean retVal = true;

    try
    {    
      Collection theCollection = (Collection)pageContext.getAttribute(this.collection, PageContext.REQUEST_SCOPE);
      if (theCollection != null) 
      {
        if (theCollection.size() > 0) 
        {
          retVal = false;
        }
      }    
    }
    catch (Exception e)
    {
      retVal = true;
    }
    
    return retVal;
  }
}