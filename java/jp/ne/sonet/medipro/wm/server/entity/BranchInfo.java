package jp.ne.sonet.medipro.wm.server.entity;

import java.lang.String;

/**
 * <strong>�x�X�E�c�Ə����Entity�N���X.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class BranchInfo {
	/////////////////////////////////////////////
	//class variables
	//
	protected String shitenCD;		// �x�X�R�[�h
	protected String shitenName;	// �x�X��
	protected String eigyosyoCD;	// �c�Ə��R�[�h
	protected String eigyosyoName;	// �c�Ə���
	protected int	 maxRow;		// �ő�s��
	protected boolean mrFlg;		// �l�q�����t���O
	protected boolean shitenFlg;	// �x�X�t���O

	/////////////////////////////////////////////
	//constructors
	//
	/**
	 * �R���X�g���N�^.
	 */
	public BranchInfo() {
		shitenCD = null;
		shitenName = null;
		eigyosyoCD = null;
		eigyosyoName = null;
		maxRow = 0;
		mrFlg = false;
		shitenFlg = false;
	}
	
	/////////////////////////////////////////////
	//class methods
	//

	/**
	 * �x�X�R�[�h�̎擾.
	 * @return String
	 */
	public String getShitenCD() {
		return shitenCD;
	}
	/**
	 * �x�X���̎擾.
	 * @return String
	 */
	public String getShitenName() {
		return shitenName;
	}
	/**
	 * �c�Ə��R�[�h�̎擾.
	 * @return String
	 */
	public String getEigyosyoCD() {
		return eigyosyoCD;
	}
	/**
	 * �c�Ə����̎擾.
	 * @return String
	 */
	public String getEigyosyoName() {
		return eigyosyoName;
	}
	/**
	 * �ő�s���̎擾.
	 * @return int
	 */
	public int getMaxRow() {
		return maxRow;
	}
	/**
	 * �l�q�����t���O�̎擾.
	 * @return boolean
	 */
	public boolean isMr() {
		return mrFlg;
	}
	/**
	 * �x�X�t���O�̎擾.
	 * @return boolean
	 */
	public boolean isShiten() {
		return shitenFlg;
	}
	/**
	 * �x�X�R�[�h�̃Z�b�g.
	 * @param newShitenCD String
	 */
	public void setShitenCD(String newShitenCD) {
		shitenCD = newShitenCD;
	}
	/**
	 * �x�X���̃Z�b�g.
	 * @param newShitenName String
	 */
	public void setShitenName(String newShitenName) {
		shitenName = newShitenName;
	}
	/**
	 * �c�Ə��R�[�h�̃Z�b�g.
	 * @param newDrRecvMsgNoCount String
	 */
	public void setEigyosyoCD(String newEigyosyoCD) {
		eigyosyoCD = newEigyosyoCD;
	}
	/**
	 * �c�Ə����̃Z�b�g.
	 * @param newEigyosyoName String
	 */
	public void setEigyosyoName(String newEigyosyoName) {
		eigyosyoName = newEigyosyoName;
	}
	/**
	 * �ő�s���̃Z�b�g.
	 * @param newMaxRow int
	 */
	public void setMaxRow(int newMaxRow) {
		maxRow = newMaxRow;
	}
	/**
	 * �l�q�����t���O�̃Z�b�g.
	 * @param newMrFlg boolean
	 */
	public void setMrFlg(boolean newMrFlg) {
		mrFlg = newMrFlg;
	}
	/**
	 * �x�X�t���O�̃Z�b�g.
	 * @param newShitenFlg boolean
	 */
	public void setShitenFlg(boolean newShitenFlg) {
		shitenFlg = newShitenFlg;
	}

	/**
	 * �����񉻂���.
	 * @return String
	 */
	public String toString() {
		StringBuffer me = new StringBuffer();
		me.append(shitenCD + "\n");
		me.append(shitenName + "\n");
		me.append(eigyosyoCD + "\n");
		me.append(eigyosyoName + "\n");
			
		return me.toString();
	}
}
