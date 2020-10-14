package jp.ne.sonet.medipro.mr.server.entity;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 *Å@Enquete Info
 */
public class EnqueteQuestionInfo {

    //ENQUETE_QUESTION
    private String EnqId;
    private String ShinryokaCd;
    private String GroupId;
    private String GroupName;
    private String MinorId;
    private Vector Question;

    public EnqueteQuestionInfo(String enqid,
			String minor_id){
	this.setEnqId( enqid );
	this.setMinorId( minor_id );
    }

    public String getEnqId(){
	return EnqId;
    }

    public void setEnqId(String value){
	EnqId = value;
    }

    public String getShinryokaCd(){
	return ShinryokaCd;
    }

    public void setShinryokaCd(String value){
	ShinryokaCd = value;
    }

    public String getGroupId(){
	return GroupId;
    }

    public void setGroupId(String value){
	GroupId = value;
    }

    public String getGroupName(){
	return GroupName;
    }

    public void setGroupName(String value){
	GroupName = value;
    }

    public String getMinorId(){
	return MinorId;
    }

    public void setMinorId(String value){
	MinorId = value;
    }

    public Vector getQuestion(){
	return Question;
    }

    public void addQuestion(String value){
	if(Question == null){
	    Question = new Vector();
        }
	Question.addElement( value );
    }

}
