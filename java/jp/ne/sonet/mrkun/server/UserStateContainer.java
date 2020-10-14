
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.util.*;
import java.rmi.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.valueobject.*;
import jp.ne.sonet.mrkun.sessionEJB.*;

import org.jdom.*;
import java.text.*;

/**
 * This is a container class, representing an individual user's state within
 * the application. This is never directly instantiated, except inside the
 * UserStateFactory class. The factory class ensures uniqueness and
 * synchronisation.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id$
 */
public abstract class UserStateContainer extends BaseValueObject
{
  protected User        userItem;
  protected Date        lastAccessed;
  protected Map         opponentList;       // Used for retrieval by key
  protected Map         sortedOpponentList; // Used when we want a particular sort
  protected Map         sortedMessageHeaders;
  protected Map         sessionItem;

  final static String DEFAULT_DRI_SORT_ORDER      = "action";
  final static String DEFAULT_MRP_SORT_ORDER      = "date";
  final static String DEFAULT_MESSAGE_SORT_ORDER  = "date";

  final static String MESSAGE_SORT_ORDERS[] = {"recieved", "sent", "date", "subject"};

  /**
   * Constructor
   */
  public UserStateContainer(String userId)
  {
    // Attempt to load the appropriate user item
    loadUserItem(userId);

    // Load the message headers
    loadMessageHeaders(userId);
    lastAccessed = new Date();
    sessionItem = new Hashtable();
  }

  protected abstract void loadUserItem(String userId);

  protected abstract boolean isDoctor();

  public User getUserItem()
  {
    lastAccessed = new Date();
    return this.userItem;
  }

  public Object getSessionItem(Object key)
  {
    lastAccessed = new Date();
    return this.sessionItem.get(key);
  }

  public void setSessionItem(Object key, Object value)
  {
    lastAccessed = new Date();
    this.sessionItem.put(key, value);;
  }

  public Date getLastAccessed()
  {
    return lastAccessed;
  }

  /**
   * Return a map of opponents by their Ids
   */
  public Map getOpponentList()
  {
    return this.opponentList;
  }

  /**
   * Retrieve the opponent list sorted in the requested order.
   */
  public List getOpponentList(String sortOrder)
  {
    lastAccessed = new Date();
    List opponentList = (List) sortedOpponentList.get(sortOrder);
    if (opponentList == null)
    {
      opponentList = sortOpponents((List) sortedOpponentList.values().iterator().next(),
                                   sortOrder);
      sortedOpponentList.put(sortOrder,opponentList);
    }
    return opponentList;
  }

  /**
   * Retrieve the message headers (as a collection) between this user and
   * the specified "opponent" user (ie dr if mr, or vice versa).
   */
  public List getMessageHeaders(String opponentId, String sortOrder)
  {
    lastAccessed = new Date();
    Map thisOpponentMsgs = (Map) this.sortedMessageHeaders.get(opponentId);

    // If opponent not found, return empty list
    if (thisOpponentMsgs == null)
      return new ArrayList();
    else
    {
      List reqMsgHdrs = (List) thisOpponentMsgs.get(sortOrder);
      if (reqMsgHdrs == null)
      {
        reqMsgHdrs = sortMsgHdrs((List) thisOpponentMsgs.get(DEFAULT_MESSAGE_SORT_ORDER),
                                 sortOrder);
        thisOpponentMsgs.put(sortOrder, reqMsgHdrs);
      }
      return reqMsgHdrs;
    }
  }

  /**
   * Loads up the message headers, using the MessageManager.
   * @param rankingMrId The MrId with which to load the ranking object. If this is null,
   * the ranking object is not loaded.
   */
  public void loadMessageHeaders(String userId)
  {
    // Load up the result
    this.sortedMessageHeaders = new Hashtable();

    if (this.opponentList == null)
      throw new ApplicationError("opponentList not available - can't set the message headers list");
    else
    {
      // Use existing code lookups - warning dirty hacks approaching
      MessageHelperManager mhm = new MessageHelperManager();
      for (Iterator i = opponentList.keySet().iterator(); i. hasNext(); )
      {
        String opponentId = (String) i.next();
        Collection oneOpponentsMessages =
            mhm.getMessages((isDoctor()?opponentId:userId),
                            (isDoctor()?userId:opponentId),
                            "F3", new Integer(0), new Integer(100000), false,
                            (isDoctor()?MessageHelperManager.ALL_DR_MESSAGES:MessageHelperManager.ANY_MESSAGE));
        Map sortedMsgLists = new Hashtable();
        sortedMsgLists.put(DEFAULT_MESSAGE_SORT_ORDER, sortMsgHdrs(oneOpponentsMessages, DEFAULT_MESSAGE_SORT_ORDER));
        this.sortedMessageHeaders.put(opponentId, sortedMsgLists);

        // FIXME:
        // Iterate through collection and determine the no of unread contacts
        // and edetails, then update the opponent object as appropriate.
      }
    }
  }

  protected List sortOpponents(Collection currentOrder, String sortOrder)
  {
    System.out.println("Sorting opponents by: " + sortOrder);
    if (currentOrder.size() > 0)
    {
      // Determine the correct sorter type
      Comparator sorter = null;
      Object firstElement = currentOrder.iterator().next();
      if (firstElement instanceof DRInformation)
        sorter = new DRInfoComparator(sortOrder);
      else if (firstElement instanceof MrProfile)
        sorter = new MrProfileComparator(sortOrder);
      else
        throw new ApplicationError("OpponentList contains invalid object type - " +
            firstElement.getClass());

      // Sort the list
      Object opponentArray[] = currentOrder.toArray();
      Arrays.sort(opponentArray, sorter);
      return Arrays.asList(opponentArray);
    }
    else
      return new ArrayList();
  }

  protected List sortMsgHdrs(Collection currentOrder, String sortOrder)
  {
    System.out.println("Sorting messages by: " + sortOrder);
    Object unsortedMsgs[] = currentOrder.toArray();
    Comparator drc = new MessageComparator(sortOrder, (this.userItem instanceof DR));
    Arrays.sort(unsortedMsgs, drc);
    return Arrays.asList(unsortedMsgs);
  }

  /**
   * Serializes the contents of the object to an XML element.
   *
   * @param The name of the element under which the content will be serialized.
   */
  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);
    SimpleDateFormat sdfDates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    if (getLastAccessed() != null)
      masterElement.addContent(new Element("lastAccessedDate").setText(sdfDates.format(getLastAccessed())));
    if (getUserItem() != null)
      masterElement.addContent(this.userItem.toXML("user"));
    if (this.opponentList != null)
      masterElement.addContent(mapToXML("opponentList", this.opponentList, "opponent"));
    if ((this.sortedMessageHeaders != null) &&
        (this.sortedMessageHeaders.get(DEFAULT_MESSAGE_SORT_ORDER) != null))
      masterElement.addContent(
          collectionToXML("messageHdrSortList",
                          (List) this.sortedMessageHeaders.get(DEFAULT_MESSAGE_SORT_ORDER),
                          "messageHdrSort"));
    if (this.sessionItem != null)
      masterElement.addContent(mapToXML("sessionItemList", this.sessionItem, "sessionItem"));
    return masterElement;
  }
}

