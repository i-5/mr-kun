
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.dao.*;
import java.util.*;
import java.rmi.*;
import java.text.*;

/**
 * The implementation class for the MRManager bean.
 * This bean is stateless in nature, and uses DAO support classes to
 * write persistent information to the database.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 *
 * @version $Id: MRManager.java,v 1.1.2.36 2001/12/20 08:59:09 rick Exp $
 */
public class MRManager extends BaseSessionEJB
{
    
  /**
   * Get the MR object by its ID from persistent storage
   */
  public MR getMRById(String mrId, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    return getMRById(mrId, mrDAO, sessionId);
  }

  private MR getMRById(String mrId, DAOFacade mrDAO, String sessionId)
  {
    // Get a DAO and perform the save query
    try
    {
      return (MR) mrDAO.searchRecord(mrId, "mrId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Get the MR object by its name from persistent storage
   */
  public MR getMRByName(String name, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    try
    {
      return (MR) mrDAO.searchRecord(name, "name", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }
  
  /**
   * Get the DRInformation object that associated with the MR
   */
  public DRInformation getDRInformation(String mrId, String drId, String sessionId)
  {
    // Get a DAO and perform the search query
    Map params = new Hashtable();
    params.put("mrId", mrId);
    params.put("drId", drId);
    DAOFacade driDAO = new DAOFacade(DRInformation.class);
    try
    {
      return (DRInformation) driDAO.searchRecord(params, "drId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Get the list of DRInformation objects that associated with the MR
   */
  public Collection getDRInformationList(String mrId, String sessionId)
  {
    // Get a DAO and perform the search query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    try
    {
      return (Collection) mrDAO.searchRecord(mrId, "mr_drInfo_list", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }
  
  /**
   * This obtains the data for pages MR2.0 and MR 5.0 - it uses the optimised
   * DRInfo retrieval query (some fields may not be present - check first).
   */
  public Collection getDRInformationList(String mrId, Map messageCounts, String sessionId)
  {
    // Get a DAO and perform the search query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    try
    {
      Map params = new Hashtable();
      params.put("mrId", mrId);
      params.put("messageCounts", messageCounts);
      return (Collection) mrDAO.searchRecord(params, "mr2_optimised", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }
  
  /**
   * Get DRInformation list with MR's message count for newly received Contact and DR's unread sent EDetail
   */
  public Collection getDRListWithMessageCount(MR mr, Map mrMessageHashData, String sessionId)
  {
  	if (mr == null)
	  {
	    throw new ApplicationError(this.getClass().getName()
        + ": Parameter mr object is passed in as null in getDRListWithMessageCount.");
	  }
	
    EJBUtil ejbUtil = new EJBUtil();
	  Collection drInformationList = getDRInformationList(mr.getMrId(), sessionId);
    
	  try
	  {
	    // Get MessageManager for the obtaining MR's message information
	    MessageManagerRemoteIntf messageManager = (MessageManagerRemoteIntf) ejbUtil.getManager(HttpConstant.MESSAGEMANAGER_HOME);
	  
	    // Build a list of all drInformation object
      mrMessageHashData = messageManager.getMRMessageCount(mr, sessionEJBConstant.MR_MESSAGE_COUNT, sessionId);  
	    if ( mrMessageHashData == null )
      {
	      throw new ApplicationError(this.getClass().getName()
          + ": The returned MR message data is null in getDRListWithMessageCount.");
	    }
		
    } 
	  catch (RemoteException e)
	  {
	    // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName()
             			+ ": Problem on building the drInfoMessageData object in getDRListWithMessageCount and RemoteException is thrown.");
    }
	
	  // These logic will scan thru drInfoMessageData (the MR's message info) to set
	  // each DRInformation's actionValue, receivedMessage and unreadSentMessage as well
	  // as put the message ID of receivedMessage and unreadSentMessage on the list for
	  // JSP population.
    for (Iterator drItr = drInformationList.iterator(); drItr.hasNext(); )
	  {
      DRInformation drInfo = (DRInformation) drItr.next();
	    Object messageInfo[] = (Object[]) mrMessageHashData.get(drInfo.getDrId());
      if (messageInfo != null)
      {
  	    for (int mIndex=0; mIndex < messageInfo.length; mIndex++)
	      {
	      	switch (mIndex)
  		    {
            case sessionEJBConstant.RECEIVED_MESSAGE:
      		  	drInfo.setReceivedMessage((Integer)messageInfo[mIndex]);
		          break;

  	  	    case sessionEJBConstant.UNREAD_SENT_MESSAGE:
        			drInfo.setUnreadSentMessage((Integer)messageInfo[mIndex]);
		          break;

      		  case sessionEJBConstant.ACTION_VALUE:
        			drInfo.setActionValue((Integer)messageInfo[mIndex]);
		          break;
		      }
	      }
      }
      else
      {
        drInfo.setReceivedMessage(new Integer(0));
        drInfo.setUnreadSentMessage(new Integer(0));
        drInfo.setActionValue(new Integer(1));
      }
	  }
  	return drInformationList;
  }

  /**
   * Save the already persisted DR object into persistent storage
   * Note - we are ignoring the NoObjectFoundException returned here.
   * It will be passed on to the calling class.
   */
  public void updateMR(MR userMR, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    if(!mrDAO.updateRecord(userMR, sessionId))
      throw new ApplicationError("Fail to update mr.");
  }

  /**
   * Save a DRInformation object to persistent storage
   */
  public void updateDRInfo(DRInformation drInfo, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade drInfoDAO = new DAOFacade(DRInformation.class);
    if(!drInfoDAO.updateRecord(drInfo, sessionId))
      throw new ApplicationError("Fail to update drinfo.");
  }

  /**
   * Gets all the Importance list of Company
   */
  public Collection getImportanceList(Company company, String sessionId)
  {
    // Get a DAO and perform the search query
    DAOFacade mrDAO = new DAOFacade(Importance.class);
    try
    {
      return (Collection) mrDAO.searchMultiple(company, "company", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }

  /**
   * Get the WebImage object by its ID from persistent storage
   */
  public WebImage getWebImageById(String webImageId, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade imageDAO = new DAOFacade(WebImage.class);
    try
    {
      return (WebImage) imageDAO.searchRecord(webImageId, "webImageId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Gets all the webImages for a given MR.
   */
  public Collection getWebImagesByMrId(String mrId, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade imageDAO = new DAOFacade(WebImage.class);
    return (Collection) imageDAO.searchRecord(mrId, "mrId", sessionId);
  }

  /**
   * This method attempts an MR login, handling the not found and
   * bad password exception cases.
   */
  public MR loginMR(String login, String password, String userAgent, String sessionId)
    throws UserNotFoundException, LoginFailedException
  {
    // Get the MR object from the database
    DAOFacade mrDAO = new DAOFacade(MR.class);
    MR userMR = getMRById(login, mrDAO, sessionId);
    if (userMR == null)
      throw new UserNotFoundException("Login " + login + " was not found in the database.");
    else if (!password.equals(userMR.getPassword()))
      throw new LoginFailedException("Password supplied for user " + login + " was incorrect.");
    else
    {
      // Log the successful attempt in the last login attempt
      Map params = new Hashtable();
      params.put("mrId", login);
      params.put("userAgent", userAgent);
      params.put("operation", "loginTime");
      mrDAO.updateRecord(params, sessionId);
      return userMR;
    }
  }

  /**
   * Retrieve a collection of MRProfile objects matching the DrId specified
   */
  public Map getMRProfileByDRId(String drId, String sessionId)
  {
    // Get a DAO and retrieve the MR objects
    DAOFacade mrDAO = new DAOFacade(MR.class);

    // New way ... calling the new faster dr2.0 query
    return (Map) mrDAO.searchRecord(drId, "dr2_optimised", sessionId);
/*
    Collection mrList = (Collection) mrDAO.searchRecord(drId, "drId");

    // Now iterate through the MRs and find a message count for each
    MessageHelperManager mhm = new MessageHelperManager();
    if (mrList != null)
    {
      Map mrProfileList = new Hashtable();
      for (Iterator i = mrList.iterator(); i.hasNext(); )
      {
        // Set the element's MR object
        MR mrCurrent = (MR) i.next();
        MrProfile mrpCurrent = new MrProfile(mrCurrent.getMrId(), mrCurrent);

        // Get the message count info from the MHM
        Integer unreadMessageCount = mhm.getAvailableMessages(mrCurrent.getMrId(),
                                                              drId,
                                                              true,
                                                              MessageHelperManager.EDETAILS_ONLY);
        if (unreadMessageCount != null)
        {
          mrpCurrent.setReceivedMessage(unreadMessageCount.intValue());
          if (mrpCurrent.getReceivedMessage() > 0)
          {
            // Get the first message
            Collection unreadMessages = mhm.getMessages(mrCurrent.getMrId(),
                                                        drId,
                                                        "F1",
                                                        new Integer(0),
                                                        new Integer(1),
                                                        true,
                                                        MessageHelperManager.EDETAILS_ONLY);
            // Read the first message and populate the mrProfile object
            if (unreadMessages.size() > 0)
            {
              Iterator j = unreadMessages.iterator();
              Message msg = (Message) j.next();
              mrpCurrent.setUnreadDate(msg.getSentDate());
              mrpCurrent.setUnreadMessageId(msg.getMessageId());
              mrpCurrent.setUnreadSubject(msg.getTitle());
            }
          }
        }

        String position = getBannerPosition(mrCurrent.getMrId(), drId);
        if(position != null)
          mrpCurrent.setMrBannerPosition(position);
        else
        {
          // FIXME: !!Uncomment out this when database data are ready for this
          // throw new ApplicationError(this.getClass().getName()
          //   + ": MR's banner position value is null.");
          mrpCurrent.setMrBannerPosition("");
        }

        mrProfileList.put(mrCurrent.getMrId(), mrpCurrent);
      }
      return mrProfileList;
    }
    else
      return new Hashtable();
*/
  }

  /**
   * Retrieve the mr's banner position
   */
  public String getBannerPosition(String mrId, String drId, String sessionId)
  {
    if((mrId == null) || (drId == null))
      throw new ApplicationError(this.getClass().getName()
        + ": Parameter mrId or drId is passed in as null in getBannerPosition.");
    else
    {
  	  // Put mrId and drId in container for the sql criteria
  	  Map sqlParams = new Hashtable();
      sqlParams.put("mrId", mrId);
      sqlParams.put("drId", drId);
    
      // Get a DAO and perform the search query
      DAOFacade mrDAO = new DAOFacade(MR.class);
      try
      {
        return (String) mrDAO.searchRecord(sqlParams, "bannerPosition", sessionId);
      }
      catch (NoObjectFoundException errNOF)
      {
        return null;
      }
    }  
  }
  
  
  /**
   * Retrieve list of MrProfile to be deleted 
   */
  public Collection getDeletingMRProfileList(String[] deleteMRIdList, String drId, String sessionId)
  {
    // Get a DAO and retrieve the list of MR objects
    DAOFacade mrDAO = new DAOFacade(MR.class);
    Map params = new Hashtable();
    params.put("mrList", deleteMRIdList);
    params.put("drId", drId);
    Collection mrList = (Collection) mrDAO.searchRecord(drId, "drId", sessionId);
    Collection deleteMRList = new ArrayList();

    if (mrList != null)
    {
      for (int index=0; index < deleteMRIdList.length; index++)
      {
        String mrId = deleteMRIdList[index];
        for (Iterator itr = mrList.iterator(); itr.hasNext(); )
        {
          MR mr = (MR) itr.next();  
          if (mrId.equals(mr.getMrId()))
            deleteMRList.add(mr);
        }
      }  
    }
    
    return deleteMRList;     
  } 

  /**
   * Retrieve an MrProfile object matching the DrId and MrId specified
   */
  public MrProfile getMRProfileById(String mrId, String drId, String sessionId)
  {
    // Get a DAO and retrieve the MR objects
    DAOFacade mrDAO = new DAOFacade(MR.class);
    MR mrCurrent = (MR) mrDAO.searchRecord(mrId, "mrId", sessionId);

    // Now iterate through the MRs and find a message count for each
    MessageHelperManager mhm = new MessageHelperManager();
    if (mrCurrent != null)
    {
      // Set the element's MR object
      MrProfile mrpCurrent = new MrProfile(mrCurrent.getMrId(), mrCurrent);

      // Get the DR memo field
      String drMemo = getDRMemo(mrId, drId, sessionId);
      
      // Get the message count info from the MHM
      Integer unreadMessageCount = mhm.getAvailableMessages(mrCurrent.getMrId(),
                                                              drId,
                                                              true,
                                                              MessageHelperManager.EDETAILS_ONLY,
                                                              sessionId);
      if (unreadMessageCount != null)
      {
        mrpCurrent.setReceivedMessage(unreadMessageCount.intValue());
        if (mrpCurrent.getReceivedMessage() > 0)
        {
          // Get the first message
          Collection unreadMessages = mhm.getMessages(mrCurrent.getMrId(),
                                                        drId,
                                                        "F1",
                                                        new Integer(0),
                                                        new Integer(1),
                                                        true,
                                                        MessageHelperManager.EDETAILS_ONLY,
                                                        sessionId);
          // Read the first message and populate the mrProfile object
          if (unreadMessages.size() > 0)
          {
            Iterator j = unreadMessages.iterator();
            Message msg = (Message) j.next();
            mrpCurrent.setUnreadDate(msg.getSentDate());
            mrpCurrent.setUnreadMessageId(msg.getMessageId());
            mrpCurrent.setUnreadSubject(msg.getTitle());
          }
          else
            throw new ApplicationError("Query inconsistency - message count > 0, but no messages.");
        }
      }
      if (drMemo != null)
      {
        // Set the DR memo field
        mrpCurrent.setDrMemo(drMemo);
      }
      else
      {
        mrpCurrent.setDrMemo("");
      }
      
      return mrpCurrent;
    }
    else
      return null;
  }
  
 /**
  * Retrieve the doctor's memo field for MrProfile object with the DrId and MrId specified
  */
  public String getDRMemo(String mrId, String drId, String sessionId)
  {
  
  	if((mrId == null) || (drId == null))
      throw new ApplicationError(this.getClass().getName()
        + ": Parameter mrId or drId is passed in as null in getDRMemo.");
    else
    { 
  	  // Put mrId and drId in container for the sql criteria  
  	  Map sqlParams = new Hashtable();
      sqlParams.put("mrId", mrId);
      sqlParams.put("drId", drId);
    
      // Get a DAO and retrieve the MR objects
      DAOFacade mrDAO = new DAOFacade(MR.class);
      try
      {
        return (String) mrDAO.searchRecord(sqlParams, "drMemo", sessionId);
      }
      catch (NoObjectFoundException errNOF)
      {
        return null;
      }      
    }
  }
  
 /**
  * Remove registed mr(s) from dr's mr profile list 
  */
  public void removeMR(Collection colDeleteMRList, String drId, String sessionId)
  {
  	// Put deleteing mr list and dr Id in parameter container
    Map params = new Hashtable();
    params.put(sessionEJBConstant.DELETE_MR_LIST, colDeleteMRList);
    params.put(sessionEJBConstant.DR_ID, drId);

    // Get a DAO and perform the search query
    DAOFacade mrDAO = new DAOFacade(MR.class);
    if(!mrDAO.deleteMultiple(params, sessionId))
      throw new ApplicationError("Fail to delete mr.");
  }

  /**
   * Send a big brother cc of the new edetail sent but the MR
   */
  public void notifyBigBrotherEDetail(MR userMR, String drId, Message msg, String plainText, String sessionId)
  {
    // Now send an email to the bigbrother cc address
    Collection recipientList = new ArrayList();
    EmailContact bigBrotherAddress = new EmailContact();
    bigBrotherAddress.setName(userMR.getCCEmail());
    bigBrotherAddress.setEmailAddress(userMR.getCCEmail());
    recipientList.add(bigBrotherAddress);
    DR dr = null;

    try
    {
      DRManagerRemoteIntf drm = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      dr = drm.getDRById(drId, sessionId);
    }
    catch (RemoteException errRemote)
    {
      throw new ApplicationError("Error retrieving DR info for the big brother cc email", errRemote);
    }

    // Build subject
    String origSubject  = SystemMessages.get("mr_newEDetailBigBrotherSubject");
    String origBody     = SystemMessages.get("mr_newEDetailBigBrotherBody");

    // Build body
    DateFormat sdfStamp = new SimpleDateFormat("yyyyMMddhhmmssSSSSSS");
    Map bodyParams = new Hashtable();
    bodyParams.put("[#MESSAGE_TITLE]", msg.getTitle());
    bodyParams.put("[#MESSAGE_BODY]", plainText);
    bodyParams.put("[#MESSAGE_DATE]", sdfStamp.format(new Date()));
    bodyParams.put("[#MR_NAME]", userMR.getKanjiName());
    bodyParams.put("[#DR_NAME]", dr.getKanjiName());
    bodyParams.put("[#DR_HOSPITAL]", dr.getHospital());

    String newSubject   = StringUtils.globalReplace(origSubject,
                                                  bodyParams);
    String newBody = StringUtils.globalReplace(origBody, bodyParams);

    String senderName   = SystemMessages.get("mr_newEDetailBigBrotherSenderName");
    String senderEmail  = SystemMessages.get("mr_newEDetailBigBrotherSenderEmail");

    MessageHelperManager mhm = new MessageHelperManager();
    mhm.sendEmailMessage(senderEmail, senderName, recipientList, newSubject, newBody, sessionId);
  }
}

