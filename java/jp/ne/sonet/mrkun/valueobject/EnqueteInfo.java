
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import java.util.*;
import jp.ne.sonet.mrkun.framework.valueobject.*;


/**
 *　Enquete Info
 * アンケート全般に使用するclassです。関係するテーブルはEnquete_id,
 * Enquete_send_log, Enquete_answer です。
 * <P>
 * @author M.Mizuki</a>
 */
public class EnqueteInfo extends BaseValueObject
{
    private String     EnqId;
    private String     drId;

    private String     Url;
    private Boolean    EnqIdStatus;

    private String     MessageHeaderId;
    private Boolean    ReplyStatus = new Boolean(false);

    private String     GroupId;

    private Collection EnqueteAnswer;

    static final int ON = 1;

  /**
   * Constructor
   */
    public EnqueteInfo(){
    }

    public EnqueteInfo(String userid,String enqid){
	this.setDrId(userid);
	this.setEnqId(enqid);
    }


    public String getEnqId(){
	return EnqId;
    }

    public void setEnqId(String value){
	EnqId = value;
    }

    public String getUrl(){
	return Url;
    }

    public void setUrl(String value){
	Url = value;
    }

    public Boolean getEnqIdStatus(){
	return EnqIdStatus;
    }

    public void setEnqIdStatus(int value){
	if( value == 1 ){
	    EnqIdStatus = new Boolean(true);
	}else{
	    EnqIdStatus = new Boolean(false);
	}
    }

    public void setEnqIdStatus(Boolean value){
	EnqIdStatus = value;
    }

    public String getMessageHeaderId(){
	return MessageHeaderId;
    }

    public void setMessageHeaderId(String value){
	MessageHeaderId = value;
    }

    public Boolean getReplyStatus(){
	return ReplyStatus;
    }

    public void setReplyStatus(int value){
	if( value == 1 ){
	    ReplyStatus = new Boolean(true);
	}else{
	    ReplyStatus = new Boolean(false);
	}
    }

    public void setReplyStatus(Boolean value){
	ReplyStatus = value;
    }

    public String getDrId(){
	return drId;
    }

    public void setDrId(String value){
	drId = value;
    }

    public String getGroupId(){
	return GroupId;
    }

    public void setGroupId(String value){
	GroupId = value;
    }

    public Collection getEnqueteAnswer(){
	return EnqueteAnswer;
    }

    public void addEnqueteAnswer(EnqueteAnswer value){
	if(EnqueteAnswer == null){
	    EnqueteAnswer = new ArrayList();
        }
	EnqueteAnswer.add( value );
    }

}
