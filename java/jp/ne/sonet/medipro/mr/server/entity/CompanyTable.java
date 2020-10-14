package jp.ne.sonet.medipro.mr.server.entity;

/**
 * <h3>会社テーブル情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/30 午後 06:31:40)
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
 * CompanyTable コンストラクター・コメント。
 */
public CompanyTable() {
	super();
}
/**
 * <h3>コードprefixの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCdPrefix() {
	return cdPrefix;
}
/**
 * <h3>会社コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyCD() {
	return companyCD;
}
/**
 * <h3>会社区分の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyKbn() {
	return companyKbn;
}
/**
 * <h3>会社名の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getCompanyName() {
	return companyName;
}
/**
 * <h3>デフォルトリンクコードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getLinkCD() {
	return linkCD;
}
/**
 * <h3>リンクライブラリの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 06:13:59)
 * @return jp.ne.sonet.medipro.mr.server.entity.LinkLibInfo
 */
public LinkLibInfo getLinklibinfo() {
	return linklibinfo;
}
/**
 * <h3>デフォルト画像コードの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @return java.lang.String
 */
public java.lang.String getPictureCD() {
	return pictureCD;
}
/**
 * <h3>コードprefixのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newCdPrefix java.lang.String
 */
public void setCdPrefix(java.lang.String newCdPrefix) {
	cdPrefix = newCdPrefix;
}
/**
 * <h3>会社コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newCompanyCD java.lang.String
 */
public void setCompanyCD(java.lang.String newCompanyCD) {
	companyCD = newCompanyCD;
}
/**
 * <h3>会社区分のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newCompanyKbn java.lang.String
 */
public void setCompanyKbn(java.lang.String newCompanyKbn) {
	companyKbn = newCompanyKbn;
}
/**
 * <h3>会社名のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newCompanyName java.lang.String
 */
public void setCompanyName(java.lang.String newCompanyName) {
	companyName = newCompanyName;
}
/**
 * <h3>デフォルトリンクコードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newLinkCD java.lang.String
 */
public void setLinkCD(java.lang.String newLinkCD) {
	linkCD = newLinkCD;
}
/**
 * <h3>リンクライブラリのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/03 午後 06:13:59)
 * @param newLinklibinfo jp.ne.sonet.medipro.mr.server.entity.LinkLibInfo
 */
public void setLinklibinfo(LinkLibInfo newLinklibinfo) {
	linklibinfo = newLinklibinfo;
}
/**
 * <h3>デフォルト画像コードのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:43:10)
 * @param newPictureCD java.lang.String
 */
public void setPictureCD(java.lang.String newPictureCD) {
	pictureCD = newPictureCD;
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
	me.append(cdPrefix + "\n");
	me.append(companyCD + "\n");
	me.append(companyKbn + "\n");
	me.append(companyName + "\n");
	me.append(linkCD + "\n");
	me.append(pictureCD + "\n");	
	return me.toString();
}
}
