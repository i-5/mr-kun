package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

/**
 * <h3>���b�Z�[�W�֘A�e�[�u�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:27:50)
 * @author: 
 */
public class MessageTable {
	protected MessageHeaderTable msgHTable;
	protected MessageBodyTable msgBTable;
	protected Enumeration AttachFTable;
	protected Enumeration AttachLTable;
/**
 * MessageTable �R���X�g���N�^�[�E�R�����g�B
 */
public MessageTable() {
}
/**
 * <h3>�Y�t�t�@�C���e�[�u�����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @return java.util.Enumeration (A)
 */
public java.util.Enumeration getAttachFTable() {
	return AttachFTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getAttachLTable() {
	return AttachLTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public MessageBodyTable getMsgBTable() {
	return msgBTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @return jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public MessageHeaderTable getMsgHTable() {
	return msgHTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @param newAttachFTable java.util.Enumeration
 */
public void setAttachFTable(java.util.Enumeration newAttachFTable) {
	AttachFTable = newAttachFTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @param newAttachLTable java.util.Enumeration
 */
public void setAttachLTable(java.util.Enumeration newAttachLTable) {
	AttachLTable = newAttachLTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @param newMsgBTable jp.ne.sonet.medipro.mr.server.entity.MessageBodyTable
 */
public void setMsgBTable(MessageBodyTable newMsgBTable) {
	msgBTable = newMsgBTable;
}
/**
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:22)
 * @param newMsgHTable jp.ne.sonet.medipro.mr.server.entity.MessageHeaderTable
 */
public void setMsgHTable(MessageHeaderTable newMsgHTable) {
	msgHTable = newMsgHTable;
}
}
