package jp.ne.sonet.medipro.wm.server.manager;

import java.util.Vector;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.server.entity.SentakuTorokuInfo;

/**
 * <strong>�I��o�^�e�[�u��Manager�N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class SentakuTorokuInfoManager {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** �w�肵��MR�Ǘ���DR���X�g�擾 */
    protected static final String DR_LIST_SQL
        = "SELECT"
        + " sentaku_toroku.dr_id"
        + ",sentaku_toroku.mr_id"
        + ",sentaku_toroku.seq"
        + ",sentaku_toroku.name"
        + ",sentaku_toroku.kinmusaki"
        + ",sentaku_toroku.maker_shisetsu_id"
        + ",doctor.name AS dr_name"
        + " FROM sentaku_toroku, doctor"
        + " WHERE"
        + " sentaku_toroku.mr_id = ?"
        + " AND sentaku_toroku.dr_id = doctor.dr_id";

    /** �I��o�^�ǉ� */
    protected static final String SENTAKU_TOROKU_INSERT_SQL
        = "INSERT INTO sentaku_toroku ( "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME " //1218 �X�V���Ԃ�ǉ�����
        + ") "
        + "SELECT "
        + "DR_ID,"
        + "?,"
        + "?,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "SYSDATE "//1218 �X�V���Ԃ�����
        + "FROM "
        + "sentaku_toroku "
        + "WHERE "
        + "dr_id = ? "
        + "AND mr_id = ? "
        + "AND seq = ?";

    /** �I��o�^����ǉ� */
    protected static final String SENTAKU_TOROKU_HIST_INSERT_SQL
        = "INSERT INTO sentaku_toroku_hist ( "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "END_YMD,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME "//1221 �A�b�v�f�[�g�^�C�����ړ�
        + ") "
        + "SELECT "
        + "DR_ID,"
        + "MR_ID,"
        + "SEQ,"
        + "DR_MEMO,"
        + "SENTAKU_KBN,"
        + "START_YMD,"
        + "SYSDATE,"
        + "NAME,"
        + "KINMUSAKI,"
        + "MAKER_CUST_ID,"
        + "MAKER_SHISETSU_ID,"
        + "TARGET_RANK,"
        + "SYOKUSYU,"
        + "SENMON1,"
        + "SENMON2,"
        + "SENMON3,"
        + "YAKUSYOKU,"
        + "SOTSUGYO_DAIGAKU,"
        + "SOTSUGYO_YEAR,"
        + "SYUMI,"
        + "SONOTA, "
        + "UPDATE_TIME "//1221 �A�b�v�f�[�g�^�C�����ړ�
        + "FROM "
        + "sentaku_toroku "
        + "WHERE "
        + "dr_id = ? "
        + "AND mr_id = ? "
        + "AND seq = ?";

    /** �I��o�^�����폜 */
    protected static final String SENTAKU_TOROKU_DELETE_SQL
        = "DELETE FROM sentaku_toroku"
        + " WHERE dr_id = ?"
        + " AND mr_id = ?"
        + " AND seq = ?";

    /** �I��o�^�V�[�P���X */
    protected static final String SENTAKU_TOROKU_SEQ_SQL
        = "SELECT TO_CHAR(sentaku_toroku_seq.nextval,'000') counter FROM dual";

    /** �S��DR���݃`�F�b�N */
    protected static final String IN_CHARGE_COUNT_SQL
	= "SELECT count(*)"
        + " FROM sentaku_toroku"
        + " WHERE"
        + " sentaku_toroku.mr_id = ?";

    /** �S���֌W�J�E���g�@*/
    protected static final String SENTAKU_TOROKU_COUNT_SQL
	= "SELECT"
	+ " count(*) counter"
	+ " FROM sentaku_toroku"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND mr_id = ?";

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** DB�ւ̃R�l�N�V���� */
    protected Connection connection = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * DB�ւ̐ڑ��Ɏg��connection��ݒ肷��B
     * @param initConnection connection�I�u�W�F�N�g
     */
    public SentakuTorokuInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * �w�肵��MR-ID�ŁA�I��o�^�e�[�u������DR�ꗗ���擾����B
     * @param  mrId �i�荞�ݑΏۂ�MR-ID
     * @return DR�ꗗ
     */
    public Vector getDrList(String mrId) {
        Vector list = new Vector();

        try {
            PreparedStatement pstmt = connection.prepareStatement(DR_LIST_SQL);
            
	    try {
		pstmt.setString(1, mrId);

		ResultSet rs = pstmt.executeQuery();
            
		while (rs.next()) {
		    SentakuTorokuInfo info = new SentakuTorokuInfo();
		    info.setDrId(rs.getString("dr_id"));
		    info.setMrId(rs.getString("mr_id"));
		    info.setSeq(rs.getInt("seq"));
		    info.setName(rs.getString("name"));
		    info.setKinmusaki(rs.getString("kinmusaki"));
		    info.setMakerShisetsuId(rs.getString("maker_shisetsu_id"));
		    info.setDrName(rs.getString("dr_name"));

		    list.addElement(info);
		}
	    } finally {
		pstmt.close();
	    }
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return list;
    }

    /**
     * �w�肵��MR���Ǘ�����DR�𑼂�MR�̊Ǘ��Ɉڂ��A���̃��R�[�h��I��o�^�����e�[�u���Ɉڂ��B
     * @param drId     �ړ��Ώۃ��R�[�h��DR-ID
     * @param fromMrId �ړ������R�[�h��MR-ID
     * @param toMrId   �ړ��惌�R�[�h��MR-ID
     * @param seq      �ړ��Ώۃ��R�[�h�̃V�[�P���X
     */
    public void moveSentakuToroku(String drId, String fromMrId, String toMrId, int seq) {
        try {
            //����Commit�I�t
            connection.setAutoCommit(false);

            //�V�����֌W�̃V�[�P���X���擾
            Statement stmt = connection.createStatement();
	    String seqCount = null;

	    try {
		ResultSet rs = stmt.executeQuery(SENTAKU_TOROKU_SEQ_SQL);
		if (rs.next()) {
		    seqCount = rs.getString("counter").trim();
		}
	    } finally {
		stmt.close();
	    }

	    //���Ɋ֌W�������Ă��Ȃ����J�E���g
	    int relationCount = 0;
	    PreparedStatement pstmt
		= connection.prepareStatement(SENTAKU_TOROKU_COUNT_SQL);
	    
	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, toMrId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
		    relationCount = rs.getInt("counter");
		}
	    } finally {
		pstmt.close();
	    }

	    //���ݒS���֌W�ɂȂ��ꍇ�V�����֌W�ɃR�s�[
	    if (relationCount == 0) {
		pstmt = connection.prepareStatement(SENTAKU_TOROKU_INSERT_SQL);

		try {
		    pstmt.setString(1, toMrId);
		    pstmt.setInt(2, new Integer(seqCount).intValue());
		    pstmt.setString(3, drId);
		    pstmt.setString(4, fromMrId);
		    pstmt.setInt(5, seq);

		    int rc = pstmt.executeUpdate();
		} finally {
		    pstmt.close();
		}
	    }

            //���̊֌W�𗚗��ɃR�s�[
            pstmt = connection.prepareStatement(SENTAKU_TOROKU_HIST_INSERT_SQL);

	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, fromMrId);
		pstmt.setInt(3, seq);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

            //���̊֌W���폜
            pstmt = connection.prepareStatement(SENTAKU_TOROKU_DELETE_SQL);

	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, fromMrId);
		pstmt.setInt(3, seq);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
            
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
            }

            throw new WmException(ex);
        } finally {
            try {
		connection.setAutoCommit(true);
            } catch (SQLException e) {
            }
	}
    }

    /**
     * �w�肵��MR-ID�ŁA�I��o�^�e�[�u���ɒS���֌W�ɂ���DR�����擾����.
     * @param  mrId �����Ώۂ�MR-ID
     * @return �S�����Ă���DR��
     */
    public int getDrCountInCharge(String mrId) {
	int chargeCount = 0;

        try {
            PreparedStatement pstmt = connection.prepareStatement(IN_CHARGE_COUNT_SQL);

            try {
		pstmt.setString(1, mrId);
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    chargeCount = rs.getInt(1);
		}
	    } finally {
		pstmt.close();
	    }
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

	return chargeCount;
    }
}
