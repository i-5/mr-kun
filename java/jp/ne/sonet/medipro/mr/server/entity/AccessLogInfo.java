package jp.ne.sonet.medipro.mr.server.entity;

public class AccessLogInfo {
    private String drId;
    private String userAgent;

    public AccessLogInfo() {
    }

    public String getDrId() {
	return drId;
    }

    public void setDrId(String value) {
	this.drId = value;
    }

    public String getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(String value) {
	this.userAgent = value;
    }


}
