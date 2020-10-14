
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.util.*;

import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This class is used to provide an alternative List implementation, which
 * the difference that two objects may have equal positions in the list, and
 * therefore the list's get() command returns a Collection, which has one or
 * more elements. The rankings are kept "Olympic style" (ie 1st, 2nd, 2nd, 4th).
 * <br>
 * <b>Note - all indices are one-based (ie starting at one, not zero)</b>
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public class RankingList
{
  private List rankingObjects;

  /**
   * A List that holds the number of equally ranked objects in this position
   * Note - a zero value means the object is equal with an object previous in
   * the list.
   */
  private List equalRanks;

  /**
   * Constructor
   */
  public RankingList()
  {
    this.rankingObjects = new Vector();
    this.equalRanks = new Vector();
  }

  public int size() {return this.rankingObjects.size();}

  /**
   * Insert the item at the requested rank. If the rank is unavailable, return
   * the rank it was actually inserted with. eg if there are 2 equal objects at
   * index 2, a request to insert(3, object) will return 4, rather than 3. This
   * is the same as <code>insert(index, item, true)</code>.
   * @param index The desired insertion index
   * @param item The item to insert
   * @returns The actual insertion index
   */
  public int insert(int index, Object item)
    {return insert(index, item, false);}

  /**
   * Insert the item at the requested rank. If the rank is unavailable, return
   * the rank it was actually inserted with. eg if there are 2 equal objects at
   * index 2, a request to insert(3, object) will return 4, rather than 3.
   * @param index The desired insertion index
   * @param item The item to insert
   * @param forceAbove If true, always inserts above the specified position.
   * @returns The actual insertion index
   */
  public int insert(int index, Object item, boolean forceAbove)
  {
    // Before we start offset the index back to zero
    int realIndex = index - 1;

    // If empty list, just put at the start
    if (this.rankingObjects.isEmpty())
    {
      this.rankingObjects.add(item);
      this.equalRanks.add(new Integer(1));
      return 1;
    }
    // If index is past the end of the list, just put at the end
    else if (realIndex >= this.rankingObjects.size())
    {
      this.rankingObjects.add(item);
      this.equalRanks.add(new Integer(1));
      return this.rankingObjects.size();
    }
    else
    {
      Integer equalRanksAtTarget = (Integer) this.equalRanks.get(realIndex);

      // If this is in the middle of a bunch, insert above the next lot below
      if (equalRanksAtTarget.equals(new Integer(0)))
      {
        int newPos = realIndex;
        while (equalRanksAtTarget.equals(new Integer(0)))
        {
          newPos++;
          if (newPos == this.rankingObjects.size())
            break;
          else
            equalRanksAtTarget = (Integer) this.equalRanks.get(newPos);
        }
        return insert(newPos + 1, item, true);
      }
      // Otherwise, just insert at the requested position
      else
      {
        this.rankingObjects.add(realIndex, item);

        // If forceAbove is true (ie we always want to insert above any
        // objects already ranked at this position), force it to be above
        if (forceAbove)
          this.equalRanks.add(realIndex, new Integer(1));
        else
        {
          this.equalRanks.set(realIndex, new Integer(equalRanksAtTarget.intValue() + 1));
          this.equalRanks.add(realIndex + 1, new Integer(0));
        }
        return index;
      }
    }
  }

  /**
   * Return a collection of the objects held at this exact rank.
   */
  public Collection get(int index)
  {
    // Before we start offset the index for 1 based
    int actualIndex = index - 1;

    if (actualIndex >= this.rankingObjects.size())
      throw new ApplicationError("The list is not this large: " + index);

    Integer equalRanksAtTarget = (Integer) this.equalRanks.get(actualIndex);
    if (equalRanksAtTarget.equals(new Integer(0)))
      throw new ApplicationError("No object at that rank - " + index);
    else
    {
      Collection returnValue = new Vector();
      for (int nLoop = actualIndex; nLoop < (actualIndex + equalRanksAtTarget.intValue()); nLoop++)
        returnValue.add(rankingObjects.get(nLoop));
      return returnValue;
    }
  }

  /**
   * Retrieve the collection one below this rank
   */
  public int getRankBelow(int index)
  {
    // Before we start offset the index for 1 based
    int actualIndex = index - 1;

    Integer equalRanksAtTarget = (Integer) this.equalRanks.get(actualIndex);
    if (equalRanksAtTarget.equals(new Integer(0)))
      throw new ApplicationError("No object at that rank - " + index);
    else
    {
      int resultRank = index + equalRanksAtTarget.intValue();
      return (resultRank > this.rankingObjects.size() ? 0 : resultRank); // 0 = Dead ranking - invalid
    }
  }

  /**
   * Retrieve the collection one above this rank
   */
  public int getRankAbove(int index)
  {
    // Before we start offset the index for 1 based
    int actualIndex = index - 1;

    if (actualIndex == 0) return 0; // 0 = Dead ranking - invalid
    Integer equalRanksAtTarget = (Integer) this.equalRanks.get(actualIndex);
    if (equalRanksAtTarget.equals(new Integer(0)))
      throw new ApplicationError("No object at that rank");
    else
    {
      int aboveRank = actualIndex - 1;
      while (equalRanksAtTarget.equals(new Integer(0)))
      {
        aboveRank--;
        equalRanksAtTarget = (Integer) this.equalRanks.get(aboveRank);
      }
      return aboveRank + 1;
    }
  }

  /**
   * Remove a single item from the list. Note you must actually pass the object
   * in, because there can be multiple at one rank.
   */
  public void remove(int index, Object item)
  {
    // Before we start offset the index for 1 based
    int actualIndex = index - 1;

    Integer equalRanksAtTarget = (Integer) this.equalRanks.get(actualIndex);
    if (equalRanksAtTarget.equals(new Integer(0)))
      throw new ApplicationError("No object at that rank");
    else if (equalRanksAtTarget.equals(new Integer(1)))
    {
      // Remove this object only
      this.equalRanks.remove(actualIndex);
      this.rankingObjects.remove(actualIndex);
    }
    else
    {
      for (int n = actualIndex; n < actualIndex + equalRanksAtTarget.intValue(); n++)
        if (this.rankingObjects.get(n).equals(item))
        {
          this.equalRanks.set(actualIndex, new Integer(equalRanksAtTarget.intValue() - 1));
          this.equalRanks.remove(actualIndex + 1);
          this.rankingObjects.remove(n);
          return;
        }
    }
  }

  /**
   * Get the ranking index of this object
   */
  public int indexOf(Object item)
  {
    int position = this.rankingObjects.indexOf(item);
    if (position == -1)
      return position;
    else
    {
      // Look for whether this is an intermediate rank
      while (this.equalRanks.get(position).equals(new Integer(0)))
        position--;
      return position + 1;
    }
  }

  public static void main(String argv[])
  {
    RankingList rank = new RankingList();
    int test[] = {1,1,3,3,3,6,7,7,9,10,10,6,7,50};
    for (int n = 0; n < test.length; n++)
      System.out.println("Inserting " + n + " into rank " + (test[n]) +
            " - final rank = " + rank.insert(test[n], new Integer(n)));

    rank.remove(3, new Integer(3));
    rank.insert(1, new Integer(3), true);
    for (Iterator i = rank.equalRanks.iterator(); i.hasNext(); )
      System.out.println("Array contents - " + i.next());
  }
}

 