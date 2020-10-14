package jp.ne.sonet.medipro.wm.server.entity;

public class AttachFileInfo {
    public String seq;
    public String messageId;
    public String attachFile;
    public String fileName;
    public String fileKbn;

    public AttachFileInfo() {
    }

    public String toString() {
	return "(" + seq
	    + "," + messageId
	    + "," + attachFile
	    + "," + fileName
	    + "," + fileKbn
	    + ")";
    }
}
