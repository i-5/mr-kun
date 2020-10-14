package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�l�r�f�w�b�_���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 22:52:56)
 * @author: 
 */
public class MsgHeaderInfo {
	protected String messageHeaderID;
	protected String messageKbn;
	protected String fromUserID;
	protected String toUserID;
	protected String receiveTime;
	protected String receiveTimed;
	protected String fromName;
	protected String toName;
	protected String fromNameKana;
	protected String toNameKana;
	protected String noRead;
	protected int noReadDay;
	protected java.lang.String fromCompanyName;
	protected java.lang.String toCompanyName;
	protected java.lang.String targetRank;
	protected java.lang.String targetName;//1025 y-yamada add
/**
 * Msgheader �R���X�g���N�^�[�E�R�����g�B
 */
public MsgHeaderInfo(String messageHeaderID, String messageKbn, String fromUserID,
					String toUserID, String receiveTime, String fromName, String toName, String fromCompanyName, String toCompanyName) {

	this.messageHeaderID = messageHeaderID;
	this.messageKbn = messageKbn;
	this.fromUserID = fromUserID;
	this.toUserID = toUserID;
	this.receiveTime = receiveTime;
	this.fromName = fromName;
	this.toName = toName;
	this.fromCompanyName = fromCompanyName;
	this.toCompanyName = toCompanyName;
	
}
/**
 * <h3>���M�ҋΖ���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 06:53:21)
 * @return java.lang.String
 */
public java.lang.String getFromCompanyName() {
	return fromCompanyName;
}
/**
 * <h3>���M�Ҏ����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getFromName() {
	return fromName;
}
/**
 * <h3>���M�Ҏ����i�J�i�j�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߑO 12:46:23)
 * @return java.lang.String
 */
public java.lang.String getFromNameKana() {
	return fromNameKana;
}
/**
 * <h3>�e�q�n�l���[�U�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getFromUserID() {
	return fromUserID;
}
/**
 * <h3>���b�Z�[�W�w�b�_�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getMessageHeaderID() {
	return messageHeaderID;
}
/**
 * <h3>�l�r�f�敪�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getMessageKbn() {
	return messageKbn;
}
/**
 * <h3>���Ǐ��̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getNoRead() {
	return noRead;
}
/**
 * <h3>���Ǔ����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return int
 */
public int getNoReadDay() {
	return noReadDay;
}
/**
 * <h3>��M�i���M�j���Ԃ̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getReceiveTime() {
	return receiveTime;
}
/**
 * <h3>��M�J�����Ԃ̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getReceiveTimed() {
	return receiveTimed;
}
/**
 * <h3>�^�[�Q�b�g�����N�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:26)
 * @return java.lang.String
 */
public java.lang.String getTargetRank() {
	return targetRank;
}
/**
 * <h3>�^�[�Q�b�g�����N���̎擾</h3>
 * 1025 y-yamada add
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:26)
 * @return java.lang.String
 */
public java.lang.String getTargetName() {
	return targetName;
}
/**
 * <h3>��M�ҋΖ���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/24 �ߌ� 06:53:21)
 * @return java.lang.String
 */
public java.lang.String getToCompanyName() {
	return toCompanyName;
}
/**
 * <h3>��M�Ҏ����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/20 �ߌ� 03:17:47)
 * @return java.lang.String
 */
public java.lang.String getToName() {
	return toName;
}
/**
 * <h3>��M�Ҏ����i�J�i�j�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߑO 12:46:23)
 * @return java.lang.String
 */
public java.lang.String getToNameKana() {
	return toNameKana;
}
/**
 * <h3>�s�n���[�U�[�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:02:33)
 * @return java.lang.String
 */
public java.lang.String getToUserID() {
	return toUserID;
}
/**
 * <h3>���M�Ҏ����i�J�i�j�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߑO 12:46:23)
 * @param newFromNameKana java.lang.String
 */
public void setFromNameKana(java.lang.String newFromNameKana) {
	fromNameKana = newFromNameKana;
}
/**
 * <h3>���Ǐ��̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:22:05)
 * @param newNo_Read java.lang.String
 */
public void setNoRead(java.lang.String newNoRead) {
	noRead = newNoRead;
}
/**
 * <h3>���Ǔ����̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:22:05)
 * @param newNo_ReadDay int
 */
public void setNoReadDay(int newNoReadDay) {
	noReadDay = newNoReadDay;
}
/**
 * <h3>��M�J�����Ԃ̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/19 23:22:05)
 * @param newReceive_timed java.lang.String
 */
public void setReceiveTimed(java.lang.String newReceiveTimed) {
	receiveTimed = newReceiveTimed;
}
/**
 * <h3>�^�[�Q�b�g�����N�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:26)
 * @param newTargetRank java.lang.String
 */
public void setTargetRank(java.lang.String newTargetRank) {
	targetRank = newTargetRank;
}
/**
 * <h3>�^�[�Q�b�g�����N���̃Z�b�g</h3>
 * 1025 y-yamada add
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 11:19:26)
 * @param newTargetRank java.lang.String
 */
public void setTargetName(java.lang.String newTargetName) {
	targetName = newTargetName;
}
/**
 * <h3>��M�Ҏ����i�J�i�j�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߑO 12:46:23)
 * @param newToNameKana java.lang.String
 */
public void setToNameKana(java.lang.String newToNameKana) {
	toNameKana = newToNameKana;
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
	me.append("from_name          = [" + fromName + "]\n");
	me.append("from_name          = [" + fromNameKana + "]\n");
	me.append("from_userid        = [" + fromUserID + "]\n");
	me.append("message_header_id  = [" + messageHeaderID + "]\n");
	me.append("message_kbn        = [" + messageKbn + "]\n");
	me.append("no_Read            = [" + noRead + "]\n");
	me.append("no_ReadDay         = [" + noReadDay + "]\n");
	me.append("recevi_time        = [" + receiveTime + "]\n");
	me.append("recevi_timed       = [" + receiveTimed + "]\n");
	me.append("to_name            = [" + toName + "]\n");
	me.append("to_name            = [" + toNameKana + "]\n");
	me.append("to_userid          = [" + toUserID + "]\n");
	me.append("target_rank        = [" + targetRank + "]\n");
	me.append("target_name        = [" + targetName + "]\n");
	return me.toString();
}
}
