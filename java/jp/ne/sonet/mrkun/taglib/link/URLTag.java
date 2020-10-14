/*
 * URLTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.link;


import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which encodes an URL for an HTML link.
 * <p>
 * The body of the tag will be used as the url.
 * <p>
 * The url is passed through encodeURL to enable users
 * without cookies.
 * <p>
 *
 * Examples:
 * <pre>
 * &lt;x:url&gt;www.acme.com&lt;/x:url&gt;
 * </pre>
 *
 * @author
 */
 
public class URLTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * If 'true' the url is to be treated as an absoulut url.
   */
   private String absolute = "false";
  
  /**
   * If 'true' link to a secure url.
   */
   private String secure = null;
  
  /**
   * Count the number of page views given this type.
   */
   private int countType = 0;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the absolut indicator.
   */
  public String getAbsolute()
  {
    return this.absolute;
  }
  
  /**
   * Set the absolut indicator.
   */
  public void setAbsolute(String absolute)
  {
    this.absolute = absolute;
  }

  /**
   * Return the secure indicator.
   */
  public String getSecure()
  {
    return this.secure;
  }
  
  /**
   * Set the secure indicator.
   */
  public void setSecure(String secure)
  {
    this.secure = secure;
  }

  /**
   * Return the count type.
   */
  public int getCountType()
  {
    return this.countType;
  }
  
  /**
   * Set the count type.
   */
  public void setCountType(int countType)
  {
    this.countType = countType;
  }

  
  // --------------------------------------------------------- Public Methods
   
  /**
   * Process the body of the tag: encode the url
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to not evaluate the body again
   */
  public int doAfterBody()
  throws JspException
  {
    String body = getBodyContent().getString();
      
    if (body != null) 
    {
      StringBuffer completeURL = new StringBuffer(128);
      
      // If the secure indicator is set, prefix the url with the correct domain
      if (secure != null)  
      {
        ServletContext servletContext = pageContext.getServletContext();
        if (secure.equalsIgnoreCase("true")) 
        {
          completeURL.append(servletContext.getInitParameter("SecureDomain"));
        }
        if (secure.equalsIgnoreCase("false")) 
        {
          completeURL.append(servletContext.getInitParameter("NonSecureDomain"));
        }
      }

      // Add the context path, unless the url is absolute
      if (!absolute.equalsIgnoreCase("true"))
      {
        String contextPath = ((HttpServletRequest)(pageContext.getRequest())).getContextPath();
        if (contextPath != null) 
        {
          completeURL.append(contextPath);
        }
      }
      
      // Add the encoded url.
      completeURL.append(((HttpServletResponse)((pageContext.getResponse()))).encodeURL(body));

      // Write contents of the body to the parent's out stream. 
      try
      {
        getPreviousOut().print(completeURL.toString());
      }
      catch(IOException e)
      {
        throw new JspException("IO Error: " + e.getMessage());
      }
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
}