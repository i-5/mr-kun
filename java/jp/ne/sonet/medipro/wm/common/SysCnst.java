package jp.ne.sonet.medipro.wm.common;

/**
 * Medipro ���ʃR���X�^���X
 * <br>
 * �T�[�o�A�N���C�A���g�Ԃŋ��ʂɎg�p����R���X�^���X.
 * 
 * @author
 * @version
 */
public class SysCnst {

    ////////////////////////////////////////////////////////////////
    // DB�ڑ����
    /** �h���C�o�N���X�� */
    public static final String DB_DRIVER = "weblogic.jdbc.pool.Driver";
    /** URL */
    public static final String DB_URL = "jdbc:weblogic:pool:oraclePool";
    /** URL(�e�X�g�p) */
    public static final String DB_TEST_URL = "jdbc:oracle:thin:@ultra5:1521:TICD";
    /** �h���C�o�N���X��(�e�X�g�p) */
    public static final String DB_TEST_DRIVER = "oracle.jdbc.driver.OracleDriver";
    /** ���[�UID(�e�X�g�p) */
    public static final String DB_TEST_USER = "MWMDBOWN";
    /** �p�X���[�h(�e�X�g�p) */
    public static final String DB_TEST_PASS = "MWMDBOWN";

    ////////////////////////////////////////////////////////////////
    // �G���g���|�C���g
    /** Servlet�G���g���|�C���g */
    public static final String SERVLET_ENTRY_POINT;
    /** HTML�G���g���|�C���g */
    public static final String HTML_ENTRY_POINT = "/medipro/";

    ////////////////////////////////////////////////////////////////
    // �Z�b�V�������擾�L�[
    /** ���p�Z�b�V�������擾�L�[ */
    public static final String KEY_COMMON_SESSION = "wm_common";
    /** MR�ꗗ�Z�b�V����Key */
    public static final String KEY_MRLIST_SESSION = "WM_MR_LIST";
    /** MR�ύX�E�ǉ��Z�b�V����Key */
    public static final String KEY_MRUPDATE_SESSION = "WM_MR_UPDATE";
    /** MR�S���ڋq�ύX�Z�b�V����KEY */
    public static final String KEY_MRCLIENT_SESSION = "WM_MR_CLIENT";
    /** MR�L���b�`�摜�ꗗ�Z�b�V����KEY*/
    public static final String KEY_MRCATCHLIST_SESSION = "WM_MR_CATCH_LIST";
    /** MR�L���b�`�摜�ǉ��E�ύX�Z�b�V����KEY*/
    public static final String KEY_MRCATCHUPDATE_SESSION = "WM_MR_CATCH_UPDATE";
    /** �T�u�}�X�^�[�ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_SUBLIST_SESSION = "WM_SUB_LIST";
    /** �T�u�}�X�^�[�ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_SUBUPDATE_SESSION = "WM_SUB_UPDATE";
    /** ��ЃL���b�`�摜�ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_CATCH_SESSION = "WM_CATCH_LIST";
    /** ��ЃL���b�`�摜�ǉ��E�X�V��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_CATCHUPDATE_SESSION = "WM_CATCH_UPDATE";
    /** �����N�ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_LINKLIST_SESSION = "WM_LINK_LIST";
    /** �����N�ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_LINKUPDATE_SESSION = "WM_LINK_UPDATE";
    /** �����N���ޒǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_LINKCLASSUPDATE_SESSION = "WM_LINK_CLASS_UPDATE";
    /** ��^���ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_EXPRESSIONLIST_SESSION = "WM_EXPRESSION_LIST";
    /** ��^���ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_EXPRESSIONUPDATE_SESSION = "WM_EXPRESSION_UPDATE";
    /** hb010814 added this to handle template categories */
    public static final String KEY_TEIKEICLASSUPDATE_SESSION = "WM_TEIKEI_CLASS_UPDATE";

    /** �R�[�����e�ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_CALLLIST_SESSION = "WM_CALL_LIST";
    /** �R�[�����e�ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_CALLUPDATE_SESSION = "WM_CALL_UPDATE";
    /** �x�X�E�c�Ə��ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_BRANCHLIST_SESSION = "WM_BRANCH_LIST";
    /** �x�X�E�c�Ə��ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_BRANCHUPDATE_SESSION = "WM_BRANCH_UPDATE";
    /** �����ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_ATTRIBUTELIST_SESSION = "WM_ATTRIBUTE_LIST";
    /** �����ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_ATTRIBUTEUPDATE_SESSION = "WM_ATTRIBUTE_UPDATE";
    /** �d�v�x�ꗗ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_ACTION_SESSION = "WM_ACTION";
    /** �d�v�x�ǉ��E�ύX��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_ACTIONUPDATE_SESSION = "WM_ACTION_UPDATE";
    /** �����L���O�ݒ��ʗp�Z�b�V�������擾�L�[ */
    public static final String KEY_ACTIONRANKING_SESSION = "WM_ACTION_RANKING";

    ////////////////////////////////////////////////////////////////
    // �e��ʋ��ʏ��
    /** �}�X�^�[�t���O(�E�F�u) */
    public static final String FLG_MASTER_WEB = "1";
    /** �}�X�^�[�t���O(�T�u) */
    public static final String FLG_MASTER_SUB = "2";
    /** �}�X�^�[����(�x�X) */
    public static final String FLG_AUTHORITY_BRANCH = "1";
    /** �}�X�^�[����(�c�Ə�) */
    public static final String FLG_AUTHORITY_OFFICE = "2";
    /** �}�X�^�[����(�����P) */
    public static final String FLG_AUTHORITY_ATTRIBUTE1 = "1";
    /** �}�X�^�[����(�����Q) */
    public static final String FLG_AUTHORITY_ATTRIBUTE2 = "2";
    
    ////////////////////////////////////////////////////////////////
    // �T�u�}�X�^�[�ꗗ��ʏ��
    /** �T�u�}�X�^�[�ꗗ�f�t�H���g�\�[�g�L�[ */
    public static final String SORTKEY_SUBMASTER_LIST = "mr.mr_id";
    /** �T�u�}�X�^�[�ꗗ�f�t�H���g�I�[�_�[ */
    public static final String ORDER_SUBMASTER_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // �����N�ꗗ��ʏ��
    /** �����N�ꗗ�f�t�H���g�\�[�g�L�[ */
    public static final String SORTKEY_LINK_LIST = "link_lib.link_bunrui_cd";
    /** �����N�ꗗ�f�t�H���g�I�[�_�[ */
    public static final String ORDER_LINK_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // �����ꗗ��ʏ��
    /** �����ꗗ�f�t�H���g�\�[�g�L�[ */
    public static final String SORTKEY_ATTRIBUTE_LIST =        "mr_attribute.mr_attribute_cd";
    /** �����ꗗ�f�t�H���g�I�[�_�[ */
    public static final String ORDER_ATTRIBUTE_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // ��^���ꗗ��ʏ��
    /** ��^���ꗗ�f�t�H���g�\�[�g�L�[ */
    public static final String SORTKEY_EXPRESSION_LIST = "teikeibun_lib.title";
    /** ��^���ꗗ�f�t�H���g�I�[�_�[ */
    public static final String ORDER_EXPRESSION_LIST = "ASC";

    ////////////////////////////////////////////////////////////////
    // �x�X�E�c�Ə����
    /** �x�X�E�c�Ə����b�Z�[�W(���b�Z�[�W�Ȃ�) */
    public static final int BRANCH_LIST_MSG_NONE = 0;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG1) */
    public static final int BRANCH_LIST_MSG_PLURAL = 1;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG2) */
    public static final int BRANCH_LIST_MSG_NOCHECK = 2;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG3) */
    public static final int BRANCH_LIST_MSG_CONFIRM = 3;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG4) */
    public static final int BRANCH_LIST_MSG_CANNOT = 4;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG5) */
    public static final int BRANCH_LIST_MSG_DONE = 5;
    
    /** �x�X�E�c�Ə����b�Z�[�W(���b�Z�[�W�Ȃ�) */
    public static final int BRANCH_UPDATE_MSG_NONE = 0;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG1) */
    public static final int BRANCH_UPDATE_MSG_CONFIRM = 1;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG2) */
    public static final int BRANCH_UPDATE_MSG_NOSELECT = 2;
    /** �x�X�E�c�Ə����b�Z�[�W(MSG3) */
    public static final int BRANCH_UPDATE_MSG_DONE = 3;

    ////////////////////////////////////////////////////////////////
    // ��ЃL���b�`�摜
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(���b�Z�[�W�Ȃ�) */
    public static final int CATCH_LIST_MSG_NONE = 0;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG1) */
    public static final int CATCH_LIST_MSG_NOCHECK = 1;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG2) */
    public static final int CATCH_LIST_MSG_DELETE = 2;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG3) */
    public static final int CATCH_LIST_MSG_DEFAULT = 3;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG4) */
    public static final int CATCH_LIST_MSG_UPDATE = 4;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG5) */
    public static final int CATCH_LSIT_MSG_SAVEDONE = 5;
    /** ��ЃL���b�`�摜�ꗗ���b�Z�[�W(MSG6) */
    public static final int CATCH_LIST_MSG_DELDONE = 6;
        
    /** ��ЃL���b�`�摜�ꗗ�摜�`��(�㉺) */
    public static final int PICTURE_TYPE_VERTICAL = 1;
    /** ��ЃL���b�`�摜�ꗗ�摜�`��(���E) */
    public static final int PICTURE_TYPE_HORIZONTAL = 2;
    /** ��ЃL���b�`�摜�ꗗ(�S��) */
    public static final int PICTURE_TYPE_WHOLE = 3;

    /** ��ЃL���b�`�摜�ǉ��E�X�V���b�Z�[�W(���b�Z�[�W�Ȃ�) */
    public static final int CATCH_UPDATE_MSG_NONE = 0;
    /** ��ЃL���b�`�摜�ǉ��E�X�V���b�Z�[�W(MSG1) */
    public static final int CATCH_UPDATE_MSG_CONFIRM = 1;
    /** ��ЃL���b�`�摜�ǉ��E�X�V���b�Z�[�W(MSG2) */
    public static final int CATCH_UPDATE_MSG_DONE = 2;

    //////////////////////////////////////////////////////////////////////
    // MR�Ǘ�
    /** MR��E�R�[�hDefault�l */
    public static final String MR_YAKUSYOKU_CD_DEFAULT = "0001";
    /** MR�c�Ɠ��敪Default�l */
    public static final String MR_EIGYO_DATE_KBN_DEFAULT = "0";
    /** MR�c�Ǝ��ԋ敪Default�l */
    public static final String MR_EIGYO_TIME_KBN_DEFAULT = "0";
    /** MR�c�ƊJ�n����Default�l */
    public static final String MR_EIGYO_START_TIME_DEFAULT = "to_date('0900', 'hh24mi')";
    /** MR�c�ƏI������Default�l */
    public static final String MR_EIGYO_END_TIME_DEFAULT = "to_date('1800', 'hh24mi')";
    /** �E�F�u�}�X�^�\�L�� */
    public static final String WEBMASTER_LABEL = "�E�F�u";
    /** �T�u�}�X�^�\�L�� */
    public static final String SUBMASTER_LABEL = "�T�u";

    ////////////////////////////////////////////////////////////////
    // �d�v�x�ꗗ��ʏ��
    /** �d�v�x�ꗗ�f�t�H���g�\�[�g�L�[ */
    public static final String SORTKEY_ACTION_LIST = "action.target_name";
    /** �d�v�x�ꗗ�f�t�H���g�I�[�_�[ */
    public static final String ORDER_ACTION_LIST = "ASC";
    

    /** �t�@�C���Z�p���[�^ */
//      public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String SEPARATOR = "/";
        
    /** �f�o�b�O�t���O */
    public static final boolean DEBUG = false;

    /** �e�X�g���t���O */
//    public static final boolean TEST = true;
      public static final boolean TEST = false;

    static {
        if (TEST) {
            SERVLET_ENTRY_POINT = "/medipro/servlet/";
        } else {
            SERVLET_ENTRY_POINT = "/";
        }
    }

    /**
     * SysCnst �R���X�g���N�^�[�E�R�����g�B
     */
    public SysCnst() {
    }
}
