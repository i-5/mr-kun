package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 *　Enquete Info
 * アンケート全般に使用するclassです。関係するテーブルはEnquete_id,
 * Enquete_send_log, Enquete_answer です。
 */
public class EnqueteInfo {

    private String EnqId;
    private String Url;
    private boolean EnqIdStatus;
    private String MessageHeaderId;
    private boolean ReplyStatus;
    private String UserId;
    private Vector EnqueteAnswer;

    static final int ON = 1;

    public EnqueteInfo(){
    }

    public EnqueteInfo(String userid,String enqid){
	this.setUserId(userid);
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

    public boolean getEnqIdStatus(){
	return EnqIdStatus;
    }

    public void setEnqIdStatus(int value){
	if( value == 1 ){
	    EnqIdStatus = true;
	}else{
	    EnqIdStatus = false;
	}
    }

    public void setEnqIdStatus(boolean value){
	EnqIdStatus = value;
    }

    public String getMessageHeaderId(){
	return MessageHeaderId;
    }

    public void setMessageHeaderId(String value){
	MessageHeaderId = value;
    }

    public boolean getReplyStatus(){
	return ReplyStatus;
    }

    public void setReplyStatus(int value){
	if( value == 1 ){
	    ReplyStatus = true;
	}else{
	    ReplyStatus = false;
	}
    }

    public void setReplyStatus(boolean value){
	ReplyStatus = value;
    }

    public String getUserId(){
	return UserId;
    }

    public void setUserId(String value){
	UserId = value;
    }

    public Vector getEnqueteAnswer(){
	return EnqueteAnswer;
    }

    public void addEnqueteAnswer(EnqueteAnswer value){
	if(EnqueteAnswer == null){
	    EnqueteAnswer = new Vector();
        }
	EnqueteAnswer.addElement( value );
    }

}
