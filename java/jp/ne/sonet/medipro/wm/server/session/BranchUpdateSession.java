package jp.ne.sonet.medipro.wm.server.session;

import java.util.*;
import jp.ne.sonet.medipro.wm.common.*;

/**
 * <strong>x“XE‰c‹ÆŠ’Ç‰ÁEXVƒZƒbƒVƒ‡ƒ“ƒNƒ‰ƒX.</strong>
 * @auther
 * @version
 */
public class BranchUpdateSession {
    /////////////////////////////////////////////
    //class variables
    //
    private String	branchCD;		// x“XƒR[ƒh
    private String	officeCD;		// ‰c‹ÆŠƒR[ƒh
    private boolean	newBranch;		// x“XV‹K“o˜^ƒtƒ‰ƒO
    private boolean newOffice;		// ‰c‹ÆŠV‹K“o˜^ƒtƒ‰ƒO
    private String	branchName;		// x“X–¼(Œ»İ“ü—Í‚³‚ê‚Ä‚¢‚é)
    private String	officeName;		// ‰c‹ÆŠ–¼(Œ»İ“ü—Í‚³‚ê‚Ä‚¢‚é)
    private int		messageState;	// ƒƒbƒZ[ƒWID
    private String	originalBranch;	// x“X–¼(•ÏX‘O)
    private String	originalOffice;	// ‰c‹ÆŠ–¼(•ÏX‘O)

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * ƒRƒ“ƒXƒgƒ‰ƒNƒ^.
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
     * x“XƒR[ƒh‚ğİ’è‚·‚é.
     * @param newCD String
     */
    public void setBranchCD(String newCD) {
	this.branchCD = newCD;
    }
    /**
     * x“XƒR[ƒh‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getBranchCD() {
	return this.branchCD;
    }
    /**
     * ‰c‹ÆŠƒR[ƒh‚ğİ’è‚·‚é.
     * @param newCD String
     */
    public void setOfficeCD(String newCD) {
	this.officeCD = newCD;
    }
    /**
     * ‰c‹ÆŠƒR[ƒh‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getOfficeCD() {
	return this.officeCD;
    }
    /**
     * x“XV‹K“o˜^ƒtƒ‰ƒO‚ğİ’è‚·‚é.
     * @param flg boolean
     */
    public void setNewBranch(boolean flg) {
	this.newBranch = flg;
    }
    /**
     * x“XV‹K“o˜^ƒtƒ‰ƒO‚ğæ“¾‚·‚é.
     * @return boolean
     */
    public boolean isNewBranch() {
	return this.newBranch;
    }
    /**
     * ‰c‹ÆŠV‹K“o˜^ƒtƒ‰ƒO‚ğİ’è‚·‚é.
     * @param flg boolean
     */
    public void setNewOffice(boolean flg) {
	this.newOffice = flg;
    }
    /**
     * ‰c‹ÆŠV‹K“o˜^ƒtƒ‰ƒO‚ğæ“¾‚·‚é.
     * @return boolean
     */
    public boolean isNewOffice() {
	return this.newOffice;
    }
    /**
     * x“X–¼‚ğİ’è‚·‚é.
     * @param newName String
     */
    public void setBranchName(String newName) {
	this.branchName = newName;
    }
    /**
     * x“X–¼‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getBranchName() {
	return this.branchName;
    }
    /**
     * ‰c‹ÆŠ–¼‚ğİ’è‚·‚é.
     * @param newName String
     */
    public void setOfficeName(String newName) {
	this.officeName = newName;
    }
    /**
     * ‰c‹ÆŠ–¼‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getOfficeName() {
	return this.officeName;
    }
    /**
     * ƒƒbƒZ[ƒWID‚ğİ’è‚·‚é.
     * @param newMessageState int
     */
    public void setMessageState(int newMessageState) {
	this.messageState = newMessageState;
    }
    /**
     * ƒƒbƒZ[ƒWID‚ğæ“¾‚·‚é.
     * @return int
     */
    public int getMessageState() {
	return this.messageState;
    }
    /**
     * •ÏX‘O‚Ì(DBã‚Ì)x“X–¼‚ğİ’è‚·‚é.
     * @param newName String
     */
    public void setOriginalBranch(String newName) {
	this.originalBranch = newName;
    }
    /**
     * •ÏX‘O‚Ì(DBã‚Ì)x“X–¼‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getOriginalBranch() {
	return this.originalBranch;
    }
    /**
     * •ÏX‘O‚Ì(DBã‚Ì)‰c‹ÆŠ–¼‚ğİ’è‚·‚é.
     * @param newName String
     */
    public void setOriginalOffice(String newName) {
	this.originalOffice = newName;
    }
    /**
     * •ÏX‘O‚Ì(DBã‚Ì)‰c‹ÆŠ–¼‚ğæ“¾‚·‚é.
     * @return String
     */
    public String getOriginalOffice() {
	return this.originalOffice;
    }
}
