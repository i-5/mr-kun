
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;

import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class provides a static method that looks up an XML document
 * with system messages in it. The <code>get(String key)</code> method
 * retrieves the appropriate message for the language specified in the
 * root element's language tag.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: SystemMessages.java,v 1.1.2.3 2001/09/12 08:35:49 rick Exp $
 */
public class SystemMessages extends Object
{
  static final String DOCUMENT_LOCATION = "SystemMessages.xml";
  static final String ENGLISH           = "english";
  static final String ATTR_LANGUAGE     = "language";
  static final String ATTR_KEY          = "key";

  private static Map      messages  = null;
  private static String   defaultLanguage  = ENGLISH;

  public static synchronized void loadMessages()
  {
    InputStream inMessages = null;
    try
    {
      // Try to load the document and build a JDOM tree
      inMessages = SystemMessages.class.getClassLoader().getResourceAsStream(DOCUMENT_LOCATION);
      SAXBuilder bldMessageLoader = new SAXBuilder(false);
      Document messageDoc = bldMessageLoader.build(inMessages);

      // Extract the info from the tree into a map. The map has an element for
      // each message key, being itself a map. The nested map is keyed on language,
      // used below to retrieve the same message in different languages.
      Element   rootElement = messageDoc.getRootElement();
      Attribute language    = rootElement.getAttribute(ATTR_LANGUAGE);
      if (language != null)
        defaultLanguage = language.getValue().toLowerCase();
      List messageList = rootElement.getChildren();
      messages = new Hashtable();

      for (Iterator m = messageList.iterator(); m.hasNext(); )
      {
        Element   messageElement  = (Element) m.next();
        Attribute messageKey      = messageElement.getAttribute(ATTR_KEY);
        if (messageKey == null)
          throw new ApplicationError("An element in the system messages document has no key");
        else
        {
          // Iterate through available messages and extract the languages available
          List  languageList        = messageElement.getChildren();
          Map   translatedMessages  = new Hashtable();

          for (Iterator l = languageList.iterator(); l.hasNext(); )
          {
            Element translatedMessageElement  = (Element) l.next();
            String  languageName              = translatedMessageElement.getName();
            String  translatedMessage         = translatedMessageElement.getText();

            // Add to the map of available languages for this message
            translatedMessages.put(languageName.toLowerCase(), translatedMessage);
          }

          // Add this whole message to the map of available messages
          messages.put(messageKey.getValue(), translatedMessages);
        }
      }
    }
    catch (JDOMException errJDOM)
    {
      throw new ApplicationError("Error parsing the System Messages document.", errJDOM);
    }
    finally
    {
      if (inMessages != null)
      try   {inMessages.close();}
      catch (IOException errIO){}
    }
  }

  /**
   * Calls get() with the default language specified in the document's root element.
   * @param key The key of the message requested.
   */
  public static synchronized String get(String key)
  {
    return get(key, null);
  }

  /**
   * Retrieve the message matching the key in the specified language
   * @param key The key of the message requested.
   * @param language The desired language for the message requested.
   */
  public static synchronized String get(String key, String language)
  {
    // Ensure the message objects are loaded
    if (messages == null)
      loadMessages();

    // Use default language if there was none specified
    if (language == null)
      language = defaultLanguage;

    // Check the language, and return the appropriate case
    Map multilingualMessage = (Map) messages.get(key);
    if (multilingualMessage == null)
      throw new ApplicationError("Key not found : " + key);
    else
    {
      String message = (String) multilingualMessage.get(language.toLowerCase());
      if (message == null)
        throw new ApplicationError("Language \"" + language.toLowerCase() +
          "\" not available for key \"" + key + "\"");
      else
        return message;
    }
  }

  /**
   * A testing method for this class.
   */
  public static void main(String argv[])
  {
    System.out.println("Default language  : " + SystemMessages.get("mr51_noBodyFound"));
    System.out.println("English language  : " + SystemMessages.get("mr51_noBodyFound", "english"));
    System.out.println("Japanese language : " + SystemMessages.get("mr51_noBodyFound", "japanese"));
  }
}

 