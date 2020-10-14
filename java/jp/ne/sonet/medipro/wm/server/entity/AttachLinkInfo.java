package jp.ne.sonet.medipro.wm.server.entity;

public class AttachLinkInfo {
    public String seq;
    public String messageId;
    public String url;
    public String honbunText;
    public String picture;
    public String naigaiLinkKbn;

    public AttachLinkInfo() {
    }

    public String toString() {
	return "(" + seq
	    + "," + messageId
	    + "," + url
	    + "," + honbunText
	    + "," + picture
	    + "," + naigaiLinkKbn
	    + ")";
    }
}
