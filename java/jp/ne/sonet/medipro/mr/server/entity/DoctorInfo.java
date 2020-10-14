package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>医師情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:19:16)
 * @author: 
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
     * <h3>医師ＩＤの取得</h3>
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/18 午後 04:54:37)
     * @return String
     */
    public String getDrID() {
	return drID;
    }
    /**
     * <h3>システム医師コードの取得</h3>
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (01/01/14 午後 03:15:15)
     * @return String
     */
    public String getSysDrCD() {
	return SysDrCD;
    }
    /**
     * <h3>勤務先名の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @return String
     */
    public String getKinmusakiName() {
	return kinmusakiName;
    }
    /**
     * <h3>公務員区分の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/25 午後 04:23:35)
     * @return String
     */
    public String getKoumuin() {
	return koumuin;
    }
    /**
     * <h3>ＭＲ君未使用フラグの取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @return String
     */
    public String getMrkunMishiyoFlg() {
	return mrkunMishiyoFlg;
    }
    /**
     * <h3>氏名の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @return String
     */
    public String getName() {
	return name;
    }
    /**
     * <h3>氏名(カナ)の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/03 午前 12:42:50)
     * @return String
     */
    public String getNameKana() {
	return nameKana;
    }
    /**
     * <h3>パスワードの取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/23 午前 02:06:33)
     * @return String
     */
    public String getPassword() {
	return password;
    }
    /**
     * <h3>ポイントの取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @return int
     */
    public int getPoint() {
	return point;
    }
    /**
     * <h3>医師ＩＤのセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/18 午後 04:54:37)
     * @param newDrID String
     */
    public void setDrID(String newDrID) {
	drID = newDrID;
    }
    /**
     * <h3>システム医師コードのセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (01/01/14 午後 03:15:15)
     * @param newSysDrCD String
     */
    public void setSysDrCD(String newSysDrCD) {
	SysDrCD = newSysDrCD;
    }
    /**
     * <h3>勤務先名のセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @param newKinmusakiName String
     */
    public void setKinmusakiName(String newKinmusakiName) {
	kinmusakiName = newKinmusakiName;
    }
    /**
     * <h3>公務員区分のセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/25 午後 04:23:35)
     * @param newKoumuin String
     */
    public void setKoumuin(String newKoumuin) {
	koumuin = newKoumuin;
    }
    /**
     * <h3>ＭＲ君未使用フラグのセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @param newMrkunMishiyoFlg String
     */
    public void setMrkunMishiyoFlg(String newMrkunMishiyoFlg) {
	mrkunMishiyoFlg = newMrkunMishiyoFlg;
    }
    /**
     * <h3>氏名のセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
     * @param newName String
     */
    public void setName(String newName) {
	name = newName;
    }
    /**
     * <h3>氏名(カナ)のセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/03 午前 12:42:50)
     * @param newNameKana String
     */
    public void setNameKana(String newNameKana) {
	nameKana = newNameKana;
    }
    /**
     * <h3>パスワードのセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/23 午前 02:06:33)
     * @param newPassword String
     */
    public void setPassword(String newPassword) {
	password = newPassword;
    }
    /**
     * <h3>ポイントのセット</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/21 午後 08:22:46)
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
     * <h3>文字列化する</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/19 23:51:35)
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
