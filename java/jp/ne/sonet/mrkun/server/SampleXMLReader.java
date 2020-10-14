
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import org.jdom.*;
import org.jdom.input.*;
import java.io.*;
import jp.ne.sonet.mrkun.framework.exception.*;


/**
 * A Class class.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class SampleXMLReader extends Object
{

  /**
   * Loads the XML parameter descriptor into a JDOM object
   */
  protected Document loadParameterObject()
  {
    String location = "pages.xml";
    InputStream inParams = this.getClass().getClassLoader().getResourceAsStream(location);
    if (inParams == null)
      throw new ApplicationError("Parameter file not found");
    else
    try
    {
      SAXBuilder bldLoader = new SAXBuilder(false);
      Document result = bldLoader.build(inParams);
      return result;
    }
    catch (JDOMException errJDOM)
    {
      throw new ApplicationError("Error parsing the parameters document", errJDOM);
    }
    finally
    {
      try {inParams.close();}
      catch (IOException errIO){}
    }
  }

  public static void main(String argv [])
  {
    SampleXMLReader s = new SampleXMLReader();
    Document d = s.loadParameterObject();
    System.out.println("The number of pages loaded is: " +
          d.getRootElement().getChildren().size());
  }
}

 