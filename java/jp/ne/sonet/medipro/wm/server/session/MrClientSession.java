package jp.ne.sonet.medipro.wm.server.session;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.entity.SentakuTorokuInfo;

/**
 * <strong>MR�Ǘ��S���ڋq�ύX��ʗp�Z�b�V�����N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrClientSession {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** ���� */
    public static final int NORMAL = 0;
    /** �ۑ��m�F */
    public static final int SAVE_CONFIRM = 1;
    /** �ۑ����� */
    public static final int SAVE_DONE = 2;
    /** MRID�񑶍݃G���[ */
    public static final int NO_MR_EXIST_ERROR = 3;
    /** ����MRID�G���[ */
    public static final int SAME_MRID_ERROR = 4;
    /** �����G���[ */
    public static final int AUTHORITY_ERROR = 5;

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    /** ����MR��� */
    private Mr leftMr = null;
    /** �E��MR��� */
    private Mr rightMr = null;
    /** �����X�V�t���O */
    private boolean leftLoadFlag = false;
    /** �E���X�V�t���O */
    private boolean rightLoadFlag = false;
    /** ���݂�status */
    private int status = NORMAL;
    
    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    public MrClientSession() {
        leftMr = new Mr();
        rightMr = new Mr();

        if (SysCnst.DEBUG) {
            System.err.println("MrClientSession is created");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * ���݂�status���擾����B
     * @return status�R�[�h�̂ǂꂩ
     */
    public int getStatus() {
        return status;
    }

    /**
     * ���݂�status��ݒ肷��B
     * @param status status�R�[�h�̂ǂꂩ
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * ������MR�����擾����B
     * @return Mr�I�u�W�F�N�g
     */
    public Mr getLeftMr() {
        return leftMr;
    }

    /**
     * �E����MR�����擾����B
     * @return Mr�I�u�W�F�N�g
     */
    public Mr getRightMr() {
        return rightMr;
    }

    /**
     * ������MR����DR���X�g��ݒ肷��B
     * @param info   MR���
     * @param drList DR���X�g
     */
    public void setLeftMrInfo(MrInfo info, Vector drList) {
        leftMr.setMrId(info.getMrId());
        leftMr.setName(info.getName());
        leftMr.setShitenName(info.getShitenName());
        leftMr.setEigyosyoName(info.getEigyosyoName());
        leftMr.setAttributeName1(info.getMrAttributeName1());
        leftMr.setAttributeName2(info.getMrAttributeName2());
        leftMr.setDrList(drList);
        leftLoadFlag = false;
    }

    /**
     * �E����MR����DR���X�g��ݒ肷��B
     * @param info   MR���
     * @param drList DR���X�g
     */
    public void setRightMrInfo(MrInfo info, Vector drList) {
        rightMr.setMrId(info.getMrId());
        rightMr.setName(info.getName());
        rightMr.setShitenName(info.getShitenName());
        rightMr.setEigyosyoName(info.getEigyosyoName());
        rightMr.setAttributeName1(info.getMrAttributeName1());
        rightMr.setAttributeName2(info.getMrAttributeName2());
        rightMr.setDrList(drList);
        rightLoadFlag = false;
    }

    /**
     * ������MR-ID���擾����B
     * @return MR-ID
     */
    public String getLeftMrId() {
        return leftMr.getMrId();
    }

    /**
     * �E����MR-ID���擾����B
     * @return MR-ID
     */
    public String getRightMrId() {
        return rightMr.getMrId();
    }

    /**
     * ������MR-ID��ݒ肷��B
     * @param mrID �ݒ肷��MR-ID
     */
    public void setLeftMrId(String mrId) {
        if (mrId == null || mrId.equals("")) {
            leftMr = new Mr();
        } else {
            leftMr.setMrId(mrId);
            leftLoadFlag = true;
        }
    }

    /**
     * �E����MR-ID��ݒ肷��B
     * @param mrID �ݒ肷��MR-ID
     */
    public void setRightMrId(String mrId) {
        if (mrId == null || mrId.equals("")) {
            rightMr = new Mr();
        } else {
            rightMr.setMrId(mrId);
            rightLoadFlag = true;
        }
    }

    /**
     * �����̎w�肵��Key��DR���E�Ɉڂ��B
     * @param key �ړ�����DR��Key
     */
    public void moveToRight(String key) {
        if (getLeftMrId().equals("") || getRightMrId().equals("")) {
            return;
        }
        rightMr.addDr(leftMr.getDr(key));
        leftMr.remove(key);
    }

    /**
     * �E���̎w�肵��Key��DR�����Ɉڂ��B
     * @param key �ړ�����DR��Key
     */
    public void moveToLeft(String key) {
        if (getLeftMrId().equals("") || getRightMrId().equals("")) {
            return;
        }
        leftMr.addDr(rightMr.getDr(key));
        rightMr.remove(key);
    }

    /**
     * �����̏���DB����ǂݍ��ޕK�v�����邱�Ƃ������t���O�B
     * @return �K�v��������true
     */
    public boolean isNeedToLeftMrLoad() {
        return leftLoadFlag;
    }

    /**
     * �E���̏���DB����ǂݍ��ޕK�v�����邱�Ƃ������t���O�B
     * @return �K�v��������true
     */
    public boolean isNeedToRightMrLoad() {
        return rightLoadFlag;
    }

    /**
     * <strong>�Ȉ�MR���N���X.</strong>
     * MrInfo�̃��b�s���O��DR���X�g�̕ێ��@�\������.
     */
    public class Mr {
        /** MRID*/
        private String mrId = new String();
        /** ���� */
        private String name = new String();
        /** �x�X���� */
        private String shitenName = new String();
        /** �c�Ə����� */
        private String eigyosyoName = new String();
        /** MR����1 */
        private String attributeName1 = new String();
        /** MR����2 */
        private String attributeName2 = new String();
        /** ��t���� */
        private Hashtable drList = null;

        /**
         * ���DR���X�g�𐶐�����.
         */
        public Mr() {
            drList = new Hashtable();
        }

        /**
         * MR-ID���擾����.
         * @return MR-ID
         */
        public String getMrId() {
            return mrId;
        }

        /**
         * MR-ID��ݒ肷��.
         * @param val �ݒ肷��MR-ID
         */
        public void setMrId(String val) {
            mrId = val;
        }

        /**
         * MR�������擾����.
         * @return ����
         */
        public String getName() {
            return name;
        }

        /**
         * MR������ݒ肷��.
         * @param �ݒ肷��MR����
         */
        public void setName(String val) {
            if (val == null) {
                name = "";
            } else {
                name = val;
            }
        }
        
        /**
         * MR��������x�X�����擾����.
         * @return �x�X��
         */
        public String getShitenName() {
            return shitenName;
        }

        /**
         * MR��������x�X����ݒ肷��.
         * @param val �ݒ肷��x�X��
         */
        public void setShitenName(String val) {
            if (val == null) {
                shitenName = "";
            } else {
                shitenName = val;
            }
        }

        /**
         * MR��������c�Ə������擾����.
         * @return �c�Ə���
         */
        public String getEigyosyoName() {
            return eigyosyoName;
        }

        /**
         * MR��������c�Ə�����ݒ肷��.
         * @param val �ݒ肷��c�Ə���
         */
        public void setEigyosyoName(String val) {
            if (val == null) {
                eigyosyoName = "";
            } else {
                eigyosyoName = val;
            }
        }
        
        /**
         * MR�������鑮����1���擾����.
         * @return ������1
         */
        public String getAttributeName1() {
            return attributeName1;
        }

        /**
         * MR�������鑮����1��ݒ肷��.
         * @param val �ݒ肷�鑮����1
         */
        public void setAttributeName1(String val) {
            if (val == null) {
                attributeName1 = "";
            } else {
                attributeName1 = val;
            }
        }
        
        /**
         * MR�������鑮����2���擾����.
         * @return ������2
         */
        public String getAttributeName2() {
            return attributeName2;
        }

        /**
         * MR�������鑮����2��ݒ肷��.
         * @param val �ݒ肷�鑮����2
         */
        public void setAttributeName2(String val) {
            if (val == null) {
                attributeName2 = "";
            } else {
                attributeName2 = val;
            }
        }

        /**
         * MR���S������DR�ꗗ���擾����.
         * @return Dr�C���X�^���X�̈ꗗ
         */
        public Enumeration getDrList() {
            return drList.elements();
        }

        /**
         * ���ݕێ�����DR�ꗗ����V���ɒS���ɂȂ���DR-ID�ꗗ���擾����.
         * @return DR-ID���X�g
         */
        public Enumeration getMovedDrList() {
            Enumeration e = getDrList();
            Vector list = new Vector();

            while (e.hasMoreElements()) {
                Dr dr = (Dr)e.nextElement();
                if (!getMrId().equals(dr.getMrId())) {
                    list.addElement(dr);
                }
            }

            return list.elements();
        }

        /**
         * DR����l�ǉ����܂�.
         * @param dr �ǉ�����DR���
         */
        public void addDr(Dr dr) {
            drList.put(dr.getKey(), dr);
        }

        /**
         * DR�ꗗ��ݒ肵�܂�(������DR�͔j��).
         * @param list �ݒ肷��DR���̃��X�g
         */
        public void setDrList(Vector list) {
            drList.clear();
            
            if (list == null) {
                return;
            }

            Enumeration e = list.elements();
            while (e.hasMoreElements()) {
                SentakuTorokuInfo info = (SentakuTorokuInfo)e.nextElement();
                drList.put(info.getDrId() + info.getSeq(), new Dr(info));
            }
        }

        /**
         * �w�肵��ID��DR�����擾���܂�.
         * @param drId �擾����Dr��DR-ID
         * @return DR���
         */
        public Dr getDr(String key) {
            return (Dr)drList.get(key);
        }

        /**
         * �w�肵��ID��DR���ꗗ����폜���܂�.
         * @param drId �폜����Dr��DR-ID
         */
        public void remove(String key) {
            drList.remove(key);
        }

    }

    /**
     * <strong>SentakuTorokuInfo�̃��b�p�N���X.</strong>
     */
    public class Dr {
        /** DR���̕\�������� */
        static final int NAME_LENGTH = 14;
        /** �Ζ���̕\�������� */
        static final int KINMUSAKI_LENGTH = 20;
        /** ���[�J�[�{��ID�̕\�������� */
        static final int MAKER_ID_LENGTH = 10;
        /** �I��o�^��� */
        private SentakuTorokuInfo info = null;

        /**
         * �Ȉ�DR���𐶐����܂�.
         * @param info DR���
         */
        public Dr(SentakuTorokuInfo info) {
            this.info = info;
        }

        /**
         * ����DR����ӂɔ��ʂ��邽�߂�Key���擾���܂�.
         * @return Key�ƂȂ镶����
         */
        public String getKey() {
            return info.getDrId() + info.getSeq();
        }

        /**
         * DR-ID���擾���܂�.
         * @return MR-ID
         */
        public String getDrId() {
            return info.getDrId();
        }

        /**
         * ����DR��S������MR��ID���擾���܂�.
         * @return MR-ID
         */
        public String getMrId() {
            return info.getMrId();
        }

        /**
         * �V�[�P���X���擾���܂�.
         * @return �V�[�P���X
         */
        public int getSeq() {
            return info.getSeq();
        }

        /**
         * ��t�������擾���܂�.
         * �I��o�^�e�[�u���ɐݒ肳��Ă��Ȃ���΁A��t�e�[�u������擾���܂�.
         * @return ��t����
         */
        public String getName() {
            String name = info.getName() == null ? info.getDrName() : info.getName();

            return format(name, NAME_LENGTH);
        }

        /**
         * �Ζ�����擾���܂�.
         * @return �Ζ���
         */
        public String getKinmusaki() {
            return format(info.getKinmusaki(), KINMUSAKI_LENGTH);
        }

        /**
         * ���[�J�[�{��ID���擾���܂�.
         * @return ���[�J�[�{��ID
         */
        public String getMakerId() {
            return format(info.getMakerShisetsuId(), MAKER_ID_LENGTH);
        }

        /**
         * �w�肵�������Ƀt�H�[�}�b�g������������擾����.
         * @param str           �t�H�[�}�b�g�Ώ�
         * @param displayLength �t�H�[�}�b�g���钷��
         */
        private String format(String str, int displayLength) {
            int length = str == null ? 0 : str.getBytes().length;

            if (length < displayLength) {
                StringBuffer buff = new StringBuffer();
                if (str != null) {
                    buff.append(str);
                }

                for (int i = 0; i < displayLength - length; i += 2) {
                    buff.append("�@");
                }
                return buff.toString();
            }

            return new String(str.getBytes(), 0, displayLength);
        }

        /**
         * ���̃I�u�W�F�N�g�̕�����\�����擾���܂�.
         * @return ������\��
         */
        public String toString() {
            return getName() + " " + getKinmusaki() + " "+ getMakerId();
        }

    }

}
