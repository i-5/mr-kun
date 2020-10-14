package jp.ne.sonet.mrkun.server;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

/**
 * This class is used to encapsulate the decoding of HTTP POST requests
 * using the "multipart/form-data" encoding type.
 * <br/>
 * <br/>
 * It uses Javamail for Mime libraries and the JavaBeans Activation
 * Framework (JAF), so make sure you have activation.jar and mail.jar
 * in the class path before using this class.
 * <br/>
 * <br/>
 * An example: To extract a binary file from an HTTP POST request (where
 * req is the HttpServletRequest object), as well as a text field, use:
 * <br/>
 * <pre>
 * MultipartHandler mph = new MultipartHandler(req);
 * String textField = (String) mph.getParameter("textFieldNameInForm");
 * InputStream binaryFile = (ByteArrayInputStream) mph.getParameter("binaryFieldNameInForm");
 * // ... and read binary file from the inputstream.
 * </pre>
 * Note: The servlet input stream is empty after the contructor executes.
 * This prevents the use of this class on the same request twice.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: MultipartHandler.java,v 1.1.2.6 2001/12/12 09:15:51 rick Exp $
 */
public class MultipartHandler implements DataSource
{
  private byte            mimeByteArray[] = null;
  private InputStream     mimeInputStream = null;
  private String          contentType     = "";
  private Map             parameters      = null;
  private Map             rawParameters   = null;
  private Map             mimeTypes       = null;
  private Map             fileNames       = null;
  private int             parameterCount  = 0;

  /**
   * Constructor - this uses a servlet request, validating it to make
   * sure it's a multipart/form-data request, then reads the
   * ServletInputStream, storing the results after Mime decoding in
   * a member array. Use getParameter etc to retrieve the contents.
   * @param request The Servlet's request object.
   */
  public MultipartHandler(ServletRequest request) throws IOException
  {
    /*
     * If the content type includes the boundary,
     * include it in the inputstream
     */
    this.contentType = request.getContentType();
    if (this.contentType.toLowerCase().indexOf("multipart/form-data") == -1)
      throw new IOException("The MIME Content-Type of the Request must be " +
                            "\"multipart/form-data\", not \"" +
                            this.contentType + "\".");
    else
    {
      ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
      InputStream inputServlet = request.getInputStream();
      int readValue = inputServlet.read();
      while (readValue != -1)
      {
        byteArray.write(readValue);
        readValue = inputServlet.read();
      }
      inputServlet.close();
      this.mimeByteArray = byteArray.toByteArray();
      byteArray.close();
    }

    try
    {
      MimeMultipart parts = new MimeMultipart(this);
      this.parameterCount = parts.getCount();
      this.parameters     = new Hashtable();
      this.rawParameters  = new Hashtable();
      this.mimeTypes      = new Hashtable();
      this.fileNames      = new Hashtable();
      for (int loopCount = 0; loopCount < this.parameterCount; loopCount++)
      {
        MimeBodyPart current = (MimeBodyPart) parts.getBodyPart(loopCount);
        String headers = current.getHeader("Content-Disposition", "; ");
        // Get the name field
        if (headers.indexOf(" name=\"") == -1)
          throw new MessagingException("No \"name\" header found in Content-Disposition field.");
        else
        {
          // Get the name field
          String nameField = headers.substring(headers.indexOf(" name=\"") + 7);
          nameField = nameField.substring(0, nameField.indexOf("\""));

          //this.parameters.put(nameField, current.getContent());
          addParameter(nameField, current.getContent());

          InputStream inRaw = current.getInputStream();
          byte stash[] = new byte[inRaw.available()];
          inRaw.read(stash);
          inRaw.close();
          this.rawParameters.put(nameField, new ByteArray(stash));

          if (current.getContentType() == null)
            this.mimeTypes.put(nameField, "text/plain");
          else
            this.mimeTypes.put(nameField, current.getContentType());

          if (headers.indexOf(" filename=\"") != -1)
          {
            String fileName = headers.substring(headers.indexOf(" filename=\"") + 11);
            fileName = fileName.substring(0, fileName.indexOf("\""));
            this.fileNames.put(nameField, fileName);
          }
        }
      }
      parts = null;
    }
    catch (MessagingException errMime)
    {
      throw new IOException(errMime.toString());
    }
  }

  /**
   * Required for implementation of the DataSource interface.
   * Internal use only.
   */
  public String getName()
    {return "MultipartHandler";}

  /**
   * Required for implementation of the DataSource interface.
   * Internal use only.
   */
  public String getContentType()
    {return contentType;}

  /**
   * Required for implementation of the DataSource interface.
   * Internal use only.
   */
  public java.io.InputStream getInputStream() throws java.io.IOException
    {return new ByteArrayInputStream(this.mimeByteArray);}

  /**
   * Required for implementation of the DataSource interface.
   * Internal use only.
   */
  public java.io.OutputStream getOutputStream() throws java.io.IOException
    {throw new IOException("This is a read-only datasource.");}

  /**
   * The value of the parameter requested (as an Object). The type
   * of this Object is determined by the MimeType of the supplied
   * value, but if unknown (ie application/octet-stream) will be
   * a ByteArrayInputStream. Plain text fields will be returned as
   * Strings.
   * @param name The parameter you wish to retrieve.
   * @return An Object representation of the supplied parameter.
   */
  public Object getParameter(String name)
    {return this.parameters.get(name);}

  /**
   * The byte array version of the parameter requested (as an Object).
   * This always returns a byte array, ignoring the mime type of the
   * submitted object.
   * @param name The parameter you wish to retrieve.
   * @return A byte array representation of the supplied parameter.
   */
  public byte[] getRawParameter(String name)
  {
    if (this.rawParameters.get(name) != null)
      return ((ByteArray) this.rawParameters.get(name)).getBytes();
    else
      return null;
  }

  /**
   * Get the MimeType of a particular parameter.
   * @param name The parameter you wish to find the Mime type of.
   * @return The Mime type for the requested parameter (as specified
   * in the Mime header during the post).
   */
  public String getContentType(String name)
    {return (String) this.mimeTypes.get(name);}

  /**
   * The local (client) name of the file submitted if this parameter was
   * a file.
   * @param name The parameter you wish to find the file name for.
   * @return The local name for the requested parameter (as specified
   * in the Mime header during the post).
   */
  public String getFileName(String name)
    {return (String) this.fileNames.get(name);}

  /**
   * The no of parameters supplied in the HTTP POST request.
   */
  public int getParameterCount()
    {return this.parameterCount;}

  /**
   * Retrieve a Map of the parameters supplied in the HTTP POST request.
   */
  public Map getParameters()
    {return this.parameters;}

  /**
   * Retrieve a Map of the raw bytes of the parameters supplied in the HTTP POST request.
   */
  public Map getRawParameters()
    {return this.rawParameters;}

  /**
   * Adds a parameter to the array - if a parameter exists with this name
   * create an Object[]. If the object is not a string, it will have a toString()
   * performed on it - avoid using the MultipartHandler with files of the same
   * parameter name.
   */
  private void addParameter(String name, Object content)
  {
    if (this.parameters.get(name) != null)
    {
      // Check if this already has a string array in it
      Object curr = this.parameters.get(name);
      if (curr instanceof Object[])
      {
        // Build a list then add the new object. Convert back to array
        List currList = Arrays.asList((Object []) curr);
        currList.add(content);
        this.parameters.put(name, currList.toArray());
      }
      else
      {
        // Build the array
        Object newParamVal[] = new Object[2];
        newParamVal[0] = curr;
        newParamVal[1] = content;
        this.parameters.put(name, newParamVal);
      }
    }
    else
      this.parameters.put(name, content);
  }
}

 