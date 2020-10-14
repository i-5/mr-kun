
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;
import java.io.*;
import org.jdom.*;

/**
 * <P>DRInformation - This class encapsulates DR information that managed by MR.			  with the Medical Representative.
 * 
 * @author Damon Lok
 * @version $id$
 * @since JDK1.3
 */
public class DRInformation extends BaseValueObject
{
	
	//Number of new messages (Contacts) sent by this DR  
	private Integer receivedMessage;
	
	//Number of messages (eDetail) that sent by the MR but haven't been read by this DR
	private Integer unreadSentMessage;
	
	//Action value that indicates what the performance status about this DR
	private Integer actionValue;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/NAME	
	 */
	private String name;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/KINMUSAKI
	 */
	private String hospital;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/MAKER_CUST_ID
	 */
	private String clientId;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/TARGET_RANK	
	 */
	private Importance importance;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SYOKUSYU	 
	 */
	private String occupation;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SENMON	 
	 */
	private String division;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/YAKUSYOKU	 
	 */
	private String position;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SOTSUGYO_DAIGAKU	
	 */
	private String graduatedUniversity;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SYUMI	 
	 */
	private String hobby;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SOTSUGYO_YEAR	
	 */
	private String yearOfGraduation;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/SONOTA	 
	 */
	private String memo;
	
	/**
	 * Datamapping: DR/DR_ID	 
	 */
	private String drId;
	
	/**
	 * Datamapping: SENTAKU_TOROKU/MAKER_SHISETSU_ID	 
	 */
	private String hospitalId;
	
	/**
	 * Datamapping: MR/MR_ID	 
	 */
	private String mrId;
	
	public DRInformation() {}
	
	/**
	 * Purpose: default constructor for DRInformation	 
	 * @param drId
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public DRInformation(String drId) 
	{
		this.drId = drId;
	}
	
	/**
	 * Purpose: return receivedMessage	 
	 * @param
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public Integer getReceivedMessage()
	{
		return this.receivedMessage;
	}
	
	/**
	 * Purpose: set receivedMessage	 
	 * @param Integer receivedMessage
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public void setReceivedMessage(Integer receivedMessage)
	{
		this.receivedMessage = receivedMessage;
	}
	
	/**
	 * Purpose: set sentMessage	 
	 * @param Integer sentMessage
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public void setUnreadSentMessage(Integer unreadSentMessage)
	{
		this.unreadSentMessage = unreadSentMessage;
	}
	
	/**
	 * Purpose: return sentMessage	 
	 * @param 
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public Integer getUnreadSentMessage()
	{
		return this.unreadSentMessage;
	}
	
	/**
	 * Purpose: return actionValue	 
	 * @param 
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public Integer getActionValue()
	{
		return this.actionValue;
	}
	
	/**
	 * Purpose: set actionValue	 
	 * @param Integer actionValue
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1DD2B30028
	 */
	public void setActionValue(Integer actionValue)
	{
		this.actionValue = actionValue;
	}	
	
	/**
	 * Purpose: sets name	 
	 * @param name
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: name has been updated
	 * @roseuid 3B1E3671016E
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * Purpose: returns name	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3ABE0370
	 */
	public String getName() 
	{
		return this.name;
	}
	
	/**
	 * Purpose: sets hospital	 
	 * @param hospitalName
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: hospital has been updated
	 * @roseuid 3B1E3AC501DF
	 */
	public void setHospital(String hospital) 
	{
		this.hospital = hospital;
	}
	
	/**
	 * Purpose: return hospital	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3AD002F4
	 */
	public String getHospital() 
	{
		return this.hospital;
	}
	
	/**
	 * Purpose: sets clientId	 
	 * @param clientId
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: clientId has been updated
	 * @roseuid 3B1E3AD7038A
	 */
	public void setClientId(String clientId) 
	{
		this.clientId = clientId;
	}
	
	/**
	 * Purpose: returns clientId	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3AE20336
	 */
	public String getClientId() 
	{
		return this.clientId;
	}
	
	/**
	 * Purpose: sets importance	 
	 * @param importanceId
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: importance has been updated
	 * @roseuid 3B1E3AE90336
	 */
	public void setImportance(Importance importanceId) 
	{
		this.importance = importanceId;
	}
	
	/**
	 * Purpose: returns importance	 
	 * @return Importance
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3AF4016F
	 */
	public Importance getImportance() 
	{
		return this.importance;
	}
	
	/**
	 * Purpose: sets occupation	 
	 * @param occupation
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: occupation has been updated
	 * @roseuid 3B1E3B00027B
	 */
	public void setOccupation(String occupation) 
	{
		this.occupation = occupation;
	}
	
	/**
	 * Purpose: returns occupation	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B1401ED
	 */
	public String getOccupation() 
	{
		return this.occupation;
	}
	
	/**
	 * Purpose: returns division	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B1B0053
	 */
	public String getDivision() 
	{
		return this.division;
	}
	
	/**
	 * Purpose: sets division	 
	 * @param division
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: division has been updated
	 * @roseuid 3B1E3B2702D1
	 */
	public void setDivision(String division) 
	{
		this.division = division;
	}
	
	/**
	 * Purpose: sets position	 
	 * @param position
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: position has been updated
	 * @roseuid 3B1E3B2D03CA
	 */
	public void setPosition(String position) 
	{
		this.position = position;
	}
	
	/**
	 * Purpose: returns position	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B3A00F7
	 */
	public String getPosition() 
	{
		return this.position;
	}
	
	/**
	 * Purpose: sets graduatedUniversity	 
	 * @param university
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: graduatedUniversity has been updated
	 * @roseuid 3B1E3B4002C3
	 */
	public void setGraduatedUniversity(String university) 
	{
		this.graduatedUniversity = university;
	}
	
	/**
	 * Purpose: returns graduatedUniversity	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B4D0072
	 */
	public String getGraduatedUniversity() 
	{
		return this.graduatedUniversity;
	}
	
	/**
	 * Purpose: sets hobby	 
	 * @param hobby
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B630331
	 */
	public void setHobby(String hobby) 
	{
		this.hobby = hobby;
	}
	
	/**
	 * Purpose: returns hobby	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B6F0003
	 */
	public String getHobby() 
	{
		return this.hobby;
	}
	
	/**
	 * Purpose: sets yearOfGraduation	 
	 * @param year
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: yearOfGraduation has been updated
	 * @roseuid 3B1E3B74039A
	 */
	public void setYearOfGraduation(String year) 
	{
		this.yearOfGraduation = year;
	}
	
	/**
	 * Purpose: return yearOfGraduation	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B820105
	 */
	public String getYearOfGraduation() 
	{
		return this.yearOfGraduation;
	}
	
	/**
	 * Purpose: set memo	 
	 * @param memo
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: memo has been updated
	 * @roseuid 3B1E3B8B0176
	 */
	public void setMemo(String memo) 
	{
		this.memo = memo;
	}
	
	/**
	 * Purpose: return memo	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B92002C
	 */
	public String getMemo() 
	{
		return this.memo;
	}
	
	/**
	 * Purpose: return DRID	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B97030E
	 */
	public String getDrId() 
	{
		return this.drId;
	}
	
	/**
	 * Purpose: sets DRID	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: DRID has been updated
	 * @roseuid 3B1E3BAB030D
	 */
	public void setDrId(String drId) 
	{
		this.drId = drId;
	}
	
	/**
	 * Purpose: return hospitalId	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B272D0E01EA
	 */
	public String getHospitalId() 
	{
		return this.hospitalId;
	}
	
	/**
	 * Purpose: sets hospitalId	 
	 * @param hospitalId
	 * @return Void
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: hospitalId has been updated
	 * @roseuid 3B27346C005A
	 */
	public void setHospitalId(String hospitalId) 
	{
		this.hospitalId = hospitalId;
	}
	
	/**
	 * Purpose: return MRID	 
	 * @return String
	 * @exception 
	 * @since 
	 * Pre-condition: none
	 * Post-condition: none
	 * @roseuid 3B1E3B97030E
	 */
	public String getMrId() 
	{
		return this.mrId;
	}
	
	/**
	 * Purpose: sets MRID	 * @return Void
	 * @exception
	 * @since
	 * Pre-condition: none
	 * Post-condition: MRID has been updated
	 * @roseuid 3B1E3BAB030D
	 */
	public void setMrId(String mrId)
	{
		this.mrId = mrId;
	}

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getDrId() != null)
      masterElement.addContent(new Element("drId").setText(getDrId()));

    if (getName() != null)
      masterElement.addContent(new Element("name").setText(getName()));

    if (getHospital() != null)
      masterElement.addContent(new Element("hospital").setText(getHospital()));

    if (getClientId() != null)
      masterElement.addContent(new Element("clientId").setText(getClientId()));

    if (getHospitalId() != null)
      masterElement.addContent(new Element("hospitalId").setText(getHospitalId()));

    if (getImportance() != null)
      masterElement.addContent(getImportance().toXML("importance"));

    if (getOccupation() != null)
      masterElement.addContent(new Element("occupation").setText(getOccupation()));

    if (getDivision() != null)
      masterElement.addContent(new Element("division").setText(getDivision()));

    if (getPosition() != null)
      masterElement.addContent(new Element("position").setText(getPosition()));

    if (getGraduatedUniversity() != null)
      masterElement.addContent(new Element("graduatedUniversity").setText(getGraduatedUniversity()));

    if (getHobby() != null)
      masterElement.addContent(new Element("hobby").setText(getHobby()));

    if (getYearOfGraduation() != null)
      masterElement.addContent(new Element("yearOfGraduation").setText(getYearOfGraduation()));

    if (getReceivedMessage() != null)
      masterElement.addContent(new Element("receivedMessage").setText("" + getReceivedMessage()));

    if (getUnreadSentMessage() != null)
      masterElement.addContent(new Element("unreadSentMessage").setText("" + getUnreadSentMessage()));

    if (getActionValue() != null)
      masterElement.addContent(new Element("actionValue").setText("" + getYearOfGraduation()));

    return masterElement;
  }

}

 