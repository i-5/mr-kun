package jp.ne.sonet.medipro.wm.server.session;

import java.util.Hashtable;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MR�L���b�`�摜�ꗗ��ʗp�Z�b�V�����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchListSession {
    //////////////////////////////////////////////////////////////////////
    // constants
    //
    /** �ʏ� */
    public static final int NORMAL = 0;
    /** �폜�Ώ۔񑶍� */
    public static final int NO_SELECTION = 1;
    /** �폜�m�F */
    public static final int REMOVE_CONFIRM = 2;
    /** �폜���� */
    public static final int REMOVE_DONE = 3;
    /** �폜�s�� */
    public static final int UNABLE_TO_REMOVE = 4;
    /** �ۑ��m�F */
    public static final int SAVE_CONFIRM = 5;
    /** �ۑ�����*/
    public static final int SAVE_DONE = 6;
    /** ���� */
    private static final String ASCENDING = "ASC";
    /** �~�� */
    private static final String DESCENDING = "DESC";
    /** �f�t�H���g�\�[�g���� */
    private static final String DEFAULT_SORT_ITEM = "catch_picture.picture_name";
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
    /** ���݂̕\���y�[�W */
    private int page = 0;
    /** �`�F�b�N�{�b�N�X�̑I����� */
    private Hashtable checkedMap = null;
    /** DB�̃f�t�H���gPictureCd */
    private String dbDefaultPictureCd = new String();
    /** MR��� */
    private MrInfo info = null;
    /** ���݂�status */
    private int status = NORMAL;

    static {
	toTableColumnMap = new Hashtable();
	toTableColumnMap.put("title", "catch_picture.picture_name");
    }

    //////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MR�L���b�`�摜�ꗗ�̃Z�b�V�����𐶐�����.
     */
    public MrCatchListSession() {
	toDirectionMap = new Hashtable();
	toDirectionMap.put("catch_picture.picture_name", ASCENDING);

	checkedMap = new Hashtable();
	info = new MrInfo();

	if (SysCnst.DEBUG) {
	    System.err.println("MrCatchListSession is created");
	}
    }

    //////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * �y�[�W�ȊO�̏����N���A����B
     */
    public void clear() {
	setCheckedList(null);
	setStatus(NORMAL);
    }

    /**
     * ���ݕێ�����MR�����擾����.
     * @return MR���
     */
    public MrInfo getMrInfo() {
	return info;
    }

    /**
     * MR����ݒ肷��.
     * @param info �ݒ肷��MR���
     */
    public void setMrInfo(MrInfo info) {
	this.info = info;
	dbDefaultPictureCd = info.getPictureCd();
    }

    /**
     * �f�t�H���g�摜�R�[�h���擾����.
     * @return ���ݐݒ肳��Ă���f�t�H���g�摜�R�[�h
     */
    public String getDBDefaultPictureCd() {
	return dbDefaultPictureCd;
    }
    
    /**
     * �f�t�H���g�摜�R�[�h��ݒ肷��.
     * @param pictureCd �ݒ肷��f�t�H���g�摜�R�[�h
     */
    public void setDBDefaultPictureCd(String pictureCd) {
	dbDefaultPictureCd = pictureCd;
    }

    /**
     * ���݂̃\�[�g���ږ����擾����.
     * @return �\�[�g�ΏۃJ������
     */
    public String getCurrentSortItem() {
	return currentSortItem;
    }

    /**
     * ���݂̃\�[�g���ڂ̃\�[�g�������擾����.
     * @return �\�[�g�Ώۂ̃\�[�g����(ASCENDING or DESCENDING)
     */
    public String getCurrentSortDirection() {
	return (String)toDirectionMap.get(currentSortItem);
    }

    /**
     * ���݂̃y�[�W�ԍ����擾����.
     * @return ���ݕ\�����Ă���y�[�W(0�`)
     */
    public int getPage() {
	return page;
    }

    /**
     * ��y�[�W�i�߂�.
     */
    public void nextPage() {
	page++;
    }

    /**
     * ��y�[�W�߂�.
     */
    public void previousPage() {
	page--;

	page = page < 0 ? 0 : page;
    }

    /**
     * �w�肵���J���������\�[�g�Ώۂɐݒ肵�A�\�[�g������O��̕������甽�]������.
     * @param columnName �\�[�g�ΏۂƂ���J������
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
     * �w�肵���J�����̃\�[�g�����𔽓]������.
     * @param sortItem ���e�[�u���J������
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
     * ���݂̃`�F�b�N�{�b�N�X�̑I����Ԃ��擾����.
     * @param key ���ڂ���ӂɎ��ʂł���key
     */
    public boolean isChecked(String key) {
	if (checkedMap.get(key) != null) {
	    return true;
	}

	return false;
    }

    /**
     * �`�F�b�N�{�b�N�X�̑I����Ԃ�ݒ肷��.
     * @param checkedItems �`�F�b�N���鍀�ڂ�key
     */
    public void setCheckedList(String[] checkedItems) {
	checkedMap.clear();

	if (checkedItems != null) {
	    for (int i = 0; i < checkedItems.length; i++) {
		checkedMap.put(checkedItems[i], "checked");
	    }
	}
    }

    /**
     * ���ݐݒ肳��Ă���X�e�[�^�X���擾����.
     * @return �X�e�[�^�X
     */
    public int getStatus() {
	return status;
    }

    /**
     * �X�e�[�^�X��ݒ肷��.
     * @param status �ݒ肷��X�e�[�^�X
     */
    public void setStatus(int status) {
	this.status = status;
    }


}
