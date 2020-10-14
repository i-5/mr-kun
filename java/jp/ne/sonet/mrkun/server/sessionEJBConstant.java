// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server; 

/**
 *
 * session EJB constants used by all Managers and their respective DAO classes
 * to exchange parameters.
 *
 * @author Damon Lok
 * @version 1
 *
 */
public class sessionEJBConstant
{
  // MR message related constants
  public final static int RECEIVED_MESSAGE_ID = 0;
  public final static int RECEIVED_MESSAGE = 1;
  public final static int UNREAD_SENT_MESSAGE = 2;
  public final static int UNREAD_MESSAGE_ID = 3;
  public final static int ACTION_VALUE = 4;
  public final static String NOT_SET = "no_messageId";
  public final static String MR_MESSAGE_COUNT = "mr_message_count";
  public final static String MR_DR_MESSAGE_COUNT = "mr_dr_message_count";
  public final static String GET_MESSAGE_ID = "get_message_id";
  public final static String GET_MESSAGE = "whole_message";
  public final static String MESSAGE_ID_LIST = "message_id_list";
  public final static String MESSAGE_PAGES = "message_pages";
  
  // MR usage statistic related constants 
  public final static String MR_SINGLE_USAGE_COUNT = "usage_point_single_count";
  public final static String MR_USAGE_SELF_COUNT = "usage_point_mr_self_count";
  public final static String MR_USAGE_COMPANY_COUNT = "usage_point_company_average_count";
  public final static String MR_USAGE_RANKING_COUNT = "usage_point_ranking_count";
  public final static String MR_USAGE_DR_PROFILE_COUNT = "usage_point_doctor_profile_count";

  // DR delete MR related constants
  public final static String DR_ID = "drId";
  public final static String DELETE_MR_LIST = "deleteMRList";
}


