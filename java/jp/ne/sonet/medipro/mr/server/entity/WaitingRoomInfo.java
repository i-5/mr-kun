package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�ҍ������</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 12:46:32)
 * @author: 
 */
public class WaitingRoomInfo {
	protected MrInfo mrinfo;
	protected int msgCount;
	protected MsgInfo msginfo;
	protected String mrID;
/**
 * WaitingRoom �R���X�g���N�^�[�E�R�����g�B
 */
public WaitingRoomInfo() {
	super();
}
/**
 * <h3>�l�q�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߌ� 06:42:34)
 * @return java.lang.String
 */
public java.lang.String getMrID() {
	return mrID;
}
/**
 * <h3>�l�q���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 04:21:02)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getMrinfo() {
	return mrinfo;
}
/**
 * <h3>�l�q���b�Z�[�W�J�E���^�[�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 04:21:02)
 * @return int
 */
public int getMsgCount() {
	return msgCount;
}
/**
 * <h3>�l�r�f���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:08:42)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getMsginfo() {
	return msginfo;
}
/**
 * <h3>�l�q�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߌ� 06:42:34)
 * @param newMrID java.lang.String
 */
public void setMrID(java.lang.String newMrID) {
	mrID = newMrID;
}
/**
 * <h3>�l�q���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 04:21:02)
 * @param newMrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setMrinfo(MrInfo newMrinfo) {
	mrinfo = newMrinfo;
}
/**
 * <h3>�l�q���b�Z�[�W�J�E���^�[�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/28 �ߑO 04:21:02)
 * @param newMsgCount int
 */
public void setMsgCount(int newMsgCount) {
	msgCount = newMsgCount;
}
/**
 * <h3>�l�r�f���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:08:42)
 * @param newMsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setMsginfo(MsgInfo newMsginfo) {
	msginfo = newMsginfo;
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
	me.append(msginfo + "\n");
	me.append(mrinfo + "\n");
	me.append(msgCount + "\n");
	me.append(mrID + "\n");
	
	return me.toString();
}
}
