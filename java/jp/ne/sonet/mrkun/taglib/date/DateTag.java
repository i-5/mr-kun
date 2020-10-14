/*
 * DateTag.java
 *
 */

package jp.ne.sonet.mrkun.taglib.date;


import java.io.*;
import java.text.*;
import java.util.Date;
import java.util.Locale;
  
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;


/**
 * A tag which displays the current system date
 * formatted according to the specified format.
 * <p>
 *
 * Example:
 * <pre>
 * &lt;x:date pattern="yyyy.MM.dd HH:mm:ss" /&gt;
 * will display 2000.05.05 15:59:42 if you ran it now.
 * </pre>
 *
 * @author
 */
 
public class DateTag extends TagSupport
{
 
  // ----------------------------------------------------- Instance Variables
  
  /**
   * The pattern to pass to SimpleDateFormat.
   */
  private String pattern;

  /**
   * The locale for SimpleDateFormat.
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
   * Process the end tag: output the current date in the specified format.
   *
   * @exception JspException if a JSP exception has occurred
   * @return EVAL_PAGE to tell the JSP page to continue evaluating page
   */
  public int doEndTag() 
  throws JspException 
  {
    if (pattern != null) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = null;
        if (locale == null)
          simpleDateFormat = new SimpleDateFormat(pattern);
        else
          simpleDateFormat = new SimpleDateFormat(pattern, locale);
    
        try
        {
          pageContext.getOut().write(simpleDateFormat.format(date));
        }
        catch(IOException e)
        {
          throw new JspException("IO Error: " + e.getMessage());
        }
    }
    
    return EVAL_PAGE;
  }
}