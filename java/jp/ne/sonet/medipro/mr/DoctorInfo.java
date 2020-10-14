package jp.ne.sonet.medipro.mr;

/**
 * @author: Harry Behrens
 */
public class DoctorInfo {
    protected String drID;
    protected String SysDrCD;
    protected String password;
    protected String name;
    protected String nameKana;
    protected String kinmusakiName;
    protected String mrkunMishiyoFlg;
    protected String email;
    protected int point;
    protected String koumuin;

    /**
     * DoctorInfo コンストラクター・コメント。
     */
    public DoctorInfo() {
    }
    /**
     * @return String
     */
    public String getDrID() {
	return drID;
    }
    /**
     * @return String
     */
    public String getSysDrCD() {
	return SysDrCD;
    }
    /**
     * @return String
     */
    public String getKinmusakiName() {
	return kinmusakiName;
    }
    /**
     * @return String
     */
    public String getKoumuin() {
	return koumuin;
    }
    /**
     * @return String
     */
    public String getMrkunMishiyoFlg() {
	return mrkunMishiyoFlg;
    }
    /**
     * @return String
     */
    public String getName() {
	return name;
    }
    /**
     * @return String
     */
    public String getNameKana() {
	return nameKana;
    }
    /**
     * @return String
     */
    public String getPassword() {
	return password;
    }
    /**
     * @return int
     */
    public int getPoint() {
	return point;
    }
    /**
     * @param newDrID String
     */
    public void setDrID(String newDrID) {
	drID = newDrID;
    }
    /**
     * @param newSysDrCD String
     */
    public void setSysDrCD(String newSysDrCD) {
	SysDrCD = newSysDrCD;
    }
    /**
     * @param newKinmusakiName String
     */
    public void setKinmusakiName(String newKinmusakiName) {
	kinmusakiName = newKinmusakiName;
    }
    /**
     * @param newKoumuin String
     */
    public void setKoumuin(String newKoumuin) {
	koumuin = newKoumuin;
    }
    /**
     * @param newMrkunMishiyoFlg String
     */
    public void setMrkunMishiyoFlg(String newMrkunMishiyoFlg) {
	mrkunMishiyoFlg = newMrkunMishiyoFlg;
    }
    /**
     * @param newName String
     */
    public void setName(String newName) {
	name = newName;
    }
    /**
     * @param newNameKana String
     */
    public void setNameKana(String newNameKana) {
	nameKana = newNameKana;
    }
    /**
     * @param newPassword String
     */
    public void setPassword(String newPassword) {
	password = newPassword;
    }
    /**
     * @param newPoint int
     */
    public void setPoint(int newPoint) {
	point = newPoint;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String str) {
	email = str;
    }

    /**
     * @return String
     */
    public String toString() {
	StringBuffer me = new StringBuffer();
	me.append(name + "\n");
	me.append(nameKana + "\n");
	me.append(kinmusakiName + "\n");
	me.append(mrkunMishiyoFlg + "\n");
	me.append(email + "\n");
	me.append(point + "\n");
	me.append(password + "\n");
	return me.toString();
    }
}
