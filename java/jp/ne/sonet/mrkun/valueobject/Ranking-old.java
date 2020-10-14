// Copyright (c) 2001 So-net M3, Inc

package jp.ne.sonet.mrkun.valueobject;

import java.util.Date;
import java.io.Serializable;
import java.lang.*;
import jp.ne.sonet.mrkun.framework.valueobject.BaseValueObject;
/**
 * <P>Ranking - 
 * This class encapsulates Ranking information that associated with MR.	
 * 
 * @author Damon Lok
 * @version 2.0
 * @since JDK1.3
 */
public class Ranking extends BaseValueObject implements Serializable 
{

	//MR Id
	private String mrId;
	
	//The date of the ranking conducted
	private Date rankingDate;
	
	//The total EDetails that sent by MR and have been read by his/her DR
	private Integer totalReadEDetail;
	
	//The ranking figure
	private Integer ranking;
	
	//The total number of ranking of MR
	private Integer totalInRanking;
	
	public Ranking() 
	{ }
	
	/**
	 * Purpose: constructor
	 * @return ranking instance
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public Ranking(String mrId) 
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
	 * Purpose: return rankingDate
	 * @return java.util.Date
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public void setMrId(String mrId) 
	{
		this.mrId = mrId;
	}
	
	/**
	 * Purpose: return rankingDate
	 * @return java.util.Date
	 * @exception 
	 * @since 
	 * @roseuid 3B49492E03DD
	 */
	public Date getRankingDate() 
	{
		return this.rankingDate;
	}
	
	/**
	 * Purpose: set rankingDate
	 * @param rankingDate
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49493C01FD
	 */
	public void setRankingDate(Date rankingDate) 
	{
		this.rankingDate = rankingDate;
	}
	
	/**
	 * Purpose: return totalReadEDetail
	 * @return java.lang.Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49494F02F4
	 */
	public Integer getTotalReadEDetail() 
	{
		return this.totalReadEDetail;
	}
	
	/**
	 * Purpose: set totalReadEDetail
	 * @param totalReadEDetail
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49495F01E9
	 */
	public void setTotalReadEDetail(Integer totalReadEDetail) 
	{
		this.totalReadEDetail = totalReadEDetail;
	}
	
	/**
	 * Purpose: return ranking
	 * @return java.lang.Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B4949730256
	 */
	public Integer getRanking() 
	{
		return this.ranking;
	}
	
	/**
	 * Purpose: set ranking
	 * @param ranking
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B49497A0256
	 */
	public void setRanking(Integer ranking) 
	{
		this.ranking = ranking;
	}
	
	/**
	 * Purpose: return totalInRanking
	 * @return java.lang.Integer
	 * @exception 
	 * @since 
	 * @roseuid 3B49497F0307
	 */
	public Integer getTotalInRanking() 
	{
		return this.totalInRanking;
	}
	
	/**
	 * Purpose: set totalInRanking
	 * @param totalInRanking
	 * @return void
	 * @exception 
	 * @since 
	 * @roseuid 3B4949890352
	 */
	public void setTotalInRanking(Integer totalInRanking) 
	{
		this.totalInRanking = totalInRanking;
	}
}