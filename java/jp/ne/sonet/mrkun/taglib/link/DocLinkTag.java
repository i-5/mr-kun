/*
 * DocLinkTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.link;


import java.io.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which creates an HTML link to a Tara document.
 * <p>
 * The parameter docId will be passed to the DisplayDocument
 * servlet.
 * The body of the tag will be used as the text of the link.
 * <p>
 * The url is passed through encodeURL to enable users
 * without cookies.
 * <p>
 *
 * Examples:
 * <pre>
 * &lt;x:docLink docId="123"&gt;Press Release 5/5/2000&lt;/x:docLink&gt;
 * will create &lt;a href="/DisplayDocument?docId=123"&gt;Press Release 5/5/2000&lt;/a&gt;
 * </pre>
 *
 * @author
 */
 
public class DocLinkTag extends BodyTagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The URL for the document id.
   */
   private String docId;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the document id.
   */
  public String getDocId()
  {
    return this.docId;
  }
  
  /**
   * Set the document id.
   */
  public void setDocId(String docId)
  {
    this.docId = docId;
  }

  // --------------------------------------------------------- Public Methods

   
  /**
   * Process the start tag: build the url, encode it and output the HTML link command.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG to tell the JSP page to continue evaluating the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    String url = "/DisplayDocument?docId=" + docId;
    String encodedURL = ((HttpServletResponse)((pageContext.getResponse()))).encodeURL(url);
    try
    {
      pageContext.getOut().write("<a href=\"" + encodedURL + "\">");
    }
    catch(IOException e)
    {
      throw new JspException("IO Error: " + e.getMessage());
    }
    
    return EVAL_BODY_TAG;
  }
  

  /**
   * Process the body of the tag: print it to the current output stream.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to not evaluate the body again
   */
  public int doAfterBody()
  throws JspException
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