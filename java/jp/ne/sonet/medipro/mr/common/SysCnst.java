package jp.ne.sonet.medipro.mr.common;

/**
 * <h3>���ʃR���X�^���X.</h3> 
 * <br>
 * �T�[�o�A�N���C�A���g�Ԃŋ��ʂɎg�p����R���X�^���X.
 * 
 * @author
 */
public class SysCnst {

    // ��ʋ敪 
    public static final String MODE_MR = "1";	// ��ʋ敪  �l�q���
    public static final String MODE_DR = "2";	// ��ʋ敪  ��t���
    // ���b�Z�[�W��ԋ敪
    public static final String MSG_STATUS_NOMAL = "1";	// ���b�Z�[�W��ԋ敪  �ꗗ
    public static final String MSG_STATUS_SAVE  = "2";	// ���b�Z�[�W��ԋ敪  �ۊǂa�n�w
    public static final String MSG_STATUS_DUST  = "3";	// ���b�Z�[�W��ԋ敪  ���ݔ�
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[ 
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  ����
    public static final String SORTKEY_DR_NAME          = "dr.name";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  �^�[�Q�b�g�����N
    public static final String SORTKEY_TARGET_RANK      = "sen.target_rank";
    public static final String SORTKEY_ACTION           = "action";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  ���ǎ�M���b�Z�[�W
    public static final String SORTKEY_RECV_COUNT       = "recvCount";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  �V�����J���m�点
    public static final String SORTKEY_NEW_OPEN_COUNT   = "newOpenCount";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  �O��J������̓���
    public static final String SORTKEY_LAST_OPEN_DAY    = "lastOpenDay";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  ���Ǒ��M���b�Z�[�W
    public static final String SORTKEY_SEND_COUNT       = "sendCount";
    // �ڋq�Ǘ��ꗗ�\�[�g�L�[  �ŐV���M�̖��Ǔ���
    public static final String SORTKEY_SEND_NO_READ_DAY = "sendNoReadDay";
    // �l�q����\�[�g�L�[
    // �l�q����\�[�g�L�[  �I���敪
    public static final String SORTKEY_SENTAKU_KBN      = "sentaku_kbn";
    // �l�q����\�[�g�L�[  ����
    public static final String SORTKEY_COMPANY_NAME     = "company_name";
    // �l�q����\�[�g�L�[  ��Ж�
    public static final String SORTKEY_NAME             = "name";

    //��M�ꗗ����M�a�n�w�ꗗ�\�[�g�L�[
    // ��M�\�[�g�L�[  ��M����
    public static final String SORTKEY_RECV_RECEIVE_TIME     = "receive_time";
    // ��M�\�[�g�L�[  ���M��
    public static final String SORTKEY_RECV_FROM_USER        = "from_userid";
    // ��M�\�[�g�L�[  ����
    public static final String SORTKEY_RECV_RECEIVE_TIMED    = "receive_timed";

    //���M�ꗗ�����M�a�n�w�ꗗ�\�[�g�L�[
    // ���M�\�[�g�L�[  ����
    public static final String SORTKEY_SEND_TO_USER          = "to_userid";
    // ���M�\�[�g�L�[  �^�[�Q�b�g�����N
    public static final String SORTKEY_SEND_TARGET_RANK      = "target_rank";
    // ���M�\�[�g�L�[  ���M����
    public static final String SORTKEY_SEND_RECEIVE_TIME     = "receive_time";
    // ���M�\�[�g�L�[  ����
    public static final String SORTKEY_SEND_RECEIVE_TIMED    = "receive_timed";
    // ���M�\�[�g�L�[  ���Ǔ���
    public static final String SORTKEY_SEND_UNREAD_DAY       = "unread_day";

    //�ڋq����\�[�g�L�[
    // �ڋq����\�[�g�L�[  ������E�Ζ���
    public static final String SORTKEY_DR_KINMUSAKI          = "kinmusaki";
    // �ڋq����\�[�g�L�[  ����
    public static final String SORTKEY_DR_DR_ID              = "dr_id";
    // �ڋq����\�[�g�L�[  �^�[�Q�b�g�����N
    public static final String SORTKEY_DR_TARGET_RANK        = "sen.target_rank";
    // �ڋq����\�[�g�L�[  �E��
    public static final String SORTKEY_DR_SYOKUSYU           = "syokusyu";
    // �ڋq����\�[�g�L�[  ���̈�P
    public static final String SORTKEY_DR_SENMON1            = "senmon1";
    // �ڋq����\�[�g�L�[  ���̈�Q
    public static final String SORTKEY_DR_SENMON2            = "senmon2";
    // �ڋq����\�[�g�L�[  ���̈�R
    public static final String SORTKEY_DR_SENMON3            = "senmon3";

    //���v���ڋq�ʃ\�[�g�L�[
    // ���v���ڋq�ʃ\�[�g�L�[  ����
    public static final String SORTKEY_STAT_NAME             = "dr_id";
    // ���v���ڋq�ʃ\�[�g�L�[  �^�[�Q�b�g�����N
    public static final String SORTKEY_STAT_TARGET_RANK      = "target_rank";
    // ���v���ڋq�ʃ\�[�g�L�[  ���M���R�O
    public static final String SORTKEY_STAT_SEND_COUNT30     = "send_count30";
    // ���v���ڋq�ʃ\�[�g�L�[  �N���b�N���R�O
    public static final String SORTKEY_STAT_CKICK_COUNT30    = "click_count30";
    // ���v���ڋq�ʃ\�[�g�L�[  �N���b�N���R�O
    public static final String SORTKEY_STAT_CKICK_RATE30     = "click_rate30";
    // ���v���ڋq�ʃ\�[�g�L�[  ���M���P�W�O
    public static final String SORTKEY_STAT_SEND_COUNT180    = "send_count180";
    // ���v���ڋq�ʃ\�[�g�L�[  �N���b�N���P�W�O
    public static final String SORTKEY_STAT_CKICK_COUNT180   = "click_count180";
    // ���v���ڋq�ʃ\�[�g�L�[  �N���b�N���P�W�O
    public static final String SORTKEY_STAT_CKICK_RATE180    = "click_rate180";

    //�R�~���j�P�[�V���������\�[�g�L�[
    // �R�~���j�P�[�V���������\�[�g�L�[  ���b�Z�[�W�敪
    public static final String SORTKEY_COMMUNI_MESSAGE_KBN   = "message_kbn";
    // �R�~���j�P�[�V���������\�[�g�L�[  ����M����
    public static final String SORTKEY_COMMUNI_RECEIVE_TIME  = "receive_time";
    // �R�~���j�P�[�V���������\�[�g�L�[  �^�C�g��
    public static final String SORTKEY_COMMUNI_TITLE         = "title";
    // �R�~���j�P�[�V���������\�[�g�L�[  ����
    public static final String SORTKEY_COMMUNI_RECEIVE_TIMED = "receive_timed";
    // �R�~���j�P�[�V���������\�[�g�L�[  ���Ǔ���
    public static final String SORTKEY_COMMUNI_UNREAD_DAY    = "recvDay";

    //added by doi
    //���b�Z�[�W�敪�ݒ�l
    public static final String MESSAGE_KBN_TO_MR = "1";
    public static final String MESSAGE_KBN_TO_DR = "2";
    public static final String MESSAGE_KBN_TO_OTHER = "3";

	public static final String MR_SESSION_ID = "MR_SESSION_ID";
	
    /**
     * SysCnst �R���X�g���N�^�[�E�R�����g�B
     */
    public SysCnst() {
    }
}
