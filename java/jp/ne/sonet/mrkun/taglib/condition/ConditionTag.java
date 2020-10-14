/*
 * ConditionTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.condition;

import java.io.*;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which evaluates an expression and compares it to a
 * specified value. If the values are equal the body tag is
 * evaluated.
 * <p>
 * Both the expresson and the value are converted to strings
 * before they are compared.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:compare expression="&lt;%= name %&gt;" value="Boy"&gt;
 * Hi Boy!
 * &lt;/x:isNull&gt;
 * </pre>
 *
 * @author 
 */
 
public class ConditionTag extends IsNullTag
{
 
  // ----------------------------------------------------- Instance Variables
  
	/**
	 * The value to compare to.
	 */
	private Object value;
	//private String value;

	/**
	 * True if case should be ignored in the comparison.
	 */
	private boolean ignoreCase = false;

	/**
	 * If true, the condition should be negated and the body is evaluated
	 * if the expression and the value are not equal.
	 */
	private boolean negate = false;
  

  	// ------------------------------------------------------------- Properties

	/**
	 * Return the value.
	 */
	public Object getValue()
	{
		return this.value;
	}

	/**
	 * Set the value.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Set the value as an object.
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	/**
	 * Return case sensitivity.
	 */
	public boolean getIgnoreCase()
	{
		return this.ignoreCase;
	}
  
	/**
	 * Set case sensitivity.
	 */
	public void setIgnoreCase(boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Return negation indicator.
	 */
	public boolean getNegate()
	{
		return this.negate;
	}
  
	/**
	 * Set negation indicator.
	 */
	public void setNegate(boolean negate)
	{
		this.negate = negate;
	}

  
  	// --------------------------------------------------------- Public Methods
   
	/**
	 * Evaluate the expression.
	 *
	 * @param Object the expression to compare to the attribute value.
	 * @return true if the expression and the value are equal.
	 */
	public boolean evaluate()
	{
		boolean retVal = false;

		if (expression != null && value != null){
			try {
				if (ignoreCase){
					if (expression.toString().equalsIgnoreCase(value.toString()))
						retVal = true;
				}else{
					if (expression.toString().equals(value.toString()))
						retVal = true;
				}
			}catch (Exception e) {
				retVal = false;
			}
		}

		// Special case
		if (expression == null && value == null)
			retVal = true;

		// If negate is true, negate the comparison
		if (negate)
			retVal = !retVal;

		return retVal;
	}
}