/*
 * LeftNavTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.navbar;


import java.util.Collection;

import javax.servlet.jsp.*;


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
 
public class LeftNavTag extends TopNavTag
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * An optional sub identifier.
   */
   private String subId;

  /**
   * The name of an optional collection.
   */
   private String collection;
   
  // ------------------------------------------------------------- Properties

  /**
   * Return the sub identifier.
   */
  public String getSubId()
  {
    return this.subId;
  }
  
  /**
   * Set the sub identifier.
   */
  public void setSubId(String subId)
  {
    this.subId = subId;
  }

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

  public int doStartTag()
  throws JspException 
  {
    if (this.getPageId() == null) 
    {
      TopNavTag myParent = (TopNavTag) getParent();
      if (myParent != null) 
      {
        this.setPageId(myParent.getPageId());
      }
    }
    
    if (this.getSubId() != null) 
    {
      pageContext.getRequest().setAttribute("subId", this.getSubId());
    }

    if (this.getCollection() != null) 
    {
      Collection tmpCollection = (Collection) pageContext.getRequest().getAttribute(this.getCollection());
      if (tmpCollection != null) 
      {
        pageContext.getRequest().setAttribute("collection", tmpCollection);
      }
    }

    return super.doStartTag();
  }
  

  // ------------------------------------------------------ Protected Methods

   
  protected String createUrl(String pageId, int type)
  {
    StringBuffer url = new StringBuffer(32);
    url.append("/navbar/leftnav_");

    int pos1 = pageId.indexOf("_");
    if (pos1 == -1) 
    {
      return null;
    }
    int pos2 = pageId.indexOf("-", pos1);
    if (pos2 == -1) 
    {
      pos2 = pageId.indexOf(".", pos1);
      if (pos2 == -1) 
      {
        return null;
      }
    }
    
    try
    {
      url.append(pageId.substring(pos1 + 1, pos2));
    }
    catch (IndexOutOfBoundsException e)
    {
      return null;
    }
    
    if (type == TYPE_START)
    {
      url.append("_start.jsp");
    }
    else 
    {
      url.append("_end.jsp");
    }
    
    return url.toString();
  }
}