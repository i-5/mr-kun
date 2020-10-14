package jp.ne.sonet.medipro.wm.server.session;

import java.io.File;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.CatchInfo;

/**
 * <strong>MR�L���b�`�摜�ǉ��E�X�V��ʗp�Z�b�V�����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateSession implements HttpSessionBindingListener {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    //
    /** �ʏ� */
    public static final int NORMAL = 0;
    /** �ۑ��m�F */
    public static final int SAVE_CONFIRM = 1;
    /** �ۑ����� */
    public static final int SAVE_DONE = 2;

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** ���݂�status */
    private int status = NORMAL;
    /** MR-ID */
    private String mrId = new String();
    /** �L���b�`�摜��� */
    private CatchInfo info = null;
    /** DB����ǂݍ��ޕK�v�������t���O */
    private boolean loadFlag = false;
    /** �ꎞ�t�@�C���� */
    private String tempPicture = null;
    /** �g���q */
    private String extension = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * MR�L���b�`�摜�Z�b�V�����I�u�W�F�N�g�𐶐�����.
     */
    public MrCatchUpdateSession() {
        info = new CatchInfo();

        if (SysCnst.DEBUG) {
            System.err.println("MrCatchUpdateSession is created");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * �ŐV�̏���DB����ǂݍ��ޕK�v�����邩�m�F����.
     * @return �ǂݍ��ޕK�v�������true
     */
    public boolean isNeedToLoad() {
        return loadFlag;
    }

    /**
     * �ŐV�̏���DB����ǂݍ��ޕK�v�����邩�ǂ�����ݒ肷��.
     * @param flag �ǂݍ��݂�v������ꍇ��true
     */
    public void setLoadFlag(boolean flag) {
        loadFlag = flag;
    }

    /**
     * ���݂̃X�e�[�^�X���擾����.
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

    /**
     * �ΏۂƂ���摜��������MR-ID���擾����.
     * @return MR-ID
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * �ΏۂƂ���摜��������MR-ID��ݒ肷��.
     * @param mrId �ݒ肷��MR-ID
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /**
     * �摜�����擾����.
     * @return �摜����
     */
    public String getPictureName() {
        return info.getPictureName() == null ? "" : info.getPictureName();
    }

    /**
     * �摜����ݒ肷��.
     * @param name �ݒ肷��摜��
     */
    public void setPictureName(String name) {
        info.setPictureName(name);
    }

    /**
     * �摜�R�[�h���擾����.
     * @return �摜�R�[�h
     */
    public String getPictureCd() {
        return info.getPictureCD() == null ? "" : info.getPictureCD();
    }

    /**
     * �摜�R�[�h��ݒ肷��.
     * @param cd �ݒ肷��摜�R�[�h
     */
    public void setPictureCd(String cd) {
        info.setPictureCD(cd);
    }

    /**
     * �摜�t�@�C�������擾����.
     * �������A�ꎞ�摜���ݒ肳��Ă���ꍇ�͂������Ԃ�.
     * @return �摜�t�@�C����
     */
    public String getPicture() {
        if (tempPicture == null || tempPicture.equals("")) {
            return info.getPicture() == null ? "" : info.getPicture();
        }
        
        return tempPicture;
    }

    /**
     * �摜�t�@�C������ݒ肷��.
     * @param path �ݒ肷��摜�t�@�C����
     */
    public void setPicture(String path) {
        info.setPicture(path);
    }

    /**
     * �ꎞ�摜�t�@�C�������擾����.
     * @return �ꎞ�摜�t�@�C����
     */
    public String getTempPicture() {
        return tempPicture;
    }

    /**
     * �ꎞ�摜�t�@�C������ݒ肷��.
     * @param path �ݒ肷��ꎞ�摜�t�@�C����
     */
    public void setTempPicture(String path) {
        tempPicture = path;
    }

    /**
     * �ꎞ�摜�t�@�C���̊g���q��ݒ肷��.
     * @param ext �g���q(gif, jpg��)
     */
    public void setExtension(String ext) {
        extension = ext;
    }

    /**
     * �ꎞ�摜�t�@�C���̊g���q���擾����.
     * @return �g���q
     */
    public String getExtension() {
        return extension;
    }

    /**
     * ���ݕێ�����摜�����擾����.
     * @return �摜���
     */
    public CatchInfo getCatchInfo() {
        return info;
    }

    /**
     * �摜����ݒ肷��.
     * @param info �ݒ肷��摜���
     */
    public void setCatchInfo(CatchInfo info) {
        this.info = info;
    }

    public boolean isChecked() {
	return info.getDefaultFlg();
    }

    public void setCheck(boolean flag) {
	info.setDefaultFlg(flag);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // �ȉ� HttpSessionBindingListener��concreate
    //
    /**
     * ���̃Z�b�V�����I�u�W�F�N�g��bound�����Ƃ��Ă΂��.
     * @param event �n�����C�x���g
     */
    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * ���̃Z�b�V������unbound�����Ƃ��Ă΂��.
     * �ꎞ�摜�t�@�C�����c���Ă���΍폜����.
     * @param event �n�����C�x���g
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (SysCnst.DEBUG) {
            System.err.println("MrCatchUpdateSession is unbound");
        }

        if (tempPicture == null || tempPicture.trim().equals("")) {
            return;
        }

        try {
            new File(tempPicture).delete();
        } catch (Exception ex) {
        }
    }
}
