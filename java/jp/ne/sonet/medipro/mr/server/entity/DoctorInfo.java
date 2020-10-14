package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>��t���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 08:19:16)
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
     * DoctorInfo �R���X�g���N�^�[�E�R�����g�B
     */
    public DoctorInfo() {
    }
    /**
     * <h3>��t�h�c�̎擾</h3>
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 04:54:37)
     * @return String
     */
    public String getDrID() {
	return drID;
    }
    /**
     * <h3>�V�X�e����t�R�[�h�̎擾</h3>
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (01/01/14 �ߌ� 03:15:15)
     * @return String
     */
    public String getSysDrCD() {
	return SysDrCD;
    }
    /**
     * <h3>�Ζ��於�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @return String
     */
    public String getKinmusakiName() {
	return kinmusakiName;
    }
    /**
     * <h3>�������敪�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/25 �ߌ� 04:23:35)
     * @return String
     */
    public String getKoumuin() {
	return koumuin;
    }
    /**
     * <h3>�l�q�N���g�p�t���O�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @return String
     */
    public String getMrkunMishiyoFlg() {
	return mrkunMishiyoFlg;
    }
    /**
     * <h3>�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @return String
     */
    public String getName() {
	return name;
    }
    /**
     * <h3>����(�J�i)�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/03 �ߑO 12:42:50)
     * @return String
     */
    public String getNameKana() {
	return nameKana;
    }
    /**
     * <h3>�p�X���[�h�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/23 �ߑO 02:06:33)
     * @return String
     */
    public String getPassword() {
	return password;
    }
    /**
     * <h3>�|�C���g�̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @return int
     */
    public int getPoint() {
	return point;
    }
    /**
     * <h3>��t�h�c�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/18 �ߌ� 04:54:37)
     * @param newDrID String
     */
    public void setDrID(String newDrID) {
	drID = newDrID;
    }
    /**
     * <h3>�V�X�e����t�R�[�h�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (01/01/14 �ߌ� 03:15:15)
     * @param newSysDrCD String
     */
    public void setSysDrCD(String newSysDrCD) {
	SysDrCD = newSysDrCD;
    }
    /**
     * <h3>�Ζ��於�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @param newKinmusakiName String
     */
    public void setKinmusakiName(String newKinmusakiName) {
	kinmusakiName = newKinmusakiName;
    }
    /**
     * <h3>�������敪�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/25 �ߌ� 04:23:35)
     * @param newKoumuin String
     */
    public void setKoumuin(String newKoumuin) {
	koumuin = newKoumuin;
    }
    /**
     * <h3>�l�q�N���g�p�t���O�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @param newMrkunMishiyoFlg String
     */
    public void setMrkunMishiyoFlg(String newMrkunMishiyoFlg) {
	mrkunMishiyoFlg = newMrkunMishiyoFlg;
    }
    /**
     * <h3>�����̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
     * @param newName String
     */
    public void setName(String newName) {
	name = newName;
    }
    /**
     * <h3>����(�J�i)�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/03 �ߑO 12:42:50)
     * @param newNameKana String
     */
    public void setNameKana(String newNameKana) {
	nameKana = newNameKana;
    }
    /**
     * <h3>�p�X���[�h�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/23 �ߑO 02:06:33)
     * @param newPassword String
     */
    public void setPassword(String newPassword) {
	password = newPassword;
    }
    /**
     * <h3>�|�C���g�̃Z�b�g</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/21 �ߌ� 08:22:46)
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
     * <h3>�����񉻂���</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/19 23:51:35)
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
