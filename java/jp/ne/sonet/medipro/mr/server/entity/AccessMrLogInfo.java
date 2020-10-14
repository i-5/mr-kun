package jp.ne.sonet.medipro.mr.server.entity;

public class AccessMrLogInfo {
    private String mrId;
    private String userAgent;

    public AccessMrLogInfo() {
    }

    public String getMrId() {
	return mrId;
    }

    public void setMrId(String value) {
	this.mrId = value;
    }

    public String getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(String value) {
	this.userAgent = value;
    }


}
