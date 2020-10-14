
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.valueobject.*;
import java.util.Comparator;
import java.util.*;

/**
 * A Comparator implementation used for sorting the Message objects.<br/>
 * Note: The sortOrder in the constructor must be one of:
 * <ul>
 * <li>recieved</li>
 * <li>sent</li>
 * <li>date</li>
 * <li>subject</li>
 * </ul>
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class MessageComparator implements Comparator
{
  private String  sortOrder;
  private boolean isDoctor;
  private boolean ascending;

  /**
   * Constructor - assumes ascending
   */
  public MessageComparator(String sortField, boolean isDoctor) {this(sortField, isDoctor, true);}

  /**
   * Constructor - explicitly sets all params
   */
  public MessageComparator(String sortField, boolean isDoctor, boolean ascending)
  {
    this.sortOrder = sortField;
    this.isDoctor  = isDoctor;
    this.ascending = ascending;
  }

  /**
   * The method that handles comparison - this is what we're really
   * interested in. This method allows sorting by the order specified in the
   * constructor.
   */
  public int compare(Object objOne, Object objTwo)
  {
    if (!(objOne instanceof Message))
      throw new ClassCastException("Object 1 is not a Message object - " +
                      objOne.getClass());
    else if (!(objTwo instanceof Message))
      throw new ClassCastException("Object 2 is not a Message object - " +
                      objTwo.getClass());
    else
    {
      Message msgOne = null;
      Message msgTwo = null;
      if (this.ascending)
      {
        msgOne = (Message) objOne;
        msgTwo = (Message) objTwo;
      }
      else
      {
        msgOne = (Message) objTwo;
        msgTwo = (Message) objOne;
      }


      // Perform the comparison
      if (sortOrder.equals("recieved"))
        return nullCheckCompareMsgType(msgOne, msgTwo);
      else if (sortOrder.equals("sent"))
        return nullCheckCompareMsgType(msgOne, msgTwo);
      else if (sortOrder.equals("date"))
        return nullCheckCompareDate(msgOne.getSentDate(), msgTwo.getSentDate());
      else if (sortOrder.equals("subject"))
        return nullCheckCompareString(msgOne.getTitle(), msgTwo.getTitle());
      else
        throw new IllegalArgumentException("Unknown sort order - " + sortOrder);
    }
  }

  private int nullCheckCompareString(String objOne, String objTwo)
  {
    if ((objOne == null) && (objTwo == null))
      return 0;
    else if (objOne == null)
      return -1;
    else if (objTwo == null)
      return 1;
    else
      return objOne.compareToIgnoreCase(objTwo);
  }

  private int nullCheckCompareInt(Integer objOne, Integer objTwo)
  {
    if ((objOne == null) && (objTwo == null))
      return 0;
    else if (objOne == null)
      return -1;
    else if (objTwo == null)
      return 1;
    else
      return objOne.compareTo(objTwo);
  }

  private int nullCheckCompareDate(Date objOne, Date objTwo)
  {
    if ((objOne == null) && (objTwo == null))
      return 0;
    else if (objOne == null)
      return -1;
    else if (objTwo == null)
      return 1;
    else
      return objOne.compareTo(objTwo);
  }

  private int nullCheckCompareMsgType(Message objOne, Message objTwo)
  {
    if ((objOne == null) && (objTwo == null))
      return 0;
    else if (objOne == null)
      return -1;
    else if (objTwo == null)
      return 1;
    else if ((objOne instanceof EDetail) && (objTwo instanceof EDetail))
      return 0;
    else if ((objOne instanceof Contact) && (objTwo instanceof Contact))
      return 0;

    // now if the sortorder is received, favour edetails for DRs and contacts for MRs
    else if (sortOrder.equals("recieved"))
    {
      if (isDoctor)
        return (objOne instanceof EDetail ? 1 : -1);
      else
        return (objOne instanceof Contact ? 1 : -1);
    }
    // now if the sortorder is received, favour edetails for DRs and contacts for MRs
    else if (sortOrder.equals("sent"))
    {
      if (isDoctor)
        return (objOne instanceof Contact ? 1 : -1);
      else
        return (objOne instanceof EDetail ? 1 : -1);
    }
    else
      throw new IllegalArgumentException("Unknown sort order - why are we calling this method ?");
  }
}

