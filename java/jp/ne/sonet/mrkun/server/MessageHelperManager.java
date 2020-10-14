package jp.ne.sonet.mrkun.server;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.rmi.*;
import java.text.SimpleDateFormat;
// Commented out by Damon 011015: import jp.ne.sonet.mrkun.dao.DAOFacade;
// Commented out by Damon 011015: import jp.ne.sonet.mrkun.dao.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
// import jp.ne.sonet.medipro.wm.common.util.*;
import java.util.*;
import jp.ne.sonet.medipro.mr.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.log.*;

public class MessageHelperManager // implements MessageManagerRemoteIntf
{
 protected Collection recipients;
 protected Vector vctRecipients;
 protected Message msg;
 protected String text;
 boolean isDetailManager = true;
 static final Map SQL_MESSAGE_LIST_orderBy;

 static 
 {
 	SQL_MESSAGE_LIST_orderBy = new Hashtable();
 
	SQL_MESSAGE_LIST_orderBy.put("F1","ORDER BY DECODE(H.MESSAGE_KBN, 1, 1, 4, 1, 2) DESC,RECEIVE_TIMED DESC, RECEIVE_TIME DESC");
	SQL_MESSAGE_LIST_orderBy.put("F2","ORDER BY DECODE(H.MESSAGE_KBN, 1, 1, 4, 1, 2) ASC,RECEIVE_TIMED DESC, RECEIVE_TIME DESC");
	SQL_MESSAGE_LIST_orderBy.put("F3","ORDER BY RECEIVE_TIME DESC");
	SQL_MESSAGE_LIST_orderBy.put("F4","ORDER BY TITLE, RECEIVE_TIMED DESC, RECEIVE_TIME DESC");
 
 }

  public static final String CONTACTS_ONLY    = "contactsOnly";
  public static final String EDETAILS_ONLY    = "edetailsOnly";
  public static final String ANY_MESSAGE      = "anyMessage";
  public static final String ALL_DR_MESSAGES  = "allDRMessages";

 protected static final String SQL_MR_MESSAGE_COUNT_OLD = 
 	"SELECT DISTINCT MR_ID, DR_ID, "
	+ "fnNowRenraku(MR_ID,DR_ID) AS newRenraku, "
	+ "fnNowEdtl(MR_ID,DR_ID) AS midokuEdetail "
	+ "FROM vwMRkunKKanri WHERE MR_ID= ? ";
	
 protected static final String SQL_MR_MESSAGE_COUNT = 
	"SELECT DISTINCT MR_ID, DR_ID, "
	+ "fnNowRenraku(MR_ID,DR_ID) AS newRenraku, "
	+ "fnNowEdtl(MR_ID,DR_ID) AS midokuEdetail "
	+ "FROM SENTAKU_TOROKU WHERE MR_ID= ? ";
	
  protected static final String SQL_DR_MESSAGE_COUNT = 
	"SELECT DISTINCT MR_ID, DR_ID, "
	+ "fnNowRenraku(MR_ID,DR_ID) AS newRenraku, "
	+ "fnNowEdtl(MR_ID,DR_ID) AS midokuEdetail "
	+ "FROM SENTAKU_TOROKU WHERE DR_ID= ? ";	
	
 protected static final String SQL_GET_UNIQUE_CONTACT_ID =
	"SELECT COUNT(DISTINCT MESSAGE_HEADER_ID) AS count, "
	+ " message_header_id FROM MESSAGE_HEADER H "
	+ " WHERE H.MESSAGE_KBN=2 AND H.RECEIVE_TIMED IS NULL "
	+ "	AND H.TO_USERID=? AND H.FROM_USERID=? "
 	+ " group by message_header_id having count(message_header_id)=1";
 		
 protected static final String SQL_GET_UNIQUE_DETAIL_ID =
 	"SELECT COUNT(DISTINCT MESSAGE_HEADER_ID) AS count, "
 	+ " message_header_id FROM MESSAGE_HEADER H "
 	+ " WHERE H.MESSAGE_KBN=1 AND H.RECEIVE_TIMED IS NULL "
 	+ "	AND H.FROM_USERID=? AND H.TO_USERID=? "
	+ " AND SEND_STATUS = 1 "
	+ " group by message_header_id having count(message_header_id)=1";
	
 protected static final String SQL_MESSAGE_LIST =
	"SELECT H.MESSAGE_KBN AS KBN,H.RECEIVE_TIME RECEIVE_TIME,B.TITLE TITLE, "
	+ "H.MESSAGE_HEADER_ID MESSAGE_HEADER_ID, H.RECEIVE_TIMED RECEIVE_TIMED, "
	+ "H.FROM_USERID FROM_USERID, H.TO_USERID TO_USERID "
 	+ "FROM MESSAGE_HEADER H,MESSAGE_BODY B "
	+ "WHERE	H.MESSAGE_ID=B.MESSAGE_ID "
	+ "AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID,6,H.TO_USERID)= ? "
	+ "AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID,6,H.FROM_USERID)= ? "
 	+ "AND H.RECEIVE_STATUS <> 3 ";
 	
/*
 protected static final String SQL_MR_MESSAGE_LIST =
 	"SELECT H.MESSAGE_KBN AS KBN,H.RECEIVE_TIME RECEIVE_TIME,B.TITLE TITLE, "
 	+ "H.MESSAGE_HEADER_ID MESSAGE_HEADER_ID, H.RECEIVE_TIMED RECEIVE_TIMED, "
	+ "H.RECEIVE_STATUS RECEIVE_STATUS, H.SEND_STATUS SEND_STATUS, "
 	+ "H.FROM_USERID FROM_USERID, H.TO_USERID TO_USERID "
 	+ "FROM MESSAGE_HEADER H,MESSAGE_BODY B, "
 	+ "mr mr, "
 	+ "doctor dr "
 	+ "WHERE	H.MESSAGE_ID=B.MESSAGE_ID "
    + " AND ( (B.YUKO_KIGEN is NULL) OR "
    + " (decode(H.MESSAGE_KBN,1,B.YUKO_KIGEN,4,B.YUKO_KIGEN, SYSDATE + 1) "
    + " >= to_date(to_char(SYSDATE,'dd-mon-yyyy'),'dd-mon-yyyy hh24:mi') ) ) "
 	+ " AND (message_kbn = '1' OR message_kbn = '4' OR message_kbn = '2' OR message_kbn = '3' OR message_kbn = '5') "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID)= ? "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID) = mr.mr_id "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID)= ? "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID)= dr.dr_id "
  + " AND DECODE(H.MESSAGE_KBN,1,SEND_STATUS,4,SEND_STATUS,6,0,RECEIVE_STATUS) = 1";

 protected static final String SQL_NUMBER_OF_MESSAGES =
 	"SELECT count(*)  AS COUNT "
 	+ "FROM MESSAGE_HEADER H,MESSAGE_BODY B, "
 	+ "mr mr, "
 	+ "doctor dr "
 	+ "WHERE	H.MESSAGE_ID=B.MESSAGE_ID "
 	+ " AND ( (B.YUKO_KIGEN is NULL) OR (decode(H.MESSAGE_KBN,1,B.YUKO_KIGEN,4,B.YUKO_KIGEN, SYSDATE + 1) "
    + " >= to_date(to_char(SYSDATE,'dd-mon-yyyy'),'dd-mon-yyyy hh24:mi') ) ) "
 	+ " AND (message_kbn = '1' OR message_kbn = '4' OR message_kbn = '2' OR message_kbn = '3' OR message_kbn = '5') "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID)= ? "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID) = mr.mr_id "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID)= ? "
 	+ " AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID)= dr.dr_id "
  + " AND DECODE(H.MESSAGE_KBN,1,SEND_STATUS,4,SEND_STATUS,6,0,RECEIVE_STATUS) = 1";
*/

 protected static String SQL_MESSAGE_LIST_V2(boolean isDoctor)
 {
  return
 	"SELECT H.MESSAGE_KBN AS KBN,H.RECEIVE_TIME RECEIVE_TIME,B.TITLE TITLE, "
 	+ "H.MESSAGE_HEADER_ID MESSAGE_HEADER_ID, H.RECEIVE_TIMED RECEIVE_TIMED, "
 	+ "H.RECEIVE_STATUS RECEIVE_STATUS, H.SEND_STATUS SEND_STATUS, "
 	+ "H.FROM_USERID FROM_USERID, H.TO_USERID TO_USERID "
 	+ "FROM message_header h, message_body b "
 	+ "WHERE h.message_id = b.message_id "
 	+ "AND ( "
 	+ "     (h.message_kbn IN ('1', '4') " // this is an EDetail
 	+ "      AND h.from_userid = ? " // MR Id
 	+ "      AND h.to_userid = ? " // DR Id
 	+ "      AND h." + (isDoctor?"receive_status":"send_status") + " = '1' "
 	+ "      AND (b.yuko_kigen IS NULL OR b.yuko_kigen >= trunc(SYSDATE))) "
 	+ "     OR "
 	+ "     (h.message_kbn IN ('2', '3', '5') " // this is a Contact
 	+ "      AND h.to_userid = ? " // MR Id
 	+ "      AND h.from_userid = ? " // DR Id
 	+ "      AND h." + (isDoctor?"send_status":"receive_status") + " = '1') "
 	+ "    ) ";
 }

 protected static String SQL_NUMBER_OF_MESSAGES_V2(boolean isDoctor)
 {
  return
 	"SELECT count(1) AS count "
 	+ "FROM message_header h, message_body b "
 	+ "WHERE h.message_id = b.message_id "
 	+ "AND ( "
 	+ "     (h.message_kbn IN ('1', '4') " // this is an EDetail
 	+ "      AND h.from_userid = ? " // MR Id
 	+ "      AND h.to_userid = ? " // DR Id
 	+ "      AND h." + (isDoctor?"receive_status":"send_status") + " = '1' "
 	+ "      AND (b.yuko_kigen IS NULL OR b.yuko_kigen >= trunc(SYSDATE))) "
 	+ "     OR "
 	+ "     (h.message_kbn IN ('2', '3', '5') " // this is a Contact
 	+ "      AND h.to_userid = ? " // MR Id
 	+ "      AND h.from_userid = ? " // DR Id
 	+ "      AND h." + (isDoctor?"send_status":"receive_status") + " = '1') "
 	+ "    ) ";
 }
/*
 	protected static final String SQL_NUMBER_OF_MESSAGES_NEW =
	"SELECT count(*)  AS COUNT "
 	+ "FROM MESSAGE_HEADER H "
	+ "WHERE "
	+ " (message_kbn = '1' OR message_kbn = '4' OR message_kbn = '2' OR message_kbn = '3' OR message_kbn = '5') "
	+ " AND DECODE(H.MESSAGE_KBN,1,H.FROM_USERID,4,H.FROM_USERID,2,H.TO_USERID,3,H.TO_USERID,5,H.TO_USERID)= ? "
	+ " AND DECODE(H.MESSAGE_KBN,1,H.TO_USERID,4,H.TO_USERID,2,H.FROM_USERID,3,H.FROM_USERID,5,H.FROM_USERID)= ? "
 	+ " AND H.RECEIVE_STATUS = DECODE(H.MESSAGE_KBN,1,1,4,1,H.RECEIVE_STATUS) "
 	+ " AND H.SEND_STATUS = DECODE(H.MESSAGE_KBN,2,1,3,1,5,1,H.SEND_STATUS) ";
*/
 protected static final String ATTACH_FILE_SEQ_STRING
 	= "SELECT (TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || "
 	+ "TRIM(TO_CHAR(attach_file_seq.nextval,'00'))) filename, "
 	+ "TRIM(TO_CHAR(attach_file_seq.nextval,'00')) counter FROM dual";

 protected static final String SQL_SEARCH_BY_ID =
  "SELECT h.message_header_id, h.from_userid, h.to_userid, b.title, "
  + "b.message_honbun, h.receive_time, h.receive_timed, h.send_delete_time, "
  + "h.receive_delete_time, h.send_torikeshi_time, b.call_naiyo_cd, "
  + "b.picture_cd, h.message_id, h.message_kbn, b.url "
  + "FROM message_header h, message_body b "
  + "WHERE h.message_header_id = ? AND h.message_id = b.message_id";

 protected static final String SQL_SEARCH_BY_ID_LIST_PRE =
  "SELECT h.message_header_id, h.from_userid, h.to_userid, b.title, "
  + "b.message_honbun, h.receive_time, h.receive_timed, h.send_delete_time, "
  + "h.receive_delete_time, h.send_torikeshi_time, b.call_naiyo_cd, "
  + "b.picture_cd, h.message_id, h.message_kbn, b.url "
  + "FROM message_header h, message_body b "
  + "WHERE h.message_id = b.message_id "
  + " AND NVL(b.yuko_kigen,SYSDATE + 1) "
  + " >= to_date(to_char(SYSDATE,'dd-mon-yyyy'),'dd-mon-yyyy hh24:mi') "
  + " AND h.message_header_id IN (";

 protected static final String SQL_SEARCH_BY_ID_LIST_POST = ") ORDER BY h.receive_time DESC";
 
 protected static final String SQL_LOAD_MESSAGE_ATTACHMENTS =
  "SELECT seq, attach_file, file_name FROM attach_file WHERE message_id = ?";

 protected static final String SQL_LOAD_MESSAGE_RESOURCES =
  "SELECT seq, honbun_text, url FROM attach_link WHERE message_id = ?";

 protected static final String WEB_IMAGE_QUERY =
  "SELECT picture_cd, picture_name, picture_type, jikosyokai, picture "
  + "FROM catch_picture WHERE picture_cd = ?";

/*
  protected static final String SQL_MR_DELETE_MESSAGES =
   "UPDATE MESSAGE_HEADER SET "
   + " SEND_STATUS = DECODE(MESSAGE_KBN,1,3,4,3,SEND_STATUS), "
   + " RECEIVE_STATUS = DECODE(MESSAGE_KBN,2,3,3,3,5,3,RECEIVE_STATUS), "
   + " SEND_DELETE_TIME = DECODE(MESSAGE_KBN,1,SYSDATE,4,SYSDATE,SEND_DELETE_TIME), "
   + " RECEIVE_DELETE_TIME = DECODE(MESSAGE_KBN,2,SYSDATE,3,SYSDATE,5,SYSDATE,RECEIVE_DELETE_TIME) ";
*/
   
  protected static final String SQL_DELETE_MESSAGES_V2(boolean isDoctor)
  {
    if (isDoctor)
      return "UPDATE MESSAGE_HEADER SET "
           + " RECEIVE_STATUS = DECODE(MESSAGE_KBN,1,3,4,3,RECEIVE_STATUS), "
           + " RECEIVE_DELETE_TIME = DECODE(MESSAGE_KBN,1,SYSDATE,4,SYSDATE,RECEIVE_DELETE_TIME), "
           + " SEND_STATUS = DECODE(MESSAGE_KBN,2,3,3,3,5,3,SEND_STATUS), "
           + " SEND_DELETE_TIME = DECODE(MESSAGE_KBN,2,SYSDATE,3,SYSDATE,5,SYSDATE,SEND_DELETE_TIME) ";
    else
      return "UPDATE MESSAGE_HEADER SET "
           + " SEND_STATUS = DECODE(MESSAGE_KBN,1,3,4,3,SEND_STATUS), "
           + " SEND_DELETE_TIME = DECODE(MESSAGE_KBN,1,SYSDATE,4,SYSDATE,SEND_DELETE_TIME), "
           + " RECEIVE_STATUS = DECODE(MESSAGE_KBN,2,3,3,3,5,3,RECEIVE_STATUS), "
           + " RECEIVE_DELETE_TIME = DECODE(MESSAGE_KBN,2,SYSDATE,3,SYSDATE,5,SYSDATE,RECEIVE_DELETE_TIME) ";
  }
  
   protected static final String SQL_MR_RETRACT_MESSAGE =
    "UPDATE MESSAGE_HEADER SET "
    + " SEND_STATUS = 3, "
    + " SEND_TORIKESHI_TIME = SYSDATE, "
    + " RECEIVE_STATUS = 3, "
    + " RECEIVE_DELETE_TIME = SYSDATE"
	+ " WHERE MESSAGE_HEADER_ID = ? "
	+ " AND (MESSAGE_KBN = 1 OR MESSAGE_KBN = 4) "
	+ " AND (RECEIVE_TIMED IS NULL)";

   protected static final String SQL_SET_READ_DATE =
   "UPDATE message_header "
   + "SET receive_timed = sysdate "
   + "WHERE message_header_id = ?";
   	public MessageHelperManager()
	{
	}


	public void addMessage(User u, Collection r, Message b, String plainText, int kbn, String sessionId)
	{
		this.recipients = r;
		this.msg = b;
		this.text = plainText;

		//System.out.println("addMessage.1: msg is "+msg.toString() + 
		//	" body is "+ msg.getBody());
		vctRecipients = makeRecipientVector(r);  
		
       	doSend(u,b,kbn, sessionId);
	}		
    
	public void addMessage(User u, Collection r, Message b, String plainText, String sessionId)
	{
		addMessage(u,r,b,plainText,0, sessionId);	
	
	} // addMessage
    
	Vector makeRecipientVector(Collection in)
	{
	 Vector ret = new Vector();
	 
	 // System.out.println("makeRecipientVector.1");
	 Iterator iter = in.iterator();
	 // System.out.println("makeRecipientVector.2");
	 
	 while (iter.hasNext())
	 {
	   String to = (String) iter.next();
	   ret.add(to);
     }
     // System.out.println("makeRecipientVector.3");
	 
	 return(ret);
	}	

  /**
   * Send an email from the specified address to the specified address list
   * with the subject and body provided. This is essentially a wrapper around
   * a call to a JavaMail provider.<br>
   * <em>Implicit assumption - the emails sent by this method suppress the list
   * of recipients somehow when the message is sent.</em>
   * @param fromAddress A valid email sender address.
   * @param toAddressList A collection of Strings, containing recipient email addresses
   * @param subject The subject for the email
   * @param body The content of the message to be sent.
   */
  public void sendEmailMessage(String     fromAddress,
  		String fromName,Collection toAddressList,String     subject,
        String     body, String sessionId)
  {

    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"sendEmailMessage\",\"DAOStart\"");
  	Connection conn = null;
    ConstantMasterTableManager constantmastertablemanager = null;
    ConstantMasterTable constantmaster = null;

    try
    {
    	conn = getConnection();
    	constantmastertablemanager
	    	= new ConstantMasterTableManager(conn);
    	constantmaster = constantmastertablemanager.getConstantMasterTable("SMTPSRV");
   	}
    catch(SQLException e)
    {
    	throw new ApplicationError("MessageHelperManager.sendEmailMessage: SQLException",e);
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
    

     System.out.println("MessageHelperManager.sendEmailMessage: sending to "+toAddressList.size()+" addresses");
    Iterator iter = toAddressList.iterator();
    
    while (iter.hasNext())
    {
      MailUtil mailUtil = new MailUtil(constantmaster.getNaiyo1());

      mailUtil.setFrom(fromAddress,fromName);
      mailUtil.setSubject(subject);
      EmailContact address = (EmailContact) iter.next();
      mailUtil.setTo(address.getEmailAddress(),address.getName());      
      mailUtil.setText(body);
      mailUtil.send();
      System.out.println("MessageHelperManager.sendEmailMessage: sending from:"
       +fromName+" to "+address.getEmailAddress());
    }
    // System.out.println("makeRecipientVector.3");
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"sendEmailMessage\",\"DAOEnd\"");

    
  }


  /**
   * Loads a single Message by it's messageId
   */
  public Message getMessageById(String messageId, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessageById\",\"DAOStart\"");
    Connection conn = null;
    try
    {
      conn = getConnection();
      
      // Create the query and populate a return object
	    PreparedStatement searchQuery = conn.prepareStatement(SQL_SEARCH_BY_ID);
	    searchQuery.setObject(1, messageId);
	    ResultSet results = searchQuery.executeQuery();
      Object found   = populateObject(conn, results, sessionId);
      results.close();
      searchQuery.close();

    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessageById\",\"DAOEnd\"");
      return (Message) found;
    }
    catch (SQLException errSQL)
    {
      throw new ApplicationError("Error retrieving a message by id", errSQL);
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
  }

  /**
   * Set the read date on this message
   */
  public void setMessageAsRead(Message msg, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"setMessageAsRead\",\"DAOStart\"");
    try
    {
      // Set the message as read
      Connection conn = getConnection();
      PreparedStatement setReadDate = conn.prepareStatement(SQL_SET_READ_DATE);
      setReadDate.setString(1, msg.getMessageId());
      if (setReadDate.executeUpdate() != 1)
        throw new ApplicationError("Error setting the read date on msg " + msg.getMessageId());
      setReadDate.close();
      conn.close();
    }
    catch (SQLException errSQL)
    {
      throw new ApplicationError("Error setting the read date on the message", errSQL);
    }
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"setMessageAsRead\",\"DAOEnd\"");
  }

  /**
   * Loads multiple Messages by messageId
   */
  public Collection getMessages(Collection messageIdList, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessages\",\"DAOStart\"");
    Connection conn = null;
    try
    {
      conn = getConnection();
      
      // Create the query and populate a return object
	    StringBuffer sqlText = new StringBuffer();
      int size = messageIdList.size();
      for (int nLoop = 0; nLoop < size;  nLoop++)
    		sqlText.append(", ?");

      PreparedStatement searchQuery = conn.prepareStatement(SQL_SEARCH_BY_ID_LIST_PRE +
	                                                        sqlText.substring(2) + 
															SQL_SEARCH_BY_ID_LIST_POST);
      int nPosition = 1;
      for (Iterator i = messageIdList.iterator(); i. hasNext(); )
        searchQuery.setObject(nPosition++, (String) i.next());
	  
      ResultSet results = searchQuery.executeQuery();
      Collection returnList = new ArrayList();
      Object oneMessage = populateObject(conn, results, sessionId);
      while (oneMessage != null)
      {
  		  returnList.add(oneMessage);
        System.out.println("getMessages.collection: one message returned");
        oneMessage = populateObject(conn, results, sessionId);
      }

      results.close();
      searchQuery.close();
      ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessages\",\"DAOEnd\"");
      return returnList;
    }
    catch (SQLException errSQL)
    {
      throw new ApplicationError("Error retrieving a message by id", errSQL);
    }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
  }

  /**
   * Loads available Messages, between the from and to Ids, and
   * matching the sent or received flag value. Assumes that it is an MR that's asking.
   */
  public Collection getMessages(String  mrId,
                                String  drId,
                                String  sortOrder,
                                Integer messageCountFrom,
                                Integer messageCountTo,
                                String sessionId)
  {
    return getMessages(mrId,
                       drId,
                       sortOrder,
                       messageCountFrom,
                       messageCountTo,
                       false,
                       MessageHelperManager.ANY_MESSAGE,
                       sessionId);
  }

  /**
   * Assumes that it is an MR that's asking.
   */
  public Collection getMessages(String  mrId,
                                String  drId,
                                String  sortOrder,
                                Integer messageCountFrom,
                                Integer messageCountTo,
                                boolean unreadOnly,
                                String  filterType,
                                String  sessionId)
  {
    return getMessages(mrId,
                       drId,
                       sortOrder,
                       messageCountFrom,
                       messageCountTo,
                       unreadOnly,
                       filterType,
                       false,
                       sessionId);
  }

  public Collection getMessages(String  mrId,
                                String  drId,
                                String  sortOrder,
                                Integer messageCountFrom,
                                Integer messageCountTo,
                                boolean unreadOnly,
                                String  filterType,
                                boolean requestingUserIsDoctor,
                                String  sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessages\",\"DAOStart\"");
    Connection conn = null;
	  ResultSet rs;
    PreparedStatement pstmt;
    // Build a map of params
    Map params = new Hashtable();
    params.put("mrId", mrId);
    params.put("drId", drId);
    params.put("sortOrder", sortOrder);
    params.put("messageCountFrom", messageCountFrom);
    params.put("messageCountTo", messageCountTo);
    Collection ret = new Vector();
    
	try
	{
		conn = getConnection();

		//StringBuffer sqlTextBuffer = new StringBuffer(SQL_MR_MESSAGE_LIST);
		StringBuffer sqlTextBuffer = new StringBuffer(SQL_MESSAGE_LIST_V2(requestingUserIsDoctor));
    if (unreadOnly)
      sqlTextBuffer.append(" AND h.receive_timed is NULL ");
    if (filterType.equals(MessageHelperManager.CONTACTS_ONLY))
      sqlTextBuffer.append(" AND h.message_kbn in ('2', '3', '5') ");
    else if (filterType.equals(MessageHelperManager.EDETAILS_ONLY))
      sqlTextBuffer.append(" AND h.message_kbn in ('1', '4') ");
    else if (filterType.equals(MessageHelperManager.ALL_DR_MESSAGES))
      sqlTextBuffer.append(" AND h.message_kbn NOT IN ('3') ");
		sqlTextBuffer.append(SQL_MESSAGE_LIST_orderBy.get(sortOrder));
		String sqltext = sqlTextBuffer.toString();
    //System.out.println("getMessages.1: sqltext is:" + sqltext);
		pstmt = conn.prepareStatement(sqltext);
		pstmt.setString(1,mrId);
		pstmt.setString(2,drId);
		pstmt.setString(3,mrId);
		pstmt.setString(4,drId);
		rs   = pstmt.executeQuery();
		int cnt = 0;
		int from = messageCountFrom.intValue();
		int to = messageCountTo.intValue();
		
		while (rs.next())
		{
		 Message m = null;

		 if ( cnt >= from && cnt <= to )
		 {

		  String value = rs.getString("KBN");
		  String status = new String("0");
		  if (value.equals("1") || value.equals("4"))
		  {
			   m = new EDetail();
		  }
		  else if (value.equals("2") || value.equals("3") || value.equals("5") || value.equals("6"))
		  {
		  	m = new Contact();
		  }
		  else // this would be weird
		  {
		   throw new ApplicationError("MessageHelperManager.getMrMessages: undefined MESSAGE_KBN");
		  }

			  value = rs.getString("MESSAGE_HEADER_ID");
			  m.setMessageId(value);
			  value = rs.getString("FROM_USERID");
			  m.setSender(value);
			  value = rs.getString("TO_USERID");
			  m.setRecipient(value);
			  value = rs.getString("TITLE");
			  m.setTitle(value);
			  java.sql.Date valueDate = rs.getDate("RECEIVE_TIMED");
			  m.setReadDate(valueDate);
			  valueDate = rs.getDate("RECEIVE_TIME");
			  m.setSentDate(valueDate);
			  ret.add(m);

		 } // if ( cnt >= from && cnt <= to )
		 
     cnt++;
		} // while rs.next()
	 
	} // try
	catch(SQLException e)
	{
		throw new ApplicationError("MessageHelperManager.getMessages: SQLException",e);
	}	
	finally
	{
		try
		{
			conn.close();
		}
		catch(SQLException e) {}
		

	}
	
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMessages\",\"DAOEnd\"");
	return(ret);
  }

  /**
   * Get a count of the available messages for this MR/DR combination. Assumes an
   * MR is asking.
   * @param mrId The MR to filter for.
   * @param drId The DR to filter for.
   * @param filterType The options for filtering (eg contactsOnly or edetailsOnly)
   * @returns An Integer representing the number of messages available.
   */
  public Integer getAvailableMessages(String  mrId,
                                      String  drId,
                                      boolean unreadOnly,
                                      String  filterType,
                                      String  sessionId)
  {
    return getAvailableMessages(mrId, drId, unreadOnly, filterType, false, sessionId);
  }


  /**
   * Get a count of the available messages for this MR/DR combination
   * @param mrId The MR to filter for.
   * @param drId The DR to filter for.
   * @param filterType The options for filtering (eg contactsOnly or edetailsOnly)
   * @returns An Integer representing the number of messages available.
   */
  public Integer getAvailableMessages(String  mrId,
                                      String  drId,
                                      boolean unreadOnly,
                                      String  filterType,
                                      boolean requestingUserIsDoctor,
                                      String  sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getAvailableMessages\",\"DAOStart\"");
    // hb010814 DAOFacade messageDAO = new DAOFacade(Message.class);
	  Integer ret = null;
	  Connection conn = null;
	

	  try
	  {
    	conn = getConnection();
	    ResultSet rs;
    	PreparedStatement pstmt;

		  // hb010815	StringBuffer sqlTextBuffer = new StringBuffer(SQL_NUMBER_OF_MESSAGES);
		  //StringBuffer sqlTextBuffer = new StringBuffer(SQL_NUMBER_OF_MESSAGES);
		  StringBuffer sqlTextBuffer = new StringBuffer(SQL_NUMBER_OF_MESSAGES_V2(requestingUserIsDoctor));
      if (unreadOnly)
        sqlTextBuffer.append(" AND h.receive_timed IS NULL");
      if (filterType.equals(MessageHelperManager.CONTACTS_ONLY))
        sqlTextBuffer.append(" AND h.message_kbn IN ('2', '3', '5') ");
      else if (filterType.equals(MessageHelperManager.EDETAILS_ONLY))
        sqlTextBuffer.append(" AND h.message_kbn IN ('1', '4') ");
      else if (filterType.equals(MessageHelperManager.ALL_DR_MESSAGES))
        sqlTextBuffer.append(" AND h.message_kbn NOT IN ('3') ");
		  //System.out.println("getAvailableMessages.1: sqltext is " + sqlTextBuffer.toString());
	    pstmt = conn.prepareStatement(sqlTextBuffer.toString());
    	pstmt.setString(1, mrId);
	    pstmt.setString(2, drId);
    	pstmt.setString(3, mrId);
	    pstmt.setString(4, drId);
    	rs   = pstmt.executeQuery();
		  if (rs.next())
		  {
				ret = new Integer(rs.getInt("count"));
	  		//System.out.println("getAvailableMessages.2: "+ ret.intValue() + " messages available for dr: " + drId + " and mr: " + mrId);
		  }
		  else
		  {
			  //System.out.println("getAvailableMessages.3: SQL_NUMBER_OF_MESSAGES returned empty ResultSet");
			  ret = new Integer(0);
		  }
	  } // try
	  catch(SQLException e)
	  {
	    throw new ApplicationError("MessageHelperManager.getAvailableMessages: SQLException",e);
	  }
	  finally
  	{
	  	try
		  {
			  conn.close();
		  }
		  catch(SQLException e) {}
  	}
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getAvailableMessages\",\"DAOEnd\"");
	  return(ret);
  }

  /**
   * Remove all the messages matching the ids specified in the collection supplied.
   * Assumes an MR is requesting
   */
  public void removeMessages(Collection messageIdList, String sessionId)
  {
    removeMessages(messageIdList, false, sessionId);
  }

  /**
   * Remove all the messages matching the ids specified in the collection supplied.
   */
  public void removeMessages(Collection messageIdList, boolean requestingUserIsDoctor, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"removeMessages\",\"DAOStart\"");
	String sqltext = null;
	Iterator iter = messageIdList.iterator();
	StringBuffer whereClause = null;
	
	//whereClause = new StringBuffer(SQL_MR_DELETE_MESSAGES);
	whereClause = new StringBuffer(SQL_DELETE_MESSAGES_V2(requestingUserIsDoctor));

	int size = messageIdList.size();
	if (size == 0)
	{
		System.out.println("MessageHelperManager.removeMessages: Collection.size() = 0");
		return;		// ERROR
	}
	else if (size == 1)
	{
		whereClause.append(" WHERE MESSAGE_HEADER_ID in ('");
		whereClause.append(iter.next()).append("')");
	}
	else if (size > 1)
	{
		whereClause.append(" WHERE MESSAGE_HEADER_ID in ('");
		whereClause.append(iter.next()).append("',");
		for (int i = 1; i < size - 1 ; i++)
		{
			whereClause.append("'").append(iter.next()).append("',");
		}
		whereClause.append("'").append(iter.next()).append("')");
	}
	sqltext = whereClause.toString();
	//System.out.println("MessageHelperManager.removeMessages: sqltext is "+sqltext);
  Connection conn = null;
	try
	{
		conn = getConnection();
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sqltext);
	}
	catch(SQLException e)
	{
		throw new ApplicationError("MessageHelperManager.removeMessages: SQLException",e);
	}			
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"removeMessages\",\"DAOEnd\"");
  } // .removeMessages()

  /**
   * This will return a map that contains the DR's (the key for the hash map)
   * message count.
   * @param mr The MR to be filtered for.
   * @param indicator 
   * @returns Map that contains the drId and their message count.
   */
  public Map getMRMessageCount(MR mr, String indicator, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMRMessageCount\",\"DAOStart\"");
    // Get a DAO
    Connection conn = null;
	ResultSet rs;
	PreparedStatement pstmt;
	
	Map ret = new Hashtable();
	int action = -1;
	boolean flag = false;
	
	try
	{
		conn = getConnection();

		StringBuffer sqlTextBuffer = new StringBuffer(SQL_MR_MESSAGE_COUNT);
		String sqltext = sqlTextBuffer.toString();
		//System.out.println("MessageHelperManager.getMRMessageCount.1: sql=" + sqltext);
		pstmt = conn.prepareStatement(sqltext);
		pstmt.setString(1,mr.getMrId());
		rs   = pstmt.executeQuery();
		
		while (rs.next())
		{

			flag = false;
			String drId = rs.getString("DR_ID");			
			String mrId = rs.getString("MR_ID");			
			DRMessageCount dr = new DRMessageCount();
			dr.setDrId(drId);
			//System.out.println("MessageHelperManager.getMRMessageCount: processing DR "+drId);
			


			int newContact = rs.getInt("newRenraku");
			dr.setNewContacts(newContact);
			//System.out.println("MessageHelperManager.getMRMessageCount: "+newContact+ " new contacts from "+drId);
			if (newContact > 0)
			{
				action = 0;
				flag = true;
			}
			if (newContact == 1)
			{
				sqlTextBuffer = new StringBuffer(SQL_GET_UNIQUE_CONTACT_ID);
				sqltext = sqlTextBuffer.toString();
				//System.out.println("MessageHelperManager.getMRMessageCount.3: unique Contact from "+drId);
				pstmt = conn.prepareStatement(sqltext);
				pstmt.setString(1,mrId);
				pstmt.setString(2,drId);
				ResultSet rs1   = pstmt.executeQuery();
				if (rs1.next())
				{
					int cnt = rs1.getInt("count");
					if (cnt != 1) // this should be impossible here
					{
						throw new ApplicationError("MessageHelperManager.getMRMessageCount: received more than one unique Contact ID");
					
					}
					dr.setNewContactId(rs1.getString("message_header_id"));
				}
				else // shouldn't happen
				{
					throw new ApplicationError("MessageHelperManager.getMRMessageCount: received empty RS on unique Contact ID");
				
				}
			
			} // new Msg == 1



			int newDetail = rs.getInt("midokuEdetail");
			//System.out.println("MessageHelperManager.getMRMessageCount.3: "+newDetail+" newDetails for "+drId);
			dr.setNewEDetails(newDetail);


			if (newDetail == 0 && newContact == 0 && flag == false)
			{
				action = 1;
				flag = true;
			}
			else if (newContact == 0 && newDetail > 0)
			{
			
				action = 2;
				flag = true;
			}

			if ( ! flag )
			{
			
				throw new ApplicationError("MessageHelperManager.getMRMessageCount: action logic failed");
			}
			else
			{
				dr.setActionValue(action);
			}

			if (newDetail == 1)
			{
				sqlTextBuffer = new StringBuffer(SQL_GET_UNIQUE_DETAIL_ID);
				sqltext = sqlTextBuffer.toString();
				pstmt = conn.prepareStatement(sqltext);
				pstmt.setString(1,mrId);
				pstmt.setString(2,drId);
				ResultSet rs2   = pstmt.executeQuery();
				if (rs2.next())
				{
					int cnt = rs2.getInt("count");
					if (cnt != 1) // this should be impossible here
					{
						throw new ApplicationError("MessageHelperManager.getMRMessageCount: received more than one unique Detail ID");
					
					}
					dr.setNewEDetailId(rs2.getString("message_header_id"));
					//System.out.println("MessageHelperManager.getMRMessageCount.4: unique Detail ID is" + rs2.getString("message_header_id"));
					
				}
				else // shouldn't happen
				{
					throw new ApplicationError("MessageHelperManager.getMRMessageCount: received empty RS on unique Detail ID");
			
				}
			
			} // new Msg == 1

			//System.out.println("MessageHelperManager.getMRMessageCount.5: adding "+drId+" to map");
			ret.put(drId,dr);
						
		} // while rs.next()		
	
	}
	catch(SQLException e)
	{
	throw new ApplicationError("MessageHelperManager.getMRMessageCount: SQLException",e);
			
	
	}
	finally
	{
		try
		{
		  conn.close();
		}
		catch (SQLException e)
		{
		}
	} // finally	
	//System.out.println("MessageHelperManager.getMRMessageCount: returning "+ret.size()+" DRInformationCounts");
 	
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getMRMessageCount\",\"DAOEnd\"");
    return ret;
  } // getMRMessageCount()



  	/*
    * this method sets fields in the legacy objects
    * it differentiates between EDetail and Contact
    * @param conn JDBC connection
    * @param from the sender. this gets casted to MR or DR
    * @param msg the message. this gets casted to EDetail or Contact
    * @param msgtbl master message table (legacy)
    * @param body message body table (legacy)
    * @param head message header table (legacy)
    */
    void setMsgTbl(Connection conn,User from, Message msg,
     MessageTable msgtbl, MessageBodyTable body, MessageHeaderTable head, String sessionId)
     {
     	setMsgTbl(conn,from,msg,msgtbl,body,head,0,sessionId);
    }


    /*
    * this method sets fields in the legacy objects
    * it differentiates between EDetail and Contact
    * @param conn JDBC connection
    * @param from the sender. this gets casted to MR or DR
    * @param msg the message. this gets casted to EDetail or Contact
    * @param msgtbl master message table (legacy)
    * @param body message body table (legacy)
    * @param head message header table (legacy)
    */
    void setMsgTbl(Connection conn,User from, Message msg, MessageTable msgtbl,
      MessageBodyTable body, MessageHeaderTable head,int kbn, String sessionId)
    {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"setMsgTbl\",\"DAOStart\"");


    	// this is specific to EDetails
        if ( from instanceof MR)
        {
        	MR mr = (MR) from;
            EDetail dtl = (EDetail) msg;
            String mrid = mr.getMrId();


            head.setMessageKbn("1");
            head.setFromUserID(mr.getMrId());
            head.setCcFlg("0");

            /*
            * it shouldn't be necessary to call the old MrInfoManager
            * with its full database access
            *
		    MrInfoManager MrManager = new MrInfoManager(conn);
		    MrInfo mrinfo = MrManager.getMrLoginInfo(mrid);
		    String JikoSyokai = mrinfo.getJikosyokai();
		    */
            String JikoSyokai = mr.getJikoSyokai();
		    if ( JikoSyokai != null )
		    {
			    body.setJikosyokai(JikoSyokai);
		    }
		    String pictureCD = null;
		    if (dtl.getImage() != null)
		    {

		     pictureCD = dtl.getImage().getWebImageId();

		    }
		    if (pictureCD != null)
		    {
		    	body.setPictureCD(pictureCD); 
		    }
		    String value = mr.getCompany().getCompanyId();
		    if (value != null) 
		    {
		    	body.setCompanyCD(value);
		    }
		    value = dtl.getEDetailCategory();
		    if ( value != null) 
            {
		    	body.setCallNaiyoCD(value);
		    }
		    if (dtl.getExpireDate() != null) 
		    {
		    	SimpleDateFormat formatter
		    	 = new SimpleDateFormat ("yyyyMMdd");
		    	String dateString = formatter.format(dtl.getExpireDate());
		
		    	body.setYukoKigen(dateString); 
		    }

			if (dtl.getResourceList() != null)
			{

				Iterator urls = dtl.getResourceList().iterator();
				System.out.println("MessageHelperManager.doSend: processing "+dtl.getResourceList().size()+" urls");


				Vector linktbl_work  = new Vector();
	

			
				String linkUrl = null;
				String linkName = null;
				while(urls.hasNext())
				{
				 ResourceLink link = (ResourceLink) urls.next();
				 AttachLinkTable  atlinktbl = new AttachLinkTable();
				 linkUrl = link.getURL();
				 linkName = link.getName();
				 System.out.println("MessageHelperManager.doSend: adding url="+linkUrl);
				 
				atlinktbl.setUrl(linkUrl);
				atlinktbl.setHonbuText(linkName);
				linktbl_work.addElement(atlinktbl);
			
				}
				msgtbl.setAttachLTable(linktbl_work.elements());
			} // if dtl.getResourceList()
		} // if ( from instanceof MR)
        else if ( from instanceof DR )
        {
        	DR dr = (DR) from;
            if ( kbn == 0)
            {
	            head.setMessageKbn("2");
            }
            else
            {
            	head.setMessageKbn("3");
            }
            head.setFromUserID(dr.getDrId());
            
            // this is needed because MessageTableManager expects
            // the .elements() of this vector
            Vector  attachtbl_work  = new Vector(); 
    
            msgtbl.setAttachFTable(attachtbl_work.elements());
            msgtbl.setAttachLTable(attachtbl_work.elements());
            
        }
        
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"setMsgTbl\",\"DAOEnd\"");
    } // setMsgTbl()
    

    boolean doSend ( User from, Message message, String sessionId)
    {
    	return(doSend(from,message,0, sessionId));
    }

    boolean doSend( User from, Message message, int kbn, String sessionId)
	{
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"doSend\",\"DAOStart\"");
		Connection conn = null;
		java.util.Date b = new java.util.Date();
		SimpleDateFormat f = new SimpleDateFormat ("yyyy.MM.dd/hh:mm:ss:SSSS");
        
		System.out.println("MessageHelperManager.doSend: start at "+f.format(b));
		
		// DB Connection
		try
		{
			conn  = getConnection();
		}
		catch(SQLException e)
		{
			throw new ApplicationError("MessageHelperManager.doSend SQLException",e);
		}
		try 
        {

			MessageTableManager manager = new MessageTableManager(conn);
			MessageTable msgtbl  = new MessageTable();
			MessageHeaderTable msgtblhead = new MessageHeaderTable();
			MessageBodyTable msgtblbody = new MessageBodyTable();

			Vector   recipients = new Vector();
            recipients.addAll(vctRecipients);

            
            setMsgTbl(conn, from, message,msgtbl, msgtblbody, msgtblhead, kbn, sessionId);
			

			String value = message.getTitle();
			if ( value != null) 
			{
				msgtblbody.setTitle(value);
			}

			String plainMessage = null;
			String str = message.getBody();

			if (str != null) 
			{
			
				plainMessage = this.text;

				if (plainMessage == null) 
                {
					plainMessage = str;
				}

				msgtblbody.setMessageHonbun(str);
			
			}
			 
			msgtbl.setMsgHTable(msgtblhead);
			msgtbl.setMsgBTable(msgtblbody);
 
			AttachFileTable  atfiletbl;
			atfiletbl  = new AttachFileTable();
			Vector filetbl_work = new Vector();

			if (message.getAttachmentList() != null)
			{

			 Iterator attachments = message.getAttachmentList().iterator();
			 while (attachments.hasNext())
			 {
				
				atfiletbl  = new AttachFileTable();
				AttachmentLink attach_file = (AttachmentLink) attachments.next();
				String filename = attach_file.getFileName();
				// System.out.println("MessageHelpermanager.doSend: name(attach_file) is "+filename);
				atfiletbl.setAttachFile(filename);
				String seq = null;
				


				try {
				    Statement stmt = conn.createStatement();
				    try 
					{
						ResultSet rsAttach = stmt.executeQuery(ATTACH_FILE_SEQ_STRING);
						while ( rsAttach.next() ) 
						{
							seq = rsAttach.getString("counter");
						    filename = rsAttach.getString("filename");
							
						}
				    } 
					finally 
					{
						stmt.close();
				    }
				    /*
				    * <hb010810
				    * this sequence number is provided 
				    * by AttachFileInfoManager
				    * atfiletbl.setSeq((String)session.getValue("upload_file_Seq"));
				    */
				    atfiletbl.setSeq(seq);
				    //System.out.println("MessageHelpermanager.doSend: seq(attach_file) is "+seq);
					
				    /* 
				    * Make sure to use this hb0108010>
				    */
				    atfiletbl.setFileKbn("0");
				    filetbl_work.addElement(atfiletbl);
				}
				catch(SQLException e)
			 	{
					throw new ApplicationError("MessageHelpermanager.doSend: SQLException.attachfile",e);
				}



			 }  // while attachments.hasNext()
			} // if getAttachmentList != null							
			// END REWRITE hb010810
			msgtbl.setAttachFTable(filetbl_work.elements());

            java.util.Date start = new java.util.Date();
            
			String msgID = manager.insert(recipients.elements(), msgtbl, null, plainMessage);
        //    System.out.println("MessageHelperManager.doSend: send msgId="+msgID);
			java.util.Date end = new java.util.Date();
            
		    System.out.println("MessageHelperManager.doSend posting message from "
             +f.format(start)+" to "+f.format(end));

		}
        catch(Exception e)
        {
			throw new ApplicationError("MessageHelperManager.doSend",e);
        }
        finally {
			try
			{
				conn.close();
			}
			catch(SQLException e)
			{
			}
			
		}

        System.out.println("MessageHelperManager.doSend.1");
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"doSend\",\"DAOEnd\"");
		return true;
    }



  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection  conn,
                                ResultSet   attributes,
                                String      sessionId)
    throws SQLException
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateObject\",\"DAOStart\"");
    // Check the state of the resultset
    if (attributes == null)
      throw new NoObjectFoundException("Resultset was null");
    else if (!attributes.next())
      return null;
	  else
	  {
      // Determine whether or not the message is an EDetail by matching
      // the database fields with the conditions in supplied Map
      // eDetailCondition. If all fields match, we can build an eDetail,
      // otherwise a Contact.
      String kbn = attributes.getString("message_kbn");
      boolean  isEDetail  = true;
      if (kbn != null)
        isEDetail = (kbn.equals("1") || kbn.equals("4"));

 	    // Build a message object, determining whether to use an EDetail or
      // a Contact based on the messageTypeIndicator field.
      Message returnMessage = null;
      String message_body_id = attributes.getString(13);

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
        String imageID = attributes.getString(12);
        ResultSet webImageResults = null;
        if (imageID != null)
        {
          PreparedStatement searchQuery = conn.prepareStatement(WEB_IMAGE_QUERY);
          searchQuery.setObject(1, imageID);
          webImageResults = searchQuery.executeQuery();

          if (webImageResults.next())
          {
            WebImage resultWebImage = new WebImage();
            resultWebImage.setWebImageId(webImageResults.getString("picture_cd"));
            resultWebImage.setName(webImageResults.getString("picture_name"));
            resultWebImage.setImageType(webImageResults.getString("picture_type"));
            resultWebImage.setDescription(webImageResults.getString("jikosyokai"));
            resultWebImage.setFileName(webImageResults.getString("picture"));
            edetail.setImage(resultWebImage);
          }
          webImageResults.close();
          searchQuery.close();
        }

        // Load resources
        edetail.setResourceList(populateResourceList(conn, message_body_id, sessionId));
        String url = attributes.getString("url");
        if (  (url != null) && ! (url.equals("")) )
        {
        	System.out.println("MessageHelperManager.populateObject: receiving url=@"+url +"@");
            ResourceLink linkObject = new ResourceLink();
            linkObject.setURL(url);
            linkObject.setName(url);
	        edetail.getResourceList().add(linkObject);
        	
        }
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
      returnMessage.setAttachmentList(populateAttachmentList(conn, message_body_id, sessionId));

    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateObject\",\"DAOEnd\"");
      return returnMessage;
	  }
  }

  /**
   * This method loads the attachment list associated with the message.
   */
  private Collection populateAttachmentList(Connection conn, String messageId, String sessionId)
    throws SQLException
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateAttachmentList\",\"DAOStart\"");
    // Get a list of attachments
    PreparedStatement attQuery = conn.prepareStatement(SQL_LOAD_MESSAGE_ATTACHMENTS);
    attQuery.setObject(1, messageId);
    ResultSet attSet = attQuery.executeQuery();
	  Collection attachmentList = new ArrayList();

    // Add them to the list
    while (attSet.next())
    {
      AttachmentLink resultAttachment = new AttachmentLink();
      resultAttachment.setAttachmentLinkId(attSet.getString("seq"));
      resultAttachment.setName            (attSet.getString("attach_file"));
      resultAttachment.setFileName        (attSet.getString("attach_file"));
      attachmentList.add(resultAttachment);
    }
    attSet.close();
    attQuery.close();
	
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateAttachmentList\",\"DAOEnd\"");
	  return attachmentList;
  }

  /**
   * This method loads the resource list associated with the message.
   */
  private Collection populateResourceList(Connection conn, String messageId, String sessionId)
    throws SQLException
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateResourceList\",\"DAOStart\"");
    // Get a list of resources
    PreparedStatement resQuery = conn.prepareStatement(SQL_LOAD_MESSAGE_RESOURCES);
    resQuery.setObject(1, messageId);
    ResultSet resSet = resQuery.executeQuery();
	  Collection resourceList = new ArrayList();

    // Add them to the list
    while (resSet.next())
    {
      String url = resSet.getString("url");
      if ((url != null) && !url.equals(""))
      {
        ResourceLink link = new ResourceLink();
        link.setResourceLinkId(resSet.getString("seq"));
        link.setName(resSet.getString("honbun_text"));
        link.setURL(url);
        resourceList.add(link);
      }
    }
    resSet.close();
    resQuery.close();
	
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"populateResourceList\",\"DAOEnd\"");
	  return resourceList;
  }

  /**
   * Retracts a message that hasn't been read by the recipients yet
   */
  public void retractMessage(Message retractMessage, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"retractMessage\",\"DAOStart\"");
	  String id = retractMessage.getMessageId();
	  int numRecordsUpdated = -1;
	  Connection conn = null;
	  try
	  {
	  	  conn = getConnection();
	  	  //System.out.println("MessageHelperManager.retractMessage: preparing "+SQL_MR_RETRACT_MESSAGE+" id="+id);
		  PreparedStatement pstmt = conn.prepareStatement(SQL_MR_RETRACT_MESSAGE);
		  pstmt.setString(1,id);
		  numRecordsUpdated = pstmt.executeUpdate();
	  }
	  catch(SQLException e)
	  {
	  	throw new ApplicationError("MessageHelperManager.removeMessages: SQLException",e);
	  }
    finally
    {
      try
      {
        if (conn != null)
          conn.close();
      }
      catch (SQLException errSQL) {}
    }

	  System.out.println("MessageHelperManager.retractMessage: updated "+numRecordsUpdated+" records");
	  
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"retractMessage\",\"DAOEnd\"");
  } // retractMessage()
  
  
  /**
   * This will return a map that contains the DR's (the key for the hash map)
   * message count.
   * @param mr The MR to be filtered for.
   * @param indicator 
   * @returns Map that contains the drId and their message count.
   */
  public Map getDRMessageCount(DR dr, String indicator, String sessionId)
  {
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getDRMessageCount\",\"DAOStart\"");
    // Get a DAO
    Connection conn = null;
  ResultSet rs;
  PreparedStatement pstmt;
  
  Map ret = new Hashtable();
  
  try
  {
  	conn = getConnection();

  	StringBuffer sqlTextBuffer = new StringBuffer(SQL_DR_MESSAGE_COUNT);
  	String sqltext = sqlTextBuffer.toString();
  	//System.out.println("MessageHelperManager.getDRMessageCount.1: sql=" + sqltext);
  	pstmt = conn.prepareStatement(sqltext);
  	pstmt.setString(1,dr.getDrId());
  	rs   = pstmt.executeQuery();
  	
  	while (rs.next())
  	{

  		String drId = rs.getString("DR_ID");			
  		String mrId = rs.getString("MR_ID");			
  		MRMessageCount mrc = new MRMessageCount();
  		mrc.setMrId(mrId);
  		//System.out.println("MessageHelperManager.getDRMessageCount: processing MR "+mrId);
  		


  		int newContact = rs.getInt("newRenraku");
  		mrc.setNewContacts(newContact);
  		//System.out.println("MessageHelperManager.getDRMessageCount: "+newContact+ " new contacts from "+drId);
  		if (newContact == 1)
  		{
  			sqlTextBuffer = new StringBuffer(SQL_GET_UNIQUE_CONTACT_ID);
  			sqltext = sqlTextBuffer.toString();
  			//System.out.println("MessageHelperManager.getDRMessageCount.3: unique Contact from "+drId);
  			pstmt = conn.prepareStatement(sqltext);
  			pstmt.setString(1,mrId);
  			pstmt.setString(2,drId);
  			ResultSet rs1   = pstmt.executeQuery();
  			if (rs1.next())
  			{
  				int cnt = rs1.getInt("count");
  				if (cnt != 1) // this should be impossible here
  				{
  					throw new ApplicationError("MessageHelperManager.getDRMessageCount: received more than one unique Contact ID");
  				
  				}
  				mrc.setNewContactId(rs1.getString("message_header_id"));
  			}
  			else // shouldn't happen
  			{
  				throw new ApplicationError("MessageHelperManager.getDRMessageCount: received empty RS on unique Contact ID");
  			
  			}
  		
  		} // newContact == 1



  		int newDetail = rs.getInt("midokuEdetail");
  		//System.out.println("MessageHelperManager.getDRMessageCount.3: "+newDetail+" newDetails from "+mrId);
  		mrc.setNewEDetails(newDetail);


  		if (newDetail == 1)
  		{
  			sqlTextBuffer = new StringBuffer(SQL_GET_UNIQUE_DETAIL_ID);
  			sqltext = sqlTextBuffer.toString();
  			pstmt = conn.prepareStatement(sqltext);
  			pstmt.setString(1,mrId);
  			pstmt.setString(2,drId);
  			ResultSet rs2   = pstmt.executeQuery();
  			if (rs2.next())
  			{
  				int cnt = rs2.getInt("count");
  				if (cnt != 1) // this should be impossible here
  				{
  					throw new ApplicationError("MessageHelperManager.getMRMessageCount: received more than one unique Detail ID");

  				}
  				mrc.setNewEDetailId(rs2.getString("message_header_id"));
  				//System.out.println("MessageHelperManager.getMRMessageCount.4: unique Detail ID is" + rs2.getString("message_header_id"));

  			}
  			else // shouldn't happen
  			{
  				throw new ApplicationError("MessageHelperManager.getMRMessageCount: received empty RS on unique Detail ID");

  			}
        rs2.close();
        pstmt.close();
  		} // newDeyail == 1

  		//System.out.println("MessageHelperManager.getDRMessageCount.5: adding "+mrId+" to map");
  		ret.put(mrId,mrc);
  					
  	} // while rs.next()		
  
  }
  catch(SQLException e)
  {
  throw new ApplicationError("MessageHelperManager.getDRMessageCount: SQLException",e);
  		
  
  }
  finally
  {
  	try
  	{
  	  conn.close();
  	}
  	catch (SQLException e)
  	{
  	}
  } // finally	
  //System.out.println("MessageHelperManager.getDRMessageCount: returning "+ret.size()+" DRInformationCounts");
  
    ThreadLogger.log("\"" + sessionId + "\",\"MessageHelperManager\",\"getDRMessageCount\",\"DAOEnd\"");
    return ret;
  } // getDRMessageCount
  
  
  // This method will return the JDBC connection instance - Added by Damon 011015
  private Connection getConnection() throws SQLException
  {
  	Connection conn = null;
    
    try
    {
      Driver driver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
      Properties properties = new Properties();
      properties.setProperty("user", dbUtilConstant.JDBC_USER);
      properties.setProperty("password", dbUtilConstant.JDBC_PASS);
      conn = driver.connect(dbUtilConstant.JDBC_LOOKUP, properties);
      return conn;
   	}
    catch(ClassNotFoundException cnfExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: ClassNotFoundException", cnfExc);
    }
    catch(InstantiationException iExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: InstantiationException", iExc);
    }
    catch(IllegalAccessException iaExc)
    {
      throw new ApplicationError("In MessageHelperManager.getConnection: IllegalAccessException", iaExc);
    }
  }  
}
