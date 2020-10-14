// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import java.util.*;
import java.rmi.*;

/**
 * The remote interface for the MessageManager Session EJB. This
 * bean loads Messages from the database, and returns them within
 * valueobjects. It also allows writing them back to the database.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: MessageManagerRemoteIntf.java,v 1.1.2.14 2001/11/13 07:56:56 rick Exp $
 */
public interface MessageManagerRemoteIntf extends BaseSessionEJBRemoteIntf
{
  /**
   * Loads a single Message by it's messageId
   */
  public Message getMessageById(String messageId, String sessionId) throws RemoteException;

  /**
   * Loads available Messages, between the from and to Ids, and
   * matching the sent or received flag value.
   */
  public Collection getMessages(String  mrId,
                                String  drId,
                                String  sortOrder,
                                Integer messageCountFrom,
                                Integer messageCountTo,
                                String sessionId) throws RemoteException;

  /**
   * Loads the messages specified in the collection of ids into a collection of
   * message objects.
   */
  public Collection getMessages(String mrId, Collection messageIds, String sessionId) throws RemoteException;

  /**
   * Get a count of the available messages for this MR/DR combination
   * @param mrId The MR to filter for.
   * @param drId The DR to filter for.
   * @param direction The direction the message was sent (either Message.EDETAIL or
   *                  or Message.CONTACT).
   * @returns An Integer representing the number of messages available.
   */
  public Integer getAvailableMessages(String  mrId,
                                      String  drId,
                                      Integer direction,
                                      String sessionId) throws RemoteException;

  /**
   * Remove all the messages matching the ids specified in the collection supplied.
   */
  public void removeMessages(String userId, Collection messageIdList, String sessionId) throws RemoteException;

  /**
   * This will return a hashtable that contains the person's (the key for the hash map)
   * message count.
   * @param mr The MR to be filtered for.
   * @param indicator ?
   * @returns Map that contains the drId and their message count.
   */
  public Map getMRMessageCount(MR mr, String indicator, String sessionId) throws RemoteException;

  /**
   * This method adds a new message to persistent storage.
   * @param sender Either a DR or an MR, representing the sender.
   * @param recipientIds The list of DR ids or MR ids who will be recipients.
   * @param messageBody The message implementation valueobject that will be
   *                    written to the database.
   */
  public void addMessage(User sender, Collection recipientIds, Message messageBody, String plainText, String sessionId) throws RemoteException;

  /**
   * Retracts a message that hasn't been read by the recipients yet
   */
  public void retractMessage(Message retractMessage, String sessionId) throws RemoteException;
  
}

