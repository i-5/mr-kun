/*
 * SubNavTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.navbar;


import java.util.Collection;

import javax.servlet.jsp.*;


/**
 * A tag for sub navigation includes.
 *
 * @author 
 */
 
public class SubNavTag extends LeftNavTag
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * An optional sub object.
   */
   private Object subObject;

  /**
   * The name of the jsp file to include as the sub navigation.
   * The name should not include the /navbar/ prefix or the .jsp
   * suffix.
   */
   private String include;

  // ------------------------------------------------------------- Properties

  /**
   * Return the sub object.
   */
  public Object getSubObject()
  {
    return this.subObject;
  }
  
  /**
   * Set the sub object.
   */
  public void setSubObject(Object subObject)
  {
    this.subObject = subObject;
  }

  /**
   * Return the name of the file to include.
   */
  public String getInclude()
  {
    return this.include;
  }
  
  /**
   * Set the name of the file to include.
   */
  public void setInclude(String include)
  {
    this.include = include;
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
    
    if (this.getSubObject() != null) 
    {
      pageContext.getRequest().setAttribute("subObject", this.getSubObject());
    }

    return super.doStartTag();
  }
  

  // ------------------------------------------------------ Protected Methods

   
  protected String createUrl(String pageId, int type)
  {
    StringBuffer url = new StringBuffer(32);
    url.append("/navbar/");
    url.append(include);

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