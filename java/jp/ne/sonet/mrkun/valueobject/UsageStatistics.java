// Copyright (c) 2001 So-net M3, Inc

package jp.ne.sonet.mrkun.valueobject;

import java.util.Date;
import java.lang.*;
import jp.ne.sonet.mrkun.framework.valueobject.BaseValueObject;
/**
 * <P>Statistics - 
 * This class encapsulates usage statistics information that associated with MR.	
 * 
 * @author Damon Lok
 * @version $id$
 * @since JDK1.3
 */
public class UsageStatistics extends BaseValueObject 
{
	public static final int SELF_COUNT = 0;
    public static final int COMPANY_AVG = 1;
	public static final int PER_DR_USAGE = 2;
    public static final int SINGLE_DR_DATA = 3;

  public Object mrDrLevelSemaphore = new Boolean(true);

	//MR Id
	private String mrId;
	
	//Current month of the statistics
	private Date statisticMonth;
	
	//Count of sent EDetail
	private Integer sentEDetail;
    
    //Count of EDetail have been read by DR
    private Integer readEDetail;
    
    //Percentage
    private Integer percentage;
    
    //Count of Contact sent from DR
    private Integer receivedContact;
    
    //Number of registered DR
    private Integer registeredDR;
    
    //Active channel
    private Integer activeCount;
    
    //Usage point type
    private Integer type;
    
    // !The following 5 attributes will be set only if this.type == DR_PROFILE
    
    //DR name
    private String drName;
    
    //DR ID
    private String drId;
    
    //Hospital name
    private String hospitalName;
    
    //Specialty
    private String specialty;
    
    //Status of this DR (active or not active)
    private Integer status;
    
	public UsageStatistics() 
	{ }
	
	/**
	 * Purpose: constructor
	 * @return statistics instance
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public UsageStatistics(String mrId) 
	{
		this.mrId = mrId;
	}

	/**
	 * Purpose: return mkId
	 * @return String
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public String getMrId() 
	{
		return this.mrId;
	}
	
	/**
	 * Purpose: set MR ID
	 * @return void
	 * @param String mrId
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public void setMrId(String mrId) 
	{
		this.mrId = mrId;
	}
	
	/**
	 * Purpose: return currentMonth
	 * @return java.util.Date
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public Date getStatisticMonth() 
	{
		return this.statisticMonth;
	}
	
	/**
	 * Purpose: set currentMonth
	 * @param currentMonth
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49493C01FD
	 */
	public void setStatisticMonth(Date statisticMonth) 
	{
		this.statisticMonth = statisticMonth;
	}
	
	/**
	 * Purpose: return sentEDetail
	 * @return Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getSentEDetail() 
	{
		return this.sentEDetail;
	}
	
	/**
	 * Purpose: set sentEDetail
	 * @param sentEDetail
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setSentEDetail(Integer sentEDetail) 
	{
		this.sentEDetail = sentEDetail;
	}
	
	/**
	 * Purpose: return sentEDetail
	 * @return Date
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getReadEDetail() 
	{
		return this.readEDetail;
	}
	
	/**
	 * Purpose: set readEDetail
	 * @param readEDetail
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setReadEDetail(Integer readEDetail) 
	{
		this.readEDetail = readEDetail;
	}
		
	/**
	 * Purpose: return percentage
	 * @return Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getPercentage() 
	{
		return this.percentage;
	}
	
	/**
	 * Purpose: set percentage
	 * @param percentage
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setPercentage(Integer percentage) 
	{
		this.percentage = percentage;
	}
	
    /**
	 * Purpose: return receivedContact
	 * @return Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getReceivedContact() 
	{
		return this.receivedContact;
	}
	
	/**
	 * Purpose: set receivedContact
	 * @param receivedContact
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setReceivedContact(Integer receivedContact) 
	{
		this.receivedContact = receivedContact;
	}
    	
	/**
	 * Purpose: return registeredDR
	 * @return Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getRegisteredDR() 
	{
		return this.registeredDR;
	}
	
	/**
	 * Purpose: set registeredDR
	 * @param registeredDR
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setRegisteredDR(Integer registeredDR) 
	{
		this.registeredDR = registeredDR;
	}
    
    /**
	 * Purpose: return active
	 * @return Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getActiveCount() 
	{
		return this.activeCount;
	}
	
	/**
	 * Purpose: set active
	 * @param active
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setActiveCount(Integer activeCount) 
	{
		this.activeCount = activeCount;
	}
    
    /**
	 * Purpose: set report type
	 * @param type
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setType(Integer type) 
	{
		this.type = type;
	}
	
    /**
	 * Purpose: get report type
	 * @return type
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public Integer getType() 
	{
		return this.type;
	}

    /**
	 * Purpose: set DR ID
	 * @return type 
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public void setDrId(String drId)
    {
      this.drId = drId;
    }
        
    /**
	 * Purpose: get DR ID
	 * @return type String drId
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public String getDrId()
    {
      return this.drId;
    }
    
    /**
	 * Purpose: set DR name
	 * @return type 
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public void setDrName(String drName)
    {
      this.drName = drName;
    }
        
    /**
	 * Purpose: get DR name
	 * @return type String drName
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public String getDrName()
    {
      return this.drName;
    }
        
    /**
	 * Purpose: get hospital name
	 * @return type 
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public void setHospitalName(String hospitalName)
    {
      this.hospitalName = hospitalName;
    }
    
    /**
	 * Purpose: get hospital name
	 * @return type String hospitalName
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public String getHospitalName()
    {
      return this.hospitalName;
    }
    
    /**
	 * Purpose: set DR's specialty
	 * @return type
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public void setSpecialty(String specialty)
    {
      this.specialty = specialty;
    }
    
    /**
	 * Purpose: get DR's specialty
	 * @return type String specialty
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public String getSpecialty()
    {
      return this.specialty;
    }
    
    /**
	 * Purpose: set DR's status
	 * @return type 
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public void setStatus(Integer status)
    {
      this.status = status;
    }
    
    /**
	 * Purpose: get DR's status
	 * @return type String status
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
    public Integer getStatus()
    {
      return this.status;
    }	
}