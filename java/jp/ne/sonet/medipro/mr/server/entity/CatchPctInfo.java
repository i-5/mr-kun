package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>�L���b�`�摜���</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:18:38)
 * @author: 
 */
public class CatchPctInfo {
	protected String picture;
	protected String pictureType;
	protected java.lang.String pictureCD;
	protected java.lang.String pictureName;
/**
 * CatchPctInfo �R���X�g���N�^�[�E�R�����g�B
 */
public CatchPctInfo() {
}
/**
 * <h3>�摜�t�@�C�����e�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPicture() {
	return picture;
}
/**
 * <h3>�摜�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>�摜���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:14:51)
 * @return java.lang.String
 */
public java.lang.String getPictureName() {
	return pictureName;
}
/**
 * <h3>�摜�`���̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:24:03)
 * @return java.lang.String
 */
public java.lang.String getPictureType() {
	return pictureType;
}
/**
 * <h3>�摜�t�@�C�����e�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:24:03)
 * @param newPicture java.lang.String
 */
public void setPicture(java.lang.String newPicture) {
	picture = newPicture;
}
/**
 * <h3>�摜�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:14:51)
 * @param newPicture_cd java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
}
/**
 * <h3>�摜���̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 08:14:51)
 * @param newPicture_name java.lang.String
 */
public void setPictureName(java.lang.String newPictureName) {
	pictureName = newPictureName;
}
/**
 * <h3>�摜�`���̃Z�b�g</h3>
 * 
 * <br>

 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 06:24:03)
 * @param newPictureType java.lang.String
 */
public void setPictureType(java.lang.String newPictureType) {
	pictureType = newPictureType;
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
	me.append(pictureCD + "\n");
	me.append(pictureName + "\n");
	me.append(picture + "\n");
	me.append(pictureType + "\n");	
	return me.toString();
}
}
