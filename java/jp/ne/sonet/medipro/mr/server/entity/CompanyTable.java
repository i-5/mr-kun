package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>��Ѓe�[�u�����</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/30 �ߌ� 06:31:40)
 * @author: 
 */
 
public class CompanyTable {
	protected String companyCD;
	protected String companyKbn;
	protected String companyName;
	protected String cdPrefix;
	protected String pictureCD;
	protected String linkCD;
	protected LinkLibInfo linklibinfo;
/**
 * CompanyTable �R���X�g���N�^�[�E�R�����g�B
 */
public CompanyTable() {
	super();
}
/**
 * <h3>�R�[�hprefix�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCdPrefix() {
	return cdPrefix;
}
/**
 * <h3>��ЃR�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyCD() {
	return companyCD;
}
/**
 * <h3>��Ћ敪�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyKbn() {
	return companyKbn;
}
/**
 * <h3>��Ж��̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyName() {
	return companyName;
}
/**
 * <h3>�f�t�H���g�����N�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getLinkCD() {
	return linkCD;
}
/**
 * <h3>�����N���C�u�����̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 06:13:59)
 * @return jp.ne.sonet.medipro.mr.server.entity.LinkLibInfo
 */
public LinkLibInfo getLinklibinfo() {
	return linklibinfo;
}
/**
 * <h3>�f�t�H���g�摜�R�[�h�̎擾</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>�R�[�hprefix�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newCdPrefix java.lang.String
 */
public void setCdPrefix(java.lang.String newCdPrefix) {
	cdPrefix = newCdPrefix;
}
/**
 * <h3>��ЃR�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newCompanyCD java.lang.String
 */
public void setCompanyCD(java.lang.String newCompanyCD) {
	companyCD = newCompanyCD;
}
/**
 * <h3>��Ћ敪�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newCompanyKbn java.lang.String
 */
public void setCompanyKbn(java.lang.String newCompanyKbn) {
	companyKbn = newCompanyKbn;
}
/**
 * <h3>��Ж��̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newCompanyName java.lang.String
 */
public void setCompanyName(java.lang.String newCompanyName) {
	companyName = newCompanyName;
}
/**
 * <h3>�f�t�H���g�����N�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newLinkCD java.lang.String
 */
public void setLinkCD(java.lang.String newLinkCD) {
	linkCD = newLinkCD;
}
/**
 * <h3>�����N���C�u�����̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/03 �ߌ� 06:13:59)
 * @param newLinklibinfo jp.ne.sonet.medipro.mr.server.entity.LinkLibInfo
 */
public void setLinklibinfo(LinkLibInfo newLinklibinfo) {
	linklibinfo = newLinklibinfo;
}
/**
 * <h3>�f�t�H���g�摜�R�[�h�̃Z�b�g</h3>
 * 
 * <br>
 * �����Ń��\�b�h�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 04:43:10)
 * @param newPictureCD java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
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
	me.append(cdPrefix + "\n");
	me.append(companyCD + "\n");
	me.append(companyKbn + "\n");
	me.append(companyName + "\n");
	me.append(linkCD + "\n");
	me.append(pictureCD + "\n");	
	return me.toString();
}
}
