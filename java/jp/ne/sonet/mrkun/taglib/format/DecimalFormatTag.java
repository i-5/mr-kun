/*
 * DecimalFormatTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.format;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
  
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which displays a specified number formatted according to the specified format.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:decimalFormat number="&lt;%= myNumber %&gt;" pattern="#,##0.00" /&gt;
 * will display 42.00
 * </pre>
 *
 * @author 
 */
 
public class DecimalFormatTag extends TagSupport
{
 
  // ----------------------------------------------------- Instance Variables

  /**
   * The number to format.
   */
  private Number number;

  /**
   * The pattern to pass to NumberFormat.
   */
  private String pattern;

  /**
   * The locale for NumberFormat.
   */
  private Locale locale;
  

  // ------------------------------------------------------------- Properties

  /**
   * Return the pattern.
   */
  public String getPattern()
  {
    return this.pattern;
  }
  
  /**
   * Set the pattern.
   */
  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }

  /**
   * Return the number.
   */
  public Number getNumber()
  {
    return this.number;
  }
  
  /**
   * Set the number.
   */
  public void setNumber(Number number)
  {
    this.number = number;
  }

  /**
   * Return the locale.
   */
  public Locale getLocale()
  {
    return this.locale;
  }
  
  /**
   * Set the locale.
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  
  // --------------------------------------------------------- Public Methods

  /**
   * Process the start tag: nothing to do here.
   *
   * @exception JspException if a JSP exception has occurred
   * @return SKIP_BODY to tell the JSP page to skip the body of the tag
   */
  public int doStartTag()
  throws JspException 
  {
    return SKIP_BODY;
  }
  

  /**
   * Process the end tag: output the number in the specified format.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_PAGE to tell the JSP page to continue evaluating page
   */
  public int doEndTag() 
  throws JspException 
  {
    if (number != null && pattern != null) {
        DecimalFormat decimalFormat = null;
        if (locale == null)
          decimalFormat = new DecimalFormat(pattern);
        else
          decimalFormat = new DecimalFormat(pattern, new DecimalFormatSymbols(locale));

        try
        {
          pageContext.getOut().write(decimalFormat.format(number));
        }
        catch(IOException e)
        {
          throw new JspException("IO Error: " + e.getMessage());
        }
    }
    
    return EVAL_PAGE;
  }
}