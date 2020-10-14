package jp.ne.sonet.medipro.mr.server.entity;

public class KinmusakiInfo {
    private String drId;
    private String seq;
    private String kinmusakiName;

    public KinmusakiInfo() {
    }

    public String getDrId() {
	return drId;
    }

    public void setDrId(String value) {
	drId = value;
    }

    public String getKinmusakiName() {
	return kinmusakiName;
    }

    public void setKinmusakiName(String value) {
	kinmusakiName = value;
    }

    public String getSeq() {
	return seq;
    }

    public void setSeq(String value) {
	seq = value;
    }

    public String toString() {
	return "(" + drId
	    + "," + seq
	    + "," + kinmusakiName
	    + ")";
    }
}
