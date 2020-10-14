
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;

import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class converts supplied kana into one of the requested formats.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class KanaConverter extends Object
{
  final   static String docLocation   = "kanaChars.xml";
  final   static String KATAKANA      = "katakana";
  final   static String HALF_KATAKANA = "halfKatakana";
  final   static String HIRAGANA      = "hiragana";
  final   static String ROMAJI        = "romaji";

  private static Map    kanaChars     = null;
  private static String semaphore     = "semaphore";

  private String sourceString = "";

  public static synchronized void loadKanaTable(InputStream inXML)
  {
    try
    {
      SAXBuilder bldDocLoader = new SAXBuilder(false);
      Document docAll = bldDocLoader.build(inXML);
      Element rootElement = docAll.getRootElement();
      List children = rootElement.getChildren();

      // Build the base languages
      kanaChars = new Hashtable();
      List lstKatakana      = new ArrayList();
      List lstKatakanaHalf  = new ArrayList();
      List lstHiragana      = new ArrayList();
      List lstRomaji        = new ArrayList();

      for (Iterator i = children.iterator(); i.hasNext(); )
      {
        Element elmCurrent = (Element) i.next();
        if (elmCurrent.getAttribute(KATAKANA) == null)
          lstKatakana.add("");
        else
          lstKatakana.add(elmCurrent.getAttribute(KATAKANA).getValue());

        if (elmCurrent.getAttribute(HALF_KATAKANA) == null)
          lstKatakanaHalf.add("");
        else
          lstKatakanaHalf.add(elmCurrent.getAttribute(HALF_KATAKANA).getValue());

        if (elmCurrent.getAttribute(HIRAGANA) == null)
          lstHiragana.add("");
        else
          lstHiragana.add(elmCurrent.getAttribute(HIRAGANA).getValue());

        if (elmCurrent.getAttribute(ROMAJI) == null)
          lstRomaji.add("");
        else
          lstRomaji.add(elmCurrent.getAttribute(ROMAJI).getValue());
      }

      kanaChars.put(KATAKANA, lstKatakana);
      kanaChars.put(HALF_KATAKANA, lstKatakanaHalf);
      kanaChars.put(HIRAGANA, lstHiragana);
      kanaChars.put(ROMAJI, lstRomaji);
    }
    catch (JDOMException errJDOM)
    {
      throw new ApplicationError("Error parsing the characters document", errJDOM);
    }
  }

  /**
   * Constructor
   */
  public KanaConverter(String sourceString)
  {
    synchronized (semaphore)
    {
      if (kanaChars == null)
      try
      {
        InputStream inLoad = this.getClass().getClassLoader().getResourceAsStream(docLocation);
        loadKanaTable(inLoad);
        inLoad.close();
      }
      catch (IOException errIO)
      {
        throw new ApplicationError("Error finding the kana source document.", errIO);
      }
    }
    this.sourceString = sourceString;
  }

  public String getHiragana()
  {
    return getFormat(HIRAGANA);
  }

  public String getKatakana()
  {
    return getFormat(KATAKANA);
  }

  public String getHalfKatakana()
  {
    return getFormat(HALF_KATAKANA);
  }

  public String getRomaji()
  {
    return getFormat(ROMAJI);
  }

  public String getFormat(String formatType)
  {
    List desiredKana = (List) kanaChars.get(formatType);
    StringBuffer result = new StringBuffer(sourceString.length());
    for (int nLoop = 0; nLoop < sourceString.length(); nLoop++)
    {
      char sourceChar = sourceString.charAt(nLoop);
      Integer sourceCharIndex = findChar(sourceChar);
      if (sourceCharIndex != null)
      {
        if (desiredKana.size() <= sourceCharIndex.intValue())
          throw new ApplicationError("Bad kana value");
        else
          result.append((String) desiredKana.get(sourceCharIndex.intValue()));
      }
      else
        result.append(sourceChar);
    }
    return result.toString();
  }

  private Integer findChar(char inputChar)
  {
    return findString("" + inputChar);
  }

  public Integer findString(String inputString)
  {
    if (inputString == null) return null;
    
    // Iterate through the character sets
    for (Iterator i = kanaChars.keySet().iterator(); i.hasNext(); )
    {
      // Iterate through the list of chars break if found
      String key = (String) i.next();
      List charSet = (List) kanaChars.get(key);
      int n = 0;

      for (Iterator j = charSet.iterator(); j.hasNext(); n++)
      {
        String thisKana = (String) j.next();
        if (inputString.equalsIgnoreCase(thisKana))
          return new Integer(n);
      }
    }
    return null;
  }

  public static void main(String argv[]) throws Exception
  {
    Reader inKana = new InputStreamReader(System.in);
    char inData[] = new char[255];
    int nCount = inKana.read(inData);
    KanaConverter k = new KanaConverter(new String(inData, 0, nCount));
    System.out.println("getHiragana: " + k.getHiragana());
    System.out.println("getRomaji: " + k.getRomaji());
    inKana.close();
  }
}

