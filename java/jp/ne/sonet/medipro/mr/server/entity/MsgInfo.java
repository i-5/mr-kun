package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>ＭＳＧ情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/19 23:33:06)
 * @author: 
 */
public class MsgInfo {
    protected MsgHeaderInfo header;
    protected MsgBodyInfo body;

    public MsgInfo(String messageHeaderID,
		   String messageKbn,
		   String fromUserID,
		   String toUserID,
		   String receiveTime,
		   String fromName,
		   String toName,
		   String fromCompanyName,
		   String toCompanyName) {
	header = new MsgHeaderInfo(messageHeaderID,
				   messageKbn,
				   fromUserID,
				   toUserID,
				   receiveTime,
				   fromName,
				   toName,
				   fromCompanyName,
				   toCompanyName);
    }
    
    public MsgInfo(String messageHeaderID,
		   String messageKbn,
		   String fromUserID,
		   String toUserID,
		   String receiveTime,
		   String fromName,
		   String toName,
		   String fromCompanyName,
		   String toCompanyName,
		   String messageID) {
	header = new MsgHeaderInfo(messageHeaderID,
				   messageKbn,
				   fromUserID,
				   toUserID,
				   receiveTime,
				   fromName,
				   toName,
				   fromCompanyName,
				   toCompanyName);
	body = new MsgBodyInfo(messageID);
    }

    /**
     * <h3>ＭＳＧ本体の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 23:36:21)
     * @return jp.ne.sonet.medipro.mr.sever.entity.MsgBody
     */
    public MsgBodyInfo getBody() {
	return body;
    }

    /**
     * <h3>ＭＳＧヘッダの取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 23:36:21)
     * @return jp.ne.sonet.medipro.mr.sever.entity.MsgHeader
     */
    public MsgHeaderInfo getHeader() {
	return header;
    }

    /**
     * <h3>文字列化する</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 23:51:35)
     * @return java.lang.String
     */
    public String toString() {
	StringBuffer me = new StringBuffer();
	me.append("********** header **********\n");
	me.append(getHeader().toString());
	if (getBody() != null) {
	    me.append("********** body **********\n");
	    me.append(getBody().toString());
	}
	return me.toString();
    }
}
