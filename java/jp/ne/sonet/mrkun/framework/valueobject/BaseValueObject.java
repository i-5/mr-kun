// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.framework.valueobject;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.jdom.*;
import org.jdom.output.*;
import java.util.*;

/**
 * <P>BaseValueObject - This class serves as the parent for classes that modeled as entity class. 
 * 						The main job of this class is to define common database timestamp fields (createdBy, creatDate, 
 *						modifiedBy, modifiedDate, etc) for its subclasses.  The entity classes act as the place holder for  
 * 						database table data.  In other words, they are the object-level data entity and encapsulate all 
 *						essential knownledge for the business entity in the application.   
 * 
 * @author Damon Lok
 * @version 2
 * @since JDK1.3
 */
public class BaseValueObject implements Serializable, Cloneable
{
    /* The id of the user who initially created (inserted into the database) this data bean */
    private Integer createdBy;
    
    /* The date and time this bean was initially created (inserted into the database) */
    private Timestamp createdDate;
    
    /* The id of the user who last modified (updated in the database) this data bean */
    private Integer modifiedBy;
    
    /* The date and time this bean was last modifed (updated in the database) */
    private Timestamp modifiedDate;

    
	/**
	 * Default constructor
	 */
	public BaseValueObject ()
	{
		super();
	}

    /**
     * Get the created by user id.
     *
     * @return  The user id
     */
	public Integer getCreatedBy() {
	    return this.createdBy;
	}

    /**
     * Set the created by user id.
     *
     * @param   createdBy   The user id
     */
	public void setCreatedBy(Integer createdBy) {
	    this.createdBy = createdBy;
	}
	
    /**
     * Get the timestamp for when the record was created.
     *
     * @return  The timestamp
     */
	public Timestamp getCreatedDate() {
	    return this.createdDate;
	}

    /**
     * Set the timestamp for when the record was created.
     *
     * @param   createdDate  The timestamp
     */
	public void setCreatedDate(Timestamp createdDate) {
	    this.createdDate = createdDate;
	}
	
    /**
     * Set the timestamp for when the record was created.
     *
     * @param   createdDateStr  A string representing the timestamp 
     *                          as a millisecond value.
     */
	public void setCreatedDate(String createdDateStr) {
        this.createdDate = this.stringToTimestamp(createdDateStr);
	}
	
    /**
     * Get the modified by user id.
     *
     * @return  The user id
     */
	public Integer getModifiedBy() {
	    return this.modifiedBy;
	}

    /**
     * Set the modified by user id.
     *
     * @param   modifiedBy  The user id
     */
	public void setModifiedBy(Integer modifiedBy) {
	    this.modifiedBy = modifiedBy;
	}
	
    /**
     * Get the timestamp for when the record was modified.
     *
     * @return  The timestamp
     */
	public Timestamp getModifiedDate() {
	    return this.modifiedDate;
	}

    /**
     * Set the timestamp for when the record was modified.
     *
     * @param   modifiedDate    The timestamp
     */
	public void setModifiedDate(Timestamp modifiedDate) {
	    this.modifiedDate = modifiedDate;
	}
    
    /**
     * Set the timestamp for when the record was modified.
     *
     * @param   createdDateStr  A string representing the timestamp 
     *                          as a millisecond value.
     */
	public void setModifiedDate(String modifiedDateStr) {
        this.modifiedDate = this.stringToTimestamp(modifiedDateStr);
	}
    
	/**
	 * Converts a string, holding the millisecond value, to a Timestamp.
     * 
     * @param   dateStr     A string with the date and time represented as a 
     *                      millisecond value.
     * @return  A Timestamp
	 */
    public static Timestamp stringToTimestamp(String dateStr) {
        Timestamp timestamp = null;
        try {
            timestamp = new Timestamp(Long.parseLong(dateStr));
        } catch (NumberFormatException e) {
            // If the string is invalid, return null
        }
        return timestamp;
    }

	/**
	 * <P>Creates a copy of the current object; this is necessary
     * to avoid modifications to the remote object, which WebLogic allows
     * (pass by reference).
     * 
     * @return A copy/clone of this object
	 */
	public BaseValueObject getCopy ()
	{
        try {
    		return (BaseValueObject)this.clone ();
        } catch (CloneNotSupportedException exc) {
			//FIXME: put the handle here.
            // This will never happen, unless an object
            // other than "this" is cloned
        }
        return null;
	}
    
    //TODO: add some type of modified state check?

  /**
   * Serializes the contents of the object to an XML element. It is intended
   * that methods subclassing BaseValueObject will override this method and
   * add the child elements specific to their own properties.
   *
   * @param The name of the element under which the content will be serialized.
   */
  public Element toXML(String elementName)
  {
    Element masterElement = new Element(elementName);
    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    if (getCreatedBy() != null)
      masterElement.addContent(new Element("createdBy").setText(getCreatedBy().toString()));
    if (getCreatedDate() != null)
      masterElement.addContent(new Element("createdDate").setText(sdfDates.format(getCreatedDate())));
    if (getModifiedBy() != null)
      masterElement.addContent(new Element("modifiedBy").setText(getModifiedBy().toString()));
    if (getModifiedDate() != null)
      masterElement.addContent(new Element("modifiedDate").setText(sdfDates.format(getModifiedDate())));
    return masterElement;
  }

  /**
   * Serializes an entire collection to an XML tree. Important - for subclasses
   * of BaseValueObject, the toXML method is called on each element, otherwise
   * toString is called.
   *
   * @param elementName The xml tag name given to the parent element.
   * @param coll The collection to be serialized.
   * @param childNames The xml tag name for each child element.
   */
  public Element collectionToXML(String elementName, Collection coll, String childNames)
  {
    Element masterElement = new Element(elementName);
    boolean isList = (coll instanceof List);
    int counter = 0;
    for (Iterator i = coll.iterator(); i. hasNext(); counter++)
    {
      Object item = i.next();

      // Serialize this child object
      if (item instanceof BaseValueObject)
      {
        BaseValueObject vo = (BaseValueObject) item;
        Element child = vo.toXML(childNames);
        if (isList)
          child.addAttribute("listIndex", counter + "");
        masterElement.addContent(child);
      }
      else
      {
        Element child = new Element(childNames).setText(item.toString());
        if (isList)
          child.addAttribute("listIndex", counter + "");
        masterElement.addContent(child);
      }
    }
    return masterElement;
  }

  /**
   * Serializes an entire map to an XML tree. Important - for subclasses
   * of BaseValueObject, the toXML method is called on each element, otherwise
   * toString is called. The key is added added as an attribute to each child
   * element.
   *
   * @param elementName The xml tag name given to the parent element.
   * @param map The collection to be serialized.
   * @param childNames The xml tag name for each child element.
   */
  public Element mapToXML(String elementName, Map map, String childNames)
  {
    Element masterElement = new Element(elementName);
    for (Iterator i = map.keySet().iterator(); i. hasNext(); )
    {
      Object key    = i.next();
      Object value  = map.get(key);

      // Serialize this child object
      if (value instanceof BaseValueObject)
      {
        BaseValueObject vo = (BaseValueObject) value;
        Element child = vo.toXML(childNames);
        masterElement.addContent(child.addAttribute("key", key.toString()));
      }
      else
        masterElement.addContent(new Element(childNames)
                                    .addAttribute("key", key.toString())
                                    .setText(value.toString()));
    }
    return masterElement;
  }

  /**
   * Serializing to XML
   */
  private void writeObject(java.io.ObjectOutputStream out)
     throws IOException
  {
    String className = this.getClass().getName();

    XMLOutputter outWriter = new XMLOutputter("  ", true);
    PrintWriter outPW = new PrintWriter(out);
    outWriter.output(toXML(className), outPW);
    outPW.flush();
  }
}
