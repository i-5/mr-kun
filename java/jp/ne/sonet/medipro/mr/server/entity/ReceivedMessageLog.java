package jp.ne.sonet.medipro.mr.server.entity;

import java.util.Date;

public class ReceivedMessageLog {
    protected String name = null;
    protected String title = null;
    protected Date receiveTime = null;

    public String getName() {
	return name;
    }

    public String getTitle() {
	return title;
    }

    public Date getReceiveTime() {
	return receiveTime;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setReceiveTime(Date date) {
	this.receiveTime = date;
    }
    
}
