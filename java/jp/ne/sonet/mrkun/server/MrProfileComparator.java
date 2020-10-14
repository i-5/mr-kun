
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.valueobject.*;
import java.util.Comparator;
import java.util.*;

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
 * @version $Id: MrProfileComparator.java,v 1.1.2.5 2001/09/14 07:11:20 rick Exp $
 */
public class MrProfileComparator implements Comparator
{
  private String  sortOrder;
  private boolean ascending;

  /**
   * Constructor - assumes ascending
   */
  public MrProfileComparator(String sortField) {this(sortField, true);}

  /**
   * Constructor - explicitly sets all params
   */
  public MrProfileComparator(String sortField, boolean ascending)
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
    if (!(objOne instanceof MrProfile))
      throw new ClassCastException("Object 1 is not a MrProfile object - " +
                      objOne.getClass());
    else if (!(objTwo instanceof MrProfile))
      throw new ClassCastException("Object 2 is not a MrProfile object - " +
                      objTwo.getClass());
    else
    {
      MrProfile mriOne = null;
      MrProfile mriTwo = null;
      if (this.ascending)
      {
        mriOne = (MrProfile) objOne;
        mriTwo = (MrProfile) objTwo;
      }
      else
      {
        mriOne = (MrProfile) objTwo;
        mriTwo = (MrProfile) objOne;
      }


      // Perform the comparison
      if (sortOrder.equals("name"))
        return nullCheckCompareString(mriOne.getKanaName(), mriTwo.getKanaName());
      else if (sortOrder.equals("company"))
        return nullCheckCompareString(mriOne.getCompany(), mriTwo.getCompany());
      else if (sortOrder.equals("date"))
        return nullCheckCompareDate(mriOne.getUnreadDate(), mriTwo.getUnreadDate());
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
}

