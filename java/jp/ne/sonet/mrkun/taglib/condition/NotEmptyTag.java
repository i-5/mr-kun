/*
 * NotEmptyTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;


/**
 * A tag which checks the size of a collection and if it is
 * not empty evaluates the body tag.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:notEmpty collection="&lt;%= my_list %&gt;"&gt;
 * my_list is not empty!
 * &lt;/x:notEmpty&gt;
 * </pre>
 *
 * @author 
 */
 
public class NotEmptyTag extends IsEmptyTag
{
  /**
   * Evaluate the collection.
   *
   * @return true if the collection is not empty.
   */
  public boolean evaluate()
  {
    return (!super.evaluate());
  }
}