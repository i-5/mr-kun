package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

/**
 * <h3>メッセージ関連テーブル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 08:27:50)
 * @author: 
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
 * <h3>添付ファイルテーブル情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @return java.util.Enumeration (A)
 */
public java.util.Enumeration getAttachFTable() {
	return AttachFTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getAttachLTable() {
	return AttachLTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public MessageBodyTable getMsgBTable() {
	return msgBTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public MessageHeaderTable getMsgHTable() {
	return msgHTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @param newAttachFTable java.util.Enumeration
 */
public void setAttachFTable(java.util.Enumeration newAttachFTable) {
	AttachFTable = newAttachFTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @param newAttachLTable java.util.Enumeration
 */
public void setAttachLTable(java.util.Enumeration newAttachLTable) {
	AttachLTable = newAttachLTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @param newMsgBTable jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public void setMsgBTable(MessageBodyTable newMsgBTable) {
	msgBTable = newMsgBTable;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:19:22)
 * @param newMsgHTable jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public void setMsgHTable(MessageHeaderTable newMsgHTable) {
	msgHTable = newMsgHTable;
}
}
