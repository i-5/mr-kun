package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>ƒAƒNƒVƒ‡ƒ“î•ñ</strong>
 * <br>
 * @author
 * @version
 */
public class ActionInfo {
    private String companyCd = null;
    private String targetRank = null;
    private String targetName = null;
    private int threshold = 0;
    private String message1 = null;
    private String message2 = null;
    private String message3 = null;
    private String message4 = null;

    public ActionInfo() {
    }

    public String getCompanyCd() {
	return companyCd;
    }

    public void setCompanyCd(String value) {
	companyCd = value;
    }

    public String getTargetRank() {
	return targetRank;
    }

    public void setTargetRank(String value) {
	targetRank = value;
    }

    public String getTargetName() {
	return targetName;
    }

    public void setTargetName(String value) {
	targetName = value;
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

    public void setMessage1(String value) {
	message1 = value;
    }

    public String getMessage2() {
	return message2;
    }

    public void setMessage2(String value) {
	message2 = value;
    }

    public String getMessage3() {
	return message3;
    }

    public void setMessage3(String value) {
	message3 = value;
    }

    public String getMessage4() {
	return message4;
    }

    public void setMessage4(String value) {
	message4 = value;
    }

    public String toString() {
	return "(" + companyCd
	    + "," + targetRank
	    + "," + targetName
	    + "," + threshold
	    + "," + message1
	    + "," + message2
	    + "," + message3
	    + "," + message4
	    + ")";
    }
}
