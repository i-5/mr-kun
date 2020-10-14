package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;
import jp.ne.sonet.medipro.wm.common.SysCnst;

/**
 * <strong>MR�Ǘ��ꗗ�p�Z�b�V�����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrListSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** �ʏ� */
    public static final int NORMAL = 0;
    /** �폜�Ώ۔񑶍� */
    public static final int NO_SELECTION = 1;
    /** �폜�m�F */
    public static final int REMOVE_CONFIRM = 2;
    /** �S���ڋq��ʈړ��O�m�F */
    public static final int REPLACE_CONFIRM = 3;
    /** �폜���� */
    public static final int REMOVE_DONE = 4;
    /** �S���ڋq���� */
    public static final int HAS_DR_IN_CHARGE = 5;
    /** ���� */
    private static final String ASCENDING = "ASC";
    /** �~�� */
    private static final String DESCENDING = "DESC";
    /** �f�t�H���g�\�[�g���� */
    private static final String DEFAULT_SORT_ITEM = "mr.mr_id";

    //////////////////////////////////////////////////////////////////////
    // class valiables
    //
    /** �\�[�g���ڂƃe�[�u���J�������̃}�b�v */
    private static Hashtable toTableColumnMap = null;

    //////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** ���݂̃\�[�g����(�e�[�u���J������) */
    private String currentSortItem = DEFAULT_SORT_ITEM;
    /** �e�J�����̃\�[�g���� */
    private Hashtable toDirectionMap = null;
    /** �`�F�b�N�{�b�N�X�̑I����� */
    private Hashtable checkedMap = null;
    /** ���݂̕\���y�[�W */
    private int page = 0;
    /** ���݂�status */
    private int status = NORMAL;

    private String refMrId = null;
    private String refMrName = null;

    static {
	//��ʃ\�[�gkey �� �e�[�u���J������
	toTableColumnMap = new Hashtable();
	toTableColumnMap.put("id", "mr.mr_id");
	toTableColumnMap.put("name", "mr.name_kana");
	toTableColumnMap.put("shiten", "mr.shiten_cd");
	toTableColumnMap.put("eigyosyo", "mr.eigyosyo_cd");
	toTableColumnMap.put("attribute1", "mr.mr_attribute_cd1");
	toTableColumnMap.put("attribute2", "mr.mr_attribute_cd2");
	toTableColumnMap.put("master", "mr.master_flg");
	toTableColumnMap.put("nyusya", "mr.nyusya_year");
    }

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * �e�[�u���J�������ɑ΂��鏉���\�[�g�����̃Z�b�g���s��.
     */
    public MrListSession() {
	toDirectionMap = new Hashtable();
	toDirectionMap.put("mr.mr_id", ASCENDING);
	toDirectionMap.put("mr.name_kana", ASCENDING);
	toDirectionMap.put("mr.shiten_cd", ASCENDING);
	toDirectionMap.put("mr.eigyosyo_cd", ASCENDING);
	toDirectionMap.put("mr.mr_attribute_cd1", ASCENDING);
	toDirectionMap.put("mr.mr_attribute_cd2", ASCENDING);
	toDirectionMap.put("mr.master_flg", ASCENDING);
	toDirectionMap.put("mr.nyusya_year", ASCENDING);

	checkedMap = new Hashtable();
	
	if (SysCnst.DEBUG) {
	    System.err.println("MrListSession is created");
	}
    }

    //////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * ���݂̃\�[�g���ږ����擾����.
     * @return ���݂̃\�[�g�Ώۃe�[�u���J������
     */
    public String getCurrentSortItem() {
	return currentSortItem;
    }

    /**
     * ���݂̃\�[�g���ڂ̃\�[�g�������擾����.
     * @return ���݂̃\�[�g����(ASC or DESC)
     */
    public String getCurrentSortDirection() {
	return (String)toDirectionMap.get(currentSortItem);
    }

    /**
     * �y�[�W�ȊO�̏����N���A����B
     */
    public void clear() {
	setCheckedList(null);
	setStatus(NORMAL);
    }

    /**
     * ���݂̕\���y�[�W���擾����.
     * @return �y�[�W�i���o�[
     */
    public int getPage() {
	return page;
    }

    /**
     * �y�[�W��i�߂�.
     */
    public void nextPage() {
	page++;
    }

    /**
     * �y�[�W��߂�.
     */
    public void previousPage() {
	page--;
	
	page = page < 0 ? 0 : page;
    }

    /**
     * ���݂̃\�[�g���ڂ̐ݒ�A���\�[�g�����̔��]���s��.
     * @param columnName �ݒ肷��\�[�g���ږ�
     */
    public void setSortTarget(String columnName) {
	//�Ή�����e�[�u���J���������擾
	currentSortItem = (String)toTableColumnMap.get(columnName);
	
	//�s����������f�t�H���g��
	if (currentSortItem == null) {
	    currentSortItem = DEFAULT_SORT_ITEM;
	}

	//�\�[�g�����𔽓]
	reverseDirection(currentSortItem);

	//�y�[�W�����Z�b�g
	page = 0;
    }

    /**
     * �w�肵���J�����̃\�[�g�����𔽓]����.
     * @param sortItem �\�[�g�Ώۃe�[�u���J������
     */
    private void reverseDirection(String sortItem) {
	String direction = (String)toDirectionMap.get(sortItem);
	
	if (direction.equals(DESCENDING)){
	    toDirectionMap.put(sortItem, ASCENDING);
	} else {
	    toDirectionMap.put(sortItem, DESCENDING);
	}
    }

    /**
     * �w�肵��MR�̃`�F�b�N�{�b�N�X�̏�Ԃ𒲂ׂ�.
     * @param  mrId ���ׂ�MR��ID
     * @return �`�F�b�N����Ă�����true
     */
    public boolean isChecked(String mrId) {
	if (checkedMap.get(mrId) != null) {
	    return true;
	}

	return false;
    }

    /**
     * MRID�̔z����󂯎��A�`�F�b�N��Ԃɂ���.
     * @param checkedMrIds �`�F�b�N����MRID�̃��X�g
     */
    public void setCheckedList(String[] checkedMrIds) {
	checkedMap.clear();

	if (checkedMrIds != null) {
	    for (int i = 0; i < checkedMrIds.length; i++) {
		checkedMap.put(checkedMrIds[i], "checked");
	    }
	}
    }

    /**
     * ���݂�status���擾����.
     * @return status
     */
    public int getStatus() {
	return status;
    }

    /**
     * ���݂�status��ݒ肷��.
     * @param status �ݒ�status
     */
    public void setStatus(int status) {
	this.status = status;
    }

    public String getRefMrId() {
	return refMrId;
    }

    public void setRefMrId(String value) {
	refMrId = value;
    }

    public String getRefMrName() {
	return refMrName;
    }

    public void setRefMrName(String value) {
	refMrName = value;
    }
    
}
