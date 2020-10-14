package jp.ne.sonet.medipro.wm.server.entity;

public class AccessWmLogInfo {
    private String wmId;
    private String userAgent;

    public AccessWmLogInfo() {
    }

    public String getWmId() {
	return wmId;
    }

    public void setWmId(String value) {
	this.wmId = value;
    }

    public String getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(String value) {
	this.userAgent = value;
    }


}
