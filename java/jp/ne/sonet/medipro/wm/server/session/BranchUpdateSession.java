package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>支店・営業所追加・更新セッションクラス.</strong>
 * @auther
 * @version
 */
public class BranchUpdateSession {
    /////////////////////////////////////////////
    //class variables
    //
    private String	branchCD;		// 支店コード
    private String	officeCD;		// 営業所コード
    private boolean	newBranch;		// 支店新規登録フラグ
    private boolean newOffice;		// 営業所新規登録フラグ
    private String	branchName;		// 支店名(現在入力されている)
    private String	officeName;		// 営業所名(現在入力されている)
    private int		messageState;	// メッセージID
    private String	originalBranch;	// 支店名(変更前)
    private String	originalOffice;	// 営業所名(変更前)

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * コンストラクタ.
	 */
    public BranchUpdateSession() {
	if (SysCnst.DEBUG) {
	    System.out.println("BranchUpdateSession Created!!");
	}
	this.branchCD = null;
	this.officeCD = null;
	this.newBranch = true;
	this.newOffice = true;
	this.branchName = null;
	this.officeName = null;
	this.messageState = 0;
	this.originalBranch = null;
	this.originalOffice = null;
    }
	
    /////////////////////////////////////////////
    //class methods
    //
    /**
     * 支店コードを設定する.
     * @param newCD String
     */
    public void setBranchCD(String newCD) {
	this.branchCD = newCD;
    }
    /**
     * 支店コードを取得する.
     * @return String
     */
    public String getBranchCD() {
	return this.branchCD;
    }
    /**
     * 営業所コードを設定する.
     * @param newCD String
     */
    public void setOfficeCD(String newCD) {
	this.officeCD = newCD;
    }
    /**
     * 営業所コードを取得する.
     * @return String
     */
    public String getOfficeCD() {
	return this.officeCD;
    }
    /**
     * 支店新規登録フラグを設定する.
     * @param flg boolean
     */
    public void setNewBranch(boolean flg) {
	this.newBranch = flg;
    }
    /**
     * 支店新規登録フラグを取得する.
     * @return boolean
     */
    public boolean isNewBranch() {
	return this.newBranch;
    }
    /**
     * 営業所新規登録フラグを設定する.
     * @param flg boolean
     */
    public void setNewOffice(boolean flg) {
	this.newOffice = flg;
    }
    /**
     * 営業所新規登録フラグを取得する.
     * @return boolean
     */
    public boolean isNewOffice() {
	return this.newOffice;
    }
    /**
     * 支店名を設定する.
     * @param newName String
     */
    public void setBranchName(String newName) {
	this.branchName = newName;
    }
    /**
     * 支店名を取得する.
     * @return String
     */
    public String getBranchName() {
	return this.branchName;
    }
    /**
     * 営業所名を設定する.
     * @param newName String
     */
    public void setOfficeName(String newName) {
	this.officeName = newName;
    }
    /**
     * 営業所名を取得する.
     * @return String
     */
    public String getOfficeName() {
	return this.officeName;
    }
    /**
     * メッセージIDを設定する.
     * @param newMessageState int
     */
    public void setMessageState(int newMessageState) {
	this.messageState = newMessageState;
    }
    /**
     * メッセージIDを取得する.
     * @return int
     */
    public int getMessageState() {
	return this.messageState;
    }
    /**
     * 変更前の(DB上の)支店名を設定する.
     * @param newName String
     */
    public void setOriginalBranch(String newName) {
	this.originalBranch = newName;
    }
    /**
     * 変更前の(DB上の)支店名を取得する.
     * @return String
     */
    public String getOriginalBranch() {
	return this.originalBranch;
    }
    /**
     * 変更前の(DB上の)営業所名を設定する.
     * @param newName String
     */
    public void setOriginalOffice(String newName) {
	this.originalOffice = newName;
    }
    /**
     * 変更前の(DB上の)営業所名を取得する.
     * @return String
     */
    public String getOriginalOffice() {
	return this.originalOffice;
    }
}
