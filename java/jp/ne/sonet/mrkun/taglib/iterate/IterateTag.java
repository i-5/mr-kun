/*
 * IterateTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.iterate;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which iterates over the elements of a collection.
 * <p>
 * The "collection" parameter holds the name of the collection 
 * to iterate over. This object must be a subclass of Collection.
 * <p>
 * The "type" parameter specifes the complete class name of the 
 * elements in the collection.
 * <p>
 * The "name" parameter is the name of a variable that holds the 
 * current element in the collection. The variable can be referenced 
 * inside the body of the tag.
 * <p>
 * The optional "counterVariable" parameter is the name of a variable 
 * that holds the number of the current element in the collection.
 * The variable can be referenced inside the body of the tag.
 * <p>
 * The optional "start" parameter is the start value for the 
 * counterVariable. This only affects the counterVariable.
 * The iterate tag always goes through the entire collection.
 * <p>
 *
 * Examples:
 * <pre>
 * &lt;tt:iterate collection="offering_list" type="com.eoffering.server.PublicOfferingData" variable="publicOffering"&gt;
 * &lt;tr&gt;
 * &lt;td&gt;&lt;%= publicOffering.getOfferingType() %&gt;&lt;/td&gt;
 * &lt;/tr&gt;
 * &lt;/tt:iterate&gt;
 * </pre>
 *
 * @author 
 */
 
 
public class IterateTag extends LoopTag
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * The name of the collection to iterate over.
   */
   private String collection;
   
  /**
   * The collection itself.
   */
   private Collection theCollection;
   
  /**
   * An iterator to iterate over the collection.
   */
   private Iterator iterator;
   
  /**
   * The name of the type of each element in the collection.
   */
   private String type;
  
  /**
   * The names of the variable to make available to the body of the tag.
   */
   private String variable;
  
  
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

  /**
   * Return the collection.
   */
  public Collection getTheCollection()
  {
    return this.theCollection;
  }
  
  /**
   * Set the collection.
   */
  public void setTheCollection(Collection theCollection)
  {
    this.theCollection = theCollection;
  }

  /**
   * Return the name of the type of the elements.
   */
  public String getType()
  {
    return this.type;
  }
  
  /**
   * Set the name of the type of the elements.
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * Return the names of the variable.
   */
  public String getVariable()
  {
    return this.variable;
  }
  
  /**
   * Set the name of the variable.
   */
  public void setVariable(String variable)
  {
    this.variable = variable;
  }

  // --------------------------------------------------------- Public Methods

   
  /**
   * Process the start tag: initialize variables.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_BODY_TAG to tell the JSP page to continue evaluating the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    try
    {
      // If theCollection is not set, get the collection from the collection name
      if (this.theCollection == null) {
      	this.theCollection = (Collection)pageContext.getAttribute(this.collection, PageContext.REQUEST_SCOPE);
      }
      
      this.end = this.start + this.theCollection.size() - 1;
      this.iterator = this.theCollection.iterator();
      nextSetAttribute();
    }
    catch (ClassCastException e)
    {
      this.theCollection = null;
      this.end = -1;
      this.iterator = null;
    }
    catch (NullPointerException e)
    {
      this.theCollection = null;
      this.end = -1;
      this.iterator = null;
    }
    
    return super.doStartTag();
  }
  
  /**
   * Process the body of the tag: check it the end of the loop has
   * been reached. If not, evaluate the body again.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY or EVAL_BODY_TAG
   */
  public int doAfterBody()
  throws JspException
  {
    if (this.counter < this.end)
      nextSetAttribute();

    return super.doAfterBody();
  }
  
  public void nextSetAttribute()
  {
    if (this.iterator != null && this.iterator.hasNext()) {
      pageContext.setAttribute(this.variable, this.iterator.next());
    }
  }
}