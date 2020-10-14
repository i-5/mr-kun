package jp.ne.sonet.medipro.mr.server.entity;

public class DoctorProfileInfo {
    private String drId;
    private String itemCd;
    private String seq;
    private String item;

    public DoctorProfileInfo() {
    }

    public String getDrId() {
	return drId;
    }

    public void setDrId(String value) {
	drId = value;
    }

    public String getItemCd() {
	return itemCd;
    }

    public void setItemCd(String value) {
	itemCd = value;
    }

    public String getSeq() {
	return seq;
    }

    public void setSeq(String value) {
	seq = value;
    }

    public String getItem() {
	return item;
    }

    public void setItem(String value) {
	item = value;
    }

    public String toString() {
	return "(" + drId
	    + "," + itemCd
	    + "," + seq
	    + "," + item
	    + ")";
    }
}
