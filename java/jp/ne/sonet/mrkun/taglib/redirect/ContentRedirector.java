
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.taglib.redirect;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

/**
 * This tag takes the body content after evaluation and writes it to the
 * pageContext attribute specified in the attribute "outputVar" of type
 * java.lang.String.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: ContentRedirector.java,v 1.1.2.1 2001/07/17 03:39:48 rick Exp $
 */
public class ContentRedirector extends BodyTagSupport
{
  private String outputVar;

  /**
   * Used to set the variable which will receive the content
   * @param outputVar The variable into which the body content will go.
   */
  public void setOutputVar(String outputVar)  {this.outputVar = outputVar;}

  /**
   * Evaluated before the tag starts.
   */
  public int doStartTag() throws JspException
  {
    if (outputVar == null)
      throw new JspTagException("There was no output variable specified in the attributes.");
    else return EVAL_BODY_TAG;
  }

  /**
   * Evaluated after the body of the tag has been executed. This contains
   * the code to write the contents to the variable specified in the outputVar.
   */
  public int doAfterBody() throws JspException
  {
    try
    {
      // Set up for the write out
      StringWriter outWriter = new StringWriter();

      // Write out the content to the attribute
      BodyContent bodOutput = getBodyContent();
      bodOutput.writeOut(outWriter);

      // Now update the string in the attribute
      StringBuffer result = (StringBuffer) pageContext.getRequest().getAttribute(outputVar);
      if (result == null)
      {
        result = new StringBuffer();
        pageContext.getRequest().setAttribute(outputVar, result);
      }
      result.append(outWriter.toString());
      return SKIP_BODY;
    }
    catch (IOException errIO)
    {
      throw new JspTagException("Error writing out tag content - " + errIO.toString());
    }
  }

}

 