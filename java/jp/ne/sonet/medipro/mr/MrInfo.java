package jp.ne.sonet.medipro.mr;

import java.util.*;

/**
 * <h3>�l�q���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:01:58)
 * @author: 
 */
public class MrInfo {
	protected String companyCD;
	protected String id;
	protected String name;
	protected String jikosyokai;
	protected String picture_cd;
	protected String telNo;
	protected String keitaiNo;
	protected String faxNo;
	protected String email;
	protected String zipCD;
	protected String address;
	protected String ccEmail1;
	protected String ccEmail2;
	protected String ccEmail3;
	protected String ccEmail4;
	protected String eigyoDateKbn;
	protected String eigyoTimeKbn;
	protected String eigyoStartTime;
	protected String eigyoEndTime;
	protected String companyName;
	protected String password;
	protected String previousLoginTime;
	protected CatchPctInfo mrCatchpctinfo;
	protected CatchPctInfo coCatchpctinfo;
	protected Enumeration callnaiyoinfo;
	protected Enumeration catchpctinfo;
	protected java.lang.String mrYakusyoku;
	protected java.lang.String nameKana;
/**
 * MrInfo �R���X�g���N�^�[�E�R�����g�B
 */
public MrInfo() {
}
/**
 * <h3>�Z���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getAddress() {
	return address;
}
/**
 * <h3>�R�[�����e���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 09:09:03)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getCallnaiyoinfo() {
	return callnaiyoinfo;
}
/**
 * <h3>�L���b�`�摜���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 09:09:03)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getCatchpctinfo() {
	return catchpctinfo;
}
/**
 * <h3>cc�d�q���[���A�h���X�P�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail1() {
	return ccEmail1;
}
/**
 * <h3>cc�d�q���[���A�h���X�Q�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail2() {
	return ccEmail2;
}
/**
 * <h3>cc�d�q���[���A�h���X�R�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail3() {
	return ccEmail3;
}
/**
 * <h3>cc�d�q���[���A�h���X�S�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail4() {
	return ccEmail4;
}
/**
 * <h3>��Ѓf�t�H���g�L���b�`�摜�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/26 �ߌ� 03:51:03)
 * @return jp.ne.sonet.medipro.mr.server.entity.CatchPctInfo
 */
public CatchPctInfo getCoCatchpctinfo() {
	return coCatchpctinfo;
}
/**
 * <h3>��ЃR�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCompanyCD() {
	return companyCD;
}
/**
 * <h3>��Ж��̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCompanyName() {
	return companyName;
}
/**
 * <h3>�c�Ɠ��ݒ�敪�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoDateKbn() {
	return eigyoDateKbn;
}
/**
 * <h3>�c�ƏI�����Ԃ̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoEndTime() {
	return eigyoEndTime;
}
/**
 * <h3>�c�ƊJ�n���Ԃ̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoStartTime() {
	return eigyoStartTime;
}
/**
 * <h3>�c�Ǝ��Ԑݒ�敪�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoTimeKbn() {
	return eigyoTimeKbn;
}
/**
 * <h3>���J�d�q���[���A�h���X�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEmail() {
	return email;
}
/**
 * @return java.lang.String
 */
public java.lang.String getFaxNo() {
	return faxNo;
}

public java.lang.String getId() {
	return id;
}

/**
 * <h3>�f�t�H���g���ȏЉ�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getJikosyokai() {
	return jikosyokai;
}
/**
 * <h3>�g�ѓd�b�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 09:54:15)
 * @return java.lang.String
 */
public java.lang.String getKeitaiNo() {
	return keitaiNo;
}
/**
 * @return jp.ne.sonet.medipro.mr.CatchPctInfo
 */
public CatchPctInfo getMrCatchpctinfo() {
	return mrCatchpctinfo;
}
/**
 * @return java.lang.String
 */
public java.lang.String getMrYakusyoku() {
	return mrYakusyoku;
}
/**
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * @return java.lang.String
 */
public java.lang.String getNameKana() {
	return nameKana;
}
/**
 * @return java.lang.String
 */
public java.lang.String getPassword() {
	return password;
}

public java.lang.String getPictureCd()
{
    return picture_cd;
}
/**
 * @return java.lang.String
 */
public java.lang.String getPreviousLoginTime() {
	return previousLoginTime;
}
/**
 * @return java.lang.String
 */
public java.lang.String getTelNo() {
	return telNo;
}
/**
 * @return java.lang.String
 */
public java.lang.String getZipCD() {
	return zipCD;
}
/**
 * @param newAddress java.lang.String
 */
public void setAddress(java.lang.String newAddress) {
	address = newAddress;
}
/**
 * @param newCallnaiyoinfo java.util.Enumeration
 */
public void setCallnaiyoinfo(java.util.Enumeration newCallnaiyoinfo) {
	callnaiyoinfo = newCallnaiyoinfo;
}
/**
 * @param newCatchpctinfo java.util.Enumeration
 */
public void setCatchpctinfo(java.util.Enumeration newCatchpctinfo) {
	catchpctinfo = newCatchpctinfo;
}
/**
 * @param newCc_email1 java.lang.String
 */
public void setCcEmail1(java.lang.String newCcEmail1) {
	ccEmail1 = newCcEmail1;
}
/**
 * @param newCc_email2 java.lang.String
 */
public void setCcEmail2(java.lang.String newCcEmail2) {
	ccEmail2 = newCcEmail2;
}
/**
 * @param newCc_email3 java.lang.String
 */
public void setCcEmail3(java.lang.String newCcEmail3) {
	ccEmail3 = newCcEmail3;
}
/**
 * @param newCc_email4 java.lang.String
 */
public void setCcEmail4(java.lang.String newCcEmail4) {
	ccEmail4 = newCcEmail4;
}
/**
 * @param newCoCatchpctinfo jp.ne.sonet.medipro.mr.CatchPctInfo
 */
public void setCoCatchpctinfo(CatchPctInfo newCoCatchpctinfo) {
	coCatchpctinfo = newCoCatchpctinfo;
}
/**
 * @param newCompanyCD java.lang.String
 */
public void setCompanyCD(java.lang.String newCompanyCD) {
	companyCD = newCompanyCD;
}
/**
 * @param newCompany_name java.lang.String
 */
public void setCompanyName(java.lang.String newCompanyName) {
	companyName = newCompanyName;
}
/**
 * @param newEigyoDateKbn java.lang.String
 */
public void setEigyoDateKbn(java.lang.String newEigyoDateKbn) {
	eigyoDateKbn = newEigyoDateKbn;
}
/**
 * @param newEigyoEndTime java.lang.String
 */
public void setEigyoEndTime(java.lang.String newEigyoEndTime) {
	eigyoEndTime = newEigyoEndTime;
}
/**
 * @param newEigyoStartTime java.lang.String
 */
public void setEigyoStartTime(java.lang.String newEigyoStartTime) {
	eigyoStartTime = newEigyoStartTime;
}
/**
 * @param newEigyoTimeKbn java.lang.String
 */
public void setEigyoTimeKbn(java.lang.String newEigyoTimeKbn) {
	eigyoTimeKbn = newEigyoTimeKbn;
}
/**
 * @param newEmail java.lang.String
 */
public void setEmail(java.lang.String newEmail) {
	email = newEmail;
}
/**
 * @param newFaxNo java.lang.String
 */
public void setFaxNo(java.lang.String newFaxNo) {
	faxNo = newFaxNo;
}

public void setId(java.lang.String ID) {
	id = ID;
}

/**
 * @param newJikosyokai java.lang.String
 */
public void setJikosyokai(java.lang.String newJikosyokai) {
	jikosyokai = newJikosyokai;
}
/**
 * @param newKeitaiNo java.lang.String
 */
public void setKeitaiNo(java.lang.String newKeitaiNo) {
	keitaiNo = newKeitaiNo;
}
/**
 * @param newCatchpctinfo jp.ne.sonet.medipro.mr.CatchPctInfo
 */
public void setMrCatchpctinfo(CatchPctInfo newMrCatchpctinfo) {
	mrCatchpctinfo = newMrCatchpctinfo;
}
/**
 * @param newMrYakusyokuCD java.lang.String
 */
public void setMrYakusyoku(java.lang.String newMrYakusyoku) {
	mrYakusyoku = newMrYakusyoku;
}
/**
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * @param newNameKana java.lang.String
 */
public void setNameKana(java.lang.String newNameKana) {
	nameKana = newNameKana;
}
/**
 * @param newPassword java.lang.String
 */
public void setPassword(java.lang.String newPassword) {
	password = newPassword;
}

public void setPictureCd(java.lang.String cd)
{
    picture_cd = cd;
}
/**
 * @param newPreviousLoginTime java.lang.String
 */
public void setPreviousLoginTime(java.lang.String newPreviousLoginTime) {
	previousLoginTime = newPreviousLoginTime;
}
/**
 * @param newTelNo java.lang.String
 */
public void setTelNo(java.lang.String newTelNo) {
	telNo = newTelNo;
}
/**
 * @param newZipCD java.lang.String
 */
public void setZipCD(java.lang.String newZipCD) {
	zipCD = newZipCD;
}
/**
 * @return java.lang.String
 */
public String toString() {
	StringBuffer me = new StringBuffer();
	me.append(mrYakusyoku + "\n");
	me.append(companyCD + "\n");
	me.append(jikosyokai + "\n");
	me.append(name + "\n");
	me.append(nameKana + "\n");
	me.append(password + "\n");
	me.append(telNo + "\n");
	me.append(keitaiNo + "\n");
	me.append(faxNo + "\n");
	me.append(email + "\n");
	me.append(zipCD + "\n");
	me.append(address + "\n");
	me.append(ccEmail1 + "\n");
	me.append(ccEmail2 + "\n");
	me.append(ccEmail3 + "\n");
	me.append(ccEmail4 + "\n");
	me.append(eigyoDateKbn + "\n");
	me.append(eigyoTimeKbn + "\n");
	me.append(eigyoStartTime + "\n");
	me.append(eigyoEndTime + "\n");
	me.append(previousLoginTime + "\n");
	me.append(companyName + "\n");
	me.append(mrCatchpctinfo + "\n");
	me.append(coCatchpctinfo + "\n");
	me.append(callnaiyoinfo + "\n");
	me.append(catchpctinfo + "\n");
	return me.toString();
}
}