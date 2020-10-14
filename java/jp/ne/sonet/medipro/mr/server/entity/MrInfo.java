package jp.ne.sonet.medipro.mr.server.entity;

import java.util.*;

/**
 * <h3>ＭＲ情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:01:58)
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
 * MrInfo コンストラクター・コメント。
 */
public MrInfo() {
}
/**
 * <h3>住所の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getAddress() {
	return address;
}
/**
 * <h3>コール内容情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 09:09:03)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getCallnaiyoinfo() {
	return callnaiyoinfo;
}
/**
 * <h3>キャッチ画像情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 09:09:03)
 * @return java.util.Enumeration
 */
public java.util.Enumeration getCatchpctinfo() {
	return catchpctinfo;
}
/**
 * <h3>cc電子メールアドレス１の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail1() {
	return ccEmail1;
}
/**
 * <h3>cc電子メールアドレス２の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail2() {
	return ccEmail2;
}
/**
 * <h3>cc電子メールアドレス３の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail3() {
	return ccEmail3;
}
/**
 * <h3>cc電子メールアドレス４の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCcEmail4() {
	return ccEmail4;
}
/**
 * <h3>会社デフォルトキャッチ画像の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:51:03)
 * @return jp.ne.sonet.medipro.mr.server.entity.CatchPctInfo
 */
public CatchPctInfo getCoCatchpctinfo() {
	return coCatchpctinfo;
}
/**
 * <h3>会社コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCompanyCD() {
	return companyCD;
}
/**
 * <h3>会社名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getCompanyName() {
	return companyName;
}
/**
 * <h3>営業日設定区分の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoDateKbn() {
	return eigyoDateKbn;
}
/**
 * <h3>営業終了時間の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoEndTime() {
	return eigyoEndTime;
}
/**
 * <h3>営業開始時間の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoStartTime() {
	return eigyoStartTime;
}
/**
 * <h3>営業時間設定区分の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEigyoTimeKbn() {
	return eigyoTimeKbn;
}
/**
 * <h3>公開電子メールアドレスの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getEmail() {
	return email;
}
/**
 * <h3>ＦＡＸ番号の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getFaxNo() {
	return faxNo;
}

public java.lang.String getId() {
	return id;
}

/**
 * <h3>デフォルト自己紹介の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getJikosyokai() {
	return jikosyokai;
}
/**
 * <h3>携帯電話の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 09:54:15)
 * @return java.lang.String
 */
public java.lang.String getKeitaiNo() {
	return keitaiNo;
}
/**
 * <h3>ＭＲデフォルトキャッチ画像の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 09:00:51)
 * @return jp.ne.sonet.medipro.mr.server.entity.CatchPctInfo
 */
public CatchPctInfo getMrCatchpctinfo() {
	return mrCatchpctinfo;
}
/**
 * <h3>ＭＲ役職名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 07:45:11)
 * @return java.lang.String
 */
public java.lang.String getMrYakusyoku() {
	return mrYakusyoku;
}
/**
 * <h3>氏名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * <h3>氏名（カナ）の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午前 12:44:29)
 * @return java.lang.String
 */
public java.lang.String getNameKana() {
	return nameKana;
}
/**
 * <h3>パスワードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/23 午前 01:51:17)
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
 * <h3>前回ログイン日時の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 05:33:33)
 * @return java.lang.String
 */
public java.lang.String getPreviousLoginTime() {
	return previousLoginTime;
}
/**
 * <h3>電話番号の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getTelNo() {
	return telNo;
}
/**
 * <h3>郵便番号の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @return java.lang.String
 */
public java.lang.String getZipCD() {
	return zipCD;
}
/**
 * <h3>住所のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newAddress java.lang.String
 */
public void setAddress(java.lang.String newAddress) {
	address = newAddress;
}
/**
 * <h3>コール内容情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 09:09:03)
 * @param newCallnaiyoinfo java.util.Enumeration
 */
public void setCallnaiyoinfo(java.util.Enumeration newCallnaiyoinfo) {
	callnaiyoinfo = newCallnaiyoinfo;
}
/**
 * <h3>キャッチ画像情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 09:09:03)
 * @param newCatchpctinfo java.util.Enumeration
 */
public void setCatchpctinfo(java.util.Enumeration newCatchpctinfo) {
	catchpctinfo = newCatchpctinfo;
}
/**
 * <h3>cc電子メールアドレス１のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCc_email1 java.lang.String
 */
public void setCcEmail1(java.lang.String newCcEmail1) {
	ccEmail1 = newCcEmail1;
}
/**
 * <h3>cc電子メールアドレス２のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCc_email2 java.lang.String
 */
public void setCcEmail2(java.lang.String newCcEmail2) {
	ccEmail2 = newCcEmail2;
}
/**
 * <h3>cc電子メールアドレス３のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCc_email3 java.lang.String
 */
public void setCcEmail3(java.lang.String newCcEmail3) {
	ccEmail3 = newCcEmail3;
}
/**
 * <h3>cc電子メールアドレス４のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCc_email4 java.lang.String
 */
public void setCcEmail4(java.lang.String newCcEmail4) {
	ccEmail4 = newCcEmail4;
}
/**
 * <h3>ＭＲデフォルトキャッチ画像のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午後 03:51:03)
 * @param newCoCatchpctinfo jp.ne.sonet.medipro.mr.server.entity.CatchPctInfo
 */
public void setCoCatchpctinfo(CatchPctInfo newCoCatchpctinfo) {
	coCatchpctinfo = newCoCatchpctinfo;
}
/**
 * <h3>会社コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCompanyCD java.lang.String
 */
public void setCompanyCD(java.lang.String newCompanyCD) {
	companyCD = newCompanyCD;
}
/**
 * <h3>会社名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newCompany_name java.lang.String
 */
public void setCompanyName(java.lang.String newCompanyName) {
	companyName = newCompanyName;
}
/**
 * <h3>営業日設定区分のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newEigyoDateKbn java.lang.String
 */
public void setEigyoDateKbn(java.lang.String newEigyoDateKbn) {
	eigyoDateKbn = newEigyoDateKbn;
}
/**
 * <h3>営業終了時間のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newEigyoEndTime java.lang.String
 */
public void setEigyoEndTime(java.lang.String newEigyoEndTime) {
	eigyoEndTime = newEigyoEndTime;
}
/**
 * <h3>営業開始時間のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newEigyoStartTime java.lang.String
 */
public void setEigyoStartTime(java.lang.String newEigyoStartTime) {
	eigyoStartTime = newEigyoStartTime;
}
/**
 * <h3>営業時間設定区分のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newEigyoTimeKbn java.lang.String
 */
public void setEigyoTimeKbn(java.lang.String newEigyoTimeKbn) {
	eigyoTimeKbn = newEigyoTimeKbn;
}
/**
 * <h3>公開電子メールアドレスのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newEmail java.lang.String
 */
public void setEmail(java.lang.String newEmail) {
	email = newEmail;
}
/**
 * <h3>ＦＡＸ番号のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newFaxNo java.lang.String
 */
public void setFaxNo(java.lang.String newFaxNo) {
	faxNo = newFaxNo;
}

public void setId(java.lang.String ID) {
	id = ID;
}

/**
 * <h3>デフォルト自己紹介のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newJikosyokai java.lang.String
 */
public void setJikosyokai(java.lang.String newJikosyokai) {
	jikosyokai = newJikosyokai;
}
/**
 * <h3>携帯電話のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 09:54:15)
 * @param newKeitaiNo java.lang.String
 */
public void setKeitaiNo(java.lang.String newKeitaiNo) {
	keitaiNo = newKeitaiNo;
}
/**
 * <h3>ＭＲデフォルトキャッチ画像のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 09:00:51)
 * @param newCatchpctinfo jp.ne.sonet.medipro.mr.server.entity.CatchPctInfo
 */
public void setMrCatchpctinfo(CatchPctInfo newMrCatchpctinfo) {
	mrCatchpctinfo = newMrCatchpctinfo;
}
/**
 * <h3>ＭＲ役職名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/29 午後 07:45:11)
 * @param newMrYakusyokuCD java.lang.String
 */
public void setMrYakusyoku(java.lang.String newMrYakusyoku) {
	mrYakusyoku = newMrYakusyoku;
}
/**
 * <h3>氏名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * <h3>氏名（カナ）のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午前 12:44:29)
 * @param newNameKana java.lang.String
 */
public void setNameKana(java.lang.String newNameKana) {
	nameKana = newNameKana;
}
/**
 * <h3>パスワードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/23 午前 01:51:17)
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
 * <h3>前回ログイン日時のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/26 午前 05:33:33)
 * @param newPreviousLoginTime java.lang.String
 */
public void setPreviousLoginTime(java.lang.String newPreviousLoginTime) {
	previousLoginTime = newPreviousLoginTime;
}
/**
 * <h3>電話番号のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newTelNo java.lang.String
 */
public void setTelNo(java.lang.String newTelNo) {
	telNo = newTelNo;
}
/**
 * <h3>郵便番号のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 06:17:02)
 * @param newZipCD java.lang.String
 */
public void setZipCD(java.lang.String newZipCD) {
	zipCD = newZipCD;
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
