
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.dao.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import java.util.*;
import java.rmi.RemoteException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;

/**
 * The implementation class for the MessageManager Session EJB. This
 * bean loads Messages from the database, and returns them within
 * valueobjects. It also allows writing them back to the database.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: MessageManager.java,v 1.1.2.17 2001/11/13 07:56:56 rick Exp $
 */
public class MessageManager extends BaseSessionEJB
{
  /**
   * Loads a single Message by it's messageId
   */
  public Message getMessageById(String messageId, String sessionId) throws RemoteException
  {
    DAOFacade messageDAO = new DAOFacade(Message.class);
    try
    {
      return (Message) messageDAO.searchRecord(messageId, "mrId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Loads available Messages, between the from and to Ids, and
   * matching the sent or received flag value.
   */
  public Collection getMessages(String  mrId,
                                String  drId,
                                String  sortOrder,
                                Integer messageCountFrom,
                                Integer messageCountTo,
                                String sessionId) throws RemoteException
  {
    // Build a map of params
    Map params = new Hashtable();
    params.put("mrId", mrId);
    params.put("drId", drId);
    params.put("sortOrder", sortOrder);
    params.put("messageCountFrom", messageCountFrom);
    params.put("messageCountTo", messageCountTo);
    
    DAOFacade messageDAO = new DAOFacade(Message.class);
    return (Collection) messageDAO.searchMultiple(params, sessionEJBConstant.MESSAGE_PAGES, sessionId);
  }

  /**
   * Loads the messages specified in the collection of ids into a collection of
   * message objects.
   */
  public Collection getMessages(String mrId, Collection messageIds, String sessionId) throws RemoteException
  {
    DAOFacade messageDAO = new DAOFacade(Message.class);
    Map params = new Hashtable();
    params.put("mrId", mrId);
    params.put("messageIdList", messageIds);
    return (Collection) messageDAO.searchMultiple(params, sessionEJBConstant.MESSAGE_ID_LIST, sessionId);
  }

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
                                      String sessionId) throws RemoteException
  {
    DAOFacade messageDAO = new DAOFacade(Message.class);
    Map params = new Hashtable();
    params.put("mrId", mrId);
    params.put("drId", drId);
    params.put("direction", direction);
    return (Integer) messageDAO.searchMultiple(params, sessionEJBConstant.MR_DR_MESSAGE_COUNT, sessionId);
  }

  /**
   * Remove all the messages matching the ids specified in the collection supplied.
   */
  public void removeMessages(String userId, Collection messageIdList, String sessionId) throws RemoteException
  {
    DAOFacade messageDAO = new DAOFacade(Message.class);
    Map params = new Hashtable();
    params.put("userId", userId);
    params.put("messageIdList", messageIdList);
    messageDAO.deleteMultiple(params, sessionId);
  }

  /**
   * This will return a map that contains the DR's (the key for the hash map)
   * message count.
   * @param mr The MR to be filtered for.
   * @param indicator 
   * @returns Map that contains the drId and their message count.
   */
  public Map getMRMessageCount(MR mr, String indicator, String sessionId) throws RemoteException
  {
    // Get a DAO
    DAOFacade messageDAO = new DAOFacade(Message.class);
    Map messageList = null;

    if (indicator.equals(sessionEJBConstant.MR_MESSAGE_COUNT))
    {
      try
      {
        messageList = (Map) messageDAO.searchRecord(mr, sessionEJBConstant.MR_MESSAGE_COUNT, sessionId);
      }
      catch (NoObjectFoundException errNOF)
      {
		System.out.println("No object found");
        return new Hashtable();
      }
    }
    return messageList;
  }

  /**
   * This method adds a new message to persistent storage.
   * @param sender Either a DR or an MR, representing the sender.
   * @param recipientIds The list of DR ids or MR ids who will be recipients.
   * @param messageBody The message implementation valueobject that will be
   *                    written to the database.
   */
  public void addMessage(User sender, Collection recipientIds, Message messageBody, String plainText, String sessionId)
  {
    System.out.println("Trying to send message");
  }

  /**
   * Retracts a message that hasn't been read by the recipients yet
   */
  public void retractMessage(Message retractMessage, String sessionId)
  {
    System.out.println("Trying to retract message");
  }
}

