/*
 * LinkTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.link;


import java.io.*;

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
 
public class LinkTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The URL for the link.
   */
   private String url;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the URL.
   */
  public String getUrl()
  {
    return this.url;
  }
  
  /**
   * Set the URL.
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  // --------------------------------------------------------- Public Methods

   
  /**
   * Process the start tag: encode the url and output an HTML link command.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG to tell the JSP page to continue evaluating the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    if (url != null) {
        try
        {
          String encodedURL = ((HttpServletResponse)((pageContext.getResponse()))).encodeURL(url);
          pageContext.getOut().write("<a href=\"" + encodedURL + "\">");
        }
        catch(IOException e)
        {
          throw new JspException("IO Error: " + e.getMessage());
        }
    }
    
    return EVAL_BODY_TAG;
  }
  

  /**
   * Process the body of the tag: print it to the current output stream.
   * If the body is empty replace it with the URL.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to not evaluate the body again
   */
  public int doAfterBody()
  throws JspException
  {
    String body = getBodyContent().getString();
    
    if (body.trim().length() == 0)
      body = url;

    // Write contents of the body to the parent's out stream. 
    try 
    {
      getPreviousOut().print(body);
    }
    catch(IOException e)
    {
      throw new JspException("IO Error: " + e.getMessage());
    }
    
    return SKIP_BODY;
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
    try
    {
      pageContext.getOut().write("</a>");
    }
    catch(IOException e)
    {
      throw new JspException("IO Error: " + e.getMessage());
    }
    
    return EVAL_PAGE;
  }
}