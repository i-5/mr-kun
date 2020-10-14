package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�l�r�f���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:33:06)
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
     * <h3>�l�r�f�{�̂̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:36:21)
     * @return jp.ne.sonet.medipro.mr.sever.entity.MsgBody
     */
    public MsgBodyInfo getBody() {
	return body;
    }

    /**
     * <h3>�l�r�f�w�b�_�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:36:21)
     * @return jp.ne.sonet.medipro.mr.sever.entity.MsgHeader
     */
    public MsgHeaderInfo getHeader() {
	return header;
    }

    /**
     * <h3>�����񉻂���</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:51:35)
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
