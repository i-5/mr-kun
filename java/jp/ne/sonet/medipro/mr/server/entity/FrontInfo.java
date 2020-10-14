package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�t�����g���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:01:04)
 * @author: 
 */
public class FrontInfo {
	protected MrInfo leftmrinfo;
	protected String leftmrID;
	protected int leftMsgCount;
	protected MsgInfo leftmsginfo;
	protected CompanyTable leftcompanytable;
	protected MrInfo rightmrinfo;
	protected String rightmrID;
	protected int rightMsgCount;
	protected MsgInfo rightmsginfo;
	protected CompanyTable rightcompanytable;
	protected int newMsgCount;
	protected DoctorInfo doctorinfo;
/**
 * Fronthfo �R���X�g���N�^�[�E�R�����g�B
 */
public FrontInfo() {
	super();
}
/**
 * <h3>��t���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 04:35:21)
 * @return jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public DoctorInfo getDoctorinfo() {
	return doctorinfo;
}
/**
 * <h3>�l�q����Џ��̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:55:32)
 * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public CompanyTable getLeftcompanytable() {
	return leftcompanytable;
}
/**
 * <h3>���l�q�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/04 �ߌ� 10:33:17)
 * @return java.lang.String
 */
public java.lang.String getLeftmrID() {
	return leftmrID;
}
/**
 * <h3>�l�q���l�q���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 03:28:55)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getLeftmrinfo() {
	return leftmrinfo;
}
/**
 * <h3>�l�q�����b�Z�[�W�J�E���g�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @return int
 */
public int getLeftMsgCount() {
	return leftMsgCount;
}
/**
 * <h3>�l�q���l�r�f���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:00:34)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getLeftmsginfo() {
	return leftmsginfo;
}
/**
 * <h3>�V�K���b�Z�[�W�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @return int
 */
public int getNewMsgCount() {
	return newMsgCount;
}
/**
 * <h3>�l�q�E��Џ��̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:55:32)
 * @return jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public CompanyTable getRightcompanytable() {
	return rightcompanytable;
}
/**
 * <h3>�E�l�q�h�c�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/04 �ߌ� 10:33:17)
 * @return java.lang.String
 */
public java.lang.String getRightmrID() {
	return rightmrID;
}
/**
 * <h3>�l�q�E�l�q���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 03:28:55)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getRightmrinfo() {
	return rightmrinfo;
}
/**
 * <h3>�l�q�E���b�Z�[�W�J�E���g�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @return int
 */
public int getRightMsgCount() {
	return rightMsgCount;
}
/**
 * <h3>�l�q�E�l�r�f���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:00:34)
 * @return jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public MsgInfo getRightmsginfo() {
	return rightmsginfo;
}
/**
 * <h3>��t���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 04:35:21)
 * @param newDoctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public void setDoctorinfo(DoctorInfo newDoctorinfo) {
	doctorinfo = newDoctorinfo;
}
/**
 * <h3>�l�q����Џ��̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:55:32)
 * @param newLeftcompanytable jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public void setLeftcompanytable(CompanyTable newLeftcompanytable) {
	leftcompanytable = newLeftcompanytable;
}
/**
 * <h3>���l�q�h�c�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/04 �ߌ� 10:33:17)
 * @param newLeftmrID java.lang.String
 */
public void setLeftmrID(java.lang.String newLeftmrID) {
	leftmrID = newLeftmrID;
}
/**
 * <h3>�l�q���l�q���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 03:28:55)
 * @param newLeftmrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setLeftmrinfo(MrInfo newLeftmrinfo) {
	leftmrinfo = newLeftmrinfo;
}
/**
 * <h3>�l�q�����b�Z�[�W�J�E���g�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @param newLeftMsgCount int
 */
public void setLeftMsgCount(int newLeftMsgCount) {
	leftMsgCount = newLeftMsgCount;
}
/**
 * <h3>�l�q���l�r�f���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:00:34)
 * @param newLeftmsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setLeftmsginfo(MsgInfo newLeftmsginfo) {
	leftmsginfo = newLeftmsginfo;
}
/**
 * <h3>�V�K���b�Z�[�W�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @param newNewMsgCount int
 */
public void setNewMsgCount(int newNewMsgCount) {
	newMsgCount = newNewMsgCount;
}
/**
 * <h3>�l�q�E��Џ��̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:55:32)
 * @param newRightcompanytable jp.ne.sonet.medipro.mr.server.entity.CompanyTable
 */
public void setRightcompanytable(CompanyTable newRightcompanytable) {
	rightcompanytable = newRightcompanytable;
}
/**
 * <h3>�E�l�q�h�c�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/04 �ߌ� 10:33:17)
 * @param newRightmrID java.lang.String
 */
public void setRightmrID(java.lang.String newRightmrID) {
	rightmrID = newRightmrID;
}
/**
 * <h3>�l�q�E�l�q���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 03:28:55)
 * @param newRightmrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setRightmrinfo(MrInfo newRightmrinfo) {
	rightmrinfo = newRightmrinfo;
}
/**
 * <h3>�l�q�E���b�Z�[�W�J�E���g�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߑO 11:15:03)
 * @param newRightMsgCount int
 */
public void setRightMsgCount(int newRightMsgCount) {
	rightMsgCount = newRightMsgCount;
}
/**
 * <h3>�l�q�E�l�r�f���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 04:00:34)
 * @param newRightmsginfo jp.ne.sonet.medipro.mr.server.entity.MsgInfo
 */
public void setRightmsginfo(MsgInfo newRightmsginfo) {
	rightmsginfo = newRightmsginfo;
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
	me.append("*********��t���*********" + "\n");
	me.append(doctorinfo + "\n");
	me.append("*********�l�q�����*********" + "\n");
	me.append(leftmrinfo + "\n");
	me.append(leftmrID + "\n");
	me.append(leftMsgCount + "\n");
	me.append(leftmsginfo + "\n");
	me.append(leftcompanytable + "\n");
	me.append("*********�l�q�E���*********" + "\n");
	me.append(rightmrinfo + "\n");
	me.append(rightmrID + "\n");
	me.append(rightMsgCount + "\n");
	me.append(rightmsginfo + "\n");
	me.append(rightcompanytable + "\n");
	me.append("*********����*********" + "\n");
	me.append(newMsgCount + "\n");
	
	return me.toString();
}
}
