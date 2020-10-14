package jp.ne.sonet.medipro.mr.server.entity;

public class ActionInfo {
    private String companyCd;
    private String targetRank;
    private int threshold;
    private String message1;
    private String message2;
    private String message3;
    private String message4;
    private String targetName;//1020 y-yamada add 

    public String getCompanyCd() {
	return companyCd;
    }

    public void setCompanyCd(String cd) {
	companyCd = cd;
    }

    public String getTargetRank() {
	return targetRank;
    }

    public void setTargetRank(String rank) {
	targetRank = rank;
    }

    public int getThreshold() {
	return threshold;
    }

    public void setThreshold(int value) {
	threshold = value;
    }

    public String getMessage1() {
	return message1;
    }

    public void setMessage1(String message) {
	message1 = message;
    }

    public String getMessage2() {
	return message2;
    }

    public void setMessage2(String message) {
	message2 = message;
    }

    public String getMessage3() {
	return message3;
    }

    public void setMessage3(String message) {
	message3 = message;
    }

    public String getMessage4() {
	return message4;
    }

    public void setMessage4(String message) {
	message4 = message;
    }
/* 1020 y-yamada add start */    
    public String getTargetName() {
	return targetName;
    }

    public void setTargetName(String value) {
	targetName = value;
    }
/* 1020 y-yamada add end */    
}
