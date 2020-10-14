
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import jp.ne.sonet.mrkun.valueobject.*;
import java.util.Comparator;
import java.util.*;

/**
 * A Comparator implementation used for sorting the MRLevelStatistics objects.<br/>
 * Note: The sortOrder in the constructor must be one of:
 * <ul>
 * <li>active</li>
 * <li>read_edetails</li>
 * <li>sent_edetails</li>
 * <li>received_contacts</li>
 * </ul>
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class StatisticsRankingComparator implements Comparator
{
  private String  sortOrder;
  private boolean ascending;

  /**
   * Constructor - assumes ascending
   */
  public StatisticsRankingComparator(String sortField)
  {
    this(sortField, true);
  }

  /**
   * Constructor - explicitly sets all params
   */
  public StatisticsRankingComparator(String sortField, boolean ascending)
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
    if (!(objOne instanceof MRLevelStatistics))
      throw new ClassCastException("Object 1 is not an MRLevelStatistics object - " +
                      objOne.getClass());
    else if (!(objTwo instanceof MRLevelStatistics))
      throw new ClassCastException("Object 2 is not an MRLevelStatistics object - " +
                      objTwo.getClass());
    else
    {
      MRLevelStatistics mrsOne = null;
      MRLevelStatistics mrsTwo = null;
      if (this.ascending)
      {
        mrsOne = (MRLevelStatistics) objOne;
        mrsTwo = (MRLevelStatistics) objTwo;
      }
      else
      {
        mrsOne = (MRLevelStatistics) objTwo;
        mrsTwo = (MRLevelStatistics) objOne;
      }

      /*
       * Perform the comparison
       */

      // Option 1: active line comparison
      if (sortOrder.equals("active"))
        return new Integer(mrsOne.getActiveDRCount())
                .compareTo(new Integer(mrsTwo.getActiveDRCount()));
      // Option 2: read edetail count comparison
      else if (sortOrder.equals("read_edetails"))
        return new Integer(mrsOne.getReadEDetailCount())
                .compareTo(new Integer(mrsTwo.getReadEDetailCount()));
      // Option 3: sent edetail count comparison
      else if (sortOrder.equals("sent_edetails"))
        return new Integer(mrsOne.getSentEDetailCount())
                .compareTo(new Integer(mrsTwo.getSentEDetailCount()));
      // Option 4: received contact count comparison
      else if (sortOrder.equals("received_contacts"))
        return new Integer(mrsOne.getReceivedContactCount())
                .compareTo(new Integer(mrsTwo.getReceivedContactCount()));
      // Unknown option
      else
        throw new IllegalArgumentException("Unknown sort order - " + sortOrder);
    }
  }
}

