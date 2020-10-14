
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import jp.ne.sonet.mrkun.framework.exception.ApplicationError;

/**
 * A helper class for displaying strings in javascript.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: StringUtils.java,v 1.1.2.20 2001/09/20 09:14:01 rick Exp $
 */
public class StringUtils extends Object
{
  /**
   * This method cleans up the input for display within a Javascript page.
   */
  public static String cleanForJS(String  input,
                                  boolean keepLinefeeds,
                                  boolean insertBR,
                                  boolean entityQuotes)
  {
  	//System.out.println("StringUtils.cleanForJS: cleaning:"+input);
    if (input == null) return null;
    else if (input.length() == 0)
      return "";
    else if (input.indexOf("\r\n") != -1)
    {
      int nCharPos = input.indexOf("\r\n");
      if (nCharPos == 0)
        return cleanForJS(input.substring(2), keepLinefeeds, insertBR, entityQuotes);
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + (insertBR?"<br>":(keepLinefeeds?"\\r\\n":"")) +
             cleanForJS(input.substring(nCharPos + 2), keepLinefeeds, insertBR, entityQuotes);
    }
    else if (input.indexOf('\r') != -1)
    {
      int nCharPos = input.indexOf('\r');
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + (insertBR?"<br>":(keepLinefeeds?"\\r":"")) +
             cleanForJS(input.substring(nCharPos + 1), keepLinefeeds, insertBR, entityQuotes);
    }
    else if (input.indexOf('\n') != -1)
    {
      int nCharPos = input.indexOf('\n');
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + (insertBR?"<br>":(keepLinefeeds?"\\n":"")) +
             cleanForJS(input.substring(nCharPos + 1), keepLinefeeds, insertBR, entityQuotes);
    }
    else if (input.indexOf('\'') != -1)
    {
      int nCharPos = input.indexOf('\'');
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + (entityQuotes?"&#39;":"\\'") +
             cleanForJS(input.substring(nCharPos + 1), keepLinefeeds, insertBR, entityQuotes);
    }
    else if (input.indexOf('"') != -1)
    {
      int nCharPos = input.indexOf('"');
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + (entityQuotes?"&quot;":"\\\"") +
             cleanForJS(input.substring(nCharPos + 1), keepLinefeeds, insertBR, entityQuotes);
    }
    else if (input.indexOf('\\') != -1)
    {
      int nCharPos = input.indexOf('\\');
      return cleanForJS(input.substring(0, nCharPos), keepLinefeeds, insertBR, entityQuotes) + "\\\\" +
             cleanForJS(input.substring(nCharPos + 1), keepLinefeeds, insertBR, entityQuotes);
    }
    else
      return convertJP(input);
  }

  public static String cleanForJS(String input)
  {
    return cleanForJS(input, true, false);
  }

  public static String cleanForJS(String input, boolean keepLinefeeds, boolean insertBR)
  {
    return cleanForJS(input, keepLinefeeds, insertBR, false);
  }

  public static void main(String argv[]) throws Exception
  {
    String test = "not found";

    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection("jdbc:oracle:oci8:@medipro.linux", "mr", "mr");
    PreparedStatement stm = conn.prepareStatement("SELECT message_honbun FROM message_body WHERE message_id in (select message_id from message_header where message_header_id = ?)");
    stm.setString(1, "200109121653173652");
    ResultSet rst = stm.executeQuery();
    if (rst.next())
      test = rst.getString(1);
    rst.close();
    stm.close();
    conn.close();

    System.out.println("Original message: @" + test + "@");
    System.out.println("Line wraps preserved message: @" + cleanForJS(test, true, false) + "@");
    System.out.println("Line wraps removed message: @" + cleanForJS(test, false, false) + "@");
    System.out.println("Line wraps removed BR inserted message: @" + cleanForJS(test, false, true) + "@");
    System.out.println("Another test:" + SystemMessages.get("dr_OpenedNewEDetailBody", "japanese"));
    System.out.println("Another test:" + StringUtils.cleanForJS(SystemMessages.get("dr_OpenedNewEDetailBody", "japanese"), false, true));
    System.out.println("Another test:" + StringUtils.cleanForJS("One mor'\"'e ye\nah \" sdfkljhsdfgsdf", false, true));
    System.out.println("Another test:" + StringUtils.cleanForJS("One mor'\"'e ye\nah \" sdfkljhsdfgsdf", false, true,true));
  }

  public static String globalReplace(String input, String find, String replace)
  {
    // If found, substitute find for replace and call recursively on surrounding text
    int position = input.indexOf(find);
    if (position != -1)
      return  globalReplace(input.substring(0, position), find, replace) +
              replace +
              globalReplace(input.substring(position + find.length()), find, replace);
    else
      return input;
  }

  public static String globalReplace(String input, Map replacements)
  {
    // Loop through the replacement list - this could be done much more efficiently
    // using stringbuffers
    Collection findKeys = replacements.keySet();
    String result = input;
    for (Iterator i = findKeys.iterator(); i.hasNext(); )
    {
      String find    = (String) i.next();
      String replace = (String) replacements.get(find);
      result = globalReplace(result, find, replace);
    }
    return result;
  }

  /**
   * Decodes a parameter from the request using Japanese SJIS encoding
   * @param req The request object to extract the param from
   * @param parameterName The name of the parameter to be extracted
   */
  public static String getParameter(HttpServletRequest req, String parameterName)
  {
    String value = req.getParameter(parameterName);
    if (value == null)
    {
	    return null;
	  }
  	try
    {
	    value = new String(value.getBytes("8859_1"), "SJIS");
	  }
    catch (UnsupportedEncodingException ex)
    {
	    throw new ApplicationError("Error decoding parameter", ex);
	  }
  	return value;
  }

  private static final char[][] table = { {0xff0d, 0x2212}, {0xff5e, 0x301c}, };

  /**
   * Removes any dodgy characters from a string for display in japanese
   */
  public static String convertJP(String str)
  {
  	if (str == null)
    {
	    return str;
	  }

  	char[] array = str.toCharArray();
	  StringBuffer result = new StringBuffer("");

  	for (int i = 0; i < array.length; i++)
    {
	    for (int j = 0; j < table.length; j++)
      {
		    if (array[i] == table[j][0])
        {
		      array[i] = table[j][1];
		    }
	    }
	    result.append(array[i]);
	  }

  	return result.toString();
  }
}

