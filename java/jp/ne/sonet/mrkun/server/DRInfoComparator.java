
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.valueobject.*;
import java.util.Comparator;

/**
 * A Comparator implementation used for sorting the DRInformation objects.<br/>
 * Note: The sortOrder in the constructor must be one of:
 * <ul>
 * <li>name</li>
 * <li>hospital</li>
 * <li>specialty</li>
 * <li>importance</li>
 * <li>action</li>
 * <li>unread</li>
 * <li>unrecieved</li>
 * </ul>
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: DRInfoComparator.java,v 1.1.2.7 2001/08/21 09:49:52 rick Exp $
 */
public class DRInfoComparator implements Comparator
{
  private String  sortOrder;
  private boolean ascending;

  /**
   * Constructor - assumes ascending
   */
  public DRInfoComparator(String sortField) {this(sortField, true);}

  /**
   * Constructor - explicitly sets all params
   */
  public DRInfoComparator(String sortField, boolean ascending)
  {
    this.sortOrder = sortField;
    this.ascending = ascending;
  }

  /**
   * The method that handles comparison - this is what we're really
   * interested in. This method allows sorting by the order specified in the
   * constructor.
   */
  public int compare(Object objOne, Object objTwo)
  {
    if (!(objOne instanceof DRInformation))
      throw new ClassCastException("Object 1 is not a DRInformation object - " +
                      objOne.getClass());
    else if (!(objTwo instanceof DRInformation))
      throw new ClassCastException("Object 2 is not a DRInformation object - " +
                      objTwo.getClass());
    else
    {
      DRInformation driOne = null;
      DRInformation driTwo = null;
      if (this.ascending)
      {
        driOne = (DRInformation) objOne;
        driTwo = (DRInformation) objTwo;
      }
      else
      {
        driOne = (DRInformation) objTwo;
        driTwo = (DRInformation) objOne;
      }


      // Perform the comparison
      if (sortOrder.equals("name"))
        return nullCheckCompareString(driOne.getName(), driTwo.getName());
      else if (sortOrder.equals("hospital"))
        return nullCheckCompareString(driOne.getHospital(), driTwo.getHospital());
      else if (sortOrder.equals("specialty"))
        return nullCheckCompareString(driOne.getDivision(), driTwo.getDivision());
      else if (sortOrder.equals("importance"))
        return nullCheckCompareString(driOne.getImportance().getImportanceId(), driTwo.getImportance().getImportanceId());
      else if (sortOrder.equals("action"))
        return nullCheckCompareInt(driOne.getActionValue(), driTwo.getActionValue());
      else if (sortOrder.equals("unread"))
        return nullCheckCompareInt(driOne.getReceivedMessage(), driTwo.getReceivedMessage());
      else if (sortOrder.equals("unrecieved"))
        return nullCheckCompareInt(driOne.getUnreadSentMessage(), driTwo.getUnreadSentMessage());
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
}

