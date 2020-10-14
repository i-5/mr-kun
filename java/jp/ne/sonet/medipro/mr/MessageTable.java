package jp.ne.sonet.medipro.mr;

import java.util.*;

/**
 * @author: Harry Behrens
 */
public class MessageTable {
	protected MessageHeaderTable msgHTable;
	protected MessageBodyTable msgBTable;
	protected Enumeration AttachFTable;
	protected Enumeration AttachLTable;
/**
 * MessageTable コンストラクター・コメント。
 */
public MessageTable() {
}
/**
 * @return java.util.Enumeration (A)
 */
public java.util.Enumeration getAttachFTable() {
	return AttachFTable;
}
/**
 * @return java.util.Enumeration
 */
public java.util.Enumeration getAttachLTable() {
	return AttachLTable;
}
/**
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public MessageBodyTable getMsgBTable() {
	return msgBTable;
}
/**
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public MessageHeaderTable getMsgHTable() {
	return msgHTable;
}
/**
 * @param newAttachFTable java.util.Enumeration
 */
public void setAttachFTable(java.util.Enumeration newAttachFTable) {
	AttachFTable = newAttachFTable;
}
/**
 * @param newAttachLTable java.util.Enumeration
 */
public void setAttachLTable(java.util.Enumeration newAttachLTable) {
	AttachLTable = newAttachLTable;
}
/**
 * @param newMsgBTable jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public void setMsgBTable(MessageBodyTable newMsgBTable) {
	msgBTable = newMsgBTable;
}
/**
 * @param newMsgHTable jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public void setMsgHTable(MessageHeaderTable newMsgHTable) {
	msgHTable = newMsgHTable;
}
}
