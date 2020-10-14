package jp.ne.sonet.medipro.wm.server.entity;

import java.sql.Date;

public class MessageHeaderInfo {
    public String messageHeaderId;
    public String messageId;
    public String messageKbn;
    public String fromUserId;
    public String toUserId;
    public String ccFlg;
    public Date receiveTime;
    public Date sendTorikeshiTime;
    public Date sendSaveTime;
    public Date sendDeleteTime;
    public String sendStatus;
    public Date receiveTimed;
    public Date receiveSaveTime;
    public Date receiveDeleteTime;
    public String receiveStatus;

    public MessageHeaderInfo() {
    }

}
