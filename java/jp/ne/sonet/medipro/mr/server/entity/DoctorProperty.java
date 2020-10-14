package jp.ne.sonet.medipro.mr.server.entity;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 *　統合医師情報.
 */
public class DoctorProperty {
    static final String PLACE = "0000000001";
    static final String NETWORK = "0000000002";
    static final String WORK_TYPE = "0000000003";
    static final String CLINIC = "0000000004";
    static final String SPECIAL = "0000000005";
    static final String ACADEMIC = "0000000006";
    static final String COLLEGE = "0000000007";
    static final String FACULTY = "0000000008";
    static final String YEAR = "0000000009";
    static final String PREFECTURE = "0000000010";
    static final String REGION = "0000000011";
    static final String MEDIPRO_MEMBER = "0000000012";
    static final String SONET_MEMBER = "0000000013";
    static final String NEED_NOTIFY = "0000000014";
    static final String SHIKAKU = "0000000015";
    static final String ADDRESS = "0000000016";
    static final String TEL = "0000000017";
    static final String OFFICE_ADDRESS = "0000000018";
    static final String OFFICE_TEL = "0000000019";

    //医師に相当する資格コード
    static final String DOCTOR = "0001";

    //医師情報
    private DoctorInfo doctor;
    //勤務先情報
    private KinmusakiInfo kinmusaki;
    //プロファイルリスト
    private Hashtable profileList;

    //誕生年
    private String birthYear;
    //誕生月
    private String birthMonth;
    //誕生日
    private String birthDay;
    //元号
    private String gengo;
    //年
    private String year;
    //再確認Email
    private String reEmail;

    public String getBirthYear() {
	return birthYear;
    }

    public void setBirthYear(String value) {
	birthYear = value;
    }

    public String getBirthMonth() {
	return birthMonth;
    }

    public void setBirthMonth(String value) {
	birthMonth = value;
    }

    public String getBirthDay() {
	return birthDay;
    }

    public void setBirthDay(String value) {
	birthDay = value;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String value) {
	year = value;
    }

    public String getYearAD() {
	return get(YEAR);
    }

    public void setYearAD(String value) {
	put(YEAR, value);
    }

    public String getGengo() {
	return gengo;
    }

    public void setGengo(String value) {
	gengo = value;
    }

    public DoctorProperty(String drId) {
	doctor = new DoctorInfo();
	doctor.setDrID(drId);
	kinmusaki = new KinmusakiInfo();
	kinmusaki.setDrId(drId);
	profileList = new Hashtable();
    }
    
    /**
     * 指定したkeyでプロファイル情報を保持する
     */
    private void put(String key, String value) {
	DoctorProfileInfo info = new DoctorProfileInfo();
	info.setDrId(getDrId());
	info.setItemCd(key);
	info.setSeq("1");
	info.setItem(value);
	
	profileList.put(key, info);
    }

    /**
     * 指定したkeyで複数のプロファイル情報を保持する
     */
    private void puts(String key, Vector list) {
	Vector pList = new Vector();

	for (int i = 0; i < list.size(); i++) {
	    DoctorProfileInfo info = new DoctorProfileInfo();
	    info.setDrId(getDrId());
	    info.setItemCd(key);
	    info.setSeq(new Integer(i+1).toString());
	    info.setItem((String)list.elementAt(i));
	    
	    pList.addElement(info);
	}

	profileList.put(key, pList);
    }

    /**
     * 指定したkeyに対応する文字列を取得する
     */
    private String get(String key) {
	DoctorProfileInfo info = (DoctorProfileInfo)profileList.get(key);

	if (info == null) {
	    return null;
	}

	return info.getItem();
    }

    private Vector gets(String key) {
	Vector v = (Vector)profileList.get(key);

	if (v == null) {
	    return new Vector();
	}

	Vector list = new Vector();
	Enumeration e = v.elements();
	while (e.hasMoreElements()) {
	    list.addElement(((DoctorProfileInfo)e.nextElement()).getItem());
	}

	return list;
    }

    public String getDrId() {
	return doctor.getDrID();
    }

    public String getName() {
	return doctor.getName();
    }

    public void setName(String value) {
	doctor.setName(value);
    }

    public String getNameKana() {
	return doctor.getNameKana();
    }

    public void setNameKana(String value) {
	doctor.setNameKana(value);
    }

    public String getEmail() {
	return doctor.getEmail();
    }

    public void setEmail(String value) {
	doctor.setEmail(value);
    }

    public String getReEmail() {
	return reEmail;
    }

    public void setReEmail(String value) {
	reEmail = value;
    }

    public String getKoumuin() {
	return doctor.getKoumuin();
    }

    public void setKoumuin(String value) {
	doctor.setKoumuin(value);
    }

    public String getKinmusaki() {
	return kinmusaki.getKinmusakiName();
    }

    public void setKinmusaki(String value) {
	kinmusaki.setKinmusakiName(value);
    }

    public String getPlace() {
	return get(PLACE);
    }

    public void setPlace(String value) {
	put(PLACE, value);
    }

    public String getNetwork() {
	return get(NETWORK);
    }

    public void setNetwork(String value) {
	put(NETWORK, value);
    }

    public String getWorkType() {
	return get(WORK_TYPE);
    }
    
    public void setWorkType(String value) {
	put(WORK_TYPE, value);
    }

    public String getClinic() {
	return get(CLINIC);
    }

    public void setClinic(String value) {
	put(CLINIC, value);
    }

    public Vector getSpecialList() {
	return gets(SPECIAL);
    }

    public void setSpecialList(Vector list) {
	puts(SPECIAL, list);
    }

    public Vector getAcademicList() {
	return gets(ACADEMIC);
    }

    public void setAcademicList(Vector list) {
	puts(ACADEMIC, list);
    }

    public String getCollege() {
	return get(COLLEGE);
    }

    public void setCollege(String value) {
	put(COLLEGE, value);
    }

    public String getFaculty() {
	return get(FACULTY);
    }

    public void setFaculty(String value) {
	put(FACULTY, value);
    }

    public String getPrefecture() {
	return get(PREFECTURE);
    }

    public void setPrefecture(String value) {
	put(PREFECTURE, value);
    }

    public String getRegion() {
	return get(REGION);
    }

    public void setRegion(String value) {
	put(REGION, value);
    }

    public String getMediproMember() {
	return get(MEDIPRO_MEMBER);
    }

    public void setMediproMember(String value) {
	put(MEDIPRO_MEMBER, value);
    }

    public String getSonetMember() {
	return get(SONET_MEMBER);
    }

    public void setSonetMember(String value) {
	put(SONET_MEMBER, value);
    }

    public String getNeedNotify() {
	return get(NEED_NOTIFY);
    }

    public void setNeedNotify(String value) {
	put(NEED_NOTIFY, value);
    }

    public String getShikaku() {
	return get(SHIKAKU);
    }

    public void setShikaku(String value) {
	put(SHIKAKU, value);
    }

    public String getAddress() {
	return get(ADDRESS);
    }

    public void setAddress(String value) {
	put(ADDRESS, value);
    }

    public String getTel() {
	return get(TEL);
    }

    public void setTel(String value) {
	put(TEL, value);
    }

    public String getOfficeAddress() {
	return get(OFFICE_ADDRESS);
    }

    public void setOfficeAddress(String value) {
	put(OFFICE_ADDRESS, value);
    }

    public String getOfficeTel() {
	return get(OFFICE_TEL);
    }

    public void setOfficeTel(String value) {
	put(OFFICE_TEL, value);
    }

    public boolean isEnqueteCompleted() {
//	if (getPlace().equals("") ||
//	    getNetwork().equals("") ||
//	    getShikaku().equals("") ||
//	    getCollege().equals("") ||
//	    getFaculty().equals("") ||
//	    getYear().equals("") ||
//	    getPrefecture().equals("") ||
//	    ((getPrefecture().equals("13") ||  //東京
//	      getPrefecture().equals("27")) && //大阪
//	     getRegion().equals(""))) {
	if (getShikaku().equals("")) {
	    return false;
	} else if (getShikaku().equals(DOCTOR)) {
	    if (getWorkType().equals("") ||
		getClinic().equals("") ||
		getSpecialList().size() == 0) {
		return false;
	    }
	    
//	    Enumeration e = getAcademicList().elements();
//	    while (e.hasMoreElements()) {
//		if (!((String)e.nextElement()).equals("")) {
//		    return true;
//		}
//	    }
	}

	return true;
    }

    public boolean isEntryCompleted() {
	if (getName().equals("") ||
	    getNameKana().equals("") ||
	    getKinmusaki().equals("") ||
	    getBirthYear().equals("") ||
	    getBirthMonth().equals("") ||
	    getBirthDay().equals("") ||
	    getEmail().equals("")) {
	    return false;
	}

	return true;
    }

    public boolean isInputCompleted() {
	if (getName().equals("") ||
	    getNameKana().equals("") ||
	    getKinmusaki().equals("") ||
	    getBirthYear().equals("") ||
	    getBirthMonth().equals("") ||
	    getBirthDay().equals("") ||
	    getEmail().equals("")) {
	    return false;
	}

	return true;
    }

    public String toString() {
	return "(" + getName()
	    + "," + getNameKana()
	    + "," + getKinmusaki()
	    + "," + getKoumuin()
	    + "," + getBirthYear() + getBirthMonth() + getBirthDay()
	    + "," + getEmail()
	    + "," + getPlace()
	    + "," + getNetwork()
	    + "," + getWorkType()
	    + "," + getClinic()
	    + "," + getCollege()
	    + "," + getShikaku()
	    + "," + getSpecialList()
	    + "," + getAcademicList()
	    + "," + getFaculty()
	    + "," + getGengo()
	    + "," + getYear()
	    + "," + getYearAD()
	    + "," + getPrefecture()
	    + "," + getRegion()
	    + "," + getMediproMember()
	    + "," + getSonetMember()
	    + "," + getNeedNotify()
	    + ")";
    }
}
