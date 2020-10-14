/*
 * TopNavTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.navbar;


import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which creates an HTML link.
 * <p>
 * The parameter url will be used as the href in the link.
 * The body of the tag will be used as the text of the link.
 * If the body is empty, the url will be used as the text.
 * <p>
 * The url is passed through encodeURL to enable users
 * without cookies.
 * <p>
 *
 * Examples:
 * <pre>
 * &lt;x:link url="www.acme.com"&gt;ACME&lt;/x:link&gt;
 * will create &lt;a href="www.acme.com"&gt;ACME&lt;/a&gt;
 *
 * &lt;x:link url="www.acme.com" /&gt; or
 * &lt;x:link url="www.acme.com"&gt;&lt;/x:link&gt; or
 * will create &lt;a href="www.acme.com"&gt;www.acme.com&lt;/a&gt;
 * </pre>
 *
 * @author 
 */
 
public class TopNavTag implements Tag
{
 
  // ----------------------------------------------------- Instance Variables

  PageContext pageContext;
  Tag parent;

  /**
   * The page identifier.
   */
   private String pageId = null;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the page identifier.
   */
  public String getPageId()
  {
    return this.pageId;
  }
  
  /**
   * Set the page identifier.
   */
  public void setPageId(String pageId)
  {
    this.pageId = pageId;
  }

  // --------------------------------------------------------- Public Methods

   
  public void setPageContext(final PageContext pageContext)
  {
    this.pageContext = pageContext;
  }
  
  public void setParent(final Tag parent)
  {
    this.parent = parent;
  }
  
  public Tag getParent()
  {
    return parent;
  }
  
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
    pageContext.getRequest().setAttribute("pageId", this.getPageId());

    String url = createUrl(pageId, TYPE_START);
    if (url != null) 
    {
      includeJsp(url);
    }
    
    
    return EVAL_BODY_INCLUDE;
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
    String url = createUrl(pageId, TYPE_END);
    if (url != null) 
    {
      includeJsp(url);
    }
    
    return EVAL_PAGE;
  }
  
  private void includeJsp(String pUrl)
  throws JspException 
  {
    ServletContext context = pageContext.getServletContext();

    try 
    {
      context.getRequestDispatcher(pUrl).include(pageContext.getRequest(), pageContext.getResponse());
    } 
    catch (Exception e) 
    {
      throw new JspException("Include Error: " + e.toString());
    }
  }
  
  protected String createUrl(String pPageId, int pType)
  {
    String url = "/navbar/topnav";
    if (pType == TYPE_START)
    {
      url += "_start.jsp";
    }
    else 
    {
      url += "_end.jsp";
    }
    
    return url;
  }
    
  protected static int TYPE_START = 1;
  protected static int TYPE_END = 2;
  
}