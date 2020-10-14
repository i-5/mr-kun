/*
 * NotNullTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;


/**
 * A tag which evaluates an expression and if the expression
 * is not null evaluates the body tag.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:notNull expression="&lt;%= name %&gt;"&gt;
 * Name is not null.
 * &lt;/x:notNull&gt;
 * </pre>
 *
 * @author 
 */
 
public class NotNullTag extends IsNullTag
{

  // --------------------------------------------------------- Public Methods
  
  /**
   * Evaluate the expression.
   *
   * @param String the expression to evaluate
   * @return true if the expression is not null or a String is not empty
   */
  public boolean evaluate()
  {
    return (!super.evaluate());
  }
}