
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.framework.log.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * This class implements the persistence mechanism for the Message class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @version $Id: Message_DAO.java,v 1.1.2.15 2001/08/14 14:15:02 rick Exp $
 */
public class Message_DAO implements DAO_SQL
{
  final String  ADD_QUERY                           = "java:comp/env/sql/MessageInsertSQL";

  final String  SEARCH_BY_ID_QUERY                  = "java:comp/env/sql/MessageSearchIDSQL";
  final String  SEARCH_MR_NEW_MESSAGE_BY_ID_HEAD    = "java:comp/env/sql/MessageSearchMRNewMessageSQLHead";
  final String  SEARCH_MR_NEW_MESSAGE_BY_ID_END     = "java:comp/env/sql/MessageSearchMRNewMessageSQLEnd";
  final String  SEARCH_DR_UNREAD_MESSAGE_BY_ID_HEAD = "java:comp/env/sql/MessageSearchDRUnreadNewMessageSQLHead";
  final String  SEARCH_DR_UNREAD_MESSAGE_BY_ID_END  = "java:comp/env/sql/MessageSearchDRUnreadNewMessageSQLEnd";
  final String  SEARCH_BY_NAME_QUERY                = "java:comp/env/sql/MessageSearchIDSQL";
  final String  SEARCH_GROUP_PRE                    = "java:comp/env/sql/MessageGroupSearchSQLPre";
  final String  SEARCH_GROUP_POST                   = "java:comp/env/sql/MessageGroupSearchSQLPost";
  final String  SEARCH_MR_DR_COUNT                  = "java:comp/env/sql/MessageMRDRCountSQL";
  final String  SEARCH_PAGES_SENT                   = "java:comp/env/sql/MessagePagesSentSQL";
  final String  SEARCH_PAGES_RECEIVED               = "java:comp/env/sql/MessagePagesReceivedSQL";
  final String  SEARCH_PAGES_DATE                   = "java:comp/env/sql/MessagePagesDateSQL";
  final String  SEARCH_PAGES_SUBJECT                = "java:comp/env/sql/MessagePagesSubjectSQL";

  final String  LOAD_MESSAGE_ATTACHMENTS            = "java:comp/env/sql/MessageSearchAttachmentsSQL";
  final String  LOAD_MESSAGE_RESOURCES              = "java:comp/env/sql/MessageSearchResourcesSQL";

  final String  SAVE_QUERY                          = "java:comp/env/sql/MessageUpdateSQL";

  final String  REMOVE_QUERY                        = "java:comp/env/sql/MessageDeleteSQL";
  final String  REMOVE_GROUP_PRE                    = "java:comp/env/sql/MessageGroupDeleteSQLPre";
  final String  REMOVE_GROUP_POST                   = "java:comp/env/sql/MessageGroupDeleteSQLPost";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  public Object populateObject(Connection  conn,
                                ResultSet   attributes,
                                String      returnTypeIndicator,
                                Map         eDetailCondition)
    throws SQLException, NamingException
  {
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
      throw new NoObjectFoundException("Resultset contained no rows");
  	else if ( returnTypeIndicator.equals(sessionEJBConstant.GET_MESSAGE_ID))
	    return (attributes.getString(1));
	  else if ( returnTypeIndicator.equals(sessionEJBConstant.GET_MESSAGE))
	  {
      // Determine whether or not the message is an EDetail by matching
      // the database fields with the conditions in supplied Map
      // eDetailCondition. If all fields match, we can build an eDetail,
      // otherwise a Contact.
      Iterator conditions = eDetailCondition.keySet().iterator();
      boolean  isEDetail  = true;
      while (conditions.hasNext())
      {
        String filterType = (String) conditions.next();
        if (!attributes.getObject(filterType).equals(eDetailCondition.get(filterType)))
          isEDetail = false;
      }

	    // Build a message object, determining whether to use an EDetail or
      // a Contact based on the messageTypeIndicator field.
      Message returnMessage = null;

      if (isEDetail)
      {
        // Populate with the EDetail specific parts if required
        EDetail edetail = new EDetail();
        edetail.setMessageId(attributes.getString(1));
        edetail.setSender(attributes.getString(2));
        edetail.setRecipient(attributes.getString(3));
        edetail.setTitle(attributes.getString(4));
        edetail.setBody(attributes.getString(5));
        edetail.setSentDate(attributes.getDate(6));
        edetail.setReadDate(attributes.getDate(7));
        edetail.setSentDeleteDate(attributes.getDate(8));
        edetail.setReceivedDeleteDate(attributes.getDate(9));
        edetail.setCancelTime(attributes.getDate(10));
        edetail.setEDetailCategory(attributes.getString(11));

        // WebImage DAO is used to get a WebImage object
        DAO_SQL daoWebImage = new WebImage_DAO();
        String imageID = attributes.getString(12);
        if (imageID != null)
        {
          WebImage resultImage = (WebImage) daoWebImage.searchRecord(conn, imageID, "webImageId");
          edetail.setImage(resultImage);
        }

        // Load resources
        edetail.setResourceList(populateResourceList(conn, edetail.getMessageId()));
        returnMessage = edetail;
      }
      else
      {
        returnMessage = new Contact();
        returnMessage.setMessageId(attributes.getString(1));
        returnMessage.setSender(attributes.getString(2));
        returnMessage.setRecipient(attributes.getString(3));
        returnMessage.setTitle(attributes.getString(4));
        returnMessage.setBody(attributes.getString(5));
        returnMessage.setSentDate(attributes.getDate(6));
        returnMessage.setReadDate(attributes.getDate(7));
        returnMessage.setSentDeleteDate(attributes.getDate(8));
        returnMessage.setReceivedDeleteDate(attributes.getDate(9));
      }

      // Load attachments
      returnMessage.setAttachmentList(populateAttachmentList(conn, returnMessage.getMessageId()));

      return returnMessage;
	  }
    else
      throw new ApplicationError("Unknown indicator type - " + returnTypeIndicator);
  }

  /**
   * This method loads the attachment list associated with the message.
   */
  private Collection populateAttachmentList(Connection conn, String messageId)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Get a list of attachments
    PreparedStatement attQuery = conn.prepareStatement(getSQLStatement(LOAD_MESSAGE_ATTACHMENTS));
    attQuery.setObject(1, messageId);
    ResultSet attSet = attQuery.executeQuery();
	  Collection attachmentList = new ArrayList();

    // Add them to the list
    while (attSet.next())
    {
      AttachmentLink resultAttachment = new AttachmentLink();
      resultAttachment.setAttachmentLinkId(attSet.getString("seq"));
      resultAttachment.setName            (attSet.getString("attach_file"));
      resultAttachment.setFileName        (attSet.getString("file_name"));
      attachmentList.add(resultAttachment);
    }
    attSet.close();
    attQuery.close();
	
	  return attachmentList;
  }
  
  /**
   * This method loads the resource list associated with the message.
   */
  private Collection populateResourceList(Connection conn, String messageId)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Get a list of resources
    PreparedStatement resQuery = conn.prepareStatement(getSQLStatement(LOAD_MESSAGE_RESOURCES));
    resQuery.setObject(1, messageId);
    ResultSet resSet = resQuery.executeQuery();
	  Collection resourceList = new ArrayList();

    // Add them to the list
    while (resSet.next())
      resourceList.add(new Integer(resSet.getInt("link_cd")));

    resSet.close();
    resQuery.close();
	
	  return resourceList;
  }
  
  /**
   * This method is used for populating the MR's message (MR2 and MR5)
   * resultant hash data structure.
   *
   * @param conn The JDBC connection object.
   * @param attributes The resultset with the retrieved object in it.
   * @return the hash data structure of MR's message related data.
   */
  private Map populateMRMessageHashData(Map drHash,
                                        ResultSet attributes,
  											                Connection conn,
											                  String indicator)
    throws SQLException, NamingException
  {
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
      throw new NoObjectFoundException("Resultset contained no rows");
    else
    {
  	  // Loop through the resultset and loadup the hash data structure
	    while (attributes.next())
	    {
  	    // These data containers will hold mr's message data of each dr

  	  	Integer count = new Integer(attributes.getInt("messageCount"));

    		if ( indicator.equals(SEARCH_MR_NEW_MESSAGE_BY_ID_HEAD))
	      {
		      String drId = attributes.getString("from_userid");
	        String mrId = attributes.getString("to_userid");

          if (drHash.get(drId) == null)
            drHash.put(drId, new Object[5]);
          Object messageData[] = (Object []) drHash.get(drId);
          if (messageData[sessionEJBConstant.RECEIVED_MESSAGE_ID] == null)
		        messageData[sessionEJBConstant.RECEIVED_MESSAGE_ID] = sessionEJBConstant.NOT_SET;
          if (messageData[sessionEJBConstant.UNREAD_MESSAGE_ID] == null)
		        messageData[sessionEJBConstant.UNREAD_MESSAGE_ID] = sessionEJBConstant.NOT_SET;
          if (messageData[sessionEJBConstant.RECEIVED_MESSAGE] == null)
		        messageData[sessionEJBConstant.RECEIVED_MESSAGE] = new Integer(0);
          if (messageData[sessionEJBConstant.UNREAD_SENT_MESSAGE] == null)
		        messageData[sessionEJBConstant.UNREAD_SENT_MESSAGE] = new Integer(0);

    		  // MR has exact one new message from DR, then get that message ID
		      if (count.intValue() == 1)
	        {
    		  	Map senderAndRecipient = new Hashtable();
	          senderAndRecipient.put("sender", mrId);
		        senderAndRecipient.put("recipient", drId);
	          String messageId = getIdOfNewMessage(conn, senderAndRecipient);
		        messageData[sessionEJBConstant.RECEIVED_MESSAGE_ID] = messageId;
	        }
		      else
		        messageData[sessionEJBConstant.RECEIVED_MESSAGE_ID] = sessionEJBConstant.NOT_SET;

    		  // Put all message related data in the array list then the array list in hashtable
		      messageData[sessionEJBConstant.RECEIVED_MESSAGE] = count;
		      drHash.put(drId, messageData);
	      }

		    if ( indicator.equals(SEARCH_DR_UNREAD_MESSAGE_BY_ID_HEAD))
    		{
		      String mrId = attributes.getString("from_userid");
	        String drId = attributes.getString("to_userid");

          if (drHash.get(drId) == null)
            drHash.put(drId, new Object[5]);
          Object messageData[] = (Object []) drHash.get(drId);

    		  // DR has exact one unread message from MR, then get that message ID
		      if (count.intValue() == 1)
	        {
		  	    Map senderAndRecipient = new Hashtable();
	          senderAndRecipient.put("sender", mrId);
  		      senderAndRecipient.put("recipient", drId);
	          String messageId = getIdOfNewMessage(conn, senderAndRecipient);
		        messageData[sessionEJBConstant.UNREAD_MESSAGE_ID] = messageId;
	        }
    		  else
		        messageData[sessionEJBConstant.UNREAD_MESSAGE_ID] =  sessionEJBConstant.NOT_SET;

    		  // Put all message related data in the array list then the array list in hashtable
		      messageData[sessionEJBConstant.UNREAD_SENT_MESSAGE] = count;
	        drHash.put(drId, messageData);
		    }
	    }
      return drHash;
    }
  }

  /**
   * Get the specific message id (new/unopen message) given the sender
   * and recipient userid.
   *
   * @param conn The JDBC connection object.
   * @param searchObject The query's parameter data.
   * @param filter The query's criteria to filter for.
   * @return the message ID.
   */
  private String getIdOfNewMessage(Connection conn, Object searchObject)
    throws SQLException, NamingException
  {
  	String messageId = "";

	  if (!(searchObject instanceof Hashtable))
	    throw new RuntimeException("Invalid searchObject type - " + searchObject.getClass() +". SearchObject should be in Hashtable type.");

    messageId = (String) searchRecord(conn, searchObject, sessionEJBConstant.GET_MESSAGE_ID);
	  return messageId;
  }

  /**
   * Get a sql string from the environment variables in the deployment
   * descriptor
   */
  private String getSQLStatement(String statementLocation) throws NamingException
  {
    InitialContext ctx = new InitialContext();
    String sqlStatement = (String) ctx.lookup(statementLocation);
    return sqlStatement;
  }

  /**
   * This will analyze mr's new message count and dr's unread message count
   * to figure out the action value.
   *
   * @param mr The MR object.
   * @param mrMessageCountData The Hashtable of Mr's message data.
   */
  private void setActionValue(MR mr, Map mrMessageCountData)
  {
    if (mr == null)
      throw new RuntimeException("Mr is null in " + this.getClass().getName());

	  List drIdList = (ArrayList) mr.getDRList();
	  for (int index = 0; index < drIdList.size(); index++)
	  {
      if (mrMessageCountData.get(drIdList.get(index)) == null)
        mrMessageCountData.put(drIdList.get(index), new Object[5]);
      Object messageData[] = (Object []) mrMessageCountData.get(drIdList.get(index));
	    Integer mrNewMessageCount = (Integer) messageData[sessionEJBConstant.RECEIVED_MESSAGE];
	    Integer drUnreadMessageCount = (Integer) messageData[sessionEJBConstant.UNREAD_SENT_MESSAGE];

	    if (mrNewMessageCount == null)    mrNewMessageCount = new Integer(0);
	    if (drUnreadMessageCount == null) drUnreadMessageCount = new Integer(0);

	    else if (mrNewMessageCount.intValue() > 0)
        messageData[sessionEJBConstant.ACTION_VALUE] = new Integer(0);
	    else if(drUnreadMessageCount.intValue() == 0)
	      messageData[sessionEJBConstant.ACTION_VALUE] = new Integer(1);
	    else
	      messageData[sessionEJBConstant.ACTION_VALUE] = new Integer(2);
    }
  }

  /**
   * This will build and execute the SQL statement for querying of MR's new received message
   * from each DR that the MR associate with.
   *
   * @param mr The MR object.
   * @param mrMessageCountData The Hashtable of Mr's message data.
   * @param conn The JDBC connection object.
   * @param filter The query's criteria to filter for.
   */
  private void loadMRMessageCount(MR mr,
                                  Map mrMessageCountData,
								                  Connection conn,
								                  String indicator)
    throws SQLException, NamingException
  {
  	if (mr == null)
      throw new ApplicationError("Mr is null in " + this.getClass().getName());

  	String queryStringHead = "";
  	String queryStringEnd = "";
	  List drIdList = (List) mr.getDRList();
    if (drIdList.isEmpty()) return;

    // Build the complete sql statement
  	if ( indicator.equals(SEARCH_MR_NEW_MESSAGE_BY_ID_HEAD))
	  {
	    queryStringHead = getSQLStatement(SEARCH_MR_NEW_MESSAGE_BY_ID_HEAD);
      queryStringEnd = getSQLStatement(SEARCH_MR_NEW_MESSAGE_BY_ID_END);
  	}
	  else if ( indicator.equals(SEARCH_DR_UNREAD_MESSAGE_BY_ID_HEAD))
  	{
	    queryStringHead = getSQLStatement(SEARCH_DR_UNREAD_MESSAGE_BY_ID_HEAD);
      queryStringEnd = getSQLStatement(SEARCH_DR_UNREAD_MESSAGE_BY_ID_END);
	  }

  	StringBuffer sqlStatement = new StringBuffer();

  	for (int index = 0; index < drIdList.size(); index++)
	    sqlStatement.append(", ?");
    sqlStatement.delete(0, 2);
	  sqlStatement.insert(0, queryStringHead);
	  sqlStatement.append(queryStringEnd);

  	// Set sql type parameter per drId
	  PreparedStatement searchQuery = conn.prepareStatement(sqlStatement.toString());
  	searchQuery.setObject(1, mr.getMrId());
	  for (int index = 0; index < drIdList.size(); index++)
      searchQuery.setObject(index+2, drIdList.get(index));

  	// Execute the query
	  ResultSet results = searchQuery.executeQuery();
    try
    {
      populateMRMessageHashData(mrMessageCountData, results, conn, indicator);
    }
    catch (NoObjectFoundException errNOF) {/* Don't care */}
    results.close();
    searchQuery.close();
  }

  /**
   * This method contains the logic for adding a new EmailContact
   * into the database.
   *
   * @param conn The JDBC connection object.
   * @param addDR The DR object to be inserted.
   */
  public boolean createRecord(Connection  conn,
                              Object      addEmail)
    throws SQLException, NamingException
  {
/*  Will modify this later

    // Check delete object type
    if (!(addEmail instanceof EmailContact))
      throw new RuntimeException("Add object is incorrect type - " + addEmail.getClass());
    EmailContact workingContact = (EmailContact) addEmail;

    // Update the EmailContact
    if (workingContact.getEmailContactId() == null)
      workingContact.setEmailContactId("" + Sequence.getNext(conn, "EmailContact"));
    PreparedStatement addEmailQuery = conn.prepareStatement(getSQLStatement(ADD_QUERY));

    addEmailQuery.setString(1, workingContact.getEmailContactId());

    if (workingContact.getName() == null)
      addEmailQuery.setNull(2, Types.VARCHAR);
    else
      addEmailQuery.setString(2, workingContact.getName());

    if (workingContact.getEmailAddress() == null)
      addEmailQuery.setNull(3, Types.VARCHAR);
    else
      addEmailQuery.setString(3, workingContact.getEmailAddress());

    int rowsAffected = addEmailQuery.executeUpdate();
    addEmailQuery.close();
    return (rowsAffected == 1);
*/

	  return false;
  }

  /**
   * This method contains the logic for retrieving a Message from the database.<br/>
   * Note: While this query may return many EmailContact objects
   * if the name field is not unique, only the first will be retrieved.
   *
   * @param conn The JDBC connection object.
   * @param searchObject The query's parameter data.
   * @param filter The query's criteria to filter for.
   * @returns resultset data.
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
	  // This will return a Hashtable with all the mr message data for MR2 and MR5
    if (filter.equals(sessionEJBConstant.MR_MESSAGE_COUNT))
	  {
	    if (!(searchObject instanceof MR))
	      throw new RuntimeException("Invalid searchObject type - " + searchObject.getClass() +". SearchObject should be in MR type.");
	    else
      {
  	    MR mr = (MR) searchObject;
        Map mrMessageCountData = new Hashtable();
	      loadMRMessageCount(mr, mrMessageCountData, conn, SEARCH_MR_NEW_MESSAGE_BY_ID_HEAD);
	      loadMRMessageCount(mr, mrMessageCountData, conn, SEARCH_DR_UNREAD_MESSAGE_BY_ID_HEAD);
        setActionValue(mr, mrMessageCountData);
	      return mrMessageCountData;
      }
	  }
	  else if (filter.equals(sessionEJBConstant.GET_MESSAGE_ID))
	  {
  	  Map     params      = (Map) searchObject;
	    String  sender      = (String) params.get("sender");
	    String  recipient   = (String) params.get("recipient");

      // Create the query and populate a return object
	    PreparedStatement searchQuery = conn.prepareStatement(getSQLStatement(SEARCH_BY_ID_QUERY));
	    searchQuery.setObject(1, sender);
  	  searchQuery.setObject(2, recipient);

	    ResultSet results = searchQuery.executeQuery();
	    Object    found   = populateObject(conn, results, sessionEJBConstant.GET_MESSAGE_ID, null);
      
      results.close();
      searchQuery.close();
      
      return found;
  	}
    else if (filter.equals(sessionEJBConstant.GET_MESSAGE))
	  {
  	  String  messageId   = (String) searchObject;

      // Create the query and populate a return object
	    PreparedStatement searchQuery = conn.prepareStatement(getSQLStatement(SEARCH_BY_ID_QUERY));
	    searchQuery.setObject(1, messageId);

	    ResultSet results = searchQuery.executeQuery();
	    Object    found   = populateObject(conn, results, sessionEJBConstant.GET_MESSAGE, null);
      
      results.close();
      searchQuery.close();
      
      return found;
  	}
	  else
      throw new RuntimeException("Invalid query filter type");
  }

  /**
   * This method contains the logic for updating an
   * existing EmailContact in the database.
   *
   * @param conn The JDBC connection object.
   * @param saveDR The EmailContact object to be saved.
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
  	/* Will modify this later

    // Check save object type
    if (saveContact == null)
      return true;
    else if (!(saveContact instanceof EmailContact))
      throw new RuntimeException("Save object is incorrect type - " + saveContact.getClass());
    EmailContact workingContact = (EmailContact) saveContact;

    // Create the query
    PreparedStatement saveQuery = conn.prepareStatement(getSQLStatement(SAVE_QUERY));
    if (workingContact.getName() == null)
      saveQuery.setNull(1, Types.VARCHAR);
    else
      saveQuery.setString(1, workingContact.getName());
    if (workingContact.getEmailAddress() == null)
      saveQuery.setNull(2, Types.VARCHAR);
    else
      saveQuery.setString(2, workingContact.getEmailAddress());
    saveQuery.setString(3, workingContact.getEmailContactId());

	  int rowsAffected = saveQuery.executeUpdate();
    saveQuery.close();
    return (rowsAffected == 1);
	  */

  	return false;
  }

  /**
   * This method contains the logic for deleting an
   * existing message from the database.
   *
   * @param conn The JDBC connection object.
   * @param deleteMessage The Message object to be removed.
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteMessage)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(deleteMessage instanceof Message))
      throw new RuntimeException("Deleting object is incorrect type - " + deleteMessage.getClass());

	  Message message = (Message) deleteMessage;

    // Delete the Message
    PreparedStatement deleteQuery = conn.prepareStatement(getSQLStatement(REMOVE_QUERY));
    deleteQuery.setString(1, message.getMessageId());
    int rowsAffected = deleteQuery.executeUpdate();
    deleteQuery.close();
    return (rowsAffected == 1);
  }

  /**
   * Create new instances in persistent storage
   */
  public boolean createMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Retrieves existing instances from persistent storage
   */
  public Object searchMultiple(Connection conn, Object searchObject, String filter)
    throws SQLException, NamingException
  {
    // Sort into different filter types ... first: the list of ids
    if (filter.equals(sessionEJBConstant.MESSAGE_ID_LIST))
    {
      // Get the search parameters out of the search object map
      Map         params      = (Map) searchObject;
      String      mrId        = (String) params.get("mrId");
      Collection  selectList  = (Collection) params.get("messageIdList");
      Collection  returnList  = new ArrayList();

      // Build a query with an IN clause for message Ids
      if (!selectList.isEmpty())
      {
        // Build the id list string
        StringBuffer idList = new StringBuffer();
        for (int nLoop = 0; nLoop < selectList.size(); nLoop++)
          idList.append(", ?");

        // Retrieve the messages
        PreparedStatement selectQuery = conn.prepareStatement(
                      getSQLStatement(SEARCH_GROUP_PRE)
                      + idList.toString().substring(2)
                      + getSQLStatement(SEARCH_GROUP_POST));

        selectQuery.setString(1, mrId);
        selectQuery.setString(2, mrId);
        Iterator i = selectList.iterator();
        int paramCounter = 3;
        while (i.hasNext())
          selectQuery.setString(paramCounter++, (String) i.next());

        ResultSet attributes  = selectQuery.executeQuery();

        // Loop through the resultset building the collection
        Map eDetailConditions = new Hashtable();
        eDetailConditions.put("from_userId", mrId);
        try
        {
          while (true)
            returnList.add(populateObject(conn, attributes, sessionEJBConstant.GET_MESSAGE, eDetailConditions));
        }
        catch (NoObjectFoundException errNOF)
        {/* We've reached the end of the list - don't panic */}

        attributes.close();
        selectQuery.close();
      }
      return returnList;
    }
    else if (filter.equals(sessionEJBConstant.MR_DR_MESSAGE_COUNT))
    {
      // Get the search parameters out of the search object map
      Map     params    = (Map) searchObject;
      String  mrId      = (String) params.get("mrId");
      String  drId      = (String) params.get("drId");
      Integer direction = (Integer) params.get("direction");

      // Retrieve the message counts
      int total = 0;
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_MR_DR_COUNT));
      if (direction.equals(Message.SENT) || direction.equals(Message.SENT_RECEIVED))
      {
        // Sent messages: mr = sender, dr = recipient
        selectQuery.setString(1, mrId);
        selectQuery.setString(2, drId);
        ResultSet result = selectQuery.executeQuery();
        if (result.next())
          total += result.getInt(1);
        result.close();
      }
      else if (direction.equals(Message.RECEIVED) || direction.equals(Message.SENT_RECEIVED))
      {
        // Sent messages: dr = sender, mr = recipient
        selectQuery.setString(1, drId);
        selectQuery.setString(2, mrId);
        ResultSet result = selectQuery.executeQuery();
        if (result.next())
          total += result.getInt(1);
        result.close();
      }

        selectQuery.close();

      return new Integer(total);
    }
    else if (filter.equals(sessionEJBConstant.MESSAGE_PAGES))
    {
      Collection  returnList  = new ArrayList();

      // Get the search parameters out of the search object map
      Map     params           = (Map) searchObject;
      String  mrId             = (String) params.get ("mrId");
      String  drId             = (String) params.get ("drId");
      String  sortOrder        = (String) params.get ("sortOrder");
      Integer messageCountFrom = (Integer) params.get("messageCountFrom");
      Integer messageCountTo   = (Integer) params.get("messageCountTo");

      // Retrieve the messages
      PreparedStatement selectQuery;
      if (sortOrder.equals("sent"))
      {
        selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_PAGES_SENT));
        selectQuery.setString(1, mrId);
        selectQuery.setString(2, drId);
//      selectQuery.setInt   (3, messageCountFrom.intValue());
//      selectQuery.setInt   (4, messageCountTo.intValue());
      }
      else if (sortOrder.equals("recieved"))
      {
        selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_PAGES_RECEIVED));
        selectQuery.setString(1, drId);
        selectQuery.setString(2, mrId);
//      selectQuery.setInt   (3, messageCountFrom.intValue());
//      selectQuery.setInt   (4, messageCountTo.intValue());
      }
      else if (sortOrder.equals("date"))
      {
        selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_PAGES_DATE));
        selectQuery.setString(1, drId);
        selectQuery.setString(2, mrId);
        selectQuery.setString(3, mrId);
        selectQuery.setString(4, drId);
//      selectQuery.setInt   (5, messageCountFrom.intValue());
//      selectQuery.setInt   (6, messageCountTo.intValue());
      }
      else if (sortOrder.equals("subject"))
      {
        selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_PAGES_SUBJECT));
        selectQuery.setString(1, drId);
        selectQuery.setString(2, mrId);
        selectQuery.setString(3, mrId);
        selectQuery.setString(4, drId);
//      selectQuery.setInt   (5, messageCountFrom.intValue());
//      selectQuery.setInt   (6, messageCountTo.intValue());
      }
      else throw new ApplicationError("Bad sort order or filter requested: " + sortOrder);
      ResultSet attributes = selectQuery.executeQuery();

      // Loop through the resultset building the collection
      Map eDetailConditions = new Hashtable();
      eDetailConditions.put("from_userId", mrId);
      try
      {
        while (true)
          returnList.add(populateObject(conn, attributes, sessionEJBConstant.GET_MESSAGE, eDetailConditions));
      }
      catch (NoObjectFoundException errNOF)
      {/* We've reached the end of the list - don't panic */}

      attributes.close();
      selectQuery.close();
      return returnList;
    }
    else
      throw new NotImplementedException("Unknown multiple message filter type - " + filter);
  }

  /**
   * Saves existing instances in persistent storage
   */
  public boolean updateMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Deletes existing instances from persistent storage
   */
  public boolean deleteMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    // Check delete object type
    if (!(modifyingObject instanceof Map))
      throw new RuntimeException("Deleting object is incorrect type (expect Collection) - " + modifyingObject.getClass());
    Map params = (Map) modifyingObject;
    String      mrId        = (String)     params.get("userId");
    Collection  deleteList  = (Collection) params.get("messageIdList");

    if (!deleteList.isEmpty())
    {
      // Build the id list string
      StringBuffer idList = new StringBuffer();
      for (int nLoop = 0; nLoop < deleteList.size(); nLoop++)
        idList.append(", ?");

      // Retrieve the messages
      PreparedStatement deleteQuery = conn.prepareStatement(
                    getSQLStatement(REMOVE_GROUP_PRE)
                    + idList.toString().substring(2)
                    + getSQLStatement(REMOVE_GROUP_POST));

      deleteQuery.setString(1, mrId);
      deleteQuery.setString(2, mrId);

      Iterator i = deleteList.iterator();
      int paramCounter = 3;
      while (i.hasNext())
        deleteQuery.setString(paramCounter++, (String) i.next());

      int rowsAffected = deleteQuery.executeUpdate();
      deleteQuery.close();
      return (rowsAffected == deleteList.size());
    }
    else
      return true;
  }
}

